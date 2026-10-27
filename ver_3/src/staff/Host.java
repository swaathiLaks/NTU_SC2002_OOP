package staff;

import entity.Restaurant;

import java.util.ArrayList;
import java.util.List;

// New file added in Stage 3
// Front door staff: seats at most one queued customer per turn, on top of whatever the Waiter does.
// The Waiter can still seat customers too. Uses the same SeatAction as the Waiter
public class Host {
    static final String NAME = "Host";

    // One SeatAction per queued customer (for the first free table), plus Wait
    // r: the restaurant
    // return: actions the player can choose for the host this turn
    public List<StaffAction> availableActions(Restaurant r) {
        List<StaffAction> actions = new ArrayList<>(SeatAction.optionsFor(NAME, r));
        // Always available, so the menu is never empty
        actions.add(new WaitAction(NAME));
        return actions;
    }
}
