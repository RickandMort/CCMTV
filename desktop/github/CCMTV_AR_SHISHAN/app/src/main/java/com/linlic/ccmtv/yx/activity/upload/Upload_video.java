package com.linlic.ccmtv.yx.activity.upload;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.UploadModel;
import com.linlic.ccmtv.yx.activity.upload.adapter.UploadFeesAdapter;
import com.linlic.ccmtv.yx.activity.upload.service.MyUploadVideoService;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.Base64utils;
import com.linlic.ccmtv.yx.utils.FileSizeUtil;
import com.linlic.ccmtv.yx.utils.GetPathFromUri4kitkat;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okserver.OkUpload;
import com.lzy.okserver.upload.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.NumberFormat;

/**
 * name：上传视频
 * author：MrSong
 * data：2016/3/29 14:50
 */
public class Upload_video extends BaseActivity {
    private MyUploadVideoService.MyBinder myBinder;
    private String videoName;
    private String videoDurationText = "";
    private String videoPath;
    private PostRequest<String> postRequest;

    private GridView gvSelectFees;
    private RadioGroup radioGroup;
    private RadioButton rbfees, rbFree;

    private TextView charge_videoPath;
    //,upload_videoPath;
    private TextView upload_text1;
    private ImageView upload_text2;
    private File videoFile;
    private File imageFile;
    private EditText upload_plName;
    private EditText upload_fileMsg;
    //private LinearLayout upload_charge;
    private String[] items;
    private String[] items2;
    private Context context;
    //用户统计
    private String type;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    MyProgressBarDialogTools.hide();
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            items = new String[dataArray.length()];
                            items2 = new String[dataArray.length()];
                            for (int i = 0; i < dataArray.length(); i++) {
                                String str = dataArray.getString(i);
                                if (Integer.parseInt(str) > 0) {
                                    items[i] = str + "元";
                                    items2[i] = str;
                                } /*else {
                                    items[i] = "免费分享";
                                    items2[i] = str;
                                }*/
                            }
                        } else {
                            Toast.makeText(Upload_video.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
//                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    MyProgressBarDialogTools.hide();
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    private UploadFeesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_video);
        context = this;

        type = getIntent().getStringExtra("type");

        super.findId();
        super.setActivity_title_name(R.string.upload_video);

        //进入当前页获取一下用户ID，不然上传会闪退
        SharedPreferencesTools.getUid(Upload_video.this);


        upload_text1 = (TextView) findViewById(R.id.id_tv_select_video);//
        upload_text2 = (ImageView) findViewById(R.id.id_iv_upload_select_video);
        //upload_videoPath = (TextView) findViewById(R.id.upload_videoPath);//
        FrameLayout upload_upvideo = (FrameLayout) findViewById(R.id.id_fl_upload_video_select_video);//点击选择视频布局
        upload_plName = (EditText) findViewById(R.id.id_et_upload_video_title);
        upload_fileMsg = (EditText) findViewById(R.id.id_et_upload_video_filemsg);
        upload_upvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVideo();
            }
        });
        //upload_charge = (LinearLayout) findViewById(R.id.upload_charge);
        charge_videoPath = (TextView) findViewById(R.id.charge_videoPath);

        gvSelectFees = (GridView) findViewById(R.id.id_gv_upload_fees);
        radioGroup = (RadioGroup) findViewById(R.id.id_radiogroup_upload_fees);
        rbFree = (RadioButton) findViewById(R.id.id_rb_upload_video_free);
        rbfees = (RadioButton) findViewById(R.id.id_rb_upload_video_fees);

        setValue();
        //注册上传广播
