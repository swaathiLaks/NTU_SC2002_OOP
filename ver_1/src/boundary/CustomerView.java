package boundary;

import entity.CustomerState;
import entity.OrderStatus;

/** Read-only view of a customer for display. orderItem and orderStatus are null before ordering. */
public record CustomerView(int id, int satisfaction, CustomerState state, String orderItem, OrderStatus orderStatus) {
}
