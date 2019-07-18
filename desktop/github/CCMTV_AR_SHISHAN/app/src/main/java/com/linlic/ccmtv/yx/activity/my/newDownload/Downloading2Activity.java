package com.linlic.ccmtv.yx.activity.my.newDownload;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StatFs;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.VideoModel;
import com.linlic.ccmtv.yx.utils.FileSizeUtil;
import com.linlic.ccmtv.yx.utils.SDCardUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.getSdcardPath;
import com.lzy.okgo.db.DownloadManager;
import com.lzy.okgo.model.Progress;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadTask;
import com.lzy.okserver.task.XExecutor;

import java.util.ArrayList;
import java.util.List;

public class Downloading2Activity extends BaseActivity implements XExecutor.OnAllTaskEndListener, View.OnClickListener {

    private DownloadAdapterNew adapter;
    private OkDownload okDownload;
    private RecyclerView recyclerView;
    private Context context;
    private TextView activity_title_name, download_topRightTxt, tv_pause, delete_video;
    private LinearLayout start_pause, download_bottom;
    //内存信息
    private TextView memory_tv;
    private ProgressBar memory_progress;
    private RelativeLayout download_memory;
    private List<DownloadTask> values;
    private List<DownloadTask> videoValues = new ArrayList<>();
    //下载中progress信息
    List<Progress> downloading = DownloadManager.getInstance().getDownloading();
    List<Progress> downloadingNew = new ArrayList<>();
    private TextView deleteVideo_tv;//删除按钮
    private TextView selectAllCheck_tv;//全选按钮
    private VideoModel videoModel;
    private DownloadTask downloadTask;
    private int checkNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消屏幕旋转
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_downloading2);
        context = this;
        initView();
        initData();

        memoryUtils();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        activity_title_name = (TextView) findViewById(R.id.activity_title_name);
        download_topRightTxt = (TextView) findViewById(R.id.download_topRightTxt2);
        memory_tv = (TextView) findViewById(R.id.memory_tv);
        memory_progress = (ProgressBar) findViewById(R.id.memory_progress);
        download_memory = (RelativeLayout) findViewById(R.id.download_memory);
        start_pause = (LinearLayout) findViewById(R.id.ll_start_pause);
        tv_pause = (TextView) findViewById(R.id.tv_pause);
        download_bottom = (LinearLayout) findViewById(R.id.download_bottom2);
        deleteVideo_tv = (TextView) findViewById(R.id.delete_video2);
        selectAllCheck_tv = (TextView) findViewById(R.id.selectAllCheck2);
        delete_video = (TextView) findViewById(R.id.delete_video2);

        start_pause.setOnClickListener(this);
        download_topRightTxt.setOnClickListener(this);
        selectAllCheck_tv.setOnClickListener(this);
        deleteVideo_tv.setOnClickListener(this);
    }

    private void initData() {
        try {
            activity_title_name.setText("正在缓存");
            values = OkDownload.restore(DownloadManager.getInstance().getDownloading());
            videoValues = changeValues(values);

            okDownload = OkDownload.getInstance();
            adapter = new DownloadAdapterNew(this);
            adapter.updateData(DownloadAdapterNew.TYPE_ING);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
            adapter.OnAddItemClickListener(new DownloadAdapterNew.OnItemClickListener() {
                @Override
                public void plusNum() {
                    checkNum++;
                    delete_video.setTextColor(Color.RED);
                    delete_video.setText("删除(" + checkNum + ")");
                }

                @Override
                public void downNum() {
                    checkNum--;
                    if (checkNum != 0) {
                        delete_video.setTextColor(Color.RED);
                        delete_video.setText("删除(" + checkNum + ")");
                    } else if (checkNum == 0) {
                        delete_video.setText("删除");
                        selectAllCheck_tv.setText("全选");
                        delete_video.setTextColor(Color.BLACK);
                    }
                }
            });
            for (int i = 0; i < videoValues.size(); i++) {
                downloadTask = videoValues.get(i);
                VideoModel videoModel = (VideoModel) videoValues.get(i).progress.extra1;
                videoModel.setIsVisibility("1");

                /*------------------初始化时使所有item都处于未选中状态-----------------------*/
                if (videoModel.isChecked()) {
                    videoModel.setChecked(false);
                }
                videoModel.setCheckBoxStatus("0");
                /*------------------初始化时使所有item都处于未选中状态-----------------------*/

            }
            adapter.notifyDataSetChanged();
            okDownload.addOnAllTaskEndListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void memoryUtils() {
        String cachePath = SharedPreferencesTools.getCachePath(context);
        if (cachePath.contains("emulated")) {//手机内存
            String phoneMemory = FileSizeUtil.FormetFileSize(SDCardUtils.getTotalInternalMemorySize()) + "，" + "当前可用空间" + FileSizeUtil.FormetFileSize(SDCardUtils.getAvailableInternalMemorySize());
            memory_tv.setText("总空间" + phoneMemory);

            double resultDouble = ((double) SDCardUtils.getAvailableInternalMemorySize()) / ((double) SDCardUtils.getTotalInternalMemorySize());
            int resultInt = (int) (resultDouble * 100);
            memory_progress.setProgress(100 - resultInt);
        } else {//SD卡
            String sdcardMemory = FileSizeUtil.FormetFileSize(getAllSize()) + "，" + "当前可用空间" + FileSizeUtil.FormetFileSize(getAvailableExternalMemorySize());
            memory_tv.setText("总空间" + sdcardMemory);

            double resultDouble = ((double) getAvailableExternalMemorySize()) / ((double) getAllSize());
            int resultInt = (int) (resultDouble * 100);
            memory_progress.setProgress(100 - resultInt);
        }
    }

    public long getAllSize() {
        StatFs stat = new StatFs(getSdcardPath.getStoragePath(context, true));
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getBlockCount();
        return availableBlocks * blockSize;
    }

    public long getAvailableExternalMemorySize() {
        StatFs stat = new StatFs(getSdcardPath.getStoragePath(context, true));
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    @Override
    public void onAllTaskEnd() {
//        Toast.makeText(context,"所有下载任务已结束",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        okDownload.removeOnAllTaskEndListener(this);
        adapter.unRegister();
    }

    @Override
    protected void onResume() {
        initData();
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn/Member/Index.html";
        super.onPause();
    }

    public void back(View view) {
        values = OkDownload.restore(DownloadManager.getInstance().getDownloading());
        videoValues = changeValues(values);
        for (int i = 0; i < videoValues.size(); i++) {
            VideoModel videoModel = (VideoModel) videoValues.get(i).progress.extra1;
            if (videoModel.isChecked()) {
                videoModel.setChecked(false);
            }
            videoModel.setCheckBoxStatus("0");
        }
        finish();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        videoModel = (VideoModel) downloadTask.progress.extra1;
        switch (v.getId()) {
            case R.id.ll_start_pause:
                okDownload.pauseAll();
                break;
            case R.id.download_topRightTxt2://右上角编辑按钮
                checkNum = 0;
                //okDownload.pauseAll();
                values = OkDownload.restore(DownloadManager.getInstance().getDownloading());
                videoValues = changeValues(values);
                if (videoValues.size() > 0) {
                    deleteVideo_tv.setTextColor(Color.parseColor("#000000"));
                    for (int i = 0; i < videoValues.size(); i++) {
                        downloadTask = videoValues.get(i);
                        videoModel = (VideoModel) downloadTask.progress.extra1;
                        downloading = DownloadManager.getInstance().getDownloading();
                        downloadingNew = changeValues1(downloading);
                        if (videoModel.getCheckBoxStatus().equals("0")) {
                            videoModel.setCheckBoxStatus("1");
                            download_topRightTxt.setText("取消");
                            download_memory.setVisibility(View.GONE);
                            download_bottom.setVisibility(View.VISIBLE);
                            adapter.notifyDataSetChanged();
                        } else if (videoModel.getCheckBoxStatus().equals("1")) {
                            videoModel.setCheckBoxStatus("0");
                            download_bottom.setVisibility(View.GONE);
                            download_topRightTxt.setText("编辑");
                            //清空选中的信息
                            for (int j = 0; j < videoValues.size(); j++) {//清空checkbox选中的状态
                                VideoModel videoModel = (VideoModel) videoValues.get(i).progress.extra1;
                                if (videoModel.isChecked()) {
                                    videoModel.setChecked(false);
                                }
                            }
                            selectAllCheck_tv.setText("全选");
                            deleteVideo_tv.setText("删除");
                            download_memory.setVisibility(View.VISIBLE);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
                break;
            case R.id.selectAllCheck2://全选按钮
                values = OkDownload.restore(DownloadManager.getInstance().getDownloading());
                videoValues = changeValues(values);
                if (selectAllCheck_tv.getText().toString().equals("全选")) {
                    selectAllCheck_tv.setText("取消全选");
                    for (int i = 0; i < videoValues.size(); i++) {
                        VideoModel videoModel = (VideoModel) videoValues.get(i).progress.extra1;
                        if (!videoModel.isChecked()) {
                            videoModel.setChecked(true);
                            checkNum++;
                            delete_video.setText("删除(" + checkNum + ")");
                            delete_video.setTextColor(Color.RED);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else if (selectAllCheck_tv.getText().toString().equals("取消全选")) {
                    selectAllCheck_tv.setText("全选");
                    for (int i = 0; i < videoValues.size(); i++) {
                        VideoModel videoModel = (VideoModel) videoValues.get(i).progress.extra1;
                        if (videoModel.isChecked()) {
                            videoModel.setChecked(false);
                            checkNum--;
                            if (checkNum != 0) {
                                delete_video.setText("删除(" + checkNum + ")");
                            } else if (checkNum == 0) {
                                delete_video.setText("删除");
                                selectAllCheck_tv.setText("全选");
                                delete_video.setTextColor(Color.BLACK);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.delete_video2://删除按钮
                try {
                    values = OkDownload.restore(DownloadManager.getInstance().getDownloading());
                    videoValues = changeValues(values);
                    for (int i = 0; i < videoValues.size(); i++) {
                        VideoModel videoModel = (VideoModel) videoValues.get(i).progress.extra1;
                        if (videoModel.isChecked()) {
                            videoValues.get(i).remove(true);
                        }
                    }
                    download_bottom.setVisibility(View.GONE);
                    download_topRightTxt.setText("编辑");
                    values = OkDownload.restore(DownloadManager.getInstance().getDownloading());
                    videoValues = changeValues(values);
                    for (int i = 0; i < videoValues.size(); i++) {
                        downloadTask = videoValues.get(i);
                        videoModel = (VideoModel) downloadTask.progress.extra1;
                        videoModel.setCheckBoxStatus("0");
                        adapter.notifyDataSetChanged();
                    }
                    Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                    initData();
                    downloading = DownloadManager.getInstance().getDownloading();
                    downloadingNew = changeValues1(downloading);
                    if (downloadingNew.size() == 0) {
                        finish();
                    }
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            values = OkDownload.restore(DownloadManager.getInstance().getDownloading());
            videoValues = changeValues(values);
            for (int i = 0; i < videoValues.size(); i++) {
                VideoModel videoModel = (VideoModel) videoValues.get(i).progress.extra1;
                if (videoModel.isChecked()) {
                    videoModel.setChecked(false);
                }
                videoModel.setCheckBoxStatus("0");
            }
            finish();
            adapter.notifyDataSetChanged();
        }
        return super.onKeyDown(keyCode, event);
    }

    private List<DownloadTask> changeValues(List<DownloadTask> values) {
        List<DownloadTask> list = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {//type为video表示视频
            if (values.get(i).progress.extra1 != null && 1 == 1) {
                VideoModel model = (VideoModel) values.get(i).progress.extra1;
                if (model.getType().equals("video")) {
                    list.add(values.get(i));
                }
            }
        }
        return list;
    }

    private List<Progress> changeValues1(List<Progress> download) {
        List<Progress> list = new ArrayList<>();
        for (int i = 0; i < download.size(); i++) {//type为video表示视频
            if (download.get(i).extra1 != null && 1 == 1) {
                VideoModel model = (VideoModel) download.get(i).extra1;
                if (model.getType().equals("video")) {
                    list.add(download.get(i));
                }
            }
        }
        return list;
    }
}
