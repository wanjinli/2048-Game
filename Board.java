package org.cis120.twentyfortyeight;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;

public class Board extends JPanel {
    private TwentyFortyEight tfe;
    private JLabel status;

    public static final int BOARD_WIDTH = 400;
    public static final int BOARD_HEIGHT = 400;

    public Board(JLabel statusInit) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setFocusable(true);

        tfe = new TwentyFortyEight();
        status = statusInit;

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                if (!tfe.hasWon() && !tfe.isGameOver()) {
                    if (ke.getKeyCode() == KeyEvent.VK_A) {
                        tfe.moveLeft();
                    } else if (ke.getKeyCode() == KeyEvent.VK_D) {
                        tfe.moveRight();
                    } else if (ke.getKeyCode() == KeyEvent.VK_W) {
                        tfe.moveUp();
                    } else if (ke.getKeyCode() == KeyEvent.VK_S) {
                        tfe.moveDown();
                    }
                    repaint();
                }
            }
            public void keyReleased(KeyEvent ke) {
                updateStatus();
            }
            public void keyTyped(KeyEvent ke) { }
        });
    }

    public void reset() {
        tfe.reset();
        status.setText("Press WASD to merge the numbers and get to the 2048 tile!");
        repaint();
        requestFocusInWindow();
    }

    public void undo() {
        if (!tfe.hasWon() && !tfe.isGameOver() && tfe.numEmptyTile() != 14) {
            tfe.undo();
        } else if (tfe.hasWon()) {
            status.setText("YOU WON!");
        } else if (tfe.isGameOver()) {
            status.setText("Game Over :(");
        }
        status.setText("Press WASD to merge the numbers and get to the 2048 tile!");
        repaint();
        requestFocusInWindow();
    }

    public void save() {
        tfe.saveBoard("files/SavedBoards.txt");
        status.setText("Press WASD to merge the numbers and get to the 2048 tile!");
        repaint();
        requestFocusInWindow();
    }

    public void resume() throws IOException {
        tfe.resumeBoard("files/SavedBoards.txt");
        status.setText("Press WASD to merge the numbers and get to the 2048 tile!");
        repaint();
        requestFocusInWindow();
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        if (tfe.hasWon()) {
            status.setText("YOU WON!");
        } else if (tfe.isGameOver()) {
            status.setText("Game Over :(");
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Tile[][] board = tfe.getBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                g.setColor(board[i][j].getBackgroundColor());
                g.fillRect(50 + 75 * j, 50 + 75 * i, 75, 75);
                g.setColor(board[i][j].getNumberColor());
                g.setFont(new Font("TimesRoman", Font.PLAIN, 24));
                g.drawString(String.valueOf(board[i][j].getValue()),
                        79 + 75 * j, 89 + 75 * i);
                g.setColor(Color.BLACK);
                g.drawLine(50, 50, 350, 50);
                g.drawLine(50, 125, 350, 125);
                g.drawLine(50, 200, 350, 200);
                g.drawLine(50, 275, 350, 275);
                g.drawLine(50, 350, 350, 350);
                g.drawLine(50, 50, 50, 350);
                g.drawLine(125, 50, 125, 350);
                g.drawLine(200, 50, 200, 350);
                g.drawLine(275, 50, 275, 350);
                g.drawLine(350, 50, 350, 350);
            }
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 18));
        g.drawString("Highest Score: ", 50, 30);
        g.drawString(String.valueOf(tfe.getHighestScore()), 160, 30);
        g.drawString("Current Score: ", 230, 30);
        g.drawString(String.valueOf(tfe.getCurrScore()), 340, 30);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
