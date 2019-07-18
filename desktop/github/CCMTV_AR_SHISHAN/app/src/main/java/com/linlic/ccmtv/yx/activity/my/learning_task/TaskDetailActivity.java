package com.linlic.ccmtv.yx.activity.my.learning_task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lidroid.xutils.http.RequestParams;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.UploadModel;
import com.linlic.ccmtv.yx.activity.my.learning_task.download.DownloadService;
import com.linlic.ccmtv.yx.activity.upload.new_upload.IsUpload3;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.Base64utils;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyListView;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.convert.Converter;
import com.lzy.okgo.db.UploadManager;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okserver.OkUpload;
import com.lzy.okserver.upload.UploadListener;
import com.lzy.okserver.upload.UploadTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

/**
 * Author:Nillaus
 * Date:2017年10月18日
 * Description:任务详情界面
 */
public class TaskDetailActivity extends BaseActivity {

    private Context context;
    private ImageView task_video_image;
    private TextView detail_title, detail_time, detail_finish, detail_ask, detail_description, task_detail_catename, task_contentname, task_catename;
    private TextView audit_score, task_status, exam_test, task_detail_download, is_start, task_upload_file, task_upload_video;
    private MyListView video_list;
    private LinearLayout task_ask, audit_score_layout;
    private LinearLayout ll_task_upload, task_detail_description_layout;
    BaseListAdapter baseListAdapter;
    private ScrollView myScrollView;
    private String taskid = "";
    private String cid = "";
    private String url = "";
    private String fileName = "";
    private String filePath = "";
    private RequestParams params = new RequestParams();
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private int pause = 0;
    private ProgressDialog progressDialog;
    private RecyclerView studyUploadRecyclerview;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        JSONObject dataObject = jsonObject.getJSONObject("data");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            //视频任务参数
                            JSONObject taskObject = dataObject.getJSONObject("task_info");
                            JSONArray videoArray = dataObject.getJSONArray("video_info");
                            audit_score_layout.setVisibility(View.GONE);
                            if (videoArray.length() > 0) {
                                for (int i = 0; i < videoArray.length(); i++) {
                                    JSONObject customJson = videoArray.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("aid", customJson.getString("aid"));
                                    map.put("task_video_title", customJson.getString("title"));
                                    map.put("task_image", customJson.getString("picurl"));
                                    map.put("task_vtime", customJson.getString("vtime"));
                                    map.put("is_show", customJson.getString("is_show"));
//                                    Log.e("is_show", customJson.getString("is_show"));
                                    map.put("task_status", customJson.getString("status"));
                                    data.add(map);
                                }
                            } else {
                                Toast.makeText(context, "暂无数据", Toast.LENGTH_SHORT).show();
                            }
                            //设置数据
                            detail_title.setText(Html.fromHtml(taskObject.getString("title")));
                            detail_time.setText(taskObject.getString("time"));
                            detail_description.setText("\u3000\u3000" + taskObject.getString("description"));
                            //detail_finish.setText(taskObject.getString("finish"));
                            detail_ask.setText("\u3000\u3000" + taskObject.getString("ask"));
                            is_start.setText(taskObject.getString("is_start"));
                        } else {
                            Toast.makeText(TaskDetailActivity.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        baseListAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        JSONObject dataObject = jsonObject.getJSONObject("data");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            //图文任务参数
                            if (jsonObject.has("task_grade") && jsonObject.getString("task_grade").length() > 0) {
                                audit_score_layout.setVisibility(View.VISIBLE);
                                if (jsonObject.getString("task_grade").equals("0")) {
                                    audit_score.setText("\u3000\u3000" + "暂无评分");
                                } else {
                                    audit_score.setText("\u3000\u3000" + jsonObject.getString("task_grade") + " 分");
                                }
                            } else {
                                audit_score_layout.setVisibility(View.GONE);
                            }
                            JSONObject taskObject = dataObject.getJSONObject("task_info");
                            JSONArray fileArrry = jsonObject.getJSONArray("file_list");
                            String is_show = jsonObject.getString("is_show");
                            if (is_show.equals("0")) {
                                //task_upload.setBackgroundResource(R.mipmap.learning_task04);
                                task_upload_file.setClickable(true);
                                task_upload_video.setClickable(true);
                            } else {
                                //task_upload.setBackgroundResource(R.mipmap.video_sign2);
                                task_upload_file.setBackgroundColor(Color.LTGRAY);
                                task_upload_file.setClickable(false);
                                task_upload_video.setClickable(false);
                                ll_task_upload.setBackgroundColor(Color.LTGRAY);
                            }
                            if (fileArrry.length() > 0) {
                                for (int i = 0; i < fileArrry.length(); i++) {
                                    JSONObject customJson = fileArrry.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("id", customJson.getString("id"));
                                    map.put("filename", customJson.getString("filename"));
                                    map.put("down_url", customJson.getString("down_url"));
                                    map.put("addtime", customJson.getString("addtime"));
                                    map.put("imgurl", customJson.getString("imgurl"));
                                    map.put("task_is_upload", "no");
                                    data.add(map);
                                }
                            } else {
//                                Toast.makeText(context, "暂无数据", Toast.LENGTH_SHORT).show();
                            }
                            //设置数据
                            detail_title.setText(Html.fromHtml(taskObject.getString("title")));
                            detail_time.setText(taskObject.getString("time"));
                            detail_ask.setText("\u3000\u3000" + taskObject.getString("description"));
                            is_start.setText(taskObject.getString("is_start"));
                        } else {
                            Toast.makeText(TaskDetailActivity.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        baseListAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        JSONObject dataObject = jsonObject.getJSONObject("data");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            //PPT任务参数
                            JSONObject taskObject = dataObject.getJSONObject("task_info");
                            JSONArray videoArray = dataObject.getJSONArray("video_info");
                            audit_score_layout.setVisibility(View.GONE);
                            if (videoArray.length() > 0) {
                                for (int i = 0; i < videoArray.length(); i++) {
                                    JSONObject customJson = videoArray.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("task_video_title", customJson.getString("title"));
                                    map.put("task_status", customJson.getString("status"));
                                    map.put("pptid", customJson.getString("id"));
                                    map.put("is_show", customJson.getString("is_show"));
                                    map.put("cid", customJson.getString("cid"));
//                                    Log.e("is_show", customJson.getString("is_show"));
                                    map.put("task_image", customJson.getString("picurl"));
                                    data.add(map);
                                }
                            } else {
                                Toast.makeText(context, "暂无数据", Toast.LENGTH_SHORT).show();
                            }
                            //设置数据
                            detail_title.setText(Html.fromHtml(taskObject.getString("title")));
                            detail_time.setText(taskObject.getString("time"));
                            detail_description.setText("\u3000\u3000" + taskObject.getString("description"));
                            //detail_finish.setText(taskObject.getString("finish"));
                            detail_ask.setText("\u3000\u3000" + taskObject.getString("ask"));
                            is_start.setText(taskObject.getString("is_start"));
                        } else {
                            Toast.makeText(TaskDetailActivity.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        baseListAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case 6:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        JSONObject dataObject = jsonObject.getJSONObject("data");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            //音频任务参数
                            JSONObject taskObject = dataObject.getJSONObject("task_info");
                            JSONArray videoArray = dataObject.getJSONArray("video_info");
                            audit_score_layout.setVisibility(View.GONE);
                            if (videoArray.length() > 0) {
                                for (int i = 0; i < videoArray.length(); i++) {
                                    JSONObject customJson = videoArray.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("task_video_title", customJson.getString("title"));
                                    map.put("task_status", customJson.getString("status"));
                                    map.put("aid", customJson.getString("id"));
                                    map.put("is_show", customJson.getString("is_show"));
//                                    Log.e("is_show", customJson.getString("is_show"));
                                    map.put("task_image", customJson.getString("picurl"));
                                    data.add(map);
                                }
                            } else {
                                Toast.makeText(context, "暂无数据", Toast.LENGTH_SHORT).show();
                            }
                            //设置数据
                            detail_title.setText(Html.fromHtml(taskObject.getString("title")));
                            detail_time.setText(taskObject.getString("time"));
                            detail_description.setText("\u3000\u3000" + taskObject.getString("description"));
                            //detail_finish.setText(taskObject.getString("finish"));
                            detail_ask.setText("\u3000\u3000" + taskObject.getString("ask"));
                            is_start.setText(taskObject.getString("is_start"));
                        } else {
                            Toast.makeText(TaskDetailActivity.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        baseListAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case 7:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            Toast.makeText(context, "成功删除", Toast.LENGTH_SHORT).show();
                            data.removeAll(data);
                            setValue();
                            /*if (pause > 0) {
                                data.removeAll(data);
                                setValue();
                            }*/
                        } else {
//                            Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
                            if (pause > 0) {
                                data.removeAll(data);
                                setValue();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 9:
                    studyUploadAdapter.updateData();
                    baseListAdapter.notifyDataSetChanged();
                    break;
                case 10:
                    data.removeAll(data);
                    setValue();
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }
    };
    private List<UploadTask<?>> uploadTaskList;
    private StudyUploadAdapter studyUploadAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_task_detail);
        context = this;
        initView();
        initData();
        setValue();
    }

    private void initView() {
        detail_title = (TextView) findViewById(R.id.task_detail_title);
        detail_time = (TextView) findViewById(R.id.task_detail_time);
        //detail_finish = (TextView) findViewById(R.id.task_detail_finish);
        detail_ask = (TextView) findViewById(R.id.task_detail_ask);
        detail_description = (TextView) findViewById(R.id.task_detail_description);
        task_video_image = (ImageView) findViewById(R.id.task_video_image);
        //task_detail_catename = (TextView) findViewById(R.id.task_detail_catename);
        video_list = (MyListView) findViewById(R.id.video_list);
        task_contentname = (TextView) findViewById(R.id.task_detail_contentname);
        ll_task_upload = (LinearLayout) findViewById(R.id.ll_task_detail_upload);
        task_upload_file = (TextView) findViewById(R.id.task_detail_upload_file);
        task_upload_video = (TextView) findViewById(R.id.task_detail_upload_video);
        task_ask = (LinearLayout) findViewById(R.id.task_detail_ask_all);
        task_detail_description_layout = (LinearLayout) findViewById(R.id.task_detail_description_layout);
        audit_score_layout = (LinearLayout) findViewById(R.id.audit_score_layout);
        task_status = (TextView) findViewById(R.id.task_status);
        audit_score = (TextView) findViewById(R.id.audit_score);
        exam_test = (TextView) findViewById(R.id.task_status_image);
        task_detail_download = (TextView) findViewById(R.id.task_detail_download);
        myScrollView = (ScrollView) findViewById(R.id.myScrollView);
        is_start = (TextView) findViewById(R.id.is_start);
        task_catename = (TextView) findViewById(R.id.task_catename);

        studyUploadRecyclerview = (RecyclerView) findViewById(R.id.id_uploading_recyclerView_study);
        studyUploadRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        studyUploadRecyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        studyUploadAdapter = new StudyUploadAdapter(TaskDetailActivity.this);
        studyUploadAdapter.updateData();
        studyUploadRecyclerview.setAdapter(studyUploadAdapter);
    }

    private void initData() {
        taskid = getIntent().getStringExtra("taskid");
        cid = getIntent().getStringExtra("cid");//1.视频 3.PPT 5.图文  6.音频
//        upload = getIntent().getStringExtra("upload");//1.隐藏ask 0.显示 我在下方直接判断，没用这个

        //加载数据
        switch (cid) {
            case "1":
                task_video_image.setImageResource(R.mipmap.ic_learning_task_list_video2);
                //task_detail_catename.setText("视频");
                ll_task_upload.setVisibility(View.GONE);
                task_contentname.setText("任务内容：");
                task_catename.setText("视频");
                task_ask.setVisibility(View.VISIBLE);
                audit_score_layout.setVisibility(View.GONE);
                task_detail_description_layout.setVisibility(View.VISIBLE);
                //detail_finish.setVisibility(View.VISIBLE);
                initItemVideo();
                break;
            case "5":
                task_video_image.setImageResource(R.mipmap.ic_learning_task_list_wrod2);
                //task_detail_catename.setText("PPT");
                ll_task_upload.setVisibility(View.GONE);
                task_contentname.setText("任务内容：");
                task_catename.setText("文档");
                task_ask.setVisibility(View.VISIBLE);
                //detail_finish.setVisibility(View.VISIBLE);
                audit_score_layout.setVisibility(View.GONE);
                task_detail_description_layout.setVisibility(View.VISIBLE);
                initItemAudio();
                break;

            case "6":
                task_video_image.setImageResource(R.mipmap.ic_learning_task_list_audio2);
                //task_detail_catename.setText("PPT");
                ll_task_upload.setVisibility(View.GONE);
                task_contentname.setText("任务内容：");
                task_catename.setText("音频");
                task_ask.setVisibility(View.VISIBLE);
                //detail_finish.setVisibility(View.VISIBLE);
                audit_score_layout.setVisibility(View.GONE);
                task_detail_description_layout.setVisibility(View.VISIBLE);
                initItemAudio();
                break;
            case "3":

                //task_upload.setBackgroundResource(R.mipmap.learning_task04);
                task_upload_file.setClickable(true);
                task_upload_video.setClickable(true);
                audit_score_layout.setVisibility(View.VISIBLE);
                task_detail_description_layout.setVisibility(View.GONE);
                task_video_image.setImageResource(R.mipmap.ic_learning_task_list_file2);
                //task_detail_catename.setText("图文");
                ll_task_upload.setVisibility(View.VISIBLE);
                task_contentname.setText("已上传文件：");
                task_catename.setText("图文");
                task_ask.setVisibility(View.VISIBLE);
                //detail_finish.setVisibility(View.GONE);
                initItemImage();
                break;
            default:
                break;
        }
    }

    /**
     * name: 点击跳转签到界面
     */
    private class learning_task_listListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
            HashMap<String, String> map = (HashMap<String, String>) arg0.getItemAtPosition(arg2);
            String is_start = ((TextView) findViewById(R.id.is_start)).getText().toString();

            String expired = getIntent().getStringExtra("expired");
            if (is_start.equals("0")) {
                switch (cid) {
                    case "1"://跳转到视频签到界面
                        final String aid = map.get("aid").toString();
                        Intent intent = new Intent(TaskDetailActivity.this, VideoSignActivity.class);
//                        Log.e("我要看aid", aid);
                        intent.putExtra("aid", aid);
                        intent.putExtra("tid", taskid);
                        intent.putExtra("expired", expired);
                        intent.putExtra("my_our_video", "videosign");
                        startActivity(intent);
                        break;
                    case "5"://跳转到PPT签到界面
                        String cid_type = ((TextView) view.findViewById(R.id.cid_type)).getText().toString();
                        Intent intent1 = null;
                        String id = map.get("pptid").toString();
//                        Log.e("我要看这个参数", cid_type);
                        switch (cid_type) {
                            case "5"://PPT
                                intent1 = new Intent(TaskDetailActivity.this, PPTSignActivity.class);
                                intent1.putExtra("tid", taskid);
                                intent1.putExtra("pptid", id);
                                intent1.putExtra("expired", expired);
                                startActivity(intent1);
                                break;
                            case "7"://PDF
                                intent1 = new Intent(TaskDetailActivity.this, PDFSignActivity.class);
                                intent1.putExtra("tid", taskid);
                                intent1.putExtra("pptid", id);
                                intent1.putExtra("expired", expired);
                                startActivity(intent1);
                                break;
                            case "8"://text
                                intent1 = new Intent(TaskDetailActivity.this, TXTSignActivity.class);
                                intent1.putExtra("tid", taskid);
                                intent1.putExtra("pptid", id);
                                intent1.putExtra("expired", expired);
                                startActivity(intent1);
                                break;
                            default:
                                break;
                        }

                        break;
                    case "3":
                        url = map.get("down_url").toString();
                        fileName = map.get("filename").toString();
                        break;
                    case "6":
                        final String aid2 = map.get("aid").toString();
                        Intent intent2 = new Intent(TaskDetailActivity.this, AudioSignActivity.class);
                        intent2.putExtra("aid", aid2);
                        intent2.putExtra("tid", taskid);
                        intent2.putExtra("expired", expired);
                        startActivity(intent2);
                        break;
                    default:
                        break;
                }
            }

        }
    }

    private void setValue() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getTaskInfo);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("tid", taskid);
                    String result = HttpClientUtils.sendPost(context, URLConfig.Learning_task, obj.toString());
