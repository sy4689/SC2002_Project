package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import entity.customer.Customer;

/**
 * Class describing common operation of a waiting queue in a restaurant
 */
public class WaitingQueue {
    private final List<Customer> custList = new ArrayList<>();

    /**
     * Adds the supplied customer to the end of the waiting collection.
     *
     * @param cust customer joining the waiting queue
     */
    public void enqueueCustomer(Customer cust) {
        custList.add(cust);
    }

    /**
     * Removes the specified customer if present, regardless of its position.
     * Does not change customer state or table occupancy.
     *
     * @param cust customer to remove
     * @return true if an entry was removed; false if the customer was absent
     */
    public boolean dequeueCustomer(Customer cust) {
        return custList.remove(cust);
    }

    /**
     * Searches the waiting collection for a customer with the supplied ID.
     *
     * @param customerID identifier of the customer to find
     * @return the first matching customer, or null if no entry matches
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
     * Checks whether any customer is waiting in this collection.
     *
     * @return true if the waiting collection has no entries; false otherwise
     */
    public boolean isEmpty() {
        return custList.isEmpty();
    }

    /**
     * Returns the number of entries in the waiting collection.
     *
     * @return the number of queued customers
     */
    public int size() {
        return custList.size();
    }

    /**
     * Returns an unmodifiable view of the waiting collection in insertion order.
     * This prevents list edits through the view, but does not freeze customer state.
     *
     * @return an unmodifiable view of queued customers
     */
    public List<Customer> getCustomers() {
        return Collections.unmodifiableList(custList);
    }
}
