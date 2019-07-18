package com.linlic.ccmtv.yx.activity.vip;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.Popular_search;
import com.linlic.ccmtv.yx.activity.base.BaseFragment;
import com.linlic.ccmtv.yx.activity.entity.Home_Video;
import com.linlic.ccmtv.yx.activity.home.VideoFive;
import com.linlic.ccmtv.yx.activity.my.MyOpenMenberActivity;
import com.linlic.ccmtv.yx.activity.vip.adapter.OnRecyclerviewItemClickListener;
import com.linlic.ccmtv.yx.activity.vip.adapter.VipRecyclerViewAdapter;
import com.linlic.ccmtv.yx.activity.vip.adapter.VipRecyclerViewAdapter2;
import com.linlic.ccmtv.yx.activity.vip.adapter.VipRecyclerViewAdapter3;
import com.linlic.ccmtv.yx.adapter.home_videos_GridAdapter;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.util.ImageLoader;
import com.linlic.ccmtv.yx.utils.Carousel_figure;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.GlideImageLoader;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyGridView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.MyRecyclerView;
import com.linlic.ccmtv.yx.utils.RoundImageView;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.CircleImageView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.linlic.ccmtv.yx.widget.Util.dip2px;

/**
 * name：Vip专区
 * author：Larry
 * data：2016/4/12 14:42
 */
