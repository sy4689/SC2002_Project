package entity.staff;

import entity.Table;
import entity.customer.Customer;

public interface Seater {

    /**
     * Seats a queued customer at a free table using the implementing staff member's task allowance.
     * The operation updates the table and customer state. Queue removal is the caller's responsibility.
     *
     * @param cust non-null customer waiting in the queue
     * @param t non-null, unoccupied target table
     * @return true after successful table assignment
     * @throws IllegalArgumentException if the customer or table is null
     * @throws IllegalStateException if the staff task is unavailable, the table is occupied or the customer cannot be seated
     */
    public boolean seatCustomer(Customer cust, Table t);
    }
