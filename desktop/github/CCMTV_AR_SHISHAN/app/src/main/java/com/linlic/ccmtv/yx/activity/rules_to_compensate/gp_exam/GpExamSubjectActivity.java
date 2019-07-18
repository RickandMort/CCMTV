package com.linlic.ccmtv.yx.activity.rules_to_compensate.gp_exam;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.adapter.GpExamSubjectAdapter;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.javabean.GpExamSubject;
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
 * 规培考试科目界面
 * Created by bentley on 2018/6/26.
 */

public class GpExamSubjectActivity extends BaseActivity implements View.OnClickListener {

    private TextView title_name;
    private ListView lvSubject;
    private EditText etReason;
    private TextView tvSubmit, tvCancel;
    private RadioGroup radioGroupStatus;
    private RadioButton rbNormal, rbAbsence, rbNonExam;
    private LinearLayout llAbsenceNonexam;
    private LinearLayout llSaveOrCancel;
    private Context context;
    private List<GpExamSubject> examSubjectList = new ArrayList<>();
    private GpExamSubjectAdapter gpExamSubjectAdapter;
    private String gp_uid = "";
    private String details_id = "";
    private String standard_kid = "";
    private String leave_ks_id = "";
    private String status = "";
    private String reason = "";
    private String fid = "";
    private String disabled = "";
    private String name = "";
    private String pid="";
    private JSONArray itemsArray;
    private String year = "";
    private String month = "";
    private String week = "";
    private String is_allow_reupdatedisable="";

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
                                gp_uid = dataObject.getString("gp_uid");
                                details_id = dataObject.getString("details_id");
                                standard_kid = dataObject.getString("standard_kid");
                                is_allow_reupdatedisable=dataObject.getString("is_allow_reupdatedisable");
                                status = dataObject.getString("status");
                                reason = dataObject.getString("reason");
//                                disabled = dataObject.getString("disabled");
                                if (dataObject.has("items")) {
                                    examSubjectList.clear();
                                    itemsArray = dataObject.getJSONArray("items");
                                    for (int i = 0; i < itemsArray.length(); i++) {
                                        JSONObject itemObject = itemsArray.getJSONObject(i);
                                        GpExamSubject gpExamSubject = new GpExamSubject();
                                        gpExamSubject.setId(itemObject.getString("id"));
                                        gpExamSubject.setDisabled(itemObject.getString("disabled"));
                                        gpExamSubject.setItem_id(itemObject.getString("item_id"));
                                        if (itemObject.has("score")) {
                                            gpExamSubject.setScore(itemObject.getString("score"));
                                        } else {
                                            gpExamSubject.setScore("");
                                        }
                                        if (itemObject.has("daily_exam_details_id")) {
                                            gpExamSubject.setDaily_exam_details_id(itemObject.getString("daily_exam_details_id"));
                                        } else {
                                            gpExamSubject.setDaily_exam_details_id("");
                                        }
                                        gpExamSubject.setName(itemObject.getString("name"));
                                        gpExamSubject.setItem_status(itemObject.getString("item_status"));

                                        gpExamSubject.setType(itemObject.getString("type"));
                                        if (itemObject.has("url")) {
                                            gpExamSubject.setUrl(itemObject.getString("url"));
                                        }
                                        gpExamSubject.setRelated_examinations( itemObject.has("lilun_status")?itemObject.getString("lilun_status"):"0");

                                        examSubjectList.add(gpExamSubject);
                                    }
                                    gpExamSubjectAdapter.notifyDataSetChanged();
                                }

