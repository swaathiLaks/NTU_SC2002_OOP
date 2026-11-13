# AI chat log – Stage 1 (Rachel Lim Hsin Hui, Member A: Turn engine)

**Tool:** Claude (claude.ai)
**Stage:** Planning and Stage 1 (design, work split, timeline, GitHub setup, class and sequence diagrams)

---

## 1. Breaking down the assignment brief

**Prompt:**
> can you break down the entire assignment for me
> *(assignment brief file attached)*

**Response summary:**
- Overview: group Java CLI turn-based restaurant management game, built in three tagged GitHub stages, graded on files, repo and a ~15-minute recorded video (no live demo).
- Marks breakdown: Class diagram 25, Sequence diagram 20, Design consideration 15, Implementation code 20, Thinking & development process 20 — noted that 80% of marks are for design and explaining it.
- Base game rules: 18 turns, 2 tables, Waiter/Chef/Cashier, six customers arriving at turns 1, 4, 7, 10, 13, 16; menu Salad $9 (1 unit), Burger $15 (1 unit), Pasta $18 (2 units); win needs ≥4 paid, ≥$45 revenue, average paid satisfaction ≥60.
- The six steps of a turn (arrivals, manager choices, kitchen & serving, eating & payment, waiting penalty, result) and a timing table for the fastest path of a 1-unit dish.
- Stage 1 (base game), Stage 2 (VIP and Critic customers via polymorphism, no `instanceof` chains), Stage 3 (Host, ComboMeal, Happy Hour) requirements.
- Deliverables list: class diagram, sequence diagram, design discussion, README, 7-segment video, zip and declarations.
- Practical tips: design for Stage 3 from day one, use a fixed seed/deterministic rule, commit steadily, and a suggested split of work.

**How we used it:** To understand the brief and marking scheme before planning.

---

## 2. Splitting the work among 5 members

**Prompt:**
> if my group members are going to do the entire assignement on our own, how should we split the work among 5 people

**Response summary:**
- Principle: each person owns one area of code **and** one written deliverable, since every member must present in the video and diagrams must match code.
- Suggested roles: A – Turn engine (+ sequence diagram), B – Customers (+ class diagram), C – Staff and menu (+ design discussion), D – Interface and pricing (+ functional evidence), E – Integration and process (README, report assembly, video, submission).
- Order of work: design together first → Stage 1 → Stage 2 → Stage 3 → code freeze → documents.
- Working rules: feature branches + reviewed pull requests, each tag must build on its own, keep diagram owner updated, weekly check-ins.

**How we used it:** Starting point for our group's role allocation.

---

## 3. Giving Member E a coding area

**Prompt:**
> can you split member E part into 5 group members and give member E a specific function to work on too

**Response summary:**
- Revised table: E now owns `ArrivalSchedule`, `Objective` (victory/defeat check), `GameSetup`, and Stage 3 `HappyHour` and `PriceCalculator`; D gives Happy Hour pricing to E to balance load.
- E's former coordination duties shared out: A – GitHub setup; B – Stage 1→2 README table; C – report assembly/proofread; D – video recording/editing; E – submission checks, zip, declarations.
- Shared duties for everyone: tests for own code, stage tagging by stage lead, own reflection, commits/PRs under own account.

**How we used it:** Final role allocation for the group.

---

## 4. Two-week timeline

**Prompt:**
> can you help me to create a detailed timeline for my 5 group member. we hope to complete everything in 2 weeks

**Response summary:**
- 14-day plan in phases: Design & skeleton (Days 1–2), Stage 1 build (Days 3–6), Stage 2 (Days 7–8), Stage 3 (Days 9–11, code freeze), Diagrams/report/video/submission (Days 12–14).
- Day-by-day task table per member (A–E), including two integration days and a full-group video recording day.
- Checkpoints: daily 15-min stand-up, PR reviews, go/no-go meetings on Days 6, 8 and 11, firm code freeze.
- Risks: little buffer, video day needs everyone, E should trial the clean-build check early.

**How we used it:** Draft schedule (superseded by entry 5).

---

## 5. Timeline starting today

**Prompt:**
> can you change the timeline to start today? we are currently having our meeting now

**Response summary:**
- Timeline shifted to start **Thu 8 Oct**, submission **Wed 21 Oct**.
- Day 1 meeting agenda: confirm roles and video segments, decide open rules (food choice, Critic behaviour, combo rule, Host before/after Waiter), agree class names and method signatures, set up GitHub repo, lock in the video slot (Tue 20 Oct), agree stand-up time.
- Updated phase table and day-by-day task table; go/no-go meetings Tue 13, Thu 15, Sun 18 Oct.

**How we used it:** Adopted as the group's working timeline.

---

## 6. What to do in the group meeting

**Prompt:**
> what should i do during my group meeting today

**Response summary:**
- ~90-minute timed plan:
  1. Roles and logistics (10 min)
  2. Decide open rules (25 min), with suggested answers (fixed food rotation, Critic always orders Pasta, combo when satisfaction < 90, Host before Waiter, VIP discount then Happy Hour, integer cents)
  3. Agree how the code fits together (35 min) — focusing on the interfaces between members' code
  4. Repo and first commit (10 min)
  5. Wrap-up (10 min)
- Checklist of outputs to leave the meeting with.

