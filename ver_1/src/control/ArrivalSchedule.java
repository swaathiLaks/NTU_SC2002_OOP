package control;

import entity.Customer;
import entity.RegularCustomer;

import java.util.Optional;
import java.util.Set;

public class ArrivalSchedule {
    private final Set<Integer> arrivalTurns = Set.of(1, 4, 7, 10, 13, 16);
    private int nextCustomerId = 1;

    public Optional<Customer> customerArrivingOn(int turn) {
        if (!arrivalTurns.contains(turn)) return Optional.empty();
        return Optional.of(new RegularCustomer(nextCustomerId++));
    }
}
