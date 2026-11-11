package entity.customer;

import entity.DiscountPolicy.DiscountPolicy;
import entity.DiscountPolicy.VIPDiscountPolicy;

public class VIPCustomer extends Customer {

    public VIPCustomer(int customerID, int arrivalTurn) {

        super(customerID, arrivalTurn);
        penalty=5;
    }

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
    @Override
    public DiscountPolicy getDiscountPolicy() {
        DiscountPolicy policy = new VIPDiscountPolicy();
        return policy;
    }
}
