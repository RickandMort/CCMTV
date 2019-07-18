package com.linlic.ccmtv.yx.activity.upload;

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
import com.linlic.ccmtv.yx.activity.entity.UploadModel;
import com.linlic.ccmtv.yx.activity.upload.adapter.UploadingAdapter;
import com.linlic.ccmtv.yx.utils.FileSizeUtil;
import com.linlic.ccmtv.yx.utils.SDCardUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.getSdcardPath;
import com.lzy.okgo.db.UploadManager;
import com.lzy.okgo.model.Progress;
import com.lzy.okserver.OkUpload;
import com.lzy.okserver.task.XExecutor;
import com.lzy.okserver.upload.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class UploadingActivity extends BaseActivity implements XExecutor.OnAllTaskEndListener, View.OnClickListener {

    private UploadingAdapter adapter;
    private OkUpload okUpload;
    private RecyclerView recyclerView;
    private Context context;
    private TextView activity_title_name, upload_topRightTxt, tv_pause, delete_video;
    private LinearLayout start_pause, upload_bottom;
    //内存信息
    private TextView memory_tv;
    private ProgressBar memory_progress;
    private RelativeLayout upload_memory;
    private List<UploadTask<?>> values;
    private List<UploadTask<?>> videoValues = new ArrayList<>();
    //下载中progress信息
    List<Progress> uploading = UploadManager.getInstance().getUploading();
    List<Progress> uploadingNew = new ArrayList<>();
    private TextView deleteVideo_tv;//删除按钮
    private TextView selectAllCheck_tv;//全选按钮
    private UploadModel uploadModel;
    private UploadTask uploadTask;
    private int checkNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消屏幕旋转
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_uploading);
        context = this;
        initView();
        initData();

        memoryUtils();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.id_uploading_recyclerView);
        activity_title_name = (TextView) findViewById(R.id.activity_title_name);
        upload_topRightTxt = (TextView) findViewById(R.id.id_upload_topRightTxt2);
        memory_tv = (TextView) findViewById(R.id.memory_tv);
        memory_progress = (ProgressBar) findViewById(R.id.memory_progress);
        upload_memory = (RelativeLayout) findViewById(R.id.download_memory);
        start_pause = (LinearLayout) findViewById(R.id.id_ll_upload_start_pause);
        tv_pause = (TextView) findViewById(R.id.id_tv_upload_pause);
        upload_bottom = (LinearLayout) findViewById(R.id.id_upload_bottom2);
        deleteVideo_tv = (TextView) findViewById(R.id.id_upload_delete_video2);
        selectAllCheck_tv = (TextView) findViewById(R.id.id_upload_selectAllCheck2);
        delete_video = (TextView) findViewById(R.id.id_upload_delete_video2);

        start_pause.setOnClickListener(this);
        upload_topRightTxt.setOnClickListener(this);
        selectAllCheck_tv.setOnClickListener(this);
        deleteVideo_tv.setOnClickListener(this);
    }

    private void initData() {
        activity_title_name.setText("正在上传");
        values = OkUpload.restore(UploadManager.getInstance().getUploading());
        videoValues = values;

        okUpload = OkUpload.getInstance();
        adapter = new UploadingAdapter(this);
        adapter.updateData(UploadingAdapter.TYPE_ING);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.OnAddItemClickListener(new UploadingAdapter.OnItemClickListener() {
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
            uploadTask = videoValues.get(i);
            UploadModel uploadModel = (UploadModel) videoValues.get(i).progress.extra1;
            uploadModel.setIsVisibility("1");

            /*------------------初始化时使所有item都处于未选中状态-----------------------*/
            if (uploadModel.isChecked()) {
                uploadModel.setChecked(false);
            }
            uploadModel.setCheckBoxStatus("0");
            /*------------------初始化时使所有item都处于未选中状态-----------------------*/

        }
        adapter.notifyDataSetChanged();
        okUpload.addOnAllTaskEndListener(this);
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
        okUpload.removeOnAllTaskEndListener(this);
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
        values = OkUpload.restore(UploadManager.getInstance().getUploading());
        videoValues = values;
        for (int i = 0; i < videoValues.size(); i++) {
            UploadModel uploadModel = (UploadModel) videoValues.get(i).progress.extra1;
            if (uploadModel.isChecked()) {
                uploadModel.setChecked(false);
            }
            uploadModel.setCheckBoxStatus("0");
        }
        finish();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        uploadModel = (UploadModel) uploadTask.progress.extra1;
        switch (v.getId()) {
            case R.id.id_ll_upload_start_pause:
                okUpload.pauseAll();
                break;
            case R.id.id_upload_topRightTxt2://右上角编辑按钮
                checkNum = 0;
                //okUpload.pauseAll();
                values = OkUpload.restore(UploadManager.getInstance().getUploading());
                videoValues = values;
                if (videoValues.size() > 0) {
                    deleteVideo_tv.setTextColor(Color.parseColor("#000000"));
                    for (int i = 0; i < videoValues.size(); i++) {
                        uploadTask = videoValues.get(i);
                        uploadModel = (UploadModel) uploadTask.progress.extra1;
                        uploading = UploadManager.getInstance().getUploading();
                        uploadingNew = uploading;
                        if (uploadModel.getCheckBoxStatus().equals("0")) {
                            uploadModel.setCheckBoxStatus("1");
                            upload_topRightTxt.setText("取消");
                            upload_memory.setVisibility(View.GONE);
                            upload_bottom.setVisibility(View.VISIBLE);
                            adapter.notifyDataSetChanged();
                        } else if (uploadModel.getCheckBoxStatus().equals("1")) {
                            uploadModel.setCheckBoxStatus("0");
                            upload_bottom.setVisibility(View.GONE);
                            upload_topRightTxt.setText("编辑");
                            //清空选中的信息
                            for (int j = 0; j < videoValues.size(); j++) {//清空checkbox选中的状态
                                UploadModel uploadModel = (UploadModel) videoValues.get(i).progress.extra1;
                                if (uploadModel.isChecked()) {
                                    uploadModel.setChecked(false);
                                }
                            }
                            selectAllCheck_tv.setText("全选");
                            deleteVideo_tv.setText("删除");
                            upload_memory.setVisibility(View.VISIBLE);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
                break;
            case R.id.id_upload_selectAllCheck2://全选按钮
                values = OkUpload.restore(UploadManager.getInstance().getUploading());
                videoValues = values;
                if (selectAllCheck_tv.getText().toString().equals("全选")) {
                    selectAllCheck_tv.setText("取消全选");
                    for (int i = 0; i < videoValues.size(); i++) {
                        UploadModel uploadModel = (UploadModel) videoValues.get(i).progress.extra1;
                        if (!uploadModel.isChecked()) {
                            uploadModel.setChecked(true);
                            checkNum++;
                            delete_video.setText("删除(" + checkNum + ")");
                            delete_video.setTextColor(Color.RED);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else if (selectAllCheck_tv.getText().toString().equals("取消全选")) {
                    selectAllCheck_tv.setText("全选");
                    for (int i = 0; i < videoValues.size(); i++) {
                        UploadModel uploadModel = (UploadModel) videoValues.get(i).progress.extra1;
                        if (uploadModel.isChecked()) {
                            uploadModel.setChecked(false);
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
            case R.id.id_upload_delete_video2://删除按钮
                values = OkUpload.restore(UploadManager.getInstance().getUploading());
                videoValues = values;
                for (int i = 0; i < videoValues.size(); i++) {
                    UploadModel uploadModel = (UploadModel) videoValues.get(i).progress.extra1;
                    if (uploadModel.isChecked()) {
                        videoValues.get(i).remove();
                    }
                }
                upload_bottom.setVisibility(View.GONE);
                upload_topRightTxt.setText("编辑");
                values = OkUpload.restore(UploadManager.getInstance().getUploading());
                videoValues = values;
                for (int i = 0; i < videoValues.size(); i++) {
                    uploadTask = videoValues.get(i);
                    uploadModel = (UploadModel) uploadTask.progress.extra1;
                    uploadModel.setCheckBoxStatus("0");
                    adapter.notifyDataSetChanged();
                }
                Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                initData();
                uploading = UploadManager.getInstance().getUploading();
                uploadingNew = uploading;
                if (uploadingNew.size() == 0) {
                    finish();
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            values = OkUpload.restore(UploadManager.getInstance().getUploading());
            videoValues = values;
            for (int i = 0; i < videoValues.size(); i++) {
                UploadModel uploadModel = (UploadModel) videoValues.get(i).progress.extra1;
                if (uploadModel.isChecked()) {
                    uploadModel.setChecked(false);
                }
                uploadModel.setCheckBoxStatus("0");
            }
            finish();
            adapter.notifyDataSetChanged();
        }
        return super.onKeyDown(keyCode, event);
    }
}
