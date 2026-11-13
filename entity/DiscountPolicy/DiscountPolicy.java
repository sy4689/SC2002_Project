package entity.DiscountPolicy;

/**
 * Interface to implement contract for discount policy
 */
public interface DiscountPolicy {

    /**
     * Abstract method to force implementing class to implement method calculating the
     * amount payable after applying a custom pricing policy.
     *
     * Note that implementations define their rounding rule.
     *
     * @param priceInCents incoming price in cents
     * @return payable amount in cents after this policy
     */
    public int applyDiscount(int priceInCents);
}
