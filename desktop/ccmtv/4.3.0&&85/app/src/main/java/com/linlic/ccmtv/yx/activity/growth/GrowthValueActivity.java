package com.linlic.ccmtv.yx.activity.growth;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.growth.adapter.GrowthValueAdapter;
import com.linlic.ccmtv.yx.activity.growth.bean.GrowthRecyclerItemBean;
import com.linlic.ccmtv.yx.activity.growth.bean.GrowthValueBean;
import com.linlic.ccmtv.yx.activity.medal.utils.MyHandler;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.kzbf.widget.MyProgessLine;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class GrowthValueActivity extends BaseActivity implements View.OnClickListener {
    private RecyclerView growth_demand;
    private Context context;
    private List<GrowthRecyclerItemBean> valuebeanList = new ArrayList<>();

    private MyHandler handler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    GrowthValueBean valueBean = new Gson().fromJson(String.valueOf(msg.obj), GrowthValueBean.class);
                    if (valueBean != null) setValue(valueBean);
                    break;
                default:
                    break;
            }
        }
    };
    private GrowthValueAdapter valueAdapter;
    private LinearLayout user_parent;
    private ImageView user_icon;
    private ImageView official_icon;
    private TextView official_name;
    private TextView username;
    private TextView tag_name;
    private MyProgessLine progress_line;

    public void setValue(GrowthValueBean value) {
        //用户详情展示
        GrowthValueBean.DataBean valueData = value.getData();
        progress_line.setLeftDesc(valueData.getCur_jyz());
        progress_line.setRightDesc(valueData.getNext_gradejyz());
        progress_line.setProgress(valueData.getJyz_per() * 1.0f);
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.growth_value_task_offcial_icon_default);
        Glide.with(context)
                .asBitmap()
                .load(valueData.getCur_hatpic())
                .apply(options)
                .into(official_icon);
        RequestOptions options1 = new RequestOptions().placeholder(R.mipmap.img_default2);
        Glide.with(context)
                .asBitmap()
                .load(valueData.getUserface())
                .apply(options1)
                .into(user_icon);
        tag_name.setText(valueData.getCur_rank_name());
        official_name.setText(valueData.getCur_grade());
        username.setText(valueData.getUsername());
        //用户成长等级展示Banner
        List<GrowthValueBean.UserRankInfoBean> userRankInfo = value.getUserRankInfo();
        if (userRankInfo != null && userRankInfo.size() > 0) {
            valuebeanList.add(new GrowthRecyclerItemBean(GrowthValueAdapter.BANNER_SHADOW_UPPER, null));
            valuebeanList.add(new GrowthRecyclerItemBean(GrowthValueAdapter.BANNER_TAG, userRankInfo));
            valuebeanList.add(new GrowthRecyclerItemBean(GrowthValueAdapter.BANNER_SHADOW_LOWER, null));
        }
        //经验值攻略
        List<GrowthValueBean.UserRankRuleBean> userRankRule = value.getUserRankRule1();
        if (userRankRule != null && userRankRule.size() > 0) {
            int size = userRankRule.size();
            valuebeanList.add(new GrowthRecyclerItemBean(GrowthValueAdapter.LIST_GROWTH_STRATEGY_TITLE, null));
            for (int i = 0; i < size; i++) {
                valuebeanList.add(new GrowthRecyclerItemBean(GrowthValueAdapter.LIST_DEFAULT, userRankRule.get(i)));

            }
        }
        //经验值点数详情
        List<GrowthValueBean.UserRankRuleBean> userRankRule2 = value.getUserRankRule2();
        if (userRankRule2 != null && userRankRule2.size() > 0) {
            int size = userRankRule2.size();
            valuebeanList.add(new GrowthRecyclerItemBean(GrowthValueAdapter.LIST_GROWTH_VALUE_TITLE, null));
            for (int i = 0; i < size; i++) {
                valuebeanList.add(new GrowthRecyclerItemBean(GrowthValueAdapter.LIST_DEFAULT, userRankRule2.get(i)));
            }
        }
        //当前已经获得的经验值数值
        valueAdapter.setCurrentGrowthValue(Integer.parseInt(valueData.getCur_jyz()));
        startPositions.clear();//清空标识的点  这是计算间隔时用到的
        valueAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_growth_valuectivity);
        context = this;
        //沉浸式状态栏
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            ViewGroup contentView = (ViewGroup) window.getDecorView().findViewById(Window.ID_ANDROID_CONTENT);
            contentView.getChildAt(0).setFitsSystemWindows(false);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup contentView = (ViewGroup) window.getDecorView().findViewById(Window.ID_ANDROID_CONTENT);
            contentView.getChildAt(0).setFitsSystemWindows(false);
        }

        growth_demand = (RecyclerView) findViewById(R.id.growth_value_demand);
        user_parent = (LinearLayout) findViewById(R.id.growth_value_user_center_parent);
        user_icon = (ImageView) findViewById(R.id.growth_value_user_icon);
        username = (TextView) findViewById(R.id.growth_value_username);
        official_icon = (ImageView) findViewById(R.id.growth_value_official_icon);
        official_name = (TextView) findViewById(R.id.growth_value_official_name);
        tag_name = (TextView) findViewById(R.id.growth_value_offcial_tag_name);
        progress_line = (MyProgessLine) findViewById(R.id.growth_value_offcial_progress_line);
        findViewById(R.id.growth_value_toolbar_back).setOnClickListener(this);

        int titlebarPadding = getTitlebarPadding();
        initToolbar(titlebarPadding);
        initData();
        getMedalDetial();
    }

    private void initToolbar(int titlebarPadding) {
        final Toolbar tb_toolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        tb_toolbar.setPadding(0, titlebarPadding, 0, 0);
        AppBarLayout mAppBarLayout = (AppBarLayout) findViewById(R.id.growth_value_appbarlayout);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                tb_toolbar.setBackgroundColor(changeAlpha(getResources().getColor(R.color.toolbarBgColor), Math.abs(verticalOffset * 1.0f) / appBarLayout.getTotalScrollRange()));
            }
        });
        user_parent.setPadding(0, titlebarPadding, 0, 0);
    }

    /**
     * 根据百分比改变颜色透明度
     */
    public int changeAlpha(int color, float fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = (int) (Color.alpha(color) * fraction);
        return Color.argb(alpha, red, green, blue);
    }

    private int startPosition = 0;//LIST_DEFAULT item开始的位置
    private SparseArray<Integer> startPositions = new SparseArray<>();//保存DEFAULT item初始化的位置,item的Position- startPosition 获取item在单一DEFAULT状态下的真正位置

    private void initData() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (valuebeanList.get(position).getType() == GrowthValueAdapter.LIST_DEFAULT) {
                    return 1;//默认条目占一个位置  （屏幕的1/3 )
                } else {
                    return 3;//默认条目占三个位置  （满屏幕)
                }
            }
        });
        growth_demand.setLayoutManager(layoutManager);
        //设置GridLayout间隔
        growth_demand.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildLayoutPosition(view);
                GrowthRecyclerItemBean bean = valuebeanList.get(position);
                if (bean.getType() == GrowthValueAdapter.LIST_DEFAULT) {
                    if (valuebeanList.get(position - 1).getType() != GrowthValueAdapter.LIST_DEFAULT)
                        startPosition = position;
                    if (startPositions.get(position) == null) {
                        startPositions.put(position, startPosition);
                    }
                    outRect.bottom = 15;
                    int i = Math.abs(position - startPositions.get(position)) % 3;
                    switch (i) {
                        case 0://最左边的Item
                            outRect.left = 30;
                            break;
                        case 1://中间的Item
                            outRect.left = 15;
                            outRect.right = 15;
                            break;
                        case 2://最右边的Item
                            outRect.right = 30;
                            break;
                        default:
                            break;
                    }
                }
                if (bean.getType() == GrowthValueAdapter.LIST_GROWTH_STRATEGY_TITLE) {
                    outRect.top = 20;
                }
            }
        });
        valueAdapter = new GrowthValueAdapter(valuebeanList, this);
        growth_demand.setAdapter(valueAdapter);
    }

    private void getMedalDetial() {
        final String uid = SharedPreferencesTools.getUid(context);
        if (TextUtils.isEmpty(uid)) return;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getUserRankInfo);
                    obj.put("uid", uid);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP, obj.toString());
                    Message message = new Message();
                    message.what = 0;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }


    /**
     * 获取StatusBar的高度
     */
    private int getTitlebarPadding() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.growth_value_toolbar_back:
                finish();
                break;
            default:
                break;
        }
    }
}
