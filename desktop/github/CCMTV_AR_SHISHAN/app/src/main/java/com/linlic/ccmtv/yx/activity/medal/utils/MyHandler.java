package com.linlic.ccmtv.yx.activity.medal.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by bentley on 2018/11/26.
 */

public class MyHandler extends Handler {
    private final WeakReference<Activity> mActivity;

    public MyHandler(Activity activity) {
        mActivity = new WeakReference<Activity>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        if (mActivity.get() == null) {
            return;
        }
    }
}
