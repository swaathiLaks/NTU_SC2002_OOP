## Stage 1 → Stage 2

Compare: 

### New files
| File | Purpose |
|---|---|
| src/entity/VIPCustomer.java | Loses 5 per waiting turn; overrides priceFor() for 10% off |
| src/entity/CriticCustomer.java | Loses 12 per waiting turn; overrides chooseItem() to always order the dish with the most prep units |

### Existing files changed
| File | Change | Why |
|---|---|---|
| entity/Customer.java | Added abstract typeLabel() | Each subclass names its own type, so no type checks are needed elsewhere |
| entity/RegularCustomer.java | Implements typeLabel() | Required by the new abstract method |
| control/ArrivalSchedule.java | Set of arrival turns → Map of turn to customer factory | Different customer types arrive on different turns |
| control/GameController.java | Arrival message and view() include the type | Show the type in the UI |
| boundary/CustomerView.java | Added `type` field | Carry the type to the CLI |
| boundary/GameCLI.java | Version label; describe() prints the type | Display only |
| docs/test-notes.md, docs/*-input.txt, README.md | Rewritten for Stage 2 | New runs include a VIP and a Critic; the defeat input changed one line because the menu size differs |

### Deleted files
None.

### Unchanged
Restaurant, Order, Table, Statistics, ResultEvaluator, GameConfig, and all staff classes and actions. The turn rules did not change.

### Why the design held up (OCP)
Stage 1's `Customer` already exposed `waitingLoss()` (abstract), `priceFor()` and `chooseItem()`. Stage 2 only overrides them in new subclasses. The one new extension point is `typeLabel()`.