package com.linlic.ccmtv.yx.activity.conference;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.Popular_search;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.conference.adapter.ConferenceAdapter;
import com.linlic.ccmtv.yx.activity.conference.adapter.ConferenceDepartmentAdapter;
import com.linlic.ccmtv.yx.activity.conference.adapter.ConferenceTimeAdapter;
import com.linlic.ccmtv.yx.activity.conference.databean.ConferenceBean;
import com.linlic.ccmtv.yx.activity.conference.databean.ConferenceDepartmentBean;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.GlideImageLoader;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyListView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConferenceMainActivity extends BaseActivity {

    private Context mContext;
    private TextView title_name;
    private ImageView ivTitleIconRight;
    private TextView conferenceDepartmentAll;
    private TextView conferenceTimeAll;
    private Banner banner;
    private RecyclerView recyclerViewDepartment;
    private RecyclerView recyclerViewYear;
    private MyListView lvConference;
    private LinearLayout llConferenceRecyclerview, llTimeRecyclerview, llActivityTitle8;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private ImageView ivActivityTitle8;
    private NodataEmptyLayout lt_nodata1;
    private CoordinatorLayout coordinatorLayout;

    private NestedScrollView nestedScrollView;
    private List<String> images = new ArrayList<>();
    private List<ConferenceDepartmentBean> departmentDatas = new ArrayList<>();
    private List<String> timeDatas = new ArrayList<>();
    private List<ConferenceBean> conferenceBeanList = new ArrayList<>();
    private List<ConferenceBean> conferenceBannerList = new ArrayList<>();
    private ConferenceDepartmentAdapter conferenceDepartmentAdapter;
    private ConferenceTimeAdapter conferenceTimeAdapter;
    private ConferenceAdapter conferenceAdapter;
    private SystemBarTintManager tintManager;
    private JSONObject result, data;
    private String departmentParam = "";
    private String timeParam = "";
    private boolean isNoMore = false;
    private int page = 1;
    private BaseListAdapter baseListAdapter;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        departmentDatas.clear();
                        timeDatas.clear();
                        result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {// 成功
                            data = result.getJSONObject("data");
                            if (data.has("keshi")) {
                                JSONArray keshiArray = data.getJSONArray("keshi");
                                for (int i = 0; i < keshiArray.length(); i++) {
                                    JSONObject keshiObject = keshiArray.getJSONObject(i);
                                    ConferenceDepartmentBean departmentBean = new ConferenceDepartmentBean();
                                    departmentBean.setId(keshiObject.getString("id"));
                                    departmentBean.setKid(keshiObject.getString("kid"));
                                    departmentBean.setKname(keshiObject.getString("kname"));
                                    departmentDatas.add(departmentBean);
                                }
                            }
                            if (data.has("time")) {
                                JSONArray timeArray = data.getJSONArray("time");
                                for (int i = 0; i < timeArray.length(); i++) {
                                    String time = timeArray.getString(i);
                                    timeDatas.add(time);
                                }
                            }

                            conferenceDepartmentAdapter.notifyDataSetChanged();
                            conferenceTimeAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(mContext, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {// 成功
                            MyProgressBarDialogTools.hide();
                            JSONArray dataConference = result.getJSONArray("data");
                            if (dataConference == null || dataConference.length() < 10) {
                                isNoMore = true;
                            }
                            for (int i = 0; i < dataConference.length(); i++) {
                                JSONObject conferenceObject = dataConference.getJSONObject(i);
                                ConferenceBean conferenceBean = new ConferenceBean();
                                conferenceBean.setId(conferenceObject.getString("id"));
                                conferenceBean.setTitle(conferenceObject.getString("title"));
                                conferenceBean.setIconUrl(conferenceObject.getString("picurl"));
                                conferenceBean.setCollectStatus(conferenceObject.getString("collection"));  //是否收藏  0为未收藏     1为已收藏
                                conferenceBeanList.add(conferenceBean);
                            }
                            baseListAdapter.notifyDataSetChanged();

                            JSONArray dataConferenceBanner = result.getJSONArray("ccmtvactivitytivityArr");
                            if (dataConferenceBanner != null && dataConferenceBanner.length() > 0) {
                                images.clear();
                                conferenceBannerList.clear();
                                for (int i = 0; i < dataConferenceBanner.length(); i++) {
                                    JSONObject conferenceBannerObject = dataConferenceBanner.getJSONObject(i);
                                    ConferenceBean conferenceBean = new ConferenceBean();
                                    conferenceBean.setId(conferenceBannerObject.getString("id"));
                                    conferenceBean.setTitle(conferenceBannerObject.getString("title"));
                                    conferenceBean.setIconUrl(conferenceBannerObject.getString("picurl"));
                                    conferenceBannerList.add(conferenceBean);
                                    images.add(conferenceBean.getIconUrl());
                                }
                                initBanner();
                            }
                        } else {
                            MyProgressBarDialogTools.hide();
                            isNoMore = true;// 失败
                            Toast.makeText(mContext, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        setResultStatus(conferenceBeanList.size() > 0, result.getInt("status"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        setResultStatus(conferenceBeanList.size() > 0, HttpClientUtils.UNKONW_EXCEPTION_CODE);
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 500:
                    Toast.makeText(mContext, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    setResultStatus(conferenceBeanList.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOST);
                    break;
                default:
                    break;
            }
        }
    };

    private void setResultStatus(boolean status, int code) {
        collapsingToolbarLayout.setVisibility(images.size() == 0 ? View.GONE : View.VISIBLE);
        llConferenceRecyclerview.setVisibility(departmentDatas.size() == 0 ? View.GONE : View.VISIBLE);
        llTimeRecyclerview.setVisibility(timeDatas.size() == 0 ? View.GONE : View.VISIBLE);
        if (status) {
            lvConference.setVisibility(View.VISIBLE);
            lt_nodata1.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(mContext, code)) {
                lt_nodata1.setNetErrorIcon();
            } else {
                lt_nodata1.setLastEmptyIcon();
            }
            lvConference.setVisibility(View.GONE);
            lt_nodata1.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.white);//通知栏所需颜色
        }
        setContentView(R.layout.activity_conference_main);

        mContext = ConferenceMainActivity.this;
        findId();
        initData();
        //initBanner();
        initListView();
        initRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        page = 1;
    }

    public void findId() {
        title_name = (TextView) findViewById(R.id.activity_title_name);
        llActivityTitle8 = (LinearLayout) findViewById(R.id.id_ll_activity_title_8);
        conferenceDepartmentAll = (TextView) findViewById(R.id.id_tv_conference_department_all);
        conferenceTimeAll = (TextView) findViewById(R.id.id_tv_conference_time_all);
        banner = (Banner) findViewById(R.id.id_conference_banner);
        recyclerViewDepartment = (RecyclerView) findViewById(R.id.id_recyclerview_conference_department);
        recyclerViewYear = (RecyclerView) findViewById(R.id.id_recyclerview_conference_time);
        lvConference = (MyListView) findViewById(R.id.id_lv_conference);
        nestedScrollView = (NestedScrollView) findViewById(R.id.id_nested_scrollview_conference);
        llConferenceRecyclerview = (LinearLayout) findViewById(R.id.id_ll_conference_recyclerview);
        llTimeRecyclerview = (LinearLayout) findViewById(R.id.id_ll_time_recyclerview);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        ivActivityTitle8 = (ImageView) findViewById(R.id.id_iv_activity_title_8);
        lt_nodata1 = (NodataEmptyLayout) findViewById(R.id.lt_nodata1);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.id_coordinator_layout_conference_main);
        ivTitleIconRight = (ImageView) findViewById(R.id.id_iv_activity_title_8_right);

        ivTitleIconRight.setVisibility(View.VISIBLE);
        ivTitleIconRight.setImageResource(R.mipmap.conference_search_gray);
        ivTitleIconRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Popular_search.class);
                intent.putExtra("mode", "1");//方式 1 表示从首页title-img 进入到热门搜索页 2 表示从搜索也进入到搜索页
                startActivity(intent);
            }
        });

        conferenceDepartmentAll.setBackgroundResource(R.drawable.conference_condition_selected);
        conferenceTimeAll.setBackgroundResource(R.drawable.conference_condition_selected);

        conferenceDepartmentAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conferenceDepartmentAll.setBackgroundResource(R.drawable.conference_condition_selected);
                conferenceDepartmentAdapter.setSelected(-1);
                departmentParam = "";
                page = 1;
                getConferenceAll();
            }
        });

        conferenceTimeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conferenceTimeAll.setBackgroundResource(R.drawable.conference_condition_selected);
                conferenceTimeAdapter.setSelected(-1);
                timeParam = "";
                page = 1;
                getConferenceAll();
            }
        });

        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
