package control.view;

/**
 * Read-only snapshot of one table for the UI.
 * customer and order are null when the table is free or no order was taken yet.
 */
public final class TableView {
    private final int id;
    private final boolean occupied;
    private final CustomerView customer;
    private final OrderView order;

    public TableView(int id, boolean occupied, CustomerView customer, OrderView order) {
        this.id = id;
        this.occupied = occupied;
        this.customer = customer;
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public CustomerView getCustomer() {
        return customer;
    }

    public OrderView getOrder() {
        return order;
    }
}
