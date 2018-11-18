package com.aquilaleo.memorymatchmaker;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
        int screenWidth = displayMetrics.widthPixels;

        int rowSize = 4;
        int columnSize = 3;

        int cardHeight = (int)(screenHeight/(float)(rowSize+1));
        int cardWidth = (int)((cardHeight*9)/16.0);

        Drawable orgFireDemon = getResources().getDrawable(R.drawable.fire_demon);
        Bitmap bitmap = ((BitmapDrawable) orgFireDemon).getBitmap();
        final Drawable drawFireDemon = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, cardWidth, cardHeight, true));

        Drawable orgCardBack = getResources().getDrawable(R.drawable.card_back);
        bitmap = ((BitmapDrawable) orgCardBack).getBitmap();
        final Drawable drawCardBack = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, cardWidth, cardHeight, true));



        Card[][] cards= new Card[rowSize][columnSize];

        TableLayout board = (TableLayout) findViewById(R.id.board);


        for (int i = 0; i <rowSize; i++) {

            TableRow row= new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            row.setLayoutParams(lp);
            //row.setBackgroundColor(Color.GREEN);
            row.setGravity(Gravity.CENTER_HORIZONTAL);

            for(int j = 0; j<columnSize; j++){

                FrameLayout frameLayout = new FrameLayout(this);
                frameLayout.setPadding(2,2,2,2);
                //frameLayout.setBackgroundColor(Color.MAGENTA);
                ToggleButton card = new ToggleButton(this);
                card.setLayoutParams(new FrameLayout.LayoutParams(cardWidth,cardHeight));

                card.setBackgroundDrawable(drawCardBack);
                card.setText(null);
                card.setTextOn(null);
                card.setTextOff(null);
                card.setHeight(32);
                card.setWidth(18);
                card.setTag(new Pair(i,j));
                card.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "Clicked "+((Pair)v.getTag()).first+" "+((Pair)v.getTag()).second, Toast.LENGTH_SHORT).show();
                        if(((ToggleButton)v).isChecked()){
                            v.setBackgroundDrawable(drawFireDemon);
                        }
                        else{
                            v.setBackgroundDrawable(drawCardBack);
                        }
                    }
                });
                frameLayout.addView(card);

                row.addView(frameLayout);
            }


            board.addView(row,i);
        }

    }
}
