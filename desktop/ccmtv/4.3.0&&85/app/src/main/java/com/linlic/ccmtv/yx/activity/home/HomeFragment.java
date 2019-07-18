package com.linlic.ccmtv.yx.activity.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.CustomActivity;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.Popular_search;
import com.linlic.ccmtv.yx.activity.base.BaseFragment;
import com.linlic.ccmtv.yx.activity.channel.Channel_selection;
import com.linlic.ccmtv.yx.activity.conference.ConferenceMainActivity;
import com.linlic.ccmtv.yx.activity.direct_broadcast.Direct_broadcast_main;
import com.linlic.ccmtv.yx.activity.home.adapter.HomeAdapter;
import com.linlic.ccmtv.yx.activity.home.adapter.HomeGVAdapter;
import com.linlic.ccmtv.yx.activity.home.adapter.RecyclerViewAdapter;
import com.linlic.ccmtv.yx.activity.home.apricotcup.ApricotActivity;
import com.linlic.ccmtv.yx.activity.home.entry.FloatArrInfo;
import com.linlic.ccmtv.yx.activity.home.entry.IconArr;
import com.linlic.ccmtv.yx.activity.home.entry.KeshiData;
import com.linlic.ccmtv.yx.activity.home.hxsl.BreatheActivity;
import com.linlic.ccmtv.yx.activity.home.util.RecyclerItemClickListener;
import com.linlic.ccmtv.yx.activity.home.willowcup.WillowCupActivity;
import com.linlic.ccmtv.yx.activity.home.yxzbjrrom.MedicalLiveRoomActivity;
import com.linlic.ccmtv.yx.activity.hospital_training.Hospital_training_entrance;
import com.linlic.ccmtv.yx.activity.hospital_training.Hospital_training_main;
import com.linlic.ccmtv.yx.activity.login.LoginActivity;
import com.linlic.ccmtv.yx.activity.login.Qr_code_to_log_in;
import com.linlic.ccmtv.yx.activity.my.Integral;
import com.linlic.ccmtv.yx.activity.my.MyInvitationFriend;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.kzbf.MedicineMessageActivity;
import com.linlic.ccmtv.yx.kzbf.SkyProtocolActivity;
import com.linlic.ccmtv.yx.util.ImageLoader;
import com.linlic.ccmtv.yx.utils.Carousel_figure;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.GlideImageLoader;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.XUtilsImageLoader;
import com.linlic.ccmtv.yx.utils.carouselFigure.CycleViewPager;
import com.linlic.ccmtv.yx.widget.MyGridView;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * name:首页
 * author:Tom
 * 2016-3-2下午7:02:24
 * http://www.cnblogs.com/-cyb/articles/android_TextView_WordWrap.html
 */
