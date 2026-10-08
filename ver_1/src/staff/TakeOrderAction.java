package staff;

import control.ActionResult;
import entity.Customer;
import entity.CustomerState;
import entity.Order;
import entity.Restaurant;
import entity.Table;

public class TakeOrderAction implements StaffAction {
    private final Table table;

    public TakeOrderAction(Table table) { this.table = table; }

    @Override
    public String describe() {
        return "Take order at table T" + table.getId()
                + table.getOccupant().map(c -> " (C" + c.getId() + ")").orElse("");
    }

    @Override
    public ActionResult execute(Restaurant r) {
        Customer c = table.getOccupant().orElse(null);
        if (c == null)
            return ActionResult.failure("Table T" + table.getId() + " is empty.");
        if (c.getState() != CustomerState.SEATED)
            return ActionResult.failure("C" + c.getId() + " has already ordered.");
        Order o = r.placeOrder(c);
        return ActionResult.success(String.format("Waiter takes C%d's order: %s ($%.2f, %d unit%s) as order #%d.",
                c.getId(), o.getItem().getName(), o.getPriceCents() / 100.0,
                o.getRemainingUnits(), o.getRemainingUnits() == 1 ? "" : "s", o.getId()));
    }
}
