package entity.DiscountPolicy;

/**
 * Class to describe discount policy for VIP customer
 */
public class VIPDiscountPolicy implements  DiscountPolicy{

    private static final double DISCOUNT_RATE = 0.10;

    /**
     * Calculate discounted price by applying VIP customer-specific discount
     * formula to original menu price
     *
     *  @param priceInCents incoming price in integer cents
     *  @return the discounted payable amount in integer cents
     */
    @Override
    public int applyDiscount(int priceInCents) {
        return (int) Math.round(
                priceInCents * (1.0 - DISCOUNT_RATE));
    }
}
