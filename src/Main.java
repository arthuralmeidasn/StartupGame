import ui.ConsoleApp;
import java.util.*;
import engine.GameEngine;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== Startup Game (Console) ===");

        GameEngine game = new GameEngine();
        game.executar();

    } 
}