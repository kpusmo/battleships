package Battleships.Coordinates.Point;

import java.util.Random;

public class Point {
    public int x;
    public int y;
    private static Random rand = new Random();

    public Point (int xx, int yy) {
        x = xx;
        y = yy;
    }

    public boolean isOnBoard(int boardSize) {
        return x >= 0 && x < boardSize && y >= 0 && y < boardSize;
    }

    public static Point getRandom(int max) {
        return new Point(rand.nextInt(max), rand.nextInt(max));
    }
}
