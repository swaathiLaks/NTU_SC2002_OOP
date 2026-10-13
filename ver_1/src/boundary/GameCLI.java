package boundary;

import control.ActionResult;
import control.GameConfig;
import control.GameController;
import entity.GameResult;
import staff.StaffAction;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.io.Console;
import java.util.ArrayDeque;
import java.util.Deque;

// Command-line boundary: shows state and collects the manager's choices. No game rules live here.
public class GameCLI {
    // styling
    // Override with env RUSH_ASCII=1 (force ASCII) or RUSH_UNICODE=1 (force block characters).
    private static final boolean UNICODE = detectUnicode();
    private static final boolean COLOR = System.getenv("NO_COLOR") == null;
    private static final int WIDTH = UNICODE ? 84 : 74;
    public static final String EXIT_MESSAGE = "Game exited early. No result recorded.";
    private static final boolean INTERACTIVE = detectInteractive();
    private static final int LOG_LINES = 8;

    private static final String CHECKER = UNICODE ? "▀▄" : "#=";
    private static final String HEAVY = UNICODE ? "━" : "=";
    private static final String FULL = UNICODE ? "█" : "#";
    private static final String EMPTY = UNICODE ? "░" : "-";
    private static final String VBAR = UNICODE ? "│" : "|";
    private static final String SPEED = UNICODE ? "▬" : "=";
    private static final String DASH = UNICODE ? "──" : "--";

    // The console's output encoding decides this: a legacy Windows code page (e.g. cp437) cannot show block characters
    private static boolean detectUnicode() {
        if (System.getenv("RUSH_ASCII") != null) return false;
        if (System.getenv("RUSH_UNICODE") != null) return true;
        if (!System.getProperty("os.name", "").toLowerCase().contains("win")) return true;
        String enc = System.getProperty("stdout.encoding"); // set by Java 19+ from the console code page
        return enc != null && enc.toUpperCase().contains("UTF");
    }

