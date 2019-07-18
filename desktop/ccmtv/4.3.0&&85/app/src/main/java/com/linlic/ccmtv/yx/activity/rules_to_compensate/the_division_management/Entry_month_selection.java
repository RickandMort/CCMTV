package com.linlic.ccmtv.yx.activity.rules_to_compensate.the_division_management;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
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
import com.linlic.ccmtv.yx.utils.LogUtil;
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
 * 入科管理
 * Created by Administrator on 2018/7/3.
 */

public class Entry_month_selection extends BaseActivity {

    @Bind(R.id.activity_title_name)
    TextView activityTitleName;
    private Context context;
    private String fid = "";
    @Bind(R.id.listView)
    ListView listView;//
    @Bind(R.id.entry_month_nodata)
    NodataEmptyLayout entry_month_nodata;//
    private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
    private BaseListAdapter baseListAdapter;
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
                                JSONArray dateJson = dataJson.getJSONArray("date");
                                listData.clear();
                                for (int i = 0; i < dateJson.length(); i++) {
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("title", dataJson1.getString("title"));
                                    map.put("year", dataJson1.getString("year"));
                                    map.put("month", dataJson1.getString("month"));
                                    map.put("type", dataJson1.getString("type"));
                                    map.put("standard_kid_", dataJson1.getString("standard_kid_"));
                                    listData.add(map);
                                }
                                baseListAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                        MyProgressBarDialogTools.hide();
                        setResultStatus(listData.size() > 0, jsonObject.getInt("code"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                        setResultStatus(listData.size() > 0, HttpClientUtils.UNKONW_EXCEPTION_CODE);
                    }
                    break;
                case 500:
                    Toast.makeText(context
                            , R.string.post_hint1,
                            Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    setResultStatus(listData.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                    break;
                default:
                    break;
            }
        }
    };

    private void setResultStatus(boolean status, int code) {
        if (status) {
            listView.setVisibility(View.VISIBLE);
            entry_month_nodata.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                entry_month_nodata.setNetErrorIcon();
            } else {
                entry_month_nodata.setLastEmptyIcon();
            }
            listView.setVisibility(View.GONE);
            entry_month_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.entry_month_selection);
        context = this;
        ButterKnife.bind(this);
        activityTitleName.setText(getIntent().getStringExtra("name"));
        fid = getIntent().getStringExtra("fid");
        findId();
        initViews();
        getUrlRulest();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/rk/index.html";
        super.onPause();
    }

    @Override
    public void findId() {
        super.findId();
        initDatas();


    }

    private void initDatas() {


    }

    private void initViews() {
        baseListAdapter = new BaseListAdapter(listView, listData, R.layout.item_entry_month_selection_list) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map<String, Object> map = (Map) item;
                helper.setText(R.id._item_tiem, map.get("month").toString());
                helper.setText(R.id._item_title, map.get("title").toString(), "html");
            }
        };
        listView.setAdapter(baseListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> map = listData.get(position);
                Intent intent = null;
                switch (map.get("type").toString()) {
                    case "next_month"://跳转到入科基地学员列表
                        intent = new Intent(context, Enter_the_list_of_scientists.class);
                        intent.putExtra("title", map.get("title").toString());
                        intent.putExtra("year", map.get("year").toString());
                        intent.putExtra("month", map.get("month").toString());
                        intent.putExtra("type", map.get("type").toString());
                        intent.putExtra("standard_kid_", map.get("standard_kid_").toString());
                        intent.putExtra("fid", fid);
                        break;
                    case "edit"://跳转到入科管理学员列表
                        intent = new Intent(context, Enter_the_list_of_scientists2.class);
                        intent.putExtra("title", map.get("title").toString());
                        intent.putExtra("year", map.get("year").toString());
                        intent.putExtra("month", map.get("month").toString());
                        intent.putExtra("type", map.get("type").toString());
                        intent.putExtra("standard_kid_", map.get("standard_kid_").toString());
                        intent.putExtra("fid", getIntent().getStringExtra("fid"));
                        intent.putExtra("fid", fid);
                        break;
                    case "info"://跳转到入科管理学员列表
                        intent = new Intent(context, Enter_the_list_of_scientists2.class);
                        intent.putExtra("title", map.get("title").toString());
                        intent.putExtra("year", map.get("year").toString());
                        intent.putExtra("month", map.get("month").toString());
                        intent.putExtra("type", map.get("type").toString());
                        intent.putExtra("standard_kid_", map.get("standard_kid_").toString());
                        intent.putExtra("fid", fid);
                        break;
                }

                if (intent != null) {
                    if(fastClick()){
                     startActivity(intent);
                    }

                }

            }
        });

    }


    public void getUrlRulest() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.rkList);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    obj.put("fid", fid);

                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());

                    LogUtil.e("入科最外层列表", result);

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
