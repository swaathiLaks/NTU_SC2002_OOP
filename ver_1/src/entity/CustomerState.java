package entity;

// Stages a customer moves through, from arrival to leaving 
public enum CustomerState {
    // In the queue, no table yet
    WAITING, 
    // At a table, not ordered yet
    SEATED, 
    // Order taken, waiting for food
    ORDERED, 
    // Food delivered, eating
    SERVED, 
    // Finished eating, waiting for the cashier
    READY_TO_PAY, 
    // Paid and left happy 
    PAID, 
    // Left without paying (satisfaction hit 0)
    LEFT
}