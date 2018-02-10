package Battleships.Board;

import Battleships.Coordinates.Point.Point;
import Battleships.Ship.Ship;

class Field {
    private Point coords;
    private Ship ship;
    private boolean isShot = false;
    private boolean touchesShip = false;
    private boolean touchesSunkenShip = false;
    private boolean lastMove = false;

    public Field(Point coordinates) {
        coords = coordinates;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public Ship getShip() {
        return ship;
    }

    public boolean hasShip() {
        return ship != null;
    }

    public void shoot() {
        isShot = true;
        lastMove = true;
    }

    public boolean isTouchingShip() {
        return touchesShip;
    }

    public void setTouchesShip() {
        touchesShip = true;
    }

    public Point getCoords() {
        return coords;
    }

    public boolean isShot() {
        return isShot;
    }

    public boolean isTouchingSunkenShip() {
        return touchesSunkenShip;
    }

    public void setTouchesSunkenShip() {
        touchesSunkenShip = true;
    }

    public boolean isLastMove() {
        return lastMove;
    }

    public void resetIsLastMove() {
        this.lastMove = false;
    }
}
