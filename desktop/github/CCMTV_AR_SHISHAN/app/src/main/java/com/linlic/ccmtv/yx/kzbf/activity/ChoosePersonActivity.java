package com.linlic.ccmtv.yx.kzbf.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.kzbf.adapter.WriteMessageAdapter;
import com.linlic.ccmtv.yx.kzbf.bean.DbMedicineDetial;
import com.linlic.ccmtv.yx.kzbf.widget.RecyclerViewNoBugLinearLayoutManager;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChoosePersonActivity extends BaseActivity {
    Context context;
    private RecyclerView recyclerView;
    private NodataEmptyLayout lt_nodata1;
    private WriteMessageAdapter wmAdapter;
    private List<DbMedicineDetial> list;
    private List<DbMedicineDetial> listMore = new ArrayList<>();
    private DbMedicineDetial dbMd;
    private String helper;
    private String id;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    listMore.clear();
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        String code = jsonObject.getString("code");
                        if (code.equals("0")) { // 成功
                            list = new ArrayList<>();
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                dbMd = new DbMedicineDetial(1);
                                dbMd.setRecommend_id(object.getString("uid"));
                                dbMd.setRecommend_title(object.getString("helper"));
                                list.add(dbMd);
                            }
                            listMore.addAll(list);
                            wmAdapter.loadMoreComplete();
                            wmAdapter.notifyDataSetChanged();
                        } else {
                            wmAdapter.loadMoreFail();
                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                        if ("code".equals(code)) {
                            setResultStatus(listMore.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                        } else {
                            setResultStatus(listMore.size() > 0, Integer.valueOf(code));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        setResultStatus(listMore.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                    }
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    setResultStatus(listMore.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                    break;

                default:
                    break;
            }
        }
    };

    private void setResultStatus(boolean status, int code) {
        if (status) {
            recyclerView.setVisibility(View.VISIBLE);
            lt_nodata1.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                lt_nodata1.setNetErrorIcon();
            } else {
                lt_nodata1.setLastEmptyIcon();
            }
            recyclerView.setVisibility(View.GONE);
            lt_nodata1.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_person);
        context = this;

        initView();
        initData();
        setValue();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.send_fouse_list);
        lt_nodata1 = (NodataEmptyLayout) findViewById(R.id.rl_choose_nodata1);
    }

    private void initData() {
        recyclerView.setLayoutManager(new RecyclerViewNoBugLinearLayoutManager(context));
        wmAdapter = new WriteMessageAdapter(context, listMore);
        recyclerView.setAdapter(wmAdapter);
        wmAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                helper = listMore.get(position).getRecommend_title();
                id = listMore.get(position).getRecommend_id();
                Intent intent = new Intent();
                intent.putExtra("helper", helper);
                intent.putExtra("id", id);
                setResult(1, intent);
                finish();
            }
        });
    }

    private void setValue() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.focusList);
                    obj.put("uid", SharedPreferencesTools.getUid(context));

                    String result = HttpClientUtils.sendPost(context, URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看发消息下拉列表数据", result);

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
    public void back(View view) {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn";
        super.onPause();
    }

}
