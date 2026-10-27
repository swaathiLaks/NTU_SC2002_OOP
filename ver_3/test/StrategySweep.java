import boundary.GameSnapshot;
import control.GameConfig;
import control.GameController;
import staff.StaffAction;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

// New file added in Stage 3
// Plays a whole game through GameController with a fixed, scripted manager, so the table in docs/test-notes.md
// can be reproduced. It also writes the menu numbers it chose, which is how docs/victory-input.txt and
// docs/defeat-input.txt were made. Not part of the game itself.
//
// Usage: java -cp out:out-test StrategySweep <happyHourTurn> <useHost> [inputFileToWrite]
//   happyHourTurn: turn to start Happy Hour on, 0 = never, -1 = everyone always chooses Wait (the defeat run)
//   useHost:       true = the Host seats a queued customer whenever possible, false = the Host always waits
// Scripted manager: Waiter serves a READY order first, else takes an order, else seats; Chef works the oldest order.
public class StrategySweep {
    public static void main(String[] args) throws Exception {
        int hhTurn = Integer.parseInt(args[0]);
        boolean useHost = Boolean.parseBoolean(args[1]);
        boolean allWait = hhTurn == -1;
        GameController game = new GameController(GameConfig.base());
        StringBuilder inputs = new StringBuilder();
        int paid = 0;
        while (!game.isGameOver()) {
            game.startTurn();
            int turn = game.snapshot().turn();
            if (game.canActivateHappyHour()) { // the prompt only appears while Happy Hour is unused
                boolean start = turn == hhTurn;
                inputs.append(start ? "2\n" : "1\n");
                if (start) game.activateHappyHour();
            }
            List<StaffAction> host = game.hostOptions();
            int h = allWait ? host.size() : (useHost && host.size() > 1 ? 1 : host.size());
            inputs.append(h).append('\n');
            game.applyHostAction(host.get(h - 1));

            List<StaffAction> waiter = game.waiterOptions();
            int w = allWait ? waiter.size() : firstStarting(waiter, "Serve", "Take order", "Seat");
            inputs.append(w).append('\n');
            game.applyWaiterAction(waiter.get(w - 1));

            List<StaffAction> chef = game.chefOptions();
            int c = allWait ? chef.size() : 1;
            inputs.append(c).append('\n');
            game.applyChefAction(chef.get(c - 1));

            for (String event : game.endTurn()) if (event.startsWith("Cashier collects")) paid++;
        }
        GameSnapshot s = game.snapshot();
        System.out.printf("host=%-5s hh=%-2d paid=%d revenue=$%d.%02d avg=%s served=%d unhappy=%d -> %s%n",
                useHost, hhTurn, paid, s.revenueCents() / 100, s.revenueCents() % 100,
                s.avgSatisfaction().isPresent() ? String.format("%.2f", s.avgSatisfaction().getAsDouble()) : "N/A",
                s.servedCount(), s.unhappyDepartures(), game.getResult());
        if (args.length > 2) Files.writeString(Path.of(args[2]), inputs.toString());
    }

    // Index (1-based) of the first option starting with the first matching prefix; the last option (Wait) if none match
    private static int firstStarting(List<StaffAction> options, String... prefixes) {
        for (String prefix : prefixes)
            for (int i = 0; i < options.size(); i++)
                if (options.get(i).describe().startsWith(prefix)) return i + 1;
        return options.size();
    }
}
