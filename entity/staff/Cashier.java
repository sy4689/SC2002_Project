
package entity.staff;

import entity.order.Order;
import entity.order.OrderStatus;
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

        Order order = cust.getCurrentOrder();

        if (order == null || order.getStatus() != OrderStatus.READY_TO_PAY){
            throw new IllegalStateException("Customer's order is not ready for payment");
        }
        
        markBusy();
        int bill = order.getFinalPriceInCents();
        
        order.markPaid();
        cust.setState(CustomerState.PAID_AND_LEFT);
        
        r.recordPayment(cust, bill);
        t.releaseTable();
        return bill;
    }
}
