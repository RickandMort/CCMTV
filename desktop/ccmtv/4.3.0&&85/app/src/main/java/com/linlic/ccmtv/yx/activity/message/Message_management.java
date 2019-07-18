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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.adapter.Message_Hu_adapter;
import com.linlic.ccmtv.yx.adapter.Message_managementAdapter;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;
import com.linlic.ccmtv.yx.widget.SwipeListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.linlic.ccmtv.yx.utils.HttpClientUtils.UNKONW_EXCEPTION_CODE;

/**
 * Created by Administrator on 2018/10/17.
 * 消息管理主页
 */

public class Message_management extends BaseActivity implements PullToRefreshBase.OnRefreshListener2<ScrollView> {

    @Bind(R.id.hu_msg_list)
    SwipeListView huMsgList;
    private Context context;
    @Bind(R.id.pull_refresh_scrollview)
    PullToRefreshScrollView mPullRefreshScrollView;
    @Bind(R.id.message_list)
    SwipeListView message_list;
    @Bind(R.id.management_nodata)
    NodataEmptyLayout management_nodata;
    @Bind(R.id.message_list2)
    ListView message_list2;
    @Bind(R.id.system_information)
    TextView system_information;//系统通知
    @Bind(R.id.announcement_notice)
    TextView announcement_notice;//公告通知
    @Bind(R.id.interactive)
    TextView interactive;//互动
    @Bind(R.id.system_information_layout)//系统通知容器
            LinearLayout system_information_layout;
    @Bind(R.id.announcement_notice_layout)//公告通知容器
            LinearLayout announcement_notice_layout;
    @Bind(R.id.interactive_layout)//互动容器
            LinearLayout interactive_layout;
    @Bind(R.id.system_information_new)//系统通知 红点
            View system_information_new;
    @Bind(R.id.announcement_notice_new)//公告通知 红点
            View announcement_notice_new;
    @Bind(R.id.interactive_new)//互动 红点
            View interactive_new;