public class VipFragment_new extends BaseFragment implements View.OnClickListener{
    private static final int REQUEST_PERMISSION_STORAGE = 0x01;
    private RecyclerView vip_item_horv, vip_item_horv2;
    private MyRecyclerView vip_video;
    private VipRecyclerViewAdapter adapter;
    private VipRecyclerViewAdapter2 adapter2;
    private TextView iv_openvip, tv_tip;
    private CircleImageView iv_vipheader;
    private Banner banner;
    private MZBannerView exclusive_theater;
    private List<JSONObject> exclusive_theater_data = new ArrayList<>();
    private home_videos_GridAdapter home_videos_gridAdapter;
    private MyGridView videos;//视频集合
    public List<Home_Video> videodata = new ArrayList<>();
    private JSONArray indexInfoArray;
    private List<String> images = new ArrayList<>();
    private List<Map<String, Object>> vip_item_horv_data = new ArrayList<>();
    private List<Map<String, Object>> vip_item_horv_data2 = new ArrayList<>();
    private TextView username;//用户名
    private TextView vip_endtiem,selKeshi;//VIP到期时间
    private TextView keshi_handpick, keshi_internal_medicine, keshi_surgical, keshi_especially, keshi_other;//精选  内科  外科  妇儿科  其他
    private VipRecyclerViewAdapter3 vipRecyclerViewAdapter3;
    private ImageView img_default2_button_icon;
    private List<Map<String, Object>> vip_video_data = new ArrayList<>();
    private LinearLayout tv_tip2, exclusive_theater_layout, vip_section_more, privilege_more,ll_is_show;
    private ImageButton bt_set,bt_search;
    private String select_str= "精选";
    private int page=1;
    private BaseListAdapter myadapter;
    private NestedScrollView nestedScrollView;
    private OnRecyclerviewItemClickListener onRecyclerviewItemClickListener = new OnRecyclerviewItemClickListener() {
        @Override
        public void onItemClickListener(View v, int position) {
            //这里的view就是我们点击的view  position就是点击的position
            Intent intent = new Intent(getContext(), Vip_Channel.class);
            intent.putExtra("type",vip_item_horv_data.get(position).get("tv_name").toString());
            intent.putExtra("position",String.valueOf(position));
            startActivity(intent);
        }
    };
    private OnRecyclerviewItemClickListener onRecyclerviewItemClickListener2 = new OnRecyclerviewItemClickListener() {
        @Override
        public void onItemClickListener(View v, int position) {
            //  vip_item_horv_data2
            final String uid = SharedPreferencesTools.getUidToLoginClose(getContext());
            if (uid == null || ("").equals(uid)) {
                return;
            }
            Intent intent = new Intent(getContext(), Vip_Channel.class);
            intent.putExtra("type", vip_item_horv_data2.get(position).get("tv_name").toString());
            intent.putExtra("position","-1");
            getContext().startActivity(intent);
        }
    };
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {
                            JSONObject dataJson = result.getJSONObject("data");

                            //获取个人信息集合 person_info:个人信息
                            JSONObject person_info = dataJson.getJSONObject("person_info");
                            //设置用户头像
                            new Carousel_figure(getActivity()).loadImageNoCache(person_info.getString("icon"), iv_vipheader);  //无缓存
                            //isvip =3 未登录
                            switch (person_info.getString("isvip")) {
                                case "1":
                                    //设置VIP标识出来
                                    img_default2_button_icon.setVisibility(View.VISIBLE);
                                    //是VIP 登录了 显示登录层出来
                                    tv_tip2.setVisibility(View.VISIBLE);
                                    tv_tip.setVisibility(View.GONE);
                                    //设置用户名
                                    username.setText("用户"+person_info.getString("username"));
                                    //设置VIP到期时间
                                    vip_endtiem.setText("有效期： " + dataJson.getString("vipEndTime").substring(0, 10));
                                    //设置开通VIP按钮状态
                                    iv_openvip.setText("续费");
                                    break;
                                case "2":
                                    //设置VIP标识隐藏
                                    img_default2_button_icon.setVisibility(View.GONE);
                                    //是VIP 登录了 显示登录层出来
                                    tv_tip2.setVisibility(View.VISIBLE);
                                    tv_tip.setVisibility(View.GONE);
                                    //设置用户名
                                    username.setText("用户"+person_info.getString("username"));
//                                    if(dataJson.getString("vipEndTime").length()>0){
//                                        //设置VIP到期时间
//                                        vip_endtiem.setText("VIP 有效期： "+dataJson.getString("vipEndTime").substring(0,10));
//                                    }else{
//                                        //未购买过VIP  没有到期时间
//                                        vip_endtiem.setText("开通VIP观看海量专属视频");
//                                    }

                                    vip_endtiem.setText("开通VIP观看海量专属视频");
                                    //设置开通VIP按钮状态
                                    iv_openvip.setText("开通VIP");
                                    break;
                                case "3":
                                    //设置VIP标识隐藏
                                    img_default2_button_icon.setVisibility(View.GONE);
                                    //是VIP 登录了 显示登录层出来
                                    tv_tip2.setVisibility(View.GONE);
                                    tv_tip.setVisibility(View.VISIBLE);
                                    //设置开通VIP按钮状态
                                    iv_openvip.setText("去登录");
                                    tv_tip.setText("登录后开通VIP，观看海量专属视频");
                                    break;
                                default:
                                    //设置VIP标识隐藏
                                    img_default2_button_icon.setVisibility(View.GONE);
                                    break;
                            }

                            //设置banner 源数据
                            indexInfoArray = dataJson.getJSONArray("lun_Data");
                            //设置轮播图的图片
                            images.clear();
                            for (int i = 0; i < indexInfoArray.length(); i++) {
//                                LogUtil.e("banner——vip图片地址",indexInfoArray.getJSONObject(i).get("picurl").toString());
                                images.add(indexInfoArray.getJSONObject(i).get("picurl").toString());
                            }
                            //初始化轮播图
                            initBanner();
                            //获取VIP 权限集合
                            vip_item_horv_data.clear();
                            JSONArray Vip_item_horvJson = dataJson.getJSONArray("icons");
                            for (int i = 0; i < Vip_item_horvJson.length(); i++) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("tv_name", Vip_item_horvJson.getJSONObject(i).getString("name"));
                                map.put("tv_img", Vip_item_horvJson.getJSONObject(i).getString("icon"));
                                vip_item_horv_data.add(map);
                            }

