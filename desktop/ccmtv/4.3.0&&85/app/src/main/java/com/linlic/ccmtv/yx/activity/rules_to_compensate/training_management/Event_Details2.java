package com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Courseware;
import com.linlic.ccmtv.yx.activity.entity.Resident;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.evaluation.Evaluation_department_list;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.evaluation.The_evaluation_of_teaching_list;
import com.linlic.ccmtv.yx.activity.upload.PicViewerActivity;
import com.linlic.ccmtv.yx.activity.upload.VideoPlayerActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.Base64utils;
import com.linlic.ccmtv.yx.utils.DateUtil;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyGridView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.linlic.ccmtv.yx.utils.GetPathFromUri4kitkat.getDataColumn;
import static com.linlic.ccmtv.yx.utils.GetPathFromUri4kitkat.isDownloadsDocument;
import static com.linlic.ccmtv.yx.utils.GetPathFromUri4kitkat.isExternalStorageDocument;
import static com.linlic.ccmtv.yx.utils.GetPathFromUri4kitkat.isMediaDocument;

/**
 * Created by Administrator on 2018/8/29. 活动详情
 */

public class Event_Details2 extends BaseActivity {
    private Context context;
    @Bind(R.id.activity_name)
    TextView activity_name;//活动名
    @Bind(R.id.the_activity_type)
    TextView the_activity_type;//活动类型
    @Bind(R.id.start_time)
    TextView start_time;//开始时间
    @Bind(R.id.end_time)
    TextView end_time;//结束时间
    @Bind(R.id.place)
    TextView place;//地点
    @Bind(R.id.approval_status)
    TextView approval_status;//状态
    @Bind(R.id.reason_for_review)
    TextView reason_for_review;//审核信息
    @Bind(R.id.approval_img)
    ImageView approval_img;//状态图片

    @Bind(R.id.accompanying_notes_layout)
    LinearLayout accompanying_notes_layout;//随堂笔记 容器
    @Bind(R.id.accompanying_notes_grid)
    MyGridView accompanying_notes_grid;//随堂笔记
    @Bind(R.id.lecturer_layout)
    LinearLayout lecturer_layout;//讲师 模块 容器
    @Bind(R.id.leave)
    LinearLayout leave;//请假
    @Bind(R.id.evaluate)
    LinearLayout evaluate;//评价
    @Bind(R.id.evaluate_text)
    TextView evaluate_text;//评价
    @Bind(R.id.submit_layout3)
    LinearLayout submit_layout3;//请假模块 容器
    @Bind(R.id.submit_layout3_text)
    EditText submit_layout3_text;//请假理由
    @Bind(R.id.close_text)
    TextView close_text;//取消请假
    @Bind(R.id.submit_text)
    TextView submit_text;//提交请假
    @Bind(R.id.lecturer_num)
    TextView lecturer_num;//讲师 人数字段
    @Bind(R.id.teaching_num)
    TextView teaching_num;//带教 人数字段
    @Bind(R.id.teaching_layout)
    LinearLayout teaching_layout;//带教 模块  容器
    // 录制视频begin
    @Bind(R.id.ll_video_cell)
    LinearLayout ll_video_cell;
    @Bind(R.id.video_tv_select_file)
    TextView tv_video_select;
    @Bind(R.id.video_grid)
    MyGridView video_grid;
    // 录制视频end

