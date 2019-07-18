package com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.DateUtil;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;
import com.linlic.ccmtv.yx.widget.SwipeListView;

import org.feezu.liuli.timeselector.TimeSelector;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 教学活动 老师端  培训管理
 * Created by Administrator on 2018/8/20.
 */

public class Training_management3 extends BaseActivity implements PullToRefreshBase.OnRefreshListener2<ScrollView> {

    @Bind(R.id.activity_title_name)
    TextView activityTitleName;
    private Context context;
    @Bind(R.id.training_list)
    SwipeListView training_list;
    @Bind(R.id.training_list2)
    ListView training_list2;
    @Bind(R.id.pull_refresh_scrollview)
    PullToRefreshScrollView mPullRefreshScrollView;
    @Bind(R.id.tranining2_nodata)
    NodataEmptyLayout tranining2_nodata;

    @Bind(R.id.the_activity_type)
    TextView the_activity_type;//活动类型
    @Bind(R.id.the_activity_type_icon)
    ImageView the_activity_type_icon;//活动类型
    @Bind(R.id.time_to_screen)
    TextView time_to_screen;//时间筛选
    @Bind(R.id.time_to_screen_icon)
    ImageView time_to_screen_icon;//
    @Bind(R.id.popwindow1)
    LinearLayout popwindow1;//
    @Bind(R.id.popwindow2)
    LinearLayout popwindow2;//
    @Bind(R.id.that_day)
    TextView that_day;//当天
    @Bind(R.id.current_month)
    TextView current_month;//本月
    @Bind(R.id.recent_half_a_year)
    TextView recent_half_a_year;//最近半年
    @Bind(R.id.in_recent_year)
    TextView in_recent_year;//最近半年
    @Bind(R.id.start_time)
    TextView start_time;//自定义开始时间
    @Bind(R.id.end_time)
    TextView end_time;//自定义结束时间
    @Bind(R.id.time_submit)
    TextView time_submit;//自定义时间 按钮

