package entity.customer;

import entity.order.Order;
import entity.menu.Menu;
import entity.menu.MenuItem;
import java.util.Random;
public abstract class Customer {
    protected int satisfaction = 100;
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

    public abstract void applyWaitingPenalty();

    public MenuItem chooseMenuItem(Menu menu){
        Random random = new Random(41); //the seed ensure every game generate the same random sequence
        int index = random.nextInt(menu.getItems().size());
        return menu.getItemByIndex(index);
    }

    public void startEating(int currentTurn) {
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
        if (state == CustomerState.EATING && eatingTurnsLeft == 0){
            return true;
        }

        else return false;
    }

    public boolean isAbandoned(){
        if (state == CustomerState.ABANDONED){
            return true;
        }
        else return false;
    }


}
