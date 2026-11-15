package entity.order;

/**
 * Enumeration storing all possible status for an order under the game-flow
 */
public enum OrderStatus {
    PENDING,
    IN_KITCHEN,
    READY,
    SERVED,
    READY_TO_PAY,
    PAID,
    ABANDONED
}
