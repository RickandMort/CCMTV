package com.linlic.ccmtv.yx.activity.rules_to_compensate.gp_exam;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.DateUtil;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.othershe.calendarview.bean.DateBean;
import com.othershe.calendarview.listener.OnSingleChooseListener;
import com.othershe.calendarview.utils.CalendarUtil;
import com.othershe.calendarview.weiget.CalendarView;

import org.angmarch.views.NiceSpinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MakeApplyForDelayActivity extends BaseActivity {

    @Bind(R.id.arrow_back)
    LinearLayout arrowBack;
    @Bind(R.id.activity_title_name)
    TextView activityTitleName;
    @Bind(R.id.rl_choose_ks)
    RelativeLayout rlChooseKs;
    @Bind(R.id.tv_plan_time)
    TextView tvPlanTime;
    @Bind(R.id.tv_start_time)
    TextView tvStartTime;
    @Bind(R.id.tv_end_time)
    TextView tvEndTime;
    @Bind(R.id.et_reason)
    EditText etReason;
    @Bind(R.id.btn_summit)
    Button btnSummit;
    @Bind(R.id.nice_spinner)
    NiceSpinner niceSpinner;
    @Bind(R.id.tv_look)
    TextView tvLook;
    private Dialog dialog;
    private Dialog look_dialog;
    private View layout_view;
    private int flag = -1;
    private List<String> sp_list = new ArrayList<>();
    private Context context;
    private List<Map<String, Object>> list = new ArrayList<>();
    private String ks_id;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getString("code").equals("200")) {
                            JSONObject data = result.getJSONObject("data");
                            if (data.getString("status").equals("1")) {
                                JSONArray jsonArray = data.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<>();
                                    if(i==0){
                                      map.put("standard_name","选择科室");
                                    }else {
                                      map.put("standard_name", object.getString("standard_name"));
                                      map.put("standard_kid", object.getString("standard_kid"));
                                        JSONArray array = object.getJSONArray("time_list");
                                        List<String> time_list = new ArrayList<>();
                                        for (int j = 0; j < array.length(); j++) {
                                            //Map<String,Object> t_map = new HashMap<>();
                                            //t_map.put("time",array.getJSONObject(j).getString("time"));
                                            time_list.add(array.getJSONObject(j).getString("time"));
                                        }
                                        map.put("times", time_list);
                                    }

                                    list.add(map);
                                }
                                for (int i = 0; i < list.size(); i++) {
                                    sp_list.add(list.get(i).get("standard_name").toString());
                                }
                                niceSpinner.attachDataSource(sp_list);
                                niceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        try {
                                            if(i>0){
                                                JSONArray times_array = new JSONArray(list.get(i).get("times").toString());
                                                if(times_array.length()>1){
                                                    tvLook.setVisibility(View.VISIBLE);
                                                }else {
                                                    tvLook.setVisibility(View.GONE);
                                                }
                                                String times = "";
                                                for (int k = 0; k < times_array.length(); k++) {
                                                    times += times_array.get(k) + "," + "\n\n";
                                                }
                                                if (!times.equals("")) {
                                                    tvPlanTime.setText(times.substring(0, times.lastIndexOf(",")));
                                                } else {
                                                    tvPlanTime.setText("");
                                                }
                                                ks_id = list.get(i).get("standard_kid").toString();
                                            }else {
                                                tvLook.setVisibility(View.GONE);
                                                tvPlanTime.setText("");
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });
//                                JSONArray zero_array = new JSONArray(list.get(0).get("times").toString());
//                                if(zero_array.length()>1){
//                                  tvLook.setVisibility(View.VISIBLE);
//                                }else {
//                                  tvLook.setVisibility(View.GONE);
//                                }
                                //tvPlanTime.setText(new JSONArray(list.get(0).get("times").toString()).get(0).toString());
                                //ks_id = list.get(0).get("standard_kid").toString();

                            } else {
                                Toast.makeText(context, data.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, result.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        MyProgressBarDialogTools.hide();
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getString("code").equals("200")) {
                            JSONObject data = result.getJSONObject("data");
                            if (data.getString("status").equals("1")) {
                                toastShort(data.getString("msg"));
                                finish();
                            } else {
                                Toast.makeText(context, data.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, result.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        MyProgressBarDialogTools.hide();
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_apply_for_delay);
        ButterKnife.bind(this);
        context = MakeApplyForDelayActivity.this;
        getstudentList();
    }

    @OnClick({R.id.rl_choose_ks, R.id.tv_start_time, R.id.tv_end_time, R.id.btn_summit,R.id.tv_look})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_choose_ks:
                break;
            case R.id.tv_start_time:
                flag = 0;
                To_calendarView(view);
                break;
            case R.id.tv_end_time:
                flag = 1;
                To_calendarView(view);
                break;
            case R.id.btn_summit:
                if (tvPlanTime.getText().toString().trim().equals("")) {
                    toastShort("请选择科室");
                    return;
                }
                if (tvStartTime.getText().toString().trim().equals("请选择")) {
                    toastShort("请选择开始时间");
                    return;
                }
                if (tvEndTime.getText().toString().trim().equals("请选择")) {
                    toastShort("请选择结束时间");
                    return;
                }
                if (etReason.getText().toString().trim().equals("")) {
                    toastShort("请输入延期原因");
                    return;
                }
                submit_apply();
                break;
            case R.id.tv_look:
                dialog_look();
                break;
        }
    }


    public void getstudentList() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.userCyclePlan);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("fid", getIntent().getExtras().getString("fid"));
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("轮转计划列表", result);
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

    public void submit_apply() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.postponeApply);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("ks_id", ks_id);
                    obj.put("original_time", tvPlanTime.getText().toString());
                    obj.put("start_time", tvStartTime.getText().toString());
                    obj.put("end_time", tvEndTime.getText().toString());
                    obj.put("reason", etReason.getText().toString());
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.GpSubmitApply, obj.toString());
                    LogUtil.e("提交延迟申请", result);
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

    private void dialog_look(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View look_view = inflater.inflate(R.layout.delay_dialog, null);
        TextView tv_times = look_view.findViewById(R.id.tv_times);
        TextView tv_close = look_view.findViewById(R.id.tv_close);
        // 对话框
        look_dialog = new Dialog(this);
        look_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        look_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        look_dialog.show();
        // 设置宽度为屏幕的宽度
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = look_dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() - 240); // 设置宽度
        look_dialog.getWindow().setAttributes(lp);
        look_dialog.getWindow().setContentView(look_view);
        look_dialog.setCancelable(true);
        tv_times.setText(tvPlanTime.getText().toString());
        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                look_dialog.dismiss();
            }
        });

    }

    /*
      时间选择器
     */
    public void To_calendarView(View view) {
        final TextView textView = (TextView) view;
        // 弹出自定义dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        layout_view = inflater.inflate(R.layout.dialog_item15, null);

        // 对话框
        dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        // 设置宽度为屏幕的宽度
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() - 100); // 设置宽度
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setContentView(layout_view);
        dialog.setCancelable(true);

        final CalendarView calendarView = (CalendarView) layout_view.findViewById(R.id.calendar);
