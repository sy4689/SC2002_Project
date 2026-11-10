package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import entity.customer.Customer;

public class WaitingQueue {
    private final List<Customer> custList = new ArrayList<>();

    public void enqueueCustomer(Customer cust) {
        custList.add(cust);
    }

    public boolean dequeueCustomer(Customer cust) {
        if (custList.isEmpty()){
            throw new IllegalStateException("Waiting queue is empty. Can't remove any customer");
        }

        if (!custList.contains(cust)){
            throw new IllegalStateException("Customer that need be removed can't be found from the current waiting queue");
        }

        return custList.remove(cust);
    }

    public Customer findByID(int customerID) {
        for (Customer c : custList) {
            if (c.getCustomerID() == customerID) {
                return c;
            }
        }

        return null;
    }

    public boolean isEmpty() {
        return custList.isEmpty();
    }

    public int size() {
        return custList.size();
    }

    public List<Customer> getCustomers() {
        return Collections.unmodifiableList(custList);
    }
}
