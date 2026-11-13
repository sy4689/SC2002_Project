package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import entity.customer.Customer;
import entity.customer.CustomerState;
import entity.order.Order;
import entity.order.OrderStatus;
import entity.menu.Menu;


public class Restaurant{
    public static final int TABLE_COUNT = 2;
    public static final int TARGET_PAID_CUSTOMERS = 4;
    public static final int TARGET_REVENUE_IN_CENTS = 4500;
    public static final int TARGET_AVERAGE_SATISFACTION = 60;

    private final Menu menu;
    private final WaitingQueue waitingQueue = new WaitingQueue();
    private final List<Table> tables = new ArrayList<>();
    private final List<Order> prepQueue = new ArrayList<>();
    private final List<Customer> paidCustomers = new ArrayList<>();
    private int revenueIntCents = 0;
    private int serveAndPaidCount = 0;
    private int unhappyDeparture = 0;
    private int nextOrderID = 1;

    /**
     * Creates a restaurant with the menu and two initially empty tables.
     * Initialises empty waiting, preparation and paid-customer.
     */
    public Restaurant(Menu menu) {
        this.menu = menu;
        for (int i = 1; i <= TABLE_COUNT; i++) {
            tables.add(new Table(i));
        }
    }

    // --- Menu ---

    /**
     * getter method to return menu
     */
    public Menu getMenu() {
        return menu;
    }

    /**
     * getter method to return nextOrderID
     */
    public int getNextOrderID() {
        return nextOrderID++;
    }

    // --- Queue ---

    /**
     * Adds a customer to the end of the waiting list.
    */
    public void enqueueCustomer(Customer cust) {
        waitingQueue.enqueueCustomer(cust);
    }

    /**
     * Removes the specified customer from the waiting list if present.
     * Does not change the customer's state or assign a table.
     */
    public void dequeueCustomer(Customer cust) {
        waitingQueue.dequeueCustomer(cust);
    }

    /**
     * Finds a waiting customer by identifier.
     */
    public Customer findQueuedCustomer(int customerID) {
        return waitingQueue.findByID(customerID);
    }

    /**
     * getter method to return the list of customers currently waiting to be seated.
     */
    public List<Customer> getWaitingQueue() {
        return waitingQueue.getCustomers();
    }

    // --- Tables ---

    /**
     * getter method to return the list of tables in restaurant.
     */
    public List<Table> getTables() {
        return Collections.unmodifiableList(tables);
    }

    /**
     * Finds a restaurant table by identifier.
     */
    public Table findTable(int tableID) {
        for (Table t : tables) {
            if (t.getTableID() == tableID) {
                return t;
            }
        }
        throw new IllegalStateException("Table " + tableID + " is not one of the offered tables.");
    }

    /**
     * getter method to collect the tables that are currently unoccupied.
    */
    public List<Table> getAvailableTables() {
        List<Table> result = new ArrayList<>();
        for (Table t : tables) {
            if (!t.isOccupied()) {
                result.add(t);
            }
        }
        return result;
    }

    /**
     * getter method to collect occupied tables whose customers are SEATED and awaiting an order.
     */
    public List<Table> getTablesAwaitingOrder() {
        List<Table> result = new ArrayList<>();
        for (Table t : tables) {
            if (t.isOccupied() && t.getCurrentCustomer().getState() == CustomerState.SEATED) {
                result.add(t);
            }
        }
        return result;
    }

    /**
     * getter method to collect occupied tables whose current orders have READY status.
     * This does not check the preparation-completion turn; Waiter check that timing separately before serving.
    */
    public List<Table> getTablesWithReadyOrder() {
        List<Table> result = new ArrayList<>();
        for (Table t : tables) {
            if (t.isOccupied()) {
                Order o = t.getCurrentCustomer().getCurrentOrder();
                if (o != null && o.getStatus() == OrderStatus.READY) {
                    result.add(t);
                }
            }
        }
        return result;
    }

    /**
     * Selects the table with the lowest customer ID among customers in WAITING_TO_PAY state. Customer IDs increase with arrival order in normal play.
     */
    public Table findTableToCollect() {
        Table oldest = null;
        for (Table t : tables) {
            if (t.isOccupied() && t.getCurrentCustomer().getState() == CustomerState.WAITING_TO_PAY) {
                if (oldest == null || t.getCurrentCustomer().getCustomerID() < oldest.getCurrentCustomer().getCustomerID()) {
                    oldest = t;
                }
            }
        }
        return oldest;
    }

    // --- kitchen ---

    /**
     * Adds an order to the restaurant's preparation queue.
     */
    public void addToPrepQueue(Order order) {
        prepQueue.add(order);
    }

