package com.linlic.ccmtv.yx.activity.home.apricotcup;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.kzbf.utils.SkyVisitUtils;
import com.linlic.ccmtv.yx.widget.HoverScrollView;

/**
 * name：杏林杯
 * author：Larry
 * data：2017/3/29.
 */
public class ApricotActivity extends FragmentActivity implements View.OnClickListener, HoverScrollView.OnScrollListener {
    Context context;
    //用户统计
    private String entertime, leavetime;
    public static String enterUrl = "http://www.ccmtv.cn";
    // 四个选项卡
    private LinearLayout tab1Layout, tab2Layout, tab3Layout, tab4Layout, tab5Layout;
    private LinearLayout tabLayout1, tabLayout2, tabLayout3, tabLayout4, tabLayout5;
    // 默认选中第一个tab
    private int index = 1;
    // fragment管理类
    private FragmentManager fragmentManager;
    // 四个fragment
    private ApricotHomePageFragment apricotHomePageFragment;
    private ApricotCaseShowFragment apricotCaseShowFragment;
    private ApricotRequireFragment apricotRequireFragment;
    private ApricotEntryDescriptionFragment apricotEntryDescriptionFragment;
    private ApricotTopFragment apricotTopFragment;
    private LinearLayout layout_apricotcuptab1;
    private int size;
    private HoverScrollView hoversc;
    private TextView activity_title_name;
    private ImageView mIvHead;
    // 开个轮询线程，循环直到对size赋上非0的值
    private Handler mHandler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                // 顶部高度为0时，表示布局未完成，继续轮询
                if (mIvHead.getHeight() == 0) {
                    mHandler.sendEmptyMessageDelayed(1, 10);
                    return;
                }
                size = mIvHead.getHeight();
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apricot);
        context = this;
        fragmentManager = getSupportFragmentManager();
        init();
    }

    /**
     * 初始化控件
     */
    private void init() {
        String title = getIntent().getStringExtra("title");
        activity_title_name = (TextView) findViewById(R.id.activity_title_name);
        activity_title_name.setText(title);
        tab1Layout = (LinearLayout) findViewById(R.id.tabapricot1_layout);
        tab2Layout = (LinearLayout) findViewById(R.id.tabapricot2_layout);
        tab3Layout = (LinearLayout) findViewById(R.id.tabapricot3_layout);
        tab4Layout = (LinearLayout) findViewById(R.id.tabapricot4_layout);
        tab5Layout = (LinearLayout) findViewById(R.id.tabapricot5_layout);
        tabLayout1 = (LinearLayout) findViewById(R.id.tabapricot_layout1);
        tabLayout2 = (LinearLayout) findViewById(R.id.tabapricot_layout2);
        tabLayout3 = (LinearLayout) findViewById(R.id.tabapricot_layout3);
        tabLayout4 = (LinearLayout) findViewById(R.id.tabapricot_layout4);
        tabLayout5 = (LinearLayout) findViewById(R.id.tabapricot_layout5);
        layout_apricotcuptab1 = (LinearLayout) findViewById(R.id.layout_apricotcuptab1);
        hoversc = (HoverScrollView) findViewById(R.id.hoversc);
        mIvHead = (ImageView) findViewById(R.id.mIvHead);
        hoversc.setOnScrollListener(this);
        // 如果知道准确的高度，可以直接取得
        // size = getResources().getDimensionPixelSize(R.dimen.y100);
        // 10毫秒循环判断
        mHandler.sendEmptyMessageDelayed(1, 10);

        tab1Layout.setOnClickListener(this);
        tab2Layout.setOnClickListener(this);
        tab3Layout.setOnClickListener(this);
        tab4Layout.setOnClickListener(this);
        tab5Layout.setOnClickListener(this);
        tabLayout1.setOnClickListener(this);
        tabLayout2.setOnClickListener(this);
        tabLayout3.setOnClickListener(this);
        tabLayout4.setOnClickListener(this);
        tabLayout5.setOnClickListener(this);
        WindowManager wm = this.getWindowManager();

        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        //下面这句是为了解决当数据填充不了sc时，点击tab会反弹
        // content_layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, height + mIvHead.getHeight()));
        setDefaultFragment();
    }

    /**
     * 设置默认显示的fragment
     */
    private void setDefaultFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        apricotHomePageFragment = new ApricotHomePageFragment();
        transaction.replace(R.id.content_layout, apricotHomePageFragment);
        transaction.commit();
    }

    /**
     * 切换fragment
     *
     * @param newFragment
     */
    private void replaceFragment(Fragment newFragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (!newFragment.isAdded()) {
            transaction.replace(R.id.content_layout, newFragment);
            transaction.commit();
        } else {
            transaction.show(newFragment);
        }
    }

    /**
     * 改变现象卡的选中状态
     */
    private void clearStatus() {
        if (index == 1) {
            tab1Layout.setBackgroundColor(getResources().getColor(R.color.tab_apricot));
            tabLayout1.setBackgroundColor(getResources().getColor(R.color.tab_apricot));
           /* ViewGroup.LayoutParams layoutParams = tab1Layout.getLayoutParams();
            layoutParams.height = 200;
            tab1Layout.setLayoutParams(layoutParams);*/
        } else if (index == 2) {
            tab2Layout.setBackgroundColor(getResources().getColor(R.color.tab_apricot));
            tabLayout2.setBackgroundColor(getResources().getColor(R.color.tab_apricot));
        } else if (index == 3) {
            tab3Layout.setBackgroundColor(getResources().getColor(R.color.tab_apricot));
            tabLayout3.setBackgroundColor(getResources().getColor(R.color.tab_apricot));
        } else if (index == 4) {
            tab4Layout.setBackgroundColor(getResources().getColor(R.color.tab_apricot));
            tabLayout4.setBackgroundColor(getResources().getColor(R.color.tab_apricot));
        } else if (index == 5) {
            tab5Layout.setBackgroundColor(getResources().getColor(R.color.tab_apricot));
            tabLayout5.setBackgroundColor(getResources().getColor(R.color.tab_apricot));
        }
    }

    @Override
    public void onClick(View v) {
        clearStatus();
        switch (v.getId()) {
            case R.id.tabapricot1_layout:
                if (apricotHomePageFragment == null) {
                    apricotHomePageFragment = new ApricotHomePageFragment();
                }
                replaceFragment(apricotHomePageFragment);
                tab1Layout.setBackgroundColor(getResources().getColor(
                        R.color.tab_apricot_down));
                tabLayout1.setBackgroundColor(getResources().getColor(
                        R.color.tab_apricot_down));
                index = 1;
                break;
            case R.id.tabapricot2_layout:
                if (apricotCaseShowFragment == null) {
                    apricotCaseShowFragment = new ApricotCaseShowFragment();
                }
                replaceFragment(apricotCaseShowFragment);
                tab2Layout.setBackgroundColor(getResources().getColor(
                        R.color.tab_apricot_down));
                tabLayout2.setBackgroundColor(getResources().getColor(
                        R.color.tab_apricot_down));
                index = 2;
                break;
            case R.id.tabapricot3_layout:
                if (apricotRequireFragment == null) {
                    apricotRequireFragment = new ApricotRequireFragment();
                }
                replaceFragment(apricotRequireFragment);
                tab3Layout.setBackgroundColor(getResources().getColor(
                        R.color.tab_apricot_down));
                tabLayout3.setBackgroundColor(getResources().getColor(
                        R.color.tab_apricot_down));
                index = 3;
                break;
            case R.id.tabapricot4_layout:
                if (apricotEntryDescriptionFragment == null) {
                    apricotEntryDescriptionFragment = new ApricotEntryDescriptionFragment();
                }
                replaceFragment(apricotEntryDescriptionFragment);
                tab4Layout.setBackgroundColor(getResources().getColor(
                        R.color.tab_apricot_down));
                tabLayout4.setBackgroundColor(getResources().getColor(
                        R.color.tab_apricot_down));
                index = 4;
                break;
            case R.id.tabapricot5_layout:
                if (apricotTopFragment == null) {
                    apricotTopFragment = new ApricotTopFragment();
                }
                replaceFragment(apricotTopFragment);
                tab5Layout.setBackgroundColor(getResources().getColor(
                        R.color.tab_apricot_down));
                tabLayout5.setBackgroundColor(getResources().getColor(
                        R.color.tab_apricot_down));
                index = 5;
                break;
            case R.id.tabapricot_layout1:
                if (apricotHomePageFragment == null) {
                    apricotHomePageFragment = new ApricotHomePageFragment();
                }
                replaceFragment(apricotHomePageFragment);
                tab1Layout.setBackgroundColor(getResources().getColor(
                        R.color.tab_apricot_down));
                tabLayout1.setBackgroundColor(getResources().getColor(
                        R.color.tab_apricot_down));
                index = 1;
                break;
            case R.id.tabapricot_layout2:
                if (apricotCaseShowFragment == null) {
                    apricotCaseShowFragment = new ApricotCaseShowFragment();
                }
                replaceFragment(apricotCaseShowFragment);
                tab2Layout.setBackgroundColor(getResources().getColor(
                        R.color.tab_apricot_down));
                tabLayout2.setBackgroundColor(getResources().getColor(
                        R.color.tab_apricot_down));
                index = 2;
                break;
            case R.id.tabapricot_layout3:
                if (apricotRequireFragment == null) {
                    apricotRequireFragment = new ApricotRequireFragment();
                }
                replaceFragment(apricotRequireFragment);
                tab3Layout.setBackgroundColor(getResources().getColor(
                        R.color.tab_apricot_down));
                tabLayout3.setBackgroundColor(getResources().getColor(
                        R.color.tab_apricot_down));
                index = 3;
                break;
            case R.id.tabapricot_layout4:
                if (apricotEntryDescriptionFragment == null) {
                    apricotEntryDescriptionFragment = new ApricotEntryDescriptionFragment();
                }
                replaceFragment(apricotEntryDescriptionFragment);
                tab4Layout.setBackgroundColor(getResources().getColor(
                        R.color.tab_apricot_down));
                tabLayout4.setBackgroundColor(getResources().getColor(
                        R.color.tab_apricot_down));
                index = 4;
                break;
            case R.id.tabapricot_layout5:
                if (apricotTopFragment == null) {
                    apricotTopFragment = new ApricotTopFragment();
                }
                replaceFragment(apricotTopFragment);
                tab5Layout.setBackgroundColor(getResources().getColor(
                        R.color.tab_apricot_down));
                tabLayout5.setBackgroundColor(getResources().getColor(
                        R.color.tab_apricot_down));
                index = 5;
                break;
            default:
                break;
        }
    }

    @Override
    public void onScroll(int scrollY) {
        if (scrollY >= size) {
            layout_apricotcuptab1.setVisibility(View.VISIBLE);
        } else {
            layout_apricotcuptab1.setVisibility(View.INVISIBLE);
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
        ApricotActivity.this.finish();
    }
}
