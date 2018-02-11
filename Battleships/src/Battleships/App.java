package Battleships;

import Battleships.Game.Game;
import Battleships.Game.GameMode;

public class App {
    public static void main(String args[]) {
        Game game = new Game(GameMode.SINGLE_PLAYER, 20, 3, 5, 7, 10);
        game.run();
    }
}
