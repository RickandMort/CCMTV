package com.linlic.ccmtv.yx.activity.receiver;

import android.annotation.TargetApi;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

/**
 * Created by Administrator on 2017/12/13.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class DService extends NotificationListenerService {
    private static final String TAG = "医学视频";

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i(TAG,"Notification removed");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.i(TAG, "Notification posted");
    }
}