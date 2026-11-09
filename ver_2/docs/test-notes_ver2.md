# Stage 2 test notes

All runs use the base setting: 18 turns, 2 tables, 1 Waiter, 1 Chef, 1 Cashier,
six arrivals on turns 1, 4, 7, 10, 13, 16. Stage 2 replaces two of the Regular arrivals:

| Turn | Customer | Loses per waiting turn | Bill | Food choice |
|---|---|---|---|---|
| 1 | C1 Regular | 8 | full price | seeded Random (seed 3) |
| 4 | C2 **VIP** | 5 | **10% off** | seeded Random |
| 7 | C3 **Critic** | 12 | full price | **always the dish with the most preparation units (Pasta)** |
| 10, 13, 16 | C4-C6 Regular | 8 | full price | seeded Random |

The Critic does not draw from the seeded Random, so customers after C3 get different dishes
from Stage 1. With seed 3 the orders are: C1 Pasta, C2 Pasta, C3 Pasta (Critic rule), C4 Salad, C5 Burger.

Run each test from the `ver_2` folder after compiling (see README).
Each input file has one menu number per line (Waiter choice, then Chef choice, for each turn).
In Git Bash: `java -cp out app.Main < docs/test_victory_run_ver2.txt`.
In PowerShell: `Get-Content docs\test_victory_run_ver2.txt | java -cp out app.Main`.

## Run 1: Victory with a VIP and a Critic (reproducible)

```
java -cp out app.Main < docs/test_victory_run_ver2.txt
```

Strategy: Waiter serves a READY order first, otherwise takes an order, otherwise seats the next customer.
Chef always works on the oldest order in the kitchen.

| Customer | Dish | Bill | Satisfaction recorded |
|---|---|---|---|
| C1 Regular | Pasta | $18.00 | 76 |
| C2 VIP | Pasta | **$16.20** (10% off $18.00) | 80 |
| C3 Critic | Pasta | $18.00 | 52 |
| C4 Regular | Salad | $9.00 | 68 |

| Paid | Revenue | Avg satisfaction | Served | Unhappy departures | Result |
|---|---|---|---|---|---|
| 4 | $61.20 | 69.0 | 5 | 0 | VICTORY |

Points to notice:
- The VIP's bill is $16.20, locked when the order is taken (shown in the "Waiter takes C2's order" line).
- The VIP loses 5 per waiting turn (100 -> 95 -> 90 ...), the Critic 12, Regulars 8.
- The Critic orders Pasta (2 units) and loses satisfaction fastest, so it ends lowest (52).

## Run 2: Defeat (reproducible)

```
java -cp out app.Main < docs/test_defeat_run_ver2.txt
```

Strategy: every staff member chooses Wait every turn.

| Paid | Revenue | Avg satisfaction | Unhappy departures | Result |
|---|---|---|---|---|
| 0 | $0.00 | N/A | 2 | DEFEAT |

Patience comparison from this run (nobody is ever seated):
- C1 Regular (arrives turn 1, -8 per turn) leaves on turn 13.
- C3 Critic (arrives turn 7, -12 per turn) leaves on turn 15, after only 9 turns.
- C2 VIP (arrives turn 4, -5 per turn) is still waiting at the end with satisfaction 25.

## Run 3: Invalid input

Typing `abc` or a number outside the menu prints
`Invalid choice "abc": enter a number from 1 to 2.` and asks again.
The staff task is not used up.
