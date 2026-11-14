# Restaurant Rush - Stage 2 (Customer behaviour)

Everything from Stage 1, plus **VIP** and **Critic** customers alongside Regular customers.
Same base setting and the same win and lose criteria.

| Customer | Satisfaction loss per waiting turn | Bill | Special behaviour |
|---|---|---|---|
| Regular | 8 | Full price | - |
| VIP | 5 | 10% off | - |
| Critic | 12 | Full price | Always orders the dish with the most preparation units (Pasta) |

## Build and run

From this `ver_2` folder (Java 17 or later):

```
javac --release 17 -d out $(find src -name "*.java")
java -cp out app.Main
```

On Windows Command Prompt / PowerShell, compile with:

```
javac --release 17 -d out src/app/*.java src/boundary/*.java src/control/*.java src/staff/*.java src/entity/*.java
```

## How the new behaviour is dispatched

`GameController` never checks a customer's type. It calls the same three methods on every
`Customer`, and each subclass answers differently (polymorphism):

| Call | Regular | VIP | Critic |
|---|---|---|---|
| `waitingLoss()` | 8 | 5 | 12 |
| `priceFor(base)` | base | base x 0.9 | base |
| `chooseItem(menu, rng)` | seeded random | seeded random | most preparation units |

Adding a new customer type only needs a new subclass and one line in `ArrivalSchedule`.

## Stage comparisons

In the code, every change is marked with `// New file added in Stage N`, `// Added in Stage N` or `// Changed in Stage N`.

Repository: https://github.com/swaathiLaks/NTU_SC2002_OOP 

### Stage 1 -> Stage 2 (compare: https://github.com/swaathiLaks/NTU_SC2002_OOP/compare/3710f52...ac7e941a4403390c4a7463ddca02c597cdc8215c)

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


Unchanged: Statistics, ResultEvaluator, all staff classes except PrepareAction and TakeOrderAction. Stage 1 already exposed `waitingLoss()`, `priceFor()` and `chooseItem()`, so Stage 2 only added subclasses.

## Tests

See [docs/test-notes_ver2.md](docs/test-notes_ver2.md) for the reproducible victory and defeat runs
(both include a VIP and a Critic).