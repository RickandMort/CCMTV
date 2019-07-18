package com.linlic.ccmtv.yx.activity.medal.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by bentley on 2018/11/22.
 */

public class MedalBannerItemRatingbar extends View {

    private int ratingCount = 6;//总条目数
    private Paint paint;// 画笔
    private int ratingColor = Color.parseColor("#FDD869");// rating方块颜色
    private int unRatingColor = Color.WHITE;// unrating方块颜色

    private int mWidth;
    private int mHeight;

    private int defaultPadding = 20;
    private float ratingRatio = 0.9f;


    public MedalBannerItemRatingbar(Context context) {
        this(context, null);
    }

    public MedalBannerItemRatingbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MedalBannerItemRatingbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL); // 设置实心
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = getWidth();
        mHeight = getHeight();
        defaultPadding = mWidth / 20;
        paint.setStrokeWidth(mHeight);
        paint.setColor(ratingColor);
        float rating = (mWidth - (defaultPadding * (ratingCount - 1))) * 1.0f / ratingCount;//计算出色块的宽度
        float ratingWidth = mWidth * ratingRatio;
        int startY = mHeight / 2;//Y轴绘制的底边
        float currentNode = 0f;//rating条目记录当前绘制的节点
        while (ratingWidth > 0) {
//            if (ratingWidth > (rating + defaultPadding)) {
//            } else
            if (ratingWidth > 0) {
                canvas.drawLine(currentNode, startY, currentNode + rating, startY, paint);
                currentNode += (rating + defaultPadding);
                ratingWidth = ratingWidth - rating - defaultPadding;
            }
        }
        paint.setColor(unRatingColor);
        float unratingWidth = mWidth - currentNode;
        float unRatingNode = mWidth;
        while (unratingWidth > 0) {
            if (unratingWidth > rating) {
                canvas.drawLine(unRatingNode - rating, startY, unRatingNode, startY, paint);
                unRatingNode -= (rating + defaultPadding);
                unratingWidth = unratingWidth - rating - defaultPadding;
            } else {
                canvas.drawLine(currentNode, startY, unRatingNode, startY, paint);
                unratingWidth = unratingWidth - rating;
            }
        }
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
        invalidate();
    }

    public void setRatingRatio(int ratioCount) {
        this.ratingRatio = ratioCount * 1.0f / ratingCount;
        invalidate();
    }
}
