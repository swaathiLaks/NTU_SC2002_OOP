package staff;

import control.ActionResult;
import entity.Order;
import entity.OrderStatus;
import entity.Restaurant;

public class ServeAction implements StaffAction {
    private final Order order;

    public ServeAction(Order order) { this.order = order; }

    @Override
    public String describe() {
        return "Serve order #" + order.getId() + " (" + order.getItem().getName()
                + ") to C" + order.getCustomer().getId();
    }

    @Override
    public ActionResult execute(Restaurant r) {
        if (order.getStatus() != OrderStatus.READY)
            return ActionResult.failure("Order #" + order.getId() + " is not READY (" + order.getStatus() + ").");
        r.serve(order);
        return ActionResult.success("Waiter serves order #" + order.getId() + " to C"
                + order.getCustomer().getId() + ": SERVED.");
    }
}
