package com.example.game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

class GameLevelFragment extends Fragment {
    boolean animationOver = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.game_level_fragment, container, false);

        final ImageView image = v.findViewById(R.id.imageImageView);
        FrameLayout screenCoverLayout = (FrameLayout) v.findViewById(R.id.screenFrameLayout);

        screenCoverLayout.setOnTouchListener(new View.OnTouchListener() {
            private static final int MAX_CLICK_DURATION = 200;
            private long startClickTime;

            @Override
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
                            //Only start new animation if old animation over
                            if (animationOver) {
                                //set animationOver to false to indicate click in progress
                                animationOver = false;
                                ObjectAnimator animationUp = ObjectAnimator.ofFloat(image, "translationY", -150f);
                                ObjectAnimator animationDown = ObjectAnimator.ofFloat(image, "translationY", +150f);
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


        return v;
    }
}
