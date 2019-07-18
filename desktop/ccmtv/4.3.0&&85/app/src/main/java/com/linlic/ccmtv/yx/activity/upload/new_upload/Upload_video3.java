package com.linlic.ccmtv.yx.activity.upload.new_upload;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.UploadModel;
import com.linlic.ccmtv.yx.activity.upload.ChooseVideoActivity;
import com.linlic.ccmtv.yx.activity.upload.LogUploadListener;
import com.linlic.ccmtv.yx.activity.upload.adapter.UploadFeesAdapter;
import com.linlic.ccmtv.yx.activity.upload.service.MyUploadVideoService;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.Base64utils;
import com.linlic.ccmtv.yx.utils.FileSizeUtil;
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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Upload_video3 extends BaseActivity implements View.OnClickListener {
    private Context context;
    private LinearLayout ll_title;
    private TextView title_name;
    private TextView title_rules;
    private LinearLayout llThumbnailHint;
    private ImageView ivThumbnail;
    private TextView tvPayType, tvPayDetail;
    private EditText etTitle, etDescribe;
    private TextView tvSureUpload;
    private View contentView;
    private GridView gvSelectFees;
    private CheckBox tv_check;
    private TextView tv_xieyi;

    private String videoPath;
    private File videoFile;
    private File imageFile;
    //用户统计
    private String type;
    private String videoName;
    private String videoDurationText = "";
    private PostRequest<String> postRequest;
    /*private String[] items;
    private String[] items2;*/
    private List<String> items = new ArrayList<>();
    private List<String> items2 = new ArrayList<>();
    private UploadFeesAdapter adapter;
    private PopupWindow popupWindow;
    private static int isMb = -1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            JSONArray dataArray = jsonObject.getJSONArray("data");
//                            items = new String[dataArray.length()];
//                            items2 = new String[dataArray.length()];
                            for (int i = 0; i < dataArray.length(); i++) {
                                String str = dataArray.getString(i);
                                if (Integer.parseInt(str) > 0) {
                                    items.add(str + "元");
                                    items2.add(str);
//                                    items[i] = str + "元";
//                                    items2[i] = str;
                                } else {
                                    items.add("免费");
                                    items2.add(str);
                                }
                            }
                        } else {
                            Toast.makeText(context, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
//                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            String dataString = jsonObject.getString("data");
                            showRulesPopupWindow(dataString);
                        } else {
                            Toast.makeText(context, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        MyProgressBarDialogTools.hide();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video3);
        context = this;
        type = getIntent().getStringExtra("type");
        //进入当前页获取一下用户ID，不然上传会闪退
        SharedPreferencesTools.getUid(context);
        findId();
        setFeesValue();
        initFeesGridview();
    }

    public void findId() {
        ll_title = findViewById(R.id.id_ll_activity_title_8);
        title_name = findViewById(R.id.activity_title_name);
        title_rules = findViewById(R.id.activity_title_upload_rules);
        llThumbnailHint = findViewById(R.id.id_ll_upload_select_video_hint);
        ivThumbnail = findViewById(R.id.id_iv_upload_select_video_thumbnail);
        tvPayType = findViewById(R.id.id_tv_upload_video_pay_type);
        tvPayDetail = findViewById(R.id.id_tv_upload_video_pay_detail);
        etTitle = findViewById(R.id.id_et_upload_video_title);
        etDescribe = findViewById(R.id.id_et_upload_video_describe);
        tvSureUpload = findViewById(R.id.id_tv_upload_video_sure);
        tv_check = findViewById(R.id.tv_check);
        tv_xieyi = findViewById(R.id.tv_xieyi);

        title_name.setText("上传视频");
        title_rules.setText("上传规则");
        title_rules.setVisibility(View.VISIBLE);
        title_rules.setOnClickListener(this);
        llThumbnailHint.setOnClickListener(this);
        ivThumbnail.setOnClickListener(this);
        tvPayDetail.setOnClickListener(this);
        tvSureUpload.setOnClickListener(this);
        tv_xieyi.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_ll_upload_select_video_hint:
                showVideo();
                break;
            case R.id.id_iv_upload_select_video_thumbnail:
                showVideo();
                break;
            case R.id.id_tv_upload_video_pay_detail:
                showPopupWindow();
                break;
            case R.id.id_tv_upload_video_sure:
                if(tv_check.isChecked()==true){
                    submit(view);
                }else {
                 toastShort("请勾选用户上传协议");
                }
                break;
            case R.id.activity_title_upload_rules:
                getUploadRules();
                break;
            case R.id.tv_xieyi:
                startActivity(UploadAgreementActivity.class);
                break;
        }
    }

    private void getUploadRules() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getUploadRules);
                    String result = HttpClientUtils.sendPost(context, URLConfig.ccmtvapp_uploadCase, obj.toString());
