package com.linlic.ccmtv.yx.activity.hospital_training;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.adapter.DepartmentsGridAdapter;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/5.
 */

public class Departments extends BaseActivity {
    private Context context;
    private ListView department_list;
    private GridView department_gridview;
    private DepartmentsGridAdapter departmentsGridAdapter;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> mindata = new ArrayList<Map<String, Object>>();
    BaseListAdapter baseListAdapter;
    //当前Item被点击的位置
    private int currentItem = 0;
    String aidStr = "";
    private TextView department_name, multiple_choice, begin_practice, tvCollection;
    private boolean is_show = false;
    private String select_item_name = "";
    private boolean is_multiple_choice = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
//                            setActivity_title_name(jsonObject.getString("test_list_title"));
                            JSONArray dataArray = jsonObject
                                    .getJSONArray("father");
//                            System.out.println("进入到搜索解析页：" + dataArray);
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject customJson = dataArray.getJSONObject(i);
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("department_name", customJson.getString("name"));
                                map.put("department_id", customJson.getString("id"));
                                map.put("department_fid", customJson.getString("pid"));
                                map.put("department_type", 0);
                                data.add(map);
                            }

                            baseListAdapter.notifyDataSetChanged();

                            mindata.clear();
                            //改变选中状态
                            departmentsGridAdapter.setCurrentItem(-1);
                            JSONArray childArray = jsonObject
                                    .getJSONArray("child");
