package com.linlic.ccmtv.yx.activity.rules_to_compensate.periodical_exam;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
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
import java.util.List;
import java.util.Map;


/**
 * 规培 阶段性考核 主页
 */
public class PeriodicalExamListActivity extends AppCompatActivity {

    private Context context;
    private TextView title_name;
    private TextView tvTimeSelect;
    private TextView tvExamTypeSelect;
    private ImageView ivTimeSelect;
    private ImageView ivExamTypeSelect;
    private LinearLayout llTimeSelect;
    private LinearLayout llExamTypeSelect;
    private ListView lvPeriodicalExam;
    private NodataEmptyLayout periodical_nodata;
    private View contentView1;
    private View contentView2;
    private BaseListAdapter baseListAdapter;

    private List<String> timeList = new ArrayList<>();
    private List<String> typeList = new ArrayList<>();
    private List<Map<String, String>> timeMapList = new ArrayList<>();
    private List<Map<String, String>> typeMapList = new ArrayList<>();
    private List<Map<String, String>> periodicalExamList = new ArrayList<>();
    private PopupWindow popupWindow1;
    private PopupWindow popupWindow2;
    private String fid = "";
    private String selectType = "";
    private String selectTime = "";
    private ArrayAdapter<String> arrayAdapterTime;
    private ArrayAdapter<String> arrayAdapterType;
    private boolean isNoMore = false;
    private int page = 1;


    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getString("code").equals("200")) {
                            JSONObject data = result.getJSONObject("data");
                            if (data.getInt("status") == 1) {
                                JSONObject dataObject = data.getJSONObject("data");
                                JSONArray dataArray = dataObject.getJSONArray("data");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObjectDetail = dataArray.getJSONObject(i);
                                    Map<String, String> map = new HashMap<>();
                                    map.put("exam_name", dataObjectDetail.getString("exam_name"));
                                    map.put("type_name", dataObjectDetail.getString("type_name"));
                                    map.put("base_name", dataObjectDetail.getString("base_name"));
                                    map.put("s_time", dataObjectDetail.getString("s_time"));
                                    map.put("e_time", dataObjectDetail.getString("e_time"));
                                    map.put("detail_id", dataObjectDetail.getString("detail_id"));
                                    periodicalExamList.add(map);
                                }
                                baseListAdapter.notifyDataSetChanged();

                                typeMapList.clear();
                                typeList.clear();
                                timeMapList.clear();
                                timeList.clear();
                                JSONArray dataTypeArray = dataObject.getJSONArray("exam_type");
                                for (int i = 0; i < dataTypeArray.length(); i++) {
                                    JSONObject dataTypeObject = dataTypeArray.getJSONObject(i);
                                    Map<String, String> map = new HashMap<>();
                                    map.put("type_id", dataTypeObject.getString("type_id"));
                                    map.put("name", dataTypeObject.getString("name"));
                                    typeMapList.add(map);
                                    typeList.add(dataTypeObject.getString("name"));
                                }

                                JSONArray dataTimeArray = dataObject.getJSONArray("year_arr");
                                for (int i = 0; i < dataTimeArray.length(); i++) {
                                    JSONObject dataTimeObject = dataTimeArray.getJSONObject(i);
                                    Map<String, String> map = new HashMap<>();
                                    map.put("year", dataTimeObject.getString("year"));
                                    map.put("year_name", dataTimeObject.getString("year_name"));
                                    timeMapList.add(map);
                                    timeList.add(dataTimeObject.getString("year_name"));
                                }
                                arrayAdapterType.notifyDataSetChanged();
                                arrayAdapterTime.notifyDataSetChanged();
                            } else {
                                isNoMore = true;
                                Toast.makeText(context, data.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, result.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        setResultStatus(periodicalExamList.size() > 0, result.getInt("code"));
                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        MyProgressBarDialogTools.hide();
                        setResultStatus(periodicalExamList.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    MyProgressBarDialogTools.hide();
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    setResultStatus(periodicalExamList.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                    break;
                default:
                    break;
            }
        }
    };

    private void setResultStatus(boolean status, int code) {
        if (status) {
            lvPeriodicalExam.setVisibility(View.VISIBLE);
            periodical_nodata.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                periodical_nodata.setNetErrorIcon();
            } else {
                periodical_nodata.setLastEmptyIcon();
            }
            lvPeriodicalExam.setVisibility(View.GONE);
            periodical_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_periodical_exam_list);

        context = this;
        findId();
        getIntentData();
        initPopupWindowView();
        initListViewData();
        initPeriodicalExamListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        //enterUrl = "http://yun.ccmtv.cn/admin.php/wx/StageExaminer/index.html";
        super.onPause();
    }

    private void getIntentData() {
        try {
            fid = getIntent().getStringExtra("fid");
//            Log.e(getLocalClassName(), "initData: fid:" + fid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void findId() {
        title_name = findViewById(R.id.activity_title_name);
        tvTimeSelect = findViewById(R.id.id_tv_periodical_exam_time);
        tvExamTypeSelect = findViewById(R.id.id_tv_periodical_exam_type);
        ivTimeSelect = findViewById(R.id.id_iv_periodical_exam_time_icon);
        ivExamTypeSelect = findViewById(R.id.id_iv_periodical_exam_type_icon);
        llTimeSelect = findViewById(R.id.id_ll_periodical_exam_time);
        llExamTypeSelect = findViewById(R.id.id_ll_periodical_exam_type);
        lvPeriodicalExam = findViewById(R.id.id_lv_periodical_exam);
        periodical_nodata = findViewById(R.id.periodical_nodata);
        title_name.setText("阶段性考核");
    }

    private void initListViewData() {
        /*String currentYear = TimeUtil.getCurrentYear();
        for (int i = 2018; i <= Integer.parseInt(currentYear); i++) {
            timeList.add(i+"");
        }
        arrayAdapterTime.notifyDataSetChanged();*/
        /*for (int i = 0; i < 6; i++) {
            timeList.add("201"+i);
            typeList.add("临床能力测评"+i);
        }

        for (int i = 0; i < 6; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("title","2018阶段性考核"+i);
            map.put("description","临床能力测评 全科"+i);
            map.put("time","2018-08-27 00:00:00~2018-08-28 00:00:00");
            periodicalExamList.add(map);
        }*/

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.stageExaminerIndex);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("fid", fid);
                    obj.put("page", page);
                    obj.put("year", selectTime);
                    obj.put("type_id", selectType);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("规培阶段性考核详细信息数据：", result);

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

    private void initPeriodicalExamListView() {
        baseListAdapter = new BaseListAdapter(lvPeriodicalExam, periodicalExamList, R.layout.item_gp_periodical_exam_list) {
            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map<String, String> map = (Map<String, String>) item;
                helper.setText(R.id.id_tv_item_title, map.get("exam_name").toString());
                helper.setText(R.id.id_tv_item_description, map.get("type_name").toString() + "\t" + map.get("base_name").toString());
                helper.setText(R.id.id_tv_item_time, map.get("s_time").toString() + "~" + map.get("e_time").toString());
            }
        };

        lvPeriodicalExam.setAdapter(baseListAdapter);

        lvPeriodicalExam.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(context, "选择项："+periodicalExamList.get(i).get("detail_id"), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PeriodicalExamListActivity.this, PeriodicalExamStationListActivity.class);
                intent.putExtra("fid", fid);
                intent.putExtra("detail_id", periodicalExamList.get(i).get("detail_id"));
                startActivity(intent);
            }
        });

        //分页加载
        baseListAdapter.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (isNoMore && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    //页数加1,请求数据
                    page++;
                    initListViewData();

                    isNoMore = false;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //判断是否滚到最后一行
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0) {
                    isNoMore = true;
                }
            }
        });
    }

    private void initPopupWindowView() {
        contentView1 = LayoutInflater.from(context).inflate(R.layout.popupwindow_periodical_exam_time_select, null, false);
        ListView lvTime = contentView1.findViewById(R.id.id_lv_periodical_exam_time_select);
        arrayAdapterTime = new ArrayAdapter<String>(this,
                R.layout.simple_list_item_1_center, timeList);
        lvTime.setAdapter(arrayAdapterTime);
        lvTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(context, "选择时间："+timeList.get(i), Toast.LENGTH_SHORT).show();
                selectTime = timeMapList.get(i).get("year");
                popupWindow1.dismiss();
                page = 1;
                periodicalExamList.clear();
                initListViewData();
            }
        });

        contentView2 = LayoutInflater.from(context).inflate(R.layout.popupwindow_periodical_exam_type_select, null, false);
        ListView lvType = contentView2.findViewById(R.id.id_lv_periodical_exam_type_select);
        arrayAdapterType = new ArrayAdapter<String>(this,
                R.layout.simple_list_item_1_center, typeList);
        lvType.setAdapter(arrayAdapterType);
        lvType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(context, "选择类型："+typeList.get(i), Toast.LENGTH_SHORT).show();
                selectType = typeMapList.get(i).get("type_id");
                popupWindow2.dismiss();
                page = 1;
                periodicalExamList.clear();
                initListViewData();
            }
        });
    }

    public void hidePopStatus() {
        tvTimeSelect.setTextColor(Color.parseColor("#333333"));
        ivTimeSelect.setImageResource(R.mipmap.ic_periodical_exam_arrow_down);
        tvExamTypeSelect.setTextColor(Color.parseColor("#333333"));
        ivExamTypeSelect.setImageResource(R.mipmap.ic_periodical_exam_arrow_down);
    }

    public void popwindow(View view) {
        tvTimeSelect.setTextColor(Color.parseColor("#3D97F3"));
        ivTimeSelect.setImageResource(R.mipmap.ic_periodical_exam_arrow_up);
        tvExamTypeSelect.setTextColor(Color.parseColor("#333333"));
        ivExamTypeSelect.setImageResource(R.mipmap.ic_periodical_exam_arrow_down);

        /*View contentView = LayoutInflater.from(context).inflate(R.layout.popupwindow_periodical_exam_time_select, null, false);
        ListView lvTime = contentView.findViewById(R.id.id_lv_periodical_exam_time_select);
        ArrayAdapter<String> array=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,typeData);
        lvTime.setAdapter(array);*/
        // 创建PopupWindow对象，其中：
        // 第一个参数是用于PopupWindow中的View，第二个参数是PopupWindow的宽度，
        // 第三个参数是PopupWindow的高度，第四个参数指定PopupWindow能否获得焦点
        popupWindow1 = new PopupWindow(contentView1, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);

//        setBackgroundAlpha(0.5f);//设置屏幕透明度
        // 设置PopupWindow的背景
        popupWindow1.setBackgroundDrawable(new ColorDrawable());
        // 设置PopupWindow是否能响应外部点击事件
        popupWindow1.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        popupWindow1.setTouchable(true);
        // 显示PopupWindow，其中：
        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
        popupWindow1.showAsDropDown(llTimeSelect);
//        popupWindow.showAtLocation(contentView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        popupWindow1.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // popupWindow隐藏时恢复屏幕正常透明度
//                setBackgroundAlpha(1.0f);
                hidePopStatus();
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 屏幕透明度0.0-1.0 1表示完全不透明
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) context).getWindow().setAttributes(lp);
    }

    public void popwindow2(View view) {
        tvTimeSelect.setTextColor(Color.parseColor("#333333"));
        ivTimeSelect.setImageResource(R.mipmap.ic_periodical_exam_arrow_down);
        tvExamTypeSelect.setTextColor(Color.parseColor("#3D97F3"));
        ivExamTypeSelect.setImageResource(R.mipmap.ic_periodical_exam_arrow_up);

        // 创建PopupWindow对象，其中：
        // 第一个参数是用于PopupWindow中的View，第二个参数是PopupWindow的宽度，
        // 第三个参数是PopupWindow的高度，第四个参数指定PopupWindow能否获得焦点
        popupWindow2 = new PopupWindow(contentView2, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);

//        setBackgroundAlpha(0.5f);//设置屏幕透明度
        // 设置PopupWindow的背景
        popupWindow2.setBackgroundDrawable(new ColorDrawable());
        // 设置PopupWindow是否能响应外部点击事件
        popupWindow2.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        popupWindow2.setTouchable(true);
        // 显示PopupWindow，其中：
        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
        popupWindow2.showAsDropDown(llExamTypeSelect);
//        popupWindow.showAtLocation(contentView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        popupWindow2.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // popupWindow隐藏时恢复屏幕正常透明度
//                setBackgroundAlpha(1.0f);
                hidePopStatus();
            }
        });
    }


    public void back(View view) {
        finish();
    }
}
