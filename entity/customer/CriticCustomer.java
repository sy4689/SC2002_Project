package entity.customer;

import entity.menu.Menu;
import entity.menu.MenuItem;

public class CriticCustomer extends Customer {

    /**
     * Creates a critic customer with satisfaction 100 and 12 point waiting penalty.
     */
    public CriticCustomer(int customerID, int arrivalTurn) {

        super(customerID, arrivalTurn);
        penalty=12;
    }

    /**
     * Deducts 12 satisfaction points while this critic customer is queuing, seated and ordered, or ordered but not served.
     * Lowest point is 0 so we take max (0 or satisfaction - 8)
     * customer is ABANDONED at zero.
     * Restaurant will perform departure cleanup separately.
     */
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
    /**
     * We define our criticcustomer behaviour to choose item with the highest menu price
     * Keeps the first maximum when prices tie.
     */
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
