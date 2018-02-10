package Battleships.Coordinates.Point;

public class Point {
    public int x;
    public int y;

    public Point (int xx, int yy) {
        x = xx;
        y = yy;
    }

    public boolean isOnBoard(int boardSize) {
        return x >= 0 && x < boardSize && y >= 0 && y < boardSize;
    }
}
