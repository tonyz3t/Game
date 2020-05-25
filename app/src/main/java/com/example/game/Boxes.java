package com.example.game;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.Random;

public class Boxes implements Updatable, Renderable{
    private int mBoxWidth = 100;
    private int mBoxHorizontalGap = (int) Math.random() % 200; // Alter later

    // Box movement
    private float xVel = -5.0f;
    private float x1, x2, x3;
    private float y;      // All boxes are on the ground level for now

    // Closest Box
    private int mCurrentBox;
    // Array to hold our boxes
    private float[][] mBoxCoords = new float[3][2];

    private Random rand;
    private Context mMainContext;

    public Boxes(Context context) {
        rand = new Random();

        // Get window dimensions
        mMainContext = context;

        resetBoxes();
    }

    // Resets the box positons for a new game
    private void resetBoxes() {
        mCurrentBox = 0;
        x1 = getScreenWidth() /2;
        x2 = x1 + mBoxWidth + mBoxHorizontalGap;
        x3 = x2 + mBoxWidth + mBoxHorizontalGap;

        y = (float) getScreenHeight() / 2;
    }

    private float getScreenWidth(){
        return (float) mMainContext.getResources().getDisplayMetrics().widthPixels;
    }

    private float getScreenHeight(){
        return (float) mMainContext.getResources().getDisplayMetrics().heightPixels;
    }

    public float[] getCurrentBox(){
        return mBoxCoords[mCurrentBox];
    }

    public int getCurrentBoxId(){
        return mCurrentBox;
    }

    public int getBoxWidth(){
        return 0;
    }

    @Override
    public void update() {
        x1 += xVel;
        x2 += xVel;
        x3 += xVel;

        // if our boxes reach the end of the screen, reset them to the start of the screen
        if(x1 + mBoxWidth < 0) {
            x1 = getScreenWidth();
            mCurrentBox = 1;
        }

        if(x2 + mBoxWidth < 0) {
            x2 = getScreenWidth();
            mCurrentBox = 2;
        }

        if(x2 + mBoxWidth < 0) {
            x2 = getScreenWidth();
            mCurrentBox = 0;
        }

        // Update box coordinates
        switch (mCurrentBox) {
            case 0:
                mBoxCoords[0][0] = x1;
                mBoxCoords[0][1] = y;
                break;
            case 1:
                mBoxCoords[1][0] = x1;
                mBoxCoords[1][1] = y;
                break;
            case 2:
                mBoxCoords[2][0] = x1;
                mBoxCoords[2][1] = y;
                break;
        }
    }

    @Override
    public void render(Canvas canvas, Paint globalPaint, float interpolation) {
        //canvas.drawBitmap();
    }
}


