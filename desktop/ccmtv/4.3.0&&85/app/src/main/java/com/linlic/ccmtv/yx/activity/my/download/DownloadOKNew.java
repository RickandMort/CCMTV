package com.linlic.ccmtv.yx.activity.my.download;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StatFs;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.CustomActivity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.db.MyDbUtils;
import com.linlic.ccmtv.yx.activity.entity.DbDownloadVideo;
import com.linlic.ccmtv.yx.activity.upload.VideoPlayerActivity;
import com.linlic.ccmtv.yx.utils.FileSizeUtil;
import com.linlic.ccmtv.yx.utils.SDCardUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.getSdcardPath;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * name：已完成下载列表
 * author：Niklaus
 * data：2017/11/13.
 */
public class DownloadOKNew extends BaseActivity {
    private TextView title;//顶部title
//    private String topTitle;
    private Context context;
    private List<Map<String, String>> list = new ArrayList<>();//数据list
    private ListView listview;
    private DownloadOKAdapter adapter;
    private TextView activity_title_name, tv_nodata;
    private LinearLayout ll_caching,ll_item_caching;//正在下载条目
    private LinearLayout download_bottom;//底部的全选和删除按钮
    private TextView download_topRightTxt;//右上角编辑按钮
    private TextView deleteVideo_tv;//删除按钮
    private TextView selectAllCheck_tv;//全选按钮
    private String listViewCheckBoxStatus = "none";
    private String listViewCheckBoxStatusVisible = "visible";
    private String listViewCheckBoxStatusNone = "none";
    private int checkNum = 0; // 记录选中的条目数量
    private Map<Integer, IsSelectedEntity> isSelected = new HashMap<>();// 用来控制CheckBox的选中状况

    //内存信息
    private TextView memory_tv;
    private ProgressBar memory_progress;
    private RelativeLayout download_memory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消屏幕旋转
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.download_ok_new);
        context = this;
        findViewById();
        //获取传输过来的参数