**How we used it:** Agenda for our kickoff meeting; group made its own decisions on the open rules.

---

## 7. Setting up and using GitHub

**Prompt:**
> how should i go about doing it on github

**Response summary:**
- Part 1: create the repo (Java `.gitignore`), add collaborators, protect `main` with required PR approval, push folder structure.
- Part 2: every member tests push access with a test branch.
- Part 3: daily routine — branch per task, small commits with clear messages, pull requests with one reviewer, merging `main` into the branch when needed.
- Part 4: tagging each stage (`git tag stage-1`, `git push origin stage-1`) and compare links for the README.
- Habits: commit under own account, don't edit others' files without telling them, never commit `.class` files, pull `main` daily; GitHub Desktop as an alternative.

**How we used it:** Followed to set up our repository and workflow.

---

## 8. Approaching the class diagram discussion

**Prompt:**
> how should i approach my discussion on the class diagram

**Response summary:**
- Step-by-step: pull candidate classes from the brief's nouns → give each class one job → decide inheritance hierarchies (abstract Customer, StaffMember parent, shared MenuItem type) → draw relationships and multiplicities (composition, association, inheritance) → agree how the diagram stays accurate.
- What graders look for: visibility markers, types, multiplicities, enums, notes on encapsulation/polymorphism/composition.

**How we used it:** Structure for our class diagram discussion.

---

## 9. Draft class list in point form

**Prompt:**
> can you provide a draft of what we should include in point form

**Response summary:**
- Draft class list across all stages: `ConsoleUI`, `GameController`, `GameSetup`, `Restaurant`, `Table`, `ArrivalSchedule`, `Objective`, abstract `Customer` with `RegularCustomer` / `VIPCustomer` / `CriticCustomer`, `CustomerStatus` enum, `Order`, `OrderStatus` enum, `MenuItem` interface with `Dish` and `ComboMeal`, `Menu`, abstract `StaffMember` with `Waiter` / `Chef` / `Cashier` / `Host`, `PriceCalculator`, `HappyHour`.
- Relationships to draw (composition, inheritance, interface realisation, associations with multiplicities) and notes to annotate.

**How we used it:** Starting point for discussion; edited by the group into our own Day 2 draft.

---

## 10. What to include in the sequence diagram

**Prompt:**
> what should i include for sequence diagram
> *(image of an example sequence diagram attached)*

**Response summary:**
- Pick one scenario covering arrival → order → Waiter/Chef assignment → READY → SERVED → eating → payment → result check, plus one Stage 2/3 behaviour.
- Lifelines from each layer (Manager, ConsoleUI, GameController, staff, domain objects, Stage 2/3 feature).
- Messages in turn order, return arrows, frames (`loop`, `alt`, `opt`), explanatory notes.
- Common mistakes to avoid, and a tip to map brief requirements to message numbers in the report.

**How we used it:** Checklist for planning our sequence diagram.

---

## 11. Opening a .puml file

**Prompt:**
> how do i open this
> *(.puml file attached)*

**Response summary:**
- Explained `.puml` is PlantUML text. Three ways to view it: online PlantUML server/planttext.com; VS Code with the PlantUML extension (needs Java/Graphviz, Alt+D preview); Visual Paradigm import.
- Troubleshooting: `@startuml`/`@enduml`, error line numbers, hidden `.txt` extension.

---

## 12. Using Visual Paradigm

**Prompt:**
> Visual Paradigm i need to do this

**Response summary (included web search):**
- Option 1: VPasCode, Visual Paradigm's online PlantUML editor — paste code, render, export PNG/SVG.
- Option 2: Visual Paradigm Desktop — Tools > Import > PlantUML, then tidy the layout; if not available, use the community plugin (ntua/PlantUML-VP) or redraw manually.
- Advice to check with the TA whether an editable VP model is required.

---

## 13. Constructing our sequence diagram

**Prompt:**
> *(Pasted our group's own "Sequence diagram guide for Restaurant Rush", covering the brief's requirements, marking criteria, notation, our chosen VIP scenario, lifelines, messages taken from our `GameController.java`, frames, notes and common mistakes)* … can you help me construct a sequence diagram

**Response summary:**
- Built a PlantUML file (`sequence-diagram.puml`) with PNG and SVG previews, using the lifelines and method names from our guide.
- Scenario: VIP arrives turn 4, seated; orders Pasta at turn 5 (price locked at $16.20 via `priceFor(1800)`); Chef cooks turns 5–6; manager choice on turn 7 (serve VIP vs seat arriving Critic); VIP ready to pay turn 8; pays turn 9; result evaluated after turn 18.
- Frames: loop over turns, loop re-asking invalid Waiter choices, `alt` for Waiter actions, `opt` for arrival/eating/payment/penalty/leaving, `alt` for VICTORY/DEFEAT. Four notes (polymorphism, price lock, Waiter-before-Chef, manager choice).
- Reminder to check every arrow against our real code and satisfaction numbers before submitting.

**How we used it:** Draft diagram, to be checked and edited against our final code by the sequence diagram owner, then imported into Visual Paradigm.

---

## Declaration note

All code, final diagrams, the report and the video are our group's own work. AI was used for explanation, planning and diagram guidance as recorded above; any AI-drafted material (the class list draft and sequence diagram draft) was reviewed and revised by the group against our own implementation.
