package com.linlic.ccmtv.yx.activity.rules_to_compensate.periodical_exam;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 规培 阶段性考核 学员成绩详情界面
 */
public class StuPeriodicalExamScoreActivity extends BaseActivity {

    private Context context;
    private TextView title_name;
    private ListView lvStuCheckScore;
    private BaseListAdapter baseListAdapter;
    private List<Map<String, String>> stuCheckScoreList = new ArrayList<>();
    private String fid = "";
    private String exam_id = "";
    private String score_id = "";

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
                                JSONArray dataArray = dataObject.getJSONArray("score_list");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObjectDetail = dataArray.getJSONObject(i);
                                    Map<String, String> map = new HashMap<>();
                                    map.put("score_id", dataObjectDetail.getString("score_id"));
                                    map.put("score", dataObjectDetail.getString("score"));
                                    map.put("manage_id", dataObjectDetail.getString("manage_id"));
                                    map.put("station_name", dataObjectDetail.getString("station_name"));  //考站
                                    map.put("type", "0");  //type    0:考站    1：总分或平均分
                                    stuCheckScoreList.add(map);
                                }

                                if (dataObject.has("v_score")) {      //v_score；平均分
                                    Map<String, String> map = new HashMap<>();
                                    map.put("score_id", "");
                                    map.put("score", dataObject.getString("v_score"));
                                    map.put("manage_id", "");
                                    map.put("station_name", "平均分");
                                    map.put("type", "1");  //type    0:考站    1：总分或平均分
                                    stuCheckScoreList.add(map);
                                }

                                if (dataObject.has("s_score")) {      //s_score:总分
                                    Map<String, String> map = new HashMap<>();
                                    map.put("score_id", "");
                                    map.put("score", dataObject.getString("s_score"));
                                    map.put("manage_id", "");
                                    map.put("station_name", "总分");
                                    map.put("type", "1");  //type    0:考站    1：总分或平均分
                                    stuCheckScoreList.add(map);
                                }
                                baseListAdapter.notifyDataSetChanged();

                                /*if (stuCheckList.size()>0) {
                                    lvStuCheck.setVisibility(View.VISIBLE);
                                    lt_nodata1.setVisibility(View.GONE);
                                }else {
                                    lvStuCheck.setVisibility(View.GONE);
                                    lt_nodata1.setVisibility(View.VISIBLE);
                                }*/
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
                                if (infoData.has("examiner_list")) {
                                    JSONArray array = infoData.getJSONArray("examiner_list");
                                    if (array.length() > 0) {
                                        Intent intent = new Intent(StuPeriodicalExamScoreActivity.this, StuPeriodicalExamActivity.class);
                                        intent.putExtra("fid",fid);
                                        intent.putExtra("score_id", score_id);
                                        startActivity(intent);
                                    }
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
        setContentView(R.layout.activity_stu_periodical_exam_score);
        context = this;
        findId();
        getIntentData();
        initData();
        initListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/StageExaminer/index.html";
        super.onPause();
    }

    private void getIntentData() {
        try {
            fid = getIntent().getStringExtra("fid");
            exam_id = getIntent().getStringExtra("exam_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void findId() {
        title_name = findViewById(R.id.activity_title_name);
        lvStuCheckScore = findViewById(R.id.id_lv_stu_check_periodical_exam_score);

        title_name.setText("学员查看成绩");
    }

    private void initData() {
        /*for (int i = 0; i < 6; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("title", "综合知识" + i);
            map.put("score", "9" + i);
            if (i % 2 != 0) {
                map.put("type", "0");
            } else {
                map.put("type", "1");
            }
            stuCheckScoreList.add(map);
        }*/

        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.stageUserScoreList);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("fid", fid);
                    obj.put("exam_id", exam_id);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("规培阶段性考核学员成绩信息数据：", result);

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

    private void initListView() {
        baseListAdapter = new BaseListAdapter(lvStuCheckScore, stuCheckScoreList, R.layout.item_gp_stu_check_periodical_exam_score_list) {
            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map<String, String> map = (Map<String, String>) item;
                helper.setText(R.id.id_tv_exam_station_name, map.get("station_name").toString());
                helper.setText(R.id.id_tv_exam_score, map.get("score").toString()+"分");
                if (map.get("type").equals("0")) {
                    helper.setVisibility(R.id.id_iv_right_arrow, View.VISIBLE);
                    helper.setTextColor2(R.id.id_tv_exam_station_name, Color.parseColor("#000000"));
                    helper.setTextColor2(R.id.id_tv_exam_score, Color.parseColor("#000000"));
                } else {
                    helper.setVisibility(R.id.id_iv_right_arrow, View.INVISIBLE);
                    helper.setTextColor2(R.id.id_tv_exam_station_name, Color.parseColor("#3897f9"));
                    helper.setTextColor2(R.id.id_tv_exam_score, Color.parseColor("#3897f9"));
                }
            }
        };

        lvStuCheckScore.setAdapter(baseListAdapter);

        lvStuCheckScore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (stuCheckScoreList.get(i).get("type").equals("0")) {
                    score_id = stuCheckScoreList.get(i).get("score_id");
                    initExaminerData();
                }
            }
        });
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

    public void back(View view) {
        finish();
    }
}
