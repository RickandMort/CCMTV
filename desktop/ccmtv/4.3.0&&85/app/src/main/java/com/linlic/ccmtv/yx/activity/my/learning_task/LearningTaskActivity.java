package com.linlic.ccmtv.yx.activity.my.learning_task;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.Popular_search2;
import com.linlic.ccmtv.yx.kzbf.utils.SkyVisitUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Nillaus
 * Date:2017年10月16日
 * Description:任务列表界面
 */
public class LearningTaskActivity extends FragmentActivity implements View.OnClickListener {

    private TextView item_ongoing, item_completed, item_expired, activity_title_name;
    private ViewPager vp;
    private int mPosition;
    private Context mContext;
    private SharedPreferencesTools sp;
    private OngoingFragment ongoingFragment;
    private CompletedFragment completedFragment;
    private ExpiredFragment expiredFragment;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private FragmentAdapter mFragmentAdapter;
    private View line1, line2, line3;
    private int requestCode;
    public static boolean istop = true;
    public static String keyword = "";
    public static String poptype = "";
    public static String popqualified = "";
    //用户统计
    private String entertime, leavetime;
    public static String enterUrl = "http://www.ccmtv.cn/Member/Index.html";
    private LinearLayout arrow_search, ll_screen;
    private TopMiddlePopup middlePopup;
    public static int screenW, screenH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_learning_task);
        mContext = this;

        registerBoradcastReceiver();

        getScreenPixels();
        initViews();

        mFragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), mFragmentList);
        vp.setOffscreenPageLimit(3);//ViewPager的缓存为3帧
        vp.setAdapter(mFragmentAdapter);
        vp.setCurrentItem(0);//初始设置ViewPager选中第一帧
        item_ongoing.setTextColor(Color.parseColor("#66CDAA"));

        //ViewPager的监听事件
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                /*此方法在页面被选中时调用*/
                mPosition = position;
                changeTextColor(position);
                LearningTaskActivity.poptype = "";
                LearningTaskActivity.popqualified = "";
                mFragmentList.get(vp.getCurrentItem()).onResume();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                /*此方法是在状态改变的时候调用，其中arg0这个参数有三种状态（0，1，2）。
                arg0 ==1的时辰默示正在滑动，
                arg0==2的时辰默示滑动完毕了，
                arg0==0的时辰默示什么都没做。*/
            }
        });
    }



    /**
     * 初始化布局View
     */
    private void initViews() {
        item_ongoing = (TextView) findViewById(R.id.item_ongoing);
        item_completed = (TextView) findViewById(R.id.item_completed);
        item_expired = (TextView) findViewById(R.id.item_expired);
        arrow_search = (LinearLayout) findViewById(R.id.arrow_search);
        activity_title_name = (TextView) findViewById(R.id.activity_title_name);

        ll_screen = (LinearLayout) findViewById(R.id.ll_screen);
        /*screen_yes = (TextView) findViewById(R.id.screen_yes);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_qualified = (TextView) findViewById(R.id.tv_qualified);
        ll_type = (LinearLayout) findViewById(R.id.ll_type);
        ll_qualified = (LinearLayout) findViewById(R.id.ll_qualified);*/

        line1 = findViewById(R.id.v_line1);
        line2 = findViewById(R.id.v_line2);
        line3 = findViewById(R.id.v_line3);

        item_ongoing.setOnClickListener(this);
        item_completed.setOnClickListener(this);
        item_expired.setOnClickListener(this);

        vp = (ViewPager) findViewById(R.id.mainViewPager);
        ongoingFragment = new OngoingFragment();
        completedFragment = new CompletedFragment();
        expiredFragment = new ExpiredFragment();
        //给FragmentList添加数据
        mFragmentList.add(ongoingFragment);
        mFragmentList.add(completedFragment);
        mFragmentList.add(expiredFragment);
    }

    /**
     * 点击底部Text 动态修改ViewPager的内容
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_ongoing:
                vp.setCurrentItem(0, true);
                break;
            case R.id.item_completed:
                vp.setCurrentItem(1, true);
                break;
            case R.id.item_expired:
                vp.setCurrentItem(2, true);
                break;
        }
    }

    /**
     * 搜索按钮点击事件
     */
    public void ss(View view) {
        Intent intent = new Intent(LearningTaskActivity.this, Popular_search2.class);
        // 请求码的值随便设置，但必须>=0
        requestCode = 0;
        LearningTaskActivity.istop = false;
        startActivityForResult(intent, requestCode);
    }

    /**
     * 筛选按钮点击事件
     *
     * @param view
     */
    public void screen(View view) {
//        LearningTaskActivity.poptype = "";
//        LearningTaskActivity.popqualified = "";
        setPopup(mPosition);
        middlePopup.show(ll_screen);
        mFragmentList.get(vp.getCurrentItem()).onPause();
    }

    public void search(View view) {
        LearningTaskActivity.keyword = "";
        LearningTaskActivity.istop = true;
        mFragmentList.get(vp.getCurrentItem()).onResume();
        view.setVisibility(View.GONE);
        activity_title_name.setText("学习任务");
    }

    public class FragmentAdapter extends FragmentPagerAdapter {

        List<Fragment> fragmentList = new ArrayList<Fragment>();

        public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

    }

    /*
     *由ViewPager的滑动修改底部导航Text的颜色
     */
    private void changeTextColor(int position) {
        if (position == 0) {
            //字体颜色
            item_ongoing.setTextColor(Color.parseColor("#3797FB"));
            item_completed.setTextColor(Color.parseColor("#000000"));
            item_expired.setTextColor(Color.parseColor("#000000"));
            //滑块
            line1.setVisibility(View.VISIBLE);
            line2.setVisibility(View.INVISIBLE);
            line3.setVisibility(View.INVISIBLE);
        } else if (position == 1) {
            item_completed.setTextColor(Color.parseColor("#3797FB"));
            item_ongoing.setTextColor(Color.parseColor("#000000"));
            item_expired.setTextColor(Color.parseColor("#000000"));
            line1.setVisibility(View.INVISIBLE);
            line2.setVisibility(View.VISIBLE);
            line3.setVisibility(View.INVISIBLE);
        } else if (position == 2) {
            item_expired.setTextColor(Color.parseColor("#3797FB"));
            item_ongoing.setTextColor(Color.parseColor("#000000"));
            item_completed.setTextColor(Color.parseColor("#000000"));
            line1.setVisibility(View.INVISIBLE);
            line2.setVisibility(View.INVISIBLE);
            line3.setVisibility(View.VISIBLE);
        }
    }

    public void back(View view) {
        sp.saveType(mContext, "1");
        sp.saveQualified(mContext, "1");
        finish();
    }

    @Override
    public void finish() {
        LearningTaskActivity.keyword = "";
        LearningTaskActivity.poptype = "";
        LearningTaskActivity.popqualified = "";
        LearningTaskActivity.istop = true;
        super.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.e("requestCode", requestCode + "");
//        Log.e("resultCode", resultCode + "");
//        Log.e("data", data.getExtras().toString());
        // 根据上面发送过去的请求吗来区别
        switch (requestCode) {
            case 0:
                LearningTaskActivity.keyword = data.getStringExtra("keyword");

                if (LearningTaskActivity.keyword.length() > 0) {
                    arrow_search.setVisibility(View.VISIBLE);
                    activity_title_name.setText("搜索");
                } else {
                    arrow_search.setVisibility(View.GONE);
                    activity_title_name.setText("学习任务");
                }
                LearningTaskActivity.istop = true;
//                mFragmentList.get(vp.getCurrentItem()).onResume();
                break;
            default:
                break;
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
        //保存推出的日期
        leavetime = SkyVisitUtils.getCurrentTime();
        //保存日期到服务器
        SkyVisitUtils.OnlineStatistical(mContext, enterUrl, entertime, leavetime);
        super.onPause();
    }

    /**
     * 设置弹窗
     *
     * @param position
     */
    private void setPopup(int position) {
        middlePopup = new TopMiddlePopup(LearningTaskActivity.this, screenW, screenH, position);
    }

    /**
     * 获取屏幕的宽和高
     */
    public void getScreenPixels() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenW = metrics.widthPixels;
        screenH = metrics.heightPixels;
    }

    @Override
    protected void onDestroy() {
        sp.saveType(mContext, "1");
        sp.saveType1(mContext, "11");
        sp.saveType2(mContext, "111");
        sp.saveQualified(mContext, "1");
        super.onDestroy();
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("first")) {
                LearningTaskActivity.poptype = intent.getStringExtra("poptype");
                mFragmentList.get(0).onResume();
            } else if (action.equals("second")) {
                LearningTaskActivity.poptype = intent.getStringExtra("poptype");
                mFragmentList.get(1).onResume();
            } else if (action.equals("third")) {
                LearningTaskActivity.poptype = intent.getStringExtra("poptype");
                LearningTaskActivity.popqualified = intent.getStringExtra("popqualified");
                mFragmentList.get(2).onResume();
            }
        }
    };

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("first");
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);

        IntentFilter myIntentFilter1 = new IntentFilter();
        myIntentFilter1.addAction("second");
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter1);

        IntentFilter myIntentFilter2 = new IntentFilter();
        myIntentFilter2.addAction("third");
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter2);
    }
}
