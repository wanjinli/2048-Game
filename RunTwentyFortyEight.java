package org.cis120.twentyfortyeight;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;

public class RunTwentyFortyEight implements Runnable {
    public void run() {
        // NOTE: the 'final' keyword denotes immutability even for local variables.

        // A popup instruction window that displays the game instruction and a simple description
        // of the game.
        final JFrame popup = new JFrame();
        JOptionPane.showMessageDialog(popup,
                "This is the 2048 game!\n" +
                        "Press W, A, S, or D to move the numbers around. Numbers \n" +
                        "with the same value in the same row/column will be merged\n" +
                        "when moved around. Press 'Save' when you want to save the\n" +
                        "current game state, and press 'Resume' to resume your last\n" +
                        "saved progress! Try to get a 2048 to win the game.\n" +
                        "Enjoy :)!",
                "Instructions for 2048",
                JOptionPane.INFORMATION_MESSAGE);

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("2048");
        frame.setLocation(400, 400);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        // Game board
        final Board board = new Board(status);
        frame.add(board, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.reset();
            }
        });
        control_panel.add(reset);

        // New button added: Save for saving progress in the file
        final JButton save = new JButton("Save");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.save();
            }
        });
        control_panel.add(save);

        // New button added: Resume for resuming progress in the file
        final JButton resume = new JButton("Resume");
        resume.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    board.resume();
                } catch (IOException ex) {
                    System.out.println("File does not exist");
                }
            }
        });
        control_panel.add(resume);

        // New button added: Undo for undoing the last step
        final JButton undo = new JButton("Undo");
        undo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.undo();
            }
        });
        control_panel.add(undo);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        board.reset();
    }
}
