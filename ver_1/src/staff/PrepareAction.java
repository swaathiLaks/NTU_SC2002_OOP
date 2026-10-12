package staff;

import control.ActionResult;
import entity.Order;
import entity.Restaurant;

// Chef action: does one prep unit on a specific order
public class PrepareAction implements StaffAction {
    private final Order order;

    // order: the order to work on
    public PrepareAction(Order order) { this.order = order; }

    // Menu text showing the order, dish and prep units left
    @Override
    public String describe() {
        return "Prepare order #" + order.getId() + " (" + order.getItem().getName() + ", "
                + order.getRemainingUnits() + " unit" + (order.getRemainingUnits() == 1 ? "" : "s") + " left)";
    }

    // Advances the order by one unit
    // return: success with progress, or failure if the order has left the kitchen
    @Override
    public ActionResult execute(Restaurant r) {
        if (!order.isInKitchen())
            return ActionResult.failure("Order #" + order.getId() + " is not in the kitchen (" + order.getStatus() + ").");
        boolean ready = order.advance();
        return ActionResult.success(ready
                ? "Chef finishes order #" + order.getId() + ": READY."
                : "Chef prepares order #" + order.getId() + ": " + order.getRemainingUnits() + " unit(s) left.");
    }
}