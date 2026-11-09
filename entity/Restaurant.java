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

    public Restaurant(Menu menu) {
        this.menu = menu;
        for (int i = 1; i <= TABLE_COUNT; i++) {
            tables.add(new Table(i));
        }
    }

    // --- Menu ---
    public Menu getMenu() {
        return menu;
    }
    public int getNextOrderID() {
        return nextOrderID++;
    }

    // --- Queue ---
    public void enqueueCustomer(Customer cust) {
        waitingQueue.enqueueCustomer(cust);
    }

    public void dequeueCustomer(Customer cust) {
        waitingQueue.dequeueCustomer(cust);
    }

    public Customer findQueuedCustomer(int customerID) {
        return waitingQueue.findByID(customerID);
    }

    public List<Customer> getWaitingQueue() {
        return waitingQueue.getCustomers();
    }

    // --- Tables ---
    public List<Table> getTables() {
        return Collections.unmodifiableList(tables);
    }

    public Table findTable(int tableID) {
        for (Table t : tables) {
            if (t.getTableID() == tableID) {
                return t;
            }
        }

        return null;
    }

    public List<Table> getAvailableTables() {
        List<Table> result = new ArrayList<>();
        for (Table t : tables) {
            if (!t.isOccupied()) {
                result.add(t);
            }
        }
        return result;
    }

    public List<Table> getTablesAwaitingOrder() {
        List<Table> result = new ArrayList<>();
        for (Table t : tables) {
            if (t.isOccupied() && t.getCurrentCustomer().getState() == CustomerState.SEATED) {
                result.add(t);
            }
        }
        return result;
    }

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
    public void addToPrepQueue(Order order) {
        prepQueue.add(order);
    }

    public void removeFromPrepQueue(Order order) {
        prepQueue.remove(order);
    }

    public List<Order> getPrepQueue() {
        return Collections.unmodifiableList(prepQueue);
    }

    // --- Processing for each turn ---
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

    public void recordPayment(Customer cust, int amount) {
        revenueIntCents += amount;
        serveAndPaidCount++;
        paidCustomers.add(cust);
    }

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
    public int getRevenueInCents() {
        return revenueIntCents;
    }
    public int getServeAndPaidCount() {
        return serveAndPaidCount;
    }

    public List<Customer> getPaidCustomers() {
        return paidCustomers;
    }

    public int getUnhappyDeparture() {
        return unhappyDeparture;
    }

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

    public boolean isObjectiveMet() {
        return serveAndPaidCount >= TARGET_PAID_CUSTOMERS
                && revenueIntCents >= TARGET_REVENUE_IN_CENTS
                && getAveragePaidSatisfaction() >= TARGET_AVERAGE_SATISFACTION;
    }
}