package entity.menu;

public class ComboMeal implements MenuItem {
    private static final int COMBO_PERCENT = 90;

    private MenuItem item1;
    private MenuItem item2;

    /**
     * Creates a combo referencing the two supplied menu items.
     *
     * The component objects are shared by reference rather than copied.
     *
     * @param item1 non-null first component of the combo
     * @param item2 non-null second component of the combo
     */
    public ComboMeal (MenuItem item1, MenuItem item2){
        this.item1= item1;
        this.item2 = item2;
    }

    /**
     * Builds the combo display name from the two component names separated by a plus sign.
     *
     * @return the combined food name
     */
    @Override
    public String getFoodName(){
        return item1.getFoodName() + " + " + item2.getFoodName();
    }

    /**
     * Calculates 90 percent of the components' combined price, rounded to the nearest cent.
     *
     * @return the combo price before any customer or event discount, in integer cents
     */
    @Override
    public int getPriceInCents(){
        int combinedPrice = item1.getPriceInCents() + item2.getPriceInCents();
        int finalPrice = (int) Math.round((COMBO_PERCENT / 100.0) * combinedPrice);
        return finalPrice;
    }

    /**
     * Adds the preparation units required by both components.
     *
     * @return the total preparation units required for the combo
     */
    @Override
    public int getPrepUnits() {
        return item1.getPrepUnits() + item2.getPrepUnits();
    }

    /**
     * Returns the first component referenced by this combo.
     *
     * @return the first menu item
     */
    public MenuItem getItem1() {
        return item1;
    }

    /**
     * Returns the second component referenced by this combo.
     *
     * @return the second menu item
     */
    public MenuItem getItem2() {
        return item2;
    }
}