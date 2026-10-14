package boundary;

import entity.CustomerState;
import entity.OrderStatus;

// Read-only view of a customer for display. orderItem and orderStatus are null before ordering
// Changed in Stage 2: added the type field
// type: Regular, VIP or Critic
public record CustomerView(int id, String type, int satisfaction, CustomerState state, String orderItem, OrderStatus orderStatus) {
}
