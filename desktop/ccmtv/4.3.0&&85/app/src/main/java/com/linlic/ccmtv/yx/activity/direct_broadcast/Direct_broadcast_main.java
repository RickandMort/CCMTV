package com.linlic.ccmtv.yx.activity.direct_broadcast;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.Popular_search3;
import com.linlic.ccmtv.yx.activity.entity.Direct_broadcast;
import com.linlic.ccmtv.yx.activity.entity.Live_broadcast;
import com.linlic.ccmtv.yx.activity.home.ActivityWebActivity;
import com.linlic.ccmtv.yx.activity.home.SpecialActivity;
import com.linlic.ccmtv.yx.activity.home.apricotcup.ApricotActivity;
import com.linlic.ccmtv.yx.activity.home.hxsl.BreatheActivity;
import com.linlic.ccmtv.yx.activity.home.willowcup.WillowCupActivity;
import com.linlic.ccmtv.yx.activity.my.MyInvitationFriend;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.CategoryView2;
import com.linlic.ccmtv.yx.utils.GlideImageLoader;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyListView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 直播首页 new
 * Created by Administrator on 2018/3/7.
 */

public class Direct_broadcast_main extends AppCompatActivity {

    private Banner banner;
    private List<String> images = new ArrayList<>();
    private Context context;
    private JSONArray indexInfoArray;
    private CategoryView2 categoryView2;
    private String departmentsKey = "科室";
    private List<String> departmentsList = new ArrayList<>();//科室
    private String disease_class = "";
    private MyListView direct_broadcast_list;
    private BaseListAdapter baseListAdapter;
    private List<Direct_broadcast> data = new ArrayList<Direct_broadcast>();
    private int page = 1;
    private boolean isNoMore = false;
    private TabLayout tabLayout;
    private LinearLayout under_way;
    private View under_way_color;
    private LinearLayout trail;
    private View trail_color;
    private LinearLayout recent_review;
    private View recent_review_color;
    private ImageView search;
    private int direct_broadcast_type = 1;//1 代表直播 2 代表预告 3 代表近期回顾
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
//                        LogUtil.e("搜索数据", msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject customJson = dataArray.getJSONObject(i);


                                Direct_broadcast direct_broadcast = new Direct_broadcast();
                                direct_broadcast.setDirect_broadcast_item_type(customJson.getString("type"));
                                direct_broadcast.setDirect_broadcast_item_title( customJson.getString("livename"));
                                if(direct_broadcast_type == 1){
                                    direct_broadcast.setDirect_broadcast_item_icon("1");
                                }else{
                                    direct_broadcast.setDirect_broadcast_item_icon("2");
                                }

                                direct_broadcast.setDirect_broadcast_item_add(""+(i+1));
                                direct_broadcast.setDirect_broadcast_item_banner(customJson.getString("banner"));
                                direct_broadcast.setPosition(i);
                                direct_broadcast.setAbout(customJson.getString("about"));
                                direct_broadcast.setAddress(customJson.getString("address"));
                                direct_broadcast.setEndtime(customJson.getString("endtime"));
                                direct_broadcast.setStarttime(customJson.getString("starttime"));

