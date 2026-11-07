import boundary.GameCLI;
import control.GameUI;
import control.Manager;
import entity.Restaurant;
import entity.menu.Menu;

public class Main
{
    public static void main(String[]args)
    {
        GameUI ui = new GameCLI();
        Restaurant restaurant = new Restaurant(new Menu());
        new Manager(ui, restaurant).startGame();
    }
}
