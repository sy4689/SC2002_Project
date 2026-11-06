public class Table {
    private int tableID;
    private boolean isOccupied = false;
    private Customer currentCustomer;

    public Table (int tableID){
        this.tableID = tableID;
    }

    public int getTableID() {
        return tableID;
    }

    public Customer getCurrentCustomer(){
        return currentCustomer;
    }

    public boolean isOccupied(){
        return isOccupied;
    }
    public void assignCustomer(Customer cust) {
        //check whether customer exists
        if (cust == null){
            throw new IllegalArgumentException("Customer cannot be null");
        }
        //check whether table is available
        if (isOccupied){
            throw new IllegalStateException("Table is already occupied");
        }
        //assign customer to this table
        currentCustomer = cust;
        isOccupied = true;
        //update customer state
        cust.setState(CustomerState.SEATED);
    }

    public Customer releaseTable() {
        if (currentCustomer.getState() == CustomerState.PAID_AND_LEFT || currentCustomer.getState() == CustomerState.ABANDONED) {

            Customer customer_left = currentCustomer;
            currentCustomer = null;
            isOccupied = false;
            return customer_left;
        }

        return null;
    }

}
