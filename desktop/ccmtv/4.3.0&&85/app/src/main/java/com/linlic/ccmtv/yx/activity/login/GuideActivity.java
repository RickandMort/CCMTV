package com.linlic.ccmtv.yx.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import java.util.ArrayList;

/**
 * name：引导页
 * author：Larry
 * data：2016/6/15.
 */

public class GuideActivity extends BaseActivity implements View.OnClickListener,
        ViewPager.OnPageChangeListener {
    private Context context;
    private int[] images;
    private ViewPager viewpager;
    private ArrayList<View> views;
    private LinearLayout indicatorLayout;
    private ImageView[] indicators = null;
    private PagerAdapter pagerAdapter;
    private Button startButton, regist_Button, login_Button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);
        context = this;
        // 添加引导页图片
        images = new int[]{ R.mipmap.app_icon_noyuan};
/*        images = new int[]{R.mipmap.   , R.mipmap.lunch2,
                R.mipmap.lunch3, R.mipmap.lunch4, R.mipmap.lunch5};*/
        initview();
    }

    // 初始化试图
    private void initview() {
        // 实例化视图控件
        viewpager = (ViewPager) findViewById(R.id.viewpage);
        startButton = (Button) findViewById(R.id.start_Button);
        regist_Button = (Button) findViewById(R.id.regist_Button);
        login_Button = (Button) findViewById(R.id.login_Button);
        startButton.setOnClickListener(this);
        regist_Button.setOnClickListener(this);
        login_Button.setOnClickListener(this);
        indicatorLayout = (LinearLayout) findViewById(R.id.indicator); // 定义指示器数组大小
        views = new ArrayList<View>();
        indicators = new ImageView[images.length]; // 定义指示器数组大小
        for (int i = 0; i < images.length; i++) {
            // 循环加入图片
            ImageView imageView = new ImageView(context);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setBackgroundResource(images[i]);
            views.add(imageView);
            // 循环加入指示器
            indicators[i] = new ImageView(context);

            indicators[i].setBackgroundResource(R.mipmap.dotc);
            if (i == 0) {
                indicators[i].setBackgroundResource(R.mipmap.dotn);
            }
            indicatorLayout.addView(indicators[i]);
        }

        pagerAdapter = new BasePagerAdapter(views);
        viewpager.setAdapter(pagerAdapter); // 设置适配器
        viewpager.setOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_Button:
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
                break;
            case R.id.regist_Button:
                startActivity(new Intent(GuideActivity.this, RegisterActivity.class));
                break;
            case R.id.login_Button:
                startActivity(new Intent(GuideActivity.this, LoginActivity.class));
                break;
            default:
                break;
        }
        GuideActivity.this.finish();
    }


    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    // 监听viewpage
    @Override
    public void onPageSelected(int arg0) {
        // 显示最后一个图片时显示按钮
        if (arg0 == indicators.length - 1) {
            SharedPreferencesTools.saveIsFrist(GuideActivity.this, false);
            startButton.setVisibility(View.VISIBLE);
            login_Button.setVisibility(View.VISIBLE);
            regist_Button.setVisibility(View.VISIBLE);
        } else {
            startButton.setVisibility(View.INVISIBLE);
            login_Button.setVisibility(View.INVISIBLE);
            regist_Button.setVisibility(View.INVISIBLE);
        }
        // 更改指示器图片
        for (int i = 0; i < indicators.length; i++) {
            indicators[arg0].setBackgroundResource(R.mipmap.dotc);
            if (arg0 != i) {
                indicators[i].setBackgroundResource(R.mipmap.dotn);
            }
        }
    }


}
