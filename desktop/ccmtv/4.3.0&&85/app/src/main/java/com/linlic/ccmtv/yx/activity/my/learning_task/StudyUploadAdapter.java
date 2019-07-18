package com.linlic.ccmtv.yx.activity.my.learning_task;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.util.Log;
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
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.entity.UploadModel;
import com.linlic.ccmtv.yx.activity.my.newDownload.VideoUtils;
import com.linlic.ccmtv.yx.activity.upload.LogUploadListener;

import com.linlic.ccmtv.yx.activity.upload.adapter.UploadingAdapter;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.db.UploadManager;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.base.Request;
import com.lzy.okserver.OkUpload;
import com.lzy.okserver.upload.UploadListener;
import com.lzy.okserver.upload.UploadTask;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yu on 2018/5/10.
 */

public class StudyUploadAdapter extends RecyclerView.Adapter<StudyUploadAdapter.ViewHolder> {

    public static final int TYPE_ALL = 0;
    public static final int TYPE_FINISH = 1;
    public static final int TYPE_ING = 2;
    private List<UploadTask<?>> values;
    private NumberFormat numberFormat;
    private LayoutInflater inflater;
    private Context context;
    private int type;

    public StudyUploadAdapter(Context context) {
        this.context = context;

        numberFormat = NumberFormat.getPercentInstance();
        numberFormat.setMinimumFractionDigits(2);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void updateData() {
        //这里是将数据库的数据恢复
        List<UploadTask<?>> uploadTasks = OkUpload.restore(UploadManager.getInstance().getUploading());
        try {
            values = new ArrayList<>();
            for (UploadTask<?> task : uploadTasks) {
                String tag = task.progress.tag;
                if (tag.contains("image_text_task")) {
                    values.add(task);
                }
            }

            //由于Converter是无法保存下来的，所以这里恢复任务的时候，需要额外传入Converter，否则就没法解析数据
            //至于数据类型，统一就行，不一定非要是String
            for (UploadTask<?> task1 : this.values) {
                //noinspection unchecked
                Request<String, ? extends Request> request = (Request<String, ? extends Request>) task1.progress.request;
                request.converter(new StringConvert());
            }

            notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public StudyUploadAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Log.e("StudyUploadAdapter", "onCreateViewHolder: ");
        View view = inflater.inflate(R.layout.item_study_uploading, parent, false);
        return new StudyUploadAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StudyUploadAdapter.ViewHolder holder, int position) {
        UploadTask task = values.get(position);
//        Log.e("StudyUploadAdapter", "onBindViewHolder: " + task.progress);
        if (task.progress.extra1 != null && 1 == 1) {
            String tag = createTag(task);
            task.register(new ListUploadListener(tag, holder)).register(new LogStudyUploadListener());
            holder.setTag(tag);
            holder.setTask(task);
            holder.bind();
            holder.setIsRecyclable(false);
            holder.refresh(task.progress);
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

        //@Bind(R.id.id_iv_item_uploading_name)
        TextView name;
        //@Bind(R.id.id_iv_item_uploadingSize)
        TextView downloadSize;
        //@Bind(R.id.id_iv_item_uploading_pbProgress)
//        NumberProgressBarNew pbProgress;
        ProgressBar pbProgress;
        TextView delete;

        private UploadTask task;
        private String tag;

        public ViewHolder(View itemView) {
            super(itemView);
            //ButterKnife.bind(this, itemView);
            name= (TextView) itemView.findViewById(R.id.id_iv_item_uploading_name);
            downloadSize= (TextView) itemView.findViewById(R.id.id_iv_item_uploadingSize);
            pbProgress= (ProgressBar) itemView.findViewById(R.id.id_iv_item_uploading_pbProgress);
            delete= (TextView) itemView.findViewById(R.id.task_study_detail_delete);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    task.remove();
                    updateData();
                }
            });
        }

        public void setTask(UploadTask task) {
//            Log.e("StudyUploadAdapter", "setTask: " + task.progress);
            this.task = task;
        }

        public void bind() {
            Progress progress = task.progress;
            if (progress.extra1 != null && 1 == 1) {
                UploadModel uploadModel=(UploadModel) progress.extra1;
                if (progress.filePath != null) {
                    //Glide.with(context).load(progress.filePath).error(R.mipmap.ic_launcher).into(icon);
                    name.setText(uploadModel.getName());
                } else {
                    name.setText(uploadModel.getName());
                }
            }
            refresh(progress);
        }

        public void refresh(Progress progress) {
            if (progress.extra1 != null && 1 == 1) {
                String currentSize = Formatter.formatFileSize(context, progress.currentSize);
                String totalSize = Formatter.formatFileSize(context, progress.totalSize);
                downloadSize.setText(currentSize + "/" + totalSize);
                switch (progress.status) {
                    case Progress.NONE:
                        hideUploadStatus(true);
                        break;
                    case Progress.PAUSE:
                        hideUploadStatus(false);
                        break;
                    case Progress.ERROR:
//                        Log.e("StudyUploadAdapter", "Progress.ERROR,上传出错：" + progress.exception);
                        hideUploadStatus(false);
                        break;
                    case Progress.WAITING:
                        downloadSize.setVisibility(View.GONE);
                        hideUploadStatus(true);
                        break;
                    case Progress.FINISH:
                        downloadSize.setVisibility(View.GONE);
                        break;
                    case Progress.LOADING:
                        downloadSize.setVisibility(View.VISIBLE);
                        String speed = Formatter.formatFileSize(context, progress.speed);
//                    tvProgress.setText(String.format("%s/s", speed));
                        hideUploadStatus(true);
                        break;
                }
//            netSpeed.setText("已缓存" + numberFormat.format(progress.fraction));
                pbProgress.setMax(10000);
                pbProgress.setProgress((int) (progress.fraction * 10000));
            }
        }

        private void hideUploadStatus(boolean isUpload) {
            if (isUpload) {
                pbProgress.setProgressDrawable(context.getResources().getDrawable(R.drawable.layer_list_progress_drawable));
            } else {
                pbProgress.setProgressDrawable(context.getResources().getDrawable(R.drawable.pb_upload_gray));
            }
        }

        @OnClick(R.id.remove)
        public void remove() {
            task.remove();
        }


        @OnClick(R.id.id_ll_uploading_item)
        public void start_pause() {
            if (type == TYPE_ING) {
                    Progress progress = task.progress;
//                    Log.e("StudyUploadAdapter", "start_pause: 点击之前当前状态" + progress.status);
                    switch (progress.status) {
                        case Progress.PAUSE:
//                            Log.e("StudyUploadAdapter", "start_pause: PAUSE" + task.progress);
                            task.restart();
                            break;
                        case Progress.NONE:
//                            Log.e("StudyUploadAdapter", "start_pause: NONE" + task.progress);
                            break;
                        case Progress.WAITING:
//                            Log.e("StudyUploadAdapter", "start_pause: WAITING" + task.progress);
                            task.restart();
                            break;
                        case Progress.ERROR:
//                            Log.e("StudyUploadAdapter", "start_pause: ERROR" + task.progress);
                            task.restart();
                            break;
                        case Progress.LOADING:
//                            Log.e("StudyUploadAdapter", "start_pause: LOADING" + task.progress);
                            //task.pause();
                            break;
                        case Progress.FINISH:
//                            Log.e("StudyUploadAdapter", "start_pause: FINISH" + task.progress);
                            if (VideoUtils.isAvailable(context, new File(progress.filePath))) {
                                VideoUtils.uninstall(context, VideoUtils.getPackageName(context, progress.filePath));
                            } else {
                                VideoUtils.install(context, new File(progress.filePath));
                            }
                            break;
                    }
//                    Log.e("StudyUploadAdapter", "start_pause: 点击之后当前状态" + progress.status);
                    refresh(progress);
            } else if (type == TYPE_FINISH) {

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

        private ViewHolder holder;

        ListUploadListener(Object tag, ViewHolder holder) {
            super(tag);
            this.holder = holder;
        }

        @Override
        public void onStart(Progress progress) {
//            Log.e("StudyUploadAdapter", "Progress.onStart,上传开始" + progress);
        }

        @Override
        public void onProgress(Progress progress) {
//            Log.e("StudyUploadAdapter", "Progress.onProgress,上传：" + progress);
            if (tag == holder.getTag()) {
                holder.refresh(progress);
            }
        }

        @Override
        public void onError(Progress progress) {
//            Log.e("StudyUploadAdapter", "Progress.onError,上传出错：" + progress.exception);
            Throwable throwable = progress.exception;
            if (throwable != null) throwable.printStackTrace();
        }

        @Override
        public void onFinish(Object o, Progress progress) {
//            Log.e("StudyUploadAdapter", "Progress.onFinish,上传完成：" + progress);
            Toast.makeText(context, "上传完成:" + progress.extra2, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRemove(Progress progress) {
//            Log.e("StudyUploadAdapter", "Progress.onRemove,移除上传：" + progress);
        }
    }
}