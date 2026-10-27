package staff;

import entity.Order;
import entity.Restaurant;

import java.util.ArrayList;
import java.util.List;

// Kitchen staff: works one prep unit on one order per turn
public class Chef {

    // One PrepareAction per order still in the kitchen, plus Wait
    // r: the restaurant
    // return: actions the player can choose for the chef this turn
    public List<StaffAction> availableActions(Restaurant r) {
        List<StaffAction> actions = new ArrayList<>();
        for (Order o : r.getOrders()) {
            // Oldest order listed first
            if (o.isInKitchen()) actions.add(new PrepareAction(o));
        }
        // Always available, so the menu is never empty
        actions.add(new WaitAction("Chef"));
        return actions;
    }
}