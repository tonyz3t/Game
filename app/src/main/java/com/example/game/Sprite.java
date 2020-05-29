package com.example.game;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

// This class represents our main character sprite
public class Sprite implements Updatable {

    // X and Y coordinates of sprite object
    private int mX, mY;
    // how high the jumps
    private int mJumpVel = -150;
    // the speed at which the sprite falls
    private int mGravity = 15;


    //Height of the screen
    private int SCREEN_HEIGHT;

    public Sprite(Context context){
        SCREEN_HEIGHT = context.getResources().getDisplayMetrics().heightPixels;

        // set the starting position of the sprite
        mX = 100;
        mY = (SCREEN_HEIGHT/2) -200;

    }

    // Make the sprite jump
    public void jump(){
        mY += mJumpVel;
    }


    @Override
    public void update() {
        mY += mGravity;

        // dont let the sprite fall beyond the floor line
        if(mY > SCREEN_HEIGHT/2) mY = SCREEN_HEIGHT/2;


    }

    public int getY(){
        return mY;
    }

    public int getX(){
        return mX;
    }


}
