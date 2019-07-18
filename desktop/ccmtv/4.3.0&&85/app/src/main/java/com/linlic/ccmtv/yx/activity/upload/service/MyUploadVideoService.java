package com.linlic.ccmtv.yx.activity.upload.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.Base64utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;

/**
 * name：上传视频服务
 * author：MrSong
 * data：2016/4/22.
 */
public class MyUploadVideoService extends Service {
    public static int upload_success = 200;//上传成功
    public static int upload_failure = 500;//上传失败
    public static int upload_progress = 18;//上传中
    public static int upload_wait = 300;//等待上传
    public static int now_statu = 0;//当前上传状态

    private MyBinder myBinder = new MyBinder();
    public static final String MyLog = "上传视频服务：";

    @Override
    public void onCreate() {
        super.onCreate();
//        Log.d(MyLog, "service onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.d(MyLog, "service onStartCommand，开始执行后台任务");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Log.d(MyLog, "service onDestroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    public class MyBinder extends Binder {
        public void startUpload(File videoFile, File imageFile, final String json,String datacheck) {

            if (videoFile == null) return;

            HttpUtils httpUtils = new HttpUtils();
            //设置线程数
//                httpUtils.configRequestThreadPoolSize(1);
            RequestParams params = new RequestParams();
            params.addBodyParameter("fileSource", videoFile);
            params.addBodyParameter("fileSourceImg", imageFile);
            params.addBodyParameter("data", Base64utils.getBase64(Base64utils.getBase64(json)));
            params.addBodyParameter("datacheck", Base64utils.getBase64(Base64utils.getBase64(datacheck)));

            httpUtils.send(HttpRequest.HttpMethod.POST, URLConfig.ccmtvapp_uploadVideo, params,
                    new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            now_statu = upload_success;
                            String result = Base64utils.getFromBase64(Base64utils.getFromBase64(responseInfo.result));
//                            Log.d(MyLog, "上传成功：" + responseInfo.result);
//                            Log.d(MyLog, "解密后：" + result);
                            try {
//                                Log.d(MyLog, "Message：" + new JSONObject(result).getString("errorMessage"));
                            } catch ( Exception e) {
                                e.printStackTrace();
                            }
                            Intent i = new Intent();
                            i.setAction("upload_video_success");
                            i.putExtra("result", result);
                            sendBroadcast(i);
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            now_statu = upload_failure;
//                            Log.d(MyLog, "上传失败：" + s);
                            Intent i = new Intent();
                            i.setAction("upload_video_failure");
                            i.putExtra("result", s);
                            sendBroadcast(i);
                        }

                        @Override
                        public void onLoading(long total, long current, boolean isUploading) {
                            now_statu = upload_progress;
//                            Log.d(MyLog, "total:" + total + ",current:" + current + ",isUploading:" + isUploading);
                            if (isUploading == true) {
                                double cu = current;
                                double to = total;
                                double cuto = cu / to;
                                if (cuto <= 0) {
                                    cuto = 0 + cuto;
                                }
                                String result = "0%";
                                if (cuto != 0) {
                                    result = new DecimalFormat("#0.00").format(cuto * 100) + "%";
                                }
//                                Log.d(MyLog, "除以计算：" + result);
                                Intent i = new Intent();
                                i.setAction("upload_video_progress");
                                i.putExtra("progress", result);
                                i.putExtra("total", ((int) (to / 1024 / 1024)) + "M");
                                i.putExtra("current", ((int) (cu / 1024 / 1024)) + "M");
                                sendBroadcast(i);
                            }
                        }
                    });
        }
    }

}