//        IntentFilter fil = new IntentFilter();
//        fil.addAction("upload_progress");
//        fil.addAction("upload_failure");
//        fil.addAction("upload_success");
//        registerReceiver(new MyUploadReveiver(), fil);
        click();

        initGridview();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == rbFree.getId()) {
                    charge_videoPath.setText("收费");
                    charge_videoPath.setTag(0);
                    gvSelectFees.setVisibility(View.GONE);
                } else if (checkedId == rbfees.getId()) {
                    gvSelectFees.setVisibility(View.VISIBLE);
                    if (adapter == null) {
//                        adapter = new UploadFeesAdapter(Upload_video.this, items);
                        gvSelectFees.setAdapter(adapter);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initGridview() {
        //String[] feesDatas={"5元","10元","15元","20元","30元","40元","50元","60元","70元","80元","90元","100元"};
        gvSelectFees.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.e("Upload_video", "onItemClick: "+position);
                if (position != -1) {
//                    Log.e("Upload_video", "选择的费用"+items[position]+"items2"+items2[position]);
                    if (items[position].contains("免费")) {
                        rbFree.setChecked(true);
                    }
                    charge_videoPath.setText(items[position]);
                    charge_videoPath.setTag(items2[position]);
                    adapter.setSelected(position);
                }
            }
        });
    }

    public void click() {
        /*upload_charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingleChoiceDialog();
            }
        });*/
    }

    /**
     * name：提交
     * author：MrSong
     * data：2016/3/29 15:24
     */
    public void submit(View view) {
        videoName = upload_plName.getText().toString();
        String videoMsg = upload_fileMsg.getText().toString();

        if (TextUtils.isEmpty(videoName)) {
            Toast.makeText(getApplicationContext(), "视频名称不可为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(videoMsg)) {
            Toast.makeText(getApplicationContext(), "文件摘要不可为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (videoFile == null) {
            Toast.makeText(getApplicationContext(), "请选择视频文件", Toast.LENGTH_SHORT).show();
            return;
        }
        if (MyUploadVideoService.now_statu == MyUploadVideoService.upload_progress) {
            Toast.makeText(getApplicationContext(), "当前任务已正在上传，请等待上传完毕后在提交", Toast.LENGTH_SHORT).show();
            return;
        }
        //存储数据
        /*MyDbUtils.saveUploadVideoMsg(Upload_video.this, MyUploadVideoService.upload_wait + "", videoName, videoMsg, videoFile.getPath(), imageFile.getPath());
        //获取当前保存数据的ID
        int dbid = MyDbUtils.findUploadVideoMsg(Upload_video.this, videoName, videoMsg, videoFile.getPath(), imageFile.getPath());
        //发送广播
        Intent intent = new Intent();
        intent.setAction("upload_video");
        intent.putExtra("videoName", videoName);
        intent.putExtra("videoMsg", videoMsg);
        intent.putExtra("filePath", videoFile.getPath());
        intent.putExtra("fileImgPath", imageFile.getPath());
        intent.putExtra("upload_charge", charge_videoPath.getText().toString());
        intent.putExtra("dbid", dbid);
        intent.putExtra("type","home");
        sendBroadcast(intent);*/
        //启动服务
        //bindService(new Intent(Upload_video.this, MyUploadVideoService.class), connection, BIND_AUTO_CREATE);

        /**
         * 尝试更改上传为okUpload
         */
        OkUploadVideo();
        Toast.makeText(Upload_video.this, "视频已提交，可在上传列表中查看", Toast.LENGTH_SHORT).show();
        Intent intent1 = new Intent(Upload_video.this, IsUpload2.class);
        intent1.putExtra("TAG", "Video");
        //intent.putExtra("type","home");
        startActivity(intent1);
        finish();
    }

    private void OkUploadVideo() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(videoPath);
            mediaPlayer.prepare();
            long videoDuration = mediaPlayer.getDuration();
            videoDurationText = FileSizeUtil.formatLongToTimeStr(videoDuration);
        } catch (Exception e) {
            e.printStackTrace();
        }

        UploadModel uploadModel = new UploadModel();
        uploadModel.setName(videoName);
        uploadModel.setType("video");
        uploadModel.setVtime(videoDurationText);
        uploadModel.setIconUrl(imageFile.getAbsolutePath());
        uploadModel.setUrl(videoPath);
        JSONObject object = new JSONObject();
        JSONObject obj = new JSONObject();

        try {
            object.put("title", upload_plName.getText().toString());
            object.put("remark", upload_fileMsg.getText().toString());
//                object.put("filePath", upload_videoPath.getText().toString());
            object.put("act", URLConfig.uploadVideo);
            object.put("uid", SharedPreferencesTools.getUid(Upload_video.this));
            object.put("mvmoney", charge_videoPath.getTag().toString());
            //收费
            object.put("upload_charge", charge_videoPath.getText().toString());
            object.put("vtime", videoDurationText);
            obj.put("userAccount", SharedPreferencesTools.getUserName(Upload_video.this));
            obj.put("password", SharedPreferencesTools.getPassword(Upload_video.this));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        postRequest = OkGo.<String>post(URLConfig.ccmtvapp_uploadCase)
                .params("data", Base64utils.getBase64(Base64utils.getBase64(object.toString())))
                .params("datacheck", Base64utils.getBase64(Base64utils.getBase64(obj.toString())))
                .params("fileSource", videoFile)
                .params("fileSourceImg", imageFile)
                .converter(new StringConvert());

        UploadTask<String> task = OkUpload.request(imageFile.getPath(), postRequest)
                .priority(35)
                .extra1(uploadModel)
                .extra2(videoFile.getName())
                .save()
                .register(new LogUploadListener<String>());
        task.start();
    }

    int yourChoice;

    private void showSingleChoiceDialog() {
        yourChoice = -1;
        AlertDialog.Builder singleChoiceDialog = new AlertDialog.Builder(Upload_video.this);
        singleChoiceDialog.setTitle("是否收费");
        // 第二个参数是默认选项，此处设置为0
        singleChoiceDialog.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        yourChoice = which;
                    }
                });
        singleChoiceDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (yourChoice != -1) {
                            charge_videoPath.setText(items[yourChoice]);
                            charge_videoPath.setTag(items2[yourChoice]);
                        }
                    }
                });
        singleChoiceDialog.show();
    }

    public void setValue() {
        charge_videoPath.setTag("0");
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.upmoney);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, obj.toString());
                    MyProgressBarDialogTools.hide();

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
     * name：传输数据给service
     * author：MrSong
     * data：2016/3/29 15:33
     */
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            /*JSONObject object = new JSONObject();
            JSONObject obj = new JSONObject();
            try {
                object.put("title", upload_plName.getText().toString());
                object.put("remark", upload_fileMsg.getText().toString());
//                object.put("filePath", upload_videoPath.getText().toString());
                object.put("act", URLConfig.uploadVideo);
                object.put("uid", SharedPreferencesTools.getUid(Upload_video.this));
                object.put("mvmoney", charge_videoPath.getTag().toString());
                //收费
                object.put("upload_charge", charge_videoPath.getText().toString());
                obj.put("userAccount", SharedPreferencesTools.getUserName(Upload_video.this));
                obj.put("password", SharedPreferencesTools.getPassword(Upload_video.this));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            *//*视频上传*//*
            myBinder = (MyUploadVideoService.MyBinder) service;
            myBinder.startUpload(videoFile, imageFile, object.toString(), obj.toString());*/

