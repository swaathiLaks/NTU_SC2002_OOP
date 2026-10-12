package staff;

import entity.Customer;
import entity.CustomerState;
import entity.Order;
import entity.OrderStatus;
import entity.Restaurant;
import entity.Table;

import java.util.ArrayList;
import java.util.List;

// Front-of-house staff: seats customers, takes orders and serves food
public class Waiter {

    // Every task the Waiter could do right now, plus Wait
    // r: the restaurant
    // return: actions the player can choose for the waiter this turn 
    public List<StaffAction> availableActions(Restaurant r) {
        List<StaffAction> actions = new ArrayList<>();
        
        // Seat: any waiting customer at the first free table
        r.findFreeTable().ifPresent(t -> {
            for (Customer c : r.getWaitingQueue()) actions.add(new SeatAction(c, t));
        });
        
        // Take order: tables with a seated customer who hasn't ordered
        for (Table t : r.getTables()) {
            t.getOccupant()
                    .filter(c -> c.getState() == CustomerState.SEATED)
                    .ifPresent(c -> actions.add(new TakeOrderAction(t)));
        }
        
        // Serve: orders the chef has finished
        for (Order o : r.getOrders()) {
            if (o.getStatus() == OrderStatus.READY) actions.add(new ServeAction(o));
        }
        
        // always available, so the menu is never empty
        actions.add(new WaitAction("Waiter"));
        return actions;
    }
}