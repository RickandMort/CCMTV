package com.linlic.ccmtv.yx.activity.rules_to_compensate.the_division_management;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/7/4.
 */

public class To_deal_with_the_family extends BaseActivity {

    private Context context;
    private String fid = "";
    /* @Bind(R.id.listView)
     ListView listView;//*/
    final List<String> dataset = new ArrayList<>();
    Map<String, Object> teacher_map = new HashMap<>();
    Map<String, Object> teacher_map_pos = new HashMap<>();
    /*  @Bind(R.id._item_with_the_teacher)
      NiceSpinner _item_with_the_teacher;*/
    @Bind(R.id._item_name)
    TextView _item_name;//
    @Bind(R.id._item_phone)
    TextView _item_phone;//
    @Bind(R.id._item_recor_of_formal_schooling)
    TextView _item_recor_of_formal_schooling;//
    @Bind(R.id._item_certificate_of_medical_practitioner)
    TextView _item_certificate_of_medical_practitioner;//
    @Bind(R.id._item_admission_time)
    TextView _item_admission_time;//
    @Bind(R.id._item_base)
    TextView _item_base;//
    @Bind(R.id._item_rotary_department)
    TextView _item_rotary_department;//
    @Bind(R.id._item_plan_your_admission_time)
    TextView _item_plan_your_admission_time;//
    @Bind(R.id.add_layout)
    LinearLayout add_layout;//
    @Bind(R.id.add_view)
    TextView add_view;//
    @Bind(R.id.del_view)
    TextView del_view;//
    @Bind(R.id.close_icon)
    TextView close_icon;//
    @Bind(R.id.submit_icon)
    TextView submit_icon;//
    @Bind(R.id.function_layout)
    LinearLayout function_layout;//
    @Bind(R.id.function_layout2)
    LinearLayout function_layout2;//

    private String plan_starttime = "";
    private String plan_endtime = "";
    private String input_starttime = "";
    private String input_endtime = "";
    private Dialog dialog;
    private View layout_view;
    private String type = "1";
    private int restrict_add_teacher_num = 2;
    private String is_edit = "";
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
                                JSONObject dateJson = dataJson.getJSONObject("data");
                                input_starttime = dateJson.has("input_starttime") ? dateJson.getString("input_starttime") : "";
                                input_endtime = dateJson.has("input_endtime") ? dateJson.getString("input_endtime") : "";
                                plan_starttime = dateJson.getString("plan_starttime");
//                                is_edit = getIntent().getStringExtra("is_edit");
                                plan_endtime = dateJson.getString("plan_endtime");
                                plan_starttime.replaceAll("-", ".");
                                plan_endtime.replaceAll("-", ".");
                                _item_base.setText(dateJson.getString("base_name"));//基地
                                _item_plan_your_admission_time.setText(dateJson.getString("plan_starttime") + "~" + dateJson.getString("plan_endtime"));//轮转时间
                                restrict_add_teacher_num = dateJson.has("restrict_add_teacher_num") ? dateJson.getInt("restrict_add_teacher_num") : 2;

                                JSONArray assign_config = dateJson.getJSONArray("assign_config");
//                                        ((NiceSpinner) ((LinearLayout) add_layout_view.getChildAt(0)).getChildAt(1)).attachDataSource(dataset);
//                                        ((NiceSpinner) ((LinearLayout) add_layout_view.getChildAt(0)).getChildAt(1)).setTextColor(Color.BLACK);
//                                        ((NiceSpinner) ((LinearLayout) add_layout_view.getChildAt(0)).getChildAt(1)).setSelectedIndex(Integer.parseInt(teacher_map_pos.get(dataJson1.getString("teacher_name")).toString()));
//                                        ((TextView) ((LinearLayout) add_layout_view.getChildAt(1)).getChildAt(2)).setText(dateJson.getString("input_starttime"));
//                                        ((TextView) ((LinearLayout) add_layout_view.getChildAt(2)).getChildAt(2)).setText(dateJson.getString("input_endtime"));

