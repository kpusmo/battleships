package Battleships.Player;

import Battleships.Coordinates.Direction.Direction;
import Battleships.Coordinates.Point.Point;

import java.util.Random;

public class PlayerAI extends Player {
    private Random rand;

    public PlayerAI(int boardSize) {
        super(boardSize);
        rand = new Random();
        showOutput = false;
    }

    @Override
    public void chooseName(int index) {
        name = "Player AI";
    }

    @Override
    public Point chooseShipStartPoint() {
        return getRandomPoint();
    }

    @Override
    public Direction chooseShipDirection() {
        return Direction.factory(rand.nextInt(4));
    }

    @Override
    public Point shoot() {
        return getRandomPoint();
    }

    private Point getRandomPoint() {
        return new Point(rand.nextInt(board.getBoardSize()), rand.nextInt(board.getBoardSize()));
    }
}
