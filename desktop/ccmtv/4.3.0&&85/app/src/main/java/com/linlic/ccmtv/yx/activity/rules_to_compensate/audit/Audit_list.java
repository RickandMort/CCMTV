package com.linlic.ccmtv.yx.activity.rules_to_compensate.audit;

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
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.home.ActivityWebActivity;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.the_teachers_management.The_teachers_management_audit;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management.Event_Details3;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/7/13.
 */

public class Audit_list extends BaseActivity {
    private Context context;
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.audit_list2)
    ListView audit_list2;
    @Bind(R.id.audit_list3)
    ListView audit_list3;
    @Bind(R.id.popwindow1)
    LinearLayout popwindow1;//
    @Bind(R.id.popwindow2)
    LinearLayout popwindow2;//
    @Bind(R.id.the_activity_type)
    TextView the_activity_type;//活动类型
    @Bind(R.id.the_activity_type_icon)
    ImageView the_activity_type_icon;//活动类型
    @Bind(R.id.time_to_screen)
    TextView time_to_screen;//时间筛选
    @Bind(R.id.time_to_screen_icon)
    ImageView time_to_screen_icon;//
    @Bind(R.id.audit_nodata)
    NodataEmptyLayout audit_nodata;//
    private int page = 1;
    private int count = 0;
    JSONObject result, data;
    private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> listData2 = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> listData3 = new ArrayList<Map<String, Object>>();
    private BaseListAdapter baseListAdapterVideo, baseListAdapterStatus, baseListAdapterType;
    private Map<String, Object> type_select_map = new HashMap<>();
    private Map<String, Object> status_select_map = new HashMap<>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            if (dataJson.getInt("status") == 1) { // 成功
                                JSONArray dateJson = dataJson.getJSONObject("data").getJSONArray("list");
                                listData.clear();
                                if (page == 1) {
                                    count = dataJson.getJSONObject("data").getInt("count");
                                }
                                for (int i = 0; i < dateJson.length(); i++) {
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("id", dataJson1.getString("id"));
                                    map.put("fid", dataJson1.getString("fid"));
                                    map.put("title", dataJson1.getString("title"));
                                    map.put("tableName", dataJson1.getString("tableName"));
                                    map.put("primaryId", dataJson1.getString("primaryId"));
                                    map.put("realname", dataJson1.getString("realname"));
                                    map.put("keshiname", dataJson1.getString("keshiname"));
                                    map.put("is_look", dataJson1.getString("is_look"));
                                    map.put("status", dataJson1.getString("status"));

                                    map.put("url", dataJson1.has("url")?dataJson1.getString("url"):"");
                                    map.put("type", "4");
                                    listData.add(map);
                                }
                                baseListAdapterVideo.notifyDataSetChanged();
                            } else {
                                if (page == 1) {
                                    listData.clear();
                                    baseListAdapterVideo.notifyDataSetChanged();
                                }
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        setResultStatus(listData.size() > 0, jsonObject.getInt("code"));
                        MyProgressBarDialogTools.hide();

                    } catch (Exception e) {
                        e.printStackTrace();
                        setResultStatus(listData.size() > 0, HttpClientUtils.UNKONW_EXCEPTION_CODE);
                    }
                    break;
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            if (dataJson.getInt("status") == 1) { // 成功
                                listData2.clear();
                                listData3.clear();
                                JSONArray statusList = dataJson.getJSONObject("data").getJSONArray("statusList");
                                for (int i = 0; i < statusList.length(); i++) {
                                    JSONObject dataJson1 = statusList.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("status_id", dataJson1.getString("status_id"));
                                    map.put("status_name", dataJson1.getString("status_name"));
                                    listData2.add(map);
                                }
                                baseListAdapterStatus.notifyDataSetChanged();
                                JSONArray funList = dataJson.getJSONObject("data").getJSONArray("funList");
                                for (int i = 0; i < funList.length(); i++) {
                                    JSONObject dataJson1 = funList.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("fid", dataJson1.getString("fid"));
                                    map.put("funcname", dataJson1.getString("funcname"));
                                    listData3.add(map);
                                }
                                baseListAdapterType.notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case 500:
                    Toast.makeText(context
                            , R.string.post_hint1,
                            Toast.LENGTH_SHORT).show();
                    setResultStatus(listData.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                    break;

                default:
                    break;
            }

        }
    };

    private void setResultStatus(boolean status, int code) {
        if (status) {
            listView.setVisibility(View.VISIBLE);
            audit_nodata.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                audit_nodata.setNetErrorIcon();
            } else {
                audit_nodata.setLastEmptyIcon();
            }
            listView.setVisibility(View.GONE);
            audit_nodata.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.audit_list);
        context = this;
        ButterKnife.bind(this);
        initView();
        initDatas();
        getUrlRulest();
    }


    private void initDatas() {
        listData.clear();
        inittextData();
        workFlowOptionList();
        baseListAdapterVideo.notifyDataSetChanged();
    }

    public void inittextData() {
      /*  for (int i = 0 ; i<5;i++){
            Map<String,Object> map = new HashMap<>();
            map.put("id",i);
            map.put("time","0"+i);
            map.put("content","2018年0"+i+"月评价带教");
            listData.add(map);
        }*/
    }

    public void initView() {

        baseListAdapterStatus = new BaseListAdapter(audit_list2, listData2, R.layout.item_training_management2) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map map = (Map) item;

                helper.setText(R.id._item2_text1, map.get("status_name").toString());

            }
        };
        audit_list2.setAdapter(baseListAdapterStatus);
        // listview点击事件
        audit_list2.setOnItemClickListener(new casesharing_listListener2());
        baseListAdapterType = new BaseListAdapter(audit_list3, listData3, R.layout.item_training_management2) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map map = (Map) item;

                helper.setText(R.id._item2_text1, map.get("funcname").toString());

            }
        };
        audit_list3.setAdapter(baseListAdapterType);
        // listview点击事件
        audit_list3.setOnItemClickListener(new casesharing_listListener3());

        baseListAdapterVideo = new BaseListAdapter(listView, listData, R.layout.item_audit_list) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map map = (Map) item;
                helper.setText(R.id._item_id, map.get("id").toString());
                helper.setText(R.id._item_department, map.get("title").toString());
                helper.setText(R.id._item_submitter, map.get("realname").toString());
                helper.setText(R.id._item_department_2, map.get("keshiname").toString());
                switch (map.get("fid").toString()) {
                    case "95":
                        //跳转审核日常考核学员列表
                        helper.setText(R.id.type_text, "日常考核");
                        break;
                    case "100"://出科考核
                        helper.setText(R.id.type_text, "出科考核");
                        break;
                    case "126"://教学活动
                        helper.setText(R.id.type_text, "教学活动");
                        break;
                    case "228"://师资管理
                        helper.setText(R.id.type_text, "师资管理");
                        break;
                    case "134"://轮转手册模块
                        helper.setText(R.id.type_text, "轮转手册");
                        break;
                    default:
                        break;
                }
                if (map.get("is_look").toString().equals("1")) {//1 已看 0未看
                    helper.setVisibility(R.id.is_look, View.GONE);
                } else {
                    helper.setVisibility(R.id.is_look, View.VISIBLE);
                }
                switch (map.get("status").toString()) {
                    case "0":
                        helper.setVisibility(R.id.status_image, View.GONE);
                        break;
                    case "1":
                        helper.setImage(R.id.status_image, R.mipmap.training_32);
                        helper.setVisibility(R.id.status_image, View.VISIBLE);
                        break;
                    case "2":
                        helper.setImage(R.id.status_image, R.mipmap.training_31);
                        helper.setVisibility(R.id.status_image, View.VISIBLE);
                        break;
                }
            }
        };
        listView.setAdapter(baseListAdapterVideo);
        // listview点击事件
        listView.setOnItemClickListener(new casesharing_listListener());
        baseListAdapterVideo.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > 0) {
                }
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = listView.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
//                        isNoMore = false;
                    }
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = listView.getChildAt(listView.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == listView.getHeight()) {
                        int sum = page * 20;
                        if (sum < count) {
                            page += 1;
                            getUrlRulest();
                        }
                    }
                }
            }
        });
    }


    /**
     * name: 点击查看某个视频的详细 author:Tom 2016-1-28下午3:42:08
     */
    private class casesharing_listListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {
            Map<String, Object> map = listData.get(arg2);
            Intent intent = null;
            switch (map.get("fid").toString()) {
               /* case "1":
                    intent = new Intent(context, Evaluation_department_list2.class);
                    break;
                case "2":
                    intent = new Intent(context, Evaluation_department_list3.class);
                    break;
                case "3":
                    intent = new Intent(context, Evaluation_department_list.class);
                    break;*/
                case "134":
                    //跳转审核日常考核学员列表
                    intent = new Intent(context, ActivityWebActivity.class);
                    intent.putExtra("aid", map.get("url").toString());
                    intent.putExtra("title", map.get("title").toString());
                    break;
                case "95":
                    //跳转审核日常考核学员列表
                    intent = new Intent(context, GpDailyExamAuditList.class);
                    intent.putExtra("id", map.get("id").toString());
                    intent.putExtra("fid", map.get("fid").toString());
                    intent.putExtra("primaryId", map.get("primaryId").toString());
                    break;
                case "100"://出科审核
                    //跳转审核出科考核学员列表
                    intent = new Intent(context, GpGraduateExamAuditList.class);
                    intent.putExtra("id", map.get("id").toString());
                    intent.putExtra("fid", map.get("fid").toString());
                    intent.putExtra("primaryId", map.get("primaryId").toString());
                    break;
                case "126"://教学活动
                    //教学活动详情
                    intent = new Intent(context, Event_Details3.class);
                    intent.putExtra("w_id", map.get("id").toString());
                    intent.putExtra("fid", map.get("fid").toString());
                    intent.putExtra("id", map.get("primaryId").toString());
                    break;
                case "228"://师资管理
                    intent = new Intent(context, The_teachers_management_audit.class);
                    intent.putExtra("w_id", map.get("id").toString());
                    intent.putExtra("fid", map.get("fid").toString());
                    intent.putExtra("id", map.get("primaryId").toString());
                    break;
                default:
                    break;
            }

            if (intent != null) {
                startActivity(intent);
            }

        }

    }


    public void getUrlRulest() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.workFlowList);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("page", page);
                    if (status_select_map != null && status_select_map.containsKey("status_id")) {
                        obj.put("status_id", status_select_map.get("status_id").toString());
                    }
                    if (type_select_map != null && type_select_map.containsKey("fid")) {
                        obj.put("f_id", type_select_map.get("fid").toString());
                    }
                    obj.put("limit", 20);

                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("审核列表", result);

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

    public void workFlowOptionList() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.workFlowOptionList);
                    obj.put("uid", SharedPreferencesTools.getUid(context));


                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("审核列表 条件", result);

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

    /**
     * 返回
     *
     * @param view
     */
    public void back(View view) {
        finish();
    }

    public void popwindow(View view) {
        LogUtil.e("数据", listData2.size() + "    " + listData3.size());
        if (popwindow2.getVisibility() == View.VISIBLE) {
            popwindow2.setVisibility(View.GONE);

            the_activity_type.setTextColor(Color.parseColor("#666666"));
            the_activity_type_icon.setImageResource(R.mipmap.training_08);
        } else {
            popwindow2.setVisibility(View.VISIBLE);
            popwindow1.setVisibility(View.GONE);

            the_activity_type.setTextColor(Color.parseColor("#3698F9"));
            the_activity_type_icon.setImageResource(R.mipmap.training_07);
            time_to_screen.setTextColor(Color.parseColor("#666666"));
            time_to_screen_icon.setImageResource(R.mipmap.training_08);
        }
    }

    public void popwindow2(View view) {
        LogUtil.e("数据", listData2.size() + "    " + listData3.size());
        if (popwindow1.getVisibility() == View.VISIBLE) {
            popwindow1.setVisibility(View.GONE);
            time_to_screen.setTextColor(Color.parseColor("#666666"));
            time_to_screen_icon.setImageResource(R.mipmap.training_08);
        } else {
            popwindow1.setVisibility(View.VISIBLE);
            popwindow2.setVisibility(View.GONE);
            the_activity_type.setTextColor(Color.parseColor("#666666"));
            the_activity_type_icon.setImageResource(R.mipmap.training_08);
            time_to_screen.setTextColor(Color.parseColor("#3698F9"));
            time_to_screen_icon.setImageResource(R.mipmap.training_07);
        }
    }

    /**
     * name:  状态选择
     */
    private class casesharing_listListener2 implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {

            status_select_map = listData2.get(arg2);
            popwindow1.setVisibility(View.GONE);
            the_activity_type.setTextColor(Color.parseColor("#666666"));
            the_activity_type_icon.setImageResource(R.mipmap.training_08);
            time_to_screen.setTextColor(Color.parseColor("#666666"));
            time_to_screen_icon.setImageResource(R.mipmap.training_08);
            page = 1;
            getUrlRulest();

        }

    }

    /**
     * name:  状态选择
     */
    private class casesharing_listListener3 implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {

            type_select_map = listData3.get(arg2);
            popwindow2.setVisibility(View.GONE);
            the_activity_type.setTextColor(Color.parseColor("#666666"));
            the_activity_type_icon.setImageResource(R.mipmap.training_08);
            time_to_screen.setTextColor(Color.parseColor("#666666"));
            time_to_screen_icon.setImageResource(R.mipmap.training_08);
            page = 1;
            getUrlRulest();

        }

    }

    public void ishide(View view) {

        popwindow2.setVisibility(View.GONE);
        popwindow1.setVisibility(View.GONE);
        the_activity_type.setTextColor(Color.parseColor("#666666"));
        the_activity_type_icon.setImageResource(R.mipmap.training_08);
        time_to_screen.setTextColor(Color.parseColor("#666666"));
        time_to_screen_icon.setImageResource(R.mipmap.training_08);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SharedPreferencesTools.getEvent_details_status(context).length() > 0) {
            try {
                JSONObject json = new JSONObject(SharedPreferencesTools.getEvent_details_status(context));
                Iterator<String> iterator = json.keys();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    for (int i = 0; i < listData.size(); i++) {
                        if (listData.get(i).get("id").toString().equals(key)) {
                            listData.get(i).put("status", json.getString(key));
                        }
                    }
                }
                baseListAdapterVideo.notifyDataSetChanged();
                SharedPreferencesTools.saveEvent_details_status(context, "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/verify/index.html";
        super.onPause();
    }
}
