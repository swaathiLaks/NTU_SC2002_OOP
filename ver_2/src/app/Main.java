package app;

import boundary.GameCLI;
import control.GameConfig;
import control.GameController;

import java.util.Scanner;

import java.util.concurrent.atomic.AtomicBoolean; 

// Entry point of Restaurant Rush. Wires the controller to the CLI
public class Main {
    // Creates the game controller and starts the CLI game loop
    public static void main(String[] args) {
        AtomicBoolean finished = new AtomicBoolean(false);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!finished.get()) System.out.println("\u001B[0m\n" + GameCLI.EXIT_MESSAGE);
        }));
        GameController controller = new GameController(GameConfig.base());
        new GameCLI(controller, new Scanner(System.in)).run();
        finished.set(true);
    }
}