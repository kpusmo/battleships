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

public class Game {
    private static final int INITIAL_BOARD_SIZE = 10;
    private static final int INITIAL_FOUR_DECKER_COUNT = 1;
    private static final int INITIAL_THREE_DECKER_COUNT = 2;
    private static final int INITIAL_TWO_DECKER_COUNT = 3;
    private static final int INITIAL_ONE_DECKER_COUNT = 4;
    private static final int SHIP_TYPE_COUNT = 4;

    private int boardSize;
    private GameMode gameMode;
    private GameState gameState;
    private Player firstPlayer;
    private Player secondPlayer;
    private String winnerName;
    private int shipUnitCount;
    private boolean printHitInfo;
    private boolean printSunkenInfo;
    private final ShipType SHIP_TYPES[] = {ShipType.ONE_DECKER_SHIP, ShipType.TWO_DECKER_SHIP, ShipType.THREE_DECKER_SHIP, ShipType.FOUR_DECKER_SHIP};
    private int[] shipCounts;

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
            shipUnitCount += shipCounts[i] * SHIP_TYPES[i].getValue();
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
        gameState = GameState.NOT_STARTED;
    }

    public void run() {
        initializeGame();
        gameState = GameState.STARTED;
        gameLoop();
        gameFinished();
    }

    private void gameFinished() {
        System.out.printf("Gra zakończona - zwycięzca: %s\n", winnerName);
    }

    private void gameLoop() {
        while (gameState != GameState.FINISHED) {
            if (!playerMove(firstPlayer, secondPlayer)) {
                playerMove(secondPlayer, firstPlayer);
            }
        }
        drawPlayerBoards(firstPlayer, secondPlayer);
        drawPlayerBoards(secondPlayer, firstPlayer);
        System.out.print("\n\n\n\n\n");
    }

    private void drawPlayerBoards(Player movingPlayer, Player enemy) {
        if (movingPlayer.getShowOutput()) {
            clearConsole();
            System.out.print("Twoja plansza:\n\n");
            movingPlayer.getBoard().drawBoard(true);
            System.out.print("\nPlansza przeciwnika:\n\n\n");
            enemy.getBoard().drawBoard(false);
            if (printHitInfo) {
                printMonit(movingPlayer, "Trafiony!\n");
                printHitInfo = false;
            }
            if (printSunkenInfo) {
                printMonit(movingPlayer, "Trafiony zatopiony!\n");
                printSunkenInfo = false;
            }
        }
    }

    private boolean playerMove(Player movingPlayer, Player enemy) {
        drawPlayerBoards(movingPlayer, enemy);
        Board enemyBoard = enemy.getBoard();
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
                if (movingPlayer.getShowOutput()) {
                    printSunkenInfo = true;
                }
                enemyBoard.updateFieldsAroundShip(hitShip);
            } else {
                if (movingPlayer.getShowOutput()) {
                    printHitInfo = true;
                }
            }
            return playerMove(movingPlayer, enemy);
        }
        return false;
    }

    private void initializeGame() {
        Player players[] = {firstPlayer, secondPlayer};
        for (int i = 0; i < players.length; ++i) {
            players[i].chooseName(i);
            setPlayerShips(players[i]);
        }
    }

    private void setPlayerShips(Player player) {
        for (int i = 0; i < SHIP_TYPE_COUNT; ++i) {
            for (int j = 0; j < shipCounts[i]; ++j) {
                if (player.getShowOutput()) {
                    clearConsole();
                    player.getBoard().drawBoard(true);
                }
                setPlayerShip(player, SHIP_TYPES[i]);
            }
        }
    }

    private void setPlayerShip(Player player, ShipType shipType) {
        Ship ship = new Ship(shipType);
        Board playerBoard = player.getBoard();
        String monit = "";
        do {
            printMonit(player, monit);
            Point startPoint;
            if (shipType == ShipType.ONE_DECKER_SHIP) {
                monit = "Wporwadź współrzędne (x, y) jednomasztowca";
            } else {
                monit = "Wprowadź współrzędne (x, y) początkowego punktu dla statku typu " + shipType.getName();
            }
            do {
                printMonit(player, monit);
                startPoint = player.chooseShipStartPoint();
                monit = "Błędne współrzędne lub pole jest już zajęte. Wprowadź współrzędne ponownie (" +  shipType.getName() + ')';
            } while (!playerBoard.isPointValidAndNotTaken(startPoint));
            ship.setStartPoint(startPoint);

            if (shipType == ShipType.ONE_DECKER_SHIP) {
                ship.setDirection(Direction.UP);
            } else {
                monit = "Wprowadź kierunek statku:\n0 - góra\n1 - dół\n2 - lewo\n3 - prawo";
                boolean validDirection;
                do {
                    printMonit(player, monit);
                    try {
                        validDirection = true;
                        ship.setDirection(player.chooseShipDirection());
                    } catch (InvalidDirectionInitializerException ex) {
                        validDirection = false;
                        monit = "Podałeś błędny kierunek. Spróbuj ponownie\n0 - góra\n1 - dół\n2 - lewo\n3 - prawo";
                    }
                } while (!validDirection);
            }
            monit = "Nie możesz tak ustawić statku - wychodzi on poza planszę, lub dotyka innego statku. Spróbuj ponownie.";
        } while (!playerBoard.setShip(ship));
    }

    private void clearConsole() {
        System.out.print("\033[H\033[2J");
    }

    private void printMonit(Player player, String monit) {
        if (player.getShowOutput()) {
            System.out.println(monit);
        }
    }

}
