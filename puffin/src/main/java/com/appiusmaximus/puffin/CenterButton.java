package com.appiusmaximus.puffin;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by appi on 7/9/13.
 */
public class CenterButton extends View{
    int BG_NORMAL;
    int BG_PRESSED;

    boolean PRESSED;

    Paint buttonPaint;

    public CenterButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        buttonPaint = new Paint();
        buttonPaint.setStyle(Paint.Style.FILL);
        buttonPaint.setAntiAlias(true);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CenterButton, 0, 0);
        // Try and obtain values
        try {
            BG_NORMAL = a.getColor(R.styleable.CenterButton_bg, Color.BLUE);
            BG_PRESSED = a.getColor(R.styleable.CenterButton_st,Color.GREEN);
        } finally {
            a.recycle();
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //Get canvas width and height
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);

        w = Math.min(w, h);
        h = w;

        setMeasuredDimension(w, h);
    }

    @Override
    protected void onDraw(Canvas canvasObject) {
        int h = getMeasuredHeight();

        int r = h/2;
        if (PRESSED) {
            buttonPaint.setColor(BG_PRESSED);
        } else {
            buttonPaint.setColor(BG_NORMAL);

        }
        buttonPaint.setAlpha(170);
        canvasObject.drawCircle(r, r, r, buttonPaint);

        buttonPaint.setAlpha(255);
        canvasObject.drawCircle(r,r,r-13.0f,buttonPaint);

        super.onDraw(canvasObject);
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
