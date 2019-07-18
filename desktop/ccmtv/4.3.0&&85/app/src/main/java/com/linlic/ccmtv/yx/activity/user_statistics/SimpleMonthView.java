package com.linlic.ccmtv.yx.activity.user_statistics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;

/**
 * 高仿魅族日历布局
 * Created by huanghaibin on 2017/11/15.
 */

public class SimpleMonthView extends MonthView {

    private int mRadius;
    private Paint otherMonthSchemeBackgorundPaint = new Paint();
    private Paint notSchemeBackgorundPaint = new Paint();

    public SimpleMonthView(Context context) {
        super(context);
    }

    /*private void initPaint() {
        schemeTextPaint.setAntiAlias(true);
        schemeTextPaint.setStyle(Paint.Style.FILL);
        schemeTextPaint.setTextAlign(Paint.Align.CENTER);
        schemeTextPaint.setColor(0xffffffff);
        schemeTextPaint.setFakeBoldText(true);
        schemeTextPaint.setTextSize(28);

        schemeBackgorundPaint.setAntiAlias(true);
        schemeBackgorundPaint.setStyle(Paint.Style.FILL);
        schemeBackgorundPaint.setStrokeWidth(2);
        schemeBackgorundPaint.setColor(0xffefefef);
    }*/

    @Override
    protected void onPreviewHook() {
        mRadius = Math.min(mItemWidth, mItemHeight) / 5 * 2;
        mSchemePaint.setStyle(Paint.Style.FILL);
        //mSchemePaint.setColor(0xFF3698F9);

        otherMonthSchemeBackgorundPaint.setAntiAlias(true);
        otherMonthSchemeBackgorundPaint.setStyle(Paint.Style.FILL);
        otherMonthSchemeBackgorundPaint.setStrokeWidth(2);
        otherMonthSchemeBackgorundPaint.setColor(0xFFB2D4F7);

        mSelectedPaint.setColor(0x00000000);
    }

    @Override
    protected void onLoopStart(int x, int y) {

    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
//        canvas.drawCircle(cx, cy, mRadius, calendar.isCurrentDay() ? mSchemePaint : mSelectedPaint);
        canvas.drawCircle(cx, cy, mRadius, hasScheme ? mSchemePaint : mSelectedPaint);
        return false;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
//        canvas.drawCircle(cx, cy, mRadius, mSchemePaint);
        canvas.drawCircle(cx, cy, mRadius,
                calendar.isCurrentDay() ? mSchemePaint :
                calendar.isCurrentMonth() ? mSchemePaint : otherMonthSchemeBackgorundPaint);
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        float baselineY = mTextBaseLine + y;
        int cx = x + mItemWidth / 2;

        /*if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    mSelectTextPaint);
        }else */if (hasScheme) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                            calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);

        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY,
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
        }
    }
}
