package entity;

// A dish on the menu
public class MenuItem {
    private final String name;
    private final int priceCents;
    private final int prepUnits;

    // name: dish name
    // priceCents: base price in cents
    // prepUnits: units of chef work needed to finish the dish
    public MenuItem(String name, int priceCents, int prepUnits) {
        this.name = name;
        this.priceCents = priceCents;
        this.prepUnits = prepUnits;
    }

    // Getters
    public String getName() { return name; }
    public int getPriceCents() { return priceCents; }
    public int getPrepUnits() { return prepUnits; }
}