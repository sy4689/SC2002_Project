package control.view;

public final class CustomerView {
    private final int id;
    private final int satisfaction;
    private final String state;

    public CustomerView(int id, int satisfaction, String state) {
        this.id = id;
        this.satisfaction = satisfaction;
        this.state = state;
    }

    public int getId(){
        return id;
    }

    public int getSatisfaction() {
        return satisfaction;
    }

    public String getState() {
        return state;
    }
}
