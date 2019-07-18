package com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.BitmapFactory;
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
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Courseware;
import com.linlic.ccmtv.yx.activity.entity.Event_Details_Leave_bean;
import com.linlic.ccmtv.yx.activity.entity.Files_info;
import com.linlic.ccmtv.yx.activity.entity.Resident;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.evaluation.Evaluation_department_list;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.evaluation.The_evaluation_of_teaching_list;
import com.linlic.ccmtv.yx.activity.upload.ChooseVideoActivity;
import com.linlic.ccmtv.yx.activity.upload.PicViewerActivity;
import com.linlic.ccmtv.yx.activity.upload.VideoPlayerActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.enums.ActionEnum;
import com.linlic.ccmtv.yx.enums.PermissionEnum;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.util.PathUtil;
import com.linlic.ccmtv.yx.utils.Base64utils;
import com.linlic.ccmtv.yx.utils.DateUtil;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyGridView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.PhotoBitmapUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.XUtilsImageLoader;
import com.linlic.ccmtv.yx.widget.ActionSheetDialog;
import com.linlic.ccmtv.yx.widget.CustomDatePicker;
import com.linlic.ccmtv.yx.widget.PermissionTipsDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import org.json.JSONArray;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.linlic.ccmtv.yx.utils.GetPathFromUri4kitkat.getDataColumn;
import static com.linlic.ccmtv.yx.utils.GetPathFromUri4kitkat.isDownloadsDocument;
import static com.linlic.ccmtv.yx.utils.GetPathFromUri4kitkat.isExternalStorageDocument;
import static com.linlic.ccmtv.yx.utils.GetPathFromUri4kitkat.isMediaDocument;

/**
 * Created by Administrator on 2018/8/29. 活动详情
 */

public class Event_Details extends BaseActivity {
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


    @Bind(R.id.photo)
    MyGridView photo;//照片
    @Bind(R.id.photo_layout)
    LinearLayout photo_layout;//照片集合
    @Bind(R.id.hand)
    MyGridView hand;//照片
    @Bind(R.id.hand_layout)
    LinearLayout hand_layout;//照片集合

    @Bind(R.id.submit_layout)
    LinearLayout submit_layout;//二维码展示区域
    @Bind(R.id.reason_for_review)
    TextView reason_for_review;//审核理由
    @Bind(R.id.approval_status)
    TextView approval_status;//审核状态
    @Bind(R.id.approval_layout)
    LinearLayout approval_layout;//审核模块
    @Bind(R.id.qr_code_layout)
    LinearLayout qr_code_layout;//二维码右上角模块
    @Bind(R.id.close_layout)
    LinearLayout close_layout;//取消按钮容器
    @Bind(R.id.submit_layout2)
    LinearLayout submit_layout2;//提交按钮容器
    @Bind(R.id.close)
    TextView close;//取消按钮容器
    @Bind(R.id.submit)
    TextView submit;//提交按钮容器
    @Bind(R.id.remarks)
    TextView remarks;//备注
    @Bind(R.id.code_img)
    ImageView code_img;//二维码图片
    @Bind(R.id.manual_update)
    TextView manual_update;//手动更新二维码
    @Bind(R.id.submit_layout3)
    LinearLayout submit_layout3;//  手写记录与现场照片提交确认容器
    @Bind(R.id.submit_layout3_text)
    TextView submit_layout3_text;//  手写记录与现场照片提交确认 提示语
    @Bind(R.id.submit_text)
    TextView submit_text;//  手写记录与现场照片提交确认 提交
    @Bind(R.id.close_text)
    TextView close_text;//  手写记录与现场照片提交确认 取消
    @Bind(R.id.title)
    TextView title;//  二维码 title
    @Bind(R.id.status_img)
    ImageView status_img;//  二维码 title
    @Bind(R.id.delete)
    TextView delete;//  删除教学活动
    @Bind(R.id.hand_upload)
    TextView hand_upload;// 手写记录文件上传按钮
    @Bind(R.id.lecturer_layout)
    LinearLayout lecturer_layout;//讲师 模块 容器
    @Bind(R.id.leave_layout)
    LinearLayout leave_layout;//请假 模块 容器
    @Bind(R.id.leave_text)
    TextView leave_text;//请假title
    @Bind(R.id.delete_layout)
    LinearLayout delete_layout;//删除教学活动 弹出框容器
    @Bind(R.id.cel_del_text)
    TextView cel_del_text;//删除教学活动 取消按钮
    @Bind(R.id.del_submit_text)
    TextView del_submit_text;//删除教学活动 确认按钮
    @Bind(R.id.partivipant_num)
    TextView partivipant_num;//参与人员 人数字段
    @Bind(R.id.lecturer_num)
    TextView lecturer_num;//讲师 人数字段
    @Bind(R.id.fixed_layout)
    LinearLayout fixed_layout;//二维码 固定 容器
    @Bind(R.id.fixed_icon1)
    View fixed_icon1;//二维码 固定  icon 选中
    @Bind(R.id.fixed_icon2)
    View fixed_icon2;//二维码 固定  icon 未选中
    @Bind(R.id.dynamic_layout)
    LinearLayout dynamic_layout;//二维码 动态 容器
    @Bind(R.id.dynamic_icon1)
    View dynamic_icon1;//二维码 动态  icon 选中
    @Bind(R.id.dynamic_icon2)
    View dynamic_icon2;//二维码 动态  icon 未选中
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

    private String is_show_btn = "";//是否显示培训活动 的 发布 编辑 删除 按钮 1不显示 2正常显示
    private String is_code = "1";//1是动态二维码 2是固定二维码
    private List<Event_Details_Leave_bean> leave_data = new ArrayList<Event_Details_Leave_bean>();
    Map<String, Object> leave_select = new HashMap<>();//当前审核 学员请假

    private Long set_time = 0L;//二维码刷新时间
    private String id = "";//教学活动ID
    private String ing = "";//二维码可点开 不可点开 状态

    List<Courseware> coursewares = new ArrayList<>();//课件
    List<Courseware> photos = new ArrayList<>();//和学生合照
    List<Courseware> hands = new ArrayList<>();//手写记录
    List<Courseware> videoList = new ArrayList<>();//课程录制
    List<Resident> residents = new ArrayList<>();//住院医师
    List<Resident> lecturer_data = new ArrayList<>();//讲师 数据
    public List<Resident> teaching_data = new ArrayList<>();//带教 数据
    Map<String, Integer> coursewares_pos = new HashMap<>();
    Map<String, Integer> photos_pos = new HashMap<>();
    Map<String, Integer> hands_pos = new HashMap<>();
    Map<String, Integer> videos_pos = new HashMap<>();
    CustomDatePicker customDatePicker1, customDatePicker2;
    private List<String> allKeshi_list = new ArrayList<>(), allStatus_list = new ArrayList<>();//活动类型数据
    JSONObject result, data;
    private String fid = "";
    private String http = "";
    private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
    private BaseListAdapter baseListAdapterphoto;
    private BaseListAdapter baseListAdapterhand;
    private BaseListAdapter baseListAdapterVideo;
    java.util.Timer timer = null;
    private String statusSub;
    private String is_upload_files = "";

    private static final int REQUEST_CODE_SELECT_VIDEO = 30; // 选择的是视频
    private String is_video = ""; // 用来判断录制按钮是否显示的
    private static final int REQUEST_CODE_SYSTEM_CAMERA = 202; // 选择的系统相机录制
    // 保存录制视频的路径
    private String saveDir = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "ccmtvCache" + File.separator + "videoDir";
    private File videoFile;

    // 表示视频正在上传之中，不可删除
    private boolean video_is_uploading = false;


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
                                if (dateJson.has("is_video_upd")) {
                                    String is_video_upd = dateJson.getString("is_video_upd");
                                    if (!TextUtils.isEmpty(is_video_upd) && is_video_upd.equals("1")) {
                                        tv_video_select.setVisibility(View.VISIBLE);
                                    } else {
                                        tv_video_select.setVisibility(View.GONE);
                                    }
                                }
                                if (dateJson.has("is_video"))
                                    is_video = dateJson.getString("is_video");

