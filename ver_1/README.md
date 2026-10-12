# Restaurant Rush - Stage 1 (Basic operation)
Regular customers, menu, Waiter, Chef, Cashier, the full six-step turn sequence, victory and defeat.

## Build and run
From this `ver_1` folder (Java 17 or later):

javac --release 17 -d out $(find src -name "*.java")
java -cp out app.Main

On Windows Command Prompt / PowerShell, compile with:

javac --release 17 -d out src/app/*.java src/boundary/*.java src/control/*.java src/staff/*.java src/entity/*.java

## Packages
| Package | Role | Main classes |
|---|---|---|
| `app` | Entry point | `Main` |
| `boundary` | Command-line UI only (no game rules) | `GameCLI`, `InputValidator`, `GameSnapshot` |
| `control` | Turn sequence and result | `GameController`, `ArrivalSchedule`, `ResultEvaluator`, `GameConfig` |
| `staff` | Staff and the tasks they can do | `Waiter`, `Chef`, `Cashier`, `StaffAction` and its actions |
| `entity` | Restaurant state and rules | `Restaurant`, `Customer`, `Order`, `Table`, `Menu`, `Statistics` |

## Customer food choice rule
Customers choose their own food in a fixed cycle by arrival number:
C1 Pasta, C2 Burger, C3 Salad, C4 Pasta, ... (see `Customer.chooseItem`).

## Tests
See [docs/test-notes.md](docs/test-notes.md) for the reproducible victory and defeat runs.