package boundary;

import java.util.Optional;

public record TableView(int tableId, Optional<CustomerView> occupant) {
}
