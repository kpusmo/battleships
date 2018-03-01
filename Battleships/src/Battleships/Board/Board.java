package Battleships.Board;

import Battleships.Coordinates.Direction.Direction;
import Battleships.Coordinates.Point.Point;
import Battleships.Ship.Ship;
import Utils.Range;

import java.util.ArrayList;

public class Board {
    private static final char HORIZONTAL_SEPARATOR = '-';
    private static final char VERTICAL_SEPARATOR = '|';

    private Field board[][];
    private int boardSize;

    public Board(int boardSize) {
        this.boardSize = boardSize;
        board = new Field[boardSize][boardSize];
        for (int i = 0; i < boardSize; ++i) {
            for (int j = 0; j < boardSize; ++j) {
                board[i][j] = new Field(new Point(i, j));
            }
        }
    }

    public void drawBoard(boolean isOwn) {
        printHeader();
        printHorizontalLine();
        for (int y = 0; y < boardSize; ++y) {
            System.out.printf("%3s%3s", y, VERTICAL_SEPARATOR);
            for (int x = 0; x < boardSize; ++x) {
                System.out.print(String.format("%2s%s%3s", " ", board[x][y].getStringToPrint(isOwn), VERTICAL_SEPARATOR));
                board[x][y].resetIsLastMove();
            }
            System.out.print('\n');
            printHorizontalLine();
        }
    }

    public ArrayList<Point> getShootPointsAroundGiven(ArrayList<Point> points) {
        ArrayList<Point> ret = new ArrayList<>();
        if (points.size() == 1) {
            ret.add(new Point(points.get(0).x, points.get(0).y + 1));
            ret.add(new Point(points.get(0).x, points.get(0).y - 1));
            ret.add(new Point(points.get(0).x + 1, points.get(0).y));
            ret.add(new Point(points.get(0).x - 1, points.get(0).y));
        } else {
            if (points.get(0).x - points.get(1).x == 0) { //vertical
                points.forEach(p -> {
                    ret.add(new Point(p.x, p.y + 1));
                    ret.add(new Point(p.x, p.y - 1));
                });
            } else { //horizontal
                points.forEach(p -> {
                    ret.add(new Point(p.x + 1, p.y));
                    ret.add(new Point(p.x - 1, p.y));
                });
            }
        }
        ret.removeIf((Point p) -> !isValidShootPoint(p));
        return ret;
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
        return shootField.hasShip();
    }

    public Ship getShipOfPoint(Point coords) {
        return board[coords.x][coords.y].getShip();
    }

    public void updateFieldsAroundSunkenShip(Ship ship) {
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

    private void printHorizontalLine() {
        for (int i = 0; i < (boardSize + 1) * 6; ++i) {
            System.out.print(HORIZONTAL_SEPARATOR);
        }
        System.out.print('\n');
    }

    private void printHeader() {
        System.out.print(String.format("%6s", VERTICAL_SEPARATOR));
        for (int i = 0; i < boardSize; ++i) {
            System.out.print(String.format("%3s%3s", i, VERTICAL_SEPARATOR));
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

    private Field[] getFieldsOfShip(int x, int y, int shipLength, Direction shipDirection, boolean isNewShip) throws InvalidShipCoordinatesException {
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
