package com.linlic.ccmtv.yx.activity.my.download;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.db.MyDbUtils;
import com.linlic.ccmtv.yx.activity.entity.DbDownloadVideo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * name：下载中和下载失败列表
 * author：MrSong
 * data：2016/6/28.
 */
public class DownloadingActivity extends BaseActivity {
    private Context context;
    private List<Map<String, String>> list = new ArrayList<>();//数据list
    private ListView listview;
    private DownloadingAdapter adapter;
    private MyReve myReve = new MyReve();
    private LinearLayout ll_caching, ll_cached;
    private RelativeLayout download_memory;
    //    private ImageView download_topRightImg;//右上角删除图标
    private TextView activity_title_name;
    private TextView download_topRightTxt;//右上角编辑按钮
    private LinearLayout download_bottom;//底部的全选和删除按钮
    private TextView deleteVideo_tv;//删除按钮
    private TextView selectAllCheck_tv;//全选按钮
    private String listViewCheckBoxStatus = "none";
    private String listViewCheckBoxStatusVisible = "visible";
    private String listViewCheckBoxStatusNone = "none";
    private int checkNum = 0; // 记录选中的条目数量
    private Map<Integer, IsSelectedEntity> isSelected = new HashMap<>();// 用来控制CheckBox的选中状况

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消屏幕旋转
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.download_ok_new);
        context = this;
        findViewById();
        //查询所有正在下载的视频 和 下载失败的视频 和 等待下载的视频
        findAllDownloadingAndFail("one");
        //再次注册广播
        registerDownloadReceiver();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView videoId = (TextView) view.findViewById(R.id.upload_down_item_videoid);
                TextView state = (TextView) view.findViewById(R.id.upload_down_item_state);
                TextView videoName = (TextView) view.findViewById(R.id.upload_down_item_videoname);
                if (listViewCheckBoxStatus.equals(listViewCheckBoxStatusNone)) {
                    Intent i = new Intent();
                    i.setAction("download_stop");
                    i.putExtra("videoId", videoId.getText().toString());
                    i.putExtra("videoState", state.getText().toString());
                    sendBroadcast(i);
                } else if (listViewCheckBoxStatus.equals(listViewCheckBoxStatusVisible)) {
                    DbDownloadVideo video = MyDbUtils.findVideoMsg(context, videoId.getText().toString());
                    DownloadingAdapter.ViewHolder holder = (DownloadingAdapter.ViewHolder) view.getTag();
                    // 改变CheckBox的状态
                    holder.checkBox.toggle();
                    // 将CheckBox的选中状况记录下来
                    IsSelectedEntity ise = new IsSelectedEntity();
                    ise.setIsSelectedStatus(holder.checkBox.isChecked());
                    ise.setFilePath(video.getFilePath());
                    ise.setVideoName(videoName.getText().toString());
                    ise.setVideoId(videoId.getText().toString());
                    isSelected.put(position, ise);
                    // 调整选定条目
                    if (holder.checkBox.isChecked() == true) {
                        checkNum++;
                    } else {
                        checkNum--;
                    }
                    //显示选中数字
                    if (checkNum == 0) {//没有任何选中
                        deleteVideo_tv.setText("删除");
                        selectAllCheck_tv.setText("全选");
                        deleteVideo_tv.setTextColor(getResources().getColor(R.color.activity_title_view));
                        deleteVideo_tv.setClickable(false);
                    } else {
                        deleteVideo_tv.setText("删除（" + checkNum + "）");
                        deleteVideo_tv.setTextColor(getResources().getColor(R.color.red));
                        deleteVideo_tv.setClickable(true);
                        if (checkNum == list.size()) {//已选中了全部的列表
                            selectAllCheck_tv.setText("取消全选");
                        } else {
                            selectAllCheck_tv.setText("全选");
                        }
                    }
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    /**
     * name：全选
     * author：MrSong
     * data：2016/7/5 17:10
     */
    public void selectAllCheck(View view) {
        if (selectAllCheck_tv.getText().toString().equals("全选")) {
            for (int i = 0; i < list.size(); i++) {
                IsSelectedEntity ise = new IsSelectedEntity();
                ise.setIsSelectedStatus(true);
                ise.setFilePath(list.get(i).get("filePath"));
                ise.setVideoName(list.get(i).get("videoName"));
                ise.setVideoId(list.get(i).get("videoId"));
                isSelected.put(i, ise);
            }
            checkNum = list.size();
            selectAllCheck_tv.setText("取消全选");
            deleteVideo_tv.setText("删除（" + list.size() + "）");
            deleteVideo_tv.setTextColor(getResources().getColor(R.color.red));
            deleteVideo_tv.setClickable(true);
        } else if (selectAllCheck_tv.getText().toString().equals("取消全选")) {
            for (int i = 0; i < list.size(); i++) {
                IsSelectedEntity ise = new IsSelectedEntity();
                ise.setIsSelectedStatus(false);
                isSelected.put(i, ise);
            }
            checkNum = 0;
            selectAllCheck_tv.setText("全选");
            deleteVideo_tv.setText("删除");
            deleteVideo_tv.setTextColor(getResources().getColor(R.color.activity_title_view));
            deleteVideo_tv.setClickable(false);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * name：删除
     * author：MrSong
     * data：2016/7/5 17:10
     */
    public void deleteVideo(View v) {
        int iii = 0;
        String iiistr = "";
        for (int i = 0; i < isSelected.size(); i++) {
            if (isSelected.get(i).getIsSelectedStatus() == true) {
                iii++;
                iiistr = "";
                iiistr = isSelected.get(i).getVideoName();
            }
        }
        View views = getLayoutInflater().inflate(R.layout.download_video_delete_dialog, null);
        TextView textview = (TextView) views.findViewById(R.id.tv_showstr);
        if (iii == 1) {
            textview.setText("确定删除 " + iiistr + " ？");
        } else {
            textview.setText("确定删除" + iii + "个视频？");
        }
        DeleteDialog deleteDialog = new DeleteDialog(context, R.style.mystyle, views);
        deleteDialog.show();
    }

    /**
     * name：右上角删除按钮
     * author：MrSong
     * data：2016/7/5 17:10
     */
    public void selectDelete(View view) {
        allAndDelete();
    }

    private void allAndDelete() {
        if (listViewCheckBoxStatus == listViewCheckBoxStatusNone) {
            listViewCheckBoxStatus = listViewCheckBoxStatusVisible;
            download_bottom.setVisibility(View.VISIBLE);
//            download_topRightImg.setSelected(true);
            download_topRightTxt.setText("取消");
            download_memory.setVisibility(View.GONE);
            deleteVideo_tv.setClickable(false);
            deleteVideo_tv.setTextColor(getResources().getColor(R.color.activity_title_view));
        } else if (listViewCheckBoxStatus == listViewCheckBoxStatusVisible) {
            listViewCheckBoxStatus = listViewCheckBoxStatusNone;
            download_bottom.setVisibility(View.GONE);
            download_topRightTxt.setText("编辑");
            download_memory.setVisibility(View.VISIBLE);
//            download_topRightImg.setSelected(false);
            //清空选中的信息
            selectAllCheck_tv.setText("全选");
            deleteVideo_tv.setText("删除");
            checkNum = 0;
            for (int i = 0; i < isSelected.size(); i++) {
                IsSelectedEntity ise = new IsSelectedEntity();
                ise.setIsSelectedStatus(false);
                isSelected.put(i, ise);
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * name：查询所有正在下载的视频 和 下载失败的视频 和 等待下载的视频
     * author：MrSong
     * data：2016/7/12 13:54
     * <p/>
     * receiver 参数：如果传进来的参数是receiver（广播跳进来的），就获取isSelected内的isSelectedStatus动态参数
     * 如果不是就设置为默认的false
     */
    private void findAllDownloadingAndFail(String receiver) {
        list.clear();
        List<DbDownloadVideo> downIng = MyDbUtils.findDownloading(context);
        if (downIng != null) {
            if (downIng.size() != 0) {
                for (int i = 0; i < downIng.size(); i++) {
                    Map<String, String> map = new HashMap<String, String>();
                    DbDownloadVideo video = downIng.get(i);
                    map.put("videoId", video.getVideoId());
                    map.put("videoName", video.getVideoName());
                    map.put("fileSize", video.getCurrent() + "/" + video.getTotal());
//                    map.put("speed", video.getSpeed());
//                    map.put("prog", video.getDownProgress());
                    map.put("picUrl", video.getPicUrl());
                    map.put("state", video.getState());
                    list.add(map);
                    IsSelectedEntity ise = new IsSelectedEntity();
                    if (receiver.equals("receiver")) {
                        ise.setIsSelectedStatus(isSelected.get(i).getIsSelectedStatus());
                    } else {
                        ise.setIsSelectedStatus(false);
                    }
                    ise.setFilePath(video.getFilePath());
                    ise.setVideoName(video.getVideoName());
                    ise.setVideoId(video.getVideoId());
                    isSelected.put(i, ise);
                }
                if (adapter == null) {
                    adapter = new DownloadingAdapter();
                    listview.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            } else {
                layout_nodata.setVisibility(View.VISIBLE);
                download_topRightTxt.setVisibility(View.GONE);
            }
        } else {
            layout_nodata.setVisibility(View.VISIBLE);
            download_topRightTxt.setVisibility(View.GONE);
        }
    }

    /**
     * name：查询所有ID
     * author：MrSong
     * data：2016/6/28 17:02
     */
    private void findViewById() {
        listview = (ListView) findViewById(R.id.download_listview);
        layout_nodata = findViewById(R.id.layout_nodata);
//        download_topRightImg = (ImageView) findViewById(R.id.download_topRightImg);
        download_topRightTxt = (TextView) findViewById(R.id.download_topRightTxt);
        download_bottom = (LinearLayout) findViewById(R.id.download_bottom);
        deleteVideo_tv = (TextView) findViewById(R.id.delete_video);
        selectAllCheck_tv = (TextView) findViewById(R.id.selectAllCheck);
        activity_title_name = (TextView) findViewById(R.id.activity_title_name);
        activity_title_name.setText("正在缓存");
        download_memory = (RelativeLayout) findViewById(R.id.download_memory);
        ll_caching = (LinearLayout) findViewById(R.id.ll_caching);
        ll_cached = (LinearLayout) findViewById(R.id.ll_cached);
        ll_caching.setVisibility(View.GONE);
        ll_cached.setVisibility(View.GONE);
    }

    /**
     * name：再次注册广播
     * author：MrSong
     * data：2016/6/28 19:13
     */
    private void registerDownloadReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("download_progress");
        filter.addAction("download_failure");
        filter.addAction("download_success");
        filter.addAction("download_stop");
        filter.addAction("download");
        filter.addAction("download_stopser");
        registerReceiver(myReve, filter);
    }

    private class MyReve extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            switch (action) {
                case "download":
                    break;
                case "download_failure":
                    break;
                case "download_progress":
                    break;
                case "download_stop":
                    break;
                case "download_stopser":
                    break;
                case "download_success":
                    break;
            }
            findAllDownloadingAndFail("receiver");
        }
    }

    /*********************************************************************************************
     * 以下为adapter
     *********************************************************************************************/
    class DownloadingAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                convertView = LayoutInflater.from(context).inflate(R.layout.upload_download_list_item_new, null);
                holder.img = (ImageView) convertView.findViewById(R.id.upload_down_item_img);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.upload_down_item_select);
                holder.videoname = (TextView) convertView.findViewById(R.id.upload_down_item_videoname);
                holder.size = (TextView) convertView.findViewById(R.id.upload_down_item_size);
//                holder.speed = (TextView) convertView.findViewById(R.id.upload_down_item_speed);
//                holder.prog = (TextView) convertView.findViewById(R.id.upload_down_item_prog);
                holder.status = (TextView) convertView.findViewById(R.id.upload_down_item_state);
                holder.videoid = (TextView) convertView.findViewById(R.id.upload_down_item_videoid);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (list.size() != 0) {
                holder.videoname.setText(list.get(position).get("videoName"));
                holder.size.setText(list.get(position).get("fileSize"));
                holder.status.setText(list.get(position).get("state"));
                holder.videoid.setText(list.get(position).get("videoId"));
//                holder.speed.setText(list.get(position).get("speed"));
//                holder.prog.setText(list.get(position).get("prog"));
                RequestOptions options = new RequestOptions()
                        .placeholder(R.mipmap.img_default)
                        .error(R.mipmap.img_default);
                Glide.with(context).load(list.get(position).get("picUrl"))
                        .apply(options)
                        .into(holder.img);
                //根据状态判断当前CheckBox是否显示
                if (listViewCheckBoxStatus.equals(listViewCheckBoxStatusNone)) {
                    holder.checkBox.setVisibility(View.GONE);
                } else if (listViewCheckBoxStatus.equals(listViewCheckBoxStatusVisible)) {
                    holder.checkBox.setVisibility(View.VISIBLE);
                    holder.checkBox.setChecked(isSelected.get(position).getIsSelectedStatus());
                }

                //根据视频状态显示等待中、下载中
                if (holder.status.getText().toString().equals("500")) {//下载失败
//                    holder.prog.setText("下载失败");
//                    holder.speed.setText("点击继续下载");
                    holder.size.setVisibility(View.VISIBLE);
//                    holder.speed.setVisibility(View.VISIBLE);
                } else if (holder.status.getText().toString().equals("100")) {//下载暂停
//                    holder.prog.setText("已暂停");
                    holder.size.setVisibility(View.INVISIBLE);
//                    holder.speed.setVisibility(View.INVISIBLE);
                } else if (holder.status.getText().toString().equals("300")) {//等待下载
//                    holder.prog.setText("等待下载");
                    holder.size.setVisibility(View.INVISIBLE);
//                    holder.speed.setVisibility(View.INVISIBLE);
                } else if (holder.status.getText().toString().equals("18")) {//下载中
//                    holder.speed.setVisibility(View.VISIBLE);
                    holder.size.setVisibility(View.VISIBLE);
                }
            }
            return convertView;
        }

        public final class ViewHolder {
            ImageView img;
            CheckBox checkBox;
            TextView videoname;
            TextView size;
            TextView status;
            TextView videoid;
//            TextView speed;
//            TextView prog;
        }
    }

    /**
     * name：自定义dialog
     * author：MrSong
     * data：2016/6/29 17:37
     */
    class DeleteDialog extends Dialog implements View.OnClickListener {
        private View view;
        private Button btns, btns1;

        public DeleteDialog(Context context, int theme, View view) {
            super(context, theme);
            this.view = view;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            btns = (Button) findViewById(R.id.cancel_btns);
            btns1 = (Button) findViewById(R.id.cancel_btns1);
            btns.setOnClickListener(this);
            btns1.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cancel_btns://取消
                    dismiss();
                    break;
                case R.id.cancel_btns1://确定
//                    Toast.makeText(context, "删除中...", Toast.LENGTH_SHORT).show();

                    boolean checkDeleteTaskIsProgressState = false;
                    for (int i = 0; i < isSelected.size(); i++) {
                        IsSelectedEntity ise = isSelected.get(i);
                        if (ise.getIsSelectedStatus() == true) {
                            DbDownloadVideo video = MyDbUtils.findVideoMsg(context, ise.getVideoId());
                            if (video.getState().equals(DownloadService.download_progress + "")) {
                                //发送一条广播，停止当前任务
                                Intent intent = new Intent(context, DownloadService.class);
                                intent.putExtra("videoId", video.getVideoId());
                                intent.putExtra("videoName", "");
                                intent.putExtra("downProgress", "");
                                intent.putExtra("downURL", "");
                                intent.putExtra("isStop", true);
                                context.startService(intent);
                                checkDeleteTaskIsProgressState = true;
                            }
                            deleteFileAndDB(ise.getFilePath(), ise.getVideoId());
                        }
                    }
//                    Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                    dismiss();
                    //重新刷新页面
                    findAllDownloadingAndFail("two");
                    checkNum = 0;
                    listViewCheckBoxStatus = listViewCheckBoxStatusVisible;
                    allAndDelete();
                    //清除顶部通知栏任务
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(1);
                    if (checkDeleteTaskIsProgressState = true) {
                        //继续下载剩余没有删除的任务
                        String waitDown = MyDbUtils.findWaitDown(context);
                        if (waitDown.length() == 0) {//已经没有等待下载的视频了
                            context.stopService(new Intent(context, DownloadService.class));
                        } else {
                            Intent intent1 = new Intent();
                            intent1.setAction("download");
                            context.sendBroadcast(intent1);
                        }
                    }
                    break;
            }

        }

        private void deleteFileAndDB(String filePath, String videoId) {
            if (filePath != null && filePath.length() > 0) {
                MyDbUtils.deleteVideo(context, videoId);
                File file = new File(filePath);
                if (file.exists()) {// 判断文件是否存在
                    if (file.isFile()) {// 判断是否是文件
                        file.delete();
                    }
                } else {
//                    Log.i("下载中页面删除功能", "当前需要删除的文件不存在");
                }
            }
        }
    }

    class IsSelectedEntity {
        boolean isSelectedStatus;
        String filePath;
        String videoName;
        String videoId;

        IsSelectedEntity() {

        }

        public boolean getIsSelectedStatus() {
            return isSelectedStatus;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setIsSelectedStatus(boolean isSelectedStatus) {
            this.isSelectedStatus = isSelectedStatus;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public String getVideoName() {
            return videoName;
        }

        public void setVideoName(String videoName) {
            this.videoName = videoName;
        }

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }

        @Override
        public String toString() {
            return "IsSelectedEntity{" +
                    "isSelectedStatus=" + isSelectedStatus +
                    ", filePath='" + filePath + '\'' +
                    ", videoName='" + videoName + '\'' +
                    ", videoId='" + videoId + '\'' +
                    '}';
        }
    }

    @Override
    public void onBackPressed() {
        unregisterReceiver(myReve);
        super.onBackPressed();
    }

    public void back(View view) {
        unregisterReceiver(myReve);
        finish();
    }

    /*@com.arialyy.annotations.Download.onTaskStart void taskStart(DownloadTask task) {
        if(task.getKey().equals(DOWNLOAD_URL)){
            mAdapter.setProgress(task.getDownloadEntity());
        }
    }*/

}
