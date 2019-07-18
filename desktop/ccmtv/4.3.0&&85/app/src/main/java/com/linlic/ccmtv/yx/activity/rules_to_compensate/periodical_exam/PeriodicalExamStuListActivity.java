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
 * 规培 阶段性考核 学员列表
 */
public class PeriodicalExamStuListActivity extends AppCompatActivity {

    private Context context;
    private TextView title_name;
    private ListView lvExamStuListView;
    private List<Map<String, String>> examStuList = new ArrayList<>();
    private BaseListAdapter baseListAdapter;
    private String fid = "";
    private String id = "";
    private String site_detail_id = "";

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
                                examStuList.clear();
                                JSONObject dataObject = data.getJSONObject("data");
                                site_detail_id = dataObject.getString("site_detail_id");
                                JSONArray dataArray = dataObject.getJSONArray("users");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObjectDetail = dataArray.getJSONObject(i);
                                    Map<String, String> map = new HashMap<>();
                                    map.put("uid", dataObjectDetail.getString("uid"));
                                    map.put("realname", dataObjectDetail.getString("realname"));
                                    map.put("IDphoto", dataObjectDetail.getString("IDphoto"));
                                    map.put("score_id", dataObjectDetail.getString("score_id"));
                                    map.put("detail_id", dataObjectDetail.getString("detail_id"));
                                    map.put("disabled", dataObjectDetail.getString("disabled"));             //disabled:0打分，1查看
                                    map.put("is_end", dataObjectDetail.getString("is_end"));          //is_end：1，过期未打分；0，正常
                                    map.put("end_msg", dataObjectDetail.getString("end_msg"));             //end_msg ：过期未打分msg
                                    examStuList.add(map);
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
        setContentView(R.layout.activity_periodical_exam_stu_list);

        context = this;
        title_name = findViewById(R.id.activity_title_name);
        lvExamStuListView = findViewById(R.id.id_lv_periodical_exam_stu_list);

        title_name.setText("学员列表");
        getIntentData();
        initListView();
    }

    private void getIntentData() {
        try {
            fid = getIntent().getStringExtra("fid");
            id = getIntent().getStringExtra("id");
//            Log.e(getLocalClassName(), "initData: fid:" + fid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        iniData();
    }

    @Override
    protected void onPause() {
        //enterUrl = "http://yun.ccmtv.cn/admin.php/wx/StageExaminer/index.html";
        super.onPause();
    }

    private void iniData() {
        /*for (int i = 0; i < 6; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("stuName", "考生" + i);
            if (i % 2 != 0) {
                map.put("markType", "0");
            } else {
                map.put("markType", "1");
            }
            examStuList.add(map);
        }*/

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.stageExaminerUserList);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("fid", fid);
                    obj.put("id", id);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("规培阶段性考核学员列表信息数据：", result);

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
        baseListAdapter = new BaseListAdapter(lvExamStuListView, examStuList, R.layout.item_gp_periodical_exam_stu_list) {
            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map<String, String> map = (Map<String, String>) item;
                helper.setText(R.id.id_tv_stu_name, map.get("realname").toString());
                if (map.get("disabled").equals("0")) {
                    helper.setImageResource(R.id.id_iv_mark, R.mipmap.ic_periodical_exam_mark);
                } else {
                    helper.setImageResource(R.id.id_iv_mark, R.mipmap.ic_periodical_exam_check);
                }
                if (!map.get("IDphoto").toString().isEmpty()) {
                    helper.setCircleImageViewBitmapGlide2(context,R.id.id_iv_icon,map.get("IDphoto").toString());
                }
            }
        };

        lvExamStuListView.setAdapter(baseListAdapter);

        lvExamStuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (examStuList.get(i).get("is_end").equals("0")) {  //is_end：1，过期未打分；0，正常
                    Intent intent = new Intent(PeriodicalExamStuListActivity.this, PeriodicalExamActivity.class);
                    intent.putExtra("fid",fid);
                    intent.putExtra("site_detail_id",site_detail_id);
                    intent.putExtra("user_id", examStuList.get(i).get("uid"));
                    intent.putExtra("score_id", examStuList.get(i).get("score_id"));
                    intent.putExtra("disabled", examStuList.get(i).get("disabled"));
//                intent.putExtra("disabled", "0");
                    startActivity(intent);
                } else {
                    Toast.makeText(context, examStuList.get(i).get("end_msg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void back(View view) {
        finish();
    }
}
