package entity.DiscountPolicy;

public class VIPDiscountPolicy implements  DiscountPolicy{

    private static final double DISCOUNT_RATE = 0.10;

    /**
     * Calculate 90% of the original price and rounds the result to the nearest whole cent
     */
    @Override
    public int applyDiscount(int priceInCents) {
        return (int) Math.round(
                priceInCents * (1.0 - DISCOUNT_RATE));
    }
}