                                    add_layout.removeAllViews();
                                    for (int i = 0; i < assign_config.length(); i++) {
                                        LayoutInflater inflater = LayoutInflater.from(context);
                                        LinearLayout add_layout_view = (LinearLayout) inflater.inflate(R.layout.item_to_deal_with_the_family, null);
                                        NiceSpinner niceSpinner = add_layout_view.findViewById(R.id._item_with_the_teacher);
                                        TextView startTime = add_layout_view.findViewById(R.id._item_start_time);
                                        TextView endTime = add_layout_view.findViewById(R.id._item_end_time);

                                        JSONObject dataJson1 = assign_config.getJSONObject(i);
                                        dataset.add(dataJson1.getString("teacher_name"));
                                        teacher_map.put(dataJson1.getString("teacher_name"), dataJson1.getString("teacher"));
                                        teacher_map_pos.put(dataJson1.getString("teacher_name"), i);
                                        niceSpinner.attachDataSource(dataset);
                                        niceSpinner.setSelectedIndex(Integer.parseInt(teacher_map_pos.get(dataJson1.getString("teacher_name")).toString()));
                                        niceSpinner.setTextColor(Color.BLACK);
                                        startTime.setText(dataJson1.getString("starttime"));
                                        endTime.setText(dataJson1.getString("endtime"));

                                        //判断是否是编辑模式
                                        if (is_edit != null && is_edit.equals("0")) {
                                            ((NiceSpinner) ((LinearLayout) add_layout_view.getChildAt(0)).getChildAt(1)).setClickable(false);
                                            function_layout.setVisibility(View.GONE);
                                            function_layout2.setVisibility(View.GONE);
                                        } else {
                                            if (is_edit.equals("1")) {
                                                ((NiceSpinner) ((LinearLayout) add_layout_view.getChildAt(0)).getChildAt(1)).setClickable(true);
                                                ((TextView) ((LinearLayout) add_layout_view.getChildAt(1)).getChildAt(2)).setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        To_calendarView(v);
                                                    }
                                                });
                                                ((TextView) ((LinearLayout) add_layout_view.getChildAt(2)).getChildAt(2)).setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        To_calendarView2(v);
                                                    }
                                                });
                                                function_layout.setVisibility(View.VISIBLE);
                                                function_layout2.setVisibility(View.VISIBLE);
                                            } else {
                                                ((NiceSpinner) ((LinearLayout) add_layout_view.getChildAt(0)).getChildAt(1)).setClickable(false);
                                                function_layout.setVisibility(View.GONE);
                                                function_layout2.setVisibility(View.GONE);
                                            }
                                        }

                                        add_layout.addView(add_layout_view);
                                    }
                                    if ("1".equals(is_edit)){
                                        //可编辑状态下才访问带教老师的接口
                                        getUrlRulest();
                                    }

                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                JSONArray dateJson = dataJson.getJSONArray("data");
//                                dataset.clear();
//                                teacher_map.clear();
                                for (int i = 0; i < dateJson.length(); i++) {
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    if(!teacher_map.containsKey(dataJson1.getString("teacher_name"))){
                                        dataset.add(dataJson1.getString("teacher_name"));
                                        teacher_map.put(dataJson1.getString("teacher_name"), dataJson1.getString("teacher"));
                                        teacher_map_pos.put(dataJson1.getString("teacher_name"), i);
                                    }
                                }
                                if(add_layout.getChildCount()<1){
                                    add_layout.removeAllViews();
                                    LayoutInflater inflater = LayoutInflater.from(context);
                                    LinearLayout add_layout_view = (LinearLayout) inflater.inflate(R.layout.item_to_deal_with_the_family, null);
                                    NiceSpinner niceSpinner = add_layout_view.findViewById(R.id._item_with_the_teacher);
                                    TextView startTime = add_layout_view.findViewById(R.id._item_start_time);
                                    TextView endTime = add_layout_view.findViewById(R.id._item_end_time);
                                    if (dataset.size() > 0){
                                        niceSpinner.attachDataSource(dataset);
                                    }else{
                                        niceSpinner.attachDataSource(dataset);
                                    }
                                    niceSpinner.setTextColor(Color.BLACK);
                                    startTime.setText(input_starttime);
                                    endTime.setText(input_endtime);
                                    if (is_edit != null && is_edit.equals("0")) {
                                        ((NiceSpinner) ((LinearLayout) add_layout_view.getChildAt(0)).getChildAt(1)).setClickable(false);
                                        function_layout.setVisibility(View.GONE);
                                        function_layout2.setVisibility(View.GONE);
                                    } else {
                                        //判断是否是编辑模式
                                        if (is_edit.equals("1")) {
                                            niceSpinner.setClickable(true);
                                            startTime.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    To_calendarView(v);
                                                }
                                            });
                                            endTime.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    To_calendarView2(v);
                                                }
                                            });
                                            function_layout.setVisibility(View.VISIBLE);
                                            function_layout2.setVisibility(View.VISIBLE);
                                        } else {
                                            ((NiceSpinner) ((LinearLayout) add_layout_view.getChildAt(0)).getChildAt(1)).setClickable(false);
                                            function_layout.setVisibility(View.GONE);
                                            function_layout2.setVisibility(View.GONE);
                                        }
                                    }
                                    //没有带教老师的信息不显示
                                    add_layout.addView(add_layout_view);
                                }

                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
