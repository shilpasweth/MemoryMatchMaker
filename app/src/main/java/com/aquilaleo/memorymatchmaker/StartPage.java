package com.aquilaleo.memorymatchmaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_page);

        Button button = (Button) findViewById(R.id.startGame);
        TextView text = findViewById(R.id.textView);

        text.setText(" This is a memory match game with normal cards.\n\n" +
                " Each card has an 'element' and a 'number' associated with it.\n\n"+
                " At the start of the game you are required to memorize the positions of the elements.\n\n"+
                " The cards will then flip over to reveal the numbers.\n\n"+
                "Gameplay requires you to select two cards at a time after which scores will be tallied for that combination.\n\n"+
                " There are 3 types of scores\n\n" +
                " Base is derived by 10 - abs(card1-card2).\n\n"+
                " Element gives a +5 bonus if both cards are of the same element, -5 if they are of oppossing natures(FIRE->WATER, WIND->EARTH).\n\n"+
                " Royal is multiplier that is activated when a royal card is drawn, it favours identical elements and gives negation for opposing elements.\n\n");

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent = new Intent(StartPage.this,
                        MainActivity.class);
                startActivity(myIntent);
            }
        });
    }

}
