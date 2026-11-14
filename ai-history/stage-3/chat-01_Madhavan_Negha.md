# AI communication history — Stage 3

> Transcript assembled from the supplied conversation export. The full conversation is reproduced in its original message order, including follow-ups and corrections. Unrelated conversations have been excluded. Timestamps are shown in Singapore time (SGT). Image attachments are noted where they occurred but are not embedded.


## Assignment tasks and design diagram

Original conversation: 12–13 November 2026.

### Human — 2026-11-12 13:04 SGT

Could you help me figure out what my tasks are for the report. Aside from the architecture that I have already completed, there are tasks such as section 3, section 5 and an individual reflection. Could you guide me on how I could go on about doing those sections?
---

### Assistant — 2026-11-12 13:04 SGT

I'll check the assignment brief and what we already have on your part so I can tell you exactly what's yours.
```
This block is not supported on your current device yet.
```


```
This block is not supported on your current device yet.
```

Breathe, this is less than it looks. You have **three jobs**, and in two of them you're the organiser, not the sole writer.

## What's actually yours

| Section | Your role | What that means |
|---|---|---|
| **3.3 Staff and menu package** | You draw it | One piece of the class diagram, covering the staff and menu classes. B stitches everyone's pieces together. |
| **5 Design discussion** | You collect | Everyone sends you their points; you write your own, merge the rest and make it read as one section. |
| **8 Reflection** | You assemble | You write one paragraph of your own, then paste in everyone else's and tidy it. |

Two things to confirm with your group:
- **3.1 Overview diagram** isn't struck through either, but it has no letter. It's probably B's since B integrates, so check before you assume it's yours.
- **What the strikethrough means.** I'm reading it as "not yours", but it could also mean "already done".

## What order to do it in

1. **Message your team today.** Sections 5 and 8 depend on other people, so ask now for their design points for 5.1 to 5.4 and their reflection paragraphs, with a deadline.
2. **Do 3.3 next.** B is blocked until they have your piece, and the class diagram is the biggest chunk of marks (25).
3. **Write your own parts of 5** while you wait for the others. This section is worth 15 marks.
4. **Do the reflection last.** It's the easiest and needs the project to be nearly finished anyway.

## What 3.3 needs

The brief says the diagram must match the Stage 3 code, so for your package:
- **Staff:** Waiter, Chef, Cashier and Host, plus whatever parent class or interface they share.
- **Menu:** the menu item class, ComboMeal and the menu itself.
- **For each class:** key attributes and methods with visibility (`+`, `-`, `#`).
- **Between classes:** inheritance arrows, and associations with multiplicities (for example, a ComboMeal has exactly 2 menu items).

Happy Hour and pricing are E's (3.6), so leave those out.

## Next step

