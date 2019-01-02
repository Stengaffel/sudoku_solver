package com.example.android.sudoku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Point selected = null;
    private SudokuSolver solver;

    // Text sizes to be calculated using screen density.
    private float gridTextSize, inputTextSize, otherTextSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        solver = new SudokuSolver();

        // Calculate the size of the text.
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float dpGridText = 9f; // Size of digits in grid in dp.
        float dpInputText = 10f; // Size of digits on buttons in dp.
        float dpOtherText = 5f; // Size of solve and clear texts in dp.

        // Convert dp to pixels.
        gridTextSize = metrics.density * dpGridText;
        inputTextSize = metrics.density * dpInputText;
        otherTextSize = metrics.density * dpOtherText;

        setupSudokuGrid();

        setupButtonRows();
    }

    private void setupSudokuGrid() {
        TableLayout grid = (TableLayout) findViewById(R.id.grid);

        // LayoutParams that specify that the TextView should fill the entire height of the row and
        TableRow.LayoutParams cellLayoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT);
        cellLayoutParams.weight=1; // that the row should be equally divided into the TextViews
        // Same for the rows, but fill horizontally and divide vertically
        TableLayout.LayoutParams rowLayoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, 0);
        rowLayoutParams.weight=1;

        // Creation of Sudoku-grid
        for(int i = 1; i < 10; i++) {
            final int row = i;
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(rowLayoutParams);
            grid.addView(tableRow);

            for(int k = 1; k < 10; k++) {
                final int col = k;
                final TextView curTextView = new TextView(this);
                curTextView.setGravity(Gravity.CENTER);
                curTextView.setTextSize(gridTextSize);
                curTextView.setText("");
                curTextView.setId(row * 10 + col);
                curTextView.setLayoutParams(cellLayoutParams);

                if((i%2==1&&k%2==1) || (i%2==0&&k%2==0)) { // Color every other differently
                    curTextView.setBackgroundResource(R.color.gridColor);
                } else {
                    curTextView.setBackgroundResource(R.color.defaultWhite);
                }

                curTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(selected == null) {
                            selected = new Point(row, col); // First selection, set selection to touched point
                        }
                        else {
                            // Get last selection and reset its color
                            int i = selected.getRow() - 1;
                            int k = selected.getCol() - 1;
                            TextView prevSelected = (TextView) findViewById(selected.getRow() * 10 + selected.getCol());
                            if((i%2==1&&k%2==1) || (i%2==0&&k%2==0)) {
                                prevSelected.setBackgroundResource(R.color.gridColor);
                            }
                            else {
                                prevSelected.setBackgroundResource(R.color.defaultWhite);
                            }
                            selected.setPoint(row, col); // Update selection to touched point
                        }
                        curTextView.setBackgroundResource(R.color.highlight); // Highlight selection
                    }
                });
                tableRow.addView(curTextView);
            }
        }
    }

    private void setupButtonRows() {

        // Find button rows
        LinearLayout topRow = (LinearLayout) findViewById(R.id.top_row);
        LinearLayout bottomRow = (LinearLayout) findViewById(R.id.bottom_row);

        //LayoutParameters for input-buttons
        LinearLayout.LayoutParams inputLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        inputLayoutParams.weight = 15;

        // Creation of the input-buttons (1-9 + C)
        for(int i = 0; i < 10; i++) {
            final int num = i;
            Button bt = new Button(this);
            bt.setText("" + num);
            bt.setLayoutParams(inputLayoutParams);
            if(num == 0) {
                bt.setText("C"); // The button to reset the cell
            }
            bt.setTextSize(inputTextSize);
            bt.setGravity(Gravity.CENTER);

            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selected == null) {
                        return; // If no cell is selected, do nothing
                    }

                    // Get selected textview and update its text
                    TextView curTextView = (TextView) findViewById(selected.getRow() * 10 + selected.getCol());
                    curTextView.setText("" + num);
                    if(num == 0) {
                        curTextView.setText(""); // But clear it if the "C" button was pressed
                    }
                    // Update the sudoku model
                    solver.setDigit(selected.getRow()-1, selected.getCol()-1, num);
                }
            });

            if(num < 5) {
                topRow.addView(bt);
            }

            else {
                bottomRow.addView(bt);
            }
        }

        // LayoutParameters for solve- and reset-button
        LinearLayout.LayoutParams buttonsLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        buttonsLayoutParams.weight = 14;

        // SolveButton function
        Button solveBt = new Button(this);
        solveBt.setLayoutParams(buttonsLayoutParams);
        solveBt.setText("solve");
        solveBt.setTextSize(otherTextSize);
        solveBt.setGravity(Gravity.CENTER);
        solveBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(solver.solve()) { // If solvable, update textviews with the solution
                    for (int i = 0; i < 9; i++) {
                        for (int k = 0; k < 9; k++) {
                            TextView curTextView = (TextView) findViewById((i+1) * 10 + (k+1));
                            curTextView.setText("" + solver.getDigit(i, k));
                        }
                    }
                }
                else { // Otherwise, notify the user
                    Toast.makeText(getApplicationContext(), "No solution found: this sudoku is unsolvable!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        // ResetButton function
        Button resetBt = new Button(this);
        resetBt.setLayoutParams(buttonsLayoutParams);
        resetBt.setText("reset");
        resetBt.setTextSize(otherTextSize);
        resetBt.setGravity(Gravity.CENTER);
        resetBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < 9; i++) {
                    for(int k = 0; k < 9; k++) {
                        // Clear both the textviews texts and the sudoku model
                        TextView curTextView = (TextView) findViewById((i+1) * 10 + (k+1));
                        solver.setDigit(i,k,0);
                        curTextView.setText("");
                    }
                }
            }
        });
        bottomRow.addView(solveBt);
        topRow.addView(resetBt);
    }
}
