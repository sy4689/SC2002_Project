package entity.customer;

/**
 * Entity class describe information about a regular customer
 */
public class RegularCustomer extends Customer{

    /**
     * Creates a regular customer with satisfaction 100 and 8 point for waiting penalty.
     *
     * @param customerID identifier assigned to the regular customer
     * @param arrivalTurn turn on which the customer joins the restaurant
     */
    public RegularCustomer(int customerID, int arrivalTurn) {

        super(customerID, arrivalTurn);
        penalty = 8;
    }

    /**
     * Deducts regular customer's regular penalty before he/she start eating.
     * Floors the score at zero and mark ABANDONED at zero.
     */
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
