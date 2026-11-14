# SC2002 Restaurant Rush – Stage 3 Chat Summary

## Your part
ArrivalSchedule, Objective, GameSetup; Stage 3 HappyHour and PriceCalculator (VIP discount first, then Happy Hour, price locked at ordering).

Teammates' parts (from your message):
- GameController and the six-step turn sequence; restaurant rules.
- Customer, Order, Table, enums; VIPCustomer and CriticCustomer; Stage 3 architecture.
- Waiter, Chef, Cashier, menu classes; Host and ComboMeal.
- ConsoleUI, input validation, money in cents, dashboard.

## What Stage 3 requires of your part
- **Happy Hour:** once per game, activated at the start of a turn, lasts that turn and the next, remaining duration displayed.
- Orders taken while active get 20% off.
- Each active turn, customers still waiting for a table or food recover 15 satisfaction at turn start (max 100).
- VIP discount first, then Happy Hour. Price locked when the order is taken (VIP Burger during Happy Hour: $15 x 0.9 x 0.8 = $10.80).
- Does not change placed orders or affect departed customers.
- **Combo:** price = 90% of component total, rounded to the nearest cent (Salad + Pasta = $24.30, 3 units). Owned by the Waiter/menu teammate; your calculator provides `comboBasePrice()`.
- ArrivalSchedule and Objective do not change in Stage 3.

## Code delivered (compiled, 18/18 checks pass)
- `HappyHour.java` – state and rules (`activate()`, `isActive()`, `getRemainingTurns()`, `getDiscountPercent()`, `applyRecovery()`, `endTurn()`, `statusText()`).
- `PriceCalculator.java` – `priceFor(base, customerDiscount, happyHourDiscount)`, `comboBasePrice()`, integer cents, round half up per step.
- `Stage3PricingTest.java` – plain-Java checks.
- `Stage3_Report_and_README_notes.md` – integration snippets, README text, report text.

## Can I just push my own files?
Yes, on a branch, but Stage 3 is only playable once teammates hook them in. You need:
- **GameController:** Happy Hour prompt at turn start, recovery for waiting customers, `endTurn()`.
- **Customer/Order:** `getDiscountPercent()` (0 / 10), satisfaction getter and setter, a "still waiting" check, locked-price field in `Order`.
- **Waiter/Cashier/ComboMeal:** Waiter calls `PriceCalculator` at order time and stores the result; Cashier reads it; ComboMeal base-price getter.
- **ConsoleUI:** activation option and `statusText()` on the dashboard.
- **Yours:** `GameSetup` creates and wires `HappyHour` and `PriceCalculator`.

Git: commit on a feature branch, open a PR, tag `stage-3` only on a runnable, integrated commit.

## Checklist after the code
1. **Integration:** share hooks with teammates, merge, confirm the full game runs; agree turn order (ask at turn start, recover immediately, `endTurn()` at turn end).
2. **Testing:** run `Stage3PricingTest`; make one reproducible Happy Hour run; test the price-lock case; confirm Stage 1 and 2 runs still work.
3. **GitHub:** tag `stage-3`; README 2 -> 3 transition (new files, changed files with reasons, compare link); build/run instructions and test notes.
4. **Report (PDF):** class diagram entries (visibility, multiplicities, encapsulation/polymorphism/composition annotations); sequence diagram for VIP + Happy Hour pricing with guards and a loop; design discussion (responsibilities, SRP, OCP, rounding trade-off, Strategy-based alternative, how timing changes results); reflection.
5. **Group submission:** record your video segment on Happy Hour and the price lock (add timestamps to the README); sign the Declaration of Original Work and your own GAI declaration; zip as `LABGroup_AssignmentGrp.zip` with source, README, PDF, video/link and declarations; check the NTULearn deadline.
6. **Final check:** diagrams match the code, game works offline, you can explain your classes without notes.

## Assignment facts to remember
- Base setting: 18 turns, 2 tables, Waiter/Chef/Cashier x 1, arrivals on turns 1, 4, 7, 10, 13, 16.
- Victory: at least 4 paid customers, revenue at least $45, average satisfaction of paid customers at least 60.
- Marks: class diagram 25, sequence diagram 20, design consideration 15, implementation 20, process/video 20.
