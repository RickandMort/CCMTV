package com.linlic.ccmtv.yx.activity.medal.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.linlic.ccmtv.yx.R;


/**
 * Created by bentley on 2018/11/26.
 */

public class MedalDetialTitleImageView extends View {

    private int DEFAULT_MEDAL_IMG_WIDTH = 750;
    private int DEFAULT_MEDAL_IMG_HEIGHT = 191;
    private Paint mPaint;
    private Bitmap bgIcon;
    private float ratio;
    private Rect imgRect = new Rect();

    public MedalDetialTitleImageView(Context context) {
        this(context, null);
    }

    public MedalDetialTitleImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MedalDetialTitleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.MedalDetialTitleImageView);
        bgIcon = setCenterBitmap(a.getResourceId(R.styleable.MedalDetialTitleImageView_medal_src, R.mipmap.medal_detial_title_lower));
        DEFAULT_MEDAL_IMG_HEIGHT = a.getInt(R.styleable.MedalDetialTitleImageView_medal_default_height, DEFAULT_MEDAL_IMG_HEIGHT);
        a.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);// 消除锯齿
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureDimension(DEFAULT_MEDAL_IMG_WIDTH, widthMeasureSpec);//默认宽度屏幕的1/2.2
        int height = measureDimension((int) (DEFAULT_MEDAL_IMG_HEIGHT * (width / (DEFAULT_MEDAL_IMG_WIDTH * 1.0f))), heightMeasureSpec);
        ratio = width / (DEFAULT_MEDAL_IMG_WIDTH * 1.0f);
        //将计算的宽和高设置进去，保存，最后一步一定要有
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bgIcon == null) return;
        float bw = DEFAULT_MEDAL_IMG_WIDTH * ratio;
        float bh = DEFAULT_MEDAL_IMG_HEIGHT * ratio;

        imgRect.left = getPaddingLeft();
        imgRect.top = getPaddingTop();
        imgRect.bottom = (int) (bh + getPaddingTop());
        imgRect.right = (int) (bw + getPaddingLeft());
        canvas.drawBitmap(bgIcon, null, imgRect, mPaint);
//        // 绘图
//        Matrix mCurrentMatrix = new Matrix();
//        mCurrentMatrix.postScale(bw / bgIcon.getWidth(), bh / bgIcon.getHeight());
//        mCurrentMatrix.postTranslate(getWidth() / 2 - bw / 2,
//                (getHeight() - bh / 2));
//        canvas.drawBitmap(bgIcon, mCurrentMatrix, mPaint);
    }


    public Bitmap setCenterBitmap(int resId) {
        bgIcon = BitmapFactory.decodeResource(getResources(), resId);
        return bgIcon;
    }

    public void setImageResource(int resId) {
        bgIcon = BitmapFactory.decodeResource(getResources(), resId);
        invalidate();
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
}
