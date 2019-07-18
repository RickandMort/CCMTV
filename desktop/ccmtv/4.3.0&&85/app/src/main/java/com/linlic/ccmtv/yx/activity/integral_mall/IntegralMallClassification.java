package com.linlic.ccmtv.yx.activity.integral_mall;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.kzbf.utils.SkyVisitUtils;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import java.util.ArrayList;
import java.util.List;


public class IntegralMallClassification extends FragmentActivity implements View.OnClickListener {
    Context context;
    //用户统计
    private String entertime, leavetime;
    public static String enterUrl = "http://www.ccmtv.cn/Member/Index.html";
    private TextView item_ongoing0, item_ongoing, item_completed, item_expired, activity_title_name;
    private ViewPager vp;
    private Latest_goods latest_goods;
    private Souvenir souvenir;
    private Electronic_products electronic_products;
    private Medical_data medical_data;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private IntegralMallClassification.FragmentAdapter mFragmentAdapter;
    private View line0, line1, line2, line3;
    public static boolean istop = true;
    public static String keyword = "";
    private int requestCode;
    private LinearLayout arrow_search;
    private NodataEmptyLayout classifaication_nodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_integral_mall_classification);
        initViews();
        context = this;

        mFragmentAdapter = new IntegralMallClassification.FragmentAdapter(this.getSupportFragmentManager(), mFragmentList);
        vp.setOffscreenPageLimit(4);//ViewPager的缓存为3帧
        vp.setAdapter(mFragmentAdapter);
        vp.setCurrentItem(getIntent().getIntExtra("position", 0));//初始设置ViewPager选中第一帧
        changeTextColor(getIntent().getIntExtra("position", 0));

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
    }

    /**
     * 初始化布局View
     */
    private void initViews() {
        item_ongoing = (TextView) findViewById(R.id.item_ongoing);
        item_ongoing0 = (TextView) findViewById(R.id.item_ongoing0);
        item_completed = (TextView) findViewById(R.id.item_completed);
        item_expired = (TextView) findViewById(R.id.item_expired);
        activity_title_name = (TextView) findViewById(R.id.activity_title_name);
        arrow_search = (LinearLayout) findViewById(R.id.arrow_search);

        item_ongoing.setOnClickListener(this);
        item_ongoing0.setOnClickListener(this);
        item_completed.setOnClickListener(this);
        item_expired.setOnClickListener(this);
        line0 = findViewById(R.id.v_line0);
        line1 = findViewById(R.id.v_line1);
        line2 = findViewById(R.id.v_line2);
        line3 = findViewById(R.id.v_line3);
        vp = (ViewPager) findViewById(R.id.mainViewPager);
        latest_goods = new Latest_goods();
        souvenir = new Souvenir();
        electronic_products = new Electronic_products();
        medical_data = new Medical_data();
        //给FragmentList添加数据
        mFragmentList.add(latest_goods);
        mFragmentList.add(souvenir);
        mFragmentList.add(electronic_products);
        mFragmentList.add(medical_data);
    }

    /**
     * name: 点击查看某个礼品详细
     */
    public void clickCommodity(View view) {
        TextView textView = (TextView) view.findViewById(R.id.commodity_id);
        String id = textView.getText().toString();
        //跳转到礼品详情
        Intent intent = new Intent(IntegralMallClassification.this, Commodity_introduction.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    /**
     * 点击底部Text 动态修改ViewPager的内容
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_ongoing0:
                vp.setCurrentItem(0, true);
                break;
            case R.id.item_ongoing:
                vp.setCurrentItem(1, true);
                break;
            case R.id.item_completed:
                vp.setCurrentItem(2, true);
                break;
            case R.id.item_expired:
                vp.setCurrentItem(3, true);
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
            item_ongoing0.setTextColor(Color.parseColor("#3797FB"));
            item_ongoing.setTextColor(Color.parseColor("#000000"));
            item_completed.setTextColor(Color.parseColor("#000000"));
            item_expired.setTextColor(Color.parseColor("#000000"));
            line0.setVisibility(View.VISIBLE);
            line1.setVisibility(View.INVISIBLE);
            line2.setVisibility(View.INVISIBLE);
            line3.setVisibility(View.INVISIBLE);
        } else if (position == 1) {
            item_ongoing0.setTextColor(Color.parseColor("#000000"));
            item_ongoing.setTextColor(Color.parseColor("#3797FB"));
            item_completed.setTextColor(Color.parseColor("#000000"));
            item_expired.setTextColor(Color.parseColor("#000000"));
            line0.setVisibility(View.INVISIBLE);
            line1.setVisibility(View.VISIBLE);
            line2.setVisibility(View.INVISIBLE);
            line3.setVisibility(View.INVISIBLE);
        } else if (position == 2) {
            item_ongoing0.setTextColor(Color.parseColor("#000000"));
            item_ongoing.setTextColor(Color.parseColor("#000000"));
            item_completed.setTextColor(Color.parseColor("#3797FB"));
            item_expired.setTextColor(Color.parseColor("#000000"));
            line0.setVisibility(View.INVISIBLE);
            line1.setVisibility(View.INVISIBLE);
            line2.setVisibility(View.VISIBLE);
            line3.setVisibility(View.INVISIBLE);
        } else if (position == 3) {
            item_ongoing0.setTextColor(Color.parseColor("#000000"));
            item_ongoing.setTextColor(Color.parseColor("#000000"));
            item_completed.setTextColor(Color.parseColor("#000000"));
            item_expired.setTextColor(Color.parseColor("#3797FB"));
            line0.setVisibility(View.INVISIBLE);
            line1.setVisibility(View.INVISIBLE);
            line2.setVisibility(View.INVISIBLE);
            line3.setVisibility(View.VISIBLE);
        }
    }

    public void back(View view) {
        finish();
    }

    public void search(View view) {
        mFragmentList.get(vp.getCurrentItem()).onResume();
        view.setVisibility(View.GONE);
        activity_title_name.setText("积分商城");
    }

    @Override
    public void finish() {
        super.finish();
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

}