//                    Log.e(getLocalClassName(), "获取上传规则: "+result);
                    MyProgressBarDialogTools.hide();

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
        new Thread(runnable).start();
    }

    /**
     * name：选择视频
     * author：MrSong
     * data：2016/3/23 18:06
     */
    private void showVideo() {
        Intent intent = new Intent(context, ChooseVideoActivity.class);
        startActivityForResult(intent, 30);
    }

    private void initFeesGridview() {
        // 用于PopupWindow的View
        contentView = LayoutInflater.from(context).inflate(R.layout.popupwindow_upload_video_pay_type, null, false);
        gvSelectFees = contentView.findViewById(R.id.id_gv_popwindow_pay_type);

        adapter = new UploadFeesAdapter(context, items);
        gvSelectFees.setAdapter(adapter);

        gvSelectFees.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.e("Upload_video", "onItemClick: "+position);
                if (position != -1) {
//                    Log.e("Upload_video", "选择的费用"+items.get(position)+"items2"+items2.get(position));
                    if (items.get(position).contains("免费")) {
                        tvPayType.setText("免费");
                    } else {
                        tvPayType.setText("收费");
                    }
                    tvPayDetail.setText(items.get(position));
                    tvPayDetail.setTag(items2.get(position));
                    adapter.setSelected(position);
                    popupWindow.dismiss();
                }
            }
        });
    }

    /**
     * name：选择收费标准
     * author：bentley
     * data：2018/7/17 10:27
     */
    private void showPopupWindow() {
        adapter.notifyDataSetChanged();
        // 创建PopupWindow对象，其中：
        // 第一个参数是用于PopupWindow中的View，第二个参数是PopupWindow的宽度，
        // 第三个参数是PopupWindow的高度，第四个参数指定PopupWindow能否获得焦点
        popupWindow = new PopupWindow(contentView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);

        setBackgroundAlpha(0.5f);//设置屏幕透明度
        // 设置PopupWindow的背景
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        // 设置PopupWindow是否能响应外部点击事件
        popupWindow.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        popupWindow.setTouchable(true);
        // 显示PopupWindow，其中：
        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
//        window.showAsDropDown(contentView);
        popupWindow.showAtLocation(contentView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // popupWindow隐藏时恢复屏幕正常透明度
                setBackgroundAlpha(1.0f);
            }
        });
    }

    /**
     * name：显示上传规则
     * author：bentley
     * data：2018/7/17 10:27
     *
     * @param dataString
     */
    private void showRulesPopupWindow(String dataString) {
        // 创建PopupWindow对象，其中：
        // 第一个参数是用于PopupWindow中的View，第二个参数是PopupWindow的宽度，
        // 第三个参数是PopupWindow的高度，第四个参数指定PopupWindow能否获得焦点
        // 用于PopupWindow的View
        View contentViewRules = LayoutInflater.from(context).inflate(R.layout.popupwindow_upload_rules, null, false);
        final PopupWindow popupWindowRules = new PopupWindow(contentViewRules, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);
        TextView tvUploadRules = contentViewRules.findViewById(R.id.id_tv_upload_rules);
        TextView tvUploadRulesClose = contentViewRules.findViewById(R.id.id_tv_upload_rules_close);
        WebView webView = contentViewRules.findViewById(R.id.webview);
        webView.setBackgroundColor(2);
        webView.loadDataWithBaseURL(null, dataString, "text/html", "utf-8", null);

        tvUploadRules.setText(Html.fromHtml(dataString));
        tvUploadRulesClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindowRules.dismiss();
            }
        });
//        setBackgroundAlpha(0.5f);//设置屏幕透明度
        // 设置PopupWindow的背景
        popupWindowRules.setBackgroundDrawable(new ColorDrawable());
        // 设置PopupWindow是否能响应外部点击事件
        popupWindowRules.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        popupWindowRules.setTouchable(true);
        // 显示PopupWindow，其中：
        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
        popupWindowRules.showAsDropDown(ll_title, 0, 0);
//        popupWindowRules.showAtLocation(contentView, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 50);

        popupWindowRules.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // popupWindow隐藏时恢复屏幕正常透明度
