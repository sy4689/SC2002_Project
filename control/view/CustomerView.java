package control.view;

public final class CustomerView {
    private final int id;
    private final int satisfaction;
    private final String state;
    private final String type;

    public CustomerView(
            int id,
            int satisfaction,
            String state,
            String type
    ) {
        this.id = id;
        this.satisfaction = satisfaction;
        this.state = state;
        this.type = type;
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

    public String getType() {return type;
    }
}
