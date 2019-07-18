/*
 * Copyright (C) 2015 pengjianbo(pengjianbosoft@gmail.com), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.linlic.ccmtv.yx.floatWindow.service;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.home.VideoFive;
import com.linlic.ccmtv.yx.floatWindow.Float_Main;
import com.linlic.ccmtv.yx.floatWindow.widget.FloatLayout;
import com.linlic.ccmtv.yx.floatWindow.widget.FloatView;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Desction:Float view service
 * Author:pengjianbo
 * Date:15/10/26 下午5:15
 */
public class FloatViewService extends Service {
    public static NotificationManager notificationManager;
    private RemoteViews contentView;
    public static Notification notification;
    /**
     * 通知栏按钮点击事件对应的ACTION（标识广播）
     */
    public final static String ACTION_START = "START";
    public final static String ACTION_PAUSE = "PAUSE";
    /**
     * 播放/暂停 按钮点击 ID
     */
    public final static int BUTTON_PALY_ID = 1;
    public final static int BUTTON_PALY_ID2 = 2;
    private final int NOTIFICATION_ID = 0xa01;
    private final int REQUEST_CODE = 0xb01;
    public final static String INTENT_BUTTONID_TAG = "ButtonId";
    /**
     * 标识按钮状态：是否在播放
     */
    public static boolean isPlay = false;

    /**
     * 通知栏按钮广播
     */
    public static ButtonBroadcastReceiver mReceiver;

    private static final int START = 0;//开始计时消息标志，下面用到
    private static final int STOP = 1;//停止计时消息标志，下面用到
    private static int RUN = 0;//子线程中的while循环判断标志
    //    private Thread thread;
    private FloatView mFloatView;

    //用于播放音乐等媒体资源
    private MediaPlayer mediaPlayer;
    //标志判断播放歌曲是否是停止之后重新播放，还是继续播放
    private boolean isStop = true;
    //判断是否加载通知栏
    private boolean isInit = false;

    private SimpleDateFormat formatter;

    /**
     * home键监听
     */
    private HomeWatcherReceiver mHomeKeyReceiver;

    static final String SYSTEM_REASON = "reason";
    static final String SYSTEM_RECENT_APPS = "recentapps";
    private int second = 0;

