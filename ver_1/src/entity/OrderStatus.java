package entity;

// Stages an order moves through, from taken to paid
public enum OrderStatus {
    // Taken by the waiter, chef hasn't started
    PLACED, 
    // Chef has started, prep units remaining
    COOKING, 
    // Cooked, waiting for the waiter to serve
    READY,
    // Delivered to the customer
    SERVED, 
    // Paid at the cashier
    PAID, 
    // Customer left before being served
    CANCELLED
}