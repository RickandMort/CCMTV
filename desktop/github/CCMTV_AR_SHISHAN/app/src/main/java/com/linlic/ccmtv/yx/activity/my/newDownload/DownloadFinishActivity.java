package com.linlic.ccmtv.yx.activity.my.newDownload;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.Util;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.CustomActivity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.VideoModel;
import com.linlic.ccmtv.yx.utils.DataCleanManager;
import com.linlic.ccmtv.yx.utils.FileSizeUtil;
import com.linlic.ccmtv.yx.utils.SDCardUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.Utils;
import com.linlic.ccmtv.yx.utils.getSdcardPath;
import com.lzy.okgo.db.DownloadManager;
import com.lzy.okgo.model.Progress;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;
import com.lzy.okserver.task.XExecutor;

import java.io.File;
import java.io.FileFilter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DownloadFinishActivity extends BaseActivity implements XExecutor.OnAllTaskEndListener, View.OnClickListener {

    private Context context;
    //private DownloadAdapterNew adapter;
    private DownloadVideoFileAdapter adapter;
    private OkDownload okDownload;
    private RecyclerView recyclerView;
    private TextView activity_title_name, download_topRightTxt, downloading_num, upload_down_item_size, down_item_group_title;
    private LinearLayout ll_caching, download_bottom;
    //内存信息
    private TextView memory_tv, download_prog, first_downloading_time, tv_iscaching, delete_video;
    private ProgressBar memory_progress;
    private RelativeLayout download_memory;
    private DownloadTask downloadTask;
    //下载中progress信息
    List<Progress> downloading = DownloadManager.getInstance().getDownloading();
    List<Progress> downloadingNew = new ArrayList<>();
    //已完成progress信息
    List<Progress> downloadfinish = DownloadManager.getInstance().getFinished();
    List<Progress> downloadfinishNew = new ArrayList<>();
    //全部下载progress信息
    List<Progress> download = DownloadManager.getInstance().getAll();
    List<Progress> downloadNew = new ArrayList<>();

    private List<VideoBean> videoFiles = new ArrayList<>();
    private List<DownloadTask> values;
    private List<DownloadTask> videoValues = new ArrayList<>();
    private List<DownloadTask> values1;
    private List<DownloadTask> videoValues1 = new ArrayList<>();
    /*private double totalSize = 0;
    private double currentSize = 0;*/
    private long totalSize = 0;
    private long currentSize = 0;
    private long csAll = 0;
    private long csFirst = 0;
    /*private double csAll = 0;
    private double csFirst = 0;*/
    private double csSecond = 0;
    private double csThird = 0;
    private ImageView upload_down_item_img, iv_caching;
    private ProgressBar download_progress;
    private TextView deleteVideo_tv;//删除按钮
    private TextView selectAllCheck_tv;//全选按钮
    private VideoModel videoModel;
    private int checkNum = 0;
    private View bottom_view;
    private DownloadCompleteReceiver downloadCompleteReceiver;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    initData();
                    MediaScannerConnection.scanFile(context, new String[]{msg.obj + ""}
                            , new String[]{"video/mp4"}, new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                }
                            });
                    break;
                default:
                    break;
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_finish);
        context = this;
        findId();
        initView();
        initBroadcastReceiver();
        //isShowNoData();
        //initData();

        memoryUtils();
    }

    private void initBroadcastReceiver() {
        downloadCompleteReceiver = new DownloadCompleteReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("intent.action.my.download.complete");
        registerReceiver(downloadCompleteReceiver, filter);
    }

    private void isShowNoData() {
        download = DownloadManager.getInstance().getAll();
        downloadNew = changeValues1(download);
//        if (downloadNew.size() == 0) {
        if (videoFiles.size() == 0 && downloading.size() <= 0) {
            layout_nodata.setVisibility(View.VISIBLE);
            download_topRightTxt.setVisibility(View.GONE);
            download_memory.setVisibility(View.VISIBLE);
        } else {
            layout_nodata.setVisibility(View.GONE);
            download_topRightTxt.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView1);
        activity_title_name = (TextView) findViewById(R.id.activity_title_name);
        download_topRightTxt = (TextView) findViewById(R.id.download_topRightTxt1);
        memory_tv = (TextView) findViewById(R.id.memory_tv);
        memory_progress = (ProgressBar) findViewById(R.id.memory_progress);
        download_memory = (RelativeLayout) findViewById(R.id.download_memory);
        ll_caching = (LinearLayout) findViewById(R.id.ll_caching1);
        downloading_num = (TextView) findViewById(R.id.downloading_num1);
        upload_down_item_size = (TextView) findViewById(R.id.upload_down_item_size1);
        upload_down_item_img = (ImageView) findViewById(R.id.upload_down_item_img1);
        download_progress = (ProgressBar) findViewById(R.id.download_progress1);
        down_item_group_title = (TextView) findViewById(R.id.down_item_group_title1);
//        layout_nodata = (LinearLayout) findViewById(R.id.layout_nodata);
        download_bottom = (LinearLayout) findViewById(R.id.download_bottom1);
        deleteVideo_tv = (TextView) findViewById(R.id.delete_video1);
        selectAllCheck_tv = (TextView) findViewById(R.id.selectAllCheck1);
        download_prog = (TextView) findViewById(R.id.download_prog1);
//        btn_nodata = (Button) findViewById(R.id.btn_nodata);
//        tv_nodata = (TextView) findViewById(R.id.tv_nodata);
        first_downloading_time = (TextView) findViewById(R.id.first_downloading_time);
        iv_caching = (ImageView) findViewById(R.id.iv_caching);
        tv_iscaching = (TextView) findViewById(R.id.tv_iscaching);
        delete_video = (TextView) findViewById(R.id.delete_video1);
        bottom_view = findViewById(R.id.bottom_view);

        download_topRightTxt.setOnClickListener(this);
        ll_caching.setOnClickListener(this);
        deleteVideo_tv.setOnClickListener(this);
        selectAllCheck_tv.setOnClickListener(this);

        super.setClick();
        super.btn_nodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DownloadFinishActivity.this, CustomActivity.class);
                intent.putExtra("type", "my");
                startActivity(intent);
            }
        });
    }

    private void initRecyclerView() {
        adapter = new DownloadVideoFileAdapter(this, videoFiles);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.OnAddItemClickListener(new DownloadVideoFileAdapter.OnItemClickListener() {
            @Override
            public void plusNum() {
                checkNum++;
                delete_video.setText("删除(" + checkNum + ")");
                delete_video.setTextColor(Color.RED);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void downNum() {
                checkNum--;
                if (checkNum != 0) {
                    delete_video.setText("删除(" + checkNum + ")");
                    delete_video.setTextColor(Color.RED);
                } else if (checkNum == 0) {
                    delete_video.setText("删除");
                    selectAllCheck_tv.setText("全选");
                    delete_video.setTextColor(Color.BLACK);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initData() {
        try {
            videoFiles.clear();
            downloading = DownloadManager.getInstance().getDownloading();
            //MyProgressBarDialogTools.show(this);

            videoFiles = getDownloadVideoFiles(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ccmtvCache/video/"));
            //MyProgressBarDialogTools.hide();
            initRecyclerView();

            activity_title_name.setText("下载");
            tv_nodata.setText("您还没有下载视频");
            values = OkDownload.restore(DownloadManager.getInstance().getDownloading());
            videoValues = changeValues(values);
            values1 = OkDownload.restore(DownloadManager.getInstance().getFinished());
            videoValues1 = changeValues(values1);
            is_caching();

            okDownload = OkDownload.getInstance();
        /*adapter = new DownloadAdapterNew(this);
        adapter.updateData(DownloadAdapterNew.TYPE_FINISH);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.OnAddItemClickListener(new DownloadAdapterNew.OnItemClickListener() {
            @Override
            public void plusNum() {
                checkNum++;
                delete_video.setText("删除(" + checkNum + ")");
                delete_video.setTextColor(Color.RED);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void downNum() {
                checkNum--;
                if (checkNum != 0) {
                    delete_video.setText("删除(" + checkNum + ")");
                    delete_video.setTextColor(Color.RED);
                } else if (checkNum == 0) {
                    delete_video.setText("删除");
                    selectAllCheck_tv.setText("全选");
                    delete_video.setTextColor(Color.BLACK);
                }
                adapter.notifyDataSetChanged();
            }
        });
        for (int i = 0; i < videoValues1.size(); i++) {
            VideoModel videoModel = (VideoModel) videoValues1.get(i).progress.extra1;
            videoModel.setIsVisibility("0");
        }*/
//            Log.e(getLocalClassName(), "initData: videoFiles大小:" + videoFiles.size());
            isShowNoData();
            adapter.notifyDataSetChanged();
            okDownload.addOnAllTaskEndListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取视频文件
     *
     * @param file
     * @return
     */
    private List<VideoBean> getDownloadVideoFiles(File file) {
        final List<VideoBean> list = new ArrayList<>();
        file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                String name = file.getName();
                int i = name.indexOf('.');
                if (i != -1) {
                    name = name.substring(i);
                    if (name.equalsIgnoreCase(".mp4") || name.equalsIgnoreCase(".3gp")) {
                        VideoBean video = new VideoBean();
                        long time = 0;
                        try {
                            String[] thumbColumns = {MediaStore.Video.Thumbnails.DATA,
                                    MediaStore.Video.Thumbnails.VIDEO_ID};

                            String[] mediaColumns = {MediaStore.Video.Media._ID,
                                    MediaStore.Video.Media.DATA, MediaStore.Video.Media.DURATION};

                            Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media
                                            .EXTERNAL_CONTENT_URI,
                                    mediaColumns, MediaStore.Images.Media.DISPLAY_NAME + "= ?",
                                    new String[]{file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("/") + 1)}, null);
                            if (cursor.moveToFirst()) {
                                do {
                                    //获得了视频的时长（以毫秒为单位）
                                    time = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                                } while (cursor.moveToNext());

                                if (cursor != null) {
                                    cursor.close();
                                }
                            }

                            if (time == 0) {
                                try {
                                    MediaPlayer meidaPlayer = new MediaPlayer();
                                    meidaPlayer.setDataSource(file.getPath());
                                    meidaPlayer.prepare();
                                    time = meidaPlayer.getDuration();//获得了视频的时长（以毫秒为单位）
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            video.setVideoSize(Formatter.formatFileSize(context, file.length()));
                            video.setVideoTime(FileSizeUtil.formatLongToTimeStr(time));
                            video.setVideoName(file.getName());
                            video.setVideoPath(file.getAbsolutePath());
                            if (Utils.getDownloadVedioIdMap().get(video.getVideoPath())==null){
                                Utils.getDownloadVedioIdMap().put(video.getVideoPath(),System.currentTimeMillis());
                                video.setAid(Utils.getDownloadVedioIdMap().get(video.getVideoPath())+"");
                            }


                            /*if (downloading.size() > 0) {
                                for (Progress progress : downloading) {
                                    if (!video.getVideoName().equals(progress.fileName)) {
                                        list.add(video);
                                        Log.e("CJT", "最后的大小1" + "ScannerAnsyTask---视频名称--name--" + video.getVideoName() + "\t视频时长：" + video.getVideoTime());
                                    }
                                }
                            } else */
                            if (time > 0) {
                                list.add(video);
//                                Log.e("CJT", "最后的大小2" + "ScannerAnsyTask---视频名称--name--" + video.getVideoName() + "\t视频时长：" + video.getVideoTime());
                            }

                            if (downloading.size() > 0) {
                                for (Progress progress : downloading) {
                                    if (video.getVideoName().equals(progress.fileName)) {
//                                        Log.e("CJT", "删除" + "ScannerAnsyTask---视频名称--name--" + video.getVideoName() + "\t视频时长：" + video.getVideoTime());
                                        list.remove(video);
                                    }
                                }
                            }
                            return true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    // 判断是不是目录
                } else if (file.isDirectory()) {
                    getDownloadVideoFiles(file);
                }
                return false;
            }
        });

        return list;
    }

    private void is_caching() {//是否显示进入下载中界面
        List<Progress> downloading = DownloadManager.getInstance().getDownloading();
        downloadingNew = changeValues1(downloading);
        if (downloadingNew.size() != 0) {
            ll_caching.setVisibility(View.VISIBLE);
            downloading_num.setText(downloadingNew.size() + "");//正在下载的视频个数
            tv_iscaching.setText("正在缓存(" + downloadingNew.size() + "个文件)");
            progressSize();
            setMSize();
        } else {
            ll_caching.setVisibility(View.GONE);
            tv_iscaching.setText("正在缓存");
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
        ll_caching.setVisibility(View.GONE);
        //initData();//全部任务下载完成后刷新界面
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        okDownload.removeOnAllTaskEndListener(this);
        unregisterReceiver(downloadCompleteReceiver);
        //adapter.unRegister();
    }

    @Override
    protected void onResume() {
        initData();
        //adapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn/Member/Index.html";
        super.onPause();
    }

    public void back(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.download_topRightTxt1://右上角编辑按钮
                checkNum = 0;
                values1 = OkDownload.restore(DownloadManager.getInstance().getFinished());
                videoValues1 = changeValues(values1);
                if (videoFiles.size() > 0) {
                    for (int i = 0; i < videoFiles.size(); i++) {
                        deleteVideo_tv.setTextColor(Color.parseColor("#000000"));
                        VideoBean videoBean = videoFiles.get(i);
                        if (videoBean.getCheckBoxStatus().equals("0")) {
                            videoBean.setCheckBoxStatus("1");
                            download_topRightTxt.setText("取消");
                            download_memory.setVisibility(View.GONE);
                            download_bottom.setVisibility(View.VISIBLE);
                            bottom_view.setVisibility(View.VISIBLE);
                            adapter.notifyDataSetChanged();
                        } else if (videoBean.getCheckBoxStatus().equals("1")) {
                            videoBean.setCheckBoxStatus("0");
                            download_bottom.setVisibility(View.GONE);
                            bottom_view.setVisibility(View.GONE);
                            download_topRightTxt.setText("编辑");
                            //清空选中的信息
                            for (int j = 0; j < videoFiles.size(); j++) {//清空checkbox选中的状态
                                VideoBean videoBean2 = videoFiles.get(j);
                                if (videoBean2.isChecked()) {
                                    videoBean2.setChecked(false);
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
            case R.id.ll_caching1://正在缓存条目
                startActivity(new Intent(context, Downloading2Activity.class));
                break;
//            case R.id.btn_nodata:
//
//                break;
            case R.id.selectAllCheck1://全选按钮
                values1 = OkDownload.restore(DownloadManager.getInstance().getFinished());
                videoValues1 = changeValues(values1);
                if (selectAllCheck_tv.getText().toString().equals("全选")) {
                    selectAllCheck_tv.setText("取消全选");
                    for (int i = 0; i < videoFiles.size(); i++) {
                        VideoBean videoBean = videoFiles.get(i);
                        if (!videoBean.isChecked()) {
                            videoBean.setChecked(true);
                            checkNum++;
                            delete_video.setText("删除(" + checkNum + ")");
                            delete_video.setTextColor(Color.RED);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else if (selectAllCheck_tv.getText().toString().equals("取消全选")) {
                    selectAllCheck_tv.setText("全选");
                    for (int i = 0; i < videoFiles.size(); i++) {
                        VideoBean videoBean = videoFiles.get(i);
                        if (videoBean.isChecked()) {
                            videoBean.setChecked(false);
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
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.delete_video1://删除按钮
                values1 = OkDownload.restore(DownloadManager.getInstance().getFinished());
                videoValues1 = changeValues(values1);

                Iterator<VideoBean> iterator = videoFiles.iterator();
                while (iterator.hasNext()) {
                    VideoBean videoBean = iterator.next();
                    if (videoBean.isChecked()) {
                        iterator.remove();
                        DataCleanManager.deleteFileSafely(new File(videoBean.getVideoPath()));
                    }
                }

                download_bottom.setVisibility(View.GONE);
                bottom_view.setVisibility(View.GONE);
                download_topRightTxt.setText("编辑");
                selectAllCheck_tv.setText("全选");
                values1 = OkDownload.restore(DownloadManager.getInstance().getFinished());
                videoValues1 = changeValues(values1);

                Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                initData();
                break;
        }
    }

    public void progressSize() {
        values = OkDownload.restore(DownloadManager.getInstance().getDownloading());
        videoValues = changeValues(values);
        for (int i = 0; i < videoValues.size(); i++) {
            DownloadTask task = videoValues.get(i);
            String tag = task.progress.tag;
            task.register(new DownloadingListener(tag));
        }
    }

    private class DownloadingListener extends DownloadListener {

        DownloadingListener(Object tag) {
            super(tag);
        }

        @Override
        public void onStart(Progress progress) {
        }

        @Override
        public void onProgress(Progress progress) {
            values = OkDownload.restore(DownloadManager.getInstance().getDownloading());
            videoValues = changeValues(values);
            DecimalFormat df = new DecimalFormat(".00");
            for (int i = 0; i < videoValues.size(); i++) {
//                csFirst = videoValues.get(i).progress.currentSize / 1024.0 / 1024.0;
                csFirst = videoValues.get(i).progress.currentSize;
                csAll = csAll + csFirst;
            }
            /*download_progress.setProgress((int) ((csAll / totalSize) * 100));
            download_prog.setText("已缓存" + (int) ((csAll / totalSize) * 100) + "%");*/
//            upload_down_item_size.setText(df.format(csAll) + "M/" + df.format(totalSize) + "M");
            double progress_double = (double) csAll / (double) totalSize;
//            Log.e(getLocalClassName(), "onProgress:csAll:"+csAll+"\ttotalSize:"+totalSize+" \tprogress_test:"+progress_double);
            download_progress.setProgress((int) ((progress_double) * 100));
            download_prog.setText("已缓存" + (int) ((progress_double) * 100) + "%");
            upload_down_item_size.setText(Formatter.formatFileSize(context, currentSize) + "/" + Formatter.formatFileSize(context, totalSize) + "");
            csAll = 0;

            is_caching();

        }

        @Override
        public void onError(Progress progress) {
            Throwable throwable = progress.exception;
            if (throwable != null) throwable.printStackTrace();
        }

        @Override
        public void onFinish(File file, Progress progress) {
            //下载完成通知已改到 LogDownloadListener，不用重复发送
            //showNotifictionIcon(context, progress);
            Message message = new Message();
            message.what = 1;
            message.obj = progress.filePath;
            handler.sendMessage(message);
        }

        @Override
        public void onRemove(Progress progress) {
        }
    }

    /**
     * 设置下载中的视频总大小和已下载的视频的总大小
     */
    private void setMSize() {
        DecimalFormat df = new DecimalFormat(".00");
        currentSize = 0;
        totalSize = 0;
        downloading = DownloadManager.getInstance().getDownloading();
        downloadingNew = changeValues1(downloading);
        for (int i = 0; i < downloadingNew.size(); i++) {
            /*totalSize = totalSize + (downloadingNew.get(i).totalSize / 1024.0 / 1024.0);//视频总大小
            currentSize = currentSize + (downloadingNew.get(i).currentSize / 1024.0 / 1024.0);//视频已缓存大小*/
            totalSize = totalSize + (downloadingNew.get(i).totalSize);//视频总大小
            currentSize = currentSize + (downloadingNew.get(i).currentSize);//视频已缓存大小
        }
        download_progress.setProgress((int) (((double) currentSize / (double) totalSize) * 100));

        values = OkDownload.restore(DownloadManager.getInstance().getDownloading());
        videoValues = changeValues(values);
        Progress progress = null;
        try {
            progress = videoValues.get(0).progress;
            videoModel = (VideoModel) progress.extra1;
            if (Util.isOnMainThread()) {//防止界面销毁时Glide报错闪退
                RequestOptions options = new RequestOptions().error(R.mipmap.ic_launcher);
                Glide.with(getApplication())
                        .load(videoModel.iconUrl)
                        .apply(options)
                        .into(upload_down_item_img);
            }
            first_downloading_time.setText(videoModel.getVtime());
            down_item_group_title.setText(videoModel.name.substring(0, videoModel.name.length() - 4));
            //upload_down_item_size.setText(df.format(currentSize) + "M/" + df.format(totalSize) + "M");
            upload_down_item_size.setText(Formatter.formatFileSize(context, currentSize) + "/" + Formatter.formatFileSize(context, totalSize) + "");
            download_prog.setText("已缓存" + (int) (((double) currentSize / (double) totalSize) * 100) + "%");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public static void showNotifictionIcon(Context context, Progress progress) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Intent intent = new Intent(context, DownloadFinishActivity.class);//将要跳转的界面
        builder.setAutoCancel(true);//点击后消失
        builder.setSmallIcon(R.mipmap.app_icon);//设置通知栏消息标题的头像
//        builder.setDefaults(NotificationCompat.DEFAULT_SOUND);//设置通知铃声
        //2018-03-28 增加下载完成振动提示
        builder.setDefaults(NotificationCompat.DEFAULT_VIBRATE);
        builder.setTicker("临床频道");
        builder.setContentText(progress.fileName);//通知内容
        //利用PendingIntent来包装我们的intent对象,使其延迟跳转
        PendingIntent intentPend = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(intentPend);
        NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    private List<DownloadTask> changeValues(List<DownloadTask> values) {
        List<DownloadTask> list = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {//type为video表示视频
            boolean is = values.get(i).progress.extra1 != null ? true : false;
            if (is) {
                VideoModel model = (VideoModel) values.get(i).progress.extra1;
//                return list;
                if (model.getType() == null) {
                    model.setType("video");
                }
                if (model.getType().equals("video")) {
                    list.add(values.get(i));
                }
            }

        }
        return list;
    }

    private List<Progress> changeValues1(List<Progress> download) {
        List<Progress> list = new ArrayList<>();
        for (int i = 0; i < download.size(); i++) {
            boolean is = download.get(i).extra1 != null ? true : false;
            if (is) {
                VideoModel model = (VideoModel) download.get(i).extra1;
//                return list;
                if (model.getType() == null) {
                    model.setType("video");
                }
                if (model.getType().equals("video")) {
                    list.add(download.get(i));
                }
            } else {

            }

        }
        return list;
    }


    class DownloadCompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("intent.action.my.download.complete")) {
                initData();
            }
        }
    }
}
