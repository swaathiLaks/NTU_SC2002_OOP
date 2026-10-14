package entity;

import java.util.Optional;

// A table that seats at most one customer
public class Table {
    private final int id;
    // Null when free
    private Customer occupant;

    // id: table number
    public Table(int id) { this.id = id; }

    public int getId() { return id; }
    
    // True if no one is seated here
    public boolean isFree() { return occupant == null; }

    // Returns the seated customer, or empty if free
    public Optional<Customer> getOccupant() { return Optional.ofNullable(occupant); }

    // Seats a customer at this table
    // throws IllegalStateException: if the table is already occupied
    public void seat(Customer c) {
        if (!isFree()) throw new IllegalStateException("Table " + id + " is occupied");
        occupant = c;
    }

    // Clears the table after the customer pays or leaves
    public void free() { occupant = null; }
}