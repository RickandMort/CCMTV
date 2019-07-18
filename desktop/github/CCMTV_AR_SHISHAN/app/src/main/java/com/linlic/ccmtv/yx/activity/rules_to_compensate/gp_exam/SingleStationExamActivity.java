package com.linlic.ccmtv.yx.activity.rules_to_compensate.gp_exam;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.adapter.GirdDropDownAdapter;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.adapter.StationDropDownAdapter;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.adapter.TypeDropDownAdapter;
import com.linlic.ccmtv.yx.adapter.BaseRecyclerViewAdapter;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.JsonUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.ToastUtils;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yyydjk.library.DropDownMenu;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SingleStationExamActivity extends BaseActivity  {
    @Bind(R.id.arrow_back)
    LinearLayout arrowBack;
    @Bind(R.id.activity_title_name)
    TextView activityTitleName;
    @Bind(R.id.ll_time)
    LinearLayout llTime;
    @Bind(R.id.ll_state)
    LinearLayout llState;
    @Bind(R.id.ll_type)
    LinearLayout llType;
    NodataEmptyLayout layoutNodata;
    SmartRefreshLayout refreshLayout;
    @Bind(R.id.dropDownMenu)
    DropDownMenu dropDownMenu;
    private BaseRecyclerViewAdapter adapter;
    private List<ListInfo> list_data = new ArrayList<>();
    private List<Map<String, Object>> time_data = new ArrayList<>();
    private List<Map<String, Object>> type_data = new ArrayList<>();
    private List<Map<String, Object>> more_data = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapterTime;
    private String[] times;
    private String[] types;
    private String[] mores;
    private int flag = -1;
    private Context context;
    private String page = "1";
    private String fid = "";
    private String year = "";
    private String type_id = "";
    private String is_more = "";
    private String icon = "";
    private int pages;//总页数
    private String headers[] = {"时间", "站点", "类型"};
    private List<View> popupViews = new ArrayList<>();


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getString("code").equals("200")) { // 成功
                            JSONObject data = result.getJSONObject("data");
                            if (data.getString("status").equals("1")) {
                                JSONObject dataList = data.getJSONObject("data");
                                pages = dataList.getInt("pages");
                                JSONArray jsonArray = dataList.getJSONArray("data");
                                if (popupViews.size()<1){
                                    JSONArray type_list = dataList.getJSONArray("type_list");
                                    JSONArray year_list = dataList.getJSONArray("year_list");
                                    JSONArray more_list = dataList.getJSONArray("more_list");
                                    if (year_list.length() != 0) {
                                        times = new String[year_list.length()];
                                        for (int i = 0; i < year_list.length(); i++) {
                                            Map<String, Object> map = new HashMap<String, Object>();
                                            map.put("year", year_list.getJSONObject(i).getString("year"));
                                            map.put("year_name", year_list.getJSONObject(i).getString("year_name"));
                                            time_data.add(map);
                                            times[i] = year_list.getJSONObject(i).getString("year_name");
                                        }
                                    }
                                    if (type_list.length() != 0) {
                                        types = new String[type_list.length()];
                                        for (int i = 0; i < type_list.length(); i++) {
                                            Map<String, Object> map = new HashMap<String, Object>();
                                            map.put("type_id", type_list.getJSONObject(i).getString("type_id"));
                                            map.put("name", type_list.getJSONObject(i).getString("name"));
                                            type_data.add(map);
                                            types[i] = type_list.getJSONObject(i).getString("name");
                                        }
                                    }
                                    if (more_list.length() != 0) {
                                        mores = new String[more_list.length()];
                                        for (int i = 0; i < more_list.length(); i++) {
                                            Map<String, Object> map = new HashMap<String, Object>();
                                            map.put("is_more", more_list.getJSONObject(i).getString("is_more"));
                                            map.put("name", more_list.getJSONObject(i).getString("name"));
                                            more_data.add(map);
                                            mores[i] = more_list.getJSONObject(i).getString("name");
                                        }
                                    }
                                    initpopwindow();
                                    refresh();
                                }
                                if (page.equals("1") && jsonArray.length() == 0) {
                                    layoutNodata.setVisibility(View.VISIBLE);
                                    exam_recyclerview.setVisibility(View.GONE);
                                }
                                if (jsonArray.length() != 0) {
                                    layoutNodata.setVisibility(View.GONE);
                                    exam_recyclerview.setVisibility(View.VISIBLE);
                                    List<ListInfo> datas = JsonUtils.fromJsonArray(jsonArray.toString(), ListInfo.class);
                                    if (page.equals("1")){
                                        list_data.clear();
                                    }
                                    list_data.addAll(datas);
                                    if (list_data.size() != 0) {
                                        if(adapter==null){
                                            adapter = new BaseRecyclerViewAdapter(R.layout.adapter_single_station, list_data) {
                                                @Override
                                                public void convert(BaseViewHolder helper, Object item) {
                                                    super.convert(helper, item);
                                                    ListInfo data = (ListInfo) item;
                                                    helper.setText(R.id.tv_name, data.getExam_name());
                                                    helper.setText(R.id.tv_status, data.getStatus_n());
                                                    helper.setText(R.id.tv_class, data.getType_name());
                                                    helper.setText(R.id.tv_date, data.getS_time() + " 至 " + data.getE_time());
                                                    String base_name = data.getBase_name();
                                                    String m_status = data.getIs_more();
                                                    String status = data.getStatus_n();
                                                    if (status.equals("进行中")) {
                                                        helper.setTextColor(R.id.tv_status, context.getResources().getColor(R.color.color3897f9));
                                                        helper.setBackgroundRes(R.id.tv_status, R.drawable.single_pro1);
                                                    } else {
                                                        helper.setTextColor(R.id.tv_status, context.getResources().getColor(R.color.black99));
                                                        helper.setBackgroundRes(R.id.tv_status, R.drawable.single_pro2);
                                                    }
                                                    if (base_name.equals("")) {
                                                        helper.setGone(R.id.tv_base_name, false);
                                                    } else {
                                                        helper.setGone(R.id.tv_base_name, true);
                                                        helper.setText(R.id.tv_base_name, data.getBase_name());
                                                    }
                                                    if (m_status.equals("1")) {
                                                        helper.setText(R.id.tv_type, "单站");
                                                        helper.setBackgroundRes(R.id.tv_type, R.drawable.much_type);
                                                    } else if (m_status.equals("2")) {
                                                        helper.setText(R.id.tv_type, "多站");
                                                        helper.setBackgroundRes(R.id.tv_type, R.drawable.single_type);
                                                    }
                                                }
                                            };
                                            adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
                                            adapter.isFirstOnly(false);
                                            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                                    Intent intent = new Intent(SingleStationExamActivity.this, ExamStationListActivity.class);
                                                    intent.putExtra("fid", fid);
                                                    intent.putExtra("detail_id", list_data.get(position).getDetail_id());
                                                    intent.putExtra("title_name",list_data.get(position).getExam_name());
                                                    startActivity(intent);
                                                }
                                            });

                                            exam_recyclerview.setAdapter(adapter);
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                }

                            } else {
                                Toast.makeText(context, data.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, result.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        MyProgressBarDialogTools.hide();
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        final JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getString("code").equals("200")) { // 成功
                            JSONObject data = result.getJSONObject("data");
                            if (data.getString("status").equals("1")) {
                                JSONObject dataList = data.getJSONObject("data");
                                pages = dataList.getInt("pages");
                                JSONArray jsonArray = dataList.getJSONArray("data");
                                if (popupViews.size()<1){
                                    JSONArray type_list = dataList.getJSONArray("type_list");
                                    JSONArray year_list = dataList.getJSONArray("year_list");
                                    JSONArray more_list = dataList.getJSONArray("more_list");
                                    if (year_list.length() != 0) {
                                        times = new String[year_list.length()];
                                        for (int i = 0; i < year_list.length(); i++) {
                                            Map<String, Object> map = new HashMap<String, Object>();
                                            map.put("year", year_list.getJSONObject(i).getString("year"));
                                            map.put("year_name", year_list.getJSONObject(i).getString("year_name"));
                                            time_data.add(map);
                                            times[i] = year_list.getJSONObject(i).getString("year_name");
                                        }
                                    }
                                    if (type_list.length() != 0) {
                                        types = new String[type_list.length()];
                                        for (int i = 0; i < type_list.length(); i++) {
                                            Map<String, Object> map = new HashMap<String, Object>();
                                            map.put("type_id", type_list.getJSONObject(i).getString("type_id"));
                                            map.put("name", type_list.getJSONObject(i).getString("name"));
                                            type_data.add(map);
                                            types[i] = type_list.getJSONObject(i).getString("name");
                                        }
                                    }
                                    if (more_list.length() != 0) {
                                        mores = new String[more_list.length()];
                                        for (int i = 0; i < more_list.length(); i++) {
                                            Map<String, Object> map = new HashMap<String, Object>();
                                            map.put("is_more", more_list.getJSONObject(i).getString("is_more"));
                                            map.put("name", more_list.getJSONObject(i).getString("name"));
                                            more_data.add(map);
                                            mores[i] = more_list.getJSONObject(i).getString("name");
                                        }
                                    }
                                    initpopwindow();
                                    refresh();
                                }
                                if (page.equals("1") && jsonArray.length() == 0) {
                                    layoutNodata.setVisibility(View.VISIBLE);
                                    exam_recyclerview.setVisibility(View.GONE);
                                }
                                if (jsonArray.length() != 0) {
                                    layoutNodata.setVisibility(View.GONE);
                                    exam_recyclerview.setVisibility(View.VISIBLE);
                                    List<ListInfo> datas = JsonUtils.fromJsonArray(jsonArray.toString(), ListInfo.class);
                                    if (page.equals("1")){
                                        list_data.clear();
                                    }
                                    list_data.addAll(datas);
                                    if (list_data.size() != 0) {
                                        if(adapter==null){
                                            adapter = new BaseRecyclerViewAdapter(R.layout.adapter_single_station, list_data) {
                                                @Override
                                                public void convert(BaseViewHolder helper, Object item) {
                                                    super.convert(helper, item);
                                                    ListInfo data = (ListInfo) item;
                                                    helper.setText(R.id.tv_name, data.getExam_name());
                                                    helper.setText(R.id.tv_status, data.getStatus_n());
                                                    helper.setText(R.id.tv_class, data.getType_name());
                                                    helper.setText(R.id.tv_date, data.getS_time() + " 至 " + data.getE_time());
                                                    helper.setGone(R.id.tv_base_name, false);
                                                    String status = data.getStatus_n();
                                                    if (status.equals("进行中")) {
                                                        helper.setTextColor(R.id.tv_status, context.getResources().getColor(R.color.color3897f9));
                                                        helper.setBackgroundRes(R.id.tv_status, R.drawable.single_pro1);
                                                    } else {
                                                        helper.setTextColor(R.id.tv_status, context.getResources().getColor(R.color.black99));
                                                        helper.setBackgroundRes(R.id.tv_status, R.drawable.single_pro2);
                                                    }
                                                    String m_status = data.getIs_more();
                                                    if (m_status.equals("1")) {
                                                        helper.setText(R.id.tv_type, "单站");
                                                        helper.setBackgroundRes(R.id.tv_type, R.drawable.much_type);
                                                    } else if (m_status.equals("2")) {
                                                        helper.setText(R.id.tv_type, "多站");
                                                        helper.setBackgroundRes(R.id.tv_type, R.drawable.single_type);
                                                    }
                                                }
                                            };
                                            adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
                                            adapter.isFirstOnly(false);
                                            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                                    Intent intent = new Intent(SingleStationExamActivity.this, StudentExamStationDetailsActivity.class);
                                                    intent.putExtra("fid", fid);
                                                    intent.putExtra("exam_id", list_data.get(position).getExam_id());
                                                    intent.putExtra("title_name",list_data.get(position).getExam_name());
                                                    startActivity(intent);
                                                }
                                            });
                                            exam_recyclerview.setAdapter(adapter);
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            } else {
                                Toast.makeText(context, data.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, result.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        MyProgressBarDialogTools.hide();
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    MyProgressBarDialogTools.hide();
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_station_exam);
        context = SingleStationExamActivity.this;
        ButterKnife.bind(this);
        getIntentData();
    }

    private void getIntentData() {
        fid = getIntent().getStringExtra("fid");
        icon = getIntent().getStringExtra("icon");
        if (icon.equals("stageExam")) {
            initdata();
        } else if (icon.equals("osce")) {
            stu_data();
        }
    }

    private void refresh() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = "1";
                if (icon.equals("stageExam")) {
                    initdata();
                } else if (icon.equals("osce")) {
                    stu_data();
                }
                refreshLayout.finishRefresh();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page = String.valueOf(Integer.valueOf(page) + 1);
                if ((Integer.valueOf(page)-1)>=Integer.valueOf(pages)) {
                    ToastUtils.makeText(context, "暂无更多数据");
                } else {
                    if (icon.equals("stageExam")) {
                        initdata();
                    } else if (icon.equals("osce")) {
                        stu_data();
                    }
                }
                refreshlayout.finishLoadmore();
            }
        });
    }

    private void initdata() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getStageExaminerIndex);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("page", page);
                    obj.put("year", year);
                    obj.put("fid", fid);
                    obj.put("type_id", type_id);
                    obj.put("is_more", is_more);
                    obj.put("new", "1");
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("单站考核列表数据：", result);

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

    private void stu_data() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getStageUserIndex);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("page", page);
                    obj.put("year", year);
                    obj.put("fid", fid);
                    obj.put("type_id", type_id);
                    obj.put("is_more", is_more);
                    obj.put("new", "1");
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("单站考核学生端列表数据：", result);

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
    private GirdDropDownAdapter myadapter;
    private TypeDropDownAdapter typeDropDownAdapter;
    private StationDropDownAdapter stationDropDownAdapter;
    private void initpopwindow() {
        ListView listview1 = new ListView(context);
        //arrayAdapterTime = new ArrayAdapter<String>(this, R.layout.simple_list_item_1_center, times);
        myadapter = new GirdDropDownAdapter(this, Arrays.asList(times));
        listview1.setAdapter(myadapter);

        ListView listview2 = new ListView(context);
        //arrayAdapterTime = new ArrayAdapter<String>(this, R.layout.simple_list_item_1_center, mores);
        stationDropDownAdapter = new StationDropDownAdapter(this, Arrays.asList(mores));
        listview2.setAdapter(stationDropDownAdapter);

        ListView listview3 = new ListView(context);
        //arrayAdapterTime = new ArrayAdapter<String>(this, R.layout.simple_list_item_1_center, types);
        typeDropDownAdapter = new TypeDropDownAdapter(this, Arrays.asList(types));
        listview3.setAdapter(typeDropDownAdapter);

        popupViews.add(listview1);
        popupViews.add(listview2);
        popupViews.add(listview3);

        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                myadapter.setCheckItem(i);
                if(i==0){
                    dropDownMenu.setTabText("时间");
                }else {
                    dropDownMenu.setTabText(times[i]);
                }
                year = time_data.get(i).get("year").toString();
                page = "1";
                if (icon.equals("stageExam")) {
                    initdata();
                } else if (icon.equals("osce")) {
                    stu_data();
                }
                dropDownMenu.closeMenu();
            }
        });

        listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                stationDropDownAdapter.setCheckItem(i);
                if(i==0){
                    dropDownMenu.setTabText("站点");
                }else {
                    dropDownMenu.setTabText(mores[i]);
                }
                is_more = more_data.get(i).get("is_more").toString();
                page = "1";
                if (icon.equals("stageExam")) {
                    initdata();
                } else if (icon.equals("osce")) {
                    stu_data();
                }
                dropDownMenu.closeMenu();
            }
        });

        listview3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                typeDropDownAdapter.setCheckItem(i);
                if(i==0){
                    dropDownMenu.setTabText("类型");
                }else {
                    dropDownMenu.setTabText(types[i]);
                }
                type_id = type_data.get(i).get("type_id").toString();
                page = "1";
                if (icon.equals("stageExam")) {
                    initdata();
                } else if (icon.equals("osce")) {
                    stu_data();
                }
                dropDownMenu.closeMenu();
            }
        });
        View fifthView = LayoutInflater.from(this).inflate(R.layout.activity_station, null);
        exam_recyclerview = (RecyclerView) fifthView.findViewById(R.id.exam_recyclerview);
        layoutNodata=fifthView.findViewById(R.id.layout_nodata);
        refreshLayout=fifthView.findViewById(R.id.refreshLayout);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        exam_recyclerview.setLayoutManager(layoutManager);
