package staff;

import entity.Customer;
import entity.CustomerState;
import entity.Restaurant;

import java.util.Optional;

public class Cashier {

    /**
     * Charges the oldest READY_TO_PAY customer, if they became ready on an earlier turn.
     * Adds the bill to revenue, marks customer and order PAID, and frees the table.
     */
    public Optional<Customer> collectPayment(Restaurant r, int currentTurn) {
        Customer c = r.getReadyToPay().peekFirst();
        if (c == null || c.getReadyToPayTurn() >= currentTurn) return Optional.empty();
        c.getOrder().markPaid();
        c.setState(CustomerState.PAID);
        r.getStats().recordPayment(c.getOrder().getPriceCents(), c.getSatisfaction());
        r.removeCustomer(c);
        return Optional.of(c);
    }
}
