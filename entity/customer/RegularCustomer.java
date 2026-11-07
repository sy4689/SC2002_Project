package entity.customer;

public class RegularCustomer extends Customer{

    private int regularPenalty = 8;

    public RegularCustomer(int customerID, int arrivalTurn) {
        super(customerID, arrivalTurn);
    }
    @Override
    public void applyWaitingPenalty() {
        if (getState() == CustomerState.WAITING_IN_QUEUE ||
                getState() == CustomerState.SEATED ||
                getState() == CustomerState.ORDERED){

            satisfaction = Math.max(0, satisfaction - regularPenalty);

            if (satisfaction == 0) {
                setState(CustomerState.ABANDONED);
            }
        }

    }

}
