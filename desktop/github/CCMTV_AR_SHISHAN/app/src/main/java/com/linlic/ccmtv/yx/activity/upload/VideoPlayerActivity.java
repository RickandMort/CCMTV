package com.linlic.ccmtv.yx.activity.upload;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.db.MyDbUtils;
import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.NiceVideoPlayerManager;
import com.xiao.nicevideoplayer.TxVideoPlayerController;

/**
 * name：视频播放
 * author：Niklaus
 * data：2017/11/16
 */
public class VideoPlayerActivity extends AppCompatActivity {
    private String mVideoPath;
    private String aid;
    private Context context;
    private NiceVideoPlayer mNiceVideoPlayer;
    private String videoTitle="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        context = this;
        //停止播放音频
        MainActivity.stopFloatingView(context);


        Intent intent = getIntent();
        mVideoPath = intent.getStringExtra("videoPath");
        videoTitle = intent.getStringExtra("videoTitle");
        aid = intent.getStringExtra("aid");


        String intentAction = intent.getAction();
        if (!TextUtils.isEmpty(intentAction)
                && intentAction.equals(Intent.ACTION_VIEW)) {
            mVideoPath = intent.getDataString();
        }


        init();
    }
    private void init() {
        mNiceVideoPlayer = (NiceVideoPlayer) findViewById(R.id.nicevideoplayer);
        mNiceVideoPlayer.setPlayerType(NiceVideoPlayer.TYPE_IJK); // or NiceVideoPlayer.TYPE_NATIVE
        mNiceVideoPlayer.setUp(mVideoPath, null);

        TxVideoPlayerController controller = new TxVideoPlayerController(this);
        controller.setTitle(videoTitle);
        controller.setImage(R.mipmap.ys);
        mNiceVideoPlayer.setController(controller);
        mNiceVideoPlayer.start();
        mNiceVideoPlayer.enterFullScreen();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
//        enterUrl = "http://www.ccmtv.cn";
        super.onPause();
    }


    @Override
    protected void onStop() {
        super.onStop();
        // 在onStop时释放掉播放器
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
    }
    @Override
    public void onBackPressed() {
        // 在全屏或者小窗口时按返回键要先退出全屏或小窗口，
        // 所以在Activity中onBackPress要交给NiceVideoPlayer先处理。

        if (NiceVideoPlayerManager.instance().onBackPressd()) return;
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (NiceVideoPlayerManager.instance().onBackPressd()) return super.onKeyDown(keyCode, event);
            // 在onStop时释放掉播放器
            Log.d("mason"," mVideoPath       " + aid);
            Log.d("mason","CurrentPosition   "+mNiceVideoPlayer.getCurrentPosition()/1000);
            MyDbUtils.updateLocalRecordVideo(context, aid, mNiceVideoPlayer.getCurrentPosition()/1000);
            NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
