package com.linlic.ccmtv.yx.activity.my.unit_notification;

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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tom on 2017/9/20.
 * 医考-公告列表
 */
public class Unit_notification extends BaseActivity {
    private Context context;
    private ListView unit_notification_list;
    BaseListAdapter baseListAdapter;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private boolean isNoMore = false;
    private int currPage = 1;
    private int pause = 0;
    private NodataEmptyLayout un_nodata;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObjects = new JSONObject(msg.obj + "");
                        if (jsonObjects.getInt("status") == 1) { // 成功
                            JSONArray jsonObject = jsonObjects.getJSONArray("data");
                            /*解析考试其他信息start*/

                            /*解析考试其他信息end*/
                            /*解析题型列表start*/
                            for (int i = 0; i < jsonObject.length(); i++) {
                                JSONObject customJson = jsonObject.getJSONObject(i);
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("id", customJson.getString("id"));
                                map.put("title", customJson.getString("title"));
                                map.put("createtime", customJson.getString("createtime"));
                                map.put("is_look", customJson.getString("is_look"));
                                data.add(map);
                            }
//                            if (data.size() > 0) {
//                                unit_notification_list.setVisibility(View.VISIBLE);
//                                un_nodata.setVisibility(View.GONE);
//                            } else {
//                                unit_notification_list.setVisibility(View.GONE);
//                                un_nodata.setVisibility(View.VISIBLE);
//                            }
                            if (jsonObject.length() < 1) {
                                isNoMore = true;
                            }
                             /*解析题型列表end*/
                        } else {
                            isNoMore = true;
//                            if (data.size() > 0) {
//                                unit_notification_list.setVisibility(View.VISIBLE);
//                                un_nodata.setVisibility(View.GONE);
//                            } else {
//                                unit_notification_list.setVisibility(View.GONE);
//                                un_nodata.setVisibility(View.VISIBLE);
//                            }
//                            Toast.makeText(Unit_notification.this, jsonObjects.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
//                        MyProgressBarDialogTools.hide();
                        baseListAdapter.notifyDataSetChanged();
                        setResultStatus(data.size() > 0, jsonObjects.getInt("status"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    setResultStatus(data.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOST);
                    break;
                default:
                    break;
            }
        }
    };

    private void setResultStatus(boolean status, int code) {
        if (status) {
            unit_notification_list.setVisibility(View.VISIBLE);
            un_nodata.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                un_nodata.setNetErrorIcon();
            } else {
                un_nodata.setLastEmptyIcon();
            }
            unit_notification_list.setVisibility(View.GONE);
            un_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.unit_notification);
        context = this;
        findId();
        initdata();
        setValue2();
    }

    @Override
    public void findId() {
        super.findId();
        unit_notification_list = (ListView) findViewById(R.id.unit_notification_list);
        un_nodata = (NodataEmptyLayout) findViewById(R.id.un_nodata);
    }

    public void initdata() {
        baseListAdapter = new BaseListAdapter(unit_notification_list, data, R.layout.unit_notification_list_item) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                helper.setText(R.id.text1, ((Map) item).get("title") + "");
                helper.setText(R.id.text2, ((Map) item).get("createtime") + "");
                helper.setText(R.id.id, ((Map) item).get("id") + "");
                if (Integer.parseInt(((Map) item).get("is_look").toString()) > 0) {
                    helper.setVisibility(R.id.is_look, View.VISIBLE);
                } else {
                    helper.setVisibility(R.id.is_look, View.GONE);
                }
            }
        };
        unit_notification_list.setAdapter(baseListAdapter);

        baseListAdapter.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = unit_notification_list.getChildAt(0);
//                                video_comment_dilong.setVisibility(View.GONE);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        isNoMore = false;
                    }
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = unit_notification_list.getChildAt(unit_notification_list.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == unit_notification_list.getHeight()) {
                        if (!isNoMore) {
                            currPage += 1;
                            setValue2();
                        }
                    }
                }
            }
        });
        unit_notification_list.setOnItemClickListener(new casesharing_listListener());
    }

    /**
     * name: 点击查看某个考试详细
     */
    private class casesharing_listListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
            TextView textView = (TextView) view.findViewById(R.id.id);
            String id = textView.getText().toString();
            // MyProgressBarDialogTools.show(context);
            //跳转到考试详情
            Intent intent = new Intent(context, Viewing_unit_notification.class);
            intent.putExtra("nid", id);
            startActivity(intent);
        }
    }

    public void setValue2() {
//        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getNoticeList);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("page", currPage);

                    String result = HttpClientUtils.sendPost(context, URLConfig.Medical_examination, obj.toString());

//                    MyProgressBarDialogTools.hide();
                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
//                    MyProgressBarDialogTools.hide();
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    @Override
    protected void onResume() {
        unit_notification_list.setSelection(0);
        if (pause > 0) {
            data.removeAll(data);
            currPage = 1;
            setValue2();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        enterUrl = "http://www.ccmtv.cn/Member/Index.html";
        pause++;
        super.onPause();
    }
}
