package com.example.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameOver extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        //Play Again
        Button button = findViewById(R.id.playAgainButton);
        //Detect click on button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //switch activity to main activity. IE play game again
                startActivity(new Intent(GameOver.this,MainActivity.class));
            }
        });
        //Go back to main menu
        Button menuButton = findViewById(R.id.mainMenuButton);
        //Listen for click
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go back to main menu
                startActivity(new Intent(GameOver.this, MainMenuActivity.class));
            }
        });
    }
}
