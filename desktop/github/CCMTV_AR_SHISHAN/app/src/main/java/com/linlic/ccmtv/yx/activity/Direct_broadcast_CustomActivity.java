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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.db.MyDbUtils;
import com.linlic.ccmtv.yx.activity.direct_broadcast.Live_broadcast_introduction;
import com.linlic.ccmtv.yx.activity.entity.Direct_broadcast;
import com.linlic.ccmtv.yx.activity.entity.Live_broadcast;
import com.linlic.ccmtv.yx.activity.home.VideoFive;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.XUtilsImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * name:搜索
 * author:Tom
 * 2016-3-2下午7:07:12
 */
public class Direct_broadcast_CustomActivity extends BaseActivity {
    static Direct_broadcast_CustomActivity isFilsh;
    private ListView department_list;// 数据加载
    private List<Direct_broadcast> data = new ArrayList<>();
    BaseListAdapter baseListAdapter;
    private Context context = null;
    //用户统计
    private String type;
    private String keyword = "";
    private int page = 1;
    private boolean isNoMore = false;
    private RelativeLayout hearderViewLayout;
    private EditText editText1;
    private Map<String, Object> keshiMap = new HashMap<>();
    /*====================筛选条件====================*/

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
//                        LogUtil.e("搜索数据", msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject customJson = dataArray.getJSONObject(i);
                                Direct_broadcast direct_broadcast = new Direct_broadcast();
                                direct_broadcast.setDirect_broadcast_item_type(customJson.getString("type"));
                                direct_broadcast.setDirect_broadcast_item_title(customJson.getString("livename"));
                                if (true) {
                                    direct_broadcast.setDirect_broadcast_item_icon("1");
                                } else {
                                    direct_broadcast.setDirect_broadcast_item_icon("2");
                                }

                                direct_broadcast.setDirect_broadcast_item_add("" + (i + 1));
                                direct_broadcast.setDirect_broadcast_item_banner(customJson.getString("banner"));
                                direct_broadcast.setPosition(i);
                                direct_broadcast.setAbout(customJson.getString("about"));
                                direct_broadcast.setAddress(customJson.getString("address"));
                                direct_broadcast.setEndtime(customJson.getString("endtime"));
                                direct_broadcast.setStarttime(customJson.getString("starttime"));

