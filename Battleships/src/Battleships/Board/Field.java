package Battleships.Board;

import Battleships.Coordinates.Point.Point;
import Battleships.Ship.Ship;

class Field {
    private static final char MISHIT = 'O';
    private static final char HIT = 'X';
    private static final char OWN_SHIP = 'Z';
    private static final char EMPTY_FIELD = ' ';
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";

    private Point coords;
    private Ship ship;
    private boolean isShot = false;
    private boolean touchesShip = false;
    private boolean touchesSunkenShip = false;
    private boolean lastMove = false;

    public Field(Point coordinates) {
        coords = coordinates;
    }

    public String getStringToPrint(boolean isOnOwnBoard) {
        char signToDraw;
        String colorCode = "";
        if (!isShot) {
            if (hasShip()) {
                if (isOnOwnBoard) {
                    signToDraw = OWN_SHIP;
                    colorCode = ANSI_CYAN;
                } else {
                    signToDraw = EMPTY_FIELD;
                }
            } else if (touchesSunkenShip) {
                signToDraw = MISHIT;
            } else {
                signToDraw = EMPTY_FIELD;
            }
        } else if (hasShip()) {
            if (lastMove) {
                if (isOnOwnBoard) {
                    colorCode = ANSI_RED;
                } else {
                    colorCode = ANSI_GREEN;
                }
            } else {
                colorCode = ANSI_BLUE;
            }
            signToDraw = HIT;
        } else {
            if (lastMove) {
                colorCode = ANSI_YELLOW;
            }
            signToDraw = MISHIT;
        }
        return colorCode + signToDraw + ANSI_RESET;
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
