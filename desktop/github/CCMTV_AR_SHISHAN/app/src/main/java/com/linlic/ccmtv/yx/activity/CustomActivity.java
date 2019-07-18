package com.linlic.ccmtv.yx.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.base.LocalApplication;
import com.linlic.ccmtv.yx.activity.db.MyDbUtils;
import com.linlic.ccmtv.yx.activity.home.VideoFive;
import com.linlic.ccmtv.yx.activity.login.LoginActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.CategoryView;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.XUtilsImageLoader;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

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
public class CustomActivity extends BaseActivity {
    static CustomActivity isFilsh;
    private ListView department_list;// 数据加载
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    BaseListAdapter baseListAdapter;
    private Context context = null;
    //用户统计
    private String type;
    private String custom_title;
    private String video_class = "";
    private String disease_class = "";
    private String keywords = "";
    private String keyword = "";
    private String posttime = "";
    private LinearLayout custom_result;
    private String departmentsSelect = "";
    private int page = 1;
    private boolean isNoMore = false;
    private RelativeLayout hearderViewLayout;
    private CategoryView categoryView;
    private CategoryView categoryView2;
    private static long lastClickTime;
    /*====================筛选条件====================*/
    private String newestKey = "最新";
    private List<String> newestList = null;//最新
    private String typesKey = "类型";
    private List<String> typesList = null;//类型
    private String yearsKey = "年份";
    private List<String> yearsList = null;//年份
    private String departmentsKey = "科室";
    private List<String> departmentsList = null;//科室
    private String memberKey = "会员";
    private List<String> memberList = null;//是否是会员
    private TextView newest_result;
    private TextView types_result;
    private TextView years_result;
    private TextView departments_result;
    private String departments_name = "";
    private EditText editText1;
    private TextView member_result;
    private LinearLayout categoryLayout;
    private LinearLayout customData;
    private NodataEmptyLayout customNodata;
    private ImageView categoryImg;
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
                        boolean status = jsonObject.getInt("status") == 1;
                        if (status) { // 成功
                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject customJson = dataArray.getJSONObject(i);
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("departemnt_item_title", customJson.getString("title"));
                                map.put("department_id", customJson.getString("aid"));
                                map.put("department_on_demand", "播放数：" + customJson.getString("hits"));
                                map.put("department_times", customJson.getString("posttime"));
                                map.put("departemnt_item_img", customJson.getString("picurl"));
                                map.put("money", customJson.getString("money"));
                                map.put("flag", customJson.getString("flag"));
                                map.put("videopaymoney", customJson.getString("videopaymoney"));
                                data.add(map);
                            }
                        } else {
                            isNoMore = true;
                            Toast.makeText(CustomActivity.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        setRelustStatus(data.size() > 0, jsonObject.getInt("status"));
//                        MyProgressBarDialogTools.hide();
                        baseListAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        boolean status = jsonObject.getInt("status") == 1;
                        setRelustStatus(status);
                        if (status) { // 成功
                            JSONObject dataArray = jsonObject.getJSONObject("data");
                            newestList = new ArrayList<String>();//最新
                            typesList = new ArrayList<String>();//类型
                            yearsList = new ArrayList<String>();//年份
                            departmentsList = new ArrayList<String>();//科室
                            memberList = new ArrayList<String>();//是否是会员
                            newestList.clear();
                            typesList.clear();
                            yearsList.clear();
                            departmentsList.clear();
                            memberList.clear();
                            newestList.clear();
                            categoryView.removeAllViews();
                            categoryView2.removeAllViews();
                            try {
                                if (getIntent().getExtras().getString("mode").equals("1")) {
                                    editText1.setText(custom_title);
                                }
                                if (getIntent().getExtras().getString("mode").equals("2")) {
                                    editText1.setText(custom_title);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            JSONArray newHotArr = dataArray.getJSONArray("newHotArr");
                            for (int i = 0; i < newHotArr.length(); i++) {
                                newestList.add(newHotArr.getString(i));
                            }
                            categoryView.add(newestList, newestKey, video_class);

                            for (String str : newestList) {
                                if (str.equals(video_class)) {
                                    newest_result.setText(video_class);
                                }
                            }

                            JSONArray videoClassArr = dataArray.getJSONArray("videoClassArr");
                            for (int i = 0; i < videoClassArr.length(); i++) {
                                typesList.add(videoClassArr.getString(i));
                            }

                            categoryView.add(typesList, typesKey, video_class);
                            switch (video_class) {
                                case "为我推荐":
                                    newest_result.setText("最新");
                                    break;
                                case "最新视频":
                                    newest_result.setText("最新");
                                    break;
                                case "手术演示":
                                    types_result.setText("·手术");
                                    video_class = "手术";
                                    break;
                                case "病例讨论":
                                    types_result.setText("·病例");
                                    video_class = "病例";
                                    break;
                                case "超级访问":
                                    types_result.setText("·座谈");
                                    video_class = "座谈";
                                    break;
                                case "百家讲坛":
                                    types_result.setText("·讲座");
                                    video_class = "讲座";
                                    break;
                                case "名家视角":
                                    types_result.setText("·采访");
                                    video_class = "采访";
                                    break;
                            }

                            JSONArray timeArr = dataArray.getJSONArray("timeArr");
                            for (int i = 0; i < timeArr.length(); i++) {
                                yearsList.add(timeArr.getString(i));
                            }

                            categoryView.add(yearsList, yearsKey, "");

                            JSONArray keshiArr = dataArray.getJSONArray("keshiArr");
                            for (int i = 0; i < keshiArr.length(); i++) {
                                JSONObject keshi = keshiArr.getJSONObject(i);
                                keshiMap.put(keshi.getString("name"), keshi.getString("id"));
                                departmentsList.add(keshi.getString("name"));
                            }

                            categoryView2.add(departmentsList, departmentsKey, disease_class);
                            if (keshiMap.containsKey(disease_class)) {
                                departments_result.setText(keshiMap.get(disease_class).toString());
                                departments_name = disease_class;
                            }

                            JSONArray hyArr = dataArray.getJSONArray("hyArr");
                            for (int i = 0; i < hyArr.length(); i++) {
                                memberList.add(hyArr.getString(i));
                            }

                            categoryView.add(memberList, memberKey, "");
                            if (video_class.equals("为我推荐")) {
                                video_class = "";
                            }
                            if (video_class.equals("最新视频")) {
                                video_class = "";
                            }
                        } else {
                            Toast.makeText(CustomActivity.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
//                        baseListAdapter.notifyDataSetChanged();
                        wDelayed();
                        setTexts();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 201:
                    LinearLayout rrr = (LinearLayout) categoryView.getChildAt(3);
                    HorizontalScrollView horizontalScrollView = (HorizontalScrollView) rrr.getChildAt(0);
                    RadioGroup groupr = (RadioGroup) horizontalScrollView.getChildAt(0);
                    RadioButton radioButton = (RadioButton) findViewById(groupr.getCheckedRadioButtonId());
                    horizontalScrollView.fullScroll(ScrollView.FOCUS_RIGHT);
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    setRelustStatus(data.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOST);
                    MyProgressBarDialogTools.hide();
                    break;
                default:
                    break;
            }
        }
    };


    private void setRelustStatus(boolean status, int code) {
        if (status) {
            customData.setVisibility(View.VISIBLE);
            customNodata.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                customNodata.setNetErrorIcon();
            } else {
                customNodata.setLastEmptyIcon();
            }
            customData.setVisibility(View.GONE);
            customNodata.setVisibility(View.VISIBLE);
        }
    }

    private void setRelustStatus(boolean sucess) {
        if (sucess) {
            customData.setVisibility(View.VISIBLE);
            customNodata.setVisibility(View.GONE);
        } else {
            if (page != 1) return;
            customData.setVisibility(View.GONE);
            customNodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_main);
        context = this;
        isFilsh = this;
        type = getIntent().getStringExtra("type");
        try {
            if (getIntent().getExtras().getString("mode").equals("1")) {
                custom_title = getIntent().getExtras().getString("custom_title");
                keyword = getIntent().getExtras().getString("custom_title");
            }
            if (getIntent().getExtras().getString("mode").equals("2")) {
                video_class = getIntent().getExtras().getString("video_class");
                custom_title = getIntent().getExtras().getString("video_class");
            }
            if (getIntent().getExtras().getString("mode").equals("3")) {
                disease_class = getIntent().getExtras().getString("disease_class");
                custom_title = getIntent().getExtras().getString("disease_class");
            }
            if (getIntent().getExtras().getString("mode").equals("4")) {
                disease_class = getIntent().getExtras().getString("disease_class");
                custom_title = getIntent().getExtras().getString("disease_class");
            }
            if (getIntent().getExtras().getString("mode").equals("5")) {
                video_class = getIntent().getExtras().getString("video_class");
            }
            if (getIntent().getExtras().getString("mode").equals("6")) {
                video_class = getIntent().getExtras().getString("video_class");
                disease_class = getIntent().getExtras().getString("disease_class");
            }
            if (getIntent().getExtras().containsKey(departmentsSelect)) {
                departmentsSelect = getIntent().getExtras().getString("getIntent().getExtras().");
            }
            switch (video_class) {
                case "手术演示":
                    video_class = "手术";
                    break;
                case "病例讨论":
                    video_class = "病例";
                    break;
                case "超级访问":
                    video_class = "座谈";
                    break;
                case "百家讲坛":
                    video_class = "讲座";
                    break;
                case "名家视角":
                    video_class = "采访";
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        findId();
        newvideoSearch();

//        setmsgdb();
        onClick();
    }

    public void wDelayed() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    // 水平直接滚动800px，如果想效果更平滑可以使用smoothScrollTo(int x, int y)
                    LinearLayout linearLayout = (LinearLayout) categoryView2.getChildAt(0);
                    HorizontalScrollView horizontalScrollView = (HorizontalScrollView) linearLayout.getChildAt(0);
                    RadioGroup groupr = (RadioGroup) horizontalScrollView.getChildAt(0);
                    RadioButton radioButton = (RadioButton) findViewById(groupr.getCheckedRadioButtonId());
                    horizontalScrollView.requestChildFocus(groupr, radioButton);
                    LinearLayout linearLayout1 = (LinearLayout) categoryView.getChildAt(1);
                    HorizontalScrollView horizontalScrollView1 = (HorizontalScrollView) linearLayout1.getChildAt(0);
                    RadioGroup groupr1 = (RadioGroup) horizontalScrollView1.getChildAt(0);
                    RadioButton radioButton1 = (RadioButton) findViewById(groupr1.getCheckedRadioButtonId());
                    horizontalScrollView1.requestChildFocus(groupr1, radioButton1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1000);
    }

    @Override
    public void findId() {
        super.findId();
        department_list = (ListView) findViewById(R.id.department_list);
        hearderViewLayout = (RelativeLayout) View.inflate(this, R.layout.custom_text, null);
        department_list.addHeaderView(hearderViewLayout);
        categoryView = (CategoryView) findViewById(R.id.category);
        custom_result = (LinearLayout) findViewById(R.id.custom_result);
        newest_result = (TextView) findViewById(R.id.newest_result);
        types_result = (TextView) findViewById(R.id.types_result);
        categoryView2 = (CategoryView) findViewById(R.id.category2);
        years_result = (TextView) findViewById(R.id.years_result);
        departments_result = (TextView) findViewById(R.id.departments_result);
        member_result = (TextView) findViewById(R.id.member_result);
        categoryLayout = (LinearLayout) findViewById(R.id.categoryLayout);
        categoryImg = (ImageView) findViewById(R.id.categoryImg);
        editText1 = (EditText) findViewById(R.id.editText1);

        customData = (LinearLayout) findViewById(R.id.ll_custom_data);
        customNodata = (NodataEmptyLayout) findViewById(R.id.rl_custom_nodata1);
    }

    public void onClick() {
        editText1.setOnKeyListener(new View.OnKeyListener() {

            @Override

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    //以下方法防止两次发送请求
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(CustomActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    //存储数据
                    saveHot_search_grid();
                    setmsgdb();
                }
                return false;
            }
        });

        categoryView2.setOnClickCategoryListener(new CategoryView.OnClickCategoryListener() {
            //逻辑回掉
            @Override
            public void click(RadioGroup group, int checkedId) {
                RadioButton button = (RadioButton) group.findViewById(checkedId);
                if (button.getTag().equals("最新")) {
                    newest_result.setText(button.getText());
                } else if (button.getTag().equals("类型")) {
                    types_result.setText("·" + button.getText());
                } else if (button.getTag().equals("年份")) {
                    years_result.setText("·" + button.getText());
                } else if (button.getTag().equals("科室")) {
                    departments_result.setText(keshiMap.get(button.getText()).toString());
                    departments_name = button.getText().toString();
                } else if (button.getTag().equals("会员")) {
                    member_result.setText("·" + button.getText());
                }
                video_class = types_result.getText().toString();
                disease_class = departments_result.getText().toString();
                keywords = "";
                posttime = years_result.getText().toString();
                page = 1;
                setmsgdb();
            }
        });

        //设置自定义监听器
        categoryView.setOnClickCategoryListener(new CategoryView.OnClickCategoryListener() {
            //逻辑回掉
            @Override
            public void click(RadioGroup group, int checkedId) {
                RadioButton button = (RadioButton) group.findViewById(checkedId);
                if (button.getTag().equals("最新")) {
                    newest_result.setText(button.getText());
                } else if (button.getTag().equals("类型")) {
                    types_result.setText("·" + button.getText());
                } else if (button.getTag().equals("年份")) {
                    years_result.setText("·" + button.getText());
                } else if (button.getTag().equals("科室")) {
                    departments_result.setText(keshiMap.get(button.getText()).toString());
                    departments_name = button.getText().toString();
                } else if (button.getTag().equals("会员")) {
                    member_result.setText("·" + button.getText());

                }
                video_class = types_result.getText().toString();
                disease_class = departments_result.getText().toString();
                keywords = "";
                posttime = years_result.getText().toString();
                page = 1;
                setmsgdb();
            }
        });

        categoryLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag().toString().equals("展开")) {
                    categoryImg.setImageResource(R.mipmap.categoryimg1);
                    categoryView.setVisibility(View.GONE);
                    v.setTag("收缩");
                } else {
                    categoryImg.setImageResource(R.mipmap.categoryimg2);
                    categoryView.setVisibility(View.VISIBLE);
                    v.setTag("展开");
                }
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
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.xinvideoSearch);
                    obj.put("video_class", video_class.replaceAll("·", ""));
                    obj.put("keshiid", departments_result.getText().toString());
                    obj.put("keshiname", departments_name);
                    obj.put("keywords", keywords.replaceAll("·", ""));
                    obj.put("keyword", keyword.replaceAll("·", ""));
                    obj.put("posttime", posttime.replaceAll("·", ""));
                    obj.put("newest_result", newest_result.getText().toString().replaceAll("·", ""));
                    obj.put("member_result", member_result.getText().toString().replaceAll("·", ""));
                    obj.put("page", page);
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP_SEARCH, obj.toString());
                    MyProgressBarDialogTools.hide();
