package entity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

// The restaurant's fixed menu: three single dishes and the combos made from them
public class Menu {
    // name, price in cents, turns to cook
    private final List<MenuItem> items = List.of(
            new MenuItem("Salad", 900, 1),
            new MenuItem("Burger", 1500, 1),
            new MenuItem("Pasta", 1800, 2));

    // Added in Stage 3: every pair of different single dishes makes one combo
    private final List<ComboMeal> combos = List.of(
            new ComboMeal(items.get(0), items.get(1)),
            new ComboMeal(items.get(0), items.get(2)),
            new ComboMeal(items.get(1), items.get(2)));

    // Added in Stage 3: single dishes followed by combos, in the order customers pick from
    private final List<MenuItem> orderable = Stream.concat(items.stream(), combos.stream()).toList();

    // Returns the single dishes only (Stage 1 and 2 behaviour, unchanged)
    public List<MenuItem> getItems() { return items; }

    // Added in Stage 3: returns the combos
    public List<ComboMeal> getCombos() { return combos; }

    // Added in Stage 3: returns everything a customer can order, single dishes first and then combos
    public List<MenuItem> getOrderableItems() { return orderable; }

    // Finds an orderable item by name, ignoring case
    // Changed in Stage 3: also searches the combos
    // name: item name
    // return: the item, or empty if not on the menu
    public Optional<MenuItem> find(String name) {
        return orderable.stream().filter(i -> i.getName().equalsIgnoreCase(name)).findFirst();
    }
}