                                JSONArray zis = customJson.getJSONArray("zi");
                                List<Live_broadcast> live_broadcasts = new ArrayList<>();
                                for (int j = 0; j < zis.length(); j++) {
                                    JSONObject Live_broadcast_json = zis.getJSONObject(j);
                                    Live_broadcast live_broadcast = new Live_broadcast();
                                    live_broadcast.setHid(Live_broadcast_json.getString("hid"));
                                    live_broadcast.setReviewurl(Live_broadcast_json.getString("reviewurl"));
                                    live_broadcast.setSurl(Live_broadcast_json.getString("surl"));
                                    live_broadcast.setTime(Live_broadcast_json.getString("dendtime"));
                                    live_broadcast.setTitle(Live_broadcast_json.getString("name"));
                                    live_broadcast.setTurl(Live_broadcast_json.getString("turl"));
                                    live_broadcast.setTid(Live_broadcast_json.getInt("tid"));
                                    live_broadcast.setDename(Live_broadcast_json.getString("dename"));
                                    live_broadcast.setBotten_text(Live_broadcast_json.getString("note"));

                                    live_broadcasts.add(live_broadcast);
                                }
                                direct_broadcast.setLive_broadcasts(live_broadcasts);
                                data.add(direct_broadcast);
                            }
                        } else {
                            isNoMore = true;
                            Toast.makeText(Direct_broadcast_CustomActivity.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
//                        MyProgressBarDialogTools.hide();
                        baseListAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.custom_main2);
        context = this;
        isFilsh = this;
        type = getIntent().getStringExtra("type");
        try {
            if (getIntent().getExtras().getString("mode").equals("1")) {
                keyword = getIntent().getExtras().getString("custom_title");
            }
            if (getIntent().getExtras().getString("mode").equals("2")) {
                keyword = getIntent().getExtras().getString("video_class");
            }
            if (getIntent().getExtras().getString("mode").equals("3")) {
                keyword = getIntent().getExtras().getString("disease_class");
            }
            if (getIntent().getExtras().getString("mode").equals("4")) {
                keyword = getIntent().getExtras().getString("disease_class");
            }
            if (getIntent().getExtras().getString("mode").equals("5")) {
                keyword = getIntent().getExtras().getString("video_class");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        findId();
        setTexts();
        setmsgdb();
        onClick();
    }

    public void findId() {
        department_list = (ListView) findViewById(R.id.department_list);
        editText1 = (EditText) findViewById(R.id.editText1);

        hearderViewLayout = (RelativeLayout) View.inflate(this, R.layout.custom_text, null);
        department_list.addHeaderView(hearderViewLayout);

      /*
        categoryView = (CategoryView) findViewById(R.id.category);
        custom_result = (LinearLayout) findViewById(R.id.custom_result);
        newest_result = (TextView) findViewById(R.id.newest_result);
        types_result = (TextView) findViewById(R.id.types_result);
        categoryView2 = (CategoryView) findViewById(R.id.category2);
        years_result = (TextView) findViewById(R.id.years_result);
        departments_result = (TextView) findViewById(R.id.departments_result);
        member_result = (TextView) findViewById(R.id.member_result);
        categoryLayout = (LinearLayout) findViewById(R.id.categoryLayout);
        categoryImg = (ImageView) findViewById(R.id.categoryImg);*/

    }

    public void onClick() {
        editText1.setOnKeyListener(new View.OnKeyListener() {

            @Override

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    //以下方法防止两次发送请求
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(Direct_broadcast_CustomActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    //存储数据
                    saveHot_search_grid();
                    setmsgdb();
                }
                return false;
            }
        });


    }

    public void saveHot_search_grid() {
        if (editText1.getText().toString().trim().length() > 0) {
            MyDbUtils.saveHot_search_grid(context, editText1.getText().toString().trim());
        }
        keyword = editText1.getText().toString().trim();
    }

    public void setTexts() {

        editText1.setText(keyword);

        baseListAdapter = new BaseListAdapter(department_list, data, R.layout.direct_broadcast_list_item) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                //先判断该类型是 1、会议还是 2、专题
                Direct_broadcast direct_broadcast = (Direct_broadcast) item;
                switch (direct_broadcast.getDirect_broadcast_item_type()) {
                    case "1":
                        //会议
                        helper.setText(R.id.direct_broadcast_item_type, "会议");
                        helper.setBackground_Image(R.id.direct_broadcast_item_type, R.mipmap.direct_broadcast_icon02);
                        break;
                    case "2":
                        //专题
                        helper.setText(R.id.direct_broadcast_item_type, "专题");
                        helper.setBackground_Image(R.id.direct_broadcast_item_type, R.mipmap.direct_broadcast_icon03);
                        break;
                    default:
                        //会议
                        helper.setText(R.id.direct_broadcast_item_type, "会议");
                        helper.setBackground_Image(R.id.direct_broadcast_item_type, R.mipmap.direct_broadcast_icon02);
                        break;
                }
                //设置title
                helper.setText(R.id.direct_broadcast_item_title, direct_broadcast.getDirect_broadcast_item_title());
                //增加内容
                helper.setDirect_broadcastAdapter(R.id.direct_broadcast_item_myrecyclerview, direct_broadcast.getLive_broadcasts());
                //设置ICON状态
                if (direct_broadcast.getDirect_broadcast_item_icon().equals("1")) {
                    helper.setBackground_Image(R.id.direct_broadcast_item_icon, R.mipmap.direct_broadcast_icon07);
                    helper.setTag(R.id.direct_broadcast_item_icon, "1");
                    helper.setVisibility(R.id.direct_broadcast_item_myrecyclerview, View.VISIBLE);
                } else {
                    helper.setBackground_Image(R.id.direct_broadcast_item_icon, R.mipmap.direct_broadcast_icon06);
                    helper.setTag(R.id.direct_broadcast_item_icon, "2");
                    helper.setVisibility(R.id.direct_broadcast_item_myrecyclerview, View.GONE);
                }
                helper.setDirect_broadcastOnClick(R.id.direct_broadcast_item_icon, R.id.direct_broadcast_item_myrecyclerview, baseListAdapter, direct_broadcast);
                //设置banner 图
                helper.setImageBitmapGlide(context, R.id.direct_broadcast_item_banner, direct_broadcast.getDirect_broadcast_item_banner());
                //设置位置
                helper.setText(R.id.position, direct_broadcast.getPosition() + "");
            }
        };
        department_list.setAdapter(baseListAdapter);
        // listview点击事件
        department_list.setOnItemClickListener(new casesharing_listListener());
      /*  baseListAdapter.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > 0) {
                }
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = department_list.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        isNoMore = false;
                    }
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = department_list.getChildAt(department_list.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == department_list.getHeight()) {
                        if (!isNoMore) {
                            page += 1;
                            setmsgdb();
                        }
                    }
                }
            }
        });*/
    }

    /**
     * name:设置listview中的值 author:Tom 2016-1-28下午3:42:47
     */
    public void setmsgdb() {
        if (page == 1) {
            data.removeAll(data);
            //   baseListAdapter.notifyDataSetChanged();
            MyProgressBarDialogTools.show(context);
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.selmeet);
                    obj.put("word", keyword.replaceAll("·", ""));
                    obj.put("uid", SharedPreferencesTools.getUid(context));
//                    obj.put("page", page);
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP_ccmtvapplive, obj.toString());
                    MyProgressBarDialogTools.hide();
//                    LogUtil.e("直播搜索页",result);
                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }


    public void getVideoRulest(final String aid) {
        final String uid = SharedPreferencesTools.getUidToLoginClose(context);
        if (uid == null || ("").equals(uid)) {
            return;
        }
        Intent intent = new Intent(context, VideoFive.class);
        intent.putExtra("aid", aid);
        startActivity(intent);

    }


    /**
     * name: 点击查看某个视频的详细 author:Tom 2016-1-28下午3:42:08
     */
    private class casesharing_listListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {
            TextView textView = (TextView) view
                    .findViewById(R.id.position);
            String id = textView.getText().toString();
            Intent intent = new Intent(context, Live_broadcast_introduction.class);
            intent.putExtra("direct_broadcast", data.get(Integer.parseInt(id)));
            startActivity(intent);

        }

    }

    /**
     * name:使用xutils 夹在图片 author:Tom 2016-1-7下午1:28:03
     *
     * @param img  图片控件
     * @param path 图片网络地址
     */
    public void loadImg(ImageView img, String path) {

        XUtilsImageLoader xUtilsImageLoader = new XUtilsImageLoader(Direct_broadcast_CustomActivity.this);
        xUtilsImageLoader.display(img, FirstLetter.getSpells(path));
    }

    @Override
    public void onResume() {
        MyProgressBarDialogTools.hide();
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (type.equals("home")) {
            enterUrl = "http://www.ccmtv.cn";
        } else {
            enterUrl = "http://www.ccmtv.cn/Member/Index.html";
        }
        super.onPause();
    }

}