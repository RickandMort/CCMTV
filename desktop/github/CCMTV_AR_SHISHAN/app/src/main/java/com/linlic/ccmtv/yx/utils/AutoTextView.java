package com.linlic.ccmtv.yx.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/4/12.
 */

public class AutoTextView extends TextView {

    private int tagTextColor = 0xFFFF0000;
    private String tag = "...[更多]";
    private String tag2 = "   [收起]";
    private boolean isexpanddescripe = false;
    private String contentStr = "";



    public void setText2(String str){
        contentStr = str ;
        onPreDraw();
    }

    public AutoTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(this);
        setText(getText().toString());
    }
    public AutoTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public AutoTextView(Context context) {
        this(context, null);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
    @Override
    public boolean onPreDraw() {
        if (!isexpanddescripe) {
            replaceText();
        }else{
            replaceText2();
        }
        return super.onPreDraw();
    }
    public void replaceText() {
        int count=getLineCount();
        if(count>3){
            int st=getLayout().getLineEnd(2);
            String content = getText().toString().substring(0,st);
            Paint paint = new Paint();
            paint.setTextSize(getTextSize());
            float pointWidth = paint.measureText(tag);
            char[] textCharArray = content.toCharArray();
            float drawedWidth = 0;
            float charWidth;
            for (int i = textCharArray.length-1; i >0 ; i--) {
                charWidth = paint.measureText(textCharArray, i, 1);
                if (drawedWidth < pointWidth ) {
                    drawedWidth += charWidth;
                } else {
                    content=content.substring(0,i)+tag;
                    break;
                }
            }

            setColor(content, content.length()-4, content.length(), tagTextColor);
            Spannable spannable = Spannable.Factory.getInstance().newSpannable(content);
            spannable.setSpan(new ClickableSpan() {
                                  @Override
                                  public void onClick(View widget) {

                                      if (isexpanddescripe) {
                                          isexpanddescripe = false;
                                           setMaxLines(3);// 收起
                                      } else {
                                          isexpanddescripe = true;
                                          setMaxLines(1000);// 展开
                                          setText(contentStr+tag2);
                                      }

                                  }
                              },
                     content.length()-4, content.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            setMovementMethod(LinkMovementMethod.getInstance());
            setText(spannable);
        }
    }

    public void replaceText2() {
        int count=getLineCount();
        if(count>3){
            setColor(getText().toString(), getText().length()-4, getText().length(), tagTextColor);
            Spannable spannable = Spannable.Factory.getInstance().newSpannable(getText());
            spannable.setSpan(new ClickableSpan() {
                                  @Override
                                  public void onClick(View widget) {

                                      if (isexpanddescripe) {
                                          isexpanddescripe = false;
                                          setMaxLines(3);// 收起
                                      } else {
                                          isexpanddescripe = true;
                                          setMaxLines(1000);// 展开
                                          setText(contentStr);
                                      }

                                  }
                              },
                    getText().length()-4, getText().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            setMovementMethod(LinkMovementMethod.getInstance());
            setText(spannable);
        }
    }
    private void setColor(String content, int start, int end, int textColor) {
        if (start <= end) {
            SpannableStringBuilder style = new SpannableStringBuilder(content);
            style.setSpan(new ForegroundColorSpan(textColor), start, end,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            setText(style);
        }
    }
}