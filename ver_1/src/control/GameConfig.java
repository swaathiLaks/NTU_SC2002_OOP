package control;

public record GameConfig(int maxTurns, int tableCount, int minPaid, int minRevenueCents,
                         int minAvgSatisfaction, long seed) {

    /** The required base setting from Section 2.1 of the brief. */
    public static GameConfig base() {
        return new GameConfig(18, 2, 4, 4500, 60, 2002L);
    }
}
