package boundary;

import java.util.Scanner;
import java.util.List;

import control.GameController;
import control.view.*;
import control.GameState;
import control.WaiterAction;
import control.GameDisplay;


/**
 * Command-line interface of the game
 * It only prints information and reads input; all game rules stay in
 * GameController. It talks to the controller through GameDisplay and
 * read-only view objects, so it never touches entity classes directly.
 */
public class GameCLI implements GameUI, GameDisplay {
    private final Scanner sc = new Scanner(System.in);
    private final GameController manager;

    /**
     * Creates the CLI.
     *
     * @param manager  Accepts the GameController which runs the game logic.
     */
    public GameCLI(GameController manager) {
        this.manager = manager;
    }

    // --- Game UI ---
    @Override
    public void start() {
        // Hand control to the controller; "this" is passed as the display it calls back
        manager.startGame(this);
    }
    // --- DISPLAY ---


    @Override
    public void displayTurnHeader(int turn, int maxTurns) {
        System.out.println();
        System.out.println("==================== TURN " + turn + " / " + maxTurns + " ====================");
    }

    /**
     * Prints the full restaurant statistics: revenue, average satisfaction of paid customers, the waiting queue, occupied tables,
     * each customer's satisfaction, order status, and the staff tasks available
     *
     * @param view  Accepts a read-only snapshot of the restaurant
     */
    @Override
    public void displayRestaurantState(RestaurantView view) {
        System.out.println("Revenue: " + formatMoney(view.getRevenueInCents())
                + "     Paid customers: " + view.getServedAndPaidCount()
                + "     Average satisfaction (paid) : " + averageSatisfactionText(view.getAverageSatisfaction()));

        List<CustomerView> queue = view.getQueue();
        System.out.println("Waiting queue: " + ((queue.isEmpty()) ? "(Empty)" : ""));
        for (CustomerView c : queue) {
            System.out.println(
                    " #" + c.getId() + " (" + c.getType() + ")" + " satisfaction " + c.getSatisfaction());
        }

        System.out.println("Tables:");
        for (TableView t : view.getTables()) {
            // A free table has no customer/order, so print and skip to the next table
            if (!t.isOccupied()) {
                System.out.println("   Table " + t.getId() + ": free");
                continue;
            }

            CustomerView c = t.getCustomer();
            OrderView o = t.getOrder();
            // Order text depends on whether the customer has ordered, and whether
            // the order is still being prepared in the kitchen (then show units left)
            String orderText = (o == null)? "no order yet"
                    : "Order " + o.getId() + " " + o.getItemName() + " " + formatMoney(o.getPriceInCents())
                    + " [" + o.getStatus() + (o.isAwaitingPreparation()? ", " + o.getRemainingPrepUnits() + " unit(s) left]" : "]");
            System.out.println(
                    "   Table " + t.getId() + ": #" + c.getId() + " (" + c.getType() + ")" + " satisfaction " + c.getSatisfaction() + " " + c.getState() + " " + orderText);
        }
        System.out.println("Staff tasks available:");
        System.out.println("   Waiter (1 task) : seat (" + queue.size() + " queued, " + view.getFreeTableIDs().size() + " free table(s)) | take order ("
                + view.getTablesAwaitingOrder().size() + ") | serve (" + view.getTablesWithReadyOrder().size() + " READY) | wait");
        System.out.println("   Chef (1 prep unit): " + view.getPrepQueue().size() + " order(s) to prepare | wait");
        System.out.println("   Cashier (automatic, 1 payment): " + view.getToPayCount() + " customer(s) ready to pay");
    }

    @Override
    public void displayAction(String msg) {
        System.out.println(" >>>>> " + msg + " <<<<<");
    }

    @Override
    public void displayOrderTaken(int customerID, OrderView order) {
        displayAction("ORDER TAKEN \n"
                + "Customer ID : " + customerID + "\n"
                + "Order ID    : " + order.getId() + "\n"
                + "Item Name   : " + order.getItemName() + "\n"
                + "Preparation Units : " + order.getRemainingPrepUnits() + " unit(s)" + "\n"
                + "Bill        : " + formatMoney(order.getPriceInCents())
        );
    }

    @Override
    public void displayPaymentDone(int customerID, int amountInCents, int satisfaction, int tableID) {
        displayAction("PAYMENT DONE\n"
                + "Amount Paid  : " + formatMoney(amountInCents) + "\n"
                + "Customer ID  : " + customerID + "\n"
                + "Satisfaction Score   : " + satisfaction + "\n"
                + "Table being released : " + tableID
        );
    }

    @Override
    public void displayEndOfTurnSummary(SummaryView s, int turn, int maxTurns) {
        System.out.println("-------------------- END OF TURN " + turn + " --------------------\n"
                + "Customers Served And Paid: " + s.getPaidCount()+ "\n"
                + "Unhappy Departures " + s.getUnhappyDepartures() + "\n"
                + "Revenue: " + formatMoney(s.getRevenueCents()) + "\n"
                + "Average Satisfaction (Paid): " + averageSatisfactionText(s.getAverageSatisfaction()) + "\n"
                + "Turns used: " + turn + "/" + maxTurns);
    }

