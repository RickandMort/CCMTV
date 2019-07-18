package com.linlic.ccmtv.yx.activity.integral_mall;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Exchange_record_entity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/4.
 */

public class Exchange_record extends BaseActivity implements MyItemClickListener {
    private RecyclerView recyclerView;
    private Context context;
    private Exchange_recordAdapter exchange_recordAdapter;
    private List<Exchange_record_entity> datas = new ArrayList<>();
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    JSONObject result = null;
                    datas.clear();
                    try {
                        result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {// 成功
                            JSONArray dataArray = result.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                datas.add(new Exchange_record_entity(dataArray.getJSONObject(i)));
                            }
                            exchange_recordAdapter.notifyDataSetChanged();
                        } else {                                                                        // 失败
                            Toast.makeText(Exchange_record.this, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        setResultStatus(datas.size() > 0, result.getInt("status"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        setResultStatus(datas.size() > 0, 0);
                    }
                    break;
                default:
                    break;
            }
        }
    };
    private NodataEmptyLayout exchange_nodata;

    private void setResultStatus(boolean status, int code) {
        if (status) {
            recyclerView.setVisibility(View.VISIBLE);
            exchange_nodata.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                exchange_nodata.setNetErrorIcon();
            } else {
                exchange_nodata.setLastEmptyIcon();
            }
            recyclerView.setVisibility(View.GONE);
            exchange_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.exchange_record);
        context = this;
        findId();
        init();
        setValue2();
    }

    @Override
    public void findId() {
        super.findId();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_test_rv);
        exchange_nodata = (NodataEmptyLayout) findViewById(R.id.exchange_record_nodata);
    }

    public void init() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        exchange_recordAdapter = new Exchange_recordAdapter(datas);
        recyclerView.setAdapter(exchange_recordAdapter);
        this.exchange_recordAdapter.setOnItemClickListener(this);
    }

    public void setValue2() {
//        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.exchange);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP_Commodity, obj.toString());

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

    @Override
    public void onItemClick(View view, int postion) {
        Exchange_record_entity bean = datas.get(postion);
        if (bean != null) {
            Intent intent = new Intent(context, Order_details.class);
            intent.putExtra("id", bean.getId());
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn/Member/Index.html";
        super.onPause();
    }

}
