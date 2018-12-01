package com.aquilaleo.memorymatchmaker;

import android.animation.Animator;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int screenHeight;
    int screenWidth;

    View root;

    int rowSize = 4;
    int columnSize = 3;

    int cardHeight;
    int cardWidth;

    TableLayout board;

    int score = 0;

    int backDrawables[] = {R.drawable.card_back_ace
            ,R.drawable.card_back_two
            ,R.drawable.card_back_three
            ,R.drawable.card_back_four
            ,R.drawable.card_back_five
             ,R.drawable.card_back_six
            ,R.drawable.card_back_seven
            ,R.drawable.card_back_eight
            ,R.drawable.card_back_nine
            ,R.drawable.card_back_ten
            ,R.drawable.card_back};

    int frontDrawables[] = {R.drawable.fire_demon
            ,R.drawable.water_demon
            ,R.drawable.earth_demon
            ,R.drawable.air_demon

            ,R.drawable.fire_jack
            ,R.drawable.water_jack
            ,R.drawable.earth_jack
            ,R.drawable.air_jack

            ,R.drawable.fire_queen
            ,R.drawable.water_queen
            ,R.drawable.earth_queen
            ,R.drawable.air_queen

            ,R.drawable.fire_king
            ,R.drawable.water_king
            ,R.drawable.earth_king
            ,R.drawable.air_king};

    static int FIRE = 0;
    static int WATER = 1;
    static int EARTH = 2;
    static int AIR = 3;

    Card cardDeck[][] = new Card[4][13];
    Boolean cardDealt[][] = new Boolean[4][13];
    final Card[][] boardCards= new Card[rowSize][columnSize];
    ArrayList<View> selectedPositions = new ArrayList<View>();
    ArrayList<View> cardSlots = new ArrayList<View>();

    void setCardElements(){
        for(int i=0;i<4;i++){
            for(int j=0;j<13;j++){
                int front, back;
                if(j<10){
                    front = frontDrawables[i];
                    back = backDrawables[j];
                }
                else{
                    front = frontDrawables[i+(j%10)*4+4];
                    back = backDrawables[10];
                }
                cardDeck[i][j] = new Card(i, j, front, back);
                cardDeck[i][j].scaledFrontDrawable = createScaledDrawable(cardDeck[i][j].frontDrawable);
                cardDeck[i][j].scaledBackDrawable = createScaledDrawable(cardDeck[i][j].backDrawable);
                cardDealt[i][j] = false;
            }
        }
    }

    void setScreenDim(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
    }

    Drawable createScaledDrawable(int d){
        Drawable orgDrawable = getResources().getDrawable(d);
        Bitmap bitmap = ((BitmapDrawable) orgDrawable).getBitmap();
        final Drawable scaledDrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, cardWidth, cardHeight, true));
        return scaledDrawable;
    }

    Boolean isAllCardsDealt(){
        for(int i=0; i<4;i++){
            for(int j=0;j<13;j++){
                if(cardDealt[i][j]==false){
                    return false;
                }
            }
        }
        return true;
    }

    void frontFlipAll(){
        for(int i=0;i<4;i++){
            for(int j=0;i<3;j++){
                cardSlots.get(i*(3)+j).setBackgroundDrawable(boardCards[i][j].scaledFrontDrawable);
            }
        }
    }

    void backFlipAll(){
        for(int i=0;i<4;i++){
            for(int j=0;j<3;j++){
                Log.d("MainActivity","i: "+i+", j: "+j+", index: "+(i*3+j));
                //cardSlots.get(i*3+j).setBackgroundDrawable(boardCards[i][j].scaledBackDrawable);
                flipAnimation(cardSlots.get(i*3+j),boardCards[i][j].scaledBackDrawable);
            }
        }
    }

    public void singleDisableTouch(int row, int col) {
        int i = row*(3)+col;
        cardSlots.get(i).setClickable(false);
    }

    public void singleEnableTouch(int row, int col) {
        int i = row*(3)+col;
        cardSlots.get(i).setClickable(true);
    }

    public void disableTouch() {
        for(int i=0; i<cardSlots.size();i++){
            cardSlots.get(i).setClickable(false);
        }
    }

    public void enableTouch() {
        for(int i=0; i<cardSlots.size();i++){
            cardSlots.get(i).setClickable(true);
        }
    }

    public void flipAnimation(final View iv, final Drawable drawable){
        iv.setRotationY(0f);
        iv.animate().rotationY(90f).setListener(new Animator.AnimatorListener()
        {

            @Override
            public void onAnimationStart(Animator animation)
            {
            }

            @Override
            public void onAnimationRepeat(Animator animation)
            {
            }

            @Override
            public void onAnimationEnd(Animator animation)
            {
                iv.setBackgroundDrawable(drawable);
                iv.setRotationY(270f);
                iv.animate().rotationY(360f).setListener(null);

            }

            @Override
            public void onAnimationCancel(Animator animation)
            {
            }
        });
    }

    void cardClick(View v){

        int row = (int)((Pair)v.getTag()).first;
        int col = (int)((Pair)v.getTag()).second;
        v.setBackgroundDrawable(boardCards[row][col].scaledFrontDrawable);
        singleDisableTouch(row, col);
        selectedPositions.add(v);

        if(selectedPositions.size()==2){
            disableTouch();
            Runnable r = new Runnable() {
                @Override
                public void run(){
                    for(int i=0;i<2;i++){
                        View v = selectedPositions.get(i);
                        int a = (int)((Pair)(v.getTag())).first;
                        int b = (int)((Pair)(v.getTag())).second;
                        v.setBackgroundDrawable(boardCards[a][b].scaledBackDrawable);
                    }
                    selectedPositions.clear();
                    enableTouch();
                }
            };

            Handler h = new Handler();
            h.postDelayed(r, 5000);
        }

        //Toast.makeText(MainActivity.this, "Clicked "+((Pair)v.getTag()).first+" "+((Pair)v.getTag()).second, Toast.LENGTH_SHORT).show();



    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        setContentView(R.layout.activity_main);

        board = (TableLayout) findViewById(R.id.board);

        //Get screen dimensions
        setScreenDim();

        cardHeight = (int)(screenHeight/(float)(rowSize+1));
        cardWidth = (int)((cardHeight*9)/16.0);

        setCardElements();

        for (int i = 0; i <rowSize; i++) {

            TableRow row= new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            row.setLayoutParams(lp);
            //row.setBackgroundColor(Color.GREEN);
            row.setGravity(Gravity.CENTER_HORIZONTAL);

            for(int j = 0; j<columnSize; j++){
                Random r = new Random();
                int a = r.nextInt(4);
                int b = r.nextInt(10);

                while(cardDealt[a][b]){
                    a++;
                    a%=4;
                    b++;
                    b%=10;
                }

                boardCards[i][j] = cardDeck[a][b];
                cardDealt[a][b] = true;

                FrameLayout frameLayout = new FrameLayout(this);
                frameLayout.setPadding(2,2,2,2);
                //frameLayout.setBackgroundColor(Color.MAGENTA);
                ToggleButton card = new ToggleButton(this);
                card.setLayoutParams(new FrameLayout.LayoutParams(cardWidth,cardHeight));

                card.setBackgroundDrawable(boardCards[i][j].scaledFrontDrawable);
                card.setText(null);
                card.setTextOn(null);
                card.setTextOff(null);
                card.setHeight(32);
                card.setWidth(18);
                card.setTag(new Pair(i,j));
                card.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        cardClick(v);
                    }
                });
                cardSlots.add(card);
                frameLayout.addView(card);

                row.addView(frameLayout);
            }


            board.addView(row,i);
        }
        disableTouch();
        Runnable r = new Runnable() {
            @Override
            public void run(){
                backFlipAll();
                enableTouch();
            }
        };

        Handler h = new Handler();
        h.postDelayed(r, 10000);

    }
}