                                JSONArray zis =  customJson.getJSONArray("zi");
                                List<Live_broadcast> live_broadcasts = new ArrayList<>();
                                for (int j = 0;j<zis.length();j++){
                                    JSONObject Live_broadcast_json = zis.getJSONObject(j);
                                    Live_broadcast live_broadcast = new Live_broadcast();
                                    live_broadcast.setHid(Live_broadcast_json.getString("hid"));
                                    live_broadcast.setReviewurl(Live_broadcast_json.getString("reviewurl"));
                                    live_broadcast.setSurl(Live_broadcast_json.getString("surl"));
                                    live_broadcast.setTime(Live_broadcast_json.getString("dendtime"));
                                    live_broadcast.setTitle(Live_broadcast_json.getString("name"));
                                    live_broadcast.setTurl(Live_broadcast_json.getString("turl"));
                                    live_broadcast.setTid(Live_broadcast_json.getInt("tid"));
                                    live_broadcast.setDename(Live_broadcast_json.getString("dename"));
                                    live_broadcast.setBotten_text(Live_broadcast_json.getString("note"));

                                    live_broadcasts.add(live_broadcast);
                                }
                                direct_broadcast.setLive_broadcasts( live_broadcasts);
                                data.add(direct_broadcast);
                            }
                            baseListAdapter.notifyDataSetChanged();
                            direct_broadcast_list.setSelection(0);
                        } else {
                            isNoMore = true;
                            Toast.makeText(Direct_broadcast_main.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                     /*   for (int i = 0; i < 6; i++) {
//                                JSONObject customJson = dataArray.getJSONObject(i);
                            Direct_broadcast direct_broadcast = new Direct_broadcast();
                            direct_broadcast.setDirect_broadcast_item_type("1");
                            direct_broadcast.setDirect_broadcast_item_title( "测试测试测试title"+i);
                            direct_broadcast.setDirect_broadcast_item_icon("1");
                            direct_broadcast.setDirect_broadcast_item_add(""+(i+1));
                            direct_broadcast.setDirect_broadcast_item_banner(images.get(i));
                            direct_broadcast.setPosition(i);
//                            map.put("direct_broadcast_item_banner", images.get(i));
                            List<Live_broadcast> live_broadcasts = new ArrayList<>();
                            for (int j = 0;j<=i;j++){
                                live_broadcasts.add(new Live_broadcast("B3消化内镜"+j,"12月1日08:00 倒计时 1天2时30分"));
                            }
                            direct_broadcast.setLive_broadcasts( live_broadcasts);
                            data.add(direct_broadcast);
                        }*/
//                        MyProgressBarDialogTools.hide();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        MyProgressBarDialogTools.hide();
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
        setContentView(R.layout.direct_broadcast_main);
        context = this;
        findId();
        initdata();
        initBanner();
        initcategoryView();
        initDirect_broadcast_list();
        setmsgdb();
        initdirect_broadcast_type();
    }

    public void findId() {
        tabLayout = (TabLayout)findViewById(R.id.tablayout);
        direct_broadcast_list = (MyListView) findViewById(R.id.direct_broadcast_list);
        View home_center = LayoutInflater.from(context).inflate(R.layout.direct_broadcast_list_bottom, null);
        under_way = (LinearLayout) findViewById(R.id.under_way);
        under_way_color = findViewById(R.id.under_way_color);
        trail = (LinearLayout) findViewById(R.id.trail);
        trail_color = findViewById(R.id.trail_color);
        recent_review = (LinearLayout) findViewById(R.id.recent_review);
        recent_review_color = findViewById(R.id.recent_review_color);
        direct_broadcast_list.addFooterView(home_center);
        search = (ImageView) findViewById(R.id.search);
    }

    public void initdirect_broadcast_type(){
        under_way.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(direct_broadcast_type != 1){
                    direct_broadcast_type = 1;
                    under_way_color.setVisibility(View.VISIBLE);
                    trail_color.setVisibility(View.INVISIBLE);
                    recent_review_color.setVisibility(View.INVISIBLE);
                    page = 1;
                    setmsgdb();
                }
            }
        });
        trail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(direct_broadcast_type != 2){
                    direct_broadcast_type = 2;
                    under_way_color.setVisibility(View.INVISIBLE);
                    trail_color.setVisibility(View.VISIBLE);
                    recent_review_color.setVisibility(View.INVISIBLE);
                    page = 1;
                    setmsgdb();
                }
            }
        });
        recent_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(direct_broadcast_type != 3){
                    direct_broadcast_type = 3;
                    under_way_color.setVisibility(View.INVISIBLE);
                    trail_color.setVisibility(View.INVISIBLE);
                    recent_review_color.setVisibility(View.VISIBLE);
                    page = 1;
                    setmsgdb();
                }
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Popular_search3.class);
                intent.putExtra("mode","1");
                startActivity(intent);
            }
        });
    }

    public void initDirect_broadcast_list(){
        baseListAdapter = new BaseListAdapter(direct_broadcast_list, data, R.layout.direct_broadcast_list_item) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                //先判断该类型是 1、会议还是 2、专题
                Direct_broadcast direct_broadcast = (Direct_broadcast)item;
                switch (direct_broadcast.getDirect_broadcast_item_type()){
                    case "1":
                        //会议
                        helper.setText(R.id.direct_broadcast_item_type,   "会议");
                        helper.setBackground_Image(R.id.direct_broadcast_item_type,R.mipmap.direct_broadcast_icon02);
                        break;
                    case "2":
                        //专题
                        helper.setText(R.id.direct_broadcast_item_type,   "专题");
                        helper.setBackground_Image(R.id.direct_broadcast_item_type,R.mipmap.direct_broadcast_icon03);
                        break;
                    default:
                        //会议
                        helper.setText(R.id.direct_broadcast_item_type,   "会议");
                        helper.setBackground_Image(R.id.direct_broadcast_item_type,R.mipmap.direct_broadcast_icon02);
                        break;
                }
                //设置title
                helper.setText(R.id.direct_broadcast_item_title, direct_broadcast.getDirect_broadcast_item_title());
                //增加内容
                helper.setDirect_broadcastAdapter(R.id.direct_broadcast_item_myrecyclerview,direct_broadcast.getLive_broadcasts());
                //设置ICON状态
                if(direct_broadcast.getDirect_broadcast_item_icon().equals("1")){
                    helper.setBackground_Image(R.id.direct_broadcast_item_icon,R.mipmap.direct_broadcast_icon07);
                    helper.setTag(R.id.direct_broadcast_item_icon,"1");
                    helper.setVisibility(R.id.direct_broadcast_item_myrecyclerview,View.VISIBLE);
                }else{
                    helper.setBackground_Image(R.id.direct_broadcast_item_icon,R.mipmap.direct_broadcast_icon06);
                    helper.setTag(R.id.direct_broadcast_item_icon,"2");
                    helper.setVisibility(R.id.direct_broadcast_item_myrecyclerview,View.GONE);
                }
                helper.setDirect_broadcastOnClick(R.id.direct_broadcast_item_icon,R.id.direct_broadcast_item_myrecyclerview,baseListAdapter,direct_broadcast);
                //设置banner 图
                helper.setImageBitmapGlide(context, R.id.direct_broadcast_item_banner, direct_broadcast.getDirect_broadcast_item_banner());
                //设置位置
                helper.setText(R.id.position,direct_broadcast.getPosition()+"");
            }
        };
        direct_broadcast_list.setAdapter(baseListAdapter);
        // listview点击事件
        direct_broadcast_list.setOnItemClickListener(new casesharing_listListener());
        baseListAdapter.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > 0) {
                }
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = direct_broadcast_list.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        isNoMore = false;
                    }
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = direct_broadcast_list.getChildAt(direct_broadcast_list.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == direct_broadcast_list.getHeight()) {
                        if (!isNoMore) {
                            page += 1;
                            setmsgdb();
                        }
                    }
                }
            }
        });
    }

    /**
     * name: 点击查看某个直播的详细
     */
    private class casesharing_listListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {

            TextView textView = (TextView) view
                    .findViewById(R.id.position);
            String id = textView.getText().toString();
            Intent intent = new Intent(context,Live_broadcast_introduction.class);
            intent.putExtra("direct_broadcast",   data.get(Integer.parseInt(id)));
            startActivity(intent);
        }

    }

    public void initcategoryView(){
        tabLayout.addTab(tabLayout.newTab().setText("2月18日"));
        tabLayout.addTab(tabLayout.newTab().setText("2月19日"));
        tabLayout.addTab(tabLayout.newTab().setText("2月20日"));
        tabLayout.addTab(tabLayout.newTab().setText("2月21日"));
        tabLayout.addTab(tabLayout.newTab().setText("2月22日"));
        tabLayout.addTab(tabLayout.newTab().setText("2月24日"));
        tabLayout.addTab(tabLayout.newTab().setText("2月25日"));
        tabLayout.addTab(tabLayout.newTab().setText("2月26日"));
        tabLayout.addTab(tabLayout.newTab().setText("2月26日"));
        tabLayout.addTab(tabLayout.newTab().setText("2月26日"));
        tabLayout.addTab(tabLayout.newTab().setText("2月26日"));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

                @Override

                public void onTabSelected(TabLayout.Tab tab) {

                //选中了tab的逻辑
//                Log.e("选中了tab的逻辑",tab.getText()+"");
                            }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                //未选中tab的逻辑
//                    Log.e("未选中tab的逻辑",tab.getText()+"");
                            }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                //再次选中tab的逻辑
