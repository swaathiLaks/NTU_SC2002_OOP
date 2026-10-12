package staff;

import control.ActionResult;
import entity.Restaurant;

// One task a staff member can do this turn. A failed execute() changes no state
public interface StaffAction {
    // Returns the text shown for this option in the menu
    String describe();
    // Performs the action on the restaurant
    // r: the restaurant
    // return: success with what happened, or failure with the reason
    ActionResult execute(Restaurant r);
}