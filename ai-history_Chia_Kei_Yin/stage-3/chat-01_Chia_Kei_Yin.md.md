# SC2002 Restaurant Rush Assistance – Stage 3 (Chia Kei Yin)

<https://gemini.google.com/app/a23f46c47f188117>

---

**User prompt:** its integration day for ver_3 and eclipse says selection does not contain a main type, plus i cant find version 3 in run configurations. how do i fix this?

**Response:** Here are the quick steps to fix the Eclipse `ver_3` launch issue:
- **Mark source folder:** Right-click `ver_3/src` -> **Build Path -> Use as Source Folder** so Eclipse compiles it as Java source.
- **Remove duplicate build path:** Remove `ver_1/src` from the build path (or import `ver_3` separately) to prevent duplicate `app.Main` conflicts.
- **Register main class:** Run `ver_3/src/app/Main.java` via **Run As -> Java Application**.

---

**User prompt:** okay it runs now! when we merge and test stage 3 together, what are the main things each of us should verify?

**Response:** Here are the key integration checks for each teammate during your Stage 3 merge:
- **Chloe (Member E — Pricing & Rules):** Verify the `\$10.80` VIP Happy Hour Burger (`\$15.00` base $\rightarrow$ `\$13.50` VIP 10% off $\rightarrow$ `\$10.80` Happy Hour 20% off, locked at ordering).
- **Negha (Member C — Staff & Menu):** Verify the `\$24.30` Salad + Pasta `ComboMeal` (`3` prep units, 90% combined price) and independent `Host` seating.
- **Rachel (Member A — Turn Engine):** Verify `HappyHour` 2-turn duration, single-use limit, and `+15` satisfaction recovery timing (waiting customers only, capped at `100`).
- **Kei Yin (Member D — Interface):** Verify `HappyHourView` dashboard banners, Host action prompts, and `InputValidator` bounds checking on the new prompts.

---

**User prompt:** i finished recording the stage 3 victory, defeat, and invalid-input runs. what key points should i add to functional evidence for the combo and happy hour runs?

**Response:** Here are the main points to highlight when adding Stage 3 runs to Section 7 (Functional Evidence):
- **Stage 3 Victory run (`test_victory_run_ver3.txt`):** Using seed 4 with `Host` seating and Turn 4 `HappyHour` activation, highlight `4` paid customers, `\$75.10` total revenue, `78.5` average satisfaction, and `0` unhappy departures (`VICTORY`).
- **Combo & Happy Hour stacking:** Highlight Customer 2 (VIP ordering a Salad + Pasta Combo during Happy Hour) having stacked discounts locked at order creation (`\$27.00` $\rightarrow$ Combo 10% $\rightarrow$ VIP 10% $\rightarrow$ Happy Hour 20% = `\$17.50`).
- **Defeat & invalid-input runs:** Note the all-`Wait` defeat outcome (`0` paid, `\$0.00` revenue, `DEFEAT`) and show `InputValidator` catching invalid inputs (`abc`, out-of-range numbers) on the Host/Happy Hour prompts without consuming turns.
- **Test notes (`StrategySweep` & `Stage3Checks`):** Summarize the `StrategySweep.java` Host/Happy Hour trade-off table (Turn 7 Happy Hour + Host reaching `84.00` avg satisfaction) and `Stage3Checks.java` invariant checks.

---

**User prompt:** now im writing our boundary package section in the report based on the class diagram and entity section style. what key points should i include for classes, relationships, and trade-offs?

**Response:** Here are the key points to cover for the Boundary package documentation (Section 3.5):
- **Package contents:** 2 classes (`GameCLI`, `GameExitException`), `InputValidator`, and 4 immutable presentation records (`GameSnapshot`, `CustomerView`, `TableView`, `HappyHourView`).
- **Relationships:** `Main` creates `GameCLI`; `GameCLI` owns `InputValidator` (composition), drives `GameController`, and reads `GameSnapshot` (which composes/aggregates the view records).
- **Design decisions:** Using Java `record` DTOs prevents the UI from mutating entities; centralizing validation in `InputValidator` protects turn state.
- **Trade-offs:** View records duplicate some entity fields (boilerplate for safety), and `GameCLI` accumulates many terminal formatting helpers.

---

**User prompt:** for my individual reflection (8.1 and 8.2), i wrote about decoupling with records, integer cents, edge-case testing, and splitting gamecli helpers. is this consistent with stage3checks.java and strategysweep.java?

**Response:** Yes, both Section 8.1 and Section 8.2 are completely consistent with `Stage3Checks.java` and `StrategySweep.java`:
- **Alignment with `Stage3Checks.java`:** Matches integer-cent pricing checks (`2430`, `1350`, `1080` cents), VIP-then-Happy-Hour discount order, queue recovery cap at `100`, and `Restaurant` precondition exceptions that leave state intact on invalid actions.
- **Alignment with `StrategySweep.java`:** Matches headless game execution via `GameController` and `GameSnapshot` (`turn()`, `revenueCents()`, `avgSatisfaction()`, `servedCount()`, `unhappyDepartures()`) as well as scripted Victory/Defeat input generation.

---

**User prompt:** for my video segments (00:00-01:24 game overview, 04:48-06:00 victory/defeat runs, and 24s reflection at the end), what key points should i hit? only need an intro for the first part

**Response:** Here are the key talking points for your three video segments:
- **00:00 – 01:24 (Intro & Overview):** Introduce Member D and the 18-turn win goals (`4+` paid, `\$45+` revenue, `60+` avg satisfaction); compile and run `ver_3`; point out `GameSnapshot` dashboard fields and integer-cent pricing.
- **04:48 – 06:00 (Victory & Defeat Runs):** Run `test_victory_run.txt` (Seed 4, Host + Turn 4 Happy Hour $\rightarrow$ `VICTORY`) and `test_defeat_run.txt` (all `Wait` $\rightarrow$ customers abandon at 0 satisfaction $\rightarrow$ `DEFEAT`).
- **Closing Reflection (24s):** Highlight refactoring `GameCLI` to read-only records and splitting `GameCLI` formatting helpers as a future improvement.