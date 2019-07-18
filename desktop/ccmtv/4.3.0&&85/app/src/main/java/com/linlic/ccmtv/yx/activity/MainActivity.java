package com.linlic.ccmtv.yx.activity;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.mobstat.SendStrategyEnum;
import com.baidu.mobstat.StatService;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.LocalApplication;
import com.linlic.ccmtv.yx.activity.base.SophixStubApplication;
import com.linlic.ccmtv.yx.activity.home.HomeFragment_new;
import com.linlic.ccmtv.yx.activity.home.VideoFive;
import com.linlic.ccmtv.yx.activity.login.LoginActivity;
import com.linlic.ccmtv.yx.activity.my.Integral;
import com.linlic.ccmtv.yx.activity.my.MyFragment2;
import com.linlic.ccmtv.yx.activity.my.learning_task.VideoSignActivity;
import com.linlic.ccmtv.yx.activity.step.service.StepService;
import com.linlic.ccmtv.yx.activity.subscribe.SubscribeFragment;
import com.linlic.ccmtv.yx.activity.upload.UploadFragment;
import com.linlic.ccmtv.yx.activity.vip.VipFragment_new;
import com.linlic.ccmtv.yx.activity.vip.Vip_Channel;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.floatWindow.service.FloatViewService;
import com.linlic.ccmtv.yx.floatWindow.widget.FloatLayout;
import com.linlic.ccmtv.yx.kzbf.utils.SkyVisitUtils;
import com.linlic.ccmtv.yx.util.Utils;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.NetUtil;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.SystemUtil;
import com.linlic.ccmtv.yx.widget.TipsDialog;
import com.lzy.okgo.db.UploadManager;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.OkUpload;
import com.lzy.okserver.upload.UploadTask;
import com.taobao.sophix.SophixManager;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class MainActivity extends FragmentActivity {
    private static final int REQUEST_EXTERNAL_STORAGE_PERMISSION = 0;
    private final String TAG = this.getClass().getSimpleName();

    private static final int REQUEST_PERMISSION_STORAGE = 0x01;
    public static int video_book = 0;
    public static FloatViewService mFloatViewService;
    //用户统计
    private String entertime, leavetime;
    public static String enterUrl = "http://www.ccmtv.cn";
    // 主页
    private HomeFragment_new home;
    // 科室
    private VipFragment_new vip;
    // 上传
    private UploadFragment upload;
    //订阅
    private SubscribeFragment subscribe;
    // 我的
    public static MyFragment2 my;
    private static Fragment[] fragments;
    // button点击的index  当前fragment的index
    private int index, currentTabIndex;
    private Button[] mTabs;
    private String unselectColor, fontColor;
    private LinearLayout code_tx_layout;
    //引导图数组
    private int[] guidePic = {R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3};
    //视频所需
    private LocalApplication mApplication;
    Context context;
    private Drawable drawable1, drawable2, drawable3, drawable4, drawable5, drawable6, drawable7, drawable8, drawable9, drawable10;
    private String buttomText1, buttomText2, buttomText3, buttomText4, buttomText5;
    private int MinimumWidth, MinimumHeight;
    private TipsDialog tipsDialog;
    public static int curr_num = -1;

    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };
    private int medalTag;//1为勋章模块开启   0为关


    private NetworkChangeReceiver networkChangeRecever;

    public class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connecttivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);//使用getSystemService得到ConnectivityManager实例
            NetworkInfo networkInfo = connecttivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                // 这里需要判断，虽然连接了，但是网络仍然不可访问
                if (!NetUtil.isNetworkOnline()) {
                    netWorkView.setVisibility(View.VISIBLE);
                } else {
                    Log.e("PRETTY_LOGGER", "onReceive() returned: " + "当前网络可以用");
                    netWorkView.setVisibility(View.GONE);
                }
            } else {
                netWorkView.setVisibility(View.VISIBLE);
            }
        }
    }

    private View netWorkView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 解决当程序crash，切换fragment无效的问题
        if (savedInstanceState != null) {
            savedInstanceState.putParcelable("android:support:fragments", null);
        }
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        netWorkView = findViewById(R.id.newtWork_view_placeHolder);
        netWorkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
            }
        });

        // 注册一个网络状态监听的广播
        //@author eric
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");//添加广播
        networkChangeRecever = new NetworkChangeReceiver();
        registerReceiver(networkChangeRecever, intentFilter);

        LocalApplication.setMainActivity(this);
        context = this;
        code_tx_layout = (LinearLayout) findViewById(R.id.code_tx_layout);
        getgpRole();
