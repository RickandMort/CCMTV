package com.linlic.ccmtv.yx.activity.upload.adapter;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.entity.UploadModel;
import com.linlic.ccmtv.yx.activity.entity.UploadedBean;
import com.lzy.okgo.db.UploadManager;
import com.lzy.okgo.model.Progress;
import com.lzy.okserver.OkUpload;
import com.lzy.okserver.upload.UploadListener;
import com.lzy.okserver.upload.UploadTask;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yu on 2018/4/16.
 */

public class UploadAdapterNew extends RecyclerView.Adapter<UploadAdapterNew.ViewHolder> {

    public static final int TYPE_ALL = 0;
    public static final int TYPE_FINISH = 1;
    public static final int TYPE_ING = 2;
    public UploadAdapterNew uploadAdapterNew;
    private List<UploadTask<?>> values1;
    private List<UploadTask<?>> values;
    private List<UploadedBean> uploadedBeanList;
    private NumberFormat numberFormat;
    private LayoutInflater inflater;
    private Context context;
    private int type;

    public UploadAdapterNew(Context context,List<UploadedBean> uploadedBeanList) {
        this.context = context;
        this.uploadedBeanList=uploadedBeanList;
        numberFormat = NumberFormat.getPercentInstance();
        numberFormat.setMinimumFractionDigits(2);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        uploadAdapterNew = this;
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
        notifyDataSetChanged();
    }

    @Override
    public UploadAdapterNew.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_upload_manager, parent, false);
        return new UploadAdapterNew.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UploadAdapterNew.ViewHolder holder, final int position) {
        holder.bind(position);

        if(OnItemClickListener!= null){
            holder.llUploadItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnItemClickListener.onClick(position);
                }
            });
            holder.llUploadItem.setOnLongClickListener( new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    OnItemClickListener.onLongClick(position);
                    return false;
                }
            });
        }
        /*UploadTask task = values.get(position);
        if (task.progress.extra1 != null && 1 == 1) {
            String tag = createTag(task);
            task.register(new UploadAdapterNew.ListUploadListener(tag, holder)).register(new LogUploadListener(context));
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
            *//*if (uploadModel.getIsVisibility().equals("1")) {
                holder.pbProgress.setVisibility(View.VISIBLE);
            } else {
                holder.pbProgress.setVisibility(View.GONE);
            }*//*
        }*/
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
        //return values == null ? 0 : values.size();
        return uploadedBeanList == null ? 0 : uploadedBeanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.id_ll_uploaded_item)
        LinearLayout llUploadItem;
        @Bind(R.id.icon)
        ImageView icon;
        @Bind(R.id.name)
        TextView name;
        //        @Bind(R.id.tvProgress)
