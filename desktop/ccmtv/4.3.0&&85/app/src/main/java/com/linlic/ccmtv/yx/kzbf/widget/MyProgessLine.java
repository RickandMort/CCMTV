package com.linlic.ccmtv.yx.kzbf.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.linlic.ccmtv.yx.R;


/**
 * 自定义进度
 *
 * @author Rock Lee
 * @date 2016年4月18日
 */
public class MyProgessLine extends View {

    //需要执行动画的参数名
    private static final String PROGRESS_PROPERTY = "progress";

    private Paint paint;// 画笔
    private float currentProgress = 0f;
    private int viewWidth;
    RectF rectF;

    private Paint textPaint;// 文字画笔
    private String leftDesc = "";
    private String rightDesc = "";
    private int defaultPadding = 10;
    Rect textBound;


    public void setLeftAndRightDesc(String leftDesc, String rightDesc) {
        this.leftDesc = leftDesc;
        this.rightDesc = rightDesc;
        invalidate();
    }

    private int bmColor;// 底部横线颜色
    private float bmHight;// 底部横线高度
    private int color;// 进度条颜色
    private float hight;// 进度条高度

    protected float progress;
    private int descColor;
    private float mTextSize;

    public void setColor(int color) {
        this.color = color;
    }

    public MyProgessLine(Context context) {
        this(context, null);
    }

    public MyProgessLine(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyProgessLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
        paint = new Paint();
        textPaint = new Paint();
        rectF = new RectF();
        textBound = new Rect();

        TypedArray mTypedArray = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.MyProgressLine, defStyleAttr, 0);

        bmColor = mTypedArray.getColor(R.styleable.MyProgressLine_bmColor,
                Color.GRAY);
        bmHight = mTypedArray
                .getDimension(R.styleable.MyProgressLine_bmHight, 2);
        color = mTypedArray.getColor(R.styleable.MyProgressLine_lineColor,
                Color.BLUE);
        hight = mTypedArray.getDimension(R.styleable.MyProgressLine_linehight, 2);
        descColor = mTypedArray.getColor(R.styleable.MyProgressLine_descColor, Color.parseColor("#676767"));
        mTypedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        textPaint.setColor(descColor);
        textPaint.setTextSize(25);
        textPaint.setStyle(Paint.Style.FILL); // 设置实心
        textPaint.setAntiAlias(true); // 消除锯齿
        textPaint.getTextBounds(leftDesc, 0, leftDesc.length(), textBound);
        canvas.drawText(leftDesc, 0, getHeight() / 2 + textBound.height() / 2, textPaint);
        int start = textBound.width() + defaultPadding;
        textPaint.getTextBounds(rightDesc, 0, rightDesc.length(), textBound);
        canvas.drawText(rightDesc, getWidth() - textBound.width(), getHeight() / 2 + textBound.height() / 2, textPaint);
        int end = getWidth() - textBound.width() - defaultPadding;


        paint.setColor(bmColor);
        paint.setStyle(Paint.Style.FILL); // 设置实心
        paint.setStrokeWidth(bmHight); // 设置笔画的宽度
        paint.setAntiAlias(true); // 消除锯齿
        rectF.set(start, 0, end, bmHight);
        canvas.drawRoundRect(rectF, bmHight / 2, bmHight / 2, paint);

        paint.setColor(color);
        paint.setStrokeWidth(hight); // 设置笔画的宽度
        rectF.set(start, 0, start + progress * (end - start), bmHight);
        canvas.drawRoundRect(rectF, hight / 2, hight / 2, paint);

    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        if (progress > 100) progress = 100;
        if (progress < 0) progress = 0;
        this.progress = progress / 100;
        invalidate();
    }

    public void setFloatProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    public void setLeftDesc(String leftDesc) {
        if (leftDesc == null) return;
        this.leftDesc = leftDesc;
        invalidate();
    }


    public void setRightDesc(String rightDesc) {
        this.rightDesc = rightDesc;
        invalidate();
    }

    /**
     * 赋值+执行动画
     *
     * @param progressText 进度 float
     */
    public void dodo(float progressText) {
        ObjectAnimator progressAnimation;
        AnimatorSet animation = new AnimatorSet();
        progressAnimation = ObjectAnimator.ofFloat(this, PROGRESS_PROPERTY,
                currentProgress, progressText);
        currentProgress = progressText;
        progressAnimation.setDuration((int) progressText * 10);//动画耗时
        progressAnimation.setInterpolator(new AccelerateDecelerateInterpolator());

        animation.playTogether(progressAnimation);
        animation.start();
    }

    /**
     * Re size the font so the specified text fits in the text box assuming the
     * text box is the specified width.
     *
     * @param text
     * @param textWidth
     */
//    private void refitText(String text, int textViewWidth) {
//        if (text == null || textViewWidth <= 0)
//            return;
//        int availableTextViewWidth = getWidth() - getPaddingLeft() - getPaddingRight();
//        float[] charsWidthArr = new float[text.length()];
//        Rect boundsRect = new Rect();
//        paint.getTextBounds(text, 0, text.length(), boundsRect);
//        int textWidth = boundsRect.width();
//        mTextSize = getTextSize();
//        while (textWidth > availableTextViewWidth) {
//            mTextSize -= 1;
//            mTextPaint.setTextSize(mTextSize);
//            textWidth = mTextPaint.getTextWidths(text, charsWidthArr);
//        }
//    }

}