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

public class GameController
{
    public static final int MAX_TURNS = 18;
    private static final int[] ARRIVAL_TURNS = {1,4,7,10,13,16};

    private static final Restaurant restaurant = new Restaurant(new Menu());
    private final Waiter waiter = new Waiter("Waiter");
    private final Chef chef = new Chef("Chef");
    private final Cashier cashier = new Cashier("Cashier");
    private final ViewBuilder viewBuilder = new ViewBuilder();

    private GameDisplay ui;
    private int currentTurn = 1;
    private int turnsCompleted = 0;
    private int nextCustomerID = 1;
    private WaiterAction decidedWaiterAction;

    public void startGame(GameDisplay display) {
        this.ui = display;
        for (currentTurn = 1 ; currentTurn <= MAX_TURNS; currentTurn ++) {
            runTurn();
        }
        ui.displayFinalResult(checkEndCondition(), viewBuilder.buildSummaryView(restaurant), MAX_TURNS);
    }

    public void runTurn() {
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
        turnsCompleted++;
        ui.displayEndOfTurnSummary(viewBuilder.buildSummaryView(restaurant), currentTurn, MAX_TURNS);
    }

    private void processArrival(int turn) {
        boolean hasArrival = false;

        for (int t : ARRIVAL_TURNS) {
            if (t == turn) {
                hasArrival = true;
                break;
            }
        }

        if (hasArrival) {
            int id = nextCustomerID++;
            Customer c = new RegularCustomer(id, turn);
            restaurant.enqueueCustomer(c);
            ui.displayAction("Customer #" + c.getCustomerID() + " joins the queue.");
        } else {
            ui.displayAction("No customer joins the queue this turn!");
        }
    }

    private void handlePlayerChoice() {
        while (true) {
            WaiterAction action = ui.promptWaiterTask(viewBuilder.buildRestaurantView(restaurant));
            try {
                validateWaiterAction(action);
                decidedWaiterAction = action;
                return;
            } catch (InvalidChoiceException e) {
                ui.displayChoiceError(e.getMessage());
            }
        }
    }

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
                break;
        }
    }
    private static void validateCustomer(List<Customer> options, int id) throws InvalidChoiceException {
        for (Customer c : options) {
            if (c.getCustomerID() == id) {
                return;
            }
        }
        throw new InvalidChoiceException("Customer #" + id + " is not one of the offered customers.");
    }

    private static void validateTable(List<Table> options, int id) throws InvalidChoiceException {
        for (Table t : options) {
            if (t.getTableID() == id) {
                return;
            }
        }
        throw new InvalidChoiceException("Table " + id + " is not one of the offered tables.");
    }

    private static void validateOrder(int orderID) throws InvalidChoiceException {
        if (orderID == GameDisplay.WAIT_CHOICE) {
            return;
        }
        for (Order o : restaurant.getPrepQueue()) {
            if (o.getOrderID() == orderID) {
                return;
            }
        }
        throw new InvalidChoiceException("Order " + orderID + " is not one of the offered orders.");
    }

    private void executeKitchenAndServing() {
        performWaiterAction(decidedWaiterAction);
        performChefAction();
    }

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
            ui.displayStateError(e.getMessage());
        }
    }

    private void performChefAction() {
        try{
            List<Order> foodQueue = restaurant.getPrepQueue();
            if (foodQueue.isEmpty()) {
                chef.waitTurn();
                ui.displayAction("Chef has nothing to prepare.");
                return;
            }
            int id;
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
            Order targetOrder = null;
            for (Order order : foodQueue) {
                if (order.getOrderID() == id) {
                    targetOrder = order;
                    break;
                }
            }

            chef.prepOrder(targetOrder, currentTurn);
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

    private void processEatingAndPayment() {
        Table tableReadyToPay = restaurant.findTableToCollect();
        if (tableReadyToPay != null) {
            Customer c = tableReadyToPay.getCurrentCustomer();
            try {
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
            for (Customer c : restaurant.processEatingCust(currentTurn)) {
                ui.displayAction("Customer #" + c.getCustomerID() + " finishes eating and is ready to pay.");
            }
        }catch(IllegalStateException e){
            ui.displayStateError(e.getMessage());
        }
    }

    private void updateWaitingAndAbandonment() {
        for (Customer c : restaurant.getUnservedCustomer()) {
            c.applyWaitingPenalty();
            if (c.getSatisfaction() == 0) {
                try {
                    restaurant.recordAbandoned(c);
                }catch(IllegalStateException e){
                    ui.displayStateError(e.getMessage());
                }
                ui.displayAction("Customer #" + c.getCustomerID() + " runs out of patience and leave without paying.");
            }
        }
    }

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
