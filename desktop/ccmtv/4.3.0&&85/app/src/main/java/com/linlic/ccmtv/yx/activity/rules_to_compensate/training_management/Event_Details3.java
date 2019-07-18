package com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.EditText;
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
import com.linlic.ccmtv.yx.activity.upload.PicViewerActivity;
import com.linlic.ccmtv.yx.activity.upload.VideoPlayerActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyGridView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/8/29. 活动详情
 */

public class Event_Details3 extends BaseActivity {
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


    @Bind(R.id.reason_for_review)
    TextView reason_for_review;//审核理由
    @Bind(R.id.approval_status)
    TextView approval_status;//审核状态
    @Bind(R.id.approval_layout)
    LinearLayout approval_layout;//审核模块
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
    @Bind(R.id.submit_layout3)
    LinearLayout submit_layout3;//  手写记录与现场照片提交确认容器
    @Bind(R.id.submit_layout3_text)
    EditText submit_layout3_text;//  手写记录与现场照片提交确认 提示语
    @Bind(R.id.submit_text)
    TextView submit_text;//  手写记录与现场照片提交确认 提交
    @Bind(R.id.close_text)
    TextView close_text;//  手写记录与现场照片提交确认 取消
    @Bind(R.id.status_img)
    ImageView status_img;//  状态图片
    @Bind(R.id.lecturer_layout)
    LinearLayout lecturer_layout;//讲师 模块 容器
    @Bind(R.id.lecturer_num)
    TextView lecturer_num;//讲师 人数字段
    @Bind(R.id.leave_layout)
    LinearLayout leave_layout;//请假 模块 容器
    @Bind(R.id.leave_text)
    TextView leave_text;//请假title
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
    @Bind(R.id.partivipant_num)
    TextView partivipant_num;//参与人员 人数字段
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

    public List<Resident> teaching_data = new ArrayList<>();//带教 数据
    private String is_code = "1";//1是动态二维码 2是固定二维码
    private List<Event_Details_Leave_bean> leave_data = new ArrayList<Event_Details_Leave_bean>();
    Map<String, Object> leave_select = new HashMap<>();//当前审核 学员请假

