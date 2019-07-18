package com.linlic.ccmtv.yx.activity.hospital_training;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.VideoModel;
import com.linlic.ccmtv.yx.activity.my.learning_task.VideoSignActivity;
import com.linlic.ccmtv.yx.adapter.MyGridAdapter3;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyGridView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/25.
 */

public class Section_list extends BaseActivity {
    private MyGridView learning_video;
    private Context context;
    private int page = 1;
    private String id = "";
    private MyGridAdapter3 myGridAdapter3;
    private List<VideoModel> videos = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            JSONArray dataArray = jsonObject.getJSONArray("video_list");
                            for (int i = 0; i < dataArray.length(); i++) {
                                VideoModel commodity_module = new VideoModel(dataArray.getJSONObject(i));
                                videos.add(commodity_module);
                            }
                        } else {
                            Toast.makeText(Section_list.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
//                        MyProgressBarDialogTools.hide();
                        learning_video.notifyAll();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
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
        initLearning_video();
        setVideos();
    }

    public void initLearning_video() {

        myGridAdapter3 = new MyGridAdapter3(context, videos);
        learning_video.setAdapter(myGridAdapter3);
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
    }

    @Override
    public void findId() {
        super.findId();
        learning_video = (MyGridView) findViewById(R.id.learning_video);
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
                    obj.put("type", id);

                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.Medical_examination, obj.toString());
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