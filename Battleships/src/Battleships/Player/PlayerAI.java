package Battleships.Player;

import Battleships.Coordinates.Direction.Direction;
import Battleships.Coordinates.Point.Point;

import java.util.ArrayList;
import java.util.Random;

public class PlayerAI extends Player {
    private static Random rand = new Random();

    public PlayerAI(int boardSize) {
        super(boardSize);
        showOutput = false;
        randomShipPlacement = true;
    }

    @Override
    public void chooseName() {
        name = "Player AI";
    }

    @Override
    public Point chooseShipStartPoint() {
        return getRandomPoint();
    }

    @Override
    public Direction chooseShipDirection() {
        return getRandomDirection();
    }

    @Override
    public Point shoot() {
        if (lastHits.size() > 0) {
            ArrayList<Point> availablePoints = enemyBoard.getShootPointsAroundGiven(lastHits);
            return lastShootPoint = availablePoints.get(rand.nextInt(availablePoints.size()));
        }
        return lastShootPoint = getRandomPoint();
    }

    @Override
    public void setShipPlacementMode() {
        return;
    }

    public void clearScannerBuffer() {
    }
}
