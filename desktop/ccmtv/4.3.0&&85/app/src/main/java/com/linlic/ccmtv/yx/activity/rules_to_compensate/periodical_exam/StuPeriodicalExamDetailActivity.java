package com.linlic.ccmtv.yx.activity.rules_to_compensate.periodical_exam;

import android.content.Context;
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
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 规培 阶段性考核 学员考核详情界面
 */
public class StuPeriodicalExamDetailActivity extends BaseActivity {

    private Context context;
    private TextView title_name;
    private TextView tvExamName;
    private TextView tvExamType;
    private TextView tvExamTime;
    private ListView lvStuCheckDetail;
    private BaseListAdapter baseListAdapter;
    private List<Map<String, String>> stuCheckDetailList = new ArrayList<>();
    private String fid = "";
    private String exam_id = "";
    private Map<String, String> mapGlobal = new HashMap<>();
    private NodataEmptyLayout lt_nodata1;

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
                                JSONArray dataArray = dataObject.getJSONArray("station_list");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObjectDetail = dataArray.getJSONObject(i);
                                    Map<String, String> map = new HashMap<>();
                                    map.put("score_id", dataObjectDetail.getString("score_id"));
                                    map.put("score", dataObjectDetail.getString("score"));//分数
                                    map.put("station_id", dataObjectDetail.getString("station_id"));//考点
                                    map.put("station_name", dataObjectDetail.getString("station_name"));//考站名称
                                    map.put("s_time", dataObjectDetail.getString("s_time"));
                                    map.put("e_time", dataObjectDetail.getString("e_time"));
                                    map.put("point_name", dataObjectDetail.getString("point_name"));  //
                                    map.put("site_name", dataObjectDetail.getString("site_name"));  //地点
                                    map.put("check_score", dataObjectDetail.getString("check_score"));  //是否查看成绩    check_score（成绩）：0暂无，1查看
                                    map.put("status", dataObjectDetail.getString("status"));  //是否查看成绩    check_score（成绩）：0暂无，1查看
                                    stuCheckDetailList.add(map);
                                }
                                baseListAdapter.notifyDataSetChanged();

                                if (stuCheckDetailList.size() > 0) {
                                    lvStuCheckDetail.setVisibility(View.VISIBLE);
                                    lt_nodata1.setVisibility(View.GONE);
                                } else {
                                    lvStuCheckDetail.setVisibility(View.GONE);
                                    lt_nodata1.setVisibility(View.VISIBLE);
                                }

                                JSONObject examInfoObject = dataObject.getJSONObject("exam_info");
                                mapGlobal.put("exam_id", examInfoObject.getString("exam_id"));
                                mapGlobal.put("exam_name", examInfoObject.getString("exam_name"));
                                mapGlobal.put("s_time", examInfoObject.getString("s_time"));
                                mapGlobal.put("e_time", examInfoObject.getString("e_time"));
                                mapGlobal.put("check_score", examInfoObject.getString("check_score"));
                                mapGlobal.put("type_name", examInfoObject.getString("type_name"));
                                setViews();
                            } else {
                                Toast.makeText(context, data.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, result.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        MyProgressBarDialogTools.hide();
                        setResultStatus(stuCheckDetailList.size() > 0, result.getInt("code"));
                    } catch (Exception e) {
                        MyProgressBarDialogTools.hide();
                        e.printStackTrace();
                        setResultStatus(stuCheckDetailList.size() > 0, HttpClientUtils.UNKONW_EXCEPTION_CODE);
                    }
                    break;
                case 500:
                    MyProgressBarDialogTools.hide();
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    setResultStatus(stuCheckDetailList.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                    break;
                default:
                    break;
            }
        }
    };

    private void setResultStatus(boolean status, int code) {
        if (status) {
            lvStuCheckDetail.setVisibility(View.VISIBLE);
            lt_nodata1.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                lt_nodata1.setNetErrorIcon();
            } else {
                lt_nodata1.setLastEmptyIcon();
            }
            lvStuCheckDetail.setVisibility(View.GONE);
            lt_nodata1.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_periodical_exam_detail);
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
        tvExamName = findViewById(R.id.id_tv_exam_name);
        tvExamType = findViewById(R.id.id_tv_exam_type);
        tvExamTime = findViewById(R.id.id_tv_exam_time);
        lvStuCheckDetail = findViewById(R.id.id_lv_stu_check_periodical_exam_detail);
        lt_nodata1 = findViewById(R.id.lt_nodata1);
    }

    private void initData() {
        /*for (int i = 0; i < 6; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("title", "体格检查" + i);
            map.put("time", "2018-08-29 00:00:00~2018-08-30 00:00:00");
            map.put("location", "凌立健康" + i);
            map.put("content", "病史采集" + i);
            if (i % 2 != 0) {
                map.put("isHaveScore", "0");
                map.put("status", "进行中" + i);
            } else {
                map.put("isHaveScore", "1");
                map.put("score", "9" + i);
                map.put("status", "已结束" + i);
            }
            stuCheckDetailList.add(map);
        }*/

        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.stageUserExamInfo);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("fid", fid);
                    obj.put("exam_id", exam_id);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("规培阶段性考核学员成绩汇总信息数据：", result);

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
        baseListAdapter = new BaseListAdapter(lvStuCheckDetail, stuCheckDetailList, R.layout.item_gp_stu_check_periodical_exam_detail_list) {
            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map<String, String> map = (Map<String, String>) item;
                helper.setText(R.id.id_tv_exam_title, map.get("station_name").toString());
                helper.setText(R.id.id_tv_exam_time, map.get("s_time").toString() + "~" + map.get("e_time").toString());
                helper.setText(R.id.id_tv_exam_location, map.get("site_name").toString());
                helper.setText(R.id.id_tv_exam_content, map.get("point_name").toString());
                helper.setText(R.id.id_tv_exam_status, map.get("status").toString());
                if (map.get("check_score").equals("0")) {
                    helper.setVisibility(R.id.id_ll_exam_score, View.GONE);
                } else {
                    helper.setVisibility(R.id.id_ll_exam_score, View.VISIBLE);
                    helper.setText(R.id.id_tv_exam_score, map.get("score").toString());
                    helper.setStuCheckScoreClick(context, R.id.id_tv_exam_check_score, fid, map);
                }
            }
        };

        lvStuCheckDetail.setAdapter(baseListAdapter);

        lvStuCheckDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(context, "点击："+i, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setViews() {
        title_name.setText("考试详情");
        tvExamName.setText(mapGlobal.get("exam_name"));
        tvExamType.setText(mapGlobal.get("type_name"));
        tvExamTime.setText(mapGlobal.get("s_time") + "~" + mapGlobal.get("e_time"));
    }

    public void back(View view) {
        finish();
    }
}
