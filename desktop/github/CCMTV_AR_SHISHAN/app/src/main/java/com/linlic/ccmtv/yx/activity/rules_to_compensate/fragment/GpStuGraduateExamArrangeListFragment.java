package com.linlic.ccmtv.yx.activity.rules_to_compensate.fragment;


import android.content.Context;
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
 * A simple {@link Fragment} subclass.
 */
public class GpStuGraduateExamArrangeListFragment extends Fragment {

    private View view;
    private ListView lvArrangeList;
    private Context context;
    private BaseListAdapter baseListAdapter;
    private List<Map<String, Object>> arrangeDataList = new ArrayList<>();
    private String resultString = "";
    private String fid = "";
    private NodataEmptyLayout emptyView;

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


    public GpStuGraduateExamArrangeListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_gp_stu_graduate_exam_arrange_list, container, false);
        context = getActivity();
        lvArrangeList = (ListView) view.findViewById(R.id.id_lv_stu_graduate_arrange_list);
        emptyView = (NodataEmptyLayout) view.findViewById(R.id.entry_month_nodata);
        initListView();
        getIntentData();
        return view;
    }

    private void initListView() {
        /*arrangeDataList.clear();
        for (int i=0;i<50;i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("title", "title" + i);
            arrangeDataList.add(map);
        }*/

        baseListAdapter = new BaseListAdapter(lvArrangeList, arrangeDataList, R.layout.item_gp_stu_graduate_exam_arrange_list) {
            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                helper.setText(R.id.id_tv_item_gp_stu_graduate_exam_arrange_time, ((Map<String, Object>) item).get("exam_starttime").toString() + "~" + ((Map<String, Object>) item).get("exam_endtime").toString());
                helper.setText(R.id.id_tv_item_gp_stu_graduate_exam_arrange_location, ((Map<String, Object>) item).get("exam_place").toString());
            }
        };

        lvArrangeList.setAdapter(baseListAdapter);


        lvArrangeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    private void getIntentData() {
        MyProgressBarDialogTools.show(context);
        try {
            fid = getArguments().getString("fid");
            resultString = getArguments().getString("resultString");
            final JSONObject result = new JSONObject(resultString);
            if (result.getString("code").equals("200")) { // 成功
                arrangeDataList.clear();
                JSONObject data = result.getJSONObject("data");
                if (data.getInt("status") == 1) {
                    JSONArray dataList = data.getJSONArray("data");
                    for (int i = 0; i < dataList.length(); i++) {
                        JSONObject dataListObject = dataList.getJSONObject(i);
                        Map<String, Object> map = new HashMap<>();
                        map.put("exam_starttime", dataListObject.get("exam_starttime"));
                        map.put("exam_endtime", dataListObject.get("exam_endtime"));
                        map.put("exam_place", dataListObject.get("exam_place"));
                        arrangeDataList.add(map);
                    }
                    if (dataList.length()==0){
                        //无数据
                        setResultStatus(HttpClientUtils.UNKONW_EXCEPTION_CODE);
                    }
                    baseListAdapter.notifyDataSetChanged();
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

    private void initData() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.userlistDetail);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
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
        lvArrangeList.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }

}