//            Log.d(MyUploadVideoService.MyLog, "Activity与Service建立关联");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
//            Log.d(MyUploadVideoService.MyLog, "Activity与Service解除关联");
        }
    };

    /**
     * name：bitmap转file
     * author：MrSong
     * data：2016/4/12 21:17
     */
    public void bitmapForFile(Context context, Bitmap bitmap) {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File dir = new File(URLConfig.ccmtvapp_basesdcardpath);
            if (!dir.exists())
                dir.mkdirs();
            imageFile = new File(dir, System.currentTimeMillis() + ".png");
            try {
                FileOutputStream fos = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, R.string.myhead_sd_is_null, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * name：选择视频
     * author：MrSong
     * data：2016/3/23 18:06
     */
    private void showVideo() {
        Intent intent = new Intent(Upload_video.this, ChooseVideoActivity.class);
        startActivityForResult(intent, 30);
    }

    /**
     * name：根据视频地址保存为文件
     * author：MrSong
     * data：2016/3/23 18:06
     */
    private String uriChangePath(Uri uri) {
        String videoURL = "";
//        Log.e("系统版本",android.os.Build.VERSION.RELEASE);
        //判断是否是4.4之前的
        if (Double.parseDouble(android.os.Build.VERSION.RELEASE.substring(0, 3)) > 4.30d) {
            return GetPathFromUri4kitkat.getPath(Upload_video.this, uri);
        } else {
            Cursor cursor;
            if (uri.getScheme().toString().compareTo("content") == 0) {
                cursor = getContentResolver().query(uri, new String[]{MediaStore.Audio.Media.DATA}, null, null, null);
                if (cursor.moveToFirst()) {
                    videoURL = cursor.getString(0);
                }
            } else if (uri.getScheme().toString().compareTo("file") == 0) {        //file:///开头的uri
                // videoURL = uri.toString();
                videoURL = uri.toString().replace("file://", "");
                //替换file://
                if (!videoURL.startsWith("/mnt")) {
                    //加上"/mnt"头
                    videoURL += "/mnt";
                }
            }
            return videoURL;
        }
    }

    /**
     * name：获取视频第一帧图片
     * author：MrSong
     * data：2016/4/12 20:00
     */
    private Bitmap getVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    @Override
    protected void onDestroy() {
       /* if (connection != null) {
            unbindService(connection);
        }*/
        super.onDestroy();
    }

    // 回调方法，从第二个页面回来的时候会执行这个方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            videoPath = data.getStringExtra("videopath");
            String videoimg = data.getStringExtra("videoimg");
            String videotitle = data.getStringExtra("videotitle");

            // 根据上面发送过去的请求吗来区别
            switch (requestCode) {
                case 30:
                    videoFile = new File(videoPath);

                    videoFile = new File(videoPath);
                    String videoName = videoPath.substring(videoPath.lastIndexOf('/') + 1);
                    //upload_videoPath.setText(videoName);

                    upload_text1.setVisibility(View.GONE);
                    upload_text2.setVisibility(View.VISIBLE);

                    Bitmap bitmap = getVideoThumbnail(videoPath);
                    bitmapForFile(Upload_video.this, bitmap);
                    upload_text2.setImageBitmap(bitmap);
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            upload_text2.setImageResource(R.mipmap.img_default);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (type.equals("home")) {
            enterUrl = "http://www.ccmtv.cn";
        } else {
            enterUrl = "http://www.ccmtv.cn/Member/Index.html";
        }
        super.onPause();
    }

    public void uploadManager(View view) {
        Intent intent1 = new Intent(Upload_video.this, IsUpload2.class);
        intent1.putExtra("TAG", "Video");
        startActivity(intent1);
        finish();
    }

    private NumberFormat nf = NumberFormat.getPercentInstance();

    /*public class LogUploadListener<T> extends UploadListener<T> {
        public LogUploadListener() {
            super("LogUploadListener");
        }

        @Override
        public void onStart(Progress progress) {
            Log.e("LogUploadListener", "onStart: 开始上传视频");
        }

        @Override
        public void onProgress(Progress progress) {
            Log.e("LogUploadListener", "onProgress: 正在上传" + progress);
            double progressNum = progress.fraction;
            nf.setMaximumFractionDigits(1);
            MyNotificationCase(context, "上传任务已添加", "正在上传" + videoName, "当前进度:" + nf.format(progressNum), false);
        }

        @Override
        public void onError(Progress progress) {
            Log.e("LogUploadListener", "onError: 上传出错" + progress.exception);
            MyNotificationCase(context, "您有任务上传失败", videoName + "上传失败", "上传失败", true);
        }

        @Override
        public void onFinish(T t, Progress progress) {
            Log.e("LogUploadListener", "onFinish: 上传完成");
            MyNotificationCase(context, "1个任务上传完成", videoName + "上传成功", "上传完成", true);
        }

        @Override
        public void onRemove(Progress progress) {

        }
    }

    public void MyNotificationCase(Context context, String tan, String title, String cont, boolean isProgress) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, IsUpload2.class);
        intent.putExtra("TAG", "Case");
        PendingIntent pendingIntent3 = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // 通过Notification.Builder来创建通知，注意API Level
        Notification notify = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.app_icon)
                .setTicker(tan)
                .setContentTitle(title)
                .setContentText(cont)
                .setContentIntent(pendingIntent3).setNumber(1).build();

        // level16及之后增加的，API11可以使用getNotificatin()来替代
        notify.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
        if (isProgress == true) {
            //添加声音
            notify.defaults |= Notification.DEFAULT_SOUND;
            //添加震动
            notify.defaults |= Notification.DEFAULT_VIBRATE;
        }

        manager.notify(3, notify);// 步骤4：通过通知管理器来发起通知。如果id不同，则每click，在status哪里增加一个提示
    }*/
}
