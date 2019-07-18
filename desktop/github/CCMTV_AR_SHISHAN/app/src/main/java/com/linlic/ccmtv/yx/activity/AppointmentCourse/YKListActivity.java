package com.linlic.ccmtv.yx.activity.AppointmentCourse;

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
import com.linlic.ccmtv.yx.activity.AppointmentCourse.adapter.YKListAdapter;
import com.linlic.ccmtv.yx.activity.AppointmentCourse.entity.YKListEntity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.home.ScanActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**约课列表
 * Created by bentley on 2019/1/9.
 */

public class YKListActivity extends BaseActivity {
    //每页显示多少条
    private static final int PAGE_LIMIT = 20;

    @Bind(R.id.tv_signup_list)
    TextView mTvSignupList;
    @Bind(R.id.tv_yk_list)
    TextView mTvYkList;
    @Bind(R.id.entry_month_nodata)
    NodataEmptyLayout emptyView;
    @Bind(R.id.listView)
    XRecyclerView xRecyclerView;

    private Context context;
    private YKListAdapter ykListAdapter;
    public int currPage = 1;
    private List<YKListEntity> listData = new ArrayList<YKListEntity>();
    private String fid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.yk_list_activity);
        context = this;
        ButterKnife.bind(this);
        fid = getIntent().getStringExtra("fid");
        initViews();
        getYkList();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/Yueke.html";
        super.onPause();
    }

    private void initViews(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getYkList();
            }

            @Override
            public void onLoadMore() {
                getMoreYkList();
            }
        });

        ykListAdapter = new YKListAdapter(this);
        ykListAdapter.setItemClickListener(new YKListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, YKListEntity entrty) {
                Intent intent = new Intent(context, YKDetailActivity.class);
                intent.putExtra("id", entrty.getId());
                intent.putExtra("fid", fid);
                startActivity(intent);
            }
        });
        xRecyclerView.setAdapter(ykListAdapter);
        mTvSignupList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTvYkList.setVisibility(View.VISIBLE);
                mTvSignupList.setVisibility(View.GONE);
                getYkList();
            }
        });
        mTvYkList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTvSignupList.setVisibility(View.VISIBLE);
                mTvYkList.setVisibility(View.GONE);
                getYkList();
            }
        });


    }

    public void clickCaptureActivity(View view){
        Intent intent1 = new Intent(context,ScanActivity.class);     //二维码和AR扫描界面
        intent1.putExtra("defaultType",0);
        startActivity(intent1);
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
     * 刷新约课列数数据
     */
    private void getYkList(){
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.ykList);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    obj.put("page", 1);
                    obj.put("fid", fid);
                    obj.put("limit", PAGE_LIMIT);
                    if (mTvYkList.getVisibility()==View.VISIBLE) {
                        obj.put("type", 1);
                    }
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());

                    LogUtil.e("刷新获取约课列数数据", result);

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
     * 加载更多约课列数数据
     */
    private void getMoreYkList(){
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.ykList);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    obj.put("page", currPage);
                    obj.put("limit", PAGE_LIMIT);
                    if (mTvYkList.getVisibility()==View.VISIBLE) {
                        obj.put("type", 1);
                    }
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());

                    LogUtil.e("loadmore约课列数数据", result);

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
                                    YKListEntity ykListEntity = new YKListEntity();
                                    ykListEntity.setId(dataJson1.getString("id"));
                                    ykListEntity.setTitle(dataJson1.getString("title")+"   ");
                                    ykListEntity.setManage_id(dataJson1.getString("manage_id"));
                                    ykListEntity.setStart_time(dataJson1.getString("start_time"));
                                    ykListEntity.setEnd_time(dataJson1.getString("end_time"));
                                    ykListEntity.setNotice_id(dataJson1.getString("notice_id"));
                                    ykListEntity.setMax_num(dataJson1.getString("max_num"));
                                    ykListEntity.setEnroll_num(dataJson1.getString("enroll_num"));
                                    ykListEntity.setUsername(dataJson1.getString("username"));
                                    ykListEntity.setStatus_name(dataJson1.getString("status_name"));
                                    ykListEntity.setNotice_name(dataJson1.getString("notice_name"));
                                    ykListEntity.setNum(dataJson1.getString("num"));
                                    ykListEntity.setCreatetime(dataJson1.getString("createtime"));
                                    listData.add(ykListEntity);
                                }
                                if (dateJson.length() == 0) {
                                    //无数据
                                    listData.clear();
                                    ykListAdapter.clearData();
                                    xRecyclerView.setVisibility(View.GONE);
                                    emptyView.setVisibility(View.VISIBLE);
                                } else if (dateJson.length() > 0) {
                                    xRecyclerView.setVisibility(View.VISIBLE);
                                    emptyView.setVisibility(View.GONE);
                                    currPage = 2;
                                    ykListAdapter.refresh(listData);
                                    xRecyclerView.scrollToPosition(0);
                                }
                                xRecyclerView.refreshComplete();
                            } else {
//                                Toast.makeText(context, dataJson.getString("statusMsg"), Toast.LENGTH_SHORT).show();
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
                                listData.clear();
                                for (int i = 0; i < dateJson.length(); i++) {
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    YKListEntity ykListEntity = new YKListEntity();
                                    ykListEntity.setId(dataJson1.getString("id"));
                                    ykListEntity.setTitle(dataJson1.getString("title"));
                                    ykListEntity.setManage_id(dataJson1.getString("manage_id"));
                                    ykListEntity.setStart_time(dataJson1.getString("start_time"));
                                    ykListEntity.setEnd_time(dataJson1.getString("end_time"));
                                    ykListEntity.setNotice_id(dataJson1.getString("notice_id"));
                                    ykListEntity.setMax_num(dataJson1.getString("max_num"));
                                    ykListEntity.setUsername(dataJson1.getString("username"));
                                    ykListEntity.setStatus_name(dataJson1.getString("status_name"));
                                    ykListEntity.setNotice_name(dataJson1.getString("notice_name"));
                                    ykListEntity.setNum(dataJson1.getString("num"));
                                    ykListEntity.setCreatetime(dataJson1.getString("createtime"));
                                    listData.add(ykListEntity);
                                }
                                if (dateJson.length() > 0) {
                                    currPage++;
                                    ykListAdapter.addData(listData);
                                }
                                xRecyclerView.loadMoreComplete();
                            } else {
//                                Toast.makeText(context, dataJson.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                        MyProgressBarDialogTools.hide();
//                        setResultStatus(listData.size() > 0, jsonObject.getInt("code"));
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
