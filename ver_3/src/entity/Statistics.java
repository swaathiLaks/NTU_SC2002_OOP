package entity;

import java.util.OptionalDouble;

// Running totals used for the turn summary and the win check
public class Statistics {
    private int revenueCents;
    private int paidCount;
    private int servedCount;
    private int unhappyDepartures;
    // Of paid customers only
    private int satisfactionSum;

    // Records a completed payment
    // cents: amount paid
    // satisfaction: customer's satisfaction at payment
    void recordPayment(int cents, int satisfaction) {
        revenueCents += cents;
        paidCount++;
        satisfactionSum += satisfaction;
    }

    // Counts a customer whose food was delivered
    void recordServed() { servedCount++; }
    // Counts a customer who left without paying
    void recordDeparture() { unhappyDepartures++; }

    // Getters
    public int getRevenueCents() { return revenueCents; }
    public int getPaidCount() { return paidCount; }
    public int getServedCount() { return servedCount; }
    public int getUnhappyDepartures() { return unhappyDepartures; }

    // Average satisfaction recorded at payment; empty if nobody has paid yet
    public OptionalDouble averageSatisfaction() {
        return paidCount == 0 ? OptionalDouble.empty() : OptionalDouble.of((double) satisfactionSum / paidCount);
    }
}