package com.linlic.ccmtv.yx.activity.bigcase;

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
import com.linlic.ccmtv.yx.activity.bigcase.adapter.BigCaseUserAdapter;
import com.linlic.ccmtv.yx.activity.bigcase.entity.BigcaseUserEntity;
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

public class BigcaseUserListActivity extends BaseActivity {
    //每页显示多少条
    private static final int PAGE_LIMIT = 20;

    @Bind(R.id.tv_sbu_left_title)
    TextView tvSbuLeftTitle;
    @Bind(R.id.lt_nodata1)
    NodataEmptyLayout emptyView;
    @Bind(R.id.listView)
    XRecyclerView xRecyclerView;
    @Bind(R.id.activity_title_name)
    TextView mTvTittle;

    private Context context;
    private BigCaseUserAdapter userAdapter;
    private String fid;
    private String case_id;
    private String year;
    private String month;
    private List<BigcaseUserEntity> listData = new ArrayList<>();
    public int currPage = 1;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_assgin_ks_index);
        context = this;
        ButterKnife.bind(this);
        fid = getIntent().getStringExtra("fid");
        case_id = getIntent().getStringExtra("case_id");
        year = getIntent().getStringExtra("year");
        month = getIntent().getStringExtra("month");
        initViews();
        getUserList();
    }

    private void initViews(){
        tvSbuLeftTitle.setText("学员");
        mTvTittle.setText("人员列表");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getUserList();
            }

            @Override
            public void onLoadMore() {
                loadMoreUserList();
            }
        });

        userAdapter = new BigCaseUserAdapter(this);
        userAdapter.setItemClickListener(new BigCaseUserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, BigcaseUserEntity entrty) {
                intent = new Intent(context, MonthBigCaseActivity.class);
                intent.putExtra("case_id", entrty.getCase_id());
                intent.putExtra("fid", fid);
                intent.putExtra("user_id",entrty.getUid());
                intent.putExtra("realname",entrty.getRealname());
                startActivity(intent);
            }
        });
        xRecyclerView.setAdapter(userAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/Bigcase/index.html";
        super.onPause();
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

    /***
     * 人员列表接口
     */
    private void getUserList(){
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getBigCaseUserList);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    obj.put("fid", fid);
                    obj.put("page", 1);
                    obj.put("limit", PAGE_LIMIT);
                    obj.put("year", year);
                    obj.put("month", month);
                    obj.put("case_id", case_id);
                    //fid   222员工   123学员
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

    /***
     * loadMore
     */
    private void loadMoreUserList(){
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getBigCaseUserList);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    obj.put("fid", fid);
                    obj.put("page", currPage);
                    obj.put("limit", PAGE_LIMIT);
                    obj.put("year", year);
                    obj.put("month", month);
                    obj.put("case_id", case_id);
                    //fid   222员工   123学员
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
                case 1://刷新
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
                                    BigcaseUserEntity userEntity = new BigcaseUserEntity();
                                    userEntity.setCase_id(dataJson1.getString("case_id"));
                                    userEntity.setRealname(dataJson1.getString("realname"));
                                    userEntity.setUid(dataJson1.getString("uid"));
                                    userEntity.setSex(dataJson1.getString("sex"));
                                    userEntity.setCount(dataJson1.getString("count"));
                                    listData.add(userEntity);
                                }
                                if (dateJson.length() <= 0) {
                                    //刷新无数据
                                    listData.clear();
                                    xRecyclerView.setVisibility(View.GONE);
                                    emptyView.setVisibility(View.VISIBLE);
                                    xRecyclerView.loadMoreComplete();
                                } else if (dateJson.length() > 0) {
                                    xRecyclerView.setVisibility(View.VISIBLE);
                                    emptyView.setVisibility(View.GONE);
                                    currPage = 2;
                                    userAdapter.refresh(listData);
                                    xRecyclerView.loadMoreComplete();
                                }
                                userAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, dataJson.getString("statusMsg"), Toast.LENGTH_SHORT).show();
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
                case 2://加载更多
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            if (dataJson.getInt("status") == 1) {
                                // 成功  1存在数据 0不存在数据
                                JSONArray dateJson = dataJson.getJSONArray("data");
                                for (int i = 0; i < dateJson.length(); i++) {
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    BigcaseUserEntity userEntity = new BigcaseUserEntity();
                                    userEntity.setCase_id(dataJson1.getString("case_id"));
                                    userEntity.setRealname(dataJson1.getString("realname"));
                                    userEntity.setUid(dataJson1.getString("uid"));
                                    userEntity.setSex(dataJson1.getString("sex"));
                                    userEntity.setCount(dataJson1.getString("count"));
                                    listData.add(userEntity);
                                }
                                if (dateJson.length() > 0) {
                                    currPage++;
                                    userAdapter.addData(listData);
                                    userAdapter.notifyDataSetChanged();
                                }
                                xRecyclerView.loadMoreComplete();
                            } else {
                                Toast.makeText(context, dataJson.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                            }
                            MyProgressBarDialogTools.hide();
                        } else {
                            MyProgressBarDialogTools.hide();
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
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
