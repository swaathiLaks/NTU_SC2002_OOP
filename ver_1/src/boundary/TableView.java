package boundary;

import java.util.Optional;

// Read-only view of a table for display
// tableId: table number
// occupant: customer seated here, or empty if the table is free
public record TableView(int tableId, Optional<CustomerView> occupant) {
}