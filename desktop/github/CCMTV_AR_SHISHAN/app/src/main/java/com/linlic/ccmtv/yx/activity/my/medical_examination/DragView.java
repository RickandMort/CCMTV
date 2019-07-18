package com.linlic.ccmtv.yx.activity.my.medical_examination;

/**
 * Created by Administrator on 2019/6/27.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.linlic.ccmtv.yx.utils.LogUtil;

public class DragView extends LinearLayout {

    private float moveX;
    private float moveY;

    public DragView(Context context) {
        super(context);
    }

    public DragView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                moveX = event.getX();
                moveY = event.getY();
                LogUtil.e("moveXY","moveX:"+moveX+"   moveY:"+moveY);
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtil.e("moveXY2","moveX:"+getX()+"   moveY:"+getY() );
                LogUtil.e("moveXY3","moveX:"+event.getX() +"   moveY:"+event.getX()  );
                setTranslationX(getX() + (event.getX() - moveX));
                setTranslationY(getY() + (event.getY() - moveY));


                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }

        return true;
    }
}