package Battleships.Board;

import Battleships.Coordinates.Direction.Direction;
import Battleships.Coordinates.Point.Point;
import Battleships.Ship.Ship;
import Utils.Range;

import java.util.ArrayList;

public class Board {
    private static final char HORIZONTAL_SEPARATOR = '-';
    private static final char VERTICAL_SEPARATOR = '|';
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

    private Field board[][];
    private ArrayList<Field> lastMoveFields;
    private int boardSize;

    public Board(int boardSize) {
        this.boardSize = boardSize;
        board = new Field[boardSize][boardSize];
        for (int i = 0; i < boardSize; ++i) {
            for (int j = 0; j < boardSize; ++j) {
                board[i][j] = new Field(new Point(i, j));
            }
        }
        lastMoveFields = new ArrayList<>();
    }

    public void drawBoard(boolean isOwn) {
        printHeader();
        printHorizontalLine();
        for (int y = 0; y < boardSize; ++y) {
            System.out.printf("%3s%3s", y, VERTICAL_SEPARATOR);
            for (int x = 0; x < boardSize; ++x) {
                printField(board[x][y], isOwn);
            }
            System.out.print('\n');
            printHorizontalLine();
        }
    }

    public boolean setShip(Ship ship) {
        Direction shipDirection = ship.getDirection();
        Point shipStartPoint = ship.getStartPoint();
        int shipLength = ship.getLength();
        Field shipFields[];
        try {
            shipFields = getFieldsOfShip(shipStartPoint.x, shipStartPoint.y, shipLength, shipDirection, true);
        } catch (InvalidShipCoordinatesException ex) {
            return false;
        }
        for (int i = 0; i < shipLength; ++i) {
            shipFields[i].setShip(ship);
            setAdjoiningFieldsTouchShip(shipFields[i], false);
        }
        return true;
    }

    public boolean shoot(Point coords) {
        Field shootField = board[coords.x][coords.y];
        shootField.shoot();
        lastMoveFields.add(shootField);
        return shootField.hasShip();
    }

    public Ship getShipOfPoint(Point coords) {
        return board[coords.x][coords.y].getShip();
    }

    public void updateFieldsAroundShip(Ship ship) {
        Point shipStartPoint = ship.getStartPoint();
        int shipLength = ship.getLength();
        Field shipFields[] = getFieldsOfShip(shipStartPoint.x, shipStartPoint.y, shipLength, ship.getDirection(), false);
        for (int i = 0; i < shipLength; ++i) {
            setAdjoiningFieldsTouchShip(shipFields[i], true);
        }
    }

    public boolean isValidShootPoint(Point coords) {
        return coords.isOnBoard(boardSize) && !board[coords.x][coords.y].isShot() && !board[coords.x][coords.y].isTouchingSunkenShip();
    }

    public boolean isPointValidAndNotTaken(Point coords) {
        return coords.isOnBoard(boardSize) && !board[coords.x][coords.y].hasShip() && !board[coords.x][coords.y].isTouchingShip();
    }

    public int getBoardSize() {
        return boardSize;
    }

    private void printField(Field field, boolean isOwnBoard) {
        char signToDraw;
        String colorCode = "";
        if (!field.isShot()) {
            if (field.hasShip()) {
                if (isOwnBoard) {
                    signToDraw = OWN_SHIP;
                } else {
                    signToDraw = EMPTY_FIELD;
                }
            } else if (field.isTouchingSunkenShip()) {
                if (isOwnBoard) {
                    signToDraw = EMPTY_FIELD;
                } else {
                    signToDraw = MISHIT;
                }
            } else {
                signToDraw = EMPTY_FIELD;
            }
        } else if (field.hasShip()) {
            if (field.isLastMove()) {
                if (isOwnBoard) {
                    colorCode = ANSI_RED;
                } else {
                    colorCode = ANSI_GREEN;
                }
            } else {
                colorCode = ANSI_BLUE;
            }
            signToDraw = HIT;
        } else {
            if (field.isLastMove()) {
                if (isOwnBoard) {
                    colorCode = ANSI_YELLOW;
                } else {
                    colorCode = ANSI_CYAN;
                }
            }
            signToDraw = MISHIT;
        }
        System.out.printf(String.format("%2s%s%3s", " ", colorCode + signToDraw + ANSI_RESET, VERTICAL_SEPARATOR));
        field.resetIsLastMove();
    }

    private void printHorizontalLine() {
        for (int i = 0; i < boardSize * 7; ++i) {
            System.out.print(HORIZONTAL_SEPARATOR);
        }
        System.out.print('\n');
    }

    private void printHeader() {
        System.out.printf(String.format("%6s", VERTICAL_SEPARATOR));
        for (int i = 0; i < boardSize; ++i) {
            System.out.printf(String.format("%3s%3s", i, VERTICAL_SEPARATOR));
        }
        System.out.print('\n');
    }

    private void setAdjoiningFieldsTouchShip(Field field, boolean shipSunken) {
        Point coords = field.getCoords();
        for (int i : Range.Range(coords.x - 1, coords.x + 1)) {
            for (int j : Range.Range(coords.y - 1, coords.y + 1)) {
                Point point = new Point(i, j);
                if (point.isOnBoard(boardSize) && shipSunken) {
                    board[point.x][point.y].setTouchesSunkenShip();
                } else if (isPointValidAndNotTaken(point)) {
                    board[point.x][point.y].setTouchesShip();
                }
            }
        }
    }

    private Field[] getFieldsOfShip(int x, int y, int shipLength, Direction shipDirection, boolean isNewShip) {
        if (shipDirection.isHorizontal()) {
            return getFieldsOfHorizontalShip(x, y, shipLength, shipDirection.getValue(), isNewShip);
        } else {
            return getFieldsOfVerticalShip(x, y, shipLength, shipDirection.getValue(), isNewShip);
        }
    }

    private Field[] getFieldsOfHorizontalShip(int startX, int y, int shipLength, int directionValue, boolean isNewShip) throws InvalidShipCoordinatesException {
        Field shipFields[] = new Field[shipLength];
        int i = 0;
        for (int x : Range.Range(startX, startX + (shipLength - 1) * directionValue, directionValue)) {
            if (isNewShip && !isPointValidAndNotTaken(new Point(x, y))) {
                throw new InvalidShipCoordinatesException();
            }
            shipFields[i] = board[x][y];
            i++;
        }
        return shipFields;
    }

    private Field[] getFieldsOfVerticalShip(int x, int startY, int shipLength, int directionValue, boolean isNewShip) throws InvalidShipCoordinatesException {
        Field shipFields[] = new Field[shipLength];
        int i = 0;
        for (int y : Range.Range(startY, startY + (shipLength - 1) * directionValue, directionValue)) {
            if (isNewShip && !isPointValidAndNotTaken(new Point(x, y))) {
                throw new InvalidShipCoordinatesException();
            }
            shipFields[i] = board[x][y];
            i++;
        }
        return shipFields;
    }
}
