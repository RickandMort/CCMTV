package com.linlic.ccmtv.yx.activity.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.db.MyDbUtils;
import com.linlic.ccmtv.yx.activity.entity.User_search_grid;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyGridView;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * name：关注---用户搜索
 * author：Larry
 * data：2016/9/12.
 */
public class UserSearchActivity extends BaseActivity implements TextWatcher, View.OnClickListener {
    int page = 1;
    private EditText edit_search;
    private TextView tv_search,tv_show;
    private MyGridView user_search_grid;
    private LinearLayout hean_Search_History_layout;
    private LinearLayout three_hean_clearance;
    private List<Map<String, Object>> data_list = new ArrayList<Map<String, Object>>();
    private SimpleAdapter sim_adapter;
    Context context;
    private ListView search_list;
    BaseListAdapter baseListAdapter;
    private boolean isNoMore = false;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                   /* if (page == 1) {
                        MyProgressBarDialogTools.hide();
                    }*/
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功

                            if (page==1){
                                //为消除连续点击查找按钮，出现数据叠加问题，
                                data.clear();
                            }
                            JSONArray dataArray = result
                                    .getJSONArray("data");
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
//                                    Log.i("result", "12346" + object.toString());
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("uid", object.getString("uid"));
                                    map.put("name", object.getString("name"));
                                    map.put("icon", object.getString("icon"));
                                    map.put("username", object.getString("username"));
                                    map.put("keshi", object.getString("keshi"));
                                    map.put("attentionflg", object.getString("attentionflg"));
                                    data.add(map);
                                }
                                baseListAdapter.notifyDataSetChanged();
                            }
                        } else {//失败
                            if (page > 1) {
                                Toast.makeText(context, "暂无更多数据", Toast.LENGTH_SHORT).show();
                            } else {
                                showNoData();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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
        setContentView(R.layout.activity_usersearch);
        context = this;
        findId();
        onClick();
        setText();
        setTextList();
        setmsgdb();
    }

    public void findId() {
        super.findId();
//        btnNoData = (Button) findViewById(R.id.btn_nodata);
        btn_nodata.setVisibility(View.GONE);
        edit_search = (EditText) findViewById(R.id.edit_search);
        tv_search = (TextView) findViewById(R.id.tv_search);
        tv_show = (TextView) findViewById(R.id.tv_show);
        search_list = (ListView) findViewById(R.id.search_list);
        hean_Search_History_layout = (LinearLayout) findViewById(R.id.hean_Search_History_layout);
        three_hean_clearance = (LinearLayout) findViewById(R.id.three_hean_clearance);
        user_search_grid = (MyGridView) findViewById(R.id.user_search_grid);
    }

    public void onClick() {
        edit_search.addTextChangedListener(this);
        edit_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // do something
                    if (edit_search.getText().toString().length() > 0) {
                        page = 1;
                        data.clear();
                        baseListAdapter.notifyDataSetChanged();
                        initData();
                        //存储数据
                        MyDbUtils.saveUser_search_grid(UserSearchActivity.this, edit_search.getText().toString());
                    }
                    return true;
                }
                return false;
            }
        });
        tv_search.setOnClickListener(this);
        // listview点击事件
        search_list.setOnItemClickListener(new mymessage_listListener());

        /*edit_search.setOnKeyListener(new View.OnKeyListener() {

            @Override

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_SEARCH && event.getAction() == KeyEvent.ACTION_DOWN) {
                    //以下方法防止两次发送请求

                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(UserSearchActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    //存储数据
                    if (edit_search.getText().toString().trim().length() > 0) {
                        //存储数据
                        MyDbUtils.saveUser_search_grid(UserSearchActivity.this, edit_search.getText().toString());
                    } else {
                    }
                }
                return false;
            }
        });*/

        three_hean_clearance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDbUtils.deleteUser_search_gridALL(context);
                data_list.removeAll(data_list);
                if(sim_adapter!=null){
                    sim_adapter.notifyDataSetChanged();
                }
                tv_show.setVisibility(View.VISIBLE);
            }
        });

        user_search_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edit_search.setText(data_list.get(position).get("text").toString());
            }
        });
    }

    public void setTextList() {
        List<User_search_grid> list = MyDbUtils.findUser_search_grid_All(UserSearchActivity.this);
        if (list != null && list.size() > 0) {
            tv_show.setVisibility(View.GONE);
            for (int i = 0; i < (list.size() > 10 ? 10 : list.size()); i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("text", list.get(i).getUser_search_name());
                data_list.add(map);
            }
        }else {
            tv_show.setVisibility(View.VISIBLE);
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
            user_search_grid.setAdapter(sim_adapter);
        } else {
            //hean_Search_History_layout.setVisibility(View.GONE);
        }
    }

    /**
     * name: 点击我的关注进入详情页
     */
    private class mymessage_listListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {
            // 循环json（找到点击的文章详细）

            Intent intent = new Intent(context, MyFollowDetails.class);
            intent.putExtra("Hisuid", data.get(arg2).get("uid").toString());
            intent.putExtra("Str_username", data.get(arg2).get("name").toString());
            // intent.putExtra("isMyColleague", data.get(arg2).get("isMyColleague").toString());
            // Log.i("isMyColleague", "isMyColleague     " + data.get(arg2).get("isMyColleague").toString());
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        //if (edit_search.getText().toString().trim().length() > 0){
            page = 1;
            data.clear();
            baseListAdapter.notifyDataSetChanged();
            initData();
        //}
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn/Member/Index.html";
        super.onPause();
    }

    private void setText() {
        baseListAdapter = new BaseListAdapter(search_list, data, R.layout.follow_item) {

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
                helper.setImageBitmapGlide(context, R.id.iv_headImg, ((Map) item).get("icon") + "");
                if ("请选择科室".equals(((Map) item).get("keshi")) || "".equals(((Map) item).get("keshi"))) {
                    helper.setText(R.id.tv_department, "未知科室");
                } else {
                    helper.setText(R.id.tv_department, ((Map) item).get("keshi") + "");
                }
                if (((Map) item).get("attentionflg").equals("1")){
                    helper.setBackground_Image(R.id.id_tv_follow_item_focus,R.mipmap.focus_bg_yet);
                    helper.setText(R.id.id_tv_follow_item_focus,"已关注");
                    helper.setTextColor(R.id.id_tv_follow_item_focus, R.color.black);
                }else {
                    helper.setBackground_Image(R.id.id_tv_follow_item_focus,R.drawable.anniu17);
                    helper.setText(R.id.id_tv_follow_item_focus,"关注");
                    helper.setTextColor(R.id.id_tv_follow_item_focus, R.color.white);
                }
                helper.setUserSearchFocusOnClick(((Map) item).get("uid") + "",R.id.id_tv_follow_item_focus,item);
            }
        };

        baseListAdapter.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = search_list.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        isNoMore = false;
                    }
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = search_list.getChildAt(search_list.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == search_list.getHeight()) {
                        if (!isNoMore) {
                            page += 1;
                            initData();
                        }

                    }
                }
            }
        });
        search_list.setAdapter(baseListAdapter);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        page = 1;
        data.clear();
        baseListAdapter.notifyDataSetChanged();
        if (s.length() > 0) {
            tv_search.setBackground(getResources().getDrawable(R.drawable.tvbg_searchs));
        } else {
            tv_search.setBackground(getResources().getDrawable(R.drawable.tvbg_search));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (edit_search.getText().toString().length() > 0) {
            page = 1;
            data.clear();
            baseListAdapter.notifyDataSetChanged();
            initData();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_search:

                if (edit_search.getText().toString().length() > 0) {
                    page = 1;
                    data.clear();
                    MyDbUtils.saveUser_search_grid(UserSearchActivity.this, edit_search.getText().toString());
                    initData();
                } else {
                    Toast.makeText(context, "请输入用户名", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * name:导入初始值
     */
    public void initData() {
       /* if (page == 1) {
            MyProgressBarDialogTools.show(context);
        }*/

        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject object = new JSONObject();
                try {
                    object.put("page", page);
                    object.put("uid", SharedPreferencesTools.getUid(context));
                    object.put("username", edit_search.getText().toString());
                    object.put("act", URLConfig.searchUser);
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
}