    /**
     * onBind，返回一个IBinder，可以与Activity交互
     * 这是Bind Service的生命周期方法
     *
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        return new FloatViewServiceBinder();
    }

    private boolean mReceiverTag = false;   //广播接受者标识


    //请求悬浮窗权限
    @TargetApi(Build.VERSION_CODES.M)
    private void getOverlayPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + getPackageName()));
     //   this.startActivityForResult(intent, REQUEST_CODE_FLOATVIEW_PERMISSION);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void openFloatViewPermission() {
        //1.悬浮窗的权限
        //检查是否已经授予权限
        if (!Settings.canDrawOverlays(this)) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("权限申请")
                    .setMessage("在设置-应用-权限中开启悬浮窗权限，以正常使用")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //若未授权则请求权限
                            getOverlayPermission();
                        }
                    })
                    .create();

            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onCreate() {
        super.onCreate();
        //todo 检查权限 begin
//        if (!Settings.canDrawOverlays(this)) {
//           // openFloatViewPermission();
//            Log.e("PRETTY_LOGGER", "onCreate() returned: " + "需要打开权限");
//            return;
//        }
        //todo 检查权限 end

        mFloatView = new FloatView(this);
//        thread = new Thread(new MyThread()); // start thread
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();

            //为播放器添加播放完成时的监听器
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //发送广播到MainActivity
                    Intent intent = new Intent();
                    intent.setAction("com.complete");
                    sendBroadcast(intent);
                    handler.sendEmptyMessage(STOP);//暂停计时
                }
            });
        }

        //注册广播接收者
        mHomeKeyReceiver = new HomeWatcherReceiver();
        final IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(mHomeKeyReceiver, homeFilter);
    }

    public void showFloat() {
        if (mFloatView != null) {
            mFloatView.show();
            mFloatView.setImage();
            mFloatView.setTitle();
            FloatLayout.isshwo = true;

            if (!isInit) {//避免重复加载通知栏
                initAndNotify(FloatLayout.title);
                if (mediaPlayer.isPlaying()) {
                    contentView.setImageViewResource(R.id.start, R.mipmap.float_07);
                    isPlay = false;
                } else {
                    contentView.setImageViewResource(R.id.start, R.mipmap.float_16);
                    isPlay = true;
                }
                isInit = true;
            } else {
                if (mediaPlayer.isPlaying()) {
                    contentView.setImageViewResource(R.id.start, R.mipmap.float_16);
                    isPlay = true;
                } else {
                    contentView.setImageViewResource(R.id.start, R.mipmap.float_07);
                    isPlay = false;
                }
            }
            notificationManager.notify(NOTIFICATION_ID, notification);
        }
    }

    public void hideFloat() {
        if (mFloatView != null) {
            mFloatView.hide();
        }
    }

    public void destroyFloat() {
        if (mFloatView != null) {
            mFloatView.destroy();
        }
        mFloatView = null;
        FloatLayout.isshwo = false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyFloat();
        //注销广播接收者
        if (null != mHomeKeyReceiver) {
            unregisterReceiver(mHomeKeyReceiver);
        }
    }

    public class FloatViewServiceBinder extends Binder {
        public FloatViewService getService() {
            return FloatViewService.this;
        }
    }

    /**
     * 在此方法中，可以执行相关逻辑，如耗时操作
     *
     * @param intent  :由Activity传递给service的信息，存在intent中
     * @param flags   ：规定的额外信息
     * @param startId ：开启服务时，如果有规定id，则传入startid
     * @return 返回值规定此startservice是哪种类型，粘性的还是非粘性的
     * START_STICKY:粘性的，遇到异常停止后重新启动，并且intent=null
     * START_NOT_STICKY:非粘性，遇到异常停止不会重启
     * START_REDELIVER_INTENT:粘性的，重新启动，并且将Context传递的信息intent传递
     * 此方法是唯一的可以执行很多次的方法
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Message message = null;
        switch (intent.getIntExtra("type", -1)) {
            case FloatLayout.PLAT_MUSIC:
                FloatLayout.isplay = true;

                if (isStop) {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    try {
                        mediaPlayer.setDataSource(FloatLayout.url);
                        mediaPlayer.prepareAsync();
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mediaPlayer.start();
                                handler.sendEmptyMessage(START);//开始计时
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    isStop = false;
                } else if (!isStop && !mediaPlayer.isPlaying() && mediaPlayer != null) {
                    mediaPlayer.start();
                    handler.sendEmptyMessage(START);//开始计时
                } else {//用户重新点击了新的音频播放  先停止音乐播放 在重新赋值新的音频地址播放
                    //停止之后要开始播放音乐
                    mediaPlayer.stop();
                    isStop = true;
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    try {
                        mediaPlayer.setDataSource(FloatLayout.url);
                        mediaPlayer.prepareAsync();
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mediaPlayer.start();
                                second = 0;
                                handler.sendEmptyMessage(START);//开始计时
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    isStop = false;
                }
//                Log.e("显示状态", mFloatView.isshow() + "");
                if (contentView != null && 1 == 1) {
                    contentView.setImageViewResource(R.id.start, R.mipmap.float_16);
                    notificationManager.notify(NOTIFICATION_ID, notification);
                }

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        Log.d("tag", "播放完毕");
                        //根据需要添加自己的代码。。。
                        FloatLayout.isplay = false;
                        contentView.setImageViewResource(R.id.start, R.mipmap.float_07);
                        notificationManager.notify(NOTIFICATION_ID, notification);
                    }
                });
                break;
            case FloatLayout.PAUSE_MUSIC:
                FloatLayout.isplay = false;
                //播放器不为空，并且正在播放
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    handler.sendEmptyMessage(STOP);//暂停计时
                }
                if (contentView != null && 1 == 1) {
                    contentView.setImageViewResource(R.id.start, R.mipmap.float_07);
                    notificationManager.notify(NOTIFICATION_ID, notification);
                }
                break;
            case FloatLayout.STOP_MUSIC:
                FloatLayout.isplay = false;
                notificationManager.cancelAll();
                if (mediaPlayer != null) {
//                    notificationManager.cancel(NOTIFICATION_ID);
                    second = 0;//时间重置
                    mFloatView.setMusicTime(IntToTime3(FloatLayout.currentTime), IntToTime2(mediaPlayer.getDuration()));
                    handler.sendEmptyMessage(STOP);//暂停计时
                    //停止之后要开始播放音乐
                    mediaPlayer.stop();
                    isStop = true;
                }
                if (mReceiver != null) {
                    if (mReceiverTag) {   //判断广播是否注册
                        mReceiverTag = false;   //Tag值 赋值为false 表示该广播已被注销
                        getApplicationContext().unregisterReceiver(mReceiver);
                    }
                    isInit = false;
                }
                hideFloat();
//                Log.e("隐藏", "停止音乐");
                break;
            case FloatLayout.SPEED:
                if (mediaPlayer != null) {
                    int position = mediaPlayer.getCurrentPosition();
                    mediaPlayer.seekTo(position + 10000);
                }
                break;
            case FloatLayout.QUICK_RETREAT:
                if (mediaPlayer != null) {
                    int position = mediaPlayer.getCurrentPosition();
                    if (position > 10000) {
                        position -= 10000;
                    } else {
                        position = 0;
                    }
                    mediaPlayer.seekTo(position);
                }
                break;
            case FloatLayout.HIDE:
                hideFloat();
//                Log.e("隐藏", "隐藏");
                break;
            case FloatLayout.SHOW:
                if (FloatLayout.isshwo) {
                    showFloat();
                }
                break;
        }
        return START_NOT_STICKY;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            handler.removeMessages(START);
            switch (msg.what) {
                case START:
                    second++;
                    FloatLayout.CurrentTime = IntToTime3(mediaPlayer.getCurrentPosition());
                    FloatLayout.currentTime = mediaPlayer.getCurrentPosition();
                    FloatLayout.mediaPlayCurrentTime = mediaPlayer.getCurrentPosition();
                    FloatLayout.totalTime = mediaPlayer.getDuration();
                    mFloatView.setMusicTime(IntToTime3(FloatLayout.currentTime), IntToTime2(FloatLayout.totalTime));
                    if (VideoFive.video_view2 != null && VideoFive.video_view2.isPlaying()) {
                        //停止播放音频
                        MainActivity.stopFloatingView(getBaseContext());
                    }
                    handler.sendEmptyMessageDelayed(START, 1000);
                    break;
                case STOP:
                    break;
            }
        }
    };

    /**
     * 初始化通知栏信息，并显示
     *
     * @param title
     */
    public void initAndNotify(String title) {
        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
        // 此处设置的图标仅用于显示新提醒时候出现在设备的通知栏
        mBuilder.setSmallIcon(R.mipmap.logo);
        notification = mBuilder.build();

        // 当用户下来通知栏时候看到的就是RemoteViews中自定义的Notification布局
        contentView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.customnotice);
        //设置通知栏信息
        contentView.setTextViewText(R.id.title, title);//应用名称
        //如果版本号低于（3.0），那么不显示按钮
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            contentView.setViewVisibility(R.id.start, View.GONE);
        } else {
            if (!mReceiverTag) {     //在注册广播接受者的时候 判断是否已被注册,避免重复多次注册广播
                //注册广播
                mReceiverTag = true;    //标识值 赋值为 true 表示广播已被注册
                mReceiver = new ButtonBroadcastReceiver();
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(ACTION_START);
                getApplicationContext().registerReceiver(mReceiver, intentFilter);
            }
            //设置按钮
//            contentView.setImageViewResource(R.id.start, R.mipmap.ic_launcher);
            //设置点击的事件
            Intent startIntent = new Intent(ACTION_START);
            startIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_PALY_ID);
            PendingIntent intent_paly = PendingIntent.getBroadcast(getApplicationContext(), BUTTON_PALY_ID, startIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            contentView.setOnClickPendingIntent(R.id.start, intent_paly);
        }

        notification.contentView = contentView;
        // 点击notification自动消失
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        // 需要注意的是，作为选项，此处可以设置MainActivity的启动模式为singleTop，避免重复新建onCreate()。
        Intent intent = new Intent(getApplicationContext(), Float_Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // 当用户点击通知栏的Notification时候，切换回TaskDefineActivity。
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.contentIntent = pi;

        // 发送到手机的通知栏
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    /**
     * （通知栏中的点击事件是通过广播来通知的，所以在需要处理点击事件的地方注册广播即可）
     * 广播监听按钮点击事件
     */
    public class ButtonBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_START)) {
                //通过传递过来的ID判断按钮点击属性或者通过getResultCode()获得相应点击事件
                int buttonId = intent.getIntExtra(INTENT_BUTTONID_TAG, -1);
                switch (buttonId) {
                    case BUTTON_PALY_ID:
                        if (buttonId != -1) {
                            onDownLoadBtnClick();
                        }
                        break;
                    default:
                        break;
                }
            }
            if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                Toast.makeText(context, "锁屏界面", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void onDownLoadBtnClick() {
        if (FloatLayout.isplay) {
            //当前是进行中，则暂停
            isPlay = false;
            contentView.setImageViewResource(R.id.start, R.mipmap.float_07);
            playingmusic(FloatLayout.PAUSE_MUSIC);
        } else {
            //当前暂停，则开始
            isPlay = true;
            contentView.setImageViewResource(R.id.start, R.mipmap.float_16);
            playingmusic(FloatLayout.PLAT_MUSIC);
        }
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    public void playingmusic(int type) {
        //启动服务，播放音乐
        Intent intent = new Intent(getApplicationContext(), FloatViewService.class);
        intent.putExtra("type", type);
        getApplicationContext().startService(intent);
    }

    /**
     * 将毫秒转化为时分秒
     * 不足1小时：00：00
     * 超过1小时：00：00：00
     *
     * @param timeSec
     * @return
     */
    private String IntToTime(int timeSec) {
        if (timeSec > 3600) {
            timeSec = timeSec * 1000;
            formatter = new SimpleDateFormat("HH:mm:ss");//初始化Formatter的转换格式。
            formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        } else {
            timeSec = timeSec * 1000;
            formatter = new SimpleDateFormat("mm:ss");//初始化Formatter的转换格式。
            formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        }
        return formatter.format(timeSec);
    }

    private String IntToTime2(int timeSec) {
        if (timeSec > 3600000) {
            formatter = new SimpleDateFormat("HH:mm:ss");//初始化Formatter的转换格式。
            formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        } else {
            formatter = new SimpleDateFormat("mm:ss");//初始化Formatter的转换格式。
            formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        }
        return formatter.format(timeSec);
    }

    private String IntToTime3(int timeSec) {
        int currentTime = Math
                .round(mediaPlayer.getCurrentPosition() / 1000);

        return String.format("%s%02d:%02d", "",
                currentTime / 60, currentTime % 60);
    }

}
