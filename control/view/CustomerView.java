package control.view;

/**
 * Read-only snapshot of a customer for the UI.
 * State and type are stored as Strings so the boundary layer never needs to
 * import entity classes
 */
public final class CustomerView {
    private final int id;
    private final int satisfaction;
    private final String state;
    // Update new attribute : Customer Type for stage 2
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

