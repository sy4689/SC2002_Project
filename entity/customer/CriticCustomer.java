package entity.customer;

import entity.menu.Menu;
import entity.menu.MenuItem;

public class CriticCustomer extends Customer {


    public CriticCustomer(int customerID, int arrivalTurn) {

        super(customerID, arrivalTurn);
        penalty=12;
    }

    @Override
    public void applyWaitingPenalty() {
        if (getState() == CustomerState.WAITING_IN_QUEUE ||
                getState() == CustomerState.SEATED ||
                getState() == CustomerState.ORDERED) {

            satisfaction = Math.max(0, satisfaction - penalty);

            if (satisfaction == 0) {
                setState(CustomerState.ABANDONED);
            }
        }
    }

    @Override
    public MenuItem chooseMenuItem(Menu menu) {
        MenuItem mostExpensive = menu.getItemByIndex(0);

        for (MenuItem item : menu.getItems()) {
            if (item.getPriceInCents() > mostExpensive.getPriceInCents()) {
                mostExpensive = item;
            }
        }

        return mostExpensive;
    }
}
