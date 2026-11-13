package entity.order;

import entity.menu.MenuItem;
import entity.DiscountPolicy.DiscountPolicy;
import java.util.List;

public class Order {
    private int orderID;
    private int tableID;
    private int remainingPrepUnits;
    private int turnReady = -1;
    private OrderStatus status;
    private int finalPriceinCents;
    private MenuItem item;

    /**
     * Creates a PENDING order with the selected item price and preparation units. The order-taking workflow applies any pricing policies separately through lockPrice before publishing the order.
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
     * getter method to return food remaining preparation unit;
     */
    public int getRemainingPrepUnits() {
        return remainingPrepUnits;
    }

    /**
     * getter method to return food final price in cents after locking the price;
     */
    public int getFinalPriceInCents(){
        return finalPriceinCents;
    }

    /**
     * Calculates and stores the bill by starting from the item's original price and applying the supplied policies in list order.
     * An empty list preserves full price.
     */
    public void lockPrice(List<DiscountPolicy> policies) {
        int cents = item.getPriceInCents();

        for (DiscountPolicy policy : policies) {
            cents = policy.applyDiscount(cents);
        }

        finalPriceinCents = cents;
    }
    /**
     * getter method to return food item;
     */
    public MenuItem getItem(){
        return item;
    }

    /**
     * getter method to return food status;
     */
    public OrderStatus getStatus() {
        return status;
    }

    /**
     * getter method to return order ID;
     */
    public int getOrderID() {
        return orderID;
    }

    /**
     * getter method to return table ID;
     */
    public int getTableID() {
        return tableID;
    }

    /**
     * Decrease the remaining preparation unit without going below zero
     * Marks unfinished work (prep unit >0) as IN_KITCHEN
     * After completing the last unit marks the food as READY and records the completion turn
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
     */

    public void markServed(){
        if (status != OrderStatus.READY) {
            throw new IllegalStateException("Order is not ready");
        } 
        status = OrderStatus.SERVED;
    }

    /**
     * Changes a READY_TO_PAY order to PAID status.
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
     * Restaurant performs cleanup separately.
     */
    public void markAbandoned(){
        if (status == OrderStatus.PAID || status == OrderStatus.SERVED || status == OrderStatus.READY_TO_PAY) {
            throw new IllegalStateException("Order cannot be abandoned in state" + status);
        }
        status = OrderStatus.ABANDONED;
    }

    /**
     * Changes a SERVED order to READY_TO_PAY after customer eat finish
     */
    public void markReadyToPay() {
        if (status != OrderStatus.SERVED) {
            throw new IllegalStateException("Order has not served yet.");
        }
        status = OrderStatus.READY_TO_PAY;
    }

    /**
     * Checks both READY status and whether preparation is finished strictly on an earlier turn than the supplied turn.
     */
    public boolean isReady(int currentTurn){
        return status == OrderStatus.READY && turnReady < currentTurn;

    }
}
