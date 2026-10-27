package boundary;

import java.util.List;
import java.util.OptionalDouble;

// Everything the UI needs to draw one screen. The UI never touches entity objects directly
// Changed in Stage 3: also carries the Happy Hour status
public record GameSnapshot(int turn, int maxTurns, int revenueCents, OptionalDouble avgSatisfaction,
                           int servedCount, int unhappyDepartures,
                           List<CustomerView> queue, List<TableView> tables, HappyHourView happyHour) {
}