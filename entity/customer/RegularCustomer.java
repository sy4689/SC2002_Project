package entity.customer;

public class RegularCustomer extends Customer{
    public RegularCustomer(int customerID, int arrivalTurn) {
        super(customerID, arrivalTurn);
        penalty = 8;
    }
    @Override
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

}
