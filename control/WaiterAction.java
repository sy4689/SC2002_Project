package control;

public class WaiterAction {
    private final WaiterActionType actionType;
    private final int targetTableID;
    private final int targetCustomerID;

    private WaiterAction(WaiterActionType type, int tableID, int customerID) {
        actionType = type;
        targetTableID = tableID;
        targetCustomerID = customerID;
    }

    public static WaiterAction seat(int customerID, int tableID) {
        return new WaiterAction(WaiterActionType.SEAT_CUSTOMER, tableID, customerID);
    }

    public static WaiterAction takeOrder(int tableID) {
        return new WaiterAction(WaiterActionType.TAKE_ORDER, tableID, -1);
    }

    public static WaiterAction serve(int tableID) {
        return new WaiterAction(WaiterActionType.SERVE_ORDER, tableID, -1);
    }

    public static WaiterAction waiting() {
        return new WaiterAction(WaiterActionType.WAIT, -1,-1);
    }

    public WaiterActionType getActionType() {
        return actionType;
    }

    public int getTargetTableID() {
        return targetTableID;
    }

    public int getTargetCustomerID() {
        return targetCustomerID;
    }
}
