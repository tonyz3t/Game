package com.example.game;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Box implements Updatable {
    private int mScreenHeight;
    private int mScreenWidth;

    // x y coords
    private int mX, mY;
    private int xVel = -10;

    private Context mAppContext;

    // Constructor
    public Box(Context context, int x){
        mAppContext = context;

        mScreenHeight = context.getResources().getDisplayMetrics().heightPixels;
        mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;

        mX = x;
        mY = context.getResources().getDisplayMetrics().heightPixels/2;
    }


    @Override
    public void update() {
        mX += xVel;

        if(mX + 50 < 0) mX = mScreenWidth;

    }

    public int getX(){
        return mX;
    }

    public int getY(){
        return mY;
    }
}
