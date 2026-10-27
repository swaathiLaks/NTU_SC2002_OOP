package staff;

import control.ActionResult;
import entity.Customer;
import entity.CustomerState;
import entity.Restaurant;
import entity.Table;

import java.util.ArrayList;
import java.util.List;

// Seating action, used by the Waiter and (Added in Stage 3) the Host: seats a waiting customer at a free table
public class SeatAction implements StaffAction {
    // Added in Stage 3: who is doing the seating, shown in messages
    private final String staffName;
    private final Customer customer;
    private final Table table;

    // staffName: staff member doing the seating, e.g. "Waiter" or "Host" (Changed in Stage 3)
    // customer: customer to seat
    // table: table to seat them at
    public SeatAction(String staffName, Customer customer, Table table) {
        this.staffName = staffName;
        this.customer = customer;
        this.table = table;
    }

    // Added in Stage 3: one SeatAction for every queued customer, all for the first free table
    // Shared by Waiter and Host so the seating options are defined in one place
    // staffName: staff member who will do the seating
    // r: the restaurant
    // return: the seating options, empty if nobody is queued or no table is free
    static List<StaffAction> optionsFor(String staffName, Restaurant r) {
        List<StaffAction> actions = new ArrayList<>();
        r.findFreeTable().ifPresent(t -> {
            for (Customer c : r.getWaitingQueue()) actions.add(new SeatAction(staffName, c, t));
        });
        return actions;
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
        return ActionResult.success(staffName + " seats C" + customer.getId() + " at T" + table.getId() + ".");
    }
}