package com.linlic.ccmtv.yx.kzbf.utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by bentley on 2018/11/8.
 */

public class OrdinaryDecoration extends RecyclerView.ItemDecoration {
    private int mDecorationHeight = 5;
    private Paint mPaint;

    public OrdinaryDecoration(int decorationHeight) {
        this.mDecorationHeight = decorationHeight;
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildLayoutPosition(view);
        if (position == 0) {
            outRect.top = 0;
        } else {
            outRect.top = mDecorationHeight;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            float top = view.getTop() - mDecorationHeight;
            float bottom = view.getTop();
            //绘制悬浮栏
            if (position != 0 && isDrawDecoration(position)) {
                //绘制悬浮栏
                mPaint.setColor(getDecorationColor(position));
                c.drawRect(left, top, right, bottom, mPaint);
//                c.drawLine(view.getLeft(), view.getTop(), view.getRight(), view.getTop(), mPaint);
            }
        }
    }

    public boolean isDrawDecoration(int positon) {
        return true;
    }

    public int getDecorationColor(int positon) {
        return Color.BLACK;
    }
}
