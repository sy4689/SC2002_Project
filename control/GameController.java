package control;

import java.util.List;

import entity.customer.RegularCustomer;
import entity.customer.Customer;
import entity.menu.Menu;
import entity.order.OrderStatus;
import entity.staff.Cashier;
import entity.staff.Chef;
import entity.staff.Waiter;
import entity.Restaurant;
import entity.Table;
import entity.order.Order;

import entity.customer.VIPCustomer;
import entity.customer.CriticCustomer;

/**
 * Control class that runs the whole game.
 * It owns the restaurant and the staff, drives the turn loop, validates the
 * player's choices, and converts entity data into view objects for the UI.
 * Order in one turn: customer arrival -> show state -> player choice ->
 * waiter and chef act -> eating and payment -> waiting penalty
 */
public class GameController
{
    public static final int MAX_TURNS = 18;
    private static final int[] ARRIVAL_TURNS = {1,4,7,10,13,16};

    private final Restaurant restaurant = new Restaurant(new Menu());
    private final Waiter waiter = new Waiter("Waiter");
    private final Chef chef = new Chef("Chef");
    private final Cashier cashier = new Cashier("Cashier");
    private final ViewBuilder viewBuilder = new ViewBuilder();

    private GameDisplay ui;
    private int currentTurn = 1;
    private int turnsCompleted = 0;
    private int nextCustomerID = 1;
    private WaiterAction decidedWaiterAction;

    /**
     * Runs the full game from turn 1 to MAX_TURNS, then shows the final result.
     *
     * @param display   Accepts the UI used to show output and read player input.
     */
    public void startGame(GameDisplay display) {
        this.ui = display;
        for (currentTurn = 1 ; currentTurn <= MAX_TURNS; currentTurn ++) {
            runTurn();
        }
        ui.displayFinalResult(checkEndCondition(), viewBuilder.buildSummaryView(restaurant), MAX_TURNS);
    }

    /**
     * Runs one turn. The order of the steps below matters: e.g. the waiter acts
     * before payment, so a customer served this turn only starts eating, and
     * waiting penalties are applied last
     */
    public void runTurn() {
        // Every staff member may do only one task per turn, so clear last turn's busy flag status
        waiter.resetTurnStatus();
        chef.resetTurnStatus();
        cashier.resetTurnStatus();

        ui.displayTurnHeader(currentTurn, MAX_TURNS);
        processArrival(currentTurn);
        ui.displayRestaurantState(viewBuilder.buildRestaurantView(restaurant));
        handlePlayerChoice();
        executeKitchenAndServing();
        processEatingAndPayment();
        updateWaitingAndAbandonment();
        // Update the completed turn count only after all turn activities have finished
        turnsCompleted++;
        ui.displayEndOfTurnSummary(viewBuilder.buildSummaryView(restaurant), currentTurn, MAX_TURNS);
    }

    /**
     * Adds a new customer to the queue if this turn is a scheduled arrival turn.
     * For stage 2, the arrival of VIP Customer and Critic Customer
     * is fixed by ID: #2 is VIP, #4 is Critic, the rest are Regular.
     *
     * @param turn   Accepts the current turn number
     */
    private void processArrival(int turn) {
        boolean hasArrival = false;

        // Check whether the current turn is one of the scheduled arrival turns
        for (int t : ARRIVAL_TURNS) {
            if (t == turn) {
                hasArrival = true;
                break;
            }
        }

        if (hasArrival) {
            // Assign the next available customer ID and increment it for the next arrival
            int id = nextCustomerID++;
            Customer c;
            // Assign special customer types based on their fixed customer IDs
            switch (id) {
                case 2 -> c = new VIPCustomer(id, turn);
                case 4 -> c = new CriticCustomer(id, turn);
                default -> c = new RegularCustomer(id, turn);
            }

            restaurant.enqueueCustomer(c);
            ui.displayAction(
                    c.getClass().getSimpleName() + " #" + c.getCustomerID() + " joins the queue."
            );
        } else {
            ui.displayAction("No customer joins the queue this turn!");
        }
    }

    /**
     * Keeps asking the player for a waiter action until a valid one is given.
     * The valid action is stored and only executed later in executeKitchenAndServing().
     */
    private void handlePlayerChoice() {
        // Keep prompting until the player provides a valid waiter action
        while (true) {
            WaiterAction action = ui.promptWaiterTask(viewBuilder.buildRestaurantView(restaurant));
            try {
                validateWaiterAction(action);
                // Store the validated action for execution later in the turn
                decidedWaiterAction = action;
                return;
            } catch (InvalidChoiceException e) {
                // Display the validation error and allow the player to try again
                ui.displayChoiceError(e.getMessage());
            }
        }
    }

