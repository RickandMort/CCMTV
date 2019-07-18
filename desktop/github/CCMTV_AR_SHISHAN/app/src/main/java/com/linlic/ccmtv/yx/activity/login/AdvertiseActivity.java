package com.linlic.ccmtv.yx.activity.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.DownLoadImageUtil;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * name：广告页
 * author：Larry
 * data：2016/7/25.
 */
public class AdvertiseActivity extends BaseActivity {
    Uri uri = null;
    private ImageView iv_adv;
    private String adpicurl, advertisementurl, title;
    private int isstartup = 0;
    Context context;
    private int sleepTime = 4;//页面停留时间，单位：秒
    private TextView tv_indicator;
    private Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adv);
        context = this;
        adpicurl = getIntent().getStringExtra("adpicurl");
        advertisementurl = getIntent().getStringExtra("advertisementurl");
        title = getIntent().getStringExtra("title");
        isstartup = Integer.parseInt(getIntent().getStringExtra("isstartup"));
        iv_adv = (ImageView) findViewById(R.id.iv_adv);
        tv_indicator = (TextView) findViewById(R.id.tv_indicator);
        iv_adv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isstartup == 1) {
                    Intent intent = null;
                    intent = new Intent(AdvertiseActivity.this, SplashWebActivity.class);
                    intent.putExtra("title", title);
                    intent.putExtra("aid", advertisementurl);
                    startActivity(intent);
                    timer.cancel();
                    finish();
                }
            }
        });
        LinearLayout layout_indicator = (LinearLayout) findViewById(R.id.layout_indicator);
        layout_indicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(AdvertiseActivity.this, MainActivity.class));
                timer.cancel();
                finish();
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                final File filepath = new File(URLConfig.ccmtvapp_basesdcardpath);
                try {
                    final Uri uri = DownLoadImageUtil.getImageURI(adpicurl, filepath);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Bitmap bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                                iv_adv.setImageBitmap(bmp);
                                timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                tv_indicator.setText(String.valueOf(sleepTime) + "s");
                                            }
                                        });
                                        sleepTime = sleepTime - 1;
                                        if (sleepTime == 0) {
                                            timer.cancel();
                                            startActivity(new Intent(AdvertiseActivity.this, MainActivity.class));
                                            finish();
                                        }
                                    }
                                }, 0, 1000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
