package control;

import entity.GameResult;
import entity.Statistics;

public class ResultEvaluator {

    /** Victory needs all three targets: paid customers, revenue and average satisfaction. */
    public GameResult evaluate(Statistics stats, GameConfig cfg) {
        boolean enoughPaid = stats.getPaidCount() >= cfg.minPaid();
        boolean enoughRevenue = stats.getRevenueCents() >= cfg.minRevenueCents();
        boolean happyEnough = stats.averageSatisfaction().orElse(0) >= cfg.minAvgSatisfaction();
        return enoughPaid && enoughRevenue && happyEnough ? GameResult.VICTORY : GameResult.DEFEAT;
    }
}