public class HomeFragment extends BaseFragment implements OnClickListener, AdapterView.OnItemClickListener {
    private ImageView browsingRecord, personalCenter, scanning;
    private RelativeLayout search;
    private TextView integral_main;
    private String token;
    private List<IconArr> iconArrs;
    private RecyclerView recyclerview_horizontal;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ScrollView mScrollView;
    private int REQUEST_CODE = 1;
    private MyGridView my_gridview;
    private ImageView medical_examination_img;
    private JSONObject medical_examination_JSON = new JSONObject();
    private HomeGVAdapter homegvadapter;
    private Carousel_figure carousel_figure;
    private CycleViewPager cycleViewPager;
    private HomeAdapter adapter;                        //首页列表数据适配器
    private List<FloatArrInfo> floatArrInfos = new ArrayList<>();
    private List<KeshiData> keshiDatas = new ArrayList<>();
    private List<KeshiData> keshiDatas_nonull = new ArrayList<>();
    private List<String> images = new ArrayList<>();
    private JSONArray indexInfoArray;
    private ListView list_home;           //底部列表
    private Banner banner;
    JSONObject result, data;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        keshiDatas_nonull.clear();
                        result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {// 成功
                            data = result.getJSONObject("data");
                            indexInfoArray = new JSONArray();
                            images.clear();
                            if (data.has("hosData")) {
                                medical_examination_JSON = data.getJSONObject("hosData");//医院培训入口
                                ImageLoader.load(getContext(), medical_examination_JSON.getString("imgurl"), medical_examination_img);
                                medical_examination_img.setVisibility(View.VISIBLE);
                            } else {
                                medical_examination_JSON = data.getJSONObject("hosData");//医院培训入口
                                medical_examination_img.setVisibility(View.GONE);
                            }
                            JSONArray indexInfo = new JSONArray(data.getString("indexInfo"));
                            JSONArray activityArray = new JSONArray(data.getString("ccmtvactivity"));
                            for (int i = 0; i < activityArray.length(); i++) {
                                JSONObject activityObj = new JSONObject(activityArray.get(i).toString());
                                activityObj.put("picurl", activityObj.getString("imgurl"));
                                images.add(activityObj.getString("picurl"));
                                activityObj.put("aid", activityObj.getString("activityurl"));
                                activityObj.put("title", activityObj.getString("activitytitle"));
                                if (activityObj.getString("urlflg").equals("1")) {
                                    activityObj.put("isActivity", true);
                                } else {
                                    activityObj.put("isActivity", false);
                                }
                                indexInfoArray.put(activityObj);
                            }
                            for (int i = 0; i < indexInfo.length(); i++) {
                                JSONObject activityObj = new JSONObject(indexInfo.get(i).toString());
                                activityObj.put("picurl", activityObj.getString("picurl"));
                                images.add(activityObj.getString("picurl"));
                                activityObj.put("aid", activityObj.getString("aid"));
                                activityObj.put("title", activityObj.getString("title"));
                                if (activityObj.getString("urlflg").equals("1")) {
                                    activityObj.put("isActivity", true);
                                } else {
                                    activityObj.put("isActivity", false);
                                }
                                indexInfoArray.put(activityObj);
                            }

                         /*   for (int i = 0; i < indexInfoArray.length(); i++) {
                                Log.e("轮播图地址", indexInfoArray.get(i).toString());
                            }*/

                            //轮播图
//                            carousel_figure.initialize(indexInfoArray.toString(), cycleViewPager);
                            initBanner();
                            iconArrs = new Gson().fromJson(data.getJSONArray("iconArr").toString()
                                    , new TypeToken<List<IconArr>>() {
                                    }.getType());
//                            Log.e("iconArrs", iconArrs.toString());
                            homegvadapter = new HomeGVAdapter(getActivity(), iconArrs);
                            my_gridview.setAdapter(homegvadapter);
                            //顶部导航栏 http://blog.csdn.net/aaawqqq/article/details/25082417
                            floatArrInfos = new Gson().fromJson(data.getJSONArray("floatArrInfo").toString()
                                    , new TypeToken<List<FloatArrInfo>>() {
                                    }.getType());

                            //设置布局管理器
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                            recyclerview_horizontal.setLayoutManager(linearLayoutManager);
                            recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), floatArrInfos);
                            recyclerViewAdapter.setSelectIndex(0);
                            recyclerview_horizontal.setAdapter(recyclerViewAdapter);
                            recyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    recyclerViewAdapter.setSelectIndex(position);
                                    list_home.setSelection(position + 1);
                                    recyclerViewAdapter.notifyDataSetChanged();
                                }
                            });
                            list_home.setOnScrollListener(new AbsListView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(AbsListView view, int scrollState) {
                                }

                                @Override
                                public void onScroll(AbsListView view, final int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                                    if (firstVisibleItem > 0) {
                                        recyclerViewAdapter.setSelectIndex(firstVisibleItem - 1);
                                        recyclerview_horizontal.scrollToPosition(firstVisibleItem - 1);
                                        recyclerViewAdapter.notifyDataSetChanged();
                                    }
                                }
                            });

                            //底部科室数据
                            keshiDatas = new Gson().fromJson(data.getJSONArray("keshiData").toString()
                                    , new TypeToken<List<KeshiData>>() {
                                    }.getType());
                            for (KeshiData keshidata : keshiDatas) {
                              /*  Log.e("首页可是数据",keshidata.toString());*/
                                if (keshidata.getListdata().size() > 0) {
                                    keshiDatas_nonull.add(keshidata);
                                }
                            }
                            adapter = new HomeAdapter(getActivity(), keshiDatas_nonull);
                            list_home.setAdapter(adapter);
                        } else {                                                                        // 失败
                            Toast.makeText(getActivity(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        String uid = SharedPreferencesTools.getUidToLoginClose(getActivity());
                        if (result.getInt("status") == 1) {// 成功
                            JSONObject dataArray = result.getJSONObject("data");
                            Intent intent = new Intent(getActivity(), VideoFive.class);
                            intent.putExtra("aid", dataArray.getString("aid"));
                            if (dataArray.getString("videoplaystyle").equals("noresource")) {
                                Toast.makeText(getActivity(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(intent);
                            }
                        } else {// 失败
                            MyProgressBarDialogTools.hide();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {// 成功
                            Intent intent = new Intent(getActivity(), Qr_code_to_log_in.class);
                            intent.putExtra("token", token);
                            startActivity(intent);
                        } else {// 失败
                            Toast.makeText(getActivity(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getString("code").equals("0")) {// 成功
                            if (result.getString("sub").equals("1")) {//已签署
                                startActivity(new Intent(getActivity(), MedicineMessageActivity.class));
                            } else {// 未签署
                                startActivity(new Intent(getActivity(), SkyProtocolActivity.class));
                            }
                        } else {// 失败
                            Toast.makeText(getContext(), result.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    Toast.makeText(getActivity(), R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_hometree, container, false);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findById();
        addView();
        init();
        setOnclik();
    }

    private void addView() {
        View home_center = LayoutInflater.from(getActivity()).inflate(R.layout.home_center, null);
        my_gridview = (MyGridView) home_center.findViewById(R.id.my_gridview);
        medical_examination_img = (ImageView) home_center.findViewById(R.id.medical_examination_img_x);
        list_home.addHeaderView(home_center);
    }

    private void init() {
        carousel_figure = new Carousel_figure(getActivity());
           /*轮播图start*/
        carousel_figure.configImageLoader();
//        cycleViewPager = (CycleViewPager) getActivity().getFragmentManager().findFragmentById(R.id.fragment_cycle_viewpager_content);
             /*轮播图end*/
        getUrlRulest();
    }

    public void initBanner() {
        banner = (Banner) getActivity().findViewById(R.id.banner);
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
                        intent = new Intent(getActivity(), ActivityWebActivity.class);

                            intent.putExtra("title", json.getString("title"));

                    } else {
                        if ("邀请好友".equals(json.getString("title"))) {
                            intent = new Intent(getActivity(), MyInvitationFriend.class);
                            intent.putExtra("type","home");
                        } else if ("呼吸时令".equals(json.getString("title"))) {
                            intent = new Intent(getActivity(), BreatheActivity.class);
                        } else if (json.getString("title").contains("柳叶杯")) {
                            intent = new Intent(getActivity(), WillowCupActivity.class);
                            intent.putExtra("title", json.getString("title"));
                        } else if (json.getString("title").contains("杏林杯")) {
                            intent = new Intent(getActivity(), ApricotActivity.class);
                            intent.putExtra("title", json.getString("title"));
                        } else {
                            intent = new Intent(getActivity(), SpecialActivity.class);
                        }
                    }
                    if(json.getString("urlflg").equals("999")){
                        intent.putExtra("aid", json.getString("aid")+"?uid="+SharedPreferencesTools.getUid(getContext()));
                    }else{
                        intent.putExtra("aid", json.getString("aid"));
                    }
                    getActivity().startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    public void initdata() {
        getUrlRulest();
    }

    public void findById() {
        super.findId();
        browsingRecord = (ImageView) getActivity().findViewById(R.id.browsingRecord);
        search = (RelativeLayout) getActivity().findViewById(R.id.search);
        personalCenter = (ImageView) getActivity().findViewById(R.id.personalCenter);
        scanning = (ImageView) getActivity().findViewById(R.id.scanning);
        list_home = (ListView) getActivity().findViewById(R.id.list_home);
        recyclerview_horizontal = (RecyclerView) getActivity().findViewById(R.id.recyclerview_horizontal);
    }

    @Override
    public void onResume() {
        MyProgressBarDialogTools.hide();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void setOnclik() {
        scanning.setOnClickListener(this);
        personalCenter.setOnClickListener(this);
        browsingRecord.setOnClickListener(this);
        search.setOnClickListener(this);
        my_gridview.setOnItemClickListener(this);
        medical_examination_img.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.search:
                intent = new Intent(getActivity(), Popular_search.class);
                intent.putExtra("mode", "1");//方式 1 表示从首页title-img 进入到热门搜索页 2 表示从搜索也进入到搜索页
                break;
            case R.id.personalCenter:
                String Str_Uid = SharedPreferencesTools.getUidToLoginClose(getActivity());
                if (!TextUtils.isEmpty(Str_Uid)) {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.ToFragment(4);
                    mainActivity.refresh();
                }
                break;
            case R.id.browsingRecord:
                intent = new Intent(getActivity(), Recent_Browse_File.class);
                intent.putExtra("type","home");
                break;
            case R.id.scanning:                                                     //打开默认二维码扫描界面
//                Intent intent1 = new Intent(getActivity(), CaptureActivity.class);
//                startActivityForResult(intent1, REQUEST_CODE);
                Intent intent1 = new Intent(getActivity(),ScanActivity.class);     //二维码和AR扫描界面
                intent1.putExtra("defaultType",0);
                startActivity(intent1);
                break;
            case R.id.medical_examination_img_x:                                  //打开默认二维码扫描界面
                try {
                    if (medical_examination_JSON.getString("urlflg").equals("0")) {
                        intent = new Intent(getActivity(), Hospital_training_entrance.class);
                        getContext().startActivity(intent);
                    } else {
                        intent = new Intent(getContext(), ActivityWebActivity.class);
                        intent.putExtra("title", medical_examination_JSON.getString("activitytitle"));
                        intent.putExtra("aid", medical_examination_JSON.getString("activityurl"));
                        getContext().startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    /**
     * name：跳转至视频播放页
     * author：Larry
     * data：2017/5/25 11:34
     */
    private void ToVideoFourActivity(String aid) {
        final String uid = SharedPreferencesTools.getUidToLoginClose(getActivity());
        if (uid == null || ("").equals(uid)) {
            return;
        }
        Intent intent = new Intent(getActivity(), VideoFive.class);
        intent.putExtra("aid", aid);
        startActivity(intent);
    }

    public void getUrlRulest() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.home_page);
                    if (SharedPreferencesTools.getUidONnull(getContext()).length() > 0) {
                        obj.put("uid", SharedPreferencesTools.getUidONnull(getContext()));
                    }
                    obj.put("version", getVersionCode(getContext()) + "");
                    String result = HttpClientUtils.sendPost(getActivity(), URLConfig.CCMTVAPP1, obj.toString());
                    /*LogUtil.e("首页数据", result);*/

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

    /**
     * 获取当前本地apk的版本
     *
     * @param mContext
     * @return
     */
    public static int getVersionCode(Context mContext) {
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public void scan_valid() {
        final String uid = SharedPreferencesTools.getUidToLoginClose(getActivity());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    token = token.substring(token.indexOf("token=") + 6, token.length());
                    obj.put("action", URLConfig.scan_valid);
                    obj.put("token", token);
                    obj.put("uid", uid);
                    String result = HttpClientUtils.sendPost(getActivity(), URLConfig.QR_CCMTVAPP, obj.toString());

                    Message message = new Message();
                    message.what = 4;
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

    /**
     * name:使用xutils 加载图片 author:Tom 2016-1-7下午1:28:03
     *
     * @param img  图片控件
     * @param path 图片网络地址
     */
    public void loadImg(ImageView img, String path, String aid) {
        if (!aid.equals(img.getTag())) {
            XUtilsImageLoader xUtilsImageLoader = new XUtilsImageLoader(getActivity());
            xUtilsImageLoader.display(img, FirstLetter.getSpells(path));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    token = result;
                    scan_valid();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(getActivity(), "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = null;
        switch (iconArrs.get(position).getTitle()) {
            case "手术演示":
                intent = new Intent(getActivity(), CustomActivity.class);
                intent.putExtra("type","home");
                intent.putExtra("video_class", "手术演示");
                intent.putExtra("mode", "5");
                break;
            case "病例讨论":
                intent = new Intent(getActivity(), CustomActivity.class);
                intent.putExtra("type","home");
                intent.putExtra("video_class", "病例讨论");
                intent.putExtra("mode", "5");
                break;
            case "超级访问":
                intent = new Intent(getActivity(), CustomActivity.class);
                intent.putExtra("type","home");
                intent.putExtra("video_class", "超级访问");
                intent.putExtra("mode", "5");
                break;
            case "百家讲坛":
                intent = new Intent(getActivity(), CustomActivity.class);
                intent.putExtra("type","home");
                intent.putExtra("video_class", "百家讲坛");
                intent.putExtra("mode", "5");
                break;
            case "医学直播间":
                intent = new Intent(getActivity(), MedicalLiveRoomActivity.class);  //跳转至医学直播间
//                intent = new Intent(getActivity(), Direct_broadcast_main.class);
                break;
            case "执业考试":
                intent = new Intent(getActivity(), Practicing_physician_examination.class);
                break;
            case "职业考试":
                intent = new Intent(getActivity(), Practicing_physician_examination.class);
                break;
            case "AR":
                //intent = new Intent(getActivity(), VideoAR.class);
                /*intent = new Intent(getActivity(), ScanActivity.class);
                intent.putExtra("defaultType",1);
                intent.putExtra("isClicked", true);*/
                //测试会议界面使用
                intent = new Intent(getActivity(), ConferenceMainActivity.class);
                break;
            case "领取积分":
                intent = new Intent(getActivity(), Hospital_training_entrance.class);
                break;
            case "空中拜访":
//                IsSignAgreement();
                //测试使用
//                intent = new Intent(getActivity(), Channel_selection.class);
                intent = new Intent(getActivity(), HomeFragment_new.class);
                break;
            default:
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    //是否签署协议
    private void IsSignAgreement() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.isSubscribe);
                    obj.put("uid", SharedPreferencesTools.getUid(getContext()));
//                    Log.e("看看协议数据", obj.toString());
                    String result = HttpClientUtils.sendPost(getContext(), URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看协议数据", result);

                    Message message = new Message();
                    message.what = 5;
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
