package Battleships;

import Battleships.Game.Game;
import Battleships.Game.GameMode;

public class App {
    public static void main(String args[]) {
        Game game = new Game(GameMode.SINGLE_PLAYER, 4, 0, 0, 0, 1);
        game.run();
    }
}
