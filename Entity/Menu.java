package Entity;

import java.util.List;

public class Menu {
    private List<MenuItem> items;

    public Menu(List<MenuItem> items){
        this.items = items;
    }

    public List<MenuItem> getItems(){
        return items;
    }

    public MenuItem getItemByIndex(int index){
        return items.get(index);
    }
}
