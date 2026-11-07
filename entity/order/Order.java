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
            System.out.println("Order cannot be prepared");
        }
        else if (remainingPrepUnits <= 0) {
            System.out.println("Order is already prepared");
        }
        else {
            remainingPrepUnits -= prepCapacity;
        }
        if (remainingPrepUnits == 0) {
            status = OrderStatus.READY;
            turnReady = currentTurn;
        } else {
            status = OrderStatus.IN_KITCHEN;
        }
    }

    public void markServed(){
        if (status != OrderStatus.READY) {
            System.out.println("Order is not ready");
        }
        else status = OrderStatus.SERVED;
    }
    public void markPaid(){
        if (status != OrderStatus.READY_TO_PAY) {
            System.out.println("Order is not ready for payment");
        }
        else status = OrderStatus.PAID;
    }
    public void markAbandoned(){
        if (status == OrderStatus.PAID || status == OrderStatus.SERVED || status == OrderStatus.READY_TO_PAY) {
            System.out.println("Order cannot be abandoned");

        }
        else status = OrderStatus.ABANDONED;
    }

    public void markReadyToPay() {
        if (status != OrderStatus.SERVED) {
            System.out.println("Order is not served yet.");
        }
        else status = OrderStatus.READY_TO_PAY;
    }
    public boolean isReady(int currentTurn){
        return status == OrderStatus.READY && turnReady < currentTurn;

    }
}
