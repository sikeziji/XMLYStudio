package com.example.xmlystudio.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;

import android.graphics.Path;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;



public class RoundRectImageView extends AppCompatImageView {

    private float roundRatio = 0.3f;

    private Path path;

    public RoundRectImageView(Context context) {
        super(context);
    }

    public RoundRectImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundRectImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (path == null) {
            path = new Path();
            path.addRoundRect(new RectF(0, 0, getWidth(), getHeight()), roundRatio * getWidth(), roundRatio * getHeight(), Path.Direction.CW);
        }
        canvas.save();
        canvas.clipPath(path);
        super.onDraw(canvas);
        canvas.restore();
    }
}
