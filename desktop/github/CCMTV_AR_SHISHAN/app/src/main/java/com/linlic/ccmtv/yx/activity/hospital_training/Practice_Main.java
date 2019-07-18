package com.linlic.ccmtv.yx.activity.hospital_training;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.kzbf.utils.SkyVisitUtils;
import com.linlic.ccmtv.yx.utils.CategoryView;
import com.linlic.ccmtv.yx.utils.GlideImageLoader;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/23.
 */

public class Practice_Main extends BaseActivity {
    //用户统计
    private String entertime, leavetime;
    private String enterUrl;
    private int page = 1;
    private  Banner banner;
    private CategoryView category;
    private String bigType = "";
    private Context context;
    private JSONArray indexInfoArray;
    private TextView start_onIs_select;
    private List<String> images =new ArrayList<>();
    private boolean is_select = false;
    private String paperid = "";
    private String lastPage = "";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            is_select = true;
//                            JSONArray dataArray = jsonObject.getJSONArray("video_list");
                            //清空轮播图数据地址
                            images.clear();
                            indexInfoArray =  jsonObject.getJSONArray("data");

                            for (int i = 0; i < indexInfoArray.length(); i++) {
                                JSONObject activityObj = new JSONObject(indexInfoArray.get(i).toString());
                                images.add(activityObj.getString("img"));
                            }
                            //初始化轮播图
                            initBanner();
                            paperid = jsonObject.getString("paperid");


                        } else{
                            is_select = false;
                            if(jsonObject.has("data")){
                                //清空轮播图数据地址
                                images.clear();
                                indexInfoArray =  jsonObject.getJSONArray("data");

                                for (int i = 0; i < indexInfoArray.length(); i++) {
                                    JSONObject activityObj = new JSONObject(indexInfoArray.get(i).toString());
                                    images.add(activityObj.getString("img"));
                                }
                                //初始化轮播图
                                initBanner();
                                paperid = jsonObject.getString("paperid");
                            }
                        }
                        lastPage = jsonObject.getString("last");
//                        LogUtil.e("lastPage",lastPage);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    break;
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.practice_main);
        context = this;
        bigType = getIntent().getStringExtra("bigType");
        findId();
        setVideos();
        initdata();
//        initLearning_video();
//        initBanner();
    }


    public void initLearning_video() {

        images.add("http://shgme.ccmtv.cn/images/mob/banner.jpg");
        images.add("http://www.ccmtv.cn/do/ccmtvappandroid/dayicon/51mobcj.jpg");
        images.add("http://www.ccmtv.cn/upload_files/label/_20170426170403_qfyfw.jpg");

    }

    public void initBanner() {
        banner = (Banner)  findViewById(R.id.banner);
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
                    Intent intent = new Intent(context, Hospital_training_main.class);
                    intent.putExtra("title", indexInfoArray.getJSONObject(position).getString("type_name"));
                    intent.putExtra("title_type", indexInfoArray.getJSONObject(position).getString("type"));
                    context.startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    @Override
    public void findId() {

        super.findId();
    }

    public void initdata(){
        start_onIs_select = (TextView) findViewById(R.id.start_onIs_select);
        start_onIs_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onIs_select(v);
            }
        });
    }

    @Override
    protected void onResume() {
        //记录进入日期
        entertime = SkyVisitUtils.getCurrentTime();
        setVideos();
        super.onResume();
    }
    public void clickHistorical_records_reply(View view){
        Intent intent = new Intent(context,Historical_records_reply.class);
        intent.putExtra("bigType",getIntent().getStringExtra("bigType"));
        startActivity(intent);
    }
    public void clickCollection_list(View view){
        Intent intent = new Intent(context,Collection_list.class);
        intent.putExtra("bigType",getIntent().getStringExtra("bigType"));
        startActivity(intent);

    }

    @Override
    protected void onPause() {
    /*    //记录退出日期
        leavetime = SkyVisitUtils.getCurrentTime();
        switch (bigType) {
            case "basic"://医学三基
                enterUrl = "http://yy.ccmtv.cn/basic_index/index.html";
                break;
            case "train"://住院医师规培
                enterUrl = "http://yy.ccmtv.cn/train_index/index.html";
                break;
            case "practicing"://执医考试
                enterUrl = "http://yy.ccmtv.cn/Practicing_exam.html";
                break;
            case "general"://全科培训
                enterUrl = "http://yy.ccmtv.cn/General_practice/index.html";
                break;
        }
        //保存日期到服务器
        SkyVisitUtils.OnlineStatistical(context,enterUrl,entertime,leavetime);*/
        super.onPause();
    }

    public void onIs_select(View view){
        if(is_select){
            Intent intent = new Intent(context,Practice.class);
            intent.putExtra("paperid",paperid);
            intent.putExtra("lastPage",lastPage);
            startActivity(intent);
        }else{
            Intent intent = new Intent(context,Departments.class);
            startActivity(intent);
        }
    }

    public void setVideos(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.cateSelected);
                    obj.put("uid", SharedPreferencesTools.getUid(context));

//                    Log.e("试题练习-首页", obj.toString());
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.Hospital_training, obj.toString());
//                    LogUtil.e("试题练习-首页", result);
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
}