                                activity_name.setText(dateJson.getString("name"));
                                title.setText(dateJson.getString("name"));
                                the_activity_type.setText(dateJson.getString("type_name"));
                                start_time.setText(dateJson.getString("add_time"));
                                end_time.setText(dateJson.getString("end_time"));
                                place.setText(dateJson.getString("place"));
                                remarks.setText(Html.fromHtml(dateJson.getString("message")));
                                submit_layout3_text.setText(dateJson.getString("hint"));
                                http = dateJson.getString("http");
                                ing = dateJson.getString("ing");
                                if (id != null && id.length() > 0 && dateJson.has("is_del") && dateJson.getString("is_del").equals("1")) {
                                    delete.setVisibility(View.VISIBLE);
                                } else {
                                    delete.setVisibility(View.GONE);
                                }
                                is_code = dateJson.getString("is_code");
                                is_upload_files = dateJson.has("is_upload_files") ? dateJson.getString("is_upload_files") : "2";
                                if (is_code.equals("1")) {
                                    dynamic_icon1.setVisibility(View.VISIBLE);
                                    dynamic_icon2.setVisibility(View.GONE);
                                    fixed_icon2.setVisibility(View.VISIBLE);
                                    fixed_icon1.setVisibility(View.GONE);
                                } else {
                                    fixed_icon1.setVisibility(View.VISIBLE);
                                    fixed_icon2.setVisibility(View.GONE);
                                    dynamic_icon2.setVisibility(View.VISIBLE);
                                    dynamic_icon1.setVisibility(View.GONE);
                                }
                                set_time = dateJson.getLong("set_time") * 1000L;
                                for (int i = 0; i < dateJson.getJSONArray("kejian").length(); i++) {
                                    JSONObject kejianJson = dateJson.getJSONArray("kejian").getJSONObject(i);
                                    Courseware courseware_bean = new Courseware();
                                    courseware_bean.setFile_name(kejianJson.getString("url_name"));
                                    courseware_bean.setFile_path(kejianJson.getString("url"));
                                    courseware_bean.setIs_upload(true);
                                    coursewares.add(courseware_bean);
                                    New_teaching_activities.coursewares.add(courseware_bean);
                                    New_teaching_activities.coursewares_pos.put(courseware_bean.getId(), coursewares.size() - 1);
                                }

                                JSONObject Event_json = new JSONObject();
                                Event_json.put(id, dateJson.getString("status_sub"));
                                SharedPreferencesTools.saveEvent_details_status(context, Event_json.toString());

                                statusSub = dateJson.getString("status_sub");
                                switch (dateJson.getString("status_sub")) {
                                    case "1"://未开始
                                        status_img.setImageResource(R.mipmap.training_36);
                                        approval_layout.setVisibility(View.VISIBLE);
                                        approval_status.setText("未开始");
                                        reason_for_review.setVisibility(View.GONE);
                                        if (dateJson.getString("ing").equals("1")) {
                                            qr_code_layout.setVisibility(View.VISIBLE);
                                        } else {
                                            qr_code_layout.setVisibility(View.GONE);
                                        }

                                        close_layout.setVisibility(View.GONE);
                                        submit_layout2.setVisibility(View.GONE);
                                        hand_upload.setVisibility(View.GONE);
                                        break;
                                    case "2"://进行中
                                        hand_upload.setVisibility(View.VISIBLE);
                                        status_img.setImageResource(R.mipmap.training_34);
                                        approval_layout.setVisibility(View.VISIBLE);
                                        approval_status.setText("进行中");
                                        reason_for_review.setVisibility(View.GONE);
                                        qr_code_layout.setVisibility(View.VISIBLE);
                                        close_layout.setVisibility(View.VISIBLE);
                                        submit_layout2.setVisibility(View.VISIBLE);
                                        Courseware courseware_bean1 = new Courseware();
                                        courseware_bean1.setIs_upload(false);
                                        courseware_bean1.setId("0");
                                        photos.add(courseware_bean1);
                                        photos_pos.put(courseware_bean1.getId(), photos.size() - 1);
                                        baseListAdapterphoto.notifyDataSetChanged();
                                        baseListAdapterhand.notifyDataSetChanged();
                                        break;
                                    case "3"://已结束
                                        hand_upload.setVisibility(View.VISIBLE);
                                        status_img.setImageResource(R.mipmap.training_37);
                                        approval_layout.setVisibility(View.VISIBLE);
                                        approval_status.setText("已结束");
                                        reason_for_review.setVisibility(View.GONE);
                                        qr_code_layout.setVisibility(View.GONE);
                                        close_layout.setVisibility(View.VISIBLE);
                                        submit_layout2.setVisibility(View.VISIBLE);
                                        Courseware courseware_bean2 = new Courseware();
                                        courseware_bean2.setIs_upload(false);
                                        courseware_bean2.setId("0");
                                        photos.add(courseware_bean2);
                                        photos_pos.put(courseware_bean2.getId(), photos.size() - 1);

                                        baseListAdapterphoto.notifyDataSetChanged();
                                        baseListAdapterhand.notifyDataSetChanged();
                                        break;
                                    case "4"://审核中
                                        hand_upload.setVisibility(View.GONE);
                                        status_img.setImageResource(R.mipmap.training_39);
                                        approval_layout.setVisibility(View.VISIBLE);
                                        reason_for_review.setVisibility(View.GONE);
                                        qr_code_layout.setVisibility(View.GONE);
                                        close_layout.setVisibility(View.GONE);
                                        submit_layout2.setVisibility(View.GONE);
                                        approval_status.setText("审核中");
                                        // 如果没有视频，并且是审核中状态 整个cell 就隐藏掉
                                        ll_video_cell.setVisibility(dateJson.getJSONArray("_recorder").length() == 0 ? View.GONE : View.VISIBLE);

                                        break;
                                    case "5"://审核通过
                                        hand_upload.setVisibility(View.GONE);
                                        status_img.setImageResource(R.mipmap.training_35);
                                        approval_layout.setVisibility(View.VISIBLE);
                                        reason_for_review.setVisibility(View.GONE);
                                        qr_code_layout.setVisibility(View.GONE);
                                        close_layout.setVisibility(View.GONE);
                                        submit_layout2.setVisibility(View.GONE);
                                        approval_status.setText("审核通过");
                                        break;
                                    case "6"://审核失败
                                        hand_upload.setVisibility(View.VISIBLE);
                                        status_img.setImageResource(R.mipmap.training_38);
                                        approval_layout.setVisibility(View.VISIBLE);
                                        reason_for_review.setVisibility(View.VISIBLE);
                                        qr_code_layout.setVisibility(View.GONE);
                                        submit_layout2.setVisibility(View.GONE);
                                        reason_for_review.setText(dateJson.getString("errormsg"));
                                        approval_status.setText("审核失败");
                                        close_layout.setVisibility(View.VISIBLE);
                                        submit_layout2.setVisibility(View.VISIBLE);
                                        Courseware courseware_bean3 = new Courseware();
                                        courseware_bean3.setIs_upload(false);
                                        courseware_bean3.setId("0");
                                        photos.add(courseware_bean3);
                                        photos_pos.put(courseware_bean3.getId(), photos.size() - 1);
                                        baseListAdapterphoto.notifyDataSetChanged();
                                        baseListAdapterhand.notifyDataSetChanged();
                                        break;
                                    default:
                                        break;

                                }

                                for (int i = 0; i < dateJson.getJSONArray("photo").length(); i++) {
                                    JSONObject kejianJson = dateJson.getJSONArray("photo").getJSONObject(i);
                                    Courseware courseware_bean = new Courseware();
                                    courseware_bean.setFile_path(kejianJson.getString("photo"));
//                                    if (dateJson.getString("status_sub").equals("1") || dateJson.getString("status_sub").equals("2") || dateJson.getString("status_sub").equals("3")) {
                                    courseware_bean.setIs_upload(true);
//                                    } else {
//                                        courseware_bean.setIs_upload(false);
//                                    }
                                    courseware_bean.setId("" + (i + 1));
                                    photos.add(courseware_bean);
                                    photos_pos.put(courseware_bean.getId(), photos.size() - 1);
                                }
                                baseListAdapterphoto.notifyDataSetChanged();

