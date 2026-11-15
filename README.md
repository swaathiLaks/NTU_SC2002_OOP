# Restaurant Rush: SC2002 Group Project (BACF1 Group 4)

A turn-based restaurant management game for the command line, written in Java 17 and built in three tagged stages.

Repository: https://github.com/swaathiLaks/NTU_SC2002_OOP

## Stages

| Stage | Folder | Release tag | What it adds |
|---|---|---|---|
| 1. Basic operation | [ver_1](ver_1/README.md) | [v1.0.0](https://github.com/swaathiLaks/NTU_SC2002_OOP/releases/tag/Stage-1) | Regular customers, menu, Waiter, Chef, Cashier, full turn sequence, victory and defeat |
| 2. Customer behaviour | [ver_2](ver_2/README.md) | [v2.0.0](https://github.com/swaathiLaks/NTU_SC2002_OOP/releases/tag/Stage-2) | VIP and Critic customers |
| 3. New role and menu policy | [ver_3](ver_3/README.md) | [v3.0.0](https://github.com/swaathiLaks/NTU_SC2002_OOP/releases/tag/Stage-3) | Host, ComboMeal, Happy Hour |

Each folder builds and runs on its own. See the README inside each folder for the build commands and test runs.

## Stage comparisons

| Transition | Compare view | File-by-file table with reasons |
|---|---|---|
| Stage 1 → 2 | [v1.0.0...v2.0.0](https://github.com/swaathiLaks/NTU_SC2002_OOP/compare/3710f5247f01d43ae8c45f09ac3e0e12a57d713d...27ebbd7b5dd381a39c849849a17c79b3dd801eeb) | [ver_3/README.md, Stage comparisons](ver_3/README.md#stage-comparisons) |
| Stage 2 → 3 | [v2.0.0...v3.0.0](https://github.com/swaathiLaks/NTU_SC2002_OOP/compare/7bde336a99b71d1862ca93ffdef19a85df8f5cd2...8c10fec2342e5ba5218f5e62c62e2e333d5d55a2) | [ver_3/README.md, Stage comparisons](ver_3/README.md#stage-comparisons) |

## Diagrams

All diagrams are SVG files in [docs/svg_format](https://github.com/swaathiLaks/NTU_SC2002_OOP/tree/main/docs/svg_format).

| Group | Diagram | Link |
|---|---|---|
| Class diagrams: full | Stage 1 full class diagram | [detail_full_stage1.svg](https://github.com/swaathiLaks/NTU_SC2002_OOP/blob/main/docs/svg_format/detail_full_stage1.svg) |
|  | Stage 2 full class diagram | [detail_full_stage2.svg](https://github.com/swaathiLaks/NTU_SC2002_OOP/blob/main/docs/svg_format/detail_full_stage2.svg) |
|  | Stage 3 full class diagram | [detail_full_stage3.svg](https://github.com/swaathiLaks/NTU_SC2002_OOP/blob/main/docs/svg_format/detail_full_stage3.svg) |
| Class diagrams: overview | Stage 1 overview | [overview_stage1.svg](https://github.com/swaathiLaks/NTU_SC2002_OOP/blob/main/docs/svg_format/overview_stage1.svg) |
|  | Stage 2 overview | [overview_stage2.svg](https://github.com/swaathiLaks/NTU_SC2002_OOP/blob/main/docs/svg_format/overview_stage2.svg) |
|  | Stage 3 overview | [overview_stage3.svg](https://github.com/swaathiLaks/NTU_SC2002_OOP/blob/main/docs/svg_format/overview_stage3.svg) |
| Class diagrams: by package | Entity package, Stage 1 | [detail_entity_stage1.svg](https://github.com/swaathiLaks/NTU_SC2002_OOP/blob/main/docs/svg_format/detail_entity_stage1.svg) |
|  | Entity package, Stage 2 | [detail_entity_stage2.svg](https://github.com/swaathiLaks/NTU_SC2002_OOP/blob/main/docs/svg_format/detail_entity_stage2.svg) |
|  | Entity package, Stage 3 | [detail_entity_stage3.svg](https://github.com/swaathiLaks/NTU_SC2002_OOP/blob/main/docs/svg_format/detail_entity_stage3.svg) |
|  | Staff package and Menu classes, Stage 3 | [detail_staff_menu_stage3.svg](https://github.com/swaathiLaks/NTU_SC2002_OOP/blob/main/docs/svg_format/detail_staff_menu_stage3.svg) |
|  | Control package, Stage 3 | [detail_control_stage3.svg](https://github.com/swaathiLaks/NTU_SC2002_OOP/blob/main/docs/svg_format/detail_control_stage3.svg) |
|  | Boundary package, Stage 3 | [detail_boundary_stage3.svg](https://github.com/swaathiLaks/NTU_SC2002_OOP/blob/main/docs/svg_format/detail_boundary_stage3.svg) |
| Stage changes | Changes from Stage 1 to Stage 2 | [changes_stage1_to_stage2.svg](https://github.com/swaathiLaks/NTU_SC2002_OOP/blob/main/docs/svg_format/changes_stage1_to_stage2.svg) |
|  | Changes from Stage 2 to Stage 3 | [changes_stage2_to_stage3.svg](https://github.com/swaathiLaks/NTU_SC2002_OOP/blob/main/docs/svg_format/changes_stage2_to_stage3.svg) |
| Sequence diagram | Complete sequence diagram for stage 3 | [seq_diagram.svg](https://github.com/swaathiLaks/NTU_SC2002_OOP/blob/main/docs/svg_format/seq_diagram.svg) |
|  | Part A: arrival and manager decisions | [seq_4_1a_arrival_and_decisions.svg](https://github.com/swaathiLaks/NTU_SC2002_OOP/blob/main/docs/svg_format/seq_4_1a_arrival_and_decisions.svg) |
|  | Part B: order and kitchen | [seq_4_1b_order_and_kitchen.svg](https://github.com/swaathiLaks/NTU_SC2002_OOP/blob/main/docs/svg_format/seq_4_1b_order_and_kitchen.svg) |
|  | Part C: cooking turns | [seq_4_1c_cooking_turns.svg](https://github.com/swaathiLaks/NTU_SC2002_OOP/blob/main/docs/svg_format/seq_4_1c_cooking_turns.svg) |
|  | Part D: serving, payment and result | [seq_4_1d_serve_pay_result.svg](https://github.com/swaathiLaks/NTU_SC2002_OOP/blob/main/docs/svg_format/seq_4_1d_serve_pay_result.svg) |
| Other diagrams | Turn flow | [turn_flow.svg](https://github.com/swaathiLaks/NTU_SC2002_OOP/blob/main/docs/svg_format/turn_flow.svg) |
|  | Pricing pipeline | [pricing_pipeline.svg](https://github.com/swaathiLaks/NTU_SC2002_OOP/blob/main/docs/svg_format/pricing_pipeline.svg) |
|  | Customer states | [states_customer.svg](https://github.com/swaathiLaks/NTU_SC2002_OOP/blob/main/docs/svg_format/states_customer.svg) |
|  | Order states | [states_order.svg](https://github.com/swaathiLaks/NTU_SC2002_OOP/blob/main/docs/svg_format/states_order.svg) |

## Video

Video link: [Restaurant Rush video (Google Drive)](https://drive.google.com/drive/folders/1gDDhtjWeI6E6LjZikEXDJYSjWbZqZVoY?usp=sharing) (13:50)

| # | Time | Section | Speaker | What is shown |
|---|---|---|---|---|
| 1 | 00:00 – 01:01 | Game overview | Chia Kei Yin | Game launched from the submitted source; win targets; the start-of-turn dashboard drawn by `GameCLI` from read-only snapshot records; prices stored as integer cents |
| | 01:01 – 01:29 | The three stages | Madhavan Negha | Stage 1 base game, Stage 2 VIP and Critic customers, Stage 3 Host, ComboMeal and Happy Hour; each stage is a separate runnable folder |
| 2 | 01:29 – 02:37 | Gameplay demo: the six-step turn | Madhavan Negha | Stage 1 turn by turn; invalid input re-prompts without using up a staff task; Waiter acts before Chef; a constrained Waiter choice costs C1 8 satisfaction; step 6 checks the targets after turn 18 |
| | 02:37 – 03:45 | One customer, arrival to payment | Swaathi Lakshmanan | VIP C2 in Stage 3 beside the sequence diagram: Host seats C2, VIP then Happy Hour discount, Order 2 cooked to READY, SERVED, eats, Cashier collects $17.50 |
| | 03:45 – 04:45 | Victory and defeat runs | Chia Kei Yin | Reproducible Stage 3 victory (4 paid, $75.10, average satisfaction 78.5) and defeat run (customers leave at 0 satisfaction; 0 paid, $0.00) |
| 3 | 04:45 – 05:24 | Evidence of stage changes | Madhavan Negha | GitHub compare view between stages; Stage 1 class diagram |
| | 05:24 – 05:57 | Stage 2: VIP and Critic | Swaathi Lakshmanan | Stage 1 → 2 class changes; `VIPCustomer` and `CriticCustomer` code overriding `waitingLoss()`, `priceFor()` and `chooseItem()`; no type checks in the controller |
| | 05:57 – 07:07 | Stage 3 in play: Host and turn rules | Rachel Lim Hsin Hui | Stage 3 victory run from turn 1: Happy Hour offered before staff act, invalid input re-prompting, Host seats C1, Waiter before Chef, payment recorded |
| | 07:07 – 07:44 | Stage 3: Happy Hour | Chloe Lee Si Xuan | Happy Hour rules: only before staff act, once per game, 2 turns, +15 satisfaction capped at 100 |
| 4 | 07:44 – 08:20 | Use case: Customer places an order | Rachel Lim Hsin Hui | Actor, preconditions, main flow and alternative flow on the sequence diagram; VIP discount then Happy Hour discount |
| | 08:20 – 10:52 | Design: OODP, SOLID and change maps | Chloe Lee Si Xuan | Four layers; encapsulation, polymorphism, composition; SOLID with limits; change maps: Stage 1 → 2 (3 added, 12 modified, 20 unchanged) and Stage 2 → 3 (4 added, 12 modified, 23 unchanged); 15 files never changed |
| | 10:52 – 11:55 | Sequence diagram linked to code | Chloe Lee Si Xuan | Pricing pipeline; `priceFor()` 2430 → 2187 → 1750 in `Restaurant.placeOrder()`; price locked in `Order`; Cashier collects $17.50 |
| 5 | 11:55 – 13:50 | Individual contributions and key learning | All members | Rachel (11:55), Negha (12:09), Kei Yin (12:31), Chloe (12:54), Swaathi (13:23) |

Where each required segment from the brief (Section 6.2) appears:

| Required segment | Timestamp |
|---|---|
| 1. The game launching from the submitted project | 00:00 |
| 2. Meaningful manager choices, including a decision where staff are constrained | 01:29, 05:57 |
| 3. A dish going READY → SERVED → payment | 02:37 |
| 4. A VIP or Critic behaviour | 02:37 (VIP), 05:24 (VIP and Critic code) |
| 5. A Stage 3 feature | 02:37 (Host, Happy Hour), 05:57 (Host), 07:07 (Happy Hour), 10:52 (ComboMeal pricing) |
| 6. One victory and one defeat | 03:45 |
| Design focus: OODP, SOLID and change maps | 08:20 |

## AI declarations

All five members used generative AI tools during this project, as allowed by the course's GAI policy. Each member's full AI chat history is kept on their own branch, in folders by stage (stage-1, stage-2, stage-3), and each member's signed GAI declaration form is included in the submission.

| Member | AI history |
|---|---|
| Rachel Lim Hsin Hui | [ai-history/Rachel_Lim_Hsin_Hui](https://github.com/swaathiLaks/NTU_SC2002_OOP/tree/ai-history/Rachel_Lim_Hsin_Hui/ai-history_Rachel_Lim_Hsin_Hui) |
| Chia Kei Yin | [ai-history/Chia_Kei_Yin](https://github.com/swaathiLaks/NTU_SC2002_OOP/tree/ai-history/Chia_Kei_Yin/ai-history_Chia_Kei_Yin) |
| Swaathi Lakshmanan | [ai-history/Swaathi_Lakshmanan](https://github.com/swaathiLaks/NTU_SC2002_OOP/tree/ai-history/Swaathi_Lakshmanan/ai-history_Swaathi_Lakshmanan) |
| Madhavan Negha | [ai-history/Madhavan_Negha](https://github.com/swaathiLaks/NTU_SC2002_OOP/tree/ai-history/Madhavan_Negha/ai-history_Madhavan_Negha) |
| Chloe Lee Si Xuan | [ai-history/Chloe_Lee](https://github.com/swaathiLaks/NTU_SC2002_OOP/tree/ai-history/Chloe_Lee/ai-history_Chloe_Lee) |

## Credits and libraries

- Base ASCII banner typography generated using [oh-my-logo](https://github.com/shinshin86/oh-my-logo) and adapted for this repo's CLI(s).

## Quick start (Stage 3)

```
cd ver_3
javac --release 17 -d out $(find src -name "*.java")
java -cp out app.Main
```
