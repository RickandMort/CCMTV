package com.linlic.ccmtv.yx.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.entity.VideoModel;
import com.linlic.ccmtv.yx.holder.BaseViewHolder;
import com.linlic.ccmtv.yx.util.ImageLoader;

import java.util.List;

/**
 * @Description:gridview的Adapter
 * @author http://blog.csdn.net/finddreams 
 */
public class MyGridAdapter3 extends BaseAdapter {
	private Context mContext;
	public List<VideoModel> videos;


	public MyGridAdapter3(Context mContext,List<VideoModel> videos) {

		this.mContext = mContext;
		this.videos = videos;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return videos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.learning_video_item, parent, false);
		}
		TextView video_id = BaseViewHolder.get(convertView, R.id.video_id);
		TextView video_name = BaseViewHolder.get(convertView, R.id.video_name);
		video_id.setText(videos.get(position).getAid());
		video_name.setText(videos.get(position).getName());
		ImageView video_img1 = BaseViewHolder.get(convertView, R.id.video_img1);
		ImageLoader.load(mContext, videos.get(position).getIconUrl(),video_img1);

		return convertView;
	}

}
