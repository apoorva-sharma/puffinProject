package com.appiusmaximus.puffin;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by appi on 7/9/13.
 */
public class MouseButton extends View {
    int BG_NORMAL;
    int ST_NORMAL;

    int w, h;

    int[] colors;
    float[] pos;

    RadialGradient bgGrad;

    boolean PRESSED;

    Paint buttonPaint;

    Path buttonPath;

    public MouseButton(Context context, AttributeSet attrs)
    {
        super(context,attrs);
        buttonPaint = new Paint();
        buttonPath = new Path();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MouseButton, 0, 0);
        // Try and obtain values
        try {
            BG_NORMAL = a.getColor(R.styleable.MouseButton_button_background, 0);
            ST_NORMAL = a.getColor(R.styleable.MouseButton_button_stroke,0);
        } finally {
            a.recycle();
        }

        colors = new int[]{BG_NORMAL, BG_NORMAL, 0};
        pos = new float[]{0.0f,0.2f,1.5f};

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //Get canvas width and height
        w = MeasureSpec.getSize(widthMeasureSpec);
        h = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(w,h);
    }

    @Override
    protected void onDraw(Canvas canvasObject) {
        super.onDraw(canvasObject);

        buttonPaint.setStyle(Paint.Style.FILL);
        buttonPaint.setShader(bgGrad);
        if (PRESSED) {
            buttonPaint.setAlpha(200);
        } else {
            buttonPaint.setAlpha(255);
        }
        canvasObject.drawPath(buttonPath,buttonPaint);

        buttonPaint.setStyle(Paint.Style.STROKE);
        buttonPaint.setAntiAlias(true);
        buttonPaint.setStrokeWidth(3.0f);
        buttonPaint.setColor(ST_NORMAL);
        if (PRESSED) {
            buttonPaint.setAlpha(200);
        } else {
            buttonPaint.setAlpha(255);
        }
        canvasObject.drawPath(buttonPath,buttonPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = false;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            PRESSED = true;
            result = true;
            invalidate();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            PRESSED = false;
            result = true;
            invalidate();
        }
        return result;
    }
}
