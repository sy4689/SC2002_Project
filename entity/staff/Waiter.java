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


public class Waiter extends Staff {

    /**
     * Creates a waiter with the inherited name and available task allowance.
     */
    public Waiter(String name) {
        super(name);
    }

    /**
     * Uses one task to seat a queued customer at a free table.
     * Table assignment also changes the customer state to SEATED
     * The caller removes the customer from the waiting queue after successful seating.
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
     * Uses one task to take a seated customer's food order.
     * Obtains the optional customer policy, provide price calculation to Order,links the order, changes the customer to ORDERED and queues preparation work.
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
     * Uses one task to serve an order completed on an earlier turn.
     * Marks the order SERVED before asking the customer to start eating.
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
     * If decides to wait, consumes this turn's waiter task without seating, taking an order or serving.
    */
    public void waitTurn() {
        markBusy();
    }
}
