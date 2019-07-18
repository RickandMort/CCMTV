package com.linlic.ccmtv.yx.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by bentley on 2019/4/28.
 */

public class BaseRecyclerViewAdapter extends BaseQuickAdapter {


    public BaseRecyclerViewAdapter(@LayoutRes int layoutResId, @Nullable List data) {
        super(layoutResId, data);
    }

    @Override
    public void convert(BaseViewHolder helper, Object item) {

    }

}
