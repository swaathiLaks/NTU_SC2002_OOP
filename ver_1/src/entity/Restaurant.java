package entity;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Owns the restaurant state. Collections are private and only changed through these methods. */
public class Restaurant {
    private final List<Table> tables = new ArrayList<>();
    private final List<Customer> waitingQueue = new ArrayList<>();
    private final List<Order> orders = new ArrayList<>();
    private final Deque<Customer> readyToPay = new ArrayDeque<>();
    private final Map<Integer, Customer> customers = new LinkedHashMap<>();
    private final Menu menu = new Menu();
    private final Statistics stats = new Statistics();
    private int nextOrderId = 1;
    private int currentTurn;

    public Restaurant(int tableCount) {
        for (int i = 1; i <= tableCount; i++) tables.add(new Table(i));
    }

    public void startTurn(int turn) { currentTurn = turn; }
    public int getCurrentTurn() { return currentTurn; }

    public void addToQueue(Customer c) {
        customers.put(c.getId(), c);
        waitingQueue.add(c);
    }

    public Optional<Table> findFreeTable() {
        return tables.stream().filter(Table::isFree).findFirst();
    }

    public void seatCustomer(Customer c, Table t) {
        waitingQueue.remove(c);
        t.seat(c);
        c.setState(CustomerState.SEATED);
    }

    /** The customer chooses their own food; the price is locked here. */
    public Order placeOrder(Customer c) {
        MenuItem item = c.chooseItem(menu);
        Order order = new Order(nextOrderId++, c, item, c.priceFor(item.getPriceCents()));
        orders.add(order);
        c.setOrder(order);
        c.setState(CustomerState.ORDERED);
        return order;
    }

    public void serve(Order order) {
        order.markServed();
        order.getCustomer().markServed(currentTurn);
        stats.recordServed();
    }

    public void markReadyToPay(Customer c) {
        c.markReadyToPay(currentTurn);
        readyToPay.addLast(c);
    }

    /** Takes a paid or departing customer out of every queue and frees their table. */
    public void removeCustomer(Customer c) {
        waitingQueue.remove(c);
        readyToPay.remove(c);
        for (Table t : tables) {
            if (t.getOccupant().orElse(null) == c) t.free();
        }
    }

    /** Customers still in the restaurant, in arrival order. */
    public List<Customer> getActiveCustomers() {
        return customers.values().stream()
                .filter(c -> c.getState() != CustomerState.PAID && c.getState() != CustomerState.LEFT)
                .toList();
    }

    public List<Table> getTables() { return Collections.unmodifiableList(tables); }
    public List<Customer> getWaitingQueue() { return Collections.unmodifiableList(waitingQueue); }
    public List<Order> getOrders() { return Collections.unmodifiableList(orders); }
    public Deque<Customer> getReadyToPay() { return readyToPay; }
    public Menu getMenu() { return menu; }
    public Statistics getStats() { return stats; }
}
