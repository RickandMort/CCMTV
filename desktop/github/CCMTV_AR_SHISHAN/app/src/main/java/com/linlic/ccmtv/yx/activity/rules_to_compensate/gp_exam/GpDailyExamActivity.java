package com.linlic.ccmtv.yx.activity.rules_to_compensate.gp_exam;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Evaluation_in_detail_bean;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.javabean.Daily_exam_of_item;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 规培日常考核界面
 * Created by bentley on 2018/6/29.
 */

public class GpDailyExamActivity extends BaseActivity implements View.OnClickListener {

    private Context context;
    private TextView title_name;
    private TextView tvScore;
    private TextView tvPrompt;
    private TextView tvCancel;
    private TextView tvSubmit;
    private EditText etComments;
    private ListView lvQuestion;
    private LinearLayout llButtonLayout;
    private BaseListAdapter baseListAdapter;
    Evaluation_in_detail_bean evaluation_in_detail_bean = new Evaluation_in_detail_bean();
    //    private List<DailyExamHeadNameBean> dailyExamHeadNameBeanList = new ArrayList<>();
//    private List<String> dailyExamHeadNurseStringList = new ArrayList<>();
//    private List<DailyExamHeadNameBean> dailyExamTreatmentNameBeanList = new ArrayList<>();
//    private List<String> dailyExamTreatmentStringList = new ArrayList<>();
    private List<Daily_exam_of_item> daily_exam_of_item_list = new ArrayList<>();
    private List<Map<String, String>> teacherList = new ArrayList<>();

    private LinearLayout llStuTopLayout, llStuDepartment, llStuTeacher, llStuMarkDate;
    private LinearLayout llDoctorTopLayout, llDoctorMarkDate, llDoctorDepartment, llDoctorTeacher, llDoctorStudent;
    //    private LinearLayout llStuDoctorOfficer, llStuHeadNurse, llDoctorHeadNurse, llDoctorofficer;
    private TextView tvStuDepartment, tvStuTeacher, tvStuMarkDate;
    private TextView tvDoctorMarkDate, tvDoctorDepartment, tvDoctorTeacher, tvDoctorStudent;
//    private TextView tvStuDepartment, tvStuDoctorOfficer, tvStuTeacher, tvStuHeadNurse;
//    private NiceSpinner spinnerDoctorHeadNurse, spinnerDoctorofficer;

    private String daily_exam_id = "";
    private String user_id = "";
    private String intentStatus = "";
    private String dataType = "";
    private String fid = "";
    private String upd_status = "";
    private String standard_name = "";
    private String stuName = "";
    private String stuScore = "";
    private String identity = "";
    //    private String headNurse = "";
//    private String treatment = "";
    private String headNurse_id = "";
    private String treatment_id = "";
    private String teacherName = "";
    private String teacherId = "";
    private String fenTimeString = "";
    private String fen_time = "";
    private String comments = "";
    private String promptHtml = "";
    private boolean canEditable = false;

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
                                JSONObject dataList = data.getJSONObject("dataList");
                                stuName = dataList.getString("user_name");
                                standard_name = dataList.getString("standard_name");
                                upd_status = dataList.getString("upd_status");
                                promptHtml = dataList.getString("html");
                                /*if (dataList.has("head")) {
                                    JSONArray headArray = dataList.getJSONArray("head");
                                    for (int i = 0; i < headArray.length(); i++) {
                                        JSONObject listObject = headArray.getJSONObject(i);
                                        DailyExamHeadNameBean dailyExamHeadNameBean = new DailyExamHeadNameBean();
                                        dailyExamHeadNameBean.setId(listObject.getString("id"));
                                        dailyExamHeadNameBean.setName(listObject.getString("name"));
                                        dailyExamHeadNameBean.setHospital_kid(listObject.getString("hospital_kid"));
                                        dailyExamHeadNameBean.setHosid(listObject.getString("hosid"));
                                        dailyExamHeadNameBean.setCreatetime(listObject.getString("createtime"));
                                        dailyExamHeadNurseStringList.add(listObject.getString("name"));
                                        dailyExamHeadNameBeanList.add(dailyExamHeadNameBean);
                                    }
                                }

                                if (dataList.has("treatment")) {
                                    JSONArray headArray = dataList.getJSONArray("treatment");
                                    for (int i = 0; i < headArray.length(); i++) {
                                        JSONObject listObject = headArray.getJSONObject(i);
                                        DailyExamHeadNameBean dailyExamHeadNameBean = new DailyExamHeadNameBean();
                                        dailyExamHeadNameBean.setId(listObject.getString("id"));
                                        dailyExamHeadNameBean.setName(listObject.getString("name"));
                                        dailyExamHeadNameBean.setHospital_kid(listObject.getString("hospital_kid"));
                                        dailyExamHeadNameBean.setHosid(listObject.getString("hosid"));
                                        dailyExamHeadNameBean.setCreatetime(listObject.getString("createtime"));
                                        dailyExamTreatmentStringList.add(listObject.getString("name"));
                                        dailyExamTreatmentNameBeanList.add(dailyExamHeadNameBean);
                                    }
                                }*/

