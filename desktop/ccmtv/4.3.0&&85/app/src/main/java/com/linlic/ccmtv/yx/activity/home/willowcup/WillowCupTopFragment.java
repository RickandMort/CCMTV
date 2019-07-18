package com.linlic.ccmtv.yx.activity.home.willowcup;

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
import com.linlic.ccmtv.yx.activity.home.adapter.WillowCupTopAdapter;
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
 * name：柳叶榜
 * author：Larry
 * data：2017/4/10.
 */
public class WillowCupTopFragment extends BaseLazyFragment {
    private MyListView listview_willowtop;
    private boolean isInit = false;
    private WillowCupTopAdapter willowCupTopAdapter;
    Context context;
    Map<String, Object> map;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    data.clear();
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonObject.getInt("status") == 1) {// 成功
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
                            Toast.makeText(getActivity(), jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }

                        willowCupTopAdapter.notifyDataSetChanged();
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
        View view = inflater.inflate(R.layout.fragment_willowtop, container, false);
        listview_willowtop = (MyListView) view.findViewById(R.id.listview_willowtop);
        context = getActivity();
        isInit = true;
        LazyLoad();                                         //让第二个页面加载数据
        setAdpaer();
        return view;
    }

    private void setAdpaer() {
        willowCupTopAdapter = new WillowCupTopAdapter(context, data);
        listview_willowtop.setAdapter(willowCupTopAdapter);
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
                    obj.put("act", URLConfig.lybIndex);
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
