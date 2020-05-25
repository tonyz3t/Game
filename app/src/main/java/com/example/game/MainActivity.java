package com.example.game;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
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
    private SurfaceView screenCoverLayout;
    private SurfaceHolder holder;
    //box
    private ImageView mBoxImage;
    private Bitmap mBitmap;

    //Has double jump happened yet? Set to false by default
    boolean hasDoubleJumpHappened = false;
    //boolean to track if our surface has been created
    boolean mSurfaceCreated = false;
    // Our boxes object
    private Boxes mBoxes;
    // Window dimensions
    private Point mSize;


    // Reference to our background thread
    Thread mBackgroundThread;

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
        // Get Window Dimensions
        mSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(mSize);
        // Character Image
        image = findViewById(R.id.imageImageView);
        // Layout to detect screen touch
        screenCoverLayout = (SurfaceView) findViewById(R.id.screenFrameLayout);

        //Box init and image
        mBoxes = new Boxes(this);
        String imageUri = "drawable://" + R.drawable.simplecrate;
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.simplecrate);
        mBoxImage = (ImageView) findViewById(R.id.sprite_crate);
        mBoxImage.setImageBitmap(mBitmap);

        // Pause Button
        mPauseButton = (Button) findViewById(R.id.pause_button);

        //keep our screen on while user plays the game
        screenCoverLayout.setKeepScreenOn(true);
        //get our screen holder
        holder = screenCoverLayout.getHolder();

        addUpdatable(mBoxes);

        // Start our background task
        //startThread();

        screenCoverLayout.setZOrderOnTop(true);

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

        // handle holders callbacks
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                //set our surface created boolean to true and start our background thread
                mSurfaceCreated = true;
                startThread();
            }

            //this method can be ignored
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                //set surface boolean to false and stop our background thread
                mSurfaceCreated = false;
                stopThread();
            }
        });

        holder.setFormat(PixelFormat.TRANSLUCENT);


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

    // Stop thread once the activity is destroyed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //stopThread();
    }

    // Helper method to start our backgroundThread
    private void startThread() {
        //First clear out existing thread
        stopThread();
        // Create new thread and start
        mBackgroundThread = new BackgroundThread();
        mBackgroundThread.start();
    }

    // Helper method to stop our background thread
    private void stopThread() {
        if(mBackgroundThread != null){
            //stop our thread
            mBackgroundThread.interrupt();

            // join it back to the main thread
            try {
                mBackgroundThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //destroy background thread
            mBackgroundThread = null;
        }
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

    // method to update our updatable objects
    private void update(){
        for(Updatable u: updatables){
            u.update();
        }
    }

    // Custom background thread
    // We will handle our game loop within this class
    private class BackgroundThread extends Thread{
        @Override
        public void run() {
            super.run();

            // keep updating our objects while the thread is running
            while(!Thread.interrupted()){
                //handle game loop
                //get our canvas we will be drawing to
                Canvas canvas = null;

                try {
                    canvas = holder.lockCanvas();
                    update();
                }finally {
                    holder.unlockCanvasAndPost(canvas);
                }
                //update();
                // Handle game loop

            } // end while

        }
    }
}