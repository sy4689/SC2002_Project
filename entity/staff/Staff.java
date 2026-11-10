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