//日历init，年月日之间用点号隔开
        int[] currenDate = CalendarUtil.getCurrentDate();
        calendarView
                .setInitDate(currenDate[0] + "." + currenDate[1])//设置日历的初始显示年月
//                .setStartEndDate(currenDate[0] + "." + currenDate[1], currenDate[0] + "." + currenDate[1])//设置日历开始、结束年月
                .setSingleDate(DateUtil.format(textView.getText().toString(), "yyyy.MM.dd"))//设置单选时初始选中的日期（不设置则不默认选中）
//                .setDisableStartEndDate(input_starttime.replaceAll("-", "."), input_endtime.replaceAll("-", "."))
                .init();

        calendarView.setOnSingleChooseListener(new OnSingleChooseListener() {
            @Override
            public void onSingleChoose(View view, DateBean dateBean) {
                if (flag == 0) {
                    tvStartTime.setText(dateBean.getSolar()[0] + "-" + (dateBean.getSolar()[1] > 9 ? dateBean.getSolar()[1] : "0" + dateBean.getSolar()[1]) + "-" + (dateBean.getSolar()[2] > 9 ? dateBean.getSolar()[2] : "0" + dateBean.getSolar()[2]));
                } else {
                    tvEndTime.setText(dateBean.getSolar()[0] + "-" + (dateBean.getSolar()[1] > 9 ? dateBean.getSolar()[1] : "0" + dateBean.getSolar()[1]) + "-" + (dateBean.getSolar()[2] > 9 ? dateBean.getSolar()[2] : "0" + dateBean.getSolar()[2]));
                }
                LogUtil.e("dateBean", "当前选中的日期：" + dateBean.getSolar()[0] + "年" + dateBean.getSolar()[1] + "月" + dateBean.getSolar()[2] + "日");
                dialog.dismiss();
                dialog = null;
                layout_view = null;

            }
        });

    }
}
