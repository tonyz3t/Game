package com.example.game;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

// This class represents our main character sprite
public class Sprite implements Updatable {

    // X and Y coordinates of sprite object
    private int mX, mY;
    // how high the jumps
    private int mJumpVel = -450;
    // the speed at which the sprite falls
    private int mGravity = 40;

    // Double Jump variables
    private boolean mIsInAir;
    private boolean mHasDoubleJumped;


    //Height of the screen
    private int SCREEN_HEIGHT;

    public Sprite(Context context){
        SCREEN_HEIGHT = context.getResources().getDisplayMetrics().heightPixels;

        // set the starting position of the sprite
        mX = 100;
        mY = (SCREEN_HEIGHT/2) -200;

        mIsInAir = mHasDoubleJumped = false;

    }

    // Make the sprite jump
    public void jump(){
        // check to see if the player is in the air and has double jumped
        // exit out of this function if both conditions are true
        if(mIsInAir && mHasDoubleJumped) return;

        // double jump logic
        if (mIsInAir){
            mY += mJumpVel;
            mHasDoubleJumped = true;
        } else {
            mY += mJumpVel;
            mIsInAir = true;
        }
    }


    @Override
    public void update() {
        mY += mGravity;

        // dont let the sprite fall beyond the floor line
        if(mY > SCREEN_HEIGHT/2) {
            mY = SCREEN_HEIGHT/2;
            mIsInAir = false;
            mHasDoubleJumped = false;
        }


    }

    public int getY(){
        return mY;
    }

    public int getX(){
        return mX;
    }


}
