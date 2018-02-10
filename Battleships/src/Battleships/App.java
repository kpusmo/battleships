package Battleships;

import Battleships.Game.Game;
import Battleships.Game.GameMode;

public class App {
    public static void main(String args[]) {
        Game game = new Game(GameMode.MULTI_PLAYER, 3, 0, 0, 1, 1);
        game.run();
    }
}
