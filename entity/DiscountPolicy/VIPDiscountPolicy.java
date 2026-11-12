package entity.DiscountPolicy;

public class VIPDiscountPolicy implements  DiscountPolicy{

    private static final double DISCOUNT_RATE = 0.10;

    @Override
    public int applyDiscount(int priceInCents) {
        return (int) Math.round(
                priceInCents * (1.0 - DISCOUNT_RATE));
    }
}
