package com.example.game;

import android.view.Display;
import android.view.WindowManager;

import java.util.Random;

public class Boxes implements Updatable{
    private int mBoxWidth = 100;
    private int mBoxHorizontalGap = (int) Math.random() % 200; // Alter later

    // Box movement
    private float xVel = -5.0f;
    private float x1, x2, x3;
    private int y;      // All boxes are on the ground level for now

    // Closest Box
    private int mCurrentBox;
    // Array to hold our boxes
    private float[][] mBoxCoords = new float[3][2];

    private Random rand;

    public Boxes() {
        rand = new Random();

        resetBoxes();
    }

    // Resets the box positons for a new game
    private void resetBoxes() {
        mCurrentBox = 0;

        x1 = MainActivity.getWidth() * 2;
        x2 = x1 + mBoxWidth + mBoxHorizontalGap;
        x3 = x2 + mBoxWidth + mBoxHorizontalGap;

        y = MainActivity.getHeight() / 2;
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
            x1 = MainActivity.getWidth();
            mCurrentBox = 1;
        }

        if(x2 + mBoxWidth < 0) {
            x2 = MainActivity.getWidth();
            mCurrentBox = 2;
        }

        if(x2 + mBoxWidth < 0) {
            x2 = MainActivity.getWidth();
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
}


