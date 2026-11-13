//added getDiscountPolicy

package entity.customer;

import entity.order.Order;
import entity.order.OrderStatus;
import entity.DiscountPolicy.DiscountPolicy;
import entity.DiscountPolicy.VIPDiscountPolicy;
import entity.menu.Menu;
import entity.menu.MenuItem;
import java.util.Random;
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
     * Initialises a customer's ID and arrival turn, starting in WAITING_IN_QUEUE state with satisfaction 100
     */
    public Customer(int customerID, int arrivalTurn){
        this.customerID = customerID;
        this.arrivalTurn = arrivalTurn;
        this.state = CustomerState.WAITING_IN_QUEUE;
    }
    /**
     * getter method to return arrival turns
     */
    public int getArrivalTurn() {
        return arrivalTurn;
    }

    /**
     * getter method to return customer ID
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * getter method to return customer satisfaction
     */
    public int getSatisfaction() {
        return satisfaction;
    }

    /**
     * getter method to return customer state
     */
    public CustomerState getState() {
        return state;
    }

    /**
     * setter method to set customer state
     */
    public void setState(CustomerState state) {
        this.state = state;
    }

    /**
     * getter method to return current Order
     */
    public Order getCurrentOrder() {
        return currentOrder;
    }

    /**
     * setter method to set the current order
     */
    public void setCurrentOrder(Order order) {
        this.currentOrder = order;
    }

    /**
     * Deducts the subclass-assigned penalty while the customer is in WAITING_IN_QUEUE, SEATED or ORDERED.
     * Since minimum is 0, we take max (0 or satisfaction - penalty)
     * If satisfaction is 0, we set the customer state to ABANDONED
     * Then, restaurant cleanup is performed separately.
     * Customers in other states are unchanged.
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
     * Selects food using customer ID modulo menu size.
     * The default rule assumes a non-negative customer ID and a non-empty menu;
     * subclasses may override it to provide another preference depending on their rule.
     */
    public MenuItem chooseMenuItem(Menu menu){
        int index = customerID % menu.getItems().size();
        return menu.getItemByIndex(index);
    }

    /**
     * customer starts eating after order has been marked SERVED.
     * Records the service turn so eating cannot complete during that same turn.
     * customer cannot eat until order is served otherwise throw exception
     *
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
     * Reduces remaining eating time by 1 when the customer is EATING and the supplied turn is later than the service turn.
     * The caller invokes this once per game turn
     *
     */
    public void decrementEatingTurns(int currentTurn){
        if (currentTurn > servedTurn) {
            if (state == CustomerState.EATING && eatingTurnsLeft > 0) {
                eatingTurnsLeft--;
            }
        }
    }
    /**
     * Checks whether a customer in EATING state has no eating turns remaining.
     */
    public boolean isFinishedEating(){
        return state == CustomerState.EATING && eatingTurnsLeft == 0;
    }

    /**
     * Checks whether the customer's state is ABANDONED.
     */
    public boolean isAbandoned(){
        return (state == CustomerState.ABANDONED);
    }

    /**
     * The default is return null because regualar customers pay full price.
     * Subclasses may override to provide a new policy. As we will see, VIP Customer has their own policy where food price has 10% off.
     */
    public DiscountPolicy getDiscountPolicy() {
        return null;
    }
}
