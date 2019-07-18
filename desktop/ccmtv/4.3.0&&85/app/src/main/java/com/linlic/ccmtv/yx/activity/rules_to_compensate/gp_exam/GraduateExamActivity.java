package com.linlic.ccmtv.yx.activity.rules_to_compensate.gp_exam;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.UploadModel;
import com.linlic.ccmtv.yx.activity.my.GlideImageLoader;
import com.linlic.ccmtv.yx.activity.my.medical_examination.Check_the_answer_sheet;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.SpinerAdapter;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.SpinerPopWindow;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.adapter.GraduateExamPicGridViewAdapter;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.adapter.GraduateExamQuestionsAdapter;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.javabean.GraduateExamQuestion;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.Base64utils;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.convert.Converter;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okserver.OkUpload;
import com.lzy.okserver.upload.UploadTask;

import org.angmarch.views.NiceSpinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import okhttp3.Response;

/**
 * 规培出科考核界面
 * Created by bentley on 2018/6/26.
 */

public class GraduateExamActivity extends BaseActivity implements View.OnClickListener {

    private TextView title_name,id_tv_graduate_exam_examined_teacher_name;
    private LinearLayout llParent,ll_teacher_name;
    private LinearLayout status_layout;// 缺考 正常 按钮容器
    private GridView gvPic;
    private ListView lvQueations;
    private Button examination_instructions_buttpm1;
    //    private GridView lvQueations;
    private List<PhotoInfo> listPhoto = new ArrayList<>();
    private List<GraduateExamQuestion> listQuestion = new ArrayList<>();
    private List<Map<String, String>> listQuestionMap = new ArrayList<>();
    private List<String> listSpinner = new ArrayList<>();
    private List<String> formListSpinner = new ArrayList<>();
    private List<Map<String, String>> formList = new ArrayList<>();
    private GraduateExamPicGridViewAdapter graduateExamPicGridViewAdapter;
    private BaseListAdapter baseListAdapter;
    private Context context;
    private GraduateExamQuestionsAdapter graduateExamQuestionsAdapter;
    private RadioGroup radioGroupStatus;
    private RadioButton rbNormal, rbAbsence;
    //    private ScrollView mScrollView;
    private LinearLayout llContent, llPicture, llAbsenceNonexam, llAbsenceNonexamFooter,ll_score_type;
    private ImageView ivAddPic;
    private TextView tvSubmit, tvCancel, tvSubmitFooter, tvCancelFooter,score_type,tv_show_score;
    private LinearLayout llExaminedStuName, llTemplate, llScore, llExaminer, llExamDate, llEnteringType;
    private EditText etScore, etExaminer, etExamDate;
    private TextView tvExaminedStuName, tvEnteringType;
    private NiceSpinner niceSpinnerTemplate;
    private Map<String, String> mapGlobal = new HashMap<>();
    private Map<String, String> mapIntentData = new HashMap<>();
    private boolean canEditable = false;
    private String disabled = "";
    private PostRequest<String> postRequest;
    private String form_id = "";
    private JSONArray formListArray;
    private JSONObject formInfoObject;
    private SimpleDateFormat df;
    private RelativeLayout rlSpinnerLayout;
    private TextView tvSpinnerValue;
    private TextView examination_title;
    private String related_examinations;//关联考试 1 为关联考试
    private String type;

    private String theoretical_examination_type;
    private String theoretical_examination_score ;
    private String theoretical_examination_alertmsg  ;
    private String theoretical_examination_eid  ;
    private String theoretical_examination_is_no_exam  ;
    private String theoretical_examination_end_time  ;
    private String theoretical_examination_pid  ;
    private String theoretical_examination_is_no_join  ;

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
                                if (dataObject.has("form_list")) {
                                    formListArray = dataObject.getJSONArray("form_list");
                                    for (int i = 0; i < formListArray.length(); i++) {
                                        JSONObject formObject = formListArray.getJSONObject(i);
                                        Map<String, String> map = new HashMap<>();
                                        map.put("form_id", formObject.getString("form_id"));
                                        map.put("title", formObject.getString("title"));
                                        formListSpinner.add(formObject.getString("title"));
                                        formList.add(map);
                                    }
                                }
                                JSONObject userInfoObject = dataObject.getJSONObject("userinfo");
                                mapGlobal.put("item_id", dataObject.getString("item_id"));
                                mapGlobal.put("details_id", dataObject.getString("details_id"));
                                mapGlobal.put("standard_kid", dataObject.getString("standard_kid"));
                                mapGlobal.put("item_status", dataObject.getString("item_status"));
                                mapGlobal.put("tutorname",dataObject.getString("tutorname"));
                                mapGlobal.put("username", userInfoObject.getString("username"));
                                mapGlobal.put("realname", userInfoObject.getString("realname"));
                                mapGlobal.put("disabled", dataObject.getString("disabled"));  //是否可以编辑  1、不可编辑   0、可以编辑

                                LogUtil.e("出科参数","related_examinations:"+related_examinations+"   item_status:"+ mapIntentData.get("item_status")+"  type:"+type);

