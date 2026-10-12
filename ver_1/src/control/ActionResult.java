package control;

// Outcome of a staff action, shown to player by CLI
// ok: true if the action succeeded
// message: what happend, or why it failed
public record ActionResult(boolean ok, String message) {
    // Creates a successful result
    public static ActionResult success(String message) { return new ActionResult(true, message); }
    // Creates a failed result; the player is asked to choose again
    public static ActionResult failure(String message) { return new ActionResult(false, message); }
}