                            initVip_item_horv();
                            vip_item_horv_data2.clear();
                            //获取VIP 权限集合
                            JSONArray Vip_item_horvJson2 = dataJson.getJSONArray("vicon");
                            for (int i = 0; i < Vip_item_horvJson2.length(); i++) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("tv_name", Vip_item_horvJson2.getJSONObject(i).getString("name"));
                                map.put("tv_img", Vip_item_horvJson2.getJSONObject(i).getString("icon"));
                                vip_item_horv_data2.add(map);
                            }

                            initVip_item_horv2();
                            vip_video_data.clear();
                            //获取视频集合
                            JSONArray Vip_videosJson = dataJson.getJSONArray("videos");
                            for (int i = 0; i < Vip_videosJson.length(); i++) {
                                JSONObject vip_video = Vip_videosJson.getJSONObject(i);
                                Map<String, Object> map = new HashMap<>();
                                map.put("title", vip_video.getString("name"));
                                map.put("video", vip_video.getJSONArray("date"));
                                vip_video_data.add(map);
                            }
                            //初始化列表
                            initVip_item_horv3();

                            if (dataJson.has("video_tui")) {
                                if (dataJson.getJSONArray("video_tui").length() > 0) {
                                    exclusive_theater_data.clear();
                                    exclusive_theater_layout.setVisibility(View.VISIBLE);
                                    //获取专属影院集合
                                    JSONArray exclusive_theaterJson = dataJson.getJSONArray("video_tui");
                                    for (int i = 0; i < exclusive_theaterJson.length(); i++) {
                                        exclusive_theater_data.add(exclusive_theaterJson.getJSONObject(i));
                                    }
                                    initMZBannerView();
                                } else {
                                    exclusive_theater_layout.setVisibility(View.GONE);
                                }

                            } else {
                                exclusive_theater_layout.setVisibility(View.GONE);
                            }

                        } else {
                            Toast.makeText(getActivity(), R.string.post_hint1, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {
                            MyProgressBarDialogTools.hide();
                            JSONArray dataJson = result.getJSONArray("data");
                            if(dataJson.length()<20){
                                isNoMore=true;
                            }
                            if(page==1){
                                videodata.clear();
                            }
                            for (int i = 0; i < dataJson.length(); i++) {
                                JSONObject videoJson = dataJson.getJSONObject(i);
                                videodata.add(new Home_Video(videoJson.getString("aid"), videoJson.getString("title"), videoJson.getString("picurl"), videoJson.getString("flag"), videoJson.getString("videopaymoney"), videoJson.getString("money")));
                            }

                                myadapter= new BaseListAdapter(videos,videodata,R.layout.home_grid_item) {
                                    @Override
                                    public void refresh(Collection datas) {
                                        super.refresh(datas);
                                    }

                                    @Override
                                    public void convert(ListHolder helper, Object item, boolean isScrolling) {
                                        super.convert(helper, item, isScrolling);
                                        Home_Video data = (Home_Video) item;
                                        ImageView imageView=helper.getView(R.id.video_cover_chart);
                                        helper.setText(R.id.video_title,data.getTitle());
                                        RequestOptions options = new RequestOptions()
                                                .placeholder(R.mipmap.img_default)
                                                .error(R.mipmap.img_default);
                                        Glide.with(getActivity())
                                                .load(FirstLetter.getSpells(data.getPicurl()))
                                                .apply(options)
                                                .into(imageView);
                                    }
                                };
                                videos.setAdapter(myadapter);

                            nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                                @Override
                                public void onScrollChange(NestedScrollView v, int x, int y, int oldScrollX, int oldScrollY) {
                                    int scrollY = v.getScrollY();//顶端以及滑出去的距离
                                    int height = v.getHeight();//界面的高度
                                    int scrollViewMeasuredHeight = v.getChildAt(0).getMeasuredHeight();//scrollview所占的高度
                                    if (scrollY == 0) {//在顶端的时候
              //                    Toast.makeText(getContext(),"在顶端的时候  ", Toast.LENGTH_SHORT).show();
                                    } else if ((scrollY + height) == scrollViewMeasuredHeight) {//当在底部的时候
                                 //                    Toast.makeText(getContext(),"当在底部的时候    ", Toast.LENGTH_SHORT).show();
                                        if (!isNoMore) {
                                            page++;
                                            initvipVideo(select_str);
                                        }
                                    } else {//当在中间的时候
//                    Toast.makeText(getContext(),"当在中间的时候      ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                           // home_videos_gridAdapter.notifyDataSetChanged();

                        } else {
                            MyProgressBarDialogTools.hide();
                            isNoMore = true;
                            Toast.makeText(getActivity(), R.string.post_hint1, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
//                    try {
//                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
//                        if (jsonObject.getInt("status") == 1) { // 成功
//                            if(page==1){
//                                    String Keshi=jsonObject.getString("selKeshi");
//                                    if(Keshi.equals("")){
//                                        selKeshi.setVisibility(View.GONE);
//                                    }else {
//                                        selKeshi.setText(jsonObject.getString("selKeshi"));
//                                        selKeshi.setVisibility(View.VISIBLE);
//                                    }
//                            }
//                        } else {
//                            Toast.makeText(getContext(),jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }

                    break;

                default:
                    break;
            }
        }
    };

    private boolean isNoMore=false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // 解决当程序crash，切换fragment无效的问题
        if (savedInstanceState != null) {
            String FRAGMENTS_TAG = "android:support:fragments";
            // remove掉保存的Fragment
            savedInstanceState.remove(FRAGMENTS_TAG);
        }
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_vip_new, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //  vip_recyclerview_horizontal = (RecyclerView) getActivity().findViewById(R.id.vip_recyclerview_horizontal);
        findViewId();
        checkSDCardPermission();
//        initView();
        ImageGalleryAdapter(getContext());
        setonclick();
        initDataImgAndTop();
        initVideos();
    }

    public void ImageGalleryAdapter(Context c) {
        DisplayMetrics dm = c.getResources().getDisplayMetrics();
        float screenWidth = dm.widthPixels;
        float screenHeight = dm.heightPixels;
        float sss = screenHeight / screenWidth;
//        LogUtil.e("这是比例",sss+"");
//        LogUtil.e("这是比例",screenWidth+"=="+screenHeight);
        if (sss > 1.80) {

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) banner.getLayoutParams();
            //获取当前控件的布局对象
            params.height = dip2px(getContext(), 220);
            ;//设置当前控件布局的高度
            banner.setLayoutParams(params);//将设置好的布局参数应用到控件中

        }
    }

    private void initMZBannerView() {
        exclusive_theater.setIndicatorVisible(false);
        // 设置数据
        exclusive_theater.setPages(exclusive_theater_data, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });
        exclusive_theater.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int i) {
                try {
                    final String uid = SharedPreferencesTools.getUidToLoginClose(getContext());
                    if (uid == null || ("").equals(uid)) {
                        return;
                    }
                    if (VideoFive.isFinish == null) {
                        Intent intent = new Intent(getContext(), VideoFive.class);
                        intent.putExtra("aid", exclusive_theater_data.get(i).getString("aid"));
                        getContext().startActivity(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()){
            case R.id.bt_set:
                intent=new Intent(getActivity(),Vip_Channel_select.class);
                intent.putExtra("type",select_str);
                break;
            case R.id.bt_search:
                intent = new Intent(getActivity(), Popular_search.class);
                intent.putExtra("mode", "1");
                break;
        }
        if(intent!=null){
            startActivity(intent);
        }
    }

    public class BannerViewHolder implements MZViewHolder<JSONObject> {
        private RoundImageView mImageView;
        private TextView mText;
        private TextView mAid;

        @Override
        public View createView(Context context) {
            // 返回页面布局
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item2, null);
            mImageView = (RoundImageView) view.findViewById(R.id.banner_image);
            mText = (TextView) view.findViewById(R.id.banner_text);
            mAid = (TextView) view.findViewById(R.id.banner_aid);
            return view;
        }

        @Override
        public void onBind(Context context, int position, JSONObject data) {
            try {
                // 数据绑定
                ImageLoader.load(context, data.getString("picurl"), mImageView);
                mText.setText(data.getString("title"));
                mAid.setTag(data.getString("aid"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 检查SD卡权限
     */
    protected void checkSDCardPermission() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
        }
    }

    public void initBanner() {

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


                    final String uid = SharedPreferencesTools.getUidToLoginClose(getContext());
                    if (uid == null || ("").equals(uid)) {
                        return;
                    }
                    if (VideoFive.isFinish == null) {
                        Intent intent = new Intent(getContext(), VideoFive.class);
                        intent.putExtra("aid", json.getString("aid"));
                        getContext().startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        banner.setIndicatorGravity(BannerConfig.NOT_INDICATOR);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }


    public void initVip_item_horv() {
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
//        linearSnapHelper.attachToRecyclerView(vip_item_horv);
        vip_item_horv.setLayoutManager(linearLayoutManager);
        adapter = new VipRecyclerViewAdapter(getActivity(), vip_item_horv_data, onRecyclerviewItemClickListener);
        adapter.setSelectIndex(0);
        vip_item_horv.setAdapter(adapter);
    }

    public void initVip_item_horv2() {
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        vip_item_horv2.setLayoutManager(linearLayoutManager);
        adapter2 = new VipRecyclerViewAdapter2(getActivity(), vip_item_horv_data2, onRecyclerviewItemClickListener2);
        adapter2.setSelectIndex(0);
        vip_item_horv2.setAdapter(adapter2);
    }

    public void initVip_item_horv3() {
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        vip_video.setLayoutManager(linearLayoutManager);
        vipRecyclerViewAdapter3 = new VipRecyclerViewAdapter3(getActivity(), vip_video_data);
        vipRecyclerViewAdapter3.setSelectIndex(0);
        vip_video.setAdapter(vipRecyclerViewAdapter3);
    }

    public void initVideos() {
//        home_videos_gridAdapter = new home_videos_GridAdapter(getActivity(), videodata);
//        videos.setAdapter(home_videos_gridAdapter);
//        videos.setSelector(new ColorDrawable(Color.TRANSPARENT));//取消GridView中Item选中时默认的背景色
        videos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String uid = SharedPreferencesTools.getUidToLoginClose(getContext());
                if (uid == null || ("").equals(uid)) {
                    return;
                }
                if (VideoFive.isFinish == null) {
                    Intent intent = new Intent(getContext(), VideoFive.class);
                    intent.putExtra("aid", videodata.get(position).getAid());
                    getContext().startActivity(intent);
                }
            }
        });

    }

    private void findViewId() {
        banner = (Banner) getActivity().findViewById(R.id.banner9);
        iv_openvip = (TextView) getActivity().findViewById(R.id.iv_openvip);
        iv_vipheader = (CircleImageView) getActivity().findViewById(R.id.iv_vipheader);
        vip_item_horv = (RecyclerView) getActivity().findViewById(R.id.vip_item_horv);
        vip_item_horv2 = (RecyclerView) getActivity().findViewById(R.id.vip_item_horv2);
        tv_tip = (TextView) getActivity().findViewById(R.id.tv_tip);
        username = (TextView) getActivity().findViewById(R.id.username);
        vip_endtiem = (TextView) getActivity().findViewById(R.id.vip_endtiem);
        selKeshi = (TextView) getActivity().findViewById(R.id.selKeshi);
        keshi_handpick = (TextView) getActivity().findViewById(R.id.keshi_handpick);
        keshi_internal_medicine = (TextView) getActivity().findViewById(R.id.keshi_internal_medicine);
        keshi_surgical = (TextView) getActivity().findViewById(R.id.keshi_surgical);
        keshi_especially = (TextView) getActivity().findViewById(R.id.keshi_especially);
        keshi_other = (TextView) getActivity().findViewById(R.id.keshi_other);
        videos = (MyGridView) getActivity().findViewById(R.id.videoss);
        vip_video = (MyRecyclerView) getActivity().findViewById(R.id.vip_video);
        vip_video.setNestedScrollingEnabled(false);//解决Scrollview嵌套RecyclerView滑动不流畅的问题
        exclusive_theater = (MZBannerView) getActivity().findViewById(R.id.exclusive_theater);
        img_default2_button_icon = (ImageView) getActivity().findViewById(R.id.img_default2_button_icon);
        tv_tip2 = (LinearLayout) getActivity().findViewById(R.id.tv_tip2);
        exclusive_theater_layout = (LinearLayout) getActivity().findViewById(R.id.exclusive_theater_layout);
        vip_section_more = (LinearLayout) getActivity().findViewById(R.id.vip_section_more);
        privilege_more = (LinearLayout) getActivity().findViewById(R.id.privilege_more);
        ll_is_show = (LinearLayout) getActivity().findViewById(R.id.ll_is_show);
        bt_set = (ImageButton)getActivity().findViewById(R.id.bt_set);
        bt_search = (ImageButton) getActivity().findViewById(R.id.bt_search);
        nestedScrollView = (NestedScrollView)getActivity().findViewById(R.id.id_nested_scrollview_conference);
        bt_set.setOnClickListener(this);
        bt_search.setOnClickListener(this);

    }

    public void setonclick() {
        iv_openvip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPreferencesTools.getUids(getActivity()) == null) {
                    SharedPreferencesTools.getUid(getContext());
                } else {
                    Intent intent = new Intent(getActivity(), MyOpenMenberActivity.class);
                    startActivity(intent);
                }

            }
        });
        //        private TextView keshi_handpick,keshi_internal_medicine,keshi_surgical,keshi_especially,keshi_other;//精选  内科  外科  妇儿科  其他
        keshi_handpick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //改变选中字体颜色