//                Log.d("STATE", state.name());
                if (state == State.EXPANDED) {

                    //展开状态
                    llActivityTitle8.setBackgroundColor(Color.WHITE);
                    title_name.setTextColor(Color.BLACK);
                    ivActivityTitle8.setImageResource(R.mipmap.login_back);
                    ivTitleIconRight.setImageResource(R.mipmap.conference_search_gray);
                    if (tintManager != null) {
                        tintManager.setStatusBarTintResource(R.color.white);//通知栏所需颜色
                    }

                } else if (state == State.COLLAPSED) {

                    //折叠状态
                    llActivityTitle8.setBackgroundColor(Color.parseColor("#3698F9"));
                    title_name.setTextColor(Color.WHITE);
                    ivActivityTitle8.setImageResource(R.mipmap.back_inva);
                    ivTitleIconRight.setImageResource(R.mipmap.conference_search_white);
                    if (tintManager != null) {
                        tintManager.setStatusBarTintResource(R.color.barcolor);//通知栏所需颜色
                    }
                } else {

                    //中间状态
                    llActivityTitle8.setBackgroundColor(Color.WHITE);
                    title_name.setTextColor(Color.BLACK);
                    ivActivityTitle8.setImageResource(R.mipmap.login_back);
                    ivTitleIconRight.setImageResource(R.mipmap.conference_search_gray);
                    if (tintManager != null) {
                        tintManager.setStatusBarTintResource(R.color.white);//通知栏所需颜色
                    }
                }
            }
        });
    }


    private void initBanner() {
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(2000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
//                Log.e("会议专题banner位置", position + "  =================");
                String bannerId = conferenceBannerList.get(position).getId();
                Intent intent = new Intent(ConferenceMainActivity.this, ConferenceDetailActivity.class);
                intent.putExtra("conferenceId", bannerId);
                intent.putExtra("title",conferenceBannerList.get(position).getTitle());
                startActivity(intent);

            }
        });
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    private void initData() {
        title_name.setText("会议");

        getDepartmentAndTime();
        getConferenceAll();
    }

    private void getConferenceAll() {
        MyProgressBarDialogTools.show(mContext);
        isNoMore = false;
        if (page == 1) {
            conferenceBeanList.clear();
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.specialIndex);
                    obj.put("uid", SharedPreferencesTools.getUid(mContext));
                    obj.put("keyword", departmentParam);
                    obj.put("time", timeParam);
                    obj.put("page", page);
                    String result = HttpClientUtils.sendPost(mContext, URLConfig.conference, obj.toString());
//                    LogUtil.e("会议首页数据：", result);

                    Message message = new Message();
                    message.what = 2;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    private void getDepartmentAndTime() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.classification);
                    String result = HttpClientUtils.sendPost(mContext, URLConfig.conference, obj.toString());