//                            System.out.println("进入到搜索解析页：" + dataArray);
                            for (int i = 0; i < childArray.length(); i++) {
                                JSONObject customJson = childArray.getJSONObject(i);
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("department_name", customJson.getString("name"));
                                map.put("department_id", customJson.getString("id"));
                                map.put("department_fid", customJson.getString("pid"));
                                map.put("department_type", 0);
                                mindata.add(map);
                            }
                            departmentsGridAdapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(Departments.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功


                            if (is_show) {
                                if (jsonObject.has("data")) {
                                    //单选状态下 右边选中的子类有自己的子孙层 所以先将 右边数据复制到左边
                                    Map<String, Object> map1 = data.get(currentItem);
                                    map1.put("department_name", "返回上一级");
                                    data.clear();
                                    data.add(map1);
                                    data.addAll(mindata);
                                    currentItem = departmentsGridAdapter.getCurrentItem()+1;
                                    baseListAdapter.refresh(data);


                                    select_item_name = mindata.get(departmentsGridAdapter.getCurrentItem()).get("department_name").toString();
//                                    setDepartment_name();
                                    mindata.clear();
                                    //改变选中状态
                                    departmentsGridAdapter.setCurrentItem(-1);
                                    JSONArray dataArray = jsonObject
                                            .getJSONArray("data");
//                            System.out.println("进入到搜索解析页：" + dataArray);
                                    for (int i = 0; i < dataArray.length(); i++) {
                                        JSONObject customJson = dataArray.getJSONObject(i);
                                        Map<String, Object> map = new HashMap<String, Object>();
                                        map.put("department_name", customJson.getString("name"));
                                        map.put("department_id", customJson.getString("id"));
                                        map.put("department_fid", customJson.getString("pid"));
                                        map.put("department_type", 0);
                                        mindata.add(map);
                                    }
                                    departmentsGridAdapter.notifyDataSetChanged();
                                }
                            }

                        }else {
                            Toast.makeText(Departments.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        departmentsGridAdapter.notifyDataSetChanged();
//                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 3:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功

                            //改变选中状态
                            departmentsGridAdapter.setCurrentItem(-1);
                            if (jsonObject.has("data")) {
                                mindata.clear();

                                JSONArray dataArray = jsonObject
                                        .getJSONArray("data");
//                            System.out.println("进入到搜索解析页：" + dataArray);
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject customJson = dataArray.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("department_name", customJson.getString("name"));
                                    map.put("department_id", customJson.getString("id"));
                                    map.put("department_fid", customJson.getString("pid"));
                                    map.put("department_type", 0);
                                    mindata.add(map);
                                }
                                departmentsGridAdapter.notifyDataSetChanged();
                            }

                        } else {
                            Toast.makeText(Departments.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        departmentsGridAdapter.notifyDataSetChanged();
//                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 4:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功

                            if (jsonObject.has("data")) {
                                //单选状态下 右边选中的子类有自己的子孙层 所以先将 右边数据复制到左边

                                mindata.clear();
                                mindata.addAll(data);
                                if (mindata.get(0).get("department_name").toString().equals("返回上一级")) {
                                    mindata.remove(0);
                                }

                                for (Map map : mindata) {
                                    map.put("department_type", 0);
                                }

                                departmentsGridAdapter.notifyDataSetChanged();
                                //改变选中状态
                                departmentsGridAdapter.setCurrentItem(-1);
                                Map<String, Object> map1 = new HashMap<>();
                                map1.put("department_name", "返回上一级");
                                data.clear();

                                JSONArray dataArray = jsonObject
                                        .getJSONArray("data");
                                boolean bool = false;
//                            System.out.println("进入到搜索解析页：" + dataArray);
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject customJson = dataArray.getJSONObject(i);
                                    if(i == 0 && !customJson.getString("pid").equals("0")){
                                        map1.put("department_id", customJson.getString("pid"));
                                        data.add(map1);
                                        bool = true;
                                    }
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("department_name", customJson.getString("name"));
                                    map.put("department_id", customJson.getString("id"));
                                    map.put("department_fid", customJson.getString("pid"));
                                    map.put("department_type", 0);
                                    if (mindata.get(0).get("department_fid").equals(customJson.getString("id"))) {
                                        if(bool){
                                            currentItem = i+1;
                                        }else{
                                            currentItem = i;
                                        }
                                    }
                                    data.add(map);
                                }
                                baseListAdapter.notifyDataSetChanged();
                            }

                        } else {
                            Toast.makeText(Departments.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        departmentsGridAdapter.notifyDataSetChanged();
//                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 5:
                    try {
//                        MyProgressBarDialogTools.hide();
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功

                            Intent intent = new Intent(context, Practice.class);
                            intent.putExtra("paperid", jsonObject.getString("paperid"));
                            intent.putExtra("lastPage", "1");
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Departments.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        departmentsGridAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
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
        setContentView(R.layout.departments);
        context = this;
        findId();
        initData();
        initMax_Department_list();

    }

    @Override
    public void findId() {
        super.findId();
        department_list = (ListView) findViewById(R.id.department_list);
        department_gridview = (GridView) findViewById(R.id.department_gridview);
        department_name = (TextView) findViewById(R.id.department_name);
        multiple_choice = (TextView) findViewById(R.id.multiple_choice);
        begin_practice = (TextView) findViewById(R.id.id_tv_practice_department_begin);
        tvCollection = (TextView) findViewById(R.id.activity_collection_list);

        begin_practice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Enter_the_test(v);
            }
        });

        tvCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCollection_list(v);
            }
        });
    }

    public void initData() {
        multiple_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_multiple_choice) {
                    //修改为非多选
                    is_multiple_choice = false;
                    multiple_choice.setTextColor(Color.parseColor("#3798F9"));
                    multiple_choice.setBackground(getResources().getDrawable(R.drawable.anniu18));
                    for (int i = 0; i < mindata.size(); i++) {
//                        if (i != departmentsGridAdapter.getCurrentItem()) {
                            //改变选中状态
                            mindata.get(i).put("department_type", 0);
//                        }
                    }
                    //通知ListView改变状态
                    departmentsGridAdapter.notifyDataSetChanged();
                } else {
                    //修改为多选状态
                    is_multiple_choice = true;
                    multiple_choice.setTextColor(Color.parseColor("#ffffff"));
                    multiple_choice.setBackground(getResources().getDrawable(R.drawable.anniu3));
                }
            }
        });
        departmentsGridAdapter = new DepartmentsGridAdapter(context, mindata);
        department_gridview.setAdapter(departmentsGridAdapter);
        //注册监听事件
        department_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                    TextView department_id = (TextView) v.findViewById(R.id.department_id);
                    if (is_multiple_choice) {
                        //改变选中状态
                        if (mindata.get(position).get("department_type").toString().equals("0")) {
                            mindata.get(position).put("department_type", 1);
                            departmentsGridAdapter.setCurrentItem(position);
                            //通知ListView改变状态

                            departmentsGridAdapter.notifyDataSetChanged();
                        } else {
                            mindata.get(position).put("department_type", 0);
                            departmentsGridAdapter.setCurrentItem(position);
                            //通知ListView改变状态
                            departmentsGridAdapter.notifyDataSetChanged();
                        }

                    } else {
                        MyProgressBarDialogTools.show(context);
                        //改变选中状态
                        if (departmentsGridAdapter.getCurrentItem() == position) {
                            mindata.get(position).put("department_type", 0);
                            departmentsGridAdapter.setCurrentItem(-1);
                            //通知ListView改变状态
                            departmentsGridAdapter.notifyDataSetChanged();
                            MyProgressBarDialogTools.hide();
                        } else {
                            mindata.get(position).put("department_type", 1);
                            if (departmentsGridAdapter.getCurrentItem() != -1) {
                                mindata.get(departmentsGridAdapter.getCurrentItem()).put("department_type", 0);
                            }
                            departmentsGridAdapter.setCurrentItem(position);
                            //通知ListView改变状态
                            departmentsGridAdapter.notifyDataSetChanged();
                            is_show = true;
                            initMin_Department_list(department_id.getText().toString());
                        }

                    }

            }
        });

        baseListAdapter = new BaseListAdapter(department_list, data, R.layout.department_list_item) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);


                helper.setText(R.id.department_name, ((Map) item).get("department_name") + "");
                helper.setText(R.id.department_id, ((Map) item).get("department_id") + "");
                if (data.get(currentItem).get("department_id") == ((Map) item).get("department_id")) {
                    //如果被点击，设置当前TextView被选中
                    helper.setTextselect(R.id.department_name, true);
                    helper.setBackground_color(R.id.department_layout, Color.parseColor("#ebf4fe"));
                } else {
                    //如果没有被点击，设置当前TextView未被选中
                    helper.setTextselect(R.id.department_name, false);
                    helper.setBackground_color(R.id.department_layout, Color.parseColor("#ffffff"));
                }

            }
        };
        department_list.setAdapter(baseListAdapter);
        // listview点击事件
        department_list
                .setOnItemClickListener(new casesharing_listListener());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    /** 判断是否是快速点击 */
    private static long lastClickTime;
    public static boolean checkDoubleClick(){
        //点击时间
        long clickTime = SystemClock.uptimeMillis();
        //如果当前点击间隔小于500毫秒
        if (lastClickTime >= clickTime - 1000) {
            return true;
        }
        //记录上次点击时间
        lastClickTime = clickTime;
        return false;
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yy.ccmtv.cn/exam_bank/exercise.html";
        super.onPause();
    }

    /**
     * name: 点击查看某个科室 author:Tom 2016-1-28下午3:42:08
     */
    private class casesharing_listListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {
            MyProgressBarDialogTools.show(context);
            TextView department_name = (TextView) view
                    .findViewById(R.id.department_name);
            if (department_name.getText().toString().equals("返回上一级")) {
                initMin_Department_list3(data.get(arg2).get("department_id").toString());
            } else {
                currentItem = arg2;
                baseListAdapter.notifyDataSetChanged();
                initMin_Department_list2(data.get(arg2).get("department_id").toString());

            }


        }

    }

    public void setDepartment_name() {
        if (select_item_name.length() > 0) {
            if (department_name.getText().toString().length() > 0) {
                department_name.setText(department_name.getText() + " > " + select_item_name);
            } else {
                department_name.setText(select_item_name);
            }
        } else {
            department_name.setText(select_item_name);
        }
        if (department_name.getText().toString().trim().length() > 0) {
            department_name.setVisibility(View.VISIBLE);
        } else {
            department_name.setVisibility(View.GONE);
        }

    }


    public void initMax_Department_list() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.cateSelData);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("type", "0");
