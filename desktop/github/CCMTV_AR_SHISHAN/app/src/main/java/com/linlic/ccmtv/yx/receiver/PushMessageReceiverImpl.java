package com.linlic.ccmtv.yx.receiver;

import android.content.Context;
import android.util.Log;

import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.vivo.push.model.UPSNotificationMessage;
import com.vivo.push.sdk.OpenClientPushMessageReceiver;


public class PushMessageReceiverImpl extends OpenClientPushMessageReceiver {
    /**
     * TAG to Log
     */
    public static final String TAG ="vivoPush";

    @Override
    public void onNotificationMessageClicked(Context context, UPSNotificationMessage msg) {
        String customContentString = msg.getSkipContent();
        String notifyString = "通知点击 msgId " + msg.getMsgId() + " ;customContent=" + customContentString;
        Log.d(TAG, notifyString);

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
//        updateContent(notifyString);
    }

    @Override
    public void onReceiveRegId(Context context, String regId) {
        SharedPreferencesTools.savePush_id(context,regId);
        SharedPreferencesTools.savePush_Platform(context,"vivo");
        String responseString = TAG+"：onReceiveRegId regId = " + regId;
//        Toast.makeText(context, responseString, Toast.LENGTH_SHORT).show();
        Log.d(TAG, responseString);
//        updateContent(responseString);
    }
}
