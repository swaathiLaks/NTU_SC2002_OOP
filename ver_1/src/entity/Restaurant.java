package entity;

import java.util.Random;  
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Owns the restaurant state. Collections are private and only changed through these methods
public class Restaurant {
    private final List<Table> tables = new ArrayList<>();
    // Customers without a table, in arrival order
    private final List<Customer> waitingQueue = new ArrayList<>();
    // Every order placed this game
    private final List<Order> orders = new ArrayList<>();
    // Oldest first for the cashier
    private final Deque<Customer> readyToPay = new ArrayDeque<>();
    // All customers by ID, in arrival order
    
    private final Map<Integer, Customer> customers = new LinkedHashMap<>();
    private final Menu menu = new Menu();
    private final Statistics stats = new Statistics();
    private final Random rng;     
    private int nextOrderId = 1;
    private int currentTurn;

    // tableCount: number of tables, numbered from 1
    public Restaurant(int tableCount, long seed) {
       this.rng = new Random(seed);
        for (int i = 1; i <= tableCount; i++) tables.add(new Table(i));
    }

    // Records the current turn, used to timestamp serving and payment
    public void startTurn(int turn) { currentTurn = turn; }
    public int getCurrentTurn() { return currentTurn; }

    // Registers a new customer and adds them to the back of the queue
    public void addToQueue(Customer c) {
        customers.put(c.getId(), c);
        waitingQueue.add(c);
    }

    // Returns the lowest-numbered free table, or empty if all are taken
    public Optional<Table> findFreeTable() {
        return tables.stream().filter(Table::isFree).findFirst();
    }

    // Moves a customer from the queue to a table
    public void seatCustomer(Customer c, Table t) {
        waitingQueue.remove(c);
        t.seat(c);
        c.markSeated();
    }

    // The customer chooses their own food; the price is locked here
    public Order placeOrder(Customer c) {
        MenuItem item = c.chooseItem(menu, rng); 
        Order order = new Order(nextOrderId++, c, item, c.priceFor(item.getPriceCents()));
        orders.add(order);
        c.markOrdered(order);
        return order;
    }

    // Delivers a READY order to its customer and counts them as served
    public void serve(Order order) {
        order.markServed();
        order.getCustomer().markServed(currentTurn);
        stats.recordServed();
    }

    // Marks a customer as done eating and adds them to the back of the payment queue
    public void markReadyToPay(Customer c) {
        c.markReadyToPay(currentTurn);
        readyToPay.addLast(c);
    }

    public void collectPayment(Customer c) {
        c.getOrder().markPaid();
        c.markPaid();
        stats.recordPayment(c.getOrder().getPriceCents(), c.getSatisfaction());
        removeCustomer(c);
    }

    public void removeAbandoned(Customer c) {
        if (c.getOrder() != null) c.getOrder().cancel();
        c.markLeft();
        removeCustomer(c);
        stats.recordDeparture();
    }

    // Takes a paid or departing customer out of every queue and frees their table
    private void removeCustomer(Customer c) {
        waitingQueue.remove(c);
        readyToPay.remove(c);
        for (Table t : tables) {
            if (t.getOccupant().orElse(null) == c) t.free();
        }
    }

    // Customers still in the restaurant, in arrival order
    public List<Customer> getActiveCustomers() {
        return customers.values().stream()
                .filter(c -> c.getState() != CustomerState.PAID && c.getState() != CustomerState.LEFT)
                .toList();
    }

    // Read-only views of state
    public List<Table> getTables() { return Collections.unmodifiableList(tables); }
    public List<Customer> getWaitingQueue() { return Collections.unmodifiableList(waitingQueue); }
    public List<Order> getOrders() { return Collections.unmodifiableList(orders); }
    public Menu getMenu() { return menu; }
    public Statistics getStats() { return stats; }
    public Optional<Customer> peekNextToPay() { return Optional.ofNullable(readyToPay.peekFirst()); }

}