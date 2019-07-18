package com.linlic.ccmtv.yx.kzbf.adapter.base;

/**
 * Created by congge on 2018/7/18.
 */

public interface AdapterClickListener<T> {
    void onItemClick(BaseRecyclerAdapter.BaseViewHolder holder, T t);

    void onItemLongClick(BaseRecyclerAdapter.BaseViewHolder holder, T t);
}
