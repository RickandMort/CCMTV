package com.linlic.ccmtv.yx.activity.home.apricotcup;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseLazyFragment;
import com.linlic.ccmtv.yx.activity.home.adapter.ApricotTopAdapter;
import com.linlic.ccmtv.yx.activity.home.willowcup.Type;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * name：杏林榜
 * author：Larry
 * data：2017/4/10.
 */
public class ApricotTopFragment extends BaseLazyFragment {
    private MyListView listview_apricottop;
    private boolean isInit = false;
    private ApricotTopAdapter apricotTopAdapter;
    Map<String, Object> map;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    Context context;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    data.clear();
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        if (jsonObject.getInt("status") == 1) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if (jsonArray.length() >= 10) {
                                for (int i = 0; i < 10; i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    map = new HashMap<String, Object>();
                                    map.put("title", object.getString("title"));        // 标题
                                    map.put("percent", object.getDouble("percent"));    //百分比
                                    map.put("num", object.getString("num"));            //榜单数量
                                    data.add(map);
                                }
                            } else {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    map = new HashMap<String, Object>();
                                    map.put("title", object.getString("title"));        // 标题
                                    map.put("percent", object.getDouble("percent"));    //百分比
                                    map.put("num", object.getString("num"));            //榜单数量
                                    data.add(map);
                                }
                            }

                        } else {
                            Toast.makeText(context, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        apricotTopAdapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_apricottop, container, false);
        listview_apricottop = (MyListView) view.findViewById(R.id.listview_apricottop);
        context = getActivity();
        isInit = true;
        LazyLoad();    //让第二个页面加载数据
        setAdpaer();
        return view;
    }

    private void setAdpaer() {
        apricotTopAdapter = new ApricotTopAdapter(context, data);
        listview_apricottop.setAdapter(apricotTopAdapter);
    }


    @Override
    public void LazyLoad() {
        if (isInit && isVisible) {
            setmsgdb();
            isInit = true;      //若为false数据仅加载一次
        } else {
            setmsgdb();
        }

    }

    /**
     * name:设置listview中的值 author:Tom 2016-1-28下午3:42:47
     */
    public void setmsgdb() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.xlbIndex);
                    obj.put("type", Type.PHB.getIndex());
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.CCMTVAPP, obj.toString());
                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }


}

