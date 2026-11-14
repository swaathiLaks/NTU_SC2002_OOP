# SC2002 Restaurant Rush: chat notes

Source brief: `SC2002_Restaurant_Rush_Group_Assignment.docx`

My parts: **Stage 1** ArrivalSchedule, Objective, GameSetup. **Stage 3** HappyHour and PriceCalculator (VIP discount, then Happy Hour, price locked at ordering).

Team split:
- GameController and the six-step turn sequence; Restaurant rules (seating, eating, payment, abandonment)
- Customer, Order, Table, status enums; Stage 2 VIPCustomer and CriticCustomer; Stage 3 architecture and changes
- Waiter, Chef, Cashier, menu classes; Stage 3 Host and ComboMeal
- ConsoleUI, input validation and re-asking, money in integer cents, dashboard display
- ArrivalSchedule, Objective, GameSetup; Stage 3 HappyHour and PriceCalculator (me)

---

## 1. What do I need to do for Stage 1?

**My classes**
- **ArrivalSchedule**: answers "which customer, if any, arrives at the start of turn N". Base setting: one Regular customer on turns 1, 4, 7, 10, 13, 16 (six in total). Store it as data (turn mapped to a customer factory), not as `turn % 3` in the controller, so Stage 2 can swap two arrivals for a VIP and a Critic without touching the turn loop.
- **Objective**: checked once, after turn 18 (no early ending). Victory needs at least 4 paid customers, revenue of at least $45.00 (4500 cents) and average satisfaction of paid customers of at least 60. Compare as `sumSatisfaction >= 60 * paidCount` to avoid rounding and divide-by-zero. The UI shows "N/A" when nobody has paid.
- **GameSetup**: wiring only. Builds the Menu (Salad $9/1 unit, Burger $15/1, Pasta $18/2), 2 Tables, 1 Waiter, 1 Chef, 1 Cashier, the ArrivalSchedule and the Objective, with an 18-turn limit. No game rules.

**Whole of Stage 1**
- Domain: Regular customers, Menu items, Table, Order, waiting queue, Waiter, Chef, Cashier.
- Full 6-step turn: arrivals, manager choices, kitchen and serving, eating and payment, waiting and abandonment, result.
- Satisfaction: Regulars lose 8 per turn while waiting or seated and not served (minimum 0). At 0 they leave, the order is cancelled and the table freed.
- Order rules: Waiter acts before Chef; a dish finished this turn is served next turn at the earliest; a customer who becomes READY_TO_PAY this turn pays next turn; Cashier handles one payment per turn, oldest first.
- Invalid choices must explain why and let the manager choose again without using up the task.
- Customers choose their own food by a documented deterministic rule or seeded randomness, shown once the order is placed.
- UI only collects choices and displays state.
- No VIP, Critic, Host, Combo or Happy Hour in Stage 1.
- Tag `stage-1`; test notes with one reproducible victory run and one defeat run.

**Look-ahead for Stage 3:** the Order stores its price in cents when created, so PriceCalculator can be added later without changing how orders are paid.

---

## 2. Code

Full Stage 1 project built and tested (Java 21): `restaurant-rush-stage1.zip`.

- Victory run (`tests/victory_input.txt`): 5 paid, $66.00, average satisfaction 79.2.
- Defeat run (`tests/defeat_input.txt`): manager always waits, 0 paid.
- Invalid-input run (`tests/invalid_input.txt`): every invalid choice explains itself and is re-asked without using up the task.
- A Pasta customer's satisfaction goes 92, 84, 76, 76, 76, matching the brief's worked example.
- Not tested: the abandonment path (a customer reaching 0 satisfaction and leaving).

Food choice rule used: the n-th customer to arrive orders the n-th menu item, cycling Salad, Burger, Pasta.

Commands: `s <customer> <table>` seat, `o <customer>` take order, `v <customer>` serve, `c <customer>` Chef cooks one unit, `w` wait, `q` quit.

My files only: `ArrivalSchedule.java`, `Objective.java`, `GameSetup.java` and `GameResult.java` (the enum Objective returns).

---

## 3. Will I need my teammates' parts?

Yes, for compiling and running. I only need names and signatures, not their logic.

| My class | Needs from teammates |
|---|---|
| ArrivalSchedule | `Customer` abstract class with constructor `(int id, int arrivalTurn)`; `RegularCustomer` with the same constructor |
| Objective | `Restaurant` getters `getPaidCount()`, `getRevenueCents()`, `getSatisfactionSum()` |
| GameSetup | `Restaurant(int tableCount)`, `Menu.standard()`, `Waiter()`, `Chef()`, `Cashier()`, the `GameController(...)` constructor, `GameView` |