//                keshi_handpick.setTextColor(Color.parseColor("#333333"));
//                keshi_internal_medicine.setTextColor(Color.parseColor("#666666"));
//                keshi_surgical.setTextColor(Color.parseColor("#666666"));
//                keshi_especially.setTextColor(Color.parseColor("#666666"));
//                keshi_other.setTextColor(Color.parseColor("#666666"));
                //改变选中背景状态
                keshi_handpick.setBackground(getResources().getDrawable(R.drawable.vip_department_bg));
                keshi_internal_medicine.setBackground(null);
                keshi_surgical.setBackground(null);
                keshi_especially.setBackground(null);
                keshi_other.setBackground(null);
                //重新加载相对应的视频
                page=1;
                initvipVideo(keshi_handpick.getText().toString());
                //getUrlRulest(keshi_handpick.getText().toString());
                vip_section_more.setTag("精选");
            }
        });
        keshi_internal_medicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //改变选中字体颜色
//                keshi_handpick.setTextColor(Color.parseColor("#666666"));
//                keshi_internal_medicine.setTextColor(Color.parseColor("#333333"));
//                keshi_surgical.setTextColor(Color.parseColor("#666666"));
//                keshi_especially.setTextColor(Color.parseColor("#666666"));
//                keshi_other.setTextColor(Color.parseColor("#666666"));
                //改变选中背景状态
                keshi_handpick.setBackground(null);
                keshi_internal_medicine.setBackground(getResources().getDrawable(R.drawable.vip_department_bg));
                keshi_surgical.setBackground(null);
                keshi_especially.setBackground(null);
                keshi_other.setBackground(null);
                //重新加载相对应的视频
                page=1;
                initvipVideo(keshi_internal_medicine.getText().toString());
                //getUrlRulest(keshi_internal_medicine.getText().toString());
                vip_section_more.setTag("内科");
            }
        });
        keshi_surgical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //改变选中字体颜色
