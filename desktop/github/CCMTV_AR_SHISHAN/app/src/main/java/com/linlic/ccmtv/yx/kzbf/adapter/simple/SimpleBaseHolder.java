package com.linlic.ccmtv.yx.kzbf.adapter.simple;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.linlic.ccmtv.yx.kzbf.adapter.base.BaseRecyclerAdapter;

/**
 * Created by congge on 2018/7/20.
 */

public abstract class SimpleBaseHolder<T> extends BaseRecyclerAdapter.BaseViewHolder<T> {
    private SparseArray<View> mViews;
    private View mConvertView;
    private Context mContext;

    public SimpleBaseHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        mConvertView = itemView;
        mViews = new SparseArray<>();
    }

    @Override
    public abstract void onBind(T t);

    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }


    /****以下为辅助方法*****/

    /**
     * 设置TextView的值
     *
     * @param viewId
     * @param text
     * @return
     */
    public SimpleBaseHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public SimpleBaseHolder setBackground(int viewId, int resId) {
        View v = getView(viewId);
        v.setBackgroundResource(resId);
        return this;
    }

    public SimpleBaseHolder setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }


    public SimpleBaseHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    public SimpleBaseHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    public SimpleBaseHolder setTextColor(int viewId, int color) {
        TextView view = getView(viewId);
        view.setTextColor(color);
        return this;
    }


    /**
     * 关于事件的
     */
    public SimpleBaseHolder setOnClickListener(int viewId,
                                               View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }


}
