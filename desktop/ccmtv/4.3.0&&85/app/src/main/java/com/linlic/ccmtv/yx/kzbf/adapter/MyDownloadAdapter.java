package com.linlic.ccmtv.yx.kzbf.adapter;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.entity.VideoModel;
import com.linlic.ccmtv.yx.activity.my.newDownload.DownloadAdapterNew;
import com.linlic.ccmtv.yx.activity.my.newDownload.LogDownloadListener;
import com.linlic.ccmtv.yx.kzbf.activity.MyDownloadActivity;
import com.linlic.ccmtv.yx.kzbf.activity.PDFViewerActivity;
import com.linlic.ccmtv.yx.kzbf.widget.CircleProgressBar;
import com.linlic.ccmtv.yx.utils.CustomImageView;
import com.linlic.ccmtv.yx.widget.NumberProgressBar;
import com.lzy.okgo.db.DownloadManager;
import com.lzy.okgo.model.Progress;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;
import com.lzy.okserver.task.XExecutor;

import org.w3c.dom.Text;

import java.io.File;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 */
public class MyDownloadAdapter extends RecyclerView.Adapter<MyDownloadAdapter.ViewHolder> {

    public static final int TYPE_ALL = 0;
    public static final int TYPE_FINISH = 1;
    public static final int TYPE_ING = 2;

    private List<DownloadTask> values1;
    private List<DownloadTask> values;
    private NumberFormat numberFormat;
    private LayoutInflater inflater;
    private Context context;
    private int type;

