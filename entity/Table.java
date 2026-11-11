package entity;

import entity.customer.Customer;
import entity.customer.CustomerState;

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
        if (cust == null) {
            throw new IllegalArgumentException("Customer must be provided to be assigned to a table.");
        }

        if (isOccupied) {
            throw new IllegalStateException("Table chosen is already occupied. Can't assign customer to table.");
        }
    
        if (cust.getState() != CustomerState.WAITING_IN_QUEUE) {
            throw new IllegalStateException("Only a customer waiting in the queue can be assigned.");
        }
    
        currentCustomer = cust;
        isOccupied = true;
        cust.setState(CustomerState.SEATED);
    }

    public Customer releaseTable() {
        if (currentCustomer == null) {
            return null;
        }
        if (currentCustomer.getState() == CustomerState.PAID_AND_LEFT || currentCustomer.getState() == CustomerState.ABANDONED) {

            Customer customer_left = currentCustomer;
            currentCustomer = null;
            isOccupied = false;
            return customer_left;
        }

        return null;
    }

}
