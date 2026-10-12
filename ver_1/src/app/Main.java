package app;

import boundary.GameCLI;
import control.GameConfig;
import control.GameController;

import java.util.Scanner;

// Entry point of Restaurant Rush. Wires the controller to the CLI
public class Main {
    // Creates the game controller and starts the CLI game loop
    public static void main(String[] args) {
        // base game settings
        GameController controller = new GameController(GameConfig.base());
        // start the game
        new GameCLI(controller, new Scanner(System.in)).run();
    }
}