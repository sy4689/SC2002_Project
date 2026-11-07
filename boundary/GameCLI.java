package boundary;

import java.util.Scanner;
import java.util.List;

import control.InvalidChoiceException;
import control.Manager;
import entity.Restaurant;
import entity.customer.Customer;
import entity.customer.CustomerState;
import entity.order.Order;
import entity.order.OrderStatus;
import entity.Table;
import control.GameUI;
import control.GameState;
import control.WaiterAction;

public class GameCLI implements GameUI {
    private final Scanner sc = new Scanner(System.in);

    // --- DISPLAY ---

    @Override
    public void displayTurnHeader(int turn) {
        System.out.println();
        System.out.println("==================== TURN " + turn + " / " + Manager.MAX_TURNS + " ====================");
    }

    @Override
    public void displayRestaurantState(Restaurant restaurant) {
        System.out.println("Revenue: " + formatMoney(restaurant.getRevenueInCents())
                + "     Paid customers: " + restaurant.getServeAndPaidCount()
                + "     Average satisfaction (paid) : " + averageSatisfactionText(restaurant));

        List<Customer> queue = restaurant.getWaitingQueue();
        System.out.println("Waiting queue: " + ((queue.isEmpty()) ? "(Empty)" : ""));
        for (Customer c : queue) {
            System.out.println(" #" + c.getCustomerID() + " " + " satisfaction" + c.getSatisfaction());
        }

        System.out.println("Tables:");
        int awaitingOrder = 0;
        int readyToServe = 0;
        int toPay = 0;
        for (Table t : restaurant.getTables()) {
            if (!t.isOccupied()) {
                System.out.println("   Table " + t.getTableID() + ": free");
                continue;
            }

            Customer c = t.getCurrentCustomer();
            Order o = c.getCurrentOrder();
            String orderText = (o == null)? "no order yet"
                    : "Order " + o.getOrderID() + " " + o.getItem().getFoodName() + " " + formatMoney(o.getFinalPriceInCents())
                    + " [" + o.getStatus() + (((o.getStatus() == OrderStatus.PENDING) || (o.getStatus() == OrderStatus.IN_KITCHEN))? ", " + o.getRemainingPrepUnits() + " unit(s) left]" : "]");
            System.out.println("   Table " + t.getTableID() + ": #" + c.getCustomerID() + " " + " satisfaction " + c.getSatisfaction() + " " + c.getState() + " " + orderText);
            if (c.getState() == CustomerState.SEATED) {
                awaitingOrder++;
            }
            if (o != null && o.getStatus() == OrderStatus.READY) {
                readyToServe++;
            }

            if (t.isOccupied() && t.getCurrentCustomer().getState() == CustomerState.WAITING_TO_PAY) {
                toPay++;
            }
        }
        System.out.println("Staff tasks available:");
        System.out.println("   Waiter (1 task) : seat (" + queue.size() + " queued, " + restaurant.getAvailableTables().size() + " free table(s)) | take order ("
                + awaitingOrder + ") | serve (" + readyToServe + " READY) | wait");
        System.out.println("   Chef (1 prep unit): " + restaurant.getPrepQueue().size() + " order(s) to prepare | wait");
        System.out.println("   Cashier (automatic, 1 payment): " + toPay + " customer(s) ready to pay");
    }

    @Override
    public void displayAction(String msg) {
        System.out.println(" >>>>> " + msg + " <<<<<");
    }

    @Override
    public void displayEndOfTurnSummary(Restaurant restaurant, int turn) {
        System.out.println("-------------------- END OF TURN " + turn + " --------------------\n"
                + "Customers Served And Paid: " + restaurant.getServeAndPaidCount() + "\n"
                + "Unhappy Departures " + restaurant.getUnhappyDeparture() + "\n"
                + "Revenue: " + formatMoney(restaurant.getRevenueInCents()) + "\n"
                + "Average Satisfaction (Paid): " + averageSatisfactionText(restaurant) + "\n"
                + "Turns used: " + turn + "/" + Manager.MAX_TURNS);
    }

    @Override
    public void displayFinalResult(GameState result, Restaurant restaurant) {
        System.out.println();
        System.out.println("==================== GAME OVER ====================");
        System.out.println(" Paid Customers: " + restaurant.getServeAndPaidCount() + " (Target: " + Restaurant.TARGET_PAID_CUSTOMERS + ")\n"
                        + "Revenue: " + formatMoney(restaurant.getRevenueInCents()) + " (Target: " + formatMoney(Restaurant.TARGET_REVENUE_IN_CENTS) + ")\n"
                        + "Average Satisfaction (Paid): " + averageSatisfactionText(restaurant) + " (Target: " + Restaurant.TARGET_AVERAGE_SATISFACTION + ")\n"
                        + "Unhappy departures: " + restaurant.getUnhappyDeparture() + "\n"
                        + "Turns used: " + Manager.MAX_TURNS + "/" + Manager.MAX_TURNS + "\n"
                        + "RESULT: " + (result == GameState.VICTORY ? "VICTORY" : "DEFEAT"));
    }

