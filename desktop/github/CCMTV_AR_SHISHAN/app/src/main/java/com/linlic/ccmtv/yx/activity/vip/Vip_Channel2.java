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
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.linlic.ccmtv.yx.utils.MyScrollView;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
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

/**VIP专区 科室使用
 * Created by Administrator on 2018/6/4.
 */

public class Vip_Channel2 extends BaseActivity {

    private Context context;
    @Bind(R.id.selKeshi)
    TextView selKeshi;
    @Bind(R.id.videos)
    MyGridView videos;//视频集合
    @Bind(R.id.keshi_handpick)
    TextView keshi_handpick;//精选
    @Bind(R.id.keshi_internal_medicine)
    TextView keshi_internal_medicine;//内科
    @Bind(R.id.keshi_surgical)
    TextView keshi_surgical;//外科
    @Bind(R.id.keshi_especially)
    TextView keshi_especially;//妇儿科
    @Bind(R.id.keshi_other)
    TextView keshi_other;//       其他
    @Bind(R.id.keshi_handpick_layout)
    LinearLayout keshi_handpick_layout;
    @Bind(R.id.keshi_internal_medicine_layout)
    LinearLayout keshi_internal_medicine_layout;
    @Bind(R.id.keshi_surgical_layout)
    LinearLayout keshi_surgical_layout;
    @Bind(R.id.keshi_especially_layout)
    LinearLayout keshi_especially_layout;
    @Bind(R.id.keshi_other_layout)
    LinearLayout keshi_other_layout;
    @Bind(R.id.keshi_more_layout)
    LinearLayout keshi_more_layout;
    @Bind(R.id.scrollView)
    MyScrollView scrollView;

    private JSONArray indexInfoArray;
    private home_videos_GridAdapter home_videos_gridAdapter;
    private  Banner banner;
    private List<String> images =new ArrayList<>();
    public List<Home_Video> videodata =new ArrayList<>();
    public  Boolean is = true;
    private int page = 1;
    private int iposition = 0;

    private String select_str= "";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                           /* JSONObject data  = jsonObject.getJSONObject("data");
                            indexInfoArray = null;
                            images.clear();
                            indexInfoArray = data.getJSONArray("lun_videos");

                            for (int i = 0; i < indexInfoArray.length(); i++) {
                                images.add(indexInfoArray.getJSONObject(i).getString("picurl"));
                            }



                            initBanner();*/

                            //判断是否有选中的科室


