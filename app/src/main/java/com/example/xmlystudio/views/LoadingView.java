package com.example.xmlystudio.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.xmlystudio.R;

public class LoadingView extends AppCompatImageView {

    //旋转角度
    private int rotateDegree = 0;


    private boolean mNeedRotate = false ;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //设置图标
        setImageResource(R.mipmap.loading);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //绑定到 window的 时候
        mNeedRotate =true;
        post(new Runnable() {
            @Override
            public void run() {
                rotateDegree += 30;
                rotateDegree = rotateDegree <= 360 ? rotateDegree : 0;
                invalidate();
                //是否继续旋转
                if (mNeedRotate){
                    postDelayed(this, 100);
                }
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //从window中解绑了
        mNeedRotate = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        /**
         * 第一个参数是旋转角度
         * 第二个参数是旋转的 x坐标
         * 第三个参数是旋转的已做表
         */
        canvas.rotate(rotateDegree ,getWidth() / 2, getHeight() / 2);

        super.onDraw(canvas);
    }
}
