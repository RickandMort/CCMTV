package com.linlic.ccmtv.yx.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.text.style.ReplacementSpan;

/**
 * Created by yu on 2018/5/30.
 */
public class RoundBackgroundColorSpan extends ReplacementSpan {
    private int bgColor;
    private int textColor;
    public Context context;
    private int position = 1;
    private float fontSizePx ;     //px

    public RoundBackgroundColorSpan(Context context,int bgColor, int textColor,float fontSizePx,int position ) {
        super();
        this.bgColor = bgColor;
        this.fontSizePx = fontSizePx;
        this.textColor = textColor;
        this.context = context;
        this.position = position;
    }
    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        //设置宽度为文字宽度加16dp
        return ((int)paint.measureText(text, start, end)+ px2dip(16));
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        int originalColor = paint.getColor();
        paint.setColor(this.bgColor);
//        paint.setColor(Color.parseColor("#4492da"));
        LogUtil.e("背景数据","left:"+x +" top:"+top+" right:"+(x + ((int) paint.measureText(text, start, end)))+" bottom:"+bottom);
        //画圆角矩形背景
   /*     if(position == 0){
            canvas.drawRoundRect(new RectF(x,
                            top+5,
                            x + ((int) paint.measureText(text, start, end)),
                            bottom ),

                    px2dip(1000),
                    px2dip(1000 ),
                    paint);
        }else{*/
            canvas.drawRoundRect(new RectF(x,
                            top+px2dip(3),
                            x + ((int) paint.measureText(text, start, end)+px2dip(16)),
                            bottom-px2dip(1) ),

                    px2dip(1000),
                    px2dip(1000 ),
                    paint);
//        }

        paint.setColor(this.textColor);
        //画文字,
//        text = text.subSequence(start, end);
//        Paint p = getCustomTextPaint(paint);
//        Paint.FontMetricsInt fm = p.getFontMetricsInt();
        // 此处重新计算y坐标，使字体居中
//        canvas.drawText(text.toString(), x, y - ((y + fm.descent + y + fm.ascent) / 2 - (bottom + top) / 2), p);
     /*   if(position == 0){
            canvas.drawText(text, start, end, x, y+ px2dip(-12), paint);
        }else{
            canvas.drawText(text, start, end, x, y+ px2dip(-16), paint);
        }*/
        canvas.drawText(text, start, end, x+ px2dip(-20), y, paint);
        //将paint复原
        paint.setColor(originalColor);


    }

    /**
     * 将像素转换成dp
     * @param pxValue
     * @return
     */
    public   int px2dip( float pxValue){
        final float scale = context.getResources ().getDisplayMetrics ().density;
        return (int) (pxValue / scale + 0.5f);
    }

    private TextPaint getCustomTextPaint(Paint srcPaint) {
        TextPaint paint = new TextPaint(srcPaint);
        paint.setTextSize(fontSizePx);   //设定字体大小, sp转换为px
        return paint;
    }
}
