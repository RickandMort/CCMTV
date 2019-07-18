package com.linlic.ccmtv.yx.activity.my;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.adapter.RecommendAdapter;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * name：用户推荐页
 * author：Larry
 * data：2016/9/21.
 */
public class RecommendActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private GridView gridview;
    private RecommendAdapter adapter;
    private ArrayList<String> mList;
    // 用来存放选中的item
    private ArrayList<Integer> list;
    private ArrayList<String> mList2;// 用来存放选中的数据
    private Context context;
    private String uid;
    int page = 1;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    LinearLayout layout_onefollow;
    private TextView tv_change;
    private ImageView iv_search;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                  /*  MyProgressBarDialogTools.hide(context, R.id.layout_loading);
                    hideLoading();*/
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功
                            data.clear();
                            JSONArray dataArray = result
                                    .getJSONArray("data");
                            if (dataArray.length() == 0) {
                                //暂无更多数据
                                Toast.makeText(context, "暂无更多数据", Toast.LENGTH_SHORT).show();
                            } else {
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject object = dataArray.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("uid", object.getString("uid"));
                                    map.put("keshi", object.getString("keshi"));
                                    map.put("icon", object.getString("icon"));
                                    map.put("username", object.getString("username"));
                                    data.add(map);
                                }
                                for (int i = 0; i < data.size(); i++) {//这里必须添加   原因参见RecommendAdapter
                                    mList.add("item" + i);
                                    mList2.add("item" + i);
                                    list.add(i);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        } else {//失败
//                            showNoData();
                        }
                        setResultStatus(data.size() > 0, result.getInt("status"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    layout_onefollow.setClickable(true);
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        Toast.makeText(context,
                                result.getString("errorMessage"),
                                Toast.LENGTH_SHORT).show();
                        mList.clear();
                        list.clear();
                        mList2.clear();
                        // 添加假数据
//                        for (int i = 0; i < 9; i++) {
//                            mList.add("item" + i);
//                            mList2.add("item" + i);
//                            list.add(i);
//                        }
                        initData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    setResultStatus(data.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOST);
                    break;
                default:
                    break;
            }
        }
    };
    private NodataEmptyLayout recommend_nodata;

    private void setResultStatus(boolean status, int code) {
        if (status) {
            gridview.setVisibility(View.VISIBLE);
            layout_onefollow.setVisibility(View.VISIBLE);
            recommend_nodata.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                recommend_nodata.setNetErrorIcon();
            } else {
                recommend_nodata.setLastEmptyIcon();
            }
            gridview.setVisibility(View.GONE);
            layout_onefollow.setVisibility(View.GONE);
            recommend_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        // 初始化控件
        context = this;

        mList = new ArrayList<String>();
        list = new ArrayList<Integer>();
        mList2 = new ArrayList<String>();
//        // 添加假数据
//        for (int i = 0; i < 9; i++) {
//            mList.add("item" + i);
//            mList2.add("item" + i);
//            list.add(i);
//        }
        findId();
        setText();
        initData();
    }


    public void findId() {
        super.findId();
        gridview = (GridView) findViewById(R.id.gridView1);
        layout_onefollow = (LinearLayout) findViewById(R.id.layout_onefollow);
        tv_change = (TextView) findViewById(R.id.tv_change);
        iv_search = (ImageView) findViewById(R.id.iv_search);
        recommend_nodata = findViewById(R.id.recommend_nodata);
    }

    private void setText() {
        super.setActivity_title_name("为我推荐");
        adapter = new RecommendAdapter(mList, list, data, context);
        gridview.setAdapter(adapter);
        gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridview.setOnItemClickListener(this);
        layout_onefollow.setOnClickListener(this);
        tv_change.setOnClickListener(this);
        iv_search.setOnClickListener(this);
    }


    /**
     * name:导入初始值
     */
    public void initData() {
       /* showLoading();
        MyProgressBarDialogTools.show(context, R.id.layout_loading);*/
        //判断是否登录
        uid = SharedPreferencesTools.getUid(context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject object = new JSONObject();
                try {
                    object.put("uid", uid);
                    object.put("page", (int) (Math.random() * 100));
                    object.put("act", URLConfig.recommendAttention);
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.CCMTVAPP, object.toString());
//                    Log.i("result", "result" + result);
                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);

                } catch (JSONException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        }).start();
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

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                            long arg3) {
        // TODO Auto-generated method stub
        String name = mList.get(position);
        if (list.contains(position)) {
            for (int i = 0; i < list.size(); i++) {
                if (position == list.get(i)) {
                    list.remove(i);
                    mList2.remove(name);
                    // 可把获取到的字段作为参数传递
                }
            }

        } else {
            list.add(position);
            // 保存选中的数据
            mList2.add(name);
            // 可把获取到的字段作为参数传递
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_change:

                mList.clear();
                list.clear();
                mList2.clear();
//                // 添加假数据
//                for (int i = 0; i < 9; i++) {
//                    mList.add("item" + i);
//                    mList2.add("item" + i);
//                    list.add(i);
//                }
                initData();
                // 添加假数据
               /* for (int i = 0; i < 9; i++) {
                    mList.add("item" + i);
                    mList2.add("item" + i);
                    list.add(i);
                }*/
                break;
            case R.id.iv_search:
                Intent intent = new Intent(RecommendActivity.this, UserSearchActivity.class);
                startActivity(intent);
                break;

            case R.id.layout_onefollow:
                layout_onefollow.setClickable(false);
                ArrayList<String> mList3 = new ArrayList<String>();
                for (int i = 0; i < list.size(); i++) {
                    mList3.add(data.get(list.get(i)).get("uid").toString());
                }
                final String uidstr_one = mList3.toString().replace("[", "");
                String uidstr_two = uidstr_one.replace("]", "");
                final String uidstr = uidstr_two.replace(",", "//");
//                Log.i("uidstr", "uidstr" + uidstr);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject object = new JSONObject();
                        try {
                            object.put("uid", uid);
                            object.put("uidstr", uidstr);
                            object.put("act", URLConfig.followAllUser);
                            String result = HttpClientUtils.sendPost(context,
                                    URLConfig.CCMTVAPP, object.toString());
//                            Log.i("result", "result" + result);
                            Message message = new Message();
                            message.what = 2;
                            message.obj = result;
                            handler.sendMessage(message);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            handler.sendEmptyMessage(500);
                        }
                    }
                }).start();
                break;

            default:
                break;
        }

    }
}
