package com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Courseware;
import com.linlic.ccmtv.yx.activity.entity.Event_Details_Leave_bean;
import com.linlic.ccmtv.yx.activity.entity.Files_info;
import com.linlic.ccmtv.yx.activity.entity.Resident;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.angmarch.views.NiceSpinner;
import org.feezu.liuli.timeselector.TimeSelector;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 教学活动 员工发布活动
 * Created by Administrator on 2018/8/21.
 */

public class New_teaching_activities extends BaseActivity {

    private Context context;
    @Bind(R.id.start_time)
    TextView start_time;//自定义开始时间
    @Bind(R.id.end_time)
    TextView end_time;//自定义结束时间
    @Bind(R.id.the_activity_type)
    NiceSpinner the_activity_type;//活动类型
    @Bind(R.id.activity_name)
    EditText activity_name;//活动名
    @Bind(R.id.place)
    EditText place;//地点
    @Bind(R.id.submit_layout)
    LinearLayout submit_layout;//
    @Bind(R.id.close_text)
    TextView close_text;//
    @Bind(R.id.submit_text)
    TextView submit_text;//
    @Bind(R.id.close)
    TextView close;//
    @Bind(R.id.submit)
    TextView submit;//
    @Bind(R.id.remarks)
    TextView remarks;//备注
    @Bind(R.id.approval_layout)
    LinearLayout approval_layout;//头部
    @Bind(R.id.delete)
    TextView delete;//删除
    @Bind(R.id.lecturer_layout)
    LinearLayout lecturer_layout;//讲师 模块 容器

    @Bind(R.id.leave_layout)
    LinearLayout leave_layout;//请假 模块 容器
    @Bind(R.id.leave_text)
    TextView leave_text;//请假title
    @Bind(R.id.partivipant_num)
    TextView partivipant_num;//参与人员 人数字段
    @Bind(R.id.lecturer_num)
    TextView lecturer_num;//讲师 人数字段
    @Bind(R.id.teaching_num)
    TextView teaching_num;//带教 人数字段
    @Bind(R.id.teaching_layout)
    LinearLayout teaching_layout;//带教 模块  容器
    @Bind(R.id.delete_layout)
    LinearLayout delete_layout;//删除教学活动 弹出框容器
    @Bind(R.id.cel_del_text)
    TextView cel_del_text;//删除教学活动 取消按钮
    @Bind(R.id.del_submit_text)
    TextView del_submit_text;//删除教学活动 确认按钮
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


