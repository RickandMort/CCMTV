package com.linlic.ccmtv.yx.activity.medal.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CircleBarView extends View {

    private Paint progressPaint;//绘制圆弧的画笔
    private RectF rectF;

    private int progresssWidth = 30;
    private float progresss = 0.0f;

    public CircleBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        progressPaint = new Paint();
        progressPaint.setStyle(Paint.Style.STROKE);//只描边，不填充
        progressPaint.setStrokeWidth(progresssWidth);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setAntiAlias(true);//设置抗锯齿
    }

    public void setProgress(float progresss) {
        this.progresss = progresss;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (progresss >= 1.0f) return;
        if (rectF == null)
            rectF = new RectF(getPaddingLeft() + progresssWidth, getPaddingTop() + progresssWidth,
                    getWidth() - progresssWidth - getPaddingRight(), getHeight() - progresssWidth - getPaddingBottom());
        progressPaint.setColor(Color.LTGRAY);
        canvas.drawArc(rectF, -270, 360, false, progressPaint);
        progressPaint.setColor(Color.BLUE);
        canvas.drawArc(rectF, -270, 360 * progresss, false, progressPaint);
    }
}