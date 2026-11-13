# AI chat log – Stage 1 review (Rachel Lim Hsin Hui, Member A: Turn engine)

**Tool:** Claude (claude.ai)
**Stage:** Stage 1 (base game) – asked after the stage was built, to review and deepen my understanding of my part. Same review chat as `stage-2/chat-01_Rachel_Lim_Hsin_Hui.md` and `stage-3/chat-01_Rachel_Lim_Hsin_Hui.md`.

---

## 1. Invalid Waiter/Chef choices

**Prompt:**
> How do I make an invalid Waiter or Chef choice explain why it failed and re-ask without using up the staff member's task?

**Response summary:**
- Every staff action returns a result object instead of printing or throwing, e.g. `ActionResult(boolean ok, String reason)`.
- Validate **before** changing any state: check the target exists, is in the right status (e.g. order must be READY from an earlier turn to serve), and a table is free to seat. If a check fails, return `ActionResult.fail("Order #3 is not ready yet")` without touching anything.
- Only mark the staff member as having acted (`actedThisTurn = true`) when the result is `ok`.
- In the controller, wrap the prompt in a loop:
  ```java
  ActionResult r;
  do {
      StaffAction a = ui.chooseWaiterAction(waiter.availableActions(restaurant));
      r = a.execute(restaurant, turn);
      if (!r.ok()) ui.showError(r.reason());
  } while (!r.ok());
  ```
- "Wait" is always a valid option, so the loop can always end.
- The UI only displays the reason; the rule check lives in the domain/staff classes.

**How I used it:** To check our validation flow and explain it in the report and sequence diagram (the re-ask `loop` frame).

---

## 2. Splitting GameController and Restaurant

**Prompt:**
> How should GameController and Restaurant split responsibilities, so the controller runs the turn and Restaurant holds the rules?

**Response summary:**
- **GameController (control):** knows *when* things happen – runs the 18 turns, calls the six steps in order, asks the UI for choices, passes them to staff, and triggers the result check at the end. It should contain no game rules like penalties or payment conditions.
- **Restaurant (entity/rules):** knows *what is allowed* and holds the state – tables, queue, customers, revenue, paid count. Methods like `seatCustomer()`, `finishEating(turn)`, `recordPayment()`, `applyWaitingPenalties()`, `handleDeparture()`.
- **Staff classes:** decide whether *their own action* is valid and call Restaurant to carry it out.
- **UI:** shows a snapshot and reads input only.
- Quick test: if a method mentions a number from the rules (8, 18, 100, cents), it probably belongs in Restaurant, a customer, or the objective – not the controller.
- This split supports Single Responsibility in the design discussion and keeps the controller almost unchanged in later stages.

**How I used it:** To check our class responsibilities and write the Control package section.

---

## 3. Money in integer cents

**Prompt:**
> How do I store money in integer cents and only format it as dollars in the UI?

**Response summary:**
- Store every price and the revenue as `int` (or `long`) cents: Salad `900`, Burger `1500`, Pasta `1800`, target `4500`.
- Never use `double` for money – `0.1 + 0.2` errors build up and comparisons with the $45 target can fail.
- Discounts: compute in integers and round once, e.g. 10% off `Math.round(price * 90 / 100.0)` or integer `(price * 90 + 50) / 100`. Document the rounding rule.
- Convert only when displaying, in one helper in the UI layer:
  ```java
  static String money(int cents) {
      return String.format("$%d.%02d", cents / 100, cents % 100);
  }
  ```
- Tests compare cents directly, e.g. `assertEquals(4500, objective.minRevenueCents())`.

**How I used it:** To confirm our money handling and the formatting helper.

---

## 4. Testing the Section 3.3 timing example

**Prompt:**
> How do I write a test for the brief's Section 3.3 timing example (seat → order → READY → serve → pay)?

**Response summary:**
- Build a small game with a fixed setup (one customer arriving turn 1, a 1-unit dish), then drive it with **scripted choices** instead of keyboard input (a test UI or a list of pre-set actions).
- After each turn, assert the expected state:
  | Turn | Expected |
  |---|---|
  | 1 | customer SEATED, satisfaction 92 |
  | 2 | order taken and READY, satisfaction 84 |
  | 3 | SERVED, satisfaction 84 |
  | 4 | READY_TO_PAY |
  | 5 | PAID, revenue increased, table free |
- Also test the negative cases: trying to serve on the turn the dish became READY fails; the Cashier does not charge someone who became ready this turn.
- Plain `assert`-style checks in a test `main` are fine if the course doesn't require JUnit.

**How I used it:** To write rule checks for the turn timing.

---

## 5. Reproducible victory and defeat runs

**Prompt:**
> How do I make my victory and defeat runs reproducible for the TA?

**Response summary:**
- Use a deterministic food-choice rule (or `new Random(fixedSeed)`), never an unseeded `Random`.
- Save each run's manager inputs in a text file (e.g. `runs/stage-1-victory.txt`) and replay it: `java -cp out app.Main < runs/stage-1-victory.txt`.
- Save the expected output transcript next to it, and compare (`diff`) after any code change.
- In the README, give the exact command, the input file, and the expected final line (paid count, revenue, average).
- Re-run both from a clean checkout of the tag before submitting.

**How I used it:** To set up our scripted runs.

---

## 6. Checks before tagging stage-1

**Prompt:**
> What should I check before tagging `stage-1`?

**Response summary:**
- All Stage 1 features work: full six-step turn, 18 turns, both endings, invalid input re-asked, "N/A" average before anyone pays, customers leaving at 0.
- **No Stage 2 or 3 code** is on `main` yet (no VIP, Critic, Host, combos, Happy Hour).
- Victory and defeat runs reproduce exactly from a fresh clone.
- It builds from scratch with the documented command; no `.class` files committed.
- All pull requests for the stage are merged and `main` is pulled locally.
- Then: `git tag stage-1` and `git push origin stage-1`, and check the tag appears on GitHub.

**How I used it:** As a checklist for the stage-1 tag.
