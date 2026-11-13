package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import entity.customer.Customer;

public class WaitingQueue {
    private final List<Customer> custList = new ArrayList<>();

    /**
     * Adds new customer to the end of the waiting list.
     */
    public void enqueueCustomer(Customer cust) {
        custList.add(cust);
    }

    /**
     * Removes the specified customer if present, regardless of its position.
     * This method does not change customer state or table occupancy.
     */
    public boolean dequeueCustomer(Customer cust) {
        return custList.remove(cust);
    }

    /**
     * Searches the waiting list for a customer with the ID.
    */
    public Customer findByID(int customerID) {
        for (Customer c : custList) {
            if (c.getCustomerID() == customerID) {
                return c;
            }
        }

        return null;
    }

    /**
     * Checks whether there are any customer waiting in this list.
     */
    public boolean isEmpty() {
        return custList.isEmpty();
    }

    /**
     * Returns the number of entries in the waiting list.
     */
    public int size() {
        return custList.size();
    }


    /**
     * Returns the waiting list in insertion order.
     * This prevents list edits through the view, but does not freeze customer state.
     */
    public List<Customer> getCustomers() {
        return Collections.unmodifiableList(custList);
    }
}
