package Battleships.Player;

import Battleships.Board.Board;
import Battleships.Coordinates.Direction.Direction;
import Battleships.Coordinates.Direction.InvalidDirectionInitializerException;
import Battleships.Coordinates.Point.Point;

abstract public class Player {
    protected Board board;
    protected String name;
    protected boolean showOutput;
    private int hitCount;

    public Player(int boardSize) {
        board = new Board(boardSize);
        hitCount = 0;
    }
    abstract public Point chooseShipStartPoint();
    abstract public Direction chooseShipDirection() throws InvalidDirectionInitializerException;
    abstract public Point shoot();
    abstract public void chooseName(int index);

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
}