    private static boolean detectInteractive() {
        if (System.getenv("RUSH_NOCLEAR") != null) return false;
        Console c = System.console();
        if (c == null) return false;
        try { // Console.isTerminal() exists on Java 22+, where console is non-null even when piped
            return (Boolean) Console.class.getMethod("isTerminal").invoke(c);
        } catch (ReflectiveOperationException e) {
            return true; // Java 17-21: non-null console means a real terminal
        }
    }

    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";
    private static final String DIM = "\u001B[2m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String WHITE = "\u001B[97m";
    private static final String GREY = "\u001B[90m";
    // red -> yellow gradient for the banner rows (256-colour codes)
    private static final String[] FIRE = {
            "\u001B[38;5;196m", "\u001B[38;5;202m", "\u001B[38;5;208m",
            "\u001B[38;5;214m", "\u001B[38;5;220m", "\u001B[38;5;226m"};

    private static String paint(String text, String... codes) {
        if (!COLOR) return text;
        return String.join("", codes) + text + RESET;
    }

    // block font for the banner
    private static final Map<Character, String[]> FONT_UNICODE = new HashMap<>();
    private static final Map<Character, String[]> FONT_ASCII = new HashMap<>();
    static {
        FONT_ASCII.put('R', new String[]{" ____  ", "|  _ \\ ", "| |_) |", "|  _ < ", "|_| \\_\\"});
        FONT_ASCII.put('E', new String[]{" _____ ", "| ____|", "|  _|  ", "| |___ ", "|_____|"});
        FONT_ASCII.put('S', new String[]{" ____  ", "/ ___| ", "\\___ \\ ", " ___) |", "|____/ "});
        FONT_ASCII.put('T', new String[]{" _____ ", "|_   _|", "  | |  ", "  | |  ", "  |_|  "});
        FONT_ASCII.put('A', new String[]{"    _    ", "   / \\   ", "  / _ \\  ", " / ___ \\ ", "/_/   \\_\\"});
        FONT_ASCII.put('U', new String[]{" _   _ ", "| | | |", "| | | |", "| |_| |", " \\___/ "});
        FONT_ASCII.put('N', new String[]{" _   _ ", "| \\ | |", "|  \\| |", "| |\\  |", "|_| \\_|"});
        FONT_ASCII.put('H', new String[]{" _   _ ", "| | | |", "| |_| |", "|  _  |", "|_| |_|"});
        FONT_ASCII.put(' ', new String[]{"    ", "    ", "    ", "    ", "    "});
    }
    private static final Map<Character, String[]> FONT = UNICODE ? FONT_UNICODE : FONT_ASCII;

    // Renders a word into rows of big letters (6 rows in block mode, 5 in ASCII mode)
    private static String[] render(String word) {
        String[] rows = new String[FONT.get(' ').length];

        Arrays.fill(rows, "");
        for (char ch : word.toCharArray()) {
            String[] glyph = FONT.get(ch);
            for (int i = 0; i < rows.length; i++) rows[i] += glyph[i];
        }
        return rows;
    }

    // fields
    private final GameController controller;
    private final InputValidator validator = new InputValidator();
    private final Scanner in;
    private final Deque<String> log = new ArrayDeque<>();

    // controller: game controller to drive
    // in: source of player input
    public GameCLI(GameController controller, Scanner in) {
        this.controller = controller;
        this.in = in;

        if (UNICODE) {
            System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8));
        }
    }

    // Runs the main game loop until the game ends, then shows the result
    public void run() {
        try {
            playGame();
        } catch (GameExitException e) {
            System.out.println(paint("\n" + EXIT_MESSAGE, BOLD, YELLOW));
        }
    }

    private void playGame() {
        GameConfig cfg = controller.getConfig();
        printBanner(cfg);
        if (INTERACTIVE) {
            System.out.print("Press Enter to start (q to quit) > ");
            if (!in.hasNextLine() || in.nextLine().trim().equalsIgnoreCase("q")) throw new GameExitException("Player quit");
        }
        while (!controller.isGameOver()) {
            controller.startTurn().forEach(e -> emit(formatEvent(e)));
            runStaffStep("Waiter", CYAN, true);
            runStaffStep("Chef", YELLOW, false);
            controller.endTurn().forEach(e -> emit(formatEvent(e)));
            emit(endOfTurnLine(controller.snapshot()));
        }
        showResult(controller.getResult());
    }

    // Logs a line; when scrolling (not interactive) it is also printed straight away
    private void emit(String line) {
        log.addLast(line);
        while (log.size() > LOG_LINES) log.removeFirst();
        if (!INTERACTIVE) System.out.println(line);
    }

    private void clearScreen() {
        if (!INTERACTIVE) return;
        System.out.print("\u001B[H\u001B[2J\u001B[3J");
        System.out.flush();
    }
    // banner
    private void printBanner(GameConfig cfg) {
        String checker = CHECKER.repeat(WIDTH / CHECKER.length());
        System.out.println();
        System.out.println(paint(checker, WHITE));
        System.out.println();

        System.out.println("Ver. 1");
        String[] top = render("RESTAURANT");
        for (int i = 0; i < top.length; i++) {
            int shade = top.length > 1 ? i * (FIRE.length - 1) / (top.length - 1) : 0;
            System.out.println(paint(top[i], FIRE[shade]));
        }

        // RUSH sits on the right with speed lines trailing in from the left
        String[] rush = render("RUSH");
        int pad = Math.max(0, WIDTH - rush[0].length());
        for (int i = 0; i < rush.length; i++) {
            String lead;
            if (i % 2 == 0) {
                int gap = i * 3 + 2;
                int dashes = Math.max(0, pad - gap - 1);
                lead = " ".repeat(gap) + paint(SPEED.repeat(dashes), GREY) + " ";
            } else {
                lead = " ".repeat(pad);
            }
            System.out.println(lead + paint(rush[i], BOLD, WHITE));
        }

        System.out.println();
        System.out.println(paint(checker, WHITE));
        System.out.println();
        System.out.println(paint("       =[ ", GREY) + paint("Restaurant Rush", BOLD, WHITE)
                + paint(" - you are the manager ]", GREY));
        System.out.println(paint("+ -- --=[ ", GREY) + cfg.maxTurns() + " turns, one move per staff member each turn");
        System.out.println(paint("+ -- --=[ ", GREY) + "Goal: "
                + paint(cfg.minPaid() + "+ paid", GREEN) + " | "
                + paint(formatMoney(cfg.minRevenueCents()) + "+ revenue", GREEN) + " | "
                + paint(cfg.minAvgSatisfaction() + "+ avg satisfaction", GREEN));
        System.out.println(paint("+ -- --=[ ", GREY) + paint("Tip: a dish that is READY can only be served next turn", DIM));
        System.out.println();
    }

    // Re-prompts until the chosen action succeeds, so a failed choice never uses up the task
    // role: display name of the staff member
    // roleColor: colour used for the role label
    // isWaiter: true for waiter, false for chef
    private void runStaffStep(String role, String roleColor, boolean isWaiter) {
       while (true) {
            if (INTERACTIVE || isWaiter) showTurnState(controller.snapshot(), controller.getConfig());
            List<StaffAction> options = isWaiter ? controller.waiterOptions() : controller.chefOptions();
            StaffAction action = promptAction(role, roleColor, options);
            ActionResult result = isWaiter ? controller.applyWaiterAction(action) : controller.applyChefAction(action);
            if (result.ok()) {
                emit(paint("  [+] ", BOLD, GREEN) + result.message());
                return;
            }
            emit(paint("  [-] ", BOLD, RED) + paint(result.message(), RED));
        }
    }

    // Lists the options and reads a valid choice
    private StaffAction promptAction(String role, String roleColor, List<StaffAction> options) {
        System.out.println();
        System.out.println(paint("[" + role + "]", BOLD, roleColor) + " choose one task:");
        for (int i = 0; i < options.size(); i++) {
            String text = options.get(i).describe();
            boolean isWait = text.trim().equalsIgnoreCase("wait");
            System.out.println("    " + paint("[" + (i + 1) + "]", BOLD, CYAN) + " "
                    + (isWait ? paint(text, GREY) : text));
        }
        return options.get(validator.readChoice(in, 1, options.size()) - 1);
    }

    // state display
    // Prints turn number, revenue, waiting queue and table occupancy
    private void showTurnState(GameSnapshot s, GameConfig cfg) {
        clearScreen();
        System.out.println();
        System.out.println(paint(HEAVY.repeat(WIDTH), CYAN));
        System.out.println(paint(" TURN " + s.turn() + " / " + s.maxTurns() + "  ", BOLD, WHITE)
                + paint(bar(s.turn(), s.maxTurns(), 24), CYAN));
        System.out.println(paint(HEAVY.repeat(WIDTH), CYAN));

        boolean revOk = s.revenueCents() >= cfg.minRevenueCents();
        System.out.println(" Revenue: " + paint(formatMoney(s.revenueCents()), BOLD, revOk ? GREEN : WHITE)
                + paint(" / " + formatMoney(cfg.minRevenueCents()) + " goal", GREY)
                + "     Avg satisfaction (paid): " + formatAvgColored(s, cfg));

        System.out.println();
        System.out.println(paint(" WAITING QUEUE", BOLD, YELLOW));
        if (s.queue().isEmpty()) {
            System.out.println("    " + paint("(empty)", GREY));
        } else {
            s.queue().forEach(c -> System.out.println("    " + describe(c)));
        }

        System.out.println();
        System.out.println(paint(" TABLES", BOLD, YELLOW));
        for (TableView t : s.tables()) {
            String occupant = t.occupant().map(this::describe).orElse(paint("free", GREY));
            System.out.println("    " + paint("T" + t.tableId(), BOLD, WHITE) + paint(" " + VBAR + " ", GREY) + occupant);
        }
        System.out.println();
         if (INTERACTIVE) {
            System.out.println(paint(" RECENT EVENTS", BOLD, YELLOW));
            if (log.isEmpty()) System.out.println("    " + paint("(none yet)", GREY));
            log.forEach(l -> System.out.println("  " + l));
        }
    }

    // Prints end-of-turn events and a summary of key stats
    private String endOfTurnLine(GameSnapshot s) {
        String unhappy = paint(String.valueOf(s.unhappyDepartures()), s.unhappyDepartures() > 0 ? RED : GREEN);
        return paint("  " + DASH + " end of turn " + s.turn() + " " + DASH + " ", GREY)
                + "served " + paint(String.valueOf(s.servedCount()), GREEN)
                + paint(" | ", GREY) + "unhappy departures " + unhappy
                + paint(" | ", GREY) + "revenue " + paint(formatMoney(s.revenueCents()), GREEN)
                + paint(" | ", GREY) + "avg sat " + formatAvg(s)
                + paint(" | ", GREY) + "turns used " + s.turn() + "/" + s.maxTurns();
    }

    // Prints final stats and whether the player won or lost
    private void showResult(GameResult r) {
        clearScreen();
        GameSnapshot s = controller.snapshot();
        boolean won = r == GameResult.VICTORY;
        String color = won ? GREEN : RED;
        System.out.println();
        System.out.println(paint(CHECKER.repeat(WIDTH / CHECKER.length()), WHITE));
        System.out.println();
        System.out.println(paint("                          G A M E   O V E R", BOLD, WHITE));
        System.out.println();
        System.out.println("  Revenue: " + paint(formatMoney(s.revenueCents()), BOLD, WHITE)
                + "   Avg satisfaction (paid): " + formatAvg(s)
                + "   Served: " + s.servedCount()
                + "   Unhappy departures: " + paint(String.valueOf(s.unhappyDepartures()), s.unhappyDepartures() > 0 ? RED : GREEN));
        System.out.println();
        System.out.println(paint(won ? "                      *** V I C T O R Y ***" : "                       *** D E F E A T ***",
                BOLD, color));
        System.out.println();
        System.out.println(paint(CHECKER.repeat(WIDTH / CHECKER.length()), WHITE));
    }

    // helpers
    // Colours an event line by what it is about (cosmetic only, the wording comes from the controller)
    private String formatEvent(String e) {
        String l = e.toLowerCase();
        String tag;
        String color;
        if (l.contains("leave") || l.contains("left") || l.contains("abandon") || l.contains("unhappy") || l.contains("cancel")) {
            tag = "[-]"; color = RED;
        } else if (l.contains("pay") || l.contains("paid")) {
            tag = "[$]"; color = GREEN;
        } else if (l.contains("arrive")) {
            tag = "[*]"; color = CYAN;
        } else if (l.contains("ready")) {
            tag = "[!]"; color = YELLOW;
        } else {
            tag = "[*]"; color = BLUE;
        }
        return "  " + paint(tag, BOLD, color) + " " + e;
    }

    // Formats a customer as id, state, satisfaction bar and order (if any)
    private String describe(CustomerView c) {
        String state = String.valueOf(c.state());
        int sat = c.satisfaction();
        StringBuilder sb = new StringBuilder();
        sb.append(paint(String.format("%-4s", "C" + c.id()), BOLD, WHITE));
        sb.append(paint(String.format("%-15s", "[" + state + "]"), stateColor(state)));
        sb.append(paint(bar(sat, 100, 10), satColor(sat)));
        sb.append(paint(String.format(" %3d", sat), BOLD, satColor(sat)));
        if (c.orderItem() != null) {
            String status = String.valueOf(c.orderStatus());
            sb.append("  ").append(c.orderItem()).append(" ").append(paint(status, stateColor(status)));
        }
        return sb.toString();
    }

    // Picks a colour from a state or order-status name
    private String stateColor(String name) {
        String n = name.toUpperCase();
        if (n.contains("READY_TO_PAY")) return MAGENTA;
        if (n.contains("PAID")) return GREEN;
        if (n.contains("LEFT") || n.contains("ABANDON") || n.contains("CANCEL")) return RED;
        if (n.contains("WAIT")) return YELLOW;
        if (n.contains("SEAT") || n.contains("ORDER") || n.contains("COOK")) return CYAN;
        if (n.contains("READY")) return BOLD + GREEN;
        if (n.contains("SERVED") || n.contains("EAT")) return BLUE;
        return RESET;
    }

    // Green when happy, yellow when slipping, red when about to walk out
    private String satColor(int sat) {
        if (sat >= 70) return GREEN;
        if (sat >= 40) return YELLOW;
        return RED;
    }

    // TODO: Draws a bar such as ████████░░░░ for value out of max
    private String bar(int value, int max, int width) {
        int filled = max <= 0 ? 0 : Math.max(0, Math.min(width, Math.round((float) value * width / max)));
        return FULL.repeat(filled) + EMPTY.repeat(width - filled);
    }

    // Returns average satisfaction to 1dp or "N/A" if no one has paid yet
    private String formatAvg(GameSnapshot s) {
        return s.avgSatisfaction().isPresent() ? String.format("%.1f", s.avgSatisfaction().getAsDouble()) : "N/A";
    }

    // Same as formatAvg but green when the goal is met, yellow when not, grey for N/A
    private String formatAvgColored(GameSnapshot s, GameConfig cfg) {
        if (s.avgSatisfaction().isEmpty()) return paint("N/A", GREY);
        double avg = s.avgSatisfaction().getAsDouble();
        return paint(String.format("%.1f", avg), BOLD, avg >= cfg.minAvgSatisfaction() ? GREEN : YELLOW)
                + paint(" / " + cfg.minAvgSatisfaction() + " goal", GREY);
    }

    // Converts cents to a "$x.yy" string
    private String formatMoney(int cents) {
        return String.format("$%d.%02d", cents / 100, cents % 100);
    }
}