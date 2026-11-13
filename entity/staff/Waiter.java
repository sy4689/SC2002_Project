package entity.staff;

import entity.customer.CustomerState;
import entity.customer.Customer;
import entity.Table;
import entity.menu.Menu;
import entity.menu.MenuItem;
import entity.order.Order;
import entity.Restaurant;

import entity.DiscountPolicy.DiscountPolicy;
import java.util.List;

/**
 * Class describing operations carried out by a waiter
 */
public class Waiter extends Staff {

    /**
     * Creates a waiter with the inherited name and available task allowance.
     *
     * @param name name identifying this waiter in task-limit messages
     */
    public Waiter(String name) {
        super(name);
    }

    /**
     * Seat a queued customer at a free table.
     * Table assignment also changes the customer to SEATED.
     *
     * @param cust queued customer to seat
     * @param t free table that will hold the customer
     * @return true after the customer has been assigned successfully
     * @throws IllegalArgumentException if the customer or table is null
     * @throws IllegalStateException if the table is occupied, the customer is
     *         not WAITING_IN_QUEUE, or the waiter has already used its task
     */
    public boolean seatCustomer(Customer cust, Table t) {
        if (cust == null || t == null) {
        throw new IllegalArgumentException("Customer and table must be provided. Can't seat a customer");
        }

        if (t.isOccupied()) {
            throw new IllegalStateException("Table chosen is already occupied. Can't seat a customer");
        }
    
        if (cust.getState() != CustomerState.WAITING_IN_QUEUE) {
            throw new IllegalStateException("Only a customer waiting in the queue can be seated.");
        }
    
        markBusy();
        t.assignCustomer(cust);
        return true;
    }

    /**
     * Take a seated customer's self-selected food order.
     * Obtains the optional customer policy, delegates price calculation to Order,
     * links the order, changes the customer to ORDERED and queues preparation work.
     *
     * @param t non-null table whose customer is waiting to order
     * @param menu non-null menu offered to the customer
     * @param r restaurant supplying the order ID and preparation queue
     * @return the new PENDING order with its payable price calculated
     * @throws IllegalStateException if no SEATED customer is present or the
     *         waiter has already used its task
     */
    public Order takeOrder(Table t, Menu menu, Restaurant r) {
        Customer cust = t.getCurrentCustomer();
        if (cust == null || cust.getState() != CustomerState.SEATED) {
            throw new IllegalStateException("No seated customer.");
        }

        markBusy();
        MenuItem item = cust.chooseMenuItem(menu);
        Order order = new Order(r.getNextOrderID(), t.getTableID(), item);
        //the waiter forwards the existing policy and applies it once while taking the order.
        // Its task limit, validation,customer state changes and kitchen-que insertion are preserved.
        DiscountPolicy policy = cust.getDiscountPolicy();
        order.lockPrice(policy == null ? List.of() : List.of(policy));

        cust.setCurrentOrder(order);
        cust.setState(CustomerState.ORDERED);
        r.addToPrepQueue(order);
        return order;
    }

    /**
     * Serve an order completed on an earlier turn.
     * Marks the order SERVED before asking the customer to start eating.
     *
     * @param t non-null table whose order is being served
     * @param currentTurn turn in which service is attempted
     * @throws IllegalStateException if there is no eligible READY order, the
     *         customer cannot start eating, or the waiter has already used its task
     */
    public void serveOrder(Table t, int currentTurn) {
        Customer cust = t.getCurrentCustomer();
        Order order = (cust == null) ? null : cust.getCurrentOrder();
        if (order == null || !order.isReady(currentTurn)) {
            throw new IllegalStateException("No order is ready to be served.");
        }
        markBusy();
        order.markServed();
        cust.startEating(currentTurn);
    }

    /**
     * Consumes this turn's waiter task without seating, taking an order or serving.
     *
     * @throws IllegalStateException if the waiter has already used its task
     */
    public void waitTurn() {
        markBusy();
    }
}
