package com.linlic.ccmtv.yx.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * name：自定义TextView获取焦点
 * author：Larry
 * data：2016/8/23.
 */
public class FocuedTextView extends TextView {

    public FocuedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FocuedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocuedTextView(Context context) {
        super(context);
    }

    @Override
    public boolean isFocused() {
        return true;   //返回一个ture就可以获取焦点
    }
}