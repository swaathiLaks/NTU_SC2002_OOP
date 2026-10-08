package app;

import boundary.GameCLI;
import control.GameConfig;
import control.GameController;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GameController controller = new GameController(GameConfig.base());
        new GameCLI(controller, new Scanner(System.in)).run();
    }
}
