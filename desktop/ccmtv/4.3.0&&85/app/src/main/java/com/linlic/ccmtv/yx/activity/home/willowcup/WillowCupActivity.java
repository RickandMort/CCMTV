package com.linlic.ccmtv.yx.activity.home.willowcup;

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
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.widget.HoverScrollView;

/**
 * name：柳叶杯
 * author：Larry
 * data：2017/3/27.
 */
public class WillowCupActivity extends FragmentActivity implements View.OnClickListener, HoverScrollView.OnScrollListener {
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
    private HomePageFragment homePageFragment;
    private WonderfulShowFragment wonderfullShowFragment;
    private RequireFragment requireFragment;
    private EntryDescriptionFragment entryDescriptionFragment;
    private WillowCupTopFragment willowCupTopFragment;
    private LinearLayout layout_willowcuptab1;
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
        setContentView(R.layout.activity_willowcup);
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
        tab1Layout = (LinearLayout) findViewById(R.id.tab1_layout);
        tab2Layout = (LinearLayout) findViewById(R.id.tab2_layout);
        tab3Layout = (LinearLayout) findViewById(R.id.tab3_layout);
        tab4Layout = (LinearLayout) findViewById(R.id.tab4_layout);
        tab5Layout = (LinearLayout) findViewById(R.id.tab5_layout);
        tabLayout1 = (LinearLayout) findViewById(R.id.tab_layout1);
        tabLayout2 = (LinearLayout) findViewById(R.id.tab_layout2);
        tabLayout3 = (LinearLayout) findViewById(R.id.tab_layout3);
        tabLayout4 = (LinearLayout) findViewById(R.id.tab_layout4);
        tabLayout5 = (LinearLayout) findViewById(R.id.tab_layout5);

        layout_willowcuptab1 = (LinearLayout) findViewById(R.id.layout_willowcuptab1);
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
        homePageFragment = new HomePageFragment();
        transaction.replace(R.id.content_layout, homePageFragment);
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
            tab1Layout.setBackgroundColor(getResources().getColor(R.color.tab));
            tabLayout1.setBackgroundColor(getResources().getColor(R.color.tab));
           /* ViewGroup.LayoutParams layoutParams = tab1Layout.getLayoutParams();
            layoutParams.height = 200;
            tab1Layout.setLayoutParams(layoutParams);*/
        } else if (index == 2) {
            tab2Layout.setBackgroundColor(getResources().getColor(R.color.tab));
            tabLayout2.setBackgroundColor(getResources().getColor(R.color.tab));
        } else if (index == 3) {
            tab3Layout.setBackgroundColor(getResources().getColor(R.color.tab));
            tabLayout3.setBackgroundColor(getResources().getColor(R.color.tab));
        } else if (index == 4) {
            tab4Layout.setBackgroundColor(getResources().getColor(R.color.tab));
            tabLayout4.setBackgroundColor(getResources().getColor(R.color.tab));
        } else if (index == 5) {
            tab5Layout.setBackgroundColor(getResources().getColor(R.color.tab));
            tabLayout5.setBackgroundColor(getResources().getColor(R.color.tab));
        }
    }

    @Override
    public void onClick(View v) {
        clearStatus();
        switch (v.getId()) {
            case R.id.tab1_layout:
                if (homePageFragment == null) {
                    homePageFragment = new HomePageFragment();
                }
                replaceFragment(homePageFragment);
                tab1Layout.setBackgroundColor(getResources().getColor(
                        R.color.tab_down));
                tabLayout1.setBackgroundColor(getResources().getColor(
                        R.color.tab_down));
                index = 1;
                break;
            case R.id.tab2_layout:
                if (wonderfullShowFragment == null) {
                    wonderfullShowFragment = new WonderfulShowFragment();
                }
                replaceFragment(wonderfullShowFragment);
                tab2Layout.setBackgroundColor(getResources().getColor(
                        R.color.tab_down));
                tabLayout2.setBackgroundColor(getResources().getColor(
                        R.color.tab_down));
                index = 2;
                break;
            case R.id.tab3_layout:
                if (requireFragment == null) {
                    requireFragment = new RequireFragment();
                }
                replaceFragment(requireFragment);
                tab3Layout.setBackgroundColor(getResources().getColor(
                        R.color.tab_down));
                tabLayout3.setBackgroundColor(getResources().getColor(
                        R.color.tab_down));
                index = 3;
                break;
            case R.id.tab4_layout:
                if (entryDescriptionFragment == null) {
                    entryDescriptionFragment = new EntryDescriptionFragment();
                }
                replaceFragment(entryDescriptionFragment);
                tab4Layout.setBackgroundColor(getResources().getColor(
                        R.color.tab_down));
                tabLayout4.setBackgroundColor(getResources().getColor(
                        R.color.tab_down));
                index = 4;
                break;
            case R.id.tab5_layout:
                if (willowCupTopFragment == null) {
                    willowCupTopFragment = new WillowCupTopFragment();
                }
                replaceFragment(willowCupTopFragment);
                tab5Layout.setBackgroundColor(getResources().getColor(
                        R.color.tab_down));
                tabLayout5.setBackgroundColor(getResources().getColor(
                        R.color.tab_down));
                index = 5;
                break;
            case R.id.tab_layout1:
                if (homePageFragment == null) {
                    homePageFragment = new HomePageFragment();
                }
                replaceFragment(homePageFragment);
                tab1Layout.setBackgroundColor(getResources().getColor(
                        R.color.tab_down));
                tabLayout1.setBackgroundColor(getResources().getColor(
                        R.color.tab_down));
                index = 1;
                break;
            case R.id.tab_layout2:
                if (wonderfullShowFragment == null) {
                    wonderfullShowFragment = new WonderfulShowFragment();
                }
                replaceFragment(wonderfullShowFragment);
                tab2Layout.setBackgroundColor(getResources().getColor(
                        R.color.tab_down));
                tabLayout2.setBackgroundColor(getResources().getColor(
                        R.color.tab_down));
                index = 2;
                break;
            case R.id.tab_layout3:
                if (requireFragment == null) {
                    requireFragment = new RequireFragment();
                }
                replaceFragment(requireFragment);
                tab3Layout.setBackgroundColor(getResources().getColor(
                        R.color.tab_down));
                tabLayout3.setBackgroundColor(getResources().getColor(
                        R.color.tab_down));
                index = 3;
                break;
            case R.id.tab_layout4:
                if (entryDescriptionFragment == null) {
                    entryDescriptionFragment = new EntryDescriptionFragment();
                }
                replaceFragment(entryDescriptionFragment);
                tab4Layout.setBackgroundColor(getResources().getColor(
                        R.color.tab_down));
                tabLayout4.setBackgroundColor(getResources().getColor(
                        R.color.tab_down));
                index = 4;
                break;
            case R.id.tab_layout5:
                if (willowCupTopFragment == null) {
                    willowCupTopFragment = new WillowCupTopFragment();
                }
                replaceFragment(willowCupTopFragment);
                tab5Layout.setBackgroundColor(getResources().getColor(
                        R.color.tab_down));
                tabLayout5.setBackgroundColor(getResources().getColor(
                        R.color.tab_down));
                index = 5;
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        //保存进入的日期
        entertime = SkyVisitUtils.getCurrentTime();
        MyProgressBarDialogTools.hide();
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

    @Override
    public void onScroll(int scrollY) {
        if (scrollY >= size) {
            layout_willowcuptab1.setVisibility(View.VISIBLE);
        } else {
            layout_willowcuptab1.setVisibility(View.INVISIBLE);
        }
    }

    public void back(View view) {
        WillowCupActivity.this.finish();
    }


}
