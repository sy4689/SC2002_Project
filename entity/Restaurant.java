package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import entity.customer.Customer;
import entity.customer.CustomerState;
import entity.order.Order;
import entity.order.OrderStatus;
import entity.menu.Menu;


/**
 * Class recording game state of the restaurant game
 */
public class Restaurant {
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
     * Creates a restaurant with the supplied menu and two initially empty tables.
     * Initialises empty waiting, preparation and paid-customer collections.
     *
     * @param menu menu associated with this restaurant
     */
    public Restaurant(Menu menu) {
        this.menu = menu;
        for (int i = 1; i <= TABLE_COUNT; i++) {
            tables.add(new Table(i));
        }
    }

    // --- Menu ---

    /**
     * Getter method to return the menu associated with this restaurant.
     *
     * @return the restaurant's menu
     */
    public Menu getMenu() {
        return menu;
    }

    /**
     * Allocates the next order identifier and increments the internal counter.
     *
     * @return the newly allocated order ID
     */
    public int getNextOrderID() {
        return nextOrderID++;
    }

    // --- Queue ---

    /**
     * Adds a customer to the end of the waiting collection.
     *
     * @param cust customer joining the restaurant's waiting queue
     */
    public void enqueueCustomer(Customer cust) {
        waitingQueue.enqueueCustomer(cust);
    }

    /**
     * Removes the specified customer from the waiting collection if present.
     * Does not change the customer's state or assign a table.
     *
     * @param cust customer to remove from the waiting queue
     */
    public void dequeueCustomer(Customer cust) {
        waitingQueue.dequeueCustomer(cust);
    }

    /**
     * Finds a waiting customer by identifier.
     *
     * @param customerID identifier of the customer to find
     * @return the matching customer, or null if the ID is not in the waiting queue
     */
    public Customer findQueuedCustomer(int customerID) {
        return waitingQueue.findByID(customerID);
    }

    /**
     * Getter method to return an unmodifiable view of customers currently waiting to be seated.
     *
     * @return the waiting customers in insertion order
     */
    public List<Customer> getWaitingQueue() {
        return waitingQueue.getCustomers();
    }

    // --- Tables ---

    /**
     * Getter method to return an unmodifiable view of the restaurant's tables.
     *
     * @return the tables belonging to this restaurant
     */
    public List<Table> getTables() {
        return Collections.unmodifiableList(tables);
    }

    /**
     * Finds a restaurant table by identifier.
     *
     * @param tableID identifier of the table to find
     * @return the matching table
     * @throws IllegalStateException if no table has the supplied identifier
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
     * Collects the tables that are currently unoccupied.
     *
     * @return a new list of free tables, or an empty list if all tables are occupied
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
     * Collects occupied tables whose customers are SEATED and awaiting an order.
     *
     * @return a new list of tables with customers ready to order
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
     * Collects occupied tables whose current orders have READY status.
     * This query does not check the preparation-completion turn; Waiter checks
     * that timing separately before serving.
     *
     * @return a new list of tables with READY orders
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
     * Selects the table with the lowest customer ID among customers in
     * WAITING_TO_PAY state. Customer IDs increase with arrival order in normal play.
     *
     * @return the selected payment table, or null if no customer is waiting to pay
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
     *
     * @param order order to make available for kitchen preparation
     */
    public void addToPrepQueue(Order order) {
        prepQueue.add(order);
    }

    /**
     * Removes the specified order from the preparation queue.
     *
     * @param order order to remove from kitchen preparation
     * @throws IllegalStateException if the queue is empty or does not contain
     *                               the supplied order
     */
    public void removeFromPrepQueue(Order order) {
        if (prepQueue.isEmpty()) {
            throw new IllegalStateException("Preparing queue in kitchen is empty. Can't remove any READY order");
        }

        if (!prepQueue.contains(order)) {
            throw new IllegalStateException("Supposed READY order that need be removed can't be found from the current preparing queue in the kitchen");
        }

        prepQueue.remove(order);
    }

    /**
     * Getter method to return an unmodifiable view of the orders in the preparation queue.
     *
     * @return orders currently available for kitchen preparation
     */
    public List<Order> getPrepQueue() {
        return Collections.unmodifiableList(prepQueue);
    }

    // --- Processing for each turn ---

    /**
     * Advances eating for customers currently in EATING state.
     * Customers who finish move to WAITING_TO_PAY, and their orders move to
     * READY_TO_PAY. Eating cannot finish during the turn in which food was served.
     *
     * @param currentTurn turn being processed
     * @return a new list of customers who became ready to pay during this call
     * @throws IllegalStateException if a finished customer's order cannot
     *                               transition to READY_TO_PAY
     */
    public List<Customer> processEatingCust(int currentTurn) {
        List<Customer> finished = new ArrayList<>();
        for (Table t : tables) {
            Customer c = t.getCurrentCustomer();
            if (c != null && c.getState() == CustomerState.EATING) {
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
     * Collects waiting-queue entries and table customers who are still queued,
     * seated or ordered but unserved, for turn-end satisfaction processing.
     *
     * @return a new list of customers to consider for waiting penalties
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
     * Adds a completed payment to revenue, increments the paid-customer count
     * and retains the customer for satisfaction statistics. The caller is
     * responsible for recording a valid payment only once.
     *
     * @param cust   customer whose payment was completed
     * @param amount amount collected, in integer cents
     */
    public void recordPayment(Customer cust, int amount) {
        revenueIntCents += amount;
        serveAndPaidCount++;
        paidCustomers.add(cust);
    }

    /**
     * Marks the customer ABANDONED, removes queue or table occupancy, cancels any associated order,
     * removes its preparation work and increments unhappy departures.
     *
     * @param cust unserved customer leaving without payment
     * @throws IllegalStateException if the associated order is already SERVED,
     *                               READY_TO_PAY or PAID and cannot be abandoned
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
     * Getter method to return the total amount recorded from completed payments.
     *
     * @return collected revenue in integer cents
     */

    public int getRevenueInCents() {
        return revenueIntCents;
    }

    /**
     * Getter method to return the number of customers recorded as served and paid.
     *
     * @return the completed paid-customer count
     */
    public int getServeAndPaidCount() {
        return serveAndPaidCount;
    }

    /**
     * Getter method to return the total number of paid customers
     *
     * @return the restaurant's mutable paid-customer list
     */
    public List<Customer> getPaidCustomers() {
        return paidCustomers;
    }

    /**
     * Getter method to return the total number of abandoned customer (without paying)
     *
     * @return the unhappy-departure count
     */
    public int getUnhappyDeparture() {
        return unhappyDeparture;
    }

    /**
     * Calculates the mean satisfaction of the retained paid-customer objects.
     * Returns zero when no payment has been recorded.
     *
     * @return the average paid-customer satisfaction, or 0.0 for an empty list
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
     * Checks whether all three restaurant targets are satisfied.
     * The controller determines when this result becomes final.
     *
     * @return true if every objective threshold is met; false otherwise
     */
    public boolean isObjectiveMet() {
        return serveAndPaidCount >= TARGET_PAID_CUSTOMERS
                && revenueIntCents >= TARGET_REVENUE_IN_CENTS
                && getAveragePaidSatisfaction() >= TARGET_AVERAGE_SATISFACTION;
    }
}