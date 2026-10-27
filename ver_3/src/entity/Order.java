package entity;

// A customer's order, tracked from placed through cooking to served and paid
public class Order {
    private final int id;
    private final Customer customer;
    private final MenuItem item;
    // Locked when the order is taken
    private final int priceCents;   
    // Prep units left before READY
    private int remainingUnits;
    private OrderStatus status = OrderStatus.PLACED;

    // id: order number
    // customer: customer who placed the order
    // item: dish ordered
    // priceCents: price the customer will pay
    Order(int id, Customer customer, MenuItem item, int priceCents) {
        this.id = id;
        this.customer = customer;
        this.item = item;
        this.priceCents = priceCents;
        this.remainingUnits = item.getPrepUnits();
    }

    // Does one preparation unit. Returns true if the order just became READY
    boolean advance() {
        if (!isInKitchen()) throw new IllegalStateException("Order " + id + " is not being prepared");
        remainingUnits--;
        status = remainingUnits == 0 ? OrderStatus.READY : OrderStatus.COOKING;
        return status == OrderStatus.READY;
    }

    // Marks a READY order as delivered to the customer
    void markServed() {
        if (status != OrderStatus.READY) throw new IllegalStateException("Order " + id + " is not READY");
        status = OrderStatus.SERVED;
    }

    // Marks the order as paid by the cashier
    void markPaid() { status = OrderStatus.PAID; }

    // Cancels an unfinished order. Served or paid orders are left alone
    void cancel() {
        if (status != OrderStatus.SERVED && status != OrderStatus.PAID) status = OrderStatus.CANCELLED;
    }

    // True while the chef can still work on it (PLACED or COOKING)
    public boolean isInKitchen() { return status == OrderStatus.PLACED || status == OrderStatus.COOKING; }

    // Getters
    public int getId() { return id; }
    public Customer getCustomer() { return customer; }
    public MenuItem getItem() { return item; }
    public OrderStatus getStatus() { return status; }
    public int getPriceCents() { return priceCents; }
    public int getRemainingUnits() { return remainingUnits; }
}
