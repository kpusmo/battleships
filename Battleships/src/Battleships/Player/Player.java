package Battleships.Player;

import Battleships.Board.Board;
import Battleships.Coordinates.Direction.Direction;
import Battleships.Coordinates.Direction.InvalidDirectionInitializerException;
import Battleships.Coordinates.Point.Point;

import java.util.ArrayList;

abstract public class Player implements ClearBufferInterface {
    private Board board;
    Board enemyBoard;
    String name;
    boolean showOutput;
    private int hitCount;
    boolean randomShipPlacement;
    ArrayList<Point> lastHits;
    Point lastShootPoint;
    private boolean wasLastHit;
    private boolean wasLastSunken;

    Player(int boardSize) {
        board = new Board(boardSize);
        hitCount = 0;
        lastHits = new ArrayList<>();
    }
    abstract public Point chooseShipStartPoint();
    abstract public Direction chooseShipDirection() throws InvalidDirectionInitializerException;
    abstract public Point shoot();
    abstract public void chooseName();
    abstract public void setShipPlacementMode();

    public Board getBoard() {
        return board;
    }

    public void setEnemyBoard(Board enemyBoard) {
        this.enemyBoard = enemyBoard;
    }

    public boolean getShowOutput() {
        return showOutput;
    }

    public void hit() {
        lastHits.add(lastShootPoint);
        ++hitCount;
        wasLastHit = true;
    }

    public void sunkenShip() {
        lastHits.clear();
        wasLastSunken = true;
    }

    public boolean wasLastHit() {
        return wasLastHit;
    }

    public boolean wasLastSunken() {
        return wasLastSunken;
    }

    public void resetLastHit() {
        wasLastHit = false;
    }

    public void resetLastSunken() {
        wasLastSunken = false;
        wasLastHit = false;
    }

    public int getHitCount() {
        return hitCount;
    }

    public String getName() {
        return name;
    }

    public boolean getRandomShipPlacement() {
        return randomShipPlacement;
    }

    Point getRandomPoint() {
        return Point.getRandom(board.getBoardSize());
    }

    Direction getRandomDirection() {
        return Direction.getRandom();
    }
}
