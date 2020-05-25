package com.example.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.ImageView;

public interface Renderable {
    public void render(Canvas canvas, Paint globalPaint, float interpolation);
}
