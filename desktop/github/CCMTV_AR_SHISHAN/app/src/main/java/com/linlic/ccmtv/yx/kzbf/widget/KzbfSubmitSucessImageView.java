package com.linlic.ccmtv.yx.kzbf.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.linlic.ccmtv.yx.R;


public class KzbfSubmitSucessImageView extends View {
    private static final int DEFUALT_VIEW_WIDTH = 750;
    private static final int DEFUALT_VIEW_HEIGHT = 456;
    /**
     * 绘制图片的矩形
     */
    protected Rect rect = new Rect();
    /**
     * 圆环中心的图片
     */
    protected Bitmap mCenterIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.kzbf_pop_submit_sucess);
    /**
     * 底部文字的画笔
     */
    private Rect textBround;// 文本的绘制范围
    private int textline;//绘制文字的基准线
    protected Paint mPaint;
    protected Paint mTextPaint;
    private int mWidth;
    private int mHeight;

    private String content = "+5积分";

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureDimension(DEFUALT_VIEW_WIDTH, widthMeasureSpec);
        int height = measureDimension((int) (DEFUALT_VIEW_HEIGHT * (width / 750f)), heightMeasureSpec);
        //将计算的宽和高设置进去，保存，最后一步一定要有
        setMeasuredDimension(width, height);
    }

    public KzbfSubmitSucessImageView(Context context, AttributeSet attrs,
                                     int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        textBround = new Rect();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);// 消除锯齿
        mPaint.setStrokeCap(Paint.Cap.ROUND);// 设置
        mPaint.setStyle(Paint.Style.STROKE);// 设置空心
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.parseColor("#FFE470"));
        mTextPaint.setFakeBoldText(true);

    }

    public KzbfSubmitSucessImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KzbfSubmitSucessImageView(Context context) {
        this(context, null);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        DrawCenterIcon(canvas);
    }

    /**
     * 绘制背景的图片
     *
     * @param canvas
     */
    private void DrawCenterIcon(Canvas canvas) {
        mWidth = getWidth();
        float v = getWidth() / (DEFUALT_VIEW_WIDTH * 1.0f);//屏幕与图片本身宽度比
        mHeight = (int) (DEFUALT_VIEW_HEIGHT * v);//图片本身的高度*宽度比
        textline = (int) (mHeight - (88 * v));//文字绘制的基准线
        rect.left = 0;
        rect.top = 0;
        rect.bottom = mHeight;
        rect.right = mWidth;
        // 绘图
        canvas.drawBitmap(mCenterIcon, null, rect, mPaint);
        mTextPaint.setTextSize(60 * v);
        mTextPaint.getTextBounds(content, 0, content.length(), textBround);
        float xText = mWidth / 2 - textBround.width() / 2;
        float yText = textline + textBround.height() / 2;
        canvas.drawText(content, xText, yText, mTextPaint);
    }

    /**
     * @param defualtSize 设置的默认大小
     * @param measureSpec 父控件传来的widthMeasureSpec，heightMeasureSpec
     * @return 结果
     */
    public int measureDimension(int defualtSize, int measureSpec) {
        int result = defualtSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        //1,layout中自定义组件给出来确定的值，比如100dp
        //2,layout中自定义组件使用的是match_parent，但父控件的size已经可以确定了，比如设置的具体的值或者match_parent
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        //layout中自定义组件使用的wrap_content
        else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(defualtSize, specSize);//建议：result不能大于specSize
        }
        //UNSPECIFIED,没有任何限制，所以可以设置任何大小
        else {
            result = defualtSize;
        }
        return result;
    }

    /**
     * 设置提示的文字
     *
     * @param sign
     */
    public void setSignText(String sign) {
        content = sign == null ? "" : sign;
    }
}