    private Long set_time = 0L;//二维码刷新时间
    private String id = "";//教学活动ID
    private String w_id = "";//审核活动ID
    private String ing = "";//二维码可点开 不可点开 状态
    private String w_status = "";//审核状态
    List<Courseware> coursewares = new ArrayList<>();//课件
    List<Courseware> photos = new ArrayList<>();//和学生合照
    List<Courseware> hands = new ArrayList<>();//手写记录
    List<Courseware> videoList = new ArrayList<>();//课程录制
    List<Resident> residents = new ArrayList<>();//住院医师
    List<Resident> lecturer_data = new ArrayList<>();//讲师 数据
    Map<String, Integer> coursewares_pos = new HashMap<>();
    Map<String, Integer> photos_pos = new HashMap<>();
    Map<String, Integer> hands_pos = new HashMap<>();
    Map<String, Integer> videos_pos = new HashMap<>();
    private List<String> allKeshi_list = new ArrayList<>(), allStatus_list = new ArrayList<>();//活动类型数据
    JSONObject result, data;
    private String fid = "";
    private String http = "";
    private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
    private BaseListAdapter baseListAdapterphoto;
    private BaseListAdapter baseListAdapterhand;
    private BaseListAdapter baseListAdapterVideo; // 课程录制的


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
                                remarks.setText(Html.fromHtml(dateJson.getString("message")));
                                http = dateJson.getString("http");
                                ing = dateJson.getString("ing");
                                is_code = dateJson.getString("is_code");
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
                                }


                                JSONObject Event_json = new JSONObject();
                                Event_json.put(id, dateJson.getString("status_sub"));
                                SharedPreferencesTools.saveEvent_details_status(context, Event_json.toString());

                                switch (dateJson.getString("work_status")) {
                                    case "0"://审核中
                                        status_img.setImageResource(R.mipmap.training_39);
                                        approval_layout.setVisibility(View.VISIBLE);
                                        reason_for_review.setVisibility(View.GONE);
                                        close_layout.setVisibility(View.VISIBLE);
                                        submit_layout2.setVisibility(View.VISIBLE);
                                        approval_status.setText("审核中");
                                        break;
                                    case "1"://审核通过
                                        status_img.setImageResource(R.mipmap.training_35);
                                        approval_layout.setVisibility(View.VISIBLE);
                                        reason_for_review.setVisibility(View.GONE);
                                        close_layout.setVisibility(View.GONE);
                                        submit_layout2.setVisibility(View.GONE);
                                        approval_status.setText("审核通过");
                                        break;
                                    case "2"://审核失败
                                        status_img.setImageResource(R.mipmap.training_38);
                                        approval_layout.setVisibility(View.VISIBLE);
                                        reason_for_review.setVisibility(View.VISIBLE);
                                        close_layout.setVisibility(View.GONE);
                                        submit_layout2.setVisibility(View.GONE);
                                        reason_for_review.setText(dateJson.getString("work_errormsg"));
                                        approval_status.setText("审核失败");
                                        break;
                                    default:
                                        break;

                                }

                                for (int i = 0; i < dateJson.getJSONArray("photo").length(); i++) {
                                    JSONObject kejianJson = dateJson.getJSONArray("photo").getJSONObject(i);
                                    Courseware courseware_bean = new Courseware();
                                    courseware_bean.setFile_path(kejianJson.getString("photo"));
                                    if (dateJson.getString("status_sub").equals("1") || dateJson.getString("status_sub").equals("2") || dateJson.getString("status_sub").equals("3")) {
                                        courseware_bean.setIs_upload(true);
                                    } else {
                                        courseware_bean.setIs_upload(false);
                                    }
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
                                    if (dateJson.getString("status_sub").equals("1") || dateJson.getString("status_sub").equals("2") || dateJson.getString("status_sub").equals("3")) {
                                        courseware_bean.setIs_upload(true);
                                    } else {
                                        courseware_bean.setIs_upload(false);
                                    }
                                    courseware_bean.setId("" + (i + 1));
                                    hands.add(courseware_bean);
                                    hands_pos.put(courseware_bean.getId(), hands.size() - 1);
                                }

                                if (hands.size() < 1) {
                                    hand_layout.setVisibility(View.GONE);
                                } else {
                                    hand_layout.setVisibility(View.VISIBLE);
                                }

                                baseListAdapterhand.notifyDataSetChanged();

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
                                    resident.setKsname(dataJson1.getString("hospital_name"));
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
                                for (Courseware courseware_ben : photos) {
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

                case 5:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            MyProgressBarDialogTools.hide();
                            if (dataJson.getInt("status") == 1) { // 成功
                                submit_layout3.setVisibility(View.GONE);
                                JSONObject Event_json = new JSONObject();
                                Event_json.put(w_id, w_status);
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
        setContentView(R.layout.event_details3);
        context = this;
        ButterKnife.bind(this);
        initView();
        id = getIntent().getStringExtra("id");
        w_id = getIntent().getStringExtra("w_id");
        fid = getIntent().getStringExtra("fid");
        getUrlRulest();
        getActivitiesUsers();
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


        close_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit_layout3.setVisibility(View.GONE);
            }
        });
        submit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                w_status = "2";
                if (submit_layout3_text.getText().toString().trim().length() > 0) {
                    activitiesWork(submit_layout3_text.getText().toString().trim());
                } else {
                    Toast.makeText(context, "请填写退回原因", Toast.LENGTH_SHORT).show();
                }


            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit_layout3.setVisibility(View.VISIBLE);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                w_status = "1";
                activitiesWork(submit_layout3_text.getText().toString().trim());
            }
        });
        the_activity_type.setTextColor(Color.BLACK);


        //------教学活动录制 begin--------//
        baseListAdapterVideo = new BaseListAdapter(video_grid, videoList, R.layout.item_detail_video_audit) {

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

                        // 调用本地播放器
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

                helper.setVisibility(R.id._item_del, View.GONE);
            }
        };
        photo.setAdapter(baseListAdapterphoto);
        photo.setSelector(new ColorDrawable(Color.TRANSPARENT));
        photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList urls_huanzhexinxi = new ArrayList();
                for (Courseware courseware : photos) {
                    urls_huanzhexinxi.add(http + "" + courseware.getFile_path());
                }
                Intent intent = new Intent(context, PicViewerActivity.class);
                intent.putExtra("type", "my_case");
                intent.putExtra("urls_case", urls_huanzhexinxi);
                intent.putExtra("current_index", position);
                startActivity(intent);

            }
        });
        baseListAdapterhand = new BaseListAdapter(hand, hands, R.layout.item_coursewares2) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Courseware map = (Courseware) item;
                helper.setText(R.id._item_text, map.getFile_name());
            }
        };
        hand.setAdapter(baseListAdapterhand);
        hand.setSelector(new ColorDrawable(Color.TRANSPARENT));
        hand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList urls_huanzhexinxi = new ArrayList();
                for (Courseware courseware : hands) {
                    urls_huanzhexinxi.add(http + "" + courseware.getFile_path());
                }
                Intent intent = new Intent(context, PicViewerActivity.class);
                intent.putExtra("type", "my_case");
                intent.putExtra("urls_case", urls_huanzhexinxi);
                intent.putExtra("current_index", position);
                startActivity(intent);


            }
        });


    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
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
                    obj.put("w_id", w_id);
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


    public void activitiesWork(final String errormsg) {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.activitiesWork);
                    obj.put("fid", fid);
                    obj.put("id", w_id);
                    obj.put("primaryId", id);
                    obj.put("status", w_status);
                    if (errormsg.length() > 0) {
                        obj.put("errormsg", errormsg);
                    }
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("审核 通过 or 退回", result);
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


    @Override
    protected void onStop() {
        super.onStop();
    }

    public void open_Coursewares(View view) {
        Intent intent = new Intent(context, com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management.Courseware.class);
        //把返回数据存入Intent
        Bundle bundle = new Bundle();
        bundle.putString("http", http);
        bundle.putString("fid", fid);
        bundle.putString("id", id);
        bundle.putString("type", "2");//1代表新发布或者活动未开始可编辑 2.代表活动已开始 不可编辑
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
}