//        getIntents();
        //查询所有已完成的视频
        findAllDownlodVideoOK();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView name = (TextView) view.findViewById(R.id.upload_down_item_videoname);
                TextView filePath = (TextView) view.findViewById(R.id.upload_down_item_state);
                TextView videoid = (TextView) view.findViewById(R.id.upload_down_item_videoid);
                if (listViewCheckBoxStatus.equals(listViewCheckBoxStatusNone)) {
                    Intent intent = new Intent(context, VideoPlayerActivity.class);
                    intent.putExtra("videoTitle", name.getText().toString());
                    intent.putExtra("videoPath", filePath.getText().toString());
                    startActivity(intent);
                    /*
                    Uri uri = Uri.parse(filePath.getText().toString());
                    //调用系统自带的播放器
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Log.v("filePath", uri.toString());
                    intent.setDataAndType(uri, "video/mp4");
                    startActivity(intent);
                    */
                } else if (listViewCheckBoxStatus.equals(listViewCheckBoxStatusVisible)) {
                    DownloadOKAdapter.ViewHolder holder = (DownloadOKAdapter.ViewHolder) view.getTag();
                    // 改变CheckBox的状态
                    holder.checkBox.toggle();
                    // 将CheckBox的选中状况记录下来
                    IsSelectedEntity ise = new IsSelectedEntity();
                    ise.setIsSelectedStatus(holder.checkBox.isChecked());
                    ise.setFilePath(filePath.getText().toString());
                    ise.setVideoName(name.getText().toString());
                    ise.setVideoId(videoid.getText().toString());
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

        memoryUtils();
    }

    /**
     * name：
     * author：MrSong
     * data：2016/7/13 14:25
     * <p>
     * 手机存储路径：/storage/emulated/0/ccmtvCache
     * SD卡存储路径：/storage/extSdCard/Android/data/com.linlic.ccmtv.yx
     */
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

    private void findAllDownlodVideoOK() {
        list.clear();
        List<DbDownloadVideo> downlodVideoOK = MyDbUtils.findAllDownlodVideoOK(context);
        if (downlodVideoOK != null) {
            if (downlodVideoOK.size() != 0) {
                for (int i = 0; i < downlodVideoOK.size(); i++) {
                    DbDownloadVideo video = downlodVideoOK.get(i);
                    Map<String, String> map = new HashMap<>();
                    map.put("picUrl", video.getPicUrl());
                    map.put("videoName", video.getVideoName());
                    map.put("total", video.getTotal());
                    map.put("filePath", video.getFilePath());
                    map.put("videoId", video.getVideoId());
                    list.add(map);
                    IsSelectedEntity ise = new IsSelectedEntity();
                    ise.setIsSelectedStatus(false);
                    ise.setFilePath(video.getFilePath());
                    ise.setVideoName(video.getVideoName());
                    ise.setVideoId(video.getVideoId());
                    isSelected.put(i, ise);
                }
                if (adapter == null) {
                    adapter = new DownloadOKAdapter();
                    listview.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            } else {
                download_topRightTxt.setVisibility(View.GONE);
            }
        } else {
            download_topRightTxt.setVisibility(View.GONE);
        }
    }

    /**
     * name：查询所有ID
     * author：MrSong
     * data：2016/6/28 17:02
     */
    private void findViewById() {
        title = (TextView) findViewById(R.id.activity_title_name);
        deleteVideo_tv = (TextView) findViewById(R.id.delete_video);
        selectAllCheck_tv = (TextView) findViewById(R.id.selectAllCheck);
        listview = (ListView) findViewById(R.id.download_listview);
        download_bottom = (LinearLayout) findViewById(R.id.download_bottom);
        download_topRightTxt = (TextView) findViewById(R.id.download_topRightTxt);
        activity_title_name = (TextView) findViewById(R.id.activity_title_name);
        tv_nodata = (TextView) findViewById(R.id.tv_nodata);
        memory_tv = (TextView) findViewById(R.id.memory_tv);
        memory_progress = (ProgressBar) findViewById(R.id.memory_progress);
        download_memory = (RelativeLayout) findViewById(R.id.download_memory);
        ll_caching = (LinearLayout) findViewById(R.id.ll_caching);
        ll_item_caching = (LinearLayout) findViewById(R.id.ll_item_caching);

        activity_title_name.setText("下载");
        tv_nodata.setText("您还没有下载视频");
        tv_nodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DownloadOKNew.this, CustomActivity.class);
                startActivity(intent);
            }
        });

        ll_item_caching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击进入正在缓存界面
                startActivity(new Intent(context, DownloadingActivity.class));
            }
        });
    }

    /**
     * name：获取传输过来的参数
     * author：MrSong
     * data：2016/6/28 17:04
     */
    /*private void getIntents() {
        topTitle = getIntent().getExtras().getString("topTitle");
        title.setText(topTitle);
    }*/

    public void back(View view) {
        finish();
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
            download_topRightTxt.setText("取消");
            download_memory.setVisibility(View.GONE);
            ll_caching.setVisibility(View.GONE);
            deleteVideo_tv.setClickable(false);
            deleteVideo_tv.setTextColor(getResources().getColor(R.color.activity_title_view));
        } else if (listViewCheckBoxStatus == listViewCheckBoxStatusVisible) {
            listViewCheckBoxStatus = listViewCheckBoxStatusNone;
            download_bottom.setVisibility(View.GONE);
            download_topRightTxt.setText("编辑");
            download_memory.setVisibility(View.VISIBLE);
            ll_caching.setVisibility(View.VISIBLE);
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
     * name：获取所有大小
     * author：MrSong
     * data：2016/7/13 14:33
     */
    public long getAllSize() {
        StatFs stat = new StatFs(getSdcardPath.getStoragePath(context, true));
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getBlockCount();
        return availableBlocks * blockSize;
    }

    /**
     * name：获取SDCARD剩余存储空间
     * author：MrSong
     * data：2016/7/13 14:33
     */
    public long getAvailableExternalMemorySize() {
        StatFs stat = new StatFs(getSdcardPath.getStoragePath(context, true));
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /*********************************************************************************************
     * 以下为adapter
     *********************************************************************************************/
    class DownloadOKAdapter extends BaseAdapter {

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
                convertView = LayoutInflater.from(context).inflate(R.layout.upload_download_list_item_new1, null);
                holder.img = (ImageView) convertView.findViewById(R.id.upload_down_item_img);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.upload_down_item_select);
                holder.videoname = (TextView) convertView.findViewById(R.id.upload_down_item_videoname);
                holder.size = (TextView) convertView.findViewById(R.id.upload_down_item_size);
                holder.status = (TextView) convertView.findViewById(R.id.upload_down_item_state);
                holder.videoid = (TextView) convertView.findViewById(R.id.upload_down_item_videoid);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (list.size() != 0) {
                holder.videoname.setText(list.get(position).get("videoName"));
                holder.size.setText(list.get(position).get("total"));
                holder.status.setText(list.get(position).get("filePath"));
                holder.videoid.setText(list.get(position).get("videoId"));
                Glide.with(context).load(list.get(position).get("picUrl")).into(holder.img);

                //根据状态判断当前CheckBox是否显示
                if (listViewCheckBoxStatus.equals(listViewCheckBoxStatusNone)) {
                    holder.checkBox.setVisibility(View.GONE);
                } else if (listViewCheckBoxStatus.equals(listViewCheckBoxStatusVisible)) {
                    holder.checkBox.setVisibility(View.VISIBLE);
                    holder.checkBox.setChecked(isSelected.get(position).getIsSelectedStatus());
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
                    Toast.makeText(context, "删除中...", Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < isSelected.size(); i++) {
                        IsSelectedEntity ise = isSelected.get(i);
                        if (ise.getIsSelectedStatus() == true) {
                            deleteFileAndDB(ise.getFilePath(), ise.getVideoId());
                        }
                    }
                    Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                    dismiss();
                    //重新刷新页面
                    findAllDownlodVideoOK();
                    checkNum = 0;
                    listViewCheckBoxStatus = listViewCheckBoxStatusVisible;
                    allAndDelete();
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
//                    Toast.makeText(context, "文件不存在", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public class IsSelectedEntity {
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


}