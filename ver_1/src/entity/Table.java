package entity;

import java.util.Optional;

public class Table {
    private final int id;
    private Customer occupant;

    public Table(int id) { this.id = id; }

    public int getId() { return id; }
    public boolean isFree() { return occupant == null; }
    public Optional<Customer> getOccupant() { return Optional.ofNullable(occupant); }

    public void seat(Customer c) {
        if (!isFree()) throw new IllegalStateException("Table " + id + " is occupied");
        occupant = c;
    }

    public void free() { occupant = null; }
}
