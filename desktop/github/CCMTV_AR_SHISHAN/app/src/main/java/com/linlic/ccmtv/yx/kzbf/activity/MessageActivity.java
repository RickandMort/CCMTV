package com.linlic.ccmtv.yx.kzbf.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.kzbf.adapter.MedicineAdapter;
import com.linlic.ccmtv.yx.kzbf.fragment.InboxFragment;
import com.linlic.ccmtv.yx.kzbf.fragment.OutboxFragment;
import com.linlic.ccmtv.yx.kzbf.fragment.WriteMessageFragment;
import com.linlic.ccmtv.yx.kzbf.utils.SkyVisitUtils;
import com.linlic.ccmtv.yx.kzbf.widget.NoScrollViewPager;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 空中拜访-消息
 */
//item_message
public class MessageActivity extends FragmentActivity {
    private static final String[] CHANNELS = new String[]{"收件箱", "已发送", "写消息"};
    private List<String> mDataList = Arrays.asList(CHANNELS);
    private List<Fragment> fragmentList; //保存界面的view
    Context context;
    //用户统计
    private String entertime, leavetime;
    public static String enterUrl = "http://www.ccmtv.cn";
    private NoScrollViewPager viewPager;
    private MedicineAdapter messageAdapter;
    public static String assistant = "";
    public static String addressee_uid = "";//收件人用户ID
    public static String helper = "";//收件人名称
    public static String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        context = this;

        initView();
        initData();
    }

    private void initView() {
        viewPager = (NoScrollViewPager) findViewById(R.id.message_viewpager);
    }

    private void initData() {
        assistant = getIntent().getStringExtra("assistant");
        addressee_uid = getIntent().getStringExtra("addressee_uid");
        helper = getIntent().getStringExtra("helper");
        type = getIntent().getStringExtra("type");
        fragmentList = new ArrayList<>();
        fragmentList.add(new InboxFragment());        //收件箱
        fragmentList.add(new OutboxFragment());       //已发送
        fragmentList.add(new WriteMessageFragment()); //写消息

        messageAdapter = new MedicineAdapter(getSupportFragmentManager(), mDataList, fragmentList);
        viewPager.setAdapter(messageAdapter);
        viewPager.setNoScroll(true);//禁止ViewPager左右滑动
        initMagicIndicator();

        if (assistant != null) {//从咨询跳转过来
            if (assistant.equals("0") || assistant.equals("1")) {
                viewPager.setCurrentItem(2);
            }
        }
        if (type != null) {
            if (type.equals("0")) {
                viewPager.setCurrentItem(0);
            } else if (type.equals("1")) {
                viewPager.setCurrentItem(1);
            }
        }

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                fragmentList.get(position).onResume();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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

    private void initMagicIndicator() {
        MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.message_indicator);
        magicIndicator.setBackgroundColor(Color.parseColor("#FFFFFF"));
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setScrollPivotX(0.25f);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setNormalColor(Color.parseColor("#000000"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#3997F9"));
                simplePagerTitleView.setTextSize(16);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
//                indicator.setYOffset(UIUtil.dip2px(context, 3));
                indicator.setLineWidth(UIUtil.dip2px(context, 30));//滑块宽度
                indicator.setColors(Color.parseColor("#3997F9"));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }

    public void back(View v) {
        finish();
    }

    /**
     * 消息发送完成后，跳转到已发送Fragment
     */
    public void jump2Outbox() {
        viewPager.setCurrentItem(1);
    }
}
