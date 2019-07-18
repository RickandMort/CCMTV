package com.linlic.ccmtv.yx.activity.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.kzbf.utils.SkyVisitUtils;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.XUtilsImageLoader;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/31.
 */
public class Video_local_main extends BaseActivity {
    Context context;
    //用户统计
    private String entertime, leavetime, enterUrl, aid, fid;
    private String title_name;
    private String video_url;
    private String video_demand;
    private String video_picurl;
    private ListView video_local_list;

    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    SimpleAdapter adapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    video_local_list.setAdapter(adapter);
                    break;
                case 2:
                    try {
                        Uri uri = Uri.parse(msg.obj.toString());
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri, "video/mp4");
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    Toast.makeText(Video_local_main.this, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_local_main);
        context = this;

        aid = getIntent().getStringExtra("aid");
        fid = getIntent().getStringExtra("fid");

        Intent intent = getIntent();
        title_name = intent.getExtras().getString("video_title");
        video_url = intent.getExtras().getString("video_url");
        video_demand = intent.getExtras().getString("video_demand");
        video_picurl = intent.getExtras().getString("video_picurl");

        findId();
        setText();

        setmsgdb();
    }

    @Override
    public void findId() {
        super.findId();
        video_local_list = (ListView) findViewById(R.id.video_local_list);
    }

    public void setText() {
        super.setActivity_title_name(title_name);
    }

    public void setmsgdb() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONArray jsonArray = new JSONArray(video_url);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("Recent_Browse_File_item_title", title_name + (i + 1));
                        map.put("Recent_Browse_File_id", jsonArray.getString(i));
                        map.put("Recent_Browse_File_on_demand", "播放数：" + video_demand);
                        map.put("Recent_Browse_File_item_img", video_picurl);
                        data.add(map);
                    }

                    adapter = new SimpleAdapter(Video_local_main.this, data,
                            R.layout.video_item, new String[]{
                            "Recent_Browse_File_item_title",
                            "Recent_Browse_File_id",
                            "Recent_Browse_File_on_demand",
                            "Recent_Browse_File_item_img"}, new int[]{
                            R.id.departemnt_item_title,
                            R.id.department_id,
                            R.id.department_on_demand,
                            R.id.departemnt_item_img});
                    adapter.setViewBinder(new SimpleAdapter.ViewBinder() {

                        @Override
                        public boolean setViewValue(View view, Object data,
                                                    String textRepresentation) {
                            if (view instanceof ImageView) {
                                ImageView iv = (ImageView) view;
                                loadImg(iv, data.toString());
                                return true;
                            }
                            return false;
                        }
                    });

                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                    // listview点击事件
                    video_local_list.setOnItemClickListener(new casesharing_listListener());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    /**
     * name: 点击查看某个视频的详细 author:Tom 2016-1-28下午3:42:08
     */
    private class casesharing_listListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
            TextView textView = (TextView) view.findViewById(R.id.department_id);
            String id = textView.getText().toString();
            Message message = new Message();
            message.what = 2;
            message.obj = id;
            handler.sendMessage(message);
        }
    }

    /**
     * name:使用xutils 夹在图片 author:Tom 2016-1-7下午1:28:03
     *
     * @param img  图片控件
     * @param path 图片网络地址
     */
    public void loadImg(ImageView img, String path) {
        XUtilsImageLoader xUtilsImageLoader = new XUtilsImageLoader(Video_local_main.this);
        xUtilsImageLoader.display(img, FirstLetter.getSpells(path));
    }

    @Override
    protected void onResume() {
        //保存进入时间
        entertime = SkyVisitUtils.getCurrentTime();
        super.onResume();
    }

    @Override
    protected void onPause() {
        //保存退出的时间
        leavetime = SkyVisitUtils.getCurrentTime();
        enterUrl = "http://www.ccmtv.cn/video/" + fid + "/" + aid + ".html";//拼接url
        //保存在线时间到服务器
        SkyVisitUtils.OnlineStatistical(context, enterUrl, entertime, leavetime);
        super.onPause();
    }
}
