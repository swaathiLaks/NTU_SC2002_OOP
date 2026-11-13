# AI chat log – Stage 3 / final review (Rachel Lim Hsin Hui, Member A: Turn engine)

**Tool:** Claude (claude.ai)
**Stage:** After Stage 3 – reviewing my own section before the report and video

**My section:** GameController and the six-step turn sequence; Restaurant rules (seating, eating, payment, abandonment); sequence diagram; GitHub setup (repo, branch rules, who merges); video segments 1 (launch) and 3 (READY → SERVED → payment).

**Purpose:** A review chat with Claude (same chat as `stage-1/chat-02_Rachel_Lim_Hsin_Hui.md` and `stage-2/chat-01_Rachel_Lim_Hsin_Hui.md`), used to check and deepen my understanding of my own part so I could explain it accurately in the report and video.

---

## 1. Order of Waiter, Chef and Cashier in the turn loop

**Prompt:**
> Walk me through how my six-step turn loop in GameController should handle the order of Waiter, Chef and Cashier actions.

**Response summary:**
- The order inside one turn is fixed by the brief, so `playTurn(turn)` should call the steps in sequence and never let one step run early:
  1. **Arrivals** – ask the arrival schedule who arrives this turn and add them to the back of the queue.
  2. **Display state** – the UI gets a read-only snapshot from the controller (no rules in the UI).
  3. **Waiter** – loop until the manager gives a *valid* choice (seat / take order / serve / wait). An invalid choice returns a failure result with a reason and does **not** use up the Waiter's task.
  4. **Chef** – same validation loop; advances one order by one unit. The Chef *can* work on an order the Waiter took this same turn, which is why the Waiter acts first.
  5. **Eating and payment** – customers served on an *earlier* turn become READY_TO_PAY (record the turn). Then the Cashier charges **one** READY_TO_PAY customer, oldest first, but only if they became ready on an earlier turn.
  6. **Waiting penalty** – every customer not yet served loses satisfaction; anyone at 0 leaves.
- After turn 18, call the victory/defeat check once and show the result.
- Key off-by-one rule: store the turn a dish became READY and only allow serving when `readyTurn < currentTurn`. Do the same with `readyToPayTurn` for the Cashier. This is what stops a dish being cooked and served, or served and paid for, in the same turn.
- Suggested shape:
  ```java
  void playTurn(int turn) {
      admitArrivals(turn);
      ui.showDashboard(snapshot());
      waiterPhase(turn);      // repeats until a valid choice
      chefPhase(turn);        // repeats until a valid choice
      restaurant.finishEating(turn);
      cashier.collectPayment(restaurant, turn);
      restaurant.applyWaitingPenalties();
  }
  ```

**How I used it:** To check that our `GameController` step order matched the brief and to explain the Waiter-before-Chef rule in the report.

---

## 2. A customer leaving at 0 satisfaction

**Prompt:**
> How should Restaurant handle a customer leaving at 0 satisfaction (cancel their order, free their table) without breaking the queue?

**Response summary:**
- Keep it in **one method** in `Restaurant` (e.g. `handleDeparture(customer)`), so the clean-up always happens together:
  - set the customer's status to LEFT
  - if they have an order, cancel it (so the Chef can't keep cooking it and it can't be served)
  - if they are at a table, free the table
  - if they are still in the queue, remove them from it
  - increase the "left unhappy" count; they are **not** counted in revenue or average satisfaction
- Don't remove customers from a list while looping over it with a for-each loop – that throws `ConcurrentModificationException`. Either collect the leavers first and remove them afterwards, or use `removeIf` / an `Iterator`.
- Using a `LinkedList`/`ArrayDeque`-style queue, removing someone from the middle keeps everyone else in the same order, so "first come, first seated" still holds.
- Penalties are applied in step 6, after payment, so a customer who paid this turn can never be wrongly penalised or removed.
- Test cases worth writing: a customer in the queue reaching 0; a seated customer with an order in preparation reaching 0 (table freed, order cancelled); two customers reaching 0 on the same turn.

**How I used it:** To check our abandonment logic and add test cases for it.

---

## 3. Checking the sequence diagram against the code

**Prompt:**
> How do I check my sequence diagram against my actual code, method by method?

**Response summary:**
- Make a checklist table with one row per arrow: *message number | caller lifeline | receiver lifeline | method name and parameters | found in code? | notes*.
- For each arrow:
  - search the code for the method, e.g. `grep -rn "placeOrder(" src/`
  - check it is defined in the **receiver's** class and called from the **caller's** class
  - check the parameter list and return type match the label and the dashed return arrow
