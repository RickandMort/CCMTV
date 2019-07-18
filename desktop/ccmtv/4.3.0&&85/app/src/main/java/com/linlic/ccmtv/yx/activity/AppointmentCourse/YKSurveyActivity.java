package com.linlic.ccmtv.yx.activity.AppointmentCourse;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.AppointmentCourse.entity.FromEntity;
import com.linlic.ccmtv.yx.activity.AppointmentCourse.entity.YKSurveyEntity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyListView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 约课调研表单
 * Created by bentley on 2019/1/10.
 */

public class YKSurveyActivity extends BaseActivity {
    @Bind(R.id.activity_title_name)
    TextView mTvToolbarTittle;
    @Bind(R.id.id_tv_daily_exam_score)
    TextView tvScore;
    @Bind(R.id.view_score_head)
    View view_score_head;
    @Bind(R.id.ed_socre)
    EditText ed_socre;
    @Bind(R.id.view_departline)
    View view_departline;
    @Bind(R.id.id_lv_daily_exam_question)
    ListView lvQuestion;
    //    @Bind(R.id.id_et_daily_exam_comments)
//    EditText etComments;
    @Bind(R.id.view_tip)
    View mTipView;
    @Bind(R.id.id_ll_daily_exam_button_layout)
    LinearLayout llButtonLayout;
    @Bind(R.id.id_tv_daily_exam_submit)
    TextView tvSubmit;