    public String lecturer_isshow = "2"; //发布教学活动是否需要添加讲师模块
    public String is_teachers = "2"; //宁波李惠利医院 专属 带教老师 1
    public static Boolean is_new_teaching_activities = false;
    private String year_type = "";
    private int page = 1;
    private int limit = 20;
    private int count = 0;
    private boolean istime_submit = false;
    JSONObject result, data;
    private String fid = "";
    private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> listData2 = new ArrayList<Map<String, Object>>();
    private BaseListAdapter baseListAdapterVideo, baseListAdaptertraining_list2;
    private Map<String, Object> select_map = new HashMap<>();
    private Map<String, Object> delpos = null;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 1:
                    try {
                        mPullRefreshScrollView.onRefreshComplete();
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                JSONArray dateJson = dataJson.getJSONObject("dataList").getJSONArray("list");
                                if (page == 1) {
                                    listData.clear();
                                }
                                count = dataJson.getJSONObject("dataList").getInt("count");

                                if (dataJson.getJSONObject("dataList").has("is_speaker")) {
                                    lecturer_isshow = dataJson.getJSONObject("dataList").getString("is_speaker");
                                } else {
                                    lecturer_isshow = "2";
                                }
                                if (dataJson.getJSONObject("dataList").has("is_teachers")) {
                                    is_teachers = dataJson.getJSONObject("dataList").getString("is_teachers");
                                } else {
                                    is_teachers = "2";
                                }

                                for (int i = 0; i < dateJson.length(); i++) {
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("id", dataJson1.getString("id"));
                                    map.put("name", dataJson1.getString("name"));
                                    map.put("place", dataJson1.getString("place"));
                                    map.put("add_time", dataJson1.getString("add_time"));
                                    map.put("end_time", dataJson1.getString("end_time"));
                                    listData.add(map);
                                }
                                baseListAdapterVideo.notifyDataSetChanged();
                                if (page == 1) {
                                    training_list.setSelection(0);
                                }
                            } else {
                                if (page == 1) {
                                    listData.clear();
                                }
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
//
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        setResultStatus(listData.size() > 0, jsonObject.getInt("code"));

                    } catch (Exception e) {
                        e.printStackTrace();
                        setResultStatus(listData.size() > 0, HttpClientUtils.UNKONW_EXCEPTION_CODE);
                    } finally {
//                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                JSONArray dateJson = dataJson.getJSONArray("dataList");
                                listData2.clear();
                                for (int i = 0; i < dateJson.length(); i++) {
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("id", dataJson1.getString("id"));
                                    map.put("type", dataJson1.getString("type"));
                                    map.put("hosid", dataJson1.getString("hosid"));
                                    listData2.add(map);
                                }
                                baseListAdaptertraining_list2.notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
//
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            training_list.hiddenRight(SwipeListView.mCurrentItemView);
                            if (dataJson.getInt("status") == 1) { // 成功
                                listData.remove(delpos);
                                baseListAdapterVideo.notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
//
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        setResultStatus(listData.size() > 0, jsonObject.getInt("code"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        setResultStatus(listData.size() > 0, HttpClientUtils.UNKONW_EXCEPTION_CODE);
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 500:
                    Toast.makeText(context
                            , R.string.post_hint1,
                            Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    setResultStatus(listData.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                    break;

                default:
                    break;
            }
        }
    };

    private void setResultStatus(boolean status, int code) {
        if (status) {
            training_list.setVisibility(View.VISIBLE);
            tranining2_nodata.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                tranining2_nodata.setNetErrorIcon();
            } else {
                tranining2_nodata.setLastEmptyIcon();
            }
            training_list.setVisibility(View.GONE);
            tranining2_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.training_management3);
        context = this;
        ButterKnife.bind(this);
        fid = getIntent().getStringExtra("fid");
        activityTitleName.setText(getIntent().getStringExtra("name"));
        initView();
        getUrlRulest2();
        getUrlRulest();

    }


    public void initView() {
        //默认选中当月
        year_type = current_month.getText().toString();
        current_month.setTextColor(Color.parseColor("#ffffff"));
        current_month.setBackground(getResources().getDrawable(R.drawable.anniu3));

        that_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                istime_submit = false;
                if (year_type.equals(that_day.getText().toString())) {
                    year_type = "";
                    that_day.setTextColor(Color.parseColor("#3798F9"));
                    that_day.setBackground(getResources().getDrawable(R.drawable.anniu18));
                } else {
                    year_type = that_day.getText().toString();
                    that_day.setTextColor(Color.parseColor("#ffffff"));
                    that_day.setBackground(getResources().getDrawable(R.drawable.anniu3));
                }


                current_month.setTextColor(Color.parseColor("#3798F9"));
                current_month.setBackground(getResources().getDrawable(R.drawable.anniu18));
                recent_half_a_year.setTextColor(Color.parseColor("#3798F9"));
                recent_half_a_year.setBackground(getResources().getDrawable(R.drawable.anniu18));
                in_recent_year.setTextColor(Color.parseColor("#3798F9"));
                in_recent_year.setBackground(getResources().getDrawable(R.drawable.anniu18));

                popwindow1.setVisibility(View.GONE);
                popwindow2.setVisibility(View.GONE);
                the_activity_type.setTextColor(Color.parseColor("#666666"));
                the_activity_type_icon.setImageResource(R.mipmap.training_08);
                time_to_screen.setTextColor(Color.parseColor("#666666"));
                time_to_screen_icon.setImageResource(R.mipmap.training_08);
                page = 1;
                getUrlRulest();
            }
        });
        current_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                istime_submit = false;
                if (year_type.equals(current_month.getText().toString())) {
                    year_type = "";
                    current_month.setTextColor(Color.parseColor("#3798F9"));
                    current_month.setBackground(getResources().getDrawable(R.drawable.anniu18));
                } else {
                    year_type = current_month.getText().toString();
                    current_month.setTextColor(Color.parseColor("#ffffff"));
                    current_month.setBackground(getResources().getDrawable(R.drawable.anniu3));
                }

