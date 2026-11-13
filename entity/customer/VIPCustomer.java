//added VIP Customer;
//each turn -5 satisfaction from applyWaitingPenalty();
//every VIP customer has a discount policy where the price is 10% off his chosen menu item;
package entity.customer;

import entity.DiscountPolicy.DiscountPolicy;
import entity.DiscountPolicy.VIPDiscountPolicy;

public class VIPCustomer extends Customer {

    /**
     * Creates a VIP customer with satisfaction 100 and 5 point for waiting penalty.
     *
     * @param customerID identifier assigned to the regular customer
     * @param arrivalTurn turn on which the customer joins the restaurant
     */
    public VIPCustomer(int customerID, int arrivalTurn) {

        super(customerID, arrivalTurn);
        penalty = 5;
    }

    /**
     * Deducts VIP customer's regular penalty before he/she start eating.
     * Floors the score at zero and mark ABANDONED at zero.
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
     * Creates the pricing strategy that gives this VIP customer a set discount
     *
     * @return a new, non-null VIPDiscountPolicy instance
     */
    @Override
    public DiscountPolicy getDiscountPolicy() {
        DiscountPolicy policy = new VIPDiscountPolicy();
        return policy;
    }
}