//                keshi_handpick.setTextColor(Color.parseColor("#666666"));
//                keshi_internal_medicine.setTextColor(Color.parseColor("#666666"));
//                keshi_surgical.setTextColor(Color.parseColor("#333333"));
//                keshi_especially.setTextColor(Color.parseColor("#666666"));
//                keshi_other.setTextColor(Color.parseColor("#666666"));
                //改变选中背景状态
                keshi_handpick.setBackground(null);
                keshi_internal_medicine.setBackground(null);
                keshi_surgical.setBackground(getResources().getDrawable(R.drawable.vip_department_bg));
                keshi_especially.setBackground(null);
                keshi_other.setBackground(null);
                //重新加载相对应的视频
                page=1;
                initvipVideo(keshi_surgical.getText().toString());
                //getUrlRulest(keshi_surgical.getText().toString());
                vip_section_more.setTag("外科");
            }
        });
        keshi_especially.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //改变选中字体颜色
//                keshi_handpick.setTextColor(Color.parseColor("#666666"));
//                keshi_internal_medicine.setTextColor(Color.parseColor("#666666"));
//                keshi_surgical.setTextColor(Color.parseColor("#666666"));
//                keshi_especially.setTextColor(Color.parseColor("#333333"));
//                keshi_other.setTextColor(Color.parseColor("#666666"));
                //改变选中背景状态
                keshi_handpick.setBackground(null);
                keshi_internal_medicine.setBackground(null);
                keshi_surgical.setBackground(null);
                keshi_especially.setBackground(getResources().getDrawable(R.drawable.vip_department_bg));
                keshi_other.setBackground(null);
                //重新加载相对应的视频
                page=1;
                initvipVideo(keshi_especially.getText().toString());
                //getUrlRulest(keshi_especially.getText().toString());
                vip_section_more.setTag("妇儿科");
            }
        });
        keshi_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //改变选中字体颜色
