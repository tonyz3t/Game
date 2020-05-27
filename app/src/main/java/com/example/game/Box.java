package com.example.game;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Box implements Updatable {
    private Bitmap mImage;
    private int mScreenHeight;
    private int mScreenWidth;

    // x y coords
    private int mX, mY;
    private int xVel = -10;

    private Context mAppContext;

    // Constructor
    public Box(Context context, Bitmap image, int x){
        mAppContext = context;
        mImage = image;

        mScreenHeight = context.getResources().getDisplayMetrics().heightPixels;
        mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;

        mX = x;
        mY = context.getResources().getDisplayMetrics().heightPixels/2;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(mImage, mX, mY, null);
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
