package entity;

import java.util.OptionalDouble;

public class Statistics {
    private int revenueCents;
    private int paidCount;
    private int servedCount;
    private int unhappyDepartures;
    private int satisfactionSum;

    public void recordPayment(int cents, int satisfaction) {
        revenueCents += cents;
        paidCount++;
        satisfactionSum += satisfaction;
    }

    public void recordServed() { servedCount++; }
    public void recordDeparture() { unhappyDepartures++; }

    public int getRevenueCents() { return revenueCents; }
    public int getPaidCount() { return paidCount; }
    public int getServedCount() { return servedCount; }
    public int getUnhappyDepartures() { return unhappyDepartures; }

    /** Average satisfaction recorded at payment; empty if nobody has paid yet. */
    public OptionalDouble averageSatisfaction() {
        return paidCount == 0 ? OptionalDouble.empty() : OptionalDouble.of((double) satisfactionSum / paidCount);
    }
}
