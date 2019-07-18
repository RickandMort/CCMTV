package com.linlic.ccmtv.yx.activity.cashier;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.login.LoginActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.XUtilsImageLoader;
import com.linlic.ccmtv.yx.widget.CircleImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tom on 2016/10/27.
 * 收入记录
 */
public class Cashier_revenue_log extends BaseActivity {
    Context context;
    private CircleImageView cashier_revenue_log_img;
    private TextView cashier_revenue_log_author;
    private TextView cashier_revenue_log_integral;
    private ListView get_integral_list;
    private String integration;
    private SimpleAdapter adapter;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) { //成功
                            JSONArray dataArray = result
                                    .getJSONArray("data");
                            if (dataArray.length() == 0) {
                                showNoData();
                                get_integral_list.setVisibility(View.GONE);
                            } else {
                                hideNoData();
                                get_integral_list.setVisibility(View.VISIBLE);
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject object = dataArray.getJSONObject(i);

                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("get_integral_item_reason", object.getString("about"));
                                    map.put("get_integral_item_increase", object.getString("mvmoney"));
                                    map.put("get_integral_item_time", object.getString("currenttime").substring(0, 16));

                                    data.add(map);

                                }
                                adapter = new SimpleAdapter(Cashier_revenue_log.this, data,
                                        R.layout.get_integral_item, new String[]{
                                        "get_integral_item_reason",
                                        "get_integral_item_increase",
                                        "get_integral_item_time"}, new int[]{
                                        R.id.get_integral_item_reason,
                                        R.id.get_integral_item_increase,
                                        R.id.get_integral_item_time});

                                get_integral_list.setAdapter(adapter);

                            }

                        } else {//失败
                            showNoData();
                            get_integral_list.setVisibility(View.GONE);
                            Toast.makeText(Cashier_revenue_log.this, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    Toast.makeText(Cashier_revenue_log.this, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cashier_revenue_log);
        context = this;
        findId();
        super.setActivity_title_name("收入记录");
        setText();
        setmsgdb();
    }
    /**
     * name:查找值 author:Tom 2016-2-2下午5:29:04
     */
    @Override
    public void findId() {
        super.findId();
        get_integral_list = (ListView) findViewById(R.id.get_integral_list);
        cashier_revenue_log_author = (TextView) findViewById(R.id.cashier_revenue_log_author);
        cashier_revenue_log_integral = (TextView) findViewById(R.id.cashier_revenue_log_integral);
        cashier_revenue_log_img = (CircleImageView) findViewById(R.id.cashier_revenue_log_img);
    }

    public void setText() {
        integration = getIntent().getStringExtra("balance");
        cashier_revenue_log_integral.setText("当前余额:" + integration);
        String UserName = SharedPreferencesTools.getUserName(Cashier_revenue_log.this);
        cashier_revenue_log_author.setText(UserName);
        //显示头像
        String Str_icon = SharedPreferencesTools.getStricon(Cashier_revenue_log.this);
        loadImg(cashier_revenue_log_img, Str_icon);
    }
    /**
     * name:设置listview中的值 author:Tom 2016-1-28下午3:42:47
     */
    public void setmsgdb() {
        loadData();
    }

    /**
     * name:导入初始值 author:Tom 2016-1-28下午3:41:32
     */
    public void loadData() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
                String uid = sharedPreferences.getString("uid", "");
                if (uid == null || "".equals(uid)) {
                    startActivity(new Intent(Cashier_revenue_log.this, LoginActivity.class));
                }

                try {
                    JSONObject obj = new JSONObject();
                    obj.put("uid", uid);
                    obj.put("act", URLConfig.cashierEarning);

                    String result = HttpClientUtils.sendPost(Cashier_revenue_log.this,
                            URLConfig.CCMTVAPP, obj.toString());
                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        }).start();

    }

    /**
     * name:使用xutils 夹在图片 author:Tom 2016-1-7下午1:28:03
     *
     * @param img  图片控件
     * @param path 图片网络地址
     */
    public void loadImg(ImageView img, String path) {

        XUtilsImageLoader xUtilsImageLoader = new XUtilsImageLoader(Cashier_revenue_log.this);
        xUtilsImageLoader.display(img, FirstLetter.getSpells(path));
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