# Encapsulation issues in zip

**Source:** Claude (`https://claude.ai/chat/5ecb8451-eaf1-4cf8-b5b0-1d97767584e6`)  
**Date:** 10/14/26, 12:42 PM  
**Attachment:** `ver_2.zip`

---

### User
Hey can you can tell me the encapsulation issues in point form for this zip

---

### Claude
*Tool activity: Ran 3 commands, read a file (`SKILL.md`, reading source files for encapsulation issues).*

Here are the encapsulation issues I found in `ver_2`, most serious first.

#### Leaks of internal state
* `Restaurant.getMenu()` returns the live `Menu`, so callers can reach into its internals.
* `Restaurant.getStats()` returns the live `Statistics` object. Its mutators are package-private, so this is safe today, but it depends on package layout rather than the type itself.
* `Restaurant.getActiveCustomers()` returns the real `Customer` objects, including ones whose state can change.
* `Table.getOccupant()`, `Order.getCustomer()` and `Customer.getOrder()` all hand out mutable entities. Any holder can follow the chain `order -> customer -> order`.
* `Restaurant.getTables()`, `getWaitingQueue()` and `getOrders()` are wrapped in `unmodifiableList`, but the elements inside are still the real mutable objects.
* `MenuItem` is public and has a public constructor, so anyone can create items that aren't on the menu. `Menu.getItems()` exposes the list, though it is immutable (`List.of`).

#### Weak validation and invariants
* `Restaurant.serve()` and `placeOrder()` don't validate the state of their inputs. `placeOrder(c)` doesn't check that `c` is actually `SEATED`, and `collectPayment(c)` doesn't check that `c` is `READY_TO_PAY`. The only guards are in the action classes (`TakeOrderAction`, `SeatAction`), so calling `Restaurant` directly bypasses the rules.
* `Customer.markPaid()`, `markLeft()` and similar methods set state with no transition checks. `Order` enforces transitions in `advance()` and `markServed()`, but `Customer` doesn't, which is inconsistent.
* `Restaurant.seatCustomer()` doesn't verify that the customer is in the queue or that the table is free before calling `waitingQueue.remove(c)`. `Table.seat()` throws only after the queue removal has already happened, so state can be left half-updated.
* `Order.markPaid()` has no state check, unlike `markServed()`.
* `Customer.servedTurn` and `readyToPayTurn` default to `0` and are exposed through getters before they're set.

#### Business logic outside the entity
* `GameController.processEating()` decides when a customer has finished eating by reading `getState()` and `getServedTurn()`. That rule belongs in `Customer` or `Restaurant`.
* `Cashier` applies the "must wait one turn" rule by reading `getReadyToPayTurn()` from outside. It also depends on `Restaurant.peekNextToPay()` returning a live `Customer`.
* `Waiter.availableActions()` and the action classes (`SeatAction`, `TakeOrderAction`, `ServeAction`, `PrepareAction`) inspect `getState()` and `getStatus()` from outside. They also hold direct references to `Order`, `Customer` and `Table`. This is "ask, don't tell": the entities should answer something like `canBeSeated()` or `isReady()`.
* The actions cache `Order`, `Table` and `Customer` references when they're created. These can go stale if state changes between creating the action and executing it.
* `PrepareAction.describe()` and `execute()` read `getRemainingUnits()` and `getStatus()` directly from the order.

#### Package-level access
* Package-private mutators (`markServed`, `markPaid`, `advance`, `recordPayment` and others) rely on every class in `entity` being trusted. Any new class in that package can call them.
* `Pricing.percentOf` is package-private, while `Pricing.format` is public. It's a small mix of visibility on the same utility class.
* `Restaurant.prepare(order)` exists only so `Order.advance()` can stay package-private. It is a pass-through method that adds no checks of its own.

#### Boundary layer
* `GameCLI` imports `GameResult` and `Pricing` directly from `entity`, so the "UI never touches entities" claim isn't fully true.
* `GameCLI` and `GameController` pass `StaffAction` objects around, and each action holds entity references. So the UI does indirectly hold entity objects through `StaffAction`.
* `GameController.getConfig()` exposes the config. It's a record, so it's safe, but the CLI could get what it needs from the snapshot instead.
* `CustomerView` uses `CustomerState` and `OrderStatus` enums from `entity`. That's acceptable, but it still couples the boundary to entity internals.

#### Smaller issues
* `ArrivalSchedule` hard-codes its arrival map internally, with no way to inject it. This is mostly a testability and configurability problem rather than a leak.
* `Restaurant` creates its own `Menu` and `Random` internally, with no injection, so it is tightly coupled to them.
* In `Customer`, `chooseItem()` is public and takes a `Random`. That exposes how the choice is made.

If you'd like, I can turn this into a prioritised fix list with code changes, for example returning view objects or making state transitions self-validating.