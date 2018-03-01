package Battleships.Game;

import Battleships.Board.Board;
import Battleships.Coordinates.Direction.Direction;
import Battleships.Coordinates.Direction.InvalidDirectionInitializerException;
import Battleships.Coordinates.Point.Point;
import Battleships.Player.Player;
import Battleships.Player.PlayerAI;
import Battleships.Player.PlayerHuman;
import Battleships.Ship.Ship;
import Battleships.Ship.ShipType;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Game {
    private static final int INITIAL_BOARD_SIZE = 10;
    private static final int INITIAL_FOUR_DECKER_COUNT = 1;
    private static final int INITIAL_THREE_DECKER_COUNT = 2;
    private static final int INITIAL_TWO_DECKER_COUNT = 3;
    private static final int INITIAL_ONE_DECKER_COUNT = 4;
    private static final int SHIP_TYPE_COUNT = 4;
    private final ShipType SHIP_TYPES[] = {ShipType.ONE_DECKER_SHIP, ShipType.TWO_DECKER_SHIP, ShipType.THREE_DECKER_SHIP, ShipType.FOUR_DECKER_SHIP};
    private int boardSize;
    private GameMode gameMode;
    private GameState gameState;
    private Player firstPlayer;
    private Player secondPlayer;
    private String winnerName;
    private int shipUnitCount;
    private int[] shipCounts;
    private boolean noOutput = false;

    public Game(GameMode mode) {
        this(mode, INITIAL_BOARD_SIZE, INITIAL_FOUR_DECKER_COUNT, INITIAL_THREE_DECKER_COUNT, INITIAL_TWO_DECKER_COUNT, INITIAL_ONE_DECKER_COUNT);
    }

    public Game(GameMode mode, int size) {
        this(mode, size, INITIAL_FOUR_DECKER_COUNT, INITIAL_THREE_DECKER_COUNT, INITIAL_TWO_DECKER_COUNT, INITIAL_ONE_DECKER_COUNT);
    }

    public Game(GameMode mode, int size, int fourDeckerCount, int threeDeckerCount, int twoDeckerCount, int oneDeckerCount) {
        boardSize = size;
        gameMode = mode;
        int shipCounts[] = {oneDeckerCount, twoDeckerCount, threeDeckerCount, fourDeckerCount};
        this.shipCounts = shipCounts;

        shipUnitCount = 0;
        for (int i = 0; i < shipCounts.length; ++i) {
            shipUnitCount += shipCounts[i] * SHIP_TYPES[i].getLength();
        }

        firstPlayer = new PlayerHuman(boardSize);
        switch (gameMode) {
            case SINGLE_PLAYER:
                secondPlayer = new PlayerAI(boardSize);
                break;
            case MULTI_PLAYER:
                secondPlayer = new PlayerHuman(boardSize);
                break;
        }
        firstPlayer.setEnemyBoard(secondPlayer.getBoard());
        secondPlayer.setEnemyBoard(firstPlayer.getBoard());
        gameState = GameState.NOT_STARTED;
    }

    public void run() {
        initializeGame();
        gameState = GameState.STARTED;
        gameLoop();
    }

    private void gameFinished() {
        drawPlayerBoards(firstPlayer, secondPlayer.getBoard());
        drawPlayerBoards(secondPlayer, firstPlayer.getBoard());
        System.out.print("\n\n\n\n\n");
        System.out.printf("Gra zakończona - zwycięzca: %s\n", winnerName);
    }

    private void gameLoop() {
        while (gameState != GameState.FINISHED) {
            if (playerMove(firstPlayer, secondPlayer.getBoard())) {
                gameFinished();
                return;
            }
            if (playerMove(secondPlayer, firstPlayer.getBoard())) {
                gameFinished();
                return;
            }
        }
    }

    private void drawPlayerBoards(Player player, Board enemyBoard) {
        if (player.getShowOutput()) {
            clearConsole();
            printMonit(player, "Twoja plansza:\n\n");
            player.getBoard().drawBoard(true);
            printMonit(player, "\nPlansza przeciwnika:\n\n\n");
            enemyBoard.drawBoard(false);
            if (player.wasLastSunken()) {
                printMonit(player, "Trafiony zatopiony!\n");
                player.resetLastSunken();
            } else if (player.wasLastHit()) {
                printMonit(player, "Trafiony!\n");
                player.resetLastHit();
            }
        }
    }

    private boolean playerMove(Player movingPlayer, Board enemyBoard) {
        drawPlayerBoards(movingPlayer, enemyBoard);
        Point shootPoint;
        String monit = "Wprowadź współrzędne strzału";
        do {
            printMonit(movingPlayer, monit);
            shootPoint = movingPlayer.shoot();
            monit = "Błędne współrzędne. Spróbuj ponownie";
        } while (!enemyBoard.isValidShootPoint(shootPoint));
        if (enemyBoard.shoot(shootPoint)) {
            movingPlayer.hit();
            if (movingPlayer.getHitCount() == shipUnitCount) {
                winnerName = movingPlayer.getName();
                gameState = GameState.FINISHED;
                return true;
            }
            Ship hitShip = enemyBoard.getShipOfPoint(shootPoint);
            if (hitShip.hit()) {
                movingPlayer.sunkenShip();
                enemyBoard.updateFieldsAroundSunkenShip(hitShip);
            }
            return playerMove(movingPlayer, enemyBoard);
        }
        return false;
    }

    private void initializeGame() {
        Player players[] = {firstPlayer, secondPlayer};
        for (int i = 0; i < players.length; ++i) {
            printMonit(players[i], (new StringBuilder()).append("Gracz nr ").append(i + 1).append(": podaj swoje imię").toString());
            players[i].chooseName();
            setPlayerShips(players[i]);
        }
    }

    private void setPlayerShips(Player player) {
        printMonit(player, "Wpisz 0 jeśli chcesz losowo ustawić statki lub cokolwiek innego, jeśli chcesz to zrobić sam.");
        player.setShipPlacementMode();
        if (player.getRandomShipPlacement()) {
            noOutput = true;
        }
        for (int i = 0; i < SHIP_TYPE_COUNT; ++i) {
            for (int j = 0; j < shipCounts[i]; ++j) {
                if (player.getShowOutput() && !noOutput) {
                    clearConsole();
                    player.getBoard().drawBoard(true);
                }
                setPlayerShip(player, SHIP_TYPES[i]);
            }
        }
        noOutput = false;
    }

    private void setPlayerShip(Player player, ShipType shipType) {
        Ship ship = new Ship(shipType);
        Board playerBoard = player.getBoard();
        String monit = "";
        do {
            printMonit(player, monit);
            setShipStartPoint(player, ship);
            setShipDirection(player, ship);
            monit = "Nie możesz tak ustawić statku - wychodzi on poza planszę, lub dotyka innego statku. Spróbuj ponownie.";
        } while (!playerBoard.setShip(ship));
    }

    private void setShipDirection(Player player, Ship ship) {
        ShipType shipType = ship.getShipType();
        if (shipType == ShipType.ONE_DECKER_SHIP) {
            ship.setDirection(Direction.UP); //direction does not matter when placing one decker ship
            return;
        }
        String monit = "Wprowadź kierunek statku:\n0 - góra\n1 - dół\n2 - lewo\n3 - prawo";
        boolean validDirection = false;
        do {
            printMonit(player, monit);
            try {
                ship.setDirection(player.chooseShipDirection());
                validDirection = true;
            } catch (InvalidDirectionInitializerException | InputMismatchException ex) {
                player.clearScannerBuffer();
                monit = "Podałeś błędny kierunek. Spróbuj ponownie\n0 - góra\n1 - dół\n2 - lewo\n3 - prawo";
            }
        } while (!validDirection);
    }

    private void setShipStartPoint(Player player, Ship ship) {
        String monit;
        Point startPoint = new Point(-1, -1);
        ShipType shipType = ship.getShipType();
        if (shipType == ShipType.ONE_DECKER_SHIP) {
            monit = "Wporwadź współrzędne (x, y) jednomasztowca";
        } else {
            monit = "Wprowadź współrzędne (x, y) początkowego punktu dla statku typu " + shipType.getName();
        }
        Board playerBoard = player.getBoard();
        boolean validInput = false;
        do {
            printMonit(player, monit);
            try {
                startPoint = player.chooseShipStartPoint();
                validInput = true;
            } catch (InputMismatchException ex) {
                player.clearScannerBuffer();
            }
            monit = "Błędne współrzędne lub pole jest już zajęte. Wprowadź współrzędne ponownie (" + shipType.getName() + ')';
        } while (!(validInput && playerBoard.isPointValidAndNotTaken(startPoint)));
        ship.setStartPoint(startPoint);
    }

    private void clearConsole() {
        System.out.print("\033[H\033[2J");
    }

    private void printMonit(Player player, String monit) {
        if (!noOutput && player.getShowOutput()) {
            System.out.println(monit);
        }
    }

}
