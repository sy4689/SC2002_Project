package entity.customer;

import entity.menu.Menu;
import entity.menu.MenuItem;

/**
 * Entity class that store information about critic customer
 */
public class CriticCustomer extends Customer {

    /**
     * Creates a critic customer with satisfaction 100 and a twelve-point waiting penalty.
     *
     * @param customerID identifier assigned to the critic customer
     * @param arrivalTurn turn on which the customer joins the restaurant
     */
    public CriticCustomer(int customerID, int arrivalTurn) {

        super(customerID, arrivalTurn);
        penalty=12;
    }

    /**
     * Deducts critic customer's waiting penalty except after food get served
     * Floors the score at zero and marks ABANDONED at zero
     * customer is ABANDONED at zero.
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
     * Chooses the item with the highest base menu price as the critic's
     * defined preference. Keeps the first maximum when prices tie.
     *
     * @param menu non-null, non-empty menu to inspect
     * @return the first menu item with the highest base price
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
