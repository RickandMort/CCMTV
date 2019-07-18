package com.linlic.ccmtv.yx.activity.base;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.baidu.mobstat.SendStrategyEnum;
import com.baidu.mobstat.StatService;
import com.bumptech.glide.Glide;
import com.coloros.mcssdk.PushManager;
import com.coloros.mcssdk.callback.PushAdapter;
import com.coloros.mcssdk.callback.PushCallback;
import com.coloros.mcssdk.mode.SubscribeResult;
import com.gensee.fastsdk.GenseeLive;
import com.huawei.android.hms.agent.HMSAgent;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.home.VideoService;
import com.linlic.ccmtv.yx.activity.home.util.ImageDownLoader;
import com.linlic.ccmtv.yx.activity.home.util.MyLogger;
import com.linlic.ccmtv.yx.activity.my.download.MyDownloadReveiver;
import com.linlic.ccmtv.yx.activity.my.updateapp.MyUpdateAppReceiver;
import com.linlic.ccmtv.yx.activity.upload.service.MyUploadCaseReceiver;
import com.linlic.ccmtv.yx.activity.upload.service.MyUploadVideoReveiver;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.receiver.DemoMessageReceiver;
import com.linlic.ccmtv.yx.utils.Carousel_figure;
import com.linlic.ccmtv.yx.utils.ForegroundCallbacks;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.meizu.cloud.pushsdk.util.MzSystemUtils;
import com.mob.MobSDK;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.smtt.sdk.QbSdk;
import com.tendcloud.tenddata.TCAgent;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.vivo.push.IPushActionListener;
import com.vivo.push.PushClient;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

//import com.gensee.fastsdk.GenseeLive;


/**
 * Created by shangsong on 14-9-23.
 */
public class LocalApplication extends MultiDexApplication {
    private boolean isBind = false;
    private List<ServiceListener> mListenerList;
    private VideoService videoService = null;
    private static LocalApplication instance;
    private final String LOG_TAG = this.getClass().getSimpleName();
    private static DemoHandler handler;
    /*******小米推送*******/
    public static final String APP_ID = "2882303761517484535";
    public static final String APP_KEY = "5901748428535";
    public static final String TAG = "com.linlic.ccmtv.yx";
    /*******小米推送*******/

    /**
     * 应用未启动, 个推 service已经被唤醒,保存在该时间段内离线消息(此时 GetuiSdkDemoActivity.tLogView == null)
     */
    public static StringBuilder payloadData = new StringBuilder();
    private static MainActivity sMainActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化分享mob
        MobSDK.init(this,"1471eac2ae044","96318e8b50ab3fa477cff9b79c39c9c3");
        //在app切到后台,activity被后台回收的场景下,需要主动初始化下
        GenseeLive.initConfiguration(getApplicationContext());
        instance = this;
        initOkGo();
        //初始化ImageLoader
        Carousel_figure Carousel_figure = new Carousel_figure(instance);
        Carousel_figure.configImageLoader();
        // TCAgent.LOG_ON=true;
        // App ID: 在TalkingData创建应用后，进入数据报表页中，在“系统设置”-“编辑应用”页面里查看App ID。
        // 渠道 ID: 是渠道标识符，可通过不同渠道单独追踪数据。
        TCAgent.init(this, "BD1524458CC5DA02B2005408B45E0221", "ccmtv");
        TCAgent.setReportUncaughtExceptions(true);
        registerActivityLifecycleCallbacks(new MyLifecycleHandler());
        //注册下载广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("download_progress");
        filter.addAction("download_failure");
        filter.addAction("download_success");
        filter.addAction("download_stop");
        filter.addAction("download");
        registerReceiver(new MyDownloadReveiver(), filter);

        //注册上传视频广播
        IntentFilter fil = new IntentFilter();
        fil.addAction("upload_video_progress");
        fil.addAction("upload_video_failure");
        fil.addAction("upload_video_success");
        fil.addAction("upload_video");
        registerReceiver(new MyUploadVideoReveiver(), fil);

        //注册上传病例广播
        IntentFilter fil_uploadCase = new IntentFilter();
        fil_uploadCase.addAction("upload_case_progress");
        fil_uploadCase.addAction("upload_case_failure");
        fil_uploadCase.addAction("upload_case_success");
        fil_uploadCase.addAction("upload_case");
        registerReceiver(new MyUploadCaseReceiver(), fil_uploadCase);

