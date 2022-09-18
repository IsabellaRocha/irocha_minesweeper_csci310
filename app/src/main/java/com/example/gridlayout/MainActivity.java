package com.example.gridlayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int COLUMN_COUNT = 8;

    // save the TextViews of all cells in an array, so later on,
    // when a TextView is clicked, we know which cell it is
    private ArrayList<TextView> cell_tvs;

    private int dpToPixel(int dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cell_tvs = new ArrayList<TextView>();
        // Method (2): add four dynamically created cells
        GridLayout grid = (GridLayout) findViewById(R.id.gridLayout01);
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
            cell_tvs.get(rando).setText("BOMB");
        }


    }

    private int findIndexOfCellTextView(TextView tv) {
        for (int n=0; n<cell_tvs.size(); n++) {
            if (cell_tvs.get(n) == tv)
                return n;
        }
        return -1;
    }

    private int findNumberOfBombs(TextView tv) {
        int index = findIndexOfCellTextView(tv);
        int numBombs = 0;
        for (int indexToCheck = index - 9; indexToCheck < index - 6; indexToCheck++) {
            if (indexToCheck < 0) {
                break;
            }
            if (cell_tvs.get(indexToCheck).getText().toString().equals("BOMB")) {
                numBombs += 1;
            }
        }
        if (index % 8 != 0 && cell_tvs.get(index - 1).getText().toString().equals("BOMB")) {
            numBombs += 1;
        }
        if ((index + 1) % 8 != 0 && cell_tvs.get(index + 1).getText().toString().equals("BOMB")) {
            numBombs += 1;
        }
        for (int indexToCheck = index + 7; indexToCheck < index + 10; indexToCheck++) {
            if (indexToCheck > 70) {
                break;
            }
            if (cell_tvs.get(indexToCheck).getText().toString().equals("BOMB")) {
                numBombs += 1;
            }
        }
        return numBombs;
    }
    public void onClickTV(View view){
        TextView tv = (TextView) view;
        int n = findNumberOfBombs(tv);
        if (!tv.getText().toString().equals("BOMB") && n != 0) {
            tv.setText(String.valueOf(n));
        }
        else if (tv.getText().toString().equals("BOMB")){
            tv.setText("\uD83D\uDCA3");
        }
        tv.setTextColor(Color.GRAY);
        tv.setBackgroundColor(Color.LTGRAY);
    }
}