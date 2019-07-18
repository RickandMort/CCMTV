package com.linlic.ccmtv.yx.activity.base;

import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * name：懒加载frgment父类
 * author：Larry
 * data：2017/2/14.
 * http://blog.csdn.net/jali_li/article/details/51279774
 */
public abstract class BaseLazyFragment extends BaseFragment {
    protected boolean isVisible = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInVisible();
        }
    }

    private void onInVisible() {

    }

    private void onVisible() {
        LazyLoad();
    }

    public abstract void LazyLoad();
}
