package entity.staff;

import entity.customer.CustomerState;
import entity.customer.Customer;
import entity.Table;
import entity.menu.Menu;
import entity.menu.MenuItem;
import entity.order.Order;
import entity.Restaurant;

public class Waiter extends Staff {
    public Waiter(String name) {
        super(name);
    }

    public boolean seatCustomer(Customer cust, Table t) {
        if (!t.isOccupied()) {
            markBusy();
            t.assignCustomer(cust);
            cust.setState(CustomerState.SEATED);
        } else{
            throw new IllegalStateException("Table chosen is occupied");
        }
        return t.isOccupied();
    }

    public Order takeOrder(Table t, Menu menu, Restaurant r) {
        Customer cust = t.getCurrentCustomer();
        if (cust == null || cust.getState() != CustomerState.SEATED) {
            throw new IllegalStateException("No seated customer.");
        }

        markBusy();
        MenuItem item = cust.chooseMenuItem(menu);
        Order order = new Order(r.getNextOrderID(), t.getTableID(), item);
        cust.setCurrentOrder(order);
        cust.setState(CustomerState.ORDERED);
        r.addToPrepQueue(order);
        return order;
    }

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

    public void waitTurn() {
        markBusy();
    }
}
