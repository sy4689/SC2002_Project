/**
 * Inherit from Staff
 * Cashier auto collects payment from customer after eating;
 * Afterward, releases the customer's table.
 *
 * Both the customer and order must be ready to pay.
 * Then, a successful action marks them paid, records the amount with Restaurant and frees the table.
 * Then, the bill is read from Order; discount policies are not applied again here.
 * Staff's availability rule limits collection to one payment per turn.

 */

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
