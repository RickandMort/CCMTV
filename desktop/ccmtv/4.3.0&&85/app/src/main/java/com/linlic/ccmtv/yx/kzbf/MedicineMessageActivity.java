package com.linlic.ccmtv.yx.kzbf;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.integral_mall.Commodity_introduction;
import com.linlic.ccmtv.yx.activity.integral_mall.IntegralMall;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.kzbf.activity.MessageActivity;
import com.linlic.ccmtv.yx.kzbf.activity.MyCollectionActivity;
import com.linlic.ccmtv.yx.kzbf.activity.MyDownloadActivity;
import com.linlic.ccmtv.yx.kzbf.activity.MyFouseActivity;
import com.linlic.ccmtv.yx.kzbf.activity.SearchActivity;
import com.linlic.ccmtv.yx.kzbf.adapter.MedicineAdapter;
import com.linlic.ccmtv.yx.kzbf.fragment.InformationCenterFragment;
import com.linlic.ccmtv.yx.kzbf.fragment.IntegralMallFragment;
import com.linlic.ccmtv.yx.kzbf.utils.SkyVisitUtils;
import com.linlic.ccmtv.yx.kzbf.widget.CustomTitleBar;
import com.linlic.ccmtv.yx.kzbf.widget.NoScrollViewPager;
import com.linlic.ccmtv.yx.kzbf.widget.TopPopWindow;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.linlic.ccmtv.yx.kzbf.fragment.InformationCenterFragment.KEY_ARGMENT;

/**
 * 药讯首页
 */
public class MedicineMessageActivity extends FragmentActivity implements View.OnClickListener {

    private static final String[] CHANNELS = new String[]{"资讯中心", "相关文献", "积分商城",};
    private List<String> mDataList = Arrays.asList(CHANNELS);
    private List<Fragment> fragmentList; //保存界面的view
    private MedicineAdapter medicineAdapter;
    private ImageView medicine_search, medicine_menu;
    private NoScrollViewPager medicine_viewpager;
    Context context;
    //用户统计
    private String entertime, leavetime;
    public static String enterUrl = "http://www.ccmtv.cn";
    private TopPopWindow topPopWindow;
    private boolean is_show_red = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_search);
        context = this;

        initView();
        initData();
        recordUserTimeData();
    }

    private void initView() {
        medicine_search = (ImageView) findViewById(R.id.medicine_search);
        medicine_menu = (ImageView) findViewById(R.id.medicine_menu);
        medicine_viewpager = (NoScrollViewPager) findViewById(R.id.medicine_viewpager);

        CustomTitleBar yxTitle = findViewById(R.id.title_bar_yx);
        yxTitle.setOnSelectItemListener(new CustomTitleBar.OnSelectItemListener() {
            @Override
            public void onSelect(int index, View tv) {
                medicine_viewpager.setCurrentItem(index);
            }
        });

        medicine_menu.setOnClickListener(this);
        medicine_search.setOnClickListener(this);

    }

    private void initData() {
        fragmentList = new ArrayList<>();
        InformationCenterFragment Center1 = new InformationCenterFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ARGMENT, "资讯中心");
        Center1.setArguments(bundle);
        fragmentList.add(Center1); //资讯中心
//        fragmentList.add(new MedicineDynamicsFragment());  //药讯动态
        InformationCenterFragment wenxian = new InformationCenterFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString(KEY_ARGMENT, "专题指南");
        wenxian.setArguments(bundle1);
        fragmentList.add(wenxian); //相关文献 //相关文献
//        fragmentList.add(new RelatedliteratureFragment()); //相关文献
        fragmentList.add(new IntegralMallFragment());      //积分商城
//        fragmentList.add(new SpecialGuideFragment());      //专题指南
//        fragmentList.add(new MedicineAssistantFragment()); //药讯助手


        medicineAdapter = new MedicineAdapter(getSupportFragmentManager(), mDataList, fragmentList);
        medicine_viewpager.setAdapter(medicineAdapter);
        medicine_viewpager.setNoScroll(true);//禁止ViewPager左右滑动
    }

    /*private void initMagicIndicator() {
        MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator);
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
                        medicine_viewpager.setCurrentItem(index);
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
        ViewPagerHelper.bind(magicIndicator, medicine_viewpager);
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.medicine_search://搜索按钮
                startActivity(new Intent(context, SearchActivity.class));
                break;
            case R.id.medicine_menu://菜单按钮
                showTopRightPopMenu();
                break;
            case R.id.ll_popmenu_msg://消息
                startActivity(new Intent(context, MessageActivity.class));
                topPopWindow.dismiss();
                break;
            case R.id.ll_popmenu_fouce://我的关注
                startActivity(new Intent(context, MyFouseActivity.class));
                topPopWindow.dismiss();
                break;
            case R.id.ll_popmenu_collection://我的收藏
                startActivity(new Intent(context, MyCollectionActivity.class));
                topPopWindow.dismiss();
                break;
            case R.id.ll_popmenu_integrated_mall://积分商城
                startActivity(new Intent(context, IntegralMall.class));
                topPopWindow.dismiss();
                break;
            case R.id.ll_popmenu_download://我的下载
                startActivity(new Intent(context, MyDownloadActivity.class));
                topPopWindow.dismiss();
                break;
            default:
                break;
        }
    }

    public void back(View v) {
        finish();
    }

    /**
     * 积分商场Fragment对用的Item点击事件
     * name: 点击查看某个礼品详细
     */
    public void clickCommodity(View view) {
        TextView textView = (TextView) view.findViewById(R.id.commodity_id);
        String id = textView.getText().toString();
        // MyProgressBarDialogTools.show(mContext);
        //跳转到礼品详情
        Intent intent = new Intent(context, Commodity_introduction.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    /**
     * 显示右上角popup菜单
     */
    private void showTopRightPopMenu() {
        if (topPopWindow == null) {
            //(activity,onclicklistener,width,height)
            topPopWindow = new TopPopWindow(MedicineMessageActivity.this, this, 560, 1300);//dialog大小
            //监听窗口的焦点事件，点击窗口外面则取消显示
            topPopWindow.getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        topPopWindow.dismiss();
                    }
                }
            });
        }
        //设置默认获取焦点
        topPopWindow.setFocusable(true);
        //以某个控件的x和y的偏移量位置开始显示窗口
        topPopWindow.showAsDropDown(medicine_menu, 0, -60);//dialog位置
        //如果窗口存在，则更新
        topPopWindow.update();
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


    private void recordUserTimeData() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.recordUserTime);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    String result = HttpClientUtils.sendPost(context, URLConfig.Skyvisit, obj.toString());
                    LogUtil.e("记录用户点击空中拜访时间返回：", result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

}
