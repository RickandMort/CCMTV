package com.linlic.ccmtv.yx.activity.rules_to_compensate.college_level_activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.linlic.ccmtv.yx.activity.entity.Resident;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management.Event_Details_Teaching;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management.Event_Details_lecturer;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management.Training_management;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.angmarch.views.NiceSpinner;
import org.feezu.liuli.timeselector.TimeSelector;
import org.json.JSONArray;
import org.json.JSONObject;

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
 * 院级活动 发布活动
 * Created by Administrator on 2018/8/21.
 */

public class New_College_level_activities extends BaseActivity {
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
     @Bind(R.id.partivipant_num)
    TextView partivipant_num;//参与人员 人数字段


    public static List<Courseware> coursewares_college = new ArrayList<>();//课件
    public static  Map<String, Integer> coursewares_pos_college = new HashMap<>();
    public static JSONArray del_coursewares_college = new JSONArray();
    public static List<Resident> residents_college = new ArrayList<>();//参与人员

    private List<String> allKeshi_list = new ArrayList<>(), allStatus_list = new ArrayList<>();//活动类型数据
    Map<String, Object> allkeshi_map = new HashMap<>();
    Event_Details_Leave_bean leave_select =new Event_Details_Leave_bean();//当前审核 学员请假
            JSONObject result, data;
    private String fid = "";
    private String id = "";//活动ID
    private String message_new = "";//备注
    private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

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
                                JSONArray dateJson = dataJson.getJSONArray("data");
                                for (int i = 0; i < dateJson.length(); i++) {
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    allkeshi_map.put(dataJson1.getString("cate_name"), dataJson1.getString("cate_id"));
                                    allKeshi_list.add(dataJson1.getString("cate_name"));
                                }
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                            if (allKeshi_list.size() > 0) {

                                initthe_activity_type();
                            }

