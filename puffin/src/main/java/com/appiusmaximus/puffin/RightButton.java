package com.appiusmaximus.puffin;

/**
 * Created by appi on 7/4/13.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;

public class RightButton extends MouseButton {

    public RightButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        bgGrad = new RadialGradient(0,0,1,0,0, Shader.TileMode.CLAMP);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        bgGrad = new RadialGradient(0,0,h, colors, pos, Shader.TileMode.CLAMP);
    }

    @Override
    protected void onDraw(Canvas canvasObject) {

        buttonPath.reset();

        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        buttonPath.moveTo(w,50.0f);
        buttonPath.quadTo(3 * w / 4, 0.0f, 4.0f, 0.0f);
        buttonPath.lineTo(4.0f, h);
        buttonPath.lineTo(w,h);
        buttonPath.close();
        buttonPath.setFillType(Path.FillType.EVEN_ODD);

        super.onDraw(canvasObject);
    }

}

