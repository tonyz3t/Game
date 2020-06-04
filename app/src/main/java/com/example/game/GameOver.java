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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //activity.startActivity(new Intent(activity,GameOver.class));
                startActivity(new Intent(GameOver.this,MainActivity.class));
            }
        });
        //Go back to main menu
        Button menuButton = findViewById(R.id.mainMenuButton);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //activity.startActivity(new Intent(activity,GameOver.class));
                startActivity(new Intent(GameOver.this, MainMenuActivity.class));
            }
        });
    }
}
