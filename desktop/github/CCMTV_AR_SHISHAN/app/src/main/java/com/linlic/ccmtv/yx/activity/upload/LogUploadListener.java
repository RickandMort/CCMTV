package com.linlic.ccmtv.yx.activity.upload;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.LocalApplication;
import com.linlic.ccmtv.yx.activity.upload.new_upload.IsUpload3;
import com.lzy.okgo.model.Progress;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.upload.UploadListener;

import java.text.NumberFormat;

/**
 * Created by bentley on 2018/4/16.
 */

public class LogUploadListener<T> extends UploadListener<T> {

    Context context= LocalApplication.getAppContext();
    private NumberFormat nf = NumberFormat.getPercentInstance();

    public LogUploadListener() {
        super("LogUploadListener");
    }

    @Override
    public void onStart(Progress progress) {
//        Log.e("LogUploadListener", "onStart: 开始上传");
    }

    @Override
    public void onProgress(Progress progress) {
//        Log.e("LogUploadListener", "onProgress: 正在上传" + progress);
        double progressNum = progress.fraction;
        nf.setMaximumFractionDigits(1);
        MyNotificationCase(context, "上传任务已添加", "正在上传" + progress.extra2, "当前进度:" + nf.format(progressNum), false);
    }

    @Override
    public void onError(Progress progress) {
//        Log.e("LogUploadListener", "onError: 上传出错" + progress.exception);
        MyNotificationCase(context, "您有任务上传失败", progress.extra2 + "上传失败", "上传失败", true);
    }

    @Override
    public void onFinish(T t, Progress progress) {
//        Log.e("LogUploadListener", "onFinish: 上传完成");
        MyNotificationCase(context, "1个任务上传完成", progress.extra2 + "上传成功", "上传完成", true);
    }

    @Override
    public void onRemove(Progress progress) {

    }

    public void MyNotificationCase(Context context, String tan, String title, String cont, boolean isProgress) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, IsUpload3.class);
        intent.putExtra("TAG", "Case");
        PendingIntent pendingIntent3 = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // 通过Notification.Builder来创建通知，注意API Level
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