        //注册更新app 广播
        IntentFilter fil_updateApp = new IntentFilter();
        fil_updateApp.addAction("update_app_progress");
        fil_updateApp.addAction("update_app_failure");
        registerReceiver(new MyUpdateAppReceiver(), fil_updateApp);
        initBaidu();


        ZXingLibrary.initDisplayOpinion(this);

        x.Ext.init(this);
        x.Ext.setDebug(false); // 是否输出debug日志, 开启debug会影响性能.

        ForegroundCallbacks.init(this);
        if (handler == null) {
            handler = new DemoHandler(getAppContext());
        }

        /********华为start*********/
        //if (OSHelper.isEMUI()) {
        if(canHuaWeiPush()){
//        if (SystemUtil.getDeviceBrand().equals("Huawei")||SystemUtil.getDeviceBrand().equals("HONOR")) {
            HMSAgent.init(this);
            /********华为推送end*********/
        }else if (PushManager.isSupportPush(getApplicationContext())) {
            /********oppo推送start*********/
            PushManager.getInstance().register(getAppContext(), "6U10H9rf4gcooo48GS4Cs0wcw", "66D498593A37864897fafE1876C62C91", mPushCallback);
            /********oppo推送start*********/
        }else if (MzSystemUtils.isBrandMeizu(this)) {
            /********魅族推送start*********/
//            PushManager.register(this, APP_ID, APP_KEY);
            com.meizu.cloud.pushsdk.PushManager.register(this, "115684", "e6a71a1877c84adc9464282733e5b14f");
            /********魅族推送end*********/
        }     /********小米推送start*********/
            //初始化push推送服务
//        if (SystemUtil.getDeviceBrand().equals("Xiaomi")) {
        if(MiPushClient.getRegId(this) != null ){
            if(MiPushClient.getRegId(this) .length()>0){
                DemoMessageReceiver.ismiPush = true;
                MiPushClient.setAlias(this,MiPushClient.getRegId(this) ,null);
                SharedPreferencesTools.saveXMPush_id(this,MiPushClient.getRegId(this) );
                SharedPreferencesTools.savePush_Platform(this,"xiaomi");
            }
        }else {
            if (shouldInit()) {
                MiPushClient.registerPush(this, APP_ID, APP_KEY);
            }
        }

//        }
            //打开Log
            LoggerInterface newLogger = new LoggerInterface() {
                @Override
                public void setTag(String tag) {
                    // ignore
                }

                @Override
                public void log(String content, Throwable t) {
                    Log.d(TAG, content, t);
                }

                @Override
                public void log(String content) {
                    Log.d(TAG, content);
                }
            };
            Logger.setLogger(this, newLogger);
            /********小米推送end*********/
        /********vivo推送start*********/
        //PushClient.getInstance(getApplicationContext()).initialize();
        //bind();
        /********vivo推送end*********/
//搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        QbSdk.initX5Environment(getAppContext(), cb);

