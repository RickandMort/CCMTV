package com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Resident;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.angmarch.views.NiceSpinner;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.linlic.ccmtv.yx.utils.HttpClientUtils.UNKONW_EXCEPTION_CODE;

/**
 * Created by Administrator on 2018/8/27.
 */

public class Select_staff extends BaseActivity {
    private Context context;
    @Bind(R.id.student_list)
    ListView student_list;
    @Bind(R.id.select_all)
    TextView select_all;
    @Bind(R.id.submit)
    TextView submit;
    @Bind(R.id.search)//搜索按钮
            TextView search;
    @Bind(R.id.keyword_text)//关键字搜索输入框
            EditText keyword_text;
    @Bind(R.id.the_activity_type)//条件选择框
            NiceSpinner the_activity_type;
    @Bind(R.id.the_activity_type2)//条件选择框
            NiceSpinner the_activity_type2;
    @Bind(R.id.the_activity_type3)//条件选择框
            NiceSpinner the_activity_type3;
    @Bind(R.id.the_activity_type4)//条件选择框
            NiceSpinner the_activity_type4;
    @Bind(R.id.management_nodata)
    NodataEmptyLayout management_nodata;
    //跳转发过来的所选中的数据
    private List<String> select_list = new ArrayList<>();
    //本页面员工的数据 及位置    根据  select_list 传输过来的 数据更改初始选中的成员
    private Map<String, Resident> select_Resident = new HashMap<>();
    private Map<String, Object> select_map = new HashMap<>();
    private List<String> spinner_list = new ArrayList<>();//类型数据
    private List<String> spinner_list2 = new ArrayList<>();//类型数据
    private List<String> spinner_list3 = new ArrayList<>();//类型数据
    private List<String> spinner_list4 = new ArrayList<>();//类型数据
    Map<String, Object> spinner_map = new HashMap<>();
    Map<String, Object> spinner_map2 = new HashMap<>();
    Map<String, Object> spinner_map3 = new HashMap<>();
    Map<String, Object> spinner_map4 = new HashMap<>();
    private String fid = "";
    private Dialog dialog;
    private View view;
    JSONObject result, data;
    private int page = 1;
    private int limit = 20;
    private List<Resident> listData = new ArrayList<Resident>();
    private BaseListAdapter baseListAdapterVideo;
    List<Resident> intent_select_list = new ArrayList<>();
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
                                JSONArray dateJson = dataJson.getJSONArray("dataList");
                                if (page == 1) {
                                    listData.clear();
                                    select_map.clear();
                                }
                                for (int i = 0; i < dateJson.length(); i++) {
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    Resident resident = new Resident();
                                    resident.setId(dataJson1.getString("uid"));
                                    resident.setImgUrl(dataJson1.getString("IDphoto"));
                                    resident.setIs_select(false);
                                    resident.setIs_temp("1");
                                    resident.setName(dataJson1.getString("realname"));
                                    resident.setUsername(dataJson1.getString("username"));

                                    select_map.put(resident.getId(), listData.size());
                                    listData.add(resident);
                                }

                                //处理传输过来的数据
                                select_list.clear();

                                Set<String> keys = select_Resident.keySet();
                                Iterator<String> iterator = keys.iterator();
                                while (iterator.hasNext()) {
                                    String key = iterator.next();
                                    if (select_map.containsKey(key)) {
                                        listData.get(Integer.parseInt(select_map.get(key).toString())).setIs_select(true);
                                    }
                                }

                                baseListAdapterVideo.notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        setResultStatus(listData.size() > 0, jsonObject.getInt("code"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        setResultStatus(listData.size() > 0, UNKONW_EXCEPTION_CODE);
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
                                JSONObject dataListObj = dataJson.getJSONObject("dataList");
                                JSONArray dateJson = dataListObj.getJSONArray("base_list");
                                spinner_list.clear();
                                spinner_list2.clear();
                                spinner_list3.clear();
                                spinner_list4.clear();
                                spinner_map.clear();
                                spinner_map2.clear();
                                for (int i = 0; i < dateJson.length(); i++) {
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    spinner_map.put(dataJson1.getString("name"), dataJson1.getString("base_id"));
                                    spinner_list.add(dataJson1.getString("name"));
                                }
                                JSONArray keshi_data = dataListObj.getJSONArray("hospital_list");
                                for (int i = 0; i < keshi_data.length(); i++) {
                                    JSONObject dataJson1 = keshi_data.getJSONObject(i);
                                    spinner_map2.put(dataJson1.getString("name"), dataJson1.getString("hospital_kid"));
                                    spinner_list2.add(dataJson1.getString("name"));
                                }
                                JSONArray ls_training_years = dataListObj.getJSONArray("ls_training_years");
                                for (int i = 0; i < ls_training_years.length(); i++) {
                                    spinner_list3.add(ls_training_years.getString(i));
                                }
                                JSONArray exam_situation_is_ep = dataListObj.getJSONArray("exam_situation_is_ep");
                                for (int i = 0; i < exam_situation_is_ep.length(); i++) {
                                    spinner_list4.add(exam_situation_is_ep.getString(i));
                                }
                                initthe_activity_type();
                                MyProgressBarDialogTools.hide();
                                getstudentList();
                            } else {
                                MyProgressBarDialogTools.hide();
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
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

    private void setResultStatus(boolean status, int code) {
        if (status) {
            management_nodata.setVisibility(View.GONE);
            student_list.setVisibility(View.VISIBLE);
            submit.setVisibility(View.VISIBLE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                management_nodata.setNetErrorIcon();
            } else {
                management_nodata.setLastEmptyIcon();
            }

            student_list.setVisibility(View.GONE);
            submit.setVisibility(View.GONE);
            management_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.select_staff);
        context = this;
        ButterKnife.bind(this);
        fid = getIntent().getStringExtra("fid");
        findId();
        initViews();
        getBase();
    }

    public void initthe_activity_type() {
        the_activity_type.setTextColor(Color.BLACK);
        the_activity_type.attachDataSource(spinner_list);
        the_activity_type.setArrowDrawable(getResources().getDrawable(R.mipmap.down_icon));
        the_activity_type.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getstudentList();
            }
        });

