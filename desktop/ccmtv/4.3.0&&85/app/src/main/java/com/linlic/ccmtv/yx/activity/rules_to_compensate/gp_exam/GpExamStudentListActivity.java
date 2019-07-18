package com.linlic.ccmtv.yx.activity.rules_to_compensate.gp_exam;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.adapter.ExamStudentListAdapter;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.javabean.ExamStudent;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 规培考试学生列表界面
 * Created by bentley on 2018/6/26.
 */

public class GpExamStudentListActivity extends BaseActivity implements View.OnClickListener {

    private TextView title_name;
    private ListView lvExamStudentYes;
    private ListView lvExamStudentNo;
    private TextView tvPreviousData;
    private TextView tvHint;
    private TextView tvRejectReason;
    private ImageView ivPreviousData;
    private Button btnSubmit;
    private LinearLayout llPreviousData;
    private LinearLayout llYesLayout;
    private List<ExamStudent> listStudentNo = new ArrayList<>();
    private List<ExamStudent> listStudentYes = new ArrayList<>();
    private Context context;
    private String exam_id = "";
    private String year = "";
    private String month = "";
    private String fid = "";
    private String exam_type = "";
    private String exam_name = "";
    private ExamStudentListAdapter examStudentListAdapterYes;
    private ExamStudentListAdapter examStudentListAdapterNo;
    private String dataStatus = "";
    private String dataStatusName = "";
    private String dataErrormsg = "";//如果 status 为2 errormsg 审核失败原因
    private String dataType = "";
    private Boolean isPreviousDataOpen = false;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getString("code").equals("200")) {
                            JSONObject data = result.getJSONObject("data");
                            if (data.getString("status").equals("1")) {
                                JSONObject dataList = data.getJSONObject("dataList");
                                JSONObject statusObject = dataList.getJSONObject("status");
                                dataStatus = statusObject.getString("type");
                                dataStatusName = statusObject.getString("type_name");
                                dataErrormsg = dataList.getString("errormsg");//如果 status 为2 errormsg 审核失败原因
                                dataType = dataList.getString("date_type"); //1：当前月 2：以往月
                                exam_name = dataList.getString("exam_name");
                                if (dataList.has("yes")) {
                                    listStudentYes.clear();
                                    JSONArray listYesArray = dataList.getJSONArray("yes");
                                    for (int i = 0; i < listYesArray.length(); i++) {
                                        JSONObject listYesObject = listYesArray.getJSONObject(i);
                                        ExamStudent examStudentYes = new ExamStudent();
                                        examStudentYes.setRealname(listYesObject.getString("realname"));
                                        examStudentYes.setScore(listYesObject.getString("score"));
                                        examStudentYes.setUid(listYesObject.getString("uid"));
                                        examStudentYes.setStatus(listYesObject.getString("status"));
                                        examStudentYes.setGp_exam_id(listYesObject.getString("daily_exam_id"));
                                        examStudentYes.setStandard_kname(listYesObject.getString("standard_kid"));
                                        examStudentYes.setIs_update(listYesObject.getString("is_update"));
                                        listStudentYes.add(examStudentYes);
                                    }
                                }

                                if (dataList.has("no")) {
                                    listStudentNo.clear();
                                    JSONArray listNoArray = dataList.getJSONArray("no");
                                    for (int i = 0; i < listNoArray.length(); i++) {
                                        JSONObject listNoObject = listNoArray.getJSONObject(i);
                                        ExamStudent examStudentNo = new ExamStudent();
                                        examStudentNo.setRealname(listNoObject.getString("realname"));
                                        examStudentNo.setScore(listNoObject.getString("score"));
                                        examStudentNo.setUid(listNoObject.getString("uid"));
                                        examStudentNo.setStatus(listNoObject.getString("status"));
                                        examStudentNo.setGp_exam_id(listNoObject.getString("daily_exam_id"));
                                        examStudentNo.setStandard_kname(listNoObject.getString("standard_kid"));
                                        examStudentNo.setIs_update(listNoObject.getString("is_update"));
                                        listStudentNo.add(examStudentNo);
                                    }
                                }
                                changeView();

                                examStudentListAdapterYes.notifyDataSetChanged();
                                examStudentListAdapterNo.notifyDataSetChanged();


                            } else {
                                Toast.makeText(context, data.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, result.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        handler.sendEmptyMessageDelayed(10, 2000);
                    } catch (Exception e) {
                        MyProgressBarDialogTools.hide();
                        e.printStackTrace();
                    }
                    break;

                case 2:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getString("code").equals("200")) {
                            JSONObject data = result.getJSONObject("data");
                            if (data.getString("status").equals("1")) {
                                MyProgressBarDialogTools.hide();
                                initData();
                                Toast.makeText(context, data.getString("errorMessage"), Toast.LENGTH_SHORT).show();
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
                case 10:
                    MyProgressBarDialogTools.hide();
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
        setContentView(R.layout.activity_exam_student_list);
        context = GpExamStudentListActivity.this;
        findId();
        getIntentData();
        initListView();
    }

    public void findId() {
        title_name = (TextView) findViewById(R.id.activity_title_name);
        title_name.setText("学生列表");
        lvExamStudentYes = (ListView) findViewById(R.id.id_lv_gp_exam_student_list_yes);
        lvExamStudentNo = (ListView) findViewById(R.id.id_lv_gp_exam_student_list_no);
        tvPreviousData = (TextView) findViewById(R.id.id_tv_gp_exam_student_list_previous_data);
        tvHint = (TextView) findViewById(R.id.id_tv_gp_exam_student_list_hint);
        tvRejectReason = (TextView) findViewById(R.id.id_tv_exam_student_list_reject_reason);
        ivPreviousData = (ImageView) findViewById(R.id.id_iv_gp_exam_student_list_previous_data);
        btnSubmit = (Button) findViewById(R.id.id_btn_gp_exam_student_list_submit);
        llPreviousData = (LinearLayout) findViewById(R.id.id_ll_gp_exam_student_list_previous_data);
        llYesLayout = (LinearLayout) findViewById(R.id.id_ll_gp_exam_student_list_yes_layout);
        //
        lvExamStudentYes.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, GpDailyExamActivity.class);
                intent.putExtra("daily_exam_id", listStudentYes.get(position).getGp_exam_id());
                intent.putExtra("user_id", listStudentYes.get(position).getUid());
                intent.putExtra("data_type", dataType);
                intent.putExtra("status", dataStatus);  //1 是审核通过(不可点击) 2 是从新审核（可点击） 3 是提交成绩（可点击） 4是等待审核（不可点击） 5不显示按钮
                intent.putExtra("fid", fid);
                intent.putExtra("isEdit", true);
                intent.putExtra("identity", "staff");
                startActivity(intent);

            }
        });

        lvExamStudentNo.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listStudentNo.get(position).getIs_update().equals("")) {
                    Intent intent = new Intent(context, GpDailyExamActivity.class);
                    intent.putExtra("daily_exam_id", listStudentNo.get(position).getGp_exam_id());
                    intent.putExtra("user_id", listStudentNo.get(position).getUid());
                    intent.putExtra("data_type", dataType);
                    intent.putExtra("status", dataStatus);
                    intent.putExtra("fid", fid);
                    intent.putExtra("isEdit", false);
                    intent.putExtra("identity", "staff");
                    startActivity(intent);
                }
            }
        });

        btnSubmit.setOnClickListener(this);
        llPreviousData.setOnClickListener(this);
    }

    private void getIntentData() {
        exam_type = getIntent().getStringExtra("exam_type");
        exam_name = getIntent().getStringExtra("exam_name");
        exam_id = getIntent().getStringExtra("exam_id");
        year = getIntent().getStringExtra("year");
        month = getIntent().getStringExtra("month");
        fid = getIntent().getStringExtra("fid");

        if (exam_type.equals("1")) {
            tvHint.setVisibility(View.GONE);
        } else {
            tvHint.setVisibility(View.VISIBLE);
        }

        tvPreviousData.setText("本月的往期数据");
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_btn_gp_exam_student_list_submit:
                submitExamine();
                break;
            case R.id.id_ll_gp_exam_student_list_previous_data:
                if (isPreviousDataOpen) {
                    ivPreviousData.setImageResource(R.mipmap.details_icon2);
                    lvExamStudentNo.setVisibility(View.GONE);
                    isPreviousDataOpen = false;
                } else {
                    ivPreviousData.setImageResource(R.mipmap.details_icon3);
                    lvExamStudentNo.setVisibility(View.VISIBLE);
                    isPreviousDataOpen = true;
                }
                break;
        }
    }

    private void submitExamine() {
        /*final JSONArray idArray = new JSONArray();
        try {
            for (int i = 0; i < listStudentYes.size(); i++) {
                idArray.put(i, listStudentYes.get(i).getGp_exam_id());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        String idString = "";
        try {
            for (int i = 0; i < listStudentYes.size(); i++) {
                if (!idString.isEmpty()) {
                    idString = idString + ",";
                }
                idString = idString + listStudentYes.get(i).getGp_exam_id();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        MyProgressBarDialogTools.show(context);
        final String finalIdString = idString;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.dailyManSubmit);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("status", dataStatus);
                    obj.put("exam_name", exam_name);
                    obj.put("id", finalIdString);
                    obj.put("fid", fid);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("规培日常考核提交学员列表数据：", result);

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

    private void initListView() {
        examStudentListAdapterNo = new ExamStudentListAdapter(context, listStudentNo,"1");
        lvExamStudentNo.setAdapter(examStudentListAdapterNo);

        examStudentListAdapterYes = new ExamStudentListAdapter(context, listStudentYes,"1");
        lvExamStudentYes.setAdapter(examStudentListAdapterYes);
    }

    private void initData() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.dailyStaList);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("year", year);
                    obj.put("month", month);
                    obj.put("exam_name", exam_name);
                    obj.put("id", exam_id);
                    obj.put("fid", fid);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("规培日常考核学员列表数据：", result);

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

    private void changeView() {
        if (dataType.equals("2") || dataType.equals("")) {  //1：当前月 2：以往月
            btnSubmit.setVisibility(View.GONE);
            tvHint.setVisibility(View.GONE);
            tvRejectReason.setVisibility(View.GONE);
            llPreviousData.setVisibility(View.GONE);
        } else {

            if (listStudentNo.size() > 0) {
                llPreviousData.setVisibility(View.VISIBLE);
            }
            switch (dataStatus) {
                //1 是审核通过(不可点击) 2 是从新审核（可点击） 3 是提交成绩（可点击） 4是等待审核（不可点击） 5不显示按钮
                case "1":
                    btnSubmit.setVisibility(View.VISIBLE);
                    btnSubmit.setText(dataStatusName);
                    btnSubmit.setBackgroundResource(R.mipmap.button_03);
                    btnSubmit.setClickable(false);
                    break;
                case "2":
                    btnSubmit.setVisibility(View.VISIBLE);
                    btnSubmit.setText(dataStatusName);
                    btnSubmit.setBackgroundResource(R.mipmap.button_01);
                    btnSubmit.setClickable(true);
                    tvRejectReason.setVisibility(View.VISIBLE);
                    tvRejectReason.setText("退回原因：" + dataErrormsg);
                    break;
                case "3":
                    btnSubmit.setVisibility(View.VISIBLE);
                    btnSubmit.setText(dataStatusName);
                    btnSubmit.setBackgroundResource(R.mipmap.button_01);
                    btnSubmit.setClickable(true);
                    break;
                case "4":
                    btnSubmit.setVisibility(View.VISIBLE);
                    btnSubmit.setText(dataStatusName);
                    btnSubmit.setBackgroundResource(R.mipmap.button_03);
                    btnSubmit.setClickable(false);
                    break;
                case "5":
                    btnSubmit.setVisibility(View.GONE);
                    break;
            }
        }
        if (listStudentYes.size() <= 0) {
            ivPreviousData.setImageResource(R.mipmap.details_icon3);
            llYesLayout.setVisibility(View.GONE);
            lvExamStudentNo.setVisibility(View.VISIBLE);
            lvExamStudentNo.setFocusable(true);
            isPreviousDataOpen = true;
        } else {
            ivPreviousData.setImageResource(R.mipmap.details_icon2);
            llYesLayout.setVisibility(View.VISIBLE);
            lvExamStudentNo.setVisibility(View.GONE);
            isPreviousDataOpen = false;
        }
    }

    public void back(View view) {
        finish();
    }
}