    public  List<Resident> teaching_data = new ArrayList<>();//带教 数据
    List<Courseware> coursewares = new ArrayList<>();//课件
    List<Courseware> accompanying_notes_datas = new ArrayList<>();//随堂笔记 data
    List<Courseware> videoList = new ArrayList<>();//课程录制
    Map<String, Integer> accompanying_notes_pos = new HashMap<>();
    Map<String, Integer> videos_pos = new HashMap<>();
    private List<String> allKeshi_list = new ArrayList<>(), allStatus_list = new ArrayList<>();//活动类型数据
    JSONObject result, data;
    private String fid = "";
    private String id = "";
    private String http = "";
    private String score = "";
    private Boolean accompanying_notes_isShow = true;//是否显示随堂笔记模块
    private String ing = "";//二维码可点开 不可点开 状态
    private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
    private BaseListAdapter baseListAdapteraccompanying_notes;
    private BaseListAdapter baseListAdapterVideo; // 课程录制的
    List<Resident> lecturer_data = new ArrayList<>();//讲师 数据
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                JSONObject dateJson = dataJson.getJSONObject("dataList");
                                activity_name.setText(dateJson.getString("name"));
                                the_activity_type.setText(dateJson.getString("type_name"));
                                start_time.setText(dateJson.getString("add_time"));
                                end_time.setText(dateJson.getString("end_time"));
                                place.setText(dateJson.getString("place"));
                                http = dateJson.getString("http");
                                ing = dateJson.getString("ing");
                                score = dateJson.has("score")?dateJson.getString("score"):"";
                                if(score.trim().length()>0){
                                    evaluate_text.setText("已评价");
                                }else{
                                    evaluate_text.setText("评价");
                                }
                                //判断 显示学员可以上传的按钮 字段是否存在
                                if(dateJson.has("isShowUpload")){
                                    accompanying_notes_isShow = dateJson.getString("isShowUpload").equals("1")?true:false;//获取是否显示随堂笔记模块
                                }else{
                                    accompanying_notes_isShow = false;
                                }
                                if(accompanying_notes_isShow){//显示随堂笔记
                                    accompanying_notes_layout.setVisibility(View.VISIBLE);
                                }else{//隐藏随堂笔记
                                    accompanying_notes_layout.setVisibility(View.GONE);
                                }
                                if(dateJson.getString("status_sub").equals("1") ){
                                   leave.setVisibility(View.VISIBLE);
                                }else{
                                    leave.setVisibility(View.GONE);
                                }
                                if(!dateJson.getString("status_sub").equals("1")  ){
                                    if(dateJson.getString("sign").equals("2")){
                                        evaluate.setVisibility(View.VISIBLE);
                                    }else{
                                        evaluate.setVisibility(View.GONE);
                                    }
                                }else{
                                    evaluate.setVisibility(View.GONE);
                                }
                                //4请假中   5请假失败  reason_for_review
                                switch (dateJson.getString("sign")){
                                    case "3":
                                        reason_for_review.setText("请假理由："+dateJson.getString("leave_msg"));
                                        reason_for_review.setVisibility(View.VISIBLE);
                                        approval_status.setText("已请假");
                                        approval_img.setImageResource(R.mipmap.training_41);
                                        leave.setVisibility(View.GONE);
                                        break;
                                    case "1":
                                        approval_status.setText("未签到");
                                        reason_for_review.setVisibility(View.GONE);
                                        if (dateJson.getString("status_sub").equals("2") || dateJson.getString("status_sub").equals("1")) {
                                            approval_img.setImageResource(R.mipmap.training_27);
                                        } else {
                                            approval_img.setImageResource(R.mipmap.training_28);
                                        }
                                        break;
                                    case "4":
                                        leave.setVisibility(View.GONE);
                                        reason_for_review.setVisibility(View.VISIBLE);
                                        reason_for_review.setText("请假审批中\n请假理由："+dateJson.getString("leave_msg"));
                                        approval_status.setText("未签到");
                                        if (dateJson.getString("status_sub").equals("2") || dateJson.getString("status_sub").equals("1")) {
                                            approval_img.setImageResource(R.mipmap.training_27);
                                        } else {
                                            approval_img.setImageResource(R.mipmap.training_28);

                                        }
                                        break;
                                    case "5":
                                        leave.setVisibility(View.GONE);
                                        reason_for_review.setVisibility(View.VISIBLE);
                                        reason_for_review.setText("请假失败\n请假理由："+dateJson.getString("leave_msg"));//
                                        approval_status.setText("未签到");
                                        if (dateJson.getString("status_sub").equals("2") || dateJson.getString("status_sub").equals("1")) {
                                            approval_img.setImageResource(R.mipmap.training_27);
                                        } else {
                                            approval_img.setImageResource(R.mipmap.training_28);

                                        }
                                        break;
                                    case "2":
                                        reason_for_review.setVisibility(View.GONE);
                                        approval_status.setText("已签到");
                                        approval_img.setImageResource(R.mipmap.training_26);
                                        break;
                                }

