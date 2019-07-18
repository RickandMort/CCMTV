package com.linlic.ccmtv.yx.activity.home.yxzbjrrom;


import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.home.adapter.MyFragmentPagerAdapter;
import com.linlic.ccmtv.yx.kzbf.utils.SkyVisitUtils;

import java.util.ArrayList;

/**
 * name：医学直播间改版
 * author：Larry
 * data：2017/2/14.
 */
public class MedicalLiveRoomActivity extends FragmentActivity {
    private static final String TAG = "MedicalLiveRoomActivity";
    Context context;
    //用户统计
    private String entertime, leavetime;
    public static String enterUrl = "http://www.ccmtv.cn";
    private ViewPager mPager; // http://blog.csdn.net/lizhenmingdirk/article/details/13631813
    private ArrayList<Fragment> fragmentsList; // http://www.cnblogs.com/mengdd/archive/2013/01/08/2851368.html
    private ImageView ivBottomLine; // 那个白杠
    private TextView tv_userlive, tv_platform, activity_title_name;
    private int currIndex = 0; // ViewPager监听器里Fragment标号
    private int bottomLineWidth;// 白杠宽度
    private int offset = 0;
    private int position_one;
    private int position_two;
    private Resources resources;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_medicallive);
        context = this;
        resources = getResources();
        // 初始化操作
        InitWidth();
        InitTextView();
        InitViewPager();
    }

    private void InitTextView() {
        activity_title_name = (TextView) findViewById(R.id.activity_title_name);
        activity_title_name.setText("医学直播间");
        tv_userlive = (TextView) findViewById(R.id.tv_userlive);
        tv_platform = (TextView) findViewById(R.id.tv_platform);
        tv_userlive.setOnClickListener(new MyOnClickListener(0));
        tv_platform.setOnClickListener(new MyOnClickListener(1));
    }

    private void InitViewPager() {
        mPager = (ViewPager) findViewById(R.id.vPager);
        fragmentsList = new ArrayList<Fragment>();
        UserLiveFragment userLiveFragment = new UserLiveFragment();
        PlatformFragment platformFragment = new PlatformFragment();
        fragmentsList.add(userLiveFragment);
        fragmentsList.add(platformFragment);
        mPager.setAdapter(new MyFragmentPagerAdapter(
                getSupportFragmentManager(), fragmentsList)); // 给ViewPager设置适配器
        mPager.setCurrentItem(0); // 设置当前显示标签页为第一页
        mPager.setOnPageChangeListener(new MyOnPageChangeListener()); // 页面变化时的监听器
    }

    private void InitWidth() { // 初始化移动游标
        DisplayMetrics dm = new DisplayMetrics(); // http://blog.csdn.net/zhangqijie001/article/details/5894872
        // http://blog.csdn.net/java2009cgh/article/details/8182817
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels; // 屏幕宽度像素值
        ivBottomLine = (ImageView) findViewById(R.id.iv_bottom_line);
        ViewGroup.LayoutParams params = ivBottomLine.getLayoutParams();
        //ivBottomLine.getLayoutParams().height
        params.height = 10;
        params.width = screenW / 2;
        ivBottomLine.setLayoutParams(params);
        bottomLineWidth = ivBottomLine.getLayoutParams().width; // 那条白杠的宽度
        offset = (int) ((screenW / 2.0 - bottomLineWidth) / 2); // offset就是代表一开始白杠距离左边的距离，数学好的自己领悟，说不清楚
        position_one = (int) (screenW / 2.0); // 屏幕的1/4，为以后白杠移动距离做铺垫
        position_two = position_one * 2;
        // position_three = position_one * 3;
    }

    public class MyOnClickListener implements View.OnClickListener { // TextView的监听事件
        private int index = 0; // Fragment标号

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(index); // 跳转到对应标号的Fragmnent
        }
    }

    public class MyOnPageChangeListener implements OnPageChangeListener { // ViewPager监听器

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null; // 声明动画对象
            switch (arg0) {
                case 0:
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(position_one, 0, 0, 0); // http://www.jb51.net/article/32339.htm
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(position_two, 0, 0, 0);
                    }
                    tv_userlive.setTextColor(resources.getColor(R.color.text_blue));
                    tv_platform.setTextColor(resources.getColor(R.color.text_gray));
                    break;
                case 1:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(0, position_one, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(position_two,
                                position_one, 0, 0);
                    }
                    tv_userlive.setTextColor(resources.getColor(R.color.text_gray));
                    tv_platform.setTextColor(resources.getColor(R.color.text_blue));
                    break;
            }
            currIndex = arg0;
            animation.setFillAfter(true); // 停止动画
            animation.setDuration(300);
            ivBottomLine.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    @Override
    protected void onResume() {
        //保存进入的日期
        entertime = SkyVisitUtils.getCurrentTime();
        super.onResume();
    }

    @Override
    protected void onPause() {
        //保存退出的日期
        leavetime = SkyVisitUtils.getCurrentTime();
        //保存日期到服务器
        SkyVisitUtils.OnlineStatistical(context, enterUrl, entertime, leavetime);
        super.onPause();
    }

    public void back(View view) {
        MedicalLiveRoomActivity.this.finish();
    }
}


