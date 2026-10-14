package entity;

// Standard customer: pays full price and orders by the default cycle
public class RegularCustomer extends Customer {
    // id: unique customer number
    public RegularCustomer(int id) { super(id); }

    // Losses 8 satisfaction per turn spent waiting
    @Override
    public int waitingLoss() { return 8; }

    // Added in Stage 2: required by the new typeLabel method in Customer
    // Label shown in the CLI
    @Override
    public String typeLabel() { return "Regular"; }
}