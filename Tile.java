package org.cis120.twentyfortyeight;

import java.awt.*;

public class Tile {
    private int value;

    /**
     * Creates an instance of Tile with value 0 - that is, it currently holds no value and is ready
     * to be filled with a number generated by either a new random new number 2 or 4 or another
     * number already existing moving to this specific tile.
     */
    public Tile() {
        value = 0;
    }

    /**
     * Creates an instance of Tile with a set value as its input.
     */
    public Tile(int v) {
        value = v;
    }

    /**
     * Sets the value of this specific tile to be a certain number. Used when "adding up" numbers
     * and updating the original tile.
     */
    public void setValue(int v) {
        value = v;
    }

    /**
     * Returns the current value of the tile. Used when checking if the tile is empty or if it has
     * the same number as some other tile ready to be merged.
     * This method is also used to return the score (equal to value) the player gets for merging
     * the tile.
     * @return value of the tile.
     */
    public int getValue() {
        return value;
    }

    /**
     * Check if two tiles have the same value. If yes, then the two tiles can be merged with some
     * other conditions.
     * @param t Tile passed in to be checked for equality
     * @return true if two tiles have equal value; false otherwise.
     */
    public boolean equals(Tile t) {
        return value == t.getValue();
    }

    /**
     * Adds up the values of two tiles when prompted to merge. Note that this method should only
     * be called if the result of two merging tiles will appear on this specific tile.
     */
    public void merge(Tile t) {
        setValue(value + t.getValue());
    }

    /**
     * When prompted, the specific tile's value gets changed to 0, meaning that it is now empty.
     */
    public void clear() {
        setValue(0);
    }

    /**
     * Check if a tile's value is 0, or that it is empty
     * @return true if the tile is empty; false otherwise.
     */
    public boolean isEmpty() {
        return value == 0;
    }

    /**
     * Knowing the value of the tile, return its corresponding background color.
     * @return color of the tile background according to the value
     */
    public Color getBackgroundColor() {
        switch (value) {
            case 0: return new Color(0xcdc1b4);
            case 2: return new Color(0xeee4da);
            case 4: return new Color(0xede0c8);
            case 8: return new Color(0xf2b179);
            case 16: return new Color(0xf59563);
            case 32: return new Color(0xf67c5f);
            case 64: return new Color(0xf65e3b);
            case 128: return new Color(0xedcf72);
            case 256: return new Color(0xedcc61);
            case 512: return new Color(0xedc850);
            case 1024: return new Color(0xedc53f);
            case 2048: return new Color(0xedc22e);
            default: return new Color(0x248c51);
        }
    }

    /**
     * Knowing the value of the tile, return its corresponding font color.
     * @return color of the tile font according to the value
     */
    public Color getNumberColor() {
        switch (value) {
            case 0: return new Color(0xcdc1b4);
            case 2:
            case 4:
                return Color.BLACK;
            default: return Color.WHITE;
        }
    }

    /**
     * Overrides the default toString() method to display the value of the specific tile used by
     * the GUI.
     * @return String format of the integer value.
     */
    @Override
    public String toString() {
        return Integer.toString(value);
    }


}