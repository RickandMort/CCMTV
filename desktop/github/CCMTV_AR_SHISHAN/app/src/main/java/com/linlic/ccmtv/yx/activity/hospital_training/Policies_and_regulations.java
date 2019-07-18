package com.linlic.ccmtv.yx.activity.hospital_training;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tom on 2017/12/27. 政策法规列表
 */

public class Policies_and_regulations extends BaseActivity {
    BaseListAdapter baseListAdapter;
    private Context context;
    private String  aid;
    private String fid = "9527";
    private int pause = 0;
    private boolean isNoMore = false;
    private ListView gift_list;
    private String bigType;
    private int page = 1;
    private List<Map<String, Object>> data = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            setActivity_title_name(jsonObject.getString("artic_list_title"));
                            JSONArray dataArray = jsonObject.getJSONArray("artic_list");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject customJson = dataArray.getJSONObject(i);
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("departemnt_item_title", customJson.getString("title"));
                                map.put("department_id", customJson.getString("aid"));
                                map.put("is_digg", customJson.getString("is_digg"));
                                map.put("department_on_demand", customJson.getString("digg_num"));
                                map.put("department_times", customJson.getString("posttime"));
                                map.put("departemnt_item_img", customJson.getString("picurl"));
                                data.add(map);
                            }
                            if (dataArray.length() < 10) {
                                isNoMore = true;
                            }
                            baseListAdapter.notifyDataSetChanged();
                        } else {
                            isNoMore = true;
                            Toast.makeText(Policies_and_regulations.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.policies_and_regulations);
        context = this;
        findId();
        initdata();
        setPolicies_and_regulations();
    }


    @Override
    public void findId() {
        super.findId();
        gift_list = (ListView) findViewById(R.id.gift_list);
        LinearLayout formal_examination_butten = (LinearLayout) View.inflate(context, R.layout.direct_broadcast_list_bottom, null);
        gift_list.addFooterView(formal_examination_butten);
    }

    public void clickCollection_list(View view) {
        Intent intent = new Intent(context, Policies_and_regulations2.class);
        intent.putExtra("bigType", getIntent().getStringExtra("bigType"));
        startActivity(intent);
    }

    public void initdata() {

        bigType = getIntent().getStringExtra("bigType");

        baseListAdapter = new BaseListAdapter(gift_list, data, R.layout.hospital_training_main_list_item) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);

                helper.setText(R.id.departemnt_item_title, ((Map) item).get("departemnt_item_title") + "");
                helper.setText(R.id.department_id, ((Map) item).get("department_id") + "");
                helper.setText(R.id.department_on_demand, ((Map) item).get("department_on_demand") + "");
                helper.setText(R.id.department_times, ((Map) item).get("department_times") + "");
                helper.setImageBitmapGlide(context, R.id.departemnt_item_img, ((Map) item).get("departemnt_item_img") + "");
                if (((Map) item).get("is_digg").toString().equals("0")) {
                    helper.setImage(R.id.department_on_demand_img, R.mipmap.assist_icon02);
                } else {
                    helper.setImage(R.id.department_on_demand_img, R.mipmap.assist_icon03);
                }
            }
        };

        gift_list.setAdapter(baseListAdapter);
        // listview点击事件
        gift_list
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
                    View firstVisibleItemView = gift_list.getChildAt(0);


                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
//                        Log.d("ListView", "<----滚动到顶部----->");
                        isNoMore = false;
                    }

                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {

                    View lastVisibleItemView = gift_list.getChildAt(gift_list.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == gift_list.getHeight()) {
//                        System.out.println("#####滚动到底部######" + !isNoMore);
                        if (!isNoMore) {
                            page += 1;
                            setPolicies_and_regulations();
                        }
                    }
                }
            }
        });
    }

    public void setPolicies_and_regulations() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.trainArticleList);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("bigType", bigType);
                    obj.put("page", page);
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.Hospital_training, obj.toString());
//                    LogUtil.e("医院培训首页政策法规", result);
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
     * name: 点击查看某个政策法规的详细
     */
    private class casesharing_listListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {
            TextView textView = (TextView) view
                    .findViewById(R.id.department_id);
            String id = textView.getText().toString();
            // MyProgressBarDialogTools.show(context);
            final String uid = SharedPreferencesTools.getUidToLoginClose(context);
            if (uid == null || ("").equals(uid)) {
                return;
            }
            Intent intent = new Intent(context, Details_of_policies_and_regulations.class);
            intent.putExtra("aid", id);
            startActivity(intent);

        }

    }

    @Override
    public void onResume() {
        gift_list.setSelection(0);
        if (pause > 0) {
            page = 1;
            data.removeAll(data);
            setPolicies_and_regulations();
            pause = 0;
        }
        super.onResume();

    }

    @Override
    public void onPause() {
        enterUrl = "http://www.ccmtv.cn/article/" + fid + "/" + aid + ".html";
        pause++;
        super.onPause();
    }

}
