package Battleships.Coordinates.Direction;

import Battleships.Coordinates.Point.Point;

import java.util.Random;

public enum Direction {
    UP(-1),
    DOWN(1),
    LEFT(-1),
    RIGHT(1);

    private int value;
    private static Random rand = new Random();

    Direction(int value) {
        this.value = value;
    }

    public static Direction factory(int direction) throws InvalidDirectionInitializerException {
        switch (direction) {
            case 0:
                return UP;
            case 1:
                return DOWN;
            case 2:
                return LEFT;
            case 3:
                return RIGHT;
            default:
                throw new InvalidDirectionInitializerException();
        }
    }

    public int getValue() {
        return value;
    }

    public boolean isVertical() {
        return this == UP || this == DOWN;
    }

    public boolean isHorizontal() {
        return this == LEFT || this == RIGHT;
    }

    public static Direction getRandom() {
        return factory(rand.nextInt(4));
    }
}
