# AI chat log – Stage 2 review (Rachel Lim Hsin Hui, Member A: Turn engine)

**Tool:** Claude (claude.ai)
**Stage:** Stage 2 (VIP and Critic) – asked after the stage was built, to review how my part interacts with the new customer types. Same review chat as `stage-1/chat-02_Rachel_Lim_Hsin_Hui.md` and `stage-3/chat-01_Rachel_Lim_Hsin_Hui.md`.

---

## 1. Waiting penalty through polymorphism

**Prompt:**
> How do I make the waiting penalty in my turn loop call `customer.waitingLoss()` so the controller never checks customer types?

**Response summary:**
- Declare `waitingLoss()` as an abstract (or overridable) method in `Customer`; Regular returns 8, VIP 5, Critic 12.
- `Restaurant.applyWaitingPenalties()` loops over customers not yet served and calls `c.applyWaitingPenalty()`, which does `satisfaction = Math.max(0, satisfaction - waitingLoss())`.
- The controller only calls `restaurant.applyWaitingPenalties()` – it never sees which subclass a customer is.
- Java chooses the right version at runtime (dynamic binding), so adding a new customer type needs **no change** to this loop.
- What to avoid: `if (c instanceof VIPCustomer) ...` or a `switch` on a type string – graders look for this specifically.

**How I used it:** To check our penalty code and explain polymorphism in the report and sequence diagram.

---

## 2. What should change in GameController for Stage 2

**Prompt:**
> Ideally, what should change in GameController between Stage 1 and Stage 2, and how do I explain that it barely changed?

**Response summary:**
- Ideally **nothing**, or only display text. New customer types are new subclasses plus a change to the arrival schedule/setup that creates them.
- Possible small, justified changes: showing the customer type on the dashboard (UI), or the setup wiring.
- How to explain it: "Stage 2 added two classes, `VIPCustomer` and `CriticCustomer`. GameController was unchanged because it only calls methods on the abstract `Customer`. This shows the Open/Closed Principle – the design was open to new customer types without editing the turn logic."
- Back it up with the compare link showing GameController isn't in the changed file list.

**How I used it:** For the Stage 1 → 2 change analysis and the SOLID section.

---

## 3. Writing the Stage 1 → 2 change analysis

**Prompt:**
> How do I write the Stage 1 → 2 change analysis for the README (new files, changed files, reasons, compare link)?

**Response summary:**
- Get the file list from Git: `git diff --stat stage-1 stage-2` (or `--name-status` for added/modified/deleted).
- Write a table:
  | File | Change | Reason |
  |---|---|---|
  | `VIPCustomer.java` | Added | New type: loses 5 per turn, 10% off bill |
  | `CriticCustomer.java` | Added | New type: loses 12, our documented Critic rule |
  | `ArrivalSchedule.java` / setup | Modified | Replaces some regular arrivals with VIP and Critic, still 6 arrivals |
- Add one or two sentences on what *didn't* change and why.
- Compare link: `https://github.com/<owner>/<repo>/compare/stage-1...stage-2`.
- Keep each reason short and specific; avoid "updated code".

**How I used it:** As a template for the README section.

---

## 4. Tests proving VIP and Critic work through polymorphism

**Prompt:**
> What test runs prove VIP and Critic behave correctly through polymorphism?

**Response summary:**
- Unit-style checks on each subclass: after one waiting turn, Regular 100 → 92, VIP 100 → 95, Critic 100 → 88.
- Bill check: VIP Burger 1500 → 1350 cents; Critic and Regular pay full price.
- Critic's special behaviour: a test that shows your documented rule (e.g. always orders Pasta).
- A scripted run with at least one VIP and one Critic among the six arrivals, reproducing a victory and a defeat.
- Point to the code evidence: the penalty and pricing calls go through `Customer` references, with no `instanceof` in the controller.

**How I used it:** To plan our Stage 2 rule checks and evidence.
