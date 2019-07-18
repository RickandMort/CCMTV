package com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.base.LocalApplication;
import com.linlic.ccmtv.yx.activity.home.VideoService;
import com.linlic.ccmtv.yx.activity.home.util.LocalConstants;
import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.NiceVideoPlayerManager;
import com.xiao.nicevideoplayer.TxVideoPlayerController;

import cn.cc.android.sdk.view.VideoView;

public class DetailVideoPlayActivity extends AppCompatActivity {

    private String mVideoUrl = "";
    // private String url = "http://192.168.30.201:8083/upload_files/teaRecorder/Video.mp4";
    private String url = " http://192.168.30.201:8083/upload_files/teaRecorder/2019-07-04_01:50:57.mp4";

    // private String mVideoUrl;
    NiceVideoPlayer mNiceVideoPlayer;

    public static final String VIDEO_PATH_KEY = "VIDEO_PATH_KEY";
    public static final String VIDEO_HTTP_KEY = "VIDEO_HTTP_KEY";

    private String video_path;
    private String video_http;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_video_play);

        video_path = getIntent().getStringExtra(VIDEO_PATH_KEY);
        video_http = getIntent().getStringExtra(VIDEO_HTTP_KEY);

        Log.e("PRETTY_LOGGER", "onCreate() returned: " + video_http);
        Log.e("PRETTY_LOGGER", "onCreate() returned: " + video_path);

        if (!TextUtils.isEmpty(video_http)) {
            mVideoUrl = video_http + video_path;
        }

        mNiceVideoPlayer = findViewById(R.id.video_player);

        mNiceVideoPlayer.setPlayerType(NiceVideoPlayer.TYPE_IJK); // or NiceVideoPlayer.TYPE_NATIVE
        mNiceVideoPlayer.setUp(mVideoUrl, null);

        TxVideoPlayerController controller = new TxVideoPlayerController(this);
        controller.setTitle("");
        controller.setImage(R.mipmap.ys);
        mNiceVideoPlayer.setController(controller);
        mNiceVideoPlayer.start();
        mNiceVideoPlayer.enterFullScreen();
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
        //   if (NiceVideoPlayerManager.instance().onBackPressd()) return;
        super.onBackPressed();
    }
}