                                if (dataList.has("details")) {
                                    JSONObject detailObject = dataList.getJSONObject("details");
                                    stuScore = detailObject.getString("score");
//                                    headNurse = detailObject.getString("head");
//                                    treatment = detailObject.getString("treatment");
                                    fenTimeString = detailObject.getString("fen_time");
                                    String[] fenTimeStrings = fenTimeString.split(" ");
                                    fen_time = fenTimeStrings[0];
                                    comments = detailObject.getString("msg");
                                    if (detailObject.has("teacher")) {
                                        JSONObject teacherObject = detailObject.getJSONObject("teacher");
                                        teacherName = teacherObject.getString("realname");
                                        teacherId = teacherObject.getString("manage_id");
                                    }
                                }

                                if (dataList.has("content")) {
                                    JSONArray contentArray = dataList.getJSONArray("content");
                                    for (int i = 0; i < contentArray.length(); i++) {
                                        JSONObject listObject = contentArray.getJSONObject(i);
                                        Daily_exam_of_item daily_exam_of_item = new Daily_exam_of_item();
//                                    daily_exam_of_item.setId(listObject.getString("id"));
                                        daily_exam_of_item.setContent(listObject.getString("title"));
                                        daily_exam_of_item.setGrade(Integer.parseInt(listObject.getString("user_score")));
                                        daily_exam_of_item.setThe_weight(Integer.parseInt(listObject.getString("weight")));
                                        daily_exam_of_item.setMaxScore(listObject.getString("max_score"));
                                        if (listObject.has("score_list")) {
                                            List<String> grades = new ArrayList<String>();
                                            JSONArray scoreArray = listObject.getJSONArray("score_list");
                                            for (int j = 0; j < scoreArray.length(); j++) {
                                                grades.add(scoreArray.getString(j));
                                            }
                                            daily_exam_of_item.setGrades(grades);
                                        }

                                        daily_exam_of_item_list.add(daily_exam_of_item);
                                    }
                                }
                                setView();
                                baseListAdapter.notifyDataSetChanged();
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
//                                JSONObject dataList = data.getJSONObject("dataList");
                                Toast.makeText(context, data.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                MyProgressBarDialogTools.hide();
                                finish();
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
    private boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_exam);

