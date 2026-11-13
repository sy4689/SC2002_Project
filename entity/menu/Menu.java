package entity.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Menu {
    private final List<MenuItem> items = new ArrayList<>();

    /**
     * Creates the base menu from all MenuItemType values, preserving the order: Salad, Burger, then Pasta.
     */
    public Menu() {
        for (MenuItemType type : MenuItemType.values()) {
            items.add(type);
        }
    }

    /**
     * getter method to return items in the menu
    */
    public List<MenuItem> getItems(){
        return Collections.unmodifiableList(items);
    }

    /**
     * getter method to return the menu item stored at a zero-based position.
     */
    public MenuItem getItemByIndex(int index){
        return items.get(index);
    }
}