//                    Log.e("搜索数据-下行", result);

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

        baseListAdapter = new BaseListAdapter(department_list, data, R.layout.custom_item) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                helper.setText(R.id.departemnt_item_title, ((Map) item).get("departemnt_item_title") + "");
                helper.setText(R.id.department_id, ((Map) item).get("department_id") + "");
                helper.setText(R.id.department_on_demand, ((Map) item).get("department_on_demand") + "");
                helper.setText(R.id.department_times, ((Map) item).get("department_times") + "");
                // helper.setImageBitmap(R.id.departemnt_item_img, ((Map) item).get("departemnt_item_img") + "", ((Map) item).get("department_id") + "");
                helper.setImageBitmapGlide(context, R.id.departemnt_item_img, ((Map) item).get("departemnt_item_img") + "");
                //videopaymoney  不为0则为收费 为0 测试代表着是非收费视频
                if (!((Map) item).get("videopaymoney").equals("0")) {
                    //收费
                    helper.setImage(R.id.departemnt_item_top_img, R.mipmap.charge);
                    helper.setVisibility(R.id.departemnt_item_top_img, View.VISIBLE);
                } else {
                    helper.setVisibility(R.id.departemnt_item_top_img, View.GONE);
                    if (((Map) item).get("flag").toString().equals("3")) {
                        //VIP
                        helper.setImage(R.id.departemnt_item_top_img, R.mipmap.vip_img);
                        helper.setVisibility(R.id.departemnt_item_top_img, View.VISIBLE);
                    }
                }

            }
        };
        department_list.setAdapter(baseListAdapter);
        // listview点击事件
        department_list.setOnItemClickListener(new casesharing_listListener());
        baseListAdapter.addOnScrollListener(new AbsListView.OnScrollListener() {
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
        });
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
                    obj.put("act", URLConfig.xinvideoSearch);
                    obj.put("video_class", video_class.replaceAll("·", ""));
                    obj.put("keshiid", departments_result.getText().toString());
                    obj.put("keshiname", departments_name);
                    obj.put("keywords", keywords.replaceAll("·", ""));
                    obj.put("keyword", keyword.replaceAll("·", ""));
                    obj.put("posttime", posttime.replaceAll("·", ""));
                    obj.put("newest_result", newest_result.getText().toString().replaceAll("·", ""));
                    obj.put("member_result", member_result.getText().toString().replaceAll("·", ""));
                    obj.put("page", page);
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP_SEARCH, obj.toString());
                    MyProgressBarDialogTools.hide();

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

    public void newvideoSearch() {
        if (page == 1) {
            data.removeAll(data);
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.newvideoSearch);
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.CCMTVAPP_SEARCH, obj.toString());
//                    LogUtil.e("搜索页条件", result);
                    Message message = new Message();
                    message.what = 2;
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
                    .findViewById(R.id.department_id);
            String id = textView.getText().toString();
            // MyProgressBarDialogTools.show(context);
            if(SharedPreferencesTools.getUidONnull(context).equals("")){
                startActivity(new Intent(context, LoginActivity.class).putExtra("source", ""));
                LocalApplication.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LocalApplication.getAppContext(), "账户未登录，请先登录", Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                getVideoRulest(id);

            }


        }

    }

    /**
     * name:使用xutils 夹在图片 author:Tom 2016-1-7下午1:28:03
     *
     * @param img  图片控件
     * @param path 图片网络地址
     */
    public void loadImg(ImageView img, String path) {

        XUtilsImageLoader xUtilsImageLoader = new XUtilsImageLoader(CustomActivity.this);
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