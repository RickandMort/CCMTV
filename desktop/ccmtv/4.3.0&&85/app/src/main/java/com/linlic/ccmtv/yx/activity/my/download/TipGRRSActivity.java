package com.linlic.ccmtv.yx.activity.my.download;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.db.MyDbUtils;
import com.linlic.ccmtv.yx.activity.entity.DbDownloadVideo;

/**
 * name：提示流量
 * author：Larry
 * data：2017/2/23.
 */
public class TipGRRSActivity extends BaseActivity implements View.OnClickListener {
    Button btn_goon, btn_stop;
    String videoId;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_tip);
        context = this;
        getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        findId();
        initData();
    }

    private void initData() {
        videoId = getIntent().getStringExtra("videoId");
    }

    public void findId() {
        btn_goon = (Button) findViewById(R.id.btn_goon);
        btn_stop = (Button) findViewById(R.id.btn_stop);
        btn_goon.setOnClickListener(this);
        btn_stop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_goon:
                Intent i = new Intent();            //100  会继续下载
                i.setAction("download_stop");
                i.putExtra("videoId", videoId);
                DbDownloadVideo video = MyDbUtils.findVideoMsg(this, videoId);
                //i.putExtra("videoState", video.getState());
//                Log.i("videovideo", "video.getState()" + video.getState());
                i.putExtra("videoState", "100");
                sendBroadcast(i);

                this.finish();
                break;
            case R.id.btn_stop:
                Intent i1 = new Intent();           //18 会停止下载
                i1.setAction("download_stop");
                i1.putExtra("videoId", videoId);
                DbDownloadVideo videos = MyDbUtils.findVideoMsg(this, videoId);
                //i.putExtra("videoState", video.getState());
//                Log.i("videovideo", "video.getState()" + videos.getState());
                i1.putExtra("videoState", "18");
                sendBroadcast(i1);
                this.finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn";
        super.onPause();
    }

}
