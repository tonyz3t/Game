package com.example.game;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        //Fragment Manager
        FragmentManager fm = getSupportFragmentManager();

        //fragments
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        // if the fragment is null, create a new fragment and add it to the fragment manager
        // this code creates and commits a fragment transaction
        // fragment transactions are used to add, remove, attach, detach, or replace fragments in the fragment list
        if(fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }
}
