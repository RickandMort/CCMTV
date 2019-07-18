package com.linlic.ccmtv.yx.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * name：自定义启动页跳过按钮
 * author：Larry
 * data：2017/5/31.
 */
public class SplashJumpView extends View {
    private int mSplashDuration;
    private int mCircleRadius = 60;
    private int mRealWidth;
    Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint numberPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint secondPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public SplashJumpView(Context context) {
        super(context);
    }
    public SplashJumpView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        circlePaint.setColor(Color.GRAY);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(35);
        textPaint.setTextAlign(Paint.Align.CENTER);
        numberPaint.setColor(Color.WHITE);
        numberPaint.setTextSize(32);
        numberPaint.setTextAlign(Paint.Align.CENTER);
        secondPaint.setColor(Color.WHITE);
        secondPaint.setTextSize(30);
        secondPaint.setTextAlign(Paint.Align.CENTER);
    }

    public SplashJumpView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int expectSize = mCircleRadius * 2 + getPaddingLeft() + getPaddingRight();
        int width = resolveSize(expectSize, widthMeasureSpec);
        int height = resolveSize(expectSize, heightMeasureSpec);
        //取宽和高中较小的一个值作为View的尺寸
        mRealWidth = Math.min(width, height);
        setMeasuredDimension(mRealWidth, mRealWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mRealWidth / 2, mRealWidth / 2, mCircleRadius, circlePaint);
        String tvContent = "跳过";
        canvas.drawText(tvContent, mRealWidth / 2, mRealWidth / 2, textPaint);
        float width = numberPaint.measureText(mSplashDuration + "");
        canvas.save();                          //锁画布(为了保存之前的画布状态)
        canvas.translate(-width, 40);           //把当前画布的原点移到(10,10),后面的操作都以(10,10)作为参照点，默认原点为(0,0)
        canvas.drawText(mSplashDuration + "", mRealWidth / 2, mRealWidth / 2, numberPaint);
        canvas.restore();                       //把当前画布返回（调整）到上一个save()状态之前
        canvas.translate(0, 40);                //把当前画布的原点移到(0, 40),后面的操作都以(0, 40)作为参照点，
        canvas.drawText("S", mRealWidth / 2, mRealWidth / 2, secondPaint);
    }

    public void setText(int splashDuration) {
        mSplashDuration = splashDuration;
        invalidate();
    }
}
