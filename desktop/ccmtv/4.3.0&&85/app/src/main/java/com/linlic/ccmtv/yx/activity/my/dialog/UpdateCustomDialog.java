package com.linlic.ccmtv.yx.activity.my.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * name：升级提示对话框
 * author：Larry
 * data：2016/6/1.
 */
public class UpdateCustomDialog extends Dialog implements
        View.OnClickListener {

    /**
     * 布局文件
     **/
    int layoutRes;
    /**
     * 上下文对象
     **/
    Activity activity;
    /**
     * 确定按钮
     **/
    private Button confirmBtn;
    /**
     * 取消按钮
     **/
    private Button cancelBtn;
    /* 下载保存路径 */
    private String mSavePath;
    /* 更新进度条 */
    private ProgressBar mProgress;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;
    /* 记录进度条数量 */
    private int progress;
    private Dialog mDownloadDialog;
    private TextView tv_showphone, tv_showtip;
    private HttpHandler handler;
    private long startTime;//开始下载时获取开始时间（计算下载速度需要）
    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;
    String appName;
    String str_tip;
    //下载完成，取消通知
    NotificationManager notificationManager;
    long time ;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 正在下载
                case DOWNLOAD:
                    // 设置进度条位置
                    mProgress.setProgress(progress);
                    break;
                case DOWNLOAD_FINISH:
                    // 安装文件
                    installApks();
                    break;
                default:
                    break;
            }
        }

    };


    /**
     * 自定义主题及布局的构造方法
     */
    public UpdateCustomDialog(Activity activity, int theme, int resLayout, String str_tip) {
        super(activity, theme);
        this.activity = activity;
        this.layoutRes = resLayout;
        this.str_tip = str_tip;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 指定布局
        this.setContentView(layoutRes);

        // 根据id在布局中找到控件对象
        confirmBtn = (Button) findViewById(R.id.confirm_btns);
        cancelBtn = (Button) findViewById(R.id.cancel_btns);
        tv_showphone = (TextView) findViewById(R.id.tv_showphone);
        tv_showtip = (TextView) findViewById(R.id.tv_showtip);
        tv_showphone.setText("发现新版本");
        tv_showtip.setText(str_tip);
        // 为按钮绑定点击事件监听器
        confirmBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.confirm_btns:
                // 点击了确认按钮
                time = System.currentTimeMillis();
                Toast.makeText(activity, "已开启后台下载", Toast.LENGTH_SHORT).show();
                download(URLConfig.DOWNLOAD_APK);
                UpdateCustomDialog.this.dismiss();

                // 构造软件下载对话框
              /*  AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("正在下载....");
                // 给下载对话框增加进度条
                final LayoutInflater inflater = LayoutInflater.from(activity);
                View vs = inflater.inflate(R.layout.softupdate_progress, null);
                mProgress = (MyProgressBar) vs.findViewById(R.id.update_progress);
                builder.setView(vs);
                // 取消更新
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // 设置取消状态
                        cancelUpdate = true;
                        // 设置取消状态
                        activity.finish();
                    }
                });
                builder.setCancelable(false);
                mDownloadDialog = builder.create();
                mDownloadDialog.show();
                // 现在文件
                downloadApk();
                break;*/

            case R.id.cancel_btns:
                // 点击了取消按钮
                UpdateCustomDialog.this.dismiss();
//                if (!Settings.canDrawOverlays(getContext())) {
//                    return;
//                }
                activity.startActivity(new Intent(activity, MainActivity.class));
                activity.finish();
                //Log.e("hhhhhhh",context.getClass());
                break;

            default:
                break;
        }
    }


    /**
     * name：开始下载《自带断点》
     * author：MrSong
     * data：2016/4/1 14:35
     */
    private void download(String url) {
        startTime = System.currentTimeMillis(); // 开始下载时获取开始时间（计算下载速度需要）
        HttpUtils httpUtils = new HttpUtils();
        try {
            mSavePath = SharedPreferencesTools.getCachePath(activity)+ "/download/"+time;
        } catch (Exception e) {
            e.printStackTrace();
        }
        handler = httpUtils.download(url,
                mSavePath, true, true, new RequestCallBack<File>() {
                    @Override
                    public void onSuccess(ResponseInfo<File> responseInfo) {
                        appName = responseInfo.result.getPath();
//                        Log.i("jjjj","appName"+appName);
                        notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.cancel(4);
                        installApks();
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        //更新进度
                        Intent i = new Intent();
                        i.setAction("update_app_failure");
                        activity.sendBroadcast(i);
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
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
                            //下载速度计算
                            long curTime = System.currentTimeMillis();
                            int usedTime = (int) ((curTime - startTime) / 1000);
                            if (usedTime == 0) usedTime = 1;
                            int speed = (int) ((cu / usedTime) / 1024); // 下载速度
                            //更新进度
                            Intent i = new Intent();
                            i.setAction("update_app_progress");
                            i.putExtra("progress", result);
                            activity.sendBroadcast(i);

                        }
                    }
                });

    }



    /**
     * 下载apk文件
     */
    private void downloadApk() {
        // 启动新线程下载软件
        new downloadApkThread().start();
    }

    /**
     * 下载文件线程
     */
    private class downloadApkThread extends Thread {
        @Override
        public void run() {
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    try {
                        mSavePath = URLConfig.ccmtvapp_basesdcardpath + "/download/";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    URL url = new URL(URLConfig.DOWNLOAD_APK);
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.connect();
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();

                    File file = new File(mSavePath);
                    // 判断文件目录是否存在
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    File apkFile = new File(mSavePath, "ccmtvapp");
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        mHandler.sendEmptyMessage(DOWNLOAD);
                        if (numread <= 0) {
                            // 下载完成
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);// 点击取消就停止下载.
                    fos.close();
                    is.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 取消下载对话框显示
            mDownloadDialog.dismiss();
        }
    }

    ;



    /**
     * 安装APK文件
     */
    private void installApks() {
        File apkfile = new File(mSavePath);
        if (!apkfile.exists()) {
            return;
        }
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(i);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}