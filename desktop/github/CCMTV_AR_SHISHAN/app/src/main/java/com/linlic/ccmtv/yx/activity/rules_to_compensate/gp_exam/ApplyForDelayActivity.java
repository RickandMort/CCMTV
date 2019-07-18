package com.linlic.ccmtv.yx.activity.rules_to_compensate.gp_exam;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseViewHolder;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.adapter.BaseRecyclerViewAdapter;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.JsonUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.permission.SpacesItemDecoration;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ApplyForDelayActivity extends BaseActivity {

    @Bind(R.id.arrow_back)
    LinearLayout arrowBack;
    @Bind(R.id.apply_delay)
    LinearLayout applyDelay;
    @Bind(R.id.my_recyclerview)
    RecyclerView myRecyclerview;
    @Bind(R.id.layout_nodata)
    NodataEmptyLayout layoutNodata;
    private List<ListInfo> data = new ArrayList<>();
    private BaseRecyclerViewAdapter adapter;
    private Context context;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getString("code").equals("200")) {
                            JSONObject data = result.getJSONObject("data");
                            String is_rotate = data.getString("is_rotate");
                            if(is_rotate.equals("1")){
                                applyDelay.setVisibility(View.VISIBLE);
                            }else if(is_rotate.equals("2")){
                                applyDelay.setVisibility(View.GONE);
                            }
                            if (data.getString("status").equals("1")) {
                                JSONArray jsonArray = data.getJSONArray("data");
                                List<ListInfo> datas = JsonUtils.fromJsonArray(jsonArray.toString(), ListInfo.class);
                                if (datas.size() > 0) {
                                    layoutNodata.setVisibility(View.GONE);
                                    myRecyclerview.setVisibility(View.VISIBLE);
                                    adapter = new BaseRecyclerViewAdapter(R.layout.adapter_applyfor_delay, datas) {
                                        @Override
                                        public void convert(BaseViewHolder helper, Object item) {
                                            super.convert(helper, item);
                                            ListInfo info = (ListInfo) item;
                                            helper.setText(R.id.tv_department, info.name);
                                            helper.setText(R.id.tv_plan_time, info.plan_time);
                                            helper.setText(R.id.tv_start_time, info.start_time);
                                            helper.setText(R.id.tv_end_time, info.end_time);
                                            helper.setText(R.id.tv_reason, info.content);
                                            String status = info.status;
                                            if (status.equals("1")) {
                                                helper.setBackgroundRes(R.id.tv_status, R.drawable.delay_status2);
                                                helper.setText(R.id.tv_status, "审核中");
                                            } else if (status.equals("2")) {
                                                helper.setBackgroundRes(R.id.tv_status, R.drawable.delay_status1);
                                                helper.setText(R.id.tv_status, "已通过");
                                            } else if (status.equals("3")) {
                                                helper.setBackgroundRes(R.id.tv_status, R.drawable.delay_status3);
                                                helper.setText(R.id.tv_status, "未通过");
                                                helper.setTextColor(R.id.tv_status,context.getResources().getColor(R.color.black33));
                                            }
                                        }
                                    };
                                    myRecyclerview.setAdapter(adapter);
                                }
                            } else {
                                String message = data.getString("errorMessage");
                                if (message.equals("暂无数据")) {
                                    layoutNodata.setVisibility(View.VISIBLE);
                                    myRecyclerview.setVisibility(View.GONE);
                                } else {
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        MyProgressBarDialogTools.hide();
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_for_delay);
        context = ApplyForDelayActivity.this;
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        myRecyclerview.setLayoutManager(layoutManager);
        myRecyclerview.addItemDecoration(new SpacesItemDecoration(20));
        myRecyclerview.setNestedScrollingEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initdata();
    }

    private void initdata() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getPostponeList);
                    obj.put("uid", SharedPreferencesTools.getUid(ApplyForDelayActivity.this));
                    String result = HttpClientUtils.sendPostToGP(ApplyForDelayActivity.this, URLConfig.GpApplyfordelay, obj.toString());
                    LogUtil.e("延时出科申请列表数据：", result);
                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    @OnClick({R.id.arrow_back, R.id.apply_delay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.arrow_back:
                finish();
                break;
            case R.id.apply_delay:
                Intent intent = new Intent(context,MakeApplyForDelayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("fid",getIntent().getStringExtra("fid"));
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

    class ListInfo implements Serializable {
        private String id;
        private String uid;
        private String plan_time;
        private String start_time;
        private String end_time;
        private String hos_id;
        private String standard_kid;
        private String content;
        private String status;
        private String add_time;
        private String name;
    }
}
