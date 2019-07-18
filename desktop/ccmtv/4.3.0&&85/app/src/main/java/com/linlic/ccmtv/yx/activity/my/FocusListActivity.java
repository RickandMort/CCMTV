package com.linlic.ccmtv.yx.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FocusListActivity extends BaseActivity {

    @Bind(R.id.arrow_back)
    LinearLayout arrowBack;
    @Bind(R.id.activity_title_name)
    TextView activityTitleName;
    @Bind(R.id.tv_num)
    TextView tvNum;
    @Bind(R.id.my_listview)
    ListView myListview;
    @Bind(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @Bind(R.id.layout_nodata)
    NodataEmptyLayout layoutNodata;
    private List<Map<String, Object>> data2 = new ArrayList<Map<String, Object>>();
    private BaseListAdapter baseListAdapter;
    private int page = 1;
    private TextView tv_focus;
    private String count;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功
                            count = result.getString("count");
                            tvNum.setText("共关注" + result.getString("count") + "位专家");
                            JSONArray dataArray = result.getJSONArray("data");
                            if (dataArray.length() == 0) {
                                Toast.makeText(FocusListActivity.this, "暂无更多数据", Toast.LENGTH_SHORT).show();
                                MyProgressBarDialogTools.hide();

                            } else {
                                tvNum.setVisibility(View.VISIBLE);
                                layoutNodata.setVisibility(View.GONE);
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject object = dataArray.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("uid", object.getString("uid"));
                                    map.put("keshi", object.getString("keshi"));
                                    map.put("icon", object.getString("icon"));
                                    map.put("username", object.getString("username"));
                                    map.put("name", object.getString("name"));
                                    map.put("cityid", object.getString("cityid"));
                                    map.put("isMyColleague", object.getString("isMyColleague"));
                                    map.put("is_myattention", object.getString("is_myattention"));
                                    data2.add(map);
                                }
                                baseListAdapter = new BaseListAdapter(myListview, data2, R.layout.adapter_myfocus_list) {

                                    @Override
                                    public void refresh(Collection datas) {
                                        super.refresh(datas);
                                    }

                                    @Override
                                    public void convert(ListHolder helper, final Object item, boolean isScrolling, final int position) {
                                        super.convert(helper, item, isScrolling);
                                        helper.setText(R.id.tv_username, ((Map) item).get("username") + "");
                                        helper.setText(R.id.tv_yiyuan_name, ((Map) item).get("keshi") + "");
                                        ImageOptions options = new ImageOptions.Builder()
                                                .setCircular(true).build();
                                        x.image().bind((ImageView) helper.getView(R.id.iv_headImg), ((Map) item).get("icon") + "", options);
                                        tv_focus = (TextView) helper.getView(R.id.tv_focus);
                                        tv_focus.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                queryIsFollow(((Map) item).get("uid") + "", position);
                                            }
                                        });
                                        String type = ((Map) item).get("is_myattention") + "";
                                        if (type.equals("1")) {
                                            tv_focus.setText("已关注");
                                            tv_focus.setTextColor(getResources().getColor(R.color.blue_bg));
                                            tv_focus.setBackground(getResources().getDrawable(R.drawable.guan));
                                        } else {
                                            tv_focus.setText("关注");
                                            tv_focus.setTextColor(getResources().getColor(R.color.white));
                                            tv_focus.setBackground(getResources().getDrawable(R.drawable.yi_guan));
                                        }
                                    }
                                };
                                myListview.setAdapter(baseListAdapter);
                                myListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Intent intent = new Intent(FocusListActivity.this, MyFollowDetails.class);
                                        intent.putExtra("Hisuid", data2.get(position).get("uid").toString());
                                        intent.putExtra("Str_username", data2.get(position).get("username").toString());
                                        intent.putExtra("isMyColleague", "0");
                                        startActivity(intent);
                                    }
                                });
                                MyProgressBarDialogTools.hide();
                            }
                        } else {
                            layoutNodata.setVisibility(View.VISIBLE);
                            tvNum.setVisibility(View.GONE);
                            showNoData();
                            MyProgressBarDialogTools.hide();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case 5:
                    try {
                        int posi = msg.arg1;
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {// 成功
                            data2.get(posi).put("is_myattention", result.getString("data"));
                            baseListAdapter.notifyDataSetChanged();
                        } else {// 失败
                            Toast.makeText(FocusListActivity.this, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;

                case 500:
                    MyProgressBarDialogTools.hide();
                    Toast.makeText(FocusListActivity.this, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus_list);
        ButterKnife.bind(this);
        initdata();
        refresh();
    }

    private void refresh() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                data2.clear();
                page = 1;
                initdata();
                refreshlayout.finishRefresh();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                double num = Double.valueOf(count) / 10;
                if (num > page) {
                    initdata();
                    page = page + 1;
                } else {
                    Toast.makeText(FocusListActivity.this, "暂无更多数据", Toast.LENGTH_SHORT).show();
                }
                refreshlayout.finishLoadmore();
            }
        });
    }

    private void initdata() {
        MyProgressBarDialogTools.show(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject object = new JSONObject();
                    object.put("uid", SharedPreferencesTools.getUid(FocusListActivity.this));
                    object.put("workmate_uid", getIntent().getExtras().getString("myuid"));
                    object.put("page", page);
                    object.put("act", URLConfig.getWorkmateAttention);
                    String result = HttpClientUtils.sendPost(FocusListActivity.this, URLConfig.CCMTVAPP, object.toString());
                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        }).start();
    }

    /**
     * 关注、、、取消关注
     */
    public void queryIsFollow(final String hisuid, final int position) {
        MyProgressBarDialogTools.show(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject object = new JSONObject();
                    object.put("uid", SharedPreferencesTools.getUid(FocusListActivity.this));
                    object.put("auid", hisuid);
                    object.put("act", URLConfig.attention);
                    String result = HttpClientUtils.sendPost(FocusListActivity.this,
                            URLConfig.CCMTVAPP, object.toString());
                    Message message = new Message();
                    message.what = 5;
                    message.arg1 = position;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        }).start();
    }

    @OnClick(R.id.arrow_back)
    public void onViewClicked() {
        finish();
    }
}
