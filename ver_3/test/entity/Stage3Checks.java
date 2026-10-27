package entity;

// New file added in Stage 3
// Rule and encapsulation checks for Combo meals, Happy Hour pricing and the Restaurant invariants.
// Lives in package entity so it can reach the package-private methods. Run it as described in docs/test-notes.md

import java.util.List;

public class Stage3Checks {
    static int fails = 0;
    static void check(String name, boolean ok) { System.out.println((ok ? "PASS  " : "FAIL  ") + name); if (!ok) fails++; }
    static boolean throwsISE(Runnable r) { try { r.run(); return false; } catch (IllegalStateException | IllegalArgumentException e) { return true; } }

    public static void main(String[] args) {
        // --- combo
        Menu m = new Menu();
        MenuItem salad = m.getItems().get(0), pasta = m.getItems().get(2);
        ComboMeal sp = new ComboMeal(salad, pasta);
        check("Salad+Pasta combo = $24.30", sp.getPriceCents() == 2430);
        check("Salad+Pasta combo = 3 prep units", sp.getPrepUnits() == 3);
        check("menu has 3 combos, 6 orderable", m.getCombos().size() == 3 && m.getOrderableItems().size() == 6);
        check("combo components immutable", throwsISE(() -> { try { sp.getComponents().add(salad); } catch (UnsupportedOperationException e) { throw new IllegalStateException(); } }));
        check("Critic still picks single-dish Pasta", new CriticCustomer(1).chooseItem(m, new java.util.Random(1)).getName().equals("Pasta"));

        // --- happy hour pricing: VIP first, then Happy Hour
        HappyHour hh = new HappyHour();
        check("off: price unchanged", hh.priceFor(1500) == 1500);
        hh.activate();
        int vipBurger = new VIPCustomer(1).priceFor(1500);
        check("VIP Burger = $13.50 before HH", vipBurger == 1350);
        check("VIP Burger in Happy Hour = $10.80", hh.priceFor(vipBurger) == 1080);
        check("Regular Burger in Happy Hour = $12.00", hh.priceFor(new RegularCustomer(2).priceFor(1500)) == 1200);
        check("cannot activate twice", throwsISE(hh::activate));
        check("lasts 2 turns", hh.isActive() && hh.getRemainingTurns() == 2);
        hh.endTurn(); check("after turn 1: 1 remaining, still active", hh.isActive() && hh.getRemainingTurns() == 1);
        hh.endTurn(); check("after turn 2: inactive, not available again", !hh.isActive() && !hh.isAvailable());

        // --- recovery: waiting only, capped at 100
        Restaurant r = new Restaurant(2, 1L);
        Customer a = new RegularCustomer(1), b = new RegularCustomer(2), c = new RegularCustomer(3);
        r.startTurn(1);
        r.addToQueue(a); r.addToQueue(b); r.addToQueue(c);
        r.applyWaitingPenalties();                       // everyone 92
        r.seatCustomer(a, r.getTables().get(0));
        r.placeOrder(a);                                  // a is ORDERED (still waiting for food)
        check("recovery does nothing when Happy Hour is off", r.applyHappyHourRecovery().isEmpty() && a.getSatisfaction() == 92);
        r.activateHappyHour();
        List<Customer> rec = r.applyHappyHourRecovery();
        check("recovery capped at 100 (92 + 15 -> 100)", a.getSatisfaction() == 100 && b.getSatisfaction() == 100 && rec.size() == 3);
        r.applyWaitingPenalties(); r.applyWaitingPenalties(); // 100 -> 92 -> 84
        Order o = a.getOrder();
        while (o.isInKitchen()) r.prepare(o);
        r.startTurn(2); r.serve(o);                       // a is SERVED: no longer waiting
        int satA = a.getSatisfaction();
        r.applyHappyHourRecovery();
        check("served customer does not recover", a.getSatisfaction() == satA);
        System.out.println("      (queued customer b is at " + b.getSatisfaction() + ", expected 84 + 15 = 99)");
        check("queued customer recovers +15 (84 -> 99)", b.getSatisfaction() == 99);

        // --- invariants enforced inside Restaurant
        Restaurant r2 = new Restaurant(1, 1L);
        Customer x = new RegularCustomer(1), y = new RegularCustomer(2);
        r2.startTurn(1); r2.addToQueue(x); r2.addToQueue(y);
        Table t1 = r2.getTables().get(0);
        r2.seatCustomer(x, t1);
        check("seat into occupied table throws", throwsISE(() -> r2.seatCustomer(y, t1)));
        check("failed seat leaves customer in queue", r2.getWaitingQueue().contains(y) && y.getState() == CustomerState.WAITING);
        check("order for a customer who is not seated throws", throwsISE(() -> r2.placeOrder(y)));
        check("pay before READY_TO_PAY throws", throwsISE(() -> r2.collectPayment(x)));
        check("mark ready-to-pay before served throws", throwsISE(() -> r2.markReadyToPay(x)));
        check("queue the same customer twice throws", throwsISE(() -> r2.addToQueue(x)));
        check("queued list is unmodifiable", throwsISE(() -> { try { r2.getWaitingQueue().clear(); } catch (UnsupportedOperationException e) { throw new IllegalStateException(); } }));

        System.out.println(fails == 0 ? "\nALL PASSED" : "\n" + fails + " FAILED");
        System.exit(fails == 0 ? 0 : 1);
    }
}
