package entity.customer;

public class RegularCustomer extends Customer{
    public RegularCustomer(int customerID, int arrivalTurn) {
        super(customerID, arrivalTurn);
        penalty = 8;
    }
}
