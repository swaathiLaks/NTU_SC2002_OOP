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
// Changed in Stage 3: every public method that changes state now checks its own preconditions, so
// no caller (staff action, controller or future code) can leave the restaurant in an invalid state
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
    // Added in Stage 3: the pricing policy. Callers can read it but only Restaurant can change it
    private final HappyHour happyHour = new HappyHour();
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

    // Added in Stage 3: called once at the end of every turn so timed policies count down
    public void endTurn() { happyHour.endTurn(); }

    // Registers a new customer and adds them to the back of the queue
    // throws IllegalArgumentException: if the customer is not new or the ID is already in use
    public void addToQueue(Customer c) {
        if (c.getState() != CustomerState.WAITING || customers.containsKey(c.getId()))
            throw new IllegalArgumentException("C" + c.getId() + " cannot join the queue");
        customers.put(c.getId(), c);
        waitingQueue.add(c);
    }

    // Returns the lowest-numbered free table, or empty if all are taken
    public Optional<Table> findFreeTable() {
        return tables.stream().filter(Table::isFree).findFirst();
    }

    // Moves a customer from the queue to a table
    // Changed in Stage 3: checks first and seats before leaving the queue, so a failure changes nothing
    // throws IllegalStateException: if the customer is not queued or the table is occupied
    // throws IllegalArgumentException: if the table belongs to another restaurant
    public void seatCustomer(Customer c, Table t) {
        if (!tables.contains(t)) throw new IllegalArgumentException("Unknown table");
        if (!waitingQueue.contains(c)) throw new IllegalStateException("C" + c.getId() + " is not in the waiting queue");
        t.seat(c);
        waitingQueue.remove(c);
        c.markSeated();
    }

    // The customer chooses their own food; the price is locked here
    // Changed in Stage 3: the customer discount is applied first, then the Happy Hour discount
    // throws IllegalStateException: if the customer is not seated and still to order
    public Order placeOrder(Customer c) {
        if (c.getState() != CustomerState.SEATED)
            throw new IllegalStateException("C" + c.getId() + " is not waiting to order");
        MenuItem item = c.chooseItem(menu, rng);
        int price = happyHour.priceFor(c.priceFor(item.getPriceCents()));
        Order order = new Order(nextOrderId++, c, item, price, happyHour.isActive());
        orders.add(order);
        c.markOrdered(order);
        return order;
    }

    // Added in Stage 2: the chef does one preparation unit. Returns true if the order just became READY
    // Goes through Restaurant so Order.advance() can stay package-private
    // throws IllegalStateException: if the order is not in the kitchen
    public boolean prepare(Order order) {
        requireOwn(order);
        return order.advance();
    }

    // Delivers a READY order to its customer and counts them as served
    // throws IllegalStateException: if the order is not READY
    public void serve(Order order) {
        requireOwn(order);
        order.markServed();
        order.getCustomer().markServed(currentTurn);
        stats.recordServed();
    }

    // Marks a customer as done eating and adds them to the back of the payment queue
    // throws IllegalStateException: if the customer has not been served
    public void markReadyToPay(Customer c) {
        if (c.getState() != CustomerState.SERVED)
            throw new IllegalStateException("C" + c.getId() + " has not been served");
        c.markReadyToPay(currentTurn);
        readyToPay.addLast(c);
    }

    // Charges a customer, frees their table and records the sale
    // Changed in Stage 3: only the oldest READY_TO_PAY customer can be charged
    // throws IllegalStateException: if the customer is not the next one to pay
    public void collectPayment(Customer c) {
        if (readyToPay.peekFirst() != c)
            throw new IllegalStateException("C" + c.getId() + " is not the next customer to pay");
        c.getOrder().markPaid();
        c.markPaid();
        stats.recordPayment(c.getOrder().getPriceCents(), c.getSatisfaction());
        removeCustomer(c);
    }

    // Added in Stage 3: starts Happy Hour for this turn and the next
    // throws IllegalStateException: if it was already used this game
    public void activateHappyHour() { happyHour.activate(); }

    // Added in Stage 3: while Happy Hour is on, every customer still waiting for a table or food
    // recovers satisfaction (never above 100). Does nothing when it is off
    // return: the customers whose satisfaction actually went up
    public List<Customer> applyHappyHourRecovery() {
        List<Customer> recovered = new ArrayList<>();
        if (!happyHour.isActive()) return recovered;
        for (Customer c : getActiveCustomers()) {
            if (c.isWaitingForService() && c.recoverSatisfaction(HappyHour.RECOVERY_POINTS) > 0) recovered.add(c);
        }
        return recovered;
    }

    // Step 5: every waiting or seated customer who has not been served loses satisfaction
    // Changed in Stage 2: moved here from GameController so Customer.applyWaitingPenalty() can be package-private
    // return: the customers who reached 0 and left without paying
    public List<Customer> applyWaitingPenalties() {
        List<Customer> departed = new ArrayList<>();
        for (Customer c : getActiveCustomers()) {
            if (c.isWaitingForService() && c.applyWaitingPenalty()) {
                removeAbandoned(c);
                departed.add(c);
            }
        }
        return departed;
    }

    private void removeAbandoned(Customer c) {
        if (c.getOrder() != null) c.getOrder().cancel();
        c.markLeft();
        removeCustomer(c);
        stats.recordDeparture();
    }

    // Added in Stage 3: rejects an order that does not belong to this restaurant
    private void requireOwn(Order order) {
        if (!orders.contains(order)) throw new IllegalArgumentException("Order #" + order.getId() + " is not from this restaurant");
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
    // Added in Stage 3: read-only use only; its state can only be changed through Restaurant
    public HappyHour getHappyHour() { return happyHour; }
    public Optional<Customer> peekNextToPay() { return Optional.ofNullable(readyToPay.peekFirst()); }

}
