package entity;

import entity.customer.Customer;
import entity.customer.CustomerState;

public class Table {
    private int tableID;
    private boolean isOccupied = false;
    private Customer currentCustomer;

    /**
     * Creates an empty table with the ID
     */
    public Table (int tableID){
        this.tableID = tableID;
    }

    /**
     * getter method to return table ID
     */
    public int getTableID() {
        return tableID;
    }

    /**
     * getter method to return current customer
     */
    public Customer getCurrentCustomer(){
        return currentCustomer;
    }

    /**
     * Checks whether the table is currently occupied.
     */
    public boolean isOccupied(){
        return isOccupied;
    }

    /**
     * Assigns a queued customer to this table and changes the customer's state to SEATED.
     * The caller removes the customer from the waiting queue separately.
     */
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

    /**
     * Releases the table after its customer has paid and left or abandoned.
     * Leaves the table unchanged if the customer's state does not need to release.
     */
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
