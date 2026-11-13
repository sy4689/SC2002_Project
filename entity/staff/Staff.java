/**
 * Staff provides methods for more specific subclass like Cashier, Chef and Waiter
 * Obeys OCP for inheritance.
 *
 * The controller resets each staff object at turn start.
 * The waiter action is validated before marking itself busy.
 * Availability is per instance so that different staff members can act independently in the same turn.
 *
 *
 */

package entity.staff;

public abstract class Staff {
    private String name;
    private boolean busyThisTurn = false;

    public Staff(String name) {
        this.name = name;
    }

    public void resetTurnStatus() {
        busyThisTurn = false;
    }

    public boolean isAvailable(){
        return (!busyThisTurn);
    }

    protected void markBusy() {
        if (busyThisTurn) {
            throw new IllegalStateException(name + "has already executed task this turn.");
        }
        busyThisTurn = true;
    }

}
