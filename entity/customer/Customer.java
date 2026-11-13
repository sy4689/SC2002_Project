//added getDiscountPolicy

package entity.customer;

import entity.order.Order;
import entity.order.OrderStatus;
import entity.DiscountPolicy.DiscountPolicy;
import entity.DiscountPolicy.VIPDiscountPolicy;
import entity.menu.Menu;
import entity.menu.MenuItem;
import java.util.Random;

/**
 * Entity abstract class that describe a general template for customer
 */
public abstract class Customer {
    protected int satisfaction = 100;
    protected int penalty;
    private int customerID;
    private int arrivalTurn;
    private CustomerState state;
    private int eatingTurnsLeft = 1;
    private Order currentOrder;
    private int servedTurn = -1;

    /**
     * Initialises a customer starting in WAITING_IN_QUEUE state with satisfaction 100.
     *
     * @param customerID identifier assigned to this customer
     * @param arrivalTurn turn on which the customer joins the restaurant
     */
    public Customer(int customerID, int arrivalTurn){
        this.customerID = customerID;
        this.arrivalTurn = arrivalTurn;
        this.state = CustomerState.WAITING_IN_QUEUE;
    }
    /**
     * Getter method to return turn recorded when this customer arrived
     *
     * @return the customer's arrival turn
     */
    public int getArrivalTurn() {
        return arrivalTurn;
    }

    /**
     * Getter method to return identifier assigned to this customer.
     *
     * @return the customer ID
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * Getter method to return customer's current satisfaction score.
     *
     * @return the current satisfaction value
     */
    public int getSatisfaction() {
        return satisfaction;
    }

    /**
     * Getter method to return  customer's current service-lifecycle state.
     *
     * @return the current customer state
     */
    public CustomerState getState() {
        return state;
    }

    /**
     * Setter method to set customer state
     *
     * @param state new customer state
     */
    public void setState(CustomerState state) {
        this.state = state;
    }

    /**
     * Getter method to return current Order associated with this customer
     *
     * @return the associated order, or null if no order is assigned
     */
    public Order getCurrentOrder() {
        return currentOrder;
    }

    /**
     * Setter method to set the current order
     *
     * @param order order to associate, or null to clear the current reference
     */
    public void setCurrentOrder(Order order) {
        this.currentOrder = order;
    }

    /**
     * Deducts the subclass-assigned penalty before customer eats his/her order
     * Floors satisfaction at zero and marks ABANDONED at zero
     */
    public void applyWaitingPenalty() {
        if (getState() == CustomerState.WAITING_IN_QUEUE ||
                getState() == CustomerState.SEATED ||
                getState() == CustomerState.ORDERED){

            satisfaction = Math.max(0, satisfaction - penalty);

            if (satisfaction == 0) {
                setState(CustomerState.ABANDONED);
            }
        }
    }

    /**
     * Selects food deterministically using custom modulo function.
     * The default rule assumes a non-negative customer ID and a non-empty menu;
     * subclasses may override it to provide another preference.
     *
     * @param menu non-null, non-empty menu from which the customer chooses
     * @return the item at the calculated menu index
     */
    public MenuItem chooseMenuItem(Menu menu){
        int index = customerID % menu.getItems().size();
        return menu.getItemByIndex(index);
    }

    /**
     * Starts eating after this customer's order has been marked SERVED.
     * Records the service turn so eating cannot complete during that same turn.
     *
     * @param currentTurn turn in which the food is served
     * @throws IllegalStateException if the customer is not ORDERED, has no order,
     *         or the order is not SERVED
     */
    public void startEating(int currentTurn) {
        if (state != CustomerState.ORDERED){
            throw new IllegalStateException("Only customer that had ordered food can start eating");
        }

        if (currentOrder == null || currentOrder.getStatus() != OrderStatus.SERVED) {
            throw new IllegalStateException("The customer's order must be served before eating.");
        }
        servedTurn = currentTurn;
        state = CustomerState.EATING;
    }

    /**
     * Reduces remaining eating time by one once per game turn when the customer is EATING and
     * the supplied turn is later than the service turn.
     *
     * @param currentTurn turn in which eating progress is processed
     */
    public void decrementEatingTurns(int currentTurn){
        if (currentTurn > servedTurn) {
            if (state == CustomerState.EATING && eatingTurnsLeft > 0) {
                eatingTurnsLeft--;
            }
        }
    }
    /**
     * Checks whether an EATING customer has no eating turns remaining.
     *
     * @return true if the customer is EATING and its remaining eating time is zero
     */
    public boolean isFinishedEating(){
        return state == CustomerState.EATING && eatingTurnsLeft == 0;
    }

    /**
     * Checks whether the customer's state is ABANDONED.
     *
     * @return true if the customer is marked as abandoned; false otherwise
     */
    public boolean isAbandoned(){
        return (state == CustomerState.ABANDONED);
    }

    /**
     * Provides the optional customer-specific pricing strategy.
     * This default implementation returns null to indicate full price; caller must handle the null value
     *
     * @return the pricing policy, or null when no customer discount applies
     */
    public DiscountPolicy getDiscountPolicy() {
        return null;
    }
}
