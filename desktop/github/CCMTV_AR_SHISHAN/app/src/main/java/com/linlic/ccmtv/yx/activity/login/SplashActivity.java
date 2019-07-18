package com.linlic.ccmtv.yx.activity.login;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.android.hms.agent.common.handler.ConnectHandler;
import com.huawei.android.hms.agent.push.handler.GetPushStateHandler;
import com.huawei.android.hms.agent.push.handler.GetTokenHandler;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.login.service.DownloadARImgService;
import com.linlic.ccmtv.yx.activity.medal.MedalListActivity;
import com.linlic.ccmtv.yx.activity.my.book.Video_book;
import com.linlic.ccmtv.yx.activity.my.dialog.MustUpdateCustomDialog2;
import com.linlic.ccmtv.yx.activity.my.dialog.UpdateCustomDialog;
import com.linlic.ccmtv.yx.activity.my.newDownload.DownloadFinishActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.enums.PermissionEnum;
import com.linlic.ccmtv.yx.utils.DownLoadImageUtil;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.SystemUtil;
import com.linlic.ccmtv.yx.widget.PermissionTipsDialog;
import com.linlic.ccmtv.yx.widget.SplashJumpView;
import com.vivo.push.IPushActionListener;
import com.vivo.push.PushClient;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * name：启动页
 * <p/>
 * author: Mr.song 时间：2016-2-18 上午11:17:01
 *
 * @author Administrator
 */
public class SplashActivity extends BaseActivity {

    private static final int REQUEST_CODE_FLOATVIEW_PERMISSION = 123;
    private static final int REQUEST_CODE_DANGEROUS_PERMISSION = 124;

    private int sleepTime = 5;//页面停留时间，单位：秒
    private ImageView splash_img;
    private String title = "";
    private String webUrl = "";
    private int isstartup = 0;