                                // 如果没有视频，整个cell 就隐藏掉
                                ll_video_cell.setVisibility(dateJson.getJSONArray("_recorder").length() == 0 ? View.GONE : View.VISIBLE);
                                // 录制视频
                                for (int i = 0; i < dateJson.getJSONArray("_recorder").length(); i++) {
                                    JSONObject videoJson = dateJson.getJSONArray("_recorder").getJSONObject(i);
                                    Courseware courseware_bean = new Courseware();
                                    courseware_bean.setFile_path(videoJson.getString("url"));
                                    courseware_bean.setFile_name(videoJson.getString("url_name"));
                                    courseware_bean.setIs_upload(true);
                                    courseware_bean.setId("" + (i + 1));
                                    videoList.add(courseware_bean);
                                    videos_pos.put(courseware_bean.getId(), videoList.size() - 1);
                                }
                                baseListAdapterVideo.notifyDataSetChanged();


                                coursewares.clear();
                                for (int i = 0; i < dateJson.getJSONArray("kejian").length(); i++) {
                                    JSONObject kejianJson = dateJson.getJSONArray("kejian").getJSONObject(i);
                                    Courseware courseware_bean = new Courseware();
                                    courseware_bean.setFile_name(kejianJson.getString("url_name"));
                                    courseware_bean.setFile_path( kejianJson.getString("url"));
                                    courseware_bean.setIs_upload(true);
                                    courseware_bean.setId(i+"");
                                    coursewares.add(courseware_bean);
                                }
                                accompanying_notes_datas.clear();
                                accompanying_notes_pos.clear();
                                if(dateJson.has("files")){
                                    for (int i = 0; i < dateJson.getJSONArray("files").length(); i++) {
                                        JSONObject kejianJson = dateJson.getJSONArray("files").getJSONObject(i);
                                        Courseware courseware_bean = new Courseware();
                                        courseware_bean.setFile_name(kejianJson.getString("url_name"));
                                        courseware_bean.setFile_path(kejianJson.getString("url"));
                                        courseware_bean.setIs_upload(true);
                                        courseware_bean.setId(i+"");
                                        accompanying_notes_datas.add(courseware_bean);
                                        accompanying_notes_pos.put(courseware_bean.getId(), accompanying_notes_datas.size() - 1);
                                    }
                                    baseListAdapteraccompanying_notes.notifyDataSetChanged();
                                }
                                lecturer_data.clear();
                                //讲师
                                if(dateJson.getString("is_speaker").equals("1") && dateJson.has("speaker")){
                                    for (int i = 0; i < dateJson.getJSONArray("speaker").length(); i++) {
                                        JSONObject dataJson1 = dateJson.getJSONArray("speaker").getJSONObject(i);
                                        Resident resident = new Resident();
                                        resident.setId(dataJson1.getString("uid"));
                                        resident.setIs_select(false);
                                        resident.setName(dataJson1.getString("realname"));
                                        resident.setUsername(dataJson1.getString("username"));
                                        lecturer_data.add(resident);
                                    }
                                    if(lecturer_data.size()>0){
                                        lecturer_layout.setVisibility(View.VISIBLE);
                                    }else{
                                        lecturer_layout.setVisibility(View.GONE);
                                    }
                                        lecturer_num.setText(lecturer_data.size()+"人");
                                }else{
                                    lecturer_layout.setVisibility(View.GONE);
                                }
                                teaching_data.clear();
                                //带教
                                if(dateJson.has("is_teachers") && dateJson.getString("is_teachers").equals("1")  ){
                                    if( dateJson.has("teachers")){
                                        for (int i = 0; i < dateJson.getJSONArray("teachers").length(); i++) {
                                            JSONObject dataJson1 = dateJson.getJSONArray("teachers").getJSONObject(i);
                                            Resident resident = new Resident();
                                            resident.setId(dataJson1.getString("uid"));
                                            resident.setIs_select(false);
                                            resident.setName(dataJson1.getString("realname"));
                                            resident.setUsername(dataJson1.getString("username"));
                                            teaching_data.add(resident);
                                        }
                                        teaching_num.setText(teaching_data.size()+"人");
                                        teaching_layout.setVisibility(View.VISIBLE);
                                    }

                                }else {
                                    teaching_layout.setVisibility(View.GONE);
                                }

                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
//
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        MyProgressBarDialogTools.hide();
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                submit_layout3.setVisibility(View.GONE);
                                getUrlRulest();
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
//
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                for(int i = 0; i < accompanying_notes_datas.size() ; i++){
                                    if (dataJson.getString("dataList").equals(accompanying_notes_datas.get(i).getFile_path())) {
                                        accompanying_notes_datas.remove(accompanying_notes_datas.get(i));
                                    }
                                }

