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
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.utils.MyGridView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tom.li on 2016/3/16.
 */
public class Popular_search2 extends BaseActivity {
    private ListView popular_search_list;// 数据加载
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> data_list = new ArrayList<Map<String, Object>>();
    private SimpleAdapter sim_adapter;
    SimpleAdapter adapter;
    private ImageView search_img;
    private EditText search_edit;
    BaseListAdapter baseListAdapter;
    private MyGridView hot_search_grid;
    private TextView search_recall;
    private int resultCode = 0;
    Context context;
    private LinearLayout hean_Search_History_layout;
    private LinearLayout three_hean_clearance;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:

                    break;
                case 500:
                    System.out.println(R.string.post_hint1);
                    Toast.makeText(Popular_search2.this, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_search_three2);
        context = this;

        setText();
        findById();
        setmsgdb();
        onClick();
    }

    public void onClick() {
        search_recall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent();
                mIntent.putExtra("keyword", "");
                // 设置结果，并进行传送
                Popular_search2.this.setResult(resultCode, mIntent);
                //关闭掉这个Activity
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
                            .hideSoftInputFromWindow(Popular_search2.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    //存储数据
                    if (search_edit.getText().toString().trim().length() > 0) {
                        //存储数据
                        MyDbUtils.saveHot_search_grid(Popular_search2.this, search_edit.getText().toString());
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
        Intent mIntent = new Intent();
        mIntent.putExtra("keyword", search_edit.getText().toString());
        // 设置结果，并进行传送
        this.setResult(resultCode, mIntent);
        //关闭掉这个Activity
        finish();
    }

    public void findById() {
        hot_search_grid = (MyGridView) findViewById(R.id.hot_search_grid);
        hean_Search_History_layout = (LinearLayout) findViewById(R.id.hean_Search_History_layout);
        three_hean_clearance = (LinearLayout) findViewById(R.id.three_hean_clearance);
        search_edit = (EditText) findViewById(R.id.editText1);
        search_recall = (TextView) findViewById(R.id.search_recall);
    }

    public void setText() {
        List<Hot_search_grid> list = MyDbUtils.findHot_search_grid_All(Popular_search2.this);
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
                    hot_search_grid.setOnItemClickListener(new casesharing_listListener2());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("加载数据出错了！");
                }
            }
        };
        new Thread(runnable).start();
    }

    /**
     * name: 点击查看某个视频的详细 author:Tom 2016-1-28下午3:42:08
     */
    private class casesharing_listListener2 implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
            TextView button = (TextView) view.findViewById(R.id.Search_line);
            //存储数据
            MyDbUtils.saveHot_search_grid(Popular_search2.this, button.getText().toString());

            Intent mIntent = new Intent();
            mIntent.putExtra("keyword", button.getText().toString());
            // 设置结果，并进行传送
            Popular_search2.this.setResult(resultCode, mIntent);
            //关闭掉这个Activity
            finish();
        }
    }

    @Override
    public void back(View view) {
        Popular_search2.this.setResult(0, new Intent());
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent mIntent = new Intent();
        mIntent.putExtra("keyword", "");
        // 设置结果，并进行传送
        Popular_search2.this.setResult(resultCode, mIntent);
        //关闭掉这个Activity
        finish();
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn/Member/Index.html";
        super.onPause();
    }

}
