package com.linlic.ccmtv.yx.activity.upload.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.db.MyDbUtils;
import com.linlic.ccmtv.yx.activity.entity.UploadModel;
import com.linlic.ccmtv.yx.activity.my.newDownload.VideoUtils;
import com.linlic.ccmtv.yx.activity.upload.LogUploadListener;
import com.linlic.ccmtv.yx.activity.upload.VideoPlayerActivity;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.db.UploadManager;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.base.Request;
import com.lzy.okserver.OkUpload;
import com.lzy.okserver.upload.UploadListener;
import com.lzy.okserver.upload.UploadTask;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by bentley on 2018/4/17.
 */

public class UploadingAdapter extends RecyclerView.Adapter<UploadingAdapter.ViewHolder> {

    public static final int TYPE_ALL = 0;
    public static final int TYPE_FINISH = 1;
    public static final int TYPE_ING = 2;
    public UploadingAdapter uploadingAdapter;
    private List<UploadTask<?>> values1;
    private List<UploadTask<?>> values;
    private NumberFormat numberFormat;
    private LayoutInflater inflater;
    private Context context;
    private OkUpload okUpload;
    private int type;

    public UploadingAdapter(Context context) {
        this.context = context;
        numberFormat = NumberFormat.getPercentInstance();
        numberFormat.setMinimumFractionDigits(2);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        uploadingAdapter = this;
        okUpload=OkUpload.getInstance();
    }

    public void updateData(int type) {
        //这里是将数据库的数据恢复
        this.type = type;
        values = new ArrayList<>();
        if (type == TYPE_ALL)
            values1 = OkUpload.restore(UploadManager.getInstance().getAll());
        if (type == TYPE_FINISH)
            values1 = OkUpload.restore(UploadManager.getInstance().getFinished());
        if (type == TYPE_ING)
            values1 = OkUpload.restore(UploadManager.getInstance().getUploading());
        for (int i = 0; i < values1.size(); i++) {
            if (values1.get(i).progress.extra1 != null && 1 == 1) {
                values.add(values1.get(i));
            }
        }

        //由于Converter是无法保存下来的，所以这里恢复任务的时候，需要额外传入Converter，否则就没法解析数据
        //至于数据类型，统一就行，不一定非要是String
        for (UploadTask<?> task1 : values) {
            //noinspection unchecked
            Request<String, ? extends Request> request = (Request<String, ? extends Request>) task1.progress.request;
            request.converter(new StringConvert());
        }

        notifyDataSetChanged();
    }

    @Override
    public UploadingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_uploading_manager, parent, false);
        return new UploadingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UploadingAdapter.ViewHolder holder, int position) {
        UploadTask task = values.get(position);
//        Log.e("UploadingAdapter1", "onBindViewHolder: "+task.progress);
        if (task.progress.extra1 != null && 1 == 1) {
            String tag = createTag(task);
            task.register(new ListUploadListener(tag, holder)).register(new LogUploadListener());
            holder.setTag(tag);
            holder.setTask(task);
            holder.bind();
            holder.setIsRecyclable(false);
            holder.refresh(task.progress);
            //控制item的checkbox隐藏显示
            UploadModel uploadModel = (UploadModel) task.progress.extra1;
            if (uploadModel.getCheckBoxStatus().equals("1")) {
                holder.checkbox.setVisibility(View.VISIBLE);
            } else if (uploadModel.getCheckBoxStatus().equals("0")) {
                holder.checkbox.setVisibility(View.GONE);
            }
            if (uploadModel.isChecked()) {
                uploadModel.setChecked(true);
                holder.checkbox.setChecked(true);
            } else {
                uploadModel.setChecked(false);
                holder.checkbox.setChecked(false);
            }
            if (uploadModel.getIsVisibility().equals("1")) {
                holder.pbProgress.setVisibility(View.VISIBLE);
            } else {
                holder.pbProgress.setVisibility(View.GONE);
            }
        }
    }

    public void unRegister() {
        Map<String, UploadTask<?>> taskMap = OkUpload.getInstance().getTaskMap();
        for (UploadTask task : taskMap.values()) {
            task.unRegister(createTag(task));
        }
    }

    private String createTag(UploadTask task) {
        return type + "_" + task.progress.tag;
    }

    @Override
    public int getItemCount() {
        return values == null ? 0 : values.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.id_iv_item_uploading_icon)
        ImageView icon;
        @Bind(R.id.id_iv_item_uploading_name)
        TextView name;
        @Bind(R.id.id_iv_item_uploadingSize)
        TextView downloadSize;
        //        @Bind(R.id.tvProgress)
