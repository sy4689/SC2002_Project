package entity.Event;

import java.util.List;

import entity.customer.Customer;
import entity.customer.CustomerState;
import entity.DiscountPolicy.DiscountPolicy;
import entity.DiscountPolicy.HappyHourDiscountPolicy;

public class HappyHourEvent extends Event {

    private final int satisfactionBoost = 15;

    private final DiscountPolicy discountPolicy =
            new HappyHourDiscountPolicy();

    /**
     * While active, it restores 15 satisfaction to each supplied customer who is waiting in the queue, seated or ordered but unserved.
     * Recovery is capped at 100 for all customer.
     * This method does not activate the event or consume its duration; the controller schedules one call per turn.
     *
     * @param customers non-null customers to consider for this turn's recovery
     */
    public void applyTurnStartBoost(List<Customer> customers) {
        // An inactive event must not affect customers.
        if (!isActive()) {
            return;
        }

        for (Customer customer : customers) {
            CustomerState state = customer.getState();

            // Only customers still waiting for a table or food qualify.
            if (state == CustomerState.WAITING_IN_QUEUE
                    || state == CustomerState.SEATED
                    || state == CustomerState.ORDERED) {

                customer.recoverSatisfaction(satisfactionBoost);
            }
        }
    }

    // Supplies the pricing formula for orders taken while active.
    public DiscountPolicy getDiscountPolicy() {
        return discountPolicy;
    }
}