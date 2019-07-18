package com.linlic.ccmtv.yx.activity.channel;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.CustomActivity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.conference.ConferenceDetailActivity;
import com.linlic.ccmtv.yx.activity.entity.Home_Video;
import com.linlic.ccmtv.yx.activity.home.VideoFive;
import com.linlic.ccmtv.yx.activity.vip.adapter.OnRecyclerviewItemClickListener;
import com.linlic.ccmtv.yx.adapter.home_videos_GridAdapter;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.kzbf.utils.GlideRoundTransform;
import com.linlic.ccmtv.yx.util.ImageLoader;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyGridView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.MyScrollView;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tom on 2018/5/8.
 */

public class Channel_Main extends BaseActivity {
    private Context context;
    List<CityItem> cityList = new ArrayList<CityItem>();
    //    GridViewAdapter adapter = null;
    ChannelMainRecyclerViewAdapter adapter = null;
    /*   @Bind(R.id.tablayout)
       TabLayout tablayout;*/
    @Bind(R.id.grid)
//     HorizontalListView gridView;
            RecyclerView recyclerView;
    @Bind(R.id.banner5)
    ImageView banner5;
    @Bind(R.id.banner6)
    ImageView banner6;
    @Bind(R.id.banner)
    MZBannerView mMZBanner;
    @Bind(R.id.videos)
    MyGridView videos;//视频集合
    @Bind(R.id.scrollView)
    MyScrollView scrollView;//
    @Bind(R.id.isshow)
    LinearLayout isshow;//
    @Bind(R.id.nodata_layout)
    LinearLayout nodata_layout;
    public JSONObject banner5Json = null;
    public JSONObject banner6Json = null;
    private home_videos_GridAdapter home_videos_gridAdapter;
    public List<Home_Video> videodata = new ArrayList<>();
    private List<JSONObject> list = new ArrayList<>();
    BaseListAdapter baseListAdapter;
    public Boolean is = true;
    private int page = 1;
    private int iposition = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 1:
                    try {

                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            //获取banner
                            JSONArray lunDataArray = jsonObject.getJSONArray("lunData");
                            for (int i = 0; i < lunDataArray.length(); i++) {
                                list.add(lunDataArray.getJSONObject(i));
                            }
                            //初始化banner
                            init();
                            //获取会议与直播的数据
                          /*   [imgurl] => http://192.168.30.201/upload_files/pdicon/z2.png
                                 [activityurl] => http://www.ccmtv.cn//upload_files/new_upload_files/pro_sh/ccmtvhy/m1136.php
                                 [activitytitle] => 第十届中国医师协会外科医师年会（CCS2017）
                                  [urlflg] => 0*/

                            JSONArray meetingArray = jsonObject.getJSONArray("meeting");
                            banner5Json = meetingArray.getJSONObject(0);
                            banner6Json = meetingArray.getJSONObject(1);
                            ImageLoader.load(context, meetingArray.getJSONObject(0).getString("picurl"), banner5);
                            ImageLoader.load(context, meetingArray.getJSONObject(1).getString("picurl"), banner6);

                            //获取小分类数据
                            JSONArray ksarrArray = jsonObject.getJSONArray("ksarr");
                            for (int i = 0; i < ksarrArray.length(); i++) {
                                CityItem item = new CityItem();
                                item.setCityName(ksarrArray.getJSONObject(i).getString("name"));
                                if (i == 0) {
                                    item.setCityCode("1");
                                } else {
                                    item.setCityCode("0");
                                }
                                item.setCityID(ksarrArray.getJSONObject(i).getString("id"));
                                cityList.add(item);
                            }
                            //初始化小分类
                            setGridView();
                            //调用视频获取接口
                            getVideoRulest1();
                         /*   //获取视频数据
                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            for(int i = 0;i <dataArray.length();i++){
                                JSONObject videoJson =   dataArray.getJSONObject(i);
                                videodata.add(new Home_Video(videoJson.getString("aid"),videoJson.getString("title"),videoJson.getString("picurl"),videoJson.getString("flag"),videoJson.getString("videopaymoney"),videoJson.getString("money"))) ;
                            }
                            //刷新视频数据
                            home_videos_gridAdapter.notifyDataSetChanged();*/
                        } else {
                            Toast.makeText(Channel_Main.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
//                        MyProgressBarDialogTools.hide();

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            //获取视频数据
                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            if (page == 1) {
                                videodata.removeAll(videodata);

                                if (dataArray.length() < 1) {
                                    nodata_layout.setVisibility(View.VISIBLE);
                                } else {
                                    nodata_layout.setVisibility(View.GONE);
                                }
                            }


                            if (dataArray.length() < 20) {
                                is = false;
                            }
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject videoJson = dataArray.getJSONObject(i);
                                videodata.add(new Home_Video(videoJson.getString("aid"), videoJson.getString("title"), videoJson.getString("picurl"), videoJson.getString("flag"), videoJson.getString("videopaymoney"), videoJson.getString("money")));
                            }
                            //刷新视频数据
                            home_videos_gridAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(Channel_Main.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            is = false;
                        }
//                        MyProgressBarDialogTools.hide();

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;

                case 500:
                    Toast.makeText(context
                            , R.string.post_hint1,
                            Toast.LENGTH_SHORT).show();
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.channel_main);
        context = this;
        ButterKnife.bind(this);
        findId();
        setActivity_title_name(getIntent().getStringExtra("min_channel_title"));
        initVideos();
        getVideoRulest();


    }

    public void clickSS(View view) {
        startActivity(new Intent(context, CustomActivity.class).putExtra("type", "home"));
    }

    public void clickHY(View view) {
        try {
            //banner5
                /*Intent intent = null;
                if (banner5Json.getString("urlflg").equals("1")) {
                    intent = new Intent(context, ActivityWebActivity.class);
                    intent.putExtra("title", banner5Json.getString("activitytitle"));
                }else{
                    intent = new Intent(context, ConferenceDetailActivity.class);
                    intent.putExtra("conferenceId",banner5Json.getString("id"));
                }*/
            Intent intent = new Intent(context, ConferenceDetailActivity.class);
            intent.putExtra("conferenceId", banner5Json.getString("xid"));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void clickHY1(View view) {
        try {
            //banner6
                /*Intent intent = null;
                if (banner6Json.getString("urlflg").equals("1")) {
                    intent = new Intent(context, ActivityWebActivity.class);
                    intent.putExtra("title", banner6Json.getString("activitytitle"));
                }else{
                    intent = new Intent(context, ConferenceDetailActivity.class);
                    intent.putExtra("conferenceId",banner6Json.getString("id"));
                }*/
            Intent intent = new Intent(context, ConferenceDetailActivity.class);
            intent.putExtra("conferenceId", banner6Json.getString("xid"));
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 设置GirdView参数，绑定数据
     */
    private void setGridView() {

        /*adapter = new GridViewAdapter(getApplicationContext(),
                cityList);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    isshow.setVisibility(View.VISIBLE);
                }else{
                    isshow.setVisibility(View.GONE);
                }
                iposition = position;
                for(CityItem city:cityList){
                    if(!city.getCityCode().equals("0")){
                        city.setCityCode("0");
                    }
                }
                cityList.get(position).setCityCode("1");
                adapter.notifyDataSetChanged();
//                adapter.getItem()
                page = 1;
                is = true;
                getVideoRulest1();
            }
        });*/

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ChannelMainRecyclerViewAdapter(context, cityList, onRecyclerviewItemClickListener);
        recyclerView.setAdapter(adapter);
    }

    private OnRecyclerviewItemClickListener onRecyclerviewItemClickListener = new OnRecyclerviewItemClickListener() {
        @Override
        public void onItemClickListener(View v, int position) {
            if (position == 0) {
                isshow.setVisibility(View.VISIBLE);
            } else {
                isshow.setVisibility(View.GONE);
            }
            iposition = position;
            for (CityItem city : cityList) {
                if (!city.getCityCode().equals("0")) {
                    city.setCityCode("0");
                }
            }
            cityList.get(position).setCityCode("1");
            adapter.notifyDataSetChanged();
//                adapter.getItem()
            page = 1;
            is = true;
            MyProgressBarDialogTools.show(context);
            getVideoRulest1();
        }
    };

    /**
     * GirdView 数据适配器
     */
    public class GridViewAdapter extends BaseAdapter {
        Context context;
        List<CityItem> list;

        public GridViewAdapter(Context _context, List<CityItem> _list) {
            this.list = _list;
            this.context = _context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.list_item_03, null);
            TextView tvCity = (TextView) convertView.findViewById(R.id.tvCity);
            View tvCode = convertView.findViewById(R.id.tvCode);
            CityItem city = list.get(position);
            tvCity.setText(city.getCityName());
            if (city.getCityCode().equals("1")) {
                tvCode.setVisibility(View.VISIBLE);
                tvCity.setTextColor(getResources().getColor(R.color.categpru_chenck_text_bg));
            } else {
                tvCode.setVisibility(View.INVISIBLE);
                tvCity.setTextColor(getResources().getColor(R.color.channel_main_tab_item));
            }
            return convertView;
        }
    }

    public class CityItem {
        private String cityName;
        private String cityCode;
        private String cityID;

        public String getCityID() {
            return cityID;
        }

        public void setCityID(String cityID) {
            this.cityID = cityID;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }
    }

    private void init() {

        // 设置数据
        mMZBanner.setPages(list, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });
        mMZBanner.start();//开始轮播

        /*mMZBanner.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int i) {
                try {
                    final String uid = SharedPreferencesTools.getUidToLoginClose(context);
                    if (uid == null || ("").equals(uid)) {
                        return;
                    }
                    if(VideoFive.isFinish == null) {
                        Intent intent = new Intent(context, VideoFive.class);
                        intent.putExtra("aid", list.get(i).getString("aid"));
                        context.startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });*/

    }

    public class BannerViewHolder implements MZViewHolder<JSONObject> {
        private ImageView mImageView;

        @Override
        public View createView(Context context) {
            // 返回页面布局
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
            mImageView = view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(Context context, int position, JSONObject data) {
            try {
                RequestOptions options = new RequestOptions().transform(new GlideRoundTransform(context, 8));
                Glide.with(context)
                        .load(data.getString("picurl"))
                        .apply(options)
                        .into(mImageView);
                // 数据绑定
//                ImageLoader.load(context, data.getString("picurl"), mImageView);
                mImageView.setTag(position);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void mzBannerClickItem(View view) {
        try {
            final String uid = SharedPreferencesTools.getUidToLoginClose(context);
            if (uid == null || ("").equals(uid)) {
                return;
            }
            Intent intent = new Intent(context, VideoFive.class);
            intent.putExtra("aid", list.get(Integer.parseInt(view.getTag().toString())).getString("aid"));
            context.startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initVideos() {


        home_videos_gridAdapter = new home_videos_GridAdapter(context, videodata);
        videos.setAdapter(home_videos_gridAdapter);
        videos.setSelector(new ColorDrawable(Color.TRANSPARENT));
        videos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView video_aid = (TextView) view.findViewById(R.id.video_aid);
                final String uid = SharedPreferencesTools.getUidToLoginClose(context);
                if (uid == null || ("").equals(uid)) {
                    return;
                }
                if (VideoFive.isFinish == null) {
                    Intent intent = new Intent(context, VideoFive.class);
                    intent.putExtra("aid", video_aid.getText().toString());
                    context.startActivity(intent);
                }
            }
        });
//        videos.addOn
        scrollView.setOnScrollChanged(new MyScrollView.OnScrollChanged() {
            @Override
            public void onScroll(int x, int y, int oldScrollX, int oldScrollY) {
                int scrollY = scrollView.getScrollY();//顶端以及滑出去的距离
                int height = scrollView.getHeight();//界面的高度
                int scrollViewMeasuredHeight = scrollView.getChildAt(0).getMeasuredHeight();//scrollview所占的高度
                if (scrollY == 0) {//在顶端的时候
//                    Toast.makeText(getContext(),"在顶端的时候  ", Toast.LENGTH_SHORT).show();
                } else if ((scrollY + height) == scrollViewMeasuredHeight) {//当在底部的时候
//                    Toast.makeText(getContext(),"当在底部的时候    ", Toast.LENGTH_SHORT).show();
                    if (is) {
                        page++;
                        getVideoRulest1();
                    }
                } else {//当在中间的时候
//                    Toast.makeText(getContext(),"当在中间的时候      ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void findId() {
        super.findId();
        //视频列表 底部加载更多


    }

    public void getVideoRulest() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.ksIndex);
                    if (SharedPreferencesTools.getUidONnull(context).length() > 0) {
                        obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    }
                    obj.put("kid", getIntent().getStringExtra("min_channel_id"));
//                    obj.put("type",1);
                    String result = HttpClientUtils.sendPost(context, URLConfig.PDCCMTVAPP, obj.toString());
//                    LogUtil.e("频道首页数据", result );

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

    public void getVideoRulest1() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.diseaseVideo);
                    if (SharedPreferencesTools.getUidONnull(context).length() > 0) {
                        obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    }
                    obj.put("kid", cityList.get(iposition).getCityID());
                    obj.put("page", page);
                    String result = HttpClientUtils.sendPost(context, URLConfig.PDCCMTVAPP, obj.toString());
//                    LogUtil.e("频道首页数据", result );

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

    @Override
    protected void onResume() {
        super.onResume();
        mMZBanner.start();//开始轮播
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn/channel-122.html";
        super.onPause();
        mMZBanner.pause();//暂停轮播
    }


}