                                changeView();
                            } else {
                                rbNormal.setEnabled(false);
                                rbAbsence.setEnabled(false);
                                rbNonExam.setEnabled(false);
                                etReason.setEnabled(false);
                                Toast.makeText(context, data.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                finish();
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
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getString("code").equals("200")) {
                            JSONObject data = result.getJSONObject("data");
                            if (data.getInt("status") == 1) {
                                finish();
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
        setContentView(R.layout.activity_gp_exam_subject);
        context = this;
        findId();
        getIntentData();
        initListView();

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

    public void findId() {
        lvSubject = (ListView) findViewById(R.id.id_lv_gp_exam_subject);
        title_name = (TextView) findViewById(R.id.activity_title_name);
        etReason = (EditText) findViewById(R.id.id_et_gp_graduate_exam_reason);
        tvSubmit = (TextView) findViewById(R.id.id_tv_gp_exam_subject_submit);
        tvCancel = (TextView) findViewById(R.id.id_tv_gp_exam_subject_cancel);
        radioGroupStatus = (RadioGroup) findViewById(R.id.id_radiogroup_gp_exam_status);
        rbNormal = (RadioButton) findViewById(R.id.id_rb_gp_exam_subject_status_normal);
        rbAbsence = (RadioButton) findViewById(R.id.id_rb_gp_exam_subject_status_absence);
        rbNonExam = (RadioButton) findViewById(R.id.id_rb_gp_exam_subject_status_nonexam);
        llAbsenceNonexam = (LinearLayout) findViewById(R.id.id_ll_gp_exam_subject_absence_nonexam);
        llSaveOrCancel = (LinearLayout) findViewById(R.id.id_ll_gp_exam_subject_button_layout);

        title_name.setText("考核科目");

        tvSubmit.setOnClickListener(this);
        tvCancel.setOnClickListener(this);

        lvSubject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (examSubjectList.get(position).getType()){
                    case "0":
                    case "2":
                        if (examSubjectList.get(position).getName().contains("日常考核")) {
                            if (examSubjectList.get(position).getScore().isEmpty() || !isNumber(examSubjectList.get(position).getScore())) {
                                Toast.makeText(context, "无日常考核成绩", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(context, GpGraduateToDailyExamActivity.class);
                                intent.putExtra("fid", fid);
                                intent.putExtra("daily_exam_details_id", examSubjectList.get(position).getDaily_exam_details_id());
                                startActivity(intent);
                            }
                        } else {
                            Intent intent = new Intent(context, GraduateExamActivity.class);
                            intent.putExtra("fid", fid);
                            intent.putExtra("gp_uid", gp_uid);
                            intent.putExtra("standard_kid", standard_kid);
                            intent.putExtra("item_id", examSubjectList.get(position).getItem_id());
                            intent.putExtra("details_id", details_id);
                            intent.putExtra("item_status", examSubjectList.get(position).getItem_status());
                            intent.putExtra("itemArray", itemsArray.toString());
                            intent.putExtra("related_examinations", examSubjectList.get(position).getRelated_examinations());
                            intent.putExtra("type", examSubjectList.get(position).getType());
                            if(disabled.equals("1")){
                                intent.putExtra("disabled", disabled);       //1  不可编辑   0可以编辑
                            }else {
                                if(is_allow_reupdatedisable.equals("1")){
                                    intent.putExtra("disabled",examSubjectList.get(position).getDisabled());
                                }else {
                                    intent.putExtra("disabled", disabled);
                                }
                                     //1  不可编辑   0可以编辑
                            }
                            startActivity(intent);
                        }
                        break;
                    default:
                        Intent intent = new Intent(context, GpWebViewActivity.class);
                        intent.putExtra("url", examSubjectList.get(position).getUrl()+"&type="+disabled);
                        intent.putExtra("name", name);
                        startActivity(intent);
                        break;
                }

            }
        });

        radioGroupStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.id_rb_gp_exam_subject_status_normal:
                        status = "1";
                        lvSubject.setVisibility(View.VISIBLE);
                        llAbsenceNonexam.setVisibility(View.GONE);
                        break;
                    case R.id.id_rb_gp_exam_subject_status_absence:
                        status = "2";
                        lvSubject.setVisibility(View.GONE);
                        llAbsenceNonexam.setVisibility(View.VISIBLE);
                        break;
                    case R.id.id_rb_gp_exam_subject_status_nonexam:
                        status = "3";
                        lvSubject.setVisibility(View.GONE);
                        llAbsenceNonexam.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_tv_gp_exam_subject_submit:
//                Toast.makeText(context, "上传缺考或不考核理由", Toast.LENGTH_SHORT).show();
                if (etReason.getText().toString().isEmpty()) {
                    Toast.makeText(context, "请输入缺考或不考核理由", Toast.LENGTH_SHORT).show();
                } else {
                    sendAbsenceOrNonExam();
                }
                break;
            case R.id.id_tv_gp_exam_subject_cancel:
                finish();
                break;
        }
    }

    private void getIntentData() {
        try {
            fid = getIntent().getStringExtra("fid");
            gp_uid = getIntent().getStringExtra("gp_uid");
            standard_kid = getIntent().getStringExtra("standard_kid");
            leave_ks_id = getIntent().getStringExtra("leave_ks_id");
            year = getIntent().getStringExtra("year");
            month = getIntent().getStringExtra("month");
            week = getIntent().getStringExtra("week");
            disabled = getIntent().getStringExtra("disabled");
            name = getIntent().getStringExtra("name");
            pid = getIntent().getStringExtra("pid");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initListView() {
        /*for (int i=0;i<20;i++){
            GpExamSubject examSubject = new GpExamSubject();
            examSubject.setSubjectName("日常考核"+i);
            examSubject.setSubjectScore("99");
            examSubjectList.add(examSubject);
        }*/
        gpExamSubjectAdapter = new GpExamSubjectAdapter(context, examSubjectList);
        lvSubject.setAdapter(gpExamSubjectAdapter);
    }

    private void initData() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.leaveKsUserItems);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("fid", fid);
                    obj.put("gp_uid", gp_uid);
                    obj.put("standard_kid", standard_kid);
                    obj.put("leave_ks_id", leave_ks_id);
                    obj.put("year", year);
                    obj.put("month", month);
                    obj.put("week", week);
                    obj.put("pid",pid);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("规培出科考核科目数据：", result);

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
        //status: 1正常 2全部缺考 3不考核
        if (status.equals("1")) {
            lvSubject.setVisibility(View.VISIBLE);
            llAbsenceNonexam.setVisibility(View.GONE);
            rbNormal.setChecked(true);
//            rbNormal.setEnabled(false);
            rbAbsence.setChecked(false);
//            rbAbsence.setEnabled(false);
            rbNonExam.setChecked(false);
//            rbNonExam.setEnabled(false);
        } else if (status.equals("2")) {
            lvSubject.setVisibility(View.GONE);
            llAbsenceNonexam.setVisibility(View.VISIBLE);
            etReason.setText(reason);
            rbNormal.setChecked(false);
//            rbNormal.setEnabled(false);
            rbAbsence.setChecked(true);
//            rbAbsence.setEnabled(false);
            rbNonExam.setChecked(false);
//            rbNonExam.setEnabled(false);
        } else if (status.equals("3")) {
            lvSubject.setVisibility(View.GONE);
            llAbsenceNonexam.setVisibility(View.VISIBLE);
            etReason.setText(reason);
            rbNormal.setChecked(false);
//            rbNormal.setEnabled(false);
            rbAbsence.setChecked(false);
//            rbAbsence.setEnabled(false);
            rbNonExam.setChecked(true);
//            rbNonExam.setEnabled(false);
        } else {
            lvSubject.setVisibility(View.VISIBLE);
            llAbsenceNonexam.setVisibility(View.GONE);
        }

        if (disabled.equals("0")) {      //1  不可编辑   0可以编辑
            rbNormal.setEnabled(true);
            rbAbsence.setEnabled(true);
            rbNonExam.setEnabled(true);
            etReason.setEnabled(true);
            llSaveOrCancel.setVisibility(View.VISIBLE);
        } else {
            rbNormal.setEnabled(false);
            rbAbsence.setEnabled(false);
            rbNonExam.setEnabled(false);
            etReason.setEnabled(false);
            llSaveOrCancel.setVisibility(View.GONE);
        }
    }

    private void sendAbsenceOrNonExam() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.leaveKsEnterSatus);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("details_id", details_id);
                    obj.put("status", status);
                    obj.put("reason", etReason.getText().toString());
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("规培出科考核录入成绩（缺考+不考核）数据：", result);

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

    //判断字符串是否为数字
    public static boolean isNumber(String str) {
        String reg = "^[0-9]+(.[0-9]+)?$";
        return str.matches(reg);
    }

    public void back(View view) {
        finish();
    }
}
