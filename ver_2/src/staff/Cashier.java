package staff;

import entity.Customer;
import entity.Restaurant;

import java.util.Optional;

// Collects payment automatically at the end of each turn; not controlled by the player
public class Cashier {

    // Charges the oldest READY_TO_PAY customer, if they became ready on an earlier turn.
    // Adds the bill to revenue, marks customer and order PAID, and frees the table.
    // r: the restaurant
    // currentTurn: the turn being processed
    // return: the customer who paid, or empty if no one was eligible
    public Optional<Customer> collectPayment(Restaurant r, int currentTurn) {
        Optional<Customer> next = r.peekNextToPay()
        .filter(c -> c.getReadyToPayTurn() < currentTurn);   // must wait one turn
        next.ifPresent(r::collectPayment);
        return next;
    }
}