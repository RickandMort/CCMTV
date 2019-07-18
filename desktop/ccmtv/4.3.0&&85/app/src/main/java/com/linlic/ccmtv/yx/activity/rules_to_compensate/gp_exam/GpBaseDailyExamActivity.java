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
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.adapter.Adapter_GpBaseDailyExam;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.javabean.BaseDailyStudent;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyListView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GpBaseDailyExamActivity extends BaseActivity {

    @Bind(R.id.id_iv_activity_title_8)
    ImageView idIvActivityTitle8;
    @Bind(R.id.arrow_back)
    LinearLayout arrowBack;
    @Bind(R.id.activity_title_name)
    TextView activityTitleName;
    @Bind(R.id.id_iv_activity_title_8_right)
    ImageView idIvActivityTitle8Right;
    @Bind(R.id.id_ll_activity_title_8)
    LinearLayout idLlActivityTitle8;
    @Bind(R.id.id_tv_gp_exam_student_list_item_name)
    TextView idTvGpExamStudentListItemName;
    @Bind(R.id.id_tv_gp_exam_student_list_item_department)
    TextView idTvGpExamStudentListItemDepartment;
    @Bind(R.id.id_tv_gp_exam_student_list_item_score)
    TextView idTvGpExamStudentListItemScore;
    @Bind(R.id.id_lv_gp_exam_student_list_yes)
    MyListView idLvGpExamStudentListYes;
    @Bind(R.id.id_tv_gp_exam_student_list_hint)
    TextView idTvGpExamStudentListHint;
    @Bind(R.id.id_btn_gp_exam_student_list_submit)
    Button idBtnGpExamStudentListSubmit;
    @Bind(R.id.id_tv_exam_student_list_reject_reason)
    TextView idTvExamStudentListRejectReason;
    @Bind(R.id.id_ll_gp_exam_student_list_yes_layout)
    LinearLayout idLlGpExamStudentListYesLayout;
    @Bind(R.id.id_tv_gp_exam_student_list_previous_data)
    TextView idTvGpExamStudentListPreviousData;
    @Bind(R.id.id_iv_gp_exam_student_list_previous_data)
    ImageView idIvGpExamStudentListPreviousData;
    @Bind(R.id.id_ll_gp_exam_student_list_previous_data)
    LinearLayout idLlGpExamStudentListPreviousData;
    @Bind(R.id.id_lv_gp_exam_student_list_no)
    MyListView idLvGpExamStudentListNo;
    private String exam_id = "";
    private String year = "";
    private String month = "";
    private String fid = "";
    private String exam_type = "";
    private String exam_name = "";
    private Adapter_GpBaseDailyExam examStudentListAdapterYes;
    private List<BaseDailyStudent> listStudentYes = new ArrayList<>();
    private Context context;

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
                                JSONArray dataList = data.getJSONArray("data");
                                    listStudentYes.clear();
                                    for (int i = 0; i < dataList.length(); i++) {
                                        JSONObject listYesObject = dataList.getJSONObject(i);
                                        BaseDailyStudent examStudentYes = new BaseDailyStudent();
                                        examStudentYes.setRealname(listYesObject.getString("realname"));
                                        examStudentYes.setUsername(listYesObject.getString("username"));
                                        examStudentYes.setScore(listYesObject.getString("score"));
                                        examStudentYes.setGp_uid(listYesObject.getString("gp_uid"));
                                        examStudentYes.setBasename(listYesObject.getString("basename"));
                                        examStudentYes.setYear(listYesObject.getString("year"));
                                        examStudentYes.setStandard_kid(listYesObject.getString("standard_kid"));
                                        examStudentYes.setMonth(listYesObject.getString("month"));
                                        examStudentYes.setWeek(listYesObject.getString("week"));
                                        examStudentYes.setDisabled(listYesObject.getString("disabled"));
                                        examStudentYes.setIs_allow_enter(listYesObject.getString("is_allow_enter"));
                                        examStudentYes.setLeave_ks_id(listYesObject.getString("leave_ks_id"));
                                        examStudentYes.setStandard_kname(listYesObject.getString("standard_kname"));
                                        listStudentYes.add(examStudentYes);
                                    }

                                examStudentListAdapterYes = new Adapter_GpBaseDailyExam(context, listStudentYes,"1");
                                idLvGpExamStudentListYes.setAdapter(examStudentListAdapterYes);

                                //changeView();
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
        setContentView(R.layout.activity_gp_base_daily_exam);
        context=GpBaseDailyExamActivity.this;
        ButterKnife.bind(this);
        activityTitleName.setText("学生列表");
        getIntentData();
        setonClick();
    }


    private void setonClick(){

        idLvGpExamStudentListYes.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //若出科是基地身份查看并且成绩为待录入，点击不进入出科考核科目界面
                  try {
                       if (listStudentYes.get(position).getScore() != null && listStudentYes.get(position).getScore().equals("待录入")) {
                            Toast.makeText(context, "暂无成绩显示", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent intent = new Intent(context, GpExamSubjectActivity.class);
                        intent.putExtra("fid", fid);
                        intent.putExtra("gp_uid", listStudentYes.get(position).getGp_uid());  //1 是审核通过(不可点击) 2 是从新审核（可点击） 3 是提交成绩（可点击） 4是等待审核（不可点击） 5不显示按钮
                        intent.putExtra("standard_kid", listStudentYes.get(position).getStandard_kid());
                        intent.putExtra("leave_ks_id", listStudentYes.get(position).getLeave_ks_id());
                        intent.putExtra("disabled", listStudentYes.get(position).getDisabled());
                        intent.putExtra("year", listStudentYes.get(position).getYear());
                        intent.putExtra("month", listStudentYes.get(position).getMonth());
                        intent.putExtra("week", listStudentYes.get(position).getWeek());
                        intent.putExtra("name", listStudentYes.get(position).getRealname());
                        intent.putExtra("pid","");
                        startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void getIntentData() {
        year = getIntent().getStringExtra("year");
        month = getIntent().getStringExtra("month");
        fid = getIntent().getStringExtra("fid");
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

    private void initData() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getleaveKsBaseUsersList);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("year", year);
                    obj.put("month", month);
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


    @OnClick({R.id.arrow_back, R.id.id_btn_gp_exam_student_list_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.arrow_back:
                finish();
                break;
            case R.id.id_btn_gp_exam_student_list_submit:
                break;
        }
    }
}
