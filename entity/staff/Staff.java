package entity.staff;

/**
 * Abstract class describing a general template for staff members in the restaurant
 */
public abstract class Staff {
    private String name;
    private boolean busyThisTurn = false;

    /**
     * Initialises a staff member's name with its task allowance available.
     *
     * @param name staff name used in task-limit error messages
     */
    public Staff(String name) {
        this.name = name;
    }

    /**
     * Restores this staff member's task allowance for a new turn.
     * The controller calls this at turn start.
     */
    public void resetTurnStatus() {
        busyThisTurn = false;
    }

    /**
     * Checks whether this staff member has used its current turn's task.
     *
     * @return true if the task allowance remains available; false otherwise
     */
    public boolean isAvailable(){
        return (!busyThisTurn);
    }

    /**
     * Consumes this staff member's current task allowance.
     * The allowance remains used until resetTurnStatus is called.
     *
     * @throws IllegalStateException if a task has already been consumed since
     *         the last reset
     */
    protected void markBusy() {
        if (busyThisTurn) {
            throw new IllegalStateException(name + "has already executed task this turn.");
        }
        busyThisTurn = true;
    }

}
