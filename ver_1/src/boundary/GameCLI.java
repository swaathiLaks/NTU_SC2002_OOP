package boundary;

import control.ActionResult;
import control.GameConfig;
import control.GameController;
import entity.GameResult;
import staff.StaffAction;

import java.util.List;
import java.util.Scanner;

// Command-line boundary: shows state and collects the manager's choices. No game rules live here. 
public class GameCLI {
    private final GameController controller;
    private final InputValidator validator = new InputValidator();
    private final Scanner in;
    // controller: game controller to drive
    // in: source of player input
    public GameCLI(GameController controller, Scanner in) {
        this.controller = controller;
        this.in = in;
    }

    // Runs the main game loop until the game ends, then shows the result
    public void run() {
        GameConfig cfg = controller.getConfig();
        System.out.println("=== Restaurant Rush ===");
        System.out.printf("Goal after turn %d: at least %d paid customers, revenue at least %s, average satisfaction at least %d.%n",
                cfg.maxTurns(), cfg.minPaid(), formatMoney(cfg.minRevenueCents()), cfg.minAvgSatisfaction());

        while (!controller.isGameOver()) {
            List<String> arrivals = controller.startTurn();
            showTurnState(controller.snapshot());
            arrivals.forEach(e -> System.out.println("  * " + e));

            runStaffStep("Waiter", true);
            runStaffStep("Chef", false);

            showEndOfTurn(controller.endTurn(), controller.snapshot());
        }
        showResult(controller.getResult());
    }

    // Re-prompts until the chosen action succeeds, so a failed choice never uses up the task
    // role: display name of the staff member
    // isWaiter: true for waiter, false for chef
    private void runStaffStep(String role, boolean isWaiter) {
        while (true) {
            List<StaffAction> options = isWaiter ? controller.waiterOptions() : controller.chefOptions();
            StaffAction action = promptAction(role, options);
            ActionResult result = isWaiter ? controller.applyWaiterAction(action) : controller.applyChefAction(action);
            System.out.println((result.ok() ? "  -> " : "  !! ") + result.message());
            if (result.ok()) return;
        }
    }

    // Prints turn number, revenue, waiting queue and table occupancy
    private void showTurnState(GameSnapshot s) {
        System.out.println();
        System.out.println("==================== TURN " + s.turn() + " / " + s.maxTurns() + " ====================");
        System.out.println("Revenue: " + formatMoney(s.revenueCents()) + "   Avg satisfaction (paid): " + formatAvg(s));
        System.out.print("Waiting queue: ");
        if (s.queue().isEmpty()) System.out.println("(empty)");
        else {
            System.out.println();
            s.queue().forEach(c -> System.out.println("    " + describe(c)));
        }
        System.out.println("Tables:");
        for (TableView t : s.tables()) {
            System.out.println("    T" + t.tableId() + ": " + t.occupant().map(this::describe).orElse("free"));
        }
    }

    // Lists the options and reads a valid choice
    private StaffAction promptAction(String role, List<StaffAction> options) {
        System.out.println(role + " - choose one task:");
        for (int i = 0; i < options.size(); i++) {
            System.out.println("    " + (i + 1) + ". " + options.get(i).describe());
        }
        return options.get(validator.readChoice(in, 1, options.size()) - 1);
    }

    // Prints end-of-turn events and a summary of key stats
    private void showEndOfTurn(List<String> events, GameSnapshot s) {
        events.forEach(e -> System.out.println("  * " + e));
        System.out.printf("End of turn %d: served %d | unhappy departures %d | revenue %s | avg satisfaction %s | turns used %d/%d%n",
                s.turn(), s.servedCount(), s.unhappyDepartures(), formatMoney(s.revenueCents()), formatAvg(s),
                s.turn(), s.maxTurns());
    }

    // Prints final stats and whether the player won or lost
    private void showResult(GameResult r) {
        GameSnapshot s = controller.snapshot();
        System.out.println();
        System.out.println("==================== GAME OVER ====================");
        System.out.println("Revenue: " + formatMoney(s.revenueCents()) + "   Avg satisfaction (paid): " + formatAvg(s)
                + "   Served: " + s.servedCount() + "   Unhappy departures: " + s.unhappyDepartures());
        System.out.println(r == GameResult.VICTORY ? "*** VICTORY! ***" : "*** DEFEAT ***");
    }

    // Formats a customer as id, state, satisfaction and order (if any)
    private String describe(CustomerView c) {
        String order = c.orderItem() == null ? "" : ", order " + c.orderItem() + " " + c.orderStatus();
        return "C" + c.id() + " [" + c.state() + "] satisfaction " + c.satisfaction() + order;
    }

    // Returns average satisfaction to 1dp or "N/A" if no one has paid yet
    private String formatAvg(GameSnapshot s) {
        return s.avgSatisfaction().isPresent() ? String.format("%.1f", s.avgSatisfaction().getAsDouble()) : "N/A";
    }

    // Coverts cents to a "$x.yy" string
    private String formatMoney(int cents) {
        return String.format("$%d.%02d", cents / 100, cents % 100);
    }
}