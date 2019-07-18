package com.linlic.ccmtv.yx.activity.rules_to_compensate.rotation;

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
import android.widget.LinearLayout;
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
 * Created by Administrator on 2018/6/26.
 */

public class Rotation extends BaseActivity {

    private Context context;
    @Bind(R.id.activity_title_name)
    TextView activityTitleName;
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.rotation_nodata)
    NodataEmptyLayout rotation_nodata;
    @Bind(R.id._item_ds)
    TextView _item_ds;
    @Bind(R.id.ds_layout)
    LinearLayout ds_layout;
    private String fid = "";
    private Dialog dialog;
    private View view;
    JSONObject result, data;
    private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
    private BaseListAdapter baseListAdapterVideo, baseListAdapterVideo2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        String code = jsonObject.getString("code");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            if (dataJson.getInt("status") == 1) { // 成功
                                if(dataJson.getString("ds").length()>0){
                                    _item_ds.setText(dataJson.getString("ds"));
                                    _item_ds.setVisibility(View.VISIBLE);
                                    ds_layout.setVisibility(View.VISIBLE);
                                }else{
                                    _item_ds.setVisibility(View.GONE);
                                    ds_layout.setVisibility(View.GONE);
                                }
                                JSONArray dateJson = dataJson.getJSONArray("data");
                                listData.clear();
//                                if (dateJson.length() < 1) {
//                                    layout_nodata.setVisibility(View.VISIBLE);
//                                } else {
//                                    layout_nodata.setVisibility(View.GONE);
//                                }
                                for (int i = 0; i < dateJson.length(); i++) {
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("keshi_status", dataJson1.getString("keshi_status"));
                                    map.put("standard_kid", dataJson1.getString("standard_kid"));
                                    map.put("standard_name", dataJson1.getString("standard_name"));
                                    map.put("jmname", dataJson1.getString("jmname"));
                                    List<Map<String, Object>> datas = new ArrayList<>();
                                    JSONArray jsonArray = dataJson1.getJSONArray("time_list");
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                                        Map<String, Object> map1 = new HashMap<>();
                                        map1.put("time", jsonObject1.getString("time"));
                                        map1.put("time_class", jsonObject1.getString("time_class"));
                                        datas.add(map1);
                                    }
                                    map.put("list", datas);

                                    List<Map<String, Object>> ls_lists = new ArrayList<>();
                                    JSONArray ls_list = dataJson1.getJSONArray("ls_list");
                                    for (int k = 0; k < ls_list.length(); k++) {
                                        JSONObject ls_list_json = ls_list.getJSONObject(k);
                                        Map<String, Object> map2 = new HashMap<>();
                                        map2.put("plan_starttime", ls_list_json.getString("plan_starttime"));
                                        map2.put("plan_endtime", ls_list_json.getString("plan_endtime"));
                                        map2.put("startime", ls_list_json.getString("startime"));
                                        map2.put("endtime", ls_list_json.getString("endtime"));
                                        map2.put("realname", ls_list_json.getString("realname"));
                                        map2.put("username", ls_list_json.getString("username"));
                                        ls_lists.add(map2);
                                    }
                                    map.put("ls_lists", ls_lists);
                                    listData.add(map);
                                }
                                baseListAdapterVideo.notifyDataSetChanged();
                            } else {
//                                layout_nodata.setVisibility(View.VISIBLE);
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        if ("code".equals(code)) {
                            setResultStatus(listData.size() > 0, HttpClientUtils.UNKONW_EXCEPTION_CODE);
                        } else {
                            setResultStatus(listData.size() > 0, Integer.parseInt(code));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        setResultStatus(listData.size() > 0, HttpClientUtils.UNKONW_EXCEPTION_CODE);
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 2:

                    final Map<String, Object> map_data = listData.get(Integer.parseInt(msg.obj + ""));
                    List<Map<String, Object>> listDatas = (List<Map<String, Object>>) map_data.get("ls_lists");

                    if (listDatas.size() > 0) {
                        // 弹出自定义dialog
                        LayoutInflater inflater = LayoutInflater.from(Rotation.this);
                        view = inflater.inflate(R.layout.dialog_item12, null);

                        // 对话框
                        dialog = new Dialog(Rotation.this);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.show();
                        // 设置宽度为屏幕的宽度
                        WindowManager windowManager = getWindowManager();
                        Display display = windowManager.getDefaultDisplay();
                        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                        lp.width = (int) (display.getWidth() - 100); // 设置宽度
                        dialog.getWindow().setAttributes(lp);
                        dialog.getWindow().setContentView(view);
//                    dialog.setCancelable(false);
                        ListView _item_list = (ListView) view.findViewById(R.id._item_list);//
                        TextView _item_content = (TextView) view.findViewById(R.id._item_content);// 姓名
                        _item_content.setText(map_data.get("standard_name").toString());
                        baseListAdapterVideo2 = new BaseListAdapter(_item_list, listDatas, R.layout.item_rotation2) {

                            @Override
                            public void refresh(Collection datas) {
                                super.refresh(datas);
                            }

                            @Override
                            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                                super.convert(helper, item, isScrolling);
                                Map map = (Map) item;
                                helper.setText(R.id._dialog_text1, map.get("plan_starttime").toString() + "~" + map.get("plan_endtime").toString());
                                helper.setText(R.id._dialog_text2, map.get("realname").toString() + "(" + map.get("username").toString() + ")");
                                helper.setText(R.id._dialog_text3, map.get("startime").toString() + "~" + map.get("endtime").toString());
                                TextView tv_jiaomi_name=helper.getView(R.id.tv_jiaomi_name);
                                LinearLayout ll_jiaomi=helper.getView(R.id.ll_jiaomi);
                                String jmname=map_data.get("jmname").toString();
                                if(jmname.equals("")){
                                    ll_jiaomi.setVisibility(View.GONE);
                                }else {
                                    ll_jiaomi.setVisibility(View.VISIBLE);
                                    tv_jiaomi_name.setText(jmname);
                                }

                            }
                        };

                        _item_list.setAdapter(baseListAdapterVideo2);
                    }
                    break;
                case 500:
                    Toast.makeText(context
                            , R.string.post_hint1,
                            Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    setResultStatus(listData.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                    break;

                default:
                    break;
            }
        }
    };

    private void setResultStatus(boolean status, int code) {
        MyProgressBarDialogTools.hide();
        if (status) {
            listView.setVisibility(View.VISIBLE);
            rotation_nodata.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                rotation_nodata.setNetErrorIcon();
            } else {
                rotation_nodata.setLastEmptyIcon();
            }
            listView.setVisibility(View.GONE);
            rotation_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.rotation);
        context = this;
        ButterKnife.bind(this);
        findId();
        fid = getIntent().getStringExtra("fid");
        activityTitleName.setText(getIntent().getStringExtra("title"));
        initViews();
        getstudentList();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/User_cycle_plan/index.html";
        super.onPause();
    }


    private void initViews() {
        baseListAdapterVideo = new BaseListAdapter(listView, listData, R.layout.item_rotation) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map map = (Map) item;
                List<Map<String, Object>> listDatas = (List<Map<String, Object>>) map.get("ls_lists");
                if (listDatas.size() < 1) {
                    helper.setViewVisibility(R.id._item_img, View.GONE);
                } else {
                    helper.setViewVisibility(R.id._item_img, View.VISIBLE);
                }
                TextView tv_jm_name=helper.getView(R.id.tv_jm_name);
                String jmname=map.get("jmname").toString();
                if(jmname.equals("")){
                    tv_jm_name.setVisibility(View.GONE);
                }else {
                    tv_jm_name.setVisibility(View.VISIBLE);
                    tv_jm_name.setText("科室教秘："+jmname);
                }
                helper.setText(R.id._item_title, map.get("standard_name").toString());
                switch (map.get("keshi_status").toString()) {
                    case "1":
                        helper.setDrawable(R.id._item_title, R.mipmap.rotation_icon4, "left");
                        break;
                    case "2":
                        helper.setDrawable(R.id._item_title, R.mipmap.rotation_icon2, "left");
                        break;
                    case "3":
                        helper.setDrawable(R.id._item_title, R.mipmap.rotation_icon1, "left");
                        break;
                    case "4":
                        helper.setDrawable(R.id._item_title, R.mipmap.rotation_icon3, "left");
                        break;
                    default:
                        helper.setDrawable(R.id._item_title, R.mipmap.rotation_icon1, "left");
                        break;

                }

                if (map.containsKey("list")) {
                    LinearLayout add_layout = helper.getView(R.id.add_layout);
                    add_layout.removeAllViews();
                    List<Map<String, Object>> list_map = (List<Map<String, Object>>) map.get("list");
                    for (int i = 0; i < list_map.size(); i++) {
                        try {
                            Map<String, Object> map1 = (Map<String, Object>) list_map.get(i);
                            TextView textView = new TextView(context);
                            textView.setText(map1.get("time").toString());
                            textView.setPadding(5, 5, 5, 5);
                            textView.setCompoundDrawablePadding(10);
                            switch (map1.get("time_class").toString()) {
                                case "green":
                                    textView.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.rotation_icon7), null, null, null);
//                                    textView.setCompoundDrawables(getResources().getDrawable(R.mipmap.rotation_icon1),null,null,null);
                                    break;
                                case "red":
                                    textView.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.rotation_icon6), null, null, null);
                                    break;
                                case "blue":
                                    textView.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.rotation_icon5), null, null, null);
                                    break;
                                case "gray":
                                    textView.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.rotation_icon8), null, null, null);
                                    break;
                                default:
                                    textView.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.rotation_icon5), null, null, null);
                                    break;
                            }

                            add_layout.addView(textView);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
        };
        listView.setAdapter(baseListAdapterVideo);
        // listview点击事件
        listView.setOnItemClickListener(new casesharing_listListener());
    }


    /**
     * name: 点击查看某个视频的详细 author:Tom 2016-1-28下午3:42:08
     */
    private class casesharing_listListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {

            Message message = new Message();
            message.what = 2;
            message.obj = arg2;
            handler.sendMessage(message);
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
                    obj.put("fid", fid);

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

    public void click_full(View view) {
        getstudentList();
    }


}