                                baseListAdapteraccompanying_notes.notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
//
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    Toast.makeText(context
                            , R.string.post_hint1,
                            Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
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
        setContentView(R.layout.event_details2);
        context = this;
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id");
        fid = getIntent().getStringExtra("fid");
        initView();
        getUrlRulest();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/activities/index.html";
        super.onPause();
    }


    public void initView() {
        evaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Training_Evaluation_in_detail.class);
                intent.putExtra("detailid",id);
                intent.putExtra("fid",fid);
                intent.putExtra("is_teaching","0");
                startActivity(intent);
            }
        });
        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit_layout3.setVisibility(View.VISIBLE);
            }
        });
        submit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(submit_layout3_text.getText().toString().trim().length()>0){
                    MyProgressBarDialogTools.show(context);
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject obj = new JSONObject();
                                obj.put("act", URLConfig.activitiesAddLeave);
                                obj.put("fid", fid);
                                obj.put("id", id);
                                obj.put("leave_msg", submit_layout3_text.getText().toString());
                                obj.put("uid", SharedPreferencesTools.getUidONnull(context));

                                String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                                LogUtil.e("请假", result);
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
                }else{
                    Toast.makeText(getApplicationContext(), "请填写请假理由！", Toast.LENGTH_SHORT).show();
                }

            }
        });
        close_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit_layout3.setVisibility(View.GONE);
            }
        });

        the_activity_type.setTextColor(Color.BLACK);

        //------教学活动录制 begin--------//
        baseListAdapterVideo = new BaseListAdapter(video_grid, videoList, R.layout.item_detail_video) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                final Courseware map = (Courseware) item;
                helper.setText(R.id._item_text, map.getFile_name());
                helper.setTag(R.id._item_img, map.getFile_path());
                if (map.is_upload()) {
                    helper.setVisibility(R.id._item_img, View.VISIBLE);
                    helper.setVisibility(R.id._item_progressbar, View.GONE);
                } else {
                    helper.setVisibility(R.id._item_img, View.GONE);
                    helper.setVisibility(R.id._item_progressbar, View.VISIBLE);
                }

                helper.setVisibility(R.id._item_img, View.GONE);

                helper.setprogressbar(R.id._item_progressbar, map.getCurrent_progress());

                helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, DetailVideoPlayActivity.class);
                        intent.putExtra(DetailVideoPlayActivity.VIDEO_PATH_KEY, map.getFile_path());
                        String substring = "";
                        if (http.equals("/")) {
                            substring = http.substring(0, http.length() - 1);
                        } else {
                            substring = http;
                        }
                        intent.putExtra(DetailVideoPlayActivity.VIDEO_HTTP_KEY, substring);
                       //  startActivity(intent);

                        Intent intent2 = new Intent(context, VideoPlayerActivity.class);
                        intent2.putExtra("videoTitle", "");//传的videoTitle不带后缀
                        intent2.putExtra("videoPath", substring+ map.getFile_path());
                        intent2.putExtra("last_look_time", "");
                        intent2.putExtra("aid", "");
                        intent2.putExtra("videoSource",1);//设置视频来源   0代表本地  1代表网络
                        startActivity(intent2);