    private String is_code = "1";//1是动态二维码 2是固定二维码
    private boolean is_teachers = false;//1宁波李惠利医院 专属 带教老师
    public static List<Courseware> coursewares = new ArrayList<>();//课件
    public static  Map<String, Integer> coursewares_pos = new HashMap<>();
    public static JSONArray del_coursewares = new JSONArray();
    public static List<Resident> lecturer_data = new ArrayList<>();//讲师 数据
    public static List<Resident> teaching_data = new ArrayList<>();//带教 数据
    public static List<Resident> residents = new ArrayList<>();//住院医师
    public static List<Event_Details_Leave_bean> leave_data = new ArrayList<Event_Details_Leave_bean>();
    private String http = "";
    private String ing = "";//二维码可点开 不可点开 状态
    private List<String> allKeshi_list = new ArrayList<>(), allStatus_list = new ArrayList<>();//活动类型数据
    Map<String, Object> allkeshi_map = new HashMap<>();
    Event_Details_Leave_bean leave_select =new Event_Details_Leave_bean();//当前审核 学员请假
            JSONObject result, data;
    private String fid = "";
    private String id = "";//活动ID
    private String message_new = "";//备注
    private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
    public Boolean lecturer_isshow = false; //发布教学活动是否需要添加讲师模块
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
                                JSONArray dateJson = dataJson.getJSONArray("data");
                                listData.clear();
                                for (int i = 0; i < dateJson.length(); i++) {
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("title", dataJson1.getString("title"));
                                    map.put("key", dataJson1.getString("key"));
                                    map.put("month", dataJson1.getString("month"));
                                    map.put("gps_ids", dataJson1.getString("gps_ids"));
                                    map.put("s_score", dataJson1.getString("s_score"));
                                    map.put("c_num", dataJson1.getString("c_num"));
                                    map.put("t_num", dataJson1.getString("t_num"));
                                    map.put("v_score", dataJson1.getString("v_score"));
                                    map.put("other_ids", dataJson1.getString("other_ids"));
                                    listData.add(map);
                                }
//                                baseListAdapterVideo.notifyDataSetChanged();
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
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            if (dataJson.getInt("status") == 1) { // 成功
                                JSONArray dateJson = dataJson.getJSONArray("dataList");
                                for (int i = 0; i < dateJson.length(); i++) {
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    allkeshi_map.put(dataJson1.getString("type"), dataJson1.getString("id"));
                                    allKeshi_list.add(dataJson1.getString("type"));
                                }
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                            if (allKeshi_list.size() > 0) {

                                initthe_activity_type();
                            }

                            if (id!=null && id.trim().length() > 0) {
                                getUrlRulest();
                                getActivitiesUsers();
                            }
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
                                delete_layout.setVisibility(View.GONE);
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
                case 4:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            if (dataJson.getInt("status") == 1) { // 成功
                                JSONObject dateJson = dataJson.getJSONObject("dataList");
                                activity_name.setText(dateJson.getString("name"));
                                for (int i = 0; i < allKeshi_list.size(); i++) {
                                    if (allKeshi_list.get(i).equals(dateJson.getString("type_name"))) {
                                        the_activity_type.setSelectedIndex(i);
                                    }
                                }
                                start_time.setText(dateJson.getString("add_time"));
                                end_time.setText(dateJson.getString("end_time"));
                                place.setText(dateJson.getString("place"));
                                if (id != null && id.length() > 0 && dateJson.has("is_del") && dateJson.getString("is_del").equals("1")) {
                                    delete.setVisibility(View.VISIBLE);
                                }else{
                                    delete.setVisibility(View.GONE);
                                }
                                is_code = dateJson.getString("is_code");
                                if(is_code.equals("1")){
                                    dynamic_icon1.setVisibility(View.VISIBLE);
                                    dynamic_icon2.setVisibility(View.GONE);
                                    fixed_icon2.setVisibility(View.VISIBLE);
                                    fixed_icon1.setVisibility(View.GONE);
                                }else{
                                    fixed_icon1.setVisibility(View.VISIBLE);
                                    fixed_icon2.setVisibility(View.GONE);
                                    dynamic_icon2.setVisibility(View.VISIBLE);
                                    dynamic_icon1.setVisibility(View.GONE);
                                }
                                http = dateJson.getString("http");
                                ing = dateJson.getString("ing");
                                JSONObject Event_json = new JSONObject();
                                Event_json.put(id, dateJson.getString("status_sub"));
                                SharedPreferencesTools.saveEvent_details_status(context, Event_json.toString());

                                for (int i = 0; i < dateJson.getJSONArray("kejian").length(); i++) {
                                    JSONObject kejianJson = dateJson.getJSONArray("kejian").getJSONObject(i);
                                    Courseware courseware_bean = new Courseware();
                                    courseware_bean.setFile_name(kejianJson.getString("url_name"));
                                    courseware_bean.setFile_path(kejianJson.getString("url"));
                                    courseware_bean.setIs_upload(true);
                                    coursewares.add(courseware_bean);
                                    coursewares_pos.put(courseware_bean.getId(), coursewares.size() - 1);
                                }



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
                                    lecturer_num.setText(lecturer_data.size()+"人");
                                        lecturer_layout.setVisibility(View.VISIBLE);

                                }
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
                                //请假列表
                                if(dateJson.has("userLeaveList")){
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

                                    if(leave_data.size()>0){
                                        leave_layout.setVisibility(View.VISIBLE);
                                        leave_text.setText(leave_data.size()+"人");
                                    }else{
                                        leave_layout.setVisibility(View.GONE);
                                    }


                                }else{
                                    leave_layout.setVisibility(View.GONE);
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
                case 5:
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
                                    resident.setIs_temp("1");//未签到
                                    if(dataJson1.has("files_info")){
                                       JSONArray files =  dataJson1.getJSONArray("files_info");
                                        for(int j = 0 ;j < files.length();j++){
                                            JSONObject jsonObject1 = files.getJSONObject(j);
                                            Files_info files_info = new Files_info();
                                            files_info.setUrl(jsonObject1.getString("url"));
                                            files_info.setUrl_name(jsonObject1.getString("url_name"));
                                            resident.getFiles_infos().add(files_info);
                                        }
                                    }
                                    residents.add(resident);
                                }

                                partivipant_num.setText(residents.size()+"人");

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
                                for (Courseware courseware_ben : coursewares) {
                                    if (dataJson.getString("dataList").equals(courseware_ben.getFile_path())) {
                                        coursewares.remove(courseware_ben);
                                    }
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
        setContentView(R.layout.new_teaching_activities);
        context = this;
        ButterKnife.bind(this);
        fid = getIntent().getStringExtra("fid");
        id = getIntent().getStringExtra("id");
        lecturer_isshow = getIntent().getStringExtra("lecturer_isshow").equals("1")?true:false;
        is_teachers = getIntent().getStringExtra("is_teachers").equals("1")?true:false;
        message_new = getIntent().getStringExtra("message_new");

        initView();

        getUrlRulest2();

    }

    public void getUrlRulest2() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getTypes);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    obj.put("fid", fid);
                    obj.put("is_show", 1);

                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("获取教学活动类型", result);

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

