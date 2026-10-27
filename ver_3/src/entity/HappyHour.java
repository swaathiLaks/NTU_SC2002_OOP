package entity;

// New file added in Stage 3
// The Happy Hour pricing policy. It can be started once per game and lasts DURATION_TURNS turns
// (the turn it is started on and the next one). All state is private: the only way to change it is
// through package-private methods that Restaurant calls, so the CLI can read it but never switch it
public class HappyHour {
    // turns the policy stays on, counting the turn it is started on
    public static final int DURATION_TURNS = 2;
    // percentage taken off the bill of every order taken while it is on
    public static final int DISCOUNT_PERCENT = 20;
    // satisfaction recovered at the start of each active turn by customers still waiting
    public static final int RECOVERY_POINTS = 15;

    // true once it has been started, so it can never be started twice
    private boolean used;
    // active turns still to play, including the current one; 0 when it is off
    private int remainingTurns;

    // True while the policy is on
    public boolean isActive() { return remainingTurns > 0; }

    // True if it has not been started yet this game
    public boolean isAvailable() { return !used; }

    // Active turns still to play, including the current one (0 when off)
    public int getRemainingTurns() { return remainingTurns; }

    // Starts the policy for this turn and the next
    // throws IllegalStateException: if it was already used this game
    void activate() {
        if (used) throw new IllegalStateException("Happy Hour has already been used this game");
        used = true;
        remainingTurns = DURATION_TURNS;
    }

    // Called once at the end of every turn: one fewer active turn to play
    void endTurn() {
        if (remainingTurns > 0) remainingTurns--;
    }

    // Price after the Happy Hour discount, or the same price when it is off
    // cents: price before Happy Hour (after any customer discount), in cents
    int priceFor(int cents) {
        return isActive() ? Pricing.percentOf(cents, 100 - DISCOUNT_PERCENT) : cents;
    }
}
