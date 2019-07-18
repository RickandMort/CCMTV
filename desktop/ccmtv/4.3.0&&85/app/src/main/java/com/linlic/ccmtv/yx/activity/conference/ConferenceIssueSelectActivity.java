package com.linlic.ccmtv.yx.activity.conference;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.conference.adapter.ConferenceIssueSelectAdapter;
import com.linlic.ccmtv.yx.activity.conference.databean.ConferenceIssueBean;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ConferenceIssueSelectActivity extends BaseActivity {

    private TextView title_name;
    private ImageView ivTitleIconRight;
    private ListView lvIssueSelect;
    private ConferenceIssueSelectAdapter conferenceIssueSelectAdapter;
    private List<ConferenceIssueBean> conferenceIssueDatats = new ArrayList<>();
    private Context context;
    private String xid;
    private String currentFid;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {// 成功
                            conferenceIssueDatats.clear();
                            JSONArray data = result.getJSONArray("data");
                            if (data != null && data.length() > 0) {
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject issueObject=data.getJSONObject(i);
                                    ConferenceIssueBean issueBean = new ConferenceIssueBean();
                                    issueBean.setId(issueObject.getString("id"));
                                    issueBean.setFid(issueObject.getString("fid"));
                                    issueBean.setXid(issueObject.getString("xid"));
                                    issueBean.setFtitle(issueObject.getString("ftitle"));
                                    conferenceIssueDatats.add(issueBean);
                                }
                                conferenceIssueSelectAdapter.notifyDataSetChanged();

                                for (int j=0; j<conferenceIssueDatats.size(); j++){
                                    if (currentFid.equals(conferenceIssueDatats.get(j).getFid())) {
                                        conferenceIssueSelectAdapter.setSelect(j);
                                    }
                                }
                            }

                        } else {

                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conference_issue_select);
        context = this;
        findId();
        initData();
        initListView();
    }

    public void findId() {
        title_name = (TextView) findViewById(R.id.activity_title_name);
        ivTitleIconRight = (ImageView) findViewById(R.id.id_iv_activity_title_8_right);
        lvIssueSelect = (ListView) findViewById(R.id.id_lv_conference_issue_select);

        ivTitleIconRight.setVisibility(View.VISIBLE);

        ivTitleIconRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        title_name.setText("会议议题");
        title_name.setTextColor(Color.parseColor("#3698F9"));
        title_name.setTextSize(20);
        ivTitleIconRight.setImageResource(R.mipmap.conference_issue_selece_close);

        xid = getIntent().getStringExtra("xid");
        currentFid = getIntent().getStringExtra("fid");
        getConferenceIssueAll();
    }

    private void getConferenceIssueAll() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.secSpecial);
                    obj.put("xid", xid);
                    String result = HttpClientUtils.sendPost(context, URLConfig.conference, obj.toString());
//                    LogUtil.e("会议议题选择数据：", result);

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

    private void initListView() {
        conferenceIssueSelectAdapter = new ConferenceIssueSelectAdapter(this, conferenceIssueDatats);
        lvIssueSelect.setAdapter(conferenceIssueSelectAdapter);

        lvIssueSelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                conferenceIssueSelectAdapter.setSelect(position);
                Intent intent = new Intent(ConferenceIssueSelectActivity.this, ConferenceIssueVideoActivity.class);
                intent.putExtra("title", conferenceIssueDatats.get(position).getFtitle());
                intent.putExtra("fid",conferenceIssueDatats.get(position).getFid());
                startActivity(intent);
                finish();
            }
        });
    }

    public void back(View view) {
        finish();
    }
}
