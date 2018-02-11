package Battleships.Player;

import Battleships.Board.Board;
import Battleships.Coordinates.Direction.Direction;
import Battleships.Coordinates.Direction.InvalidDirectionInitializerException;
import Battleships.Coordinates.Point.Point;

import java.util.Random;

abstract public class Player {
    protected Board board;
    protected String name;
    protected boolean showOutput;
    protected Random rand;
    protected int hitCount;
    protected boolean randomShipPlacement;

    public Player(int boardSize) {
        board = new Board(boardSize);
        hitCount = 0;
        rand = new Random();
    }
    abstract public Point chooseShipStartPoint();
    abstract public Direction chooseShipDirection() throws InvalidDirectionInitializerException;
    abstract public Point shoot();
    abstract public void chooseName(int index);
    abstract public void setShipPlacementMode();

    public Board getBoard() {
        return board;
    }

    public boolean getShowOutput() {
        return showOutput;
    }

    public void hit() {
        ++hitCount;
    }

    public int getHitCount() {
        return hitCount;
    }

    public String getName() {
        return name;
    }

    protected Point getRandomPoint() {
        return new Point(rand.nextInt(board.getBoardSize()), rand.nextInt(board.getBoardSize()));
    }

    protected Direction getRandomDirection() {
        return Direction.factory(rand.nextInt(4));
    }

    public boolean getRandomShipPlacement() {
        return randomShipPlacement;
    }
}
