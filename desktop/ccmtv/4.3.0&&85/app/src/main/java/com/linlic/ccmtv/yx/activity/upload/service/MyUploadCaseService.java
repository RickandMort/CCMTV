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
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.Base64utils;
import com.linlic.ccmtv.yx.utils.FileSizeUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;


/**
 * name：后台service
 * author：MrSong
 * data：2016/3/20.
 */
public class MyUploadCaseService extends Service {
    public static int upload_success = 200;//上传成功
    public static int upload_failure = 500;//上传失败
    public static int upload_progress = 18;//上传中
    public static int upload_wait = 300;//等待上传
    public static int now_statu = 0;//当前上传状态

    private MyBinder myBinder = new MyBinder();
    public static final String MyLog = "上传病例服务：";

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
        public void startUploadCase(RequestParams params, final String json) {

            HttpUtils httpUtils = new HttpUtils();
            //设置线程数
//                httpUtils.configRequestThreadPoolSize(1);
            params.addBodyParameter("data", Base64utils.getBase64(Base64utils.getBase64(json)));
            //  params.addBodyParameter("datacheck", Base64utils.getBase64(Base64utils.getBase64(datacheck)));

            httpUtils.send(HttpRequest.HttpMethod.POST, URLConfig.ccmtvapp_uploadCase, params,
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
                            i.setAction("upload_case_success");
                            i.putExtra("result", result);
                            sendBroadcast(i);
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            now_statu = upload_failure;
//                            Log.d(MyLog, "上传失败：" + s);
                            //         dialog.dismiss();
                            Intent i = new Intent();
                            i.setAction("upload_case_failure");
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
/*
                            dialog.setProgressNumberFormat((int) (cu / 1024 / 1024) + "M/" + (int) (to / 1024 / 1024) + "M");
                            //设置进度条风格STYLE_HORIZONTAL
                            dialog.setProgressStyle(
                                    ProgressDialog.STYLE_HORIZONTAL);
                            dialog.setTitle(edit_casetitle.getText().toString());
                            dialog.setProgress((int) (cu / 1024 / 1024));
                            dialog.setMax((int) (to / 1024 / 1024));
                            dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
                            dialog.show();*/
                                Intent i = new Intent();
                                i.setAction("upload_case_progress");
                                i.putExtra("progress", result);
                                i.putExtra("total", (FileSizeUtil.FormetFileSize(total)));
                                i.putExtra("current", (FileSizeUtil.FormetFileSize(current)));
                                sendBroadcast(i);
                            }
                        }
                    });
        }
    }


}
