package com.linlic.ccmtv.yx.activity.hospital_training;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/9.   收藏列表
 */

public class Collection_list extends BaseActivity {
    private Context context;
    private ListView department_list;
    private boolean isNoMore = false;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    BaseListAdapter baseListAdapter;
    //当前Item被点击的位置
    private int page = 1;
    private int count = 0;
    private int pause = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            count = jsonObject.has("count") ? jsonObject.getInt("status") : count;
                            JSONArray dataArray = jsonObject
                                    .getJSONArray("data");
//                            System.out.println("进入到搜索解析页：" + dataArray);
                            if (dataArray.length() < 15) {
                                isNoMore = true;

                            }
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject customJson = dataArray.getJSONObject(i);
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("question", customJson.getString("question"));
                                map.put("id", customJson.getString("id"));
                                map.put("type", customJson.getString("type"));
                                if (customJson.has("cate")) {
                                    map.put("cate", customJson.getJSONArray("cate"));
                                }

                                data.add(map);
                            }
                        } else {
                            isNoMore = true;
                            Toast.makeText(Collection_list.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        baseListAdapter.notifyDataSetChanged();
                        setResultStatus(data.size() > 0, jsonObject.getInt("status"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    Toast.makeText(context
                            , R.string.post_hint1,
                            Toast.LENGTH_SHORT).show();
                    setResultStatus(data.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOST);
                    break;

                default:
                    break;
            }

        }
    };
    private NodataEmptyLayout collection_nodata;

    private void setResultStatus(boolean status, int code) {
        if (status) {
            department_list.setVisibility(View.VISIBLE);
            collection_nodata.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                collection_nodata.setNetErrorIcon();
            } else {
                collection_nodata.setLastEmptyIcon();
            }
            department_list.setVisibility(View.GONE);
            collection_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.collection_list);
        context = this;
        findId();
        initData();
        initCollection_list();

    }

    @Override
    public void onResume() {
        if (pause > 0) {
            data.removeAll(data);
            page = 1;
            isNoMore = false;
            initCollection_list();
        }
        super.onResume();

    }

    @Override
    public void onPause() {
        enterUrl = "http://yy.ccmtv.cn/exam_bank/exercise.html";
        pause++;
        super.onPause();
    }

    @Override
    public void findId() {
        super.findId();
        department_list = (ListView) findViewById(R.id.department_list);
        collection_nodata = (NodataEmptyLayout) findViewById(R.id.collection_list_nodata);

    }

    public void initData() {


        baseListAdapter = new BaseListAdapter(department_list, data, R.layout.department_list_item3) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);

                helper.setText(R.id.department_name, ((Map) item).get("question") + "", "html");
                helper.setText(R.id.department_id, ((Map) item).get("id") + "");

                /* android:layout_height="wrap_content"
    android:background="@drawable/anniu20"
    android:paddingBottom="5dp"
    android:paddingRight="15dp"
    android:paddingLeft="15dp"
    android:textColor="#666"
    android:paddingTop="5dp"
    android:text="A1题型"*/
                helper.removeViews(R.id.auto_wrap_line_layout);

                View home_center = LayoutInflater.from(context).inflate(R.layout.collection_practice_layout_item, null);
                TextView _item_text = (TextView) home_center.findViewById(R.id._item_text);
                _item_text.setText(((Map) item).get("type") + "");
                helper.addview3(_item_text, R.id.auto_wrap_line_layout);
                if (((Map) item).containsKey("cate")) {
                    JSONArray jsonArray = (JSONArray) ((Map) item).get("cate");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        View home_center2 = LayoutInflater.from(context).inflate(R.layout.collection_practice_layout_item, null);
                        TextView _item_text2 = (TextView) home_center2.findViewById(R.id._item_text);
                        try {
                            _item_text2.setText(jsonArray.getString(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        helper.addview3(_item_text2, R.id.auto_wrap_line_layout);
                    }
                }

            }
        };
        department_list.setAdapter(baseListAdapter);
        LinearLayout formal_examination_butten = (LinearLayout) View.inflate(context, R.layout.direct_broadcast_list_bottom, null);
        department_list.addFooterView(formal_examination_butten);
        // listview点击事件
        department_list
                .setOnItemClickListener(new casesharing_listListener());
        baseListAdapter.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > 0) {

                }
//                System.out.println("当前条目位置："+firstVisibleItem + "   当前屏幕容纳条目数：" + visibleItemCount + "      总条目数:" + totalItemCount);
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = department_list.getChildAt(0);

                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {

                    View lastVisibleItemView = department_list.getChildAt(department_list.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == department_list.getHeight()) {
//                        System.out.println("#####滚动到底部######" + !isNoMore);

                        if (!isNoMore) {
                            page++;
                            initCollection_list();
                        }
                          /*  //增加尾部
                            */

                    }
                }
            }
        });

    }

    /**
     * name: 点击查看某个科室 author:Tom 2016-1-28下午3:42:08
     */
    private class casesharing_listListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {
            Intent intent = new Intent(context, Collection_exercises.class);
            intent.putExtra("page", (arg2 + 1) + "");
            startActivity(intent);

        }

    }

    public void initCollection_list() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.collectListNew);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("page", page);
//                    Log.e("医院培训-收藏列表条件", obj.toString());
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.Hospital_training, obj.toString());
//                    Log.e("医院培训-收藏列表", result);
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
