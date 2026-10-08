package staff;

import control.ActionResult;
import entity.Restaurant;

/** One task a staff member can do this turn. A failed execute() changes no state. */
public interface StaffAction {
    String describe();
    ActionResult execute(Restaurant r);
}