                that_day.setTextColor(Color.parseColor("#3798F9"));
                that_day.setBackground(getResources().getDrawable(R.drawable.anniu18));
                recent_half_a_year.setTextColor(Color.parseColor("#3798F9"));
                recent_half_a_year.setBackground(getResources().getDrawable(R.drawable.anniu18));
                in_recent_year.setTextColor(Color.parseColor("#3798F9"));
                in_recent_year.setBackground(getResources().getDrawable(R.drawable.anniu18));

                popwindow1.setVisibility(View.GONE);
                popwindow2.setVisibility(View.GONE);
                the_activity_type.setTextColor(Color.parseColor("#666666"));
                the_activity_type_icon.setImageResource(R.mipmap.training_08);
                time_to_screen.setTextColor(Color.parseColor("#666666"));
                time_to_screen_icon.setImageResource(R.mipmap.training_08);
                page = 1;
                getUrlRulest();
            }
        });
        recent_half_a_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                istime_submit = false;
                if (year_type.equals(recent_half_a_year.getText().toString())) {
                    year_type = "";
                    recent_half_a_year.setTextColor(Color.parseColor("#3798F9"));
                    recent_half_a_year.setBackground(getResources().getDrawable(R.drawable.anniu18));
                } else {
                    year_type = recent_half_a_year.getText().toString();
                    recent_half_a_year.setTextColor(Color.parseColor("#ffffff"));
                    recent_half_a_year.setBackground(getResources().getDrawable(R.drawable.anniu3));
                }
                current_month.setTextColor(Color.parseColor("#3798F9"));
                current_month.setBackground(getResources().getDrawable(R.drawable.anniu18));
                that_day.setTextColor(Color.parseColor("#3798F9"));
                that_day.setBackground(getResources().getDrawable(R.drawable.anniu18));
                in_recent_year.setTextColor(Color.parseColor("#3798F9"));
                in_recent_year.setBackground(getResources().getDrawable(R.drawable.anniu18));

                popwindow1.setVisibility(View.GONE);
                popwindow2.setVisibility(View.GONE);
                the_activity_type.setTextColor(Color.parseColor("#666666"));
                the_activity_type_icon.setImageResource(R.mipmap.training_08);
                time_to_screen.setTextColor(Color.parseColor("#666666"));
                time_to_screen_icon.setImageResource(R.mipmap.training_08);
                page = 1;
                getUrlRulest();
            }
        });
        in_recent_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                istime_submit = false;
                if (year_type.equals(in_recent_year.getText().toString())) {
                    year_type = "";
                    in_recent_year.setTextColor(Color.parseColor("#3798F9"));
                    in_recent_year.setBackground(getResources().getDrawable(R.drawable.anniu18));
                } else {
                    year_type = in_recent_year.getText().toString();
                    in_recent_year.setTextColor(Color.parseColor("#ffffff"));
                    in_recent_year.setBackground(getResources().getDrawable(R.drawable.anniu3));
                }
                current_month.setTextColor(Color.parseColor("#3798F9"));
                current_month.setBackground(getResources().getDrawable(R.drawable.anniu18));
                that_day.setTextColor(Color.parseColor("#3798F9"));
                that_day.setBackground(getResources().getDrawable(R.drawable.anniu18));
                recent_half_a_year.setTextColor(Color.parseColor("#3798F9"));
                recent_half_a_year.setBackground(getResources().getDrawable(R.drawable.anniu18));

                popwindow1.setVisibility(View.GONE);
                popwindow2.setVisibility(View.GONE);
                the_activity_type.setTextColor(Color.parseColor("#666666"));
                the_activity_type_icon.setImageResource(R.mipmap.training_08);
                time_to_screen.setTextColor(Color.parseColor("#666666"));
                time_to_screen_icon.setImageResource(R.mipmap.training_08);
                page = 1;
                getUrlRulest();
            }
        });
        time_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                istime_submit = true;
                year_type = "";
                in_recent_year.setTextColor(Color.parseColor("#3798F9"));
                in_recent_year.setBackground(getResources().getDrawable(R.drawable.anniu18));
                current_month.setTextColor(Color.parseColor("#3798F9"));
                current_month.setBackground(getResources().getDrawable(R.drawable.anniu18));
                that_day.setTextColor(Color.parseColor("#3798F9"));
                that_day.setBackground(getResources().getDrawable(R.drawable.anniu18));
                recent_half_a_year.setTextColor(Color.parseColor("#3798F9"));
                recent_half_a_year.setBackground(getResources().getDrawable(R.drawable.anniu18));

                popwindow1.setVisibility(View.GONE);
                popwindow2.setVisibility(View.GONE);
                the_activity_type.setTextColor(Color.parseColor("#666666"));
                the_activity_type_icon.setImageResource(R.mipmap.training_08);
                time_to_screen.setTextColor(Color.parseColor("#666666"));
                time_to_screen_icon.setImageResource(R.mipmap.training_08);
                page = 1;
                getUrlRulest();
            }
        });

        start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker("2000-01-01 00:00", "2030-12-31 23:00", start_time);
            }
        });

        end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker("2000-01-01 00:00", "2030-12-31 23:00", end_time);
            }
        });


        baseListAdaptertraining_list2 = new BaseListAdapter(training_list2, listData2, R.layout.item_training_management2) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map map = (Map) item;

                helper.setText(R.id._item2_text1, map.get("type").toString());

            }
        };
        training_list2.setAdapter(baseListAdaptertraining_list2);
        // listview点击事件
        training_list2.setOnItemClickListener(new casesharing_listListener2());


        baseListAdapterVideo = new BaseListAdapter(training_list, listData, R.layout.item_training_management3) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map map = (Map) item;

                helper.setText(R.id.item_content, map.get("name").toString());
                helper.setText(R.id.item_time, map.get("add_time").toString() + " - " + map.get("end_time").toString());
            }
        };
        training_list.setAdapter(baseListAdapterVideo);
        // listview点击事件
        training_list.setOnItemClickListener(new casesharing_listListener());
        baseListAdapterVideo.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > 0) {
                }
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = training_list.getChildAt(0);

                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = training_list.getChildAt(training_list.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == training_list.getHeight()) {
                        if (count > (page * limit)) {
                            page += 1;
                            getUrlRulest();
                        }
                    }
                }
            }
        });


        // listview点击事件
        training_list.setOnItemClickListener(new casesharing_listListener());
        mPullRefreshScrollView.setOnRefreshListener(this);

    }

    private void showDatePicker(String now, String end_time, final TextView etTime) {
        TimeSelector timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                etTime.setText(time.replaceAll("-", "").substring(0, time.length() - 8));
            }
        }, now, end_time);
        timeSelector.setIsLoop(false);
        timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
