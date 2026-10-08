package staff;

import control.ActionResult;
import entity.Order;
import entity.Restaurant;

public class PrepareAction implements StaffAction {
    private final Order order;

    public PrepareAction(Order order) { this.order = order; }

    @Override
    public String describe() {
        return "Prepare order #" + order.getId() + " (" + order.getItem().getName() + ", "
                + order.getRemainingUnits() + " unit" + (order.getRemainingUnits() == 1 ? "" : "s") + " left)";
    }

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