//                    LogUtil.e("会议科室与时间数据：", result);

                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    private void initRecyclerView() {
        //创建LinearLayoutManager
        LinearLayoutManager manager = new LinearLayoutManager(this);
        LinearLayoutManager manager2 = new LinearLayoutManager(this);
        //设置
        recyclerViewDepartment.setLayoutManager(manager);
        recyclerViewYear.setLayoutManager(manager2);
        //设置为横向滑动
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        manager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        //实例化适配器
        conferenceDepartmentAdapter = new ConferenceDepartmentAdapter(this, departmentDatas);
        conferenceTimeAdapter = new ConferenceTimeAdapter(this, timeDatas);
        //设置适配器
        recyclerViewDepartment.setAdapter(conferenceDepartmentAdapter);
        recyclerViewYear.setAdapter(conferenceTimeAdapter);

        conferenceDepartmentAdapter.setOnItemClickListener(new ConferenceDepartmentAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                conferenceDepartmentAll.setBackgroundColor(Color.TRANSPARENT);
                departmentParam = departmentDatas.get(position).getKname();
                page = 1;
                getConferenceAll();
            }
        });

        conferenceTimeAdapter.setOnItemClickListener(new ConferenceTimeAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                conferenceTimeAll.setBackgroundColor(Color.TRANSPARENT);
                timeParam = timeDatas.get(position);
                page = 1;
                getConferenceAll();
            }
        });

    }


    private void initListView() {
        baseListAdapter = new BaseListAdapter(lvConference, conferenceBeanList, R.layout.item_conference_main_list) {
            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                ConferenceBean conferenceBean = (ConferenceBean) item;
                helper.setText(R.id.id_tv_item_conference_main_list_title, ((ConferenceBean) item).getTitle());
                helper.setText(R.id.id_tv_item_conference_main_list_time, ((ConferenceBean) item).getTime());
                if ("1".equals(conferenceBean.getCollectStatus())) {
                    helper.setImageResource(R.id.id_iv_item_conference_main_list_collect, R.mipmap.ic_conference_collect);
                } else {
                    helper.setImageResource(R.id.id_iv_item_conference_main_list_collect, R.mipmap.ic_conference_uncollect);
                }
                helper.setConferenceCollectOnClick(R.id.id_iv_item_conference_main_list_collect, (ConferenceBean) item);
                helper.setImageBitmapGlide(mContext, R.id.id_iv_item_conference_main_list_pic, ((ConferenceBean) item).getIconUrl());
            }
        };

        lvConference.setAdapter(baseListAdapter);
        lvConference.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String conferenceId = conferenceBeanList.get(position).getId();
                Intent intent = new Intent(ConferenceMainActivity.this, ConferenceDetailActivity.class);
                intent.putExtra("conferenceId", conferenceId);
                startActivity(intent);
            }
        });

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int x, int y, int oldScrollX, int oldScrollY) {
                int scrollY = v.getScrollY();//顶端以及滑出去的距离
                int height = v.getHeight();//界面的高度
                int scrollViewMeasuredHeight = v.getChildAt(0).getMeasuredHeight();//scrollview所占的高度
                if (scrollY == 0) {//在顶端的时候
//                    Toast.makeText(getContext(),"在顶端的时候  ", Toast.LENGTH_SHORT).show();
                } else if ((scrollY + height) == scrollViewMeasuredHeight) {//当在底部的时候
//                    Toast.makeText(getContext(),"当在底部的时候    ", Toast.LENGTH_SHORT).show();
                    if (!isNoMore) {
                        page++;
                        getConferenceAll();
                    }
                } else {//当在中间的时候
//                    Toast.makeText(getContext(),"当在中间的时候      ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void back(View view) {
        finish();
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
