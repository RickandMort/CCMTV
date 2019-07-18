package com.linlic.ccmtv.yx.activity.rules_to_compensate.audit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.adapter.ExamStudentListAdapter;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.gp_exam.GpDailyExamActivity;
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
 * 规培审核日常考核学员列表
 */

public class GpDailyExamAuditList extends BaseActivity implements View.OnClickListener {

    private TextView title_name;
    private ListView lvAuditStudent;
    private EditText etReturnReason;
    private Button btnSubmit;
    private Button btnReturn;
//    private LinearLayout llSubmitLayout;
//    private RadioGroup radioGroupStatus;
//    private RadioButton rbPass, rbReturn;
    private List<ExamStudent> listAuditStudent = new ArrayList<>();
    private Context context;
    private String id = "";
    private String fid = "";
    private String primaryId = "";
    private ExamStudentListAdapter auditStudentListAdapter;
    private String dataStatus = "";
    private String updateStatus = "";
    private String dataType = "";
    private String auditStatus = "";

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
                                updateStatus = dataList.getString("update_status");
                                JSONObject statusObject = dataList.getJSONObject("data_status");
                                dataStatus = statusObject.getString("status");

                                if (dataList.has("data_list")) {
                                    listAuditStudent.clear();
                                    JSONArray listAuditArray = dataList.getJSONArray("data_list");
                                    for (int i = 0; i < listAuditArray.length(); i++) {
                                        JSONObject listAuditObject = listAuditArray.getJSONObject(i);
                                        ExamStudent examAuditStudent = new ExamStudent();
                                        examAuditStudent.setRealname(listAuditObject.getString("realname"));
                                        examAuditStudent.setScore(listAuditObject.getString("score"));
                                        examAuditStudent.setUid(listAuditObject.getString("uid"));
                                        examAuditStudent.setGp_exam_id(listAuditObject.getString("daily_exam_id"));
                                        examAuditStudent.setStandard_kname(listAuditObject.getString("name"));
                                        listAuditStudent.add(examAuditStudent);
                                    }
                                }

                                changeView();

                                auditStudentListAdapter.notifyDataSetChanged();
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
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getString("code").equals("200")) {
                            JSONObject data = result.getJSONObject("data");
                            if (data.getString("status").equals("1")) {
                                MyProgressBarDialogTools.hide();
                                initData();
                                JSONObject Event_json =new JSONObject();
                                Event_json.put(id,auditStatus);
                                SharedPreferencesTools.saveEvent_details_status(context,Event_json.toString());
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
        setContentView(R.layout.activity_gp_daily_exam_audit_list);
        context = GpDailyExamAuditList.this;
        findId();
        getIntentData();
        initListView();

    }

    public void findId() {
        title_name = (TextView) findViewById(R.id.activity_title_name);
        title_name.setText("审核列表");
        lvAuditStudent = (ListView) findViewById(R.id.id_lv_gp_exam_student_list);
        etReturnReason = (EditText) findViewById(R.id.id_et_id_et_return_reason);
        btnSubmit = (Button) findViewById(R.id.id_btn_gp_exam_student_list_submit);
        btnReturn = (Button) findViewById(R.id.id_btn_gp_exam_student_list_return);
//        llSubmitLayout = (LinearLayout) findViewById(R.id.id_ll_gp_audit_student_list_layout);
//        radioGroupStatus = (RadioGroup) findViewById(R.id.id_radiogroup_gp_audit_exam_status);
//        rbPass = (RadioButton) findViewById(R.id.id_rb_gp_audit_exam_status_pass);
//        rbReturn = (RadioButton) findViewById(R.id.id_rb_gp_audit_exam_status_return);

        lvAuditStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, GpDailyExamActivity.class);
                intent.putExtra("daily_exam_id", listAuditStudent.get(position).getGp_exam_id());
                intent.putExtra("user_id", listAuditStudent.get(position).getUid());
                intent.putExtra("data_type", "2");
                intent.putExtra("status", "1");  //1 是审核通过(不可点击) 2 是从新审核（可点击） 3 是提交成绩（可点击） 4是等待审核（不可点击） 5不显示按钮
                intent.putExtra("fid", fid);
                intent.putExtra("identity", "staff");
                startActivity(intent);

            }
        });

        btnSubmit.setOnClickListener(this);
        btnReturn.setOnClickListener(this);

        /*radioGroupStatus.clearCheck();

        radioGroupStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.id_rb_gp_audit_exam_status_pass:
                        etReturnReason.setVisibility(View.GONE);
                        auditStatus = "1"; //通过 为1 退回 为2
                        break;
                    case R.id.id_rb_gp_audit_exam_status_return:
                        etReturnReason.setVisibility(View.VISIBLE);
                        auditStatus = "2"; //通过 为1 退回 为2
                        break;
                }
            }
        });*/
    }

    private void getIntentData() {
        try {
            id = getIntent().getStringExtra("id");
            fid = getIntent().getStringExtra("fid");
            primaryId = getIntent().getStringExtra("primaryId");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();

    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/verify/index.html";
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_btn_gp_exam_student_list_submit:
                auditStatus = "1";  //通过 为1 退回 为2
                submitExamine();
                break;
            case R.id.id_btn_gp_exam_student_list_return:
                auditStatus = "2";
                if (etReturnReason.getText().toString().isEmpty()) {
                    Toast.makeText(context, "退回原因不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                submitExamine();
                break;
        }
    }

    private void submitExamine() {

        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.dailyManWorkOperate);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("fid", fid);
                    obj.put("id", id);
                    obj.put("status", auditStatus);
                    obj.put("errormsg", etReturnReason.getText().toString());
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("规培日常考核提交审核数据：", result);

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
        auditStudentListAdapter = new ExamStudentListAdapter(context, listAuditStudent,"1");
        lvAuditStudent.setAdapter(auditStudentListAdapter);
    }

    /**
     * 获取日常考核审核学员列表
     */
    private void initData() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.dailyManWork);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("id", id);
                    obj.put("fid", fid);
                    obj.put("primaryId", primaryId);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("日常考核审核学员列表数据：", result);

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
        switch (dataStatus) {
            case "0":
//                llSubmitLayout.setVisibility(View.VISIBLE);
                btnSubmit.setText("通过");
                btnSubmit.setBackgroundResource(R.mipmap.button_01);
                btnSubmit.setClickable(true);
                etReturnReason.setVisibility(View.VISIBLE);
                btnReturn.setVisibility(View.VISIBLE);
                break;
            case "1":
//                llSubmitLayout.setVisibility(View.GONE);
                btnSubmit.setText("审核通过");
                btnSubmit.setBackgroundResource(R.mipmap.button_03);
                btnSubmit.setClickable(false);
                etReturnReason.setVisibility(View.GONE);
                btnReturn.setVisibility(View.GONE);
                break;
            case "2":
//                llSubmitLayout.setVisibility(View.GONE);
                btnSubmit.setText("审核不通过");
                btnSubmit.setBackgroundResource(R.mipmap.button_03);
                btnSubmit.setClickable(false);
                etReturnReason.setVisibility(View.GONE);
                btnReturn.setVisibility(View.GONE);
                break;
        }
    }

    public void back(View view) {
        finish();
    }
}

