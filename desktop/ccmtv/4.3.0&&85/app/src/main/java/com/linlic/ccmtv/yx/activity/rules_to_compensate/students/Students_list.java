package com.linlic.ccmtv.yx.activity.rules_to_compensate.students;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.gp_personal_information.GpPersonalInformationActivity3;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/6/26.
 */

public class Students_list extends BaseActivity {

    private Context context;
    @Bind(R.id.listView)
    ListView listView;

    private Dialog dialog;
    private View view ;
    JSONObject result, data;
    private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
    private BaseListAdapter baseListAdapterVideo;
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
                                JSONArray dateJson = dataJson.getJSONArray("date");
                                listData.clear();
                                for (int i = 0; i<dateJson.length();i++){
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    Map<String,Object> map = new HashMap<>();
                                    map.put("name",dataJson1.getString("realname"));
                                    map.put("educational_background",dataJson1.getString("edu_highest_education"));
                                    map.put("certificate_of_medical_practitioner",dataJson1.getString("exam_situation_is_ep"));
                                    map.put("subordinate_departments",dataJson1.getString("standard_name"));
                                    map.put("start_time",dataJson1.getString("startime"));
                                    map.put("end_time",dataJson1.getString("endtime"));
                                    map.put("uid",dataJson1.getString("uid"));
                                    listData.add(map);
                                }
                                baseListAdapterVideo.notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 2:
                    // 弹出自定义dialog
                    LayoutInflater inflater = LayoutInflater.from(Students_list.this);
                    view = inflater.inflate(R.layout.dialog_item11, null);

                    // 对话框
                    dialog = new Dialog(Students_list.this);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.show();
                    // 设置宽度为屏幕的宽度
                    WindowManager windowManager = getWindowManager();
                    Display display = windowManager.getDefaultDisplay();
                    WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                    lp.width = (int) (display.getWidth()-100); // 设置宽度
                    dialog.getWindow().setAttributes(lp);
                    dialog.getWindow().setContentView(view);
//                    dialog.setCancelable(false);
                      TextView _dialog_name = (TextView) view.findViewById(R.id._dialog_name);// 姓名
                      TextView _dialog_educational_background = (TextView) view.findViewById(R.id._dialog_educational_background);// 学历
                      TextView _dialog_certificate_of_medical_practitioner = (TextView) view.findViewById(R.id._dialog_certificate_of_medical_practitioner);// 执业医师证
                      TextView _dialog_subordinate_departments = (TextView) view.findViewById(R.id._dialog_subordinate_departments);// 所属科室
                      TextView _dialog_start_time = (TextView) view.findViewById(R.id._dialog_start_time);// 开始时间
                      TextView _dialog_end_time = (TextView) view.findViewById(R.id._dialog_end_time);// 开始时间
                    _dialog_name.setText(listData.get(Integer.parseInt(msg.obj.toString())).get("name").toString());
                    _dialog_educational_background.setText(listData.get(Integer.parseInt(msg.obj.toString())).get("educational_background").toString());
                    _dialog_certificate_of_medical_practitioner.setText(listData.get(Integer.parseInt(msg.obj.toString())).get("certificate_of_medical_practitioner").toString());
                    _dialog_subordinate_departments.setText(listData.get(Integer.parseInt(msg.obj.toString())).get("subordinate_departments").toString());
                    _dialog_start_time.setText(listData.get(Integer.parseInt(msg.obj.toString())).get("start_time").toString());
                    _dialog_end_time.setText(listData.get(Integer.parseInt(msg.obj.toString())).get("end_time").toString());


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
        setContentView(R.layout.students_list);
        context = this;
        ButterKnife.bind(this);
        findId();
        initViews();
        initDatas();
        getstudentList();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/Teacher_student/index.html";
        super.onPause();
    }

    private void initDatas() {
        listData.clear();
        baseListAdapterVideo.notifyDataSetChanged();
    }

    private void initViews() {

        baseListAdapterVideo = new BaseListAdapter(listView, listData, R.layout.item_students_list) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map map = (Map) item;
                helper.setText(R.id._item_text,map.get("name").toString());
                helper.setText(R.id._item_standard_name,map.get("subordinate_departments").toString());
            }
        };
        listView.setAdapter(baseListAdapterVideo);
        // listview点击事件
        listView.setOnItemClickListener(new  casesharing_listListener());
    }


    /**
     * name: 点击查看某个视频的详细 author:Tom 2016-1-28下午3:42:08
     */
    private class casesharing_listListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {

          /*  Message message = new Message();
            message.what = 2;
            message.obj = arg2;
            handler.sendMessage(message);*/

            Intent intent = new Intent(context, GpPersonalInformationActivity3.class);
            intent.putExtra("uid",listData.get(arg2).get("uid").toString());
            intent.putExtra("name",listData.get(arg2).get("name").toString());
            startActivity(intent);

        }

    }

    public void getstudentList() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.studentInfo);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("id",getIntent().getStringExtra("id"));
                    obj.put("fid",getIntent().getStringExtra("fid"));

                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());

                    LogUtil.e("带教学员详细", result);

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




}