//                    Log.e("看看任务详情数据", result);

                    Message message = new Message();
                    switch (cid) {
                        case "1"://视频任务
                            message.what = 1;
                            break;
                        case "3"://图文任务
                            message.what = 3;
                            break;
                        case "5"://PPT任务
                            message.what = 5;
                            break;
                        case "6"://音频任务
                            message.what = 6;
                            break;
                    }
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

    private void getUploadingTask() {
        uploadTaskList = new ArrayList<>();
        List<UploadTask<?>> uploadTasks = OkUpload.restore(UploadManager.getInstance().getUploading());
        for (UploadTask<?> uploadTask : uploadTasks) {
            String tag = uploadTask.progress.tag;
            if (tag.equals("image_text_task")) {
                uploadTaskList.add(uploadTask);
            }
        }

        handler.sendEmptyMessage(9);
    }

    public void back(View view) {
        finish();
    }

    @Override
    protected void onResume() {
        if (pause > 0) {
            data.removeAll(data);
            setValue();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yy.ccmtv.cn/Task/tid=" + taskid;
        pause++;
        super.onPause();
    }

    public void upload(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //系统调用Action属性
        intent.setType("*/*");
        //设置文件类型
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // 添加Category属性
        try {
            startActivityForResult(intent, 1);
        } catch (Exception e) {
            Toast.makeText(this, "没有正确打开文件管理器", Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadVideo(View view) {
        /*Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //系统调用Action属性
        intent.setType("video*//*");
        //设置文件类型
        intent.addCategory(Intent.CATEGORY_OPENABLE);*/

        Intent intent = new Intent(Intent.ACTION_PICK);

        intent.setData(MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        // 添加Category属性
        try {
            startActivityForResult(intent, 1);
        } catch (Exception e) {
            Toast.makeText(this, "没有正确打开文件管理器", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
//                Uri uri = data.getData();
                //获得选中文件的路径
//                filePath = uri.getPath().toString();/**/
                //获得选中文件的名称


                Uri uri = data.getData();
                if ("file".equalsIgnoreCase(uri.getScheme())) {//使用第三方应用打开
                    filePath = uri.getPath();

                    //return;
                }
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                    filePath = getPath(this, uri);

                } else {//4.4以下下系统调用方法
                    filePath = getRealPathFromURI(uri);

                }
                fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
                //params.addBodyParameter("upfile", new File(filePath));
//                params.addBodyParameter("upfile", file);
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject();
                            object.put("uid", SharedPreferencesTools.getUid(context));
                            object.put("tid", taskid);

                            //startUploadFile(params, object.toString());
                            startUploadFile(new File(filePath));
                        } catch (Exception e) {
                            e.printStackTrace();
                            handler.sendEmptyMessage(500);
                        }
                    }
                };
                new Thread(runnable).start();

//                Log.e("文件路径：", filePath);
//                Log.e("文件名称：", filePath.substring(filePath.lastIndexOf("/") + 1));
            }
        }
    }

    public void startUploadFile(File file) {
//        Log.e(getLocalClassName(), "startUploadFile: filePath:"+file.getAbsolutePath());
        PostRequest postRequest = OkGo.<String>post(URLConfig.Learning_task1)
                .params("upfile", file)
                .params("tid", taskid)
                .params("uid", SharedPreferencesTools.getUid(context))
                .converter(new Converter<String>() {
                    @Override
                    public String convertResponse(Response response) throws Throwable {
                        try {
                            String responseString = Base64utils.getFromBase64(Base64utils.getFromBase64(response.body().string()));
//                            Log.e("上传图文返回", "convertResponse: " + responseString);
                        } catch (IOException e) {
                            e.printStackTrace();
                            handler.sendEmptyMessage(500);
                        }
                        return null;
                    }
                });

        UploadModel uploadModel = new UploadModel();
        uploadModel.setName(fileName);
        uploadModel.setUrl(filePath);

        UploadTask taskNew = OkUpload.request("image_text_task-" + filePath, postRequest)
                .priority(35)
                .extra1(uploadModel)
                .extra2(fileName)
                .save()
                .register(new StudyUploadListener<String>());
        taskNew.start();


        handler.sendEmptyMessage(9);
        //getUploadingTask();
        /*nf.setMaximumFractionDigits(1);
        HttpUtils httpUtils = new HttpUtils();
        //设置线程数
//                httpUtils.configRequestThreadPoolSize(1);
//        params.addBodyParameter("uid", Base64utils.getBase64(SharedPreferencesTools.getUid(context)));
        params.addBodyParameter("uid", SharedPreferencesTools.getUid(context));
//        params.addBodyParameter("tid", Base64utils.getBase64(Base64utils.getBase64(tid)));
        params.addBodyParameter("tid", taskid);
        //  params.addBodyParameter("datacheck", Base64utils.getBase64(Base64utils.getBase64(datacheck)));

        httpUtils.send(HttpRequest.HttpMethod.POST, URLConfig.Learning_task1, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = Base64utils.getFromBase64(Base64utils.getFromBase64(responseInfo.result));
                Log.e("上传成功：", responseInfo.result);
                Log.e("解密后111：", result);
                try {
                    Log.e("Message：", new JSONObject(result).getString("errorMessage"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MyNotificationCase(context, "上传任务已完成", "上传完成", "当前进度:100%", false);
                Toast.makeText(context, "上传成功", Toast.LENGTH_SHORT).show();
                if (pause > 0) {
                    data.removeAll(data);
                    setValue();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("上传失败：", s);
                Log.e("错误：", e.toString());
                //         dialog.dismiss();
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
//                Toast.makeText(context,"正在上传，完成后将退出",Toast.LENGTH_SHORT).show();
                double progress=(current/1024.0/1024.0)/(total/1024.0/1024.0);
                Log.e("TaskDetailActivity", "onLoading: 当前进度:" + nf.format(progress));
                MyNotificationCase(context, "上传任务已添加", "正在上传", "当前进度:" + nf.format(progress), false);
            }
        });*/
    }

    private NumberFormat nf = NumberFormat.getPercentInstance();

    public void MyNotificationCase(Context context, String tan, String title, String cont, boolean isProgress) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, IsUpload3.class);
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
    }

    //加载系统菜单
    //如果存在，就显示出来
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    //加载视频任务item数据
    public void initItemVideo() {
        baseListAdapter = new BaseListAdapter(video_list, data, R.layout.item_task_detail_video_ppt) {
            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                String is_show = ((Map) item).get("is_show").toString();
                helper.setText(R.id.cid_type, ((Map) item).get("cid") + "");
                if (is_show.equals("0")) {
                    helper.setText(R.id.task_aid, ((Map) item).get("aid") + "");
                    helper.setText(R.id.task_video_title, ((Map) item).get("task_video_title") + "");
                    helper.setImageBitmap(R.id.video_image, ((Map) item).get("task_image") + "");
                    helper.setText(R.id.task_status, ((Map) item).get("task_status") + "");
                    helper.setBackground_Image(R.id.task_status_image, R.mipmap.learning_task04);
                    helper.setText(R.id.task_id, ((Map) item).get("pptid") + "");
                } else if (is_show.equals("1")) {
                    helper.setText(R.id.task_aid, ((Map) item).get("aid") + "");
                    helper.setText(R.id.task_video_title, ((Map) item).get("task_video_title") + "");
                    helper.setImageBitmap(R.id.video_image, ((Map) item).get("task_image") + "");
                    helper.setText(R.id.task_status, ((Map) item).get("task_status") + "");
                    helper.setBackground_Image(R.id.task_status_image, R.mipmap.video_sign2);
                    helper.setText(R.id.task_id, ((Map) item).get("pptid") + "");
                }
//                Log.e("is_show", ((Map) item).get("is_show") + "");
                if (((Map) item).get("task_status").toString().contains("未开始")) {
                    helper.setText(R.id.task_status_image, "去完成");
                } else if (((Map) item).get("task_status").toString().contains("合格")) {
                    helper.setText(R.id.task_status_image, "再做一次");
                } else if (((Map) item).get("task_status").toString().contains("进行中")) {
                    helper.setText(R.id.task_status_image, "继续任务");
                } else if (((Map) item).get("task_status").toString().contains("已完成")) {
                    helper.setText(R.id.task_status_image, "已完成");
                }
            }
        };

        myScrollView.smoothScrollTo(0, 20);

        video_list.setAdapter(baseListAdapter);
        // listview点击事件
        video_list.setOnItemClickListener(new learning_task_listListener());
    }

    //加载音频、TXT任务item数据
    public void initItemAudio() {
        baseListAdapter = new BaseListAdapter(video_list, data, R.layout.item_task_detail_audio) {
            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                String is_show = ((Map) item).get("is_show").toString();
                if (cid.equals("6")) {
                    helper.setImageResource(R.id.video_image_small, R.mipmap.ic_learning_task_audio_item);
                    helper.setVisibility(R.id.video_image, View.GONE);
                    helper.setVisibility(R.id.video_image_small, View.VISIBLE);
                } else {
                    String cidType = ((Map) item).get("cid") + "";
                    if (cidType.equals("7")) {//PDF
                        helper.setImageResource(R.id.video_image_small, R.mipmap.learning_task05);
                        helper.setVisibility(R.id.video_image, View.GONE);
                        helper.setVisibility(R.id.video_image_small, View.VISIBLE);
                    } else if (cidType.equals("8")) {//TXT
                        helper.setImageResource(R.id.video_image_small, R.mipmap.ic_learning_task_txt_item);
                        helper.setVisibility(R.id.video_image, View.GONE);
                        helper.setVisibility(R.id.video_image_small, View.VISIBLE);
                    } else {
                        helper.setVisibility(R.id.video_image_small, View.GONE);
                        helper.setVisibility(R.id.video_image, View.VISIBLE);
                        helper.setImageBitmap(R.id.video_image, ((Map) item).get("task_image") + "");
                    }
                }
                helper.setText(R.id.cid_type, ((Map) item).get("cid") + "");

                if (is_show.equals("0")) {
                    helper.setText(R.id.task_aid, ((Map) item).get("aid") + "");
                    helper.setText(R.id.task_video_title, ((Map) item).get("task_video_title") + "");
                    helper.setText(R.id.task_status, ((Map) item).get("task_status") + "");
                    helper.setBackground_Image(R.id.task_status_image, R.mipmap.learning_task04);
                    helper.setText(R.id.task_id, ((Map) item).get("pptid") + "");
                } else if (is_show.equals("1")) {
                    helper.setText(R.id.task_aid, ((Map) item).get("aid") + "");
                    helper.setText(R.id.task_video_title, ((Map) item).get("task_video_title") + "");
                    helper.setText(R.id.task_status, ((Map) item).get("task_status") + "");
                    helper.setBackground_Image(R.id.task_status_image, R.mipmap.video_sign2);
                    helper.setText(R.id.task_id, ((Map) item).get("pptid") + "");
                }
//                Log.e("is_show", ((Map) item).get("is_show") + "");
                if (((Map) item).get("task_status").equals("未开始")) {
                    helper.setText(R.id.task_status_image, "去完成");
                } else if (((Map) item).get("task_status").equals("合格")) {
                    helper.setText(R.id.task_status_image, "再做一次");
                } else if (((Map) item).get("task_status").equals("进行中")) {
                    helper.setText(R.id.task_status_image, "继续任务");
                } else if (((Map) item).get("task_status").equals("已完成")) {
                    helper.setText(R.id.task_status_image, "已完成");
                }
            }
        };

        myScrollView.smoothScrollTo(0, 20);

        video_list.setAdapter(baseListAdapter);
        // listview点击事件
        video_list.setOnItemClickListener(new learning_task_listListener());
    }

    //加载图文任务item数据
    public void initItemImage() {
        baseListAdapter = new BaseListAdapter(video_list, data, R.layout.item_task_detail_image_text) {
            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                String expired = getIntent().getStringExtra("expired");
                if (((Map) item).get("imgurl").toString().isEmpty() || ((Map) item).get("imgurl").toString() == null) {
                    helper.setVisibility(R.id.layout1, View.GONE);
                    helper.setVisibility(R.id.layout2, View.VISIBLE);

                    helper.setText(R.id.task_image_title_2, ((Map) item).get("filename") + "");
                    helper.setText(R.id.task_uploadtime_2, ((Map) item).get("addtime") + "");
                    if (expired.equals("1")) {
                        helper.setVisibility(R.id.task_detail_delete_2, View.GONE);
                    } else {
                        helper.setVisibility(R.id.task_detail_delete_2, View.VISIBLE);
                    }
                } else {
                    helper.setVisibility(R.id.layout1, View.VISIBLE);
                    helper.setVisibility(R.id.layout2, View.GONE);
                    RequestOptions options = new RequestOptions().error(R.mipmap.img_default);
                    Glide.with(context)
                            .load(((Map) item).get("imgurl").toString())
                            .apply(options)
                            .into((ImageView) helper.getView(R.id.task_image_text1));
                    helper.setText(R.id.task_image_title, ((Map) item).get("filename") + "");
                    helper.setText(R.id.task_uploadtime, ((Map) item).get("addtime") + "");
                    if (expired.equals("1")) {
                        helper.setVisibility(R.id.task_detail_delete, View.GONE);
                    } else {
                        helper.setVisibility(R.id.task_detail_delete, View.VISIBLE);
                    }
                }
                helper.setText(R.id.cid_type, ((Map) item).get("cid") + "");
                helper.setText(R.id.task_image_id, ((Map) item).get("id") + "");
                helper.setText(R.id.task_image_download_url, ((Map) item).get("down_url") + "");
//                Log.e("is_show", ((Map) item).get("is_show") + "");
            }
        };

        myScrollView.smoothScrollTo(0, 20);

        video_list.setAdapter(baseListAdapter);
        // listview点击事件
        video_list.setOnItemClickListener(new learning_task_listListener());
    }

    public void delete(View viwe) {
        TextView textView = (TextView) findViewById(R.id.task_image_id);
        final String id = textView.getText().toString();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.userDelTaskFile);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("tid", taskid);
                    obj.put("id", id);
                    String result = HttpClientUtils.sendPost(context, URLConfig.Learning_task, obj.toString());