//                        // 调用本地播放器
//                        String url = substring + map.getFile_path();//示例，实际填你的网络视频链接
//                        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
//                        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
//                        Intent mediaIntent = new Intent(Intent.ACTION_VIEW);
//                        mediaIntent.setDataAndType(Uri.parse(url), mimeType);
//                        startActivity(mediaIntent);
                    }
                });
            }
        };
        video_grid.setAdapter(baseListAdapterVideo);
        video_grid.setSelector(new ColorDrawable(Color.TRANSPARENT));

        //------教学活动录制 end--------//



        baseListAdapteraccompanying_notes = new BaseListAdapter(accompanying_notes_grid, accompanying_notes_datas, R.layout.item_coursewares) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Courseware map = (Courseware) item;
                helper.setText(R.id._item_text, map.getFile_name());
                helper.setTag(R.id._item_img, map.getFile_path());
                if (map.is_upload()) {
                    helper.setVisibility(R.id._item_img, View.VISIBLE);
                    helper.setVisibility(R.id._item_progressbar, View.GONE);
                } else {
                    helper.setVisibility(R.id._item_img, View.GONE);
                    helper.setVisibility(R.id._item_progressbar, View.VISIBLE);
                }
                helper.setprogressbar(R.id._item_progressbar, map.getCurrent_progress());
            }
        };
        accompanying_notes_grid.setAdapter(baseListAdapteraccompanying_notes);
        accompanying_notes_grid.setSelector(new ColorDrawable(Color.TRANSPARENT));
        accompanying_notes_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Courseware courseware_bean = accompanying_notes_datas.get(position);
                ArrayList urls_huanzhexinxi = null;
                Intent intent = null;
                switch (courseware_bean.getFile_name().substring(courseware_bean.getFile_name().lastIndexOf(".") + 1, courseware_bean.getFile_name().length())) {
                    case "png":
                        urls_huanzhexinxi = new ArrayList();
                        LogUtil.e("png", courseware_bean.getFile_path());
                        urls_huanzhexinxi.add(http+courseware_bean.getFile_path());
                        intent = new Intent(context, PicViewerActivity.class);
                        intent.putExtra("type", "my_case");
                        intent.putExtra("urls_case", urls_huanzhexinxi);
                        intent.putExtra("current_index", 0);
                        startActivity(intent);
                        break;
                    case "jpg":
                        urls_huanzhexinxi = new ArrayList();
                        urls_huanzhexinxi.add(http+courseware_bean.getFile_path());
                        intent = new Intent(context, PicViewerActivity.class);
                        intent.putExtra("type", "my_case");
                        intent.putExtra("urls_case", urls_huanzhexinxi);
                        intent.putExtra("current_index", 0);
                        startActivity(intent);
                        break;
                    case "jpeg":
                        urls_huanzhexinxi = new ArrayList();
                        urls_huanzhexinxi.add(http+courseware_bean.getFile_path());
                        intent = new Intent(context, PicViewerActivity.class);
                        intent.putExtra("type", "my_case");
                        intent.putExtra("urls_case", urls_huanzhexinxi);
                        intent.putExtra("current_index", 0);
                        startActivity(intent);
                        break;
                    case "bmp":
                        urls_huanzhexinxi = new ArrayList();
                        urls_huanzhexinxi.add(http+courseware_bean.getFile_path());
                        intent = new Intent(context, PicViewerActivity.class);
                        intent.putExtra("type", "my_case");
                        intent.putExtra("urls_case", urls_huanzhexinxi);
                        intent.putExtra("current_index", 0);
                        startActivity(intent);
                        break;
                    default:
                        Intent intent1 = new Intent(context, File_down.class);
                        intent1.putExtra("file_path", http+courseware_bean.getFile_path());
                        intent1.putExtra("file_name", courseware_bean.getFile_path().substring(courseware_bean.getFile_path().lastIndexOf("/") + 1, courseware_bean.getFile_path().length()));
                        startActivity(intent1);
                        break;
                }
            }
        });



    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * name: 点击查看某个视频的详细 author:Tom 2016-1-28下午3:42:08
     */
    private class casesharing_listListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {

            Map map = listData.get(arg2);
            Intent intent = null;
            switch (map.get("key").toString()) {
                case "1"://评价带教
                    intent = new Intent(context, The_evaluation_of_teaching_list.class);
                    intent.putExtra("fid", fid);
                    intent.putExtra("gps_ids", map.get("gps_ids").toString());
                    break;
                case "2"://评价科室
                    intent = new Intent(context, Evaluation_department_list.class);
                    break;
            }

            if (intent != null) {
                startActivity(intent);
            }
        }
    }

    public void getUrlRulest() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.activitiesSelct);
                    obj.put("fid", fid);
                    obj.put("id", id);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("查看教学活动详情", result);
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

    //打开系统的文件管理器
    public void openFileMenu(View view) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            LogUtil.e("数据", "requestCode:" + requestCode + "   resultCode:" + resultCode + "   data:" + data.toString());
            if (resultCode == Activity.RESULT_OK) {
                switch (requestCode) {
                    case 1:
                        Uri uri = data.getData();
                        //获得选中文件的路径
                        String filePath = "";
                        if ("file".equalsIgnoreCase(uri.getScheme())) {//使用第三方应用打开
                            filePath = uri.getPath();

                            //return;
                        }else{
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {//24以后
                                filePath =getFilePathFromURI(this, uri);//新的方式

                            } else
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                                filePath = getPath(this, uri);

                            } else {//4.4以下下系统调用方法
                                filePath = getRealPathFromURI(uri);

                            }
                        }

                        //获得选中文件的名称
                        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
                        if(fileName.contains("image:")  || !fileName.contains(".")){
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                                filePath = getPath(this, uri);

                            } else {//4.4以下下系统调用方法
                                filePath = getRealPathFromURI(uri);
                            }

                            //获得选中文件的名称
                            fileName =
                                    filePath.substring(filePath.lastIndexOf("/") + 1);
                        }
                        Courseware courseware_bean = new Courseware();
                        courseware_bean.setId(DateUtil.getCurrDate("yyyy-MM-dd HH:mm:ss"));
                        courseware_bean.setCurrent_progress(0);
                        courseware_bean.setFile_name(fileName);
                        courseware_bean.setFile_path(filePath);
                        courseware_bean.setIs_upload(false);
                        accompanying_notes_datas.add(courseware_bean);
                        accompanying_notes_pos.put(courseware_bean.getId(), accompanying_notes_datas.size() - 1);
                        //上传文件
                        startUploadFile(accompanying_notes_datas.get(accompanying_notes_datas.size() - 1), filePath);
                        //显示内容
                        baseListAdapteraccompanying_notes.notifyDataSetChanged();
                        if (accompanying_notes_datas.size() > 0) {
                            accompanying_notes_grid.setVisibility(View.VISIBLE);
                        } else {
                            accompanying_notes_grid.setVisibility(View.GONE);
                        }