                                if (photos.size() < 1) {
                                    photo_layout.setVisibility(View.GONE);
                                } else {
                                    photo_layout.setVisibility(View.VISIBLE);
                                }
                                baseListAdapterphoto.notifyDataSetChanged();

                                for (int i = 0; i < dateJson.getJSONArray("hand").length(); i++) {
                                    JSONObject kejianJson = dateJson.getJSONArray("hand").getJSONObject(i);
                                    Courseware courseware_bean = new Courseware();
                                    courseware_bean.setFile_path(kejianJson.getString("url"));
                                    courseware_bean.setFile_name(kejianJson.getString("url_name"));
                                    courseware_bean.setIs_upload(true);
                                    courseware_bean.setId("" + (i + 1));
                                    hands.add(courseware_bean);
                                    hands_pos.put(courseware_bean.getId(), hands.size() - 1);
                                }

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

                                //讲师
                                if (dateJson.getString("is_speaker").equals("1") && dateJson.has("speaker")) {
                                    for (int i = 0; i < dateJson.getJSONArray("speaker").length(); i++) {
                                        JSONObject dataJson1 = dateJson.getJSONArray("speaker").getJSONObject(i);
                                        Resident resident = new Resident();
                                        resident.setId(dataJson1.getString("uid"));
                                        resident.setIs_select(false);
                                        resident.setName(dataJson1.getString("realname"));
                                        resident.setUsername(dataJson1.getString("username"));
                                        lecturer_data.add(resident);
                                    }
                                    if (lecturer_data.size() > 0) {
                                        lecturer_layout.setVisibility(View.VISIBLE);
                                    } else {
                                        lecturer_layout.setVisibility(View.GONE);
                                    }
                                    lecturer_num.setText(lecturer_data.size() + "人");
                                } else {
                                    lecturer_layout.setVisibility(View.GONE);
                                }

                                //带教
                                if (dateJson.has("is_teachers") && dateJson.getString("is_teachers").equals("1")) {
                                    if (dateJson.has("teachers")) {
                                        for (int i = 0; i < dateJson.getJSONArray("teachers").length(); i++) {
                                            JSONObject dataJson1 = dateJson.getJSONArray("teachers").getJSONObject(i);
                                            Resident resident = new Resident();
                                            resident.setId(dataJson1.getString("uid"));
                                            resident.setIs_select(false);
                                            resident.setName(dataJson1.getString("realname"));
                                            resident.setUsername(dataJson1.getString("username"));
                                            teaching_data.add(resident);
                                        }
                                        teaching_num.setText(teaching_data.size() + "人");
                                        teaching_layout.setVisibility(View.VISIBLE);
                                    }

                                } else {
                                    teaching_layout.setVisibility(View.GONE);
                                }

                                //请假列表
                                if (dateJson.has("userLeaveList")) {
                                    leave_data.clear();
                                    for (int i = 0; i < dateJson.getJSONArray("userLeaveList").length(); i++) {
                                        JSONObject dataJson12 = dateJson.getJSONArray("userLeaveList").getJSONObject(i);

                                        Event_Details_Leave_bean event_details_leave_bean = new Event_Details_Leave_bean();
                                        event_details_leave_bean.setSign(dataJson12.getString("sign"));
                                        event_details_leave_bean.setLeave_msg(dataJson12.getString("leave_msg"));
                                        event_details_leave_bean.setRealname(dataJson12.getString("realname"));
                                        event_details_leave_bean.setUid(dataJson12.getString("uid"));


                                        leave_data.add(event_details_leave_bean);
                                    }

                                    if (leave_data.size() > 0) {
                                        leave_layout.setVisibility(View.VISIBLE);
                                        leave_text.setText(leave_data.size() + "人");
                                    } else {
                                        leave_layout.setVisibility(View.GONE);
                                    }

                                } else {
                                    leave_layout.setVisibility(View.GONE);
                                }


                                baseListAdapterhand.notifyDataSetChanged();


                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
//
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                JSONArray dateJson = dataJson.getJSONArray("dataList");
                                for (int i = 0; i < dateJson.length(); i++) {
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    Resident resident = new Resident();
                                    resident.setId(dataJson1.getString("uid"));
//                                    resident.setImgUrl(dataJson1.getString("IDphoto"));
                                    resident.setIs_select(false);
                                    resident.setName(dataJson1.getString("realname"));
                                    resident.setUsername(dataJson1.getString("username"));
                                    resident.setMobphone(dataJson1.getString("mobphone"));
//                                    resident.setKsname(dataJson1.getString("hospital_name"));
                                    resident.setHospital_kid(dataJson1.getString("hospital_kid"));
                                    resident.setBase_name(dataJson1.getString("base_name"));
                                    resident.setSign(dataJson1.getString("sign"));
                                    resident.setIs_temp(dataJson1.getString("is_temp"));
                                    if (dataJson1.has("files_info")) {
                                        JSONArray files = dataJson1.getJSONArray("files_info");
                                        for (int j = 0; j < files.length(); j++) {
                                            JSONObject jsonObject1 = files.getJSONObject(j);
                                            Files_info files_info = new Files_info();
                                            files_info.setUrl(jsonObject1.getString("url"));
                                            files_info.setUrl_name(jsonObject1.getString("url_name"));
                                            resident.getFiles_infos().add(files_info);
                                        }
                                    }
                                    residents.add(resident);

                                }

                                partivipant_num.setText(residents.size() + "人");

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
                case 3:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                for (int i = 0; i < photos.size(); i++) {
                                    Courseware courseware_ben = photos.get(i);
                                    if (dataJson.getString("dataList").equals(courseware_ben.getFile_path())) {
                                        photos.remove(courseware_ben);
                                    }
                                }
                                baseListAdapterphoto.notifyDataSetChanged();
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
                case 4:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功

                                for (int i = 0; i < hands.size(); i++) {
                                    Courseware courseware_ben = hands.get(i);
                                    if (dataJson.getString("dataList").equals(courseware_ben.getFile_path())) {
                                        hands.remove(courseware_ben);
                                    }
                                }

