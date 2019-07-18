package com.linlic.ccmtv.yx.activity.my.personal_profile;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.home.VideoFive;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.CustomImageView;
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
 * Created by Administrator on 2017/8/17.
 */
public class Personal_profile extends BaseActivity {

    private CustomImageView my_myhead;
    private TextView personal_profile_name, personal_profile_departments, personal_profile_traffic, personal_profile_attention,
            personal_profile_fan_club, personal_profile_colleague, personal_profile_blurb_text, personal_profile_blurb, personal_profile_video, iv_red_dians;
    private ImageView personal_profile_img1, personal_profile_img2, personal_profile_img3, Modify_the_circle;
    private View personal_profile_blurb_view, personal_profile_video_view;
    private LinearLayout personal_profile_blurb_layout, personal_profile_video_layout, personal_profile_blurb_null_layout;
    private ListView department_list;
    Context context;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    BaseListAdapter baseListAdapter;
    private int page = 1;
    private boolean isNoMore = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject jsonObject1 = new JSONObject(msg.obj + "");
                        if (jsonObject1.getInt("status") == 1) { // 成功
                            JSONObject jsonObject = jsonObject1
                                    .getJSONObject("data");
                            personal_profile_traffic.setText("访问量：" + jsonObject.getString("homepage"));
                            loadImg(my_myhead, jsonObject.getString("icon"));
                            personal_profile_name.setText(jsonObject.getString("homepage"));//姓名
                            personal_profile_departments.setText(jsonObject.getString("hosName") + " " + jsonObject.getString("keshilb"));//科室
                            if (jsonObject.getInt("ifnew") > 0) {
                                iv_red_dians.setVisibility(View.VISIBLE);
                            } else {
                                iv_red_dians.setVisibility(View.GONE);
                            }
                            personal_profile_attention.setText(jsonObject.getString("attnum").length() > 0 ? jsonObject.getString("attnum") : "0");
                            personal_profile_colleague.setText(jsonObject.getString("collnum").length() > 0 ? jsonObject.getString("collnum") : "0");
                            personal_profile_fan_club.setText(jsonObject.getString("fansnum").length() > 0 ? jsonObject.getString("fansnum") : "0");
                            if (jsonObject.getString("introduce").trim().length() > 0) {
                                //有介绍内容
                                personal_profile_blurb_text.setText(jsonObject.getString("introduce"));
                                personal_profile_blurb_text.setVisibility(View.VISIBLE);
                                personal_profile_blurb_null_layout.setVisibility(View.GONE);
                                Modify_the_circle.setVisibility(View.VISIBLE);
                            } else {
                                //没有介绍内容时
                                personal_profile_blurb_text.setVisibility(View.GONE);
                                personal_profile_blurb_null_layout.setVisibility(View.VISIBLE);
                                Modify_the_circle.setVisibility(View.GONE);

                            }
                            JSONArray dataArray2 = jsonObject
                                    .getJSONArray("uploaddata");
//                            System.out.println("进入到搜索解析页：" + dataArray);
                            for (int i = 0; i < dataArray2.length(); i++) {
                                JSONObject customJson = dataArray2.getJSONObject(i);
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("departemnt_item_title", customJson.getString("title"));
                                map.put("department_id", customJson.getString("aid"));
                                map.put("department_on_demand", "播放数：" + customJson.getString("hits"));
                                map.put("department_times", customJson.getString("list"));
                                map.put("departemnt_item_img", customJson.getString("picurl"));
                                map.put("money", customJson.getString("money"));
                                map.put("videopaymoney", customJson.getString("videopaymoney"));
                                map.put("flag", customJson.getString("flag"));
                                map.put("vtime", customJson.getString("vtime"));//视频时长
                                data.add(map);
                            }

                            baseListAdapter.notifyDataSetChanged();

                        } else {//失败

                            Toast.makeText(Personal_profile.this, jsonObject1.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }

                    break;
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            JSONArray dataArray = jsonObject
                                    .getJSONArray("data");
//                            System.out.println("进入到搜索解析页：" + dataArray);
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject customJson = dataArray.getJSONObject(i);
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("departemnt_item_title", customJson.getString("title"));
                                map.put("department_id", customJson.getString("aid"));
                                map.put("department_on_demand", "播放数：" + customJson.getString("hits"));
                                map.put("department_times", customJson.getString("list"));
                                map.put("departemnt_item_img", customJson.getString("picurl"));
                                map.put("money", customJson.getString("money"));
                                map.put("videopaymoney", customJson.getString("videopaymoney"));
                                map.put("flag", customJson.getString("flag"));
                                map.put("vtime", customJson.getString("vtime"));//视频时长
                                data.add(map);
                            }

                        } else {
                            isNoMore = true;
                            Toast.makeText(Personal_profile.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
//                        MyProgressBarDialogTools.hide();
                        baseListAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }

                case 500:
                    Toast.makeText(context
                            , R.string.post_hint1,
                            Toast.LENGTH_SHORT).show();
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.personal_profile);
        context = this;
        findById();
        onclick();
        initdata();
        setValue();
    }

    public void modify_the_circle(View view) {
        Intent intent = new Intent(Personal_profile.this, Modify_the_circle.class);
        startActivity(intent);
    }

    public void initdata() {
        baseListAdapter = new BaseListAdapter(department_list, data, R.layout.personal_profile_list_item) {

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
                helper.setText(R.id.department_times, "发布时间:" + ((Map) item).get("department_times") + "");
                // helper.setImageBitmap(R.id.departemnt_item_img, ((Map) item).get("departemnt_item_img") + "", ((Map) item).get("department_id") + "");
                helper.setImageBitmapGlide(context, R.id.departemnt_item_img, ((Map) item).get("departemnt_item_img") + "");
                //videopaymoney  不为0则为收费 为0 测试代表着是非收费视频
                if (!((Map) item).get("videopaymoney").equals("0")) {
                    //收费
                    helper.setImage(R.id.departemnt_item_top_img, R.mipmap.charge);
                    helper.setVisibility(R.id.departemnt_item_top_img, View.VISIBLE);
                } else {
                    helper.setVisibility(R.id.departemnt_item_top_img, View.GONE);
                    if (((Map) item).get("money").toString().equals("3")) {
                        //会员
                        helper.setImage(R.id.departemnt_item_top_img, R.mipmap.vip_img);
                        helper.setVisibility(R.id.departemnt_item_top_img, View.VISIBLE);
                    }
                }

            }
        };
        department_list.setAdapter(baseListAdapter);
        // listview点击事件
        department_list
                .setOnItemClickListener(new casesharing_listListener());
        baseListAdapter.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > 0) {

                }
