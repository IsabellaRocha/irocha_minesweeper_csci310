package com.example.gridlayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private int clock = 0;
    private int totalFlags = 4;
    private String mode = "pick";
    private boolean won = false;
    private boolean lost = false;
    private boolean running = true;
    private Button btn;

    // save the TextViews of all cells in an array, so later on,
    // when a TextView is clicked, we know which cell it is
    private ArrayList<TextView> cell_tvs;
    private HashMap<TextView, String> ogText;

    private int dpToPixel(int dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView header = findViewById(R.id.textView);
        final Handler handler = new Handler();
        btn = findViewById(R.id.button);

        handler.post(new Runnable() {
            @Override
            public void run() {
                String flagDisplay = "\uD83D\uDEA9  " + totalFlags + "                 ";
                String time = "\uD83D\uDD53  " + clock;
                String display = flagDisplay + time;
                header.setText(display);
                if (running) clock++;
                handler.postDelayed(this, 1000);
            }
        });

        cell_tvs = new ArrayList<>();
        ogText = new HashMap<>();
        // Method (2): add four dynamically created cells
        GridLayout grid = findViewById(R.id.gridLayout01);
        for (int i = 0; i<=9; i++) {
            for (int j=0; j<=7; j++) {
                TextView tv = new TextView(this);
                tv.setHeight( dpToPixel(32) );
                tv.setWidth( dpToPixel(32) );
                tv.setTextSize( 16 );//dpToPixel(32) );
                tv.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                tv.setTextColor(Color.GREEN);
                tv.setBackgroundColor(Color.parseColor("lime"));
                tv.setOnClickListener(this::onClickTV);

                GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
                lp.setMargins(dpToPixel(2), dpToPixel(2), dpToPixel(2), dpToPixel(2));
                lp.rowSpec = GridLayout.spec(i);
                lp.columnSpec = GridLayout.spec(j);

                grid.addView(tv, lp);

                cell_tvs.add(tv);
            }
        }
        Random rand = new Random();
        for (int i = 0; i < 4; i++) {
            int rando = rand.nextInt(80);
            cell_tvs.get(rando).setText("BOMBUNCLEARED");
        }
        findNumberOfBombs();
        for(int idx = 0; idx < cell_tvs.size(); idx++) {
            ogText.put(cell_tvs.get(idx), cell_tvs.get(idx).getText().toString());
        }
    }

    private int findIndexOfCellTextView(TextView tv) {
        for (int n=0; n<cell_tvs.size(); n++) {
            if (cell_tvs.get(n) == tv)
                return n;
        }
        return -1;
    }

    private void findNumberOfBombs() {
        for(int index = 0; index < cell_tvs.size(); index++) {
            int numBombs = 0;
            boolean checkLeft = true;
            boolean checkRight = true;
            if (index % 8 == 0) checkLeft = false;
            if (((index + 1) % 8) == 0) checkRight = false;
            int indexToCheck;
            if (!checkLeft) indexToCheck = index - 8;
            else indexToCheck = index - 9;
            int bound = index - 6;
            if (!checkRight) bound = index - 7;
            for (; indexToCheck < bound; indexToCheck++) {
                if (indexToCheck < 0) {
                    break;
                }
                if (cell_tvs.get(indexToCheck).getText().toString().equals("BOMBUNCLEARED")) {
                    numBombs += 1;
                }
            }
            if(checkLeft) {
                if (cell_tvs.get(index - 1).getText().toString().equals("BOMBUNCLEARED")) {
                    numBombs += 1;
                }
            }
            if (checkRight) {

                if (cell_tvs.get(index + 1).getText().toString().equals("BOMBUNCLEARED")) {
                    numBombs += 1;
                }
            }
            if (!checkLeft) indexToCheck = index + 8;
            else indexToCheck = index + 7;
            bound = index + 10;
            if (!checkRight) bound = index + 9;
            for (; indexToCheck < bound; indexToCheck++) {
                if (indexToCheck > 79) {
                    break;
                }
                if (cell_tvs.get(indexToCheck).getText().toString().equals("BOMBUNCLEARED")) {
                    numBombs += 1;
                }
            }
            if (!cell_tvs.get(index).getText().toString().equals("BOMBUNCLEARED") && numBombs != 0) {
                cell_tvs.get(index).setText(numBombs + "UNCLEARED");
            }
            if (!cell_tvs.get(index).getText().toString().equals("BOMBUNCLEARED") && numBombs == 0) {
                cell_tvs.get(index).setText("UNCLEARED");
            }
        }
    }

    private void revealCells(TextView tv) {
        int index = findIndexOfCellTextView(tv);
        if(!tv.getText().toString().equals("UNCLEARED") && !tv.getText().toString().equals("BOMBUNCLEARED")) {
            tv.setTextColor(Color.GRAY);
            tv.setBackgroundColor(Color.LTGRAY);
            tv.setText(tv.getText().toString().substring(0, 1));
            return;
        }
        if (tv.getText().toString().equals("UNCLEARED")) {
            tv.setText("");
            tv.setTextColor(Color.GRAY);
            tv.setBackgroundColor(Color.LTGRAY);
        }
        boolean checkLeft = true;
        boolean checkRight = true;
        if (index % 8 == 0) checkLeft = false;
        if (((index + 1) % 8) == 0) checkRight = false;

        int indexToCheck;
        if (!checkLeft) indexToCheck = index - 8;
        else indexToCheck = index - 9;
        int bound = index - 6;
        if (!checkRight) bound = index - 7;
        for (; indexToCheck < bound; indexToCheck++) {
            if (indexToCheck < 0) {
                break;
            }
            if(!cell_tvs.get(indexToCheck).getText().toString().equals("BOMBUNCLEARED") && !cell_tvs.get(indexToCheck).getText().toString().equals("") && !cell_tvs.get(indexToCheck).getText().toString().equals("UNCLEARED")) {
                cell_tvs.get(indexToCheck).setTextColor(Color.GRAY);
                cell_tvs.get(indexToCheck).setBackgroundColor(Color.LTGRAY);
                String display = cell_tvs.get(indexToCheck).getText().toString().substring(0, 1);
                cell_tvs.get(indexToCheck).setText(display);
            }
            else if (cell_tvs.get(indexToCheck).getText().toString().equals("UNCLEARED")) {
                revealCells(cell_tvs.get(indexToCheck));
            }
        }
        if(checkLeft) {
            if(!cell_tvs.get(index - 1).getText().toString().equals("BOMBUNCLEARED") && !cell_tvs.get(index - 1).getText().toString().equals("") && !cell_tvs.get(index - 1).getText().toString().equals("UNCLEARED")) {
                cell_tvs.get(index-1).setTextColor(Color.GRAY);
                cell_tvs.get(index-1).setBackgroundColor(Color.LTGRAY);
                String display = cell_tvs.get(index-1).getText().toString().substring(0, 1);
                cell_tvs.get(index-1).setText(display);
            }
            else if (cell_tvs.get(index - 1).getText().toString().equals("UNCLEARED")) {
                revealCells(cell_tvs.get(index - 1));
            }
        }
        if (checkRight) {
            if(!cell_tvs.get(index + 1).getText().toString().equals("BOMBUNCLEARED") && !cell_tvs.get(index + 1).getText().toString().equals("") && !cell_tvs.get(index + 1).getText().toString().equals("UNCLEARED")) {
                cell_tvs.get(index+1).setTextColor(Color.GRAY);
                cell_tvs.get(index+1).setBackgroundColor(Color.LTGRAY);
                String display = cell_tvs.get(index+1).getText().toString().substring(0, 1);
                cell_tvs.get(index+1).setText(display);
            }
            else if (cell_tvs.get(index + 1).getText().toString().equals("UNCLEARED")) {
                revealCells(cell_tvs.get(index + 1));
            }
        }
        if (!checkLeft) indexToCheck = index + 8;
        else indexToCheck = index + 7;
        bound = index + 10;
        if (!checkRight) bound = index + 9;
        for (; indexToCheck < bound; indexToCheck++) {
            if (indexToCheck > 79) {
                break;
            }
            if(!cell_tvs.get(indexToCheck).getText().toString().equals("BOMBUNCLEARED") && !cell_tvs.get(indexToCheck).getText().toString().equals("") && !cell_tvs.get(indexToCheck).getText().toString().equals("UNCLEARED")) {
                cell_tvs.get(indexToCheck).setTextColor(Color.GRAY);
                cell_tvs.get(indexToCheck).setBackgroundColor(Color.LTGRAY);
                String display = cell_tvs.get(indexToCheck).getText().toString().substring(0, 1);
                cell_tvs.get(indexToCheck).setText(display);
            }
            else if (cell_tvs.get(indexToCheck).getText().toString().equals("UNCLEARED")) {
                revealCells(cell_tvs.get(indexToCheck));
            }
        }
    }

    private void revealBombs() {
        for (int idx = 0; idx < cell_tvs.size(); idx++) {
            if (cell_tvs.get(idx).getText().toString().contains("BOMBUNCLEARED")) {
                cell_tvs.get(idx).setText("\uD83D\uDCA3");
                cell_tvs.get(idx).setTextColor(Color.GRAY);
                cell_tvs.get(idx).setBackgroundColor(Color.LTGRAY);
            }
        }
    }
    public void onClickMode(View view) {
        String ogMode = mode;
        if (ogMode.equals("pick")) {
            mode = "flag";
            btn.setText("\uD83D\uDEA9");
        }
        if (ogMode.equals("flag")) {
            mode = "pick";
            btn.setText("\u26CF");
        }
    }

    public boolean checkWin() {
        for(int idx = 0; idx < cell_tvs.size(); idx++) {
            if(cell_tvs.get(idx).getText().toString().contains("UNCLEARED")) {
                if(!cell_tvs.get(idx).getText().toString().contains("BOMB")) {
                    return false;
                }
            }
        }
        return true;
    }
    public void onClickTV(View view){
        TextView tv = (TextView) view;
        if(lost) {
            Intent intent = new Intent(this, DisplayResult.class);
            intent.putExtra("com.example.gridlayout.MESSAGE", "Used " + clock + " seconds.\n You lost.");
            startActivity(intent);
        }
        if(won) {
            Intent intent = new Intent(this, DisplayResult.class);
            intent.putExtra("com.example.gridlayout.MESSAGE", "Used " + clock + " seconds.\n You won.\n Good job!");
            startActivity(intent);
        }
        if(mode.equals("pick")) {
            if (tv.getText().toString().contains("\uD83D\uDEA9")) {
                tv.setText(ogText.get(tv));
                tv.setTextColor(Color.GRAY);
                tv.setBackgroundColor(Color.LTGRAY);
            }
            if (tv.getText().toString().equals("BOMBUNCLEARED")){
                revealBombs();
                lost = true;
                running = false;
            }
            else if (tv.getText().toString().contains("UNCLEARED")) {
                revealCells(tv);
                tv.setTextColor(Color.GRAY);
                tv.setBackgroundColor(Color.LTGRAY);
            }
        }
        if(mode.equals("flag")) {
            if(tv.getText().toString().contains("\uD83D\uDEA9")) {
                tv.setText(ogText.get(tv));
                totalFlags += 1;
            }
            else {
                tv.setText("\uD83D\uDEA9" + ogText.get(tv));
                totalFlags -= 1;
            }
        }
        won = checkWin();
        if (won) running = false;
    }
}