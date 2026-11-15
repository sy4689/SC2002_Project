package entity.Event;

import java.util.List;

import entity.customer.Customer;
import entity.DiscountPolicy.DiscountPolicy;

public abstract class Event {    // One activation allowed per game.
    // Inactive until activate() is called.
    private int countUsage = 1;
    private int turnsRemaining = 0;

    /**
     * Performs this event's turn-start actions for the supplied customers.
     * Implementations must have no effect while inactive and must not activate the event or decrement its duration here.
     * @param customers non-null customers to consider for this turn's effects
     */
    public abstract void applyEventAction(List<Customer> customers);

    /**
     * Supplies this event's pricing policy.
     * The default leaves the price unchanged.
     * The caller checks event activity before applying the policy.
     * @return a non-null pricing policy
     */
    public DiscountPolicy getDiscountPolicy() {
        return priceInCents -> priceInCents;
    }

    /**
     * Uses the event's single activation and starts a two-turn active duration.
     * @return true after successful activation
     * @throws IllegalStateException if the event has already used its activation
     */
    public boolean activate() {
        if (countUsage == 0) {
            throw new IllegalStateException(
                    "Already activated, cannot activate anymore");
        }

        countUsage--;
        turnsRemaining = 2;
        return true;
    }

    /**
     * Checks whether the event has any active turns remaining.
     * @return true when the remaining duration is positive; false otherwise
     */
    public boolean isActive() {
        return turnsRemaining > 0;
    }

    /**
     * Checks whether the event still has an unused activation.
     * @return true when activation remains available; false after it has been used
     */
    public boolean canActivate() {
        return countUsage > 0;
    }

    /**
     * Returns the number of active turns remaining.
     *
     * @return the remaining duration, or zero when inactive
     */
    public int getTurnsRemaining() {
        return turnsRemaining;
    }

    /**
     * Reduces the remaining duration by one when it is positive, leaving zero unchanged.
     *
     * The controller calls this at turn end after the event's effects for that turn have
     * been applied.
     */
    public void decrementDuration() {
        if (turnsRemaining > 0) {
            turnsRemaining--;
        }
    }
}