package com.linlic.ccmtv.yx.activity.upload;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.VideoInfo;
import com.linlic.ccmtv.yx.activity.upload.adapter.StickyGridAdapter;
import com.linlic.ccmtv.yx.activity.upload.stickygridheaders.StickyGridHeadersGridView;
import com.linlic.ccmtv.yx.activity.upload.util.YMDComparator;
import com.linlic.ccmtv.yx.util.RxFileUtil;
import com.linlic.ccmtv.yx.utils.FileSizeUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TimeZone;

/**
 * name：我们将通过检索SDCard上的Video信息 在MediaStore中，MediaStore. .Media中就有Video相关信息，
 * 同时MediaStore. .Thumbnails中含有各个video对应的缩略图信息
 * author：Larry
 * data：2016/6/7.
 * http://blog.csdn.net/chenjie19891104/article/details/6338910
 */
public class ChooseVideoActivity extends BaseActivity {
    ArrayList<VideoInfo> videoList = new ArrayList<VideoInfo>();
    private Cursor cursor;
    VideoAdapter adapter;
    Context context;
    private StickyGridHeadersGridView mGridView;
    private RelativeLayout lt_nodata1;
    private TextView title_name;
    private List<VideoInfo> videoInfos =new ArrayList<>();
    private StickyGridAdapter stickyGridAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_choosevideo);
        context = this;
        mGridView = (StickyGridHeadersGridView) findViewById(R.id.asset_grid);
        lt_nodata1 = (RelativeLayout) findViewById(R.id.lt_nodata1);
        title_name = (TextView) findViewById(R.id.activity_title_name);
        title_name.setText("选择视频");
        //list_video = (ListView) findViewById(R.id.list_video);
        // 然后需要设置ListView的Adapter了，使用我们自定义的Adatper
        //adapter = new VideoAdapter(this, videoList);
        //list_video.setAdapter(adapter);
        init();
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent();
                mIntent.putExtra("videopath", videoList.get(position).filePath);
                mIntent.putExtra("videoimg", videoList.get(position).thumbPath);
                mIntent.putExtra("videotitle", videoList.get(position).title);
                // 设置结果，并进行传送
                setResult(30, mIntent);
                ChooseVideoActivity.this.finish();
            }
        });
        /*list_video.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent mIntent = new Intent();
                mIntent.putExtra("videopath", videoList.get(position).filePath);
                mIntent.putExtra("videoimg", videoList.get(position).thumbPath);
                mIntent.putExtra("videotitle", videoList.get(position).title);
                // 设置结果，并进行传送
                setResult(30, mIntent);
                ChooseVideoActivity.this.finish();
            }
        });*/
    }

    private void init() {
        videoList.clear();
        videoInfos.clear();

        String[] thumbColumns = new String[]{
                MediaStore.Video.Thumbnails.DATA,
                MediaStore.Video.Thumbnails.VIDEO_ID};

        String[] mediaColumns = new String[]{MediaStore.Video.Media.DATA,
                MediaStore.Video.Media._ID, MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.MIME_TYPE, MediaStore.Video.Media.DATE_ADDED, MediaStore.Video.Media.DURATION};

        try {
            // 首先检索SDcard上所有的video
//        cursor = this.managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns, null, null, null);
            cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    String filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));// 路径

                    if (!FileSizeUtil.isExists(filePath)) {
                        continue;
                    }
                    /**
                     * 视频列表显示方式修改（日期列表加九宫格）
                     */
                    VideoInfo info = new VideoInfo();

                    info.filePath = filePath;
                    info.mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));

                    info.title = info.filePath.substring(info.filePath.lastIndexOf('/') + 1);

                    long time = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED));
                    long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                    info.duration = FileSizeUtil.formatLongToTimeStr(duration);
                    info.setTime(paserTimeToYMD(time, "yyyy.MM"));

                    // 获取当前Video对应的Id，然后根据该ID获取其Thumb
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                    String selection = MediaStore.Video.Thumbnails.VIDEO_ID + "=?";
                    String[] selectionArgs = new String[]{id + ""};
                    Cursor thumbCursor = this.managedQuery(
                            MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                            thumbColumns, selection, selectionArgs, null);

                    if (thumbCursor.moveToFirst()) {
                        info.thumbPath = thumbCursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA));
                    }

                    // 然后将其加入到videoList
                    // Log.i("info", info.filePath.substring(info.filePath.lastIndexOf('.') + 1));
                    //if (("mp4").equals(info.filePath.substring(info.filePath.lastIndexOf('.') + 1)) || ("MP4").equals(info.filePath.substring(info.filePath.lastIndexOf('.') + 1))) {
                    if (duration>0){
                        videoList.add(info);
                    }

                    //给GridView的item的数据生成HeaderId
                    videoInfos = generateHeaderId(videoList);
                    //排序
                    Collections.sort(videoInfos, new YMDComparator());
                } while (cursor.moveToNext());
            }
            if (videoInfos.size() > 0) {
                lt_nodata1.setVisibility(View.GONE);
                mGridView.setVisibility(View.VISIBLE);
                stickyGridAdapter = new StickyGridAdapter(ChooseVideoActivity.this, videoInfos, mGridView);
                mGridView.setAdapter(stickyGridAdapter);
                stickyGridAdapter.notifyDataSetChanged();
            } else {
                lt_nodata1.setVisibility(View.VISIBLE);
                mGridView.setVisibility(View.GONE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * 定义一个Adapter来显示缩略图和视频title信息
     *
     * @author Administrator
     */
    static class VideoAdapter extends BaseAdapter {

        private Context context;
        private List<VideoInfo> videoItems;

        public VideoAdapter(Context context, List<VideoInfo> data) {
            this.context = context;
            this.videoItems = data;
        }

        @Override
        public int getCount() {
            return videoItems.size();
        }

        @Override
        public Object getItem(int p) {
            return videoItems.get(p);
        }

        @Override
        public long getItemId(int p) {
            return p;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.video_item, null);
                holder.thumbImage = (ImageView) convertView.findViewById(R.id.thumb_image);
                holder.titleText = (TextView) convertView.findViewById(R.id.video_title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // 显示信息`
            holder.titleText.setText(videoItems.get(position).title);
            holder.titleText.setTag(videoItems.get(position).filePath);
            try {
                if (videoItems.get(position).thumbPath != null) {
                    holder.thumbImage.setImageURI(Uri.parse(videoItems.get(position).thumbPath));
                }
            } catch (Exception e) {
                holder.thumbImage.setImageResource(R.mipmap.img_default);
            }
            return convertView;
        }

        class ViewHolder {
            ImageView thumbImage;
            TextView titleText;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn/Member/Index.html";
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 对GridView的Item生成HeaderId, 根据图片的添加时间的年、月、日来生成HeaderId
     * 年、月、日相等HeaderId就相同
     *
     * @param nonHeaderIdList
     * @return
     */
    private List<VideoInfo> generateHeaderId(List<VideoInfo> nonHeaderIdList) {
        Map<String, Integer> mHeaderIdMap = new HashMap<String, Integer>();
        int mHeaderId = 1;
        List<VideoInfo> hasHeaderIdList;

        for (ListIterator<VideoInfo> it = nonHeaderIdList.listIterator(); it.hasNext(); ) {
            VideoInfo mVideoInfo = it.next();
            String ymd = mVideoInfo.getTime();
            if (!mHeaderIdMap.containsKey(ymd)) {
                mVideoInfo.setHeaderId(mHeaderId);
                mHeaderIdMap.put(ymd, mHeaderId);
                mHeaderId++;
            } else {
                mVideoInfo.setHeaderId(mHeaderIdMap.get(ymd));
            }
        }
        hasHeaderIdList = nonHeaderIdList;

        return hasHeaderIdList;
    }


    /**
     * 将毫秒数装换成pattern这个格式，我这里是转换成年月日
     *
     * @param time
     * @param pattern
     * @return
     */
    public static String paserTimeToYMD(long time, String pattern) {
        System.setProperty("user.timezone", "Asia/Shanghai");
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(new Date(time * 1000L));
    }

    public void back(View view) {
        finish();
    }
}