    public MyDownloadAdapter(Context context) {
        this.context = context;
        numberFormat = NumberFormat.getPercentInstance();
        numberFormat.setMinimumFractionDigits(2);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        for (int i = 0;i<values1.size();i++) {
            if(values1.get(i).progress.extra2 != null && 1==1) {
                values.add(values1.get(i));
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_my_download, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DownloadTask task = values.get(position);
        if (task.progress.extra2 != null && 1 == 1) {
            String tag = createTag(task);
            task.register(new ListDownloadListener(tag, holder)).register(new LogDownloadListener());
            holder.setTag(tag);
            holder.setTask(task);
            holder.bind();
            holder.refresh(task.progress);

            VideoModel videoModel = (VideoModel) task.progress.extra2;
			if (videoModel != null) {
	            if (videoModel.getArticleIsShowPro().equals("0")) {
	                holder.right_pro.setVisibility(View.GONE);
	                holder.bottom.setVisibility(View.VISIBLE);
	            } else {
	                holder.right_pro.setVisibility(View.VISIBLE);
	                holder.bottom.setVisibility(View.GONE);
	            }
			}
        }
    }

    public void unRegister() {//在onDestroy里使用，同时关闭监听
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

        @Bind(R.id.my_download_title)
        TextView title;
        @Bind(R.id.circle_progress_bar_4)
        CircleProgressBar progressBar;
        @Bind(R.id.my_download_start)
        CustomImageView start;
        @Bind(R.id.my_download_progress)
        TextView percentage;
        @Bind(R.id.rl_bottom)
        RelativeLayout bottom;
        @Bind(R.id.rl_progress)
        RelativeLayout right_pro;
        @Bind(R.id.bottom_left)
        TextView bottom_size;
        @Bind(R.id.bottom_right)
        TextView time;

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
            if (progress.extra2 != null && 1 == 1) {
                VideoModel apk = (VideoModel) progress.extra2;
                if (apk != null) {
                    title.setText(apk.articleName);
                } else {
                    title.setText(progress.fileName);
                }
            }
        }

        public void refresh(Progress progress) {
            String currentSize = Formatter.formatFileSize(context, progress.currentSize);
            String totalSize = Formatter.formatFileSize(context, progress.totalSize);
            if (progress.extra2 != null && 1 == 1) {
                VideoModel model = (VideoModel) progress.extra2;
                progressBar.setMaxProgress(1);
                switch (progress.status) {
                    case Progress.NONE:
                        //TODO 改变图标
                        break;
                    case Progress.PAUSE:
                        //显示三角图标，隐藏进度
                        start.setVisibility(View.VISIBLE);
                        percentage.setVisibility(View.GONE);
                        percentage.setText(numberFormat.format(progress.fraction));
                        break;
                    case Progress.ERROR:
                        //隐藏图标、进度
                        start.setVisibility(View.GONE);
                        percentage.setVisibility(View.VISIBLE);
                        percentage.setText("出错");
                        break;
                    case Progress.WAITING:
                        //显示进度，隐藏图标
                        start.setVisibility(View.GONE);
                        percentage.setVisibility(View.VISIBLE);
                        percentage.setText("等待");
                        break;
                    case Progress.FINISH:
						if (model != null) {
	                        if (progress.extra2 != null && 1 == 1) {
	                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	                            String date = sdf.format(new java.util.Date());
	                            //加入已完成列表，刷新数据
	                            showNotifictionIcon(context, progress);

	                            model.setArticleIsShowPro("0");
	                            bottom_size.setText("下载完成,共" + model.getArticleSize());
	                            time.setText(date);
	                        }
						}
                        break;
                    case Progress.LOADING:
                        //显示进度 隐藏图标
                        start.setVisibility(View.GONE);
                        percentage.setVisibility(View.VISIBLE);
                        progressBar.setCurrentProgress(progress.fraction);
                        percentage.setText(numberFormat.format(progress.fraction));
                        break;
                }
            }
        }

        @OnClick(R.id.right_delete)
        public void remove() {
            task.remove(true);
            updateData(type);
            OnItemClickListener.taskRemove();
        }

        @OnClick(R.id.rl_content)//条目点击事件
        public void itemClick() {
            if (type == TYPE_ING) {
                Progress progress = task.progress;
                switch (progress.status) {
                    case Progress.NONE:
                    case Progress.PAUSE:
                        task.start();
                        break;
                    case Progress.ERROR:
                        task.restart();
                        break;
                    case Progress.LOADING:
                        task.pause();
                        break;
                }
            } else if (type == TYPE_FINISH) {
                if (task.progress.extra2 != null && 1 == 1) {
                    VideoModel model = (VideoModel) task.progress.extra2;
                    String lastName = model.getArticleName().substring(model.getArticleName().lastIndexOf("."), model.getArticleName().length());
                    if (lastName.contains("pdf") || lastName.contains("PDF")) {
                        Intent intent = new Intent(context, PDFViewerActivity.class);
                        intent.putExtra("name", model.getArticleName());
                        context.startActivity(intent);
                    } else {
//                    openAssignFolder(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ccmtvCache/article/");
                        Toast.makeText(context, "暂不支持，请去" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/ccmtvCache/article/", Toast.LENGTH_SHORT).show();
                    }
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
            Toast.makeText(context, "下载完成:" + progress.filePath, Toast.LENGTH_SHORT).show();
            updateData(type);
            OnItemClickListener.plusNum();
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
//        builder.setDefaults(NotificationCompat.DEFAULT_SOUND);//设置通知铃声
        builder.setTicker("临床频道");
        builder.setContentText(progress.fileName);//通知内容
        //利用PendingIntent来包装我们的intent对象,使其延迟跳转
        PendingIntent intentPend = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(intentPend);
        NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    public OnItemClickListener OnItemClickListener;

    public void OnAddItemClickListener(OnItemClickListener lis) {
        OnItemClickListener = lis;
    }

    public interface OnItemClickListener {
        void plusNum();

        void taskRemove();
    }

    private void openAssignFolder(String path) {
        File file = new File(path);
        if (null == file || !file.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "file/*");
        try {
            context.startActivity(intent);
            context.startActivity(Intent.createChooser(intent, "选择浏览工具"));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
