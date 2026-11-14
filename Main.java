import boundary.GameCLI;
import boundary.GameUI;
import control.GameController;

public class Main
{
    public static void main(String[]args)
    {
        GameUI ui = new GameCLI(new GameController());
        ui.start();
    }
}