package com.example.android.sudoku;

/**
 * Created by alexa on 2017-11-29.
 */

public class Point {
    private int row;
    private int col;

    public Point(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public void setPoint(int r, int c) {
        this.row = r;
        this.col = c;
    }
}
