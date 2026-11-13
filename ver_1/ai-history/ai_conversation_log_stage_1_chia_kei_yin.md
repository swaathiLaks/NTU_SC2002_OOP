# SC2002 Restaurant Rush Assistance – Stage 1 (Chia Kei Yin)

<https://gemini.google.com/app/a23f46c47f188117>

---

**User prompt:** im starting on my part for the boundary layer in stage 1. heres gamecli. can u help me pull out the main points of how it works?

**Response:** Here are the main points of how `GameCLI.java` works:
- **Strict UI separation:** `GameCLI` only handles console rendering and reading user choices; no game rules or state mutations live here.
- **Read-only data flow:** `showTurnState()` reads immutable snapshots (`GameSnapshot`, `CustomerView`, `TableView`) from `GameController.snapshot()` instead of touching entity objects.
- **Turn loop & re-prompting:** `playGame()` drives `startTurn()`, staff steps (`runStaffStep()`), and `endTurn()`, looping again on failed actions without consuming a turn.
- **Integer-cent formatting:** Stores/receives revenue in cents and converts it to `\$x.yy` strings in `formatMoney()` via integer division and modulo (`cents / 100`, `cents % 100`).

---

**User prompt:** wait so how do i show customer types on the dashboard without touching the entitty classes directly?

**Response:** Here is the key idea for displaying customer types cleanly:
- **Use `CustomerView` fields:** Pass the customer's type label (e.g., `Regular`) inside the read-only `CustomerView` record returned by `GameController.snapshot()`.
- **Format inside `GameCLI.describe()`:** Render the type label alongside the customer's ID, state, satisfaction bar, and order in both the waiting queue and table views.

---

**User prompt:** oh okay got it. im also working on inputvalidator.java next, tell me the main points and how it ties into gamecli

**Response:** Here are the key points for `InputValidator.java`:
- **Bounded integer loop:** `readChoice(Scanner in, int min, int max)` loops until the player enters a whole number within the valid `min` to `max` option range.
- **Safe exception catching:** Uses `Integer.parseInt()` inside a `try-catch` block for `NumberFormatException` so non-numeric text (like `"abc"`) falls through cleanly instead of crashing.
- **Re-prompt without turn loss:** Prints `Invalid choice "<line>": enter a number from <min> to <max>.` and asks again immediately without using up a staff task.
- **Connection to `GameCLI` & exit handling:** `GameCLI` calls `readChoice()` inside `promptAction()` and catches `GameExitException` in `run()` when the player enters `"q"`, `"quit"`, or `"exit"`.

---

**User prompt:** ok, now i finished coding stage 1 and need to record the victory and defeat runs. how should i structure the txt files so its easy to replicate?

**Response:** Here is a clean two-part structure for `test_victory_run.txt` and `test_defeat_run.txt`:
- **Section 1 — Input sequence:** List the exact turn-by-turn menu choices from Turn 1 to Turn 18 so anyone can replay the run.
- **Section 2 — Final verification summary:** Record the Turn 18 outcomes (paid customers, total revenue, average satisfaction, and `VICTORY` or `DEFEAT` result) before committing with the `stage-1` Git tag.

---

**User prompt:** what about the functional evidence section in the report? what are the main points i should highlight for scripted runs, invalid-input demo, and test notes?

**Response:** Here are the main points to highlight for Section 7 (Functional Evidence) in Stage 1:
- **Scripted runs:** Highlight the fixed base setup (18 turns, 2 tables, arrivals on turns 1, 4, 7, 10, 13, 16, seed 3) and contrast the Victory run (`5` paid, `\$75.00` revenue, `74.4` avg satisfaction) with the all-`Wait` Defeat run (`0` paid, `\$0.00` revenue, `2` unhappy departures).
- **Invalid-input demo:** Show `InputValidator` catching non-numeric inputs (e.g., `abc`) and out-of-range numbers, re-prompting without burning a staff action.
- **Test notes:** Summarize the exact 6-turn lifecycle trace for Customer 1 (`WAITING` $\rightarrow$ `SEATED` $\rightarrow$ `ORDERED` $\rightarrow$ `READY` $\rightarrow$ `SERVED` $\rightarrow$ `READY_TO_PAY` $\rightarrow$ `PAID`) and the CLI replay commands.

---

**User prompt:** also for the 15-min group video, what are the basic steps and main sections we need to plan?

**Response:** Here is a basic blueprint for your 15-minute group video:
- **Main sections:** Split into sequential time-boxed segments covering game launch, turn execution and staff constraints, customer/order lifecycle, Stage 2 and Stage 3 features, reproducible Victory/Defeat runs, and Git stage tags (`stage-1`, `stage-2`, `stage-3`) with a UML walkthrough.
- **Basic recording steps:** Pre-compile the Java project in the terminal before recording, use Zoom/Teams/OBS screen sharing, and pass speaking turns cleanly according to timestamps.