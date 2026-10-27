package entity;

import java.util.Comparator;
import java.util.Random;

// New file added in Stage 2
// Food critic: impatient, pays full price and always orders the slowest dish to test the kitchen
public class CriticCustomer extends Customer {
    // id: unique customer number
    public CriticCustomer(int id) { super(id); }

    // Loses 12 satisfaction per turn spent waiting
    @Override
    public int waitingLoss() { return 12; }

    // Always orders the dish with the most preparation units, ties go to the higher price
    // Changed in Stage 3 (comment only): the critic still looks at the single dishes (menu.getItems()),
    // never at combos, so the critic's order is the same as in Stage 2
    // On the base menu this is always Pasta, so the manager must prioritise the critic
    // The seeded random is not used, so the critic's choice never changes
    // menu: the restaurant menu
    // rng: unused by the critic
    // return: the dish the critic orders
    @Override
    public MenuItem chooseItem(Menu menu, Random rng) {
        return menu.getItems().stream()
                .max(Comparator.comparingInt(MenuItem::getPrepUnits).thenComparingInt(MenuItem::getPriceCents))
                .orElseThrow();
    }

    // Label shown in the CLI
    @Override
    public String typeLabel() { return "Critic"; }
}
