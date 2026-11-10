package control.view;

public final class OrderView {
    private final int id;
    private final int tableID;
    private final String itemName;
    private final int priceInCents;
    private final String status;
    private final int remainingPrepUnits;
    private final boolean awaitingPreparation;

    public OrderView(int id, int tableID, String itemName, int priceInCents, String status, int remainingPrepUnits, boolean awaitingPreparation) {
        this.id = id;
        this.tableID = tableID;
        this.itemName = itemName;
        this.priceInCents = priceInCents;
        this.status = status;
        this.remainingPrepUnits = remainingPrepUnits;
        this.awaitingPreparation = awaitingPreparation;
    }

    public int getId() {
        return id;
    }

    public int getTableID() {
        return tableID;
    }

    public String getItemName() {
        return itemName;
    }

    public int getPriceInCents() {
        return priceInCents;
    }

    public String getStatus() {
        return status;
    }

    public int getRemainingPrepUnits() {
        return remainingPrepUnits;
    }

    public boolean isAwaitingPreparation() {
        return awaitingPreparation;
    }
}