    /**
     * Removes the specified order from the preparation queue.
     */
    public void removeFromPrepQueue(Order order) {
        if (prepQueue.isEmpty()){
            throw new IllegalStateException("Preparing queue in kitchen is empty. Can't remove any READY order");
        }

        if(!prepQueue.contains(order)){
            throw new IllegalStateException("Supposed READY order that need be removed can't be found from the current preparing queue in the kitchen");
        }

        prepQueue.remove(order);
    }

    /**
     * getter method to return list of the orders in the preparation queue.
     */
    public List<Order> getPrepQueue() {
        return Collections.unmodifiableList(prepQueue);
    }

    // --- Processing for each turn ---
    /**
     * Advances eating for customers currently in EATING state.
     * Customers who finish to move to WAITING_TO_PAY, and their orders to move to READY_TO_PAY.
     * Eating cannot finish during the turn in which food was served.
     */
    public List<Customer> processEatingCust(int currentTurn) {
        List<Customer> finished = new ArrayList<>();
        for (Table t : tables) {
            Customer c = t.getCurrentCustomer();
            if (c != null && c.getState() == CustomerState.EATING){
                c.decrementEatingTurns(currentTurn);
                if (c.isFinishedEating()) {
                    c.setState(CustomerState.WAITING_TO_PAY);
                    c.getCurrentOrder().markReadyToPay();
                    finished.add(c);
                }
            }
        }
        return finished;
    }

    /**
     * getter method to collect waiting-queue entries and table customers who are still queued, seated or ordered but unserved
     */
    public List<Customer> getUnservedCustomer() {
        List<Customer> result = new ArrayList<>(waitingQueue.getCustomers());
        for (Table t : tables) {
            Customer c = t.getCurrentCustomer();
            if ((c != null) && (c.getState() == CustomerState.WAITING_IN_QUEUE
                          || c.getState() == CustomerState.SEATED
                          || c.getState() == CustomerState.ORDERED)) {
                result.add(c);
            }
        }
        return result;
    }

    /**
     * Adds a completed payment to revenue, increase the paid-customer count and retains the customer for satisfaction statistics.
     * The caller is responsible for recording a valid payment only once.
     */
    public void recordPayment(Customer cust, int amount) {
        revenueIntCents += amount;
        serveAndPaidCount++;
        paidCustomers.add(cust);
    }

    /**
     * Records the departure of an unserved customer.
     * Then, marks the customer ABANDONED, removes queue or table occupancy, cancels any associated order, removes its preparation work and increase unhappy departures.
     */
    public void recordAbandoned(Customer cust) {
        cust.setState(CustomerState.ABANDONED);
        if (!waitingQueue.dequeueCustomer(cust)) {
            for (Table t : tables) {
                if (t.getCurrentCustomer() == cust) {
                    t.releaseTable();
                    break;
                }
            }
        }
        Order order = cust.getCurrentOrder();
        if (order != null) {
            order.markAbandoned();
            prepQueue.remove(order);
        }
        unhappyDeparture++;
    }

    // --- Statistics ---

    /**
     * getter method to return the total amount recorded from completed payments.
     */
    public int getRevenueInCents() {
        return revenueIntCents;
    }

    /**
     * getter method to return the total number of customer recorded as served and paid.
     */
    public int getServeAndPaidCount() {
        return serveAndPaidCount;
    }

    /**
     * getter method to return the total number of paid customers
     */
    public List<Customer> getPaidCustomers() {
        return paidCustomers;
    }

    /**
     * getter method to return the total number of abandoned customer (without paying)
     */
    public int getUnhappyDeparture() {
        return unhappyDeparture;
    }

    /**
     * getter method to calculate the mean satisfaction of the recorded paid-customers.
     * Returns zero when no payment has been recorded.
     */
    public double getAveragePaidSatisfaction() {
        if (paidCustomers.isEmpty()) {
            return 0.0;
        }
        int totalPaidSatisfaction = 0;
        for (Customer c : paidCustomers) {
            totalPaidSatisfaction += c.getSatisfaction();
        }

        return (double) totalPaidSatisfaction / paidCustomers.size();
    }

    /**
     * Checks whether all three restaurant targets are satisfied: at least 4 paid customers, at least 4500 cents of revenue and average paid satisfaction of at least 60.
     * The controller determines when this result becomes final.
     */
    public boolean isObjectiveMet() {
        return serveAndPaidCount >= TARGET_PAID_CUSTOMERS
                && revenueIntCents >= TARGET_REVENUE_IN_CENTS
                && getAveragePaidSatisfaction() >= TARGET_AVERAGE_SATISFACTION;
    }
}