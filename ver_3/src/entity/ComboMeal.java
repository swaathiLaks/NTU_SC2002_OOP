package entity;

import java.util.List;

// New file added in Stage 3
// A combo of two existing menu items. Preparation units are the sum of its two components and the
// price is 90% of their combined price. It extends MenuItem, so Order, Chef and the staff actions
// handle a combo exactly like a single dish and none of them needed to change
public class ComboMeal extends MenuItem {
    // share of the combined price that the customer pays
    private static final int PERCENT_OF_COMBINED = 90;
    private final List<MenuItem> components;

    // first: first dish in the combo
    // second: second dish in the combo
    public ComboMeal(MenuItem first, MenuItem second) {
        super(first.getName() + " + " + second.getName() + " Combo",
                Pricing.percentOf(first.getPriceCents() + second.getPriceCents(), PERCENT_OF_COMBINED),
                first.getPrepUnits() + second.getPrepUnits());
        this.components = List.of(first, second);
    }

    // The two dishes this combo is made of (unmodifiable)
    public List<MenuItem> getComponents() { return components; }
}
