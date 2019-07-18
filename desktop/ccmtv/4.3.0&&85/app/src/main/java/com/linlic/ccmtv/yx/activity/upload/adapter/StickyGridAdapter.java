package com.linlic.ccmtv.yx.activity.upload.adapter;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.entity.VideoInfo;
import com.linlic.ccmtv.yx.activity.upload.stickygridheaders.StickyGridHeadersSimpleAdapter;
import com.linlic.ccmtv.yx.activity.upload.util.MyImageView;
import com.linlic.ccmtv.yx.utils.FileSizeUtil;

import java.io.File;
import java.util.List;


/**
 * StickyHeaderGridView的适配器，除了要继承BaseAdapter之外还需要
 * 实现StickyGridHeadersSimpleAdapter接口
 *
 * @author xiaanming
 * @blog http://blog.csdn.net/xiaanming
 */
public class StickyGridAdapter extends BaseAdapter implements
        StickyGridHeadersSimpleAdapter {

    private Context context;
    private List<VideoInfo> hasHeaderIdList;
    private LayoutInflater mInflater;
    private GridView mGridView;
    private Point mPoint = new Point(0, 0);//用来封装ImageView的宽和高的对象

    public StickyGridAdapter(Context context, List<VideoInfo> hasHeaderIdList,
                             GridView mGridView) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.mGridView = mGridView;
        this.hasHeaderIdList = hasHeaderIdList;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return hasHeaderIdList.size();
    }

    @Override
    public Object getItem(int position) {
        return hasHeaderIdList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.grid_item, parent, false);
            mViewHolder.mImageView = (MyImageView) convertView
                    .findViewById(R.id.grid_item);
            mViewHolder.tvVideoDuration = (TextView) convertView.findViewById(R.id.tv_duration);
            convertView.setTag(mViewHolder);

            //用来监听ImageView的宽和高
            mViewHolder.mImageView.setOnMeasureListener(new MyImageView.OnMeasureListener() {

                @Override
                public void onMeasureSize(int width, int height) {
                    mPoint.set(width, height);
                }
            });

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        String path = hasHeaderIdList.get(position).thumbPath;
        mViewHolder.mImageView.setTag(path);
        mViewHolder.tvVideoDuration.setText(hasHeaderIdList.get(position).duration);

        /*Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(path, mPoint,
                new NativeImageLoader.NativeImageCallBack() {

                    @Override
                    public void onImageLoader(Bitmap bitmap, String path) {
                        ImageView mImageView = (ImageView) mGridView
                                .findViewWithTag(path);
                        if (bitmap != null && mImageView != null) {
                            mImageView.setImageBitmap(bitmap);
                        }
                    }
                });

        if (bitmap != null) {
            mViewHolder.mImageView.setImageBitmap(bitmap);
        } else {
            mViewHolder.mImageView.setImageResource(R.drawable.ic_launcher);
        }*/

        try {
            mViewHolder.mImageView.setTag(null);
            RequestOptions options = new RequestOptions()
                    .error(R.mipmap.img_default)
                    .override(100, 85);
            Glide.with(context)
                    .load(Uri.fromFile(new File(hasHeaderIdList.get(position).filePath)))
                    .apply(options)
                    .into(mViewHolder.mImageView);
        } catch (Exception e) {
            e.printStackTrace();
            mViewHolder.mImageView.setImageResource(R.mipmap.img_default);
        }

        return convertView;
    }


    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder mHeaderHolder;

        if (convertView == null) {
            mHeaderHolder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.header, parent, false);
            mHeaderHolder.mTextView = (TextView) convertView
                    .findViewById(R.id.header);
            convertView.setTag(mHeaderHolder);
        } else {
            mHeaderHolder = (HeaderViewHolder) convertView.getTag();
        }
        mHeaderHolder.mTextView.setText(hasHeaderIdList.get(position).getTime());

        return convertView;
    }

    /**
     * 获取HeaderId, 只要HeaderId不相等就添加一个Header
     */
    @Override
    public long getHeaderId(int position) {
        return hasHeaderIdList.get(position).getHeaderId();
    }


    public static class ViewHolder {
        public MyImageView mImageView;
        public TextView tvVideoDuration;
    }

    public static class HeaderViewHolder {
        public TextView mTextView;
    }


}