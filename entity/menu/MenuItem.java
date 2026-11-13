package entity.menu;

public interface MenuItem {

    /**
     * getter method to return Food name
     */
    public String getFoodName();

    /**
     * getter method to return the food price in cents
     */
    public int getPriceInCents();

    /**
     * getter method to return the food preparation units
     */
    public int getPrepUnits();
}
