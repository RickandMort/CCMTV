package com.linlic.ccmtv.yx.activity.my.medical_examination;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseFragment;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.kzbf.utils.SkyVisitUtils;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 考试列表
 * Created by Administrator on 2017/9/6.
 */
public class My_exams_over extends BaseFragment {
    private Context context;
    //用户统计
    private String entertime, leavetime, pid;
    public static String enterUrl;
    private ListView exams_list3;
    BaseListAdapter baseListAdapter;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private boolean isNoMore = false;
    private int page = 1;
    private int pause = 0;
    private NodataEmptyLayout rl_nodata3;
    //    private SearchView editText1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject customJson = dataArray.getJSONObject(i);
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("my_exams_name", customJson.getString("paper_name"));
                                map.put("my_exams_take", customJson.getString("paper_status"));
                                map.put("my_exams_time", customJson.getString("paper_time"));
                                map.put("my_exams_id", customJson.getString("pid"));
                                map.put("calendar_mth", customJson.getString("calendar_mth"));
                                map.put("calendar_day", customJson.getString("calendar_day"));
                                map.put("calendar_new", customJson.getString("show_type"));
                                map.put("type", customJson.getString("type"));
                                map.put("type_name", customJson.getString("type_name"));
                                map.put("exam_score", customJson.getString("exam_score"));
                                map.put("cate_name", customJson.getString("cate_name"));
                                map.put("score_status", customJson.getString("score_status"));
                                map.put("start_end_time", customJson.getString("start_end_time"));
                                map.put("is_exercise", customJson.getString("is_exercise"));//1为模拟考试 0为正式考试
                                map.put("status", customJson.getString("status"));//为1时表示补考考试
                                map.put("is_cheat", customJson.has("is_cheat")?customJson.getString("is_cheat"):"0");//为1时表示作弊
                                map.put("score_show", customJson.has("score_show")?customJson.getString("score_show"):"1");//为1时表示显示
                                data.add(map);
                            }
