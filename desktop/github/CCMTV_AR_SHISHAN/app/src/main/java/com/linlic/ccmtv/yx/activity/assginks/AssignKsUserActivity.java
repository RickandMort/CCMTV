package com.linlic.ccmtv.yx.activity.assginks;

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
import com.linlic.ccmtv.yx.activity.assginks.adapter.AssginKsUserAdapter;
import com.linlic.ccmtv.yx.activity.assginks.entity.AssignKsUserEntity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
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
 * Created by bentley on 2019/1/15.
 */

public class AssignKsUserActivity extends BaseActivity {
    @Bind(R.id.lt_nodata1)
    NodataEmptyLayout emptyView;
    @Bind(R.id.listView)
    XRecyclerView xRecyclerView;
    @Bind(R.id.activity_title_name)
    TextView mTvTittle;

    private Context context;
    private String year;
    private String month;
    private AssginKsUserAdapter assginKsUserAdapter;
    private List<AssignKsUserEntity> listData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_assgin_ks_user);
        context = this;
        ButterKnife.bind(this);
        initViews();
        getAssignUserList();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/rk/assginKsIndex.html";
        super.onPause();
    }

    private void initViews(){
        year = getIntent().getStringExtra("year");
        month = getIntent().getStringExtra("month");
        mTvTittle.setText( getIntent().getStringExtra("title"));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);

        assginKsUserAdapter = new AssginKsUserAdapter(this);
        assginKsUserAdapter.setItemClickListener(new AssginKsUserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, AssignKsUserEntity entrty) {
                Intent intent = new Intent(context, AssignKsDetailActivity.class);
                intent.putExtra("details_id", entrty.getDetails_id());
                startActivity(intent);
            }
        });
        xRecyclerView.setAdapter(assginKsUserAdapter);
    }

    /**
     * 设置空界面
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
     * 入科分配科室每月人员列表
     */
    private void getAssignUserList(){
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getAssginKsUsers);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    obj.put("year", year);
                    obj.put("month", month);
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
                                    AssignKsUserEntity ksListEntity = new AssignKsUserEntity();
                                    ksListEntity.setRealname(dataJson1.getString("realname"));
                                    ksListEntity.setCycle_ks(dataJson1.getString("cycle_ks"));
                                    ksListEntity.setAssign_ks(dataJson1.getString("assign_ks"));
                                    ksListEntity.setStatus(dataJson1.getString("status"));
                                    ksListEntity.setDetails_id(dataJson1.getString("details_id"));
                                    listData.add(ksListEntity);
                                }
                                if (dateJson.length() == 0) {
                                    //无数据
                                    listData.clear();
                                    assginKsUserAdapter.clearData();
                                    xRecyclerView.setVisibility(View.GONE);
                                    emptyView.setVisibility(View.VISIBLE);
                                } else if (dateJson.length() > 0) {
                                    xRecyclerView.setVisibility(View.VISIBLE);
                                    emptyView.setVisibility(View.GONE);
                                    assginKsUserAdapter.refresh(listData);
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
