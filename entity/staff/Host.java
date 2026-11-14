package entity.staff;

import entity.Table;
import entity.customer.Customer;
import entity.customer.CustomerState;


public class Host extends Staff implements Seater{

    /**
     * Creates a Host with the supplied name and an initially available task allowance.
     *
     * @param name staff name used in task-limit messages
     */
    public Host(String name){
        super(name);
    }

    /**
     * Seats a queued customer at a free table and consumes this turn's Host task.
     *
     * Table assignment changes the customer to SEATED. The caller removes the customer from the waiting queue after seating succeeds.
     *
     * @param cust non-null customer in WAITING_IN_QUEUE state
     * @param t non-null, unoccupied target table
     * @return true after successful table assignment
     * @throws IllegalArgumentException if the customer or table is null
     * @throws IllegalStateException if the Host has already acted, the table is occupied or
     *     the customer is not waiting in the queue
     */
    @Override
    public boolean seatCustomer(Customer cust, Table t) {
        if (cust == null || t == null) {
            throw new IllegalArgumentException("Customer and table must be provided. Can't seat a customer");
        }

        if (t.isOccupied()) {
            throw new IllegalStateException("Table chosen is already occupied. Can't seat a customer");
        }

        if (cust.getState() != CustomerState.WAITING_IN_QUEUE) {
            throw new IllegalStateException("Only a customer waiting in the queue can be seated.");
        }

        markBusy();
        t.assignCustomer(cust);
        return true;
    }

    /**
     * Consumes the Host's task allowance without seating a customer.
     *
     * @throws IllegalStateException if the Host has already used its task this turn
     */
    public void waitTurn() {
        markBusy();
    }

}
