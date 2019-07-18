package com.linlic.ccmtv.yx.activity.hospital_training;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.VideoModel;
import com.linlic.ccmtv.yx.activity.my.learning_task.VideoSignActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Administrator on 2017/12/25.
 */

public class Learning_pachage extends BaseActivity {
    private GridView learning_video;
    private Context context;
    private String  bigType;
    private int page = 1;
    private String id = "";
//    private TwinklingRefreshLayout refreshLayout;
    private boolean isNoMore = true;
    private BaseListAdapter baseListAdapterVideo;
    private List<VideoModel> videos = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            JSONArray dataArray = jsonObject.getJSONArray("list");
                            for (int i = 0; i < dataArray.length(); i++) {
                                VideoModel commodity_module = new VideoModel(dataArray.getJSONObject(i));
                                videos.add(commodity_module);
                            }
                        } else {
                            Toast.makeText(Learning_pachage.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                     /*   new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                refreshLayout.finishRefreshing();
                            }
                        }, 1000);*/
                        baseListAdapterVideo.notifyDataSetChanged();
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
                            JSONArray dataArray = jsonObject.getJSONArray("list");
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
                            Toast.makeText(Learning_pachage.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                      /*  new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                refreshLayout.finishLoadmore();
                            }
                        }, 1000);*/
                        baseListAdapterVideo.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {

                    }
                    break;
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
        setContentView(R.layout.learning_package);
        context = this;
        findId();
        id = getIntent().getStringExtra("id");
        bigType = getIntent().getStringExtra("bigType");
        super.setActivity_title_name(getIntent().getStringExtra("title"));
        initLearning_video();
        setVideos();
    }


    public void initLearning_video() {

        baseListAdapterVideo = new BaseListAdapter(learning_video, videos, R.layout.learning_video_item) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                VideoModel map = (VideoModel) item;

                helper.setText(R.id.video_id, map.getAid());
                helper.setText(R.id.video_name, map.getName());
                helper.setImageBitmapGlide(context,R.id.video_img1, map.getIconUrl());
            }
        };

//        myGridAdapter3 = new MyGridAdapter3(context, videos);
        learning_video.setAdapter(baseListAdapterVideo);
        learning_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
        });
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

                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = learning_video.getChildAt(learning_video.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == learning_video.getHeight()) {
                        if (isNoMore) {
                            page += 1;
                            setVideos2();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void findId() {
        super.findId();
        learning_video = (GridView) findViewById(R.id.learning_video);
       /* refreshLayout = (TwinklingRefreshLayout) findViewById(R.id.refreshLayout);
*/
      /*  refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                videos.clear();
                page = 1;
                refreshLayout.setEnableLoadmore(true);
                setVideos();
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                if (isNoMore) {
                    page++;
                    setVideos2();
                } else {
                    refreshLayout.setEnableLoadmore(false);
                }
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        //将数据保存到服务器
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
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.trainStudy);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("page", page);
                    obj.put("type", getIntent().getStringExtra("aid"));

                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.Hospital_training, obj.toString());
//                    Log.e("医院培训-视频数据", result);
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

    public void setVideos2() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.trainStudy);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("page", page);
                    obj.put("type", getIntent().getStringExtra("aid"));

                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.Hospital_training, obj.toString());
//                    Log.e("医院培训-视频数据", result);
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