    /**
     * Checks that the waiter action is allowed in the current restaurant state
     * For each action type it first checks that the action is possible at all
     * (e.g. someone is queued, a table is free), then that the chosen ID is
     * one of the offered options.
     *
     * @param action   Accepts the action chosen by the player
     * @throws InvalidChoiceException   if the action cannot be performed
     */
    private void validateWaiterAction(WaiterAction action) throws InvalidChoiceException {
        switch (action.getActionType()) {
            case SEAT_CUSTOMER:
                if (restaurant.getWaitingQueue().isEmpty()) {
                    throw new InvalidChoiceException("No customer is waiting in the queue.");
                }
                if (restaurant.getAvailableTables().isEmpty()) {
                    throw new InvalidChoiceException(("Every table is occupied."));
                }
                validateCustomer(restaurant.getWaitingQueue(), action.getTargetCustomerID());
                validateTable(restaurant.getAvailableTables(), action.getTargetTableID());
                break;
            case TAKE_ORDER:
                if (restaurant.getTablesAwaitingOrder().isEmpty()) {
                    throw new InvalidChoiceException("No seated customer is waiting to order.");
                }
                validateTable(restaurant.getTablesAwaitingOrder(), action.getTargetTableID());
                break;
            case SERVE_ORDER:
                if (restaurant.getTablesWithReadyOrder().isEmpty()) {
                    throw new InvalidChoiceException("No order is READY to serve.");
                }
                validateTable(restaurant.getTablesWithReadyOrder(), action.getTargetTableID());
                break;
            default:
                // WAIT is always allowed
                break;
        }
    }

    /**
     * Check if the chosen customer ID is one of the given options.
     *
     * @param options   list of customers offered to the player, e.g. Waiting Queue
     * @param id        the customer ID chosen by the player
     * @throws InvalidChoiceException   if the chosen ID is not in the offered options
     */
    private static void validateCustomer(List<Customer> options, int id) throws InvalidChoiceException {
        // Loop through the offered customers to check if the chosen ID matches any of them
        for (Customer c : options) {
            if (c.getCustomerID() == id) {
                return;
            }
        }
        // Throw an exception if the chosen customer ID is not among the offered options
        throw new InvalidChoiceException("Customer #" + id + " is not one of the offered customers.");
    }

    /**
     * Check if the chosen Table ID is one of the given options
     *
     * @param options        list of customers offered to the player, e.g. list of tables with READY order
     * @param id             the table ID chosen by the player
     * @throws InvalidChoiceException   if the chosen ID is not in the offered options
     */
    private static void validateTable(List<Table> options, int id) throws InvalidChoiceException {
        // Loop through the offered tables to check if the chosen ID matches any of them
        for (Table t : options) {
            if (t.getTableID() == id) {
                return;
            }
        }
        // Throw an exception if the chosen table ID is not among the offered options
        throw new InvalidChoiceException("Table " + id + " is not one of the offered tables.");
    }

    /**
     * Check if the chosen order ID is one of the orders in the preparation queue
     *
     * @param orderID   the order ID chosen by the player
     * @throws InvalidChoiceException   if the chosen order ID is not in the preparation queue
     */
    private void validateOrder(int orderID) throws InvalidChoiceException {
        // The chef may also choose WAIT_CHOICE (0), which is not an order ID but is always valid
        if (orderID == GameDisplay.WAIT_CHOICE) {
            return;
        }
        // Loop through the preparation queue to check if the chosen order ID matches any existing order
        for (Order o : restaurant.getPrepQueue()) {
            if (o.getOrderID() == orderID) {
                return;
            }
        }
        // Throw an exception if the chosen order ID is not among the offered orders
        throw new InvalidChoiceException("Order " + orderID + " is not one of the offered orders.");
    }

    /**
     * Execute the waiter action and chef action in the correct sequence
     */
    private void executeKitchenAndServing() {
        // The waiter acts before the chef
        performWaiterAction(decidedWaiterAction);
        performChefAction();
    }

    /**
     * Carries out the validated waiter action by calling the Waiter entity,
     * then reports the result to the UI.
     * IllegalStateException from the entities is caught and shown as a state
     * error instead of crashing the game.
     *
     * @param action   Accepts the action already validated by validateWaiterAction
     */
    private void performWaiterAction(WaiterAction action)
    {
        try
        {
            switch (action.getActionType())
            {
                case SEAT_CUSTOMER:
                {
                    Customer c = restaurant.findQueuedCustomer(action.getTargetCustomerID());
                    Table t = restaurant.findTable(action.getTargetTableID());
                    if (waiter.seatCustomer(c, t))
                    {
                        // Remove the customer from the waiting queue after successfully seating them
                        restaurant.dequeueCustomer(c);
                        ui.displayAction("Waiter seats customer #" + c.getCustomerID() + " at table " + t.getTableID() + ".");
                    }
                    break;
                }
                case TAKE_ORDER:
                {
                    Table t = restaurant.findTable(action.getTargetTableID());
                    Customer c = t.getCurrentCustomer();
                    Order o = waiter.takeOrder(t, restaurant.getMenu(), restaurant);
                    ui.displayOrderTaken(c.getCustomerID(), viewBuilder.orderView(o));
                    break;
                }
                case SERVE_ORDER:
                {
                    Table t = restaurant.findTable(action.getTargetTableID());
                    Customer c = t.getCurrentCustomer();
                    waiter.serveOrder(t, currentTurn);
                    ui.displayAction("Waiter serves customer #" + c.getCustomerID() + " at table " + t.getTableID() + ".");
                    break;
                }
                default:
                    waiter.waitTurn();
                    ui.displayAction("Waiter waits.");
            }
        }catch(IllegalStateException e){
            // Display the state error without terminating the game
            ui.displayStateError(e.getMessage());
        }
    }

