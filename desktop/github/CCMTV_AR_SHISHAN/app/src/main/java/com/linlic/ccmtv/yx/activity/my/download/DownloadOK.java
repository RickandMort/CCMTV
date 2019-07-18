package com.linlic.ccmtv.yx.activity.my.download;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.db.MyDbUtils;
import com.linlic.ccmtv.yx.activity.entity.DbDownloadVideo;
import com.linlic.ccmtv.yx.activity.upload.VideoPlayerActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * name：已完成下载列表
 * author：MrSong
 * data：2016/6/28.
 */
public class DownloadOK extends BaseActivity {
    private TextView title;//顶部title
    private String topTitle;
    private Context context;
    private List<Map<String, String>> list = new ArrayList<>();//数据list
    private ListView listview;
    private DownloadOKAdapter adapter;
    private LinearLayout download_bottom;//底部的全选和删除按钮
    private ImageView download_topRightImg;//右上角删除图标
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
        setContentView(R.layout.download_ok);
        context = this;
        findViewById();
        //获取传输过来的参数
        getIntents();
        //查询当前类型下所有已完成的视频
//        findDownlodVideoOK();
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
                        deleteVideo_tv.setTextColor(getResources().getColor(R.color.activity_title_name));
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

    private void findDownlodVideoOK() {
        list.clear();
        List<DbDownloadVideo> downlodVideoOK = MyDbUtils.findDownlodVideoOK(context, topTitle);
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
                download_topRightImg.setVisibility(View.GONE);
            }
        } else {
            download_topRightImg.setVisibility(View.GONE);
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
                download_topRightImg.setVisibility(View.GONE);
            }
        } else {
            download_topRightImg.setVisibility(View.GONE);
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
        download_topRightImg = (ImageView) findViewById(R.id.download_topRightImg);
    }

    /**
     * name：获取传输过来的参数
     * author：MrSong
     * data：2016/6/28 17:04
     */
    private void getIntents() {
        topTitle = getIntent().getExtras().getString("topTitle");
        title.setText(topTitle);
    }

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
            download_topRightImg.setSelected(true);
            deleteVideo_tv.setClickable(false);
            deleteVideo_tv.setTextColor(getResources().getColor(R.color.activity_title_view));
        } else if (listViewCheckBoxStatus == listViewCheckBoxStatusVisible) {
            listViewCheckBoxStatus = listViewCheckBoxStatusNone;
            download_bottom.setVisibility(View.GONE);
            download_topRightImg.setSelected(false);
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
            deleteVideo_tv.setTextColor(getResources().getColor(R.color.activity_title_name));
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
                convertView = LayoutInflater.from(context).inflate(R.layout.upload_download_list_item, null);
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
                    findDownlodVideoOK();
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
}