        the_activity_type2.setTextColor(Color.BLACK);
        the_activity_type2.attachDataSource(spinner_list2);
        the_activity_type2.setArrowDrawable(getResources().getDrawable(R.mipmap.down_icon));
        the_activity_type2.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getstudentList();
            }
        });
        the_activity_type3.setTextColor(Color.BLACK);
        the_activity_type3.attachDataSource(spinner_list3);
        the_activity_type3.setArrowDrawable(getResources().getDrawable(R.mipmap.down_icon));
        the_activity_type3.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getstudentList();
            }
        });
        the_activity_type4.setTextColor(Color.BLACK);
        the_activity_type4.attachDataSource(spinner_list4);
        the_activity_type4.setArrowDrawable(getResources().getDrawable(R.mipmap.down_icon));
        the_activity_type4.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getstudentList();
            }
        });
    }

    private void initViews() {
        List<Resident> rs = (List<Resident>) getIntent().getSerializableExtra("select_list");//获取list方式
        for (Resident resident : rs) {
            resident.setIs_select(true);
            intent_select_list.add(resident);
            select_Resident.put(resident.getId(), resident);
        }

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 1;
                getstudentList();
            }
        });

        baseListAdapterVideo = new BaseListAdapter(student_list, listData, R.layout.item_select_staff) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Resident map = (Resident) item;
                helper.setImageBitmap(R.id._item_img, map.getImgUrl());
                helper.setText(R.id._item_name,map.getName()+"("+map.getUsername()+")");
                if (map.is_select()) {
                    helper.setImage(R.id._item_select, R.mipmap.training_11);
                } else {
                    helper.setImage(R.id._item_select, R.mipmap.training_12);
                }

            }
        };
        student_list.setAdapter(baseListAdapterVideo);
        // listview点击事件
        student_list.setOnItemClickListener(new casesharing_listListener());

        baseListAdapterVideo.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > 0) {
                }
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = student_list.getChildAt(0);

                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = student_list.getChildAt(student_list.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == student_list.getHeight()) {
                        if ((page * limit) <= listData.size()) {
                            page += 1;
                            getstudentList();
                        }
                    }
                }
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //数据是使用Intent返回
                Intent intent = new Intent();
                List<Resident> list = new ArrayList<>();
                Collection<Resident> collection = select_Resident.values();
                Iterator<Resident> iterator = collection.iterator();
                while (iterator.hasNext()) {
                    Resident resident = iterator.next();
                    if (resident.is_select()) {
                        list.add(resident);
                    }
                }
                //把返回数据存入Intent
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", (Serializable) list);//序列化,要注意转化(Serializable)
                intent.putExtras(bundle);//发送数据
                //设置返回数据
                Select_staff.this.setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        select_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int i = 0;
                for (Resident resident : listData) {
                    if (!resident.is_select()) {
                        i = 1;
                    }
                }

                if (i == 1) {
                    for (Resident resident : listData) {
                        resident.setIs_select(true);
                        select_Resident.put(resident.getId(), resident);
                    }
                } else {
                    for (Resident resident : listData) {
                        resident.setIs_select(false);
                        select_Resident.remove(resident.getId());
                    }
                }

                baseListAdapterVideo.notifyDataSetChanged();
            }
        });
    }


    /**
     * name: 点击查看某个视频的详细 author:Tom 2016-1-28下午3:42:08
     */
    private class casesharing_listListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {
            if (listData.get(arg2).is_select()) {
                listData.get(arg2).setIs_select(false);
                select_Resident.remove(listData.get(arg2).getId());
            } else {
                listData.get(arg2).setIs_select(true);
                select_Resident.put(listData.get(arg2).getId(), listData.get(arg2));
            }
            baseListAdapterVideo.notifyDataSetChanged();
        }

    }


    public void getstudentList() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getUsersNew);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("fid", fid);
                    obj.put("page", page);
                    obj.put("limit", limit);
                    obj.put("keyword", keyword_text.getText() != null ? keyword_text.getText().toString() : "");
                    obj.put("base_id", spinner_map.containsKey(the_activity_type.getText().toString()) ? spinner_map.get(the_activity_type.getText().toString()) : "");
                    obj.put("hospital_kid", spinner_map2.containsKey(the_activity_type2.getText().toString()) ? spinner_map2.get(the_activity_type2.getText().toString()) : "");
                    obj.put("ls_training_years", the_activity_type3.getText().toString());
                    obj.put("exam_situation_is_ep", the_activity_type4.getText().toString());

                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());

                    LogUtil.e("获取可选择的学员", result);

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

    public void getBase() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getScreenData);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("is_type", 1);

                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    Log.e("PRETTY_LOGGER", "run() returned: " + result);

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
