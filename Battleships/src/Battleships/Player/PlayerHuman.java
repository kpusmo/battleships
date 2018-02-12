package Battleships.Player;

import Battleships.Coordinates.Direction.Direction;
import Battleships.Coordinates.Point.Point;

import java.util.Scanner;

public class PlayerHuman extends Player {
    private Scanner scanner;

    public PlayerHuman(int boardSize) {
        super(boardSize);
        scanner = new Scanner(System.in);
        showOutput = true;
        randomShipPlacement = false;
    }

    @Override
    public void chooseName() {
        name = scanner.nextLine();
    }

    @Override
    public Point chooseShipStartPoint() {
        if (randomShipPlacement) {
            return getRandomPoint();
        } else {
            return choosePoint();
        }
    }

    @Override
    public Direction chooseShipDirection() {
        if (randomShipPlacement) {
            return getRandomDirection();
        } else {
            return chooseDirection();
        }
    }

    @Override
    public Point shoot() {
        return choosePoint();
    }

    private Point choosePoint() {
        int x = scanner.nextInt();
        int y = scanner.nextInt();
        return new Point(x, y);
    }

    private Direction chooseDirection() {
        return Direction.factory(scanner.nextInt());
    }

    @Override
    public void setShipPlacementMode() {
        if (scanner.next().equals("0")) {
            randomShipPlacement = true;
        }
    }
}
