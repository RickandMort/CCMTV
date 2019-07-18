package com.linlic.ccmtv.yx.activity.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
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
 * Created by Administrator on 2016/3/23.
 */
public class special_more extends BaseActivity {
    private ListView special_more_list;// 数据加载
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    BaseListAdapter baseListAdapter;
    private int currPage = 1;
    private String fid;
    private String special_more;
    Context context;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    MyProgressBarDialogTools.hide();
                    special_more_list.setAdapter(baseListAdapter);
                    break;
                case 500:
                    Toast.makeText(context
                            , R.string.post_hint1,
                            Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.special_more);
        context = this;

        Intent intent = getIntent();
        if (intent.getExtras().getString("special_more") != null && intent.getExtras().getString("special_more").length() > 0) {
            special_more = intent.getExtras().getString("special_more");
            fid = intent.getExtras().getString("fid");
        }

        findId();
        setText();


        baseListAdapter = new BaseListAdapter(special_more_list, data, R.layout.department_item) {
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
                helper.setImageBitmapGlide(com.linlic.ccmtv.yx.activity.home.special_more.this, R.id.departemnt_item_img, ((Map) item).get("departemnt_item_img") + "");
                //videopaymoney  不为0则为收费 为0 测试代表着是非收费视频
                if (!((Map) item).get("videopaymoney").equals("0")) {
                    //收费
                    helper.setImage(R.id.departemnt_item_top_img, R.mipmap.charge);
                    helper.setVisibility(R.id.departemnt_item_top_img, View.VISIBLE);
                } else {
                    helper.setVisibility(R.id.departemnt_item_top_img, View.GONE);
                    if(((Map) item).get("money").toString().equals("3")){
                        //会员
                        helper.setImage(R.id.departemnt_item_top_img, R.mipmap.vip_img);
                        helper.setVisibility(R.id.departemnt_item_top_img, View.VISIBLE);
                    }
                }


            }
        };

        baseListAdapter.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = special_more_list.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                    }
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = special_more_list.getChildAt(special_more_list.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == special_more_list.getHeight()) {
                        currPage += 1;
                        setmsgdb2();
                    }
                }

            }
        });

        setmsgdb();


    }

    /**
     * name:设置listview中的值 author:Tom 2016-1-28下午3:42:47
     */
    public void setmsgdb() {
        MyProgressBarDialogTools.show(com.linlic.ccmtv.yx.activity.home.special_more.this);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.indexCarouselData);
                    obj.put("fid", fid);
                    obj.put("page", currPage);
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.CCMTVAPP, obj.toString());

                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 1) {// 成功
                        JSONArray dataArray = jsonObject
                                .getJSONArray("data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject customJson = dataArray.getJSONObject(i);
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("departemnt_item_title", customJson.getString("title"));
                            map.put("department_id", customJson.getString("aid"));
                            map.put("department_on_demand", "播放数：" + customJson.getString("hits"));
                            map.put("department_times", customJson.getString("posttime"));
                            map.put("departemnt_item_img", customJson.getString("picurl"));
                            map.put("money", customJson.getString("money"));
                            map.put("flag", customJson.getString("flag"));
                            map.put("videopaymoney", customJson.getString("videopaymoney"));
                            data.add(map);
                        }
                    }
                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                    // listview点击事件
                    special_more_list
                            .setOnItemClickListener(new casesharing_listListener());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        };
        new Thread(runnable).start();
    }

    /**
     * name:设置listview中的值 author:Tom 2016-1-28下午3:42:47
     */
    public void setmsgdb2() {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.indexCarouselData);
                    obj.put("fid", fid);
                    obj.put("page", currPage);
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.CCMTVAPP, obj.toString());

                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 1) {// 成功
                        JSONArray dataArray = jsonObject
                                .getJSONArray("data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject customJson = dataArray.getJSONObject(i);
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("departemnt_item_title", customJson.getString("title"));
                            map.put("department_id", customJson.getString("aid"));
                            map.put("department_on_demand", "播放数：" + customJson.getString("hits"));
                            map.put("department_times", customJson.getString("posttime"));
                            map.put("departemnt_item_img", customJson.getString("picurl"));
                            map.put("money", customJson.getString("money"));
                            map.put("flag", customJson.getString("flag"));
                            map.put("videopaymoney", customJson.getString("videopaymoney"));
                            data.add(map);
                        }
                    }

                    baseListAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        };
        new Thread(runnable).start();
    }


    /**
     * name: 点击查看某个视频的详细 author:Tom 2016-1-28下午3:42:08
     */
    private class casesharing_listListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {
            TextView textView = (TextView) view
                    .findViewById(R.id.department_id);
            String id = textView.getText().toString();
            getVideoRulest(id);

        }

    }

    public void getVideoRulest(final String aid) {
        final String uid = SharedPreferencesTools.getUidToLoginClose(context);
        if (uid == null || ("").equals(uid)) {
            return;
        }
        Intent intent = new Intent(context, VideoFive.class);
        intent.putExtra("aid", aid);
        startActivity(intent);

    }


    @Override
    public void findId() {
        super.findId();
        special_more_list = (ListView) findViewById(R.id.special_more_list);

    }

    public void setText() {
        this.setActivity_title_name(special_more);
    }


    @Override
    public void onResume() {
        MyProgressBarDialogTools.hide();
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn";
        super.onPause();
    }
}