                                if(related_examinations.equals("1") &&  mapIntentData.get("item_status").equals("1") && type.equals("2")){
                                    JSONObject liluninfo = dataObject.getJSONObject("liluninfo");
                                    theoretical_examination_end_time = liluninfo.getString("end_time");
                                    theoretical_examination_type = liluninfo.getString("lilun_type");
                                    theoretical_examination_score = liluninfo.getString("score");
                                    theoretical_examination_eid = liluninfo.getString("exam_user_id");
                                    theoretical_examination_pid = liluninfo.getString("paper_id");
                                    theoretical_examination_alertmsg = liluninfo.getString("alertmsg");
                                    theoretical_examination_is_no_exam = liluninfo.getString("is_no_exam");
                                    theoretical_examination_is_no_join = liluninfo.getString("is_no_join");
                                }


//                                disabled = dataObject.getString("disabled");
                                setViews();

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
                            if (data.getInt("status") == 1) {
                                listQuestion.clear();
                                JSONObject dataObject = data.getJSONObject("data");
                                //disabled = dataObject.getString("disabled");   //是否可以编辑  1、不可编辑   0、可以编辑
                                formInfoObject = dataObject.getJSONObject("forminfo");
                                mapGlobal.put("is_upload_img", formInfoObject.getString("is_upload_img"));  //是否需要上传照片  1、需要   0、不需要
                                mapGlobal.put("entering", formInfoObject.getString("entering"));  //form表单录入 grade分数录入
                                mapGlobal.put("marking", formInfoObject.getString("marking"));  //deduct_marks扣分 score得分
                                mapGlobal.put("examiner", formInfoObject.getString("examiner"));  //考官
                                mapGlobal.put("enter_time", formInfoObject.getString("enter_time"));  //录入时间
                                mapGlobal.put("score", formInfoObject.getString("score"));//分数
                                mapGlobal.put("deduct_marks_account", formInfoObject.getString("deduct_marks_account"));
                                String marking = formInfoObject.getString("marking");
                                if(marking.equals("deduct_marks")){
                                    score_type.setText("扣分");
                                    ll_score_type.setVisibility(View.VISIBLE);
                                }else if(marking.equals("score")){
                                    score_type.setText("得分");
                                    ll_score_type.setVisibility(View.VISIBLE);
                                }else if(marking.equals("")){
                                    ll_score_type.setVisibility(View.GONE);
                                }
                                if (formInfoObject.has("item")) {
                                    JSONArray formItemArray = formInfoObject.getJSONArray("item");
                                    for (int i = 0; i < formItemArray.length(); i++) {
                                        GraduateExamQuestion graduateExamQuestion = new GraduateExamQuestion();
                                        JSONObject formItemObject = formItemArray.getJSONObject(i);
                                        graduateExamQuestion.setQuestionContent(formItemObject.getString("name"));
                                        graduateExamQuestion.setDataType("title");
                                        graduateExamQuestion.setDisabled(disabled);
                                        graduateExamQuestion.setMarking(mapGlobal.get("marking"));
                                        graduateExamQuestion.setDeduct_marks_account(mapGlobal.get("deduct_marks_account"));
                                        graduateExamQuestion.setSerialNumber(i + "");
                                        graduateExamQuestion.setCurr_pos(i+1);
                                        Map<String, String> mapTitle = new HashMap<>();
                                        mapTitle.put("name", formItemObject.getString("name"));
                                        mapTitle.put("dataType", "title");
                                        listQuestionMap.add(mapTitle);
                                        if(related_examinations.equals("1") &&  mapIntentData.get("item_status").equals("1") && type.equals("2")){
                                        }else{
                                            listQuestion.add(graduateExamQuestion);
                                        }
                                        if (formItemObject.has("config")) {
                                            JSONArray formConfigArray = formItemObject.getJSONArray("config");
                                            List<GraduateExamQuestion> configBeanList = new ArrayList<>();
                                            for (int j = 0; j < formConfigArray.length(); j++) {
                                                JSONObject configObject = formConfigArray.getJSONObject(j);
                                                GraduateExamQuestion graduateExamQuestion1 = new GraduateExamQuestion();
                                                graduateExamQuestion1.setQuestionContent(configObject.getString("name"));
                                                graduateExamQuestion1.setStandardScore(configObject.getString("grade"));
                                                graduateExamQuestion1.setSerialNumber(i + "");
                                                if (graduateExamQuestion1.getStandardScore() != null && !graduateExamQuestion1.getStandardScore().isEmpty()) {
                                                    List spinnerList = new ArrayList<>();
                                                    for (int k = 0; k <= Integer.parseInt(graduateExamQuestion1.getStandardScore()); k++) {
                                                        spinnerList.add(k + "");
                                                    }
                                                    graduateExamQuestion1.setScoreList(spinnerList);
                                                }
                                                graduateExamQuestion1.setDataType("content");
                                                if (configObject.has("score")) {
                                                    String score=configObject.getString("score");
                                                    if(score.equals("")||score.equals("0")){
                                                        if(mapGlobal.get("marking").toString().equals("score")){//得分
                                                            String str=configObject.getString("grade");
                                                            graduateExamQuestion1.setDeductionScore(configObject.getString("grade"));
                                                        }else if(mapGlobal.get("marking").toString().equals("deduct_marks")){//扣分
                                                            graduateExamQuestion1.setDeductionScore("0");
                                                        }
                                                    } else {
                                                        graduateExamQuestion1.setDeductionScore(score);
                                                    }
                                                } else {
                                                    graduateExamQuestion1.setDeductionScore("0");
                                                }
                                                if (configObject.has("reason")) {
                                                    graduateExamQuestion1.setDeductionReason(configObject.getString("reason"));
                                                } else {
                                                    graduateExamQuestion1.setDeductionReason("");
                                                }

                                                graduateExamQuestion1.setDisabled(disabled);
                                                graduateExamQuestion1.setMarking(mapGlobal.get("marking"));
                                                graduateExamQuestion1.setDeduct_marks_account(mapGlobal.get("deduct_marks_account"));
                                                if(related_examinations.equals("1") &&  mapIntentData.get("item_status").equals("1") && type.equals("2")){

                                                }else{
                                                    listQuestion.add(graduateExamQuestion1);
                                                }
                                                Map<String, String> mapContent = new HashMap<>();
                                                mapContent.put("name", configObject.getString("name"));
                                                mapContent.put("grade", configObject.getString("grade"));
                                                mapContent.put("score", "0");
                                                mapContent.put("dataType", "content");
                                                mapContent.put("disabled", disabled);
                                                listQuestionMap.add(mapContent);
                                            }
                                        }
                                    }
                                }
//                                Log.e(getLocalClassName(), "handleMessage: listQuestionMap大小：" + listQuestionMap.size());
                                baseListAdapter.notifyDataSetChanged();
//                                graduateExamQuestionsAdapter.notifyDataSetChanged();

                                if (formInfoObject.has("img_text")) {
                                    listPhoto.clear();
                                    JSONArray picUrlArray = formInfoObject.getJSONArray("img_text");
                                    for (int i = 0; i < picUrlArray.length(); i++) {
                                        JSONObject picUrlObject = picUrlArray.getJSONObject(i);
                                        PhotoInfo photoInfo = new PhotoInfo();
                                        photoInfo.setPhotoPath(picUrlObject.getString("imgurl"));
                                        photoInfo.setPhotoName(picUrlObject.getString("imgpath"));
                                        listPhoto.add(photoInfo);
                                    }
                                }
                                graduateExamPicGridViewAdapter.setDisabled(disabled);
                                changeFormViews();
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
                case 3:
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
                case 4:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getString("code").equals("200")) {
                            JSONObject data = result.getJSONObject("data");
                            if (data.getInt("status") == 1) {
                                Toast.makeText(context, data.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                if (data.has("data")) {
                                    JSONArray array = data.getJSONArray("data");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject dataObject = array.getJSONObject(i);
                                        PhotoInfo photoInfo = new PhotoInfo();
                                        photoInfo.setPhotoName(dataObject.getString("imgpath"));
                                        photoInfo.setPhotoPath(dataObject.getString("imgurl"));
                                        listPhoto.add(photoInfo);
                                    }
                                }
                                graduateExamPicGridViewAdapter.notifyDataSetChanged();
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
        setContentView(R.layout.activity_graduate_exam);
        context = this;
        df = new SimpleDateFormat("yyyy-MM-dd");
        findId();
        getIntentData();
        initViews();
        initData();
//        addViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/allexam/index.html";
        super.onPause();
    }

    public void findId() {
        View headerView = LayoutInflater.from(context).inflate(R.layout.gp_graduate_exam_header_view, null);
        View footerView = LayoutInflater.from(context).inflate(R.layout.gp_graduate_exam_footer_view, null);

        title_name = (TextView) findViewById(R.id.activity_title_name);

        lvQueations = findViewById(R.id.id_lv_graduate_exam_questions);
        radioGroupStatus = (RadioGroup) findViewById(R.id.id_radiogroup_gp_exam_status);
        rbNormal = (RadioButton) findViewById(R.id.id_rb_gp_exam_subject_status_normal);
        rbAbsence = (RadioButton) findViewById(R.id.id_rb_gp_exam_subject_status_absence);
//        mScrollView = findViewById(R.id.id_scrollview_graduate_exam);
        llAbsenceNonexam = (LinearLayout) findViewById(R.id.id_ll_gp_graduate_exam_absence_nonexam);
        tvSubmit = (TextView) findViewById(R.id.id_tv_gp_graduate_exam_submit);
        tvCancel = (TextView) findViewById(R.id.id_tv_gp_graduate_exam_cancel);

        llAbsenceNonexamFooter = (LinearLayout) footerView.findViewById(R.id.id_ll_gp_graduate_exam_absence_nonexam_footer);
        tvSubmitFooter = (TextView) footerView.findViewById(R.id.id_tv_gp_graduate_exam_submit_footer);
        tvCancelFooter = (TextView) footerView.findViewById(R.id.id_tv_gp_graduate_exam_cancel_footer);
        examination_instructions_buttpm1 = (Button) headerView.findViewById(R.id.examination_instructions_buttpm1);
        status_layout = (LinearLayout)  findViewById(R.id.status_layout);
        examination_title = (TextView)  headerView.findViewById(R.id.examination_title);
        ll_teacher_name = (LinearLayout) headerView.findViewById(R.id.ll_teacher_name);
        id_tv_graduate_exam_examined_teacher_name = (TextView) headerView.findViewById(R.id.id_tv_graduate_exam_examined_teacher_name);
        llContent = (LinearLayout) headerView.findViewById(R.id.id_ll_graduate_exam_content);
        llParent = (LinearLayout) headerView.findViewById(R.id.id_ll_graduate_exam_parent_layout);
        llPicture = (LinearLayout) headerView.findViewById(R.id.id_ll_graduate_exam_upload_picture);
        gvPic = (GridView) headerView.findViewById(R.id.id_gv_graduate_exam_pic);
        ivAddPic = (ImageView) headerView.findViewById(R.id.id_iv_graduate_exam_add_pic);
        llExaminedStuName = (LinearLayout) headerView.findViewById(R.id.id_ll_graduate_exam_examined_stu_name);
        tv_show_score = (TextView) headerView.findViewById(R.id.tv_show_score);
        ll_score_type = (LinearLayout) headerView.findViewById(R.id.ll_score_type);
        score_type = (TextView) headerView.findViewById(R.id.score_type);
        llTemplate = (LinearLayout) headerView.findViewById(R.id.id_ll_graduate_exam_template);
        llScore = (LinearLayout) headerView.findViewById(R.id.id_ll_graduate_exam_score);
        llExaminer = (LinearLayout) headerView.findViewById(R.id.id_ll_graduate_exam_examiner);
        llExamDate = (LinearLayout) headerView.findViewById(R.id.id_ll_graduate_exam_date);
        llEnteringType = (LinearLayout) headerView.findViewById(R.id.id_ll_graduate_exam_entering_type);
        etScore = (EditText) headerView.findViewById(R.id.id_et_graduate_exam_score);
        etExaminer = (EditText) headerView.findViewById(R.id.id_et_graduate_exam_examiner);
        etExamDate = (EditText) headerView.findViewById(R.id.id_et_graduate_exam_date);
        tvExaminedStuName = (TextView) headerView.findViewById(R.id.id_tv_graduate_exam_examined_stu_name);
        tvEnteringType = (TextView) headerView.findViewById(R.id.id_tv_graduate_exam_entering_type);
//        niceSpinnerTemplate = (NiceSpinner) headerView.findViewById(R.id.id_spinner_graduate_exam_template);
        rlSpinnerLayout = (RelativeLayout) headerView.findViewById(R.id.rl_item_grade);
        tvSpinnerValue = (TextView) headerView.findViewById(R.id.tv_value_form);

        lvQueations.addHeaderView(headerView);
        lvQueations.addFooterView(footerView);

        /*title_name = findViewById(R.id.activity_title_name);
        llParent = findViewById(R.id.id_ll_graduate_exam_parent_layout);
        gvPic = findViewById(R.id.id_gv_graduate_exam_pic);
        lvQueations = findViewById(R.id.id_lv_graduate_exam_questions);
        radioGroupStatus =  findViewById(R.id.id_radiogroup_gp_exam_status);
        rbNormal = findViewById(R.id.id_rb_gp_exam_subject_status_normal);
        rbAbsence = findViewById(R.id.id_rb_gp_exam_subject_status_absence);

//        mScrollView = findViewById(R.id.id_scrollview_graduate_exam);
        llContent = findViewById(R.id.id_ll_graduate_exam_content);
        llAbsenceNonexam = findViewById(R.id.id_ll_gp_graduate_exam_absence_nonexam);

        llExaminedStuName = findViewById(R.id.id_ll_graduate_exam_examined_stu_name);
        llTemplate = findViewById(R.id.id_ll_graduate_exam_template);
        llScore = findViewById(R.id.id_ll_graduate_exam_score);
        llExaminer = findViewById(R.id.id_ll_graduate_exam_examiner);
        llExamDate = findViewById(R.id.id_ll_graduate_exam_date);
        llEnteringType = findViewById(R.id.id_ll_graduate_exam_entering_type);
        etScore = findViewById(R.id.id_et_graduate_exam_score);
        etExaminer = findViewById(R.id.id_et_graduate_exam_examiner);
        etExamDate = findViewById(R.id.id_et_graduate_exam_date);
        tvExaminedStuName = findViewById(R.id.id_tv_graduate_exam_examined_stu_name);
        tvEnteringType = findViewById(R.id.id_tv_graduate_exam_entering_type);
        niceSpinnerTemplate = findViewById(R.id.id_spinner_graduate_exam_template);*/

        title_name.setText("出科考核");

        radioGroupStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.id_rb_gp_exam_subject_status_normal:
                        lvQueations.setVisibility(View.VISIBLE);
                        llAbsenceNonexam.setVisibility(View.GONE);
                        break;
                    case R.id.id_rb_gp_exam_subject_status_absence:
                        lvQueations.setVisibility(View.GONE);
                        llAbsenceNonexam.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        etExamDate.setFocusable(false);
        etExamDate.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        tvSubmitFooter.setOnClickListener(this);
        tvCancelFooter.setOnClickListener(this);

        etScore.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                LogUtil.e("输入的成绩：", editable.toString());
                String words = editable.toString();
                //首先内容进行非空判断，空内容（""和null）不处理
                if (!TextUtils.isEmpty(editable.toString())) {
                    Pattern p = Pattern.compile("^(100|[1-9]\\d|\\d)$");
                    Matcher m = p.matcher(words);
                    if (m.find() || ("").equals(words)) {
                        //这个时候输入的是合法范围内的值
                        tv_show_score.setText(words);
                    } else {
                        etScore.setText("");
                        Toast.makeText(context, "请输入范围在1-100之间的整数", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    tv_show_score.setText("0");
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_et_graduate_exam_date:
                showDatePickerDialog(GraduateExamActivity.this, etExamDate, Calendar.getInstance());
                break;
            case R.id.id_tv_gp_graduate_exam_submit:
                if (rbNormal.isChecked()) {
                    //发送正常考试信息
                    sendGraduateExamDetail(1);
                } else {
                    //发送缺考信息
//                    Log.e(getLocalClassName(), "onClick: 缺考");
                    sendGraduateExamDetail(2);
                }
                break;
            case R.id.id_tv_gp_graduate_exam_cancel:
                finish();
                break;
            case R.id.id_tv_gp_graduate_exam_submit_footer:
                if (rbNormal.isChecked()) {
                    //发送正常考试信息
//                    Log.e(getLocalClassName(), "onClick: 最后一项的扣分理由："+listQuestion.get(listQuestion.size()-1).getDeductionReason());
                    sendGraduateExamDetail(1);
                } else {
                    //发送缺考信息
//                    Log.e(getLocalClassName(), "onClick: 缺考：");
                    sendGraduateExamDetail(2);
                }
                break;
            case R.id.id_tv_gp_graduate_exam_cancel_footer:
                finish();
                break;
        }
    }

    private void getIntentData() {
        Intent intent = getIntent();
        mapIntentData.put("fid", intent.getStringExtra("fid"));
        mapIntentData.put("gp_uid", intent.getStringExtra("gp_uid"));
        mapIntentData.put("standard_kid", intent.getStringExtra("standard_kid"));
        mapIntentData.put("item_id", intent.getStringExtra("item_id"));
        mapIntentData.put("details_id", intent.getStringExtra("details_id"));
        mapIntentData.put("item_status", intent.getStringExtra("item_status"));
        mapIntentData.put("itemArray", intent.getStringExtra("itemArray"));
        disabled = intent.getStringExtra("disabled");
        related_examinations = intent.getStringExtra("related_examinations");
        type = intent.getStringExtra("type");
//        Log.e(getLocalClassName(), "getIntentData: itemArray:" + intent.getStringExtra("itemArray"));
    }

    private void initViews() {
        graduateExamPicGridViewAdapter = new GraduateExamPicGridViewAdapter(this, listPhoto);
        gvPic.setAdapter(graduateExamPicGridViewAdapter);

        canEditable = true;
        baseListAdapter = new BaseListAdapter(lvQueations, listQuestion, R.layout.item_graduate_exam_questions) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                int position = baseListAdapter.getPosition();
                GraduateExamQuestion map = (GraduateExamQuestion) item;
//                helper.setText(R.id._item_id, map.getId());
                if (map.getDataType() != null && map.getDataType().equals("title")) {
                    helper.setText(R.id.id_tv_questions_item_title, map.getQuestionContent());
                    helper.setVisibility(R.id.id_ll_item_graduate_exam_questions, View.GONE);
                    helper.setVisibility(R.id.id_tv_questions_item_title, View.VISIBLE);
                    helper.setTag(R.id.id_ll_graduate_exam_item_deduction_reason, position + "");
                    helper.setVisibility(R.id.id_line, View.GONE);
                } else {
                    helper.setVisibility(R.id.id_line, View.VISIBLE);
                    helper.setText(R.id.id_tv_questions_item_content, map.getQuestionContent());
                    helper.setVisibility(R.id.id_ll_item_graduate_exam_questions, View.VISIBLE);
                    helper.setVisibility(R.id.id_tv_questions_item_title, View.GONE);
                    if (map.getStandardScore().isEmpty()) {
                        helper.setVisibility(R.id.ll_right_data, View.GONE);
                    } else {
                        helper.setVisibility(R.id.ll_right_data, View.VISIBLE);
                        helper.setText(R.id.id_tv_graduate_exam_questions_standard_score, "标准分:" + map.getStandardScore());
//                        if (map.getMarking().equals("deduct_marks")) {
//                           // helper.setText(R.id.id_tv_graduate_exam_questions_deduction, "扣分");
//                        } else {
//                            //helper.setText(R.id.id_tv_graduate_exam_questions_deduction, "得分");
//                        }
                    }

                    helper.setTag(R.id.id_ll_graduate_exam_item_deduction_reason, position + "");
                    helper.setSpinnerGraduateExam(R.id._item_grade, etScore, listQuestion, map, canEditable, R.id.tv_value, R.id.id_ll_graduate_exam_item_deduction_reason, R.id.id_et_graduate_exam_item_deduction_reason);
//                    helper.setText(R.id.id_et_graduate_exam_item_deduction_reason, map.getDeductionReason());
                    helper.setFocusGraduateExamEdittext1(R.id.id_et_graduate_exam_item_deduction_reason, map, listQuestion, R.id.id_ll_graduate_exam_item_deduction_reason);
                }
            }
        };

        lvQueations.setAdapter(baseListAdapter);
        /*lvQueations.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (i == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    for (int j=0;j<listQuestion.size();j++) {
                        Log.e(getLocalClassName(), "onScrollStateChanged: 扣分原因"+j+":"+listQuestion.get(j).getDeductionReason());
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });*/

        /*graduateExamQuestionsAdapter = new GraduateExamQuestionsAdapter(GraduateExamActivity.this, listQuestion);
        lvQueations.setAdapter(graduateExamQuestionsAdapter);*/

        /*niceSpinnerTemplate.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                getFormDetails(formList.get(position).get("form_id"));
                form_id = formList.get(position).get("form_id");
            }
        });*/

        rlSpinnerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpinerPopWindow mSpinerPopWindow;
                SpinerAdapter mAdapter;
                mAdapter = new SpinerAdapter(context, formListSpinner);
                mAdapter.refreshData(formListSpinner, 0);
                //初始化PopWindow
                mSpinerPopWindow = new SpinerPopWindow(context);
                mSpinerPopWindow.setAdatper(mAdapter);
                //设置mSpinerPopWindow显示的宽度
                mSpinerPopWindow.setWidth(rlSpinnerLayout.getWidth());
                //设置显示的位置在哪个控件的下方
                mSpinerPopWindow.showAsDropDown(rlSpinnerLayout);
                mSpinerPopWindow.setItemListener(new SpinerAdapter.IOnItemSelectListener() {
                    @Override
                    public void onItemClick(int position) {
                        tvSpinnerValue.setText(formList.get(position).get("title"));
                        getFormDetails(formList.get(position).get("form_id"));
                        form_id = formList.get(position).get("form_id");
                    }
                });

            }
        });
    }

    private void initData() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getItemForms);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("fid", mapIntentData.get("fid"));
                    obj.put("gp_uid", mapIntentData.get("gp_uid"));
                    obj.put("standard_kid", mapIntentData.get("standard_kid"));
                    obj.put("item_id", mapIntentData.get("item_id"));
                    obj.put("details_id", mapIntentData.get("details_id"));
                    obj.put("item_status", mapIntentData.get("item_status"));
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("规培出科考核详细信息数据：", result);

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

    private void setViews() {
//        niceSpinnerTemplate.attachDataSource(formListSpinner);
        tvExaminedStuName.setText(mapGlobal.get("realname") + "(" + mapGlobal.get("username") + ")");
        id_tv_graduate_exam_examined_teacher_name.setText(mapGlobal.get("tutorname"));
        if (formList.size() > 0) {
            tvSpinnerValue.setText(formListSpinner.get(0));
            getFormDetails(formList.get(0).get("form_id"));
            form_id = formList.get(0).get("form_id");
        }

        //是否可以编辑  1、不可编辑   0、可以编辑
        if (disabled.equals("0")) {
            rbNormal.setEnabled(true);
            rbAbsence.setEnabled(true);
            llAbsenceNonexamFooter.setVisibility(View.VISIBLE);
//            niceSpinnerTemplate.setEnabled(true);
            rlSpinnerLayout.setEnabled(true);
        } else {
            rbNormal.setEnabled(false);
            rbAbsence.setEnabled(false);
            llAbsenceNonexamFooter.setVisibility(View.GONE);
            llAbsenceNonexam.setVisibility(View.GONE);
//            niceSpinnerTemplate.setEnabled(false);
            rlSpinnerLayout.setEnabled(false);
        }

        //item_status 1正常 2缺考
        if (mapGlobal.get("item_status").equals("1")) {
            rbNormal.setChecked(true);
            rbAbsence.setChecked(false);
        } else {
            rbNormal.setChecked(false);
            rbAbsence.setChecked(true);
        }

        //开始处理 关联考试
        if(related_examinations.equals("1") &&  mapIntentData.get("item_status").equals("1") && type.equals("2")){
            status_layout.setVisibility(View.GONE);//隐藏 缺考 正常 选项
            llTemplate.setVisibility(View.GONE);//隐藏模版
            llPicture.setVisibility(View.GONE);//图片上传隐藏
            examination_instructions_buttpm1.setVisibility(View.VISIBLE);//查看答题卡
            examination_title.setText("分数录入：");
        }else{
            status_layout.setVisibility(View.VISIBLE);//隐藏 缺考 正常 选项
            llTemplate.setVisibility(View.VISIBLE);//隐藏模版
            llPicture.setVisibility(View.VISIBLE);//图片上传隐藏
            examination_instructions_buttpm1.setVisibility(View.GONE);//查看答题卡
        }
        examination_instructions_buttpm1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enter_the_examination();
            }
        });
    }

    /*查看答题卡*/
    public void enter_the_examination( ) {
        switch (theoretical_examination_type){
            case "0":
                Toast.makeText(context, theoretical_examination_alertmsg, Toast.LENGTH_SHORT).show();
                break;
            case "1":
                Intent intent = new Intent(context, Check_the_answer_sheet.class);
                intent.putExtra("pid", theoretical_examination_pid);
                intent.putExtra("my_exams_id", theoretical_examination_pid);
                intent.putExtra("my_exams_eid", theoretical_examination_eid);
                intent.putExtra("is_mock_exam","0");
                startActivity(intent);
                break;
            case "2":
                Toast.makeText(context, theoretical_examination_alertmsg, Toast.LENGTH_SHORT).show();
                break;
        }

    }

    private void changeFormViews() {
        //开始处理 关联考试

        etScore.setText(mapGlobal.get("score"));
        tv_show_score.setText(mapGlobal.get("score"));
        etExaminer.setText(mapGlobal.get("examiner"));
        etExamDate.setText(mapGlobal.get("enter_time"));
        etExamDate.setEnabled(false);
        etExaminer.setEnabled(false);


        //开始处理 关联考试
        if(related_examinations.equals("1") &&  mapIntentData.get("item_status").equals("1") && type.equals("2")){
            llPicture.setVisibility(View.GONE);
        }else {
            if (listPhoto.size() > 0) {
                llPicture.setVisibility(View.VISIBLE);
            } else {
                llPicture.setVisibility(View.GONE);
            }
        }
        graduateExamPicGridViewAdapter.notifyDataSetChanged();

        if (mapGlobal.get("entering").equals("form")) {  //form表单录入 grade分数录入
            tvEnteringType.setText("表单录入");
        } else {
            tvEnteringType.setText("分数录入");
        }

        if (disabled.equals("0")) {  //是否可以编辑  1、不可编辑   0、可以编辑
            etScore.setEnabled(true);
//            Log.e(getLocalClassName(), "changeFormViews: 用户真实姓名：" + SharedPreferencesTools.getUserTrueName(context));
            etExaminer.setText(SharedPreferencesTools.getUserTrueName(context));
//            etExamDate.setText(df.format(System.currentTimeMillis()));
            //开始处理 关联考试
            if(related_examinations.equals("1") &&  mapIntentData.get("item_status").equals("1") && type.equals("2")){
                llPicture.setVisibility(View.GONE);
            }else {
                if (mapGlobal.get("is_upload_img").equals("1")) {
                    //是否需要上传照片  1、需要   0、不需要
                    llPicture.setVisibility(View.VISIBLE);
                } else {
                    llPicture.setVisibility(View.GONE);
                }
            }

            if (mapGlobal.get("entering").equals("form")) {  //form表单录入 grade分数录入
                if(related_examinations.equals("1") &&  mapIntentData.get("item_status").equals("1") && type.equals("2")) {
                    etScore.setEnabled(true);
                }else {
                    etScore.setEnabled(false);
                }
            } else {
                listQuestion.clear();
                etScore.setEnabled(true);
//                graduateExamPicGridViewAdapter.notifyDataSetChanged();
            }
            //开始处理 关联考试
            if(related_examinations.equals("1") &&  mapIntentData.get("item_status").equals("1") && type.equals("2")){
                etScore.setText(theoretical_examination_score);
            }else{
                if (mapGlobal.get("score").isEmpty()) {
                    etScore.setText("100");
                } else {
                    etScore.setText(mapGlobal.get("score"));
                }
            }

        } else {
            etScore.setText(mapGlobal.get("score"));
            etScore.setEnabled(false);
            etExaminer.setEnabled(false);
            etExamDate.setEnabled(false);
            ivAddPic.setVisibility(View.GONE);
            llAbsenceNonexam.setVisibility(View.GONE);
        }
    }

    private void getFormDetails(final String form_id) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getItemFormDetail);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("fid", mapIntentData.get("fid"));
                    obj.put("form_id", form_id);
                    obj.put("details_id", mapIntentData.get("details_id"));
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("规培出科考核详细表单信息数据：", result);

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

    private void sendGraduateExamDetail(int status) {
        /*if (mapGlobal.get("is_upload_img").equals("1")) {
            //是否需要上传照片  1、需要   0、不需要
            if (listPhoto.size() <= 0) {

            }
        }*/

        //  status为考核与缺考状态  1为正常，，2为缺考
        MyProgressBarDialogTools.show(context);
        try {
            //处理item_config
            JSONArray itemConfigArray = new JSONArray(mapIntentData.get("itemArray").toString());
            for (int i = 0; i < itemConfigArray.length(); i++) {
                JSONObject itemObject = itemConfigArray.getJSONObject(i);
                if (itemObject.getString("item_id").equals(mapIntentData.get("item_id").toString())) {
                    itemObject.put("score", etScore.getText().toString());
                    if (status == 2) {
                        itemObject.put("item_status", "2");
                    } else {
                        itemObject.put("item_status", "1");
                    }
                }
            }


            JSONObject formObject = new JSONObject();
            JSONArray formConfigArray = new JSONArray();

            System.out.println(df.format(System.currentTimeMillis()));
            JSONArray formArray = new JSONArray();
            JSONObject contentObject = new JSONObject();

            if (status == 1) {
                if(related_examinations.equals("1") &&  mapIntentData.get("item_status").equals("1") && type.equals("2")){}else {

                    if (mapGlobal.get("is_upload_img").equals("1")) {
                        //是否需要上传照片  1、需要   0、不需要
                        if (listPhoto.size() <= 0) {
                            Toast.makeText(context, "请上传相应图片", Toast.LENGTH_SHORT).show();
                            MyProgressBarDialogTools.hide();
                            return;
                        }
                    }
                }

                //处理form_config
                for (int i = 0; i < formList.size(); i++) {
                    if (formList.get(i).get("form_id").equals(form_id)) {
                        JSONObject formConfigObject = new JSONObject();
                        formConfigObject.put("form_id", formList.get(i).get("form_id"));
                        formConfigObject.put("title", formList.get(i).get("title"));
                        formConfigArray.put(formConfigObject);
                    }
                }
                formObject.put(mapIntentData.get("item_id").toString(), formConfigArray);

                //处理content
                String model_text=mapGlobal.get("deduct_marks_account");

                for (int i = 0; i < listQuestion.size(); i++) {
                    if (listQuestion.get(i).getDataType().equals("title")) {
                        JSONObject formArrayObject = new JSONObject();
                        formArrayObject.put("name", listQuestion.get(i).getQuestionContent());
                        JSONArray formArrayItemArray = new JSONArray();
                        for (int j = 0; j < listQuestion.size(); j++) {
                            if (listQuestion.get(i).getSerialNumber().equals(listQuestion.get(j).getSerialNumber()) && listQuestion.get(j).getDataType().equals("content")) {
                                if(model_text.contains("0")){
                                   //do 暂不做任何处理
                                }else if(model_text.equals("1")){
                                    if (Integer.parseInt(listQuestion.get(j).getDeductionScore()) > 0 && (listQuestion.get(j).getDeductionReason()!=null && listQuestion.get(j).getDeductionReason().trim().length()<1) && listQuestion.get(j).getMarking().equals("deduct_marks")&&listQuestion.get(j).getDeduct_marks_account().equals("1")) {
                                        Toast.makeText(context, "请完善扣分原因", Toast.LENGTH_SHORT).show();
                                        MyProgressBarDialogTools.hide();
                                        return;
                                    }
                                }
                                JSONObject formArrayItemObject = new JSONObject();
                                formArrayItemObject.put("name", listQuestion.get(j).getQuestionContent());
                                formArrayItemObject.put("grade", listQuestion.get(j).getStandardScore());
                                formArrayItemObject.put("score", listQuestion.get(j).getDeductionScore());
                                formArrayItemObject.put("reason", listQuestion.get(j).getDeductionReason());
                                formArrayItemArray.put(formArrayItemObject);
                            }
                        }
                        formArrayObject.put("config", formArrayItemArray);
                        formArray.put(formArrayObject);
                    }
                }
                formInfoObject.put("item", formArray);
                formInfoObject.put("score", etScore.getText().toString());
                formInfoObject.put("enter_time", df.format(System.currentTimeMillis()));
                formInfoObject.put("examiner", etExaminer.getText().toString());
                formInfoObject.remove("files_text");
                formInfoObject.remove("img_text");

                if(related_examinations.equals("1") &&  mapIntentData.get("item_status").equals("1") && type.equals("2")){

                    switch (theoretical_examination_type){
                        case "0":
                            formInfoObject.put("is_no_join", theoretical_examination_is_no_join);
                            break;
                        case "1":
                            formInfoObject.put("exam_user_id", theoretical_examination_eid);
                            break;
                        case "2":
                            formInfoObject.put("is_no_exam", theoretical_examination_is_no_exam);
                            break;
                    }
                }

                contentObject.put(form_id, formInfoObject);


            }


            JSONObject dataObj = new JSONObject();
            dataObj.put("details_id", mapIntentData.get("details_id"));
            dataObj.put("score", etScore.getText().toString());
            dataObj.put("status", "1");  //接口规定写死为1，因为缺考和不考核状态不会进入该页面，不会有其他值
            dataObj.put("item_config", itemConfigArray);
            if (status == 1) {
                dataObj.put("form_config", formObject);
                dataObj.put("content", contentObject);
            }
            LogUtil.e(getLocalClassName(), "sendGraduateExamDetail: 修改后参数:" + dataObj.toString());

            final JSONArray finalItemConfigArray = itemConfigArray;
            final JSONObject finalFormConfigArray = formObject;
            final JSONObject finalContentObject = contentObject;
            final JSONObject finalDataObj = dataObj;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("act", URLConfig.leaveKsNewEnterGrade);
                        obj.put("uid", SharedPreferencesTools.getUid(context));
                        obj.put("fid", mapIntentData.get("fid"));
                        /*obj.put("details_id", mapIntentData.get("details_id"));
                        obj.put("score", etScore.getText().toString());
                        obj.put("status", "1");  //接口规定写死为1，因为缺考和不考核状态不会进入该页面，不会有其他值
                        obj.put("item_config", finalItemConfigArray);
                        obj.put("form_config", finalFormConfigArray);
                        obj.put("content", finalContentObject);*/
                        obj.put("subdata", finalDataObj);
                        LogUtil.e("规培出科考核详细信息数据参数：", obj.toString());
                        String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                        LogUtil.e("规培出科考核详细信息数据：", result);

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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setTotalScore(int totalScore) {
        etScore.setText(totalScore + "");
    }

    public int getTotalScore() {
        return Integer.parseInt(etScore.getText().toString());
    }

    public void addExamPic(View view) {
        FunctionConfig.Builder functionConfigBuilder = new FunctionConfig.Builder();
//        functionConfigBuilder.setSelected(listPhoto);//添加过滤集合
        functionConfigBuilder.setMutiSelectMaxSize(10);
//        functionConfigBuilder.setEnableCamera(true);
        final FunctionConfig functionConfig = functionConfigBuilder.build();
        ImageLoader imageLoader = new GlideImageLoader();
        ThemeConfig themeConfig = ThemeConfig.DEFAULT;
        CoreConfig coreConfig = new CoreConfig.Builder(context, imageLoader, themeConfig)
                .setFunctionConfig(functionConfig).build();
        GalleryFinal.init(coreConfig);
        GalleryFinal.openGalleryMuti(1000, functionConfig, mOnHanlderResultCallback);
    }

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null && reqeustCode == 1000) {
//                listPhoto.clear();
//                listPhoto.addAll(resultList);
//                graduateExamPicGridViewAdapter.notifyDataSetChanged();
                uploadCasePicture(resultList);
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

    private void uploadCasePicture(List<PhotoInfo> resultList) {
        int pictureCount = 0;
        postRequest = OkGo.<String>post(URLConfig.CCMTVAPP_GpApi);

        for (int i = 0; i < resultList.size(); i++) {
            //key+i为上传的参数，后面为图片路径
            postRequest.params("file_" + i,
                    new File(resultList.get(i).getPhotoPath()));
            pictureCount++;
        }
        OkUploadCase(pictureCount, resultList);
    }

    private void OkUploadCase(int pictureCount, List<PhotoInfo> resultList) {
        MyProgressBarDialogTools.show(context);
        UploadModel uploadModel = new UploadModel();
        uploadModel.setType("gp");
        uploadModel.setPicCount(pictureCount);
        uploadModel.setIconUrl(resultList.get(0).getPhotoPath());
        JSONObject object = new JSONObject();
        try {
            object.put("act", URLConfig.uploadImg);
            object.put("uid", SharedPreferencesTools.getUid(context));
            object.put("gp_uid", mapIntentData.get("gp_uid").toString());
            object.put("standard_kid", mapIntentData.get("standard_kid").toString());
            object.put("form_id", form_id);
            object.put("details_id", mapIntentData.get("details_id").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        postRequest.params("data", Base64utils.getBase64(Base64utils.getBase64(object.toString())))
                .converter(new Converter<String>() {
                    @Override
                    public String convertResponse(Response response) throws Throwable {
                        try {
                            String responseString = Base64utils.getFromBase64(Base64utils.getFromBase64(response.body().string()));
//                            Log.e("规培出科上传图片返回", "convertResponse: " + responseString);
                            Message message = new Message();
                            message.what = 4;
                            message.obj = responseString;
                            handler.sendMessage(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                            handler.sendEmptyMessage(500);
                        }
                        return null;
                    }
                });
        HttpParams params = postRequest.getParams();
//        Log.e("上传参数", "OkUploadCase: 参数：" + params.toString());
        UploadTask<String> task = OkUpload.request("上传图片" + System.currentTimeMillis(), postRequest)
                .priority(35)
                .extra1(uploadModel)
                .extra2("规培上传图片")
                .save()
                .register(null);
        task.start();
    }

    public void deletePic(final int position) {
        final String picName = listPhoto.get(position).getPhotoName();
//        Log.e("删除图片后数组大小", "deletePic: 集合大小：" + listPhoto.size());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.deleteImg);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("form_id", form_id);
                    obj.put("details_id", mapIntentData.get("details_id").toString());
                    obj.put("filename", picName);
                    final String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("上传删除图片返回信息数据：", result);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject resultObject = new JSONObject(result);
                                if (resultObject.getString("code").equals("200")) {
                                    JSONObject dataObject = resultObject.getJSONObject("data");
                                    if (dataObject.getInt("status") == 1) { //成功
                                        listPhoto.remove(position);
                                        graduateExamPicGridViewAdapter.notifyDataSetChanged();
                                        Toast.makeText(context, dataObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, dataObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(context, resultObject.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                handler.sendEmptyMessage(500);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    public static void showDatePickerDialog(Activity activity, final EditText et, Calendar calendar) {
        new DatePickerDialog(activity, R.style.time_dialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;
                        String monthString = "";
                        String dayString = "";
                        if (dayOfMonth < 10) {
                            dayString = "0" + dayOfMonth;
                        } else {
                            dayString = "" + dayOfMonth;
                        }
                        if (month < 10) {
                            monthString = "0" + month;
                        } else {
                            monthString = "" + month;
                        }
                        et.setText(year + "/" + monthString + "/" + dayString);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void back(View view) {
        finish();
    }
}