- Check the frames match real control flow: each `loop` should correspond to a real loop (the 18 turns, the re-ask-until-valid loop), each `alt`/`opt` to a real `if`/`switch`, and each guard to the actual condition in the code (e.g. `[readyTurn < turn]`).
- Check the order of arrows top-to-bottom follows the order of calls in `playTurn()`.
- Check layering: the UI only talks to `GameController`, never directly to `Restaurant` or the customers.
- Check `<<create>>` arrows match where objects are really constructed (e.g. `new Order(...)` inside `placeOrder`).
- Do the final check **after the code freeze**, since any rename afterwards breaks the match. Update the requirement-to-message table in the report last.

**How I used it:** As a step-by-step checklist when verifying the sequence diagram against our final code.

---

## 4. GitHub branch protection and merge process

**Prompt:**
> What GitHub branch protection rules and merge process should we use, and how do I explain them in the video?

**Response summary:**
- **Rules on `main`:**
  - require a pull request before merging, with at least 1 approval
  - dismiss stale approvals when new commits are pushed
  - block force pushes and branch deletion
  - optionally require all review conversations to be resolved
- **Merge process:**
  1. pull the latest `main`, then create a feature branch (e.g. `feature/turn-loop`)
  2. make small commits with clear messages under your own account
  3. open a PR and request one teammate as reviewer
  4. the reviewer checks it builds and runs, then approves; the **author** merges
  5. delete the branch after merging
- Use a normal **merge commit** rather than squash, so each member's individual commits stay visible in the history (evidence for the process marks).
- The stage lead tags `main` once that stage's victory and defeat runs reproduce (`stage-1`, `stage-2`, `stage-3`).
- **Explaining it in the video (about 30–45 seconds):** show the branch protection settings page, then one real merged PR with its review approval, then the commit history graph and the three tags. Say in one sentence each: why `main` is protected, how a change gets reviewed, and how the tags mark each stage.

**How I used it:** To confirm our repository settings and plan what to show about the GitHub workflow in the video.

---

## 5. Explaining READY → SERVED → payment in the video

**Prompt:**
> How should I explain READY → SERVED → payment in my video segment so it's clear and accurate?

**Response summary:**
- Use **one customer** and a scripted run so the turns are reproducible, and pause on each turn's dashboard.
- Follow the fastest path for a 1-unit dish and point at the customer's status on screen at each step:
  | Turn | What to show | What to say |
  |---|---|---|
  | T | Waiter seats the customer | "The customer is seated and starts waiting for food." |
  | T+1 | Waiter takes the order, Chef cooks it → READY | "The Waiter acts before the Chef, so the Chef can cook this order in the same turn." |
  | T+2 | Waiter serves it → SERVED | "A dish that became READY last turn can only be served now, not in the same turn." |
  | T+3 | Customer → READY_TO_PAY | "They eat during the turn after being served." |
  | T+4 | Cashier charges them → PAID, table freed, revenue goes up | "The Cashier takes one payment per turn, oldest first, never someone who became ready this turn." |
- Point out that the price shown was locked when the order was taken, and that satisfaction stops dropping once the dish is served.
- Briefly mention that Pasta takes one extra turn, which is why the turn-16 customer can never pay.
- Keep it to about 1.5–2 minutes, and briefly show the matching part of the sequence diagram so the video links to the design.

**How I used it:** To plan and script my video segment 3.

---

## 6. Where the Host phase goes in the turn loop

**Prompt:**
> Where should the Host phase go in my turn loop, before or after the Waiter, and why?

**Response summary:**
- Put it **after arrivals and before the Waiter**: arrivals → Host seats one queued customer → Waiter → Chef → eating/payment → penalties.
- Why: a customer the Host seats can have their order taken by the Waiter in the same turn, so the Host actually speeds up service. If the Host acted after the Waiter, that benefit is lost until next turn.
- The Host is *in addition to* the Waiter's task, uses the same validation and re-ask loop, and seats the oldest queued customer (or the manager's pick, if your rules allow).
- Implement it as a new `hostPhase(turn)` call in `playTurn()`; the other steps don't change.
- Document the choice in the README, since the brief leaves it to you.

**How I used it:** To confirm our Host position and explain it in the report and video.

---

## 7. Happy Hour timing in the controller

**Prompt:**
> How should the controller prompt for Happy Hour at the start of a turn, track how many turns are left, and apply the +15 recovery at the right time?

