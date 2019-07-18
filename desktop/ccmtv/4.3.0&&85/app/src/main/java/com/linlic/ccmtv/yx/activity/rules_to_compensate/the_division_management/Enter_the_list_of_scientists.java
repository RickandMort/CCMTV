package com.linlic.ccmtv.yx.activity.rules_to_compensate.the_division_management;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

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
 * Created by Administrator on 2018/7/3.
 */

public class Enter_the_list_of_scientists extends BaseActivity {

    @Bind(R.id.periodical_nodata)
    NodataEmptyLayout periodicalNodata;
    private Context context;
    private String fid;
    @Bind(R.id.listView)
    ListView listView;//
    private Dialog dialog;
    private View layout_view;
    private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
    private BaseListAdapter baseListAdapter;
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
                                if(dateJson.length()==0){
                                    periodicalNodata.setVisibility(View.GONE);
                                }
                                listData.clear();
                                for (int i = 0; i < dateJson.length(); i++) {
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("realname", dataJson1.getString("realname"));
                                    map.put("mobphone", dataJson1.getString("mobphone"));
                                    map.put("base_id", dataJson1.getString("base_id"));
                                    map.put("ls_enrollment_year", dataJson1.getString("ls_enrollment_year"));
                                    map.put("edu_education", dataJson1.getString("edu_education"));
                                    map.put("exam_situation_is_ep", dataJson1.getString("exam_situation_is_ep"));
                                    map.put("edu_highest_education", dataJson1.getString("edu_highest_education"));
                                    map.put("base_name", dataJson1.getString("base_name"));
                                    map.put("standard_name", dataJson1.getString("standard_name"));
                                    map.put("plan_starttime", dataJson1.getString("plan_starttime"));
                                    map.put("plan_endtime", dataJson1.getString("plan_endtime"));
                                    map.put("uid", dataJson1.getString("uid"));
                                    map.put("ccmtvuid", dataJson1.getString("ccmtvuid"));
                                    map.put("username", dataJson1.getString("username"));
                                    map.put("truename", dataJson1.getString("truename"));
                                    map.put("is_continue", dataJson1.getString("is_continue"));
                                    map.put("is_show_exam_situation_is_ep", dataJson1.getString("is_show_exam_situation_is_ep"));
                                    map.put("IDphoto", dataJson1.getString("IDphoto"));
                                    listData.add(map);
                                }
                                baseListAdapter.notifyDataSetChanged();
                            } else {
                                String errorMessage = dataJson.getString("errorMessage");
                                if (errorMessage.equals("暂无数据")) {
                                    periodicalNodata.setVisibility(View.VISIBLE);
                                } else {
                                    Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                }

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
        setContentView(R.layout.enter_the_list_of_scientists);
        context = this;
        ButterKnife.bind(this);
        fid = getIntent().getStringExtra("fid");
        findId();
        initViews();
        getUrlRulest();
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
        setActivity_title_name( getIntent().getStringExtra("title"));

    }

    private void initDatas() {


    }

    private void initViews() {
        baseListAdapter = new BaseListAdapter(listView, listData, R.layout.item_enter_the_list_of_scientists) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map<String, Object> map = (Map) item;
                helper.setText(R.id._item_name, map.get("realname").toString());
                helper.setText(R.id._item_text, map.get("standard_name").toString(), "html");
                if (map.get("is_show_exam_situation_is_ep").toString().equals("1")) {
                    helper.setVisibility(R.id._item_licensed_doctors_certificate, View.VISIBLE);
                } else {
                    helper.setVisibility(R.id._item_licensed_doctors_certificate, View.GONE);
                }
                helper.setImageBitmap(R.id.my_myhead, map.get("IDphoto").toString());

                helper.setVisibility(R.id._item_admitted_section, View.GONE);
                helper.setVisibility(R.id._item_not_admitted, View.VISIBLE);
                if (map.get("is_continue").toString().equals("1")) {
                    helper.setVisibility(R.id._item_in_wheel, View.VISIBLE);
                } else {
                    helper.setVisibility(R.id._item_in_wheel, View.GONE);
                }
                if (map.get("is_continue").toString().equals("1")) {
                    helper.setVisibility(R.id._item_in_wheel, View.VISIBLE);
                } else {
                    helper.setVisibility(R.id._item_in_wheel, View.GONE);
                }
            }
        };
        listView.setAdapter(baseListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (dialog == null) {
                    Map<String, Object> map = listData.get(position);

                    // 弹出自定义dialog
                    LayoutInflater inflater = LayoutInflater.from(context);
                    layout_view = inflater.inflate(R.layout.dialog_item14, null);

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
                    dialog.setCancelable(false);
                    final TextView _item_content = (TextView) layout_view.findViewById(R.id._item_content);// 取消
                    final TextView _item_close = (TextView) layout_view.findViewById(R.id._item_close);// 取消
                    final TextView _item_name = (TextView) layout_view.findViewById(R.id._item_name);//
                    final TextView _item_phone = (TextView) layout_view.findViewById(R.id._item_phone);//
                    final TextView _item_recor_of_formal_schooling = (TextView) layout_view.findViewById(R.id._item_recor_of_formal_schooling);//
                    final TextView _item_certificate_of_medical_practitioner = (TextView) layout_view.findViewById(R.id._item_certificate_of_medical_practitioner);//
                    final TextView _item_admission_time = (TextView) layout_view.findViewById(R.id._item_admission_time);//
                    final TextView _item_base = (TextView) layout_view.findViewById(R.id._item_base);//
                    final TextView _item_rotary_department = (TextView) layout_view.findViewById(R.id._item_rotary_department);//
                    final TextView _item_plan_your_admission_time = (TextView) layout_view.findViewById(R.id._item_plan_your_admission_time);//
                    final TextView _item_plan_your_time = (TextView) layout_view.findViewById(R.id._item_plan_your_time);//
                    _item_content.setText("入科详情");
                    _item_name.setText(map.get("realname").toString());
                    _item_phone.setText(map.get("mobphone").toString());
                    _item_recor_of_formal_schooling.setText(map.get("edu_highest_education").toString());
                    _item_certificate_of_medical_practitioner.setText(map.get("exam_situation_is_ep").toString());
                    _item_admission_time.setText(map.get("ls_enrollment_year").toString());
                    _item_base.setText(map.get("base_name").toString());
                    _item_rotary_department.setText(map.get("standard_name").toString());
                    _item_plan_your_admission_time.setText(map.get("plan_starttime").toString());
                    _item_plan_your_time.setText(map.get("plan_endtime").toString());

                    _item_close.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            dialog = null;
                            layout_view = null;
                        }
                    });


                }

            }
        });

    }


    public void getUrlRulest() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.rkNextMonth);
                    obj.put("standard_kid_", getIntent().getStringExtra("standard_kid_"));
                    obj.put("fid", fid);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("入科-预览", result);
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
