package com.example.game;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

// This class represents our main character sprite
public class Sprite implements Updatable {

    private int mX, mY;
    private int mVel;
    private int mJumpVel = -150;
    private int mGravity = 15;
    private boolean mIsTouched;
    private boolean mIsReleased;

    //Screen space
    private int SCREEN_HEIGHT;

    public Sprite(Context context){
        SCREEN_HEIGHT = context.getResources().getDisplayMetrics().heightPixels;

        resetSprite();


        mIsTouched = false;
        mIsReleased = true;
    }

    public void resetSprite(){
        mX = 100;
        mY = (SCREEN_HEIGHT/2) -200;
        mVel = 0;
    }

    public void jump(){
        mY += mJumpVel;
    }

    @Override
    public void update() {
        mY += mGravity;

        if(mY > SCREEN_HEIGHT/2) mY = SCREEN_HEIGHT/2;

        if (isTouched())  {
            jump();
        }
    }

    private boolean isTouched() {
        boolean t = mIsTouched;
        mIsTouched = false;
        return t;
    }


    public void setIsTouched(boolean state){
        mIsTouched = state;
    }

    public void setIsReleased(boolean state){
        mIsReleased = state;
    }

    public boolean isReleased(){
        return mIsReleased;
    }

    public int getY(){
        return mY;
    }

    public int getX(){
        return mX;
    }


}
