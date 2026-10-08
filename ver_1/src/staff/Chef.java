package staff;

import entity.Order;
import entity.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class Chef {

    /** One PrepareAction per order still in the kitchen, plus Wait. */
    public List<StaffAction> availableActions(Restaurant r) {
        List<StaffAction> actions = new ArrayList<>();
        for (Order o : r.getOrders()) {
            if (o.isInKitchen()) actions.add(new PrepareAction(o));
        }
        actions.add(new WaitAction("Chef"));
        return actions;
    }
}
