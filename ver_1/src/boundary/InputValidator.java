package boundary;

import java.util.Scanner;

public class InputValidator {

    /** Keeps asking until the player types a whole number between min and max. */
    public int readChoice(Scanner in, int min, int max) {
        while (true) {
            System.out.print("> ");
            if (!in.hasNextLine()) throw new IllegalStateException("Input ended");
            String line = in.nextLine().trim();
            try {
                int choice = Integer.parseInt(line);
                if (choice >= min && choice <= max) return choice;
            } catch (NumberFormatException ignored) {
                // fall through to the message below
            }
            System.out.println("Invalid choice \"" + line + "\": enter a number from " + min + " to " + max + ".");
        }
    }
}
