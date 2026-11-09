package control;

import control.view.CustomerView;
import control.view.OrderView;
import control.view.RestaurantView;
import control.view.SummaryView;
import control.view.TableView;

import entity.Restaurant;
import entity.customer.Customer;
import entity.Table;
import entity.customer.CustomerState;
import entity.order.Order;
import entity.order.OrderStatus;

import java.util.List;
import java.util.ArrayList;

public class ViewBuilder {
    public RestaurantView buildRestaurantView(Restaurant restaurant) {
        List<CustomerView> queue = new ArrayList<>();
        for (Customer c : restaurant.getWaitingQueue()) {
            queue.add(customerView(c));
        }

        List<TableView> tables = new ArrayList<>();
        int toPay = 0;
        for (Table t : restaurant.getTables()) {
            if (!t.isOccupied()) {
                tables.add(new TableView(t.getTableID(), false, null, null));
                continue;
            }

            Customer c = t.getCurrentCustomer();
            Order o = c.getCurrentOrder();
            tables.add(new TableView(t.getTableID(), true, customerView(c), orderView(o)));
            if (c.getState() == CustomerState.WAITING_TO_PAY) {
                toPay++;
            }
        }

        List<OrderView> prepQueue = new ArrayList<>();
        for (Order o : restaurant.getPrepQueue()) {
            prepQueue.add(orderView(o));
        }

        return new RestaurantView(restaurant.getRevenueInCents(),
                                restaurant.getServeAndPaidCount(),
                                averageSatisfactionHelper(restaurant),
                                queue,
                                tables,
                                getTableIDs(restaurant.getAvailableTables()),
                                getTableIDs(restaurant.getTablesAwaitingOrder()),
                                getTableIDs(restaurant.getTablesWithReadyOrder()),
                                prepQueue,
                                toPay);
    }

    public SummaryView buildSummaryView(Restaurant restaurant) {
        return new SummaryView(
                restaurant.getServeAndPaidCount(),
                restaurant.getUnhappyDeparture(),
                restaurant.getRevenueInCents(),
                averageSatisfactionHelper(restaurant),
                Restaurant.TARGET_PAID_CUSTOMERS,
                Restaurant.TARGET_REVENUE_IN_CENTS,
                Restaurant.TARGET_AVERAGE_SATISFACTION
        );
    }

    public OrderView orderView(Order o) {
        if (o == null) {
            return null;
        }

        boolean awaitingPreparation = o.getStatus() == OrderStatus.PENDING || o.getStatus() == OrderStatus.IN_KITCHEN;
        return new OrderView(
                o.getOrderID(),
                o.getTableID(),
                o.getItem().getFoodName(),
                o.getFinalPriceInCents(),
                o.getStatus().name(),
                o.getRemainingPrepUnits(),
                awaitingPreparation);
    }

    public CustomerView customerView(Customer c) {
        return new CustomerView(
                c.getCustomerID(),
                c.getSatisfaction(),
                c.getState().name());
    }

    // --- Helper Methods ---
    private static Double averageSatisfactionHelper(Restaurant restaurant) {
        return restaurant.getPaidCustomers().isEmpty()? null : restaurant.getAveragePaidSatisfaction();
    }

    private static List<Integer> getTableIDs(List<Table> tables) {
        List<Integer> ids = new ArrayList<>();
        for (Table t : tables) {
            ids.add(t.getTableID());
        }
        return ids;
    }
}
