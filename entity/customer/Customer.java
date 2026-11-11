package entity.customer;

import entity.order.Order;
import entity.order.OrderStatus;
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


    public Customer(int customerID, int arrivalTurn){
        this.customerID = customerID;
        this.arrivalTurn = arrivalTurn;
        this.state = CustomerState.WAITING_IN_QUEUE;
    }

    public int getArrivalTurn() {
        return arrivalTurn;
    }

    public int getCustomerID() {
        return customerID;
    }
    public int getSatisfaction() {
        return satisfaction;
    }

    public CustomerState getState() {
        return state;
    }

    public void setState(CustomerState state) {
        this.state = state;
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order order) {
        this.currentOrder = order;
    }

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

    public MenuItem chooseMenuItem(Menu menu){
        int index = customerID % menu.getItems().size();
        return menu.getItemByIndex(index);
    }

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
    public void decrementEatingTurns(int currentTurn){
        if (currentTurn > servedTurn) {
            if (state == CustomerState.EATING && eatingTurnsLeft > 0) {
                eatingTurnsLeft--;
            }
        }
    }

    public boolean isFinishedEating(){
        return state == CustomerState.EATING && eatingTurnsLeft == 0;
    }

    public boolean isAbandoned(){
        return (state == CustomerState.ABANDONED);
    }


}
