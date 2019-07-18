package com.linlic.ccmtv.yx.activity.upload.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.db.MyDbUtils;
import com.linlic.ccmtv.yx.activity.upload.IsUpload;

/**
 * name：
 * author：MrSong
 * data：2016/4/22.
 */
public class MyUploadVideoReveiver extends BroadcastReceiver {
    int dbid = 0;
    String videoName = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        switch (action) {
            case "upload_video":
                //如果当前已经在上传了就跳出方法
//                if (MyUploadVideoService.now_statu == MyUploadVideoService.upload_progress) {
//                    Toast.makeText(context, "当前任务已正在上传，请等待上传完毕后在提交!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                dbid = intent.getExtras().getInt("dbid");
                videoName = intent.getExtras().getString("videoName");
                break;
            case "upload_video_success":
                MyDbUtils.updateUploadState(context, MyUploadVideoService.upload_success + "", dbid);
                //上传成功停止服务
                context.stopService(new Intent(context, MyUploadVideoService.class));

                MyNotificationVideo(context, "1个任务上传完成", videoName + "上传成功", "上传完成", true);
                break;
            case "upload_video_failure":
                MyDbUtils.updateUploadState(context, MyUploadVideoService.upload_failure + "", dbid);
                //上传失败停止服务
                context.stopService(new Intent(context, MyUploadVideoService.class));
                MyNotificationVideo(context, "您有任务上传失败", videoName + "上传失败", "上传失败", true);
                break;
            case "upload_video_progress":
                String progress = intent.getExtras().getString("progress");
                String total = intent.getExtras().getString("total");
                String current = intent.getExtras().getString("current");
                MyDbUtils.updateUploadStateMsg(context, MyUploadVideoService.upload_progress + "", dbid, progress, total, current);
                MyNotificationVideo(context, "上传任务已添加", "正在上传" + videoName, "当前进度:" + progress, false);
                break;
        }
    }


    private void MyNotificationVideo(Context context, String tan, String title, String cont, boolean isProgress) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(context, IsUpload.class);
        intent1.putExtra("TAG", "Video");
      /*  lags有四个取值：
        int FLAG_CANCEL_CURRENT：如果该PendingIntent已经存在，则在生成新的之前取消当前的。
        int FLAG_NO_CREATE：如果该PendingIntent不存在，直接返回null而不是创建一个PendingIntent.
        int FLAG_ONE_SHOT:该PendingIntent只能用一次，在send()方法执行后，自动取消。
        int FLAG_UPDATE_CURRENT：如果该PendingIntent已经存在，则用新传入的Intent更新当前的数据。*/

        PendingIntent pendingIntent4 = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        // 通过Notification.Builder来创建通知，注意API Level
        // API16之后才支持
        /*NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Notification notify4 = builder
                .setTicker(tan)
                .setContentTitle(title)
                .setContentText(cont)
                .setContentIntent(pendingIntent4).setNumber(1)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.app_smaicon)
                .setColor(Color.parseColor("#0A308F"))
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.app_icon))
                .build();*/
        Notification notify4 = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.app_icon)
                .setTicker(tan)
                .setContentTitle(title)
                .setContentText(cont)
                .setContentIntent(pendingIntent4).setNumber(1).build();
        // level16及之后增加的，API11可以使用getNotificatin()来替代
        notify4.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
        if (isProgress == true) {
            //添加声音
            notify4.defaults |= Notification.DEFAULT_SOUND;
            //添加震动
            notify4.defaults |= Notification.DEFAULT_VIBRATE;
        }

        manager.notify(2, notify4);// 步骤4：通过通知管理器来发起通知。如果id不同，则每click，在status哪里增加一个提示
    }
}
