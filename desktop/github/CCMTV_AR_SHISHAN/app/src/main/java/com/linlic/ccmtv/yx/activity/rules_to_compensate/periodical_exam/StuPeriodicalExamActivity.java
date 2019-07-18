package com.linlic.ccmtv.yx.activity.rules_to_compensate.periodical_exam;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
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
import com.linlic.ccmtv.yx.activity.entity.UploadModel;
import com.linlic.ccmtv.yx.activity.my.GlideImageLoader;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.SpinerAdapter;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.SpinerPopWindow;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.adapter.StuPeriodicalExamPicGridViewAdapter;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.adapter.StuPeriodicalExaminerAdapter;
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
import java.util.ArrayList;
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
 * 规培 阶段性考核 考核详情界面
 */
public class StuPeriodicalExamActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private TextView title_name;
    private RadioGroup radioGroupStatus;
    private RadioButton rbNormal, rbAbsence;
    private RecyclerView recyclerViewExaminer;
    private ListView lvQuestions;
    private LinearLayout llAbsenceNonExam, llAbsenceNonExamFooter, llBtn;
    private TextView tvSubmit, tvCancel, tvSubmitFooter, tvCancelFooter;
    private LinearLayout llContent, llPicture;
    private ImageView ivAddPic;
    private LinearLayout llExaminedStuName, llTemplate, llScore, llExaminer, llExamDate, llEnteringType, llFormName;
    private EditText etScore, etAbsenceReason;
    private TextView tvExaminedStuName, tvEnteringType, tvBase, tvBigScore, tvFormName, tvAbsenceStuName, tvAbsenceBase;
    private NiceSpinner niceSpinnerTemplate;
    private GridView gvPic;
    private RelativeLayout rlSpinnerLayout;
    private TextView tvSpinnerValue;
    private String fid = "";
    private String site_detail_id = "";
    private String user_id = "";
    private String score_id = "";
    private String is_edit = "";
    private String is_multi = "";
    private String examiner_id = "";
    private List<String> formListSpinner = new ArrayList<>();
    private List<Map<String, String>> formList = new ArrayList<>();
    private Map<String, String> mapGlobal = new HashMap<>();
    private StuPeriodicalExamPicGridViewAdapter periodicalExamPicAdapter;
    private BaseListAdapter baseListAdapter;
    private StuPeriodicalExaminerAdapter stuPeriodicalExaminerAdapter;
    private List<Map<String, String>> examinerList = new ArrayList<>();
    private List<PhotoInfo> listPhoto = new ArrayList<>();
    private String form_id = "";
    private List<GraduateExamQuestion> listQuestion = new ArrayList<>();
    private boolean canEditable = false;
    private String disabled = "";
    private PostRequest<String> postRequest;
    private JSONObject formInfoObject;

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
                                JSONObject dataObject = data.getJSONObject("info");
                                /*JSONArray dataArray = dataObject.getJSONArray("from_list");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObjectDetail = dataArray.getJSONObject(i);
                                    Map<String, String> map = new HashMap<>();
                                    map.put("form_id", dataObjectDetail.getString("form_id"));
                                    map.put("name", dataObjectDetail.getString("name"));
                                    formListSpinner.add(dataObjectDetail.getString("name"));
                                    formList.add(map);
                                }*/
                                /**-------------user_info   处理开始-----------**/

                                JSONObject userInfoObject = dataObject.getJSONObject("user_info");
                                mapGlobal.put("base_name", userInfoObject.getString("base_name"));
                                mapGlobal.put("realname", userInfoObject.getString("realname"));
                                mapGlobal.put("status", userInfoObject.getString("status"));  //status：1正常，2缺考
                                mapGlobal.put("score_id", userInfoObject.getString("score_id"));
                                mapGlobal.put("score", userInfoObject.getString("score"));  //分数
                                mapGlobal.put("reason", userInfoObject.getString("reason"));  //分数
                                if (userInfoObject.getString("status").equals("1")) {
                                    mapGlobal.put("form_id", userInfoObject.getString("form_id"));  //分数

                                    if (userInfoObject.has("img")) {
                                        listPhoto.clear();
                                        JSONArray picUrlArray = userInfoObject.getJSONArray("img");
                                        for (int i = 0; i < picUrlArray.length(); i++) {
                                            JSONObject picUrlObject = picUrlArray.getJSONObject(i);
                                            PhotoInfo photoInfo = new PhotoInfo();
                                            photoInfo.setPhotoPath(picUrlObject.getString("imgurl"));
                                            photoInfo.setPhotoName(picUrlObject.getString("imgpath"));
                                            listPhoto.add(photoInfo);
                                        }
                                    }
                                    periodicalExamPicAdapter.setDisabled(disabled);
                                }
                                setViews();


                                /**-------------user_info   处理完毕-----------**/

                                /**-------------form_info   处理开始-----------**/
                                if (userInfoObject.getString("status").equals("1")) {
                                    formInfoObject = dataObject.getJSONObject("form_info");
                                    mapGlobal.put("is_upload_img", formInfoObject.getString("is_upload_img"));  //是否需要上传照片  1、需要   0、不需要
                                    mapGlobal.put("entering", formInfoObject.getString("entering"));  //form表单录入 grade分数录入
                                    mapGlobal.put("marking", formInfoObject.getString("marking"));  //deduct_marks扣分 score得分
                                    mapGlobal.put("name", formInfoObject.getString("name"));  //评分表

                                    if (formInfoObject.has("item")) {
                                        JSONArray formItemArray = formInfoObject.getJSONArray("item");
                                        for (int i = 0; i < formItemArray.length(); i++) {
                                            GraduateExamQuestion graduateExamQuestion = new GraduateExamQuestion();
                                            JSONObject formItemObject = formItemArray.getJSONObject(i);
                                            graduateExamQuestion.setQuestionContent(formItemObject.getString("name"));
                                            graduateExamQuestion.setDataType("title");
                                            graduateExamQuestion.setDisabled(disabled);
                                            graduateExamQuestion.setMarking(mapGlobal.get("marking"));
                                            graduateExamQuestion.setSerialNumber(i + "");
                                            listQuestion.add(graduateExamQuestion);
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
                                                        graduateExamQuestion1.setDeductionScore(configObject.getString("score"));
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
                                                    listQuestion.add(graduateExamQuestion1);
                                                }
                                            }
                                        }
                                    }
                                    baseListAdapter.notifyDataSetChanged();
                                    changeFormViews();
                                }
                                /**-------------form_info   处理完毕-----------**/

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
//                                disabled = dataObject.getString("disabled");   //是否可以编辑  1、不可编辑   0、可以编辑
                                formInfoObject = dataObject.getJSONObject("form_info");
                                mapGlobal.put("is_upload_img", formInfoObject.getString("is_upload_img"));  //是否需要上传照片  1、需要   0、不需要
                                mapGlobal.put("entering", formInfoObject.getString("entering"));  //form表单录入 grade分数录入
                                mapGlobal.put("marking", formInfoObject.getString("marking"));  //deduct_marks扣分 score得分
                                mapGlobal.put("name", formInfoObject.getString("name"));  //评分表

                                if (formInfoObject.has("item")) {
//                                    JSONArray formItemArray = new JSONArray(formInfoObject.getString("item"));
                                    JSONArray formItemArray = formInfoObject.getJSONArray("item");
                                    for (int i = 0; i < formItemArray.length(); i++) {
                                        GraduateExamQuestion graduateExamQuestion = new GraduateExamQuestion();
                                        JSONObject formItemObject = formItemArray.getJSONObject(i);
                                        graduateExamQuestion.setQuestionContent(formItemObject.getString("name"));
                                        graduateExamQuestion.setDataType("title");
                                        graduateExamQuestion.setDisabled(disabled);
                                        graduateExamQuestion.setMarking(mapGlobal.get("marking"));
                                        graduateExamQuestion.setSerialNumber(i + "");
                                        listQuestion.add(graduateExamQuestion);
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
                                                    graduateExamQuestion1.setDeductionScore(configObject.getString("score"));
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
                                                listQuestion.add(graduateExamQuestion1);
                                            }
                                        }
                                    }
                                }
                                baseListAdapter.notifyDataSetChanged();
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
                                periodicalExamPicAdapter.notifyDataSetChanged();
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
                case 5:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getString("code").equals("200")) {
                            JSONObject data = result.getJSONObject("data");
                            if (data.getInt("status") == 1) {
                                JSONObject infoData = data.getJSONObject("info");
                                //is_multi = infoData.getString("is_multi");
                                if (infoData.has("examiner_list")) {
                                    JSONArray array = infoData.getJSONArray("examiner_list");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject dataObject = array.getJSONObject(i);
                                        if (i == 0) {
                                            examiner_id = dataObject.getString("id");
                                            initData();
                                        }
                                        Map<String, String> map = new HashMap<>();
                                        map.put("name", dataObject.getString("name"));
                                        map.put("id", dataObject.getString("id"));
                                        examinerList.add(map);
                                    }
                                }
                                stuPeriodicalExaminerAdapter.notifyDataSetChanged();
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
        setContentView(R.layout.activity_stu_periodical_exam);
        context = this;

        getIntentData();
        findId();
        initRecyclerView();
        initViews();
        //initExaminerData();
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

    public void findId() {
        View headerView = LayoutInflater.from(context).inflate(R.layout.gp_periodical_exam_header_view, null);
        View footerView = LayoutInflater.from(context).inflate(R.layout.gp_periodical_exam_footer_view, null);

        title_name = findViewById(R.id.activity_title_name);
        radioGroupStatus = (RadioGroup) findViewById(R.id.id_radiogroup_gp_exam_status);
        rbNormal = (RadioButton) findViewById(R.id.id_rb_gp_exam_subject_status_normal);
        rbAbsence = (RadioButton) findViewById(R.id.id_rb_gp_exam_subject_status_absence);
        lvQuestions = findViewById(R.id.id_lv_periodical_exam_question_list);
        llAbsenceNonExam = (LinearLayout) findViewById(R.id.id_ll_gp_periodical_exam_absence_nonexam);
        llBtn = (LinearLayout) findViewById(R.id.id_ll_gp_periodical_exam_button_layout);
        tvSubmit = (TextView) findViewById(R.id.id_tv_gp_periodical_exam_submit);
        tvCancel = (TextView) findViewById(R.id.id_tv_gp_periodical_exam_cancel);
        tvAbsenceStuName = findViewById(R.id.id_tv_periodical_exam_absence_stu_name);
        tvAbsenceBase = findViewById(R.id.id_tv_periodical_exam_absence_base);
        etAbsenceReason = findViewById(R.id.id_et_periodical_exam_input_absence_reason);
        recyclerViewExaminer = (RecyclerView) findViewById(R.id.id_recyclerview_examiner);

        llAbsenceNonExamFooter = (LinearLayout) footerView.findViewById(R.id.id_ll_gp_periodical_exam_absence_nonexam_footer);
        tvSubmitFooter = (TextView) footerView.findViewById(R.id.id_tv_gp_periodical_exam_submit_footer);
        tvCancelFooter = (TextView) footerView.findViewById(R.id.id_tv_gp_periodical_exam_cancel_footer);

        llContent = (LinearLayout) headerView.findViewById(R.id.id_ll_periodical_exam_content);
        llPicture = (LinearLayout) headerView.findViewById(R.id.id_ll_periodical_exam_upload_picture);
        gvPic = (GridView) headerView.findViewById(R.id.id_gv_periodical_exam_pic);
        ivAddPic = (ImageView) headerView.findViewById(R.id.id_iv_periodical_exam_add_pic);
        llExaminedStuName = (LinearLayout) headerView.findViewById(R.id.id_ll_periodical_exam_stu_name);
        llTemplate = (LinearLayout) headerView.findViewById(R.id.id_ll_periodical_exam_template);
        llScore = (LinearLayout) headerView.findViewById(R.id.id_ll_periodical_exam_input_score);
//        llExaminer = (LinearLayout) headerView.findViewById(R.id.id_ll_periodical_exam_base);
//        llExamDate = (LinearLayout) headerView.findViewById(R.id.id_ll_periodical_exam_date);
        llEnteringType = (LinearLayout) headerView.findViewById(R.id.id_ll_periodical_exam_input_type);
//        llFormName = (LinearLayout) headerView.findViewById(R.id.id_ll_periodical_exam_score_table);
        etScore = (EditText) headerView.findViewById(R.id.id_et_periodical_exam_input_score);
        tvBase = (TextView) headerView.findViewById(R.id.id_tv_periodical_exam_base);
        tvBigScore = (TextView) headerView.findViewById(R.id.id_tv_periodical_exam_score);
//        etExamDate = (EditText) headerView.findViewById(R.id.id_et_periodical_exam_date);
        tvExaminedStuName = (TextView) headerView.findViewById(R.id.id_tv_periodical_exam_stu_name);
        tvEnteringType = (TextView) headerView.findViewById(R.id.id_tv_periodical_exam_input_type);
//        tvFormName = (TextView) headerView.findViewById(R.id.id_tv_periodical_exam_score_table);
//        niceSpinnerTemplate = (NiceSpinner) headerView.findViewById(R.id.id_spinner_periodical_exam_template);
        rlSpinnerLayout = (RelativeLayout) headerView.findViewById(R.id.rl_item_grade);
        tvSpinnerValue = (TextView) headerView.findViewById(R.id.tv_value_form);

        lvQuestions.addHeaderView(headerView);
        lvQuestions.addFooterView(footerView);

        title_name.setText("阶段性考核");

        tvSubmit.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        tvSubmitFooter.setOnClickListener(this);
        tvCancelFooter.setOnClickListener(this);

        radioGroupStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.id_rb_gp_exam_subject_status_normal:
                        lvQuestions.setVisibility(View.VISIBLE);
                        llAbsenceNonExam.setVisibility(View.GONE);
                        break;
                    case R.id.id_rb_gp_exam_subject_status_absence:
                        lvQuestions.setVisibility(View.GONE);
                        llAbsenceNonExam.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

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
                        tvBigScore.setText(editable.toString());
                    } else {
                        etScore.setText("");
                        Toast.makeText(context, "请输入范围在1-100之间的整数", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void initRecyclerView() {
        //创建LinearLayoutManager
        LinearLayoutManager manager = new LinearLayoutManager(this);
        //设置
        recyclerViewExaminer.setLayoutManager(manager);
        //设置为横向滑动
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //实例化适配器
        final ArrayList<Map<String, String>> list =((ArrayList<Map<String, String>>)getIntent().getExtras().getSerializable("examinerList"));
        LogUtil.e("hehehe",list.size()+"");
        stuPeriodicalExaminerAdapter = new StuPeriodicalExaminerAdapter(this, list);
        recyclerViewExaminer.setAdapter(stuPeriodicalExaminerAdapter);
        stuPeriodicalExaminerAdapter.notifyDataSetChanged();
        examiner_id=list.get(0).get("id");

        initData();
        stuPeriodicalExaminerAdapter.setOnItemClickListener(new StuPeriodicalExaminerAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                examiner_id = list.get(position).get("id");
                initData();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_tv_gp_periodical_exam_submit:
                if (rbNormal.isChecked()) {
                    //发送正常考试信息
                    sendGraduateExamDetail(1);
                } else {
                    //发送缺考信息
//                    Log.e(getLocalClassName(), "onClick: 缺考");
                    sendGraduateExamDetail(2);
                }
                break;
            case R.id.id_tv_gp_periodical_exam_cancel:
                finish();
                break;
            case R.id.id_tv_gp_periodical_exam_submit_footer:
                if (rbNormal.isChecked()) {
                    //发送正常考试信息
                    sendGraduateExamDetail(1);
                } else {
                    //发送缺考信息
//                    Log.e(getLocalClassName(), "onClick: 缺考：");
                    sendGraduateExamDetail(2);
                }
                break;
            case R.id.id_tv_gp_periodical_exam_cancel_footer:
                finish();
                break;
        }
    }

    private void getIntentData() {
        try {
            fid = getIntent().getExtras().getString("fid");
            /*site_detail_id = getIntent().getStringExtra("site_detail_id");
            user_id = getIntent().getStringExtra("user_id");*/
            score_id = getIntent().getStringExtra("score_id");
            is_multi = getIntent().getStringExtra("is_multi");
            disabled = "1";        //disabled:0打分，1查看
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initViews() {
        periodicalExamPicAdapter = new StuPeriodicalExamPicGridViewAdapter(this, listPhoto);
        gvPic.setAdapter(periodicalExamPicAdapter);

        canEditable = true;
        baseListAdapter = new BaseListAdapter(lvQuestions, listQuestion, R.layout.item_graduate_exam_questions) {

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
                    helper.setVisibility(R.id.id_line, View.VISIBLE);
                } else {
                    helper.setVisibility(R.id.id_line, View.GONE);
                    helper.setText(R.id.id_tv_questions_item_content, map.getQuestionContent());
                    helper.setVisibility(R.id.id_ll_item_graduate_exam_questions, View.VISIBLE);
                    helper.setVisibility(R.id.id_tv_questions_item_title, View.GONE);
//                    if (map.getStandardScore().isEmpty()) {
//                        //helper.setVisibility(R.id.id_ll_item_graduate_exam_questions_score, View.GONE);
//                        helper.setVisibility(R.id.id_ll_graduate_exam_item_deduction_reason, View.GONE);
//                    } else {
//                        //helper.setVisibility(R.id.id_ll_item_graduate_exam_questions_score, View.VISIBLE);
//                        helper.setText(R.id.id_tv_graduate_exam_questions_standard_score, "标准分：" + map.getStandardScore());
//                        if (map.getMarking().equals("deduct_marks")) {
//                            //helper.setText(R.id.id_tv_graduate_exam_questions_deduction, "扣分");
//                        } else {
//                            //helper.setText(R.id.id_tv_graduate_exam_questions_deduction, "得分");
//                        }
//                    }

                    helper.setTag(R.id.id_ll_graduate_exam_item_deduction_reason, position + "");
                    helper.setSpinnerGraduateExam(R.id._item_grade, etScore, listQuestion, map, canEditable, R.id.tv_value, R.id.id_ll_graduate_exam_item_deduction_reason, R.id.id_et_graduate_exam_item_deduction_reason);
//                    helper.setText(R.id.id_et_graduate_exam_item_deduction_reason, map.getDeductionReason());
                    helper.setFocusGraduateExamEdittext(R.id.id_et_graduate_exam_item_deduction_reason, map, listQuestion, R.id.id_ll_graduate_exam_item_deduction_reason);
                }
            }
        };

        lvQuestions.setAdapter(baseListAdapter);

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
                        tvSpinnerValue.setText(formList.get(position).get("name"));
                        getFormDetails(formList.get(position).get("form_id"));
                        form_id = formList.get(position).get("form_id");
                    }
                });

            }
        });
    }

    private void initData() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.stageUserScoreInfo);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("fid", fid);
                    obj.put("id", examiner_id);
                    obj.put("is_multi", is_multi);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("规培阶段性考核住院医师查看表单列表信息数据：", result);

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

    private void getFormDetails(final String form_id) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.stageExaminerDetailForm);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("fid", fid);
                    obj.put("form_id", form_id);
                    obj.put("score_id", score_id);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("规培阶段性考核详细表单信息数据：", result);

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

    private void initExaminerData() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.stageUserExaminerList);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("fid", fid);
                    obj.put("score_id", score_id);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("规培阶段性考核考官列表信息数据：", result);

                    Message message = new Message();
                    message.what = 5;
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

    private void sendGraduateExamDetail(final int status) {
        //  status为考核与缺考状态  1为正常，，2为缺考
        MyProgressBarDialogTools.show(context);
        try {
            //处理item_config
            /*JSONArray itemConfigArray = new JSONArray(mapIntentData.get("itemArray").toString());
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
            }*/


            JSONObject formObject = new JSONObject();
            JSONArray formConfigArray = new JSONArray();

            JSONArray formArray = new JSONArray();
            JSONObject contentObject = new JSONObject();

            if (status == 1) {
                if (mapGlobal.get("is_upload_img").equals("1")) {
                    //是否需要上传照片  1、需要   0、不需要
                    if (listPhoto.size() <= 0) {
                        Toast.makeText(context, "请上传相应图片", Toast.LENGTH_SHORT).show();
                        MyProgressBarDialogTools.hide();
                        return;
                    }
                }

                //处理form_config
                /*for (int i = 0; i < formList.size(); i++) {
                    if (formList.get(i).get("form_id").equals(form_id)) {
                        JSONObject formConfigObject = new JSONObject();
                        formConfigObject.put("form_id", formList.get(i).get("form_id"));
                        formConfigObject.put("title", formList.get(i).get("title"));
                        formConfigArray.put(formConfigObject);
                    }
                }
                formObject.put(mapIntentData.get("item_id").toString(), formConfigArray);*/

                //处理content


                for (int i = 0; i < listQuestion.size(); i++) {
                    if (listQuestion.get(i).getDataType().equals("title")) {
                        JSONObject formArrayObject = new JSONObject();
                        formArrayObject.put("name", listQuestion.get(i).getQuestionContent());
                        JSONArray formArrayItemArray = new JSONArray();
                        for (int j = 0; j < listQuestion.size(); j++) {
                            if (listQuestion.get(i).getSerialNumber().equals(listQuestion.get(j).getSerialNumber())
                                    && listQuestion.get(j).getDataType().equals("content")) {
                                if (Integer.parseInt(listQuestion.get(j).getDeductionScore()) > 0 && listQuestion.get(j).getDeductionReason().isEmpty() && listQuestion.get(j).getMarking().equals("deduct_marks")) {
                                    Toast.makeText(context, "请完善扣分原因", Toast.LENGTH_SHORT).show();
                                    MyProgressBarDialogTools.hide();
                                    return;
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
//                formInfoObject.put("item", formArray);
//                formInfoObject.put("score", etScore.getText().toString());

                contentObject.put(form_id, formInfoObject);
            }


            JSONObject dataObj = new JSONObject();
            dataObj.put("form_id", form_id);
//            dataObj.put("score", etScore.getText().toString());
            dataObj.put("status", status);
            dataObj.put("content", formArray);
            if (status == 2) {
                if (etAbsenceReason.getText().toString().isEmpty()) {
                    Toast.makeText(context, "请填写缺考理由", Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    return;
                } else {
                    dataObj.put("reason", etAbsenceReason.getText().toString());
                    dataObj.put("score", "0");
                }
            } else {
                dataObj.put("score", etScore.getText().toString());
            }
            LogUtil.e(getLocalClassName(), "stageExaminerUpDetail: 修改后参数:" + dataObj.toString());

            final JSONObject finalFormConfigArray = formObject;
            final JSONObject finalContentObject = contentObject;
            final JSONObject finalDataObj = dataObj;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("act", URLConfig.stageExaminerUpDetail);
                        obj.put("uid", SharedPreferencesTools.getUid(context));
                        obj.put("fid", fid);
                        obj.put("user_id", user_id);
                        obj.put("score_id", score_id);
                        obj.put("data", finalDataObj);
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

    private void setViews() {
//        niceSpinnerTemplate.attachDataSource(formListSpinner);
//        tvExaminedStuName.setText(mapGlobal.get("realname") + "(" + mapGlobal.get("base_name") + ")");
        tvExaminedStuName.setText(mapGlobal.get("realname"));
        tvBase.setText(mapGlobal.get("base_name"));
        tvAbsenceStuName.setText(mapGlobal.get("realname"));
        tvAbsenceBase.setText(mapGlobal.get("base_name"));
        if (formList.size() > 0 && mapGlobal.get("form_id").isEmpty()) {
            tvSpinnerValue.setText(formListSpinner.get(0));
            getFormDetails(formList.get(0).get("form_id"));
            form_id = formList.get(0).get("form_id");
        } else {
            for (int i = 0; i < formList.size(); i++) {
                if (mapGlobal.get("form_id").equals(formList.get(i).get("form_id"))) {
                    tvSpinnerValue.setText(formList.get(i).get("name"));
                    getFormDetails(formList.get(i).get("form_id"));
                    form_id = formList.get(i).get("form_id");
                }
            }
        }

        //是否可以编辑  1、不可编辑   0、可以编辑
        if (disabled.equals("0")) {
            rbNormal.setEnabled(true);
            rbAbsence.setEnabled(true);
            llAbsenceNonExamFooter.setVisibility(View.VISIBLE);
//            niceSpinnerTemplate.setEnabled(true);
            rlSpinnerLayout.setEnabled(true);
        } else {
            rbNormal.setEnabled(false);
            rbAbsence.setEnabled(false);
            llAbsenceNonExamFooter.setVisibility(View.GONE);
            llBtn.setVisibility(View.GONE);
//            niceSpinnerTemplate.setEnabled(false);
            rlSpinnerLayout.setEnabled(false);
        }

        //status 1正常 2缺考
        if (mapGlobal.get("status").equals("1")) {
            rbNormal.setChecked(true);
            rbAbsence.setChecked(false);
        } else {
            rbNormal.setChecked(false);
            rbAbsence.setChecked(true);
            etAbsenceReason.setEnabled(false);
            etAbsenceReason.setText(mapGlobal.get("reason"));
            llAbsenceNonExam.setVisibility(View.VISIBLE);
        }
    }

    private void changeFormViews() {
        etScore.setText(mapGlobal.get("score"));
        tvBigScore.setText(mapGlobal.get("score"));
//        tvFormName.setText(mapGlobal.get("name"));

        if (listPhoto.size() > 0) {
            llPicture.setVisibility(View.VISIBLE);
        } else {
            llPicture.setVisibility(View.GONE);
        }
        periodicalExamPicAdapter.notifyDataSetChanged();

        if (mapGlobal.get("entering").equals("form")) {  //form表单录入 grade分数录入
            tvEnteringType.setText("表单录入");
        } else {
            tvEnteringType.setText("分数录入");
        }

        if (disabled.equals("0")) {  //是否可以编辑  1、不可编辑   0、可以编辑
            etScore.setEnabled(true);
            if (mapGlobal.get("is_upload_img").equals("1")) {
                //是否需要上传照片  1、需要   0、不需要
                llPicture.setVisibility(View.VISIBLE);
            } else {
                llPicture.setVisibility(View.GONE);
            }

            if (mapGlobal.get("entering").equals("form")) {  //form表单录入 grade分数录入
                etScore.setEnabled(false);
            } else {
                listQuestion.clear();
                etScore.setEnabled(true);
//                graduateExamPicGridViewAdapter.notifyDataSetChanged();
            }

            if (mapGlobal.get("score").isEmpty() && !mapGlobal.get("entering").equals("grade")) {
                etScore.setText("100");
            } else {
                etScore.setText(mapGlobal.get("score"));
            }
        } else {
            etScore.setText(mapGlobal.get("score"));
            etScore.setEnabled(false);
            ivAddPic.setVisibility(View.GONE);
//            llAbsenceNonExam.setVisibility(View.GONE);
        }
    }

    public void deletePic(final int position) {
        final String picName = listPhoto.get(position).getPhotoName();
//        Log.e("删除图片后数组大小", "deletePic: 集合大小：" + listPhoto.size());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.stageExaminerImgdel);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("score_id", score_id);
//                    obj.put("details_id", mapIntentData.get("details_id").toString());
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
                                        periodicalExamPicAdapter.notifyDataSetChanged();
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
            object.put("act", URLConfig.stageExaminerImgup);
            object.put("uid", SharedPreferencesTools.getUid(context));
            object.put("fid", fid);
            object.put("score_id", score_id);
            object.put("user_id", user_id);
//            object.put("details_id", mapIntentData.get("details_id").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        postRequest.params("data", Base64utils.getBase64(Base64utils.getBase64(object.toString())))
                .converter(new Converter<String>() {
                    @Override
                    public String convertResponse(Response response) throws Throwable {
                        try {
                            String responseString = Base64utils.getFromBase64(Base64utils.getFromBase64(response.body().string()));
                            LogUtil.e("规培出科上传图片返回", "convertResponse: " + responseString);
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

    public void back(View view) {
        finish();
    }
}
