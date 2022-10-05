package org.cis120.twentyfortyeight;

import java.io.*;
import java.util.*;

public class TwentyFortyEight {

    private int size;
    private int currScore;
    private int highestScore;
    private LinkedList<Integer> historyScores = new LinkedList<>();
    private LinkedList<int[][]> boardList = new LinkedList<>();
    private Tile[][] board;

    /**
     * Constructor sets up game state.
     */
    public TwentyFortyEight() {
        reset();
    }

    /**
     * A second constructor of TwentyFortyEight used for testing purposes.
     * @param board a 2D-array of Tile[][] as the board of the game
     */
    public TwentyFortyEight(Tile[][] board) {
        currScore = 0;
        historyScores.addLast(currScore);
        size = 4;
        this.board = board;
        boardList.addLast(convertBoard(this.board));
    }

    /**
     * Creates a default board that's in size 4x4 with all empty tiles. The current score is 0, and
     * the LinkedList scores stores the first historical score 0.
     */
    public void reset() {
        currScore = 0;
        historyScores.addLast(currScore);
        size = 4;
        board = new Tile[size][size];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new Tile();
            }
        }
        generateNewTile();
        generateNewTile();
        boardList.addLast(convertBoard(board));
    }

    /**
     * Cancels the player's previous movement by changing the board to its previous state and
     * update the scores to the previous state as well.
     */
    public void undo() {
        if (boardList.size() > 1 && historyScores.size() > 1) {
            boardList.removeLast();
            historyScores.removeLast();
            board = convertIntArray(boardList.peekLast());
            currScore = historyScores.peekLast();
            updateHighestScore();
        }
    }

    /**
     * When player chooses to save the current game board, saves it to the file input.
     * @param filename name of the file to save the progress
     */
    public void saveBoard(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException();
        }
        try {
            FileWriter fw = new FileWriter(filename);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(currScore);
            bw.write(highestScore);
            String s = "";
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    s += Integer.toString(board[i][j].getValue());
                    if (j < board[i].length - 1) {
                        s += " ";
                    }
                }
                s += "\n";
            }
            bw.write(s);
            bw.close();
        } catch (IOException e) {
            System.out.println("No more lines");
        }
    }

    /**
     * When a player chooses to resume a gaming progress saved before, the file they saved the
     * progress in gets read by a BufferedReader and resumes their progress.
     * @param filename name of the file to resume the progress
     * @throws IOException when file does not exist
     */
    public void resumeBoard(String filename) throws IOException {
        if (filename == null) {
            throw new IllegalArgumentException();
        }
        try {
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            Scanner sc = new Scanner(br);
            currScore = br.read();
            highestScore = br.read();
            while (sc.hasNextLine()) {
                for (int i = 0; i < 4; i++) {
                    String[] line = sc.nextLine().trim().split(" ");
                    for (int j = 0; j < 4; j++) {
                        board[i][j].setValue(Integer.parseInt(line[j]));
                    }
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File does not exist");
        }
    }

    /**
     * Convert a Tile[][] into an int[][]
     * @param b Tile[][] as the board
     * @return int[][] storing the values on the board
     */
    public int[][] convertBoard(Tile[][] b) {
        int[][] converted = new int[4][4];
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[i].length; j++) {
                converted[i][j] = b[i][j].getValue();
            }
        }
        return converted;
    }

    /**
     * Convert an int[][] into a Tile[][]
     * @param b int[][] as the board
     * @return Tile[][] storing the values on the board
     */
    public Tile[][] convertIntArray(int[][] b) {
        Tile[][] converted = new Tile[4][4];
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[i].length; j++) {
                converted[i][j] = new Tile();
                converted[i][j].setValue(b[i][j]);
            }
        }
        return converted;
    }

    /**
     * Iterates through the 2D array and adds up the number of empty tiles on the board
     * @return number of empty tiles on board
     */
    public int numEmptyTile() {
        int ctr = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].isEmpty()) {
                    ctr ++;
                }
            }
        }
        return ctr;
    }

    /**
     * Getter for current score
     * @return currScore
     */
    public int getCurrScore() {
        return currScore;
    }

    /**
     * Getter for the entire 2D-array (Tile[][]) of the board
     * @return board
     */
    public Tile[][] getBoard() {
        return board;
    }

    /**
     * Getter for the highestScore value
     * @return maximum score from historyScores
     */
    public int getHighestScore() {
        return highestScore;
    }

    /**
     * Iterates through the 2D-array board and return the tile with the highest value.
     * @return highest value in the 4x4 tile
     */
    public int getHighestValue() {
        int highest = 0;
        for (int i = 0; i < board.length; i++) {
            int highestInRow = 0;
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].getValue() >= highestInRow) {
                    highestInRow = board[i][j].getValue();
                }
            }
            if (highestInRow >= highest) {
                highest = highestInRow;
            }
        }
        return highest;
    }

    /**
     * Generates a random number 2 or 4 to be the value of the new tile added when a move is mode.
     * @return random number of either 2 or 4
     */
    public int generateRandomNumber() {
        Random rand = new Random();
        int num = rand.nextInt(9);
        if (num <= 1) {
            return 4; // 20% chance of generating a new 4
        } else {
            return 2; // 80% chance of generating a new 2
        }
    }

    /**
     * Sets the value of a specific tile that's originally empty to be the random value generated
     * by the generateRandomNumber() class.
     */
    public void generateNewTile() {
        if (numEmptyTile() != 0) {
            Random rand = new Random();
            while (true) {
                int randX = rand.nextInt(size);
                int randY = rand.nextInt(size);
                if (board[randX][randY].isEmpty()) {
                    board[randX][randY].setValue(generateRandomNumber());
                    return;
                }
            }
        }
    }

    /**
     * Updates the highest score to be (1) the highest score in previous games if that score
     * record is higher than the current score or (2) the current score when it is already
     * higher than the precious record.
     */
    public void updateHighestScore() {
        int highest = 0;
        for (int score: historyScores) {
            if (score >= highest) {
                highest = score;
            }
        }
        if (highest >= currScore) {
            highestScore = highest;
        } else {
            highestScore = currScore;
        }
    }

    /**
     * Reaction of the board when the user hits the "A" key to shift numbers to the left.
     *
     * The logic goes as follows:
     * 1. Move all pieces to the left side of the board where there are empty spaces.
     * 2. Add neighboring tiles of same value and changing the value of whichever tile has less
     * column number (more on the left side) to the sum, change the value of whichever tile has
     * more column number (more on the right side) to 0, or the next tile to the right.
     * 3. Move the tiles that just changed their values to the left side of the board where new
     * empty spaces are created.
     */
    public void moveLeft() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (!board[i][j].isEmpty()) {
                    int curr = board[i][j].getValue();
                    int prev = j - 1;
                    while (prev >= 0 && board[i][prev].isEmpty()) {
                        board[i][prev].setValue(curr);
                        board[i][prev + 1].clear();
                        prev --;
                    }
                }
            }
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (j + 1 < board[i].length
                        && (board[i][j].equals(board[i][j + 1]))
                        && (!board[i][j].isEmpty() || !board[i][j + 1].isEmpty())) {
                    board[i][j].merge(board[i][j + 1]);
                    board[i][j + 1].clear();
                    currScore += board[i][j].getValue();
                }
            }
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (!board[i][j].isEmpty()) {
                    int curr = board[i][j].getValue();
                    int prev = j - 1;
                    while (prev >= 0 && board[i][prev].isEmpty()) {
                        board[i][prev].setValue(curr);
                        board[i][prev + 1].clear();
                        prev --;
                    }
                }
            }
        }
        historyScores.addLast(currScore);
        updateHighestScore();
        generateNewTile();
        boardList.addLast(convertBoard(board));
    }

    /**
     * Reaction of the board when the user hits the "D" key to shift numbers to the right.
     *
     * The logic goes as follows:
     * 1. Move all pieces to the right side of the board where there are empty spaces.
     * 2. Add neighboring tiles of same value and changing the value of whichever tile has more
     * column number (more on the right side) to the sum, change the value of whichever tile has
     * less column number (more on the left side) to 0, or the next tile to the left.
     * 3. Move the tiles that just changed their values to the right side of the board where new
     * empty spaces are created.
     */
    public void moveRight() {
        for (int i = board.length - 1; i >= 0; i--) {
            for (int j = board[i].length - 1; j >= 0; j--) {
                if (!board[i][j].isEmpty()) {
                    int curr = board[i][j].getValue();
                    int prev = j + 1;
                    while (prev <= board[i].length - 1 && board[i][prev].isEmpty()) {
                        board[i][prev].setValue(curr);
                        board[i][prev - 1].clear();
                        prev ++;
                    }
                }
            }
        }
        for (int i = board.length - 1; i >= 0; i--) {
            for (int j = board[i].length - 1; j >= 0; j--) {
                if (j + 1 < board[i].length
                        && (board[i][j].equals(board[i][j + 1]))
                        && (!board[i][j].isEmpty() || !board[i][j + 1].isEmpty())) {
                    board[i][j + 1].merge(board[i][j]);
                    board[i][j].clear();
                    currScore += board[i][j + 1].getValue();
                }
            }
        }
        for (int i = board.length - 1; i >= 0; i--) {
            for (int j = board[i].length - 1; j >= 0; j--) {
                if (!board[i][j].isEmpty()) {
                    int curr = board[i][j].getValue();
                    int prev = j + 1;
                    while (prev <= board[i].length - 1 && board[i][prev].isEmpty()) {
                        board[i][prev].setValue(curr);
                        board[i][prev - 1].clear();
                        prev ++;
                    }
                }
            }
        }
        historyScores.addLast(currScore);
        updateHighestScore();
        generateNewTile();
        boardList.addLast(convertBoard(board));
    }

    /**
     * Reaction of the board when the user hits the "W" key to shift numbers upward.
     *
     * The logic goes as follows:
     * 1. Move all pieces to the upper side of the board where there are empty spaces.
     * 2. Add neighboring tiles of same value and changing the value of whichever tile has less
     * row number (more on the upper side) to the sum, change the value of whichever tile has
     * more row number (more on the lower side) to 0, or the next tile to the lower side.
     * 3. Move the tiles that just changed their values to the upper side of the board where new
     * empty spaces are created.
     */
    public void moveUp() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (!board[i][j].isEmpty()) {
                    int curr = board[i][j].getValue();
                    int prev = i - 1;
                    while (prev >= 0 && board[prev][j].isEmpty()) {
                        board[prev][j].setValue(curr);
                        board[prev + 1][j].clear();
                        prev --;
                    }
                }
            }
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (i + 1 < board[i].length
                        && (board[i][j].equals(board[i + 1][j]))
                        && (!board[i][j].isEmpty() || !board[i + 1][j].isEmpty())) {
                    board[i][j].merge(board[i + 1][j]);
                    board[i + 1][j].clear();
                    currScore += board[i][j].getValue();
                }
            }
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (!board[i][j].isEmpty()) {
                    int curr = board[i][j].getValue();
                    int prev = i - 1;
                    while (prev >= 0 && board[prev][j].isEmpty()) {
                        board[prev][j].setValue(curr);
                        board[prev + 1][j].clear();
                        prev --;
                    }
                }
            }
        }
        historyScores.addLast(currScore);
        updateHighestScore();
        generateNewTile();
        boardList.addLast(convertBoard(board));
    }

    /**
     * Reaction of the board when the user hits the "S" key to shift numbers downward.
     *
     * The logic goes as follows:
     * 1. Move all pieces to the lower side of the board where there are empty spaces.
     * 2. Add neighboring tiles of same value and changing the value of whichever tile has more
     * row number (more on the lower side) to the sum, change the value of whichever tile has
     * less row number (more on the upper side) to 0, or the next tile to the upper side.
     * 3. Move the tiles that just changed their values to the lower side of the board where new
     * empty spaces are created.
     */
    public void moveDown() {
        for (int i = board.length - 1; i >= 0; i--) {
            for (int j = board[i].length - 1; j >= 0; j--) {
                if (!board[i][j].isEmpty()) {
                    int curr = board[i][j].getValue();
                    int prev = i + 1;
                    while (prev <= board.length - 1 && board[prev][j].isEmpty()) {
                        board[prev][j].setValue(curr);
                        board[prev - 1][j].clear();
                        prev ++;
                    }
                }
            }
        }
        for (int i = board.length - 1; i >= 0; i--) {
            for (int j = board[i].length - 1; j >= 0; j--) {
                if (i + 1 < board[i].length
                        && (board[i][j].equals(board[i + 1][j]))
                        && (!board[i][j].isEmpty() || !board[i + 1][j].isEmpty())) {
                    board[i + 1][j].merge(board[i][j]);
                    board[i][j].clear();
                    currScore += board[i + 1][j].getValue();
                }
            }
        }
        for (int i = board.length - 1; i >= 0; i--) {
            for (int j = board[i].length - 1; j >= 0; j--) {
                if (!board[i][j].isEmpty()) {
                    int curr = board[i][j].getValue();
                    int prev = i + 1;
                    while (prev <= board.length - 1 && board[prev][j].isEmpty()) {
                        board[prev][j].setValue(curr);
                        board[prev - 1][j].clear();
                        prev ++;
                    }
                }
            }
        }
        historyScores.addLast(currScore);
        updateHighestScore();
        generateNewTile();
        boardList.addLast(convertBoard(board));
    }

    /**
     * Checks if there are neighboring tiles on the board with same value, meaning that they are
     * still mergeable and the game can continue. Checks both horizontally and vertically.
     * @return true if there are neighboring tiles still able to merge; false otherwise.
     */
    public boolean hasMergeableTiles() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                // checks if vertical neighbors have the same value
                if (i < board.length - 1) {
                    if (board[i][j].equals(board[i + 1][j])) {
                        return true;
                    }
                }
                // checks if horizontal neighbors have the same value
                if (j < board[0].length - 1) {
                    if (board[i][j].equals(board[i][j + 1])) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Check if a game is over. A game is over when there are no more empty tile to add new numbers
     * AND there are no neighboring tiles that have the same value.
     * @return true if the game is over; false otherwise.
     */
    public boolean isGameOver() {
        return numEmptyTile() == 0 && !hasMergeableTiles();
    }

    /**
     * Check if the player has won the game by getting a tile of 2048.
     * @return true if the highest tile value is 2048; false otherwise.
     */
    public boolean hasWon() {
        return getHighestValue() == 2048;
    }

    public void printGameState() {
        if (isGameOver()) {
            System.out.println("Game Over :(");
        } else if (hasWon()) {
            System.out.println("YOU WON!");
        } else {
            System.out.println("Join the numbers and get to the 2048 tile!");
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j] + "   ");
            }
            System.out.println();
        }
        System.out.println("Current Score: " + getCurrScore());
        System.out.println("Highest Score: " + getHighestScore());
        System.out.println("\n");
    }

    /**
     * This main method illustrates how the model is completely independent of
     * the view and controller. We can play the game from start to finish
     * without ever creating a Java Swing object.
     *
     * This is modularity in action, and modularity is the bedrock of the
     * Model-View-Controller design framework.
     *
     * Run this file to see the output of this method in your console.
     */
    public static void main(String[] args) {
        TwentyFortyEight t = new TwentyFortyEight();

        t.printGameState();
        t.moveLeft();
        t.printGameState();
        t.moveRight();
        t.printGameState();
        t.moveUp();
        t.printGameState();
        t.moveDown();
        t.printGameState();
        t.moveUp();
        t.printGameState();
        t.moveLeft();
        t.printGameState();
        t.undo();
        t.printGameState();
    }
}
