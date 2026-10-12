package control;

import entity.Customer;
import entity.RegularCustomer;

import java.util.Optional;
import java.util.Set;

// Decides which turns a new customer arrives on and assigns customer IDs
public class ArrivalSchedule {
    // fixed: one arrival every 3 turns
    private final Set<Integer> arrivalTurns = Set.of(1, 4, 7, 10, 13, 16);
    private int nextCustomerId = 1;
    // Returns a new customer if one is scheduled to arrive on this turn 
    // turn: current turn number
    // return the new customer, or empty if no arrival this turn
    public Optional<Customer> customerArrivingOn(int turn) {
        if (!arrivalTurns.contains(turn)) return Optional.empty();
        // IDs increase from 1
        return Optional.of(new RegularCustomer(nextCustomerId++));
    }
}