                            if (id!=null && id.trim().length() > 0) {
                                getUrlRulest();

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
                                Training_management.is_new_teaching_activities = true;
                                Toast.makeText(context, dataJson.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                                Intent  intent = new Intent(context, College_level_activities_Details.class);
                                intent.putExtra("id", dataJson.getJSONObject("data").getString("id"));
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(context, dataJson.getString("statusMsg"), Toast.LENGTH_SHORT).show();
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
                                JSONObject dateJson = dataJson.getJSONObject("data");
                                activity_name.setText(dateJson.getString("title"));
                                for (int i = 0; i < allKeshi_list.size(); i++) {
                                    if (allKeshi_list.get(i).equals(dateJson.getString("cate_name"))) {
                                        the_activity_type.setSelectedIndex(i);
                                    }
                                }
                                start_time.setText(dateJson.getString("start_time"));
                                end_time.setText(dateJson.getString("end_time"));
                                place.setText(dateJson.getString("address"));


                                JSONObject Event_json = new JSONObject();
                                Event_json.put(id, dateJson.getString("status"));
                                SharedPreferencesTools.saveEvent_details_status(context, Event_json.toString());

                                for (int i = 0; i < dateJson.getJSONArray("files").length(); i++) {
                                    JSONObject kejianJson = dateJson.getJSONArray("files").getJSONObject(i);
                                    Courseware courseware_bean = new Courseware();
                                    courseware_bean.setFile_name(kejianJson.getString("name"));
                                    courseware_bean.setFile_path(kejianJson.getString("url"));
                                    courseware_bean.setIs_upload(true);
                                    coursewares_college.add(courseware_bean);
                                    coursewares_pos_college.put(courseware_bean.getId(), coursewares_college.size() - 1);
                                }


                                JSONArray user_list = dateJson.getJSONArray("user_list");
                                for (int i = 0; i < user_list.length(); i++) {
                                    JSONObject dataJson1 = user_list.getJSONObject(i);
                                    Resident resident = new Resident();
                                    resident.setId(dataJson1.getString("uid"));
                                    resident.setIs_select(true);
                                    resident.setName(dataJson1.getString("realname"));
                                    resident.setUsername(dataJson1.getString("username"));
                                    resident.setImgUrl(dataJson1.getString("IDphoto"));

                                    residents_college.add(resident);
                                }

                                partivipant_num.setText(residents_college.size()+"人");



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

                case 6:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                for (Courseware courseware_ben : coursewares_college) {
                                    if (dataJson.getString("dataList").equals(courseware_ben.getFile_path())) {
                                        coursewares_college.remove(courseware_ben);
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
        setContentView(R.layout.new_college_level_activities);
        context = this;
        ButterKnife.bind(this);
        fid = getIntent().getStringExtra("fid");
        id = getIntent().getStringExtra("id");


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
                    obj.put("act", URLConfig.activitysCateList);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    obj.put("fid", fid);

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
        coursewares_college.clear();
        coursewares_pos_college.clear();
        residents_college.clear();
        del_coursewares_college = new JSONArray();

        submit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit_layout.setVisibility(View.GONE);
                switch (getTimeCompareSize(start_time.getText().toString(),end_time.getText().toString())){
                    case 1:
                        Toast.makeText(getApplicationContext(), "结束时间不能小于开始时间！", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "开始时间不能与结束时间相同！", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        if(allKeshi_list.size()>0){
                            activitiesAdd();
                        }else{
                            Toast.makeText(getApplicationContext(), "暂无活动分类，请联系管理员！", Toast.LENGTH_SHORT).show();
                        }

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
                if (residents_college.size() < 1) {
                    Toast.makeText(context, "请选择人员", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (Courseware courseware : coursewares_college) {
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
                    obj.put("act", URLConfig.activitysInfo);
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


    public void activitiesAdd() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    if (id != null && id.length() > 0) {
                        obj.put("act", URLConfig.activitysAdd);
                        obj.put("fid", fid);
                        obj.put("uid", SharedPreferencesTools.getUid(context));
                        obj.put("id", id);
                        obj.put("title", activity_name.getText().toString());
                        obj.put("address", place.getText().toString());
                        obj.put("cate_id", allkeshi_map.get(the_activity_type.getText()));
                        obj.put("start_time", start_time.getText().toString());
                        obj.put("end_time", end_time.getText().toString());
                          String user_ids = "";
                        for (Resident courseware : residents_college) {
                            if(user_ids.length()>0){
                                user_ids += ","+courseware.getId();
                            }else{
                                user_ids = courseware.getId();
                            }
                        }
                        obj.put("uids", user_ids);

                        //课件
                        JSONArray kejian = new JSONArray();

                        for (Courseware courseware : coursewares_college) {
                            JSONObject kejian_name = new JSONObject();
                            kejian_name.put("url",courseware.getFile_path());
                            kejian_name.put("name",courseware.getFile_name());
                            kejian.put(kejian_name);
                        }
                        obj.put("files", kejian.toString());
                    } else {
                        obj.put("act", URLConfig.activitysAdd);
                        obj.put("fid", fid);
                        obj.put("uid", SharedPreferencesTools.getUid(context));
                        obj.put("id", id);
                        obj.put("title", activity_name.getText().toString());
                        obj.put("address", place.getText().toString());
                        obj.put("cate_id", allkeshi_map.get(the_activity_type.getText()));
                        obj.put("start_time", start_time.getText().toString());
                        obj.put("end_time", end_time.getText().toString());
                        String user_ids = "";
                        for (Resident courseware : residents_college) {
                            if(user_ids.length()>0){
                                user_ids += ","+courseware.getId();
                            }else{
                                user_ids = courseware.getId();
                            }
                        }
                        obj.put("uids", user_ids);


                        //课件
                        JSONArray kejian = new JSONArray();

                        for (Courseware courseware : coursewares_college) {
                            JSONObject kejian_name = new JSONObject();
                            kejian_name.put("url",courseware.getFile_path());
                            kejian_name.put("name",courseware.getFile_name());
                            kejian.put(kejian_name);
                        }
                        obj.put("files", kejian.toString());
                    }

                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("发布院级活动", result);
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
        Intent intent = new Intent(context, Courseware_College_level.class);
        intent.putExtra("id",id);
        intent.putExtra("type","1");//1代表新发布或者活动未开始可编辑 2.代表活动已开始 不可编辑
        startActivity(intent);
    }
    public void selectParticipant(View view){
        Intent intent = new Intent(context, College_level_activities_Participant2.class);
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



    @Override
    protected void onResume() {
        super.onResume();

        partivipant_num.setText(residents_college.size()+"人");

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
