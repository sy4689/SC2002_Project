//added VIP Customer;
//each turn -5 satisfaction from applyWaitingPenalty();
//every VIP customer has a discount policy where the price is 10% off his chosen menu item;
package entity.customer;

import entity.DiscountPolicy.DiscountPolicy;
import entity.DiscountPolicy.VIPDiscountPolicy;

public class VIPCustomer extends Customer {

    /**
     * Creates a VIP customer WAITING_IN_QUEUE state with satisfaction 100 and 5 point waiting penalty.
     */
    public VIPCustomer(int customerID, int arrivalTurn) {

        super(customerID, arrivalTurn);
        penalty=5;
    }

    /**
     * Deducts 5 points while the VIP customer is in WAITING_IN_QUEUE, SEATED or ORDERED.
     * Since minimum is 0, we take max (0 or satisfaction - penalty)
     * If satisfaction is 0, we set the customer state to ABANDONED
     * Then, restaurant cleanup is performed separately.
     * Customers in other states are unchanged.
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
     * Creates the pricing policy that gives VIP customer 10% off
     */
    @Override
    public DiscountPolicy getDiscountPolicy() {
        DiscountPolicy policy = new VIPDiscountPolicy();
        return policy;
    }
}
