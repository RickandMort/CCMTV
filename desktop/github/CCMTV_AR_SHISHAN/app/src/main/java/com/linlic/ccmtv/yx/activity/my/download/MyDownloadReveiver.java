package com.linlic.ccmtv.yx.activity.my.download;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.db.MyDbUtils;
import com.linlic.ccmtv.yx.activity.entity.DbDownloadVideo;

/**
 * name：
 * author：MrSong
 * data：2016/4/5.
 */
public class MyDownloadReveiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action) {
            case "download":
                //如果当前已经在下载了就跳出方法
                if (DownloadService.now_statu == DownloadService.download_progress) {
                    return;
                }
//                String videoId = intent.getExtras().getString("videoId");

//                //根据ID查找数据库，更改状态为下载中后开始下载
//                MyDbUtils.updateDownState(context, videoId, DownloadService.download_progress + "");
                download(context);

                break;
            case "download_success":
                String id = intent.getExtras().getString("videoId");
                String videoName = intent.getExtras().getString("videoName");
                //根据ID查找数据库，更改数据库下载状态 和文件路径（当文件已存在时，但是数据库是没有保存当前文件路径）
                MyDbUtils.updateDownState(context, id, DownloadService.download_success + "");
                //如果当前已经在下载了就跳出方法
                if (DownloadService.now_statu == DownloadService.download_progress) {
                    return;
                }

                download(context);

                MyNotificationDownload(context, "1个任务下载完成", videoName + "下载成功", "下载完成", true);
                break;
            case "download_failure":
                String id1 = intent.getExtras().getString("videoId");
                String videoName1 = intent.getExtras().getString("videoName");
                //根据ID查找数据库，更改数据库下载状态
                MyDbUtils.updateDownState(context, id1, DownloadService.download_failure + "");
                //下载失败停止服务
                context.stopService(new Intent(context, DownloadService.class));

                MyNotificationDownload(context, "您有任务下载失败", videoName1 + "下载失败", "下载失败", true);
                break;
            case "download_progress":

                String id2 = intent.getExtras().getString("videoId");
                String progress = intent.getExtras().getString("progress");
                String videoName2 = intent.getExtras().getString("videoName");
                String total = intent.getExtras().getString("total");
                String current = intent.getExtras().getString("current");
                String filePath = intent.getExtras().getString("filePath");
                String speed = intent.getExtras().getString("speed");
//                System.out.println("intent接收到的值：" + speed);
                //根据ID查找数据库，更改数据库下载状态和下载进度
                MyDbUtils.updateDownStateAndProgress(context, id2, DownloadService.download_progress + "", progress, total, current, filePath, speed);

                MyNotificationDownload(context, "下载任务进行中", "正在下载" + videoName2, "当前进度:" + progress, false);
                break;
            case "download_stop":
                String id3 = intent.getExtras().getString("videoId");
                String videoState = intent.getExtras().getString("videoState");
                if ("18".equals(videoState)) {//下载中
                    //改变状态加入队列
                    MyDbUtils.updateDownState(context, id3, DownloadService.download_stop + "");
                    Intent i = new Intent(context, DownloadService.class);
                    i.putExtra("videoId", id3);
                    i.putExtra("videoName", "");
                    i.putExtra("downProgress", "");
                    i.putExtra("downURL", "");
                    i.putExtra("isStop", true);
                    context.startService(i);
                    //第二步，查找数据库是否有等待下载的视频
                    download(context);
                } else if (videoState.equals("500")) {//下载失败
                    if (DownloadService.now_statu == DownloadService.download_progress) {
                        //改变状态加入队列
                        MyDbUtils.updateDownState(context, id3, DownloadService.download_stop + "");
                    } else {
                        //改变状态加入队列，开启下载
                        MyDbUtils.updateDownState(context, id3, DownloadService.download_stop + "");
                        download(context);
                    }
                } else if (videoState.equals("100")) {//下载暂停
                    boolean net_fail_stop = intent.getBooleanExtra("net_fail_stop", false);
                    if (net_fail_stop) {
                        DownloadService.now_statu = DownloadService.download_stop;
                        MyDbUtils.updateDownState(context, id3, DownloadService.download_stop + "");
                    } else {
                        if (DownloadService.now_statu == DownloadService.download_progress) {
                            MyDbUtils.updateDownState(context, id3, DownloadService.download_wait + "");
                        } else {
                            DownloadService.now_statu = DownloadService.download_progress;
                            DbDownloadVideo dbDownloadVideo = MyDbUtils.findVideoMsg(context, id3);
                            Intent i = new Intent(context, DownloadService.class);
                            i.putExtra("videoId", dbDownloadVideo.getVideoId());
                            i.putExtra("videoName", dbDownloadVideo.getVideoName());
                            i.putExtra("downProgress", dbDownloadVideo.getDownProgress());
                            i.putExtra("downURL", dbDownloadVideo.getDownURL());
                            i.putExtra("isStop", false);
                            context.startService(i);
                        }
                    }
                } else if (videoState.equals("300")) {//等待下载
                    MyDbUtils.updateDownState(context, id3, DownloadService.download_stop + "");
                }
                break;
        }
    }


    private void download(Context context) {
        //查找等待下载的视频
        String waitDown = MyDbUtils.findWaitDown(context);
        if (waitDown.length() == 0) {//已经没有等待下载的视频了
            //停止service
            context.stopService(new Intent(context, DownloadService.class));
            return;
        } else {
            //启动service并且传输数据过去，开始下载
            DbDownloadVideo dbDownloadVideo = MyDbUtils.findVideoMsgForId(context, waitDown);
            Intent i = new Intent(context, DownloadService.class);
            i.putExtra("videoId", dbDownloadVideo.getVideoId());
            i.putExtra("videoName", dbDownloadVideo.getVideoName());
            i.putExtra("downProgress", dbDownloadVideo.getDownProgress());
            i.putExtra("downURL", dbDownloadVideo.getDownURL());
            i.putExtra("isStop", false);
            context.startService(i);
        }
    }

    private void MyNotificationDownload(Context context, String tan, String title, String cont, boolean isProgress) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent3 = PendingIntent.getActivity(context, 0, new Intent(context, Download.class), 0);
        // 通过Notification.Builder来创建通知，注意API Level
        // API16之后才支持
        /*NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Notification notify3 = builder
                .setTicker(tan)
                .setContentTitle(title)
                .setContentText(cont)
                .setContentIntent(pendingIntent3).setNumber(1)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.app_smaicon)
                .setColor(Color.parseColor("#0A308F"))
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.app_icon))
                .build();*/
        Notification notify3 = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.app_icon)
                .setTicker(tan)
                .setContentTitle(title)
                .setContentText(cont)
                .setContentIntent(pendingIntent3).setNumber(1).build(); // 需要注意build()是在API

        // level16及之后增加的，API11可以使用getNotificatin()来替代
        notify3.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
        if (isProgress == true) {
            //添加声音
            notify3.defaults |= Notification.DEFAULT_SOUND;
            //添加震动
            notify3.defaults |= Notification.DEFAULT_VIBRATE;
        }

        manager.notify(1, notify3);// 步骤4：通过通知管理器来发起通知。如果id不同，则每click，在status哪里增加一个提示
    }
}