Agree signatures early. GameSetup is the most likely to break. The full zip can serve as a baseline. The course requires a GAI-use declaration from each member, and AI-generated code must be declared there.

---

## 4. What to do next

1. **Finish Stage 1:** compile against teammates' code; test Objective edge cases (nobody paid, exactly 4 paid, $44.99 vs $45.00, average exactly 60 vs 59); confirm one victory and one defeat run; team tags `stage-1` on a runnable commit with no Stage 2 or 3 code.
2. **Stage 2:** change ArrivalSchedule so two arrivals are a VIP and a Critic (for example turns 4 and 10), still six arrivals; update the reproducible run; note the change and reason for the README; tag `stage-2`.
3. **Stage 3:** write PriceCalculator (VIP discount first, then Happy Hour, integer cents; $15 x 0.9 x 0.8 = $10.80) and HappyHour (once per game, at the start of a turn, lasts that turn and the next, 20% off orders taken while active, waiting customers recover 15 satisfaction at turn start up to 100, show remaining duration, price locked when the order is taken); GameSetup registration changes listed with reasons; tag `stage-3`.
4. **Writing and evidence:** README stage comparisons; class diagram entries; sequence diagram parts (result check, VIP then Happy Hour pricing); design discussion (data-driven schedule gives OCP, GameSetup gives SRP, plus one rejected alternative); video segments.
5. **Submission:** signed Declaration of Original Work, GAI declaration, zip named like `FCS1_Grp1.zip` (source, README, PDF report, video or link, declarations).

---

## 5. README contents

- Repository URL; build and run instructions.
- Stage comparisons for 1 to 2 and 2 to 3: new files; existing files changed or deleted with reasons; commit or compare link.
- Video timestamps for each member's segment.
- Rules we defined: food choice, Critic behaviour, combo choice.
- Test notes: one reproducible victory run and one defeat run with exact inputs (Stage 2 run must include a VIP and a Critic).

## 6. Video (about 15 minutes, one group recording, each member explains a part)

1. The game launching from the submitted project
2. Meaningful manager choices, including a staff-constrained decision
3. A dish going READY, SERVED, then payment
4. A VIP or Critic behaviour
5. A Stage 3 feature
6. One victory and one defeat (separate runs or an accelerated replay)
7. The stage tag comparisons and the diagrams

My natural segments: 6 (ArrivalSchedule and Objective producing victory or defeat) and 5 if Happy Hour is shown. If the file is too large, submit an accessible link that stays available to graders.

---

## 7. How to put code on GitHub

One-time setup: install Git; set `user.name` and `user.email`; one teammate creates the repository and adds the others as collaborators.

New local folder:
```
git init
git add .
git commit -m "Add ArrivalSchedule, Objective, GameSetup"
git branch -M main
git remote add origin https://github.com/<owner>/<repo>.git
git push -u origin main
```

Repo already exists:
```
git clone https://github.com/<owner>/<repo>.git
# copy files into src/restaurant/... keeping the package folders
git add .
git commit -m "Add ArrivalSchedule, Objective, GameSetup"
git pull --rebase
git push
```

Tags (once the combined stage compiles and runs):
```
git tag stage-1
git push origin stage-1
```
Repeat for `stage-2` and `stage-3`. Compare links: `https://github.com/<owner>/<repo>/compare/stage-1...stage-2`.

Tips: use a branch per person and pull requests; add `out/` to `.gitignore`; HTTPS pushes need a personal access token; GitHub Desktop is an easier option.

---

## 8. How the README works

`README.md` sits in the repo root, is written in Markdown, and GitHub shows it on the repo front page. Create it, then `git add README.md`, commit and push. Update it as stages are finished.

Template:
```markdown
# Restaurant Rush
Repository: https://github.com/<owner>/<repo>

## Build and run
## Rules we defined
- Food choice rule / Critic behaviour / Combo choice rule
## Test notes
- Victory run / Defeat run (exact inputs)
## Stage comparisons
### Stage 1 -> Stage 2
- New files / Changed files (with reasons) / Deleted files / Compare link
### Stage 2 -> Stage 3
- New files / Changed files (with reasons) / Compare link
## Video timestamps
| # | Segment | Time | Presenter |
|---|---|---|---|
```
