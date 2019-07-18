package com.linlic.ccmtv.yx.activity.rules_to_compensate.evaluation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.adapter.EvaluationBaseAdapter;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.javabean.BaseEntity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 评价基地
 * Created by bentley on 2019/1/18.
 */

public class EvaluationBase extends BaseActivity {
    @Bind(R.id.lt_nodata1)
    NodataEmptyLayout emptyView;
    @Bind(R.id.listView)
    XRecyclerView xRecyclerView;
    @Bind(R.id.activity_title_name)
    TextView activityTitleName;
    @Bind(R.id.tv_sub_left)
    TextView tvSubLeft;

    private Context context;
    private EvaluationBaseAdapter baseAdapter;
    private String fid;
    private String id;
    private String flag;
    private String year;
    private String month;
    private List<BaseEntity> listData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.the_evaluation_of_base_list);
        context = this;
        ButterKnife.bind(this);
        flag = getIntent().getStringExtra("flag");
        if (flag.equals("Base")) {
            fid = getIntent().getStringExtra("fid");
            id = getIntent().getStringExtra("id");
            activityTitleName.setText("评价基地");
            tvSubLeft.setText("专业基地");
        } else if (flag.equals("ks")) {
            year = getIntent().getStringExtra("year");
            month = getIntent().getStringExtra("month");
            activityTitleName.setText("评价科室");
            tvSubLeft.setText("科室");
        }

        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (flag.equals("Base")) {
            getBaseList();
        } else if (flag.equals("ks")) {
            getksList();
        }

    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/Allappraise/manage_index.html";
        super.onPause();
    }

    private void initViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);

        baseAdapter = new EvaluationBaseAdapter(this);
        baseAdapter.setItemClickListener(new EvaluationBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, BaseEntity entrty) {
                Intent intent = new Intent(context, EvaluationBaseDetailActivity.class);
                intent.putExtra("fid", fid);
                intent.putExtra("master_id", entrty.getMaster_id());
                intent.putExtra("base_id", entrty.getBase_id());
                intent.putExtra("detail_id", entrty.getDetail_id());
                intent.putExtra("flag",flag);
                if(flag.equals("ks")){
                intent.putExtra("id", entrty.getId());
                }
                startActivity(intent);
            }
        });
        xRecyclerView.setAdapter(baseAdapter);
    }

    /**
     * 设置空界面
     *
     * @param code
     */
    private void setResultStatus(int code) {
        if (HttpClientUtils.isNetConnectError(context, code)) {
            emptyView.setNetErrorIcon();
        } else {
            emptyView.setLastEmptyIcon();
        }
        xRecyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }

    /**
     * 获取基地列表
     */
    private void getBaseList() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getUserBaseList);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    obj.put("fid", fid);
                    obj.put("id", id);
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

    /**
     * 获取科室列表
     */
    private void getksList() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getUserKsList);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    obj.put("year", year);
                    obj.put("month", month);
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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            if (dataJson.getInt("status") == 1) {
                                // 成功  1存在数据 0不存在数据
                                JSONArray dateJson = dataJson.getJSONArray("data");
                                listData.clear();
                                for (int i = 0; i < dateJson.length(); i++) {
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    BaseEntity baseEntity = new BaseEntity();
                                    baseEntity.setBase_id(dataJson1.getString("base_id"));
                                    baseEntity.setName(dataJson1.getString("name"));
                                    baseEntity.setHosid(dataJson1.getString("hosid"));
                                    baseEntity.setMaster_id(dataJson1.getString("master_id"));
                                    baseEntity.setScore(dataJson1.getString("score"));
                                    baseEntity.setDetail_id(dataJson1.getString("detail_id"));
                                    listData.add(baseEntity);
                                }
                                if (dateJson.length() == 0) {
                                    //无数据
                                    listData.clear();
                                    baseAdapter.clearData();
                                    xRecyclerView.setVisibility(View.GONE);
                                    emptyView.setVisibility(View.VISIBLE);
                                } else if (dateJson.length() > 0) {
                                    xRecyclerView.setVisibility(View.VISIBLE);
                                    emptyView.setVisibility(View.GONE);
                                    baseAdapter.refresh(listData);
                                }
                                xRecyclerView.refreshComplete();
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                        setResultStatus(HttpClientUtils.UNKONW_EXCEPTION_CODE);
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            if (dataJson.getInt("status") == 1) {
                                // 成功  1存在数据 0不存在数据
                                JSONArray dateJson = dataJson.getJSONArray("data");
                                listData.clear();
                                for (int i = 0; i < dateJson.length(); i++) {
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    BaseEntity baseEntity = new BaseEntity();
                                    baseEntity.setId(dataJson1.getString("id"));
                                    baseEntity.setName(dataJson1.getString("name"));
                                    baseEntity.setScore(dataJson1.getString("score"));
                                    listData.add(baseEntity);
                                }
                                if (dateJson.length() == 0) {
                                    //无数据
                                    listData.clear();
                                    baseAdapter.clearData();
                                    xRecyclerView.setVisibility(View.GONE);
                                    emptyView.setVisibility(View.VISIBLE);
                                } else if (dateJson.length() > 0) {
                                    xRecyclerView.setVisibility(View.VISIBLE);
                                    emptyView.setVisibility(View.GONE);
                                    baseAdapter.refresh(listData);
                                }
                                xRecyclerView.refreshComplete();
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                        setResultStatus(HttpClientUtils.UNKONW_EXCEPTION_CODE);
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    setResultStatus(HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                    break;
                default:
                    break;
            }
        }
    };
}
