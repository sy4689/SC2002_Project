package entity.order;

import entity.menu.MenuItem;

public class Order {
    private int orderID;
    private int tableID;
    private int remainingPrepUnits;
    private int turnReady = -1;
    private OrderStatus status;
    private int finalPriceinCents;
    private MenuItem item;

    public Order(int orderID, int tableID, MenuItem item){
        this.orderID = orderID;
        this.tableID = tableID;
        this.item = item;
        this.remainingPrepUnits = item.getPrepUnits();
        this.status = OrderStatus.PENDING;
        this.finalPriceinCents = item.getPriceInCents();
    }

    public int getRemainingPrepUnits() {
        return remainingPrepUnits;
    }

    public int getFinalPriceInCents(){
        return finalPriceinCents;
    }
    public MenuItem getItem(){
        return item;
    }
    public OrderStatus getStatus() {
        return status;
    }

    public int getOrderID() {
        return orderID;
    }

    public int getTableID() {
        return tableID;
    }
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

    public void markServed(){
        if (status != OrderStatus.READY) {
            throw new IllegalStateException("Order is not ready");
        } 
        status = OrderStatus.SERVED;
    }
    public void markPaid(){
        if (status != OrderStatus.READY_TO_PAY) {
            throw new IllegalStateException("Order is not ready for payment");
        } 
        status = OrderStatus.PAID;
    }
    public void markAbandoned(){
        if (status == OrderStatus.PAID || status == OrderStatus.SERVED || status == OrderStatus.READY_TO_PAY) {
            throw new IllegalStateException("Order cannot be abandoned in state" + status");
        }
        status = OrderStatus.ABANDONED;
    }

    public void markReadyToPay() {
        if (status != OrderStatus.SERVED) {
            throw new IllegalStateException("Order has not served yet.");
        }
        status = OrderStatus.READY_TO_PAY;
    }
    public boolean isReady(int currentTurn){
        return status == OrderStatus.READY && turnReady < currentTurn;

    }
}
