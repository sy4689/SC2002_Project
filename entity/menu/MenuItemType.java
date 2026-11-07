package entity.menu;

public enum MenuItemType implements MenuItem {
    SALAD("Salad",900,1),
    BURGER("Burger",1500,1),
    PASTA("Pasta", 1800,2);

    private final String foodName;
    private final int priceInCents;
    private final int prepUnits;

    MenuItemType(String name, int priceInCents, int prepUnits){
        foodName = name;
        this.priceInCents = priceInCents;
        this.prepUnits = prepUnits;
    }

    public String getFoodName() { return foodName; }
    public int getPriceInCents(){
        return priceInCents;
    }

    public int getPrepUnits() {
        return prepUnits;
    }
}
