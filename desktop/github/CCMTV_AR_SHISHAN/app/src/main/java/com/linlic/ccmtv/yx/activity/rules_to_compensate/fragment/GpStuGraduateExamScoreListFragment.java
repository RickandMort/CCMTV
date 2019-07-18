package com.linlic.ccmtv.yx.activity.rules_to_compensate.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.gp_exam.GpExamSubjectActivity;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

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
public class GpStuGraduateExamScoreListFragment extends Fragment {

    private Context context;
    private View view;
    private ListView lvGraduateExamScoreList;
    private BaseListAdapter baseListAdapter;
    private List<Map<String, Object>> scoreReportDataList = new ArrayList<>();
    private String resultString = "";
    private String fid = "";
    private NodataEmptyLayout emptyView;

    public GpStuGraduateExamScoreListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_gp_stu_graduate_exam_score_list, container, false);
        context = getActivity();
        lvGraduateExamScoreList = (ListView) view.findViewById(R.id.id_lv_stu_graduate_score_report_list);
        emptyView = (NodataEmptyLayout) view.findViewById(R.id.entry_month_nodata);
        initListView();
        getIntentData();
        return view;
    }

    private void initListView() {
        /*scoreReportDataList.clear();
        for (int i=0;i<10;i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("title", "title" + i);
            scoreReportDataList.add(map);
        }*/

        baseListAdapter = new BaseListAdapter(lvGraduateExamScoreList, scoreReportDataList, R.layout.item_gp_stu_graduate_exam_score_report_list) {
            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                helper.setText(R.id.id_tv_item_gp_stu_graduate_exam_score_department, ((Map<String, Object>) item).get("standard_kname").toString());
                helper.setText(R.id.id_tv_item_gp_stu_graduate_exam_score, ((Map<String, Object>) item).get("score").toString());
            }
        };

        lvGraduateExamScoreList.setAdapter(baseListAdapter);

        lvGraduateExamScoreList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (scoreReportDataList.get(position).get("submit_status").equals("1")) {
                    Intent intent = new Intent(getActivity(), GpExamSubjectActivity.class);
                    intent.putExtra("fid", fid);
                    intent.putExtra("gp_uid", scoreReportDataList.get(position).get("gp_uid").toString());
                    intent.putExtra("standard_kid", scoreReportDataList.get(position).get("standard_kid").toString());
                    intent.putExtra("leave_ks_id", scoreReportDataList.get(position).get("leave_ks_id").toString());
                    intent.putExtra("disabled", scoreReportDataList.get(position).get("disabled").toString());       //1  不可编辑   0可以编辑
                    intent.putExtra("year", scoreReportDataList.get(position).get("year").toString());
                    intent.putExtra("month", scoreReportDataList.get(position).get("month").toString());
                    intent.putExtra("week", scoreReportDataList.get(position).get("week").toString());
                    intent.putExtra("pid","");
                    startActivity(intent);
                }else {
                    Toast.makeText(context, "成绩暂未公布，请稍后查看~",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getIntentData() {
        MyProgressBarDialogTools.show(context);
        try {
            fid = getArguments().getString("fid");
            resultString = getArguments().getString("resultString");
            Log.d("mason","    ----^^^--    "+resultString);
            final JSONObject result = new JSONObject(resultString);
            if (result.getString("code").equals("200")) { // 成功
                JSONObject data = result.getJSONObject("data");
                if (data.getInt("status") == 1) {
                    scoreReportDataList.clear();
                    JSONArray dataList = data.getJSONArray("data");
                    for (int i = 0; i < dataList.length(); i++) {
                        JSONObject dataListObject = dataList.getJSONObject(i);
                        Map<String, Object> map = new HashMap<>();
                        map.put("standard_kname", dataListObject.get("standard_kname"));
                        map.put("score", dataListObject.get("score"));
                        map.put("gp_uid", dataListObject.get("uid"));
                        map.put("standard_kid", dataListObject.get("standard_kid"));
                        map.put("leave_ks_id", dataListObject.get("leave_ks_id"));
                        map.put("submit_status", dataListObject.get("submit_status"));  //submit_status     0未走完，不允许查看详情     1走完，允许查看详情
                        map.put("year", dataListObject.get("year"));
                        map.put("month", dataListObject.get("month"));
                        map.put("week", dataListObject.get("week"));
                        map.put("disabled", dataListObject.get("disabled"));
                        scoreReportDataList.add(map);
                    }
                    if (dataList.length()==0){
                        //无数据
                        setResultStatus(HttpClientUtils.UNKONW_EXCEPTION_CODE);
                    }
                    baseListAdapter.notifyDataSetChanged();
//                    Log.e("出科成绩列表", "getIntentData: 刷新成绩列表");
                } else {
                    Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, result.getString("message"), Toast.LENGTH_SHORT).show();
            }
            MyProgressBarDialogTools.hide();
        } catch (Exception e) {
            MyProgressBarDialogTools.hide();
            setResultStatus(HttpClientUtils.UNKONW_EXCEPTION_CODE);
            e.printStackTrace();
        }
    }

    /**
     * 设置空界面
     * @param code
     */
    private void setResultStatus(int code) {
        if (HttpClientUtils.isNetConnectError(context, code)) {
            emptyView.setNetErrorIcon();
        } else {
            emptyView.setLastEmptyIcon();
        }
        lvGraduateExamScoreList.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }

}