//                    Log.e("再次选中tab的逻辑",tab.getText()+"");
                            }

        });

    }

    public void initdata(){
        images.add("http://www.ccmtv.cn/do/ccmtvappandroid/dayicon/yaoqinghaoyou.jpg");
        images.add("http://www.ccmtv.cn/upload_files/new_upload_files/pro_sh/ccmtvhy/img/m1136.jpg");
        images.add("http://www.ccmtv.cn/upload_files/new_upload_files/pro_sh/ccmtvhy/img/m1112.jpg");
        images.add("http://www.ccmtv.cn/upload_files/new_upload_files/pro_sh/ccmtvhy/img/m1124.jpg");
        images.add("http://www.ccmtv.cn/upload_files/new_upload_files/pro_sh/ccmtvhy/img/m1028.jpg");
        images.add("http://www.ccmtv.cn/upload_files/new_upload_files/pro_sh/ccmtvhy/img/m1077.jpg");
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
                    JSONObject json = indexInfoArray.getJSONObject(position);
                    Intent intent = null;
                    if (json.getBoolean("isActivity")) {
                        intent = new Intent(context, ActivityWebActivity.class);
                        intent.putExtra("title", json.getString("title"));
                    } else {
                        if ("邀请好友".equals(json.getString("title"))) {
                            intent = new Intent(context, MyInvitationFriend.class);
                            intent.putExtra("type","home");
                        } else if ("呼吸时令".equals(json.getString("title"))) {
                            intent = new Intent(context, BreatheActivity.class);
                        } else if (json.getString("title").contains("柳叶杯")) {
                            intent = new Intent(context, WillowCupActivity.class);
                            intent.putExtra("title", json.getString("title"));
                        } else if (json.getString("title").contains("杏林杯")) {
                            intent = new Intent(context, ApricotActivity.class);
                            intent.putExtra("title", json.getString("title"));
                        } else {
                            intent = new Intent(context, SpecialActivity.class);
                        }
                    }
                    intent.putExtra("aid", json.getString("aid"));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    /**
     * name:设置listview中的值 author:Tom 2016-1-28下午3:42:47
     */
    public void setmsgdb() {
        if (page == 1) {
            data.removeAll(data);
            //   baseListAdapter.notifyDataSetChanged();
            MyProgressBarDialogTools.show(context);
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.liveIndex);
                    obj.put("style", direct_broadcast_type+"");
                    obj.put("uid", SharedPreferencesTools.getUid(context));

                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP_ccmtvapplive, obj.toString());
                    MyProgressBarDialogTools.hide();
//                    Log.e("直播首页",result);
//                    Log.e("直播首页",URLConfig.CCMTVAPP_ccmtvapplive);

                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }
}
