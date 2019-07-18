package com.linlic.ccmtv.yx.activity.hospital_training;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.VideoModel;
import com.linlic.ccmtv.yx.activity.my.learning_task.VideoSignActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.CategoryView;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/25.
 */

public class More_video extends BaseActivity {
    private GridView learning_video;
    private Context context;
    private int page = 1;
    private CategoryView category;
    private String bigType = "";
    private List<VideoModel> videos = new ArrayList<>();
    private List<String> section_name = new ArrayList<>();
    private List<String> order_name = new ArrayList<>();
    private String section_result = "";
    private String order_result = "";
    private String section_type = "科室";
    private String order_type = "排序";
    private boolean isNoMore = true;
    private Map<String, Object> Section = new HashMap<>();
    private Map<String, Object> order_map = new HashMap<>();
    private BaseListAdapter baseListAdapterVideo;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        boolean status = jsonObject.getInt("status") == 1;
                        if (status) { // 成功
                            JSONArray dataArray = jsonObject.getJSONArray("video_list");
                            if (dataArray.length() < 10) {
                                isNoMore = false;
                            } else {
                                isNoMore = true;
                            }
                            for (int i = 0; i < dataArray.length(); i++) {
                                VideoModel commodity_module = new VideoModel(dataArray.getJSONObject(i));
                                videos.add(commodity_module);
                            }
                        } else {
                            Toast.makeText(More_video.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }

                        baseListAdapterVideo.notifyDataSetChanged();
                        setResultStatus(videos.size() > 0, jsonObject.getInt("status"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        MyProgressBarDialogTools.hide();
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            setActivity_title_name(jsonObject.getString("video_list_title"));
                            JSONObject keshi_list = jsonObject.getJSONObject("keshi_list");
                            Iterator<String> keys = keshi_list.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                section_name.add(key);
                                Section.put(key, keshi_list.getString(key));
                            }
                            category.add(section_name, section_type, "");

                            JSONObject order_list = jsonObject.getJSONObject("order_list");
                            Iterator<String> keys1 = order_list.keys();
                            while (keys1.hasNext()) {
                                String key = keys1.next();
                                order_name.add(key);
                                order_map.put(key, order_list.getString(key));
                            }
                            category.add(order_name, order_type, "");
//                            refreshLayout.finishRefresh( );
                        } else {
//                            refreshLayout.finishRefresh( false);
                            Toast.makeText(More_video.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        section_result = section_name.get(0);
                        order_result = order_name.get(0);
                        initLearning_video();
                        initdata();
                        setVideos();
                    } catch (Exception e) {
//                        refreshLayout.finishRefresh( false);
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 3:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        boolean status = jsonObject.getInt("status") == 1;
                        if (status) { // 成功
                            JSONArray dataArray = jsonObject.getJSONArray("video_list");
                            if (dataArray.length() < 10) {
                                isNoMore = false;
                            } else {
                                isNoMore = true;
                            }
                            for (int i = 0; i < dataArray.length(); i++) {
                                VideoModel commodity_module = new VideoModel(dataArray.getJSONObject(i));
                                videos.add(commodity_module);
                            }
                        } else {
                            Toast.makeText(More_video.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }

                        baseListAdapterVideo.notifyDataSetChanged();
                        setResultStatus(videos.size() > 0, jsonObject.getInt("status"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1,
                            Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    setResultStatus(videos.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOST);
                    break;
                default:
                    break;
            }

        }
    };
    private NodataEmptyLayout video_nodata;

    private void setResultStatus(boolean status, int code) {
        if (status) {
            learning_video.setVisibility(View.VISIBLE);
            video_nodata.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                video_nodata.setNetErrorIcon();
            } else {
                video_nodata.setLastEmptyIcon();
            }
            learning_video.setVisibility(View.GONE);
            video_nodata.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.more_video);
        context = this;
        bigType = getIntent().getStringExtra("bigType");
        findId();
        initLearning_video();
        setCategoryView();

    }


    public void initLearning_video() {


        baseListAdapterVideo= new BaseListAdapter(learning_video, videos, R.layout.learning_video_item) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                VideoModel map = (VideoModel) item;

                helper.setText(R.id.video_id,map.getAid());
                helper.setText(R.id.video_name,map.getName());
                helper.setImageBitmaps(R.id.video_img1,map.getIconUrl());

            }
        };
        learning_video.setAdapter(baseListAdapterVideo);
        learning_video.setSelector(new ColorDrawable(Color.TRANSPARENT));//取消GridView中Item选中时默认的背景色
        // listview点击事件
        learning_video.setOnItemClickListener(new  casesharing_listListener2());

        baseListAdapterVideo.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > 0) {
                }
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = learning_video.getChildAt(0);

                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        isNoMore = false;
                    }

                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {

                    View lastVisibleItemView = learning_video.getChildAt(learning_video.getChildCount() - 2);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == learning_video.getHeight()) {
                        if (!isNoMore) {
                            page += 1;
                            setVideos2();
                        }
                    }
                }
            }
        });
    }

    /**
     * name: 点击查看某个视频的详细 author:Tom 2016-1-28下午3:42:08
     */
    private class casesharing_listListener2 implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {

            TextView textView = (TextView) view.findViewById(R.id.video_id);
            String video_id = textView.getText().toString();
            final String uid = SharedPreferencesTools.getUidToLoginClose(context);
            if (uid == null || ("").equals(uid)) {
                return;
            }
            Intent intent = new Intent(context, VideoSignActivity.class);
            intent.putExtra("aid", video_id);
            intent.putExtra("my_our_video", "my_our_video");
            startActivity(intent);

        }

    }


    @Override
    public void findId() {
        super.findId();
        learning_video = (GridView) findViewById(R.id.learning_video);
        category = (CategoryView) findViewById(R.id.category2);
        video_nodata = (NodataEmptyLayout) findViewById(R.id.rl_more_video_nodata);
    }

    public void initdata() {

        category.setOnClickCategoryListener(new CategoryView.OnClickCategoryListener() {
            //逻辑回掉
            @Override
            public void click(RadioGroup group, int checkedId) {
                RadioButton button = (RadioButton) group.findViewById(checkedId);
                if (button.getTag().equals("科室")) {
                    section_result = button.getText().toString();
                } else if (button.getTag().equals("排序")) {
                    order_result = button.getText().toString();

                }
                videos.clear();
                page = 1;
                setVideos();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        switch (bigType) {
            case "basic"://医学三基
                enterUrl = "http://yy.ccmtv.cn/basic_index/index.html";
                break;
            case "train"://住院医师规培
                enterUrl = "http://yy.ccmtv.cn/train_index/index.html";
                break;
            case "practicing"://执医考试
                enterUrl = "http://yy.ccmtv.cn/Practicing_exam.html";
                break;
            case "general"://全科培训
                enterUrl = "http://yy.ccmtv.cn/General_practice/index.html";
                break;
        }
        super.onPause();
    }

    public void setVideos() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.trainVideoList);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("page", page);
                    obj.put("order", order_map.get(order_result));
                    obj.put("fid", Section.get(section_result));
                    obj.put("fname", section_result.equals("全部") ? "" : section_result);
                    obj.put("bigType", getIntent().getStringExtra("bigType"));
//                    Log.e("医院培训-访问条件", obj.toString());
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.Hospital_training, obj.toString());
//                    Log.e("医院培训-视频数据", result);
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

    public void setVideos2() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.trainVideoList);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("page", page);
                    obj.put("order", order_map.get(order_result));
                    obj.put("fid", Section.get(section_result));
                    obj.put("fname", section_result.equals("全部") ? "" : section_result);
                    obj.put("bigType", getIntent().getStringExtra("bigType"));
//                    Log.e("医院培训-访问条件", obj.toString());
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.Hospital_training, obj.toString());
//                    Log.e("医院培训-视频数据", result);
                    Message message = new Message();
                    message.what = 3;
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

    public void setCategoryView() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.trainVideoListCate);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("bigType", getIntent().getStringExtra("bigType"));
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.Hospital_training, obj.toString());
//                    Log.e("医院培训-视频数据-条件", result);
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

}