//        ButterKnife.bind(this);
        MinimumWidth = getResources().getDrawable(R.mipmap.menu_icon1).getMinimumWidth() - 10;
        MinimumHeight = getResources().getDrawable(R.mipmap.menu_icon1).getMinimumHeight() - 10;
        init();
        initBaidu();

        showNotificationSettingDialog(isNotificationEnabled(this));
        medalTag = getIntent().getIntExtra("medalTag", 0);

        home = new HomeFragment_new();
        vip = new VipFragment_new();
        upload = new UploadFragment();
        subscribe = new SubscribeFragment();
        my = new MyFragment2();

        Bundle bundle = new Bundle();
        bundle.putInt("medalTag", medalTag);
        my.setArguments(bundle);

        fragments = new Fragment[]{home, upload, vip, subscribe, my};
        mTabs = new Button[5];
        mTabs[0] = (Button) findViewById(R.id.bot_1);
        mTabs[1] = (Button) findViewById(R.id.bot_2);
        mTabs[2] = (Button) findViewById(R.id.bot_3);
        mTabs[3] = (Button) findViewById(R.id.bot_4);
        mTabs[4] = (Button) findViewById(R.id.bot_5);

        // 把第一个tab设为选中状态
        mTabs[0].setSelected(true);
        // image.setImageDrawable(drawable);
        try {
            initData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getSupportFragmentManager().beginTransaction().add(R.id.s_fragments, home)
                .add(R.id.s_fragments, upload).add(R.id.s_fragments, vip).add(R.id.s_fragments, subscribe).add(R.id.s_fragments, my)
                .commit();
        //进入APP后检测一下是否有正在下载的视频，如果有就继续当前任务下载
        continueDownload();
        //'switch'=>1,//1为引导页开启   0为关闭引导页
        if (SharedPreferencesTools.getIsShowGuidePic(context).equals("1")) {
            code_tx_visi();
        }
        naozhong();
        startFloatService(context);//开启音频播放服务
        checkSDCardPermission();
        getPermissions();


 /*热修复 阿里 sophix   暂时不能放开 无预算 20190619 tom
        initPermissions();*/

    }

    public void getgpRole() {
        if (SharedPreferencesTools.getUidONnull(context).length() > 0) {
            if (SharedPreferencesTools.getIsdocexam(context)) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject();
                            obj.put("act", URLConfig.gpRole);
                            obj.put("uid", SharedPreferencesTools.getUid(context));
                            String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());

                            try {
                                final JSONObject result1 = new JSONObject(result);
                                if (result1.getInt("code") == 200) {
                                    JSONObject dataJson = result1.getJSONObject("data");
                                    if (dataJson.getInt("status") == 1) { //成功
                                        JSONObject jsonObject = dataJson.getJSONObject("data");
                                        //第一步保存身份类型  0规培生  1医院正式员工 2医考
                                        SharedPreferencesTools.saveGp_type(context, jsonObject.getString("gp_type"));
                                        //第二步保存用户身份
                                        SharedPreferencesTools.saveRoleList(context, jsonObject.getString("roleList"));
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                new Thread(runnable).start();
            }
        }

    }

    /**
     * 检查SD卡权限
     */
    protected void checkSDCardPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
        }
        /**  .permission(
         Permission.READ_EXTERNAL_STORAGE
         )
         * 动态获取权限，Android 6.0 新特性，一些保护权限，除了要在AndroidManifest中声明权限，还要使用如下代码动态获取
         */
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }
    }

    /*
      AndPermission处理权限问题，api版本22以下不会回调，在此记录一下，待升级后优化
     */
    public void getPermissions() {
        AndPermission.with(this)
                .runtime()

                .permission(
                Permission.Group.CAMERA,
                Permission.Group.LOCATION,
                Permission.Group.STORAGE
        )
                //.rationale((Rationale<List<String>>) this)//添加拒绝权限回调
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        //授权成功的操作
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        /**
                         * 当用户没有允许该权限时，回调该方法
                         */
                        //Toast.makeText(MainActivity.this, "请授权相应权限，以免影响部分功能无法使用", Toast.LENGTH_SHORT).show();
                        /**
                         * 判断用户是否点击了禁止后不再询问，AndPermission.hasAlwaysDeniedPermission(MainActivity.this, data)
                         */
//                        if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, data)) {
//                            //true，弹窗再次向用户索取权限
//                            //showSettingDialog(MainActivity.this, data);
//                            Toast.makeText(MainActivity.this, "禁止干嘛，我要权限", Toast.LENGTH_SHORT).show();
//                        }
                    }
                }).start();
    }

    public void naozhong() {

      /*  if(!notificationListenerEnable()){
            gotoNotificationAccessSetting(context);
        }*/
        Intent intent1 = new Intent(context, StepService.class);
        startService(intent1);
        try {
            Intent intent = new Intent(this, FloatViewService.class);
            startService(intent);
            bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void code_tx_visi() {
        if (SharedPreferencesTools.getcodezd(this).equals("3")) {
            code_tx_layout.setVisibility(View.GONE);
        } else {
            code_tx_layout.setVisibility(View.VISIBLE);
        }
    }

    int nowPosition = 1;

    /**
     * name：
     * author：
     * data：2016/6/1 10:00
     */
    public void codezd(View view) {

        if (nowPosition == guidePic.length) {
            SharedPreferencesTools.savecodezd(this, "3");//暂时处理方式为把原来为1的值变为2,后续如有更好处理方式再更改
            code_tx_layout.setVisibility(View.GONE);
        } else {
            code_tx_layout.setBackgroundResource(guidePic[nowPosition]);
            nowPosition++;
        }

    }

    private void initBaidu() {
        //初始化百度统计
        // 测试时，可以使用1秒钟session过期，这样不断的间隔1S启动退出会产生大量日志。
        StatService.setSessionTimeOut(30);
        // setOn也可以在AndroidManifest.xml文件中填写，BaiduMobAd_EXCEPTION_LOG，打开崩溃错误收集，默认是关闭的
        StatService.setOn(this, StatService.EXCEPTION_LOG);
        /*
         * 设置启动时日志发送延时的秒数<br/> 单位为秒，大小为0s到30s之间<br/> 注：请在StatService.setSendLogStrategy之前调用，否则设置不起作用
         *
         * 如果设置的是发送策略是启动时发送，那么这个参数就会在发送前检查您设置的这个参数，表示延迟多少S发送。<br/> 这个参数的设置暂时只支持代码加入，
         * 在您的首个启动的Activity中的onCreate函数中使用就可以。<br/>
         */
        StatService.setLogSenderDelayed(0);
        /*
         * 用于设置日志发送策略<br /> 嵌入位置：Activity的onCreate()函数中 <br />
         * 调用方式：StatService.setSendLogStrategy(this,SendStrategyEnum. SET_TIME_INTERVAL, 1, false); 第二个参数可选：
         * SendStrategyEnum.APP_START SendStrategyEnum.ONCE_A_DAY SendStrategyEnum.SET_TIME_INTERVAL 第三个参数：
         * 这个参数在第二个参数选择SendStrategyEnum.SET_TIME_INTERVAL时生效、 取值。为1-24之间的整数,即1<=rtime_interval<=24，以小时为单位 第四个参数：
         * 表示是否仅支持wifi下日志发送，若为true，表示仅在wifi环境下发送日志；若为false，表示可以在任何联网环境下发送日志
         */
        StatService.setSendLogStrategy(this, SendStrategyEnum.SET_TIME_INTERVAL, 1, false);
        // 调试百度统计SDK的Log开关，可以在Eclipse中看到sdk打印的日志，发布时去除调用，或者设置为false
        StatService.setDebugOn(true);
        String sdkVersion = StatService.getSdkVersion();

    }


    private void initData() throws JSONException {
        String AppConfig = SharedPreferencesTools.getAppConfig(context);

        final JSONObject obj = new JSONObject(new JSONObject(AppConfig).getString("newConfigData"));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    drawable1 = new BitmapDrawable(Utils.returnBitmap(obj.getJSONObject("indexUrl").getString("img")));
                    drawable2 = new BitmapDrawable(Utils.returnBitmap(obj.getJSONObject("indexUrl").getString("imghover")));
                    buttomText1 = obj.getJSONObject("indexUrl").getString("word");
                    drawable3 = new BitmapDrawable(Utils.returnBitmap(obj.getJSONObject("uploadUrl").getString("img")));
                    drawable4 = new BitmapDrawable(Utils.returnBitmap(obj.getJSONObject("uploadUrl").getString("imghover")));
                    buttomText2 = obj.getJSONObject("uploadUrl").getString("word");
                    drawable5 = new BitmapDrawable(Utils.returnBitmap(obj.getJSONObject("vipUrl").getString("img")));
                    drawable6 = new BitmapDrawable(Utils.returnBitmap(obj.getJSONObject("vipUrl").getString("imghover")));
                    buttomText3 = obj.getJSONObject("vipUrl").getString("word");
                    drawable7 = new BitmapDrawable(Utils.returnBitmap(obj.getJSONObject("subscribeUrl").getString("img")));
                    drawable8 = new BitmapDrawable(Utils.returnBitmap(obj.getJSONObject("subscribeUrl").getString("imghover")));
                    buttomText4 = obj.getJSONObject("subscribeUrl").getString("word");
                    drawable9 = new BitmapDrawable(Utils.returnBitmap(obj.getJSONObject("mineUrl").getString("img")));
                    drawable10 = new BitmapDrawable(Utils.returnBitmap(obj.getJSONObject("mineUrl").getString("imghover")));
                    buttomText5 = obj.getJSONObject("mineUrl").getString("word");
                    fontColor = obj.getString("fontColor");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setButtomText();
                        setButtomImg();
                    }
                });
            }
        }).start();

    }

    private void setButtomImg() {
        int selectColor = Color.parseColor(fontColor);               //Tab字体选中之后的字体颜色
        int unselectColor = Color.parseColor("#4A5578");            //Tab字体未选中的字体颜色
        StateListDrawable drawable11 = new StateListDrawable();
        //Non focused states
        drawable11.addState(new int[]{-android.R.attr.state_focused, -android.R.attr.state_selected},
                drawable1);
        drawable11.addState(new int[]{-android.R.attr.state_focused, android.R.attr.state_selected},
                drawable2);
        //Focused states
        drawable11.addState(new int[]{android.R.attr.state_focused, -android.R.attr.state_selected},
                drawable2);
        //Pressed
        drawable11.addState(new int[]{android.R.attr.state_selected},
                drawable2);
        drawable11.setBounds(0, 0, MinimumWidth, MinimumHeight);
        mTabs[0].setCompoundDrawables(null, drawable11, null, null);
        mTabs[0].setSelected(true);
        mTabs[0].setTextColor(createColorStateList(unselectColor, selectColor));
        StateListDrawable drawable12 = new StateListDrawable();
        //Non focused states
        drawable12.addState(new int[]{-android.R.attr.state_focused, -android.R.attr.state_selected},
                drawable3);
        drawable12.addState(new int[]{-android.R.attr.state_focused, android.R.attr.state_selected},
                drawable4);
        //Focused states
        drawable12.addState(new int[]{android.R.attr.state_focused, -android.R.attr.state_selected},
                drawable4);
        //Pressed
        drawable12.addState(new int[]{android.R.attr.state_selected},
                drawable4);
        drawable12.setBounds(0, 0, MinimumWidth, MinimumHeight);
        mTabs[1].setCompoundDrawables(null, drawable12, null, null);
        StateListDrawable drawable13 = new StateListDrawable();
        //Non focused states
        drawable13.addState(new int[]{-android.R.attr.state_focused, -android.R.attr.state_selected},
                drawable5);
        drawable13.addState(new int[]{-android.R.attr.state_focused, android.R.attr.state_selected},
                drawable6);
        //Focused states
        drawable13.addState(new int[]{android.R.attr.state_focused, -android.R.attr.state_selected},
                drawable6);
        //Pressed
        drawable13.addState(new int[]{android.R.attr.state_selected},
                drawable6);
        drawable13.setBounds(0, 0, MinimumWidth, MinimumHeight);
        mTabs[2].setCompoundDrawables(null, drawable13, null, null);
        StateListDrawable drawable14 = new StateListDrawable();
        //Non focused states
        drawable14.addState(new int[]{-android.R.attr.state_focused, -android.R.attr.state_selected},
                drawable7);
        drawable14.addState(new int[]{-android.R.attr.state_focused, android.R.attr.state_selected},
                drawable8);
        //Focused states
        drawable14.addState(new int[]{android.R.attr.state_focused, -android.R.attr.state_selected},
                drawable8);
        //Pressed
        drawable14.addState(new int[]{android.R.attr.state_selected},
                drawable8);
        drawable14.setBounds(0, 0, MinimumWidth, MinimumHeight);
        mTabs[3].setCompoundDrawables(null, drawable14, null, null);
        StateListDrawable drawable15 = new StateListDrawable();
        //Non focused states
        drawable15.addState(new int[]{-android.R.attr.state_focused, -android.R.attr.state_selected},
                drawable9);
        drawable15.addState(new int[]{-android.R.attr.state_focused, android.R.attr.state_selected},
                drawable10);
        //Focused states
        drawable15.addState(new int[]{android.R.attr.state_focused, -android.R.attr.state_selected},
                drawable10);
        //Pressed
        drawable15.addState(new int[]{android.R.attr.state_selected},
                drawable10);
        drawable15.setBounds(0, 0, MinimumWidth, MinimumHeight);
        mTabs[4].setCompoundDrawables(null, drawable15, null, null);
    }

    private void setButtomText() {
        mTabs[0].setText(buttomText1);
        mTabs[1].setText(buttomText2);
        mTabs[2].setText(buttomText3);
        mTabs[3].setText(buttomText4);
        mTabs[4].setText(buttomText5);
    }


    /**
     * 对TextView设置不同状态时其文字颜色。
     * <p/>
     * http://blog.csdn.net/jdsjlzx/article/details/7645004
     */
    private ColorStateList createColorStateList(int normal, int focused) {
        int[] colors = new int[]{normal, focused};
        int[][] states = new int[2][];
        states[0] = new int[]{-android.R.attr.state_selected};
        states[1] = new int[]{android.R.attr.state_selected};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }


    /**
     * name：进入APP后检测一下是否有正在下载的视频，如果有就继续当前任务下载
     * author：MrSong
     * data：2016/7/15 13:46
     * <p>
     * 2018  修改为暂停所有上传下载
     */
    private void continueDownload() {
        /*List<DbDownloadVideo> list = MyDbUtils.findAll(context);
        if (list != null) {
            if (list.size() != 0) {
                for (DbDownloadVideo video : list) {
                    if (video.getState().equals(DownloadService.download_progress + "")) {
                        //如果有正在下载的任务（意外停止进程，显示为正在下载，实际已经停止下载），更改他的状态为等待下载，然后发送下载广播
                        MyDbUtils.updateDownState(context, video.getVideoId(), DownloadService.download_wait + "");
                    }
                }
                Intent intent = new Intent();
                intent.setAction("download");
                context.sendBroadcast(intent);
            }
        }*/

        /*List<DownloadTask> downloadTasks= OkDownload.restore(DownloadManager.getInstance().getDownloading());
        for (DownloadTask task : downloadTasks) {
            int status=task.progress.status;
            if (status != 3 && status != 5){
                task.pause();
            }
        }*/

        OkDownload.getInstance().pauseAll();
        //OkUpload.getInstance().pauseAll();
        List<UploadTask<?>> uploadTasks = OkUpload.restore(UploadManager.getInstance().getUploading());
        for (UploadTask<?> task : uploadTasks) {
            int status = task.progress.status;
            if (status != 3 && status != 5) {
                task.progress.status = 3;
                task.progress.currentSize = 0;
                task.progress.fraction = 0;
                task.pause();
            }
        }
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (("upload").equals(intent.getStringExtra("type"))) {
            ToFragment(1);
            upload.initData();
        } else if (("phone_rz").equals(intent.getStringExtra("type"))) {
            ToFragment(4);
            my.initdata();
        } else if (("register").equals(intent.getStringExtra("type"))) {
            ToFragment(4);
            my.initdata();
        } else if (("changepass").equals(intent.getStringExtra("type"))) {
            ToFragment(0);
        } else if (("quit").equals(intent.getStringExtra("type"))) {
            ToFragment(0);
            home.initdata();
        } else if (("tohome").equals(intent.getStringExtra("type"))) {
            ToFragment(0);
            home.initdata();
        }
    }

    //视频所需
    private void init() {
        mApplication = (LocalApplication) getApplication();
        mApplication.onActivityCreate();
    }

    /**
     * name：底部button点击事件
     * <p/>
     * author: Mr.song
     * 时间：2016-1-29 下午2:32:37
     *
     * @param view
     */
    public void onTabClicked(View view) {
        switch (view.getId()) {
            case R.id.bot_1:
                index = 0;
                home.initdata();
                break;
            case R.id.bot_2:
                index = 1;
                upload.initData();
                break;
            case R.id.bot_3:
                index = 2;
//                vip.initTitle();
                vip.initDataImgAndTop();
                break;
            case R.id.bot_4:
                index = 3;
                subscribe.initData();
                break;
            case R.id.bot_5:
                //保存退出的日期
                leavetime = SkyVisitUtils.getCurrentTime();
                //保存日期到服务器
                SkyVisitUtils.OnlineStatistical(context, enterUrl, entertime, leavetime);
                index = 4;
                my.initdata();
                break;
        }
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager()
                    .beginTransaction();
            trx.hide(fragments[0]);
            trx.hide(fragments[1]);
            trx.hide(fragments[2]);
            trx.hide(fragments[3]);
            trx.hide(fragments[4]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.s_fragments, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        mTabs[currentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
        mTabs[index].setSelected(true);
        currentTabIndex = index;
    }

    public static void refresh() {
        my.initdata();
    }

    /**
     * name：跳转至fragment
     * author：Larry
     * data：2016/4/5 18:07
     */
    public void ToFragment(int index) {
        FragmentTransaction trx = getSupportFragmentManager()
                .beginTransaction();
        if (currentTabIndex != index) {
            trx.hide(fragments[0]);
            trx.hide(fragments[1]);
            trx.hide(fragments[2]);
            trx.hide(fragments[3]);
            trx.hide(fragments[4]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.s_fragments, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        mTabs[currentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
        mTabs[index].setSelected(true);
        currentTabIndex = index;
    }


    //下载随访家患者伴
    public void load_patient(View v) {
        Uri uri = Uri.parse(URLConfig.SuiFangJia_Patient);
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(it);
    }

    //下载随访家医生版
    public void load_doctor(View v) {
        Uri uri = Uri.parse(URLConfig.SuiFangJia_Doctor);
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(it);
    }

    // 跳转至积分页面
    public void toIntegral(View view) {
        Intent intent = new Intent(MainActivity.this, Integral.class);
        startActivity(intent);
    }

    //订阅未登录--点击登录
    public void btnClick(View view) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.putExtra("source", "up");
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        //保存进入的日期
        entertime = SkyVisitUtils.getCurrentTime();
        /**
         * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
         */
        StatService.onResume(this);
       /* Intent intent = getIntent();
        Log.e("是否扫描二维码进入",intent.getStringExtra("video_book"));
        Log.e("是否扫描二维码进入参数",intent.getStringExtra("name")+"   "+intent.getStringExtra("pwd"));
        if(!intent.getStringExtra("video_book").equals("0")){
            //调整到电子书页面
            startActivity(new Intent(context, Video_book.class));
        }*/

        updateUserVersion();

            if(curr_num > -1){
                ToFragment( curr_num );
                refresh();
                curr_num = -1;
            }


        super.onResume();
    }




    public void onPause() {
        //保存退出的日期
        leavetime = SkyVisitUtils.getCurrentTime();
        //保存日期到服务器
        SkyVisitUtils.OnlineStatistical(context, enterUrl, entertime, leavetime);
        /**
         * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
         */
        StatService.onPause(this);
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            stopFloatingView(context);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    System.exit(0);
                }
            }, 1000);

        }
    }


    @Override
    protected void onDestroy() {
        destroy();
        if (tipsDialog != null && tipsDialog.isShowing()) tipsDialog.dismiss();
        LocalApplication.setMainActivity(null);
        super.onDestroy();
        if (SophixStubApplication.isRelaunch) {
            Log.i(TAG, "如果是冷启动，则杀死App进程，从而加载补丁:" );
            SophixStubApplication.isRelaunch = false;
            SophixManager.getInstance().killProcessSafely();
        }
    }


    /******************************************悬浮窗start**********************************************/
    public void startFloatService(Context context) {
        try {
            Intent intent = new Intent(context, FloatViewService.class);
//            startService(intent);
            bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示悬浮图标
     */
    public static void showFloatingView(Context context) {
        if (mFloatViewService != null) {
            FloatLayout.isshwo = true;
            mFloatViewService.showFloat();
            if (VideoFive.mVideoView != null && 1 == 1) {
                VideoFive.mVideoView.pause();
            }
            if (VideoFive.video_view2 != null && 1 == 1) {
                VideoFive.video_view2.pause();
            }
            if (VideoSignActivity.mVideoView != null && 1 == 1) {
                VideoSignActivity.mVideoView.pause();
            }
            //启动服务，播放音乐
            Intent intent = new Intent(context, FloatViewService.class);
            intent.putExtra("type", FloatLayout.PLAT_MUSIC);
            context.startService(intent);
        }
    }

    /**
     * 隐藏悬浮图标
     */
    public void hideFloatingView() {
        if (mFloatViewService != null) {
            mFloatViewService.hideFloat();
        }
    }

    /**
     * 隐藏悬浮图标
     */
    public static void stopFloatingView(Context context) {
        if (mFloatViewService != null) {
            if (mFloatViewService.isPlay) {
                //启动服务，播放音乐
                Intent intent = new Intent(context, FloatViewService.class);
                intent.putExtra("type", FloatLayout.STOP_MUSIC);
                context.startService(intent);
                FloatLayout.isshwo = false;
            }

            mFloatViewService.hideFloat();
        }
    }

    /**
     * 释放PJSDK数据
     */
    public void destroy() {
        try {
            stopService(new Intent(this, FloatViewService.class));
            unbindService(mServiceConnection);
        } catch (Exception e) {
        }
    }

    /**
     * 连接到Service
     */
    public final static ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mFloatViewService = ((FloatViewService.FloatViewServiceBinder) iBinder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mFloatViewService = null;
        }
    };

    /******************************************悬浮窗end**********************************************/

    public void clickVideo(View view) {
        final String uid = SharedPreferencesTools.getUidToLoginClose(context);
        if (uid == null || ("").equals(uid)) {
            return;
        }
        Intent intent = new Intent(context, VideoFive.class);
        intent.putExtra("aid", view.getTag().toString());
        context.startActivity(intent);
    }

    public void clickmoreVideos(View view) {
        final String uid = SharedPreferencesTools.getUidToLoginClose(context);
        if (uid == null || ("").equals(uid)) {
            return;
        }
        Intent intent = new Intent(context, Vip_Channel.class);
        intent.putExtra("type", view.getTag().toString());
        intent.putExtra("position","-1");
        context.startActivity(intent);
    }

    /*
      推送信息传给后台
     */
    public void updateUserVersion() {
        List<String> alias_list = MiPushClient.getAllAlias(context);
        for (int i = 0; i < alias_list.size(); i++) {
            LogUtil.e("小米标签", alias_list.get(i));
        }
        String a1=SharedPreferencesTools.getUidONnull(context).trim();
        String a2=SharedPreferencesTools.getPush_id(context);
        String a3=SharedPreferencesTools.getXMPush_id(context);
        LogUtil.e("我的数据"," aa: "+a1+" bb: "+a2+" cc: "+a3);

        if (SharedPreferencesTools.getUidONnull(context).trim().length() > 0 && (SharedPreferencesTools.getPush_id(context).trim().length() > 0 ||SharedPreferencesTools.getXMPush_id(context).trim().length() > 0)) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("act", URLConfig.updateUserVersion);
                        obj.put("uid", SharedPreferencesTools.getUid(context));
                        obj.put("version",SharedPreferencesTools.getPush_Platform(context));
                        obj.put("cid", SharedPreferencesTools.getPush_id(context).length()>0?SharedPreferencesTools.getPush_id(context):SharedPreferencesTools.getXMPush_id(context));
                        obj.put("attach_cid", SharedPreferencesTools.getXMPush_id(context));
                        obj.put("model",SystemUtil.getDeviceBrand().toLowerCase());
                        String result = HttpClientUtils.sendPostToGP(context,
                                URLConfig.CCMTVAPP_PUTHApi, obj.toString());
                        LogUtil.e("推送接口上行", obj.toString());
                        LogUtil.e("推送接口", result);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            };
            new Thread(runnable).start();
        }

    }

    public void showNotificationSettingDialog(boolean enabled) {
        if (!enabled) {
            if (tipsDialog == null) {
                tipsDialog = new TipsDialog(this, "部分功能需要通知权限才能正常使用").setClicklistener(new TipsDialog.ClickListenerInterface() {

                    @Override
                    public void doConfirm(Context context) {
                        gotoNotificationSetting();
                    }

                    @Override
                    public void doCancel(Context context) {

                    }
                }).setCancleButton("取消")
                        .setConfirmButton("去设置");
                tipsDialog.setCanceledOnTouchOutside(false);
            }
            tipsDialog.show();
        }
    }

    /**
     * 跳到通知栏设置界面
     */
    private void gotoNotificationSetting(){
        Intent localIntent = new Intent();
        //直接跳转到应用通知设置的代码：
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            localIntent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
            localIntent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            localIntent.putExtra(Settings.EXTRA_CHANNEL_ID, getApplicationInfo().uid);
            //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
            localIntent.putExtra("app_package", getPackageName());
            localIntent.putExtra("app_uid", getApplicationInfo().uid);
        } else if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            localIntent.addCategory(Intent.CATEGORY_DEFAULT);
            localIntent.setData(Uri.parse("package:" + getPackageName()));
        } else {
            //4.4以下没有从app跳转到应用通知设置页面的Action，可考虑跳转到应用详情页面,
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= 9) {
                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                localIntent.setData(Uri.fromParts("package", getPackageName(), null));
            } else if (Build.VERSION.SDK_INT <= 8) {
                localIntent.setAction(Intent.ACTION_VIEW);
                localIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
                localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
            }
        }
        startActivity(localIntent);
    }

    /**
     * 获取通知权限
     *
     * @param context
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private boolean isNotificationEnabled(Context context) {

        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }




    /**
     * 配置Android 6.0 以上额外的权限
     */
    private void initPermissions() {
        //配置微信登录和6.0权限
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,//读取储存权限
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,//写入储存权限
            };
            if (checkPermissionAllGranted(mPermissionList)) {
                /*查询是否有新补丁需要载入*/
                SophixManager.getInstance().queryAndLoadNewPatch();
            } else {

                ActivityCompat.requestPermissions(this, mPermissionList, REQUEST_EXTERNAL_STORAGE_PERMISSION);
            }
        } else {
            /*查询是否有新补丁需要载入*/
            SophixManager.getInstance().queryAndLoadNewPatch();
        }

    }

    private void test() {
        String versionName = null;
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Toast.makeText(getBaseContext(), "当前版本" + versionName, Toast.LENGTH_SHORT).show();
    }

    /**
     * 检查是否拥有指定的所有权限
     */
    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "成功获得权限");
                     /*查询是否有新补丁需要载入*/
                    SophixManager.getInstance().queryAndLoadNewPatch();
                } else {
                    new AlertDialog.Builder(this)
                            .setMessage("未获得权限，无法获得补丁升级功能")
                            .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.setData(Uri.parse("package:" + getPackageName()));
                                    startActivity(intent);
                                }
                            }).setNegativeButton("取消", null).show();
                }
            default:
                break;
        }
    }

}