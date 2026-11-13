/**
 * inherit from Staff
 * Performs one preparation unit per turn or waits if got no order.
 *
 * Preparation accepts a non-null PENDING or IN_KITCHEN order.
 * Once preparation unit is 0, order marks the last unit READY with its completion turn
 *
 */

package entity.staff;

import entity.order.OrderStatus;
import entity.order.Order;

public class Chef extends Staff {
    private final int prepCapacity = 1;

    public Chef (String name) {
        super(name);
    }

    public void prepOrder (Order order, int currentTurn) {
        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.IN_KITCHEN)
            throw new IllegalStateException("Order " + order.getOrderID() + " do not need to be prepared.");
        markBusy();
        order.decrementPrepUnits(currentTurn, prepCapacity);
    }

    public void waitTurn() {
        markBusy();
    }
}
