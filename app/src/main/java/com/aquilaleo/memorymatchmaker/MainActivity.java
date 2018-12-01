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
import android.widget.ImageView;
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

    int cardCount = 52;
    int royalBonus = 1;
    int elementBonus = 0;

    int cardHeight;
    int cardWidth;

    TableLayout board;
    ImageView cardDeckView;
    ToggleButton royalSlotView;

    int score = 0;

    Boolean gameOver = false;

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



    int enemyElement[] = {WATER,FIRE,AIR,EARTH};

    Card cardDeck[][] = new Card[4][13];
    Boolean cardDealt[][] = new Boolean[4][13];
    final Card[][] boardCards= new Card[rowSize][columnSize];
    ArrayList<View> selectedPositions = new ArrayList<View>();
    ArrayList<View> cardSlots = new ArrayList<View>();
    ArrayList<Card> dealtRoyals = new ArrayList<Card>();

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
                    Log.d("PicTest","i: "+i+", j: "+j+", front: "+i+(j%10)*4+4);
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
                //cardSlots.get(i*(3)+j).setBackgroundDrawable(boardCards[i][j].scaledFrontDrawable);
                flipAnimation(cardSlots.get(i*3+j),boardCards[i][j].scaledFrontDrawable);
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

    public void dealCards(final View view){
        Log.d("TestingCards","Deal1");
        if(isAllCardsDealt()){
            Toast.makeText(MainActivity.this, "Game ended with Score: "+score, Toast.LENGTH_SHORT).show();
            gameOver = true;
            return;
        }
        Log.d("TestingCards","Deal2");
        int a = (int)((Pair)(view.getTag())).first;
        int b = (int)((Pair)(view.getTag())).second;

        //Replace card1
        Random rand = new Random();
        int p = rand.nextInt(4);
        int q = rand.nextInt(10);

        while(cardDealt[p][q]||cardDeck[p][q].number>=10){
            q++;
            p+=q/13;
            p%=4;
            q%=13;

            if(cardDeck[p][q].number>=10&&!cardDealt[p][q]){
                dealtRoyals.add(cardDeck[p][q]);
                cardDealt[p][q] = true;
                Drawable royalDrawable = dealtRoyals.get(dealtRoyals.size()-1).scaledFrontDrawable;
                flipAnimation(cardDeckView, royalDrawable);
                flipAnimation(cardDeckView, createScaledDrawable(R.drawable.card_back));
                flipAnimation(royalSlotView, royalDrawable);
                cardCount--;
                if(isAllCardsDealt()){
                    Toast.makeText(MainActivity.this, "Game ended with Score: "+score, Toast.LENGTH_SHORT).show();
                    gameOver = true;
                    return;
                }
            }
        }

        final Drawable newDrawableFront = cardDeck[p][q].scaledFrontDrawable;
        final Drawable newDrawableBack = cardDeck[p][q].scaledBackDrawable;
        cardDealt[p][q] = true;
        boardCards[a][b] = cardDeck[p][q];

        Runnable r = new Runnable() {
            @Override
            public void run(){
                flipAnimation(cardDeckView, newDrawableFront);
            }
        };

        Handler h = new Handler();
        h.postDelayed(r, 1000);

        r = new Runnable() {
            @Override
            public void run(){
                //view.setBackgroundDrawable(newDrawableFront);
                flipAnimation(view, newDrawableFront);

            }
        };
        h.postDelayed(r, 1000);
        r = new Runnable() {
            @Override
            public void run(){
                flipAnimation(view,newDrawableBack);
            }
        };
        h.postDelayed(r, 3000);
        Log.d("TestingCards","Deal4");
    }

    void cardClick(final View v){

        int row = (int)((Pair)v.getTag()).first;
        int col = (int)((Pair)v.getTag()).second;
        //v.setBackgroundDrawable(boardCards[row][col].scaledFrontDrawable);
        flipAnimation(v,boardCards[row][col].scaledFrontDrawable);


        singleDisableTouch(row, col);
        selectedPositions.add(v);

        if(selectedPositions.size()==2){

            disableTouch();
            final View view1 = selectedPositions.get(0);
            int a = (int)((Pair)(view1.getTag())).first;
            int b = (int)((Pair)(view1.getTag())).second;
            Card card1 = boardCards[a][b];

            final View view2 = selectedPositions.get(1);
            int c = (int)((Pair)(view2.getTag())).first;
            int d = (int)((Pair)(view2.getTag())).second;
            Card card2 = boardCards[c][d];

            //element bonus
            if(card1.element == card2.element){
                elementBonus = 5;
            }
            else if(card1.element == enemyElement[card2.element]){
                elementBonus = -5;
            }
            else{
                elementBonus = 0;
            }
            //Toast.makeText(MainActivity.this, "Element Bonus: "+elementBonus, Toast.LENGTH_SHORT).show();
            TextView elementView = findViewById(R.id.elementBonusView);
            elementView.setText("Element Bonus = "+elementBonus);
            Log.d("TestingCards1","Clicked "+((Pair)v.getTag()).first+" "+((Pair)v.getTag()).second);
            //royal bonus
            if(dealtRoyals.size()<=0) {
                royalBonus = 1;
            }
            else{
                Card royal = dealtRoyals.get(dealtRoyals.size()-1);
                if(card1.element == royal.element && card2.element == royal.element){
                    royalBonus = 10 + (royal.number%10);
                }
                else if(card1.element == royal.element || card2.element == royal.element){
                    royalBonus = 5 + (royal.number%10)*2;
                    if(card1.element == enemyElement[royal.element] || card2.element == enemyElement[royal.element]){
                        royalBonus = 2 + (royal.number%10);
                    }
                }
                else if(card1.element == enemyElement[royal.element] || card2.element == enemyElement[royal.element]){
                    royalBonus = -5 - (royal.number%10);
                }
                else{
                    royalBonus = 1;
                }
                dealtRoyals.remove(dealtRoyals.size()-1);
                if(dealtRoyals.size() <= 0){
                    royalSlotView.setBackgroundDrawable(createScaledDrawable(R.drawable.royal_slot));
                }
                else{
                    flipAnimation(royalSlotView,dealtRoyals.get(dealtRoyals.size()-1).scaledFrontDrawable);
                }
            }
            //Toast.makeText(MainActivity.this, "Royal Bonus: "+royalBonus, Toast.LENGTH_SHORT).show();
            TextView royalView = findViewById(R.id.royalBonusView);
            royalView.setText("Royal Bonus = "+royalBonus);
            Log.d("TestingCards2","Clicked "+((Pair)v.getTag()).first+" "+((Pair)v.getTag()).second);

            int base = 10-Math.abs(card1.number-card2.number);
            TextView baseView = findViewById(R.id.baseScoreView);
            baseView.setText("Base Score = "+base);

            int addit = base*royalBonus+elementBonus;

            score+= addit;

            //Toast.makeText(MainActivity.this, "Score: "+score, Toast.LENGTH_SHORT).show();
            TextView scoreView = findViewById(R.id.scoreView);
            scoreView.setText("Score = "+score+"("+addit+")");

            Log.d("TestingCards3","Clicked "+((Pair)v.getTag()).first+" "+((Pair)v.getTag()).second);

            Runnable run = new Runnable() {
                @Override
                public void run(){
                    dealCards(view1);
                }
            };

            Handler hand = new Handler();
            hand.postDelayed(run, 1000);

            run = new Runnable() {
                @Override
                public void run(){
                    dealCards(view2);
                }
            };
            hand.postDelayed(run, 4000);

            if(!gameOver){

                selectedPositions.clear();
                run = new Runnable() {
                    @Override
                    public void run(){
                        flipAnimation(cardDeckView, createScaledDrawable(R.drawable.card_back));
                    }
                };
                hand.postDelayed(run, 6000);
                enableTouch();
            }





        }




        Log.d("TestingCards7","Clicked "+((Pair)v.getTag()).first+" "+((Pair)v.getTag()).second);
        //Toast.makeText(MainActivity.this, "Clicked "+((Pair)v.getTag()).first+" "+((Pair)v.getTag()).second, Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        setContentView(R.layout.activity_main);

        board = (TableLayout) findViewById(R.id.board);
        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);
        cardDeckView = (ImageView) findViewById(R.id.cardDeckView);
        royalSlotView = (ToggleButton) findViewById(R.id.royalSlotView);
        royalSlotView.setText(null);
        royalSlotView.setTextOn(null);
        royalSlotView.setTextOff(null);

        //Get screen dimensions
        setScreenDim();

        cardHeight = (int)(screenHeight/(float)(rowSize+2));
        cardWidth = (int)((cardHeight*9)/16.0);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(cardWidth, cardHeight);
        cardDeckView.setLayoutParams(layoutParams);
        cardDeckView.setBackgroundDrawable(createScaledDrawable(R.drawable.card_back));
        royalSlotView.setLayoutParams(layoutParams);
        royalSlotView.setBackgroundDrawable(createScaledDrawable(R.drawable.royal_slot));
        royalSlotView.setClickable(false);

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

                int inita = a;
                int initb =b;

                while(cardDealt[a][b]){
                    b++;
                    a+=b/10;
                    a%=4;
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
                //card.setHeight(32);
                //card.setWidth(18);
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

        cardCount-=12;

        disableTouch();
        Runnable r = new Runnable() {
            @Override
            public void run(){
                backFlipAll();
                enableTouch();
            }
        };

        Handler h = new Handler();
        h.postDelayed(r, 5000);

    }
}
