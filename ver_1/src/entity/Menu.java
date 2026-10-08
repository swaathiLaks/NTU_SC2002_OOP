package entity;

import java.util.List;
import java.util.Optional;

public class Menu {
    private final List<MenuItem> items = List.of(
            new MenuItem("Salad", 900, 1),
            new MenuItem("Burger", 1500, 1),
            new MenuItem("Pasta", 1800, 2));

    public List<MenuItem> getItems() { return items; }

    public Optional<MenuItem> find(String name) {
        return items.stream().filter(i -> i.getName().equalsIgnoreCase(name)).findFirst();
    }
}