        ISNav.getInstance().init(new ImageLoader() {//给第三方图片选择器初始化一个图片加载器
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });
    }


    /**
     * 判断是否可以使用华为推送
     *
     * @return
     */
    public static Boolean canHuaWeiPush() {

        int emuiApiLevel = 0;
        try {
            Class cls = Class.forName("android.os.SystemProperties");
            Method method = cls.getDeclaredMethod("get", new Class[]{String.class});
            emuiApiLevel = Integer.parseInt((String) method.invoke(cls, new Object[]{"ro.build.hw_emui_api_level"}));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return emuiApiLevel > 5.0;

    }


    static{
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.white, android.R.color.darker_gray);//全局设置主题颜色
                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
    }


    //10-29 16:17:34.972 9538-9538/com.linlic.ccmtv.yx E/vivo: 打开push成功  15407989705881027533833
    public void bind() {
        PushClient.getInstance(getApplicationContext()).turnOnPush(new IPushActionListener() {

            @Override
            public void onStateChanged(int state) {
                if (state != 0) {
                    LogUtil.e("vivo", "打开push异常[" + state + "]");
                } else {
                    LogUtil.e("vivo", "打开push成功  " + PushClient.getInstance(getApplicationContext()).getRegId());

                }
            }
        });

    }

    /********小米推送start*********/
    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    /********小米推送end*********/
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
         *
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

    private void initOkGo() {
        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
        HttpHeaders headers = new HttpHeaders();
        headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文，不允许有特殊字符
        headers.put("commonHeaderKey2", "commonHeaderValue2");
        HttpParams params = new HttpParams();
        params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
        params.put("commonParamsKey2", "这里支持中文参数");
        //----------------------------------------------------------------------------------------//

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //log相关
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setColorLevel(Level.INFO);                               //log颜色级别，决定了log在控制台显示的颜色
        builder.addInterceptor(loggingInterceptor);                                 //添加OkGo默认debug日志
        //第三方的开源库，使用通知显示当前请求的log，不过在做文件下载的时候，这个库好像有问题，对文件判断不准确
        //builder.addInterceptor(new ChuckInterceptor(this));

        //超时时间设置，默认60秒
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);      //全局的读取超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);     //全局的写入超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);   //全局的连接超时时间

        //自动管理cookie（或者叫session的保持），以下几种任选其一就行
        //builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));            //使用sp保持cookie，如果cookie不过期，则一直有效
        builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));              //使用数据库保持cookie，如果cookie不过期，则一直有效
        //builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));            //使用内存保持cookie，app退出后，cookie消失

        //https相关设置，以下几种方案根据需要自己设置
        //方法一：信任所有证书,不安全有风险
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        //方法二：自定义信任规则，校验服务端证书
//        HttpsUtils.SSLParams sslParams2 = HttpsUtils.getSslSocketFactory(new SafeTrustManager());
        //方法三：使用预埋证书，校验服务端证书（自签名证书）
        //HttpsUtils.SSLParams sslParams3 = HttpsUtils.getSslSocketFactory(getAssets().open("srca.cer"));
        //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
        //HttpsUtils.SSLParams sslParams4 = HttpsUtils.getSslSocketFactory(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"));
