package staff;

import entity.Customer;
import entity.CustomerState;
import entity.Order;
import entity.OrderStatus;
import entity.Restaurant;
import entity.Table;

import java.util.ArrayList;
import java.util.List;

public class Waiter {

    /** Every task the Waiter could do right now, plus Wait. */
    public List<StaffAction> availableActions(Restaurant r) {
        List<StaffAction> actions = new ArrayList<>();
        r.findFreeTable().ifPresent(t -> {
            for (Customer c : r.getWaitingQueue()) actions.add(new SeatAction(c, t));
        });
        for (Table t : r.getTables()) {
            t.getOccupant()
                    .filter(c -> c.getState() == CustomerState.SEATED)
                    .ifPresent(c -> actions.add(new TakeOrderAction(t)));
        }
        for (Order o : r.getOrders()) {
            if (o.getStatus() == OrderStatus.READY) actions.add(new ServeAction(o));
        }
        actions.add(new WaitAction("Waiter"));
        return actions;
    }
}
