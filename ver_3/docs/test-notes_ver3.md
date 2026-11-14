# Stage 3 test notes

All runs use the base setting: 18 turns, 2 tables, 1 Host, 1 Waiter, 1 Chef, 1 Cashier,
six arrivals on turns 1, 4, 7, 10, 13, 16 (Regular, VIP, Critic, Regular, Regular, Regular), seed 4.

Run each test from the `ver_3` folder after compiling (see README).
Each input file has one menu number per line. Each turn needs, in this order:
1. the Happy Hour answer (`1` = not now, `2` = start), **only while Happy Hour is still unused**;
2. the Host choice, then the Waiter choice, then the Chef choice.

In Git Bash: `java -cp out app.Main < docs/test_victory_run_ver3.txt`.
In PowerShell: `Get-Content docs\test_victory_run_ver3.txt | java -cp out app.Main`.
OR `Get-Content ../docs/test_victory_run_ver3.txt | java -cp out app/Main.java`

## Run 1: Victory with a Host, combos and Happy Hour (reproducible)

```
java -cp out app.Main < docs/test_victory_run_ver3.txt

Get-Content ../docs/test_victory_run_ver3.txt | java -cp out app/Main.java

```

Strategy: Happy Hour is started on turn 4, when the VIP arrives. Every turn the Host seats the next queued customer.
The Waiter serves a READY order first, otherwise takes an order. The Chef works on the oldest order in the kitchen.

| Customer | Dish | Bill | Satisfaction recorded |
|---|---|---|---|
| C1 Regular | Pasta | $18.00 | 84 |
| C2 **VIP** | **Salad + Pasta Combo** (3 units) | **$17.50** (= $24.30 x 0.9 x 0.8, Happy Hour on) | 90 |
| C3 Critic | Pasta (single dish, as in Stage 2) | $18.00 | 64 |
| C4 Regular | Salad + Burger Combo (2 units) | $21.60 | 76 |

C5 (Salad + Pasta Combo, $24.30) is served on turn 15 and C6 arrives on turn 16, so neither pays before turn 18. This is intended.

| Paid | Revenue | Avg satisfaction | Served | Unhappy departures | Result |
|---|---|---|---|---|---|
| 4 | $75.10 | 78.5 | 5 | 0 | VICTORY |

Points to notice:
- **Host:** each turn the Host seats the customer and the Waiter takes their order in the same turn (for example turn 1: "Host seats C1 at T1", then "Waiter takes C1's order").
- **Happy Hour timeline:** starts on turn 4 (2 turns remaining), continues on turn 5 (1 turn remaining, C2 recovers up to 15), and "Happy Hour is over" at the end of turn 5.
- **Price lock and stacking:** the order line on turn 4 reads `Salad + Pasta Combo ($17.50, Happy Hour -20%, 3 units)`. C2's bill stays $17.50 at payment.
- **Combo rule:** price $24.30 and 3 units match the brief's example (Salad + Pasta). C4's Salad + Burger is $21.60 and 2 units.
- **Orders before Happy Hour** (C1) and **after it** (C3, C4, C5, C6) are at normal prices.

## Run 2: Defeat (reproducible)

```
java -cp out app.Main < docs/test_defeat_run_ver3.txt
```

Strategy: the manager never starts Happy Hour and every staff member (Host, Waiter, Chef) chooses Wait every turn.

| Paid | Revenue | Avg satisfaction | Unhappy departures | Result |
|---|---|---|---|---|
| 0 | $0.00 | N/A | 2 | DEFEAT |

- C1 Regular (arrives turn 1, -8 per turn) leaves on turn 13.
- C3 Critic (arrives turn 7, -12 per turn) leaves on turn 15, after only 9 turns.
- Nobody is ever seated, so no orders are placed.

## Run 3: Invalid input

Typing `abc` or a number outside the menu at the Happy Hour prompt or any staff prompt prints
`Invalid choice "abc": enter a number from 1 to 2.` and asks again. The task is not used up.

## How the manager's choices change the result

Same seed, same arrivals, same scripted Waiter and Chef (Waiter: serve a READY order, else take an order, else seat; Chef: oldest order).
Only the Host and the Happy Hour start turn change. Reproduce any row with `test/StrategySweep.java` (see below).

| Host | Happy Hour started | Paid | Revenue | Avg satisfaction | Served |
|---|---|---|---|---|---|
| always Wait | never | 4 | $79.47 | 65.75 | 4 |
| always Wait | turn 4 | 4 | $79.47 | 70.75 | 4 |
| always Wait | turn 10 | 4 | $79.47 | 75.25 | 4 |
| seats whenever possible | never | 4 | $79.47 | 77.25 | 5 |
| seats whenever possible | turn 4 | 4 | $75.10 | 78.50 | 5 |
| seats whenever possible | turn 7 | 4 | $75.87 | 84.00 | 5 |

Reading the table (each point checked against the printed orders of those runs):
- **The Host** raises average satisfaction from 65.75 to 77.25 (+11.5) when Happy Hour is never used, and one more customer is served.
  The Waiter no longer has to choose between seating and taking orders.
- **Without the Host**, starting Happy Hour on turn 4 or 10 raises satisfaction at no revenue cost, because the Waiter was busy and
  no order was taken while it was on (C2's order, for example, was taken after it ended and was billed $21.87).
- **With the Host**, orders are taken straight away, so the discount does apply. Turn 4 discounts the VIP's Salad + Pasta Combo
  ($21.87 -> $17.50, -$4.37). Turn 7 discounts the Critic's Pasta ($18.00 -> $14.40, -$3.60) and gives the best satisfaction (84.00).
  So the Host and Happy Hour interact: the timing decides how much revenue is traded for satisfaction.
- Every row here is still a victory, so these choices change the margin rather than the ending. Run 2 shows a losing strategy.

## Rule and encapsulation checks

`test/entity/Stage3Checks.java` checks the combo price and units, VIP-then-Happy-Hour pricing ($15 Burger: VIP $13.50, then $10.80),
the two-turn duration and one-use limit, recovery capped at 100 and only for waiting customers, and that `Restaurant` rejects invalid calls.
It lives in package `entity` so it can reach package-private methods.

```
javac --release 17 -d out $(find src -name "*.java")
javac -cp out -d out-test test/entity/Stage3Checks.java
java -cp out:out-test entity.Stage3Checks
```

On Windows use `;` instead of `:` in the classpath. Expected last line: `ALL PASSED`.

`test/StrategySweep.java` plays a whole game with the scripted manager above and prints one result line. It also wrote
`test_victory_run_ver3.txt` and `test_defeat_run_ver3.txt` (it regenerates them byte for byte):

```
javac -cp out -d out-test test/StrategySweep.java
java -cp out:out-test StrategySweep 4 true      # Host on, Happy Hour on turn 4  -> victory-input.txt
java -cp out:out-test StrategySweep -1 false    # everyone always waits          -> defeat-input.txt
java -cp out:out-test StrategySweep 0 false     # first row of the table (0 = never start Happy Hour)
```

Add a file name as a third argument to write the menu numbers it chose.

To see the encapsulation from outside the package, put any of these in a class in package `boundary` and compile it: the compiler rejects
`restaurant.getHappyHour().activate();`, `customer.markPaid();`, `customer.recoverSatisfaction(50);`, `order.advance();`,
`table.free();`, `new Order(1, null, null, 1, false);`.
