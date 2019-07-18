package com.linlic.ccmtv.yx.activity.login;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.login.service.DownloadARImgService;
import com.linlic.ccmtv.yx.activity.my.book.Video_book;
import com.linlic.ccmtv.yx.activity.my.dialog.MustUpdateCustomDialog2;
import com.linlic.ccmtv.yx.activity.my.dialog.UpdateCustomDialog;
import com.linlic.ccmtv.yx.activity.my.newDownload.DownloadFinishActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.DownLoadImageUtil;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.SplashJumpView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * name：启动页
 * <p/>
 * author: Mr.song 时间：2016-2-18 上午11:17:01
 *
 * @author Administrator
 */
public class WelcomeActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    private Button btnNow;
    private ViewPager viewPager;
    private GuideAdapter adapter;
    private List<Fragment> fragments;
    private int[] images = {R.mipmap.welcomepage1,R.mipmap.welcomepage2,R.mipmap.welcomepage3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        btnNow = findViewById(R.id.id_btn_now);
        initImages();
    }

    private void initImages(){
        //设置每一张图片都填充窗口
        viewPager = (ViewPager) findViewById(R.id.guide_viewpager);
        fragments = new ArrayList<>();
        for(int i=0;i<images.length;i++){
            GuideFragment guide = new GuideFragment(images[i]);
            fragments.add(guide);
        }
        adapter = new GuideAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == images.length - 1) {
            btnNow.setVisibility(View.VISIBLE);
        } else {
            btnNow.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void toMainActivity(View view) {
        Intent intent =  new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

