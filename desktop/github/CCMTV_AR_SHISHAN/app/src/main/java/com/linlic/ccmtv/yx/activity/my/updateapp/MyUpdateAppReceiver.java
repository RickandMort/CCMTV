package com.linlic.ccmtv.yx.activity.my.updateapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.my.MySettingActivity;

/**
 * name：
 * author：Larry
 * data：2016/6/2.
 */
public class MyUpdateAppReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        switch (action) {
            case "update_app_progress":
                String progress = intent.getExtras().getString("progress");
                MyNotificationUpdate(context,"正在下载", "CCMTV app正在更新" , "当前进度:" + progress, false);
                break;
            case "update_app_failure":
                MyNotificationUpdate(context,"下载失败", "CCMTV app下载失败" , "下载失败", false);
                break;
        }
    }


    private void MyNotificationUpdate(Context context, String tan, String title, String cont, boolean isProgress) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, MySettingActivity.class);
        PendingIntent pendingIntent3 = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Notification notify = builder
                .setTicker(tan)
                .setContentTitle(title)
                .setContentText(cont)
                .setContentIntent(pendingIntent3).setNumber(1)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.app_smaicon)
                .setColor(Color.parseColor("#0A308F"))
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.app_icon))
                .build();

        // level16及之后增加的，API11可以使用getNotificatin()来替代
        notify.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
        if (isProgress == true) {
            //添加声音
            notify.defaults |= Notification.DEFAULT_SOUND;
            //添加震动
            notify.defaults |= Notification.DEFAULT_VIBRATE;
        }

        manager.notify(4, notify);// 步骤4：通过通知管理器来发起通知。如果id不同，则每click，在status哪里增加一个提示
    }
}
