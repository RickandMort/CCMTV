package com.linlic.ccmtv.yx.activity.rules_to_compensate.gp_exam;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.periodical_exam.StuPeriodicalExamActivity;
import com.linlic.ccmtv.yx.adapter.BaseRecyclerViewAdapter;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.JsonUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.ToastUtils;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

//蔡超玄 2019-5-13
public class GradeListActivity extends BaseActivity {
    @Bind(R.id.arrow_back)
    LinearLayout arrowBack;
    @Bind(R.id.activity_title_name)
    TextView activityTitleName;
    @Bind(R.id.tv_type)
    TextView tvType;
    @Bind(R.id.tv_exam_type)
    TextView tvExamType;
    @Bind(R.id.tv_total_score)
    TextView tvTotalScore;
    @Bind(R.id.tv_average_score)
    TextView tvAverageScore;
    @Bind(R.id.grade_recyclerview)
    RecyclerView gradeRecyclerview;
    @Bind(R.id.tv_status)
    TextView tvStatus;
    @Bind(R.id.layout_nodata)
    NodataEmptyLayout layoutNodata;
    private Context context;
    private String fid = "";
    private String exam_id = "";
    private String score_id = "";
    private List<ListInfo> list_data = new ArrayList<>();
    private BaseRecyclerViewAdapter adapter;
    private List<Map<String, String>> examinerList = new ArrayList<>();
    private String is_multi = "";
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
                                JSONObject jsonObject = data.getJSONObject("data");
                                JSONObject exam_info=jsonObject.getJSONObject("exam_info");
                                tvTotalScore.setText(jsonObject.getString("s_score"));
                                tvAverageScore.setText(jsonObject.getString("v_score"));
                                tvType.setText(exam_info.getString("exam_name"));
                                tvStatus.setText(exam_info.getString("status_n"));
                                tvExamType.setText(exam_info.getString("type_name"));
                                JSONArray jsonArray = jsonObject.getJSONArray("score_list");
                                if (jsonArray.length() == 0) {
                                    layoutNodata.setVisibility(View.VISIBLE);
                                    gradeRecyclerview.setVisibility(View.GONE);
                                }
                                if (jsonArray.length() != 0) {
                                    layoutNodata.setVisibility(View.GONE);
                                    gradeRecyclerview.setVisibility(View.VISIBLE);
                                    list_data = JsonUtils.fromJsonArray(jsonArray.toString(), ListInfo.class);
                                    if (list_data.size() != 0) {
                                        adapter = new BaseRecyclerViewAdapter(R.layout.adapter_single_grade_list, list_data) {
                                            @Override
                                            public void convert(BaseViewHolder helper, Object item) {
                                                super.convert(helper, item);
                                                ListInfo data = (ListInfo) item;
                                                helper.setText(R.id.tv_name, data.station_name);
                                                TextView tv_score=helper.getView(R.id.tv_score);
                                                String detail_id=data.detail_id;
                                                if(detail_id.equals("0")){
                                                    tv_score.setText(data.score+"分");
                                                    tv_score.setTextColor(context.getResources().getColor(R.color.text2));
                                                } else {
                                                    tv_score.setText(data.score+"分>");
                                                    tv_score.setTextColor(context.getResources().getColor(R.color.color3897f9));
                                                }
                                            }
                                        };
                                        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
                                        adapter.isFirstOnly(false);
                                        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                                        gradeRecyclerview.setLayoutManager(layoutManager);
                                        gradeRecyclerview.setAdapter(adapter);
                                        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                                String detail_id=list_data.get(position).detail_id;
                                                if(detail_id.equals("0")){
                                                    ToastUtils.makeText(context,list_data.get(position).detail_msg);
                                                } else {
                                                    score_id=list_data.get(position).score_id;
                                                    initExaminerData(score_id);
                                                }
                                            }
                                        });
                                    }
                                    adapter.notifyDataSetChanged();
                                }
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
                                is_multi = infoData.getString("is_multi");
                                if (infoData.has("examiner_list")) {
                                    examinerList.clear();
                                    JSONArray array = infoData.getJSONArray("examiner_list");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject dataObject = array.getJSONObject(i);
//                                        if (i == 0) {
//                                            //examiner_id = dataObject.getString("id");
//                                            //initData();
//                                        }
                                        Map<String, String> map = new HashMap<>();
                                        map.put("name", dataObject.getString("name"));
                                        map.put("id", dataObject.getString("id"));
                                        examinerList.add(map);
                                    }
                                    Intent intent = new Intent(context, StuPeriodicalExamActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("examinerList",(Serializable)examinerList);
                                    bundle.putString("fid",fid);
                                    bundle.putString("is_multi",is_multi);
                                    if(!score_id.equals("")){
                                        bundle.putString("score_id",score_id);
                                    }
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                                //stuPeriodicalExaminerAdapter.notifyDataSetChanged();
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
        setContentView(R.layout.activity_grade_list);
        ButterKnife.bind(this);
        context = GradeListActivity.this;
        getIntentData();
    }

    private void getIntentData() {
        fid = getIntent().getStringExtra("fid");
        exam_id = getIntent().getStringExtra("exam_id");
    }

    @Override
    protected void onResume() {
        super.onResume();
        initdata();
    }

    private void initdata() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getStageUserScoreList);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("fid", fid);
                    obj.put("exam_id", exam_id);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("单多站考核学生成绩单：", result);

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

    private void initExaminerData(final String score_id){
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

    class ListInfo implements Serializable {
        private String score_id;
        private String detail_id;
        private String score;
        private String manage_id;
        private String station_name;
        private String detail_msg;
    }

}
