
package entity.staff;

import entity.order.Order;
import entity.Table;
import entity.Restaurant;
import entity.customer.Customer;
import entity.customer.CustomerState;

public class Cashier extends Staff {
    private final int payCapacity = 1;

    public Cashier(String name) {
        super(name);
    }

    public int processPayment(Table t, Restaurant r) {
        Customer cust = t.getCurrentCustomer();
        if (cust == null || cust.getState() != CustomerState.WAITING_TO_PAY) {
            throw new IllegalStateException("Nobody at table " + t.getTableID() + " is ready to pay");
        }

        markBusy();
        Order order = cust.getCurrentOrder();
        int bill = order.getFinalPriceInCents();
        r.recordPayment(cust, bill);
        order.markPaid();
        cust.setState(CustomerState.PAID_AND_LEFT);
        t.releaseTable();
        return bill;
    }
}