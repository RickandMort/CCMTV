package com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.enums.ActionEnum;
import com.linlic.ccmtv.yx.util.TimerUtils;
import com.linlic.ccmtv.yx.utils.CameraUtils;
import com.linlic.ccmtv.yx.utils.DateUtil;
import com.linlic.ccmtv.yx.utils.NetUtil;
import com.linlic.ccmtv.yx.utils.TimeUtil;

import java.io.File;


public class VideoRecordingActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout video_ll_time;
    private TextView video_tv_time;
    private ImageView video_iv_point;
    private TextView tv_remake;
    private TextView tv_use_video;
    private static final String TAG = "PRETTY_LOGGER";
    private SurfaceView surfaceView;
    private CameraUtils cameraUtils;
    private String path, name;
    private ImageView btn;
    private ImageView camera;
    private ImageView change;
    int x = 0;

    // 开始计时
    int seconds = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1 && isRecording) {
                seconds++;
                video_tv_time.setText(TimerUtils.getTime(seconds));
                startTime();
            }
        }
    };

    private Runnable task; //计时任务
    private boolean isRecording = false; // 正在录制？

    private NetworkChangeReceiver networkChangeRecever;

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tv_remake:
                // 重拍
                seconds = 0;
                video_tv_time.setText(TimerUtils.getTime(seconds));
                name = DateUtil.getCurrDate("yyyy-MM-dd_hh-mm-ss");
                btn.setVisibility(View.VISIBLE);
                btn.setImageResource(R.mipmap.recordvideo_stop);
                tv_remake.setVisibility(View.GONE);
                tv_use_video.setVisibility(View.GONE);

                isRecording = true;
                cameraUtils.startRecord(path, name);
                btn.setImageResource(R.mipmap.recordvideo_stop);
                x = 1;

                video_ll_time.setVisibility(View.VISIBLE);
                video_tv_time.setVisibility(View.VISIBLE);
                video_tv_time.setText(TimerUtils.getTime(seconds));
                startTime();
                break;

            case R.id.tv_use_video:
                // 使用视频
                Intent intent = new Intent();
                intent.setAction(ActionEnum.EVENT_DETAIL_SELECT_VIDEO.getAction());
                intent.putExtra(Event_Details.VIDEO_PATH_KEY, path + File.separator + name + ".mp4");
                VideoRecordingActivity.this.sendBroadcast(intent);
                finish();
                break;
        }
    }

    public class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connecttivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);//使用getSystemService得到ConnectivityManager实例
            NetworkInfo networkInfo = connecttivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                // 这里需要判断，虽然连接了，但是网络仍然不可访问
                if (!NetUtil.isNetworkOnline()) {
                    Toast.makeText(context, R.string.post_hint4, Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("PRETTY_LOGGER", "onReceive() returned: " + "当前网络可以用");
                }
            } else {
                Toast.makeText(context, R.string.post_hint4, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_video_recording);

        // 注册一个网络状态监听的广播
        //@author eric
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");//添加广播
        networkChangeRecever = new NetworkChangeReceiver();
        registerReceiver(networkChangeRecever, intentFilter);

        tv_remake = findViewById(R.id.tv_remake);
        tv_use_video = findViewById(R.id.tv_use_video);
        video_ll_time = findViewById(R.id.video_ll_time);
        video_tv_time = findViewById(R.id.video_tv_time);
        video_iv_point = findViewById(R.id.video_iv_point);
        btn = (ImageView) findViewById(R.id.btn);
        camera = (ImageView) findViewById(R.id.camera);
        change = (ImageView) findViewById(R.id.change);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);

        tv_remake.setOnClickListener(this);
        tv_use_video.setOnClickListener(this);

        cameraUtils = new CameraUtils();
        cameraUtils.create(surfaceView, this);
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "ccmtvCache" + File.separator + "videoDir";

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (x == 0) {
                    // cameraUtils.changeCamera();
                    name = DateUtil.getCurrDate("yyyy-MM-dd_hh-mm-ss");
                    isRecording = true;
                    cameraUtils.startRecord(path, name);
                    btn.setImageResource(R.mipmap.recordvideo_stop);
                    x = 1;

                    video_ll_time.setVisibility(View.VISIBLE);
                    video_tv_time.setText(TimerUtils.getTime(seconds));
                    startTime();
                } else if (x == 1) {
                    isRecording = false;
                    cameraUtils.stopRecord();
                    btn.setImageResource(R.mipmap.recordvideo_stop);
                    x = 0;
                    video_ll_time.setVisibility(View.GONE);
                    seconds = 0;
                    video_tv_time.setText(TimerUtils.getTime(seconds));

                    tv_remake.setVisibility(View.VISIBLE);
                    tv_use_video.setVisibility(View.VISIBLE);
                    video_tv_time.setVisibility(View.GONE);
                    btn.setVisibility(View.GONE);
                }
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraUtils.takePicture(path, "name.png");
            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraUtils.changeCamera();
            }
        });
    }

    private void startTime() {
        task = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    if (isRecording)
                        mHandler.sendEmptyMessage(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(task).start();
    }

    //实现图片闪烁效果
    private void setFlickerAnimation(ImageView iv_chat_head) {
        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(1000); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); //
        iv_chat_head.setAnimation(animation);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: ");

        seconds = 0;
        video_tv_time.setText(TimerUtils.getTime(seconds));
        video_tv_time.setVisibility(View.VISIBLE);
        isRecording = false;
        x = 0;
        btn.setVisibility(View.VISIBLE);
        btn.setImageResource(R.mipmap.recordvideo_start);
        cameraUtils.stop();
        cameraUtils.destroy();
        mHandler.removeCallbacks(task);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
        cameraUtils.destroy();
        if (networkChangeRecever != null)
            this.unregisterReceiver(networkChangeRecever);
    }

    @Override
    public void onBackPressed() {
        // 保存确认上传吗
        AlertDialog dialog = new AlertDialog.Builder(VideoRecordingActivity.this)
                .setTitle("温馨提示")
                .setMessage("确定要退出吗?")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        VideoRecordingActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .create();
        dialog.show();
    }
}
