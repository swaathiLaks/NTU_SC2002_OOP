package control;

import boundary.CustomerView;
import boundary.GameSnapshot;
import boundary.TableView;
import entity.Customer;
import entity.CustomerState;
import entity.GameResult;
import entity.Order;
import entity.Pricing;
import entity.Restaurant;
import entity.Statistics;
import staff.Cashier;
import staff.Chef;
import staff.StaffAction;
import staff.Waiter;

import java.util.ArrayList;
import java.util.List;

// Runs the six-step turn sequence. The UI calls, in order: startTurn() -> waiter action -> chef action -> endTurn()
// The Waiter always acts before the Chef, so a dish finished this turn is served next turn at the earliest
public class GameController {
    private final GameConfig config;
    private final Restaurant restaurant;
    private final Waiter waiter = new Waiter();
    private final Chef chef = new Chef();
    private final Cashier cashier = new Cashier();
    private final ArrivalSchedule arrivals = new ArrivalSchedule();
    private final ResultEvaluator evaluator = new ResultEvaluator();
    private boolean waiterActed, chefActed;
    // 0 before the first turn starts
    private int currentTurn;

    // config: game settings and win conditions
    public GameController(GameConfig config) {
        this.config = config;
        this.restaurant = new Restaurant(config.tableCount(), config.seed());
    }

    // Step 1: advances the turn and adds any arriving customer to the queue
    // return: arrival messages for display
    public List<String> startTurn() {
        currentTurn++;
        restaurant.startTurn(currentTurn);
        waiterActed = false;
        chefActed = false;
        List<String> events = new ArrayList<>();
        arrivals.customerArrivingOn(currentTurn).ifPresent(c -> {
            restaurant.addToQueue(c);
            // Changed in Stage 2: arrival message includes the customer type
            events.add("C" + c.getId() + " (" + c.typeLabel() + ") arrives and joins the queue (satisfaction "
                    + c.getSatisfaction() + ").");
        });
        return events;
    }

    // Steps 2-3: the manager picks one action for each staff member
    public List<StaffAction> waiterOptions() { return waiter.availableActions(restaurant); }
    // Returns the chef's available actions this turn
    public List<StaffAction> chefOptions() { return chef.availableActions(restaurant); }

    // Executes the chosen waiter action
    public ActionResult applyWaiterAction(StaffAction a) {
        if (waiterActed) return ActionResult.failure("The Waiter has already acted this turn.");
        ActionResult r = a.execute(restaurant);
        if (r.ok()) waiterActed = true;
        return r;
    }
    // Executes the chosen chef action
    public ActionResult applyChefAction(StaffAction a) {
        if (chefActed) return ActionResult.failure("The Chef has already acted this turn.");
        ActionResult r = a.execute(restaurant);
        if (r.ok()) chefActed = true;
        return r;
    }
    // Steps 4-5: eating, payment, waiting penalties and abandonment
    // return: end-of-turn messages for display
    public List<String> endTurn() {
        List<String> events = new ArrayList<>();
        events.addAll(processEating());
        events.addAll(processPayment());
        events.addAll(applyWaitingPenalties());
        return events;
    }

    // A customer served on an earlier turn finishes eating and becomes READY_TO_PAY
    private List<String> processEating() {
        List<String> events = new ArrayList<>();
        for (Customer c : restaurant.getActiveCustomers()) {
            if (c.getState() == CustomerState.SERVED && c.getServedTurn() < currentTurn) {
                restaurant.markReadyToPay(c);
                events.add("C" + c.getId() + " finishes eating: READY_TO_PAY.");
            }
        }
        return events;
    }

    // The Cashier handles at most one customer per turn, oldest first
    private List<String> processPayment() {
        return cashier.collectPayment(restaurant, currentTurn)
                .map(c -> List.of(String.format("Cashier collects %s from C%d: PAID, table freed. Satisfaction %d recorded.",
                        Pricing.format(c.getOrder().getPriceCents()), c.getId(), c.getSatisfaction())))
                .orElse(List.of());
    }

    // Everyone still waiting for a table or food loses satisfaction; at 0 they leave without paying
    // Changed in Stage 2: Restaurant applies the penalties, the controller only reports who left
    private List<String> applyWaitingPenalties() {
        List<String> events = new ArrayList<>();
        for (Customer c : restaurant.applyWaitingPenalties())
            events.add("C" + c.getId() + " reached 0 satisfaction and leaves without paying.");
        return events;
    }

    // Returns true once the final turn has been played
    public boolean isGameOver() { return currentTurn >= config.maxTurns(); }

    // Returns VICTORY or DEFEAT based on the final stats
    public GameResult getResult() { return evaluator.evaluate(restaurant.getStats(), config); }

    // Returns a read-only copy of the current state for the UI
    public GameSnapshot snapshot() {
        Statistics s = restaurant.getStats();
        List<CustomerView> queue = restaurant.getWaitingQueue().stream().map(GameController::view).toList();
        List<TableView> tables = restaurant.getTables().stream()
                .map(t -> new TableView(t.getId(), t.getOccupant().map(GameController::view)))
                .toList();
        return new GameSnapshot(currentTurn, config.maxTurns(), s.getRevenueCents(), s.averageSatisfaction(),
                s.getServedCount(), s.getUnhappyDepartures(), queue, tables);
    }

    // Returns the game settings
    public GameConfig getConfig() { return config; }

    // Converts a customer to a read-only view, including the customer type; order field are null if no order yet
    private static CustomerView view(Customer c) {
        Order o = c.getOrder();
        // Changed in Stage 2: also passes the customer type to the view
        return new CustomerView(c.getId(), c.typeLabel(), c.getSatisfaction(), c.getState(),
                o == null ? null : o.getItem().getName(), o == null ? null : o.getStatus());
    }
}