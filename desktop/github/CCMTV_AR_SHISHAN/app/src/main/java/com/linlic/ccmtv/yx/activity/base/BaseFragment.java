package com.linlic.ccmtv.yx.activity.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.baidu.mobstat.StatService;
import com.linlic.ccmtv.yx.R;

public class BaseFragment extends Fragment {
    public Context mContext;
    private LinearLayout layout_loading;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * name:查找值 author:Tom 2016-2-2下午5:29:04
     */
    public void findId() {
        layout_loading = (LinearLayout) getActivity().findViewById(R.id.layout_loading);
    }

    /**
     * 设置加载布局可见
     */
    public void showLoading() {
        layout_loading.setVisibility(View.VISIBLE);
    }

    /**
     * 设置加载布局隐藏
     */
    public void hideLoading() {
        layout_loading.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        /**
         * Fragment页面起始 (注意： 如果有继承的父Fragment中已经添加了该调用，那么子Fragment中务必不能添加）
         */
        StatService.onResume(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        /**
         *Fragment 页面结束（注意：如果有继承的父Fragment中已经添加了该调用，那么子Fragment中务必不能添加）
         */
        StatService.onPause(this);

    }


}
