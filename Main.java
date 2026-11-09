import boundary.GameCLI;
import boundary.GameUI;
import control.Manager;
import entity.Restaurant;
import entity.menu.Menu;

public class Main
{
    public static void main(String[]args)
    {
        GameUI ui = new GameCLI(new Manager());
        ui.start();
    }
}