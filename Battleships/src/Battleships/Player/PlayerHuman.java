package Battleships.Player;

import Battleships.Coordinates.Direction.Direction;
import Battleships.Coordinates.Direction.InvalidDirectionInitializerException;
import Battleships.Coordinates.Point.Point;

import java.util.Scanner;

public class PlayerHuman extends Player {
    private Scanner scanner;

    public PlayerHuman(int boardSize) {
        super(boardSize);
        scanner = new Scanner(System.in);
        showOutput = true;
    }

    @Override
    public void chooseName(int index) {
        System.out.printf("Gracz nr %d: podaj swoje imię\n", index + 1);
        name = scanner.nextLine();
    }

    @Override
    public Point chooseShipStartPoint() {
        return choosePoint();
    }

    @Override
    public Direction chooseShipDirection() {
        return Direction.factory(scanner.nextInt());
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
}
