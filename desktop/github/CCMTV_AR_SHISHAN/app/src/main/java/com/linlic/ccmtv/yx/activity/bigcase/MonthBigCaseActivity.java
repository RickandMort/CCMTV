package com.linlic.ccmtv.yx.activity.bigcase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.bigcase.adapter.BigCaseMonthAdapter;
import com.linlic.ccmtv.yx.activity.bigcase.entity.BigCaseMonthEntrry;
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
 * Created by bentley on 2018/12/18.
 */

public class MonthBigCaseActivity extends BaseActivity {
    //每页显示多少条
    private static final int PAGE_LIMIT = 20;

    @Bind(R.id.tv_add_bigcase)
    TextView mTvAddBigcase;
    @Bind(R.id.listView)
    XRecyclerView xRecyclerView;
    @Bind(R.id.entry_month_nodata)
    NodataEmptyLayout emptyView;
    @Bind(R.id.iv_back)
    ImageView iv_back;
    public static int is_new = 0;
    @Bind(R.id.tv_realname)
    TextView tvRealname;
    private Context context;
    private BigCaseMonthAdapter bigCaseMonthAdapter;
    private List<BigCaseMonthEntrry> listData = new ArrayList<BigCaseMonthEntrry>();
    private String caseId;
    public int currPage = 1;

    private String is_type;
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
                                is_type = dataJson.getString("is_type");//为1的话 是员工查看
                                JSONArray dateJson = dataJson.getJSONArray("data");
                                if (currPage == 1) {
                                    listData.clear();
                                }
                                for (int i = 0; i < dateJson.length(); i++) {
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    BigCaseMonthEntrry bigCaseYearMonthEntrty = new BigCaseMonthEntrry();
                                    bigCaseYearMonthEntrty.setId(dataJson1.getString("id"));
                                    bigCaseYearMonthEntrty.setTitle(dataJson1.getString("title"));
                                    bigCaseYearMonthEntrty.setIs_edit(dataJson1.getInt("is_edit"));
                                    bigCaseYearMonthEntrty.setTime(dataJson1.getString("time"));
                                    bigCaseYearMonthEntrty.setCount(dataJson1.getString("count"));
                                    bigCaseYearMonthEntrty.setImg(dataJson1.getString("img"));
                                    bigCaseYearMonthEntrty.setHttp_url(dataJson1.getString("http_url"));
                                    listData.add(bigCaseYearMonthEntrty);
                                }
                                if (currPage == 1 && dateJson.length() == 0) {
                                    //刷新无数据
                                    listData.clear();
                                    xRecyclerView.setVisibility(View.GONE);
                                    emptyView.setVisibility(View.VISIBLE);
                                } else if (dateJson.length() > 0) {
                                    xRecyclerView.setVisibility(View.VISIBLE);
                                    emptyView.setVisibility(View.GONE);
                                    currPage++;
                                    bigCaseMonthAdapter.refresh(listData);
                                }
                                xRecyclerView.loadMoreComplete();
                                bigCaseMonthAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, dataJson.getString("statusMsg"), Toast.LENGTH_SHORT).show();
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
                case 2://加载更多
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            if (dataJson.getInt("status") == 1) {
                                // 成功  1存在数据 0不存在数据
                                is_type = dataJson.getString("is_type");
                                JSONArray dateJson = dataJson.getJSONArray("data");
//                                listData.clear();
                                for (int i = 0; i < dateJson.length(); i++) {
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    BigCaseMonthEntrry bigCaseYearMonthEntrty = new BigCaseMonthEntrry();
                                    bigCaseYearMonthEntrty.setId(dataJson1.getString("id"));
                                    bigCaseYearMonthEntrty.setTitle(dataJson1.getString("title"));
                                    bigCaseYearMonthEntrty.setIs_edit(dataJson1.getInt("is_edit"));
                                    bigCaseYearMonthEntrty.setTime(dataJson1.getString("time"));
                                    bigCaseYearMonthEntrty.setCount(dataJson1.getString("count"));
                                    bigCaseYearMonthEntrty.setImg(dataJson1.getString("img"));
                                    bigCaseYearMonthEntrty.setHttp_url(dataJson1.getString("http_url"));
                                    listData.add(bigCaseYearMonthEntrty);
                                }
                                if (dateJson.length() > 0) {
                                    currPage++;
                                    bigCaseMonthAdapter.addData(listData);
                                    xRecyclerView.loadMoreComplete();
                                    bigCaseMonthAdapter.notifyDataSetChanged();
                                } else {
                                    xRecyclerView.loadMoreComplete();
                                }
                            } else {
                                Toast.makeText(context, dataJson.getString("statusMsg"), Toast.LENGTH_SHORT).show();
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
    private String fid;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.month_big_case_activity);
        context = this;
        ButterKnife.bind(this);
        is_new = 0;
        tvRealname.setText(getIntent().getStringExtra("realname"));
        tvRealname.setSelected(true);
        caseId = getIntent().getStringExtra("case_id");
        fid = getIntent().getStringExtra("fid");
        if ("222".equals(fid)) {
            //fid   222员工   123学员
            mTvAddBigcase.setVisibility(View.GONE);
        }
        user_id = getIntent().getStringExtra("user_id");
        findId();
        initViews();
        getBigCaseMonthList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (is_new > 0) {
            is_new = 0;
            currPage = 1;
            getBigCaseMonthList();
        }
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/Bigcase/index.html";
        super.onPause();
    }

    private void initViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                currPage = 1;
                getBigCaseMonthList();
            }

            @Override
            public void onLoadMore() {
                currPage = currPage + 1;
                getBigCaseMonthList();
            }
        });
        bigCaseMonthAdapter = new BigCaseMonthAdapter(this, is_type);
        xRecyclerView.setAdapter(bigCaseMonthAdapter);

        //添加
        mTvAddBigcase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BigCaseUpdateActivity.class);
                intent.putExtra("case_id", caseId);
                startActivity(intent);
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /***
     * 刷新内层用户已上传列表
     */
    private void getBigCaseMonthList() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getBigCaseUserUploadList);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    obj.put("page", currPage);
                    obj.put("limit", PAGE_LIMIT);
                    obj.put("case_id", caseId);
                    obj.put("fid", fid);
                    obj.put("user_id", user_id);
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
     * 加载更多大病历最外层列表
     */
    private void getMoreBigCaseMonthList() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getBigCaseUserUploadList);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    obj.put("page", currPage);
                    obj.put("limit", PAGE_LIMIT);
                    obj.put("case_id", caseId);
                    obj.put("fid", fid);
                    obj.put("user_id", user_id);
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
}
