# SC2002 Restaurant Rush Assistance – Stage 2 (Chia Kei Yin)

<https://gemini.google.com/app/a23f46c47f188117>

---

**User prompt:** moving into stage 2 now. wht do i need to focus on in the boundary views so the dashboard shows the new vip and critic customer types?

**Response:** Here are the key points for updating the dashboard in Stage 2:
- **Expose customer archetype labels:** Ensure `CustomerView` carries the customer's `type` label (`Regular`, `VIP`, `Critic`) so `GameCLI.describe()` displays it clearly in the waiting queue and occupied tables.
- **Keep views read-only:** Keep `CustomerView` and `TableView` as immutable Java `record` DTOs so the dashboard reflects Critic (`-12/turn`) and VIP (`-5/turn`) satisfaction changes without touching entity state.
- **Whole-cent VIP pricing display:** Format all revenue and bill outputs via `Pricing.format()` so the VIP 10% discount (e.g., `\$16.20` Pasta) displays cleanly in dollars and cents.

---

**User prompt:** ok, what about happy hour status on the dashboard? how should i set that up in the view layer without breaking encapsulation?

**Response:** Here is the main idea for adding Happy Hour status cleanly:
- **Use a read-only view record:** Create a `HappyHourView` record embedded inside `GameSnapshot` with boolean/integer flags (`available`, `active`, `turnsRemaining`).
- **Render from snapshot flags:** Have `GameCLI` read `snapshot.happyHour()` to display the Happy Hour status banner without ever querying `Restaurant` or `HappyHour` directly.

---

**User prompt:** i recorded the stage 2 victory and defeat runs. what are the main points i shiuld highlight when adding the vip and critic runs to the functional evidence section?

**Response:** Here are the main points to highlight for the Stage 2 runs in Section 7:
- **Stage 2 Victory run (`test_victory_run_ver2.txt`):** Highlight `4` paid customers, `\$61.20` total revenue, `69.0` average satisfaction, and `0` unhappy departures (`VICTORY`) under seed 3.
- **VIP & Critic impact:** Note Customer 2 (`VIPCustomer`) getting a locked 10% discount (`\$16.20` for Pasta) and `-5/turn` decay, while Customer 3 (`CriticCustomer`) decays at `-12/turn` and always orders Pasta (2 prep units), recording the lowest individual satisfaction (`52`).
- **Stage 2 Defeat run (`test_defeat_run_ver2.txt`):** Highlight patience differences when all staff choose `Wait` (`DEFEAT`): Critic C3 abandons earliest at Turn 15 (`-12/turn`), Regular C1 abandons at Turn 13 (`-8/turn`), and VIP C2 remains at Turn 18 with `25` satisfaction (`-5/turn`).

---

**User prompt:** also for our 15-min video, its a 5 person group and we split it like this: rachel (turn engine), swaathi (customers), negha (staff and menu), kei yin (interface), chloe (rules and pricing). can u help map out the main points for each person?

**Response:** Here is the 15-minute storyboard mapped to your 5-member group division across 7 segments:
- **Segment 1 (00:00–01:30) — Kei Yin (Interface):** System launch from submitted source code, start-of-turn UI dashboard (turn, revenue, average satisfaction, queue, tables, staff), and integer-cents pricing display.
- **Segment 2 (01:30–04:00) — Rachel (Turn Engine):** 6-step turn execution flow, manager choices, staff task constraints, and invalid-input re-asking without wasting staff turns.
- **Segment 3 (04:00–06:15) — Swaathi (Customers):** Customer and order lifecycle (seating, ordering, eating, payment, `-8/turn` queue decay) plus Stage 2 `VIPCustomer` and `CriticCustomer` behaviors.
- **Segment 4 (06:15–08:30) — Negha (Staff & Menu):** Stage 3 `Host` seating (without using the Waiter's action) and `ComboMeal` rules (combined prep units, 90% price rounded to nearest cent).
- **Segment 5 (08:30–10:45) — Chloe (Rules & Pricing):** Stage 3 `HappyHour` (2 turns active, `+15` waiting recovery, 20% discount) and exact discount stacking locked at ordering time.
- **Segment 6 (10:45–13:00) — Rachel & Chloe:** Reproducible Victory run (4+ paid, `\$45+` revenue, 60+ avg satisfaction) and Defeat run (customer abandonment on 0 satisfaction).
- **Segment 7 (13:00–15:00) — All / Kei Yin:** Git stage tag history (`stage-1`, `stage-2`, `stage-3`) and brief UML class/sequence diagram walkthrough.