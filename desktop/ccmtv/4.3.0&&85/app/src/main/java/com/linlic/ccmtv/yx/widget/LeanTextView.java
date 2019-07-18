package com.linlic.ccmtv.yx.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;

/**
 * Created by Administrator on 2019/6/25.
 */

public class LeanTextView extends TextView {
    private int mDegrees;

    public LeanTextView(Context context) {
        super(context, null);
    }

    public LeanTextView(Context context, AttributeSet attrs) {
        super(context, attrs, android.R.attr.textViewStyle);
        this.setGravity(Gravity.CENTER);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LeanTextView);
        mDegrees = a.getDimensionPixelSize(R.styleable.LeanTextView_degree, 0);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getMeasuredWidth() > getMeasuredHeight()){
            setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
        }else{
            setMeasuredDimension(getMeasuredHeight(), getMeasuredHeight());
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(getCompoundPaddingLeft(), getExtendedPaddingTop());
        if (this.getWidth() > this.getHeight()){
            canvas.rotate(mDegrees, this.getWidth() / 2f, this.getWidth() / 2f);
        }else{
            canvas.rotate(mDegrees, this.getHeight() / 2f, this.getHeight() / 2f);
        }

        super.onDraw(canvas);
        canvas.restore();
    }

    public int getDegrees() {
        return mDegrees;
    }

    public void setDegrees(int mDegrees) {
        this.mDegrees = mDegrees;
        invalidate();
    }
}