package entity;
import java.util.Random; 
import java.util.List;

// Base customer
public abstract class Customer {
    private final int id;
    // Starts full, drops while waiting
    private int satisfaction = 100;
    // New customers join the queue
    private CustomerState state = CustomerState.WAITING;
    // Null until the customer orders
    private Order order;
    private int servedTurn;
    private int readyToPayTurn;

    // id: unique customer number, starting from 1
    protected Customer(int id) { this.id = id; }

    // Satisfaction lost for each turn spent waiting for a table or food
    public abstract int waitingLoss();

    // Bill for an item with the given base price. Regular customers pay full price
    public int priceFor(int baseCents) { return baseCents; }

    // Added in Stage 2: lets the CLI show Regular, VIP or Critic
    // Name of the customer type for display, given by each subclass
    // so no other class needs to check which kind of customer it has
    public abstract String typeLabel();

    // Changed in Stage 3: picks (seeded random) from everything orderable, so combos can be chosen too
    // Regular and VIP customers use this rule; the Critic overrides it
    public MenuItem chooseItem(Menu menu, Random rng) {
        List<MenuItem> items = menu.getOrderableItems();
        return items.get(rng.nextInt(items.size()));
    }

    // Applies one turn of waiting. Returns true if satisfaction has reached 0
    boolean applyWaitingPenalty() {
        satisfaction = Math.max(0, satisfaction - waitingLoss());
        return satisfaction == 0;
    }

    // Added in Stage 3: Happy Hour gives back satisfaction, never going above 100
    // points: satisfaction to give back
    // return: how many points were actually gained
    int recoverSatisfaction(int points) {
        int before = satisfaction;
        satisfaction = Math.min(100, satisfaction + points);
        return satisfaction - before;
    }

    // True while waiting for a table or for food: these customers lose satisfaction
    public boolean isWaitingForService() {
        return state == CustomerState.WAITING || state == CustomerState.SEATED || state == CustomerState.ORDERED;
    }

    // Marks the food as delivered and records the turn, so eating finishes next turn
    void markServed(int turn) { state = CustomerState.SERVED; servedTurn = turn; }
    // Marks the customer as finished eating and records the turn, for oldest-first payment
    void markReadyToPay(int turn) { state = CustomerState.READY_TO_PAY; readyToPayTurn = turn; }
    void markSeated()        { state = CustomerState.SEATED; }
    void markOrdered(Order o){ order = o; state = CustomerState.ORDERED; }
    void markPaid()          { state = CustomerState.PAID; }
    void markLeft()          { state = CustomerState.LEFT; }
    // Getters
    public int getId() { return id; }
    public int getSatisfaction() { return satisfaction; }
    public CustomerState getState() { return state; }
    public int getServedTurn() { return servedTurn; }
    public int getReadyToPayTurn() { return readyToPayTurn; }
    public Order getOrder() { return order;}
}