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
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
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
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ExamStationListActivity extends BaseActivity {
    @Bind(R.id.layout_nodata)
    NodataEmptyLayout layoutNodata;
    private Context context;
    @Bind(R.id.arrow_back)
    LinearLayout arrowBack;
    @Bind(R.id.activity_title_name)
    TextView activityTitleName;
    @Bind(R.id.station_recyclerview)
    RecyclerView stationRecyclerview;
    private BaseRecyclerViewAdapter adapter;
    private List<ListInfo> list_data = new ArrayList<>();
    private String fid = "";
    private String detail_id = "";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getString("code").equals("200")) { // 成功
                            JSONObject data = result.getJSONObject("data");
                            if (data.getString("status").equals("1")) {
                                JSONArray jsonArray = data.getJSONArray("data");
                                if (jsonArray.length() == 0) {
                                    layoutNodata.setVisibility(View.VISIBLE);
                                    stationRecyclerview.setVisibility(View.GONE);
                                }
                                if (jsonArray.length() != 0) {
                                    layoutNodata.setVisibility(View.GONE);
                                    stationRecyclerview.setVisibility(View.VISIBLE);
                                    list_data = JsonUtils.fromJsonArray(jsonArray.toString(), ListInfo.class);
                                    if (list_data.size() != 0) {
                                        adapter = new BaseRecyclerViewAdapter(R.layout.adapter_examstationlist, list_data) {
                                            @Override
                                            public void convert(BaseViewHolder helper, Object item) {
                                                super.convert(helper, item);
                                                ListInfo data = (ListInfo) item;
                                                helper.setText(R.id.tv_name, data.getStation_name());
                                                helper.setText(R.id.tv_num, data.getNo_score()+"人");
                                                helper.setText(R.id.tv_address, data.getSite_name());
                                                helper.setText(R.id.tv_date, data.getS_time() + " 至 " + data.getE_time());
                                            }
                                        };
                                        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
                                        adapter.isFirstOnly(false);
                                        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                                        stationRecyclerview.setLayoutManager(layoutManager);
                                        stationRecyclerview.setAdapter(adapter);
                                        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                                Intent intent = new Intent(ExamStationListActivity.this, ExamStationStudentListActivity.class);
                                                intent.putExtra("fid",fid);
                                                intent.putExtra("id",list_data.get(position).getId());
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            } else {
                                Toast.makeText(context, data.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, result.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        MyProgressBarDialogTools.hide();
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    MyProgressBarDialogTools.hide();
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_station_list);
        ButterKnife.bind(this);
        context = ExamStationListActivity.this;
        getIntentData();
    }

    private void getIntentData() {
        activityTitleName.setText(getIntent().getStringExtra("title_name"));
        activityTitleName.setSelected(true);
        fid = getIntent().getStringExtra("fid");
        detail_id = getIntent().getStringExtra("detail_id");
    }

    @Override
    protected void onResume() {
        super.onResume();
        initdata();
    }

    private void initdata() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getStageExaminerList);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("fid", fid);
                    obj.put("detail_id", detail_id);
                    obj.put("new", "1");
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("单多站考核详情列表数据：", result);

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


    class ListInfo implements Serializable {
        private String id;
        private String station_name;
        private String s_time;
        private String e_time;
        private String site_name;
        private String no_score;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStation_name() {
            return station_name;
        }

        public void setStation_name(String station_name) {
            this.station_name = station_name;
        }

        public String getS_time() {
            return s_time;
        }

        public void setS_time(String s_time) {
            this.s_time = s_time;
        }

        public String getE_time() {
            return e_time;
        }

        public void setE_time(String e_time) {
            this.e_time = e_time;
        }

        public String getSite_name() {
            return site_name;
        }

        public void setSite_name(String site_name) {
            this.site_name = site_name;
        }

        public String getNo_score() {
            return no_score;
        }

        public void setNo_score(String no_score) {
            this.no_score = no_score;
        }
    }
}