                                baseListAdapterhand.notifyDataSetChanged();
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
                case 5:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            MyProgressBarDialogTools.hide();
                            if (dataJson.getInt("status") == 1) { // 成功
                                submit_layout3.setVisibility(View.GONE);
                                JSONObject Event_json = new JSONObject();
                                Event_json.put(id, "4");
                                SharedPreferencesTools.saveEvent_details_status(context, Event_json.toString());
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                submit_layout3.setVisibility(View.GONE);
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            MyProgressBarDialogTools.hide();
                        }

                    } catch (Exception e) {
                        MyProgressBarDialogTools.hide();
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功

                                XUtilsImageLoader xUtilsImageLoader = new XUtilsImageLoader(context);
                                xUtilsImageLoader.display(code_img, FirstLetter.getSpells(dataJson.getString("dataList")));
                                //每次需要执行的代码放到这里面。
                                submit_layout.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 7:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            if (dataJson.getInt("status") == 1) { // 成功
                                //    delete_layout.setVisibility(View.GONE);
                                Training_management.is_new_teaching_activities = true;
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                    }
                    break;

                case 8:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功

                                for (int i = 0; i < videoList.size(); i++) {
                                    Courseware courseware_ben = videoList.get(i);
                                    if (dataJson.getString("dataList").equals(courseware_ben.getFile_path())) {
                                        videoList.remove(courseware_ben);
                                    }
                                }
                                baseListAdapterVideo.notifyDataSetChanged();
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


    public static final String VIDEO_PATH_KEY = "VIDEO_PATH_KEY";
    private VideoReceiver mReceiver;
    PermissionTipsDialog fileDialog;
    PermissionTipsDialog cameraDialog;

    class VideoReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String video_path = intent.getStringExtra(VIDEO_PATH_KEY);
            File file = new File(video_path);
            Courseware courseware_bean = new Courseware();
            courseware_bean.setId(DateUtil.getCurrDate("yyyy-MM-dd HH:mm:ss"));
            courseware_bean.setCurrent_progress(0);
            courseware_bean.setFile_name(file.getName());
            courseware_bean.setFile_path(file.getPath());
            courseware_bean.setHttp(http);
            courseware_bean.setIs_upload(false);
            videoList.add(courseware_bean);
            videos_pos.put(courseware_bean.getId(), videoList.size() - 1);
            // 开始上传
            uploadVideo(courseware_bean, video_path);
            //显示内容
            baseListAdapterVideo.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.event_details);
        context = this;
        ButterKnife.bind(this);
        initView();
        id = getIntent().getStringExtra("id");
        fid = getIntent().getStringExtra("fid");
        initcustomDatePicker();
        getUrlRulest();
        getActivitiesUsers();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ActionEnum.EVENT_DETAIL_SELECT_VIDEO.getAction());
        this.registerReceiver(mReceiver = new VideoReceiver(), filter);
    }


    public void initView() {
        New_teaching_activities.coursewares.clear();
        New_teaching_activities.coursewares_pos.clear();
        is_show_btn = getIntent().getStringExtra("is_show_btn");
        if (is_show_btn.trim().equals("2")) {
            //显示培训活动 的 发布 编辑 删除 按钮
            delete.setVisibility(View.VISIBLE);
        } else {
            //不显示
            delete.setVisibility(View.GONE);
        }
        hand_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileMenu(v, 2, 1);
            }
        });

        cel_del_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_layout.setVisibility(View.GONE);
            }
        });

        del_submit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyProgressBarDialogTools.show(context);
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject();
                            obj.put("act", URLConfig.deleteData);
                            obj.put("uid", SharedPreferencesTools.getUid(context));
                            obj.put("fid", fid);
                            obj.put("id", id);

                            String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                            LogUtil.e("删除教学活动", result);
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
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (video_is_uploading) {
                    Toast.makeText(context, "正在上传视频，请稍后再试～", Toast.LENGTH_SHORT).show();
                    return;
                }
                delete_layout.setVisibility(View.VISIBLE);

            }
        });


        close_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit_layout3.setVisibility(View.GONE);
            }
        });
        submit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
            }
        });

        manual_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activitiesCode();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (video_is_uploading) {
                    Toast.makeText(context, "正在上传视频，请稍后再试～", Toast.LENGTH_SHORT).show();
                    return;
                }
                submit_layout3.setVisibility(View.VISIBLE);
            }
        });
        submit_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                submit_layout.setVisibility(View.GONE);
            }
        });
        the_activity_type.setTextColor(Color.BLACK);
        start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                String now = sdf.format(new Date());
                customDatePicker1.show(now);
            }
        });

        end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                String now = sdf.format(new Date());
                customDatePicker2.show(now);
            }
        });


        //现场照片
        baseListAdapterphoto = new BaseListAdapter(photo, photos, R.layout.item_photo) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Courseware map = (Courseware) item;
                helper.setTag(R.id._item_del, map.getId());
                if (map.getId().equals("0")) {
                    helper.setImage(R.id.item_photo, R.mipmap.upload_addpic);
                } else {
                    helper.setImageBitmap(R.id.item_photo, http + "" + map.getFile_path());
                }
                if (map.is_upload() && (statusSub.equals("2") || statusSub.equals("3") || statusSub.equals("6"))) {
                    helper.setVisibility(R.id._item_del, View.VISIBLE);
                } else {
                    helper.setVisibility(R.id._item_del, View.GONE);
                }
            }
        };
        photo.setAdapter(baseListAdapterphoto);
        photo.setSelector(new ColorDrawable(Color.TRANSPARENT));
        photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (statusSub) {
                    case "1"://未开始
                    case "4"://审核中
                    case "5"://审核通过
                        ArrayList urlsHuanzhexinxi = null;
                        urlsHuanzhexinxi = new ArrayList();
                        int pos1 = 0;
                        for (Courseware courseware : photos) {
                            String str = courseware.getFile_path();
                            urlsHuanzhexinxi.add(http + courseware.getFile_path());
                            if (courseware.getId().equals(photos.get(position).getId())) {
                                pos1 = urlsHuanzhexinxi.size() - 1;
                            }
                        }
                        Intent intent1 = new Intent(context, PicViewerActivity.class);
                        intent1.putExtra("type", "my_case");
                        intent1.putExtra("urls_case", urlsHuanzhexinxi);
                        intent1.putExtra("current_index", pos1);
                        startActivity(intent1);
                        break;
                    default:
                        int pos = 0;
                        if (position != 0) {
                            if (photos.get(position).is_upload()) {
                                ArrayList urls_huanzhexinxi = new ArrayList();
                                for (Courseware courseware : photos) {
                                    if (courseware.is_upload()) {
                                        urls_huanzhexinxi.add(http + courseware.getFile_path());
                                        if (courseware.getId().equals(photos.get(position).getId())) {
                                            pos = urls_huanzhexinxi.size() - 1;
                                        }
                                    }
                                }
                                Intent intent = new Intent(context, PicViewerActivity.class);
                                intent.putExtra("type", "my_case");
                                intent.putExtra("urls_case", urls_huanzhexinxi);
                                intent.putExtra("current_index", pos);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "图片上传中", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (photos.get(position).is_upload()) {
                                ArrayList urls_huanzhexinxi = new ArrayList();
                                for (Courseware courseware : photos) {
                                    if (courseware.is_upload()) {
                                        urls_huanzhexinxi.add(http + courseware.getFile_path());
                                        if (courseware.getId().equals(photos.get(position).getId())) {
                                            pos = urls_huanzhexinxi.size() - 1;
                                        }
                                    }
                                }
                                Intent intent = new Intent(context, PicViewerActivity.class);
                                intent.putExtra("type", "my_case");
                                intent.putExtra("urls_case", urls_huanzhexinxi);
                                intent.putExtra("current_index", pos);
                                startActivity(intent);
                            } else {
                                //添加图片
                                openFileMenu(photo, 1, 2);
                            }
                        }
                        break;

                }

            }
        });

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


                if (statusSub.equals("4") || statusSub.equals("5")) {
                    helper.setVisibility(R.id._item_img, View.GONE);
                } else {
                    helper.setVisibility(R.id._item_img, View.VISIBLE);
                }
                if (map.getCurrent_progress() < 100) {
                    helper.setVisibility(R.id._item_img, View.GONE);
                }

                helper.setprogressbar(R.id._item_progressbar, map.getCurrent_progress());

                helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (map.getCurrent_progress() < 100)
                            return;
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
                        intent2.putExtra("videoPath", substring + map.getFile_path());
                        intent2.putExtra("last_look_time", "");
                        intent2.putExtra("aid", "");
                        intent2.putExtra("videoSource", 1);//设置视频来源   0代表本地  1代表网络
                        startActivity(intent2);

                        // 调用本地播放器
//                        String url = substring + map.getFile_path();//示例，实际填你的网络视频链接
//                        Log.e("PRETTY_LOGGER", "onClick() returned: " + url);
//                        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
//                        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
//                        Intent mediaIntent = new Intent(Intent.ACTION_VIEW);
//                        mediaIntent.setDataAndType(Uri.parse(url), mimeType);
//                        mediaIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(mediaIntent);


