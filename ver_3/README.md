# Restaurant Rush - Stage 3 (New role and menu policy)

Everything from Stage 2, plus a **Host**, **ComboMeal** items and a once-per-game **Happy Hour**.
Same base setting and the same win and lose criteria.

| Feature | Rule |
|---|---|
| Host | Seats at most one queued customer per turn, in addition to the Waiter's task. The Waiter can still seat customers |
| ComboMeal | Two different menu items. Preparation units = sum of the two. Price = 90% of the combined price, rounded to the nearest cent |
| Happy Hour | Started by the manager at the start of a turn, once per game. Lasts that turn and the next. Orders taken while it is on get 20% off. Customers still waiting for a table or food recover 15 satisfaction at the start of each active turn (max 100) |

## Build and run

From this `ver_3` folder (Java 17 or later):

```
javac --release 17 -d out $(find src -name "*.java")
java -cp out app.Main
```

On Windows Command Prompt / PowerShell, compile with:

```
javac --release 17 -d out src/app/*.java src/boundary/*.java src/control/*.java src/staff/*.java src/entity/*.java
```

Each turn the manager is now asked, in this order: **Happy Hour** (only while it is still unused: `1` = not now, `2` = start),
then **Host**, **Waiter**, **Chef**. The Host acts first, so a customer the Host seats can have their order taken by the
Waiter on the same turn. The Waiter still acts before the Chef, so a dish finished this turn is served next turn at the earliest.

Display options: `NO_COLOR=1` turns colours off, `RUSH_ASCII=1` forces ASCII characters instead of block characters.

## Combo meals

| Combo | Price | Preparation units |
|---|---|---|
| Salad + Burger | $21.60 | 2 |
| Salad + Pasta | $24.30 | 3 |
| Burger + Pasta | $29.70 | 3 |

**Choice rule (documented, seeded):** Regular and VIP customers pick uniformly at random, using the game's seeded
`Random`, from the six orderable items (three single dishes, then three combos). The choice is shown when the order is taken.
The Critic is unchanged: it still orders the single dish with the most preparation units (Pasta) and never a combo.

## Happy Hour

- The price is locked when the order is taken. The customer discount is applied first, then Happy Hour.
  Example from the run in the test notes: a VIP orders Salad + Pasta ($24.30), x 0.9 = $21.87, x 0.8 = **$17.50**.
- It does not change orders already placed and does not affect customers who have left.
- While it is on, the turn header shows a rainbow banner with the turns remaining ("2 turns remaining", then "1 turn remaining (last one!)").
  Starting it shows a full-width rainbow "HAPPY HOUR" banner. Events about it are coloured too.
- Starting it on turn 18 only gives one active turn, because the game has no turn 19.

## Encapsulation

Stage 3 follows the same rule as Stage 2: state is private, and only the owning aggregate can change it.

| Class | What is hidden | How the rest of the code uses it |
|---|---|---|
| `HappyHour` | `used` and `remainingTurns` are private; `activate()`, `endTurn()` and `priceFor()` are package-private | Others call `isActive()`, `isAvailable()`, `getRemainingTurns()`. Only `Restaurant` can start it, and it throws if started twice |
| `ComboMeal` | Components held in an unmodifiable list | `getComponents()` is read-only |
| `Menu` | All three item lists are immutable | Getters only |
| `Customer`, `Order`, `Table` | Every method that changes state is package-private (including the new `recoverSatisfaction()` and the `Order` constructor) | The CLI and staff classes cannot call them. The compiler rejects it |
| `Restaurant` | **Changed:** every public method that changes state now checks its own preconditions | See below |
| `GameSnapshot` / `HappyHourView` | Records with no setters | The CLI reads Happy Hour numbers (20%, 15, 2 turns) from the view, so no game rule is hard-coded in the UI |

What `Restaurant` now enforces itself, instead of trusting its callers:

| Method | Check |
|---|---|
| `seatCustomer` | Customer must be in the queue and the table free and owned by this restaurant. It seats first and leaves the queue second, so a failed call changes nothing. In Stage 2 the order was reversed, so a failed seat would have lost the customer from the queue |
| `placeOrder` | Customer must be SEATED |
| `markReadyToPay` | Customer must be SERVED |
| `collectPayment` | Only the oldest READY_TO_PAY customer can be charged |
| `prepare`, `serve` | Order must belong to this restaurant (and be in the right status) |
| `addToQueue` | Customer must be new and the ID unused |

The staff actions still check first and return a failed `ActionResult` with a message, so the manager can choose again.
These exceptions only fire if future code calls `Restaurant` wrongly.

## Stage comparisons

In the code, every change is marked with `// New file added in Stage 3`, `// Added in Stage 3` or `// Changed in Stage 3`.

Repository: https://github.com/swaathiLaks/NTU_SC2002_OOP

### Stage 1 -> Stage 2 (compare: https://github.com/swaathiLaks/NTU_SC2002_OOP/compare/3710f5247f01d43ae8c45f09ac3e0e12a57d713d...27ebbd7b5dd381a39c849849a17c79b3dd801eeb)

