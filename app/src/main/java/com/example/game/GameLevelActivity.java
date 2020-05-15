package com.example.game;

import androidx.fragment.app.Fragment;

public class GameLevelActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new GameLevelFragment();
    }
}
