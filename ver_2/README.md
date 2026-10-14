# Restaurant Rush - Stage 2 (Customer behaviour)

Everything from Stage 1, plus **VIP** and **Critic** customers alongside Regular customers.
Same base setting and the same win and lose criteria.

| Customer | Satisfaction loss per waiting turn | Bill | Special behaviour |
|---|---|---|---|
| Regular | 8 | Full price | - |
| VIP | 5 | 10% off | - |
| Critic | 12 | Full price | Always orders the dish with the most preparation units (Pasta) |

## Build and run

From this `ver_2` folder (Java 17 or later):

```
javac --release 17 -d out $(find src -name "*.java")
java -cp out app.Main
```

On Windows Command Prompt / PowerShell, compile with:

```
javac --release 17 -d out src/app/*.java src/boundary/*.java src/control/*.java src/staff/*.java src/entity/*.java
```

## How the new behaviour is dispatched

`GameController` never checks a customer's type. It calls the same three methods on every
`Customer`, and each subclass answers differently (polymorphism):

| Call | Regular | VIP | Critic |
|---|---|---|---|
| `waitingLoss()` | 8 | 5 | 12 |
| `priceFor(base)` | base | base x 0.9 | base |
| `chooseItem(menu, rng)` | seeded random | seeded random | most preparation units |

Adding a new customer type only needs a new subclass and one line in `ArrivalSchedule`.

## Changes from Stage 1

See [docs/stage1-to-stage2-changes.md](docs/stage1-to-stage2-changes.md). In the code, every change is
marked with `// New file added in Stage 2`, `// Added in Stage 2` or `// Changed in Stage 2`.

## Tests

See [docs/test-notes.md](docs/test-notes.md) for the reproducible victory and defeat runs
(both include a VIP and a Critic).
