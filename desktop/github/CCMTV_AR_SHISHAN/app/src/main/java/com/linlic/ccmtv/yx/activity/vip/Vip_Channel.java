package com.linlic.ccmtv.yx.activity.vip;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Home_Video;
import com.linlic.ccmtv.yx.activity.home.VideoFive;
import com.linlic.ccmtv.yx.adapter.home_videos_GridAdapter;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.GlideImageLoader;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyGridView;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/6/4.
 */

public class Vip_Channel extends BaseActivity {

    private Context context;
    @Bind(R.id.videos)
    MyGridView videos;//视频集合
    @Bind(R.id.ll_vip_channel_data)
    ScrollView channel_data;
    @Bind(R.id.rl_vip_channel_nodata)
    NodataEmptyLayout channel_nodata;

    private JSONArray indexInfoArray;
    private home_videos_GridAdapter home_videos_gridAdapter;
    private Banner banner;
    private List<String> images = new ArrayList<>();
    public List<Home_Video> videodata = new ArrayList<>();
    public Boolean is = true;
    private int page = 1;
    private int iposition = 0;
    private String mposition;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            JSONObject data = jsonObject.getJSONObject("data");
                            indexInfoArray = null;
                            images.clear();
                            indexInfoArray = data.getJSONArray("lun_videos");

                            for (int i = 0; i < indexInfoArray.length(); i++) {
                                images.add(indexInfoArray.getJSONObject(i).getString("picurl"));
                            }

                            initBanner();

                            JSONArray dataJson = data.getJSONArray("videos");
                            videodata.clear();
                            for (int i = 0; i < dataJson.length(); i++) {
                                JSONObject videoJson = dataJson.getJSONObject(i);
                                videodata.add(new Home_Video(videoJson.getString("aid"), videoJson.getString("title"), videoJson.getString("picurl"), videoJson.getString("flag"), videoJson.getString("videopaymoney"), videoJson.getString("money")));
                            }
                            home_videos_gridAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(Vip_Channel.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
//                        MyProgressBarDialogTools.hide();
                        setResultStatus(images.size() > 0 || videodata.size() > 0, jsonObject.getInt("status"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case 500:
                    Toast.makeText(context
                            , R.string.post_hint1,
                            Toast.LENGTH_SHORT).show();
                    setResultStatus(images.size() > 0 || videodata.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOST);
                    break;

                default:
                    break;
            }

        }
    };


    private void setResultStatus(boolean status, int code) {
        if (status) {
            channel_data.setVisibility(View.VISIBLE);
            channel_nodata.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                channel_nodata.setNetErrorIcon();
            } else {
                channel_nodata.setLastEmptyIcon();
            }
            channel_data.setVisibility(View.GONE);
            channel_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.vip_channel);
        context = this;
        ButterKnife.bind(this);
        findId();
        setActivity_title_name(getIntent().getStringExtra("type"));
        mposition=getIntent().getStringExtra("position");
        initVideos();
        getUrlRulest();
    }

    public void getUrlRulest() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.moreVideos);
                    obj.put("type", getIntent().getStringExtra("type"));
                    obj.put("page", 1);
                    if(mposition.equals("0")){
                        if (SharedPreferencesTools.getUidONnull(Vip_Channel.this).length() > 0) {
                            obj.put("uid", SharedPreferencesTools.getUidONnull(Vip_Channel.this));
                        }
                    }
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP1, obj.toString());
//                    LogUtil.e("首页数据", result);

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

    public void initBanner() {
        banner = (Banner) findViewById(R.id.banner);
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(2000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
//                Log.e("位置", position + "  =================");
                try {
                    JSONObject json = indexInfoArray.getJSONObject(position);

                    final String uid = SharedPreferencesTools.getUidToLoginClose(context);
                    if (uid == null || ("").equals(uid)) {
                        return;
                    }
                    if (VideoFive.isFinish == null) {
                        Intent intent = new Intent(context, VideoFive.class);
                        intent.putExtra("aid", json.getString("aid"));
                        context.startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    public void initVideos() {
        home_videos_gridAdapter = new home_videos_GridAdapter(context, videodata);
        videos.setSelector(new ColorDrawable(Color.TRANSPARENT));//取消GridView中Item选中时默认的背景色
        videos.setAdapter(home_videos_gridAdapter);
        videos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String uid = SharedPreferencesTools.getUidToLoginClose(context);
                if (uid == null || ("").equals(uid)) {
                    return;
                }
                if (VideoFive.isFinish == null) {
                    Intent intent = new Intent(context, VideoFive.class);
                    intent.putExtra("aid", videodata.get(position).getAid());
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn/channel-122.html";
        super.onPause();
    }

}
