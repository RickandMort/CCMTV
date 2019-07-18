package com.linlic.ccmtv.yx.activity.upload;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.db.MyDbUtils;
import com.linlic.ccmtv.yx.activity.entity.DbUploadCase;
import com.linlic.ccmtv.yx.activity.entity.DbUploadVideo;
import com.linlic.ccmtv.yx.activity.upload.adapter.UploadCaseListAdapter;
import com.linlic.ccmtv.yx.activity.upload.adapter.UploadVideoListAdapter;
import com.linlic.ccmtv.yx.activity.upload.service.MyUploadCaseService;
import com.linlic.ccmtv.yx.activity.upload.service.MyUploadVideoService;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * name：上传记录列表
 * author：MrSong
 * data：2016/4/23.
 */
public class IsUpload extends BaseActivity {
    private View viewOne;
    private View viewTwo;
    private ListView listView;
    private Context context;
    private UploadCaseListAdapter adapter_case;
    private UploadVideoListAdapter adapter;
    private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    private MyUploadVideoReve myUploadVideoReve = new MyUploadVideoReve();
    private MyUploadCaseReve myUploadCaseReve = new MyUploadCaseReve();
    private LinearLayout layout_nodata;
    private String tag = "Video";
    private List<DbUploadVideo> all;
    private TextView tv_selectVideo, tv_selectCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.has_been_uploaded);
        context = this;
        //第一次进入调用一下，不然会报错
        if (SharedPreferencesTools.getUid(context).equals("")) return;
        findViewById();
        try {
            tag = getIntent().getStringExtra("TAG");
        } catch (Exception e) {
            e.printStackTrace();
        }
        initData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    public void initData() {
        //再次注册广播
        registerMyReceiver();
        if ("Case".equals(tag)) {
            adapter_case = new UploadCaseListAdapter(context, list);
            listView.setAdapter(adapter_case);
            list.clear();
            viewOne.setVisibility(View.INVISIBLE);
            viewTwo.setVisibility(View.VISIBLE);
            layout_nodata.setVisibility(View.GONE);
            //添加本地数据
            findAllUploadCase();
        } else {                  //不是病例  就是视频
            //添加本地数据
            adapter = new UploadVideoListAdapter(context, list);
            listView.setAdapter(adapter);
            list.clear();
            viewOne.setVisibility(View.VISIBLE);
            viewTwo.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
            findAllUploadVideo();
        }
    }

    private void findViewById() {
        super.findId();
        super.setClick();
        super.btn_nodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if ("Video".equals(tag)) {
                    intent.setClass(IsUpload.this, Upload_video.class);
                } else {
                    intent.setClass(IsUpload.this, Upload_case.class);
                }
                startActivity(intent);
            }
        });
        super.setActivity_btnnodata(R.string.btnnodata_toup);
        super.setActivity_tvnodata(R.string.tvnodata_toupjilu);
        super.setActivity_title_name(R.string.is_uploaded);
        viewOne = findViewById(R.id.selectItemOptions_one);
        viewTwo = findViewById(R.id.selectItemOptions_two);
        listView = (ListView) findViewById(R.id.upload_selectPageList);
        findViewById(R.id.upload_has_topRightTxt).setVisibility(View.GONE);
        tv_selectVideo = (TextView) findViewById(R.id.tv_selectVideo);
        tv_selectCase = (TextView) findViewById(R.id.tv_selectCase);

        /**
         * name：视频
         * author：MrSong
         * data：2016/3/29 20:59
         */
        tv_selectVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter = new UploadVideoListAdapter(context, list);
                listView.setAdapter(adapter);
                list.clear();
                tag = "Video";
                viewOne.setVisibility(View.VISIBLE);
                viewTwo.setVisibility(View.INVISIBLE);
                findAllUploadVideo();
            }
        });
        /**
         * name：病例
         * author：MrSong
         * data：2016/3/29 20:59
         */
        tv_selectCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter_case = new UploadCaseListAdapter(context, list);
                listView.setAdapter(adapter_case);
                list.clear();
                tag = "Case";
                viewOne.setVisibility(View.INVISIBLE);
                viewTwo.setVisibility(View.VISIBLE);
                findAllUploadCase();
            }
        });
    }

    private void registerMyReceiver() {
        //注册上传视频广播 -----当前页面实时刷新数据
        IntentFilter fil = new IntentFilter();
        fil.addAction("upload_video_progress");
        fil.addAction("upload_video_failure");
        fil.addAction("upload_video_success");
        fil.addAction("upload_video");
        registerReceiver(myUploadVideoReve, fil);
        //注册上传病例广播
        IntentFilter fil_uploadCase = new IntentFilter();
        fil_uploadCase.addAction("upload_case_progress");
        fil_uploadCase.addAction("upload_case_failure");
        fil_uploadCase.addAction("upload_case_success");
        fil_uploadCase.addAction("upload_case");
        registerReceiver(myUploadCaseReve, fil_uploadCase);
    }

    private class MyUploadVideoReve extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("Video".equals(tag)) {
                list.clear();
                findAllUploadVideo();
            }
        }
    }

    private class MyUploadCaseReve extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("Case".equals(tag)) {
                list.clear();
                findAllUploadCase();
            }
        }
    }

    /**
     * name：添加本地视频到list
     * author：MrSong
     * data：2016/4/23 2:55
     */
    private void findAllUploadVideo() {
        list.clear();
        all = MyDbUtils.findAllUploadVideo(context);
//        Log.d("数据库：", all + "");
        if (all != null) {
            if (all.size() != 0) {
                layout_nodata.setVisibility(View.GONE);
                for (DbUploadVideo video : all) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("fileSize", video.getCurrent() + "/" + video.getTotal());
                    map.put("speed", "");
                    map.put("state", video.getState());
                    map.put("videoName", video.getVideoName());
                    map.put("prog", video.getUploadProgress());
                    map.put("filePath", video.getFilePath());
                    map.put("picUrl", video.getFileImgPath());
                    map.put("videoId", video.getId() + "");
                    list.add(map);
                }
            } else {
                layout_nodata.setVisibility(View.VISIBLE);
            }
        } else {
            layout_nodata.setVisibility(View.VISIBLE);
        }