//                    Log.e("看看删除数据", result);

                    Message message = new Message();
                    message.what = 7;
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

    //创建文件保存目录
    boolean isFolderExists(String strFolder) {
        File file = new File(strFolder);

        if (!file.exists()) {
            if (file.mkdir()) {
                return true;
            } else
                return false;
        }
        return true;
    }

    public void download(View view) {
        String filePath = URLConfig.ccmtvapp_basesdcardpath;
        TextView textView = (TextView) ((RelativeLayout) view.getParent().getParent().getParent().getParent()).findViewById(R.id.task_image_download_url);
        TextView textView1 = (TextView) ((RelativeLayout) view.getParent().getParent().getParent().getParent()).findViewById(R.id.task_image_title);
//        Log.e("下载地址", textView.getText().toString());
        url = textView.getText().toString().trim();
        fileName = textView1.getText().toString();
        try {
            DownloadService.getDownloadManager().startDownload(
                    url, fileName,
                    "/sdcard/ccmtvCache/" + fileName, true, false, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void download2(View view) {
        String filePath = URLConfig.ccmtvapp_basesdcardpath;
        TextView textView = (TextView) ((RelativeLayout) view.getParent().getParent().getParent().getParent()).findViewById(R.id.task_image_download_url);
        TextView textView1 = (TextView) ((RelativeLayout) view.getParent().getParent().getParent().getParent()).findViewById(R.id.task_image_title_2);
//        Log.e("下载地址", textView.getText().toString());
        url = textView.getText().toString().trim();
        fileName = textView1.getText().toString();
        try {
            DownloadService.getDownloadManager().startDownload(
                    url, fileName,
                    "/sdcard/ccmtvCache/" + fileName, true, false, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (null != cursor && cursor.moveToFirst()) {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }

    /**
     * 79      * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     * 80
     */
    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * 140      * Get the value of the data column for this Uri. This is useful for
     * 141      * MediaStore Uris, and other file-based ContentProviders.
     * 142      *
     * 143      * @param context       The context.
     * 144      * @param uri           The Uri to query.
     * 145      * @param selection     (Optional) Filter used in the query.
     * 146      * @param selectionArgs (Optional) Selection arguments used in the query.
     * 147      * @return The value of the _data column, which is typically a file path.
     * 148
     */
    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private class StudyUploadListener<T> extends UploadListener<String> {
        public StudyUploadListener() {
            super("StudyUploadListener");
        }

        @Override
        public void onStart(Progress progress) {
//            Log.e(getLocalClassName(), "onStart: " + progress.filePath);
        }

        @Override
        public void onProgress(Progress progress) {
//            Log.e(getLocalClassName(), "onProgress: " + progress.fraction);
        }

        @Override
        public void onError(Progress progress) {

        }

        @Override
        public void onFinish(String s, Progress progress) {
//            Log.e(getLocalClassName(), "onFinish: " + progress.filePath);
            handler.sendEmptyMessage(9);
            handler.sendEmptyMessage(10);
        }

        @Override
        public void onRemove(Progress progress) {

        }
    }
}
