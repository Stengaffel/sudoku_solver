package com.example.android.sudoku;

public class Point {
    private int row;
    private int col;

    /**
     * Creates a new point with row <code>row</code> and column <code>col</code>.
     * @param row The row this point represents.
     * @param col The column this point represents.
     */
    public Point(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Returns the column this point represents.
     * @return The column.
     */
    public int getCol() {
        return col;
    }

    /**
     * Returns the row this point represents.
     * @return The row.
     */
    public int getRow() {
        return row;
    }

    /**
     * Sets this points row to <code>row</code> and column to <code>col</code>.
     * @param row The points new row.
     * @param col The points new column.
     */
    public void setPoint(int row, int col) {
        this.row = row;
        this.col = col;
    }
}