    public void initthe_activity_type() {
        the_activity_type.setTextColor(Color.BLACK);
        the_activity_type.attachDataSource(allKeshi_list);
        the_activity_type.setArrowDrawable(getResources().getDrawable(R.mipmap.down_icon));
        the_activity_type.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.e("什么数据",String.valueOf(allKeshi_list.get(i)));
            }
        });
    }

    public void initView() {
        //初始化课件数组
        coursewares.clear();
        coursewares_pos.clear();
        residents.clear();
        lecturer_data.clear();
        teaching_data.clear();
        del_coursewares = new JSONArray();
        //判断是否需要显示讲师
        if(lecturer_isshow){
            lecturer_layout.setVisibility(View.VISIBLE);
        }else{
            lecturer_layout.setVisibility(View.GONE);
        }
        //判断是否需要显示带教
        if(is_teachers){
            teaching_layout .setVisibility(View.VISIBLE);
        }else{
            teaching_layout.setVisibility(View.GONE);
        }

        if (id != null && id.length() > 0) {
            approval_layout.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
        } else {
            approval_layout.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
        }

        remarks.setText(Html.fromHtml(message_new));
        submit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit_layout.setVisibility(View.GONE);
                switch (getTimeCompareSize(start_time.getText().toString(),end_time.getText().toString())){
                    case 1:
                        Toast.makeText(getApplicationContext(), "结束时间不能小于开始时间~", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "开始时间不能与结束时间相同~", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        activitiesAdd();
                        break;
                }


            }
        });

        close_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit_layout.setVisibility(View.GONE);
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
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_layout.setVisibility(View.VISIBLE);

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity_name.getText().toString().trim().length() < 1) {
                    Toast.makeText(context, "请输入活动名称", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (place.getText().toString().trim().length() < 1) {
                    Toast.makeText(context, "请输入地点", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (residents.size() < 1) {
                    Toast.makeText(context, "请选择人员", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (Courseware courseware : coursewares) {
                    if (!courseware.is_upload()) {
                        return;
                    }
                }
                submit_layout.setVisibility(View.VISIBLE);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //默认开始和结束的时间都是当前
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        start_time.setText(now);
        end_time.setText(now);

        start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                String now = sdf.format(new Date());
//                customDatePicker1.show(now);

                showDatePicker(now, "2030-12-31 23:59", start_time);
            }
        });

        end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                String now = sdf.format(new Date());
//                customDatePicker2.show(now);

                showDatePicker(now, "2030-12-31 23:59", end_time);
            }
        });





        fixed_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is_code ="2";
                fixed_icon1.setVisibility(View.VISIBLE);
                fixed_icon2.setVisibility(View.GONE);
                dynamic_icon2.setVisibility(View.VISIBLE);
                dynamic_icon1.setVisibility(View.GONE);
            }
        });
        dynamic_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is_code ="1";
                dynamic_icon1.setVisibility(View.VISIBLE);
                dynamic_icon2.setVisibility(View.GONE);
                fixed_icon2.setVisibility(View.VISIBLE);
                fixed_icon1.setVisibility(View.GONE);
            }
        });



    }

    private void showDatePicker(String now, String end_time, final TextView etTime) {
        TimeSelector timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                etTime.setText(time);
            }
        }, now, end_time);
        timeSelector.setIsLoop(false);
        timeSelector.show();
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
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("查看教学活动详情", result);
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

    public void activitiesAdd() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    if (id != null && id.length() > 0) {
                        obj.put("act", URLConfig.updateData);
                        obj.put("fid", fid);
                        obj.put("uid", SharedPreferencesTools.getUid(context));
                        obj.put("id", id);
                        JSONObject dataJson = new JSONObject();
                        dataJson.put("name", activity_name.getText().toString());
                        dataJson.put("place", place.getText().toString());
                        dataJson.put("type", allkeshi_map.get(the_activity_type.getText()));
                        dataJson.put("add_time", start_time.getText().toString());
                        dataJson.put("end_time", end_time.getText().toString());
                        dataJson.put("delete_file", del_coursewares);
                        dataJson.put("is_code", is_code);
                        dataJson.put("is_teachers", is_teachers);
//                    List<String> user_ids = new ArrayList<>();
                        //学员
                        JSONArray user_ids = new JSONArray();
                        for (Resident resident : residents) {
                            user_ids.put(resident.getId());
                        }
                        dataJson.put("user_id", user_ids);
                        //讲师
                        JSONArray lecturer_ids = new JSONArray();
                        for (Resident resident : lecturer_data) {
                            lecturer_ids.put(resident.getId());
                        }
                        dataJson.put("speaker", lecturer_ids);
                        //带教
                        JSONArray teaching_ids = new JSONArray();
                        for (Resident resident : teaching_data) {
                            teaching_ids.put(resident.getId());
                        }
                        dataJson.put("teachers", teaching_ids);
                        //课件
                        JSONArray kejian = new JSONArray();
                        JSONArray kejian_name = new JSONArray();
                        for (Courseware courseware : coursewares) {
                            kejian.put(courseware.getFile_path());
                            kejian_name.put(courseware.getFile_name());
                        }
                        dataJson.put("kejian", kejian);
                        dataJson.put("kejian_name", kejian_name);
                        obj.put("data", dataJson);
                    } else {
                        obj.put("act", URLConfig.activitiesAdd);
                        obj.put("fid", fid);
                        obj.put("uid", SharedPreferencesTools.getUid(context));
                        JSONObject dataJson = new JSONObject();
                        dataJson.put("name", activity_name.getText().toString());
                        dataJson.put("place", place.getText().toString());
                        dataJson.put("type", allkeshi_map.get(the_activity_type.getText()));
                        dataJson.put("add_time", start_time.getText().toString());
                        dataJson.put("end_time", end_time.getText().toString());
                        dataJson.put("is_code", is_code);
                        dataJson.put("is_teachers", is_teachers);
//                    List<String> user_ids = new ArrayList<>();
                        //学员
                        JSONArray user_ids = new JSONArray();
                        for (Resident resident : residents) {
                            user_ids.put(resident.getId());
                        }
                        dataJson.put("user_id", user_ids);
                        //讲师
                        JSONArray lecturer_ids = new JSONArray();
                        for (Resident resident : lecturer_data) {
                            lecturer_ids.put(resident.getId());
                        }
                        dataJson.put("speaker", lecturer_ids);
                        //带教
                        JSONArray teaching_ids = new JSONArray();
                        for (Resident resident : teaching_data) {
                            teaching_ids.put(resident.getId());
                        }
                        dataJson.put("teachers", teaching_ids);
                        //课件
                        JSONArray kejian = new JSONArray();
                        JSONArray kejian_name = new JSONArray();
                        for (Courseware courseware : coursewares) {
                            kejian.put(courseware.getFile_path());
                            kejian_name.put(courseware.getFile_name());
                        }
                        dataJson.put("kejian", kejian);
                        dataJson.put("kejian_name", kejian_name);
                        obj.put("data", dataJson);
                    }

                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("发布教学活动", result);
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

    public void open_Coursewares(View view){
        Intent intent = new Intent(context, com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management.Courseware.class);
        intent.putExtra("http",http);
        intent.putExtra("fid",fid);
        intent.putExtra("id",id);
        intent.putExtra("type","1");//1代表新发布或者活动未开始可编辑 2.代表活动已开始 不可编辑
        startActivity(intent);
    }
    public void selectParticipant(View view){
        Intent intent = new Intent(context, Event_Details_Participant.class);
        intent.putExtra("fid",fid);
        intent.putExtra("id",id);
        intent.putExtra("type","1");//1代表新发布或者活动未开始可编辑 2.代表活动已开始 不可编辑
        startActivity(intent);
    }
    public void selectLecturer(View view){
        Intent intent = new Intent(context, Event_Details_lecturer.class);
        intent.putExtra("fid",fid);
        intent.putExtra("id",id);
        intent.putExtra("type","1");//1代表新发布或者活动未开始可编辑 2.代表活动已开始 不可编辑
        startActivity(intent);
    }
    public void selectTeaching(View view){
        Intent intent = new Intent(context, Event_Details_Teaching.class);
        intent.putExtra("fid",fid);
        intent.putExtra("id",id);
        intent.putExtra("type","1");//1代表新发布或者活动未开始可编辑 2.代表活动已开始 不可编辑
        startActivity(intent);
    }
    public void openLeave(View view){
        Intent intent = new Intent(context, Event_Details_Leave.class);
        //把返回数据存入Intent
        Bundle bundle=new Bundle();
        bundle.putString("fid",fid);
        bundle.putString("id",id);
        bundle.putString("type","1");//1代表新发布或者活动未开始可编辑 2.代表活动已开始 不可编辑
        bundle.putSerializable("list",(Serializable)leave_data);//序列化,要注意转化(Serializable)
        intent.putExtras(bundle);//发送数据
        startActivity(intent);
    }



    @Override
    protected void onResume() {
        super.onResume();
        partivipant_num.setText(residents.size()+"人");
        lecturer_num.setText(lecturer_data.size()+"人");
        teaching_num.setText(teaching_data.size()+"人");
    }
    /**
     * 判断2个时间大小
     * yyyy-MM-dd HH:mm 格式（自己可以修改成想要的时间格式）
     * @param startTime
     * @param endTime
     * @return
     */
    public static int getTimeCompareSize(String startTime, String endTime){
        int i=0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");//年-月-日 时-分
        try {
            Date date1 = dateFormat.parse(startTime);//开始时间
            Date date2 = dateFormat.parse(endTime);//结束时间
            // 1 结束时间小于开始时间 2 开始时间与结束时间相同 3 结束时间大于开始时间
            if (date2.getTime()<date1.getTime()){
                i= 1;
            }else if (date2.getTime()==date1.getTime()){
                i= 2;
            }else if (date2.getTime()>date1.getTime()){
                //正常情况下的逻辑操作.
                i= 3;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  i;
    }


}
