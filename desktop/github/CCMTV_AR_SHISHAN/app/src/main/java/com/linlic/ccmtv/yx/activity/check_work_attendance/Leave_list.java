package com.linlic.ccmtv.yx.activity.check_work_attendance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.DoubleTimeSelectDialog;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.common.util.MD5;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tom on 2018/12/11.
 */

public class Leave_list extends BaseActivity {

    private Context context;
    private int page = 1;
    private int limit = 20;
    @Bind(R.id.leave)
    LinearLayout leave;
    @Bind(R.id.recording_list)
    ListView recording_list;
    @Bind(R.id.tranining2_nodata)
    NodataEmptyLayout tranining2_nodata;

    private List<String> spinner_list = new ArrayList<>(),allStatus_list = new ArrayList<>();//活动类型数据
    Map<String, Object> spinner_map = new HashMap<>();
    private BaseListAdapter baseListAdapterRecording;
    private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
    private DoubleTimeSelectDialog mDoubleTimeSelectDialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                JSONArray dateJson = dataJson.getJSONArray("data") ;
                                listData.clear();

                                for (int i = 0; i < dateJson.length(); i++) {
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("starttime", dataJson1.getString("starttime"));
                                    map.put("endtime", dataJson1.getString("endtime"));
                                    map.put("reason", dataJson1.getString("reason"));
                                    map.put("typename", dataJson1.getString("typeName"));

                                    listData.add(map);
                                }
                                baseListAdapterRecording.notifyDataSetChanged();

                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
//
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        setResultStatus(listData.size() > 0, jsonObject.getInt("code"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        setResultStatus(listData.size() > 0, HttpClientUtils.UNKONW_EXCEPTION_CODE);
                    } finally {
//                        MyProgressBarDialogTools.hide();
                    }
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
    private void setResultStatus(boolean status, int code) {
        if (status) {
            recording_list.setVisibility(View.VISIBLE);
            tranining2_nodata.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                tranining2_nodata.setNetErrorIcon();
            } else {
                tranining2_nodata.setLastEmptyIcon();
            }
            recording_list.setVisibility(View.GONE);
            tranining2_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.leave_list);
        context = this;
        ButterKnife.bind(this);

        findId();
        initView();


    }

    public void initView() {

        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Leave.class);
                startActivity(intent);
            }
        });




        baseListAdapterRecording = new BaseListAdapter(recording_list, listData, R.layout.item_leave_list) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map map = (Map) item;

                helper.setText(R.id.reason, map.get("reason").toString());
                helper.setText(R.id.starttime, map.get("starttime").toString());
                helper.setText(R.id.endtime,  map.get("endtime").toString());
                helper.setText(R.id.typename,  map.get("typename").toString());
            }
        };
        recording_list.setAdapter(baseListAdapterRecording);
        baseListAdapterRecording.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > 0) {
                }
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = recording_list.getChildAt(0);

                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = recording_list.getChildAt(recording_list.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == recording_list.getHeight()) {
                        if (listData.size()==(page*limit)) {
                            page += 1;
                            userSignLogList();
                        }
                    }
                }
            }
        });
    }


    public void userSignLogList() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj_sj = new JSONObject();
                    obj_sj.put("act", URLConfig.getTimestamp);
                    String result_sj = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_kq, obj_sj.toString());
                    JSONObject jsonObject = new JSONObject(result_sj);
                    if (jsonObject.getInt("code") == 200) {
                        JSONObject dataJson = jsonObject.getJSONObject("data");

                        if (dataJson.getInt("status") == 1) { // 成功
                            JSONObject obj = new JSONObject();
                            obj.put("act", URLConfig.askLeaveList);
                            obj.put("uid", SharedPreferencesTools.getUid(context));
                            obj.put("page", page);
                            obj.put("limit", limit);
                            obj.put("timestamp", dataJson.getJSONObject("data").getString("timestamp"));
                            obj.put("token", MD5.md5(URLConfig.key + dataJson.getJSONObject("data").getString("timestamp") + SharedPreferencesTools.getUid(context)));

                            String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_kq, obj.toString());
                            Message message = new Message();
                            message.what = 1;
                            message.obj = result;
                            handler.sendMessage(message);
                        } else {
                            Toast.makeText(context, jsonObject.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }


            }
        };
        new Thread(runnable).start();
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
    @Override
    protected void onResume() {
        userSignLogList();
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }
}
