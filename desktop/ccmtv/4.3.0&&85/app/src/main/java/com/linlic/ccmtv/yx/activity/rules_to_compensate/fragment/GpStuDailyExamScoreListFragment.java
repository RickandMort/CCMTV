package com.linlic.ccmtv.yx.activity.rules_to_compensate.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.gp_exam.GpDailyExamActivity;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.gp_exam.GpStuExamScoreListActivity;
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
 * A simple {@link Fragment} subclass.
 */
public class GpStuDailyExamScoreListFragment extends Fragment {

    private Context context;
    private View view;
    private ListView lvDailyExamScoreList;
    private BaseListAdapter baseListAdapter;
    private List<Map<String, Object>> scoreReportDataList = new ArrayList<>();
    private String daily_exam_id = "";
    private String fid = "";
    private String studentStatus = "";

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
                                JSONObject dataList = data.getJSONObject("dataList");
                                studentStatus = dataList.getString("status");
                                JSONArray dataArray = dataList.getJSONArray("list");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject scoreObject = dataArray.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("fen_time", scoreObject.get("fen_time"));
                                    map.put("standard_name", scoreObject.get("standard_name"));
                                    map.put("score", scoreObject.get("score"));
                                    map.put("user_id", scoreObject.get("user_id"));
                                    map.put("daily_exam_id", scoreObject.get("daily_exam_id"));
                                    scoreReportDataList.add(map);
                                }

                                baseListAdapter.notifyDataSetChanged();
                            } else {
                                MyProgressBarDialogTools.hide();
                                Toast.makeText(context, data.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
//                        MyProgressBarDialogTools.hide();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    Toast.makeText(context
                            , R.string.post_hint1,
                            Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }
    };


    public GpStuDailyExamScoreListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_gp_student_daily_score_list, container, false);
        context = getActivity();
        daily_exam_id = getArguments().getString("daily_exam_id");
        fid = getArguments().getString("fid");
        lvDailyExamScoreList = (ListView) view.findViewById(R.id.id_lv_stu_daily_score_report_list);
        initListView();
        initData();
        return view;
    }

    private void initListView() {
        /*scoreReportDataList.clear();
        for (int i=0;i<10;i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("title", "title" + i);
            scoreReportDataList.add(map);
        }*/

        lvDailyExamScoreList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(context, GpDailyExamActivity.class);
                intent.putExtra("daily_exam_id", scoreReportDataList.get(position).get("daily_exam_id").toString());
                intent.putExtra("user_id", scoreReportDataList.get(position).get("user_id").toString());
                intent.putExtra("data_type", "2");  //1：当前月 2：以往月      学生没有编辑功能，此处只需传过去判断不能编辑，无论是当前月还是以往月
                intent.putExtra("status", studentStatus);
                intent.putExtra("fid", fid);
                intent.putExtra("identity", "student");
                startActivity(intent);
            }
        });

        baseListAdapter = new BaseListAdapter(lvDailyExamScoreList, scoreReportDataList, R.layout.item_gp_stu_daily_exam_score_report_list) {
            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                helper.setText(R.id.id_tv_item_gp_stu_daily_exam_score_time, ((Map<String, Object>) item).get("fen_time").toString());
                helper.setText(R.id.id_tv_item_gp_stu_daily_exam_score_department, ((Map<String, Object>) item).get("standard_name").toString());
                helper.setText(R.id.id_tv_item_gp_stu_daily_exam_score, ((Map<String, Object>) item).get("score").toString());
            }
        };

        lvDailyExamScoreList.setAdapter(baseListAdapter);
    }

    private void initData() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.dailyStaScoreInfo);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("id", daily_exam_id);
                    obj.put("fid", fid);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("规培日常考核学员成绩信息列表数据：", result);

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
}
