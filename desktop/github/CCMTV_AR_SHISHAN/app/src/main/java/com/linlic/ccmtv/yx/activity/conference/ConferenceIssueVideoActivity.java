package com.linlic.ccmtv.yx.activity.conference;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.base.LocalApplication;
import com.linlic.ccmtv.yx.activity.conference.adapter.ConferenceIssueVideoAdapter;
import com.linlic.ccmtv.yx.activity.conference.databean.ConferenceVideoBean;
import com.linlic.ccmtv.yx.activity.home.VideoFive;
import com.linlic.ccmtv.yx.activity.login.LoginActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ConferenceIssueVideoActivity extends BaseActivity {

    private TextView title_name;
    private ImageView ivTitleIconRight;
    private GridView gvIssueVideo;
    private ConferenceIssueVideoAdapter conferenceIssueVideoAdapter;
    private List<ConferenceVideoBean> conferenceVideoBeanList = new ArrayList<>();
    private Context context;
    private String xId = "";
    private NodataEmptyLayout lt_nodata1;
    private String fid;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {// 成功
                            xId = result.getString("xid");
                            conferenceVideoBeanList.clear();
                            JSONArray data = result.getJSONArray("data");
                            if (data != null && data.length() > 0) {
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject videoObject = data.getJSONObject(i);
                                    ConferenceVideoBean videoBean = new ConferenceVideoBean();
                                    videoBean.setVideoPayMoney(videoObject.getString("videopaymoney"));
                                    videoBean.setFid(videoObject.getString("fid"));
                                    videoBean.setTitle(videoObject.getString("title"));
                                    videoBean.setPicUrl(videoObject.getString("picurl"));
                                    videoBean.setAid(videoObject.getString("aid"));
                                    videoBean.setFlag(videoObject.getString("flag"));
                                    videoBean.setMoney(videoObject.getString("money"));
                                    conferenceVideoBeanList.add(videoBean);
                                }
                                conferenceIssueVideoAdapter.notifyDataSetChanged();

                                if (conferenceVideoBeanList.size() > 0) {
                                    gvIssueVideo.setVisibility(View.VISIBLE);
                                    lt_nodata1.setVisibility(View.GONE);
                                } else {
                                    gvIssueVideo.setVisibility(View.GONE);
                                    lt_nodata1.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            if (conferenceVideoBeanList.size() > 0) {
                                gvIssueVideo.setVisibility(View.VISIBLE);
                                lt_nodata1.setVisibility(View.GONE);
                            } else {
                                gvIssueVideo.setVisibility(View.GONE);
                                lt_nodata1.setVisibility(View.VISIBLE);
                            }
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        setResultStatus(conferenceVideoBeanList.size() > 0, result.getInt("status"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    setResultStatus(conferenceVideoBeanList.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOST);
                    break;
                default:
                    break;
            }
        }
    };

    private void setResultStatus(boolean status, int code) {
        if (status) {
            gvIssueVideo.setVisibility(View.VISIBLE);
            lt_nodata1.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                lt_nodata1.setNetErrorIcon();
            } else {
                lt_nodata1.setLastEmptyIcon();
            }
            gvIssueVideo.setVisibility(View.GONE);
            lt_nodata1.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conference_issue_video);
        context = this;
        findId();
        //initData();
        onNewIntent(getIntent());
        initGridView();
    }

    public void findId() {
        title_name = (TextView) findViewById(R.id.activity_title_name);
        ivTitleIconRight = (ImageView) findViewById(R.id.id_iv_activity_title_8_right);
        gvIssueVideo = (GridView) findViewById(R.id.id_gv_conference_issue_video);
        lt_nodata1 = (NodataEmptyLayout) findViewById(R.id.lt_nodata1);

        ivTitleIconRight.setVisibility(View.VISIBLE);

        title_name.setTextSize(20);
        ivTitleIconRight.setImageResource(R.mipmap.conference_issue_filter);
        ivTitleIconRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConferenceIssueVideoActivity.this, ConferenceIssueSelectActivity.class);
                intent.putExtra("xid", xId);
                intent.putExtra("fid", fid);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getStringExtra("title") != null && !intent.getStringExtra("title").isEmpty()) {
            title_name.setText(intent.getStringExtra("title"));
        }
        fid = intent.getStringExtra("fid");
//        Log.e(getLocalClassName(), "initData: titlename:"+intent.getStringExtra("title"));
        initData();
    }

    private void initData() {
        getConferenceIssueVideo();

        /*for (int i = 0; i < 10; i++) {
            ConferenceBean bean=new ConferenceBean();
            bean.setIconUrl("http://h.hiphotos.baidu.com/image/pic/item/08f790529822720e23a3d3b777cb0a46f21fab09.jpg");
            bean.setTime("2018-05-10  10:00");
            bean.setTitle("2018中国小米7行业发布会即将揭晓");
            conferenceBeanList.add(bean);
        }*/
    }

    private void getConferenceIssueVideo() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.moreVideo);
                    obj.put("fid", fid);
                    String result = HttpClientUtils.sendPost(context, URLConfig.conference, obj.toString());
//                    LogUtil.e("会议专题更多视频数据：", result);

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

    private void initGridView() {
        conferenceIssueVideoAdapter = new ConferenceIssueVideoAdapter(this, conferenceVideoBeanList);
        gvIssueVideo.setAdapter(conferenceIssueVideoAdapter);
        gvIssueVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String aid = conferenceVideoBeanList.get(position).getAid();
                Intent intent = new Intent(context, VideoFive.class);
                intent.putExtra("aid", aid);
                if(SharedPreferencesTools.getUidONnull(context).equals("")){
                    startActivity(new Intent(context, LoginActivity.class).putExtra("source", ""));
                    LocalApplication.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LocalApplication.getAppContext(), "账户未登录，请先登录", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    startActivity(intent);

                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void back(View view) {
        finish();
    }
}
