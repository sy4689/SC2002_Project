package entity.menu;

/**
 * Enumeration describe all single food item
 */
public enum MenuItemType implements MenuItem {
    SALAD("Salad",900,1),
    BURGER("Burger",1500,1),
    PASTA("Pasta", 1800,2);

    private final String foodName;
    private final int priceInCents;
    private final int prepUnits;

    /**
     * Initialises the display name, base price and preparation work for one
     * menu enum constant.
     *
     * @param name display name of the dish
     * @param priceInCents base price of the dish in integer cents
     * @param prepUnits preparation units required to complete the dish
     */
    MenuItemType(String name, int priceInCents, int prepUnits){
        foodName = name;
        this.priceInCents = priceInCents;
        this.prepUnits = prepUnits;
    }

    /**
     * Returns the display name stored for this dish.
     *
     * @return the dish's food name
     */
    public String getFoodName() { return foodName; }

    /**
     * Returns the base price stored for this dish before customer discounts.
     *
     * @return the dish's price in integer cents
     */
    public int getPriceInCents(){
        return priceInCents;
    }

    /**
     * Returns the preparation work stored for this dish.
     *
     * @return the dish's required preparation units
     */
    public int getPrepUnits() {
        return prepUnits;
    }
}
