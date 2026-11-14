# Restaurant Rush - Stage 1 (Basic operation)
Regular customers, menu, Waiter, Chef, Cashier, the full six-step turn sequence, victory and defeat.

## Build and run
From this `ver_1` folder (Java 17 or later):

javac --release 17 -d out $(find src -name "*.java")
java -cp out app.Main
OR 
RUSH_NOCLEAR=1 java -cp out app.Main

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
Customers choose food uniformly at random from the menu using new Random(config.seed()) owned by Restaurant (see Customer.chooseItem). The same seed always gives the same choices.

## Tests
See [docs/test-notes_ver1.md](docs/test-notes_ver1.md) for the reproducible victory and defeat runs.

## AI History
- [Chia Kei Yin: AI Logs](https://github.com/swaathiLaks/NTU_SC2002_OOP/blob/ai-history/Chia_Kei_Yin/ai-history_Chia_Kei_Yin/stage-1/chat-01_Chia_Kei_Yin.md)