//                        MyProgressBarDialogTools.hide();
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 3:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                        submit_icon.setClickable(true);
                    }
                    break;
                case 4:
                    Toast.makeText(getApplicationContext(), "结束日期不能大于起始日期", Toast.LENGTH_SHORT).show();
                    submit_icon.setClickable(true);
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
        setContentView(R.layout.to_deal_with_the_family);
        context = this;
        ButterKnife.bind(this);
        fid = getIntent().getStringExtra("fid");
        is_edit = getIntent().getStringExtra("is_edit");
//        is_edit = "1";
        findId();
        initViews();
        getrkUserInfo();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/rk/index.html";
        super.onPause();
    }

    @Override
    public void findId() {
        super.findId();
        initDatas();
    }

    public void To_calendarView(View view) {
        final TextView textView = (TextView) view;
        // 弹出自定义dialog
        LayoutInflater inflater = LayoutInflater.from(context);
        layout_view = inflater.inflate(R.layout.dialog_item15, null);

        // 对话框
        dialog = new Dialog(context);
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
                textView.setText(dateBean.getSolar()[0] + "-" + (dateBean.getSolar()[1] > 9 ? dateBean.getSolar()[1] : "0" + dateBean.getSolar()[1]) + "-" + (dateBean.getSolar()[2] > 9 ? dateBean.getSolar()[2] : "0" + dateBean.getSolar()[2]));
                LogUtil.e("dateBean", "当前选中的日期：" + dateBean.getSolar()[0] + "年" + dateBean.getSolar()[1] + "月" + dateBean.getSolar()[2] + "日");
                dialog.dismiss();
                dialog = null;
                layout_view = null;

            }
        });

    }

    public void To_calendarView2(View view) {
        final TextView textView = (TextView) view;
        // 弹出自定义dialog
        LayoutInflater inflater = LayoutInflater.from(context);
        layout_view = inflater.inflate(R.layout.dialog_item15, null);

        // 对话框
        dialog = new Dialog(context);
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
                .setSingleDate(DateUtil.format(textView.getText().toString(), ".", "yyyy.MM.dd"))//设置单选时初始选中的日期（不设置则不默认选中）
//                .setDisableStartEndDate(input_starttime.replaceAll("-", "."), input_endtime.replaceAll("-", "."))
                .init();

        calendarView.setOnSingleChooseListener(new OnSingleChooseListener() {
            @Override
            public void onSingleChoose(View view, DateBean dateBean) {
                textView.setText(dateBean.getSolar()[0] + "-" + (dateBean.getSolar()[1] > 9 ? dateBean.getSolar()[1] : "0" + dateBean.getSolar()[1]) + "-" + (dateBean.getSolar()[2] > 9 ? dateBean.getSolar()[2] : "0" + dateBean.getSolar()[2]));
                LogUtil.e("dateBean", "当前选中的日期：" + dateBean.getSolar()[0] + "年" + dateBean.getSolar()[1] + "月" + dateBean.getSolar()[2] + "日");
                dialog.dismiss();
                dialog = null;
                layout_view = null;

            }
        });

    }

    private void initDatas() {

        _item_name.setText(getIntent().getStringExtra("realname"));
        _item_phone.setText(getIntent().getStringExtra("mobphone"));
        _item_recor_of_formal_schooling.setText(getIntent().getStringExtra("edu_highest_education"));
        _item_certificate_of_medical_practitioner.setText(getIntent().getStringExtra("exam_situation_is_ep"));
        _item_admission_time.setText(getIntent().getStringExtra("ls_enrollment_year"));
        _item_rotary_department.setText(getIntent().getStringExtra("standard_name"));

    }

    private void initViews() {
        add_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断当前是否超过两个带教
                if (add_layout.getChildCount() < restrict_add_teacher_num) {
                    LayoutInflater inflater = LayoutInflater.from(context);
                    LinearLayout add_layout_view = (LinearLayout) inflater.inflate(R.layout.item_to_deal_with_the_family, null);
                    NiceSpinner niceSpinner = add_layout_view.findViewById(R.id._item_with_the_teacher);
                    TextView startTime = add_layout_view.findViewById(R.id._item_start_time);
                    TextView endTime = add_layout_view.findViewById(R.id._item_end_time);
                    if (dataset.size() > 0)
                        niceSpinner.attachDataSource(dataset);
                    niceSpinner.setTextColor(Color.BLACK);
                    startTime.setText(input_starttime);
                    endTime.setText(input_endtime);
                    startTime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            To_calendarView(v);
                        }
                    });
                    endTime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            To_calendarView2(v);
                        }
                    });
                    add_layout.addView(add_layout_view);
                } else {
                    Toast.makeText(getApplicationContext(), "最多添加" + restrict_add_teacher_num + "个", Toast.LENGTH_SHORT).show();
                }
            }
        });

        del_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否只有一个带教
                if (add_layout.getChildCount() > 1) {
                    add_layout.removeViewAt(add_layout.getChildCount() - 1);
                } else {
                    Toast.makeText(getApplicationContext(), "至少保留一个", Toast.LENGTH_SHORT).show();
                }
            }
        });

        close_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        submit_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fastClick()){
                    submit_icon.setClickable(false);
                    getsubmit();
                }

            }
        });
    }


    public void getsubmit() {

        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.rkEdit);
                    obj.put("id", getIntent().getStringExtra("id"));
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("type", type);
                    dataJson.put("plan_starttime", plan_starttime);
                    dataJson.put("plan_endtime", plan_endtime);
                    JSONArray config = new JSONArray();
                    for (int i = 0; i < add_layout.getChildCount(); i++) {
                        LinearLayout niceLyout = (LinearLayout) add_layout.getChildAt(i);

                        JSONObject palnJson = new JSONObject();
                        palnJson.put("teacher", teacher_map.get(((NiceSpinner) ((LinearLayout) niceLyout.getChildAt(0)).getChildAt(1)).getText().toString()).toString());
                        palnJson.put("starttime", ((TextView) ((LinearLayout) niceLyout.getChildAt(1)).getChildAt(2)).getText());
                        palnJson.put("endtime", ((TextView) ((LinearLayout) niceLyout.getChildAt(2)).getChildAt(2)).getText());
                        LogUtil.e("日期判断", DateUtil.compareDateDayValue(DateUtil.formatDate2(((TextView) ((LinearLayout) niceLyout.getChildAt(1)).getChildAt(2)).getText().toString(), "yyyyy-MM-dd"), DateUtil.formatDate2(((TextView) ((LinearLayout) niceLyout.getChildAt(2)).getChildAt(2)).getText().toString(), "yyyyy-MM-dd")) + "");
                        if (DateUtil.compareDateDayValue(DateUtil.formatDate2(((TextView) ((LinearLayout) niceLyout.getChildAt(1)).getChildAt(2)).getText().toString(), "yyyyy-MM-dd"), DateUtil.formatDate2(((TextView) ((LinearLayout) niceLyout.getChildAt(2)).getChildAt(2)).getText().toString(), "yyyyy-MM-dd"))) {
                            Message message = new Message();
                            message.what = 4;
                            handler.sendMessage(message);
                            MyProgressBarDialogTools.hide();
                            return;
                        }
                        config.put(palnJson);
                    }
                    dataJson.put("config", config);
                    obj.put("data", dataJson);
                    LogUtil.e("入科-操作数据入库 上行参数", obj.toString());
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("入科-操作数据入库", result);
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

    public void getUrlRulest() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.rkTeacherList);
                    obj.put("standard_kid", getIntent().getStringExtra("standard_kid"));
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("入科-操作数据入库", result);
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

    public void getrkUserInfo() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.rkUserInfo);
                    obj.put("id", getIntent().getStringExtra("id"));
//                    obj.put("fid",fid);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("入科-入科编辑资料获取", result);
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
