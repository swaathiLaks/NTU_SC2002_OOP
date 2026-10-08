package entity;

import java.util.List;

/** Base customer. Subclasses change behaviour by overriding waitingLoss(), priceFor() or chooseItem(). */
public abstract class Customer {
    private static final List<String> ORDER_CYCLE = List.of("Pasta", "Burger", "Salad");

    private final int id;
    private int satisfaction = 100;
    private CustomerState state = CustomerState.WAITING;
    private Order order;
    private int servedTurn;
    private int readyToPayTurn;

    protected Customer(int id) { this.id = id; }

    /** Satisfaction lost for each turn spent waiting for a table or food. */
    public abstract int waitingLoss();

    /** Bill for an item with the given base price. Regular customers pay full price. */
    public int priceFor(int baseCents) { return baseCents; }

    /** Documented rule: customers order in a fixed cycle by arrival number: C1 Pasta, C2 Burger, C3 Salad, C4 Pasta, ... */
    public MenuItem chooseItem(Menu menu) {
        String name = ORDER_CYCLE.get((id - 1) % ORDER_CYCLE.size());
        return menu.find(name).orElseThrow();
    }

    /** Applies one turn of waiting. Returns true if satisfaction has reached 0. */
    public boolean applyWaitingPenalty() {
        satisfaction = Math.max(0, satisfaction - waitingLoss());
        return satisfaction == 0;
    }

    /** True while waiting for a table or for food: these customers lose satisfaction. */
    public boolean isWaitingForService() {
        return state == CustomerState.WAITING || state == CustomerState.SEATED || state == CustomerState.ORDERED;
    }

    public void markServed(int turn) { state = CustomerState.SERVED; servedTurn = turn; }
    public void markReadyToPay(int turn) { state = CustomerState.READY_TO_PAY; readyToPayTurn = turn; }

    public int getId() { return id; }
    public int getSatisfaction() { return satisfaction; }
    public CustomerState getState() { return state; }
    public void setState(CustomerState s) { state = s; }
    public Order getOrder() { return order; }
    public void setOrder(Order o) { order = o; }
    public int getServedTurn() { return servedTurn; }
    public int getReadyToPayTurn() { return readyToPayTurn; }
}
