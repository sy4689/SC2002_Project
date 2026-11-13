package entity.order;

import entity.menu.MenuItem;
import entity.DiscountPolicy.DiscountPolicy;
import java.util.List;

/**
 * Class describing information about an order for a particular customer
 */
public class Order {
    private int orderID;
    private int tableID;
    private int remainingPrepUnits;
    private int turnReady = -1;
    private OrderStatus status;
    private int finalPriceinCents;
    private MenuItem item;

    /**
     * Initialize a PENDING order with a specific menu item
     *
     * @param orderID identifier assigned to the order
     * @param tableID identifier of the table placing the order
     * @param item non-null menu item selected by the customer
     */
    public Order(int orderID, int tableID, MenuItem item){
        this.orderID = orderID;
        this.tableID = tableID;
        this.item = item;
        this.remainingPrepUnits = item.getPrepUnits();
        this.status = OrderStatus.PENDING;
        this.finalPriceinCents = item.getPriceInCents();
    }

    /**
     * Getter method to return food remaining preparation unit;
     *
     * @return the remaining preparation units
     */
    public int getRemainingPrepUnits() {
        return remainingPrepUnits;
    }

    /**
     * Getter method to return the stored payable amount.
     *
     * @return the stored bill in integer cents
     */
    public int getFinalPriceInCents(){
        return finalPriceinCents;
    }

    /**
     * Calculates and stores the bill by applying the supplied
     * policies in list order on an item's base price
     *
     * @param policies non-null list of pricing policies with no null entries
     * @throws NullPointerException if the list or one of its policies is null
     */
    public void lockPrice(List<DiscountPolicy> policies) {
        int cents = item.getPriceInCents();

        for (DiscountPolicy policy : policies) {
            cents = policy.applyDiscount(cents);
        }

        finalPriceinCents = cents;
    }
    /**
     * Getter method to return the menu item selected for this order.
     *
     * @return the ordered food item
     */
    public MenuItem getItem(){
        return item;
    }

    /**
     * Getter method to return the order's current preparation/service lifecycle status.
     *
     * @return the current order status
     */
    public OrderStatus getStatus() {
        return status;
    }

    /**
     * Getter method to return the identifier assigned to this order.
     *
     * @return the order ID
     */
    public int getOrderID() {
        return orderID;
    }

    /**
     * Getter method to return the identifier of the table associated with this order.
     *
     * @return the associated table ID
     */
    public int getTableID() {
        return tableID;
    }

    /**
     * Decrease the remaining preparation unit without going below zero
     * Marks unfinished work (prep unit >0) as IN_KITCHEN
     * After completing the last unit marks the food as READY and records the completion turn
     *
     * @param currentTurn turn in which this preparation work is performed
     * @param prepCapacity positive number of preparation units to apply
     * @throws IllegalStateException if the order is not PENDING or IN_KITCHEN,
     *         has no preparation work left, or the capacity is not positive
     */
    public void decrementPrepUnits(int currentTurn, int prepCapacity){
        if (status != OrderStatus.PENDING && status != OrderStatus.IN_KITCHEN){
            throw new IllegalStateException("Order cannot be prepared in state" + status);
        }
        if (remainingPrepUnits <= 0 || prepCapacity <= 0) {
            throw new IllegalStateException("Invalid preparation or already completed order");
        }

        remainingPrepUnits = Math.max(0, remainingPrepUnits - prepCapacity);
        
        if (remainingPrepUnits == 0) {
            status = OrderStatus.READY;
            turnReady = currentTurn;
        } else {
            status = OrderStatus.IN_KITCHEN;
        }
    }

    /**
     * Changes a READY order to SERVED status.
     *
     * @throws IllegalStateException if the order is not READY
     */

    public void markServed(){
        if (status != OrderStatus.READY) {
            throw new IllegalStateException("Order is not ready");
        } 
        status = OrderStatus.SERVED;
    }

    /**
     * Changes a READY_TO_PAY order to PAID status.
     *
     * @throws IllegalStateException if the order is not READY_TO_PAY
     */
    public void markPaid(){
        if (status != OrderStatus.READY_TO_PAY) {
            throw new IllegalStateException("Order is not ready for payment");
        } 
        status = OrderStatus.PAID;
    }

    /**
     * Marks an unserved order as ABANDONED.
     * Does not remove preparation-queue entries or release a table.
     *
     * @throws IllegalStateException if the order is SERVED, READY_TO_PAY or PAID
     */
    public void markAbandoned(){
        if (status == OrderStatus.PAID || status == OrderStatus.SERVED || status == OrderStatus.READY_TO_PAY) {
            throw new IllegalStateException("Order cannot be abandoned in state" + status);
        }
        status = OrderStatus.ABANDONED;
    }

    /**
     * Changes a SERVED order to READY_TO_PAY after eating has finished.
     *
     * @throws IllegalStateException if the order is not SERVED
     */
    public void markReadyToPay() {
        if (status != OrderStatus.SERVED) {
            throw new IllegalStateException("Order has not served yet.");
        }
        status = OrderStatus.READY_TO_PAY;
    }

    /**
     * Checks both READY status and whether preparation finished on a strictly
     * earlier turn than the supplied turn.
     *
     * @param currentTurn turn in which service is being considered
     * @return true if the order is READY and was completed on an earlier turn
     */
    public boolean isReady(int currentTurn){
        return status == OrderStatus.READY && turnReady < currentTurn;

    }
}
