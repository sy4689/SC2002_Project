package control.view;

import java.util.List;

/**
 * Read-only snapshot of the whole restaurant for the UI.
 * It contains statistics : revenue, served and paid count, average satisfaction of paid customers
 * It also carries the pre-computed lists of valid choices
 * (free tables, tables awaiting an order, tables with a READY order, prep queue),
 * so UI can build the option list without knowing any game rules.
 */
public final class RestaurantView {
    private final int revenueInCents;
    private final int servedAndPaidCount;
    private final Double averageSatisfaction;
    private final List<CustomerView> queue;
    private final List<TableView> tables;
    private final List<Integer> freeTableIDs;
    private final List<Integer> tablesAwaitingOrder;
    private final List<Integer> tablesWithReadyOrder;
    private final List<OrderView> prepQueue;
    private final int toPayCount;

    public RestaurantView(int revenueInCents, int paidCount, Double averageSatisfaction, List<CustomerView> queue, List<TableView> tables, List<Integer> freeTableIds, List<Integer> tablesAwaitingOrder, List<Integer> tablesWithReadyOrder, List<OrderView> prepQueue, int toPayCount) {
        this.revenueInCents = revenueInCents;
        this.servedAndPaidCount = paidCount;
        this.averageSatisfaction = averageSatisfaction;
        this.queue = queue;
        this.tables = tables;
        this.freeTableIDs = freeTableIds;
        this.tablesAwaitingOrder = tablesAwaitingOrder;
        this.tablesWithReadyOrder = tablesWithReadyOrder;
        this.prepQueue = prepQueue;
        this.toPayCount = toPayCount;
    }

    public int getRevenueInCents() {
        return revenueInCents;
    }

    public int getServedAndPaidCount() {
        return servedAndPaidCount;
    }

    public Double getAverageSatisfaction() {
        return averageSatisfaction;
    }

    public List<CustomerView> getQueue() {
        return queue;
    }

    public List<TableView> getTables() {
        return tables;
    }

    public List<Integer> getFreeTableIDs() {
        return freeTableIDs;
    }

    public List<Integer> getTablesAwaitingOrder() {
        return tablesAwaitingOrder;
    }

    public List<Integer> getTablesWithReadyOrder() {
        return tablesWithReadyOrder;
    }

    public List<OrderView> getPrepQueue() {
        return prepQueue;
    }

    public int getToPayCount() {
        return toPayCount;
    }
}