//        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //短按播放当前视频（本地播放器本地数据）
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                Intent intent = new Intent(IsUpload.this, VideoPlayerActivity.class);
                                                intent.putExtra("videoPath", all.get(position).getFilePath());
                                                intent.putExtra("videoTitle", all.get(position).getVideoName());
                                                startActivity(intent);
                                            }
                                        }
        );

        //长按删除视频记录
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView state = (TextView) view.findViewById(R.id.upload_down_item_state);
                final TextView videoId = (TextView) view.findViewById(R.id.upload_down_item_videoid);
                if (state.getText().toString().equals(MyUploadVideoService.upload_success + "")
                        || state.getText().toString().equals(MyUploadVideoService.upload_failure + "")) {
                    new AlertDialog.Builder(context).setMessage("是否要删除当前记录").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MyDbUtils.deleteUploadVideo(context, videoId.getText().toString());
                            findAllUploadVideo();
                        }
                    }).setNegativeButton("取消", null).show();
                } else {
                    new AlertDialog.Builder(context).setMessage("当前任务正在上传中，请稍后")
                            .setPositiveButton("确定", null).show();
                }
                return true;
            }
        });
    }

    /**
     * name：添加本地病例至list
     * author：Larry
     * data：2016/5/5 11:06
     */
    private void findAllUploadCase() {
        list.clear();
        List<DbUploadCase> all = MyDbUtils.findAllUploadCase(context);
        if (all != null) {
            if (all.size() != 0) {
                layout_nodata.setVisibility(View.GONE);
                for (DbUploadCase Case : all) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("state", Case.getState());
                    map.put("caseTitle", Case.getCaseTitle());
                    map.put("caseProg", Case.getUploadProgress());
                    map.put("case_id", Case.getId() + "");
                    map.put("currentsize", Case.getCurrent() + "");
                    map.put("totalsize", Case.getTotal() + "");
                    map.put("time", Case.getUptime() + "");
                    map.put("upfileA_1", Case.getUpfileA_1() + "");
                    list.add(map);
                }
            } else {
                layout_nodata.setVisibility(View.VISIBLE);
            }
        } else {
            layout_nodata.setVisibility(View.VISIBLE);
        }
        adapter_case.notifyDataSetChanged();
        //删除病例记录
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final TextView tv_case_id = (TextView) view.findViewById(R.id.tv_case_id);
                DbUploadCase dbUploadCase = MyDbUtils.findUploadCaseMsg_Img(context, tv_case_id.getText().toString());
                Intent intent = new Intent(context, MyCaseActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("dbUploadCase", dbUploadCase);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView state = (TextView) view.findViewById(R.id.tv_case_state);
                final TextView tv_case_id = (TextView) view.findViewById(R.id.tv_case_id);
                if (state.getText().toString().equals(MyUploadCaseService.upload_success + "")
                        || state.getText().toString().equals(MyUploadCaseService.upload_failure + "")) {
                    new AlertDialog.Builder(context).setMessage("是否要删除当前记录").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    MyDbUtils.deleteUploadCase(context, tv_case_id.getText().toString());
                                    findAllUploadCase();
                                }
                            }).setNegativeButton("取消", null).show();
                } else {
                    new AlertDialog.Builder(context).setMessage("当前任务正在上传中，请稍后").setPositiveButton("确定", null).show();
                }
                return true;   //false触发长按监听时，程序会先跳入长按事件处理，长按画面出现后随即又跳入setOnItemOnClickListener()事件处理
            }
        });
    }

    @Override
    public void onBackPressed() {
        IsUpload.this.unregisterReceiver(myUploadVideoReve);
        IsUpload.this.unregisterReceiver(myUploadCaseReve);
        finish();
    }

    public void back(View view) {
        IsUpload.this.unregisterReceiver(myUploadVideoReve);
        IsUpload.this.unregisterReceiver(myUploadCaseReve);
        finish();
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

}