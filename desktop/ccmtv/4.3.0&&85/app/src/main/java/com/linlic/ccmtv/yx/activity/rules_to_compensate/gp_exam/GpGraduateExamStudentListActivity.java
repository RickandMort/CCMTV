package com.linlic.ccmtv.yx.activity.rules_to_compensate.gp_exam;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Condition;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.adapter.ExamStudentListAdapter;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.javabean.ExamStudent;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.javabean.GpGraduateExamStudent;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.ToastUtils;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.Bind;

/**
 * 规培考试学生列表界面
 * Created by bentley on 2018/6/26.
 */

public class GpGraduateExamStudentListActivity extends BaseActivity implements View.OnClickListener {

    private TextView title_name;
    private ListView lvExamStudent1;
//    private ListView lvExamStudent2;
//    private ListView lvExamStudent3;
//    private ListView lvExamStudent4;
    private ListView cycle_listview;//周期列表
    private ListView base_listview;//基地列表
    private ListView rotation_years_listview;//轮转年限列表
    private TextView id_tv_gp_exam_student_list_item_name;//周期
    private TextView id_tv_gp_exam_student_list_item_department;//基地
    private TextView id_tv_gp_exam_student_list_item_score;//轮转年限
    private TextView tvRejectReason1;
    private LinearLayout tvRejectReason2;
    private Button btnSubmit1;
    private LinearLayout condition_layout;//条件区域
    private LinearLayout llLayout1;
    @Bind(R.id.audit_nodata)
    NodataEmptyLayout audit_nodata;//
//    private LinearLayout llLayout3;
//    private LinearLayout llLayout4;
    private List<Condition> ccycleDatas = new ArrayList<>();//周期列表数据源
    private List<Condition> baseDatas = new ArrayList<>();//基地列表数据源
    private List<Condition> rotation_yearsDatas = new ArrayList<>();//轮转年限列表数据源
    private BaseListAdapter baseListAdapterCcycle,baseListAdapterBase,baseListAdapterRotation_years;
    private GpGraduateExamStudent gpGraduateExamStudent1 = new GpGraduateExamStudent();
    private List<ExamStudent> curr_user_list = new ArrayList<>();
//    private GpGraduateExamStudent gpGraduateExamStudent3 = new GpGraduateExamStudent();
//    private GpGraduateExamStudent gpGraduateExamStudent4 = new GpGraduateExamStudent();
    private List<GpGraduateExamStudent> gpGraduateExamStudentList = new ArrayList<>();
    private Context context;
    private String exam_id = "";
    private String year = "";
    private String month = "";
    private String fid = "";
    private String exam_type = "";
    private ExamStudentListAdapter examStudentListAdapter1;
    private String dataStatus = "";
    boolean isAllScoreHave = true;

