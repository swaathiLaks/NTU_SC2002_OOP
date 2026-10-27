package control;

import entity.CriticCustomer;
import entity.Customer;
import entity.RegularCustomer;
import entity.VIPCustomer;

import java.util.Map;
import java.util.Optional;
import java.util.function.IntFunction;

// Decides which turns a new customer arrives on, which type of customer arrives and assigns customer IDs
public class ArrivalSchedule {
    // Changed in Stage 2: was a set of arrival turns that always created a regular customer
    // fixed: one arrival every 3 turns, each turn mapped to the type of customer who arrives
    // Turn 4 brings a VIP and turn 7 brings a critic, the rest are regular customers
    private final Map<Integer, IntFunction<Customer>> arrivals = Map.of(
            1, RegularCustomer::new,
            4, VIPCustomer::new,
            7, CriticCustomer::new,
            10, RegularCustomer::new,
            13, RegularCustomer::new,
            16, RegularCustomer::new);
    private int nextCustomerId = 1;

    // Returns a new customer if one is scheduled to arrive on this turn
    // turn: current turn number
    // return the new customer, or empty if no arrival this turn
    public Optional<Customer> customerArrivingOn(int turn) {
        // Changed in Stage 2: looks up the customer type instead of always creating a regular customer
        // Creates the customer type scheduled for this turn
        IntFunction<Customer> create = arrivals.get(turn);
        if (create == null) return Optional.empty();
        // IDs increase from 1
        return Optional.of(create.apply(nextCustomerId++));
    }
}
