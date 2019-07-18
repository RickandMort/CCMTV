package com.linlic.ccmtv.yx.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.db.MyDbUtils;
import com.linlic.ccmtv.yx.activity.entity.Hot_search_grid;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyGridView;
import com.linlic.ccmtv.yx.utils.MyListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tom.li on 2016/3/16.
 */
public class Popular_search extends BaseActivity {
    private ListView popular_search_list;// 数据加载
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> data_list = new ArrayList<Map<String, Object>>();
    private SimpleAdapter sim_adapter;
    SimpleAdapter adapter;
    private String mode = "";
    private ImageView search_img;
    private EditText search_edit;
    BaseListAdapter baseListAdapter;
    private MyListView trend_Search;
    private MyGridView hot_search_grid;
    private TextView search_recall;
    Context context;
    private LinearLayout hean_Search_History_layout;
    private LinearLayout three_hean_clearance;
    private boolean isEnter = true;//防止多次点击，进入页面多次

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    trend_Search.setAdapter(baseListAdapter);
                    break;
                case 500:
                    Toast.makeText(Popular_search.this, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_search_three);
        context = this;
        Intent intent = getIntent();
        mode = intent.getExtras().getString("mode");//方式 1 表示从首页title-img 进入到热门搜索页 2 表示从搜索也进入到搜索页
        setText();
        findById();
        setmsgdb();
        onClick();
    }

    public void onClick() {
        search_recall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        search_edit.setOnKeyListener(new View.OnKeyListener() {

            @Override

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    //以下方法防止两次发送请求

                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(Popular_search.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    //存储数据
                    if (search_edit.getText().toString().trim().length() > 0) {
                        //存储数据
                        MyDbUtils.saveHot_search_grid(Popular_search.this, search_edit.getText().toString());
                        intents();
                    } else {
                        intents();
                    }
                }
                return false;
            }
        });

        three_hean_clearance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDbUtils.deleteHot_search_gridALL(context);
                data_list.removeAll(data_list);
                sim_adapter.notifyDataSetChanged();
                hean_Search_History_layout.setVisibility(View.GONE);
            }
        });
    }

    public void intents() {
        if (mode.equals("1")) {//代表是从首页title-img进入的
            Intent intent = new Intent(Popular_search.this, CustomActivity2.class);
            intent.putExtra("type","home");
            intent.putExtra("custom_title", search_edit.getText().toString());
            intent.putExtra("mode", "1");
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(Popular_search.this, CustomActivity2.class);
            intent.putExtra("type","home");
            intent.putExtra("custom_title", search_edit.getText().toString());
            intent.putExtra("mode", "1");
            startActivity(intent);
            finish();
            CustomActivity2.isFilsh.finish();
        }
    }

    public void findById() {
        trend_Search = (MyListView) findViewById(R.id.trend_Search);
        hot_search_grid = (MyGridView) findViewById(R.id.hot_search_grid);
        hean_Search_History_layout = (LinearLayout) findViewById(R.id.hean_Search_History_layout);
        three_hean_clearance = (LinearLayout) findViewById(R.id.three_hean_clearance);
        search_edit = (EditText) findViewById(R.id.editText1);
        search_recall = (TextView) findViewById(R.id.search_recall);
    }

    public void setText() {
        List<Hot_search_grid> list = MyDbUtils.findHot_search_grid_All(Popular_search.this);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < (list.size() > 10 ? 10 : list.size()); i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("text", list.get(i).getHot_search_name());
                data_list.add(map);
            }
        }
    }

    /**
     * name:设置listview中的值 author:Tom 2016-1-28下午3:42:47
     */
    public void setmsgdb() {
        if (data_list.size() > 0) {
            //新建适配器
            String[] from = {"text"};
            int[] to = {R.id.Search_line};
            sim_adapter = new SimpleAdapter(this, data_list, R.layout.hot_search_grid, from, to);
            //配置适配器
            hot_search_grid.setAdapter(sim_adapter);
        } else {
            hean_Search_History_layout.setVisibility(View.GONE);
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    popular_search_list = (ListView) findViewById(R.id.popular_search_list);
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.hotKeyword);
                    String result = HttpClientUtils.sendPost(Popular_search.this, URLConfig.CCMTVAPP_SEARCH, obj.toString());
//                    Log.e(getLocalClassName(), "搜索热度排行数据："+result);
                    JSONObject jsonreults = new JSONObject(result);
                    if (jsonreults.getInt("status") == 1) {// 成功
                        JSONArray dataArray = jsonreults.getJSONArray("data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jsonObject = dataArray.getJSONObject(i);
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("Search_line", "" + (i + 1));
                            map.put("Search_Text", jsonObject.getString("name"));
                            data.add(map);
                        }
                    }

                    Button button = new Button(context);
                    button.setText("");
                    baseListAdapter = new BaseListAdapter(trend_Search, data, R.layout.acitvity_search_three_list_item) {

                        @Override
                        public void refresh(Collection datas) {
                            super.refresh(datas);
                        }

                        @Override
                        public void convert(ListHolder helper, Object item, boolean isScrolling) {
                            super.convert(helper, item, isScrolling);
                            helper.setText(R.id.Search_line, ((Map) item).get("Search_line") + "");
                            if (Integer.parseInt(((Map) item).get("Search_line").toString()) == 1) {
                                helper.setViewBG(R.id.Search_line, context.getResources().getDrawable(R.mipmap.search2_01));
                            } else if (Integer.parseInt(((Map) item).get("Search_line").toString()) == 2) {
                                helper.setViewBG(R.id.Search_line, context.getResources().getDrawable(R.mipmap.search2_02));
                            } else if (Integer.parseInt(((Map) item).get("Search_line").toString()) == 3) {
                                helper.setViewBG(R.id.Search_line, context.getResources().getDrawable(R.mipmap.search2_03));
                            } else {
                                helper.setViewBG(R.id.Search_line, context.getResources().getDrawable(R.mipmap.search2_04));
                            }
                            helper.setText(R.id.Search_Text, ((Map) item).get("Search_Text") + "");
                        }
                    };

                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                    // listview点击事件
                    trend_Search.setOnItemClickListener(new casesharing_listListener());
                    hot_search_grid.setOnItemClickListener(new casesharing_listListener2());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    /**
     * name: 点击查看某个视频的详细 author:Tom 2016-1-28下午3:42:08
     */
    private class casesharing_listListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
            TextView textView = (TextView) view.findViewById(R.id.Search_Text);
            //存储数据
            MyDbUtils.saveHot_search_grid(Popular_search.this, textView.getText().toString());
            if (mode.equals("1")) {//代表是从首页title-img进入的
                Intent intent = new Intent(Popular_search.this, CustomActivity2.class);
                intent.putExtra("type","home");
                intent.putExtra("custom_title", textView.getText().toString());
                intent.putExtra("mode", "1");
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(Popular_search.this, CustomActivity2.class);
                intent.putExtra("type","home");
                intent.putExtra("custom_title", textView.getText().toString());
                intent.putExtra("mode", "1");
                startActivity(intent);
                finish();
                CustomActivity2.isFilsh.finish();
            }
        }
    }

    /**
     * name: 点击查看某个视频的详细 author:Tom 2016-1-28下午3:42:08
     */
    private class casesharing_listListener2 implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
            if (isEnter) {
                TextView button = (TextView) view.findViewById(R.id.Search_line);
                //存储数据
                MyDbUtils.saveHot_search_grid(Popular_search.this, button.getText().toString());
                if (mode.equals("1")) {//代表是从首页title-img进入的
                    Intent intent = new Intent(Popular_search.this, CustomActivity2.class);
                    intent.putExtra("type", "home");
                    intent.putExtra("custom_title", button.getText().toString());
                    intent.putExtra("mode", "1");
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(Popular_search.this, CustomActivity2.class);
                    intent.putExtra("type", "home");
                    intent.putExtra("custom_title", button.getText().toString());
                    intent.putExtra("mode", "1");
                    startActivity(intent);
                    finish();
                    CustomActivity2.isFilsh.finish();
                }
                isEnter = false;
            }
        }
    }

    @Override
    protected void onResume() {
        isEnter = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn";
        super.onPause();
    }

    @Override
    public void back(View view) {
        Popular_search.this.setResult(0, new Intent());
        finish();

    }
}