//        exam_recyclerview.setAdapter(adapter);

        dropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, fifthView);
    }

    RecyclerView exam_recyclerview;



    class ListInfo implements Serializable {
        private String exam_name;
        private String s_time;
        private String e_time;
        private String is_more;
        private String type_name;
        private String base_name;
        private String detail_id;
        private String status;
        private String status_n;
        private String exam_id;

        public String getExam_id() {
            return exam_id;
        }

        public void setExam_id(String exam_id) {
            this.exam_id = exam_id;
        }

        public String getExam_name() {
            return exam_name;
        }

        public void setExam_name(String exam_name) {
            this.exam_name = exam_name;
        }

        public String getS_time() {
            return s_time;
        }

        public void setS_time(String s_time) {
            this.s_time = s_time;
        }

        public String getE_time() {
            return e_time;
        }

        public void setE_time(String e_time) {
            this.e_time = e_time;
        }

        public String getIs_more() {
            return is_more;
        }

        public void setIs_more(String is_more) {
            this.is_more = is_more;
        }

        public String getType_name() {
            return type_name;
        }

        public void setType_name(String type_name) {
            this.type_name = type_name;
        }

        public String getBase_name() {
            return base_name;
        }

        public void setBase_name(String base_name) {
            this.base_name = base_name;
        }

        public String getDetail_id() {
            return detail_id;
        }

        public void setDetail_id(String detail_id) {
            this.detail_id = detail_id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatus_n() {
            return status_n;
        }

        public void setStatus_n(String status_n) {
            this.status_n = status_n;
        }
    }


}
