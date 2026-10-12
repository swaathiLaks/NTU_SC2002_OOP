package staff;

import control.ActionResult;
import entity.Customer;
import entity.CustomerState;
import entity.Restaurant;
import entity.Table;

// Waiter action: seats a waiting customer at a free table
public class SeatAction implements StaffAction {
    private final Customer customer;
    private final Table table;

    // customer: customer to seat
    // table: table to seat them at
    public SeatAction(Customer customer, Table table) {
        this.customer = customer;
        this.table = table;
    }

    // Menu text naming the customer and table
    @Override
    public String describe() {
        return "Seat customer C" + customer.getId() + " at table T" + table.getId();
    }

    // Moves the customer from the queue to the table
    // return: success, or failure if the customer isn't waiting or the table is taken
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