        context = this;
        findId();
        initDatas();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        if ("84".equals(fid)) {//考核基地
            enterUrl = "http://yun.ccmtv.cn/admin.php/wx/exambase/index.html";
        } else {
            enterUrl = "http://yun.ccmtv.cn/admin.php/wx/allexam/index.html";
        }
        super.onPause();
    }

    public void findId() {
        title_name = (TextView) findViewById(R.id.activity_title_name);
        tvScore = (TextView) findViewById(R.id.id_tv_daily_exam_score);
        tvPrompt = (TextView) findViewById(R.id.id_tv_daily_exam_prompt);
        tvCancel = (TextView) findViewById(R.id.id_tv_daily_exam_cancel);
        tvSubmit = (TextView) findViewById(R.id.id_tv_daily_exam_submit);
        etComments = (EditText) findViewById(R.id.id_et_daily_exam_comments);
        lvQuestion = (ListView) findViewById(R.id.id_lv_daily_exam_question);
        llButtonLayout = (LinearLayout) findViewById(R.id.id_ll_daily_exam_button_layout);

        llStuTopLayout = (LinearLayout) findViewById(R.id.id_ll_daily_exam_stu_top_layout);
        llStuDepartment = (LinearLayout) findViewById(R.id.id_ll_daily_exam_stu_department);
//        llStuDoctorOfficer = (LinearLayout) findViewById(R.id.id_ll_daily_exam_stu_doctor_officer);
        llStuTeacher = (LinearLayout) findViewById(R.id.id_ll_daily_exam_stu_teacher);
//        llStuHeadNurse = (LinearLayout) findViewById(R.id.id_ll_daily_exam_stu_head_nurse);
        llDoctorTopLayout = (LinearLayout) findViewById(R.id.id_ll_daily_exam_doctor_top_layout);
        llDoctorMarkDate = (LinearLayout) findViewById(R.id.id_ll_daily_exam_doctor_mark_date);
        llStuMarkDate = (LinearLayout) findViewById(R.id.id_ll_daily_exam_stu_mark_date);
        llDoctorDepartment = (LinearLayout) findViewById(R.id.id_ll_daily_exam_doctor_department);
        llDoctorTeacher = (LinearLayout) findViewById(R.id.id_ll_daily_exam_doctor_doctor_teacher);
//        llDoctorHeadNurse = (LinearLayout) findViewById(R.id.id_ll_daily_exam_doctor_head_nurse);
//        llDoctorofficer = (LinearLayout) findViewById(R.id.id_ll_daily_exam_doctor_doctor_officer);
        llDoctorStudent = (LinearLayout) findViewById(R.id.id_ll_daily_exam_doctor_student);
        tvStuDepartment = (TextView) findViewById(R.id.id_tv_daily_exam_stu_department);
//        tvStuDoctorOfficer = (TextView) findViewById(R.id.id_tv_daily_exam_stu_doctor_officer);
        tvStuTeacher = (TextView) findViewById(R.id.id_tv_daily_exam_stu_teacher);
//        tvStuHeadNurse = (TextView) findViewById(R.id.id_tv_daily_exam_stu_head_nurse);
        tvDoctorMarkDate = (TextView) findViewById(R.id.id_tv_daily_exam_doctor_mark_date);
        tvStuMarkDate = (TextView) findViewById(R.id.id_tv_daily_exam_stu_mark_date);
        tvDoctorDepartment = (TextView) findViewById(R.id.id_tv_daily_exam_doctor_department);
        tvDoctorTeacher = (TextView) findViewById(R.id.id_tv_daily_exam_doctor_doctor_teacher);
        tvDoctorStudent = (TextView) findViewById(R.id.id_tv_daily_exam_doctor_student);
//        spinnerDoctorHeadNurse = (NiceSpinner) findViewById(R.id.id_spinner_daily_exam_doctor_head_nurse);
//        spinnerDoctorofficer = (NiceSpinner) findViewById(R.id.id_spinner_daily_exam_doctor_doctor_officer);

        title_name.setText("日常考核");
        tvCancel.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
        /*spinnerDoctorHeadNurse.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, "选择：" + dailyExamHeadNameBeanList.get(position).getName(), Toast.LENGTH_SHORT).show();
                headNurse_id = dailyExamHeadNameBeanList.get(position).getId();
            }
        });

        spinnerDoctorofficer.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, "选择：" + dailyExamTreatmentNameBeanList.get(position).getName(), Toast.LENGTH_SHORT).show();
                treatment_id = dailyExamTreatmentNameBeanList.get(position).getId();
            }
        });*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_tv_daily_exam_cancel:
                setCancel();
                break;
        }
    }

    private void initDatas() {
        try {
            daily_exam_id = getIntent().getStringExtra("daily_exam_id");
            user_id = getIntent().getStringExtra("user_id");
            dataType = getIntent().getStringExtra("data_type");
            intentStatus = getIntent().getStringExtra("status");
            fid = getIntent().getStringExtra("fid");
            identity = getIntent().getStringExtra("identity");
            isEdit = getIntent().getBooleanExtra("isEdit", true);

            //dataType 为1时为当前月，判断状态区分可编辑与不可编辑，为2时为以往月，直接不可编辑（审核跳转因不可编辑，直接赋值为2）
            if (isEdit) {
//                if (dataType.equals("1")) {
                if (intentStatus.equals("2") || intentStatus.equals("3") || intentStatus.equals("5")) {
                    canEditable = true;
                } else {
                    canEditable = false;
                }
//                } else {
//                    canEditable = false;
//                }
            } else {
                canEditable = false;
            }
            if (canEditable) {
//                spinnerDoctorHeadNurse.setClickable(true);
//                spinnerDoctorofficer.setClickable(true);
                etComments.setEnabled(true);
                tvCancel.setVisibility(View.VISIBLE);
                tvSubmit.setVisibility(View.VISIBLE);
            } else {
//                spinnerDoctorHeadNurse.setClickable(false);
//                spinnerDoctorofficer.setClickable(false);
//                spinnerDoctorofficer.hideArrow();
//                spinnerDoctorHeadNurse.hideArrow();
                etComments.setEnabled(false);
                tvCancel.setVisibility(View.GONE);
                tvSubmit.setVisibility(View.GONE);
            }

            if (identity.equals("student")) {
                canEditable = false;
//                spinnerDoctorHeadNurse.setClickable(false);
//                spinnerDoctorofficer.setClickable(false);
//                spinnerDoctorofficer.hideArrow();
//                spinnerDoctorHeadNurse.hideArrow();
                llStuTopLayout.setVisibility(View.VISIBLE);
                llDoctorTopLayout.setVisibility(View.GONE);
//                tvPrompt.setVisibility(View.GONE);
                tvSubmit.setVisibility(View.GONE);
                tvSubmit.setText("我知道了");
                etComments.setEnabled(false);
                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
            } else {
                llStuTopLayout.setVisibility(View.GONE);
                llDoctorTopLayout.setVisibility(View.VISIBLE);
                tvPrompt.setVisibility(View.VISIBLE);
                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setSubmit();
//                        finish();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.dailyStaInfo);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("user_id", user_id);
                    obj.put("id", daily_exam_id);
                    obj.put("fid", fid);
                    obj.put("status", intentStatus);
                    LogUtil.e("规培日常考核信息数据：", obj.toString());
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("规培日常考核信息数据：", result);

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

    private void setView() {
        /*if (canEditable) {
            if (dailyExamTreatmentStringList.size() <= 0) {
                dailyExamTreatmentStringList.add("");
            }
            if (dailyExamHeadNurseStringList.size() <= 0) {
                dailyExamHeadNurseStringList.add("");
            }
        } else {
            dailyExamHeadNurseStringList.add(headNurse);
            dailyExamTreatmentStringList.add(treatment);
        }
        if (dailyExamHeadNameBeanList.size()>0){
            headNurse_id = dailyExamHeadNameBeanList.get(0).getId();
        }
        if (dailyExamTreatmentNameBeanList.size()>0){
            treatment_id = dailyExamTreatmentNameBeanList.get(0).getId();
        }*/
//        spinnerDoctorofficer.attachDataSource(dailyExamTreatmentStringList);
//        spinnerDoctorHeadNurse.attachDataSource(dailyExamHeadNurseStringList);
        tvDoctorStudent.setText(stuName);
        tvDoctorDepartment.setText(standard_name);
        tvScore.setText(stuScore);
        if (teacherName.equals("")) {
            tvDoctorTeacher.setText("无");
        } else {
            tvDoctorTeacher.setText(teacherName);
        }
        etComments.setText(comments);
        tvDoctorMarkDate.setText(fen_time);
        tvStuMarkDate.setText(fen_time);
        tvStuDepartment.setText(standard_name);
        tvStuTeacher.setText(teacherName);
//        tvStuDoctorOfficer.setText(treatment);
//        tvStuHeadNurse.setText(headNurse);
        tvPrompt.setText(Html.fromHtml(promptHtml));
    }

    private void initViews() {
        baseListAdapter = new BaseListAdapter(lvQuestion, daily_exam_of_item_list, R.layout.item_daily_exam_in_detail) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Daily_exam_of_item map = (Daily_exam_of_item) item;
//                helper.setText(R.id._item_id, map.getId());
                helper.setText(R.id._item_content, map.getContent());
                helper.setText(R.id._item_the_weight, "权重：" + map.getThe_weight());
                helper.setSpinnerDailyExam2(R.id._item_grade, tvScore, daily_exam_of_item_list, map, canEditable, R.id.tv_value, R.id.id_iv_spinner_arrow);
            }
        };
        lvQuestion.setAdapter(baseListAdapter);
    }

    public void setCancel() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("你确定要取消吗？")
                .setPositiveButton("是的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("等一下", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /***
     * 提交
     */
    public void setSubmit() {
        if (Integer.parseInt(tvScore.getText().toString()) < 80 && etComments.getText().length() <= 0) {
            Toast.makeText(context, "成绩低于80分，请说明情况！", Toast.LENGTH_SHORT).show();
        } else {
            try {
                JSONObject dataObject = new JSONObject();
                dataObject.put("score", tvScore.getText());
                dataObject.put("manage_id", teacherId);
                dataObject.put("msg", etComments.getText());
                dataObject.put("fen_time", fenTimeString);
                dataObject.put("treatment_id", treatment_id);
                dataObject.put("head_id", headNurse_id);
                JSONArray itemArray = new JSONArray();
                for (Daily_exam_of_item item : daily_exam_of_item_list) {
                    JSONObject itemObject = new JSONObject();
                    itemObject.put("title", item.getContent());
                    itemObject.put("user_score", item.getGrade());
                    itemObject.put("weight", item.getThe_weight());
                    itemObject.put("max_score", item.getMaxScore());
                    itemArray.put(itemObject);
                }
                dataObject.put("content", itemArray);
//                Log.e(getLocalClassName(), "setSubmit: " + dataObject.toString());
                sendDailyStaAdd(dataObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void sendDailyStaAdd(final JSONObject dataObject) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.dailyStaAdd);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("user_id", user_id);
                    obj.put("fid", fid);
                    obj.put("data", dataObject);
                    obj.put("id", daily_exam_id);
                    LogUtil.e("规培日常考核发送考核信息数据：", obj.toString());
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("规培日常考核发送考核信息数据result：", result);

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

    public void back(View view) {
        finish();
    }

}
