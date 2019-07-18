package com.linlic.ccmtv.yx.activity.my.newDownload;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.db.MyDbUtils;
import com.linlic.ccmtv.yx.activity.upload.VideoPlayerActivity;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yu on 2018/5/7.
 */

public class DownloadVideoFileAdapter extends RecyclerView.Adapter<DownloadVideoFileAdapter.ViewHolder> {

    public DownloadVideoFileAdapter downloadVideoFileAdapter;
    private List<VideoBean> list = new ArrayList<>();
    private NumberFormat numberFormat;
    private LayoutInflater inflater;
    private Context context;
    private int type;

    public DownloadVideoFileAdapter(Context context, List<VideoBean> list) {
        this.context = context;
        numberFormat = NumberFormat.getPercentInstance();
        numberFormat.setMinimumFractionDigits(2);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        downloadVideoFileAdapter = this;
        this.list = list;
    }

    @Override
    public DownloadVideoFileAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_download_manager, parent, false);
        return new DownloadVideoFileAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DownloadVideoFileAdapter.ViewHolder holder, int position) {
        VideoBean videoBean = list.get(position);

        String tag = createTag(videoBean);
        holder.setTag(tag);
        holder.setTask(videoBean);
        holder.bind();
        holder.setIsRecyclable(false);
        holder.refresh(videoBean);
        holder.pbProgress.setVisibility(View.GONE);
        //控制item的checkbox隐藏显示
        if (videoBean.getCheckBoxStatus().equals("1")) {
            holder.checkbox.setVisibility(View.VISIBLE);
        } else if (videoBean.getCheckBoxStatus().equals("0")) {
            holder.checkbox.setVisibility(View.GONE);
        }
        if (videoBean.isChecked()) {
            videoBean.setChecked(true);
            holder.checkbox.setChecked(true);
        } else {
            videoBean.setChecked(false);
            holder.checkbox.setChecked(false);
        }
        if (videoBean.getIsVisibility().equals("1")) {
            holder.pbProgress.setVisibility(View.VISIBLE);
        } else {
            holder.pbProgress.setVisibility(View.GONE);
        }
        holder.pbProgress.setVisibility(View.GONE);
    }

    private String createTag(VideoBean videoBean) {
        return type + "_" + videoBean.getVideoName();
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
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
        private VideoBean videoBean;
        private String tag;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setTask(VideoBean bean) {
            this.videoBean = bean;
        }

        public void bind() {
            try {
                if (videoBean != null) {
                    RequestOptions options = new RequestOptions().error(R.mipmap.img_default);
                    Glide.with(context)
                            .load(Uri.fromFile(new File(videoBean.getVideoPath())))
                            .apply(options)
                            .into(icon);
                    name.setText(videoBean.getVideoName());
                } else {
                    name.setText("");
                }
                item_time.setText(videoBean.getVideoTime());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void refresh(VideoBean videoBean) {
            String totalSize = videoBean.videoSize;
            String vtime = videoBean.getVideoTime();
            long vt;
            if (vtime!=null && !vtime.equals("")) {
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
            } else if (vtime!=null && vtime.equals("00:00:00")) {
                vt = 0;
            } else {
                vt = 0;
            }

            downloadSize.setVisibility(View.GONE);

            //pbProgress.setVisibility(View.GONE);
            if (vt != 0) {
                double d2 = vt;
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ccmtvCache/video/" + videoBean.getVideoName();
                path = path.hashCode()+"";
                Log.d("mason","   =====  path "+path);
                if (MyDbUtils.findLocalRecordVideo(context, path) > 0) {
                    double d1 = MyDbUtils.findLocalRecordVideo(context, path);
                    DecimalFormat df = new DecimalFormat("#00");
                    String s = String.valueOf(df.format((d1 / d2) * 100).charAt(0));
                    if ("0".equals(s)) {
                        netSpeed.setText("已在手机看至" + df.format((d1 / d2) * 100).substring(1) + "%" + "  |  " + totalSize + "");
                    } else {
                        netSpeed.setText("已在手机看至" + df.format((d1 / d2) * 100) + "%" + "  |  " + totalSize + "");
                    }
                } else {
                    if (vt != 0) {
                        double d1 = videoBean.getLast_look_time();
                        DecimalFormat df = new DecimalFormat("#00");
                        String s = String.valueOf(df.format((d1 / d2) * 100).charAt(0));
                        if ("0".equals(s)) {
                            netSpeed.setText("已在手机看至" + df.format((d1 / d2) * 100).substring(1) + "%" + "  |  " + totalSize + "");
                        } else {
                            netSpeed.setText("已在手机看至" + df.format((d1 / d2) * 100) + "%" + "  |  " + totalSize + "");
                        }
                        download.setText("完成");
                    } else {
                        netSpeed.setText("已在手机看至0%" + "  |  " + totalSize + "");
                    }
                }

            } else {
                netSpeed.setText("");
            }
        }

        @OnClick(R.id.remove)
        public void remove() {

        }

        @OnClick(R.id.ll_item)
        public void start_pause() {
            /*if (checkbox.getVisibility() == View.GONE) {
                if (VideoUtils.isAvailable(context, new File(videoBean.getVideoPath()))) {
                    VideoUtils.uninstall(context, VideoUtils.getPackageName(context, videoBean.getVideoPath()));
                } else {
                    VideoUtils.install(context, new File(videoBean.getVideoPath()));
                }
                refresh(videoBean);
            } else if (checkbox.getVisibility() == View.VISIBLE) {
                if (!videoBean.isChecked()) {
                    videoBean.setChecked(true);
                    OnItemClickListener.plusNum();
                } else if (videoBean.isChecked()) {
                    videoBean.setChecked(false);
                    OnItemClickListener.downNum();
                }
                notifyDataSetChanged();
            }*/
            if (checkbox.getVisibility() == View.GONE) {//进入本地视频播放页
                Bundle bundle = new Bundle();
                String path1 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ccmtvCache/video/" + videoBean.getVideoName();
                Intent intent = new Intent(context, VideoPlayerActivity.class);
                intent.putExtra("videoTitle", name.getText().toString());//传的videoTitle不带后缀
                intent.putExtra("videoPath", path1);
                intent.putExtra("last_look_time", videoBean.getLast_look_time());
                String path = path1.hashCode()+"";
                intent.putExtra("aid", path);
                intent.putExtra("videoSource", 0);//设置视频来源   0代表本地  1代表网络
//                Log.e("last_look_time", videoBean.getLast_look_time() + "");
                context.startActivity(intent);
                refresh(videoBean);
            } else if (checkbox.getVisibility() == View.VISIBLE) {//控制item的checkbox影藏显示
                if (!videoBean.isChecked()) {
                    videoBean.setChecked(true);
                    OnItemClickListener.plusNum();
                } else if (videoBean.isChecked()) {
                    videoBean.setChecked(false);
                    OnItemClickListener.downNum();
                }
                notifyDataSetChanged();
            }
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getTag() {
            return tag;
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
}