    final Timer timer = new Timer();
    JSONObject obj_adv = null;
    Dialog dialog;
    // Button btn_skip;
    SplashJumpView btn_skip;
    private int medalTag;//1为勋章模块开启   0为关
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    final String json = (String) msg.obj;
                    try {
                        JSONObject allDataObject = new JSONObject(json);
                        JSONObject obj = new JSONObject(new JSONObject(json).getString("data"));
                        obj_adv = new JSONObject(json).getJSONObject("advertisementData");
                        final JSONObject startPageData = new JSONObject(new JSONObject(json).getString("startPageData"));
                        title = startPageData.getString("title");
                        webUrl = startPageData.getString("advertisementurl");
                        isstartup = startPageData.getInt("isstartup");
                        if (!allDataObject.isNull("medal")) {
                            medalTag = allDataObject.getInt("medal");
                        }
                        //1为年终总结开启   0为年终总结关闭
                        Log.d("mason", "----summary---" + allDataObject.getString("summary"));
                        if (!allDataObject.isNull("summary")) {
                            if ("1".equals(allDataObject.getString("summary"))) {
                                SharedPreferencesTools.saveIsReportDialogShow(SplashActivity.this, true);
                            } else {
                                SharedPreferencesTools.saveIsReportDialogShow(SplashActivity.this, false);
                            }

                        }
                        final String url = startPageData.getString("newsupicurl");
                        SharedPreferencesTools.savememberUserDataPath(SplashActivity.this, new JSONObject(json).getString("memberUserData"));
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                final File filepath = new File(URLConfig.ccmtvapp_basesdcardpath);
                                try {
                                    final Uri uri = DownLoadImageUtil.getImageURI(startPageData.getString("newsupicurl"), filepath);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                RequestOptions options = new RequestOptions();
                                                options.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                                                    Glide.with(SplashActivity.this)
                                                        .load(url)
                                                        .apply(options)
                                                        .transition(DrawableTransitionOptions.withCrossFade(1000))
                                                        .into(splash_img);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                } catch (Exception e) {
                                    e.printStackTrace();
                                   runOnUiThread(new Runnable() {
                                       @Override
                                       public void run() {
                                           Glide.with(SplashActivity.this)
                                                   .load(url)
                                                   .transition(DrawableTransitionOptions.withCrossFade(1000))
                                                   .into(splash_img);
                                       }
                                   });
                                }
                            }
                        }).start();
                        if (obj.getInt("version") <= getVersion()) { //版本一致
                            //'switch'=>1,//1为引导页开启   0为关闭引导页
                            if (allDataObject.has("switch")) {
                                SharedPreferencesTools.saveIsShowGuidePic(SplashActivity.this, allDataObject.getString("switch"));
                            }
                            //暂时处理方式为把原来为1的值变为2,后续如有更好处理方式再更改
                            if (!SharedPreferencesTools.getcodezd(SplashActivity.this).equals("3") &&
                                    SharedPreferencesTools.getIsShowGuidePic(SplashActivity.this).equals("1")) {
                                Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                times();
                            }
                        } else {//版本不一致
                            if (obj.getString("force").equals("1")) {//强制更新
                                MustUpdateCustomDialog2 dialog_up = new MustUpdateCustomDialog2(SplashActivity.this,
                                        R.style.myupgrade, R.layout.must_upcustomdialog2, obj.getString("des"));
                                dialog_up.setCancelable(false);
                                dialog_up.show();
                            } else {//不需要强制更新
                                UpdateCustomDialog dialog_up = new UpdateCustomDialog(SplashActivity.this,
                                        R.style.myupgrade, R.layout.must_upcustomdialog2, obj.getString("des"));
                                dialog_up.setCancelable(false);
                                dialog_up.show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    }
                    break;

                case 500:
                    Toast.makeText(getApplicationContext(), R.string.post_hint3, Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_splash);
        btn_skip = (SplashJumpView) findViewById(R.id.btn_skip);
        splash_img = (ImageView) findViewById(R.id.splash_img);
        //获取接口系统版本
        getAppConfig();
        setOnClick();
        if((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0){
            finish();
            return;
        }
        if (SystemUtil.getDeviceBrand().equals("Huawei") || SystemUtil.getDeviceBrand().equals("HONOR")) {
//         华为推送链接
            HMSAgent.connect(this, new ConnectHandler() {
                @Override
                public void onConnect(int rst) {
                    LogUtil.e("HMS connect end:", rst + "");
                    getToken();
                    getPushStatus();
                }
            });
        }

        getObtainMedal();
//        bind();
    }

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

    /**
     * 获取push状态 | Get Push State
     */
    private void getPushStatus() {
        LogUtil.e("Token", "getPushState:begin");
        HMSAgent.Push.getPushState(new GetPushStateHandler() {
            @Override
            public void onResult(int rst) {
                LogUtil.e("Token", "getPushState:end code=" + rst);
            }
        });
    }

    /**
     * 获取token
     */
    private void getToken() {
        LogUtil.e("Token", "get token: begin");
        HMSAgent.Push.getToken(new GetTokenHandler() {
            @Override
            public void onResult(int rst) {
                LogUtil.e("Token", "get token: end" + rst);
            }
        });
    }


    public void setOnClick() {
        splash_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isstartup == 1) {
                    Intent intent = null;
                    intent = new Intent(SplashActivity.this, SplashWebActivity.class);
                    intent.putExtra("title", title);
                    intent.putExtra("aid", webUrl);
                    timer.cancel();
                    startActivity(intent);
                    finish();
                }
            }
        });
        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                Intent intent1 = getIntent();
                Uri uri = intent1.getData();
                if (uri != null) {
                    LogUtil.e("网页地址",uri.toString());
                    if(uri.toString().contains("ccmtvMedal")){
                        MainActivity.video_book = MainActivity.video_book + 1;
                        startActivity(new Intent(SplashActivity.this, MedalListActivity.class).putExtra("type", "2") );
                    }else   {
                        MainActivity.video_book = MainActivity.video_book + 1;
                        String bookid = uri.getQueryParameter("bookid");
                        startActivity(new Intent(SplashActivity.this, Video_book.class).putExtra("type", "1").putExtra("book_id", bookid));
                    }
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("video_book", MainActivity.video_book + "");
                    intent.putExtra("medalTag",medalTag);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    /**
     * name：获取接口系统版本
     * author：MrSong
     * data：2016/4/26 10:51
     */
    private void getAppConfig() {
        int network = getNetworkState();
        if (network == 0) {
            dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_setnet, null);
            dialog.setContentView(view);
            Button btn_lookloacl = (Button) view.findViewById(R.id.btn_lookloacl);
            Button btn_toset = (Button) view.findViewById(R.id.btn_toset);
            btn_toset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    startActivity(intent);
                    finish();
                    dialog.dismiss();
                }
            });
            btn_lookloacl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SplashActivity.this, DownloadFinishActivity.class);
                    startActivity(intent);
                    finish();
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String url = URLConfig.Interface_URL + "do/ccmtvappandroid/ccmtvapp.php";
                        String result = HttpClientUtils.sendPost(SplashActivity.this, url, new JSONObject().put("act", URLConfig.updateDownApp).toString());