//                    Log.e("医院培训-科室max条件", obj.toString());
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.Hospital_training, obj.toString());
//                    Log.e("医院培训-科室max", result);
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

    /*右边边点击访问*/
    public void initMin_Department_list(final String id) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.parentData);
                    obj.put("id", id);
//                    Log.e("医院培训-科室min条件", obj.toString());
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.Hospital_training, obj.toString());
//                    Log.e("医院培训-科室min", result);
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

    /*左边点击使用*/
    public void initMin_Department_list2(final String id) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.parentData);
                    obj.put("id", id);
//                    Log.e("医院培训-科室min条件", obj.toString());
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.Hospital_training, obj.toString());
//                    Log.e("医院培训-科室min", result);
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

    /*左边点击使用*/
    public void initMin_Department_list3(final String id) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.parentData);
                    obj.put("id", id);
                    obj.put("type", 1);
//                    Log.e("医院培训-科室min条件", obj.toString());
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.Hospital_training, obj.toString());
//                    Log.e("医院培训-科室min", result);
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

    public void clickCollection_list(View view) {
        Intent intent = new Intent(context, Collection_list.class);
        intent.putExtra("bigType", getIntent().getStringExtra("bigType"));
        startActivity(intent);

    }

    public void Enter_the_test(View view) {
        MyProgressBarDialogTools.show(context);
        if (departmentsGridAdapter.getCurrentItem() != -1) {
            for (Map<String, Object> map : mindata) {
                if (map.get("department_type").toString().equals("1")) {
                    if (aidStr.length() > 0) {
                        aidStr += "," + map.get("department_id").toString();
                    } else {
                        aidStr = map.get("department_id").toString();
                    }
                }
            }
        } else {
            aidStr = data.get(currentItem).get("department_id").toString();
        }


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.questionsCate);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("ids", aidStr);
//                    Log.e("医院培训-提交条件", obj.toString());
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.Hospital_training, obj.toString());
//                    Log.e("医院培训-提交条件", result);
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

}
