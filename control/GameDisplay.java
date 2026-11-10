package control;

import control.view.OrderView;
import control.view.RestaurantView;
import control.view.SummaryView;

public interface GameDisplay {
    int WAIT_CHOICE = 0;

    void displayTurnHeader(int turn, int maxTurns);
    void displayRestaurantState(RestaurantView view);
    void displayAction(String msg);
    void displayOrderTaken(int customerID, OrderView order);
    void displayPaymentDone(int customerID, int amountInCents, int satisfaction, int tableID);

    WaiterAction promptWaiterTask(RestaurantView view);

    int promptChefOrderPriority(RestaurantView view);

    void displayEndOfTurnSummary(SummaryView summary, int turn, int maxTurns);
    void displayFinalResult(GameState result, SummaryView summary, int maxTurns);
    void displayStateError(String msg);

    void displayChoiceError(String msg);

}