//                            if (data.size() > 0) {
//                                exams_list3.setVisibility(View.VISIBLE);
//                                rl_nodata3.setVisibility(View.GONE);
//                            } else {
//                                exams_list3.setVisibility(View.GONE);
//                                rl_nodata3.setVisibility(View.VISIBLE);
//                            }
                            if (dataArray.length() < 10) {
                                isNoMore = true;
                            }
                        } else {
                            isNoMore = true;
//                            if (data.size() > 0) {
//                                exams_list3.setVisibility(View.VISIBLE);
//                                rl_nodata3.setVisibility(View.GONE);
//                            } else {
//                                exams_list3.setVisibility(View.GONE);
//                                rl_nodata3.setVisibility(View.VISIBLE);
//                            }
                        }
                        baseListAdapter.notifyDataSetChanged();
                        setResultStatus(data.size() > 0, jsonObject.getInt("status"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    setResultStatus(data.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOST);
                    break;
                default:
                    break;
            }
        }
    };

    private void setResultStatus(boolean status, int code) {
        if (status) {
            exams_list3.setVisibility(View.VISIBLE);
            rl_nodata3.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                rl_nodata3.setNetErrorIcon();
            } else {
                rl_nodata3.setLastEmptyIcon();
            }
            exams_list3.setVisibility(View.GONE);
            rl_nodata3.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_exams3, container, false);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();

        findId();
        initdata();
        setValue2();
    }

    @Override
    public void findId() {
        super.findId();
        exams_list3 = (ListView) getActivity().findViewById(R.id.exams_list3);
        rl_nodata3 = (NodataEmptyLayout) getActivity().findViewById(R.id.rl_nodata3);
    }

    public void initdata() {
        pid = getActivity().getIntent().getStringExtra("pid");
        baseListAdapter = new BaseListAdapter(exams_list3, data, R.layout.exams_list_item) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);

                helper.setText(R.id.my_exams_name, ((Map) item).get("my_exams_name") + "", "html");
                helper.setText(R.id.my_exams_time, ((Map) item).get("my_exams_time") + "");
                helper.setText(R.id.my_exams_id, ((Map) item).get("my_exams_id") + "");
                helper.setText(R.id.my_exams_time, ((Map) item).get("start_end_time") + "");
                helper.setText(R.id.fraction, ((Map) item).get("exam_score") + "");
                if (((Map) item).get("cate_name").toString().trim().length() > 0) {
                    helper.setText(R.id.my_exams_type, ((Map) item).get("cate_name") + "");
                    helper.setViewVisibility(R.id.my_exams_type, View.VISIBLE);
                } else {
                    helper.setText(R.id.my_exams_type, ((Map) item).get("cate_name") + "");
                    helper.setViewVisibility(R.id.my_exams_type, View.GONE);
                }
                if (((Map) item).get("score_status").equals("0")) {
                    helper.setTextColor2(R.id.fraction, Color.parseColor("#999999"));//分数 颜色
                    helper.setTextColor2(R.id.fraction_text, Color.parseColor("#999999"));//分数 颜色
                } else {
                    helper.setTextColor2(R.id.fraction, Color.parseColor("#3897f9"));//分数 颜色
                    helper.setTextColor2(R.id.fraction_text, Color.parseColor("#3897f9"));//分数 颜色
                }
                helper.setVisibility(R.id.fraction_layout, View.VISIBLE);
                helper.setVisibility(R.id.schedule_layout, View.GONE);
                if (Integer.parseInt(((Map) item).get("calendar_new").toString()) > 0) {
                    helper.setVisibility(R.id.calendar_new, View.VISIBLE);
                } else {
                    helper.setVisibility(R.id.calendar_new, View.GONE);
                }
              /*  if (((Map) item).containsKey("type")) {
                    switch (((Map) item).get("type") + "") {
                        case "1":
                            helper.setViewBG(R.id.calendar_layout, getResources().getDrawable(R.mipmap.exams1_06));//设置日历背景
                            helper.setViewBG(R.id.my_exams_take, getResources().getDrawable(R.mipmap.exams1_12));//设置考试状态背景
                            helper.setTextColor(R.id.my_exams_take, R.color.exams_list_item_text_color7);//设置考试状态 字体颜色 #FCA962
                            break;
                        case "2":
                            helper.setViewBG(R.id.calendar_layout, getResources().getDrawable(R.mipmap.exams1_05));//设置日历背景
                            helper.setViewBG(R.id.my_exams_take, getResources().getDrawable(R.mipmap.exams1_17));//设置考试状态背景
                            helper.setTextColor(R.id.my_exams_take, R.color.exams_list_item_text_color2);//设置考试状态 字体颜色 #FCA962
                            break;
                        case "3":
                            helper.setViewBG(R.id.calendar_layout, getResources().getDrawable(R.mipmap.exams1_04));//设置日历背景
                            helper.setViewBG(R.id.my_exams_take, getResources().getDrawable(R.mipmap.exams1_23));//设置考试状态背景
                            helper.setTextColor(R.id.my_exams_take, R.color.exams_list_item_text_color3);//设置考试状态 字体颜色 #FCA962
                            break;
                        case "4":
                            helper.setViewBG(R.id.calendar_layout, getResources().getDrawable(R.mipmap.exams1_07));//设置日历背景
                            helper.setViewBG(R.id.my_exams_take, getResources().getDrawable(R.mipmap.exams1_11));//设置考试状态背景
                            helper.setTextColor(R.id.my_exams_take, R.color.exams_list_item_text_color1);//设置考试状态 字体颜色 #FCA962
                            break;
                        default:
                            helper.setViewBG(R.id.calendar_layout, getResources().getDrawable(R.mipmap.exams1_07));//设置日历背景
                            helper.setViewBG(R.id.my_exams_take, getResources().getDrawable(R.mipmap.exams1_11));//设置考试状态背景
                            helper.setTextColor(R.id.my_exams_take, R.color.exams_list_item_text_color1);//设置考试状态 字体颜色 #FCA962
                            break;
                    }
                }*/
                if ( ((Map) item).get("status").toString().equals("1") ) {
                    helper.setVisibility(R.id.exams_type3, View.VISIBLE);
                } else {
                    helper.setVisibility(R.id.exams_type3, View.GONE);
                }

                if ( ((Map) item).get("is_exercise").toString().equals("1") ) {
                    helper.setVisibility(R.id.exams_type2, View.VISIBLE);
                    helper.setVisibility(R.id.exams_type1, View.GONE);
                } else {
                    helper.setVisibility(R.id.exams_type2, View.GONE);
                    helper.setVisibility(R.id.exams_type1, View.VISIBLE);
                }
                if ( ((Map) item).get("is_cheat").toString().equals("1") ) {
                    helper.setVisibility(R.id.leantextview, View.VISIBLE);
                } else {
                    helper.setVisibility(R.id.leantextview, View.GONE);
                }
                if ( ((Map) item).get("score_show").toString().equals("1") ) {
                    helper.setVisibility(R.id.fraction_layout1, View.VISIBLE);
                    helper.setVisibility(R.id.fraction2, View.GONE);
                } else {
                    helper.setVisibility(R.id.fraction_layout1, View.GONE);
                    helper.setVisibility(R.id.fraction2, View.VISIBLE);
                }
            }
        };
        exams_list3.setAdapter(baseListAdapter);
        // listview点击事件
        exams_list3.setOnItemClickListener(new casesharing_listListener());

        baseListAdapter.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > 0) {
                }
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = exams_list3.getChildAt(0);

                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        isNoMore = false;
                    }

                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {

                    View lastVisibleItemView = exams_list3.getChildAt(exams_list3.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == exams_list3.getHeight()) {
                        if (!isNoMore) {
                            page += 1;
                            setValue2();
                        }
                    }
                }
            }
        });
    }

    /**
     * name: 点击查看某个考试详细
     */
    private class casesharing_listListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
            TextView textView = (TextView) view.findViewById(R.id.my_exams_id);
            String id = textView.getText().toString();
            //跳转到考试详情
            Intent intent = new Intent(context, Examination_instructions.class);
            intent.putExtra("my_exams_id", id);
            startActivity(intent);
        }
    }

    public void setValue2() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getPaperList);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("page", page);
                    obj.put("type", "3");//已结束
                    obj.put("keyWord", My_exams_LearningTaskActivity.keyword.length() > 0 ? My_exams_LearningTaskActivity.keyword : "");

                    String result = HttpClientUtils.sendPost(context, URLConfig.Medical_examination, obj.toString());
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

    @Override
    public void onResume() {
        //保存进入的日期
        entertime = SkyVisitUtils.getCurrentTime();
        super.onResume();
    }

    @Override
    public void onPause() {
        //保存推出的日期
        leavetime = SkyVisitUtils.getCurrentTime();
        enterUrl = "http://yy.ccmtv.cn/exam_bank/start.html?pid=" + pid;
        //保存日期到服务器
        SkyVisitUtils.OnlineStatistical(context, enterUrl, entertime, leavetime);
        pause++;
        super.onPause();
    }

}
