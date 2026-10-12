package boundary;

import java.util.Scanner;

// Reads and validates numeric menu choices from the player
public class InputValidator {

    // Keeps asking until the player types a whole number between min and max
    // in: source of player input
    // min: loweest valid choice
    // max highest valid choice
    // IllegalStateException: if input ends before a valid choice is entered
    public int readChoice(Scanner in, int min, int max) {
        while (true) {
            System.out.print("> ");
            if (!in.hasNextLine()) throw new IllegalStateException("Input ended");
            String line = in.nextLine().trim();
            try {
                int choice = Integer.parseInt(line);
                if (choice >= min && choice <= max) return choice;
            } catch (NumberFormatException ignored) {
                // not a number: fall through to the error message
            }
            System.out.println("Invalid choice \"" + line + "\": enter a number from " + min + " to " + max + ".");
        }
    }
}