package entity.customer;

/**
 * The overall state of a customer during the whole game-flow
 */
public enum CustomerState {
    WAITING_IN_QUEUE,
    SEATED,
    ORDERED,
    EATING,
    WAITING_TO_PAY,
    PAID_AND_LEFT,
    ABANDONED
}
