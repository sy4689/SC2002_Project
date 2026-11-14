package entity.menu;

/**
 * Interface to implement contract for any food item to offer access
 * to their particular food description
 */
public interface MenuItem {

    /**
     * Returns the food name used when displaying this item.
     *
     * @return the item's display name
     */
    public String getFoodName();

    /**
     * Returns the item's base price.
     *
     * @return the base price in integer cents
     */
    public int getPriceInCents();

    /**
     * Returns the preparation work required to complete this item.
     *
     * @return the number of required preparation units
     */
    public int getPrepUnits();
}
