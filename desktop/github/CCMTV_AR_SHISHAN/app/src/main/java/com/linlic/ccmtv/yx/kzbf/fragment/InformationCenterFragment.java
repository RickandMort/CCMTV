package com.linlic.ccmtv.yx.kzbf.fragment;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseFragment;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.kzbf.adapter.MedicineAdapter;
import com.linlic.ccmtv.yx.kzbf.widget.NoScrollViewPager;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgeAnchor;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgeRule;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 资讯中心
 */
public class InformationCenterFragment extends BaseFragment {
    public static String KEY_ARGMENT = "KEY_ARGMENT";
    public static String KEY_ARGMENT_VALYE = "资讯中心";
    public static String KEY_ARGMENT_VALYE1 = "专题指南";
    private String[] CHANNELS = new String[]{"最新动态", "药讯助手"};
    private String[] CHANNELS2 = new String[]{"专题指南", "相关文献"};
    private List<String> mDataList;
    private List<Fragment> fragmentList; //保存界面的view
    private MedicineAdapter medicineAdapter;
    private View mView;
    private NoScrollViewPager viewpager;
    private boolean is_show_red;
    private MyHandler handler;
    private String arguments;

    private class MyHandler extends Handler {
        private final WeakReference<Activity> mActivity;

        public MyHandler(Activity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() == null) {
                return;
            }
            switch (msg.what) {
                case 1:
                    commonNavigatorAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }

        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_information_center, container, false);
        arguments = getArguments().getString(KEY_ARGMENT);
        fragmentList = new ArrayList<>();
        if (KEY_ARGMENT_VALYE.equals(arguments)) {
            mDataList = Arrays.asList(CHANNELS);
            fragmentList.add(new MedicineDynamicsFragment());  //药讯动态
            fragmentList.add(new MedicineAssistantFragment()); //药讯助手
        } else {
            mDataList = Arrays.asList(CHANNELS2);
            fragmentList.add(new SpecialGuideFragment()); //专题指南
            fragmentList.add(new RelatedliteratureFragment());  //相关文献

        }
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        viewpager = mView.findViewById(R.id.information_center_viewpager);


        medicineAdapter = new MedicineAdapter(getChildFragmentManager(), mDataList, fragmentList);
        viewpager.setAdapter(medicineAdapter);
        viewpager.setNoScroll(true);//禁止ViewPager左右滑动
        initMagicIndicator();
        handler = new MyHandler((Activity) mContext);
    }

    private void initData() {
        getIsShowRedDot();
    }


    @Override
    public void onResume() {
        initData();
        super.onResume();
    }


    @Override
    public void onDestroyView() {
        if (handler != null) handler.removeCallbacksAndMessages(null);
        super.onDestroyView();
    }

    private void initMagicIndicator() {
        MagicIndicator magicIndicator = mView.findViewById(R.id.information_center_magic_indicator);
        magicIndicator.setBackgroundColor(Color.parseColor("#FFFFFF"));
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setScrollPivotX(0.25f);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(commonNavigatorAdapter);
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewpager);

        //                        badgePagerTitleView.setBadgeView(null); // cancel badge when click tab
//                    badgePagerTitleView.setXBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_RIGHT, -UIUtil.dip2px(context, 6)));
//                indicator.setYOffset(UIUtil.dip2px(context, 3));
//滑块宽度
    }

    private CommonNavigatorAdapter commonNavigatorAdapter = new CommonNavigatorAdapter() {
        @Override
        public int getCount() {
            return mDataList == null ? 0 : mDataList.size();
        }

        @Override
        public IPagerTitleView getTitleView(Context context, final int index) {
            final BadgePagerTitleView badgePagerTitleView = new BadgePagerTitleView(context);

            SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
            simplePagerTitleView.setText(mDataList.get(index));
            simplePagerTitleView.setNormalColor(Color.parseColor("#000000"));
            simplePagerTitleView.setSelectedColor(Color.parseColor("#3997F9"));
            simplePagerTitleView.setTextSize(16);
            simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewpager.setCurrentItem(index);
                        //badgePagerTitleView.setBadgeView(null); // cancel badge when click tab
                }
            });

            badgePagerTitleView.setInnerPagerTitleView(simplePagerTitleView);

            if (is_show_red && index == 1) {//设置是否显示小红点
                ImageView badgeImageView = (ImageView) LayoutInflater.from(context).inflate(R.layout.simple_red_dot_badge_layout, null);
                badgePagerTitleView.setBadgeView(badgeImageView);
                    ViewGroup.LayoutParams params = badgeImageView.getLayoutParams();
                    params.height = 20;
                    params.width = 20;
                    badgeImageView.setLayoutParams(params);

                   badgePagerTitleView.setXBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_RIGHT, -UIUtil.dip2px(context, 6)));
                badgePagerTitleView.setXBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_RIGHT, 0));
                badgePagerTitleView.setYBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_TOP, 0));
            }

            badgePagerTitleView.setAutoCancelBadge(false);

            return badgePagerTitleView;
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
    };

    public void setViewpagerItem() {
        viewpager.setCurrentItem(1);
    }

    public void getIsShowRedDot() {
        if (!KEY_ARGMENT_VALYE.equals(arguments)) return;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.columnIsShowRed);
                    obj.put("uid", SharedPreferencesTools.getUid(mContext));

                    String result = HttpClientUtils.sendPost(mContext, URLConfig.Skyvisit, obj.toString());
                    LogUtil.e("看看药讯助手小红点是否显示数据", result);

                    JSONObject resultObject = new JSONObject(result);
                    if (resultObject.getString("code").equals("0")) {
                        if (resultObject.getString("is_show_red").equals("1")) {   //is_show_red    1显示 0隐藏
                            is_show_red = true;
                        } else {
                            is_show_red = false;
                        }

                        handler.sendEmptyMessage(1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }
}

