package com.linlic.ccmtv.yx.activity.message;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
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

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/10/22.   私信
 */

public class Private_letters extends BaseActivity {

    private Context context;
    @Bind(R.id.message_list)
    ListView message_list;
    @Bind(R.id.private_letters_nodata)
    NodataEmptyLayout private_letters_nodata;
    private String catid1 = "";
    private String catid2 = "";
    private int page = 1;
    private int limit = 5;
    private int count = 0;

    private BaseListAdapter baseListAdapterVideo;
    private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (isFinishing() || isDestroyed()) return;
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                JSONArray dateJson = dataJson.getJSONObject("data").getJSONArray("list");
                                if (page == 1) {
                                    JSONObject data = dataJson.getJSONObject("data");
//                                    count = data.getInt("count");
                                    try {
                                        count = data.getInt("count");
                                    } catch (Exception e) {
                                        count = 0;
                                    }
                                    listData.clear();
                                }

                                for (int i = 0; i < dateJson.length(); i++) {
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("icon", dataJson1.getString("icon"));
                                    map.put("uid", dataJson1.getString("uid"));
                                    map.put("username", dataJson1.getString("username"));
                                    map.put("content", dataJson1.getString("content"));
                                    map.put("create_time", dataJson1.getString("create_time"));
                                    map.put("is_read", dataJson1.getString("is_read"));
                                    map.put("is_friend", dataJson1.getString("is_friend"));
                                    map.put("is_workmate", dataJson1.getString("is_workmate"));
                                    map.put("position", listData.size());
                                    listData.add(map);
                                }
                                baseListAdapterVideo.notifyDataSetChanged();

                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        setResultStatus(listData.size() > 0, jsonObject.getInt("code"));
                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                        setResultStatus(listData.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOST);
                    }
                    break;
                case 500:
                    Toast.makeText(context
                            , R.string.post_hint1,
                            Toast.LENGTH_SHORT).show();
                    setResultStatus(listData.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                    break;

                default:
                    break;
            }
        }
    };

    private void setResultStatus(boolean status, int code) {
        if (status) {
            message_list.setVisibility(View.VISIBLE);
            private_letters_nodata.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                private_letters_nodata.setNetErrorIcon();
            } else {
                private_letters_nodata.setLastEmptyIcon();
            }
            message_list.setVisibility(View.GONE);
            private_letters_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.private_letters);
        context = this;
        ButterKnife.bind(this);
        catid1 = getIntent().getStringExtra("catid1");
        catid2 = getIntent().getStringExtra("catid2");
        findId();
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        page = 1;
        getUrlRulest();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/Info.html";
        super.onPause();
    }

    public void initView() {

        baseListAdapterVideo = new BaseListAdapter(message_list, listData, R.layout.item_private_letters) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map map = (Map) item;

                helper.setImageBitmap(R.id._item_icon, map.get("icon").toString());
                helper.setText(R.id._item_title, map.get("username").toString());
                helper.setText(R.id._item_content, map.get("content").toString());
                helper.setText(R.id._item_time, map.get("create_time").toString());

                if (map.get("is_friend").toString().trim().equals("1")) {//关注
                    helper.setViewVisibility(R.id._item_friend, View.VISIBLE);
                } else {
                    helper.setViewVisibility(R.id._item_friend, View.GONE);
                }
                if (map.get("is_workmate").toString().trim().equals("1")) {//同事
                    helper.setViewVisibility(R.id._item_colleague, View.VISIBLE);
                } else {
                    helper.setViewVisibility(R.id._item_colleague, View.GONE);
                }
                if (map.get("is_read").toString().trim().equals("0")) {
                    helper.setViewVisibility(R.id._item_num, View.VISIBLE);
                } else {
                    helper.setViewVisibility(R.id._item_num, View.GONE);
                }

            }
        };
        message_list.setAdapter(baseListAdapterVideo);
        message_list.setOnItemClickListener(new casesharing_listListener());
        baseListAdapterVideo.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > 0) {
                }
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = message_list.getChildAt(0);

                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = message_list.getChildAt(message_list.getChildCount() - 1);

                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == message_list.getHeight()) {
                        if (count > (page * limit)) {
                            page += 1;
                            getUrlRulest();
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
            Map<String, Object> map = listData.get(arg2);
            Intent intent = null;
            intent = new Intent(context, New_message.class);
            intent.putExtra("icon", map.get("icon").toString());
            intent.putExtra("uid", map.get("uid").toString());
            intent.putExtra("username", map.get("username").toString());
            intent.putExtra("content", map.get("content").toString());
            intent.putExtra("create_time", map.get("create_time").toString());
            intent.putExtra("is_read", map.get("is_read").toString());
            intent.putExtra("is_friend", map.get("is_friend").toString());
            intent.putExtra("is_workmate", map.get("is_workmate").toString());

            startActivity(intent);


        }
    }

    public void getUrlRulest() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.giveMyLetters);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("catid1", catid1);
                    obj.put("catid2", catid2);
                    obj.put("page", page);
                    obj.put("limit", limit);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
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


    public void new_message(View view) {

        Intent intent = new Intent(context, Select_staff.class);
//        intent.putExtra("id",view.getTag().toString());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        if (handler != null) handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