    @Override
    public void displayError(String msg) {
        System.out.println("!!! Invalid choice: " + msg + " Please choose again.");
    }

    // --- PROMPTS ---
    @Override
    public WaiterAction promptWaiterTask(Restaurant restaurant) throws InvalidChoiceException {
        System.out.println();
        System.out.println("Waiter Task:");
        System.out.println("   " + WAIT_CHOICE + ") Wait");
        System.out.println("   1) Seat a queued customer");
        System.out.println("   2) Take a seated customer's order");
        System.out.println("   3) Serve a READY order");
        int choice = inputHandling("Choice (e.g. 1): ");
        switch (choice) {
            case 1:
                if (restaurant.getWaitingQueue().isEmpty()) {
                    throw new InvalidChoiceException("No customer is waiting in the queue.");
                }
                if (restaurant.getAvailableTables().isEmpty()) {
                    throw new InvalidChoiceException("Every table is occupied.");
                }
                int queuedCustID = promptQueuedCustomer(restaurant.getWaitingQueue());
                validateCustomer(restaurant.getWaitingQueue(), queuedCustID);
                int freeTableID = promptTargetTable("Free tables: ", restaurant.getAvailableTables());
                validateTable(restaurant.getAvailableTables(), freeTableID);
                return WaiterAction.seat(queuedCustID, freeTableID);
            case 2:
                if (restaurant.getTablesAwaitingOrder().isEmpty()) {
                    throw new InvalidChoiceException("No seated customer is waiting to order.");
                }
                int awaitingOrderTableID = promptTargetTable("Tables awaiting an order: ", restaurant.getTablesAwaitingOrder());
                validateTable(restaurant.getTablesAwaitingOrder(), awaitingOrderTableID);
                return WaiterAction.takeOrder(awaitingOrderTableID);
            case 3:
                if (restaurant.getTablesWithReadyOrder().isEmpty()) {
                    throw new InvalidChoiceException("No order is READY to serve.");
                }
                int serveTableID = promptTargetTable("Tables with a READY order: ", restaurant.getTablesWithReadyOrder());
                validateTable(restaurant.getTablesWithReadyOrder(), serveTableID);
                return WaiterAction.serve(serveTableID);
            case WAIT_CHOICE:
                return WaiterAction.waiting();
            default:
                throw new InvalidChoiceException("Please enter a number based on the option given.");
        }
    }

    @Override
    public int promptQueuedCustomer(List<Customer> queue) {
        System.out.println("Queued Customers: ");
        for (Customer c : queue) {
            System.out.println("   #" + c.getCustomerID() + "    satisfaction " + c.getSatisfaction());
        }
        return inputHandling("Customer ID: ");
    }

    public int promptTargetTable(String label, List<Table> tables) {
        StringBuilder sb = new StringBuilder(label);
        for (Table t : tables) {
            sb.append(' ').append(t.getTableID());
        }
        System.out.println(sb);
        return inputHandling("Table ID: ");
    }

    public int promptChefOrderPriority(List<Order> orders) throws InvalidChoiceException {
        System.out.println();
        System.out.println("Chef - orders to prepare:");
        for (Order o : orders) {
            System.out.println("   Order #" + o.getOrderID() + "  table " + o.getTableID() + " " + o.getItem().getFoodName() + " " +
                    o.getRemainingPrepUnits() + " unit(s) left");
        }

        int choice = inputHandling("Order ID (" + WAIT_CHOICE + " = Wait): ");
        if (choice == WAIT_CHOICE) {
            return choice;
        }
        validateOrder(orders, choice);
        return choice;
    }

    // --- HELPER METHODS---
    private static String formatMoney(int cents) {
        return String.format("$%d.%02d", cents / 100, cents % 100);
    }

    private static String averageSatisfactionText(Restaurant restaurant) {
        return (restaurant.getPaidCustomers().isEmpty()) ? "N/A" : String.format("%.1f", restaurant.getAveragePaidSatisfaction());
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

    private static void validateOrder(List<Order> options, int id) throws InvalidChoiceException {
        for (Order o : options) {
            if (o.getOrderID() == id) {
                return;
            }
        }
        throw new InvalidChoiceException("Order " + id + " is not one of the offered orders.");
    }

    private int inputHandling(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!sc.hasNextLine()) {
                throw new IllegalStateException("Input ended before the game finished.");
            }
            String numString = sc.nextLine().trim();
            try {
                return Integer.parseInt(numString);
            } catch(NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }



}
