# Encapsulation issues in zip changes

**Source:** Claude (`https://claude.ai/chat/f0ef2137-2a08-4e6d-9829-4405b8d7f2a0`)  
**Date:** 10/27/26, 1:44 PM  
**Attachment:** `ver_2.zip`

---

### User
i made some changes in this zip. are there encapsulation issues left in this?

---

### Claude
*Tool activity: Ran 4 commands, read a file (`SKILL.md`, checking whether the controller and CLI bypass the model's checks).*

I've read the entity, control, staff and boundary-record classes, and I only grepped `GameCLI` for entity usage rather than reading all 20 KB. I haven't compiled it. Yes, a few encapsulation issues are left, though the structure is much better than a typical first pass.

**What's already solid:** all fields are private, and `Restaurant` hands out unmodifiable lists. `Statistics` mutators and most `Customer.mark*` methods are package-private. The CLI only touches `GameSnapshot`, `CustomerView` and `TableView`, plus the `GameResult` enum, so the boundary doesn't reach into entity state.

**What's left, most important first:**

1. **`PrepareAction` mutates an order directly (`order.advance()`).** Every other state change goes through `Restaurant`, so this one bypasses the aggregate. Add something like `Restaurant.prepare(Order)` and make `Order.advance()` package-private.

2. **Public mutators that only `Restaurant` should call:**
   * `Order.markServed()`, `markPaid()` and `cancel()` are public. Anyone can call `order.markServed()` without the customer state change or the stats update that `Restaurant.serve()` does.
   * `Customer.markServed(int)` and `markReadyToPay(int)` are public, while their siblings are package-private. Make them package-private too.
   * `Customer.setOrder()` is public and unused. It duplicates `markOrdered` and lets anyone swap a customer's order, so delete it.
   * `Table.seat()` and `Table.free()` are public, so a staff action could seat someone without updating the customer's state or the queue.
   * The `Order`, `Table`, `MenuItem` and `Menu` constructors are public. Anyone can create an order with an arbitrary price, and only `Restaurant` should create orders and tables.

3. **`Restaurant`'s public methods trust their callers.** `collectPayment(c)` doesn't check that the customer is `READY_TO_PAY` or first in the payment queue. `placeOrder` doesn't check the customer is `SEATED`, and `seatCustomer` doesn't check `WAITING`. The actions validate this today, but the entity doesn't protect its own invariants.

4. **`seatCustomer` can lose a customer.** It removes the customer from the queue before calling `t.seat(c)`. If the table is occupied, `seat` throws and the customer has already been removed. Call `t.seat(c)` first, then remove from the queue.

5. **`GameController.applyWaiterAction` and `applyChefAction` accept any `StaffAction`.** Nothing stops a chef action being passed to the waiter method. It's a minor role-encapsulation gap, and you could check that the action came from the matching options list.

Fixing 1 and 2 matters most: after that, the live entity objects that actions and controllers can reach become effectively read-only, since the mutators are only visible inside `entity`.

Unrelated to encapsulation, `Customer.ORDER_CYCLE` is unused and the `RegularCustomer` comment ("orders by the default cycle") is stale. The README also links `docs/stage1-to-stage2-changes.md`, which isn't in the zip.

I can write the patched `Order`, `Table`, `Customer`, `Restaurant` and `PrepareAction` if you'd like.