    private Context context;
    private String id;
    private BaseListAdapter baseListAdapter;
    private List<FromEntity.FromItem> fromList = new ArrayList<>();
    private List<FromEntity.Config> daily_exam_of_item_list = new ArrayList<>();//单个条目的configList
    private List<FromEntity.Config> allConfiglist = new ArrayList<>();//所有的configList
    private boolean canEditable;
    private boolean isAddScore;
    private boolean needReson;
    private boolean isGradeSubmit;//是否是grade分数录入
    private String fromId;//获取的表单id
    private MyListView configListView;
    private String fid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_yk_survey);
        context = this;
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id");
        fid = getIntent().getStringExtra("fid");
        initViews();
        getYkSurvey();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/Yueke.html";
        super.onPause();
    }

    private void initViews() {
        mTvToolbarTittle.setText("课后调研");

        baseListAdapter = new BaseListAdapter(lvQuestion, allConfiglist, R.layout.item_graduate_exam_questions2) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Log.d("ll",allConfiglist.toString());
                FromEntity.Config config = (FromEntity.Config) item;
                int position = baseListAdapter.getPosition();
                if (config.getDataType() != null && config.getDataType().equals("tittle")) {
                    helper.setText(R.id.id_tv_questions_item_title, config.getName());
                    helper.setVisibility(R.id.id_ll_item_graduate_exam_questions, View.GONE);
                    helper.setVisibility(R.id.id_tv_questions_item_title, View.VISIBLE);
                    helper.setTag(R.id.id_ll_graduate_exam_item_deduction_reason, position + "");
                    helper.setVisibility(R.id.id_line, View.VISIBLE);
                    helper.setVisibility(R.id.id_tv_graduate_exam_questions_standard_score, View.VISIBLE);
                } else {
                    helper.setVisibility(R.id.id_line, View.GONE);
                    helper.setText(R.id.id_tv_questions_item_content, config.getName());
                    helper.setVisibility(R.id.id_ll_item_graduate_exam_questions, View.VISIBLE);
                    helper.setVisibility(R.id.id_tv_questions_item_title, View.GONE);
//                    if (map.getStandardScore().isEmpty()) {
//                        helper.setVisibility(R.id.id_ll_item_graduate_exam_questions_score, View.GONE);
//                    } else {
//                        helper.setVisibility(R.id.id_ll_item_graduate_exam_questions_score, View.VISIBLE);
                        helper.setVisibility(R.id.id_tv_graduate_exam_questions_standard_score, View.INVISIBLE);
                        if (!isAddScore) {
                            helper.setText(R.id.id_tv_graduate_exam_questions_deduction, "扣分");
                        } else {
                            helper.setText(R.id.id_tv_graduate_exam_questions_deduction, "得分");
                        }
//                    }

                    helper.setTag(R.id.id_ll_graduate_exam_item_deduction_reason, position + "");
//                    helper.setSpinnerYKSurvey(R.id._item_grade, tvScore, allConfiglist, config,
//                            canEditable, R.id.tv_value, R.id.id_ll_graduate_exam_item_deduction_reason,
//                            R.id.id_et_graduate_exam_item_deduction_reason, needReson,isAddScore);
                    helper.setSpinnerYKExam(R.id._item_grade, tvScore, allConfiglist, config, canEditable, R.id.tv_value,
                            R.id.id_ll_graduate_exam_item_deduction_reason,
                            R.id.id_et_graduate_exam_item_deduction_reason, isAddScore, needReson);
                    helper.setFocusSurveyEdittext(R.id.id_et_graduate_exam_item_deduction_reason, allConfiglist);
                }
            }
        };
        lvQuestion.setAdapter(baseListAdapter);
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSubmit();
            }
        });
    }

    /**
     * 展示约课表单
     *
     * @param ykSurveyEntity
     */
    private void showYKFrom(YKSurveyEntity ykSurveyEntity) {
        fromId = ykSurveyEntity.getId();
        //是否为分数录入
        isGradeSubmit = "grade".equals(ykSurveyEntity.getForm().getEntering()) ? true : false;
        //1 不能编辑  0能编辑
        if ("0".equals(ykSurveyEntity.getIs_edit())) {
            canEditable = true;
            llButtonLayout.setVisibility(View.VISIBLE);
//            etComments.setEnabled(true);
            tvSubmit.setVisibility(View.VISIBLE);

            if (isGradeSubmit) {
                view_score_head.setVisibility(View.GONE);
                ed_socre.setVisibility(View.VISIBLE);
                view_departline.setVisibility(View.GONE);
            }
        } else {
            canEditable = false;
            llButtonLayout.setVisibility(View.GONE);
//            etComments.setEnabled(false);
            tvSubmit.setVisibility(View.GONE);
            if (isGradeSubmit) {
                view_departline.setVisibility(View.GONE);
            }
        }

        if ("score".equals(ykSurveyEntity.getForm().getMarking())) {
            //得分模式
            isAddScore = true;
        } else {
            //扣分模式
            isAddScore = false;
        }
        //扣分理由
        if ("1".equals(ykSurveyEntity.getForm().getDeduct_marks_account())) {
            //需要
            needReson = true;
        } else {
            needReson = false;
        }


        if (!TextUtils.isEmpty(ykSurveyEntity.getScore())) {
            tvScore.setText(ykSurveyEntity.getScore());
        } else {
            if (!isAddScore) {
                tvScore.setText("100");
            } else {
                tvScore.setText("0");
            }
        }
        baseListAdapter.notifyDataSetChanged();
    }

    /***
     * 获取约课调研表单
     */
    private void getYkSurvey() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.ykSurvey);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    obj.put("id", id);
                    obj.put("fid", fid);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());

                    LogUtil.e("约课调研表单", result);

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

    public void setSubmit() {
        if (!isGradeSubmit) {
            try {
                FromEntity fromEntity = new FromEntity();
                List<FromEntity.FromItem> submitFromList = new ArrayList<>();//fromArray
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < fromList.size(); i++) {
                    FromEntity.FromItem fromItem = fromList.get(i);
                    ArrayList<FromEntity.Config> configs = fromItem.getConfig();
                    for (int k = 0; k < configs.size(); k++){
                        if ("tittle".equals(configs.get(k).getDataType())){
                            configs.remove(configs.get(k));
                        }
                    }
                    FromEntity.FromItem submitFromItem = fromEntity.new FromItem();
                    JSONArray configArray = new JSONArray();
                    JSONObject objectFrom = new JSONObject();
                    submitFromItem.setName(fromItem.getName());
                    ArrayList<FromEntity.Config> submitConfigs = submitFromItem.getConfig();
                    objectFrom.put("name", fromItem.getName());
                    //configList
                    for (int j = 0; j < configs.size(); j++) {
                        FromEntity.Config config = configs.get(j);
                        JSONObject objectConfig = new JSONObject();
                        FromEntity.Config subMitConfig = fromEntity.new Config();
//                        for (int p = 0; p < allConfiglist.size(); p++){
//                            if ("tittle".equals(allConfiglist.get(p).getDataType())){
//                                allConfiglist.remove(allConfiglist.get(p));
//                            }
//                        }
//                        FromEntity.Config config = allConfiglist.get(j + i * (index - 1));
                        if (!"tittle".equals(config.getDataType())) {
                            subMitConfig.setScore(config.getScore());
                            subMitConfig.setGrade(config.getGrade());
                            subMitConfig.setName(config.getName());
                            objectConfig.put("score", config.getScore());
                            objectConfig.put("grade", config.getGrade());
                            objectConfig.put("name", config.getName());
                            if (needReson) {
                                if (config.isEditShow()) {
                                    if (!TextUtils.isEmpty(config.getEditTxt())) {
                                        subMitConfig.setDeduct_marks_account(config.getEditTxt());
//                            subMitConfig.setDeduct_marks_account(editTexts.get(j + i * configs.size()).getText() + "");
//                            objectConfig.put("deduct_marks_account", editTexts.get(j + i * configs.size()).getText() + "");
                                        objectConfig.put("deduct_marks_account", config.getEditTxt());
                                    } else {
                                        Toast.makeText(context, "请输入扣分理由", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            }
                            submitConfigs.add(subMitConfig);
                            configArray.put(objectConfig);
                        }
                    }
                    submitFromList.add(submitFromItem);
                    objectFrom.put("config", configArray);
                    jsonArray.put(objectFrom);
                }

                submit(jsonArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            submit(ed_socre.getText() + "");
        }
    }

    /**
     * 约课调研提交
     */
    public void submit(final JSONArray dataArray) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.ykSurveyScore);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("id", fromId);
                    obj.put("score", tvScore.getText());
                    obj.put("item", dataArray.toString());
                    obj.put("fid", fid);
                    LogUtil.e("约课调研提交信息数据：", obj.toString());
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("约课调研提交信息数据result：", result);

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

    public void submit(final String edScore) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.ykSurveyScore);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("id", fromId);
                    obj.put("score", edScore);
                    obj.put("item", true);
                    obj.put("fid", fid);
                    LogUtil.e("分数录入调研提交：", obj.toString());
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("分数录入提交信息数据result：", result);

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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1://调研表单
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            if (dataJson.getInt("status") == 1) {
                                // 成功  1存在数据 0不存在数据
                                JSONObject dateJson = dataJson.getJSONObject("data");
                                YKSurveyEntity ykSurveyEntity = new YKSurveyEntity();
                                ykSurveyEntity.setId(dateJson.getString("id"));
                                ykSurveyEntity.setIs_edit(dateJson.getString("is_edit"));
                                ykSurveyEntity.setScore(dateJson.getString("score"));
                                FromEntity fromEntity = new FromEntity();
                                if (!dateJson.isNull("form")) {
                                    JSONObject form = dateJson.getJSONObject("form");
                                    fromEntity.setEntering(form.getString("entering"));
                                    fromEntity.setMarking(form.getString("marking"));
                                    fromEntity.setDeduct_marks_account(form.getString("deduct_marks_account"));
                                    if (!"grade".equals(form.getString("entering"))) {
                                        if (!form.isNull("item")) {
                                            String jsonString = form.getString("item");
                                            JSONArray items = new JSONArray(jsonString);
//                                        JSONArray items = form.getJSONArray("item");
                                            ArrayList<FromEntity.FromItem> itemList = fromEntity.getItem();
                                            for (int i = 0; i < items.length(); i++) {
                                                JSONObject listObject = items.getJSONObject(i);
                                                FromEntity.FromItem fromItem = fromEntity.new FromItem();
                                                fromItem.setName(listObject.getString("name"));
                                                ArrayList<FromEntity.Config> configs = fromItem.getConfig();
                                                FromEntity.Config cFrom = fromEntity.new Config();
                                                cFrom.setName(listObject.getString("name"));
                                                cFrom.setDataType("tittle");
                                                configs.add(cFrom);
                                                JSONArray configArray = listObject.getJSONArray("config");
                                                for (int j = 0; j < configArray.length(); j++) {
                                                    JSONObject configArrayJSONObject = configArray.getJSONObject(j);
                                                    FromEntity.Config config = fromEntity.new Config();
                                                    config.setName(configArrayJSONObject.getString("name"));
                                                    config.setGrade(configArrayJSONObject.getString("grade"));
                                                    config.setItemName(fromItem.getName());
                                                    config.setId(j + i * items.length());
                                                    if ("0".equals(dateJson.getString("is_edit"))) {
                                                        if ("1".equals(form.getString("deduct_marks_account"))) {
//                                                            需要扣分理由
                                                            if ("score".equals(form.getString("marking"))){
                                                                //得分无扣分理由
                                                                config.setEditShow(false);
                                                            } else {
                                                                //扣分
                                                                if (!configArrayJSONObject.isNull("score")) {
                                                                    String score = configArrayJSONObject.getString("score");
                                                                    if (TextUtils.isEmpty(score)||"null".equals(score)){
                                                                        config.setEditShow(false);
                                                                    } else if ("0".equals(configArrayJSONObject.getString("grade"))){
                                                                        config.setEditShow(false);
                                                                    } else {
                                                                        config.setEditShow(true);
                                                                    }
                                                                }
                                                            }
                                                        } else {
                                                            config.setEditShow(false);
                                                        }
                                                    } else {
                                                        config.setEditShow(false);
                                                    }
                                                    if (!configArrayJSONObject.isNull("score")) {
                                                        config.setScore(configArrayJSONObject.getString("score"));
                                                    }
                                                    if (!configArrayJSONObject.isNull("deduct_marks_account")) {
                                                        config.setDeduct_marks_account(configArrayJSONObject.getString("deduct_marks_account"));
                                                    }
                                                    config.setDataType("content");
                                                    configs.add(config);
                                                }
                                                itemList.add(fromItem);
                                                fromList.add(fromItem);
                                                allConfiglist.addAll(fromItem.getConfig());
//                                            daily_exam_of_item_list.addAll(fromItem.getConfig());
                                            }
                                        }
                                    }
                                }
                                ykSurveyEntity.setForm(fromEntity);
                                showYKFrom(ykSurveyEntity);

                            } else {
                                Toast.makeText(context, dataJson.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "数据出了点问题", Toast.LENGTH_SHORT).show();
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            if (dataJson.getInt("status") == 1) {
                                Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(context, dataJson.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    break;
                default:
                    break;
            }
        }
    };
}
