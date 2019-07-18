package com.linlic.ccmtv.yx.activity.home.yxzbjrrom;

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
 * 医学直播间---更多精彩预告
 * Created by larry.li on 2017/3/22.
 */
public class MedicalLiveRoomTrailerAcitvity extends BaseActivity {
    private ListView special_more_list;// 数据加载
    //private GridView medical_live_room_more_gridlist;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    BaseListAdapter baseListAdapter;
    private int currPage = 1;
    private String type;
    private String style;
    Context context;
    private boolean isNoMore = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    baseListAdapter.notifyDataSetChanged();
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.medical_live_room_more);
        context = this;
        Intent intent = getIntent();
        style = intent.getExtras().getString("style");
        type = intent.getExtras().getString("type");
        findId();
        setText();
        setmsgdb();
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
                    obj.put("act", URLConfig.directSeedingMore);
                    obj.put("type", type);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("style", style);
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
                            map.put("id", customJson.getString("id"));
                            map.put("livename", customJson.getString("livename"));
                            map.put("liveurl", customJson.getString("liveurl"));
                            map.put("livestrattime", customJson.getString("livestrattime"));
                            map.put("livecontact", customJson.getString("livecontact"));
                            map.put("imgurl", customJson.getString("imgurl"));
                            data.add(map);
                        }
                    } else {
                        isNoMore = true;
                    }

                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }

            }
        };
        new Thread(runnable).start();
    }


    @Override
    public void findId() {
        super.findId();
        special_more_list = (ListView) findViewById(R.id.medical_live_room_more_list);
    }

    public void setText() {
        this.setActivity_title_name("直播预告");
        baseListAdapter = new BaseListAdapter(special_more_list, data, R.layout.medical_live_room_item) {
            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                helper.setText(R.id.department_id, ((Map) item).get("id") + "");
                helper.setText(R.id.departemnt_item_title, ((Map) item).get("livename") + "");
                helper.setText(R.id.department_web, ((Map) item).get("liveurl") + "");
                helper.setText(R.id.department_times1, ((Map) item).get("livestrattime") + "");
                helper.setText(R.id.department_times, ((Map) item).get("livecontact") + "");
                helper.setImageBitmapGlide(context, R.id.departemnt_item_img, ((Map) item).get("imgurl") + "");
            }
        };

        special_more_list.setVisibility(View.VISIBLE);
        //  medical_live_room_more_gridlist.setVisibility(View.GONE);
        special_more_list.setAdapter(baseListAdapter);
        // listview点击事件
        special_more_list
                .setOnItemClickListener(new casesharing_listListener());
        baseListAdapter.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = special_more_list.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        isNoMore = false;
                    }
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = special_more_list.getChildAt(special_more_list.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == special_more_list.getHeight()) {
                        if (!isNoMore) {
                            currPage += 1;
                            setmsgdb();
                        }
                    }
                }
            }
        });
    }

    /**
     * name: 点击查看某个视频的详细 author:Tom 2016-1-28下午3:42:08
     */
    private class casesharing_listListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {
            TextView textView = (TextView) view
                    .findViewById(R.id.department_web);
            TextView departemnt_item_title = (TextView) view
                    .findViewById(R.id.departemnt_item_title);
            String id = textView.getText().toString();
            Intent intent = null;
            intent = new Intent(context, Medical_live_RoomWebActivity.class);
            intent.putExtra("title", departemnt_item_title.getText());
            intent.putExtra("aid", id);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        MyProgressBarDialogTools.hide();
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn";
        super.onPause();
    }

}