//                setBackgroundAlpha(1.0f);
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 屏幕透明度0.0-1.0 1表示完全不透明
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) context).getWindow().setAttributes(lp);
    }

    /**
     * name：提交
     * author：MrSong
     * data：2016/3/29 15:24
     */
    public void submit(View view) {
        videoName = etTitle.getText().toString();
        String videoMsg = etDescribe.getText().toString();
//        Toast.makeText(context, tvPayDetail.getText().toString(), Toast.LENGTH_SHORT).show();
        if (tvPayDetail.getText().equals("选择收费标准")) {
            Toast.makeText(getApplicationContext(), "请选择收费类型", Toast.LENGTH_SHORT).show();
            return;
        }
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
        try {
            String fileSize=Formatter.formatFileSize(context,getFileAllSize(videoFile.getPath()));
            Pattern p = Pattern.compile("\\d+");
            Matcher m = p.matcher(fileSize);
            m.find();
            double videoSize = Double.valueOf(m.group());
            if (videoSize > 300 && !fileSize.contains("KB")||!fileSize.contains("B")) {
                toastShort("视频大小不能超过300MB");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (MyUploadVideoService.now_statu == MyUploadVideoService.upload_progress) {
            Toast.makeText(getApplicationContext(), "当前任务已正在上传，请等待上传完毕后在提交", Toast.LENGTH_SHORT).show();
            return;
        }
        //存储数据
        /*MyDbUtils.saveUploadVideoMsg(context, MyUploadVideoService.upload_wait + "", videoName, videoMsg, videoFile.getPath(), imageFile.getPath());
        //获取当前保存数据的ID
        int dbid = MyDbUtils.findUploadVideoMsg(context, videoName, videoMsg, videoFile.getPath(), imageFile.getPath());
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
        //bindService(new Intent(context, MyUploadVideoService.class), connection, BIND_AUTO_CREATE);

        /**
         * 尝试更改上传为okUpload
         */
        OkUploadVideo();
        Toast.makeText(context, "视频已提交，可在上传列表中查看", Toast.LENGTH_SHORT).show();
        Intent intent1 = new Intent(context, IsUpload3.class);
        intent1.putExtra("TAG", "Video");
        //intent.putExtra("type","home");
        startActivity(intent1);
        finish();
    }


    /**
     * 获取文件或者文件夹大小.
     */
    public static long getFileAllSize(String path) {
        File file = new File(path);
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] childrens = file.listFiles();
                long size = 0;
                for (File f : childrens) {
                    size += getFileAllSize(f.getPath());
                }
                return size;
            } else {
                return file.length();
            }
        } else {
            return 0;
        }
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
        uploadModel.setPayType(tvPayDetail.getText().toString());
        JSONObject object = new JSONObject();
        JSONObject obj = new JSONObject();

        try {
            object.put("title", etTitle.getText().toString());
            object.put("remark", etDescribe.getText().toString());
//                object.put("filePath", upload_videoPath.getText().toString());
            object.put("act", URLConfig.uploadVideo);
            object.put("uid", SharedPreferencesTools.getUid(context));
            object.put("mvmoney", tvPayDetail.getTag().toString());
            //收费
            object.put("upload_charge", tvPayDetail.getText().toString());
            object.put("vtime", videoDurationText);
            obj.put("userAccount", SharedPreferencesTools.getUserName(context));
            obj.put("password", SharedPreferencesTools.getPassword(context));
//            Log.e(getLocalClassName(), "OkUploadVideo: 上传参数："+object.toString());
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

    public void setFeesValue() {
        tvPayDetail.setTag("0");
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.upmoney);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, obj.toString());
//                    Log.e(getLocalClassName(), "收费数据: "+result);
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

    // 回调方法，从第二个页面回来的时候会执行这个方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == 0) {
                return;
            }
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

                    llThumbnailHint.setVisibility(View.GONE);
                    ivThumbnail.setVisibility(View.VISIBLE);

                    Bitmap bitmap = getVideoThumbnail(videoPath);
                    bitmapForFile(context, bitmap);
                    ivThumbnail.setImageBitmap(bitmap);
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
//            ivThumbnail.setImageResource(R.mipmap.img_default);
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        if ("home".equals(type)) {
            enterUrl = "http://www.ccmtv.cn";
        } else {
            enterUrl = "http://www.ccmtv.cn/Member/Index.html";
        }
        super.onPause();
    }

    public void back(View view) {
        finish();
    }

}
