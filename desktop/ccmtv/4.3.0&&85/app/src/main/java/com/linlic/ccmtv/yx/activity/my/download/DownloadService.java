package com.linlic.ccmtv.yx.activity.my.download;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.linlic.ccmtv.yx.activity.db.MyDbUtils;
import com.linlic.ccmtv.yx.activity.entity.DbDownloadVideo;
import com.linlic.ccmtv.yx.utils.NetUtil;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.text.DecimalFormat;

/**
 * name：下载功能服务器类
 * author：MrSong
 * data：2016/4/5.
 */
public class DownloadService extends Service {
    public static int download_success = 200;//下载成功
    public static int download_failure = 500;//下载失败
    public static int download_progress = 18;//下载中
    public static int download_stop = 100;//下载暂停
    public static int download_wait = 300;//等待下载
    public static int now_statu = 0;//当前下载状态
    private long startTime;//开始下载时获取开始时间（计算下载速度需要）
    private HttpHandler handler;
    Context context;
    private String filePath = null;
    private String videoId, videoName, downProgress, downURL;
    boolean isStop;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.d(Down.MyLog, "service onStartCommand，开始执行后台任务");
        videoId = intent.getExtras().getString("videoId");
        videoName = intent.getExtras().getString("videoName");
        downProgress = intent.getExtras().getString("downProgress");
        downURL = intent.getExtras().getString("downURL");
        isStop = intent.getExtras().getBoolean("isStop");

