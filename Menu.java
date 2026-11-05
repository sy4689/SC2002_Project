import java.util.List;

public class Menu {
    private List<MenuItem> items;

    public Menu(){

    }

    public List<MenuItem> getItems(){
        return items;
    }

    public MenuItem getItemByIndex(int index){
        return items.get(index);
    }
}
