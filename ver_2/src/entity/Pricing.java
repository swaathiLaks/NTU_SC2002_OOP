package entity;

// New file added in Stage 2
// Money helpers. All prices are whole cents, so every discount rounds the same way (half up)
public final class Pricing {
    private Pricing() { }

    // percent: share of the price that is kept, e.g. 90 for "10% off"
    // return: that share of the price, rounded to the nearest cent
    static int percentOf(int cents, int percent) { return (cents * percent + 50) / 100; }

    // Converts cents to a "$x.yy" string
    public static String format(int cents) { return String.format("$%d.%02d", cents / 100, cents % 100); }
}
