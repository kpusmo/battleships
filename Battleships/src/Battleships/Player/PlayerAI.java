package Battleships.Player;

import Battleships.Coordinates.Direction.Direction;
import Battleships.Coordinates.Point.Point;

public class PlayerAI extends Player {
    public PlayerAI(int boardSize) {
        super(boardSize);
        showOutput = false;
        randomShipPlacement = true;
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
        return getRandomDirection();
    }

    @Override
    public Point shoot() {
        return getRandomPoint();
    }

    @Override
    public void setShipPlacementMode() {
        return;
    }
}
