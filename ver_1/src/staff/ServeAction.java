package staff;

import control.ActionResult;
import entity.Order;
import entity.OrderStatus;
import entity.Restaurant;

// Waiter action: delivers a READY order to its customer
public class ServeAction implements StaffAction {
    private final Order order;

    // order: the order to serve
    public ServeAction(Order order) { this.order = order; }

    // Menu text naming the order, dish and customer
    @Override
    public String describe() {
        return "Serve order #" + order.getId() + " (" + order.getItem().getName()
                + ") to C" + order.getCustomer().getId();
    }

    // Serves the order; the customer starts eating
    // return: success, or failure if the order isn't READY
    @Override
    public ActionResult execute(Restaurant r) {
        if (order.getStatus() != OrderStatus.READY)
            return ActionResult.failure("Order #" + order.getId() + " is not READY (" + order.getStatus() + ").");
        r.serve(order);
        return ActionResult.success("Waiter serves order #" + order.getId() + " to C"
                + order.getCustomer().getId() + ": SERVED.");
    }
}