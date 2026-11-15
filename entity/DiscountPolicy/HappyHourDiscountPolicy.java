package entity.DiscountPolicy;

public class HappyHourDiscountPolicy implements DiscountPolicy{
    private static final double DISCOUNT_RATE = 0.20;

    /**
     * Applies a 20 percent reduction to the supplied bill and rounds the result to the nearest cent.
     The caller decides whether Happy Hour is active and whether this policy applies to the order.
     * @param priceInCents bill before this policy, in integer cents
     * @return the discounted bill, in integer cents
     */
    @Override
    public int applyDiscount(int priceInCents) {
        return (int) Math.round(
                priceInCents * (1.0 - DISCOUNT_RATE));
    }

}

