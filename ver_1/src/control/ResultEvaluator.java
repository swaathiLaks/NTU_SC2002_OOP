package control;

import entity.GameResult;
import entity.Statistics;

// Decides whether the game ended in victory or defeat
public class ResultEvaluator {

    // Victory needs all three targets: paid customers, revenue and average satisfaction
    // stats: final game statisticcs
    // cfg: game settings holding the targets
    // return: VICTORY if every target is met, otherwise DEFEAT
    public GameResult evaluate(Statistics stats, GameConfig cfg) {
        boolean enoughPaid = stats.getPaidCount() >= cfg.minPaid();
        boolean enoughRevenue = stats.getRevenueCents() >= cfg.minRevenueCents();
        boolean happyEnough = stats.averageSatisfaction().orElse(0) >= cfg.minAvgSatisfaction();
        return enoughPaid && enoughRevenue && happyEnough ? GameResult.VICTORY : GameResult.DEFEAT;
    }
}