//                        Intent intent = new Intent(context, VideoPlayerActivity.class);
//                        intent.putExtra("videoTitle", "");//传的videoTitle不带后缀
//                        intent.putExtra("videoPath", http + map.getFile_path());
//                        intent.putExtra("last_look_time", "");
//                        intent.putExtra("aid", "");
//                        intent.putExtra("videoSource", 1);//设置视频来源   0代表本地  1代表网络
////                    Log.e("last_look_time", vm.getLast_look_time() + "");
//                        context.startActivity(intent);

                    }
                });
            }
        };
        video_grid.setAdapter(baseListAdapterVideo);
        video_grid.setSelector(new ColorDrawable(Color.TRANSPARENT));
        //------教学活动录制 end--------//

        baseListAdapterhand = new BaseListAdapter(hand, hands, R.layout.item_hand) {

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

                if (statusSub.equals("4") || statusSub.equals("5")) {
                    helper.setVisibility(R.id._item_img, View.GONE);
                } else {
                    helper.setVisibility(R.id._item_img, View.VISIBLE);
                }

                helper.setprogressbar(R.id._item_progressbar, map.getCurrent_progress());
            }
        };
        hand.setAdapter(baseListAdapterhand);
        hand.setSelector(new ColorDrawable(Color.TRANSPARENT));
        hand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (hands.get(position).is_upload()) {
                    Courseware courseware_bean = hands.get(position);
                    ArrayList urls_huanzhexinxi = null;
                    Intent intent = null;
                    switch (courseware_bean.getFile_name().substring(courseware_bean.getFile_name().lastIndexOf(".") + 1, courseware_bean.getFile_name().length())) {
                        case "png":
                            urls_huanzhexinxi = new ArrayList();
                            urls_huanzhexinxi.add(http + "" + courseware_bean.getFile_path());
                            intent = new Intent(context, PicViewerActivity.class);
                            intent.putExtra("type", "my_case");
                            intent.putExtra("urls_case", urls_huanzhexinxi);
                            intent.putExtra("current_index", 0);
                            startActivity(intent);
                            break;
                        case "jpg":
                            urls_huanzhexinxi = new ArrayList();
                            urls_huanzhexinxi.add(http + "" + courseware_bean.getFile_path());
                            intent = new Intent(context, PicViewerActivity.class);
                            intent.putExtra("type", "my_case");
                            intent.putExtra("urls_case", urls_huanzhexinxi);
                            intent.putExtra("current_index", 0);
                            startActivity(intent);
                            break;
                        case "jpeg":
                            urls_huanzhexinxi = new ArrayList();
                            urls_huanzhexinxi.add(http + "" + courseware_bean.getFile_path());
                            intent = new Intent(context, PicViewerActivity.class);
                            intent.putExtra("type", "my_case");
                            intent.putExtra("urls_case", urls_huanzhexinxi);
                            intent.putExtra("current_index", 0);
                            startActivity(intent);
                            break;
                        case "bmp":
                            urls_huanzhexinxi = new ArrayList();
                            urls_huanzhexinxi.add(http + "" + courseware_bean.getFile_path());
                            intent = new Intent(context, PicViewerActivity.class);
                            intent.putExtra("type", "my_case");
                            intent.putExtra("urls_case", urls_huanzhexinxi);
                            intent.putExtra("current_index", 0);
                            startActivity(intent);
                            break;
                        default:
                            Intent intent1 = new Intent(context, File_down.class);
                            intent1.putExtra("file_path", http + "" + courseware_bean.getFile_path());
                            intent1.putExtra("file_name", courseware_bean.getFile_path().substring(courseware_bean.getFile_path().lastIndexOf("/") + 1, courseware_bean.getFile_path().length()));
                            startActivity(intent1);
                            break;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "图片上传中", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 录制视频相关begin
        tv_video_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  需要判断一下，当前的结果 返回isvideo 为1的话，就是现场录制 跟 选择文件
                // 否则的话 就只有 选择文件
                ActionSheetDialog actionSheetDialog = new ActionSheetDialog(context)
                        .builder()
                        .setCancelable(true)
                        .setCanceledOnTouchOutside(true)
                        .setTitle(getResources().getString(R.string.label_recording_dialog_title))
                        .addSheetItem(getResources().getString(R.string.label_recording_dialog_file), ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        AndPermission.with(context)
                                                .runtime()
                                                .permission(Permission.READ_EXTERNAL_STORAGE)
                                                .onGranted(new Action<List<String>>() {
                                                    @Override
                                                    public void onAction(List<String> permissions) {
                                                        showVideo();
                                                    }
                                                })
                                                .onDenied(new Action<List<String>>() {
                                                    @Override
                                                    public void onAction(List<String> permissions) {
                                                        // 权限拒绝了
                                                        if (AndPermission.hasAlwaysDeniedPermission(context, permissions)) {
                                                            // 用Dialog展示没有某权限，询问用户是否去设置中授权。
                                                            fileDialog = new PermissionTipsDialog.Builder()
                                                                    .init(context)
                                                                    .addTitle("权限申请")
                                                                    .addContent("在设置-应用-权限中开启" + "存储权限" + "，以正常使用")
                                                                    .addConfirm("设置")
                                                                    .builder();
                                                            fileDialog.setClickListener(new PermissionTipsDialog.DialogClickListener() {
                                                                @Override
                                                                public void onClick(int click) {
                                                                    if (click == PermissionTipsDialog.CLICK_CONFIRM) {
                                                                        AndPermission.with(context)
                                                                                .runtime()
                                                                                .setting()
                                                                                .start(100); // 这里其实不需要回调，随便写个requestCode 即可
                                                                    }
                                                                    fileDialog.dismiss();
                                                                }
                                                            });
                                                            fileDialog.show();
                                                        }
                                                    }
                                                }).start();
                                    }
                                }
                        );
                if (!TextUtils.isEmpty(is_video)) {
                    actionSheetDialog.addSheetItem(getResources().getString(R.string.label_recording_dialog_now), ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    AndPermission.with(context)
                                            .runtime()
                                            .permission(Permission.CAMERA, Permission.WRITE_EXTERNAL_STORAGE)
                                            .onGranted(new Action<List<String>>() {
                                                @Override
                                                public void onAction(List<String> permissions) {
                                                    // 开启自定义录制界面
                                                    Intent intent = new Intent(context, VideoRecordingActivity.class);
                                                    startActivity(intent);

//                                                    File file = new File(saveDir);
//                                                    if (!file.exists()) {
//                                                        file.mkdirs();
//                                                    }
//                                                    String name = DateUtil.getCurrDate("yyyy-MM-dd_hh-mm-ss");
//                                                    videoFile = new File(file, name
//                                                            + ".mp4");
//                                                    // 调用系统相机来做
//                                                    Uri fileUri = Uri.fromFile(videoFile);
//                                                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                                                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0.98);
//                                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//                                                    // 录制视频最大时长
//                                                    intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10000 * 60 * 60);
//                                                    startActivityForResult(intent, REQUEST_CODE_SYSTEM_CAMERA);

                                                }
                                            })
                                            .onDenied(new Action<List<String>>() {
                                                @Override
                                                public void onAction(List<String> permissions) {
                                                    StringBuffer stringBuffer = new StringBuffer();
                                                    // 权限拒绝了
                                                    if (AndPermission.hasAlwaysDeniedPermission(context, permissions)) {
                                                        for (int i = 0; i < permissions.size(); i++) {
                                                            String s = permissions.get(i);
                                                            stringBuffer.append(PermissionEnum.findDescByPermission(s) + "、");
                                                        }
                                                        String substring = "";
                                                        if (stringBuffer.toString().length() > 0) {
                                                            substring = stringBuffer.substring(0, stringBuffer.toString().length() - 1);
                                                        }
                                                        // 用Dialog展示没有某权限，询问用户是否去设置中授权。
                                                        cameraDialog = new PermissionTipsDialog.Builder()
                                                                .init(context)
                                                                .addTitle("权限申请")
                                                                .addContent("因" + substring + "未开启，该功能尚无法使用，请前往手机设置-应用-权限中开启" + substring + "")
                                                                .addConfirm("设置")
                                                                .builder();
                                                        cameraDialog.setClickListener(new PermissionTipsDialog.DialogClickListener() {
                                                            @Override
                                                            public void onClick(int click) {
                                                                if (click == PermissionTipsDialog.CLICK_CONFIRM) {
                                                                    AndPermission.with(context)
                                                                            .runtime()
                                                                            .setting()
                                                                            .start(101);// 这里其实不需要回调，随便写个requestCode 即可
                                                                }
                                                                cameraDialog.dismiss();
                                                            }
                                                        });
                                                        cameraDialog.show();
                                                    }
                                                }
                                            }).start();
                                }
                            }
                    );
                }
                actionSheetDialog.show();
            }
        });
        // 录制视频相关end
    }

    public void initcustomDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String now = sdf.format(new Date());

        customDatePicker2 = new CustomDatePicker(context, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                end_time.setText(time);
            }
        }, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker2.showSpecificTime(true); // 显示时和分
        customDatePicker2.setIsLoop(true); // 允许循环滚动

        customDatePicker1 = new CustomDatePicker(context, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                start_time.setText(time);
            }
        }, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker1.showSpecificTime(true); // 不显示时和分
        customDatePicker1.setIsLoop(true); // 不允许循环滚动
    }

    @Override
    protected void onDestroy() {
        try {
            customDatePicker1.dismissDialog();
            customDatePicker2.dismissDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mReceiver != null)
            this.unregisterReceiver(mReceiver);
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

    public void getActivitiesUsers() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getActivitiesUsers);
                    obj.put("fid", fid);
                    obj.put("id", id);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("获取教学活动的学员", result);
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

    public void submit_img(View view) {
        switch (ing) {
            case "0":
                Toast.makeText(context, "活动未开始，二维码未生成", Toast.LENGTH_SHORT).show();
                break;
            case "1":
                if (is_code.equals("2")) {
                    //固定二维码
                    activitiesCode();
                } else {
                    //动态二维码
                    if (timer != null) {
                        //已存在
                    } else {
                        timer = new java.util.Timer(true);
                        TimerTask task = new TimerTask() {
                            public void run() {
                                activitiesCode();
                            }
                        };
                        //delay为long,period为long：从现在起过delay毫秒以后，每隔period毫秒执行一次。
                        timer.schedule(task, 0, set_time);
                    }
                }
                break;
            case "2":
                Toast.makeText(context, "活动已结束", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;

        }

    }


    //打开系统的文件管理器
    public void openFileMenu(View view, int code, int type) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //系统调用Action属性
        if (type == 1) {
            intent.setType("*/*");
        } else {
            intent.setType("image/*");
        }
        //设置文件类型
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // 添加Category属性
        try {
            startActivityForResult(intent, code);
        } catch (Exception e) {
            Toast.makeText(this, "没有正确打开文件管理器", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * name：选择视频
     */
    private void showVideo() {
        Intent intent = new Intent(context, ChooseVideoActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
    }

    /**
     * 获取文件或者文件夹大小.
     */
    public long getFileAllSize(String path) {
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

    /**
     *  * 转换文件大小
     *  * @param fileS
     *  * @return
     *  
     */
    private int sizeType = 0;

    private String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS);//B
            sizeType = 0;
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024);//KB
            sizeType = 1;
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576);//MB
            sizeType = 2;
        } else {
            fileSizeString = df.format((double) fileS / 1073741824);//GB
            sizeType = 3;
        }
        return fileSizeString;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == REQUEST_CODE_SELECT_VIDEO && requestCode == REQUEST_CODE_SELECT_VIDEO) {
            String videoPath = data.getStringExtra("videopath");
            long fileAllSize = getFileAllSize(videoPath);
            String sizeStr = FormetFileSize(fileAllSize);
            Log.e("PRETTY_LOGGER", "onActivityResult() returned: " + sizeStr + "--" + sizeType);
            if (sizeType == 3) {
                //GB
                double size = Double.parseDouble(sizeStr);
                if (size >= 2) {
                    Toast.makeText(context, "文件不能超过2G,请重新上传~", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if (sizeType == 2) {
                //MB
                double size = Double.parseDouble(sizeStr);
                if (size > 300) {
                    Toast.makeText(context, "文件过大上传速度慢，请等待~", Toast.LENGTH_SHORT).show();
                }
            }
            File file = new File(videoPath);
            Courseware courseware_bean = new Courseware();
            courseware_bean.setId(DateUtil.getCurrDate("yyyy-MM-dd HH:mm:ss"));
            courseware_bean.setCurrent_progress(0);
            courseware_bean.setFile_name(file.getName());
            courseware_bean.setFile_path(file.getPath());
            courseware_bean.setHttp(videoPath);
            courseware_bean.setIs_upload(false);
            videoList.add(courseware_bean);
            videos_pos.put(courseware_bean.getId(), videoList.size() - 1);
            // 开始上传视频
            uploadVideo(courseware_bean, videoPath);
            //显示内容
            baseListAdapterVideo.notifyDataSetChanged();
            return;
        }

        if (data != null) {
            LogUtil.e("数据", "requestCode:" + requestCode + "   resultCode:" + resultCode + "   data:" + data.toString());
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                //获得选中文件的路径
                String filePath = "";
                if ("file".equalsIgnoreCase(uri.getScheme())) {//使用第三方应用打开
                    filePath = uri.getPath();

                    //return;
                } else {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {//24以后
                        filePath = getFilePathFromURI(this, uri);//新的方式

                    } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                        filePath = getPath(this, uri);

                    } else {//4.4以下下系统调用方法
                        filePath = getRealPathFromURI(uri);

                    }
                }
                // 得到修复后的照片路径
                if (isImageFile(filePath)) {
                    filePath = PhotoBitmapUtils.amendRotatePhoto(filePath, context);
                }
                //获得选中文件的名称
                String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);

                if (fileName.contains("image:") || !fileName.contains(".")) {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                        filePath = getPath(this, uri);
                    } else {//4.4以下下系统调用方法
                        filePath = getRealPathFromURI(uri);
                    }
                    //获得选中文件的名称
                    fileName =
                            filePath.substring(filePath.lastIndexOf("/") + 1);

                }

                Courseware courseware_bean = null;
                switch (requestCode) {
                    case 1:

                        courseware_bean = new Courseware();
                        courseware_bean.setId(DateUtil.getCurrDate("yyyy-MM-dd HH:mm:ss"));
                        courseware_bean.setCurrent_progress(0);
                        courseware_bean.setFile_name(fileName);
                        courseware_bean.setFile_path(filePath);
                        courseware_bean.setIs_upload(false);
                        photos.add(courseware_bean);
                        photos_pos.put(courseware_bean.getId(), photos.size() - 1);
                        //上传文件
                        startUploadFile(photos.get(photos.size() - 1), filePath);
                        //显示内容
                        baseListAdapterphoto.notifyDataSetChanged();

//                        Log.e("文件路径：", filePath);
//                        Log.e("文件名称：", filePath.substring(filePath.lastIndexOf("/") + 1));
                        break;
                    case 2:

                        courseware_bean = new Courseware();
                        courseware_bean.setId(DateUtil.getCurrDate("yyyy-MM-dd HH:mm:ss"));
                        courseware_bean.setCurrent_progress(0);
                        courseware_bean.setFile_name(fileName);
                        courseware_bean.setFile_path(filePath);
                        courseware_bean.setIs_upload(false);
                        hands.add(courseware_bean);
                        hands_pos.put(courseware_bean.getId(), hands.size() - 1);
                        //上传文件
                        startUploadFile2(hands.get(hands.size() - 1), filePath);
                        //显示内容
                        baseListAdapterhand.notifyDataSetChanged();

//                        Log.e("文件路径：", filePath);
//                        Log.e("文件名称：", filePath.substring(filePath.lastIndexOf("/") + 1));
                        break;

                    case REQUEST_CODE_SYSTEM_CAMERA:
                        // 视频录制的回调
                        Uri video_uri = data.getData();
                        String video_path = PathUtil.getPhotoPathFromContentUri(context, video_uri);

                        // 文件上传
                        File file = new File(video_path);
                        Courseware courseware_bean2 = new Courseware();
                        courseware_bean2.setId(DateUtil.getCurrDate("yyyy-MM-dd HH:mm:ss"));
                        courseware_bean2.setCurrent_progress(0);
                        courseware_bean2.setFile_name(file.getName());
                        courseware_bean2.setFile_path(file.getPath());
                        courseware_bean2.setHttp(http);
                        courseware_bean2.setIs_upload(false);
                        videoList.add(courseware_bean2);
                        videos_pos.put(courseware_bean2.getId(), videoList.size() - 1);
                        // 开始上传
                        uploadVideo(courseware_bean2, video_path);
                        //显示内容
                        baseListAdapterVideo.notifyDataSetChanged();

                        Log.e("PRETTY_LOGGER", "onActivityResult() returned: " + video_path);


                        break;
                    default:
                        break;
                }

            }
        }
    }

    /*
      判断是否是图片
     */
    public static boolean isImageFile(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        if (options.outWidth == -1) {
            return false;
        }
        return true;
    }

    // 上传视频
    public void uploadVideo(final Courseware pos, String videoPath) {
        video_is_uploading = true;
        Log.e("PRETTY_LOGGER", "uploadVideo() returned: " + videoPath);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("act", URLConfig.activitiesUploadVideo);
            jsonObject.put("uid", SharedPreferencesTools.getUid(context));
            jsonObject.put("fid", fid);
            jsonObject.put("id", id);
            //  jsonObject.put("type", 1);
            //上传单个文件
            OkGo.<String>post(URLConfig.teaching_activities_upload)
                    .tag(this)
                    .params("activitiesFile", new File(videoPath))
                    .params("data", Base64utils.getBase64(Base64utils.getBase64(jsonObject.toString())))
                    .isMultipart(true)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            String result = Base64utils.getFromBase64(Base64utils.getFromBase64(response.body()));
                            video_is_uploading = false; //  表示视频不是在上传状态
                            // 视频上传成功之后， 显示在下方
                            try {
                                final JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getInt("code") == 200) {
                                    JSONObject dataJson = jsonObject.getJSONObject("data");
                                    if (dataJson.getInt("status") == 1) { // 成功
                                        JSONObject dateJson = dataJson.getJSONObject("dataList");
                                        pos.setFile_path(dateJson.getString("url"));
                                        pos.setFile_name(dateJson.getString("url_name"));
                                        pos.setHttp(dateJson.getString("http"));
                                        pos.setIs_upload(true);

                                        if (videoList.get(videos_pos.get(pos.getId())).getId().equals(pos.getId())) {
                                            baseListAdapterVideo.getView(videos_pos.get(pos.getId()), video_grid.getChildAt(videos_pos.get(pos.getId())), video_grid);
                                        } else {
                                            for (int i = 0; i < videoList.size(); i++) {

                                                if (videoList.get(i).getId().equals(pos.getId())) {
                                                    videos_pos.put(videoList.get(i).getId(), i);
                                                }
                                            }
                                            baseListAdapterVideo.getView(videos_pos.get(pos.getId()), video_grid.getChildAt(videos_pos.get(pos.getId())), video_grid);
                                        }
                                    } else {
                                        Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                        videoList.remove(pos);
                                        baseListAdapterVideo.notifyDataSetChanged();
                                    }
                                } else {
                                    videoList.remove(pos);
                                    baseListAdapterVideo.notifyDataSetChanged();
                                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                videoList.remove(pos);
                                baseListAdapterVideo.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                            Log.e("PRETTY_LOGGER", "onError() returned 上传视频--> " + response.getException().getMessage());

                        }

                        @Override
                        public void uploadProgress(Progress progress) {
                            super.uploadProgress(progress);
                            Log.e("PRETTY_LOGGER", "uploadProgress() returned 上传视频--> " + progress.totalSize);


                            if (progress.currentSize / progress.totalSize == 1) {
                                pos.setCurrent_progress(100);
                            } else {
                                pos.setCurrent_progress((int) ((float) progress.currentSize / (float) progress.totalSize * 100));
                            }

                            if (videos_pos.containsKey(pos.getId()) && videoList.get(videos_pos.get(pos.getId())).getId().equals(pos.getId())) {
                                baseListAdapterVideo.getView(videos_pos.get(pos.getId()), video_grid.getChildAt(videos_pos.get(pos.getId())), video_grid);
                            } else {
                                for (int i = 0; i < videoList.size(); i++) {

                                    if (videoList.get(i).getId().equals(pos.getId())) {
                                        videos_pos.put(videoList.get(i).getId(), i);
                                    }
                                }
                                if (videos_pos.containsKey(pos.getId())) {
                                    baseListAdapterVideo.getView(videos_pos.get(pos.getId()), video_grid.getChildAt(videos_pos.get(pos.getId())), video_grid);
                                }
                            }

                        }
                    });


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void startUploadFile(final Courseware pos, String file_path) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("act", URLConfig.activitiesUpload);
            jsonObject.put("uid", SharedPreferencesTools.getUid(context));
            jsonObject.put("fid", fid);
            jsonObject.put("id", id);
            jsonObject.put("type", 1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        /**
         * 保存数据到服务器
         */
        //上传单个文件
        OkGo.<String>post(URLConfig.teaching_activities_upload)
                .tag(this)
                .params("activitiesFile", new File(file_path))
                .params("data", Base64utils.getBase64(Base64utils.getBase64(jsonObject.toString())))
                .isMultipart(true)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String result = Base64utils.getFromBase64(Base64utils.getFromBase64(response.body()));
//                Log.e("上传成功：", responseInfo.result);
//                Log.e("解密后111：", result);
                        try {
                            final JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 200) {
                                JSONObject dataJson = jsonObject.getJSONObject("data");
                                if (dataJson.getInt("status") == 1) { // 成功
                                    JSONObject dateJson = dataJson.getJSONObject("dataList");
                                    pos.setFile_path(dateJson.getString("url"));
                                    pos.setFile_name(dateJson.getString("url_name"));
                                    pos.setIs_upload(true);

                                    if (photos.get(photos_pos.get(pos.getId())).getId().equals(pos.getId())) {
                                        baseListAdapterphoto.getView(photos_pos.get(pos.getId()), photo.getChildAt(photos_pos.get(pos.getId())), photo);
                                    } else {
                                        for (int i = 0; i < photos.size(); i++) {

                                            if (photos.get(i).getId().equals(pos.getId())) {
                                                photos_pos.put(photos.get(i).getId(), i);
                                            }
                                        }
                                        baseListAdapterphoto.getView(photos_pos.get(pos.getId()), photo.getChildAt(photos_pos.get(pos.getId())), photo);
                                    }
                                } else {
                                    Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                    photos.remove(pos);
                                    baseListAdapterphoto.notifyDataSetChanged();
                                }
                            } else {
                                photos.remove(pos);
                                baseListAdapterphoto.notifyDataSetChanged();
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            photos.remove(pos);
                            baseListAdapterphoto.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        LogUtil.e("上传失败：", response.message());
                        LogUtil.e("错误：", response.getException().toString());
                        photos.remove(pos);
                        baseListAdapterphoto.notifyDataSetChanged();
                    }
                });

    }

    public void startUploadFile2(final Courseware pos, String file_path) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("act", URLConfig.activitiesUpload);
            jsonObject.put("uid", SharedPreferencesTools.getUid(context));
            jsonObject.put("fid", fid);
            jsonObject.put("id", id);
            jsonObject.put("type", 2);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //上传单个文件
        OkGo.<String>post(URLConfig.teaching_activities_upload)
                .params("activitiesFile", new File(file_path))
                .params("data", Base64utils.getBase64(Base64utils.getBase64(jsonObject.toString())))
                .isMultipart(true)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String result = Base64utils.getFromBase64(Base64utils.getFromBase64(response.body()));