//        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
//        builder.hostnameVerifier(new SafeHostnameVerifier());

        // 其他统一的配置
        // 详细说明看GitHub文档：https://github.com/jeasonlzy/
        OkGo.getInstance().init(this)                           //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置会使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3)                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
                .addCommonHeaders(headers)                      //全局公共头
                .addCommonParams(params);                       //全局公共参数
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        clearImageCache();
        MyLogger.d(LOG_TAG, "onLowMemory:release cache");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        clearImageCache();
        MyLogger.d(LOG_TAG, "onTerminate:release cache");
    }

    private void clearImageCache() {
        ImageDownLoader.getInstance().clear();
    }

    public void onActivityCreate() {
        mListenerList = new ArrayList<ServiceListener>();
        bindService();
    }

    private void bindService() {
        Intent intent = new Intent(this, VideoService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            isBind = true;
            videoService = ((VideoService.LocalBinder) iBinder).getService();

            for (ServiceListener listener : mListenerList) {
                listener.onServiceDisconnected(videoService);
            }
            mListenerList.clear();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            MyLogger.w(LOG_TAG, "onServiceDisconnected");
            isBind = false;
        }
    };

    public void getVideoService(ServiceListener listener) {
        if (videoService == null) {
            mListenerList.add(listener);
            bindService();
        } else {
            listener.onServiceDisconnected(videoService);
        }
    }

    private void unBindService() {
        if (isBind) {
            unbindService(mServiceConnection);
            isBind = false;
        }

    }

    public void clear() {
        unBindService();
        videoService = null;
    }

    public interface ServiceListener {
        void onServiceDisconnected(VideoService service);
    }

    public static Context getAppContext() {
        return instance;
    }


    public static void reInitPush(Context ctx) {
        MiPushClient.registerPush(ctx.getApplicationContext(), APP_ID, APP_KEY);
    }

    public static DemoHandler getHandler() {
        return handler;
    }


    public static void setMainActivity(MainActivity activity) {
        sMainActivity = activity;
    }


    public static final int KZBF_ARTICLELAUD_DELAY = 5000;

    public static class DemoHandler extends Handler {
        private Context context;

        public DemoHandler(Context context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            String s = (String) msg.obj;
           /* if (sMainActivity != null) {
                sMainActivity.refreshLogInfo();
            }*/
            if (!TextUtils.isEmpty(s)) {
//                Toast.makeText(context, s, Toast.LENGTH_LONG).show();
            }

          /*  Intent intent = new Intent();
            intent.setClass(context.getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);*/

            switch (msg.what) {
                case 0:

                    break;

                case 1:

                    break;

                case KZBF_ARTICLELAUD_DELAY://空中拜访点赞后延迟10s请求接口
                    final String aid = (String) msg.obj;
                    getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    JSONObject obj = new JSONObject();
                                    try {
                                        obj.put("act", URLConfig.updateLookLaudNumber);
                                        obj.put("aid", aid);
                                        HttpClientUtils.sendPost(getAppContext(), URLConfig.Skyvisit, obj.toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                            LogUtil.e("空中拜访点赞后延迟10s请求接口", "空中拜访点赞后延迟10s请求接口" + aid);
                        }
                    }, KZBF_ARTICLELAUD_DELAY);
                    break;
            }
        }
    }

    public static void sendMessage(Message msg) {
        handler.sendMessage(msg);
    }


    /***************oppo推送服务start**********************/
    /************************************************************************************
     * ***************************callbacks from mcs************************************
     ***********************************************************************************/
    private PushCallback mPushCallback = new PushAdapter() {
        @Override
        public void onRegister(int code, String s) {
            if (code == 0) {
                SharedPreferencesTools.savePush_id(getAppContext(), s);
                SharedPreferencesTools.savePush_Platform(getAppContext(),"oppo");
                LogUtil.e("注册成功", "registerId:" + s);
//                Toast.makeText(getAppContext(),"注册成功"+ "registerId:" + s, Toast.LENGTH_SHORT).show();
            } else {
                LogUtil.e("注册失败", "code=" + code + ",msg=" + s);
//                Toast.makeText(getAppContext(),"注册失败"+"code=" + code + ",msg=" + s, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onUnRegister(int code) {
            if (code == 0) {
                LogUtil.e("注销成功", "code=" + code);
            } else {
                LogUtil.e("注销失败", "code=" + code);
            }
        }

        @Override
        public void onGetAliases(int code, List<SubscribeResult> list) {
            if (code == 0) {
                LogUtil.e("获取别名成功", "code=" + code + ",msg=" + Arrays.toString(list.toArray()));
            } else {
                LogUtil.e("获取别名失败", "code=" + code);
            }
        }

        @Override
        public void onSetAliases(int code, List<SubscribeResult> list) {
            if (code == 0) {
                LogUtil.e("设置别名成功", "code=" + code + ",msg=" + Arrays.toString(list.toArray()));
            } else {
                LogUtil.e("设置别名失败", "code=" + code);
            }
        }

        @Override
        public void onUnsetAliases(int code, List<SubscribeResult> list) {
            if (code == 0) {
                LogUtil.e("取消别名成功", "code=" + code + ",msg=" + Arrays.toString(list.toArray()));
            } else {
                LogUtil.e("取消别名失败", "code=" + code);
            }
        }

        @Override
        public void onSetTags(int code, List<SubscribeResult> list) {
            if (code == 0) {
                LogUtil.e("设置标签成功", "code=" + code + ",msg=" + Arrays.toString(list.toArray()));
            } else {
                LogUtil.e("设置标签失败", "code=" + code);
            }
        }

        @Override
        public void onUnsetTags(int code, List<SubscribeResult> list) {
            if (code == 0) {
                LogUtil.e("取消标签成功", "code=" + code + ",msg=" + Arrays.toString(list.toArray()));
            } else {
                LogUtil.e("取消标签失败", "code=" + code);
            }
        }

        @Override
        public void onGetTags(int code, List<SubscribeResult> list) {
            if (code == 0) {
                LogUtil.e("获取标签成功", "code=" + code + ",msg=" + Arrays.toString(list.toArray()));
            } else {
                LogUtil.e("获取标签失败", "code=" + code);
            }
        }


        @Override
        public void onGetPushStatus(final int code, int status) {
            if (code == 0 && status == 0) {
                LogUtil.e("Push状态正常", "code=" + code + ",status=" + status);
            } else {
                LogUtil.e("Push状态错误", "code=" + code + ",status=" + status);
            }
        }

        @Override
        public void onGetNotificationStatus(final int code, final int status) {
            if (code == 0 && status == 0) {
                LogUtil.e("通知状态正常", "code=" + code + ",status=" + status);
            } else {
                LogUtil.e("通知状态错误", "code=" + code + ",status=" + status);
            }
        }

        @Override
        public void onSetPushTime(final int code, final String s) {
            LogUtil.e("SetPushTime", "code=" + code + ",result:" + s);
        }

    };
    /***************oppo推送服务end**********************/

}
