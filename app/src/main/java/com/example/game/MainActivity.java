package com.example.game;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity{
    // OUR MEMBER VARIABLES
    // Our Input

    boolean animationOver = true;
    private Button mPauseButton;
    //private PauseDialogFragment mPauseDialog;
    private ImageView image;
    private SurfaceView screenCoverLayout;
    private SurfaceHolder holder;
    // Horizontal gap for our boxes
    private double mBoxHorizontalGap = 400;

    //box
    private ImageView mBoxImage;
    private ImageView mBoxImage2;
    private ImageView mBoxImage3;
    private Bitmap mBitmap;
    private Box mBoxOne;
    private Box mBoxTwo;
    private Box mBoxThree;

    // New character object and bitmap
    private Sprite mCharSprite;
    private Bitmap mBitmapChar;
    private ImageView mCharImageView;
    private int mCharXPosition;
    private int mCharYPosition;

    // List of imageviews for easier manipulation
    private ArrayList<ImageView> mListBoxImages = new ArrayList<>();

    //Has double jump happened yet? Set to false by default
    boolean hasDoubleJumpHappened = false;
    //boolean to track if our surface has been created
    boolean mSurfaceCreated = false;
    // Window dimensions
    private Point mSize;


    // Reference to our background thread
    private BackgroundThread mBackgroundThread;

    // List of our updatable objects
    private ArrayList<Updatable> updatables = new ArrayList<>();

    // Helper method to add our boxes to a list of updatables;
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
        mBitmap = PictureUtils.getScaledBitmap(getResources(), R.drawable.simplecrate, this);
        mBoxImage = (ImageView) findViewById(R.id.sprite_crate);
        mBoxImage.setImageBitmap(mBitmap);
        mBoxImage2 = (ImageView) findViewById(R.id.sprite_crate2);
        mBoxImage2.setImageBitmap(mBitmap);
        mBoxImage3 = (ImageView) findViewById(R.id.sprite_crate3);
        mBoxImage3.setImageBitmap(mBitmap);

        // Box objects
        mBoxOne = new Box(this, mSize.x);
        mBoxTwo = new Box(this, mSize.x + 400);
        mBoxThree = new Box(this, mSize.x + 800);

        // Add boxes to our list of updatables
        addUpdatable(mBoxOne);
        addUpdatable(mBoxTwo);
        addUpdatable(mBoxThree);
        //addUpdatable(mBoxes);

        // New Character Object and bitmap
        mCharSprite = new Sprite(this);
        addUpdatable(mCharSprite);
        mBitmapChar = PictureUtils.getScaledBitmap(getResources(), R.drawable.charsprite, this);
        mCharImageView = (ImageView) findViewById(R.id.char_image_view);
        image.setImageBitmap(mBitmapChar);
        image.setX(100.0f);
        image.setY((float)getResources().getDisplayMetrics().heightPixels/2);
        //mCharImageView.setImageBitmap(mBitmapChar);
        mCharImageView.setY(mCharSprite.getY());
        mCharXPosition = 100;
        mCharYPosition = mSize.y/2;

        // Pause Button
        mPauseButton = (Button) findViewById(R.id.pause_button);

        //keep our screen on while user plays the game
        screenCoverLayout.setKeepScreenOn(true);
        //get our screen holder
        holder = screenCoverLayout.getHolder();

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
                        if(mCharSprite.isReleased()){
                            mCharSprite.setIsTouched(true);
                            mCharSprite.setIsReleased(false);
                        }
                        break;
                    }
                    //Lifting finger off screen
                    case MotionEvent.ACTION_UP: {
                        mCharSprite.setIsReleased(true);
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
                startThread(holder);
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

    //Simple up down animation method for double jump
    private void doubleJump(ImageView image) {
        hasDoubleJumpHappened = true;
        int[] locationOnScreent = new int[2];
        image.getLocationOnScreen(locationOnScreent);

        ObjectAnimator animationUp = ObjectAnimator.ofFloat(image, "translationY", -210f);
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

    private void updateAndDrawBox(){
        update();

        int x1 = mBoxOne.getX();

        mBoxImage.setX((float)mBoxOne.getX());
        mBoxImage.setY((float)mBoxOne.getY());
        mBoxImage2.setX((float)mBoxTwo.getX());
        mBoxImage2.setY((float)mBoxTwo.getY());
        mBoxImage3.setX((float)mBoxThree.getX());
        mBoxImage3.setY((float)mBoxThree.getY());
       // mCharImageView.setY((float) mCharSprite.getY());
    }

    // Helper method to start our backgroundThread
    private void startThread(SurfaceHolder holder) {
        if(mBackgroundThread == null) {
            // Create new thread and start
            mBackgroundThread = new BackgroundThread(holder);
            mBackgroundThread.start();
        } else {
            mBackgroundThread.start();
        }
    }

    // Helper method to stop our background thread
    private void stopThread() {
        if(mBackgroundThread.isRunning()){
            //stop our thread
            mBackgroundThread.setIsRunning(false);

            boolean retry = true;
            while(retry) {

                // join it back to the main thread
                try {
                    mBackgroundThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //destroy background thread
            mBackgroundThread = null;
        }
    }

    // Custom background thread
    // We will handle our game loop within this class
    private class BackgroundThread extends Thread{

        // Game loop start time and loop time
        private long mStartTime, mLoopTime;
        // Game loop delay between screen refresh in milliseconds
        private static final long DELAY = 33;
        private SurfaceHolder mHolder;

        private boolean mIsRunning;

        public BackgroundThread(SurfaceHolder holder){
            mHolder = holder;
            mIsRunning = true;
        }

        @Override
        public void run() {

            // keep updating our objects while the thread is running
            while(mIsRunning){
                // start tracking time
                mStartTime = SystemClock.uptimeMillis();
                //handle game loop
                //get our canvas we will be drawing to
                Canvas canvas = mHolder.lockCanvas(null);
                if(canvas != null) {
                    synchronized (holder) {
                        updateAndDrawBox();
                        mHolder.unlockCanvasAndPost(canvas);
                    }
                }

                // Loop time
                mLoopTime = SystemClock.uptimeMillis() - mStartTime;
                // Pausing here to make sure we update the right amount per second
                if (mLoopTime < DELAY) {
                    try {
                        Thread.sleep(DELAY - mLoopTime);
                    } catch (InterruptedException e) {
                        Log.e("Interrupted", "Interrupted while sleeping");
                    }
                }

                //update();
                // Handle game loop

            } // end while

        }

        public boolean isRunning(){
            return mIsRunning;
        }

        public void setIsRunning(boolean status){
            mIsRunning = status;
        }
    }

    // TODO: Collision detection
    // TODO: score count

}