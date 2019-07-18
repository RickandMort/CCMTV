package com.linlic.ccmtv.yx.activity.rules_to_compensate.gp_exam;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import com.linlic.ccmtv.yx.activity.entity.Condition;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.adapter.GpGraduateExamAdapter;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.javabean.DailyStudent;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.javabean.GpGraduateExamStudent1;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyListView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.igexin.sdk.GTServiceManager.context;
import static com.linlic.ccmtv.yx.R.id.audit_nodata;
import static com.linlic.ccmtv.yx.R.id.base_listview;
import static com.linlic.ccmtv.yx.R.id.condition_layout;
import static com.linlic.ccmtv.yx.R.id.cycle_listview;
import static com.linlic.ccmtv.yx.R.id.id_tv_gp_exam_student_list_item_department;
import static com.linlic.ccmtv.yx.R.id.id_tv_gp_exam_student_list_item_name;
import static com.linlic.ccmtv.yx.R.id.id_tv_gp_exam_student_list_item_score;
import static com.linlic.ccmtv.yx.R.id.rotation_years_listview;

public class ProcessExamDailyStudentListActivity extends BaseActivity {

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
    @Bind(id_tv_gp_exam_student_list_item_name)
    TextView idTvGpExamStudentListItemName;
    @Bind(id_tv_gp_exam_student_list_item_department)
    TextView idTvGpExamStudentListItemDepartment;
    @Bind(id_tv_gp_exam_student_list_item_score)
    TextView idTvGpExamStudentListItemScore;
    @Bind(R.id.id_lv_gp_graduate_exam_student_list_1)
    MyListView idLvGpGraduateExamStudentList1;
    @Bind(R.id.id_tv_gp_graduate_exam_student_list_hint_1)
    TextView idTvGpGraduateExamStudentListHint1;
    @Bind(R.id.id_btn_gp_graduate_exam_student_list_submit_1)
    Button idBtnGpGraduateExamStudentListSubmit1;
    @Bind(R.id.id_tv_gp_graduate_exam_student_list_reject_reason_2)
    LinearLayout idTvGpGraduateExamStudentListRejectReason2;
    @Bind(R.id.id_tv_gp_graduate_exam_student_list_reject_reason_1)
    TextView idTvGpGraduateExamStudentListRejectReason1;
    @Bind(R.id.id_ll_gp_graduate_exam_student_list_1_layout)
    LinearLayout idLlGpGraduateExamStudentList1Layout;
    @Bind(audit_nodata)
    NodataEmptyLayout auditNodata;
    @Bind(cycle_listview)
    ListView cycleListview;
    @Bind(base_listview)
    ListView baseListview;
    @Bind(rotation_years_listview)
    ListView rotationYearsListview;
    @Bind(condition_layout)
    LinearLayout conditionLayout;
    private List<Condition> ccycleDatas = new ArrayList<>();//周期列表数据源
    private List<Condition> baseDatas = new ArrayList<>();//基地列表数据源
    private List<Condition> rotation_yearsDatas = new ArrayList<>();//轮转年限列表数据源
    private BaseListAdapter baseListAdapterCcycle,baseListAdapterBase,baseListAdapterRotation_years;
    private String year = "";
    private String month = "";
    private String fid = "";
    private String exam_id = "";
    private String exam_name="";
    private GpGraduateExamStudent1 gpGraduateExamStudent1 = new GpGraduateExamStudent1();
    private Condition current_week = new Condition() ;//当前周
    private Condition current_base = new Condition() ;//当前基地
    private Condition current_rotation_years = new Condition() ;//当前轮转年限
    private List<GpGraduateExamStudent1> gpGraduateExamStudentList = new ArrayList<>();
    private boolean flag=true;
    private List<DailyStudent> curr_user_list = new ArrayList<>();
    private GpGraduateExamAdapter examStudentListAdapter1;
    private boolean isAllScoreHave = true;
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
                                JSONArray dataList = data.getJSONArray("dataList");
                                List<DailyStudent> user_list = new ArrayList<>();
                                for (int i=0;i<dataList.length();i++) {
//                                    if (i<=3){
                                        JSONObject dataObject = dataList.getJSONObject(i);
                                        //解析用户列表

                                        JSONArray userList = dataObject.getJSONArray("user_list");
                                        List<DailyStudent> examStudentList = new ArrayList<>();
                                        for (int j=0; j<userList.length(); j++) {
                                            JSONObject listObject = userList.getJSONObject(j);
                                            DailyStudent examStudent = new DailyStudent();
                                            examStudent.setRealname(listObject.getString("realname"));
                                            examStudent.setScore(listObject.getString("score"));
                                            examStudent.setUid(listObject.getString("uid"));//
                                            examStudent.setLs_training_years(listObject.getString("ls_training_years"));
                                            examStudent.setDaily_exam_id(listObject.getString("daily_exam_id"));
                                            examStudent.setStandard_name(listObject.getString("standard_name"));
                                            examStudent.setStandard_kid(listObject.getString("standard_kid"));
                                            examStudent.setYear(listObject.getString("year"));
                                            examStudent.setMonth(listObject.getString("month"));
                                            examStudent.setWeek(listObject.getString("week"));
                                            examStudent.setGpyear_id(listObject.getString("gpyear_id"));
                                            examStudent.setBase_id(listObject.getString("base_id"));
                                            examStudent.setUsername(listObject.getString("username"));
                                            examStudent.setCreatetime(listObject.getString("createtime"));
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
                                        GpGraduateExamStudent1 gpGraduateExamStudent = new GpGraduateExamStudent1();
                                        gpGraduateExamStudent.setUser_list(examStudentList);
                                        gpGraduateExamStudentList.add(i,gpGraduateExamStudent);

                                        //解析其他数据
                                        if (dataObject.has("flow")) {
                                            JSONObject flowObject = dataObject.getJSONObject("flow");
                                            gpGraduateExamStudentList.get(i).setType(flowObject.getString("type"));
                                            gpGraduateExamStudentList.get(i).setType_name(flowObject.getString("type_name"));
                                            gpGraduateExamStudentList.get(i).setErrormsg(flowObject.getString("errormsg"));
                                        }
                                        gpGraduateExamStudentList.get(i).setCurrent_week_msg(dataObject.getString("current_week_msg"));
                                        gpGraduateExamStudentList.get(i).setDisabled(dataObject.getString("disabled"));
                                        gpGraduateExamStudentList.get(i).setWeek(dataObject.getString("week"));
                                        gpGraduateExamStudentList.get(i).setIs_current_week(dataObject.getString("is_current_week"));
                                        gpGraduateExamStudentList.get(i).setFlow_name(dataObject.getString("flow_name"));
//                                    }
                                }

                                idTvGpExamStudentListItemName.setText(current_week.getTitle());

                                changeView();

                                handler.sendEmptyMessageDelayed(10, 2000);
                            }else {
                                Toast.makeText(ProcessExamDailyStudentListActivity.this, data.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ProcessExamDailyStudentListActivity.this, result.getString("message"), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(ProcessExamDailyStudentListActivity.this, data.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ProcessExamDailyStudentListActivity.this, data.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ProcessExamDailyStudentListActivity.this, result.getString("message"), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(ProcessExamDailyStudentListActivity.this, data.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ProcessExamDailyStudentListActivity.this, result.getString("message"), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ProcessExamDailyStudentListActivity.this, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_exam_daily_student_list);
        ButterKnife.bind(this);
        activityTitleName.setText("学生列表");
        getCkSearchInfo();
        getIntentData();
        initListView();
        setClick();
    }

    public void setClick(){
        cycleListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                conditionLayout.setVisibility(View.GONE);
                current_week = ccycleDatas.get(i);
                idTvGpExamStudentListItemName.setText(current_week.getTitle());
                changeView();
            }
        });

        baseListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                conditionLayout.setVisibility(View.GONE);
                current_base = baseDatas.get(i);
                idTvGpExamStudentListItemDepartment.setText(current_base.getTitle());
                idTvGpExamStudentListItemDepartment.setBackgroundResource(R.drawable.anniu26);
                idTvGpExamStudentListItemDepartment.setTextColor(Color.parseColor("#69ADFA"));
                Drawable drawable = ProcessExamDailyStudentListActivity.this.getResources().getDrawable(R.mipmap
                        .drop_down_icon1);
                drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                idTvGpExamStudentListItemDepartment.setCompoundDrawables(null,null,drawable,null);
                changeView();
            }
        });

        rotationYearsListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                current_rotation_years = rotation_yearsDatas.get(i);
                conditionLayout.setVisibility(View.GONE);
                idTvGpExamStudentListItemScore.setText(current_rotation_years.getTitle());
                idTvGpExamStudentListItemScore.setBackgroundResource(R.drawable.anniu26);
                idTvGpExamStudentListItemScore.setTextColor(Color.parseColor("#69ADFA"));
                Drawable drawable = ProcessExamDailyStudentListActivity.this.getResources().getDrawable(R.mipmap
                        .drop_down_icon1);
                drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                idTvGpExamStudentListItemScore.setCompoundDrawables(null,null,drawable,null);
                changeView();
            }
        });

        idLvGpGraduateExamStudentList1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                flag = false;
                Intent intent = new Intent(ProcessExamDailyStudentListActivity.this, GpDailyExamActivity.class);
                intent.putExtra("daily_exam_id", curr_user_list.get(position).getDaily_exam_id());
                intent.putExtra("user_id", curr_user_list.get(position).getUid());
                intent.putExtra("data_type", "");
                intent.putExtra("status",gpGraduateExamStudentList.get(Integer.parseInt(current_week.getId())-1).getType());  //1 是审核通过(不可点击) 2 是从新审核（可点击） 3 是提交成绩（可点击） 4是等待审核（不可点击） 5不显示按钮
                intent.putExtra("fid", fid);
                intent.putExtra("isEdit", true);
                intent.putExtra("identity", "staff");
                startActivity(intent);

            }
        });

    }


    /*
      初始化顶部三个选项的数据
     */
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
        baseListAdapterCcycle = new BaseListAdapter(cycleListview,ccycleDatas, R.layout.item_training_management2) {

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
        cycleListview.setAdapter(baseListAdapterCcycle);
        /*初始化周期数据end*/
           /*初始化基地数据start*/
        baseListAdapterBase = new BaseListAdapter(baseListview, baseDatas, R.layout.item_training_management2) {

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
        baseListview.setAdapter(baseListAdapterBase);
         /*初始化基地数据end*/
          /*初始化轮转年限数据start*/
        baseListAdapterRotation_years = new BaseListAdapter(rotationYearsListview, rotation_yearsDatas, R.layout.item_training_management2) {

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
        rotationYearsListview.setAdapter(baseListAdapterRotation_years);
      /*初始化轮转年限数据end*/

        examStudentListAdapter1 = new GpGraduateExamAdapter(ProcessExamDailyStudentListActivity.this, gpGraduateExamStudent1.getUser_list(),"1");
        idLvGpGraduateExamStudentList1.setAdapter(examStudentListAdapter1);

    }

    private void getIntentData() {
        year = getIntent().getStringExtra("year");
        month = getIntent().getStringExtra("month");
        fid = getIntent().getStringExtra("fid");
        exam_id = getIntent().getStringExtra("exam_id");
        exam_name = getIntent().getStringExtra("exam_name");
    }

    @OnClick({R.id.arrow_back, id_tv_gp_exam_student_list_item_name, id_tv_gp_exam_student_list_item_department, id_tv_gp_exam_student_list_item_score, R.id.id_btn_gp_graduate_exam_student_list_submit_1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.arrow_back:
                finish();
                break;
            case id_tv_gp_exam_student_list_item_name:
                if(conditionLayout.getVisibility() == View.VISIBLE){
                    if (cycleListview.getVisibility() == View.VISIBLE){
                        conditionLayout.setVisibility(View.GONE);
                    }else {
                        conditionLayout.setVisibility(View.VISIBLE);
                        cycleListview.setVisibility(View.VISIBLE);
                        baseListview.setVisibility(View.GONE);
                        rotationYearsListview.setVisibility(View.GONE);
                    }
                }else{
                    conditionLayout.setVisibility(View.VISIBLE);
                    cycleListview.setVisibility(View.VISIBLE);
                    baseListview.setVisibility(View.GONE);
                    rotationYearsListview.setVisibility(View.GONE);
                }
                break;
            case id_tv_gp_exam_student_list_item_department:
                //基地
                if(conditionLayout.getVisibility() == View.VISIBLE){
                    if (baseListview.getVisibility() == View.VISIBLE){
                        conditionLayout.setVisibility(View.GONE);
                    }else {
                        conditionLayout.setVisibility(View.VISIBLE);
                        cycleListview.setVisibility(View.GONE);
                        baseListview.setVisibility(View.VISIBLE);
                        rotationYearsListview.setVisibility(View.GONE);
                    }
                }else {
                    conditionLayout.setVisibility(View.VISIBLE);
                    cycleListview.setVisibility(View.GONE);
                    baseListview.setVisibility(View.VISIBLE);
                    rotationYearsListview.setVisibility(View.GONE);
                }
                break;
            case id_tv_gp_exam_student_list_item_score:
                //轮转年限
                if(conditionLayout.getVisibility() == View.VISIBLE){
                    if (rotationYearsListview.getVisibility() == View.VISIBLE){
                        conditionLayout.setVisibility(View.GONE);
                    }else{
                        conditionLayout.setVisibility(View.VISIBLE);
                        cycleListview.setVisibility(View.GONE);
                        baseListview.setVisibility(View.GONE);
                        rotationYearsListview.setVisibility(View.VISIBLE);
                    }

                }else {
                    conditionLayout.setVisibility(View.VISIBLE);
                    cycleListview.setVisibility(View.GONE);
                    baseListview.setVisibility(View.GONE);
                    rotationYearsListview.setVisibility(View.VISIBLE);
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
                    submitExamine();
                } else {
                    Toast.makeText(context, "有学员成绩未录入，不能提交", Toast.LENGTH_SHORT).show();
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
            for (int i = 0; i < curr_user_list.size(); i++) {
                if (!idString.isEmpty()) {
                    idString = idString + ",";
                }
                idString = idString + curr_user_list.get(i).getDaily_exam_id();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        MyProgressBarDialogTools.show(ProcessExamDailyStudentListActivity.this);
        final String finalIdString = idString;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.dailyManSubmit);
                    obj.put("uid", SharedPreferencesTools.getUid(ProcessExamDailyStudentListActivity.this));
                    obj.put("status",gpGraduateExamStudentList.get(Integer.parseInt(current_week.getId())-1).getType());
                    obj.put("exam_name",gpGraduateExamStudentList.get(Integer.parseInt(current_week.getId())-1).getFlow_name());
                    obj.put("id", finalIdString);
                    obj.put("fid", fid);
                    String result = HttpClientUtils.sendPostToGP(ProcessExamDailyStudentListActivity.this, URLConfig.CCMTVAPP_GpApi, obj.toString());
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

    private void initData() {

        MyProgressBarDialogTools.show(this);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.dailyStaList);
                    obj.put("uid", SharedPreferencesTools.getUid(ProcessExamDailyStudentListActivity.this));
                    obj.put("year", year);
                    obj.put("month", month);
                    obj.put("fid", fid);
                    obj.put("id",exam_id);
                    obj.put("exam_name",exam_name);
                    obj.put("is_new","1");//1是新接口
                    String result = HttpClientUtils.sendPostToGP(ProcessExamDailyStudentListActivity.this, URLConfig.CCMTVAPP_GpApi, obj.toString());
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
                    obj.put("uid", SharedPreferencesTools.getUid(ProcessExamDailyStudentListActivity.this));
                    String result = HttpClientUtils.sendPostToGP(ProcessExamDailyStudentListActivity.this, URLConfig.CCMTVAPP_GpApi, obj.toString());
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
        int a=Integer.parseInt(current_week.getId())-1;
        int b=gpGraduateExamStudentList.get(Integer.parseInt(current_week.getId())-1).getUser_list().size();
        for (int i = 0;i <gpGraduateExamStudentList.get(Integer.parseInt(current_week.getId())-1).getUser_list().size();i++){
            DailyStudent examStudent = gpGraduateExamStudentList.get(Integer.parseInt(current_week.getId())-1).getUser_list().get(i);
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
//        if (gpGraduateExamStudentList.get(Integer.parseInt(current_week.getId())-1).getMan_type().equals("2")) {
            idBtnGpGraduateExamStudentListSubmit1.setVisibility(View.VISIBLE);
            //0尚未发通知 1 尚未整体提交成绩 2 审核中 3 审核通过 4审核退回
            switch (gpGraduateExamStudentList.get(Integer.parseInt(current_week.getId())-1).getType()) {
                case "5"://5是不显示按钮
//                    idBtnGpGraduateExamStudentListSubmit1.setText("尚未发送通知");
//                    idBtnGpGraduateExamStudentListSubmit1.setBackgroundResource(R.mipmap.button_03);
//                    idBtnGpGraduateExamStudentListSubmit1.setClickable(false);
                    //isGone();
                    idBtnGpGraduateExamStudentListSubmit1.setVisibility(View.GONE);
                    idTvGpGraduateExamStudentListRejectReason1.setVisibility(View.GONE);
                    idTvGpGraduateExamStudentListRejectReason2.setVisibility(View.GONE);
                    break;
                case "3":
                    idBtnGpGraduateExamStudentListSubmit1.setText("提交");
                    idBtnGpGraduateExamStudentListSubmit1.setBackgroundResource(R.mipmap.button_01);
                    idBtnGpGraduateExamStudentListSubmit1.setClickable(true);
                    isGone();
                    break;
                case "4":
                    idBtnGpGraduateExamStudentListSubmit1.setText("审核中");
                    idBtnGpGraduateExamStudentListSubmit1.setBackgroundResource(R.mipmap.button_03);
                    idBtnGpGraduateExamStudentListSubmit1.setClickable(false);
                    isGone();
                    break;
                case "1":
                    idBtnGpGraduateExamStudentListSubmit1.setText("审核通过");
                    idBtnGpGraduateExamStudentListSubmit1.setBackgroundResource(R.mipmap.button_03);
                    idBtnGpGraduateExamStudentListSubmit1.setClickable(false);
                    isGone();
                    break;
                case "2":
                    idBtnGpGraduateExamStudentListSubmit1.setText("重新提交");
                    idBtnGpGraduateExamStudentListSubmit1.setBackgroundResource(R.mipmap.button_01);
                    idBtnGpGraduateExamStudentListSubmit1.setClickable(true);
                    idTvGpGraduateExamStudentListRejectReason1.setVisibility(View.VISIBLE);
                    idTvGpGraduateExamStudentListRejectReason2.setVisibility(View.VISIBLE);
                    idTvGpGraduateExamStudentListRejectReason1.setText(gpGraduateExamStudentList.get(Integer.parseInt(current_week.getId())-1).getErrormsg());
                    break;
            }
//        }

//        else{
//            idBtnGpGraduateExamStudentListSubmit1.setVisibility(View.GONE);
//            idTvGpGraduateExamStudentListRejectReason1.setVisibility(View.GONE);
//            idTvGpGraduateExamStudentListRejectReason2.setVisibility(View.GONE);
//        }


    }

    private void isGone(){
        idTvGpGraduateExamStudentListRejectReason1.setVisibility(View.GONE);
        idTvGpGraduateExamStudentListRejectReason2.setVisibility(View.GONE);
        idTvGpGraduateExamStudentListRejectReason1.setText(gpGraduateExamStudentList.get(Integer.parseInt(current_week.getId())-1).getErrormsg());
    }

    private void setResultStatus(boolean status, int code) {
        if (status) {
            idLlGpGraduateExamStudentList1Layout.setVisibility(View.VISIBLE);
            auditNodata.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                auditNodata.setNetErrorIcon();
            } else {
                auditNodata.setLastEmptyIcon();
            }
            idLlGpGraduateExamStudentList1Layout.setVisibility(View.GONE);
            auditNodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

}
