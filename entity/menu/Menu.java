package entity.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Menu {
    private final List<MenuItem> items = new ArrayList<>();

    public Menu() {
        for (MenuItemType type : MenuItemType.values()) {
            items.add(type);
        }
    }

    public List<MenuItem> getItems(){
        return Collections.unmodifiableList(items);
    }

    public MenuItem getItemByIndex(int index){
        return items.get(index);
    }
}
