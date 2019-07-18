package com.linlic.ccmtv.yx.activity.my;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.vip.adapter.OnRecyclerviewItemClickListener;
import com.linlic.ccmtv.yx.adapter.FollowActivityViewAdapter;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
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
 * 我的关注
 *
 * @author yu
 */
public class MyFollowActivity extends BaseActivity {
    ListView follow_list;
    BaseListAdapter baseListAdapter;
    private boolean isNoMore = false;
    private String uid;
    Context context;
    LinearLayout layou_search;//搜索
    int page = 1;
    String Str_username;  //传过去作为发件人用户名
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private Map<String, Object> data_map = new HashMap();
    private List<Map<String, Object>> data2 = new ArrayList<Map<String, Object>>();
    LinearLayout layout_followtop, is_follow_layout;
    RecyclerView recyclerview;
    private Map<String, Object> curr_map = null;
    FollowActivityViewAdapter followActivityViewAdapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
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
                                } else {
                                    //暂无更多数据
                                    isNoMore = true;
                                    Toast.makeText(context, "暂无更多数据", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                hideNoData();
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject object = dataArray.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("uid", object.getString("uid"));
                                    map.put("name", object.getString("name"));
                                    map.put("icon", object.getString("icon"));
                                    map.put("keshi", object.getString("keshi"));
                                    map.put("username", object.getString("username"));
                                    map.put("isMyColleague", object.getString("isMyColleague"));
//                                    map.put("position",data.size());
                                    data_map.put(object.getString("uid"), map);
                                    data.add(map);
                                }
                                baseListAdapter.notifyDataSetChanged();
                            }
                        } else {//失败
                            showNoData();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                  /*  MyProgressBarDialogTools.hide(context, R.id.layout_loading);
                    hideLoading();*/
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功
                            data2.clear();
                            JSONArray dataArray = result
                                    .getJSONArray("data");
                            if (dataArray.length() == 0) {
                                //暂无更多数据
                                is_follow_layout.setVisibility(View.GONE);
                            } else {
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject object = dataArray.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("uid", object.getString("uid"));
                                    map.put("keshi", object.getString("keshi"));
                                    map.put("icon", object.getString("icon"));
                                    map.put("username", object.getString("username"));
                                    data2.add(map);
                                }

                                initRecyclerView();
                            }
                        } else {//失败
//                            showNoData();
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
                            } else {
                                data.remove(curr_map);
                                baseListAdapter.notifyDataSetChanged();
                            }
                        } else {// 失败
                            Toast.makeText(context,
                                    result.getString("errorMessage"),
                                    Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
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
    public void showNoData() {
        super.showNoData();
        layout_followtop.setVisibility(View.GONE);
    }

    @Override
    public void hideNoData() {
        super.hideNoData();
        layout_followtop.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_myfollow);
        context = this;
        findId();
        setText();
        onclick();
        Str_username = getIntent().getStringExtra("Str_username");

        initdata2();
    }

    public void findId() {
        super.findId();

        follow_list = (ListView) findViewById(R.id.follow_list);
        View headView = getLayoutInflater().inflate(R.layout.activity_myfollow_top, null);
        is_follow_layout = (LinearLayout) headView.findViewById(R.id.is_follow_layout);
        layout_followtop = (LinearLayout) headView.findViewById(R.id.layout_followtop);
        recyclerview = (RecyclerView) headView.findViewById(R.id.recyclerview);
        follow_list.addHeaderView(headView);
        layou_search = (LinearLayout) findViewById(R.id.layou_search);

        // listview点击事件
        follow_list.setOnItemClickListener(new mymessage_listListener());
        layout_followtop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, RecommendActivity.class));
            }
        });
    }

    public void initRecyclerView() {
        /*home_horizontalListviewAdapter =new  Home_horizontalListviewAdapter(getActivity(),icons);
        horizontalListView.setAdapter(home_horizontalListviewAdapter);*/

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerview.setLayoutManager(linearLayoutManager);
        followActivityViewAdapter = new FollowActivityViewAdapter(context, data2, onRecyclerviewItemClickListener);
        recyclerview.setAdapter(followActivityViewAdapter);
    }

    private OnRecyclerviewItemClickListener onRecyclerviewItemClickListener = new OnRecyclerviewItemClickListener() {
        @Override
        public void onItemClickListener(View v, int position) {
            TextView home_horizontallistview_item_title = (TextView) v.findViewById(R.id.iv_id);
            Intent intent = new Intent(MyFollowActivity.this, MyFollowDetails.class);
            intent.putExtra("Hisuid", data2.get(position).get("uid").toString());
            intent.putExtra("Str_username", data2.get(position).get("username").toString());
            intent.putExtra("isMyColleague", "0");
//            Log.i("isMyColleague", "isMyColleague     " + data.get(arg2).get("isMyColleague").toString());
            startActivity(intent);
        }
    };


    public void setText() {
        super.setClick();
        super.setActivity_title_name(R.string.my_myfollow);
        super.setActivity_btnnodata(R.string.btnnodata_tofollow);
        super.setActivity_tvnodata(R.string.tvnodata_tofollow);
        super.btn_nodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyFollowActivity.this, RecommendActivity.class));
            }
        });

        baseListAdapter = new BaseListAdapter(follow_list, data, R.layout.workmate_item) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                helper.setText(R.id.tv_uid, ((Map) item).get("uid") + "");
                helper.setText(R.id.follow_item_title, ((Map) item).get("username") + "");
                helper.setText(R.id.tv_hospital, ((Map) item).get("name") + "");
                if ("请选择科室".equals(((Map) item).get("keshi")) || "".equals(((Map) item).get("keshi"))) {
                    helper.setText(R.id.tv_department, "未知科室");
                } else {
                    helper.setText(R.id.tv_department, ((Map) item).get("keshi") + "");
                }
                //   helper.setImageBitmapGlide(context, R.id.iv_headImg, ((Map) item).get("icon") + "");
                helper.setImageBitmaps(R.id.iv_headImg, ((Map) item).get("icon") + "");
                helper.setTag(R.id.is_attention, ((Map) item).get("uid") + "");
                helper.setTextColor2(R.id.is_attention, Color.parseColor("#e3e3e3"));
                helper.setText(R.id.is_attention, "√ 已关注");
                helper.setBackground_Image(R.id.is_attention, R.drawable.anniu20);

            }
        };

        baseListAdapter.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = follow_list.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        isNoMore = false;
                    }
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = follow_list.getChildAt(follow_list.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == follow_list.getHeight()) {
                        if (!isNoMore) {
                            page += 1;
                            initData();
                        }

                    }
                }
            }
        });
        follow_list.setAdapter(baseListAdapter);
    }

    public void onclick() {
        // TODO Auto-generated method stub
        layou_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyFollowActivity.this, UserSearchActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        page = 1;
        data.removeAll(data);
        initData();
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn/Member/Index.html";
        super.onPause();
    }

    /**
     * name:导入初始值
     */
    public void initData() {
        if (page == 1) {
            MyProgressBarDialogTools.show(context);
        }
        //判断是否登录
        uid = SharedPreferencesTools.getUid(context);

        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject object = new JSONObject();
                try {
                    object.put("uid", uid);
                    object.put("page", page);
                    object.put("act", URLConfig.myAttention);
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, object.toString());
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

    public void initdata2() {
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
                    message.what = 2;
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
     * name: 点击我的关注进入详情页
     */
    private class mymessage_listListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
            // 循环json（找到点击的文章详细）
            Intent intent = new Intent(MyFollowActivity.this, MyFollowDetails.class);
            intent.putExtra("Hisuid", data.get(arg2 - 1).get("uid").toString());
            intent.putExtra("Str_username", Str_username);
            intent.putExtra("isMyColleague", data.get(arg2 - 1).get("isMyColleague").toString());
//            Log.i("isMyColleague", "isMyColleague     " + data.get(arg2).get("isMyColleague").toString());
            startActivity(intent);
        }
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

    /**
     * 关注、、、取消关注
     */
    public void follow(View view) {
        curr_map = (Map<String, Object>) data_map.get(view.getTag().toString());
        MyProgressBarDialogTools.show(context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject object = new JSONObject();
                    object.put("uid", uid);
                    object.put("auid", curr_map.get("uid").toString());
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
