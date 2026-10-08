package staff;

import control.ActionResult;
import entity.Restaurant;

public class WaitAction implements StaffAction {
    private final String role;

    public WaitAction(String role) { this.role = role; }

    @Override
    public String describe() { return "Wait"; }

    @Override
    public ActionResult execute(Restaurant r) { return ActionResult.success(role + " waits this turn."); }
}
