package com.linlic.ccmtv.yx.activity.my.download;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import com.lidroid.xutils.db.table.DbModel;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.CustomActivity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.db.MyDbUtils;
import com.linlic.ccmtv.yx.activity.entity.DbDownloadVideo;
import com.linlic.ccmtv.yx.utils.FileSizeUtil;
import com.linlic.ccmtv.yx.utils.SDCardUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.getSdcardPath;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * name：下载列表页
 * author：MrSong
 * data：2016/6/23.
 */
public class Download extends BaseActivity {
    private Context context;
    private LinearLayout download_top_downloading;
    private ListView listview;
    private TextView downloading;
    private DownloadAdapter adapter;
    private List<Map<String, String>> list = new ArrayList<>();//数据list

    private LinearLayout download_bottom;//底部的全选和删除按钮
    //    private ImageView download_topRightImg;//右上角删除图标
    private TextView download_topRightTxt;//右上角编辑按钮
    private TextView deleteVideo_tv;//删除按钮
    private TextView selectAllCheck_tv;//全选按钮
    private String listViewCheckBoxStatus = "";
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
        setContentView(R.layout.activity_download);

        context = this;
        listViewCheckBoxStatus = listViewCheckBoxStatusNone;

        findViewById();

        //listview点击事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView videoClass = (TextView) view.findViewById(R.id.down_item_group_title);
                if (listViewCheckBoxStatus.equals(listViewCheckBoxStatusNone)) {
                    startActivity(new Intent(context, DownloadOK.class).putExtra("topTitle", videoClass.getText().toString()));
//                    Log.e("videoclass", videoClass.getText().toString());
                } else if (listViewCheckBoxStatus.equals(listViewCheckBoxStatusVisible)) {
                    DownloadAdapter.ViewHolder holder = (DownloadAdapter.ViewHolder) view.getTag();
                    // 改变CheckBox的状态
                    holder.checkBox.toggle();
                    // 将CheckBox的选中状况记录下来
                    IsSelectedEntity ise = new IsSelectedEntity();
                    ise.setIsSelectedStatus(holder.checkBox.isChecked());
                    ise.setVideoClass(videoClass.getText().toString());
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

        //多少个任务正在下载（点击事件）
        download_top_downloading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, DownloadingActivity.class));
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
            /*String phoneMemory = FileSizeUtil.FormetFileSize(SDCardUtils.getTotalInternalMemorySize()) + " / " + "剩余" + FileSizeUtil.FormetFileSize(SDCardUtils.getAvailableInternalMemorySize());
            memory_tv.setText("手机存储：总空间" + phoneMemory);*/

            String phoneMemory = FileSizeUtil.FormetFileSize(SDCardUtils.getTotalInternalMemorySize()) + "，" + "当前可用空间" + FileSizeUtil.FormetFileSize(SDCardUtils.getAvailableInternalMemorySize());
            memory_tv.setText("总空间" + phoneMemory);

            double resultDouble = ((double) SDCardUtils.getAvailableInternalMemorySize()) / ((double) SDCardUtils.getTotalInternalMemorySize());
            int resultInt = (int) (resultDouble * 100);
            memory_progress.setProgress(100 - resultInt);
        } else {//SD卡
            /*String sdcardMemory = FileSizeUtil.FormetFileSize(getAllSize()) + " / " + "剩余" + FileSizeUtil.FormetFileSize(getAvailableExternalMemorySize());
            memory_tv.setText("SD卡存储：总空间" + sdcardMemory);*/

            String sdcardMemory = FileSizeUtil.FormetFileSize(getAllSize()) + "，" + "当前可用空间" + FileSizeUtil.FormetFileSize(getAvailableExternalMemorySize());
            memory_tv.setText("总空间" + sdcardMemory);

            double resultDouble = ((double) getAvailableExternalMemorySize()) / ((double) getAllSize());
            int resultInt = (int) (resultDouble * 100);
            memory_progress.setProgress(100 - resultInt);
        }
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
            deleteVideo_tv.setClickable(false);
            deleteVideo_tv.setTextColor(getResources().getColor(R.color.activity_title_view));
            download_memory.setVisibility(View.GONE);
        } else if (listViewCheckBoxStatus == listViewCheckBoxStatusVisible) {
            listViewCheckBoxStatus = listViewCheckBoxStatusNone;
            download_bottom.setVisibility(View.GONE);
//            download_topRightImg.setSelected(false);
            download_topRightTxt.setText("编辑");
            //清空选中的信息
            selectAllCheck_tv.setText("全选");
            deleteVideo_tv.setText("删除");
            download_memory.setVisibility(View.VISIBLE);
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
     * name：查找所有ID
     * author：MrSong
     * data：2016/6/23 14:58
     */
    private void findViewById() {
        super.findId();
        super.setActivity_title_name(R.string.download_list);
        super.setClick();
        super.setActivity_tvnodata("您还没有下载视频");
        super.setActivity_btnnodata("去下载");
        super.btn_nodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Download.this, CustomActivity.class);
                intent.putExtra("video_class", getResources().getString(R.string.recommendForMe));
                intent.putExtra("mode", "2");
                startActivity(intent);
            }
        });
        listview = (ListView) findViewById(R.id.download_listview);
