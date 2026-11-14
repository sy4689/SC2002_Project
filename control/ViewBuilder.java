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

    /**
     * Builds a RestaurantView by collecting the latest information
     * about the restaurant, its customers, tables and orders.
     * @param restaurant  the restaurant whose information is used to build the view
     * @return            return a read-only RestaurantView object containing the restaurant's current information
     */
    public RestaurantView buildRestaurantView(Restaurant restaurant) {
        List<CustomerView> queue = new ArrayList<>();
        for (Customer c : restaurant.getWaitingQueue()) {
            queue.add(customerView(c));
        }

        List<TableView> tables = new ArrayList<>();
        int toPay = 0;
        for (Table t : restaurant.getTables()) {
            // Create a TableView with no customer or order details if the table is empty
            if (!t.isOccupied()) {
                tables.add(new TableView(t.getTableID(), false, null, null));
                continue;
            }

            // Get the customer and their order to include their details in the table view
            Customer c = t.getCurrentCustomer();
            Order o = c.getCurrentOrder();
            tables.add(new TableView(t.getTableID(), true, customerView(c), orderView(o)));

            // Count the number of customers who are currently waiting to pay
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

    /**
     * Builds a SummaryView containing the restaurant's final performance
     * and compares the results with the target values.
     * @param restaurant   the restaurant whose final performance is being summarised
     * @return             a SummaryView object containing the restaurant's performance statistics and target values
     */
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

    /**
     * Converts an Order object into an OrderView for displaying order information
     * @param o   the order to be converted into an OrderView
     * @return    an OrderView object containing the order information, or null if no order is provided
     */
    public OrderView orderView(Order o) {
        // Return null if there is no order to display
        if (o == null) {
            return null;
        }

        // Check whether the order is still waiting for preparation or being prepared
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

    /**
     * Converts a Customer object into a CustomerView for displaying customer information
     * @param c   the customer to be converted into a CustomerView
     * @return    a CustomerView object containing the customer's ID, satisfaction, state and type
     */
    public CustomerView customerView(Customer c) {
        return new CustomerView(
                c.getCustomerID(),
                c.getSatisfaction(),
                c.getState().name(),
                // Newly added in Stage 2: To retrieve the customer type label from the class name
                c.getClass().getSimpleName()
                        .replace("Customer", " Customer")
                        .trim()
        );
    }

    // --- Helper Methods ---

    private static Double averageSatisfactionHelper(Restaurant restaurant) {
        return restaurant.getPaidCustomers().isEmpty()? null : restaurant.getAveragePaidSatisfaction();
    }
    // Collect the table IDs into a list for display
    private static List<Integer> getTableIDs(List<Table> tables) {
        List<Integer> ids = new ArrayList<>();
        for (Table t : tables) {
            ids.add(t.getTableID());
        }
        return ids;
    }
}
