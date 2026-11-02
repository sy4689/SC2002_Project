public class Staff {
    private String name;
    private boolean busyThisTurn = false;

    public void Staff(String name){
        this.name = name;
    }

    public void resetTurnStatus() {
        busyThisTurn = false;
    }

    public boolean isAvailable(){
        return (!busyThisTurn);
    }

}
