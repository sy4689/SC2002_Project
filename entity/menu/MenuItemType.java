package entity.menu;

public enum MenuItemType implements MenuItem {
    SALAD("Salad",900,1),
    BURGER("Burger",1500,1),
    PASTA("Pasta", 1800,2);

    private final String foodName;
    private final int priceInCents;
    private final int prepUnits;

    /**
     * Initialise the food name, its price in cents and preparation unit
     */
    MenuItemType(String name, int priceInCents, int prepUnits){
        foodName = name;
        this.priceInCents = priceInCents;
        this.prepUnits = prepUnits;
    }

    /**
     * getter method to return Food name
     */
    public String getFoodName() { return foodName; }

    /**
     * getter method to return food price in cents
     */
    public int getPriceInCents(){
        return priceInCents;
    }

    /**
     * getter method to return food preparation units
     */
    public int getPrepUnits() {
        return prepUnits;
    }
}
