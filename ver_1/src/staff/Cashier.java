package staff;

import entity.Customer;
import entity.CustomerState;
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
        // Oldest in the payment queue
        Customer c = r.getReadyToPay().peekFirst();
        // Must wait one turn
        if (c == null || c.getReadyToPayTurn() >= currentTurn) return Optional.empty();
        c.getOrder().markPaid();
        c.setState(CustomerState.PAID);
        r.getStats().recordPayment(c.getOrder().getPriceCents(), c.getSatisfaction());
        // Also removes them from the payment queue
        r.removeCustomer(c);
        return Optional.of(c);
    }
}