    Message_managementAdapter adapter;
    private Message_Hu_adapter hu_adapter;
    private BaseListAdapter baseListAdapterVideo;
    private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> listData2 = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> hu_Data = new ArrayList<Map<String, Object>>();
    private String fid;
    private String type = "1";
    private int page = 1;
    private int count = 0;
    private int limit = 5;
    private Map<String, Object> delpos = null;
    private Map<String, Object> hu_delpos = null;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        mPullRefreshScrollView.onRefreshComplete();
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            //message_list.setVisibility(View.VISIBLE);
                            if (dataJson.getInt("status") == 1) { // 成功
                                JSONArray dateJson = dataJson.getJSONArray("data");
                                listData.clear();

                                for (int i = 0; i < dateJson.length(); i++) {
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("catid2", dataJson1.getString("catid2"));
                                    map.put("name", dataJson1.getString("name"));
                                    map.put("icon", dataJson1.getString("icon"));
                                    map.put("count", dataJson1.getString("count"));
                                    map.put("content", dataJson1.getString("content"));
                                    map.put("aid", dataJson1.getString("aid"));
                                    map.put("create_time", dataJson1.getString("create_time"));
                                    map.put("slave_id", dataJson1.getString("slave_id"));
                                    map.put("master_id", dataJson1.getString("master_id"));
                                    map.put("catid1", dataJson1.getString("catid1"));
                                    map.put("web_flg", dataJson1.getString("web_flg"));
                                    listData.add(map);
                                }
                                adapter.notifyDataSetChanged();
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
                        setResultStatus(listData.size() > 0, UNKONW_EXCEPTION_CODE);
                    }
                    break;
                case 2:
                    try {
                        mPullRefreshScrollView.onRefreshComplete();
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            message_list2.setVisibility(View.VISIBLE);
                            if (dataJson.getInt("status") == 1) { // 成功
                                JSONArray dateJson = dataJson.getJSONObject("data").getJSONArray("list");
                                if (page == 1) {
                                    listData2.clear();
                                    count = dataJson.getJSONObject("data").getInt("count");
                                }
                                for (int i = 0; i < dateJson.length(); i++) {
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("name", dataJson1.getString("name"));
                                    map.put("icon", dataJson1.getString("icon"));
                                    map.put("master_id", dataJson1.getString("master_id"));
                                    map.put("title", dataJson1.getString("title"));
                                    map.put("create_time", dataJson1.getString("create_time"));
                                    map.put("slave_id", dataJson1.getString("slave_id"));
                                    map.put("is_read", dataJson1.getString("is_read"));
                                    map.put("noread", dataJson1.getString("noread"));
                                    map.put("position", listData2.size());

                                    listData2.add(map);
                                }
                                baseListAdapterVideo.notifyDataSetChanged();

                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
//
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        MyProgressBarDialogTools.hide();
                        setResultStatus(listData2.size() > 0, jsonObject.getInt("code"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                        setResultStatus(listData.size() > 0, UNKONW_EXCEPTION_CODE);
                    }
                    break;
                case 3:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            if (dataJson.getInt("status") == 1) { // 成功
                                listData.remove(delpos);
                                adapter.notifyDataSetChanged();
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
                        setResultStatus(listData.size() > 0, UNKONW_EXCEPTION_CODE);
                    }
                    break;
                case 6:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            if (dataJson.getInt("status") == 1) { // 成功
                                hu_Data.remove(hu_delpos);
                                hu_adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        setResultStatus(hu_Data.size() > 0, jsonObject.getInt("code"));
                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                        setResultStatus(hu_Data.size() > 0, UNKONW_EXCEPTION_CODE);
                    }
                    break;
                case 4:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            if (dataJson.getInt("status") == 1) { // 成功
                                JSONObject dataJson1 = dataJson.getJSONObject("data");
                                if (dataJson1.getString("is_unread_systemInfo").equals("1")) {//系统消息
                                    system_information_new.setVisibility(View.VISIBLE);
                                } else {
                                    system_information_new.setVisibility(View.GONE);
                                }
                                if (dataJson1.getString("is_unread_noticeInfo").equals("1")) {//公告通知
                                    announcement_notice_new.setVisibility(View.VISIBLE);
                                } else {
                                    announcement_notice_new.setVisibility(View.GONE);
                                }
                                if (dataJson1.getString("is_unread_interactInfo").equals("1")) {//公告通知
                                    interactive_new.setVisibility(View.VISIBLE);
                                } else {
                                    interactive_new.setVisibility(View.GONE);
                                }

                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        //MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    try {
                        mPullRefreshScrollView.onRefreshComplete();
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            //message_list.setVisibility(View.GONE);
                            if (dataJson.getInt("status") == 1) { // 成功
                                JSONArray dateJson = dataJson.getJSONArray("data");
                                hu_Data.clear();

                                for (int i = 0; i < dateJson.length(); i++) {
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("catid2", dataJson1.getString("catid2"));
                                    map.put("name", dataJson1.getString("name"));
                                    map.put("icon", dataJson1.getString("icon"));
                                    map.put("count", dataJson1.getString("count"));
                                    map.put("content", dataJson1.getString("content"));
                                    map.put("aid", dataJson1.getString("aid"));
                                    map.put("create_time", dataJson1.getString("create_time"));
                                    map.put("slave_id", dataJson1.getString("slave_id"));
                                    map.put("master_id", dataJson1.getString("master_id"));
                                    map.put("catid1", dataJson1.getString("catid1"));
                                    map.put("web_flg", dataJson1.getString("web_flg"));
                                    map.put("is_message_flg", dataJson1.getString("is_message_flg"));
                                    hu_Data.add(map);
                                }
                                hu_adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        setResultStatus(hu_Data.size() > 0, jsonObject.getInt("code"));
                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                        setResultStatus(hu_Data.size() > 0, UNKONW_EXCEPTION_CODE);
                    }
                    break;
                case 500:
                    Toast.makeText(context
                            , R.string.post_hint1,
                            Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    setResultStatus(listData.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOST);
                    break;

                default:
                    break;
            }
        }
    };

    private void setResultStatus(boolean status, int code) {
        if (status) {
            management_nodata.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                management_nodata.setNetErrorIcon();
            } else {
                management_nodata.setLastEmptyIcon();
            }
            mPullRefreshScrollView.setVisibility(View.GONE);
            message_list2.setVisibility(View.GONE);
            management_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.message_management);
        context = this;
        ButterKnife.bind(this);
        findId();
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getUrlRulest3();
        if (system_information.getVisibility() == View.VISIBLE) {
            getUrlRulest();
        }
        if(interactive.getVisibility() == View.VISIBLE){
            get_hu_data();
        }
        if (SharedPreferencesTools.getEvent_details_status(context).trim().length() > 0 && Integer.parseInt(SharedPreferencesTools.getEvent_details_status(context).trim()) < listData2.size()) {
            listData2.get(Integer.parseInt(SharedPreferencesTools.getEvent_details_status(context).trim())).put("is_read", "1");
            SharedPreferencesTools.saveEvent_details_status(context, "");
            baseListAdapterVideo.notifyDataSetChanged();
        }
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/Info.html";
        super.onPause();
    }

    public void initView() {

        system_information_layout.setOnClickListener(new View.OnClickListener() {//系统通知
            @Override
            public void onClick(View v) {
                system_information.setVisibility(View.VISIBLE);
                announcement_notice.setVisibility(View.INVISIBLE);
                interactive.setVisibility(View.INVISIBLE);
                mPullRefreshScrollView.setVisibility(View.VISIBLE);//显示刷新
                message_list2.setVisibility(View.GONE);//隐藏公告通知 列表
                message_list.setVisibility(View.VISIBLE);
                huMsgList.setVisibility(View.GONE);
                //访问系统通知数据
                type = "1";
                getUrlRulest();
            }
        });
        announcement_notice_layout.setOnClickListener(new View.OnClickListener() {//公告通知
            @Override
            public void onClick(View v) {
                system_information.setVisibility(View.INVISIBLE);
                announcement_notice.setVisibility(View.VISIBLE);
                interactive.setVisibility(View.INVISIBLE);
                mPullRefreshScrollView.setVisibility(View.GONE);//隐藏刷新
                message_list2.setVisibility(View.VISIBLE);//显示公告通知 列表
                message_list.setVisibility(View.GONE);
                huMsgList.setVisibility(View.GONE);
                //访问系统通知数据
                page = 1;
                getUrlRulest2();
            }
        });
        interactive_layout.setOnClickListener(new View.OnClickListener() {//互动
            @Override
            public void onClick(View v) {
                system_information.setVisibility(View.INVISIBLE);
                announcement_notice.setVisibility(View.INVISIBLE);
                interactive.setVisibility(View.VISIBLE);
                mPullRefreshScrollView.setVisibility(View.VISIBLE);//显示刷新
                message_list2.setVisibility(View.GONE);//隐藏公告通知 列表
                message_list.setVisibility(View.GONE);
                huMsgList.setVisibility(View.VISIBLE);
                //访问系统通知数据
                type = "3";
                get_hu_data();
            }
        });

        baseListAdapterVideo = new BaseListAdapter(message_list2, listData2, R.layout.item_message_management2) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map map = (Map) item;

                helper.setImageBitmap(R.id._item_icon, map.get("icon").toString());
                helper.setText(R.id._item_time, map.get("create_time").toString());
                helper.setText(R.id._item_title, map.get("name").toString());
                helper.setText(R.id._item_content, map.get("title").toString());
                helper.setTag(R.id._item_view, map.get("position").toString());
                if (map.get("is_read").toString().trim().equals("0")) {
                    helper.setViewVisibility(R.id._item_new, View.VISIBLE);
                } else {
                    helper.setViewVisibility(R.id._item_new, View.GONE);
                }

            }
        };
        message_list2.setAdapter(baseListAdapterVideo);
        baseListAdapterVideo.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > 0) {
                }
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = message_list2.getChildAt(0);

                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = message_list2.getChildAt(message_list2.getChildCount() - 1);
                    LogUtil.e("lastVisibleItemView", lastVisibleItemView + "");
                    LogUtil.e(" lastVisibleItemView.getBottom()", lastVisibleItemView.getBottom() + "");
                    LogUtil.e("message_list2.getHeight()", message_list2.getHeight() + "");
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == message_list2.getHeight()) {
                        if (count > (page * limit)) {
                            page += 1;
                            getUrlRulest2();
                        }
                    }
                }
            }
        });

        adapter = new Message_managementAdapter(context, listData, message_list.getRightViewWidth());
        adapter.setOnRightItemClickListener(new Message_managementAdapter.onRightItemClickListener() {

                                                @Override
                                                public void onRightItemClick(View v, final int position) {
                                                    delpos = listData.get(position);
                                                    MyProgressBarDialogTools.show(context);
                                                    Runnable runnable = new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            try {
                                                                JSONObject obj = new JSONObject();
                                                                obj.put("act", URLConfig.clearMyInfoByCat);
                                                                obj.put("uid", SharedPreferencesTools.getUid(context));
                                                                obj.put("catid1", listData.get(position).get("catid1").toString());
                                                                obj.put("catid2", listData.get(position).get("catid2").toString());

                                                                String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                                                                Message message = new Message();
                                                                message.what = 3;
                                                                message.obj = result;
                                                                handler.sendMessage(message);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                                handler.sendEmptyMessage(500);
                                                            }
                                                        }
                                                    };
                                                    new Thread(runnable).start();

                                                    Toast.makeText(getApplicationContext(), "清空数据", Toast.LENGTH_SHORT).show();
                                                }
                                            }
        );

        message_list.setAdapter(adapter);
        // listview点击事件
        message_list.setOnItemClickListener(new casesharing_listListener());

        hu_adapter = new Message_Hu_adapter(context,hu_Data,huMsgList.getRightViewWidth());
        hu_adapter.setOnRightItemClickListener(new Message_Hu_adapter.onRightItemClickListener() {
                                                @Override
                                                public void onRightItemClick(View v, final int position) {
                                                    hu_delpos = hu_Data.get(position);
                                                    MyProgressBarDialogTools.show(context);
                                                    Runnable runnable = new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            try {
                                                                JSONObject obj = new JSONObject();
                                                                obj.put("act", URLConfig.clearMyInfoByCat);
                                                                obj.put("uid", SharedPreferencesTools.getUid(context));
                                                                obj.put("catid1", hu_Data.get(position).get("catid1").toString());
                                                                obj.put("catid2", hu_Data.get(position).get("catid2").toString());

                                                                String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                                                                Message message = new Message();
                                                                message.what = 6;
                                                                message.obj = result;
                                                                handler.sendMessage(message);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                                handler.sendEmptyMessage(500);
                                                            }
                                                        }
                                                    };
                                                    new Thread(runnable).start();

                                                    Toast.makeText(getApplicationContext(), "清空数据", Toast.LENGTH_SHORT).show();
                                                }
                                            }
        );
        huMsgList.setAdapter(hu_adapter);
        // listview点击事件
        huMsgList.setOnItemClickListener(new casesharing_listListener2());
        mPullRefreshScrollView.setOnRefreshListener(this);
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
            switch (map.get("web_flg").toString()) {
                case "0"://通用页面(多类共用)
                    intent = new Intent(context, VIP_message.class);
                    intent.putExtra("catid2", map.get("catid2").toString());
                    intent.putExtra("name", map.get("name").toString());
                    intent.putExtra("icon", map.get("icon").toString());
                    intent.putExtra("count", map.get("count").toString());
                    intent.putExtra("content", map.get("content").toString());
                    intent.putExtra("aid", map.get("aid").toString());
                    intent.putExtra("create_time", map.get("create_time").toString());
                    intent.putExtra("slave_id", map.get("slave_id").toString());
                    intent.putExtra("master_id", map.get("master_id").toString());
                    intent.putExtra("catid1", map.get("catid1").toString());
                    intent.putExtra("web_flg", map.get("web_flg").toString());
                    break;
                case "1"://粉丝页面
                    intent = new Intent(context, Fan_message.class);
                    intent.putExtra("catid2", map.get("catid2").toString());
                    intent.putExtra("name", map.get("name").toString());
                    intent.putExtra("icon", map.get("icon").toString());
                    intent.putExtra("count", map.get("count").toString());
                    intent.putExtra("content", map.get("content").toString());
                    intent.putExtra("aid", map.get("aid").toString());
                    intent.putExtra("create_time", map.get("create_time").toString());
                    intent.putExtra("slave_id", map.get("slave_id").toString());
                    intent.putExtra("master_id", map.get("master_id").toString());
                    intent.putExtra("catid1", map.get("catid1").toString());
                    intent.putExtra("web_flg", map.get("web_flg").toString());
                    break;
                case "2"://赞页面
                    intent = new Intent(context, Comment.class);
                    intent.putExtra("catid2", map.get("catid2").toString());
                    intent.putExtra("name", map.get("name").toString());
                    intent.putExtra("icon", map.get("icon").toString());
                    intent.putExtra("count", map.get("count").toString());
                    intent.putExtra("content", map.get("content").toString());
                    intent.putExtra("aid", map.get("aid").toString());
                    intent.putExtra("create_time", map.get("create_time").toString());
                    intent.putExtra("slave_id", map.get("slave_id").toString());
                    intent.putExtra("master_id", map.get("master_id").toString());
                    intent.putExtra("catid1", map.get("catid1").toString());
                    intent.putExtra("web_flg", map.get("web_flg").toString());
                    intent.putExtra("is_message_flg","0");
                    intent.putExtra("is_Fan", "1");
                    break;
                case "3"://评论/留言页面
                    intent = new Intent(context, Comment.class);
                    intent.putExtra("catid2", map.get("catid2").toString());
                    intent.putExtra("name", map.get("name").toString());
                    intent.putExtra("icon", map.get("icon").toString());
                    intent.putExtra("count", map.get("count").toString());
                    intent.putExtra("content", map.get("content").toString());
                    intent.putExtra("aid", map.get("aid").toString());
                    intent.putExtra("create_time", map.get("create_time").toString());
                    intent.putExtra("slave_id", map.get("slave_id").toString());
                    intent.putExtra("master_id", map.get("master_id").toString());
                    intent.putExtra("catid1", map.get("catid1").toString());
                    intent.putExtra("web_flg", map.get("web_flg").toString());
                    intent.putExtra("is_Fan", "0");
                    break;
                case "4"://私信页面
                    intent = new Intent(context, Private_letters.class);
                    intent.putExtra("catid2", map.get("catid2").toString());
                    intent.putExtra("name", map.get("name").toString());
                    intent.putExtra("icon", map.get("icon").toString());
                    intent.putExtra("count", map.get("count").toString());
                    intent.putExtra("content", map.get("content").toString());
                    intent.putExtra("aid", map.get("aid").toString());
                    intent.putExtra("create_time", map.get("create_time").toString());
                    intent.putExtra("slave_id", map.get("slave_id").toString());
                    intent.putExtra("master_id", map.get("master_id").toString());
                    intent.putExtra("catid1", map.get("catid1").toString());
                    intent.putExtra("web_flg", map.get("web_flg").toString());
                    break;
                default:
                    intent = new Intent(context, VIP_message.class);
                    intent.putExtra("catid2", map.get("catid2").toString());
                    intent.putExtra("name", map.get("name").toString());
                    intent.putExtra("icon", map.get("icon").toString());
                    intent.putExtra("count", map.get("count").toString());
                    intent.putExtra("content", map.get("content").toString());
                    intent.putExtra("aid", map.get("aid").toString());
                    intent.putExtra("create_time", map.get("create_time").toString());
                    intent.putExtra("slave_id", map.get("slave_id").toString());
                    intent.putExtra("master_id", map.get("master_id").toString());
                    intent.putExtra("catid1", map.get("catid1").toString());
                    intent.putExtra("web_flg", map.get("web_flg").toString());
                    break;
            }
            if (intent != null) {
                startActivity(intent);
            } else {
                Toast.makeText(context, "该数据有误，请联系客服！", Toast.LENGTH_SHORT).show();
            }

        }

    }

    /**
     *
     */
    private class casesharing_listListener2 implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {
            Map<String, Object> map = hu_Data.get(arg2);
            Intent intent = null;
            switch (map.get("web_flg").toString()) {
                case "0"://通用页面(多类共用)
                    intent = new Intent(context, VIP_message.class);
                    intent.putExtra("catid2", map.get("catid2").toString());
                    intent.putExtra("name", map.get("name").toString());
                    intent.putExtra("icon", map.get("icon").toString());
                    intent.putExtra("count", map.get("count").toString());
                    intent.putExtra("content", map.get("content").toString());
                    intent.putExtra("aid", map.get("aid").toString());
                    intent.putExtra("create_time", map.get("create_time").toString());
                    intent.putExtra("slave_id", map.get("slave_id").toString());
                    intent.putExtra("master_id", map.get("master_id").toString());
                    intent.putExtra("catid1", map.get("catid1").toString());
                    intent.putExtra("web_flg", map.get("web_flg").toString());
                    break;
                case "1"://粉丝页面
                    intent = new Intent(context, Fan_message.class);
                    intent.putExtra("catid2", map.get("catid2").toString());
                    intent.putExtra("name", map.get("name").toString());
                    intent.putExtra("icon", map.get("icon").toString());
                    intent.putExtra("count", map.get("count").toString());
                    intent.putExtra("content", map.get("content").toString());
                    intent.putExtra("aid", map.get("aid").toString());
                    intent.putExtra("create_time", map.get("create_time").toString());
                    intent.putExtra("slave_id", map.get("slave_id").toString());
                    intent.putExtra("master_id", map.get("master_id").toString());
                    intent.putExtra("catid1", map.get("catid1").toString());
                    intent.putExtra("web_flg", map.get("web_flg").toString());
                    break;
                case "2"://赞页面
                    intent = new Intent(context, Comment.class);
                    intent.putExtra("catid2", map.get("catid2").toString());
                    intent.putExtra("name", map.get("name").toString());
                    intent.putExtra("icon", map.get("icon").toString());
                    intent.putExtra("count", map.get("count").toString());
                    intent.putExtra("content", map.get("content").toString());
                    intent.putExtra("aid", map.get("aid").toString());
                    intent.putExtra("create_time", map.get("create_time").toString());
                    intent.putExtra("slave_id", map.get("slave_id").toString());
                    intent.putExtra("master_id", map.get("master_id").toString());
                    intent.putExtra("catid1", map.get("catid1").toString());
                    intent.putExtra("web_flg", map.get("web_flg").toString());
                    intent.putExtra("is_message_flg", map.get("is_message_flg").toString());
                    intent.putExtra("is_Fan", "1");
                    break;
                case "3"://评论/留言页面
                    intent = new Intent(context, Comment.class);
                    intent.putExtra("catid2", map.get("catid2").toString());
                    intent.putExtra("name", map.get("name").toString());
                    intent.putExtra("icon", map.get("icon").toString());
                    intent.putExtra("count", map.get("count").toString());
                    intent.putExtra("content", map.get("content").toString());
                    intent.putExtra("aid", map.get("aid").toString());
                    intent.putExtra("create_time", map.get("create_time").toString());
                    intent.putExtra("slave_id", map.get("slave_id").toString());
                    intent.putExtra("master_id", map.get("master_id").toString());
                    intent.putExtra("catid1", map.get("catid1").toString());
                    intent.putExtra("web_flg", map.get("web_flg").toString());
                    intent.putExtra("is_message_flg", map.get("is_message_flg").toString());
                    intent.putExtra("is_Fan", "0");
                    break;
                case "4"://私信页面
                    intent = new Intent(context, Private_letters.class);
                    intent.putExtra("catid2", map.get("catid2").toString());
                    intent.putExtra("name", map.get("name").toString());
                    intent.putExtra("icon", map.get("icon").toString());
                    intent.putExtra("count", map.get("count").toString());
                    intent.putExtra("content", map.get("content").toString());
                    intent.putExtra("aid", map.get("aid").toString());
                    intent.putExtra("create_time", map.get("create_time").toString());
                    intent.putExtra("slave_id", map.get("slave_id").toString());
                    intent.putExtra("master_id", map.get("master_id").toString());
                    intent.putExtra("catid1", map.get("catid1").toString());
                    intent.putExtra("web_flg", map.get("web_flg").toString());
                    break;
                default:
                    intent = new Intent(context, VIP_message.class);
                    intent.putExtra("catid2", map.get("catid2").toString());
                    intent.putExtra("name", map.get("name").toString());
                    intent.putExtra("icon", map.get("icon").toString());
                    intent.putExtra("count", map.get("count").toString());
                    intent.putExtra("content", map.get("content").toString());
                    intent.putExtra("aid", map.get("aid").toString());
                    intent.putExtra("create_time", map.get("create_time").toString());
                    intent.putExtra("slave_id", map.get("slave_id").toString());
                    intent.putExtra("master_id", map.get("master_id").toString());
                    intent.putExtra("catid1", map.get("catid1").toString());
                    intent.putExtra("web_flg", map.get("web_flg").toString());
                    break;
            }
            if (intent != null) {
                startActivity(intent);
            } else {
                Toast.makeText(context, "该数据有误，请联系客服！", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void getUrlRulest() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.commonInfoList);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("type", type);
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

    public void get_hu_data() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.commonInfoList);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("type", type);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    Message message = new Message();
                    message.what = 5;
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

    public void getUrlRulest2() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.noticeInfoList);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("page", page);
                    obj.put("limit", limit);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    Message message = new Message();
                    message.what = 2;
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


    public void getUrlRulest3() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.allInfoStatusList);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    Message message = new Message();
                    message.what = 4;
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

    public void message_details(View view) {
        SharedPreferencesTools.saveEvent_details_status(context, view.getTag().toString());
        Map<String, Object> map = listData2.get(Integer.parseInt(view.getTag().toString()));
        Intent intent = new Intent(context, Message_details.class);
        intent.putExtra("master_id", map.get("master_id").toString());
        intent.putExtra("slave_id", map.get("slave_id").toString());
        startActivity(intent);
    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        listData.removeAll(listData);
        getUrlRulest();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

        mPullRefreshScrollView.onRefreshComplete();
//            Toast.makeText(context, "暂无更多数据", Toast.LENGTH_SHORT).show();
    }
}
