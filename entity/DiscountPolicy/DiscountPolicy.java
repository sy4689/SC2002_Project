package entity.DiscountPolicy;

public interface DiscountPolicy {
    /**
     * Calculates the amount payable after applying this pricing policy.
     * Input and output are integer cents; the result is the price after discount,not the amount saved.
     */
    public int applyDiscount(int priceInCents);
}
