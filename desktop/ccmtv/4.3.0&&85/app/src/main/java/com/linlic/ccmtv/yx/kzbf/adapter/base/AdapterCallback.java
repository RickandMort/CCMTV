package com.linlic.ccmtv.yx.kzbf.adapter.base;

/**
 * Created by congge on 2018/7/18.
 */

public interface AdapterCallback<T> {
    void update(T t, BaseRecyclerAdapter.BaseViewHolder<T> holder);
}
