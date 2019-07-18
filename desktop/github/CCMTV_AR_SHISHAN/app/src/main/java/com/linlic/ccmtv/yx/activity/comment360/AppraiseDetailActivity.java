package com.linlic.ccmtv.yx.activity.comment360;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.comment360.entity.AppraiseFromEntity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 带教老师评估住院医师三：ShowDetail
 * Created by bentley on 2019/1/17.
 */

public class AppraiseDetailActivity extends BaseActivity {
    @Bind(R.id.activity_title_name)
    TextView mTvToolbarTittle;
    @Bind(R.id.id_tv_daily_exam_score)
    TextView tvScore;
    @Bind(R.id.id_tv_daily_exam_prompt)
    TextView tvPrompt;
    @Bind(R.id.id_lv_daily_exam_question)
    ListView lvQuestion;
    @Bind(R.id.id_et_daily_exam_comments)
    EditText etComments;
    @Bind(R.id.view_tip)
    View mTipView;
    @Bind(R.id.id_ll_daily_exam_button_layout)
    LinearLayout llButtonLayout;
    @Bind(R.id.id_tv_daily_exam_submit)
    TextView tvSubmit;
    @Bind(R.id.tv_tip)
    TextView tv_tip;
    @Bind(R.id.id_tv_daily_exam_cancel)
    TextView idTvDailyExamCancel;
    @Bind(R.id.tv_show)
    TextView tvShow;
    private String is_allow_edit;

    private Context context;
    private String fid;
    private String detail_id;
    private BaseListAdapter baseListAdapter;
    private List<AppraiseFromEntity.ContentBean> mContentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_yk_survey);
        context = this;
        ButterKnife.bind(this);
        fid = getIntent().getStringExtra("fid");
        detail_id = getIntent().getStringExtra("detail_id");
        initViews();
        getDetail();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/Allappraise/user_index.html";
        super.onPause();
    }

    private void initViews() {
        mTvToolbarTittle.setText("评价住院医师");
        tvShow.setVisibility(View.VISIBLE);
        tvShow.setText("满分100分");
        tv_tip.setText("评语：");
        mTipView.setVisibility(View.VISIBLE);

        baseListAdapter = new BaseListAdapter(lvQuestion, mContentList, R.layout.item_daily_exam_in_detail) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                AppraiseFromEntity.ContentBean map = (AppraiseFromEntity.ContentBean) item;
//                helper.setText(R.id._item_id, map.getId());
                helper.setText(R.id._item_content, map.getName());
                helper.setText(R.id._item_the_weight, "权重：" + map.getWeight());
                helper.setSpinnerAppraise(R.id._item_grade, tvScore, mContentList, map, R.id.tv_value, R.id.id_iv_spinner_arrow);
                if (is_allow_edit.equals("0")) {
                    helper.setClickable(R.id._item_grade, false);
                } else if (is_allow_edit.equals("1")) {
                    helper.setClickable(R.id._item_grade, true);
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

    public void setSubmit() {
        if (etComments.getText().length() <= 15) {
            Toast.makeText(context, "评语不能少于15字~", Toast.LENGTH_SHORT).show();
        } else {
            try {
                JSONObject dataObject = new JSONObject();
                dataObject.put("score", tvScore.getText());
                dataObject.put("detail_id", detail_id);
                dataObject.put("comment", etComments.getText());
                JSONArray itemArray = new JSONArray();
                for (AppraiseFromEntity.ContentBean item : mContentList) {
                    JSONObject itemObject = new JSONObject();
                    itemObject.put("name", item.getName());
                    itemObject.put("grade", item.getGrade());
                    itemObject.put("weight", item.getWeight());
                    itemObject.put("score", item.getScore());
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

    private void showAppraise(AppraiseFromEntity appraiseFromEntity) {
        if (!TextUtils.isEmpty(appraiseFromEntity.getScore())) {
            tvScore.setText(appraiseFromEntity.getScore());
        } else {
            tvScore.setText("100");
        }
        if (!TextUtils.isEmpty(appraiseFromEntity.getComment()) && (!"null".equals(appraiseFromEntity.getComment()))) {
            etComments.setText(appraiseFromEntity.getComment());
        } else {
            etComments.setHint("* 满分为100分；80分以下为差评。");
        }
        baseListAdapter.notifyDataSetChanged();

    }

    /***
     * 提交评价
     * @param dataObject
     */
    private void sendDailyStaAdd(final JSONObject dataObject) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.manageTeacherAppraiseUpDetail);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("fid", fid);
                    obj.put("data", dataObject.toString());
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());

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

    /***
     * 获取带教老师评估住院医师表单
     */
    private void getDetail() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.manageTeacherAppraiseShowDetail);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    obj.put("fid", fid);
                    obj.put("detail_id", detail_id);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());

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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            if (dataJson.getInt("status") == 1) {
                                // 成功  1存在数据 0不存在数据
                                JSONObject dateJson = dataJson.getJSONObject("data");
                                is_allow_edit = dateJson.getString("is_allow_edit");
                                if (is_allow_edit.equals("0")) {
                                    tvSubmit.setVisibility(View.GONE);
                                    idTvDailyExamCancel.setVisibility(View.GONE);
                                    etComments.setFocusable(false);
                                    etComments.setFocusableInTouchMode(false);
                                } else if (is_allow_edit.equals("1")) {
                                    tvSubmit.setVisibility(View.VISIBLE);
                                    idTvDailyExamCancel.setVisibility(View.VISIBLE);
                                    etComments.setFocusable(true);
                                    etComments.setFocusableInTouchMode(true);
                                }
                                AppraiseFromEntity appraiseFromEntity = new AppraiseFromEntity();
                                appraiseFromEntity.setId(dateJson.getString("id"));
                                appraiseFromEntity.setManage_id(dateJson.getString("manage_id"));
                                appraiseFromEntity.setUid(dateJson.getString("uid"));
                                appraiseFromEntity.setScore(dateJson.getString("score"));
                                appraiseFromEntity.setComment(dateJson.getString("comment"));
                                List<AppraiseFromEntity.ContentBean> contentList = appraiseFromEntity.getContent();

                                if (!dateJson.isNull("content")) {
                                    JSONArray contentJson = dateJson.getJSONArray("content");
                                    for (int i = 0; i < contentJson.length(); i++) {
                                        JSONObject contentObject = contentJson.getJSONObject(i);
                                        AppraiseFromEntity.ContentBean contentBean = new AppraiseFromEntity.ContentBean();
                                        if (!contentObject.isNull("score")) {
                                            contentBean.setScore(contentObject.getString("score"));
                                        }
                                        contentBean.setGrade(contentObject.getString("grade"));
                                        contentBean.setWeight(contentObject.getString("weight"));
                                        contentBean.setName(contentObject.getString("name"));
                                        contentList.add(contentBean);
                                    }
                                }
                                mContentList.addAll(contentList);
                                showAppraise(appraiseFromEntity);

                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
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

    @OnClick(R.id.id_tv_daily_exam_cancel)
    public void onViewClicked() {
        finish();
    }
}
