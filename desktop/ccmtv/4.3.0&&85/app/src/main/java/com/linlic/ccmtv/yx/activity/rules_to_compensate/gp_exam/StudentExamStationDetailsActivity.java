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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.periodical_exam.StuPeriodicalExamActivity;
import com.linlic.ccmtv.yx.adapter.BaseRecyclerViewAdapter;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.CircleImageView;
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
import butterknife.OnClick;

import static com.linlic.ccmtv.yx.R.id.tv_status;

/*
   蔡超玄
 */
public class StudentExamStationDetailsActivity extends BaseActivity {
    @Bind(R.id.layout_nodata)
    NodataEmptyLayout layoutNodata;
    @Bind(R.id.tv_grade)
    TextView tvGrade;
    private Context context;
    @Bind(R.id.arrow_back)
    LinearLayout arrowBack;
    @Bind(R.id.activity_title_name)
    TextView activityTitleName;
    @Bind(R.id.iv_img)
    CircleImageView ivImg;
    @Bind(R.id.tv_realname)
    TextView tvRealname;
    @Bind(R.id.tv_keshi)
    TextView tvKeshi;
    @Bind(R.id.stu_recyclerview)
    RecyclerView stuRecyclerview;
    private BaseRecyclerViewAdapter adapter;
    private List<ListInfo> list_data = new ArrayList<>();
    private String fid = "";
    private String exam_id = "";
    private String score_id = "";
    private String check_score = "-1";
    private String is_multi = "";
    private List<Map<String, String>> examinerList = new ArrayList<>();
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
                                JSONObject object = jsonObject.getJSONObject("user");
                                check_score=jsonObject.getString("check_score");
                                RequestOptions options = new RequestOptions().centerCrop();
                                Glide.with(context)
                                        .load(object.getString("IDphoto"))
                                        .apply(options)
                                        .into(ivImg);
                                tvRealname.setText(object.getString("realname"));
                               String name = object.getString("name");
                                if(name.equals("null")){
                                    tvKeshi.setText("基地：" + "无");
                                }else {
                                    tvKeshi.setText("基地：" + object.getString("name"));
                                }
                                JSONArray jsonArray = jsonObject.getJSONArray("station_list");
                                if (jsonArray.length() == 0) {
                                    layoutNodata.setVisibility(View.VISIBLE);
                                    stuRecyclerview.setVisibility(View.GONE);
                                }
                                if (jsonArray.length() != 0) {
                                    layoutNodata.setVisibility(View.GONE);
                                    stuRecyclerview.setVisibility(View.VISIBLE);

                                    list_data = JsonUtils.fromJsonArray(jsonArray.toString(), ListInfo.class);
                                    if (list_data.size() != 0) {
                                        adapter = new BaseRecyclerViewAdapter(R.layout.adapter_student_exam_station_details, list_data) {
                                            @Override
                                            public void convert(BaseViewHolder helper, Object item) {
                                                super.convert(helper, item);
                                                ListInfo data = (ListInfo) item;
                                                helper.setText(R.id.tv_name, data.station_name);
                                                helper.setText(tv_status, data.status);
                                                helper.setText(R.id.tv_address, data.site_name);
                                                helper.setText(R.id.tv_date, data.s_time + " 至 " + data.e_time);
                                                TextView tv_status=helper.getView(R.id.tv_status);
                                                if(check_score.equals("0")){//未开放
                                                  tv_status.setText(data.status);
                                                  tv_status.setTextColor(context.getResources().getColor(R.color.black99));
                                                }else if(check_score.equals("1")){//已开放
                                                  tv_status.setText(data.score+"分>");
                                                  tv_status.setTextColor(context.getResources().getColor(R.color.color3897f9));
                                                }
                                            }
                                        };
                                        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
                                        adapter.isFirstOnly(false);
                                        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                                        stuRecyclerview.setLayoutManager(layoutManager);
                                        stuRecyclerview.setAdapter(adapter);
                                        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                                String check_score=list_data.get(position).check_score;
                                                if(check_score.equals("0")){//未开放
                                                   ToastUtils.makeText(context,"成绩未开放");
                                                }else if(check_score.equals("1")){//已开放
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
        setContentView(R.layout.activity_student_exam_station_details);
        ButterKnife.bind(this);
        context = StudentExamStationDetailsActivity.this;
        getIntentData();
        initdata();
    }

    private void getIntentData() {
        activityTitleName.setText(getIntent().getStringExtra("title_name"));
        activityTitleName.setSelected(true);
        fid = getIntent().getStringExtra("fid");
        exam_id = getIntent().getStringExtra("exam_id");
    }

    private void initdata() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getStageUserExamInfo);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("fid", fid);
                    obj.put("exam_id", exam_id);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("单多站考核学生端详情列表数据：", result);

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

    @OnClick(R.id.tv_grade)
    public void onViewClicked() {
     if(check_score.equals("0")){//0成绩未开放，1开放
         ToastUtils.makeText(context,"成绩未开放");
     }else {
         Intent intent = new Intent(context,GradeListActivity.class);
         intent.putExtra("fid",fid);
         intent.putExtra("exam_id",exam_id);
         startActivity(intent);
     }
    }
    class ListInfo implements Serializable {
        private String score_id;
        private String score;
        private String station_id;
        private String station_name;
        private String s_time;
        private String e_time;
        private String point_name;
        private String site_name;
        private String check_score;
        private String status;
    }
}
