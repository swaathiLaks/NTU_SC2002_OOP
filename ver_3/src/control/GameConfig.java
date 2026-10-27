package control;

// Game setting and win conditions
// maxTurns: number of turn in game
// tableCount: number of tables in the restaurant 
// minPaid: minimum paid customers needed to win 
// minRevenueCents: minimum revenue needed to win, in cents
// minAvgSatisfaction: minimum average satisfaction of paid customers needed to win
// seed: random seed, so every run is reproducible
public record GameConfig(int maxTurns, int tableCount, int minPaid, int minRevenueCents,
                         int minAvgSatisfaction, long seed) {

    // Returns the required base setting: 18 turns, 2 tables, at least 4 paid, $45.00 revenue, 60 avg satisfaction
    // Changed in Stage 3: seed 3 -> 4. With seed 3 none of the six orderable items drawn in the base run is a combo,
    // so the Stage 3 combo meals would never show up. Seed 4 includes combos (see docs/test-notes.md)
    public static GameConfig base() {
        return new GameConfig(18, 2, 4, 4500, 60, 4L);
    }
}