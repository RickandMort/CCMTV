package com.linlic.ccmtv.yx.activity.upload.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.db.MyDbUtils;
import com.linlic.ccmtv.yx.activity.upload.IsUpload;

import java.util.Calendar;

/**
 * name：上传病例广播接收器
 * author：Larry
 * data：2016/5/4 9:30
 */
public class MyUploadCaseReceiver extends BroadcastReceiver {
    int case_id;
    String case_title = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        switch (action) {
            case "upload_case":
                case_id = intent.getExtras().getInt("case_id");
                case_title = intent.getExtras().getString("case_title");
                break;
            case "upload_case_success":
                MyDbUtils.updateUploadCaseState(context, MyUploadCaseService.upload_success + "", case_id);
//                Log.i("case_id", case_id + "");
                //上传成功停止服务
                context.stopService(new Intent(context, MyUploadVideoService.class));
                int y, m, d, h, mi, s;
                Calendar cal = Calendar.getInstance();
                y = cal.get(Calendar.YEAR);
                m = cal.get(Calendar.MONTH)+1;
                d = cal.get(Calendar.DATE);
                h = cal.get(Calendar.HOUR_OF_DAY);
                mi = cal.get(Calendar.MINUTE);
                s = cal.get(Calendar.SECOND);
                String time = y + "." + m + "." + d ;
                MyDbUtils.updateUploadCaseTime(context, time, case_id);

                MyNotificationCase(context, "1个任务上传完成", case_title + "上传成功", "上传完成", true);
                break;
            case "upload_case_failure":
                MyDbUtils.updateUploadCaseState(context, MyUploadCaseService.upload_failure + "", case_id);
                //上传失败停止服务
                context.stopService(new Intent(context, MyUploadVideoService.class));
                MyNotificationCase(context, "您有任务上传失败", case_title + "上传失败", "上传失败", true);
                break;
            case "upload_case_progress":
                String progress = intent.getExtras().getString("progress");
                String total = intent.getExtras().getString("total");
                String current = intent.getExtras().getString("current");
//                Log.i("上传病例", "total" + total + "        当前" + current);
                MyDbUtils.updateUploadCaseStateMsg(context, MyUploadCaseService.upload_progress + "", case_id, progress, total, current);
                MyNotificationCase(context, "上传任务已添加", "正在上传" + case_title, "当前进度:" + progress, false);
                break;
        }
    }


    private void MyNotificationCase(Context context, String tan, String title, String cont, boolean isProgress) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, IsUpload.class);
        intent.putExtra("TAG", "Case");
        PendingIntent pendingIntent3 = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // 通过Notification.Builder来创建通知，注意API Level
        // API16之后才支持
        /*NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Notification notify = builder
                .setTicker(tan)
                .setContentTitle(title)
                .setContentText(cont)
                .setContentIntent(pendingIntent3).setNumber(1)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.app_smaicon)
                .setColor(Color.parseColor("#0A308F"))
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.app_icon))
                .build();*/
        Notification notify = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.app_icon)
                .setTicker(tan)
                .setContentTitle(title)
                .setContentText(cont)
                .setContentIntent(pendingIntent3).setNumber(1).build();

        // level16及之后增加的，API11可以使用getNotificatin()来替代
        notify.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
        if (isProgress == true) {
            //添加声音
            notify.defaults |= Notification.DEFAULT_SOUND;
            //添加震动
            notify.defaults |= Notification.DEFAULT_VIBRATE;
        }

        manager.notify(3, notify);// 步骤4：通过通知管理器来发起通知。如果id不同，则每click，在status哪里增加一个提示
    }
}
