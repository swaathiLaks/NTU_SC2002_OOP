package entity;

public class Order {
    private final int id;
    private final Customer customer;
    private final MenuItem item;
    private final int priceCents;   // locked when the order is taken
    private int remainingUnits;
    private OrderStatus status = OrderStatus.PLACED;

    public Order(int id, Customer customer, MenuItem item, int priceCents) {
        this.id = id;
        this.customer = customer;
        this.item = item;
        this.priceCents = priceCents;
        this.remainingUnits = item.getPrepUnits();
    }

    /** Does one preparation unit. Returns true if the order just became READY. */
    public boolean advance() {
        if (!isInKitchen()) throw new IllegalStateException("Order " + id + " is not being prepared");
        remainingUnits--;
        status = remainingUnits == 0 ? OrderStatus.READY : OrderStatus.COOKING;
        return status == OrderStatus.READY;
    }

    public void markServed() {
        if (status != OrderStatus.READY) throw new IllegalStateException("Order " + id + " is not READY");
        status = OrderStatus.SERVED;
    }

    public void markPaid() { status = OrderStatus.PAID; }

    /** Cancels an unfinished order. Served or paid orders are left alone. */
    public void cancel() {
        if (status != OrderStatus.SERVED && status != OrderStatus.PAID) status = OrderStatus.CANCELLED;
    }

    public boolean isInKitchen() { return status == OrderStatus.PLACED || status == OrderStatus.COOKING; }

    public int getId() { return id; }
    public Customer getCustomer() { return customer; }
    public MenuItem getItem() { return item; }
    public OrderStatus getStatus() { return status; }
    public int getPriceCents() { return priceCents; }
    public int getRemainingUnits() { return remainingUnits; }
}
