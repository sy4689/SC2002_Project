package control;

import java.util.List;

import entity.customer.RegularCustomer;
import entity.customer.Customer;
import entity.order.OrderStatus;
import entity.staff.Cashier;
import entity.staff.Chef;
import entity.staff.Waiter;
import entity.Restaurant;
import entity.Table;
import entity.order.Order;
import control.WaiterActionType;
import control.GameUI;

public class Manager {
    public static final int MAX_TURNS = 18;
    private static final int[] ARRIVAL_TURNS = {1,4,7,10,13,16};

    private final GameUI ui;
    private final Restaurant restaurant;
    private final Waiter waiter = new Waiter("Waiter");
    private final Chef chef = new Chef("Chef");
    private final Cashier cashier = new Cashier("Cashier");

    private int currentTurn = 1;
    private int turnsCompleted = 0;
    private int nextCustomerID = 1;
    private WaiterAction decidedWaiterAction;

    public Manager(GameUI ui, Restaurant restaurant) {
        this.ui = ui;
        this.restaurant = restaurant;
    }

    public void startGame() {
        for (currentTurn = 1 ; currentTurn <= MAX_TURNS; currentTurn ++) {
            runTurn();
        }
        ui.displayFinalResult(checkEndCondition(), restaurant);
    }

    public void runTurn() {
        waiter.resetTurnStatus();
        chef.resetTurnStatus();
        cashier.resetTurnStatus();

        ui.displayTurnHeader(currentTurn);
        processArrival(currentTurn);
        ui.displayRestaurantState(restaurant);
        handlePlayerChoice();
        executeKitchenAndServing();
        processEatingAndPayment();
        updateWaitingAndAbandonment();
        turnsCompleted++;
        ui.displayEndOfTurnSummary(restaurant, currentTurn);
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
            try {
                decidedWaiterAction = ui.promptWaiterTask(restaurant);
                return;
            } catch (InvalidChoiceException e) {
                ui.displayError(e.getMessage());
            }
        }
    }

    private void executeKitchenAndServing() {
        performWaiterAction(decidedWaiterAction);
        performChefAction();
    }

    private void performWaiterAction(WaiterAction action) {
        switch (action.getActionType()) {
            case SEAT_CUSTOMER: {
                Customer c = restaurant.findQueuedCustomer(action.getTargetCustomerID());
                Table t = restaurant.findTable(action.getTargetTableID());
                if (waiter.seatCustomer(c, t)) {
                    restaurant.dequeueCustomer(c);
                    ui.displayAction("Waiter seats customer #" + c.getCustomerID() + " at table " + t.getTableID() + ".");
                }
                break;
            }
            case TAKE_ORDER: {
                Table t = restaurant.findTable(action.getTargetTableID());
                Customer c = t.getCurrentCustomer();
                Order o = waiter.takeOrder(t, restaurant.getMenu(), restaurant);
                ui.displayAction(" ORDER TAKEN \n"
                        + "Customer ID : " + c.getCustomerID() + "\n"
                        + "Order ID :    " + o.getOrderID() + "\n"
                        + "Item Name :   " + o.getItem().getFoodName() + "\n"
                        + "Preparation Units: " + o.getItem().getPrepUnits() + " unit(s)" + "\n"
                        + "Bill:         " + formatMoney(o.getFinalPriceInCents()) + ".");
                break;
            }
            case SERVE_ORDER: {
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
    }

    private void performChefAction() {
        List<Order> foodQueue = restaurant.getPrepQueue();
        if (foodQueue.isEmpty()) {
            chef.waitTurn();
            ui.displayAction("Chef has nothing to prepare.");
            return;
        }
        int id;
        while (true) {
            try {
                id = ui.promptChefOrderPriority(foodQueue);
                break;
            } catch (InvalidChoiceException e) {
                ui.displayError(e.getMessage());
            }
        }
        if (id == GameUI.WAIT_CHOICE) {
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
    }

    private void processEatingAndPayment() {
        Table tableReadyToPay = restaurant.findTableToCollect();
        if (tableReadyToPay != null) {
            Customer c = tableReadyToPay.getCurrentCustomer();
            int amount = cashier.processPayment(tableReadyToPay, restaurant);
            ui.displayAction("PAYMENT DONE\n"
                            + "Amount Paid: " + formatMoney(amount) + "\n"
                            + "Customer ID: " + c.getCustomerID() + "\n"
                            + "Satisfaction Score: " + c.getSatisfaction() + "\n"
                            + "Table being released: " + tableReadyToPay.getTableID());
        }

        for (Customer c : restaurant.processEatingCust(currentTurn)) {
            ui.displayAction("Customer #" + c.getCustomerID() + " finishes eating and is ready to pay.");
        }
    }

    private void updateWaitingAndAbandonment() {
        for (Customer c : restaurant.getUnservedCustomer()) {
            c.applyWaitingPenalty();
            if (c.getSatisfaction() == 0) {
                restaurant.recordAbandoned(c);
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
