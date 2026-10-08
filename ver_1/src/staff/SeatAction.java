package staff;

import control.ActionResult;
import entity.Customer;
import entity.CustomerState;
import entity.Restaurant;
import entity.Table;

public class SeatAction implements StaffAction {
    private final Customer customer;
    private final Table table;

    public SeatAction(Customer customer, Table table) {
        this.customer = customer;
        this.table = table;
    }

    @Override
    public String describe() {
        return "Seat customer C" + customer.getId() + " at table T" + table.getId();
    }

    @Override
    public ActionResult execute(Restaurant r) {
        if (customer.getState() != CustomerState.WAITING)
            return ActionResult.failure("C" + customer.getId() + " is not in the waiting queue.");
        if (!table.isFree())
            return ActionResult.failure("Table T" + table.getId() + " is occupied.");
        r.seatCustomer(customer, table);
        return ActionResult.success("Waiter seats C" + customer.getId() + " at T" + table.getId() + ".");
    }
}
