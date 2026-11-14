package entity;

import entity.customer.Customer;
import entity.customer.CustomerState;

/**
 * Class describing information about table in restaurant
 */
public class Table {
    private int tableID;
    private boolean isOccupied = false;
    private Customer currentCustomer;

    /**
     * Creates an empty table with the ID
     *
     * @param tableID table ID for newly added table
     */
    public Table (int tableID){
        this.tableID = tableID;
    }

    /**
     * Getter method to return the identifier assigned to this table.
     *
     * @return the table ID
     */
    public int getTableID() {
        return tableID;
    }

    /**
     * Getter method to return the customer currently assigned to this table.
     *
     * @return the assigned customer, or null when the table has no customer
     */
    public Customer getCurrentCustomer(){
        return currentCustomer;
    }

    /**
     * Checks whether the table is currently occupied.
     *
     * @return true if a customer is assigned; false otherwise
     */
    public boolean isOccupied(){
        return isOccupied;
    }

    /**
     * Assigns a queued customer to this table and changes the customer's state
     * to SEATED. The caller removes the customer from the waiting queue separately.
     *
     * @param cust customer to seat at this table
     * @throws IllegalArgumentException if the customer is null
     * @throws IllegalStateException if the table is occupied or the customer is not
     *         WAITING_IN_QUEUE
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
     * Leaves the table unchanged if the customer's state does not permit release.
     *
     * @return the departing customer, or null if the table is empty or cannot
     *         currently be released
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