    private Condition current_week = new Condition() ;//当前周
    private Condition current_base = new Condition() ;//当前基地
    private Condition current_rotation_years = new Condition() ;//当前轮转年限
    private boolean flag=true;

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
                                gpGraduateExamStudent1.getUser_list().clear();
                                JSONArray dataList = data.getJSONArray("data");
                                List<ExamStudent> user_list = new ArrayList<>();
                                for (int i=0;i<dataList.length();i++) {
                                    if (i<=3){
                                        JSONObject dataObject = dataList.getJSONObject(i);
                                        //解析用户列表

                                        JSONArray userList = dataObject.getJSONArray("user_list");
                                        List<ExamStudent> examStudentList = new ArrayList<>();
                                        for (int j=0; j<userList.length(); j++) {
                                            JSONObject listObject = userList.getJSONObject(j);
                                            ExamStudent examStudent = new ExamStudent();
                                            examStudent.setRealname(listObject.getString("realname"));
                                            examStudent.setScore(listObject.getString("score"));
                                            examStudent.setUid(listObject.getString("uid"));
                                            examStudent.setStatus(listObject.getString("status"));
                                            examStudent.setGp_exam_id(listObject.getString("leave_ks_id"));
                                            examStudent.setStandard_kname(listObject.getString("standard_kname"));
                                            examStudent.setStandard_kid(listObject.getString("standard_kid"));
                                            examStudent.setYear(listObject.getString("year"));
                                            examStudent.setMonth(listObject.getString("month"));
                                            examStudent.setWeek(listObject.getString("week"));
                                            examStudent.setGpyear_id(listObject.getString("gpyear_id"));
                                            examStudent.setBase_id(listObject.getString("base_id"));
                                            examStudent.setIs_need_temp(listObject.getString("is_need_temp"));
                                            examStudent.setIs_allow_enter(listObject.getString("is_allow_enter"));
                                            examStudent.setNoallow_reason(listObject.getString("noallow_reason"));
                                            examStudentList.add(examStudent);
                                            user_list.add(examStudent);
                                        }
                                        //判断是否是当前周is_current_week
                                        if(flag==true){
                                            if(dataObject.getString("is_current_week").equals("1")){

                                                switch (dataObject.getString("week")){
                                                    case "1":
                                                        current_week.setId("1");
                                                        current_week.setTitle("第一周 ");
                                                        break;
                                                    case "2":
                                                        current_week.setId("2");
                                                        current_week.setTitle("第二周 ");
                                                        break;
                                                    case "3":
                                                        current_week.setId("3");
                                                        current_week.setTitle("第三周 ");
                                                        break;
                                                    case "4":
                                                        current_week.setId("4");
                                                        current_week.setTitle("第四周 ");
                                                        break;
                                                }

                                            }
                                        }
                                        GpGraduateExamStudent  gpGraduateExamStudent = new GpGraduateExamStudent();
                                        gpGraduateExamStudent.setUser_list(examStudentList);
                                        gpGraduateExamStudentList.add(i,gpGraduateExamStudent);

                                        //解析其他数据
                                        if (dataObject.has("flow")) {
                                            JSONObject flowObject = dataObject.getJSONObject("flow");
                                            gpGraduateExamStudentList.get(i).setStep(flowObject.getString("step"));
                                            gpGraduateExamStudentList.get(i).setCurrent_node(flowObject.getString("current_node"));
                                            gpGraduateExamStudentList.get(i).setMessage(flowObject.getString("message"));
                                        }
                                        gpGraduateExamStudentList.get(i).setMan_type(dataObject.getString("man_type"));
                                        gpGraduateExamStudentList.get(i).setDisabled(dataObject.getString("disabled"));
                                        gpGraduateExamStudentList.get(i).setWeek(dataObject.getString("week"));
                                    }
                                }

                                id_tv_gp_exam_student_list_item_name.setText(current_week.getTitle());

                                changeView();

