package com.linlic.ccmtv.yx.activity.rules_to_compensate.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.CustomActivity;
import com.linlic.ccmtv.yx.activity.base.BaseFragment;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.evaluation.The_evaluation_of_teaching_list;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.TextHighLight;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/6/19.
 */

public class The_evaluation_of_teaching extends BaseFragment{
    JSONObject result, data;
    private ListView listView;
    private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
    private BaseListAdapter baseListAdapterVideo;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {

                        result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {// 成功

                        } else {                                                                        // 失败
                            Toast.makeText(getActivity(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.the_evaluation_of_teaching, container, false);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findById();
        inittextData();
        initView();
    }
    public void findById() {
        super.findId();
        listView = (ListView) getActivity().findViewById(R.id.listView);
    }

    public void initData(){
        listData.clear();
        inittextData();
        baseListAdapterVideo.notifyDataSetChanged();
    }

    public void inittextData(){
        for (int i = 0 ; i<5;i++){
            Map<String,Object> map = new HashMap<>();
            map.put("id",i);
            map.put("time","0"+i);
            map.put("content","2018年0"+i+"月评价带教");
            listData.add(map);
        }
    }

    public void initView(){
        baseListAdapterVideo = new BaseListAdapter(listView, listData, R.layout.item_the_evaluation_of_teaching) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map map = (Map) item;
                helper.setText(R.id._item_tiem,map.get("time").toString());
                helper.setText(R.id._item_text,map.get("content").toString());
                helper.setText(R.id._item_id,map.get("id").toString());
            }
        };
        listView.setAdapter(baseListAdapterVideo);
        // listview点击事件
        listView.setOnItemClickListener(new casesharing_listListener());
    }


    /**
     * name: 点击查看某个视频的详细 author:Tom 2016-1-28下午3:42:08
     */
    private class casesharing_listListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {
            Intent intent = new Intent(getContext(), The_evaluation_of_teaching_list.class);
            startActivity(intent);

        }

    }


    public void getUrlRulest() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.home_page);
                    if (SharedPreferencesTools.getUidONnull(getContext()).length() > 0) {
                        obj.put("uid", SharedPreferencesTools.getUidONnull(getContext()));
                    }
                    String result = HttpClientUtils.sendPost(getActivity(), URLConfig.CCMTVAPP1, obj.toString());
                    /*LogUtil.e("首页数据", result);*/

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
