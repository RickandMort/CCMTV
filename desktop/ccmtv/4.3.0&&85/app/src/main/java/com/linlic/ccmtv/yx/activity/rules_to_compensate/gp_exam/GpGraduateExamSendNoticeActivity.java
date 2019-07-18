package com.linlic.ccmtv.yx.activity.rules_to_compensate.gp_exam;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.TimeUtil;
import com.linlic.ccmtv.yx.widget.CustomDatePicker;

import org.feezu.liuli.timeselector.TimeSelector;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GpGraduateExamSendNoticeActivity extends BaseActivity implements View.OnClickListener {

    private Context context;
    private TextView title_name;
    private TextView tvHint;
    private EditText etStartTime;
    private EditText etEndTime;
    private EditText etExamSite;
    private ListView lvStuList;
    private Button btnSend;
    private BaseListAdapter baseListAdapter;
    private List<Map<String, String>> studentList = new ArrayList<>();
    private String is_send = "";
    private CustomDatePicker customDatePicker1, customDatePicker2;
    private String now;
    private SimpleDateFormat sdf;
    private String fid = "";
    private String year = "";
    private String month = "";
    private String week = "";
    private String end_time = "";
    private String current_week_msg;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getString("code").equals("200")) { // 成功
                            JSONObject data = jsonObject.getJSONObject("data");
                            if (data.getInt("status") == 1) {
                                finish();
                                Toast.makeText(context, data.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, data.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
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
        setContentView(R.layout.activity_gp_graduate_exam_send_notice);

        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        now = sdf.format(new Date());
        context=this;
        findId();
        initListView();
        getIntentData();
//        initDatePicker();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        if ("84".equals(fid)){//考核基地
            enterUrl = "http://yun.ccmtv.cn/admin.php/wx/exambase/index.html";
        } else {
            enterUrl = "http://yun.ccmtv.cn/admin.php/wx/allexam/index.html";
        }
        super.onPause();
    }

    public void findId() {
        title_name = (TextView) findViewById(R.id.activity_title_name);
        tvHint = (TextView) findViewById(R.id.id_tv_gp_graduate_exam_hint_1);
        etStartTime = (EditText) findViewById(R.id.id_et_gp_graduate_exam_send_notice_start_time);
        etEndTime = (EditText) findViewById(R.id.id_et_gp_graduate_exam_send_notice_end_time);
        etExamSite = (EditText) findViewById(R.id.id_et_gp_graduate_exam_send_notice_exam_site);
        lvStuList = (ListView) findViewById(R.id.id_lv_gp_graduate_exam_send_notice_stu_list);
        btnSend = (Button) findViewById(R.id.id_btn_gp_graduate_exam_send_notice_send);

        title_name.setText("发送出科考核通知");
        etStartTime.setOnClickListener(this);
        etEndTime.setOnClickListener(this);
        etExamSite.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        /*etStartTime.setOnFocusChangeListener(this);
        etEndTime.setOnFocusChangeListener(this);*/
       /* etStartTime.setInputType(InputType.TYPE_NULL);
        etEndTime.setInputType(InputType.TYPE_NULL);*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_et_gp_graduate_exam_send_notice_start_time:
//                showDatePickerDialog(GpGraduateExamSendNoticeActivity.this,etStartTime, Calendar.getInstance());
//                customDatePicker1.show(now);
                showDatePicker(now,end_time,etStartTime);
                break;
            case R.id.id_et_gp_graduate_exam_send_notice_end_time:
//                showDatePickerDialog(GpGraduateExamSendNoticeActivity.this,etEndTime, Calendar.getInstance());
//                customDatePicker2.show(now);
                showDatePicker(now,end_time,etEndTime);
                break;
            case R.id.id_et_gp_graduate_exam_send_notice_exam_site:
                break;
            case R.id.id_btn_gp_graduate_exam_send_notice_send:
                sendNotice();
                break;
        }
    }

    private void showDatePicker(String now, String end_time, final EditText etTime) {
        TimeSelector timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                    etTime.setText(time);
            }
        }, now, end_time);
        timeSelector.setIsLoop(false);
        timeSelector.show();
    }

    /*@Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()){
            case R.id.id_et_gp_graduate_exam_send_notice_start_time:
                customDatePicker1.show(now);
                break;
            case R.id.id_et_gp_graduate_exam_send_notice_end_time:
                customDatePicker2.show(now);
                break;
        }
    }*/

    /**
     * 需要发送通知的学员信息已在intentdate里面
     */
    private void getIntentData() {
        String result = getIntent().getStringExtra("result");
        fid = getIntent().getStringExtra("fid");
        LogUtil.e(getLocalClassName(),result);

        try {
            final JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.getString("code").equals("200")) { // 成功
                JSONObject dataObject = jsonObject.getJSONObject("data");
                if (dataObject.getInt("status") == 1) { // 成功
                    JSONObject dataObject1 = dataObject.getJSONObject("data");
                    is_send = dataObject1.getString("is_send");
                    year = dataObject1.getString("year");
                    month = dataObject1.getString("month");
                    week = dataObject1.getString("week");
                    now = dataObject1.getString("start_time");
                    end_time = dataObject1.getString("end_time");
                    current_week_msg = dataObject1.getString("current_week_msg");
                    if (!end_time.isEmpty() && !now.isEmpty() && TimeUtil.dateToMillionAskLeave(now) >= TimeUtil.dateToMillionAskLeave(end_time)) {
                        Toast.makeText(context, "数据维护中,请稍后再试！", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                    if (dataObject1.has("user_list")) {
                        JSONArray userListArray = dataObject1.getJSONArray("user_list");
                        for (int i=0;i<userListArray.length();i++) {
                            JSONObject userObject = userListArray.getJSONObject(i);
                            Map<String, String> map = new HashMap<>();
                            map.put("name", userObject.getString("realname"));
                            map.put("department", userObject.getString("standard_kname"));
                            studentList.add(map);
                        }
                    }
                    baseListAdapter.notifyDataSetChanged();
                    tvHint.setText(current_week_msg);
                } else {
                    Toast.makeText(context, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化需要发送通知的学员列表
     */
    private void initListView() {
        baseListAdapter = new BaseListAdapter(lvStuList, studentList, R.layout.item_graduate_exam_send_notice_list) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map<String,Object> map = (Map<String,Object>) item;
                helper.setText(R.id.id_tv_item_graduate_exam_send_notice_name, ((Map<String, Object>) item).get("name").toString());
                helper.setText(R.id.id_tv_item_graduate_exam_send_notice_department, ((Map<String, Object>) item).get("department").toString());
            }
        };
        lvStuList.setAdapter(baseListAdapter);
    }

    /**
     * 发送出科考核通知
     */
    private void sendNotice() {

        final String startTime = etStartTime.getText().toString();
        final String endTime = etEndTime.getText().toString();
        final String examSite = etExamSite.getText().toString();
        if (startTime.isEmpty()) {
            Toast.makeText(context, "请输入考试开始时间", Toast.LENGTH_SHORT).show();
            return;
        }

        if (endTime.isEmpty()) {
            Toast.makeText(context, "请输入考试结束时间", Toast.LENGTH_SHORT).show();
            return;
        }

        if (examSite.isEmpty()) {
            Toast.makeText(context, "请输入考试地点", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!etStartTime.getText().toString().isEmpty() &&
                TimeUtil.dateToMillionAskLeave(etStartTime.getText().toString()) >= TimeUtil.dateToMillionAskLeave(etEndTime.getText().toString())) {
            Toast.makeText(context, "考试结束时间不能小于开始时间，请重新选择！", Toast.LENGTH_SHORT).show();
            etEndTime.setText("");
            return;
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.sendLeaveNotice);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("fid", fid);
                    obj.put("year", year);
                    obj.put("month", month);
                    obj.put("week", week);
                    obj.put("starttime", startTime);
                    obj.put("endtime", endTime);
                    obj.put("exam_place", examSite);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("规培出科考核发送通知数据：", result);

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

    private void initDatePicker() {
        /*sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        now = sdf.format(new Date());
        try {
            customDatePicker1 = new CustomDatePicker(context, new CustomDatePicker.ResultHandler() {
                @Override
                public void handle(String time) { // 回调接口，获得选中的时间
                    etStartTime.setText(time);
                }
            }, now, end_time); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
            customDatePicker1.showSpecificTime(true); // 显示时和分
            customDatePicker1.setIsLoop(true); // 允许循环滚动

            customDatePicker2 = new CustomDatePicker(context, new CustomDatePicker.ResultHandler() {
                @Override
                public void handle(String time) { // 回调接口，获得选中的时间
                    etEndTime.setText(time);
                }
            }, now, end_time); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
            customDatePicker2.showSpecificTime(true); // 显示时和分
            customDatePicker2.setIsLoop(true); // 允许循环滚动
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
//            customDatePicker1.dismissDialog();
//            customDatePicker2.dismissDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void back(View view) {
        finish();
    }
}
