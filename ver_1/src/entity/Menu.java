package entity;

import java.util.List;
import java.util.Optional;

// The restaurant's fixed menu
public class Menu {
    // name, price in cents, turns to cook
    private final List<MenuItem> items = List.of(
            new MenuItem("Salad", 900, 1),
            new MenuItem("Burger", 1500, 1),
            new MenuItem("Pasta", 1800, 2));
    
    // Returns all menu items
    public List<MenuItem> getItems() { return items; }

    // Finds an item by name, ignoring case
    // name: item name
    // return: the item, or empty if not on the menu
    public Optional<MenuItem> find(String name) {
        return items.stream().filter(i -> i.getName().equalsIgnoreCase(name)).findFirst();
    }
}