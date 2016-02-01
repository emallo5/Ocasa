package com.android.ocasa.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class FractionalLinearLayout extends LinearLayout {

    private float yFraction;
    private int screenHeight;
    private float heightFraction;

    public FractionalLinearLayout(Context context) {
        super(context);
    }

    public FractionalLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int w = resolveSizeAndState(0, widthMeasureSpec, 1);
        int h = resolveSizeAndState(0, heightMeasureSpec, 1);

        screenHeight = h;

        super.onMeasure(MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec((int) (h * heightFraction), MeasureSpec.EXACTLY));
    }

    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if(heightFraction != 1)
            setY(screenHeight);
    }

    public float getYFraction() {
        return yFraction;
    }

    public void setYFraction(float yFraction) {
        this.yFraction = yFraction;
        setY((getHeight() > 0) ? (screenHeight - yFraction * getHeight()) : 0);
    }

    public float getHeightFraction() {
        return heightFraction;
    }

    public void setHeightFraction(float heightFraction) {
        this.heightFraction = heightFraction;
    }
}
