public class Customer {
    public Customer(int customerID, int arrivalTurn) {
        this.customerID = customerID;
        this.arrivalTurn = arrivalTurn;
        this.satisfaction = 100;
        this.state = CustomerState.WAITING_IN_QUEUE;
        this.eatingTurnsLeft = 1;
    }
}
