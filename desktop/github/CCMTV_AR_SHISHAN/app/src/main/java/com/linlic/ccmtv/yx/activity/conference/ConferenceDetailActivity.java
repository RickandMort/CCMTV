package com.linlic.ccmtv.yx.activity.conference;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.Popular_search;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.conference.adapter.ConferenceDetailAdapter;
import com.linlic.ccmtv.yx.activity.conference.databean.ConferenceDetailBean;
import com.linlic.ccmtv.yx.activity.home.entry.ListData;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ConferenceDetailActivity extends BaseActivity {

    private Context context;
    private TextView title_name;
    private ImageView ivTitleIconRight;
    private ListView lvConferenceDetail;
    private ImageView ivConferenceDetailIcon;
    private NodataEmptyLayout lt_nodata1;
    private List<ConferenceDetailBean> detailBeanDatas = new ArrayList<>();

    private String conferenceId;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {// 成功
                            JSONObject data = result.getJSONObject("data");
                            if (data.has("banner")) {
                                String bannerUrl = data.getString("banner");
                                Glide.with(context).load(bannerUrl).into(ivConferenceDetailIcon);
                            }
                            if (data.has("ervideo")) {
                                JSONArray erVideoArray = data.getJSONArray("ervideo");
                                for (int i = 0; i < erVideoArray.length(); i++) {
                                    JSONObject issueObject = erVideoArray.getJSONObject(i);
                                    ConferenceDetailBean detailBean = new ConferenceDetailBean();
                                    detailBean.setFid(issueObject.getString("fid"));
                                    detailBean.setFtitle(issueObject.getString("ftitle"));
                                    if (issueObject.has("videos")) {
                                        JSONArray videosArray = issueObject.getJSONArray("videos");
                                        List<ListData> listDatas = new ArrayList<>();
                                        for (int j = 0; j < videosArray.length(); j++) {
                                            JSONObject videoObject = videosArray.getJSONObject(j);
                                            ListData videoData = new ListData();
                                            videoData.setVideopaymoney(videoObject.getString("videopaymoney"));
                                            videoData.setPicurl(videoObject.getString("picurl"));
                                            videoData.setTitle(videoObject.getString("title"));
                                            videoData.setFid(videoObject.getString("fid"));
                                            videoData.setAid(videoObject.getString("aid"));
                                            videoData.setMoney(videoObject.getString("money"));
                                            videoData.setFlag(videoObject.getString("flag"));
                                            listDatas.add(videoData);
                                        }
                                        detailBean.setVideos(listDatas);
                                    }
                                    detailBeanDatas.add(detailBean);
                                }
                            }
                            conferenceDetailAdapter.notifyDataSetChanged();

//                            if (detailBeanDatas.size()>0) {
//                                lvConferenceDetail.setVisibility(View.VISIBLE);
//                                lt_nodata1.setVisibility(View.GONE);
//                            }else {
//                                lvConferenceDetail.setVisibility(View.GONE);
//                                lt_nodata1.setVisibility(View.VISIBLE);
//                            }

                        } else {
//                            if (detailBeanDatas.size()>0) {
//                                lvConferenceDetail.setVisibility(View.VISIBLE);
//                                lt_nodata1.setVisibility(View.GONE);
//                            }else {
//                                lvConferenceDetail.setVisibility(View.GONE);
//                                lt_nodata1.setVisibility(View.VISIBLE);
//                            }
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        setResultStatus(detailBeanDatas.size() > 0, result.getInt("status"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    setResultStatus(detailBeanDatas.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOST);
                    break;
                default:
                    break;
            }
        }
    };
    private ConferenceDetailAdapter conferenceDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conference_detail);
        context = this;
        findId();
        getIntentData();
        initData();
        initListView();
    }

    private void setResultStatus(boolean status, int code) {
        if (status) {
            lvConferenceDetail.setVisibility(View.VISIBLE);
            lt_nodata1.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                lt_nodata1.setNetErrorIcon();
            } else {
                lt_nodata1.setLastEmptyIcon();
            }
            lvConferenceDetail.setVisibility(View.GONE);
            lt_nodata1.setVisibility(View.VISIBLE);
        }
    }

    public void findId() {
        title_name = (TextView) findViewById(R.id.activity_title_name);
        ivTitleIconRight = (ImageView) findViewById(R.id.id_iv_activity_title_8_right);
        lvConferenceDetail = (ListView) findViewById(R.id.id_lv_conference_detail);
        ivConferenceDetailIcon = (ImageView) findViewById(R.id.id_iv_conference_detail_pic);
        lt_nodata1 = (NodataEmptyLayout) findViewById(R.id.lt_nodata1);

        ivTitleIconRight.setVisibility(View.VISIBLE);
        ivTitleIconRight.setImageResource(R.mipmap.conference_search_gray);
        ivTitleIconRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Popular_search.class);
                intent.putExtra("mode", "1");//方式 1 表示从首页title-img 进入到热门搜索页 2 表示从搜索也进入到搜索页
                startActivity(intent);
            }
        });
    }

    private void getIntentData() {
        conferenceId = getIntent().getStringExtra("conferenceId");
    }

    private void initData() {
        title_name.setText(getIntent().getStringExtra("title"));

        getConferenceDetail();

        /*for (int i = 0; i < 3; i++){
            SubKeshiData subKeshiData=new SubKeshiData();
            subKeshiData.setTitle("第"+i+"次会议");
            List<ListData> listDatas=new ArrayList<>();
            for (int j=0;j<4;j++){
                if (i==1 && j == 1){

                }else {
                    ListData listData=new ListData();
                    listData.setTitle("第"+i+"次会议"+"第"+j+"个视频");
                    listData.setPicurl("http://h.hiphotos.baidu.com/image/pic/item/08f790529822720e23a3d3b777cb0a46f21fab09.jpg");
                    listDatas.add(listData);
                }

            }
            subKeshiData.setListdata(listDatas);
            subKeshiDatas.add(subKeshiData);
        }*/
    }

    private void getConferenceDetail() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.specialDetail);
                    obj.put("id", conferenceId);
                    String result = HttpClientUtils.sendPost(context, URLConfig.conference, obj.toString());
//                    LogUtil.e("会议详情数据：", result);

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
        conferenceDetailAdapter = new ConferenceDetailAdapter(this, detailBeanDatas);
        lvConferenceDetail.setAdapter(conferenceDetailAdapter);
    }

    public void back(View view) {
        finish();
    }
}