//        TextView tvProgress;
        /*@Bind(R.id.netSpeed)
        TextView netSpeed;
        @Bind(R.id.pbProgress)
//        NumberProgressBarNew pbProgress;
                ProgressBar pbProgress;*/
        /*@Bind(R.id.id_iv_item_upload_type)
        ImageView ivUploadType;*/
        @Bind(R.id.id_tv_item_upload_type)
        TextView tvUploadType;
        @Bind(R.id.id_tv_item_upload_time)
        TextView tvUploadtime;
        @Bind(R.id.id_iv_item_upload_verify_status)
        ImageView ivUploadVerifyStatus;
        @Bind(R.id.id_tv_item_upload_verify_status)
        TextView tvUploadVerifyStatus;
        @Bind(R.id.start)
        Button download;
        @Bind(R.id.download_checkbox)
        CheckBox checkbox;
        @Bind(R.id.item_time)
        TextView item_time;
        @Bind(R.id.download_aid)
        TextView download_aid;
        private UploadTask task;
        private String tag;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setTask(UploadTask task) {
            this.task = task;
        }

        public void bind(int position) {
            UploadedBean uploadedBean=uploadedBeanList.get(position);
            if (uploadedBean!=null){
                name.setText(uploadedBean.getMvtitle());
                if (uploadedBean.getMvstatus().equals("1")){
                    ivUploadVerifyStatus.setImageResource(R.mipmap.ic_upload_verifying);
                    tvUploadVerifyStatus.setText("审核中");
                }else if (uploadedBean.getMvstatus().equals("2")){
                    ivUploadVerifyStatus.setImageResource(R.mipmap.ic_upload_verifying);
                    tvUploadVerifyStatus.setText("已审核");
                }else {
                    ivUploadVerifyStatus.setImageResource(R.mipmap.ic_upload_verify_failed);
                    tvUploadVerifyStatus.setText("审核失败");
                }
                tvUploadtime.setText(uploadedBean.getRow_add_time().substring(0,10));
                RequestOptions options = new RequestOptions().error(R.mipmap.img_default);
                if (uploadedBean.getStyletype().equals("video")){
                    Glide.with(context)
                            .load(uploadedBean.getImgurl())
                            .apply(options)
                            .into(icon);
                    tvUploadType.setText("视频");
                    item_time.setText(uploadedBean.getVtime());
                }else {
                    Glide.with(context).load(R.mipmap.ic_upload_case_pic).apply(options).into(icon);
                    tvUploadType.setText("病例");
                    item_time.setText(uploadedBean.getVtime()+"");
                }
            }

            /*Progress progress = task.progress;
            if (progress.extra1 != null && 1 == 1) {
                UploadModel apk = (UploadModel) progress.extra1;
                if (apk != null) {
                    Glide.with(context).load(apk.iconUrl).error(R.mipmap.ic_launcher).into(icon);
                    name.setText(apk.name);
                } else {
                    name.setText(progress.fileName);
                }
                if (apk.getType().equals("case")){
                    item_time.setText(apk.getPicCount()+"图");
                }else {
                    item_time.setText(apk.getVtime());
                }
            }*/
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
                switch (progress.status) {
                    case Progress.NONE:
                        //netSpeed.setText("等待缓存");
                        download.setText("下载");
                        break;
                    case Progress.PAUSE:
                        //netSpeed.setText("已暂停");
                        download.setText("继续");
                        break;
                    case Progress.ERROR:
                        //netSpeed.setText("下载出错");
                        download.setText("出错");
                        break;
                    case Progress.WAITING:
                        //netSpeed.setText("等待中");
                        download.setText("等待");
                        break;
                    case Progress.FINISH:
//                    netSpeed.setText("下载完成");
                        //showNotifictionIcon(context, progress);

//                    pbProgress.setVisibility(View.GONE);
                        /*if (vt != 0) {
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
                                    *//*Log.e("islooked3", s);
                                    Log.e("islooked1", vm.getLast_look_time() + "");
                                    Log.e("islooked2", vt + "");*//*
                                    download.setText("完成");
                                } else {
                                    netSpeed.setText("已在手机看至0%" + "  |  " + totalSize + "M");
                                }
                            }

                        } else {
                            netSpeed.setText("");
                        }*/

                        break;
                    case Progress.LOADING:
                        //netSpeed.setText("已缓存" + numberFormat.format(progress.fraction));
                        String speed = Formatter.formatFileSize(context, progress.speed);
//                    tvProgress.setText(String.format("%s/s", speed));
                        download.setText("暂停");
                        break;
                }
//            netSpeed.setText("已缓存" + numberFormat.format(progress.fraction));
                /*pbProgress.setMax(10000);
                pbProgress.setProgress((int) (progress.fraction * 10000));*/
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

        @OnClick(R.id.restart)
        public void restart() {
            task.restart();
        }

        @OnClick(R.id.id_ll_uploaded_item)
        public void start_pause() {
            /*if (type == TYPE_ING) {
                if (checkbox.getVisibility() == View.GONE) {
                    Progress progress = task.progress;
                    switch (progress.status) {
                        case Progress.PAUSE:
                        case Progress.NONE:
                        case Progress.WAITING:
                            task.pause();
                            task.start();
                            break;
                        case Progress.ERROR:
                            task.restart();
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
                    UploadModel vm = (UploadModel) progress.extra1;
                    Bundle bundle = new Bundle();
                    String path1 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ccmtvCache/video/" + vm.getName();
                    Intent intent = new Intent(context, VideoPlayerActivity.class);
                    intent.putExtra("videoTitle", name.getText().toString());//传的videoTitle不带后缀
                    intent.putExtra("videoPath", path1);
                    intent.putExtra("last_look_time", vm.getLast_look_time());
                    intent.putExtra("aid", vm.getAid());
                    Log.e("last_look_time", vm.getLast_look_time() + "");
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
            }*/
        }


        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getTag() {
            return tag;
        }
    }


    public OnItemClickListener OnItemClickListener;

    /*public void OnAddItemClickListener(UploadAdapterNew.OnItemClickListener lis) {
        OnItemClickListener = lis;
    }*/

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this. OnItemClickListener=onItemClickListener;
    }

    public interface OnItemClickListener {
        /*void plusNum();

        void downNum();*/

        void onClick(int position);

        void onLongClick(int position);
    }

    private class ListUploadListener extends UploadListener {

        private UploadAdapterNew.ViewHolder holder;

        ListUploadListener(Object tag, UploadAdapterNew.ViewHolder holder) {
            super(tag);
            this.holder = holder;
        }

        @Override
        public void onStart(Progress progress) {
        }

        @Override
        public void onProgress(Progress progress) {
            if (tag == holder.getTag()) {
                holder.refresh(progress);
            }
        }

        @Override
        public void onError(Progress progress) {
            Throwable throwable = progress.exception;
            if (throwable != null) throwable.printStackTrace();
        }

        @Override
        public void onFinish(Object o, Progress progress) {
            Toast.makeText(context, "下载完成:" + progress.fileName, Toast.LENGTH_SHORT).show();
            updateData(type);
        }

        @Override
        public void onRemove(Progress progress) {
        }
    }

    public static void showNotifictionIcon(Context context, Progress progress) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Intent intent = new Intent(context, MainActivity.class);//将要跳转的界面
        builder.setAutoCancel(true);//点击后消失
        builder.setSmallIcon(R.mipmap.app_icon);//设置通知栏消息标题的头像
        builder.setDefaults(NotificationCompat.DEFAULT_VIBRATE);//设置震动
        builder.setTicker("临床频道");
        builder.setContentText(progress.fileName + "下载完成！");//通知内容
        //利用PendingIntent来包装我们的intent对象,使其延迟跳转
        PendingIntent intentPend = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(intentPend);
        NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}