    /**
     * Lets the player pick which queued order the chef works on, then spends
     * one preparation unit on it. When the order reaches 0 units it becomes
     * READY and leaves the prep queue (it can be served from the next turn).
     */
    private void performChefAction() {
        try{
            List<Order> foodQueue = restaurant.getPrepQueue();
            // Nothing to cook: chef automatically waits and no prompt is shown
            if (foodQueue.isEmpty()) {
                chef.waitTurn();
                ui.displayAction("Chef has nothing to prepare.");
                return;
            }
            int id;
            // Keep asking until the player enters a valid order ID to be prepared (or WAIT_CHOICE)
            while (true) {
                id = ui.promptChefOrderPriority(viewBuilder.buildRestaurantView(restaurant));
                try {
                    validateOrder(id);
                    break;
                } catch (InvalidChoiceException e) {
                    ui.displayChoiceError(e.getMessage());
                }
            }
            if (id == GameDisplay.WAIT_CHOICE) {
                chef.waitTurn();
                ui.displayAction("Chef waits.");
                return;
            }

            // Find the order object that matches the chosen ID
            Order targetOrder = null;
            for (Order order : foodQueue) {
                if (order.getOrderID() == id) {
                    targetOrder = order;
                    break;
                }
            }

            // Chef performs the action of preparing order
            chef.prepOrder(targetOrder, currentTurn);
            // Still PENDING/IN_KITCHEN means units remain;
            // otherwise it is READY, so remove it from the queue
            if (targetOrder.getStatus() == OrderStatus.PENDING || targetOrder.getStatus() == OrderStatus.IN_KITCHEN) {
                ui.displayAction("Chef works on Order " + targetOrder.getOrderID() + " : " + targetOrder.getRemainingPrepUnits() + " unit(s) left.");
            } else {
                restaurant.removeFromPrepQueue(targetOrder);
                ui.displayAction("Chef finishes Order " + targetOrder.getOrderID() + " : READY (can be served next turn).");
            }

        }catch(IllegalStateException e){
            ui.displayStateError(e.getMessage());
        }
    }

    /**
     * Handles the cashier and the eating customers for this turn.
     * Payment is processed BEFORE eating, so a customer who finishes eating
     * this turn is only paid on the next turn. The cashier serves at most one
     * customer per turn (the one with the smallest ID).
     */
    private void processEatingAndPayment() {
        // Find the table whose customer is ready to make payment
        Table tableReadyToPay = restaurant.findTableToCollect();
        if (tableReadyToPay != null) {
            Customer c = tableReadyToPay.getCurrentCustomer();
            try {
                // Process payment and update the restaurant's revenue
                int amount = cashier.processPayment(tableReadyToPay, restaurant);
                ui.displayAction("PAYMENT DONE ==========\n"
                        + "Amount Paid: " + formatMoney(amount) + "\n"
                        + "Customer ID: " + c.getCustomerID() + "\n"
                        + "Satisfaction Score: " + c.getSatisfaction() + "\n"
                        + "Table being released: " + tableReadyToPay.getTableID());
            }catch(IllegalStateException e){
                ui.displayStateError(e.getMessage());
            }
        }

        try {
            // Process eating customers after payment so newly finished customers
            // will only be eligible for payment in the next turn
            for (Customer c : restaurant.processEatingCust(currentTurn)) {
                ui.displayAction("Customer #" + c.getCustomerID() + " finishes eating and is ready to pay.");
            }
        }catch(IllegalStateException e){
            ui.displayStateError(e.getMessage());
        }
    }

    /**
     * Reduces satisfaction of every customer who has not been served yet
     * (in queue, seated or ordered). A customer who reaches 0 satisfaction
     * leaves without paying and is recorded as an unhappy departure.
     */
    private void updateWaitingAndAbandonment() {
        for (Customer c : restaurant.getUnservedCustomer()) {
            c.applyWaitingPenalty();
            if (c.getSatisfaction() == 0) {
                try {
                    // Record the customer's departure so it is included in the unhappy departure statistics
                    restaurant.recordAbandoned(c);
                }catch(IllegalStateException e){
                    ui.displayStateError(e.getMessage());
                }
                ui.displayAction("Customer #" + c.getCustomerID() + " runs out of patience and leave without paying.");
            }
        }
    }

    /**
     * Check the game result.
     *
     * @return   IN_PROGRESS until all turns are completed; afterwards VICTORY if
     *           the restaurant objective is met, otherwise DEFEAT.
     */
    public GameState checkEndCondition() {
        if (turnsCompleted < MAX_TURNS) {
            return GameState.IN_PROGRESS;
        }
        return restaurant.isObjectiveMet() ? GameState.VICTORY : GameState.DEFEAT;
    }

    private static String formatMoney(int cents) {
        return String.format("$%d.%02d", cents / 100, cents % 100);
    }

}
