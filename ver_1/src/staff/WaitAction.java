package staff;

import control.ActionResult;
import entity.Restaurant;

// Do-nothing action for any staff member; always succeeds
public class WaitAction implements StaffAction {
    private final String role;

    // role: staff name shown in the message, e.g. "Chef"
    public WaitAction(String role) { this.role = role; }

    // Menu text for this option
    @Override
    public String describe() { return "Wait"; }

    // Skips the turn without changing any state
    @Override
    public ActionResult execute(Restaurant r) { return ActionResult.success(role + " waits this turn."); }
}