//                        Log.e("文件路径：", filePath);
//                        Log.e("文件名称：", filePath.substring(filePath.lastIndexOf("/") + 1));
                        break;

                    default:
                        break;
                }

            }
        }

    }
    public void startUploadFile(final Courseware pos, final String file_path) {

        LogUtil.e("文件路径", file_path);
        RequestParams params = new RequestParams();
        params.addBodyParameter("activitiesFile", new File(file_path));

        HttpUtils httpUtils = new HttpUtils();
        //设置线程数
//                httpUtils.configRequestThreadPoolSize(1);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("act", URLConfig.activitiesUpload);
            jsonObject.put("uid", SharedPreferencesTools.getUid(context));
            jsonObject.put("fid", fid);
            jsonObject.put("id", id);
            jsonObject.put("type", 3);
            LogUtil.e("随堂笔记上传",jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.addBodyParameter("data", Base64utils.getBase64(Base64utils.getBase64(jsonObject.toString())));

        httpUtils.send(HttpRequest.HttpMethod.POST, URLConfig.teaching_activities_upload, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = Base64utils.getFromBase64(Base64utils.getFromBase64(responseInfo.result));
//                Log.e("上传成功：", responseInfo.result);
                LogUtil.e("解密后111：", result);
                try {
                    final JSONObject jsonObject = new JSONObject(result);
                    LogUtil.e("随堂笔记上传  接口",jsonObject.toString());
                    if (jsonObject.getInt("code") == 200) {
                        JSONObject dataJson = jsonObject.getJSONObject("data");
                        if (dataJson.getInt("status") == 1) { // 成功
                            JSONObject dateJson = dataJson.getJSONObject("dataList");
                            pos.setFile_path(dateJson.getString("url"));
                            pos.setFile_name(dateJson.getString("url_name"));
                            pos.setIs_upload(true);
                            http = dateJson.getString("http");
                            if (accompanying_notes_datas.get(accompanying_notes_pos.get(pos.getId())).getId().equals(pos.getId())) {
                                baseListAdapteraccompanying_notes.getView(accompanying_notes_pos.get(pos.getId()), accompanying_notes_grid.getChildAt(accompanying_notes_pos.get(pos.getId())), accompanying_notes_grid);
                            } else {
                                for (int i = 0; i < accompanying_notes_datas.size(); i++) {

                                    if (accompanying_notes_datas.get(i).getId().equals(pos.getId())) {
                                        accompanying_notes_pos.put(accompanying_notes_datas.get(i).getId(), i);
                                    }
                                }
                                baseListAdapteraccompanying_notes.getView(accompanying_notes_pos.get(pos.getId()), accompanying_notes_grid.getChildAt(accompanying_notes_pos.get(pos.getId())), accompanying_notes_grid);
                            }
                        } else {
                            Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            accompanying_notes_datas.remove(pos);
//                            accompanying_notes_pos.remove(pos.getId());
                            baseListAdapteraccompanying_notes.notifyDataSetChanged();
                        }
                    } else {
                        accompanying_notes_datas.remove(pos);
//                        accompanying_notes_pos.remove(pos.getId());
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        baseListAdapteraccompanying_notes.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    accompanying_notes_datas.remove(pos);
//                    accompanying_notes_pos.remove(pos.getId());
                    baseListAdapteraccompanying_notes.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                LogUtil.e("文件路径2", 111 + "  " + file_path);
                LogUtil.e("上传失败：", s);
                LogUtil.e("错误：", e.toString());
                accompanying_notes_datas.remove(pos);
//                accompanying_notes_pos.remove(pos.getId());
                baseListAdapteraccompanying_notes.notifyDataSetChanged();
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
//                Toast.makeText(context,"正在上传，完成后将退出",Toast.LENGTH_SHORT).show();
                LogUtil.e("上传进度", current + "/" + total + "    状态：" + isUploading);
                LogUtil.e("上传进度", ((float) current / (float) total * 100) + "");

                LogUtil.e("解密后2222：","22222222222222");
                if (isUploading && pos!=null) {

                    if (current / total == 1) {
                        pos.setCurrent_progress(100);
                    } else {
                        pos.setCurrent_progress((int) ((float) current / (float) total * 100));
                    }
                    LogUtil.e("我要看的数据：",pos.getCurrent_progress()+"");
                    LogUtil.e("我要看的数据：",pos.toString());
                    LogUtil.e("我要看的数据：",pos.getId());
                    LogUtil.e("我要看的数据：",accompanying_notes_pos.toString());
                    LogUtil.e("我要看的数据：",accompanying_notes_datas .toString());
//                    LogUtil.e("我要看的数据：",accompanying_notes_pos.get(pos.getId()).toString());
//                    LogUtil.e("我要看的数据：",accompanying_notes_datas .toString());

                        if (accompanying_notes_pos.containsKey(pos.getId()) && accompanying_notes_datas.get(accompanying_notes_pos.get(pos.getId())).getId().equals(pos.getId())) {
                            baseListAdapteraccompanying_notes.getView(accompanying_notes_pos.get(pos.getId()), accompanying_notes_grid.getChildAt(accompanying_notes_pos.get(pos.getId())), accompanying_notes_grid);
                        } else {

                            for (int i = 0; i < accompanying_notes_datas.size(); i++) {
                                if (accompanying_notes_datas.get(i).getId().equals(pos.getId())) {
                                    accompanying_notes_pos.put(accompanying_notes_datas.get(i).getId(), i);
                                }
                            }
                            if (accompanying_notes_pos.containsKey(pos.getId()) ){
                                baseListAdapteraccompanying_notes.getView(accompanying_notes_pos.get(pos.getId()), accompanying_notes_grid.getChildAt(accompanying_notes_pos.get(pos.getId())), accompanying_notes_grid);
                            }
                        }
                    }


            }
        });
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

    /*删除随堂笔记*/
    public void deleteUpload(final View view) {

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("act", URLConfig.deleteUpload);
                        obj.put("fid", fid);
                        obj.put("id", id);
                        obj.put("type", 3);

                        for (int i = 0; i < accompanying_notes_datas.size(); i++) {
                            Courseware courseware_ben = accompanying_notes_datas.get(i);
                            if (view.getTag().toString().equals(courseware_ben.getFile_path())) {
                                obj.put("filename", courseware_ben.getFile_path());
                            }
                        }
                        obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                        String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                        LogUtil.e("删除文件", result);
                        Message message = new Message();
                        message.what = 6;
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

    public void open_Coursewares(View view){
        Intent intent = new Intent(context, com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management.Courseware.class);
        //把返回数据存入Intent
        Bundle bundle=new Bundle();
        bundle.putString("http",http);
        bundle.putString("fid",fid);
        bundle.putString("id",id);
        bundle.putString("type","2");//1代表新发布或者活动未开始可编辑 2.代表活动已开始 不可编辑
        bundle.putSerializable("list",(Serializable)coursewares);//序列化,要注意转化(Serializable)
        intent.putExtras(bundle);//发送数据

        startActivity(intent);
    }
    public void selectLecturer(View view){
        Intent intent = new Intent(context, Event_Details_lecturer.class);
        //把返回数据存入Intent
        Bundle bundle=new Bundle();
        bundle.putString("fid",fid);
        bundle.putString("id",id);
        bundle.putString("type","2");//1代表新发布或者活动未开始可编辑 2.代表活动已开始 不可编辑
        bundle.putSerializable("list",(Serializable)lecturer_data);//序列化,要注意转化(Serializable)
        intent.putExtras(bundle);//发送数据
        startActivity(intent);
    }
    public void selectTeaching(View view){
        Intent intent = new Intent(context, Event_Details_Teaching.class);

        //把返回数据存入Intent
        Bundle bundle=new Bundle();
        bundle.putString("fid",fid);
        bundle.putString("id",id);
        bundle.putString("type","2");//1代表新发布或者活动未开始可编辑 2.代表活动已开始 不可编辑
        bundle.putSerializable("list",(Serializable)teaching_data);//序列化,要注意转化(Serializable)
        intent.putExtras(bundle);//发送数据
        startActivity(intent);
    }
       /*使用第三方软件获取文件*/

    public String getFilePathFromURI(Context context, Uri contentUri) {
        File rootDataDir = context.getFilesDir();
        String fileName = getFileName(contentUri);
        if (!TextUtils.isEmpty(fileName)) {
            File copyFile = new File(rootDataDir + File.separator + fileName);
            copyFile(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    public static String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }

    public void copyFile(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            copyStream(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int copyStream(InputStream input, OutputStream output) throws Exception, IOException {
        final int BUFFER_SIZE = 1024 * 2;
        byte[] buffer = new byte[BUFFER_SIZE];
        BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
        BufferedOutputStream out = new BufferedOutputStream(output, BUFFER_SIZE);
        int count = 0, n = 0;
        try {
            while ((n = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                out.write(buffer, 0, n);
                count += n;
            }
            out.flush();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
            }
            try {
                in.close();
            } catch (IOException e) {
            }
        }
        return count;
    }
}
