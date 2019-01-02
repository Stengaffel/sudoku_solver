package com.example.android.sudoku;

public class SudokuSolver {
    /**
     * Contains all digits in the grid.
     * The first index is the row and the second is the column.
     * Contains 0 if no digit is present in a given cell.
     */
    private int[][] grid = new int[9][9];

    /**
     * Retrieves the digit in the cell in row {@code row} and column {@code col}, or 0 if no digit is present.
     * @param row the row of the cell to look at
     * @param col the column of the cell to look at
     * @return the digit in the cell or 0 if no digit is present
     */
    public int getDigit(int row, int col) {
        return grid[row][col];
    }

    /**
     * Sets the digit in the cell in row {@code row} and column {@code col} to {@code digit}.
     * @param row the row of the cell to modify
     * @param col the column of the cell to modify
     * @param digit the digit to put into the cell or 0 to clear the cell.
     */
    public void setDigit(int row, int col, int digit) {
        grid[row][col] = digit;
    }

    /**
     * Checks if it is legal to replace the digit in the cell in row {@code row} and column {@code col} with {@code digit}.
     * Ignores the current digit in the specified cell, if any.
     *
     * @param row the row of the cell to investigate
     * @param col the column of the cell to investigate
     * @param digit the digit to check if it is legal
     * @return true if it is legal to put {@code digit} in the specified cell and false otherwise
     */
    public boolean isLegal(int row, int col, int digit) {
        for (int i = 0; i < 9; i++) {
            if (i != col && grid[row][i] == digit) {
                return false;
            }
            if (i != row && grid[i][col] == digit) {
                return false;
            }
        }
        int boxRow = row / 3;
        int boxCol = col / 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (3*boxRow + i != row && 3*boxCol + j != col && grid[3*boxRow + i][3*boxCol + j] == digit) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Tries to solve this sudoku given the current grid as clues.
     * <p/>
     * Will alway find <em>a</em> solution if one or more exist, and will return true in that case.
     * After returning this object will contain the found solution.
     * <p/>
     * If no solution exists, this object will be unmodified and the method will return false.
     *
     * @return true if a solution was found, false otherwise
     */
    public boolean solve() {
        for(int i = 0; i < 9; i++) {
            for(int k = 0; k < 9; k++) {
                if(grid[i][k] != 0 && !isLegal(i,k,grid[i][k])) {
                    return false;
                }
            }
        }
        return recursiveSolve(0, 0);
    }

    /**
     * Recursively tries to solve this sudoku using backtracking.
     * Assumes the sudoku is filled according to the rules up to but not including row {@code row} and column {@code col}
     * and searches for a solution for each possible digit in the given cell.
     * Returns true if a solution was found and false if not.
     *
     * @param row the row to try to fill
     * @param col the column to try to fill
     * @return true if a solution was found, false otherwise
     */
    private boolean recursiveSolve(int row, int col) {
        // Base-case. Once the sudoku has gone past [8][8], the next field will be [9][0] which
        // will trigger the base-case.
        if(col > 8) {
            return true;
        }

        // Initialization of the column and row for the next field.
        int nextCol = col;
        int nextRow = row;

        // Gives the column and row for the next field that is to be checked.
        if(row < 9) {
            if(col == 8 && row < 8) {	// If the observed field is at the end of a row, set the
                nextCol = 0;			// next field to be the first field in the following row.
                nextRow++;
            }
            else {
                nextCol++;	// Set the next field to the next column in the same row.
            }
        }

        // If the field at [row][col] is occupied and legal, do not change the current digit in
        // the field. If the occupied field is not legal, return false.
        if(getDigit(row, col) != 0) {
            if(isLegal(row, col, getDigit(row, col))) {		// Checks if the occupied field is legal
                return recursiveSolve(nextRow, nextCol);	// Starts the recursive method for the next field
            }
            else {
                return false;	// Returns false if the occupied space is not legal. (the sudoku is unsolvable)
            }
        }

        // Loops through the possible digits, 1-9 to start the solving progress
        for(int i = 1; i < 10; i++) {
            if(isLegal(row, col, i)) {	// Checks if the digit 'i' is a valid digit in field [row](col]
                setDigit(row, col, i);	// Sets the digit to 'i' if it is valid

                if(recursiveSolve(nextRow, nextCol)) {	// Recursive method-call that will check the
                    return true;						// check the next field in the sudoku
                }
                else {	// If the next method-call of recursiveSolve returns false, set the current field-digit to '0'
                    setDigit(row, col, 0); // backtrack
                }
            }
        }
        return false;	// Returns false if no number (1-9) is legal in the current field
    }
}