//                Log.e("上传成功：", responseInfo.result);
//                Log.e("解密后111：", result);
                        try {
                            final JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 200) {
                                JSONObject dataJson = jsonObject.getJSONObject("data");
                                if (dataJson.getInt("status") == 1) { // 成功
                                    JSONObject dateJson = dataJson.getJSONObject("dataList");
                                    pos.setFile_path(dateJson.getString("url"));
                                    pos.setFile_name(dateJson.getString("url_name"));
                                    pos.setIs_upload(true);

                                    if (hands.get(hands_pos.get(pos.getId())).getId().equals(pos.getId())) {
                                        baseListAdapterhand.getView(hands_pos.get(pos.getId()), hand.getChildAt(hands_pos.get(pos.getId())), hand);
                                    } else {
                                        for (int i = 0; i < hands.size(); i++) {

                                            if (hands.get(i).getId().equals(pos.getId())) {
                                                hands_pos.put(hands.get(i).getId(), i);
                                            }
                                        }
                                        baseListAdapterhand.getView(hands_pos.get(pos.getId()), hand.getChildAt(hands_pos.get(pos.getId())), hand);
                                    }
                                } else {
                                    Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                    hands.remove(pos);
                                    baseListAdapterhand.notifyDataSetChanged();
                                }
                            } else {
                                hands.remove(pos);
                                baseListAdapterhand.notifyDataSetChanged();
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            hands.remove(pos);
                            baseListAdapterhand.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        LogUtil.e("上传失败：", response.message());
                        LogUtil.e("错误：", response.getException().toString());
                        hands.remove(pos);
                        baseListAdapterhand.notifyDataSetChanged();
                    }

                    @Override
                    public void uploadProgress(Progress progress) {
                        super.uploadProgress(progress);

                        if (progress.currentSize / progress.totalSize == 1) {
                            pos.setCurrent_progress(100);
                        } else {
                            pos.setCurrent_progress((int) ((float) progress.currentSize / (float) progress.totalSize * 100));
                        }

                        if (hands_pos.containsKey(pos.getId()) && hands.get(hands_pos.get(pos.getId())).getId().equals(pos.getId())) {
                            baseListAdapterhand.getView(hands_pos.get(pos.getId()), hand.getChildAt(hands_pos.get(pos.getId())), hand);
                        } else {
                            for (int i = 0; i < hands.size(); i++) {

                                if (hands.get(i).getId().equals(pos.getId())) {
                                    hands_pos.put(hands.get(i).getId(), i);
                                }
                            }
                            if (hands_pos.containsKey(pos.getId())) {
                                baseListAdapterhand.getView(hands_pos.get(pos.getId()), hand.getChildAt(hands_pos.get(pos.getId())), hand);
                            }
                        }

                    }
                });

    }

    public void deleteUpload(final View view) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.deleteUpload);
                    obj.put("fid", fid);
                    obj.put("id", id);
                    obj.put("type", 1);
                    for (int i = 0; i < photos.size(); i++) {
                        Courseware courseware_ben = photos.get(i);
                        if (view.getTag().toString().equals(courseware_ben.getId())) {
                            obj.put("filename", courseware_ben.getFile_path());
                        }
                    }
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("删除文件", result);
                    Message message = new Message();
                    message.what = 3;
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

    public void deleteUpload2(final View view) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.deleteUpload);
                    obj.put("fid", fid);
                    obj.put("id", id);
                    obj.put("type", 2);
                    for (int i = 0; i < hands.size(); i++) {
                        Courseware courseware_ben = hands.get(i);
                        if (view.getTag().toString().equals(courseware_ben.getFile_path())) {
                            obj.put("filename", courseware_ben.getFile_path());
                        }
                    }
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("删除文件", result);
                    Message message = new Message();
                    message.what = 4;
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

    // 删除教学视频
    public void deleteUpload3(final View view) {
        if (video_is_uploading) {
            Toast.makeText(context, "正在上传视频，请稍后再试~", Toast.LENGTH_SHORT).show();
            return;
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.deleteUpload);
                    obj.put("fid", fid);
                    obj.put("id", id);
                    obj.put("type", 5);
                    for (int i = 0; i < videoList.size(); i++) {
                        Courseware courseware_ben = videoList.get(i);
                        if (view.getTag().toString().equals(courseware_ben.getFile_path())) {
                            obj.put("filename", courseware_ben.getFile_path());
                        }
                    }
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("删除文件", result);
                    Message message = new Message();
                    message.what = 8;
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

    public void submitData() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.submitData);
                    obj.put("fid", fid);
                    obj.put("id", id);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("提交教学活动", result);
                    Message message = new Message();
                    message.what = 5;
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

    public void activitiesCode() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.activitiesCode);
                    obj.put("fid", fid);
                    obj.put("id", id);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("二维码获得", result);
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

    @Override
    protected void onStop() {
        try {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
    }

    public void open_Coursewares(View view) {
        Intent intent = new Intent(context, com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management.Courseware.class);
        //把返回数据存入Intent
        Bundle bundle = new Bundle();
        bundle.putString("http", http);
        bundle.putString("fid", fid);
        bundle.putString("id", id);
        bundle.putString("type", is_upload_files.equals("1") ? "1" : "2");//1代表新发布或者活动未开始可编辑 2.代表活动已开始 不可编辑
        bundle.putSerializable("list", (Serializable) coursewares);//序列化,要注意转化(Serializable)
        intent.putExtras(bundle);//发送数据

        startActivity(intent);
    }

    public void selectParticipant(View view) {
        Intent intent = new Intent(context, Event_Details_Participant.class);

        //把返回数据存入Intent
        Bundle bundle = new Bundle();
        bundle.putString("fid", fid);
        bundle.putString("id", id);
        bundle.putString("type", "2");//1代表新发布或者活动未开始可编辑 2.代表活动已开始 不可编辑
        bundle.putSerializable("list", (Serializable) residents);//序列化,要注意转化(Serializable)
        intent.putExtras(bundle);//发送数据
        startActivity(intent);
    }

    public void selectLecturer(View view) {
        Intent intent = new Intent(context, Event_Details_lecturer.class);
        //把返回数据存入Intent
        Bundle bundle = new Bundle();
        bundle.putString("fid", fid);
        bundle.putString("id", id);
        bundle.putString("type", "2");//1代表新发布或者活动未开始可编辑 2.代表活动已开始 不可编辑
        bundle.putSerializable("list", (Serializable) lecturer_data);//序列化,要注意转化(Serializable)
        intent.putExtras(bundle);//发送数据
        startActivity(intent);
    }

    public void openLeave(View view) {
        Intent intent = new Intent(context, Event_Details_Leave.class);
        //把返回数据存入Intent
        Bundle bundle = new Bundle();
        bundle.putString("fid", fid);
        bundle.putString("id", id);
        bundle.putString("type", "2");//1代表新发布或者活动未开始可编辑 2.代表活动已开始 不可编辑
        bundle.putSerializable("list", (Serializable) leave_data);//序列化,要注意转化(Serializable)
        intent.putExtras(bundle);//发送数据
        startActivity(intent);
    }

    public void openAccompanying_notes(View view) {
        Intent intent = new Intent(context, Event_Details_Accompanying_notes_Students.class);
        //把返回数据存入Intent
        Bundle bundle = new Bundle();
        bundle.putString("fid", fid);
        bundle.putString("id", id);
        bundle.putString("http", http);

        bundle.putSerializable("list", (Serializable) residents);//序列化,要注意转化(Serializable)
        intent.putExtras(bundle);//发送数据
        startActivity(intent);
    }

    public void selectTeaching(View view) {
        Intent intent = new Intent(context, Event_Details_Teaching.class);

        //把返回数据存入Intent
        Bundle bundle = new Bundle();
        bundle.putString("fid", fid);
        bundle.putString("id", id);
        bundle.putString("type", "2");//1代表新发布或者活动未开始可编辑 2.代表活动已开始 不可编辑
        bundle.putSerializable("list", (Serializable) teaching_data);//序列化,要注意转化(Serializable)
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