                                handler.sendEmptyMessageDelayed(10, 2000);
                            }else {
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
                                Toast.makeText(context, data.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                initData();
                            }else {
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
                case 3:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getString("code").equals("200")) {
                            JSONObject data = result.getJSONObject("data");
                            if (data.getString("status").equals("1")) {
                                /*解析基地*/
                                baseDatas.clear();
                                Condition condition_text = new Condition();
                                condition_text.setId("基地");
                                condition_text.setTitle("基地 ");
                                current_base = condition_text;
                                baseDatas.add(condition_text);
                               JSONArray baseinfo =  data.getJSONObject("data").getJSONArray("baseinfo");
                                for (int i = 0 ;i <baseinfo.length();i++){
                                    JSONObject beanJson = baseinfo.getJSONObject(i);
                                    Condition condition = new Condition();
                                    condition.setId(beanJson.getString("base_id"));
                                    condition.setTitle(beanJson.getString("name")+" ");
                                    baseDatas.add(condition);
                                }
                                /*解析轮转年限*/
                                rotation_yearsDatas.clear();
                                Condition condition_text2 = new Condition();
                                condition_text2.setId("轮转年限");
                                condition_text2.setTitle("轮转年限 ");
                                current_rotation_years = condition_text2;
                                rotation_yearsDatas.add(condition_text2);
                                JSONArray gpyears =  data.getJSONObject("data").getJSONArray("gpyears");
                                for (int i = 0 ;i <gpyears.length();i++){
                                    JSONObject beanJson = gpyears.getJSONObject(i);
                                    Condition condition = new Condition();
                                    condition.setId(beanJson.getString("gpyear_id"));
                                    condition.setTitle(beanJson.getString("name")+" ");
                                    rotation_yearsDatas.add(condition);
                                }
                                baseListAdapterBase.notifyDataSetChanged();
                                baseListAdapterRotation_years.notifyDataSetChanged();
                            }else {
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

    private void setResultStatus(boolean status, int code) {
        if (status) {
            llLayout1.setVisibility(View.VISIBLE);
            audit_nodata.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                audit_nodata.setNetErrorIcon();
            } else {
                audit_nodata.setLastEmptyIcon();
            }
            llLayout1.setVisibility(View.GONE);
            audit_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gp_graduate_exam_student_list);
        context = GpGraduateExamStudentListActivity.this;
        findId();
        getCkSearchInfo();
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
        title_name = (TextView) findViewById(R.id.activity_title_name);
        title_name.setText("学生列表");
        lvExamStudent1 = (ListView) findViewById(R.id.id_lv_gp_graduate_exam_student_list_1);
//        lvExamStudent2 = (ListView) findViewById(R.id.id_lv_gp_graduate_exam_student_list_2);
//        lvExamStudent3 = (ListView) findViewById(R.id.id_lv_gp_graduate_exam_student_list_3);
//        lvExamStudent4 = (ListView) findViewById(R.id.id_lv_gp_graduate_exam_student_list_4);
        tvRejectReason1 = (TextView) findViewById(R.id.id_tv_gp_graduate_exam_student_list_reject_reason_1);
        tvRejectReason2 = (LinearLayout) findViewById(R.id.id_tv_gp_graduate_exam_student_list_reject_reason_2);
        btnSubmit1 = (Button) findViewById(R.id.id_btn_gp_graduate_exam_student_list_submit_1);
        llLayout1 = (LinearLayout) findViewById(R.id.id_ll_gp_graduate_exam_student_list_1_layout);
        condition_layout = (LinearLayout) findViewById(R.id.condition_layout);
        cycle_listview = (ListView) findViewById(R.id.cycle_listview);
        base_listview = (ListView) findViewById(R.id.base_listview);
        rotation_years_listview = (ListView) findViewById(R.id.rotation_years_listview);
        audit_nodata = (NodataEmptyLayout) findViewById(R.id.audit_nodata);

//        llLayout3 = (LinearLayout) findViewById(R.id.id_ll_gp_graduate_exam_student_list_3_layout);
//        llLayout4 = (LinearLayout) findViewById(R.id.id_ll_gp_graduate_exam_student_list_4_layout);
        id_tv_gp_exam_student_list_item_name = (TextView) findViewById(R.id.id_tv_gp_exam_student_list_item_name);
        id_tv_gp_exam_student_list_item_department = (TextView) findViewById(R.id.id_tv_gp_exam_student_list_item_department);
        id_tv_gp_exam_student_list_item_score = (TextView) findViewById(R.id.id_tv_gp_exam_student_list_item_score);
        condition_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                condition_layout.setVisibility(View.GONE);
            }
        });
        lvExamStudent1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                flag=false;
                //若出科是基地身份查看并且成绩为待录入，点击不进入出科考核科目界面
                try {
                    String str=gpGraduateExamStudentList.get(Integer.parseInt(current_week.getId())-1).getMan_type();
                    if (gpGraduateExamStudentList.get(Integer.parseInt(current_week.getId())-1).getMan_type()!=null && gpGraduateExamStudentList.get(Integer.parseInt(current_week.getId())-1).getMan_type().equals("3")) {
                        String str1=curr_user_list.get(position).getScore();
                        if (curr_user_list.get(position).getScore() != null && curr_user_list.get(position).getScore().equals("待录入")) {
                            Toast.makeText(context, "暂无成绩显示", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    String is_enter=curr_user_list.get(position).getIs_allow_enter();
                    if(is_enter.equals("1")){
                        String is_temp=curr_user_list.get(position).getIs_need_temp();//根据该值判断是否要进入多选模板列表
                        if(is_temp.equals("1")){
                            Intent intent=new Intent(GpGraduateExamStudentListActivity.this,ChooseModelActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putString("fid",fid);
                            bundle.putString("standard_kid",curr_user_list.get(position).getStandard_kid());
                            bundle.putString("year",curr_user_list.get(position).getYear());
                            bundle.putString("month",curr_user_list.get(position).getMonth());
                            bundle.putString("week",curr_user_list.get(position).getWeek());
                            bundle.putString("gp_uid",curr_user_list.get(position).getUid());
                            bundle.putString("standard_kid",curr_user_list.get(position).getStandard_kid());
                            bundle.putString("leave_ks_id",curr_user_list.get(position).getGp_exam_id());
                            bundle.putString("disabled",gpGraduateExamStudentList.get(Integer.parseInt(current_week.getId())-1).getDisabled());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }else if(is_temp.equals("0")){
                            Intent intent = new Intent(context, GpExamSubjectActivity.class);
                            intent.putExtra("fid", fid);
                            intent.putExtra("gp_uid", curr_user_list.get(position).getUid());  //1 是审核通过(不可点击) 2 是从新审核（可点击） 3 是提交成绩（可点击） 4是等待审核（不可点击） 5不显示按钮
                            intent.putExtra("standard_kid", curr_user_list.get(position).getStandard_kid());
                            intent.putExtra("leave_ks_id", curr_user_list.get(position).getGp_exam_id());
                            intent.putExtra("disabled", gpGraduateExamStudentList.get(Integer.parseInt(current_week.getId())-1).getDisabled());
                            intent.putExtra("year", curr_user_list.get(position).getYear());
                            intent.putExtra("month", curr_user_list.get(position).getMonth());
                            intent.putExtra("week", curr_user_list.get(position).getWeek());
                            intent.putExtra("name", curr_user_list.get(position).getRealname());
                            intent.putExtra("pid","");
                            startActivity(intent);
                        }
                    }else if(is_enter.equals("0")){
                        ToastUtils.makeText(GpGraduateExamStudentListActivity.this,curr_user_list.get(position).getNoallow_reason());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        id_tv_gp_exam_student_list_item_name.setOnClickListener(this);
        id_tv_gp_exam_student_list_item_department.setOnClickListener(this);
        id_tv_gp_exam_student_list_item_score.setOnClickListener(this);
        rotation_years_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                current_rotation_years = rotation_yearsDatas.get(i);
                condition_layout.setVisibility(View.GONE);
                id_tv_gp_exam_student_list_item_score.setText(current_rotation_years.getTitle());
                id_tv_gp_exam_student_list_item_score.setBackgroundResource(R.drawable.anniu26);
                id_tv_gp_exam_student_list_item_score.setTextColor(Color.parseColor("#69ADFA"));
                Drawable drawable = context.getResources().getDrawable(R.mipmap
                        .drop_down_icon1);
                drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                id_tv_gp_exam_student_list_item_score.setCompoundDrawables(null,null,drawable,null);
                changeView();
            }
        });
        cycle_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                condition_layout.setVisibility(View.GONE);
                current_week = ccycleDatas.get(i);
                id_tv_gp_exam_student_list_item_name.setText(current_week.getTitle());
                changeView();
            }
        });
        base_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                condition_layout.setVisibility(View.GONE);
                current_base = baseDatas.get(i);
                id_tv_gp_exam_student_list_item_department.setText(current_base.getTitle());
                id_tv_gp_exam_student_list_item_department.setBackgroundResource(R.drawable.anniu26);
                id_tv_gp_exam_student_list_item_department.setTextColor(Color.parseColor("#69ADFA"));
                Drawable drawable = context.getResources().getDrawable(R.mipmap
                                     .drop_down_icon1);
                drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                id_tv_gp_exam_student_list_item_department.setCompoundDrawables(null,null,drawable,null);
                changeView();
            }
        });

        btnSubmit1.setOnClickListener(this);
    }

    private void getIntentData() {
        year = getIntent().getStringExtra("year");
        month = getIntent().getStringExtra("month");
        fid = getIntent().getStringExtra("fid");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_tv_gp_exam_student_list_item_name:
                //周期
                if(condition_layout.getVisibility() == View.VISIBLE){
                    if (cycle_listview.getVisibility() == View.VISIBLE){
                        condition_layout.setVisibility(View.GONE);
                    }else {
                        condition_layout.setVisibility(View.VISIBLE);
                        cycle_listview.setVisibility(View.VISIBLE);
                        base_listview.setVisibility(View.GONE);
                        rotation_years_listview.setVisibility(View.GONE);
                    }
                }else{
                    condition_layout.setVisibility(View.VISIBLE);
                    cycle_listview.setVisibility(View.VISIBLE);
                    base_listview.setVisibility(View.GONE);
                    rotation_years_listview.setVisibility(View.GONE);
                }

                break;
            case R.id.id_tv_gp_exam_student_list_item_department:
                //基地
                if(condition_layout.getVisibility() == View.VISIBLE){
                    if (base_listview.getVisibility() == View.VISIBLE){
                        condition_layout.setVisibility(View.GONE);
                    }else {
                        condition_layout.setVisibility(View.VISIBLE);
                        cycle_listview.setVisibility(View.GONE);
                        base_listview.setVisibility(View.VISIBLE);
                        rotation_years_listview.setVisibility(View.GONE);
                    }
                }else {
                    condition_layout.setVisibility(View.VISIBLE);
                    cycle_listview.setVisibility(View.GONE);
                    base_listview.setVisibility(View.VISIBLE);
                    rotation_years_listview.setVisibility(View.GONE);
                }

                break;
            case R.id.id_tv_gp_exam_student_list_item_score:
                //轮转年限
                if(condition_layout.getVisibility() == View.VISIBLE){
                    if (rotation_years_listview.getVisibility() == View.VISIBLE){
                        condition_layout.setVisibility(View.GONE);
                    }else{
                        condition_layout.setVisibility(View.VISIBLE);
                        cycle_listview.setVisibility(View.GONE);
                        base_listview.setVisibility(View.GONE);
                        rotation_years_listview.setVisibility(View.VISIBLE);
                    }

                }else {
                    condition_layout.setVisibility(View.VISIBLE);
                    cycle_listview.setVisibility(View.GONE);
                    base_listview.setVisibility(View.GONE);
                    rotation_years_listview.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.id_btn_gp_graduate_exam_student_list_submit_1:
                isAllScoreHave = true;
                for (int i=0; i<gpGraduateExamStudentList.get(Integer.parseInt(current_week.getId())-1).getUser_list().size(); i++) {
                    String stuScore = gpGraduateExamStudentList.get(Integer.parseInt(current_week.getId())-1).getUser_list().get(i).getScore();
                    if (stuScore == null || stuScore.isEmpty() || stuScore.equals("待录入")) {
                        isAllScoreHave = false;
                    }
                }
                if (isAllScoreHave) {
                    sendScoreData(gpGraduateExamStudentList.get(Integer.parseInt(current_week.getId())-1).getWeek());
                } else {
                    Toast.makeText(context, "有学员成绩未录入，不能提交", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    private void sendScoreData(final String week) {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.leaveKsBatchSub);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("year", year);
                    obj.put("month", month);
                    obj.put("week", week);
                    obj.put("fid", fid);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("发送出科整体成绩返回数据：", result);

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
        /*初始化周期数据start*/
        Condition condition = new Condition();
        condition.setId("1");
        condition.setTitle("第一周 ");
        ccycleDatas.add(condition);
        Condition condition2 = new Condition();
        condition2.setId("2");
        condition2.setTitle("第二周 ");
        ccycleDatas.add(condition2);
        Condition condition3 = new Condition();
        condition3.setId("3");
        condition3.setTitle("第三周 ");
        ccycleDatas.add(condition3);
        Condition condition4 = new Condition();
        condition4.setId("4");
        condition4.setTitle("第四周 ");
        ccycleDatas.add(condition4);
        baseListAdapterCcycle = new BaseListAdapter(cycle_listview, ccycleDatas, R.layout.item_training_management2) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Condition map = (Condition) item;

                helper.setText(R.id._item2_text1, map.getTitle());

            }
        };
        cycle_listview.setAdapter(baseListAdapterCcycle);
        /*初始化周期数据end*/
           /*初始化基地数据start*/
        baseListAdapterBase = new BaseListAdapter(base_listview, baseDatas, R.layout.item_training_management2) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Condition map = (Condition) item;

                helper.setText(R.id._item2_text1, map.getTitle());

            }
        };
        base_listview.setAdapter(baseListAdapterBase);
         /*初始化基地数据end*/
          /*初始化轮转年限数据start*/
        baseListAdapterRotation_years = new BaseListAdapter(rotation_years_listview, rotation_yearsDatas, R.layout.item_training_management2) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Condition map = (Condition) item;

                helper.setText(R.id._item2_text1, map.getTitle());

            }
        };
        rotation_years_listview.setAdapter(baseListAdapterRotation_years);
      /*初始化轮转年限数据end*/

        examStudentListAdapter1 = new ExamStudentListAdapter(context, gpGraduateExamStudent1.getUser_list(),"1");
        lvExamStudent1.setAdapter(examStudentListAdapter1);

    }

    private void initData() {

        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.leaveKsUsersList);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("year", year);
                    obj.put("month", month);
                    obj.put("fid", fid);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("规培出科考核学员列表数据：", result);

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

    private void getCkSearchInfo() {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getCkSearchInfo);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("搜索条件 【专业/基地 + 规培年限】：", result);

                    Message message = new Message();
                    message.what = 3;
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
        curr_user_list.clear();
        for (int i = 0;i <gpGraduateExamStudentList.get(Integer.parseInt(current_week.getId())-1).getUser_list().size();i++){
            ExamStudent examStudent = gpGraduateExamStudentList.get(Integer.parseInt(current_week.getId())-1).getUser_list().get(i);
            //先判断周期
            if(examStudent.getWeek().equals(current_week.getId())){
                //在判断是否需要判断基地
                if(current_base.getTitle().trim().equals("基地") && current_rotation_years.getTitle().trim().equals("轮转年限")){
//                if(examStudent.getBase_id().equals(current_base.getId())){
                    curr_user_list.add(examStudent);
                }else{
                    if(current_base.getTitle().trim().equals("基地")){
                        if(examStudent.getGpyear_id().equals(current_rotation_years.getId())){
                            curr_user_list.add(examStudent);
                        }
                    }else{
                        if(examStudent.getBase_id().equals(current_base.getId())){
                            curr_user_list.add(examStudent);
                        }
                    }
                }
            }
        }

        examStudentListAdapter1.notifyData(curr_user_list);


            setResultStatus(curr_user_list.size() > 0, 200);


        //Man_type  如果为2需展示flow里面的审核状态 如果为3则只展示数据列表即可
        if (gpGraduateExamStudentList.get(Integer.parseInt(current_week.getId())-1).getMan_type().equals("2")) {
            btnSubmit1.setVisibility(View.VISIBLE);
            //0尚未发通知 1 尚未整体提交成绩 2 审核中 3 审核通过 4审核退回
            switch (gpGraduateExamStudentList.get(Integer.parseInt(current_week.getId())-1).getStep()) {
                case "0":
                    btnSubmit1.setText("尚未发送通知");
                    btnSubmit1.setBackgroundResource(R.mipmap.button_03);
                    btnSubmit1.setClickable(false);
                    isGone();
                    break;
                case "1":
                    btnSubmit1.setText("提交");
                    btnSubmit1.setBackgroundResource(R.mipmap.button_01);
                    btnSubmit1.setClickable(true);
                    isGone();
                    break;
                case "2":
                    btnSubmit1.setText("审核中");
                    btnSubmit1.setBackgroundResource(R.mipmap.button_03);
                    btnSubmit1.setClickable(false);
                    isGone();
                    break;
                case "3":
                    btnSubmit1.setText("审核通过");
                    btnSubmit1.setBackgroundResource(R.mipmap.button_03);
                    btnSubmit1.setClickable(false);
                    isGone();
                    break;
                case "4":
                    btnSubmit1.setText("重新提交");
                    btnSubmit1.setBackgroundResource(R.mipmap.button_01);
                    btnSubmit1.setClickable(true);
                    tvRejectReason1.setVisibility(View.VISIBLE);
                    tvRejectReason2.setVisibility(View.VISIBLE);
                    tvRejectReason1.setText(gpGraduateExamStudentList.get(Integer.parseInt(current_week.getId())-1).getMessage());
                    break;
            }
        }else{
            btnSubmit1.setVisibility(View.GONE);
            tvRejectReason1.setVisibility(View.GONE);
            tvRejectReason2.setVisibility(View.GONE);
        }


    }

    private void isGone(){
        tvRejectReason1.setVisibility(View.GONE);
        tvRejectReason2.setVisibility(View.GONE);
        tvRejectReason1.setText(gpGraduateExamStudentList.get(Integer.parseInt(current_week.getId())-1).getMessage());
    }

    public void back(View view) {
        finish();
    }
}
