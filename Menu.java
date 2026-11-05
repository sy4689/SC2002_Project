import java.util.List;

public class Menu {
    private List<MenuItem> items;

    public Menu(){
        items = List.of(
                new MenuItem("Salad", 900, 1),
                new MenuItem("Burger", 1500, 1),
                new MenuItem("Pasta", 1800, 2)
        );

    }

    public List<MenuItem> getItems(){
        return items;
    }

    public MenuItem getItembyIndex(int index){
        return items.get(index);
    }
}
