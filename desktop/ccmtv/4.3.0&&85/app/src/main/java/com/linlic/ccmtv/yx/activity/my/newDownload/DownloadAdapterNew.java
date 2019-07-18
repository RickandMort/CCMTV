/*
 * Copyright 2016 jeasonlzy(廖子尧)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.linlic.ccmtv.yx.activity.my.newDownload;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.db.MyDbUtils;
import com.linlic.ccmtv.yx.activity.entity.VideoModel;
import com.linlic.ccmtv.yx.activity.upload.VideoPlayerActivity;
import com.lzy.okgo.db.DownloadManager;
import com.lzy.okgo.model.Progress;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DownloadAdapterNew extends RecyclerView.Adapter<DownloadAdapterNew.ViewHolder> {

    public static final int TYPE_ALL = 0;
    public static final int TYPE_FINISH = 1;
    public static final int TYPE_ING = 2;
    public DownloadAdapterNew downloadAdapterNew;
    private List<DownloadTask> values1;
    private List<DownloadTask> values;
    private NumberFormat numberFormat;
    private LayoutInflater inflater;
    private Context context;
    private int type;
    Handler handler = new Handler();

    public DownloadAdapterNew(Context context) {
        this.context = context;
        numberFormat = NumberFormat.getPercentInstance();
        numberFormat.setMinimumFractionDigits(2);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        downloadAdapterNew = this;
    }

    public void updateData(int type) {
        //这里是将数据库的数据恢复
        this.type = type;
        values = new ArrayList<>();
        if (type == TYPE_ALL)
            values1 = OkDownload.restore(DownloadManager.getInstance().getAll());
        if (type == TYPE_FINISH)
            values1 = OkDownload.restore(DownloadManager.getInstance().getFinished());
        if (type == TYPE_ING)
            values1 = OkDownload.restore(DownloadManager.getInstance().getDownloading());
        for (int i = 0; i < values1.size(); i++) {
            if (values1.get(i).progress.extra1 != null && 1 == 1) {
                values.add(values1.get(i));
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_download_manager, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DownloadTask task = values.get(position);
        if (task.progress.extra1 != null && 1 == 1) {
            String tag = createTag(task);
            task.register(new ListDownloadListener(tag, holder)).register(new LogDownloadListener());
            holder.setTag(tag);
            holder.setTask(task);
            holder.bind();
            holder.setIsRecyclable(false);
            holder.refresh(task.progress);
            //控制item的checkbox隐藏显示
            VideoModel videoModel = (VideoModel) task.progress.extra1;
            if (videoModel.getCheckBoxStatus().equals("1")) {
                holder.checkbox.setVisibility(View.VISIBLE);
            } else if (videoModel.getCheckBoxStatus().equals("0")) {
                holder.checkbox.setVisibility(View.GONE);
            }
            if (videoModel.isChecked()) {
                videoModel.setChecked(true);
                holder.checkbox.setChecked(true);
            } else {
                videoModel.setChecked(false);
                holder.checkbox.setChecked(false);
            }
            if (videoModel.getIsVisibility().equals("1")) {
                holder.pbProgress.setVisibility(View.VISIBLE);
            } else {
                holder.pbProgress.setVisibility(View.GONE);
            }
        }
    }

    public void unRegister() {
        Map<String, DownloadTask> taskMap = OkDownload.getInstance().getTaskMap();
        for (DownloadTask task : taskMap.values()) {
            task.unRegister(createTag(task));
        }
    }

    private String createTag(DownloadTask task) {
        return type + "_" + task.progress.tag;
    }

    @Override
    public int getItemCount() {
        return values == null ? 0 : values.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.icon)
        ImageView icon;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.downloadSize)
        TextView downloadSize;
        //        @Bind(R.id.tvProgress)
//        TextView tvProgress;
        @Bind(R.id.netSpeed)
        TextView netSpeed;
        @Bind(R.id.pbProgress)
//        NumberProgressBarNew pbProgress;
        ProgressBar pbProgress;
        @Bind(R.id.start)
        Button download;
        @Bind(R.id.download_checkbox)
        CheckBox checkbox;
        @Bind(R.id.item_time)
        TextView item_time;
        @Bind(R.id.download_aid)
        TextView download_aid;
        @Bind(R.id.ll_item)
        LinearLayout ll_item;
        private DownloadTask task;
        private String tag;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setTask(DownloadTask task) {
            this.task = task;
        }

        public void bind() {
            Progress progress = task.progress;
            if (progress.extra1 != null && 1 == 1) {
                VideoModel apk = (VideoModel) progress.extra1;
                if (apk != null) {
                    RequestOptions options = new RequestOptions().error(R.mipmap.ic_launcher);
                    Glide.with(context)
                            .load(apk.iconUrl)
                            .apply(options)
                            .into(icon);
                    name.setText(apk.name.substring(0, apk.name.length() - 4));
                } else {
                    name.setText(progress.fileName);
                }
                item_time.setText(apk.getVtime());
            }
        }

        public void refresh(Progress progress) {
            if (progress.extra1 != null && 1 == 1) {
                VideoModel vm = (VideoModel) progress.extra1;
                String currentSize = Formatter.formatFileSize(context, progress.currentSize);
                String totalSize = Formatter.formatFileSize(context, progress.totalSize);
                String vtime = vm.getVtime();
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
                        netSpeed.setText("等待缓存");
                        download.setText("下载");
                        break;
                    case Progress.PAUSE:
                        netSpeed.setText("已暂停");
                        download.setText("继续");
                        break;
                    case Progress.ERROR:
                        netSpeed.setText("下载出错");
                        download.setText("出错");
                        break;
                    case Progress.WAITING:
                        netSpeed.setText("等待中");
                        download.setText("等待");
                        downloadSize.setVisibility(View.GONE);
                        break;
                    case Progress.FINISH:
//                    netSpeed.setText("下载完成");
                        //showNotifictionIcon(context, progress);
                        downloadSize.setVisibility(View.GONE);

//                    pbProgress.setVisibility(View.GONE);
                        if (vt != 0) {
                            double d2 = vt;
                            if (MyDbUtils.findRecordVideo(context, vm.getAid()) > 0) {
                                double d1 = MyDbUtils.findRecordVideo(context, vm.getAid());
                                DecimalFormat df = new DecimalFormat("#00");
                                String s = String.valueOf(df.format((d1 / d2) * 100).charAt(0));
                                if (s.equals("0")) {
                                    netSpeed.setText("已在手机看至" + df.format((d1 / d2) * 100).substring(1) + "%" + "  |  " + totalSize + "M");
                                } else {
                                    netSpeed.setText("已在手机看至" + df.format((d1 / d2) * 100) + "%" + "  |  " + totalSize + "M");
                                }
                            } else {
                                if (vt != 0) {
                                    double d1 = vm.getLast_look_time();
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
                        netSpeed.setText("已缓存" + numberFormat.format(progress.fraction));
                        String speed = Formatter.formatFileSize(context, progress.speed);
//                    tvProgress.setText(String.format("%s/s", speed));
                        download.setText("暂停");
                        break;
                }
//            netSpeed.setText("已缓存" + numberFormat.format(progress.fraction));
                pbProgress.setMax(10000);
                pbProgress.setProgress((int) (progress.fraction * 10000));
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
            task.remove(true);
            updateData(type);
        }

        @OnClick(R.id.restart)
        public void restart() {
            task.restart();
        }

        @OnClick(R.id.ll_item)
        public void start_pause() {
            if (type == TYPE_ING) {
//                Log.e("下载点击", "start_pause: ");
                ll_item.setClickable(false);
                setClickableTrue();
                if (checkbox.getVisibility() == View.GONE) {
                    Progress progress = task.progress;
                    try {
                        switch (progress.status) {
                            case Progress.PAUSE:
                            case Progress.NONE:
                            case Progress.WAITING:
                                task.pause();
                                task.unRegister(progress.tag);
                                task.start();
                                Toast.makeText(context, "正在为您继续下载", Toast.LENGTH_SHORT).show();
                                break;
                            case Progress.ERROR:
                                task.unRegister(progress.tag);
                                task.restart();
                                Toast.makeText(context, "正在为您继续下载", Toast.LENGTH_SHORT).show();
                                break;
                            case Progress.LOADING:
                                task.pause();
                                task.unRegister(progress.tag);
                                Toast.makeText(context, "正在为您暂停", Toast.LENGTH_SHORT).show();
                                break;
                            case Progress.FINISH:
                                if (VideoUtils.isAvailable(context, new File(progress.filePath))) {
                                    VideoUtils.uninstall(context, VideoUtils.getPackageName(context, progress.filePath));
                                } else {
                                    VideoUtils.install(context, new File(progress.filePath));
                                }
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    refresh(progress);
                } else if (checkbox.getVisibility() == View.VISIBLE) {
                    Progress progress = task.progress;
                    VideoModel videoModel = (VideoModel) progress.extra1;
                    if (!videoModel.isChecked()) {
                        videoModel.setChecked(true);
                        OnItemClickListener.plusNum();
                    } else if (videoModel.isChecked()) {
                        videoModel.setChecked(false);
                        OnItemClickListener.downNum();
                    }
                    notifyDataSetChanged();
                }
            } else if (type == TYPE_FINISH) {
                if (checkbox.getVisibility() == View.GONE) {//进入本地视频播放页
                    Progress progress = task.progress;
                    VideoModel vm = (VideoModel) progress.extra1;
                    Bundle bundle = new Bundle();
                    String path1 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ccmtvCache/video/" + vm.getName();
                    Intent intent = new Intent(context, VideoPlayerActivity.class);
                    intent.putExtra("videoTitle", name.getText().toString());//传的videoTitle不带后缀
                    intent.putExtra("videoPath", path1);
                    intent.putExtra("last_look_time", vm.getLast_look_time());
                    intent.putExtra("aid", vm.getAid());
                    intent.putExtra("videoSource",0);//设置视频来源   0代表本地  1代表网络
//                    Log.e("last_look_time", vm.getLast_look_time() + "");
                    context.startActivity(intent);
                    refresh(progress);
                } else if (checkbox.getVisibility() == View.VISIBLE) {//控制item的checkbox影藏显示
                    Progress progress = task.progress;
                    VideoModel videoModel = (VideoModel) progress.extra1;
                    if (!videoModel.isChecked()) {
                        videoModel.setChecked(true);
                        OnItemClickListener.plusNum();
                    } else if (videoModel.isChecked()) {
                        videoModel.setChecked(false);
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

        private void setClickableTrue() {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    /**
                     *要执行的操作
                     */
                    ll_item.setClickable(true);
                }
            }, 1000);//1秒后执行Runnable中的run方法
        }
    }

    public OnItemClickListener OnItemClickListener;

    public void OnAddItemClickListener(OnItemClickListener lis) {
        OnItemClickListener = lis;
    }

    interface OnItemClickListener {
        void plusNum();

        void downNum();
    }

    private class ListDownloadListener extends DownloadListener {

        private ViewHolder holder;

        ListDownloadListener(Object tag, ViewHolder holder) {
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
        public void onFinish(File file, Progress progress) {
            Toast.makeText(context, "下载完成:" + progress.fileName, Toast.LENGTH_SHORT).show();
            updateData(type);
//            downloadAdapterNew.notifyDataSetChanged();
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
