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

/**
 * Class describing operation carried out by a chef
 */
public class Chef extends Staff {
    private final int prepCapacity = 1;

    /**
     * Creates a chef with the inherited name and available task allowance.
     *
     * @param name name identifying this chef in task-limit messages
     */
    public Chef (String name) {
        super(name);
    }

    /**
     * Uses one task to advance a selected order by one preparation unit.
     * The order updates its own preparation status and completion turn.
     *
     * @param order non-null PENDING or IN_KITCHEN order to prepare
     * @param currentTurn turn in which preparation occurs
     * @throws IllegalStateException if the order cannot be prepared, has no work
     *         remaining, or the chef has already used its task
     */
    public void prepOrder (Order order, int currentTurn) {
        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.IN_KITCHEN)
            throw new IllegalStateException("Order " + order.getOrderID() + " do not need to be prepared.");
        markBusy();
        order.decrementPrepUnits(currentTurn, prepCapacity);
    }

    /**
     * Consumes this turn's chef task without advancing an order.
     *
     * @throws IllegalStateException if the chef has already used its task
     */
    public void waitTurn() {
        markBusy();
    }
}
