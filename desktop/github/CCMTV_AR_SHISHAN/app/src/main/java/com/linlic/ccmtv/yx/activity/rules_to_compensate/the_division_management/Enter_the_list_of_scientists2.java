package com.linlic.ccmtv.yx.activity.rules_to_compensate.the_division_management;

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
 * Created by Administrator on 2018/7/3.
 */

public class Enter_the_list_of_scientists2 extends BaseActivity {

    private Context context;
    private String fid = "";
    @Bind(R.id.listView)
    ListView listView;//
    @Bind(R.id.listView2)
    ListView listView2;//
    @Bind(R.id.listView3)
    ListView listView3;//
    @Bind(R.id.show_text1)
    TextView show_text1;//
    @Bind(R.id.show_text2)
    TextView show_text2;//
    private int i_count = 1;
    private Dialog dialog;
    private View layout_view ;
    private List<Map<String, Object>> need_list = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> history_list = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> week_list = new ArrayList<Map<String, Object>>();
    private BaseListAdapter baseListAdapter,baseListAdapter2,baseListAdapter3;
    private String need_list_open = "";//入科 开放编辑  1可编辑 0查看
    private String history_list_open = "";//入科 开放编辑  1可编辑 0查看
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
                            JSONArray need_listJson = dateJson.getJSONArray("need_list");
                            JSONArray history_listJson = dateJson.getJSONArray("history_list");
                            JSONArray week_listJson = dateJson.getJSONArray("week_list");
                            need_list_open = dateJson.getString("need_list_open");
                            history_list_open = dateJson.getString("history_list_open");
                            need_list.clear();
                            for (int i = 0; i < need_listJson.length(); i++) {
                                JSONObject dataJson1 = need_listJson.getJSONObject(i);
                                Map<String, Object> map = new HashMap<>();
                                map.put("id", dataJson1.getString("id"));
                                map.put("uid", dataJson1.getString("uid"));
                                map.put("status", dataJson1.getString("status"));
                                map.put("base_id", dataJson1.getString("base_id"));
                                map.put("edu_highest_education", dataJson1.getString("edu_highest_education"));
                                map.put("exam_situation_is_ep", dataJson1.getString("exam_situation_is_ep"));
                                map.put("realname", dataJson1.getString("realname"));
                                map.put("standard_name", dataJson1.getString("standard_name"));
                                map.put("standard_kid", dataJson1.getString("standard_kid"));
                                map.put("year", dataJson1.getString("year"));
                                map.put("month", dataJson1.getString("month"));
                                map.put("mobphone", dataJson1.getString("mobphone"));
                                map.put("status_name", dataJson1.getString("status_name"));
                                map.put("ls_enrollment_year", dataJson1.getString("ls_enrollment_year"));
                                map.put("is_continue",dataJson1.getString("is_continue") );
                                map.put("is_show_exam_situation_is_ep",dataJson1.getString("is_show_exam_situation_is_ep") );
                                map.put("IDphoto",dataJson1.getString("IDphoto") );
                                need_list.add(map);
                            }
                            baseListAdapter.notifyDataSetChanged();

