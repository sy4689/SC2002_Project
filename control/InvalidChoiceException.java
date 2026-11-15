package control;

/**
 * Checked exception thrown when the player picks an option that is not
 * allowed (e.g. a table that is not free). The GameController catches it and
 * asks the player to choose again.
 */
public class InvalidChoiceException extends Exception {
    public InvalidChoiceException(String msg) {
        super(msg);
    }
}
