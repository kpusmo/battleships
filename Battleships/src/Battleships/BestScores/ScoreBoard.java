package Battleships.BestScores;

import Battleships.Player.Player;

import java.io.*;
import java.util.*;

public class ScoreBoard {
    private static final char HORIZONTAL_SEPARATOR = '-';
    private static final char VERTICAL_SEPARATOR = '|';
    private static final int MAX_SCORE_COUNT = 10;
    private static final String OUT_FILE_NAME = "scoreBoard";

    private Map<String, Integer> board;

    public ScoreBoard() {
        try {
            FileInputStream fileIn = new FileInputStream(OUT_FILE_NAME);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            board = (Map<String, Integer>) in.readObject();
            in.close();
            fileIn.close();
        } catch (Exception exception) {
            board = new HashMap<>();
        }
    }

    public String saveToFile() {
        try {
            FileOutputStream fileOut = new FileOutputStream(OUT_FILE_NAME);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(board);
            out.close();
            fileOut.close();
            return "";
        } catch (IOException exception) {
            return exception.getMessage();
        }
    }

    public void addToScoreBoard(Player newPlayer) {
        String newPlayerName = newPlayer.getName();
        while (board.containsKey(newPlayerName)) {
            newPlayerName += " ";
        }
        board.put(newPlayerName, newPlayer.getShootCount());
    }

    public void print() {
        if (board.size() == 0) {
            System.out.println("No scores yet!");
            return;
        }
        int i = 1;
        printHorizontalLine();

        for (Map.Entry<String, Integer> entry : sortByValue(board).entrySet()) {
            System.out.printf("%s%5s%3s%38s%3s%17s%3s\n", VERTICAL_SEPARATOR, i++, VERTICAL_SEPARATOR, entry.getKey().trim(), VERTICAL_SEPARATOR, entry.getValue(), VERTICAL_SEPARATOR);
            printHorizontalLine();
            if (i == MAX_SCORE_COUNT) {
                return;
            }
        }
    }

    private void printHorizontalLine() {
        for (int i = 0; i < 70; ++i) {
            System.out.print(HORIZONTAL_SEPARATOR);
        }
        System.out.print('\n');
    }

    private static Map<String, Integer> sortByValue(Map<String, Integer> unsortedMap) {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(unsortedMap.entrySet());
        Collections.sort(list, (Object o1, Object o2) -> ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue()));
        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
}
