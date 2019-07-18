package com.linlic.ccmtv.yx.utils;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


/** 瀑布流布局
 * @author 张统强
 * @version 创建时间：2016-9-22 上午8:54:09
 *
 * 用法:xml中像添加普通View一样添加当前自定义控件.
 * 代码中使用如下:
 * private String[] tags = new String[]{"创屎记数据","创屎记数据有限公司","创屎记科技数据","创屎记",
 * "sdadsfa创屎记数据","创屎记科技数据","创屎记","创屎记是由张统强童鞋为了打造自己的品牌而设立的名字,由于能力有限目前还无法被众多人员知晓,深感无奈!",
 * "创屎记科技数据","创屎记"};
 *
 * oncreate方法中如下:
 * MyFlowLayout flow = (MyFlowLayout) findViewById(R.id.flowLayout);
for (int i = 0; i < tags.length; i++) {
TextView tv = new TextView(this);
tv.setText(tags[i]);
tv.setTextColor(Color.BLACK);
LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
params.setMargins(10, 10, 10,10);
tv.setLayoutParams(params);
tv.setMaxWidth(com.SCREEN_WIDTH - 20);//这句话是为了限制过长的内容顶出屏幕而设置的
tv.setPadding(10, 10, 10, 10);
tv.setBackgroundResource(R.drawable.rund_bg);
flow.addView(tv);
}
 *
 */
public class MyFlowLayout extends ViewGroup {
    private List<int[]> children;

    public MyFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        children = new ArrayList<int[]>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        final int count = getChildCount(); // tag的数量
        int left = 0; // 当前的左边距离
        int top = 0; // 当前的上边距离
        int beforeBottom = 0;//上一个控件的下边距.
        int totalHeight = 0; // WRAP_CONTENT时控件总高度
        int totalWidth = 0; // WRAP_CONTENT时控件总宽度
        boolean isHH = false;//是否换行

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) child.getLayoutParams();
//当前child的高度
            int curH = params.topMargin + child.getMeasuredHeight() + params.bottomMargin;
            if (i == 0) { // 第一行的高度
                totalHeight = curH;
            }
// 换行
            if (left + params.leftMargin + child.getMeasuredWidth() + params.rightMargin > getMeasuredWidth())
            {
                left = 0;
// 每个TextView的高度都一样，随便取一个都行
                isHH = true;
                top = beforeBottom;//将上一个控件的底部位置赋值当前控件的顶部
                totalHeight = top + curH;
            }
//取当前高度跟之前值中的最大值
            beforeBottom = Math.max(curH, beforeBottom)+((isHH)?curH:0);
            isHH = false;

            children.add(new int[]{
                    left + params.leftMargin,
                    top + params.topMargin,
                    left + params.leftMargin + child.getMeasuredWidth(),
                    top + params.topMargin + child.getMeasuredHeight()});

            left += params.leftMargin + child.getMeasuredWidth() + params.rightMargin;

            if (left > totalWidth) { // 当宽度为WRAP_CONTENT时，取宽度最大的一行
                totalWidth = left;
            }
        }

        int height = 0;
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            height = totalHeight;
        }

        int width = 0;
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        } else {
            width = totalWidth;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);

            int[] position = children.get(i);
            child.layout(position[0], position[1], position[2], position[3]);
        }
    }

}

