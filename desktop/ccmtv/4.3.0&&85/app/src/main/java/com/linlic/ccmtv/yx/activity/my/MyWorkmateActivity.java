package com.linlic.ccmtv.yx.activity.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.login.LoginActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.XUtilsImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yu
 *         我的同事
 */
public class MyWorkmateActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2<ScrollView> {
    TextView activity_title_name;
    private ImageView ivSearch;
    ListView workmate_list;
    BaseListAdapter baseListAdapter;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private Map<String, Object> curr_map = null;
    private String uid;
    Context context;
    int page = 1;
    String Str_username;  //传过去作为发件人用户名
    private boolean isNoMore = false;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (page == 1) {
                        MyProgressBarDialogTools.hide();
                    }

                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功
                            JSONArray dataArray = result.getJSONArray("data");
                            if (dataArray.length() == 0) {
                                if (page == 1) {
                                    showNoData();
                                }
                            } else {
                                hideNoData();
                                if (page != 1 && dataArray.length() < 10) {
                                    isNoMore = true;
                                    Toast.makeText(context, "暂无更多数据", Toast.LENGTH_SHORT).show();
                                }
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject object = dataArray.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("Hisuid", object.getInt("uid"));
                                    map.put("icon", object.getString("icon"));
                                    map.put("username", object.getString("username"));
                                    map.put("name", object.getString("name"));
                                    map.put("follow", object.getString("follow"));
                                    map.put("position",data.size());
                                    //  map.put("keshi", object.getString("keshi"));
                                    data.add(map);
                                }

                                baseListAdapter.notifyDataSetChanged();
                                // Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                // finish();
                            }
                        } else {//失败
                            showNoData();
                            // Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:

                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {// 成功
                            if ("1".equals(result.getString("data"))) {
//                                but_follow.setText("已关注");
                                curr_map.put("follow","1");
                            } else {
//                                but_follow.setText("+关注");
                                curr_map.put("follow","0");
                            }
                            baseListAdapter.notifyDataSetChanged();
                        } else {// 失败
                            Toast.makeText(context,
                                    result.getString("errorMessage"),
                                    Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;

                case 500:
                    MyProgressBarDialogTools.hide();
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
        setContentView(R.layout.activity_myworkmate);
        context = this;
        findId();
        setText();
        initData();
        Str_username = getIntent().getStringExtra("Str_username");
    }

    @Override
    public void showNoData() {
        super.showNoData();
        btn_nodata.setVisibility(View.GONE);
    }

    public void findId() {
        super.findId();
        activity_title_name = (TextView) findViewById(R.id.activity_title_name);
        ivSearch = (ImageView) findViewById(R.id.id_iv_activity_title_8_right);
        workmate_list = (ListView) findViewById(R.id.workmate_list);
        // listview点击事件
        workmate_list.setOnItemClickListener(new mymessage_listListener());
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyWorkmateActivity.this, UserSearchActivity.class);
                startActivity(intent);
            }
        });
    }

    public void setText() {
        activity_title_name.setText(R.string.my_myworkmate);
        ivSearch.setVisibility(View.VISIBLE);
        ivSearch.setImageResource(R.mipmap.custom_search);

        baseListAdapter = new BaseListAdapter(workmate_list, data, R.layout.workmate_item) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                helper.setText(R.id.follow_item_title, ((Map) item).get("username") + "");
                helper.setText(R.id.tv_hospital, ((Map) item).get("name") + "");
                helper.setImageBitmap(R.id.iv_headImg, ((Map) item).get("icon") + "");
                helper.setVisibility(R.id.tv_department, View.GONE);
                helper.setTag(R.id.is_attention, ((Map) item).get("position") + "");
                if(((Map) item).get("follow").toString().equals("1")){
                    helper.setTextColor2(R.id.is_attention, Color.parseColor("#e3e3e3"));
                    helper.setText(R.id.is_attention, "✔ 已关注");
                    helper.setBackground_Image(R.id.is_attention, R.drawable.anniu20);
                }else{
                    helper.setTextColor2(R.id.is_attention, Color.parseColor("#69ADFA"));
                    helper.setText(R.id.is_attention, "+ 关注");
                    helper.setBackground_Image(R.id.is_attention, R.drawable.anniu26);
                }

                //helper.setText(R.id.tv_department, ((Map) item).get("keshi") + "");
            }
        };
        workmate_list.setAdapter(baseListAdapter);
        baseListAdapter.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = workmate_list.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        isNoMore = false;
                    }
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = workmate_list.getChildAt(workmate_list.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == workmate_list.getHeight()) {
                        if (!isNoMore) {
                            page += 1;
                            initData();
                        }
                    }
                }
            }
        });
    }

    /**
     * name:导入初始值
     */
    public void initData() {
        if (page == 1) {
            MyProgressBarDialogTools.show(context);
        }
        //获取uid、
        SharedPreferences sharedPreferences = getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        uid = sharedPreferences.getString("uid", "");
        if (uid == null || "".equals(uid)) {
            startActivity(new Intent(MyWorkmateActivity.this, LoginActivity.class));
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject object = new JSONObject();
                try {
                    object.put("uid", uid);
                    object.put("page", page);
                    object.put("act", URLConfig.myColleague);
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, object.toString());
//                    Log.e(getLocalClassName(), "同事数据"+result);
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

    /**
     * name: 点击我的消息进入详情页
     */
    class mymessage_listListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
            // 循环json（找到点击的文章详细）
            Intent intent = new Intent(MyWorkmateActivity.this, MyFollowDetails.class);
            intent.putExtra("Hisuid", data.get(arg2).get("Hisuid").toString());
            intent.putExtra("Str_username", Str_username);
            intent.putExtra("ismywork", true);
            startActivity(intent);
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        page = 1;
        data.removeAll(data);
        initData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        page += 1;
        initData();
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

    /**
     * 关注、、、取消关注
     */
    public void follow(View view) {
        curr_map =  data.get(Integer.parseInt(view.getTag().toString()));
        MyProgressBarDialogTools.show(context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject object = new JSONObject();
                    object.put("uid", uid);
                    object.put("auid", curr_map.get("Hisuid").toString());
                    object.put("act", URLConfig.attention);
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.CCMTVAPP, object.toString());
                    Message message = new Message();
                    message.what = 5;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        }).start();
    }


}