| New file | Purpose |
|---|---|
| entity/VIPCustomer.java | Loses 5 per waiting turn; overrides `priceFor()` for 10% off |
| entity/CriticCustomer.java | Loses 12 per waiting turn; overrides `chooseItem()` to order the dish with most prep units |
|entity/Pricing.java|Neew file with rounding rule and money formatter|
|src/test directory|Stage 1 runs, superseded by docs/test-notes.md|

| Changed file | Why |
|---|---|
| entity/Customer.java | Added abstract `typeLabel()` so subclasses name their own type |
| entity/RegularCustomer.java | Implements `typeLabel()`. Corrected inaccurate comments. |
|entity/Restaurant.java|Added `prepare()` and `applyWaitingPenalties()`. Chef and penalty logic go through the aggregate instead of the controller|
|entity/Order.java, Table.java, Customer.java|Mutating methods package-private; removed `setOrder()` and unused `ORDER_CYCLE`|
| control/ArrivalSchedule.java | Set of turns -> map of turn to customer factory, so types differ per arrival |
| control/GameController.java | Arrival message and view include the type |
| boundary/CustomerView.java, boundary/GameCLI.java | Display the type |
|boundary/GameCLI.java, GameExitException.java|Cleanup: `Pricing.format`, stale TODO removed, `serialVersionUID`|
|staff/PrepareAction.java, TakeOrderAction.java|Use `Restaurant.prepare()` / `Pricing.format` to adhere to encapsulation and enforce consistent money format.|
|README.md, docs/test-notes.md, docs/*-input.txt|Modified for Stage 2|

Unchanged: Statistics, ResultEvaluator, all staff classes except PrepareAction and TakeOrderAction. Stage 1 already exposed `waitingLoss()`, `priceFor()` and `chooseItem()`, so Stage 2 only added subclasses.

### Stage 2 -> Stage 3 (compare: https://github.com/swaathiLaks/NTU_SC2002_OOP/compare/7bde336a99b71d1862ca93ffdef19a85df8f5cd2...8c10fec2342e5ba5218f5e62c62e2e333d5d55a2)

| New file | Purpose |
|---|---|
| entity/ComboMeal.java | A `MenuItem` made of two items: summed units, 90% price |
| entity/HappyHour.java | The Happy Hour policy: once per game, two turns, 20% off, +15 satisfaction |
| staff/Host.java | Seats one queued customer per turn using the shared `SeatAction` |
| boundary/HappyHourView.java | Read-only Happy Hour data for the CLI |
| test/entity/Stage3Checks.java | 24 rule and invariant checks (see docs/test-notes.md) |
| test/StrategySweep.java | Plays a full game with a scripted manager. Reproduces the test inputs and the results table |

| Changed file | Why |
|---|---|
| entity/Menu.java | Added the three combos and `getOrderableItems()`. `getItems()` still returns the single dishes only |
| entity/Customer.java | `chooseItem()` picks from all orderable items (combos included); added package-private `recoverSatisfaction()` |
| entity/CriticCustomer.java | Comment only: it still uses the single dishes, so its order is the same as in Stage 2 |
| entity/Order.java | Remembers whether Happy Hour pricing was applied (`isHappyHourPrice()`), so the message and later display can show it |
| entity/Restaurant.java | Owns `HappyHour`; applies customer discount then Happy Hour in `placeOrder()`; added `activateHappyHour()`, `applyHappyHourRecovery()`, `endTurn()`; added precondition checks to the public mutators (see Encapsulation) |
| staff/SeatAction.java | Takes the staff name ("Host" or "Waiter") for its message; added `optionsFor()` so Host and Waiter share the seating options |
| staff/Waiter.java | Uses `SeatAction.optionsFor()` instead of its own copy of the seating loop |
| staff/TakeOrderAction.java | The order message shows "Happy Hour -20%" when it applies |
| control/GameController.java | Added the Host step, Happy Hour start/continue/end, and Happy Hour in the snapshot. Still no customer type checks |
| control/GameConfig.java | Seed 3 -> 4. With seed 3 no combo is drawn in the base run, so combos would never appear. Seed 4 includes combos (see test notes) |
| boundary/GameSnapshot.java | Added the `HappyHourView` field |
| boundary/GameCLI.java | Host step, Happy Hour prompt, rainbow banner and splash, `P`/`Y`/`O` letters for the block font, version label. `runStaffStep` now takes the controller calls instead of a waiter/chef flag |
|README.md, docs/test-notes.md, docs/*-input.txt|Modified for Stage 3|

Unchanged: `Chef`, `Cashier`, `PrepareAction`, `ServeAction`, `StaffAction`, `WaitAction`, `MenuItem`, `Pricing`, `Statistics`,
`Table`, `RegularCustomer`, `VIPCustomer`, `ArrivalSchedule`, `ResultEvaluator`, `Main` and the other boundary classes.
The Chef, kitchen and serving code did not need to change for combos, because `ComboMeal` is a `MenuItem`.

## Tests

See [docs/test-notes_ver3.md](docs/test-notes_ver3.md) for the reproducible victory and defeat runs, how the manager's choices change
the result, and how to run `test/entity/Stage3Checks.java` and `test/StrategySweep.java`.

## AI History
- [Chia Kei Yin: AI Logs](https://github.com/swaathiLaks/NTU_SC2002_OOP/blob/ai-history/Chia_Kei_Yin/ai-history_Chia_Kei_Yin/stage-3/chat-01_Chia_Kei_Yin.md)