**Response summary:**
- Keep the state in a `HappyHour` object: `used` (boolean) and `remainingTurns` (int).
- At the **start** of each turn, if `!used`, ask the manager whether to activate it. If yes: `used = true`, `remainingTurns = 2` (this turn and the next).
- Still at the start of each active turn, apply recovery: every customer still waiting for a table or food gets `satisfaction = Math.min(100, satisfaction + 15)`.
- While active, orders taken get 20% off; the price calculation applies the VIP 10% first, then Happy Hour, and the price is locked in the Order when it's taken.
- At the **end** of the turn, `remainingTurns--`; when it reaches 0 it's inactive. The dashboard shows the turns left.
- Tests: recovery capped at 100; the VIP Burger costs $10.80 (1500 → 1350 → 1080 cents); an order taken just after Happy Hour ends is full price; it can't be activated twice.

**How I used it:** To check our Happy Hour turn steps and tests.

---

## 8. ComboMeal and the Chef

**Prompt:**
> Does a ComboMeal change anything in how the Chef advances orders in my loop?

**Response summary:**
- No, if `ComboMeal` implements the same `MenuItem` interface. The Order asks its item for `prepUnits()`, and a combo returns the sum of its two dishes (e.g. Burger + Pasta = 3).
- The Chef still advances one unit per turn with `order.advance()`; the order becomes READY when `unitsDone == item.prepUnits()`.
- So the Chef and the turn loop need no changes – a good example of composition plus polymorphism for the report.
- Remember the extra prep turns affect timing: a 3-unit combo ordered late may not be paid by turn 18.

**How I used it:** To confirm the Chef code didn't need changing for combos.

---

## 9. Updating the sequence diagram for Stage 3

**Prompt:**
> How do I update my sequence diagram to include a Stage 3 behaviour?

**Response summary:**
- Pick one behaviour that fits your scenario. Happy Hour is a good choice since it involves a manager decision.
- Add lifelines only for the objects involved (e.g. `:HappyHour`, `:PriceCalculator`).
- Add an `opt [Happy Hour not used]` frame at the start of a turn: the controller asks the UI, the manager accepts, `HappyHour.activate()`.
- In the order step, show `PriceCalculator.quote(item, customer)` → `customer.priceFor(base)` → `happyHour.apply(price)`, and a note: "VIP discount first, then Happy Hour; price locked in Order".
- If you use the Host instead, add `:Host` and its `seat()` message before the Waiter's alt frame.
- Re-check every new arrow against the final code and update the requirement-to-message table.

**How I used it:** To plan the Stage 3 additions to my sequence diagram.

---

## 10. Compare links between stage tags

**Prompt:**
> How do I create the compare links between stage tags for the README?

**Response summary:**
- GitHub's format is `https://github.com/<owner>/<repo>/compare/<old-tag>...<new-tag>`.
- For our repo:
  - `https://github.com/swaathiLaks/NTU_SC2002_OOP/compare/stage-1...stage-2`
  - `https://github.com/swaathiLaks/NTU_SC2002_OOP/compare/stage-2...stage-3`
- Tag links for each stage: `https://github.com/swaathiLaks/NTU_SC2002_OOP/tree/stage-1` (and so on).
- Check the tags are pushed (`git push origin --tags`) and open each link to confirm it loads.
- The repo is private, so the TA must be added as a collaborator or the links won't open for them.

**How I used it:** To add the stage links to the README.

---

## 11. The Control package section of the report

**Prompt:**
> What should go in the Control package section of the report?

**Response summary:**
- **What's in the package:** GameController (plus any helpers like the setup or result evaluator), and one sentence on each one's responsibility.
- **The turn sequence:** the six steps (plus the Host and Happy Hour steps in Stage 3) and why the order matters, especially Waiter before Chef.
- **Design decisions:** controller holds no rules; the UI only talks to the controller; invalid choices handled with result objects and a re-ask loop.
- **OO principles shown:** Single Responsibility (controller vs Restaurant), Open/Closed (no controller change in Stage 2), polymorphism via `Customer` and `MenuItem`.
- **Changes across stages:** what was added in Stage 3 (`hostPhase`, Happy Hour prompt) and why it was small.
- Refer to the matching parts of the class and sequence diagrams by name or message number.

**How I used it:** As an outline for my report section.

---

## 12. Presenting video segment 1 (launching the game)

**Prompt:**
> How should I present segment 1 (launching the game) clearly?

**Response summary:**
- Keep it about 1 minute. Start from a fresh clone of the `stage-3` tag to show it really runs.
- Show the build and run commands from the README in the terminal, with the font enlarged.
- Pause on the start-up screen and first dashboard, and point out: turn number, revenue, average satisfaction ("N/A" until someone pays), queue, tables and staff options.
- Say in one sentence how to switch between stages (checking out each tag) and that runs are reproducible from the input files.
- Script: "This is Restaurant Rush. I'm building it from the stage-3 tag with the command in our README. Each turn shows the dashboard – here's the revenue, the queue and the tables – and the manager picks what each staff member does."

**How I used it:** To script my video segment 1.
