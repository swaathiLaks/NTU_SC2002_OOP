package entity;

// New file added in Stage 2
// VIP customer: more patient than a regular customer and gets 10% off the bill
public class VIPCustomer extends Customer {
    // id: unique customer number
    public VIPCustomer(int id) { super(id); }

    // Loses 5 satisfaction per turn spent waiting
    @Override
    public int waitingLoss() { return 5; }

    // Bill with 10% off, rounded to the nearest cent
    // Called when the order is taken, so the discounted price is locked in the order
    // baseCents: menu price in cents
    // return: discounted price in cents
    @Override
    public int priceFor(int baseCents) { return Pricing.percentOf(baseCents, 90); }

    // Label shown in the CLI
    @Override
    public String typeLabel() { return "VIP"; }
}