                            week_list.clear();
                            for (int i = 0; i < week_listJson.length(); i++) {
                                JSONObject dataJson1 = week_listJson.getJSONObject(i);
                                Map<String,Object> map = new HashMap<>();
                                map.put("realname",dataJson1.getString("realname") );
                                map.put("mobphone",dataJson1.getString("mobphone") );
                                map.put("base_id",dataJson1.getString("base_id") );
                                map.put("ls_enrollment_year",dataJson1.getString("ls_enrollment_year") );
                                map.put("edu_education",dataJson1.getString("edu_education") );
                                map.put("exam_situation_is_ep",dataJson1.getString("exam_situation_is_ep") );
                                map.put("edu_highest_education",dataJson1.getString("edu_highest_education") );
                                map.put("base_name",dataJson1.getString("base_name") );
                                map.put("standard_name",dataJson1.getString("standard_name") );
                                map.put("plan_starttime",dataJson1.getString("plan_starttime") );
                                map.put("plan_endtime",dataJson1.getString("plan_endtime") );
                                map.put("is_continue",dataJson1.getString("is_continue") );
                                map.put("is_show_exam_situation_is_ep",dataJson1.getString("is_show_exam_situation_is_ep") );
                                map.put("IDphoto",dataJson1.getString("IDphoto") );
                                map.put("status",dataJson1.getString("status") );
                                week_list.add(map);
                            }
                            if(week_list.size()<1){
                                show_text1.setVisibility(View.GONE);
                                listView2.setVisibility(View.GONE);
                            }else{
                                show_text1.setVisibility(View.VISIBLE);
                                listView2.setVisibility(View.GONE);
                                show_text1.setTextColor(getResources().getColor(R.color.text_blue));
                                show_text1.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.mipmap.details_icon2),null,null,null);
                            }
                            baseListAdapter2.notifyDataSetChanged();

                            history_list.clear();
                            for (int i = 0; i < history_listJson.length(); i++) {
                                JSONObject dataJson1 = history_listJson.getJSONObject(i);
                                Map<String, Object> map = new HashMap<>();
                                map.put("id", dataJson1.getString("id"));
                                map.put("uid", dataJson1.getString("uid"));
                                map.put("status", dataJson1.getString("status"));
                                map.put("base_id", dataJson1.getString("base_id"));
                                map.put("edu_highest_education", dataJson1.getString("edu_highest_education"));
                                map.put("exam_situation_is_ep", dataJson1.getString("exam_situation_is_ep"));
                                map.put("realname", dataJson1.getString("realname"));
                                map.put("standard_name", dataJson1.getString("standard_name"));
                                map.put("standard_kid", dataJson1.getString("standard_kid"));
                                map.put("year", dataJson1.getString("year"));
                                map.put("month", dataJson1.getString("month"));
                                map.put("status_name", dataJson1.getString("status_name"));
                                map.put("mobphone", dataJson1.getString("mobphone"));
                                map.put("ls_enrollment_year", dataJson1.getString("ls_enrollment_year"));
                                map.put("is_continue",dataJson1.getString("is_continue") );
                                map.put("is_show_exam_situation_is_ep",dataJson1.getString("is_show_exam_situation_is_ep") );
                                map.put("IDphoto",dataJson1.getString("IDphoto") );
                                history_list.add(map);
                            }
                            baseListAdapter3.notifyDataSetChanged();
                            if(history_list.size()<1){
                                show_text2.setVisibility(View.GONE);
                                listView3.setVisibility(View.GONE);
                            }else{
                                show_text2.setVisibility(View.VISIBLE);
                                listView3.setVisibility(View.GONE);
                                show_text2.setTextColor(getResources().getColor(R.color.text_blue));
                                show_text2.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.mipmap.details_icon2),null,null,null);
                            }



                            if(week_list.size()<1 && need_list.size()<1){
                                //当只有历史数据的时候 自动展开历史数据 隐藏另外两个
                                show_text1.setVisibility(View.GONE);
                                show_text2.setVisibility(View.GONE);
                                listView3.setVisibility(View.VISIBLE);
                            }

                            if(week_list.size()<1 && history_list.size()<1){
                                //当只有历史数据的时候 自动展开历史数据 隐藏另外两个
                                show_text1.setVisibility(View.GONE);
                                show_text2.setVisibility(View.GONE);
                                listView2.setVisibility(View.VISIBLE);
                            }

                        } else {
                            Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
//                        MyProgressBarDialogTools.hide();
                        }else{
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                MyProgressBarDialogTools.hide();
                            }
                        },2000);
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
                            JSONObject dateJson = dataJson.getJSONObject("data");
                            JSONArray need_listJson = dateJson.getJSONArray("need_list");
                            JSONArray history_listJson = dateJson.getJSONArray("history_list");
                            JSONArray week_listJson = dateJson.getJSONArray("week_list");
                            need_list_open = dateJson.getString("need_list_open");
                            history_list_open = dateJson.getString("history_list_open");
                            need_list.clear();
                            for (int i = 0; i < need_listJson.length(); i++) {
                                JSONObject dataJson1 = need_listJson.getJSONObject(i);
                                Map<String, Object> map = new HashMap<>();
                                map.put("id", dataJson1.getString("id"));
                                map.put("uid", dataJson1.getString("uid"));
                                map.put("status", dataJson1.getString("status"));
                                map.put("base_id", dataJson1.getString("base_id"));
                                map.put("edu_highest_education", dataJson1.getString("edu_highest_education"));
                                map.put("exam_situation_is_ep", dataJson1.getString("exam_situation_is_ep"));
                                map.put("realname", dataJson1.getString("realname"));
                                map.put("standard_name", dataJson1.getString("standard_name"));
                                map.put("standard_kid", dataJson1.getString("standard_kid"));
                                map.put("year", dataJson1.getString("year"));
                                map.put("month", dataJson1.getString("month"));
                                map.put("mobphone", dataJson1.getString("mobphone"));
                                map.put("status_name", dataJson1.getString("status_name"));
                                map.put("ls_enrollment_year", dataJson1.getString("ls_enrollment_year"));
                                map.put("is_continue",dataJson1.getString("is_continue") );
                                map.put("is_show_exam_situation_is_ep",dataJson1.getString("is_show_exam_situation_is_ep") );
                                map.put("IDphoto",dataJson1.getString("IDphoto") );
                                need_list.add(map);
                            }
                            baseListAdapter.notifyDataSetChanged();

                            week_list.clear();
                            for (int i = 0; i < week_listJson.length(); i++) {
                                JSONObject dataJson1 = week_listJson.getJSONObject(i);
                                Map<String,Object> map = new HashMap<>();
                                map.put("realname",dataJson1.getString("realname") );
                                map.put("mobphone",dataJson1.getString("mobphone") );
                                map.put("base_id",dataJson1.getString("base_id") );
                                map.put("ls_enrollment_year",dataJson1.getString("ls_enrollment_year") );
                                map.put("edu_education",dataJson1.getString("edu_education") );
                                map.put("exam_situation_is_ep",dataJson1.getString("exam_situation_is_ep") );
                                map.put("edu_highest_education",dataJson1.getString("edu_highest_education") );
                                map.put("base_name",dataJson1.getString("base_name") );
                                map.put("standard_name",dataJson1.getString("standard_name") );
                                map.put("plan_starttime",dataJson1.getString("plan_starttime") );
                                map.put("plan_endtime",dataJson1.getString("plan_endtime") );
                                map.put("is_continue",dataJson1.getString("is_continue") );
                                map.put("is_show_exam_situation_is_ep",dataJson1.getString("is_show_exam_situation_is_ep") );
                                map.put("IDphoto",dataJson1.getString("IDphoto") );
                                map.put("status",dataJson1.getString("status") );
                                week_list.add(map);
                            }
                            baseListAdapter2.notifyDataSetChanged();

                            history_list.clear();
                            for (int i = 0; i < history_listJson.length(); i++) {
                                JSONObject dataJson1 = history_listJson.getJSONObject(i);
                                Map<String, Object> map = new HashMap<>();
                                map.put("id", dataJson1.getString("id"));
                                map.put("uid", dataJson1.getString("uid"));
                                map.put("status", dataJson1.getString("status"));
                                map.put("base_id", dataJson1.getString("base_id"));
                                map.put("edu_highest_education", dataJson1.getString("edu_highest_education"));
                                map.put("exam_situation_is_ep", dataJson1.getString("exam_situation_is_ep"));
                                map.put("realname", dataJson1.getString("realname"));
                                map.put("standard_name", dataJson1.getString("standard_name"));
                                map.put("standard_kid", dataJson1.getString("standard_kid"));
                                map.put("year", dataJson1.getString("year"));
                                map.put("month", dataJson1.getString("month"));
                                map.put("status_name", dataJson1.getString("status_name"));
                                map.put("mobphone", dataJson1.getString("mobphone"));
                                map.put("ls_enrollment_year", dataJson1.getString("ls_enrollment_year"));
                                map.put("is_continue",dataJson1.getString("is_continue") );
                                map.put("is_show_exam_situation_is_ep",dataJson1.getString("is_show_exam_situation_is_ep") );
                                map.put("IDphoto",dataJson1.getString("IDphoto") );
                                history_list.add(map);
                            }
                            baseListAdapter3.notifyDataSetChanged();


                        } else {
                            Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
//                        MyProgressBarDialogTools.hide();
                        }else{
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                MyProgressBarDialogTools.hide();
                            }
                        },2000);
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
        setContentView(R.layout.enter_the_list_of_scientists2);
        context = this;
        ButterKnife.bind(this);
        fid = getIntent().getStringExtra("fid");
        findId();
        initViews();

    }

    @Override
    public void findId() {
        super.findId();
        initDatas();
        setActivity_title_name( getIntent().getStringExtra("title"));

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(i_count == 1){
            getUrlRulest();
        }else{
            getUrlRulest2();
        }
        i_count++;
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/rk/index.html";
        super.onPause();
    }

    private void initDatas() {

    }

    private void initViews() {
        show_text2.setText(getIntent().getStringExtra("year")+"年"+getIntent().getStringExtra("month")+"月往期数据");

        baseListAdapter = new BaseListAdapter(listView, need_list, R.layout.item_enter_the_list_of_scientists) {

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
                if(map.get("is_show_exam_situation_is_ep").toString().equals("1")){
                    helper.setVisibility(R.id._item_licensed_doctors_certificate,View.VISIBLE);
                }else{
                    helper.setVisibility(R.id._item_licensed_doctors_certificate,View.GONE);
                }
                helper.setImageBitmap(R.id.my_myhead,map.get("IDphoto").toString());

                if(map.get("is_continue").toString().equals("1")){
                    helper.setVisibility(R.id._item_in_wheel,View.VISIBLE);
                }else{
                    helper.setVisibility(R.id._item_in_wheel,View.GONE);
                }
                if(map.get("status").toString().equals("0")){
                    helper.setVisibility(R.id._item_admitted_section,View.GONE);
                    helper.setVisibility(R.id._item_not_admitted,View.VISIBLE);
                }else{
                    helper.setVisibility(R.id._item_admitted_section,View.VISIBLE);
                    helper.setVisibility(R.id._item_not_admitted,View.GONE);
                }
            }
        };
        listView.setAdapter(baseListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> map = need_list.get(position);
                Intent intent = null;
                intent = new Intent(context,To_deal_with_the_family.class);
                intent.putExtra("id",map.get("id").toString());
                intent.putExtra("uid",map.get("uid").toString());
                intent.putExtra("status",map.get("status").toString());
                intent.putExtra("base_id",map.get("base_id").toString());
                intent.putExtra("edu_highest_education",map.get("edu_highest_education").toString());
                intent.putExtra("exam_situation_is_ep",map.get("exam_situation_is_ep").toString());
                intent.putExtra("realname",map.get("realname").toString());
                intent.putExtra("standard_name",map.get("standard_name").toString());
                intent.putExtra("standard_kid",map.get("standard_kid").toString());
                intent.putExtra("year",map.get("year").toString());
                intent.putExtra("month",map.get("month").toString());
                intent.putExtra("mobphone",map.get("mobphone").toString());
                intent.putExtra("status_name",map.get("status_name").toString());
                intent.putExtra("ls_enrollment_year",map.get("ls_enrollment_year").toString());
                intent.putExtra("fid",fid);
                intent.putExtra("is_edit",need_list_open);
                if(intent!=null){
                    startActivity(intent);
                }


            }
        });
        baseListAdapter2 = new BaseListAdapter(listView2, week_list, R.layout.item_enter_the_list_of_scientists) {

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
                if(map.get("is_show_exam_situation_is_ep").toString().equals("1")){
                    helper.setVisibility(R.id._item_licensed_doctors_certificate,View.VISIBLE);
                }else{
                    helper.setVisibility(R.id._item_licensed_doctors_certificate,View.GONE);
                }
                helper.setImageBitmap(R.id.my_myhead,map.get("IDphoto").toString());

                if(map.get("is_continue").toString().equals("1")){
                    helper.setVisibility(R.id._item_in_wheel,View.VISIBLE);
                }else{
                    helper.setVisibility(R.id._item_in_wheel,View.GONE);
                }
                if(map.get("status").toString().equals("0")){
                    helper.setVisibility(R.id._item_admitted_section,View.GONE);
                    helper.setVisibility(R.id._item_not_admitted,View.VISIBLE);
                }else{
                    helper.setVisibility(R.id._item_admitted_section,View.VISIBLE);
                    helper.setVisibility(R.id._item_not_admitted,View.GONE);
                }
            }
        };
        listView2.setAdapter(baseListAdapter2);
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(dialog == null){

                    Map<String, Object> map = week_list.get(position);


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
                    lp.width = (int) (display.getWidth()-100); // 设置宽度
                    dialog.getWindow().setAttributes(lp);
                    dialog.getWindow().setContentView(layout_view);
                    dialog.setCancelable(false);
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
        baseListAdapter3 = new BaseListAdapter(listView3, history_list, R.layout.item_enter_the_list_of_scientists) {

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
                if(map.get("is_show_exam_situation_is_ep").toString().equals("1")){
                    helper.setVisibility(R.id._item_licensed_doctors_certificate,View.VISIBLE);
                }else{
                    helper.setVisibility(R.id._item_licensed_doctors_certificate,View.GONE);
                }
                helper.setImageBitmap(R.id.my_myhead,map.get("IDphoto").toString());

                if(map.get("is_continue").toString().equals("1")){
                    helper.setVisibility(R.id._item_in_wheel,View.VISIBLE);
                }else{
                    helper.setVisibility(R.id._item_in_wheel,View.GONE);
                }
                if(map.get("status").toString().equals("0")){
                    helper.setVisibility(R.id._item_admitted_section,View.GONE);
                    helper.setVisibility(R.id._item_not_admitted,View.VISIBLE);
                }else{
                    helper.setVisibility(R.id._item_admitted_section,View.VISIBLE);
                    helper.setVisibility(R.id._item_not_admitted,View.GONE);
                }
            }
        };
        listView3.setAdapter(baseListAdapter3);
        listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> map = history_list.get(position);
                Intent intent = null;
                intent = new Intent(context,To_deal_with_the_family.class);
                intent.putExtra("id",map.get("id").toString());
                intent.putExtra("uid",map.get("uid").toString());
                intent.putExtra("status",map.get("status").toString());
                intent.putExtra("base_id",map.get("base_id").toString());
                intent.putExtra("edu_highest_education",map.get("edu_highest_education").toString());
                intent.putExtra("exam_situation_is_ep",map.get("exam_situation_is_ep").toString());
                intent.putExtra("realname",map.get("realname").toString());
                intent.putExtra("standard_name",map.get("standard_name").toString());
                intent.putExtra("standard_kid",map.get("standard_kid").toString());
                intent.putExtra("year",map.get("year").toString());
                intent.putExtra("month",map.get("month").toString());
                intent.putExtra("mobphone",map.get("mobphone").toString());
                intent.putExtra("status_name",map.get("status_name").toString());
                intent.putExtra("ls_enrollment_year",map.get("ls_enrollment_year").toString());
                intent.putExtra("fid",fid);
                intent.putExtra("is_edit",history_list_open);

                if(intent!=null){
                    startActivity(intent);
                }

            }
        });

        show_text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listView2.getVisibility() == View.VISIBLE){
                    listView2.setVisibility(View.GONE);
                    show_text1.setTextColor(getResources().getColor(R.color.text_blue));
                    show_text1.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.mipmap.details_icon2),null,null,null);
                }else{
                    listView2.setVisibility(View.VISIBLE);
                    show_text1.setTextColor(getResources().getColor(R.color.hint_color));
                    show_text1.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.mipmap.details_icon3),null,null,null);
                }
            }
        });
        show_text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listView3.getVisibility() == View.VISIBLE){
                    listView3.setVisibility(View.GONE);
                    show_text2.setTextColor(getResources().getColor(R.color.text_blue));
                    show_text2.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.mipmap.details_icon2),null,null,null);
                }else{
                    listView3.setVisibility(View.VISIBLE);
                    show_text2.setTextColor(getResources().getColor(R.color.hint_color));
                    show_text2.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.mipmap.details_icon3),null,null,null);
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
                    obj.put("act", URLConfig.rkInfoList);
                    obj.put("standard_kid_", getIntent().getStringExtra("standard_kid_"));
                    obj.put("year", getIntent().getStringExtra("year"));
                    obj.put("month", getIntent().getStringExtra("month"));
                    obj.put("fid", fid);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
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

    public void getUrlRulest2() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.rkInfoList);
                    obj.put("standard_kid_", getIntent().getStringExtra("standard_kid_"));
                    obj.put("year", getIntent().getStringExtra("year"));
                    obj.put("month", getIntent().getStringExtra("month"));
                    obj.put("fid", fid);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("入科-预览", result);
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



}

