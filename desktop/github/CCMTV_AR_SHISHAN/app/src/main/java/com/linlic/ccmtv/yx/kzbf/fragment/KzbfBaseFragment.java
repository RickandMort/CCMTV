package com.linlic.ccmtv.yx.kzbf.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linlic.ccmtv.yx.activity.base.BaseFragment;

/**
 * Created by bentley on 2018/11/21.
 */

public abstract class KzbfBaseFragment extends BaseFragment {
    /**
     * 标识是否在setUserVisibleHint方法中已经调用了一次可见操作
     */
    protected boolean isUserVisibleHinted = false;
    protected View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = initView(inflater, container, savedInstanceState);
        if (getUserVisibleHint() && !isUserVisibleHinted) {
            onRealResume(!isUserVisibleHinted);
            isUserVisibleHinted = true;
        }
        return mView;
    }

    public void onRealResume(boolean isFirst) {

    }

    public void onRealPause() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {//当前Fragment可见时加载数据
            //当前Fragment可见时做操作
            if (mView != null) { // 确保onCreateView已经调用
                //每次可见的操作
                onRealResume(!isUserVisibleHinted);
                if (!isUserVisibleHinted) {
                    isUserVisibleHinted = true;
                }
            }
        } else {
            if (mView != null && isUserVisibleHinted) { // 确保onCreateView已经调用
                //每次不可见的操作
                onRealPause();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isUserVisibleHinted = false;
    }

    public abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
}
