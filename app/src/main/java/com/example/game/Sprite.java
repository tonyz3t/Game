package com.example.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

// This class represents our main character sprite
public class Sprite implements Updatable {

    // X and Y coordinates of sprite object
    private int mX, mY;
    // how high the jumps
    private int mJumpVel = -750;
    // the speed at which the sprite falls
    private int mGravity = 30;

    // Double Jump variables
    private boolean mIsInAir;
    private boolean mHasDoubleJumped;
    private Activity activity;

    //Height of the screen
    private int SCREEN_HEIGHT;

    public Sprite(Context context){
        SCREEN_HEIGHT = context.getResources().getDisplayMetrics().heightPixels;

        // set the starting position of the sprite
        mX = 100;
        mY = (SCREEN_HEIGHT/2) -200;

        mIsInAir = mHasDoubleJumped = false;
        this.activity = (Activity) context;

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

        //Check to see if sprite object is touching any part of crate object. If so then "Game Over".
        //
        //
        //Get character imageview
        ImageView sprite = activity.findViewById(R.id.char_image_view);
        //Get all three box imageviews
        ImageView boxOne = activity.findViewById(R.id.sprite_crate);
        ImageView boxTwo = activity.findViewById(R.id.sprite_crate2);
        ImageView boxThree = activity.findViewById(R.id.sprite_crate3);

        //Get sprite x and y coordinates.
        int[] spritePosition = new int[2];
        int[] boxOnePosition = new int[2];
        int[] boxTwoPosition = new int[2];
        int[] boxThreePosition = new int[2];

        sprite.getLocationOnScreen(spritePosition);
        boxOne.getLocationOnScreen(boxOnePosition);
        boxTwo.getLocationOnScreen(boxTwoPosition);
        boxThree.getLocationOnScreen(boxThreePosition);


        //Create rectangle objects for character sprite, box1, box2, box3
        Rect rectSprite = new Rect(spritePosition[0],spritePosition[1], spritePosition[0]+sprite.getMeasuredWidth(),spritePosition[1]+sprite.getMeasuredHeight());
        Rect rectBoxOne = new Rect(boxOnePosition[0], boxOnePosition[1], boxOnePosition[0]+boxOne.getMeasuredWidth(), boxOnePosition[1]+boxOne.getMeasuredHeight());
        Rect rectBoxTwo = new Rect(boxTwoPosition[0], boxTwoPosition[1], boxTwoPosition[0]+boxTwo.getMeasuredWidth(), boxTwoPosition[1]+ boxTwo.getMeasuredHeight());
        Rect rectBoxThree = new Rect(boxThreePosition[0], boxThreePosition[1], boxThreePosition[0]+boxThree.getMeasuredWidth(), boxThreePosition[1]+boxThree.getMeasuredHeight());

        //See if they intersect
        boolean overlapWithBoxOne = rectSprite.intersect(rectBoxOne);
        boolean overlapWithBoxTwo = rectSprite.intersect(rectBoxTwo);
        boolean overlapWithBoxThree = rectSprite.intersect(rectBoxThree);

        if(overlapWithBoxOne||overlapWithBoxTwo||overlapWithBoxThree){
            //Game over
            int x = 10;
            activity.startActivity(new Intent(activity,GameOver.class));
        }

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