//                keshi_handpick.setTextColor(Color.parseColor("#666666"));
//                keshi_internal_medicine.setTextColor(Color.parseColor("#666666"));
//                keshi_surgical.setTextColor(Color.parseColor("#666666"));
//                keshi_especially.setTextColor(Color.parseColor("#666666"));
//                keshi_other.setTextColor(Color.parseColor("#333333"));
                //改变选中背景状态
                keshi_handpick.setBackground(null);
                keshi_internal_medicine.setBackground(null);
                keshi_surgical.setBackground(null);
                keshi_especially.setBackground(null);
                keshi_other.setBackground(getResources().getDrawable(R.drawable.vip_department_bg));
                //重新加载相对应的视频
                page=1;
                initvipVideo(keshi_other.getText().toString());
                //getUrlRulest(keshi_other.getText().toString());
                vip_section_more.setTag("其他");
            }
        });


    }

    @Override
    public void onResume() {
//        page = "1";
//        getUrlRulest(select_str);
        super.onResume();
    }

    public void initDataImgAndTop() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.vipIndex);
                    obj.put("version","1");
                    if (SharedPreferencesTools.getUidONnull(getContext()).length() > 0) {
                        obj.put("uid", SharedPreferencesTools.getUidONnull(getContext()));
                    }
                    String result = HttpClientUtils.sendPost(getActivity(),
                            URLConfig.CCMTVAPP1, obj.toString());
                    LogUtil.e("VIP首页资料",result);
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

    private void initvipVideo(final String str) {
        MyProgressBarDialogTools.show(getActivity());
        isNoMore = false;
        if (page == 1) {
            videodata.clear();
        }
        select_str = str;
        if(str.equals("精选")){
            ll_is_show.setVisibility(View.VISIBLE);
            videos.setVisibility(View.GONE);
        }else {
            ll_is_show.setVisibility(View.GONE);
            videos.setVisibility(View.VISIBLE);
        }

        Runnable runnable0 = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.vipVideo);
                    if (SharedPreferencesTools.getUidONnull(getContext()).length() > 0) {
                        obj.put("uid", SharedPreferencesTools.getUidONnull(getContext()));
                    }
                    obj.put("page", page);
                    obj.put("more", "more");
                    obj.put("keshi",select_str);
                    String result = HttpClientUtils.sendPost(getActivity(),
                            URLConfig.CCMTVAPP1, obj.toString());
//                    LogUtil.e("VIP专区视频",result);
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
        new Thread(runnable0).start();
    }

//    public void getUrlRulest( String str){
//
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    JSONObject obj = new JSONObject();
//                    obj.put("act", URLConfig.vipVideo);
//                    if (SharedPreferencesTools.getUidONnull(getContext()).length() > 0) {
//                        obj.put("uid", SharedPreferencesTools.getUidONnull(getContext()));
//                    }
//                    obj.put("page", page);
//                    obj.put("more", "more");
//                    obj.put("keshi", select_str);
//                    String result = HttpClientUtils.sendPost(getActivity(), URLConfig.CCMTVAPP1, obj.toString());
////                    LogUtil.e("首页数据", result);
//
//                    Message message = new Message();
//                    message.what = 3;
//                    message.obj = result;
//                    handler.sendMessage(message);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    handler.sendEmptyMessage(500);
//                }
//            }
//        };
//        new Thread(runnable).start();
//    }


}