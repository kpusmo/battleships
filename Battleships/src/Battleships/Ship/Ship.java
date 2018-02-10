package Battleships.Ship;

import Battleships.Coordinates.Direction.Direction;
import Battleships.Coordinates.Point.Point;

public class Ship {
    private Direction direction;
    private Point startPoint;
    private ShipType shipType;
    private int hitCount;

    public Ship(ShipType shipType) {
        this.shipType = shipType;
        hitCount = 0;
    }

    public boolean hit() {
        ++hitCount;
        return hitCount == getLength();
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public int getLength() {
        return shipType.getValue();
    }
}