//        TextView tvProgress;
        @Bind(R.id.id_iv_item_uploading_netSpeed)
        TextView netSpeed;
        @Bind(R.id.id_iv_item_uploading_pbProgress)
//        NumberProgressBarNew pbProgress;
                ProgressBar pbProgress;
        @Bind(R.id.id_iv_item_uploading_start)
        Button download;
        @Bind(R.id.id_iv_item_uploading_checkbox)
        CheckBox checkbox;
        @Bind(R.id.id_iv_item_uploading_time)
        TextView item_time;
        @Bind(R.id.id_iv_item_uploading_aid)
        TextView download_aid;

        @Bind(R.id.id_iv_item_upload_status)
        ImageView ivUploadingStatus;
        @Bind(R.id.id_tv_item_upload_status)
        TextView tvUploadingStatus;
        @Bind(R.id.id_rl_item_uploading_status)
        RelativeLayout rlStatus;

        private UploadTask task;
        private String tag;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setTask(UploadTask task) {
//            Log.e("UploadingAdapter1", "setTask: "+task.progress);
            this.task = task;
        }

        public void bind() {
            Progress progress = task.progress;
            if (progress.extra1 != null && 1 == 1) {
                UploadModel apk = (UploadModel) progress.extra1;
                if (apk != null) {
                    RequestOptions options = new RequestOptions().error(R.mipmap.ic_launcher);
                    Glide.with(context)
                            .load(apk.iconUrl)
                            .apply(options)
                            .into(icon);
                    name.setText(apk.name);
                } else {
                    name.setText(progress.fileName);
                }
                item_time.setText(apk.getVtime());
            }
            refresh(progress);
        }

        public void refresh(Progress progress) {
            if (progress.extra1 != null && 1 == 1) {
                UploadModel um = (UploadModel) progress.extra1;
                String currentSize = Formatter.formatFileSize(context, progress.currentSize);
                String totalSize = Formatter.formatFileSize(context, progress.totalSize);
                String vtime = um.getVtime();
                long vt;
                if (!vtime.equals("")) {
//                    Log.e("vtime", vtime);
                    long second;
                    long minute;
                    long hour;
                    if (vtime.substring(vtime.length() - 2, vtime.length() - 1).equals(":")) {
                        second = Long.parseLong(vtime.substring(vtime.length() - 1, vtime.length()));
                        minute = Long.parseLong(vtime.substring(vtime.length() - 4, vtime.length() - 2));
                        hour = Long.parseLong(vtime.substring(0, vtime.length() - 6));
                    } else {
                        second = Long.parseLong(vtime.substring(vtime.length() - 2, vtime.length()));
                        minute = Long.parseLong(vtime.substring(vtime.length() - 5, vtime.length() - 3));
                        hour = Long.parseLong(vtime.substring(0, vtime.length() - 7));
                    }
                    vt = (hour * 60 * 60) + (minute * 60) + second;
//                    Log.e("smh", hour + "-" + minute + "-" + second);
                } else if (vtime.equals("00:00:00")) {
                    vt = 0;
                } else {
                    vt = 0;
                }
                downloadSize.setText(currentSize + "/" + totalSize);
                switch (progress.status) {
                    case Progress.NONE:
                        netSpeed.setText("等待上传");
                        download.setText("上传");
                        hideUploadStatus(true);
                        break;
                    case Progress.PAUSE:
                        netSpeed.setText("已暂停");
                        download.setText("继续");
                        hideUploadStatus(false);
                        break;
                    case Progress.ERROR:
                        netSpeed.setText("上传出错");
                        download.setText("出错");
//                        Log.e("UploadingAdapter", "Progress.ERROR,上传出错："+progress.exception);
                        hideUploadStatus(false);
                        break;
                    case Progress.WAITING:
                        netSpeed.setText("正在准备上传");
                        download.setText("等待");
                        downloadSize.setVisibility(View.GONE);
                        hideUploadStatus(true);
                        break;
                    case Progress.FINISH:
//                    netSpeed.setText("下载完成");
                        //showNotifictionIcon(context, progress);
                        downloadSize.setVisibility(View.GONE);

//                    pbProgress.setVisibility(View.GONE);
                        if (vt != 0) {
                            double d2 = vt;
                            if (MyDbUtils.findRecordVideo(context, um.getAid()) > 0) {
                                double d1 = MyDbUtils.findRecordVideo(context, um.getAid());
                                DecimalFormat df = new DecimalFormat("#00");
                                String s = String.valueOf(df.format((d1 / d2) * 100).charAt(0));
                                if (s.equals("0")) {
                                    netSpeed.setText("已在手机看至" + df.format((d1 / d2) * 100).substring(1) + "%" + "  |  " + totalSize + "M");
                                } else {
                                    netSpeed.setText("已在手机看至" + df.format((d1 / d2) * 100) + "%" + "  |  " + totalSize + "M");
                                }
                            } else {
                                if (vt != 0) {
                                    double d1 = um.getLast_look_time();
                                    DecimalFormat df = new DecimalFormat("#00");
                                    String s = String.valueOf(df.format((d1 / d2) * 100).charAt(0));
                                    if (s.equals("0")) {
                                        netSpeed.setText("已在手机看至" + df.format((d1 / d2) * 100).substring(1) + "%" + "  |  " + totalSize + "M");
                                    } else {
                                        netSpeed.setText("已在手机看至" + df.format((d1 / d2) * 100) + "%" + "  |  " + totalSize + "M");
                                    }
                                    /*Log.e("islooked3", s);
                                    Log.e("islooked1", vm.getLast_look_time() + "");
                                    Log.e("islooked2", vt + "");*/
                                    download.setText("完成");
                                } else {
                                    netSpeed.setText("已在手机看至0%" + "  |  " + totalSize + "M");
                                }
                            }

                        } else {
                            netSpeed.setText("");
                        }

                        break;
                    case Progress.LOADING:
                        downloadSize.setVisibility(View.VISIBLE);
                        netSpeed.setText("已上传" + numberFormat.format(progress.fraction));
                        String speed = Formatter.formatFileSize(context, progress.speed);
//                    tvProgress.setText(String.format("%s/s", speed));
                        download.setText("暂停");
                        hideUploadStatus(true);
                        break;
                }
//            netSpeed.setText("已缓存" + numberFormat.format(progress.fraction));
                pbProgress.setMax(10000);
                pbProgress.setProgress((int) (progress.fraction * 10000));
            }
        }

        private void hideUploadStatus(boolean isUpload) {
            if (isUpload){
                rlStatus.setVisibility(View.GONE);
                ivUploadingStatus.setImageResource(R.mipmap.ic_upload_pause);
                tvUploadingStatus.setText("暂停");
                pbProgress.setProgressDrawable(context.getResources().getDrawable(R.drawable.layer_list_progress_drawable));
            }else {
                rlStatus.setVisibility(View.VISIBLE);
                ivUploadingStatus.setImageResource(R.mipmap.ic_upload_upload);
                tvUploadingStatus.setText("重新上传");
                pbProgress.setProgressDrawable(context.getResources().getDrawable(R.drawable.pb_upload_gray));
            }
        }

        /*@OnClick(R.id.start)
        public void start() {
            Progress progress = task.progress;
            switch (progress.status) {
                case Progress.PAUSE:
                case Progress.NONE:
                case Progress.ERROR:
                    task.start();
                    break;
                case Progress.LOADING:
                    task.pause();
                    break;
                case Progress.FINISH:
                    if (VideoUtils.isAvailable(context, new File(progress.filePath))) {
                        VideoUtils.uninstall(context, VideoUtils.getPackageName(context, progress.filePath));
                    } else {
                        VideoUtils.install(context, new File(progress.filePath));
                    }
                    break;
            }
            refresh(progress);
        }*/

        @OnClick(R.id.remove)
        public void remove() {
            task.remove();
            updateData(type);
        }



        @OnClick(R.id.id_ll_uploading_item)
        public void start_pause() {
            if (type == TYPE_ING) {
                if (checkbox.getVisibility() == View.GONE) {
                    Progress progress = task.progress;
//                    Log.e("UploadingAdapter1", "start_pause: 点击之前当前状态"+progress.status);
                    switch (progress.status) {
                        case Progress.PAUSE:
//                            Log.e("UploadingAdapter1", "start_pause: PAUSE"+task.progress);
                            task.restart();
                            break;
                        case Progress.NONE:
//                            Log.e("UploadingAdapter1", "start_pause: NONE"+task.progress);
                            break;
                        case Progress.WAITING:
//                            Log.e("UploadingAdapter1", "start_pause: WAITING"+task.progress);
                            task.restart();
                            break;
                        case Progress.ERROR:
//                            Log.e("UploadingAdapter1", "start_pause: ERROR"+task.progress);
                            task.restart();
                            break;
                        case Progress.LOADING:
//                            Log.e("UploadingAdapter1", "start_pause: LOADING"+task.progress);
                            //task.pause();
                            break;
                        case Progress.FINISH:
//                            Log.e("UploadingAdapter1", "start_pause: FINISH"+task.progress);
                            if (VideoUtils.isAvailable(context, new File(progress.filePath))) {
                                VideoUtils.uninstall(context, VideoUtils.getPackageName(context, progress.filePath));
                            } else {
                                VideoUtils.install(context, new File(progress.filePath));
                            }
                            break;
                    }
//                    Log.e("UploadingAdapter1", "start_pause: 点击之后当前状态"+progress.status);
                    refresh(progress);
                } else if (checkbox.getVisibility() == View.VISIBLE) {
                    Progress progress = task.progress;
                    UploadModel uploadModel = (UploadModel) progress.extra1;
                    if (!uploadModel.isChecked()) {
                        uploadModel.setChecked(true);
                        OnItemClickListener.plusNum();
                    } else if (uploadModel.isChecked()) {
                        uploadModel.setChecked(false);
                        OnItemClickListener.downNum();
                    }
                    notifyDataSetChanged();
                }
            } else if (type == TYPE_FINISH) {
                if (checkbox.getVisibility() == View.GONE) {//进入本地视频播放页
                    Progress progress = task.progress;
                    UploadModel um = (UploadModel) progress.extra1;
                    Bundle bundle = new Bundle();
                    String path1 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ccmtvCache/video/" + um.getName();
                    Intent intent = new Intent(context, VideoPlayerActivity.class);
                    intent.putExtra("videoTitle", name.getText().toString());//传的videoTitle不带后缀
                    intent.putExtra("videoPath", path1);
                    intent.putExtra("last_look_time", um.getLast_look_time());
                    intent.putExtra("aid", um.getAid());
//                    Log.e("last_look_time", um.getLast_look_time() + "");
                    context.startActivity(intent);
                    refresh(progress);
                } else if (checkbox.getVisibility() == View.VISIBLE) {//控制item的checkbox影藏显示
                    Progress progress = task.progress;
                    UploadModel uploadModel = (UploadModel) progress.extra1;
                    if (!uploadModel.isChecked()) {
                        uploadModel.setChecked(true);
                        OnItemClickListener.plusNum();
                    } else if (uploadModel.isChecked()) {
                        uploadModel.setChecked(false);
                        OnItemClickListener.downNum();
                    }
                    notifyDataSetChanged();
                }
            }
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getTag() {
            return tag;
        }
    }


    public UploadingAdapter.OnItemClickListener OnItemClickListener;

    public void OnAddItemClickListener(UploadingAdapter.OnItemClickListener lis) {
        OnItemClickListener = lis;
    }

    public interface OnItemClickListener {
        void plusNum();

        void downNum();
    }

    private class ListUploadListener extends UploadListener {

        private UploadingAdapter.ViewHolder holder;

        ListUploadListener(Object tag, UploadingAdapter.ViewHolder holder) {
            super(tag);
            this.holder = holder;
        }

        @Override
        public void onStart(Progress progress) {
//            Log.e("UploadingAdapter", "Progress.onStart,上传开始"+progress);
        }

        @Override
        public void onProgress(Progress progress) {
//            Log.e("UploadingAdapter", "Progress.onProgress,上传："+progress);
            if (tag == holder.getTag()) {
                holder.refresh(progress);
            }
        }

        @Override
        public void onError(Progress progress) {
//            Log.e("UploadingAdapter", "Progress.onError,上传出错："+progress.exception);
            Throwable throwable = progress.exception;
            if (throwable != null) throwable.printStackTrace();
        }

        @Override
        public void onFinish(Object o, Progress progress) {
//            Log.e("UploadingAdapter", "Progress.onFinish,上传完成："+progress);
            Toast.makeText(context, "上传完成:" + progress.extra2, Toast.LENGTH_SHORT).show();
            updateData(type);
        }

        @Override
        public void onRemove(Progress progress) {
//            Log.e("UploadingAdapter", "Progress.onRemove,移除上传："+progress);
        }
    }
}
