package Entity;

public class Order {
    private int orderID;
    private int tableID;
    private int remainingPrepUnits;
    private int turnReady = -1;
    private OrderStatus status;
    private int finalPriceInCents;
    private MenuItem item;

    public Order(int orderID, int tableID, MenuItem item){
        this.orderID = orderID;
        this.tableID = tableID;
        this.item = item;
        this.remainingPrepUnits = item.getPrepUnits();
        this.status = OrderStatus.PENDING;
        this.finalPriceInCents = item.getPriceInCents();
    }

    public int getRemainingPrepUnits() {
        return remainingPrepUnits;
    }

    public int getFinalPriceInCents(){
        return finalPriceInCents;
    }
    public MenuItem getItem(){
        return item;
    }
    public OrderStatus getStatus() {
        return status;
    }

    public void decrementPrepUnits(int currentTurn){
        if (status != OrderStatus.PENDING && status != OrderStatus.IN_KITCHEN){
            throw new IllegalStateException("Entity.Order cannot be prepared in its current state");
        }
        if (remainingPrepUnits <= 0) {
            throw new IllegalStateException("Entity.Order has no preparation units remaining");
        }
        remainingPrepUnits--;

        if (remainingPrepUnits ==0){
            status = OrderStatus.READY;
            turnReady = currentTurn;
        } else {
            status = OrderStatus.IN_KITCHEN;
        }
    }

    public void markServed(){
        if (status != OrderStatus.READY) {
            throw new IllegalStateException("Only a READY order can be served");
        }
        status = OrderStatus.SERVED;
    }
    public void markPaid(){
        if (status != OrderStatus.READY_TO_PAY) {
            throw new IllegalStateException("Entity.Order is not ready for payment");
        }
        status = OrderStatus.PAID;
    }
    public void markAbandoned(){
        if (status == OrderStatus.PAID || status == OrderStatus.SERVED || status == OrderStatus.READY_TO_PAY) {
            throw new IllegalStateException("Entity.Order cannot be abandoned");
        }
        else status = OrderStatus.ABANDONED;
    }
    public boolean isReady(){
        return status == OrderStatus.READY && turnReady < currentTurn;

    }
}