//        timeSelector.setMode(TimeSelector.MODE.YMDHM);//显示 年月日时分（默认
        timeSelector.show();
    }


    @Override
    protected void onDestroy() {
        SharedPreferencesTools.saveEvent_details_status(context, "");
        super.onDestroy();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        page = 1;
        listData.removeAll(listData);
        getUrlRulest();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        if (count > (page * limit)) {
            page += 1;
            getUrlRulest();
        } else {
            mPullRefreshScrollView.onRefreshComplete();
            Toast.makeText(context, "暂无更多数据", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * name: 点击查看某个视频的详细 author:Tom 2016-1-28下午3:42:08
     */
    private class casesharing_listListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {

            Map map = listData.get(arg2);
            Intent intent = null;

            intent = new Intent(context, Event_Details4.class);

            intent.putExtra("is_teachers", is_teachers);
            intent.putExtra("id", map.get("id").toString());
            intent.putExtra("fid", fid);
            intent.putExtra("lecturer_isshow", lecturer_isshow);

            if (intent != null) {
                startActivity(intent);
            }

        }

    }

    /**
     * name: 点击查看某个视频的详细 author:Tom 2016-1-28下午3:42:08
     */
    private class casesharing_listListener2 implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {

            select_map = listData2.get(arg2);
            popwindow1.setVisibility(View.GONE);
            the_activity_type.setTextColor(Color.parseColor("#666666"));
            the_activity_type_icon.setImageResource(R.mipmap.training_08);
            time_to_screen.setTextColor(Color.parseColor("#666666"));
            time_to_screen_icon.setImageResource(R.mipmap.training_08);
            page = 1;
            getUrlRulest();

        }

    }


    public void getUrlRulest() {
//        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getActivities);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    obj.put("fid", fid);
                    obj.put("page", page);
                    obj.put("limit", limit);
                    if (select_map.containsKey("id")) {
                        obj.put("type", select_map.get("id").toString());
                    }
                    if (year_type.length() > 0) {
                        switch (year_type) {
                            case "当天":
                                obj.put("year_type", 1);
                                break;
                            case "本月":
                                obj.put("year_type", 2);
                                break;
                            case "最近半年":
                                obj.put("year_type", 3);
                                break;
                            case "最近一年":
                                obj.put("year_type", 4);
                                break;
                            default:
                                break;
                        }
                    }
                    if (istime_submit) {
                        obj.put("year_type", 5);
                        LogUtil.e("选择时间", start_time.getText().toString() + "    " + end_time.getText().toString());
                        LogUtil.e("选择时间", DateUtil.format(start_time.getText().toString(), "-", "yyyy-MM-dd") + "    " + DateUtil.format(end_time.getText().toString(), "-", "yyyy-MM-dd"));
                        obj.put("year", DateUtil.format(start_time.getText().toString(), "-", "yyyy-MM-dd") + "~" + DateUtil.format(end_time.getText().toString(), "-", "yyyy-MM-dd"));
                    }
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("取教学活动列表", result);
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

    public void getUrlRulest2() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getTypes);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    obj.put("fid", fid);

                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("获取教学活动类型", result);
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

    public void popwindow(View view) {
        if (popwindow1.getVisibility() == View.VISIBLE) {
            popwindow1.setVisibility(View.GONE);
            the_activity_type.setTextColor(Color.parseColor("#666666"));
            the_activity_type_icon.setImageResource(R.mipmap.training_08);
        } else {
            popwindow1.setVisibility(View.VISIBLE);
            popwindow2.setVisibility(View.GONE);
            the_activity_type.setTextColor(Color.parseColor("#3698F9"));
            the_activity_type_icon.setImageResource(R.mipmap.training_07);
            time_to_screen.setTextColor(Color.parseColor("#666666"));
            time_to_screen_icon.setImageResource(R.mipmap.training_08);
        }
    }

    public void popwindow2(View view) {
        if (popwindow2.getVisibility() == View.VISIBLE) {
            popwindow2.setVisibility(View.GONE);
            time_to_screen.setTextColor(Color.parseColor("#666666"));
            time_to_screen_icon.setImageResource(R.mipmap.training_08);
        } else {
            popwindow2.setVisibility(View.VISIBLE);
            popwindow1.setVisibility(View.GONE);
            the_activity_type.setTextColor(Color.parseColor("#666666"));
            the_activity_type_icon.setImageResource(R.mipmap.training_08);
            time_to_screen.setTextColor(Color.parseColor("#3698F9"));
            time_to_screen_icon.setImageResource(R.mipmap.training_07);
        }
    }

    public void ishide(View view) {
        LinearLayout linearLayout = (LinearLayout) view.getParent();
        linearLayout.setVisibility(View.GONE);
        the_activity_type.setTextColor(Color.parseColor("#666666"));
        the_activity_type_icon.setImageResource(R.mipmap.training_08);
        time_to_screen.setTextColor(Color.parseColor("#666666"));
        time_to_screen_icon.setImageResource(R.mipmap.training_08);
    }

    public void new_teaching_activities(View view) {
        Intent intent = new Intent(this, New_teaching_activities.class);
        intent.putExtra("fid", fid);
        intent.putExtra("lecturer_isshow", lecturer_isshow);
        intent.putExtra("is_teachers", is_teachers);
        startActivity(intent);
    }


}
