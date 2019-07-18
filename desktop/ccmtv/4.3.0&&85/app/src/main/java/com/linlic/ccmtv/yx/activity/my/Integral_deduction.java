package com.linlic.ccmtv.yx.activity.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
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
 * name:积分扣除记录
 * author:Tom
 * 2016-3-2下午7:04:39
 */
public class Integral_deduction extends BaseActivity {
    private TextView activity_title_name;
    private ListView get_integral_list;// 数据加载
    private RecyclerView recyclerView;
    private SimpleAdapter adapter;
    String Str_username, integration;
    private TextView cases_detail_author, cases_detail_time;
    CircleImageView circle_item_img33;
    Context context;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功
                            JSONArray dataArray = result.getJSONArray("data");
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
                                    map.put("get_integral_item_increase", object.getString("money"));
                                    map.put("get_integral_item_time", object.getString("posttime").substring(0, 16));

                                    data.add(map);
                                }
                                adapter = new SimpleAdapter(Integral_deduction.this, data, R.layout.get_integral_item, new String[]{
                                        "get_integral_item_reason",
                                        "get_integral_item_increase",
                                        "get_integral_item_time"}, new int[]{
                                        R.id.get_integral_item_reason,
                                        R.id.get_integral_item_increase,
                                        R.id.get_integral_item_time});

                                get_integral_list.setAdapter(adapter);
                            }
                        } else {//失败
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                    }
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.get_integral);
        context = this;
        findId();
        setText();
        setmsgdb();
    }

    /**
     * name:查找值 author:Tom 2016-2-2下午5:29:04
     */
    public void findId() {
        super.findId();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_test_rv);
        recyclerView.setVisibility(View.GONE);
        get_integral_list = (ListView) findViewById(R.id.get_integral_list);
        activity_title_name = (TextView) findViewById(R.id.activity_title_name);
    }

    public void setText() {
        activity_title_name.setText(R.string.integral_deduction_name);
        integration = getIntent().getStringExtra("integration");
//        cases_detail_time.setText("当前可用积分:" + integration);
//        String UserName = SharedPreferencesTools.getUserName(context);
//        cases_detail_author.setText(UserName);
        //显示头像
        String Str_icon = SharedPreferencesTools.getStricon(context);
        loadImg(circle_item_img33, Str_icon);
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
                    startActivity(new Intent(Integral_deduction.this, LoginActivity.class));
                }

                try {
                    JSONObject obj = new JSONObject();
                    obj.put("uid", uid);
                    obj.put("flg", "deduction");
                    obj.put("act", URLConfig.integrationRecord);
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, obj.toString());

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
     * name:使用xutils 加载图片 author:Tom 2016-1-7下午1:28:03
     *
     * @param img  图片控件
     * @param path 图片网络地址
     */
    public void loadImg(ImageView img, String path) {
        XUtilsImageLoader xUtilsImageLoader = new XUtilsImageLoader(getApplicationContext());
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