//        layout_nodata = (NodataEmptyLayout) findViewById(R.id.nodata_empty_layout);
        download_bottom = (LinearLayout) findViewById(R.id.download_bottom);
        download_top_downloading = (LinearLayout) findViewById(R.id.download_top_downloading);
        downloading = (TextView) findViewById(R.id.download_downloading);
//        download_topRightImg = (ImageView) findViewById(R.id.download_topRightImg);
        download_topRightTxt = (TextView) findViewById(R.id.download_topRightTxt);
        deleteVideo_tv = (TextView) findViewById(R.id.delete_video);
        selectAllCheck_tv = (TextView) findViewById(R.id.selectAllCheck);
        memory_tv = (TextView) findViewById(R.id.memory_tv);
        memory_progress = (ProgressBar) findViewById(R.id.memory_progress);
        download_memory = (RelativeLayout) findViewById(R.id.download_memory);
    }

    /**
     * name：查询多少个任务正在下载
     * author：MrSong
     * data：2016/6/28 15:58
     */
    private void findDownloading() {
        List<DbDownloadVideo> downIng = MyDbUtils.findDownloading(context);
        if (downIng != null) {
            if (downIng.size() != 0) {
                downloading.setText(downIng.size() + "个任务正在下载");
                download_top_downloading.setVisibility(View.VISIBLE);
                layout_nodata.setVisibility(View.GONE);
                download_topRightTxt.setVisibility(View.VISIBLE);
            } else {
                download_top_downloading.setVisibility(View.GONE);
            }
        }
    }

    /**
     * name：查询所有已下载完成视频类型
     * author：MrSong
     * data：2016/6/23 15:24
     */
    private void findAllDownload() {
        list.clear();

        if (MyDbUtils.findAll(context) == null) {
            listview.setVisibility(View.GONE);
            layout_nodata.setVisibility(View.VISIBLE);
            download_topRightTxt.setVisibility(View.GONE);
//            download_topRightImg.setVisibility(View.GONE);
            return;
        }
        List<DbModel> group = MyDbUtils.findAllGroup(context);

        if (group != null) {
            if (group.size() != 0) {
                for (int i = 0; i < group.size(); i++) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("picUrl", group.get(i).getString("picUrl"));
                    map.put("videoClass", group.get(i).getString("videoClass"));
                    map.put("number", group.get(i).getString("count") + "个视频 / " + group.get(i).getString("sum") + "MB");
                    list.add(map);
                    IsSelectedEntity ise = new IsSelectedEntity();
                    ise.setIsSelectedStatus(false);
                    isSelected.put(i, ise);
                }
            } else {
                layout_nodata.setVisibility(View.VISIBLE);
                download_topRightTxt.setVisibility(View.GONE);
//                download_topRightImg.setVisibility(View.GONE);
            }
        } else {
            layout_nodata.setVisibility(View.VISIBLE);
            download_topRightTxt.setVisibility(View.GONE);
//            download_topRightImg.setVisibility(View.GONE);
        }
        if (adapter == null) {
            adapter = new DownloadAdapter();
            listview.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
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
                ise.setVideoClass(list.get(i).get("videoClass"));
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
                iiistr = isSelected.get(i).getVideoClass();
            }
        }
        View views = getLayoutInflater().inflate(R.layout.download_video_delete_dialog, null);
        TextView textview = (TextView) views.findViewById(R.id.tv_showstr);
        if (iii == 1) {
            textview.setText("确定删除 " + iiistr + " ？");
        } else {
            textview.setText("确定删除" + iii + "个视频类型？");
        }
        DeleteDialog deleteDialog = new DeleteDialog(context, R.style.mystyle, views);
        deleteDialog.show();
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
                            deleteFileAndDB(ise.getVideoClass());
                        }
                    }
                    Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                    dismiss();
                    //重新刷新页面
                    checkNum = 0;
                    listViewCheckBoxStatus = listViewCheckBoxStatusVisible;
                    allAndDelete();
                    //查询所有已下载完成视频类型
                    findAllDownload();

                    //查询多少个任务正在下载
                    findDownloading();
                    break;
            }

        }

        private void deleteFileAndDB(String videoClass) {
            if (videoClass != null && videoClass.length() > 0) {
                List<DbDownloadVideo> videos = MyDbUtils.findGroupMsg(context, videoClass);
                for (int i = 0; i < videos.size(); i++) {
                    MyDbUtils.deleteVideo(context, videos.get(i).getVideoId());
                    File file = new File(videos.get(i).getFilePath());
                    if (file.exists()) {// 判断文件是否存在
                        if (file.isFile()) {// 判断是否是文件
                            file.delete();
                        }
                    } else {
//                        Toast.makeText(context, "文件不存在", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    /*********************************************************************************************
     * 以下为adapter
     *********************************************************************************************/
    class DownloadAdapter extends BaseAdapter {

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

                convertView = LayoutInflater.from(context).inflate(R.layout.upload_download_listgroup, null);
                holder.img = (ImageView) convertView.findViewById(R.id.down_item_group_img);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.down_item_group_select);
                holder.videoClass = (TextView) convertView.findViewById(R.id.down_item_group_title);
                holder.number = (TextView) convertView.findViewById(R.id.down_item_group_number);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (list.size() != 0) {
                holder.videoClass.setText(list.get(position).get("videoClass"));
                holder.number.setText(list.get(position).get("number"));
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
            TextView videoClass;
            TextView number;
        }
    }

    class IsSelectedEntity {
        boolean isSelectedStatus;
        String videoClass;

        IsSelectedEntity() {

        }

        public boolean getIsSelectedStatus() {
            return isSelectedStatus;
        }


        public void setIsSelectedStatus(boolean isSelectedStatus) {
            this.isSelectedStatus = isSelectedStatus;
        }

        public String getVideoClass() {
            return videoClass;
        }

        public void setVideoClass(String videoClass) {
            this.videoClass = videoClass;
        }

        @Override
        public String toString() {
            return "IsSelectedEntity{" +
                    "isSelectedStatus=" + isSelectedStatus +
                    ", videoClass='" + videoClass + '\'' +
                    '}';
        }
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

    @Override
    protected void onStart() {
        super.onStart();
        //查询所有已下载完成视频类型
        findAllDownload();

        //查询多少个任务正在下载
        findDownloading();
    }
}
