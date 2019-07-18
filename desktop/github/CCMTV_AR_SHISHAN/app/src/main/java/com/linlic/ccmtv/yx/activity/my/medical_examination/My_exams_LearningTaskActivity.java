package com.linlic.ccmtv.yx.activity.my.medical_examination;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.Popular_search2;
import com.linlic.ccmtv.yx.kzbf.utils.SkyVisitUtils;
import com.linlic.ccmtv.yx.utils.NetUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Nillaus
 * Date:2017年10月16日
 * Description:考试列表界面
 */
public class My_exams_LearningTaskActivity extends FragmentActivity implements View.OnClickListener {
    Context context;
    //用户统计
    private String entertime, leavetime;
    public static String enterUrl = "http://www.ccmtv.cn/Member/Index.html";
    private TextView item_ongoing, item_completed, item_expired, activity_title_name;
    private ViewPager vp;
    private My_exams_under_way my_exams_under_way;
    private My_exams_Not_yet_started my_exams_not_yet_started;
    private My_exams_over my_exams_over;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private FragmentAdapter mFragmentAdapter;
    private View line1, line2, line3;
    public static boolean istop = true;
    public static String keyword = "";
    private int requestCode;
    private LinearLayout arrow_search;

    private NetworkChangeReceiver networkChangeRecever;

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
        setContentView(R.layout.my_exams_activity_learning_task);
        initViews();
        context = this;

        mFragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), mFragmentList);
        vp.setOffscreenPageLimit(3);//ViewPager的缓存为3帧
        vp.setAdapter(mFragmentAdapter);
        vp.setCurrentItem(0);//初始设置ViewPager选中第一帧
        item_ongoing.setTextColor(Color.parseColor("#3797FB"));

        //ViewPager的监听事件
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                /*此方法在页面被选中时调用*/
                changeTextColor(position);
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

        // 注册一个网络状态监听的广播
        //@author eric
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");//添加广播
        networkChangeRecever = new NetworkChangeReceiver();
        registerReceiver(networkChangeRecever, intentFilter);
    }

    /**
     * 初始化布局View
     */
    private void initViews() {
        item_ongoing = (TextView) findViewById(R.id.item_ongoing);
        item_completed = (TextView) findViewById(R.id.item_completed);
        item_expired = (TextView) findViewById(R.id.item_expired);
        activity_title_name = (TextView) findViewById(R.id.activity_title_name);
        arrow_search = (LinearLayout) findViewById(R.id.arrow_search);
        item_ongoing.setOnClickListener(this);
        item_completed.setOnClickListener(this);
        item_expired.setOnClickListener(this);
        line1 = findViewById(R.id.v_line1);
        line2 = findViewById(R.id.v_line2);
        line3 = findViewById(R.id.v_line3);
        vp = (ViewPager) findViewById(R.id.mainViewPager);
        my_exams_under_way = new My_exams_under_way();
        my_exams_over = new My_exams_over();
        my_exams_not_yet_started = new My_exams_Not_yet_started();
        //给FragmentList添加数据
        mFragmentList.add(my_exams_under_way);
        mFragmentList.add(my_exams_not_yet_started);
        mFragmentList.add(my_exams_over);
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
            item_ongoing.setTextColor(Color.parseColor("#3797FB"));
            item_completed.setTextColor(Color.parseColor("#000000"));
            item_expired.setTextColor(Color.parseColor("#000000"));
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
        finish();
    }

    public void search(View view) {
        My_exams_LearningTaskActivity.keyword = "";
        My_exams_LearningTaskActivity.istop = true;
        mFragmentList.get(vp.getCurrentItem()).onResume();
        view.setVisibility(View.GONE);
        activity_title_name.setText("我的考试");
    }

    public void ss(View view) {
        Intent intent = new Intent(My_exams_LearningTaskActivity.this, Popular_search2.class);
        // 请求码的值随便设置，但必须>=0
        requestCode = 0;
        My_exams_LearningTaskActivity.istop = false;
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void finish() {
        My_exams_LearningTaskActivity.keyword = "";
        My_exams_LearningTaskActivity.istop = true;
        super.finish();
    }

    // 回调方法，从第二个页面回来的时候会执行这个方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.e("requestCode", requestCode + "");
//        Log.e("resultCode", resultCode + "");
//        Log.e("data", data.getExtras().toString());
        // 根据上面发送过去的请求吗来区别
        switch (requestCode) {
            case 0:
                My_exams_LearningTaskActivity.keyword = data.getStringExtra("keyword");
                if (My_exams_LearningTaskActivity.keyword.length() > 0) {
                    arrow_search.setVisibility(View.VISIBLE);
                    activity_title_name.setText("搜索");
                } else {
                    activity_title_name.setText("我的考试");
                    arrow_search.setVisibility(View.GONE);
                }
                My_exams_LearningTaskActivity.istop = true;
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
        SkyVisitUtils.OnlineStatistical(context, enterUrl, entertime, leavetime);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (networkChangeRecever != null)
            this.unregisterReceiver(networkChangeRecever);
        super.onDestroy();
    }
}
