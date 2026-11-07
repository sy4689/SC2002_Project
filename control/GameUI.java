package control;

import java.util.List;
import entity.Restaurant;
import entity.order.Order;
import entity.Table;
import entity.customer.Customer;

public interface GameUI {
    int WAIT_CHOICE = 0;
    void displayTurnHeader(int turn);
    void displayRestaurantState(Restaurant restaurant);
    void displayAction(String msg);
    WaiterAction promptWaiterTask(Restaurant restaurant) throws InvalidChoiceException;
    int promptChefOrderPriority(List<Order> orders) throws InvalidChoiceException;
    int promptTargetTable(String label, List<Table> tables);
    int promptQueuedCustomer(List<Customer> queue);
    void displayEndOfTurnSummary(Restaurant restaurant, int turn);
    void displayFinalResult(GameState result, Restaurant restaurant);
    void displayError(String msg);
}
