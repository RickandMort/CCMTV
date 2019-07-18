package com.linlic.ccmtv.yx.kzbf.adapter.simple;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.linlic.ccmtv.yx.kzbf.adapter.base.BaseRecyclerAdapter;

import java.util.List;


/**
 * Created by congge on 2018/7/18.
 */

public abstract class SimpleViewBaseAdapter<T> extends BaseRecyclerAdapter<T> {

    public SimpleViewBaseAdapter(List<T> ts) {
        super(ts);
    }

    @Override
    protected SimpleBaseHolder<T> CreateViewHolder(View itemView, int viewType) {
        return new SimpleBaseHolder<T>(itemView) {
            @Override
            public void onBind(T t) {
                convert(this, t);
            }
        };
    }

    public abstract void convert(SimpleBaseHolder<T> holder, T t);


    @Override
    protected abstract View CreateItemView(ViewGroup parent, Context context, int viewType);
}
