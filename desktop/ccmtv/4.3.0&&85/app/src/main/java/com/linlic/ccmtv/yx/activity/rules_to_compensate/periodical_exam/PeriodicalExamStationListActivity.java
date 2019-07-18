package com.linlic.ccmtv.yx.activity.rules_to_compensate.periodical_exam;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
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
 * 规培 阶段性考核 考站列表
 */
public class PeriodicalExamStationListActivity extends AppCompatActivity {

    private Context context;
    private TextView title_name;
    private ListView lvExamStation;
    private List<Map<String, String>> examStationList = new ArrayList<>();
    private BaseListAdapter baseListAdapter;
    private String fid = "";
    private String detail_id = "";

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
                                JSONArray dataArray = dataObject.getJSONArray("list");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObjectDetail = dataArray.getJSONObject(i);
                                    Map<String, String> map = new HashMap<>();
                                    map.put("id", dataObjectDetail.getString("id"));
                                    map.put("station_name", dataObjectDetail.getString("station_name"));//考站
                                    map.put("point_name", dataObjectDetail.getString("point_name"));//考点
                                    map.put("s_time", dataObjectDetail.getString("s_time"));
                                    map.put("e_time", dataObjectDetail.getString("e_time"));
                                    map.put("site_name", dataObjectDetail.getString("site_name"));  //地点
                                    examStationList.add(map);
                                }
                                baseListAdapter.notifyDataSetChanged();
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
        setContentView(R.layout.activity_periodical_exam_station_list);

        context = this;
        title_name = findViewById(R.id.activity_title_name);
        lvExamStation = findViewById(R.id.id_lv_periodical_exam_station);

        title_name.setText("2018阶段性考核");
        getIntentData();
        iniData();
        initListView();
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

    private void getIntentData() {
        try {
            fid = getIntent().getStringExtra("fid");
            detail_id = getIntent().getStringExtra("detail_id");
//            Log.e(getLocalClassName(), "initData: fid:" + fid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void iniData() {
        /*for (int i = 0; i < 6; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("stationName", "临床操作" + i);
            map.put("examTime", "2018-08-27 16:00:00");
            map.put("stationLocation", "门诊部二楼");
            map.put("examContent", "病例分析");
            examStationList.add(map);
        }*/

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.stageExaminerList);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("fid", fid);
                    obj.put("detail_id", detail_id);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("规培阶段性考核考站信息数据：", result);

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
        baseListAdapter = new BaseListAdapter(lvExamStation, examStationList, R.layout.item_gp_periodical_exam_station_list) {
            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map<String, String> map = (Map<String, String>) item;
                helper.setText(R.id.id_tv_exam_station, map.get("station_name").toString());
                helper.setText(R.id.id_tv_exam_station_time, map.get("s_time").toString() +"~"+ map.get("e_time").toString());
                helper.setText(R.id.id_tv_exam_station_location, map.get("site_name").toString());
                helper.setText(R.id.id_tv_exam_station_content, map.get("point_name").toString());
            }
        };

        lvExamStation.setAdapter(baseListAdapter);

        lvExamStation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(PeriodicalExamStationListActivity.this, PeriodicalExamStuListActivity.class);
                intent.putExtra("fid", fid);
                intent.putExtra("id", examStationList.get(i).get("id"));
                startActivity(intent);
            }
        });
    }

    public void back(View view) {
        finish();
    }
}