        if (isStop) {
            stopDownload();
        } else {
            try {
                //  download(downURL, videoName, videoId);
                startTime = System.currentTimeMillis(); // 开始下载时获取开始时间（计算下载速度需要）
                HttpUtils httpUtils = new HttpUtils();
                filePath = SharedPreferencesTools.getCachePath(context) + "/video/" + videoName + downURL.substring(downURL.lastIndexOf("."));
//            Log.i("视频下载路径","视频下载路径:"+filePath);
                downloadFile(downURL, filePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * name：停止下载
     * author：MrSong
     * data：2016/4/1 14:35
     */
    public void stopDownload() {
        if (handler != null) {
            handler.cancel();
            now_statu = download_stop;
//            Log.d(Down.MyLog, "任务已暂停");
        }
    }

    /**
     * name：开始下载《自带断点》
     * author：MrSong
     * data：2016/4/1 14:35
     */
    private void download(String url, final String videoName, final String videoId) {
//        Log.d(Down.MyLog, "已进入下载方法！！！！");
        startTime = System.currentTimeMillis(); // 开始下载时获取开始时间（计算下载速度需要）
        HttpUtils httpUtils = new HttpUtils();
        try {
            filePath = SharedPreferencesTools.getCachePath(context) + "/video/" + videoName + url.substring(url.lastIndexOf("."));
//            Log.i("视频下载路径","视频下载路径:"+filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        handler = httpUtils.download(url,
                filePath, true, true, new RequestCallBack<File>() {
                    @Override
                    public void onSuccess(ResponseInfo<File> responseInfo) {
//                        Log.d(Down.MyLog, "下载成功:" + responseInfo.result + ",路径:" + responseInfo.result.getPath());
                        now_statu = download_success;
                        Intent i = new Intent();
                        i.setAction("download_success");
                        i.putExtra("videoId", videoId);
                        i.putExtra("videoName", videoName);
                        i.putExtra("filePath", filePath);
                        sendBroadcast(i);
                    }

                    @Override
                    public void onStart() {
                        now_statu = download_progress;
                        super.onStart();
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
//                        Log.d("下载失败：", "下载失败：" + s);
                        boolean isConnected = NetUtil.isNetworkConnected(context);
//                        System.out.println("网络状态：" + isConnected);
                        if (s.equals("maybe the file has downloaded completely")) {//也许文件已完全下载
                            now_statu = download_success;
                            Intent i = new Intent();
                            i.setAction("download_success");
                            i.putExtra("videoId", videoId);
                            i.putExtra("videoName", videoName);
                            i.putExtra("filePath", filePath);
                            sendBroadcast(i);
                        } else if (isConnected) {
//                            Log.d(Down.MyLog, "下载失败：" + "网络已链接");
                            now_statu = download_failure;
                            Intent i = new Intent();
                            i.setAction("download_failure");
                            i.putExtra("videoId", videoId);
                            i.putExtra("videoName", videoName);
                            sendBroadcast(i);
                        } else {
//                            Log.d(Down.MyLog, "下载暂停：" + "网络未链接");
                            Intent i = new Intent();
                            i.setAction("download_stop");
                            i.putExtra("videoId", videoId);
                            i.putExtra("net_fail_stop", true);
                            now_statu = download_stop;
                            //i.putExtra("videoState", MyDbUtils.findVideoMsg(context, videoId).getState());
                            i.putExtra("videoState", "100");
                            sendBroadcast(i);
                        }
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        now_statu = download_progress;
//                        Log.d(Down.MyLog, "total:" + total + ",current:" + current + ",isUploading:" + isUploading);
                        if (isUploading == false) {
                            double cu = current;
                            double to = total;
                            double cuto = cu / to;
                            if (cuto <= 0) {
                                cuto = 0 + cuto;
                            }
                            String result = "排队中";
                            if (cuto != 0) {
                                result = new DecimalFormat("#0.00").format(cuto * 100) + "%";
                            }
//                            Log.d(Down.MyLog, "除以计算：" + result);
                            //下载速度计算
                            long curTime = System.currentTimeMillis();
                            int usedTime = (int) ((curTime - startTime) / 1000);
                            if (usedTime == 0) usedTime = 1;
                            int speed = (int) ((cu / usedTime) / 1024); // 下载速度
                            //更新进度
                            Intent i = new Intent();
                            i.setAction("download_progress");
                            i.putExtra("videoId", videoId);
                            i.putExtra("progress", result);
                            i.putExtra("videoName", videoName);
                            i.putExtra("total", ((int) (to / 1024 / 1024)) + "M");
                            i.putExtra("current", ((int) (cu / 1024 / 1024)) + "M");
                            i.putExtra("filePath", filePath);
                            i.putExtra("speed", speed + "KB/s");
                            sendBroadcast(i);


                        }
                    }
                });

    }


    private void downloadFile(final String url, String path) {
        
        RequestParams requestParams = new RequestParams(url);
        requestParams.setSaveFilePath(path);
        x.http().get(requestParams, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {
            }

            @Override
            public void onStarted() {
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                now_statu = download_progress;
                //  Log.i("下载", "下载中"+"total:" + total + ",current:" + current + ",isDownloading:" + isDownloading);
                if (isDownloading) {
                    double cu = current;
                    double to = total;
                    double cuto = cu / to;
                    if (cuto <= 0) {
                        cuto = 0 + cuto;
                    }
                    String result = "排队中";
                    if (cuto != 0) {
                        result = new DecimalFormat("#0.00").format(cuto * 100) + "%";
                    }
                    //下载速度计算
                    long curTime = System.currentTimeMillis();
                    int usedTime = (int) ((curTime - startTime) / 1000);
                    if (usedTime == 0) usedTime = 1;
                    int speed = (int) ((cu / usedTime) / 1024); // 下载速度
                    //更新进度
                    Intent i = new Intent();
                    i.setAction("download_progress");
                    i.putExtra("videoId", videoId);
                    i.putExtra("progress", result);
                    i.putExtra("videoName", videoName);
                    i.putExtra("total", ((int) (to / 1024 / 1024)) + "M");
                    i.putExtra("current", ((int) (cu / 1024 / 1024)) + "M");
                    i.putExtra("filePath", filePath);
                    i.putExtra("speed", speed + "KB/s");
                    sendBroadcast(i);
                }
            }

            @Override
            public void onSuccess(File result) {
                //Toast.makeText(MainActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
                now_statu = download_success;
                Intent i = new Intent();
                i.setAction("download_success");
                i.putExtra("videoId", videoId);
                i.putExtra("videoName", videoName);
                i.putExtra("filePath", filePath);
                sendBroadcast(i);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                // Toast.makeText(MainActivity.this, "下载失败，请检查网络和SD卡", Toast.LENGTH_SHORT).show();
                boolean isConnected = NetUtil.isNetworkConnected(context);
//                        System.out.println("网络状态：" + isConnected);
                if (isConnected) {
//                            Log.d(Down.MyLog, "下载失败：" + "网络已链接");
                    now_statu = download_failure;
                    Intent i = new Intent();
                    i.setAction("download_failure");
                    i.putExtra("videoId", videoId);
                    i.putExtra("videoName", videoName);
                    sendBroadcast(i);
                } else {
//                            Log.d(Down.MyLog, "下载暂停：" + "网络未链接");
                    Intent i = new Intent();
                    i.setAction("download_stop");
                    i.putExtra("videoId", videoId);
                    i.putExtra("net_fail_stop", true);
                    now_statu = download_stop;
                    //i.putExtra("videoState", MyDbUtils.findVideoMsg(context, videoId).getState());
                    i.putExtra("videoState", "100");
                    sendBroadcast(i);
                }
                ex.printStackTrace();
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        Log.d(Down.MyLog, "service onCreate");
        context = this;

        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mFilter.addAction("down_stop");
        registerReceiver(mReceiver, mFilter);
    }


    private ConnectivityManager connectivityManager;
    private NetworkInfo info;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(final Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                info = connectivityManager.getActiveNetworkInfo();
                if (info != null && info.isAvailable()) {
                    //   String name = info.getTypeName();
                    if (NetUtil.isWifiAvailable(context)) {
                        //  Toast.makeText(context, "网络已连接" + name, Toast.LENGTH_SHORT).show();
//                        Log.i("网络", "网络已连接" + "WIFI WIFI" + "接着下载");
                        Intent i = new Intent();
                        i.setAction("download_stop");
                        i.putExtra("videoId", videoId);
                        DbDownloadVideo video = MyDbUtils.findVideoMsg(context, videoId);
                        i.putExtra("videoState", video.getState());
                        sendBroadcast(i);
                        //if (NetUtil.isMobileConnected(context))
                    } else if (NetUtil.isMobileConnected(context)) {
                        // Toast.makeText(context, "网络已连接" + name, Toast.LENGTH_SHORT).show();
                        stopDownload();
//                        Log.i("网络", "网络已连接" + "GPRS GPRS" + "提示用户是否接着下载");
                        intent.setClass(context, TipGRRSActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("videoId", videoId);
                        context.startActivity(intent);

                      /*  dialog = new Dialog(getApplicationContext(), R.style.ActionSheetDialogStyle);
                        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_tip, null);
                        dialog.setContentView(view);
                        Button btn_goon = (Button) view.findViewById(R.id.btn_goon);
                        Button btn_stop = (Button) view.findViewById(R.id.btn_stop);
                        btn_goon.setOnClickListener(new View.OnClickListener() {                //继续下载
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent();
                                i.setAction("download_stop");
                                i.putExtra("videoId", videoId);
                                DbDownloadVideo video = MyDbUtils.findVideoMsg(context, videoId);
                                i.putExtra("videoState", video.getState());
                                sendBroadcast(i);
                                dialog.dismiss();
                            }
                        });
                        btn_stop.setOnClickListener(new View.OnClickListener() {                //暂停下载
                            @Override
                            public void onClick(View v) {
                                stopDownload();
                                dialog.dismiss();
                            }
                        });
                        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);//需要添加的语句，不然弹不出啦对话框---魅族可以 小米不可以
                        dialog.show();*/
                        // Toast.makeText(context, "对话框弹出", Toast.LENGTH_SHORT).show();
                    }
                } else {
//                    Log.i("网络", "网络未连接");
                    Toast.makeText(context, "网络未连接,下载任务暂停", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };


    /**
     * 当被销毁的时候，我们要把BroadcastReceiver注销
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }


}