//                System.out.println("当前条目位置："+firstVisibleItem + "   当前屏幕容纳条目数：" + visibleItemCount + "      总条目数:" + totalItemCount);
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = department_list.getChildAt(0);


                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
//                        Log.d("ListView", "<----滚动到顶部----->");
                        isNoMore = false;
                    }

                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {

                    View lastVisibleItemView = department_list.getChildAt(department_list.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == department_list.getHeight()) {
//                        System.out.println("#####滚动到底部######" + !isNoMore);
                        if (!isNoMore) {
                            page += 1;
                            setValue2();
                        }
                    }
                }
            }
        });
    }

    /**
     * name: 点击查看某个视频的详细 author:Tom 2016-1-28下午3:42:08
     */
    private class casesharing_listListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {
            TextView textView = (TextView) view
                    .findViewById(R.id.department_id);
            String id = textView.getText().toString();
            // MyProgressBarDialogTools.show(context);
            getVideoRulest(id);

        }

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

    public void setValue() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.myCircleInfo);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.CCMTVAPP1, obj.toString());
                    MyProgressBarDialogTools.hide();
                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    public void setValue2() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getUpVideo);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("page", page);

                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.CCMTVAPP, obj.toString());
                    MyProgressBarDialogTools.hide();
                    Message message = new Message();
                    message.what = 2;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    public void findById() {
        my_myhead = (CustomImageView) findViewById(R.id.my_myhead);
        personal_profile_name = (TextView) findViewById(R.id.personal_profile_name);
        personal_profile_departments = (TextView) findViewById(R.id.personal_profile_departments);
        personal_profile_traffic = (TextView) findViewById(R.id.personal_profile_traffic);
        personal_profile_attention = (TextView) findViewById(R.id.personal_profile_attention);
        personal_profile_fan_club = (TextView) findViewById(R.id.personal_profile_fan_club);
        personal_profile_colleague = (TextView) findViewById(R.id.personal_profile_colleague);
        personal_profile_blurb_text = (TextView) findViewById(R.id.personal_profile_blurb_text);
        iv_red_dians = (TextView) findViewById(R.id.iv_red_dians);
        personal_profile_blurb = (TextView) findViewById(R.id.personal_profile_blurb);
        personal_profile_video = (TextView) findViewById(R.id.personal_profile_video);
        personal_profile_img1 = (ImageView) findViewById(R.id.personal_profile_img1);
        personal_profile_img2 = (ImageView) findViewById(R.id.personal_profile_img2);
        personal_profile_img3 = (ImageView) findViewById(R.id.personal_profile_img3);
        personal_profile_blurb_view = findViewById(R.id.personal_profile_blurb_view);
        personal_profile_video_view = findViewById(R.id.personal_profile_video_view);
        personal_profile_blurb_layout = (LinearLayout) findViewById(R.id.personal_profile_blurb_layout);
        personal_profile_video_layout = (LinearLayout) findViewById(R.id.personal_profile_video_layout);
        personal_profile_blurb_null_layout = (LinearLayout) findViewById(R.id.personal_profile_blurb_null_layout);
        department_list = (ListView) findViewById(R.id.department_list);
        Modify_the_circle = (ImageView) findViewById(R.id.Modify_the_circle);
    }


    public void onclick() {
        //上传视频
        personal_profile_blurb_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                personal_profile_blurb.setTextColor(getResources().getColor(R.color.personal_profile_select));
                personal_profile_blurb_view.setBackgroundColor(getResources().getColor(R.color.personal_profile_select));
                personal_profile_video.setTextColor(getResources().getColor(R.color.personal_profile_no_select));
                personal_profile_video_view.setVisibility(View.INVISIBLE);
                personal_profile_blurb_text.setVisibility(View.VISIBLE);
                department_list.setVisibility(View.GONE);
            }
        });
        personal_profile_video_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                personal_profile_video.setTextColor(getResources().getColor(R.color.personal_profile_select));
                personal_profile_video_view.setBackgroundColor(getResources().getColor(R.color.personal_profile_select));
                personal_profile_blurb.setTextColor(getResources().getColor(R.color.personal_profile_no_select));
                personal_profile_blurb_view.setVisibility(View.INVISIBLE);
                department_list.setVisibility(View.VISIBLE);
                personal_profile_blurb_text.setVisibility(View.GONE);
            }
        });

/*
        tv_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_uploadhis.setVisibility(View.VISIBLE);
                layou_toup.setVisibility(View.GONE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject();
                            object.put("uid", SharedPreferencesTools.getUidToLoginClose(getActivity()));
                            object.put("act", URLConfig.getUserUploadInfo);
                            String result = HttpClientUtils.sendPost(getActivity(),
                                    URLConfig.CCMTVAPP1, object.toString());
                            Message message = new Message();
                            message.what = 1;
                            message.obj = result;
                            handler.sendMessage(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

*/

    }

    /**
     * name:使用xutils 夹在图片 author:Tom 2016-1-7下午1:28:03
     *
     * @param img  图片控件
     * @param path 图片网络地址
     */
    public void loadImg(ImageView img, String path) {
        XUtilsImageLoader xUtilsImageLoader = new XUtilsImageLoader(Personal_profile.this);
        xUtilsImageLoader.display(img, FirstLetter.getSpells(path));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn";
        super.onPause();
    }
}
