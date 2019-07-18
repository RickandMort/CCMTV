package com.linlic.ccmtv.yx.kzbf.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by bentley on 2018/10/30.
 */

public class CustomTitleBar extends LinearLayout {
    private String[] datas = new String[]{"资讯中心", "指南文献", "积分商城"};
    private SparseArray<TextView> views = new SparseArray<>();
    private int clickTextColor = Color.parseColor("#3997F9");
    private int noClickTextColor = Color.parseColor("#000000");
    private Context mContext;

    public CustomTitleBar(Context context) {
        this(context, null);
    }

    public CustomTitleBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        setOrientation(HORIZONTAL);
        setWeightSum(3);
        initView();
    }

    private void initView() {
        LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
        for (int i = 0; i < datas.length; i++) {
            TextView textView = new TextView(mContext);
            textView.setTag(i);
            textView.setTextSize(17);
            textView.setGravity(Gravity.CENTER);
            textView.setText(datas[i]);
//            textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            textView.setTextColor(Color.BLACK);
            textView.setOnClickListener(onClickListener);
            if (i == 0) onClickListener.onClick(textView);
            addView(textView, params);
            views.put(i, textView);
        }
    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int size = views.size();
            for (int i = 0; i < size; i++) {
                TextView tv = views.valueAt(i);
                tv.setTextColor(noClickTextColor);
            }
            ((TextView) v).setTextColor(clickTextColor);
            if (itemListener != null) itemListener.onSelect((Integer) v.getTag(), v);
        }
    };


    private OnSelectItemListener itemListener;

    public void setOnSelectItemListener(OnSelectItemListener itemListener) {
        this.itemListener = itemListener;
//        onClickListener.onClick(views.get(0));
    }

    public interface OnSelectItemListener {
        void onSelect(int index, View tv);
    }
}
