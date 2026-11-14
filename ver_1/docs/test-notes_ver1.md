# Stage 1 test notes
All runs use the base setting: 18 turns, 2 tables, 1 Waiter, 1 Chef, 1 Cashier,
arrivals on turns 1, 4, 7, 10, 13, 16. Customers choose food using a seeded java.util.Random (seed 3, see GameConfig): C1 Pasta, C2 Pasta, C3 Salad, C4 Burger, C5 Salad, C6 Salad.

Run each test from the `ver_1` folder after compiling (see README).
Each input file has one menu number per line (Waiter choice, then Chef choice, for each turn).

## Run 1: Victory (reproducible)
```
java -cp out app.Main < docs/test_victory_run_ver1.txt
```

Strategy: Waiter serves a READY order first, otherwise takes an order, otherwise seats the next customer. Chef always works on the oldest order in the kitchen.

Expected result:
| Paid | Revenue | Avg satisfaction | Unhappy departures | Result |
|---|---|---|---|---|
| 5 | $69.00 | 74.4 | 0 | VICTORY |

## Run 2: Defeat (reproducible)
```
java -cp out app.Main < docs/test_defeat_run_ver1.txt
```

Strategy: every staff member chooses Wait every turn.

Expected result:
| Paid | Revenue | Avg satisfaction | Unhappy departures | Result |
|---|---|---|---|---|
| 0 | $0.00 | N/A | 2 (C1 on turn 13, C2 on turn 16) | DEFEAT |

## Run 3: Brief Section 3.3 example (one customer, arrival to payment)
The first 6 turns of the victory run follow C1 (Pasta, 2 units) exactly as in the brief:

| Turn | What happens | Satisfaction at turn end |
|---|---|---|
| 1 | Arrives (100). Waiter seats C1 at T1. | 92 |
| 2 | Waiter takes the Pasta order. Chef does unit 1 of 2. | 84 |
| 3 | Chef does unit 2 of 2: READY. Waiter cannot serve it this turn. | 76 |
| 4 | Waiter serves: SERVED. | 76 |
| 5 | C1 finishes eating: READY_TO_PAY. | 76 |
| 6 | Cashier collects $18.00: PAID, table freed. 76 recorded. | - |

## Run 4: Invalid input
Typing `abc` or a number outside the menu prints
`Invalid choice "abc": enter a number from 1 to 2.` and asks again.
The staff task is not used up.