                            if(page == 1){
                                if(jsonObject.has("selKeshi")){
                                    selKeshi.setText(jsonObject.getString("selKeshi"));
                                    selKeshi.setVisibility(View.VISIBLE);
                                }else{
                                    selKeshi.setVisibility(View.GONE);
                                }

                                if(jsonObject.has("lun_date")){
                                    indexInfoArray = null;
                                    images.clear();
                                    indexInfoArray = jsonObject.getJSONArray("lun_date");

                                    for (int i = 0; i < indexInfoArray.length(); i++) {
                                        images.add(indexInfoArray.getJSONObject(i).getString("picurl"));
                                    }

                                    initBanner();
                                }
                                videodata.removeAll(videodata);
                            }
                            JSONArray dataJson = jsonObject.getJSONArray("data");
                            if(dataJson.length()<10){
                                is = false;
                            }
                            for(int i = 0 ; i< dataJson.length();i++){
                                JSONObject videoJson =   dataJson.getJSONObject(i);
                                videodata.add(new Home_Video(videoJson.getString("aid"),videoJson.getString("title"),videoJson.getString("picurl"),videoJson.getString("flag"),videoJson.getString("videopaymoney"),videoJson.getString("money"))) ;
                            }
                            home_videos_gridAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(Vip_Channel2.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            is = false;
                        }
//                        MyProgressBarDialogTools.hide();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case 500:
                    Toast.makeText(context
                            , R.string.post_hint1,
                            Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.vip_channel2);
        context = this;
        ButterKnife.bind(this);
        findId();
        click();
        initVideos();
        getUrlRulest("精选");
    }



    public void click(){

        keshi_handpick_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //改变选中字体颜色
                keshi_handpick.setTextColor(Color.parseColor("#333333"));
                keshi_internal_medicine.setTextColor(Color.parseColor("#666666"));
                keshi_surgical.setTextColor(Color.parseColor("#666666"));
                keshi_especially.setTextColor(Color.parseColor("#666666"));
                keshi_other.setTextColor(Color.parseColor("#666666"));
                //改变选中背景状态
                keshi_handpick.setBackground(getResources().getDrawable(R.drawable.vip_department_bg));
                keshi_internal_medicine.setBackground(null);
                keshi_surgical.setBackground(null);
                keshi_especially.setBackground(null);
                keshi_other.setBackground(null);
                //重新加载相对应的视频
                page = 1;
                is = true;
                getUrlRulest(keshi_handpick.getText().toString());
            }
        });
        keshi_internal_medicine_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //改变选中字体颜色
                keshi_handpick.setTextColor(Color.parseColor("#666666"));
                keshi_internal_medicine.setTextColor(Color.parseColor("#333333"));
                keshi_surgical.setTextColor(Color.parseColor("#666666"));
                keshi_especially.setTextColor(Color.parseColor("#666666"));
                keshi_other.setTextColor(Color.parseColor("#666666"));
                //改变选中背景状态
                keshi_handpick.setBackground(null);
                keshi_internal_medicine.setBackground(getResources().getDrawable(R.drawable.vip_department_bg));
                keshi_surgical.setBackground(null);
                keshi_especially.setBackground(null);
                keshi_other.setBackground(null);
                //重新加载相对应的视频
                page = 1;
                is = true;
                getUrlRulest(keshi_internal_medicine.getText().toString());
            }
        });
        keshi_surgical_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //改变选中字体颜色
                keshi_handpick.setTextColor(Color.parseColor("#666666"));
                keshi_internal_medicine.setTextColor(Color.parseColor("#666666"));
                keshi_surgical.setTextColor(Color.parseColor("#333333"));
                keshi_especially.setTextColor(Color.parseColor("#666666"));
                keshi_other.setTextColor(Color.parseColor("#666666"));
                //改变选中背景状态
                keshi_handpick.setBackground(null);
                keshi_internal_medicine.setBackground(null);
                keshi_surgical.setBackground(getResources().getDrawable(R.drawable.vip_department_bg));
                keshi_especially.setBackground(null);
                keshi_other.setBackground(null);
                //重新加载相对应的视频
                page = 1;
                is = true;
                getUrlRulest(keshi_surgical.getText().toString());
            }
        });
        keshi_especially_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //改变选中字体颜色
                keshi_handpick.setTextColor(Color.parseColor("#666666"));
                keshi_internal_medicine.setTextColor(Color.parseColor("#666666"));
                keshi_surgical.setTextColor(Color.parseColor("#666666"));
                keshi_especially.setTextColor(Color.parseColor("#333333"));
                keshi_other.setTextColor(Color.parseColor("#666666"));
                //改变选中背景状态
                keshi_handpick.setBackground(null);
                keshi_internal_medicine.setBackground(null);
                keshi_surgical.setBackground(null);
                keshi_especially.setBackground(getResources().getDrawable(R.drawable.vip_department_bg));
                keshi_other.setBackground(null);
                //重新加载相对应的视频
                page = 1;
                is = true;
                getUrlRulest(keshi_especially.getText().toString());
            }
        });
        keshi_other_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //改变选中字体颜色
                keshi_handpick.setTextColor(Color.parseColor("#666666"));
                keshi_internal_medicine.setTextColor(Color.parseColor("#666666"));
                keshi_surgical.setTextColor(Color.parseColor("#666666"));
                keshi_especially.setTextColor(Color.parseColor("#666666"));
                keshi_other.setTextColor(Color.parseColor("#333333"));
                //改变选中背景状态
                keshi_handpick.setBackground(null);
                keshi_internal_medicine.setBackground(null);
                keshi_surgical.setBackground(null);
                keshi_especially.setBackground(null);
                keshi_other.setBackground(getResources().getDrawable(R.drawable.vip_department_bg));
                //重新加载相对应的视频
                page = 1;
                is = true;
                getUrlRulest(keshi_other.getText().toString());
            }
        });

        keshi_more_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Vip_Channel_select.class);
                intent.putExtra("type",select_str);
                startActivity(intent);
            }
        });
    }

    public void getUrlRulest( String str) {
        select_str = str;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.vipVideo);
                    obj.put("uid", SharedPreferencesTools.getUid(context) );
                    obj.put("page", page);
                    obj.put("more", "more");
                    obj.put("keshi", select_str);
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
                    if(VideoFive.isFinish == null) {
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

    public void initVideos(){
        home_videos_gridAdapter = new home_videos_GridAdapter(context,videodata);
        videos.setAdapter(home_videos_gridAdapter);
        videos.setSelector(new ColorDrawable(Color.TRANSPARENT));//取消GridView中Item选中时默认的背景色
        videos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String uid = SharedPreferencesTools.getUidToLoginClose(context);
                if (uid == null || ("").equals(uid)) {
                    return;
                }
                if(VideoFive.isFinish == null) {
                    Intent intent = new Intent(context, VideoFive.class);
                    intent.putExtra("aid", videodata.get(position).getAid());
                    startActivity(intent);
                }
            }
        });
//        videos.addOn

        scrollView.setOnScrollChanged(new MyScrollView.OnScrollChanged() {
            @Override
            public void onScroll(int x, int y, int oldScrollX, int oldScrollY) {
                int scrollY=scrollView.getScrollY();//顶端以及滑出去的距离
                int height=scrollView.getHeight();//界面的高度
                int scrollViewMeasuredHeight=scrollView.getChildAt(0).getMeasuredHeight();//scrollview所占的高度
                if(scrollY==0){//在顶端的时候
//                    Toast.makeText(getContext(),"在顶端的时候  ", Toast.LENGTH_SHORT).show();
                }else if((scrollY+height)==scrollViewMeasuredHeight){//当在底部的时候
//                    Toast.makeText(getContext(),"当在底部的时候    ", Toast.LENGTH_SHORT).show();
                    if(is){
                        page++;
                        getUrlRulest(select_str);
                    }
                }else {//当在中间的时候
//                    Toast.makeText(getContext(),"当在中间的时候      ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        page = 1;
        is = true;
        getUrlRulest(select_str);
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn/channel-122.html";
        super.onPause();
    }

}
