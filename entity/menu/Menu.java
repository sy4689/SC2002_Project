package entity.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class describes menu list all possible food options for customer
 */
public class Menu {
    private final List<MenuItem> items = new ArrayList<>();

    /**
     * Initialize menu by listing all possible food options
     */
    public Menu() {
        for (MenuItemType type : MenuItemType.values()) {
            items.add(type);
        }
    }

    /**
     * Getter method to return an unmodifiable view of the available items in menu order.
     *
     * @return the menu's items in their indexed selection order
     */
    public List<MenuItem> getItems(){
        return Collections.unmodifiableList(items);
    }

    /**
     * Returns the menu item stored at a zero-based position.
     *
     * @param index zero-based position of the requested item
     * @return the item at the supplied index
     * @throws IndexOutOfBoundsException if the index is negative or is not less
     *         than the number of menu items
     */
    public MenuItem getItemByIndex(int index){
        return items.get(index);
    }
}
