package com.example.game;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.game.R;

public class MainActivity extends AppCompatActivity {
    // OUR MEMBER VARIABLES
    boolean animationOver = true;
    private Button mPauseButton;
    //private PauseDialogFragment mPauseDialog;
    private ImageView image;
    private FrameLayout screenCoverLayout;
    //Has double jump happened yet? Set to false by default
    boolean hasDoubleJumpHappened = false;

    // Our boxes object
    Boxes mBox = new Boxes();

    //Our Display size
    //private Display mDisplay = getWindowManager().getDefaultDisplay();
    private static Point mDisplaySize = new Point();

    // List of our updatable objects
    private ArrayList<Updatable> updatables = new ArrayList<>();

    private void addUpdatable(Updatable u){
        updatables.add(u);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Character Image
        image = findViewById(R.id.imageImageView);
        // Layout to detect screen touch
        screenCoverLayout = (FrameLayout) findViewById(R.id.screenFrameLayout);
        mPauseButton = (Button) findViewById(R.id.pause_button);

        addUpdatable(mBox);

        // Start our background task
        new BackGroundTask().execute();


        //Detect Screen touch
        screenCoverLayout.setOnTouchListener(new View.OnTouchListener() {
            //set max allowable click duration to prevent long holds
            private static final int MAX_CLICK_DURATION = 450;
            //t=0
            private long startClickTime;


            @Override
            //Screen has been touched. Carry out onTouch method
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    //Store time of inital press
                    case MotionEvent.ACTION_DOWN: {
                        startClickTime = Calendar.getInstance().getTimeInMillis();
                        break;
                    }
                    //Lifting finger off screen
                    case MotionEvent.ACTION_UP: {
                        //Only press if not long hold
                        long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
                        if (clickDuration < MAX_CLICK_DURATION) {
                            if (!hasDoubleJumpHappened && !animationOver) {
                                doubleJump(image);

                            }

                            //Only start new animation if old animation over
                            if (animationOver) {
                                //set animationOver to false to indicate click in progress
                                animationOver = false;

                                ObjectAnimator animationUp = ObjectAnimator.ofFloat(image, "translationY", -210f);
                                ObjectAnimator animationDown = ObjectAnimator.ofFloat(image, "translationY", +210f);
                                AnimatorSet set = new AnimatorSet();
                                set.playSequentially(animationUp, animationDown);
                                set.setDuration(220);
                                set.setInterpolator(new LinearInterpolator());
                                set.start();

                                //set animation listener to tell when animations over.
                                set.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        //animation over set to true
                                        animationOver = true;
                                    }
                                });

                            }
                        }
                    }

                }
                return true;
            }
        });

        // Pause button
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When this button is pressed. Create an alert dialog and pause what is below
                AlertDialog.Builder pauseDialog = new AlertDialog.Builder(v.getContext());
                pauseDialog.setMessage(R.string.quit_game)
                        // The positive button destroys current activity and sends the user back to the main menu
                        .setPositiveButton(R.string.quit, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        // Negative button just closes the dialog and returns to the game
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .create();

                // Make the alert dialog visible
                pauseDialog.show();
            }
        });
    }

    //Simple up down animation method for double jump
    private void doubleJump(ImageView image) {
        hasDoubleJumpHappened = true;
        int[] locationOnScreent = new int[2];
        image.getLocationOnScreen(locationOnScreent);

        ObjectAnimator animationUp = ObjectAnimator.ofFloat(image, "translationY", -500f);
        ObjectAnimator animationDown = ObjectAnimator.ofFloat(image, "translationY", +210f);
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(animationUp, animationDown);
        set.setDuration(220);
        set.setInterpolator(new LinearInterpolator());
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                hasDoubleJumpHappened = false;
            }
        });

    }

    // Async task class to do our background tasks
    private class BackGroundTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            // have the thread always running in the back and update every tick
            // causes leaks???
            boolean running = true;
            while(running) {
                update();
            }

            // always returns null
            return null;
        }
    }

    // method to update our updatable objects
    private void update(){
        for(Updatable u: updatables){
            u.update();
        }
    }

    // Getter for our screen width
    public static int getWidth(){
        int maxWidth = mDisplaySize.x;
        return maxWidth;
    }

    // Getter for our screen height
    public static int getHeight(){
        int maxHeight = mDisplaySize.y;
        return maxHeight;
    }
}