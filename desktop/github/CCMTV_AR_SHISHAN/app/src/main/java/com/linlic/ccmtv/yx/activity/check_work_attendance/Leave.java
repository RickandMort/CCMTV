package com.linlic.ccmtv.yx.activity.check_work_attendance;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.CustomDatePicker;

import org.angmarch.views.NiceSpinner;
import org.feezu.liuli.timeselector.TimeSelector;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.common.util.MD5;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/** 考勤请假
 * Created by tom on 2019/1/7.
 */

public class Leave extends BaseActivity{
    private Context context;
    @Bind(R.id.start_time)
    TextView start_time;
    @Bind(R.id.end_time)
    TextView end_time;
    @Bind(R.id.submit)
    TextView submit;
    @Bind(R.id.excuse_for_leave)//关键字搜索输入框
            EditText excuse_for_leave;
    @Bind(R.id.the_activity_type)//条件选择框
            NiceSpinner the_activity_type;
    private String fid = "";
    private List<String> spinner_list = new ArrayList<>() ;//类型数据
    Map<String, Object> spinner_map = new HashMap<>();
    CustomDatePicker customDatePicker1, customDatePicker2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            MyProgressBarDialogTools.hide();
                            if (dataJson.getInt("status") == 1) { // 成功
                                Toast.makeText(context, dataJson.getString("msg"), Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(context, dataJson.getString("msg"), Toast.LENGTH_SHORT).show();
                                submit.setClickable(true);
                            }
                        }else{
                            MyProgressBarDialogTools.hide();
                            submit.setClickable(true);
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        submit.setClickable(true);
                        MyProgressBarDialogTools.hide();
                        e.printStackTrace();
                    }finally {
                        submit.setClickable(true);
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
                                spinner_list.clear();
                                spinner_map.clear();
                                for(int i = 0; i<dateJson.length();i++){
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    spinner_map.put(dataJson1.getString("typeName"),dataJson1.getString("typeId") );
                                    spinner_list.add(dataJson1.getString("typeName"));
                                }
                                initthe_activity_type();
                                MyProgressBarDialogTools.hide();
                            } else {
                                MyProgressBarDialogTools.hide();
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            MyProgressBarDialogTools.hide();
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
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
        setContentView(R.layout.leave);
        context = this;
        ButterKnife.bind(this);
        findId();
        initViews();
        getLeaveType();
    }

    public void initthe_activity_type(){
        the_activity_type.setTextColor(Color.BLACK);
        the_activity_type.attachDataSource(spinner_list);
        the_activity_type.setArrowDrawable(getResources().getDrawable(R.mipmap.down_icon));

    }
    private void initViews() {
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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setClickable(false);
                if(excuse_for_leave.getText().toString().trim().length()>0){
                    addAskLeave();
                }else{
                    v.setClickable(true);
                    Toast.makeText(getApplicationContext(), "请输入请假事由！", Toast.LENGTH_SHORT).show();
                }
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

    public void getLeaveType() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj_sj = new JSONObject();
                    obj_sj.put("act", URLConfig.getTimestamp);
                    String result_sj = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_kq, obj_sj.toString());
                    JSONObject jsonObject = new JSONObject(result_sj);
                    if (jsonObject.getInt("code") == 200) {
                        JSONObject dataJson = jsonObject.getJSONObject("data");

                        if (dataJson.getInt("status") == 1) { // 成功
                            JSONObject obj = new JSONObject();
                            obj.put("act", URLConfig.getLeaveType);
                            obj.put("uid", SharedPreferencesTools.getUid(context));
                            obj.put("timestamp", dataJson.getJSONObject("data").getString("timestamp"));
                            obj.put("token", MD5.md5(URLConfig.key + dataJson.getJSONObject("data").getString("timestamp") + SharedPreferencesTools.getUid(context)));

                            String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_kq, obj.toString());
                            Message message = new Message();
                            message.what = 2;
                            message.obj = result;
                            handler.sendMessage(message);
                        } else {
                            Toast.makeText(context, jsonObject.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }


            }
        };
        new Thread(runnable).start();
    }

    public void addAskLeave() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj_sj = new JSONObject();
                    obj_sj.put("act", URLConfig.getTimestamp);
                    String result_sj = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_kq, obj_sj.toString());
                    JSONObject jsonObject = new JSONObject(result_sj);
                    if (jsonObject.getInt("code") == 200) {
                        JSONObject dataJson = jsonObject.getJSONObject("data");

                        if (dataJson.getInt("status") == 1) { // 成功
                            JSONObject obj = new JSONObject();
                            obj.put("act", URLConfig.addAskLeave);
                            obj.put("uid", SharedPreferencesTools.getUid(context));
                            obj.put("timestamp", dataJson.getJSONObject("data").getString("timestamp"));
                            obj.put("token", MD5.md5(URLConfig.key + dataJson.getJSONObject("data").getString("timestamp") + SharedPreferencesTools.getUid(context)));
                            obj.put("typeId", spinner_map.containsKey(the_activity_type.getText().toString())?spinner_map.get(the_activity_type.getText().toString()):"");
                            obj.put("starttime", start_time.getText().toString());
                            obj.put("endtime", end_time.getText().toString());
                            obj.put("reason", excuse_for_leave.getText().toString().trim());

                            String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_kq, obj.toString());
                            Message message = new Message();
                            message.what = 1;
                            message.obj = result;
                            handler.sendMessage(message);
                        } else {
                            Toast.makeText(context, jsonObject.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }


            }
        };
        new Thread(runnable).start();
    }

}
