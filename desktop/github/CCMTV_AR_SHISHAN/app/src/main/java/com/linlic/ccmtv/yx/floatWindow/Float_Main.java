package com.linlic.ccmtv.yx.floatWindow;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewDebug;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.floatWindow.service.FloatViewService;
import com.linlic.ccmtv.yx.floatWindow.widget.FloatLayout;
import com.linlic.ccmtv.yx.utils.LogUtil;

import java.text.SimpleDateFormat;
import java.util.TimeZone;


/**
 * Created by Administrator on 2018/2/1.
 */

public class Float_Main extends BaseActivity {

    private TextView title, videoClass, experts, time_start, totalTime;
    private ImageView float_img, close_icon, quick_retreat, speed, play;
    private Context context;
    private int i = 0;
    private boolean bool = true;
    private Thread thread;
    private ProgressBar bar;
    //纯音频 服务
    private FloatViewService mFloatViewService;
    /**
     * 标识按钮状态：是否在播放
     */
    public boolean isPlay = false;
    /**
     * 播放/暂停 按钮点击 ID
     */
    public final static int BUTTON_PALY_ID = 1;
    public final static int BUTTON_PALY_ID2 = 2;
    private final int NOTIFICATION_ID = 0xa01;
    private final int REQUEST_CODE = 0xb01;
    public final static String INTENT_BUTTONID_TAG = "ButtonId";
    /**
     * 通知栏按钮点击事件对应的ACTION（标识广播）
     */
    public final static String ACTION_START = "START";
    public final static String ACTION_PAUSE = "PAUSE";
//    private ButtonBroadcastReceiver broadcastReceiver;
    private SimpleDateFormat formatter;
    private static final int START = 0;//开始计时消息标志，下面用到
    private static final int STOP = 1;//停止计时消息标志，下面用到

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.float_main);
        context = this;
        findID();
        init();
        startFloatService();
        handler.sendEmptyMessage(START);
    }

    public void findID() {
        title = (TextView) findViewById(R.id.title);
        totalTime = (TextView) findViewById(R.id.totalTime);
        time_start = (TextView) findViewById(R.id.time_start);
        videoClass = (TextView) findViewById(R.id.videoClass);
        experts = (TextView) findViewById(R.id.experts);
        float_img = (ImageView) findViewById(R.id.float_img);
        close_icon = (ImageView) findViewById(R.id.close_icon);
        quick_retreat = (ImageView) findViewById(R.id.quick_retreat);
        play = (ImageView) findViewById(R.id.play);
        speed = (ImageView) findViewById(R.id.speed);
        bar = (ProgressBar) findViewById(R.id.bar);
    }

    public void init() {
        title.setText(FloatLayout.title);
        videoClass.setText(FloatLayout.videoClass);
        experts.setText(FloatLayout.experts);
        com.linlic.ccmtv.yx.util.ImageLoader.load(context, FloatLayout.imgUrl, float_img);
        close_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFloatingView();
                finish();
            }
        });
        quick_retreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动服务，播放音乐
                Intent intent = new Intent(context, FloatViewService.class);
                intent.putExtra("type", FloatLayout.QUICK_RETREAT);
                context.startService(intent);
            }
        });
        speed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动服务，播放音乐
                Intent intent = new Intent(context, FloatViewService.class);
                intent.putExtra("type", FloatLayout.SPEED);
                context.startService(intent);
            }
        });
        if (FloatLayout.isplay) {
            play.setImageResource(R.mipmap.float_16);
        } else {
            play.setImageResource(R.mipmap.float_07);
        }
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FloatLayout.isplay) {
                    play.setImageResource(R.mipmap.float_07);
                    //启动服务，播放音乐
                    Intent intent = new Intent(context, FloatViewService.class);
                    intent.putExtra("type", FloatLayout.PAUSE_MUSIC);
                    context.startService(intent);
                } else {
                    play.setImageResource(R.mipmap.float_16);
                    //启动服务，播放音乐
                    Intent intent = new Intent(context, FloatViewService.class);
                    intent.putExtra("type", FloatLayout.PLAT_MUSIC);
                    context.startService(intent);
                    hideFloatingView();
                }
                FloatViewService.notificationManager.notify(NOTIFICATION_ID, FloatViewService.notification);
            }
        });
        totalTime.setText(IntToTime2(FloatLayout.totalTime));
        LogUtil.e("我要看看数据", FloatLayout.TotalTime);
    }

    /******************************************悬浮窗start**********************************************/
    public void startFloatService() {
        try {
            Intent intent = new Intent(this, FloatViewService.class);
            context.startService(intent);
            bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示悬浮图标
     */
    public void showFloatingView() {
        if (mFloatViewService != null) {
            mFloatViewService.showFloat();
        }
    }

    /**
     * 隐藏悬浮图标
     */
    public void hideFloatingView() {
        if (mFloatViewService != null) {
            mFloatViewService.hideFloat();
//            Log.e("隐藏", "Float_Main");
        }
    }

    /**
     * 释放PJSDK数据
     */
    public void destroy() {
        try {
            stopService(new Intent(this, FloatViewService.class));
            unbindService(mServiceConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接到Service
     */
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mFloatViewService = ((FloatViewService.FloatViewServiceBinder) iBinder).getService();
            hideFloatingView();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mFloatViewService = null;
        }
    };

    /******************************************悬浮窗end**********************************************/
// handler类接收数据
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case START:
                    time_start.setText( FloatLayout.CurrentTime);
                    bar.setMax(FloatLayout.totalTime);
//                //设定初始化已经增长到的进度
//                bar.incrementProgressBy(FloatLayout.CurrentTime/FloatLayout.TotalTime);
//                //进度条是明确显示进度的,false表示明确显示进度,反之.
//                bar.setIndeterminate(false);
                    bar.setProgress(FloatLayout.mediaPlayCurrentTime);
                    handler.sendEmptyMessageDelayed(START,1000);
                    if (!FloatLayout.isplay) {
                        play.setImageResource(R.mipmap.float_07);
                    } else {
                        play.setImageResource(R.mipmap.float_16);
                    }
                    System.out.println("receive....");
                    break;
                case STOP:
                    break;
            }
        }
    };

    @Override
    public void finish() {
        bool = false;
        super.finish();
    }

    /********************************************************
     * 对时间的格式化
     * @param time
     * @return
     */
    public static String formatTime(int time) {
        if (time / 1000 % 60 < 10) {
            return time / 1000 / 60 + ":0" + time / 1000 % 60;
        } else {
            return time / 1000 / 60 + ":" + time / 1000 % 60;
        }
    }

    // 捕获返回键的方法1
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // 按下BACK，同时没有重复
            showFloatingView();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    // 捕获返回键的方法2
    @Override
    public void onBackPressed() {
        showFloatingView();
        finish();
    }

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

    @Override
    protected void onResume() {
        super.onResume();

        //启动服务，播放音乐
        Intent intent = new Intent(context, FloatViewService.class);
        intent.putExtra("type", FloatLayout.HIDE);
        context.startService(intent);
    }
}
