package boundary;

// New file added in Stage 3
// Read-only view of Happy Hour for display. The numbers come from the game rules,
// so the CLI never hard-codes them
// available: true if it has not been started yet this game
// active: true while it is on
// turnsRemaining: active turns still to play, including the current one (0 when off)
// durationTurns: how many turns it lasts in total
// discountPercent: percentage taken off new orders
// recoveryPoints: satisfaction recovered per active turn by customers still waiting
public record HappyHourView(boolean available, boolean active, int turnsRemaining, int durationTurns,
                            int discountPercent, int recoveryPoints) {
}