    @Override
    public void displayFinalResult(GameState result, SummaryView s, int maxTurns) {
        System.out.println();
        System.out.println("==================== GAME OVER ====================");
        System.out.println(" Paid Customers: " + s.getPaidCount()+ " (Target: " + s.getTargetPaidCustomers() + ")\n"
                        + "Revenue: " + formatMoney(s.getRevenueCents()) + " (Target: " + formatMoney(s.getTargetRevenueCents()) + ")\n"
                        + "Average Satisfaction (Paid): " + averageSatisfactionText(s.getAverageSatisfaction()) + " (Target: " + s.getTargetAverageSatisfaction() + ")\n"
                        + "Unhappy departures: " + s.getUnhappyDepartures() + "\n"
                        + "Turns used: " + maxTurns + "/" + maxTurns + "\n"
                        + "RESULT: " + (result == GameState.VICTORY ? "VICTORY" : "DEFEAT"));
    }

    @Override
    public void displayChoiceError(String msg) {
        System.out.println("!!! Invalid choice: " + msg + " Please choose again.");
    }

    public void displayStateError(String msg){
        System.out.println("!!! Invalid state (Error from entity class): " + msg + " Please check entity class");
    }


    // --- PROMPTS ---

    /**
     * Shows the waiter task options to the player and creates a WaiterAction from the player's input
     * Only the format of the input is checked here; the GameController validates
     * whether the chosen IDs are really allowed.
     *
     * @param view  Accepts a read-only snapshot used to list valid options.
     * @return      The WaiterAction chosen by the player.
     */
    @Override
    public WaiterAction promptWaiterTask(RestaurantView view) {
        System.out.println();
        System.out.println("Waiter Task:");
        System.out.println("   " + WAIT_CHOICE + ") Wait");
        System.out.println("   1) Seat a queued customer");
        System.out.println("   2) Take a seated customer's order");
        System.out.println("   3) Serve a READY order");

        // Loop until the player enters one of the listed options
        while (true) {
            int choice = inputHandling("Choice (e.g. 1): ");
            switch (choice) {
                case 1:
                    // If nothing can be seated, return a dummy action (-1,-1) so the
                    // GameController rejects it with a proper error message
                    if (view.getQueue().isEmpty() || view.getFreeTableIDs().isEmpty()) {
                        return WaiterAction.seat(-1,-1);
                    }
                    int queuedCustID = promptQueuedCustomer(view.getQueue());
                    int freeTableID = promptTargetTable("Free tables: ", view.getFreeTableIDs());
                    return WaiterAction.seat(queuedCustID, freeTableID);
                case 2:
                    // Same dummy-action approach when there is no table awaiting an order
                    if (view.getTablesAwaitingOrder().isEmpty()) {
                        return WaiterAction.takeOrder(-1);
                    }
                    int awaitingOrderTableID = promptTargetTable("Tables awaiting an order: ", view.getTablesAwaitingOrder());
                    return WaiterAction.takeOrder(awaitingOrderTableID);
                case 3:
                    // Same dummy-action approach when no order is READY
                    if (view.getTablesWithReadyOrder().isEmpty()) {
                        return WaiterAction.serve(-1);
                    }
                    int serveTableID = promptTargetTable("Tables with a READY order: ", view.getTablesWithReadyOrder());
                    return WaiterAction.serve(serveTableID);
                case WAIT_CHOICE:
                    return WaiterAction.waiting();
                default:
                    displayChoiceError("Please enter a number based on the option given");
            }
        }

    }

    private int promptQueuedCustomer(List<CustomerView> queue) {
        System.out.println("Queued Customers: ");
        for (CustomerView c : queue) {
            System.out.println(
                    "   #" + c.getId() + " (" + c.getType() + ")" + " satisfaction " + c.getSatisfaction());
        }
        return inputHandling("Customer ID: ");
    }

    private int promptTargetTable(String label, List<Integer> tableIDs) {
        StringBuilder sb = new StringBuilder(label);
        for (int id : tableIDs) {
            sb.append(' ').append(id);
        }
        System.out.println(sb);
        return inputHandling("Table ID: ");
    }

    /**
     * Lists the orders waiting in the kitchen and asks which one the chef
     * should work on this turn.
     *
     * @param view   Accepts a read-only snapshot containing the prep
     * @return       The chosen order ID, or WAIT_CHOICE if the chef should wait
     */
    public int promptChefOrderPriority(RestaurantView view) {
        System.out.println();
        System.out.println("Chef - orders to prepare:");
        for (OrderView o : view.getPrepQueue()) {
            System.out.println("   Order #" + o.getId() + "  table " + o.getTableID() + " " + o.getItemName() + " " +
                    o.getRemainingPrepUnits() + " unit(s) left");
        }

        return inputHandling("Order ID (" + WAIT_CHOICE + " = Wait): ");
    }

    // --- HELPER METHODS---
    // Converts cents to a dollar string, e.g. 1250 -> "$12.50" (%02d pads cents to 2 digits)
    private static String formatMoney(int cents) {
        return String.format("$%d.%02d", cents / 100, cents % 100);
    }

    // The average is null when nobody has paid yet, so show "N/A" instead of a number
    private static String averageSatisfactionText(Double average) {
        return (average == null ? "N/A" : String.format("%.1f", average));
    }

    /**
     * Prompts repeatedly until the player enters a whole number (format checking).
     *
     * @param prompt   Accepts the text to show before reading input
     * @return         The integer entered by the player
     * @throws IllegalStateException   if the input stream ends before the game is finished.
     */
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