//                        Log.e(getLocalClassName(), "启动页数据: "+result);
                        final JSONObject object = new JSONObject(result);
                        SharedPreferencesTools.saveAppConfig(SplashActivity.this, object.toString());
                        if (object.getInt("status") == 1) {
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = object.toString();
                            handler.sendMessage(msg);
                        } else {
                            handler.sendEmptyMessage(500);
                        }
                    } catch (Exception e) {
                        Toast.makeText(SplashActivity.this, getResources().getString(R.string.post_hint3), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }).start();
/*            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String url = URLConfig.CCMTVAPP + "?source=ANDROID";
                        String result = HttpClientUtils.sendPost(SplashActivity.this, url, new JSONObject().put("act", URLConfig.updateDownApp).toString());
                        final JSONObject object = new JSONObject(result);
                        SharedPreferencesTools.saveAppConfig(SplashActivity.this, object.toString());
                        if (object.getInt("status") == 1) {
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = object.toString();
                            handler.sendMessage(msg);
                        } else {
                            handler.sendEmptyMessage(500);
                        }
                    } catch (Exception e) {
                        Toast.makeText(SplashActivity.this, getResources().getString(R.string.post_hint3), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }).start();*/

            DownLoadARAssert();
        }
    }


    /**
     * 开启服务下载AR资源
     */
    public void DownLoadARAssert() {
        File filepath = new File(URLConfig.ccmtvapp_basesdcardpath + "/arImage");  //存储位置为URLConfig.ccmtvapp_basesdcardpath，非固定路径。可选择内置或者外置内存卡
        //判断文件夹是否存在,如果不存在则创建文件夹
        if (!filepath.exists()) {
            filepath.mkdir();
        }
        Intent intent = new Intent(SplashActivity.this, DownloadARImgService.class);
        startService(intent);
    }

    /**
     * name：倒计时进入首页
     * author：MrSong
     * data：2016/4/26 10:51
     */
    private void times() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sleepTime--;
                runOnUiThread(new Runnable() {
                    public void run() {
                        // btn_skip.setText("点击跳过" + sleepTime + "s");
                        btn_skip.setText(sleepTime);
                    }
                });
                if (sleepTime == 0) {
                    try {
                        if ("1".equals(obj_adv.getString("ishasad"))) {
                            Intent intent = new Intent(SplashActivity.this, AdvertiseActivity.class);
                            intent.putExtra("adpicurl", obj_adv.getString("adpicurl"));
                            intent.putExtra("isstartup", obj_adv.getString("isstartup"));
                            intent.putExtra("advertisementurl", obj_adv.getString("advertisementurl"));
                            intent.putExtra("title", obj_adv.getString("title"));
                            startActivity(intent);
                            finish();
                        } else {
                            timer.cancel();
                            Intent intent1 = getIntent();
                            Uri uri = intent1.getData();
                            if (uri != null) {
                                LogUtil.e("网页地址",uri.toString());
                                if(uri.toString().contains("ccmtvMedal")){
                                    MainActivity.video_book = MainActivity.video_book + 1;
                                    startActivity(new Intent(SplashActivity.this, MedalListActivity.class).putExtra("type", "2") );
                                }else   {
                                    MainActivity.video_book = MainActivity.video_book + 1;
                                    String bookid = uri.getQueryParameter("bookid");
                                    startActivity(new Intent(SplashActivity.this, Video_book.class).putExtra("type", "1").putExtra("book_id", bookid));
                                }
                                finish();
                            } else {

                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                intent.putExtra("medalTag",medalTag);
                                startActivity(intent);
                                finish();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, 0, 1000);
    }

    /**
     * name：获取当前应用程序的版本号
     * author：MrSong
     * data：2016/4/26 10:53
     */
    private int getVersion() {
        int st = 1;
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packinfo = pm.getPackageInfo(getPackageName(), 0);
            int version = packinfo.versionCode;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return st;
        }
    }

    /**
     * name：获取当前网络状态
     * author：MrSong
     * data：2016/4/26 11:32
     * return result int
     * 0    没有网络（手机未开启网络连接）
     * 1    WIFI网络
     * 2    2G网络
     * 3    3G网络
     * 4    4G网络
     */
    private int getNetworkState() {
        int netType = 0;
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager conn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        if (info == null) {//手机未开启网络
            return netType;
        }
        if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int subtype = info.getSubtype();
            //NETWORK_TYPE_EDGE 移动2G
            //NETWORK_TYPE_GPRS 联通2G
            //NETWORK_TYPE_CDMA 电信2G
            if (subtype == TelephonyManager.NETWORK_TYPE_EDGE ||
                    subtype == TelephonyManager.NETWORK_TYPE_GPRS ||
                    subtype == TelephonyManager.NETWORK_TYPE_CDMA) {
                netType = 2;
                //NETWORK_TYPE_UMTS 联通3g（网络类型：UMTS）
                //NETWORK_TYPE_HSDPA 联通3g（网络类型：HSDPA）
                //NETWORK_TYPE_EVDO_A 电信3G（网络类型：EVDO   版本A）
                //NETWORK_TYPE_EVDO_0 电信3G（网络类型：EVDO   版本0）
                //NETWORK_TYPE_EVDO_B 电信3G（网络类型：EVDO   版本B）
            } else if (subtype == TelephonyManager.NETWORK_TYPE_UMTS ||
                    subtype == TelephonyManager.NETWORK_TYPE_HSDPA ||
                    subtype == TelephonyManager.NETWORK_TYPE_EVDO_A ||
                    subtype == TelephonyManager.NETWORK_TYPE_EVDO_0 ||
                    subtype == TelephonyManager.NETWORK_TYPE_EVDO_B) {
                netType = 3;
                // LTE是3g到4g的过渡，是3.9G的全球标准
            } else if (subtype == TelephonyManager.NETWORK_TYPE_LTE) {
                netType = 4;
            }
        } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            netType = 1;
        }
        return netType;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        JPushInterface.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //  unregisterReceiver(receiver);
    }

    /**
     * 提醒后台查询用户的勋章完成情况
     */
    private void getObtainMedal() {
        final String uid = SharedPreferencesTools.getUids(this);
        if (TextUtils.isEmpty(uid)) return;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.obtainMedal);
                    obj.put("uid", uid);
                    String result = HttpClientUtils.sendPostToGP(SplashActivity.this, URLConfig.CCMTVAPP, obj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }
}

