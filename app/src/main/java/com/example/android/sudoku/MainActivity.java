package com.example.android.sudoku;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.button;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.os.Build.VERSION_CODES.M;

public class MainActivity extends AppCompatActivity {

    static Point selected = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TableLayout grid = (TableLayout) findViewById(R.id.grid);
        final SudokuSolver ss = new SudokuSolver();

        TableRow.LayoutParams ptv = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT);
        ptv.weight=1;
        TableLayout.LayoutParams pRow = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);

        // Creation of Sudoku-grid
        for(int i = 1; i < 10; i++) {
            final int row = i;
            TableRow r = new TableRow(this);
            r.setLayoutParams(pRow);
            grid.addView(r);

            for(int k = 1; k < 10; k++) {
                final int col = k;
                TextView tv = (TextView) new TextView(this);
                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(30);
                tv.setText("");
                tv.setId(row * 10 + col);
                tv.setLayoutParams(ptv);

                if((i%2==1&&k%2==1) || (i%2==0&&k%2==0)) {
                    tv.setBackgroundResource(R.color.gridColor);
                }

                tv.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       // Toast.makeText(getApplicationContext(), "You clicked (" + row + "," + col + ")",Toast.LENGTH_SHORT).show();
                       if(selected == null) {
                           selected = new Point(row, col);
                       }
                       else {
                           int i = selected.getRow() - 1;
                           int k = selected.getCol() - 1;
                           TextView prevOne = (TextView) findViewById(selected.getRow() * 10 + selected.getCol());
                           if((i%2==1&&k%2==1) || (i%2==0&&k%2==0)) {
                               prevOne.setBackgroundResource(R.color.gridColor);
                           }
                           else {
                               prevOne.setBackgroundResource(R.color.defaultWhite);
                           }
                           selected.setPoint(row, col);
                       }
                       TextView theOne = (TextView) findViewById(selected.getRow() * 10 + selected.getCol());
                       theOne.setBackgroundResource(R.color.highlight);
                   }
                });
                r.addView(tv);
            }
        }

        LinearLayout topRow = (LinearLayout) findViewById(R.id.top_row);
        LinearLayout bottomRow = (LinearLayout) findViewById(R.id.bottom_row);

        //LayoutParameters for input-buttons
        LinearLayout.LayoutParams pNums = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        pNums.weight = 15;

        // Creation of the input-buttons (1-9 + C)
        for(int i = 0; i < 10; i++) {
            final int num = i;
            Button bt = (Button) new Button(this);
            bt.setText("" + num);
            bt.setLayoutParams(pNums);
            if(num == 0) {
                bt.setText("C");
            }
            bt.setTextSize(25);
            bt.setGravity(Gravity.CENTER_HORIZONTAL);

            bt.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       if(selected == null) {
                           return;
                       }
                       TextView theOne = (TextView) findViewById(selected.getRow() * 10 + selected.getCol());
                       theOne.setText("" + num);
                       if(num == 0) {
                           theOne.setText("");
                       }
                       ss.setDigit(selected.getRow()-1, selected.getCol()-1, num);
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
        LinearLayout.LayoutParams pOthers = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        pOthers.weight = 14;

        // SolveButton function
        Button solveBt = (Button) new Button(this);
        solveBt.setLayoutParams(pOthers);
        solveBt.setText("solve");
        solveBt.setGravity(Gravity.CENTER);
        solveBt.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
               if(ss.solve()) {
                   for (int i = 0; i < 9; i++) {
                       for (int k = 0; k < 9; k++) {
                           TextView theOne = (TextView) findViewById((i+1) * 10 + (k+1));
                           theOne.setText("" + ss.getDigit(i, k));
                       }
                   }
               }
               else {
                   Toast.makeText(getApplicationContext(), "ERROR! Sudoku is unsolvable!",Toast.LENGTH_SHORT).show();
               }
           }
        });

        // ResetButton function
        Button resetBt = (Button) new Button(this);
        resetBt.setLayoutParams(pOthers);
        resetBt.setText("reset");
        resetBt.setGravity(Gravity.CENTER);
        resetBt.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               for(int i = 0; i < 9; i++) {
                   for(int k = 0; k < 9; k++) {
                       TextView theOne = (TextView) findViewById((i+1) * 10 + (k+1));
                       ss.setDigit(i,k,0);
                       theOne.setText("");
                   }
               }
           }
        });
        bottomRow.addView(solveBt);
        topRow.addView(resetBt);
    }
}