Paste the staff and menu classes from your Stage 3 code (or just the class names and fields if it's long). I'll walk you through turning them into the diagram one step at a time, the same way we did before.

---

### Human — 2026-11-12 13:08 SGT

okok I'll do 3.3 now . I don't have the code but I have this diagram, please give me a format to refer to.

---

### Assistant — 2026-11-12 13:08 SGT

```
This block is not supported on your current device yet.
```

Good news: **the diagram for 3.3 already exists** (your Figure 3.3), so you're not drawing from scratch. What's left is checking it, a few small fixes, and writing the text that goes with it.

## Step 1: Get it confirmed against the code

The brief says the class diagram must match the Stage 3 code, and you can't check that without the code. Do one of these:
- Ask for the GitHub repo link and open the staff and menu files at the `stage-3` tag.
- Send Figure 3.3 to whoever wrote those classes and ask "is anything here wrong or missing?"

## Step 2: Small fixes to the figure

From what I can see in the image:
- **Missing multiplicities.** The four lines from GameController to Cashier, Host, Waiter and Chef have no numbers. Each should be `1`, since the base setting has one of each. The arrows from the action classes to Order, Table and Customer also look unlabelled.
- **ComboMeal's link to Pricing.** The Stage 2 to 3 diagram shows ComboMeal depending on Pricing, but Figure 3.3 doesn't. Add a dashed context box, or agree with E that it lives only in 3.6.
- **Annotations.** The brief wants encapsulation, polymorphism and composition pointed out. That's B's 3.7, so tell B which spots in your package are the good examples (Step 3 helps you find them).

## Step 3: Write the explanation yourself

Aim for one short paragraph per question. Answer these by looking at your figure:

1. **Staff:** Host, Waiter and Chef all have `availableActions()`, but Cashier has `collectPayment()` instead. Why? (Hint: your 3.4.2 table and step 4 of the brief's turn sequence.)
2. **StaffAction:** five classes implement one interface. What does GameController get to *not* know because of that? This is your polymorphism example.
3. **Host in Stage 3:** which existing action class did Host reuse rather than getting a new one? What does that say about how easy the design was to extend?
4. **Menu:** ComboMeal both *is a* MenuItem (the triangle arrow) and *has* two MenuItems (the diamond). Why is that useful for Order, which only knows about MenuItem?
5. **Private fields:** MenuItem's fields are all `-`. That's your encapsulation example in one sentence.

Have a go at question 1 and send me what you write. I'll tell you if it's right before you move on to the next.

---


### Human — 2026-11-12 15:25 SGT

Could you break down what I am supposed to do for me? I am having trouble understanding

---

### Assistant — 2026-11-12 15:25 SGT

Forget everything I've thrown at you so far. Here's the whole picture.

Your group is writing **one PDF report** about the game. The code is already written by others. You are not coding anything; your job is **writing three bits of that report**.

## Your three tasks

**Task 1: Section 3.3 (do this now, about 45 minutes)**
- The diagram is already in the doc (Figure 3.3), and we just confirmed it matches the code.
- What's missing is the **writing underneath it**: a few short paragraphs explaining what the picture shows.

**Task 2: Section 5, Design discussion (later)**
- Your teammates each send you a few points.
- You add your own, then paste everything into the four sub-headings (5.1 to 5.4) and tidy it.

**Task 3: Section 8, Reflection (last)**
- You write one paragraph about how the project went for you.
- You paste in everyone else's paragraphs.

Only Task 1 matters right now.

## Task 1, broken down

Under Figure 3.3, write four short paragraphs of two or three sentences each:

1. **The staff:** there are four staff. Three give the player choices; the Cashier works automatically.
2. **The actions:** every choice a staff member offers is a `StaffAction`. There are five kinds.
3. **The Host:** added in Stage 3, and it reused the existing seating action instead of needing new code.
4. **The menu:** a Menu holds MenuItems and ComboMeals, and a combo is made of two items.


## Your next step

Write paragraph 1 only, in your own words, however rough. Something like "The staff package has four classes…" and keep going for two or three sentences. Send it to me and I'll fix it up with you.

---

### Human — 2026-11-12 16:10 SGT

Hey claude, here is what I wrote, idk if it's technical enough or if it makes sense. Let me know what improvements have to be made :

Staff:

1. There are 4 main classes of staff : Cashier, Host, Waiter and Chef. 
2. The roles of the Cashier includes:
   1. Processing the payments from the customers
   2. The Cashier works autonomously, meaning that it works in the background without requiring any prompting. 
3. The roles of the Host includes:
   1. Seating customers. This is along with the Waiter who also can seat customers.  They ensure that at any one time, at most 1 queued customer is seated
   2. They also ensure that the menu always has options available to be offered to the customers, ensuring the menu is never empty
4. The roles of the Waiter include:
   1. Seat the customers :  Seat the queued customers that the host has yet to seat
   2. Get the customer’s orders:  Provide them with the options in the menu and from there, take the orders of the customers 
   3. Relay order information: send the order information to the chef for cooking 
   4. Serve the food: Orders completed by the chef , one at a time, would then be served to the customers. This would also be done on a first come first serve basis
5. The roles of the chef include
   1. Completing the orders delivered by the waiters
   2. Alerting the waiters of the completed orders

---

### Assistant — 2026-11-12 16:11 SGT

It makes sense as a description of a restaurant, and your Cashier point is correct. The main problem is that it describes what real staff do, not what the classes in the diagram do. A marker wants to see class names, method names and who makes the decision.

## What's wrong, point by point

| Your point | Problem |
|---|---|
| **Host: "menu is never empty"** | Misread of the code comment. "Menu" there means the list of choices shown to the player, not the food menu. The Host always adds a `WaitAction` so the player always has at least one option to pick. |
| **Host: "at most 1 seated at any one time"** | It's at most one **per turn**, and it's on top of the Waiter's task. |
| **Waiter: offers menu options** | Customers choose their own food by a rule. The Waiter only takes the order. |
| **Waiter: relays the order to the Chef** | This doesn't exist in the code. The Chef picks from orders already in the restaurant. |
| **Waiter: "first come first serve"** | The opposite is true. The player picks which customer to seat, take an order from or serve, which is the point of the game. Also, the Waiter does only **one** of those three tasks per turn. |
| **Chef: alerts the waiters** | This doesn't exist either. The order's status just becomes READY. The Chef advances one order by one preparation unit per turn, and the player chooses which. |

## What's missing

The idea that ties the figure together: Host, Waiter and Chef each have `availableActions()`, which returns a list of `StaffAction` choices. The player picks one and it runs. The Cashier has no such method because the player never controls it.

## How technical it should be

Here's your Cashier point rewritten at the right level, as a model:

> **Cashier** has one method, `collectPayment()`, and no `availableActions()`. This is because payment is automatic: each turn it charges at most one READY_TO_PAY customer, oldest first, with no input from the player.

The pattern is: class name, its key method, what it does per turn, and who decides (player or automatic).

## Your turn

Rewrite Host, Waiter and Chef using that same pattern, two sentences each. Drop the nested numbering and write short paragraphs instead. To be safe on the details, open `Waiter.java` and `Chef.java` and look at what `availableActions()` adds to the list. Send me the three and I'll check them.

---

### Human — 2026-11-12 17:06 SGT

wait cashier has no available action because it only has 1 method and the fact that it's automatic right? Can I write that as the reasoning or is it js the fact that it is automatic

---

### Assistant — 2026-11-12 17:06 SGT

Just the fact that it's automatic. That's the reason; having one method is the result, not a second reason.

The logic runs like this:
- `availableActions()` exists to give the player a list of choices.
- The player never makes a choice for the Cashier, because the brief says payment happens automatically.
- So there is nothing to list, and the Cashier only needs the one method that does the work, `collectPayment()`.

If you wrote "because it only has one method", a marker could fairly ask "why does it only have one method?", and the answer would be "because it's automatic" anyway.

A safe way to phrase it:

> The Cashier has no `availableActions()` because payment is automatic, so there are no choices to offer the player. It therefore needs only one method, `collectPayment()`.

---


---

### Human — 2026-11-12 22:30 SGT

I did section 2 earlier which is the part of the report that is the architecture of all the different aspects of the report. How does section 5 differ from section 2.

---

### Assistant — 2026-11-12 22:30 SGT

I haven't seen your architecture section in this chat, so I can only compare in general terms. The intended difference is:

| | Architecture section | Section 5, Design discussion |
|---|---|---|
| **Question it answers** | What is the structure? | Why is it built that way, and how good is it? |
| **Typical content** | The packages or layers (boundary, control, entity, staff), what sits in each, and how a turn flows through them. | Whether the design follows SOLID, what compromises were made, what alternative was rejected, and how player choices affect the result. |
| **Tone** | Descriptive | Evaluative: it argues and admits weaknesses. |

So 5.2, 5.3 and 5.4 shouldn't overlap with it at all, because they're judgement, not description.

The one place that probably does overlap is **5.1**, the abstractions table. If your architecture section already lists the classes and what each does, the table will repeat it. If so, there are two easy fixes:
- **Keep 5.1 and trim the architecture section** to the package level only (what each layer is for, not each class). The brief asks for this table by name under design discussion, so 5.1 is the one to keep.
- **Shorten 5.1** to the eight or so most important classes and add "see Section X for the package structure".

Paste your architecture section here and I'll tell you exactly which lines duplicate 5.1 and which to cut.

---

### Human — 2026-11-12 22:30 SGT

_[Image/file attachment was part of the original exchange; the attachment itself is not reproduced in this text transcript.]_

---

### Assistant — 2026-11-12 22:31 SGT

Section 2 says **what each class has and does**. Section 5 says **why the design is good or weak**. Only 5.1 overlaps; 5.2, 5.3 and 5.4 don't.

## The overlap

| Section 5 part | Overlap with Section 2 | What to do |
|---|---|---|
| **5.1 table** | 7 of the 12 rows repeat your Responsibility column (`StaffAction`, staff, `Cashier`, menu, `Order`, `Customer`, `Restaurant`). | Keep it, because the brief asks for this table by name. Add one line above it: "Fields and methods for each class are in Section 2.1; this table summarises responsibilities across all four layers." The other 5 rows (controller, CLI, snapshot, Happy Hour, evaluator) are new. |
| **5.2 SOLID** | None. It uses facts from 2.2 as evidence, which is fine. | Leave as is. |
| **5.3 Trade-offs** | None. | Leave as is. |
| **5.4 Player choices** | None. | Leave as is. |

## Errors in Section 2 to fix

Reading it against the Stage 3 code turned up some mistakes:

1. **Line 19, `PrepareAction`:** "do **no** preparation unit" should be "do **one** preparation unit".
2. **Line 27, Chef:** "first in first out" is misleading. The orders are *listed* oldest first, but the player picks which one to cook. Also change "and waits" to "plus a Wait option".
3. **Line 23, Waiter:** "serve a ready customer" should be "serve a READY order".
4. **Line 82, ComboMeal:** "rounded **up** to the nearest cent" should be "rounded to the nearest cent". The brief doesn't say "up".
5. **Line 81, Stage 3:** the sentence never names the Host. Start it with "The Host seats at most one queued customer per turn, in addition to the Waiter's task."
6. **Line 67, seed:** it says the seed is 3. Your Stage 2 to 3 diagram notes that `GameConfig` changed it from 3 to 4 in Stage 3 so a combo is drawn. Say "seed 3 in Stage 1" or mention the change.
7. **Line 13, `SeatAction`:** the fields should be `staffName`, `customer`, `table`. `staffName` was added in Stage 3.

## One bigger gap

The 2.1 tables describe the Stage 1 design. They have no `Host`, `ComboMeal`, `HappyHour` or `VIPCustomer`/`CriticCustomer`, and `Menu` is shown with only `items` and `getItems()`, when Stage 3 has combos and `getOrderableItems()`. The heading also promises four layers, but only staff and entity have tables.

Check with your group whether Section 2 is meant to show the final design. If so, add rows for `Host` and `ComboMeal`, update the `Menu` row, and get tables for boundary and control from whoever owns them.

---

### Human — 2026-11-12 22:31 SGT

The architecture is for stage 1, so the VIPCustomer and CriticCustomer, ComboMeal, HappyHour, have yet to be added into section 2, whereas they are present in section 5.

---

### Assistant — 2026-11-12 22:32 SGT

That changes a few things, and makes the overlap less of a concern.

## What it means for Section 5

Section 2 describes the Stage 1 design and 5.1 describes the final Stage 3 design, so they're no longer covering the same ground. Change the line above the 5.1 table to: "Section 2.1 gives the Stage 1 classes in detail; this table summarises the final Stage 3 responsibilities across all four layers."

## Revised list of fixes for Section 2

| Fix | Status |
|---|---|
| 1. `PrepareAction`: "no preparation unit" → "one preparation unit" | Still applies |
| 2. Chef: "first in first out" is misleading; "and waits" → "plus a Wait option" | Still applies |
| 3. Waiter: "serve a ready customer" → "serve a READY order" | Still applies |
| 4. ComboMeal: "rounded up" → "rounded to the nearest cent" | Still applies (it's in the Stage 3 summary in 2.2) |
| 5. Stage 3 summary: name the Host in the first sentence | Still applies |
| 6. Seed of 3 | Drop it. Correct for Stage 1. |
| 7. `SeatAction` missing `staffName` | Drop it. That field was added in Stage 3. |

The "bigger gap" about missing Host, ComboMeal and so on no longer applies either.

## One small addition

Nothing in the 2.1 headings says the tables are Stage 1, so a marker could read them as an incomplete final design. Add "(Stage 1)" to the 2.1 heading or put one sentence under it, such as "The tables below describe the Stage 1 design; later additions are summarised in 2.2 and shown in full in Section 3."

---

### Human — 2026-11-12 22:51 SGT

For the different customer type, for example CriticCustomer, VIPCustomer and RegularCustomer, could you tell me which of which methods they override.

---

### Assistant — 2026-11-12 22:51 SGT

I haven't seen the customer files, so this is worked out from your Section 2 table and the brief. Treat the "likely" rows as unconfirmed.

| Method in `Customer` | `RegularCustomer` | `VIPCustomer` | `CriticCustomer` |
|---|---|---|---|
| `waitingLoss()` (abstract) | Must override: returns 8 | Must override: returns 5 | Must override: returns 12 |
| `priceFor()` | Likely inherits it (full price) | Likely overrides it (10% off) | Likely inherits it (full price) |
| `chooseItem()` | Likely inherits it | Likely inherits it | Possibly overrides it, if the Critic's special behaviour is a food preference |
| `applyWaitingPenalty()` | Not overridden | Not overridden | Not overridden, unless the Critic's special behaviour is a reaction to waiting |

What is certain:
- **`waitingLoss()` is overridden by all three.** Your Section 2 marks it abstract, so every subclass has to supply it.
- **`applyWaitingPenalty()` lives in `Customer`** and calls `waitingLoss()` to find out how many points to subtract. That's how the turn sequence can call one method on any customer and get 8, 5 or 12 without checking the type.

What I can't tell from here:
- Whether `VIPCustomer` really overrides `priceFor()`, or the discount is done another way.
- What the Critic's special behaviour is and which method carries it. Your group defined that, and it could be in `chooseItem()`, `applyWaitingPenalty()` or a method I haven't seen.

To confirm, open `VIPCustomer.java` and `CriticCustomer.java` in the `entity` folder and look for the `@Override` lines; each one marks an overridden method. Paste the two files here if you want me to check, and it also gives us the Critic line that 5.4 is missing.

---

### Human — 2026-11-12 23:00 SGT

---
I�m done with the table and now I need to work in the SOLID part 

---

### Assistant — 2026-11-12 23:00 SGT

Noted. I've got a list of fixes for the 5.1 table and I'll hold it until you ask.

## What SOLID is

SOLID is 5 rules for good object-oriented design, one per letter. The brief wants you to say where your group's code follows each rule, with a real example, and to be honest where it doesn't ("only where genuine").

| Letter | The rule in plain words | Where your code shows it |
|---|---|---|
| **S**: Single Responsibility | Each class has one job. | `GameCLI` only shows things and reads input. `GameController` only runs the turn order. `Cashier` only collects payment. |
| **O**: Open/Closed | Add new features by adding new classes, not by editing old ones. | VIP and Critic were added as new `Customer` subclasses. `Host` was a new file that reused `SeatAction`. **Not perfect:** `GameController` and `SeatAction` had to be edited for the Host. |
| **L**: Liskov Substitution | A child class can be used anywhere its parent is expected, and nothing breaks. | A `ComboMeal` works anywhere a `MenuItem` does. A `VIPCustomer` goes through the same flow as any `Customer`. |
| **I**: Interface Segregation | Keep interfaces small, so no class is forced to have methods it doesn't need. | `StaffAction` has just 2 methods and all 5 actions use both. `Cashier` isn't forced to have `availableActions()`. |
| **D**: Dependency Inversion | Depend on interfaces, not on specific classes. | **Only partly.** `GameController` runs actions through the `StaffAction` interface, but it holds `Host`, `Waiter`, `Chef` and `Cashier` as specific classes. |

## What you're supposed to do

For each of the 5 letters, write one short paragraph with this shape:

1. Where the code follows the rule, naming the classes.
2. Where it falls short, if it does (O and D).

So 5.2 is 5 short paragraphs of two to four sentences each.

The draft I gave you for 5.2 is already written in that shape. You can either paste it in as is, or rewrite each paragraph in your own words using the table above, like you did with 5.1. If you rewrite, send me what you write and I'll check it.

---

### Human — 2026-11-12 23:15 SGT

Can you help me improve my section 5.4 based on what my groupmate has done. I missed this in my earlier upload. 

_[Image/file attachment was part of the original exchange; the attachment itself is not reproduced in this text transcript.]_

---

### Assistant — 2026-11-12 23:16 SGT

That table is exactly what 5.4 needs. It shows the same game played three ways, with different results each time.

## What the table says

| Decision | Effect |
|---|---|
| **Host seats vs Host always waits** (rows 2 and 3) | C2's order is taken on turn 4 instead of turn 6. C2's satisfaction is 85 instead of 75, and the average is 77.25 instead of 65.75. Revenue is the same. |
| **Happy Hour on turn 4 vs never** (rows 1 and 2) | C2's satisfaction rises from 85 to 90 and the average from 77.25 to 78.50. But C2's bill drops from $21.87 to $17.50, so revenue drops from $79.47 to $75.10. |

I checked the arithmetic and it's consistent: $21.87 with 20% off is $17.50, and the revenue gap of $4.37 is exactly that discount.

## Final paragraph for 5.4

Replace the last paragraph of 5.4 (the one with the two brackets) with this:

---

Table 4.1 shows these choices changing the result for 1 customer, C2, in the same run. When the Host seats C2, the Waiter takes C2's order on turn 4. When the Host always waits, the order is taken on turn 6, C2's satisfaction falls from 85 to 75, and the average satisfaction of paid customers falls from 77.25 to 65.75, which is only 5.75 points above the target of 60. Starting Happy Hour on turn 4 has the opposite effect on satisfaction, raising C2 from 85 to 90 and the average to 78.50, but it cuts C2's bill from $21.87 to $17.50 and the game revenue from $79.47 to $75.10. The reproducible victory and defeat runs are given in Section 7.3.

---

Two things to check:
- **The table number.** I've assumed this is Table 4.1, as your friend's message said. Change the number in the paragraph if it's labelled differently.
- **The last sentence.** I haven't seen Section 7.3, so confirm that it does contain the victory and defeat runs. If it doesn't, delete that sentence.

With that paragraph and the Critic sentence from my last message, 5.4 has no blanks left, and Section 5 is complete apart from anything E wants to add to the trade-offs table.

---

### Human — 2026-11-12 23:18 SGT

Based on my groupmates work that I took info from to fill up section 5, do you think these segments are redundant in the places they were originally sourced from. You can refer to the docs provided in the project briefing

---

### Assistant — 2026-11-12 23:18 SGT

No, keep them. They aren't redundant, because of how Section 5 is now written.

## Why both should stay

- **Section 5 is the summary; the originals are the evidence.** 5.2 and 5.3 give one or two lines per point and then cite 3.2.4, 3.4.4 and 3.5.4, where the detail lives: the code figures, the method counts and the stage-by-stage reasoning. If you delete the originals, those citations point to nothing.
- **The brief wants the design discussion as its own item.** It lists a table of abstractions, SOLID, trade-offs with one alternative, and player choices, and marks "Design consideration" separately (15 marks). So Section 5 has to exist with all four parts, and a marker will look for them there.
- **The originals earn marks elsewhere.** The package sections sit inside the class diagram chapter, which is worth 25 marks, and they explain the diagrams. Table 4.1 belongs to its own section; 5.4 only refers to it.
- **They're your teammates' sections.** Cutting them isn't your call to make alone.

## The one real duplicate

The rejected `ActionType` alternative appears almost word for word in both 3.4.3 and 5.3. That's two sentences, so it's harmless. If someone wants to tidy it, shorten the 3.4.3 copy to "see Section 5.3", since 5.3 is where a marker expects it.

## When to cut

The brief gives no page limit, so check the NTULearn announcement. If there is one and you're over, trim the originals, not Section 5: shorten the trade-off lists in 3.2.4, 3.4.4 and 3.5.4 to their key numbers, and keep the full explanation in one place only.


---


### Human — 2026-11-12 23:42 SGT

also do I use the quotation marks like in this for the methods and classes?

---

### Assistant — 2026-11-12 23:42 SGT

No. Those marks are backticks, which are leftover formatting symbols, not something that belongs in a report. They should come out of Section 2 as well.

## What to do

1. **Remove them all at once.** In Google Docs, press Ctrl+H (Cmd+Shift+H on Mac), put a backtick in "Find", leave "Replace with" empty, and click "Replace all".
2. **Pick one style for class and method names and use it everywhere.** There are two acceptable options:
   - **Plain text**, such as GameController and execute(). The capital letters and brackets already make them recognisable.
   - **A code font**, such as Courier New or Consolas, applied only to the names.

## Which one to pick

Match your teammates. The sections you pasted earlier (3.2.4, 3.4.4, 3.5.4) came through as plain text with no backticks, so look at one of them in the doc. If their names are in a code font, use the same font; if they're plain, go plain. Plain is also the faster choice if you're short on time.

Check Section 5 after pasting it too: depending on how it copied from here, the names may have arrived in a code font or with a grey background. Make them match the rest.

That screenshot also still shows "do no preparation unit" in the third row, which is one of the pinned Section 2 fixes. It should say "do one preparation unit".


