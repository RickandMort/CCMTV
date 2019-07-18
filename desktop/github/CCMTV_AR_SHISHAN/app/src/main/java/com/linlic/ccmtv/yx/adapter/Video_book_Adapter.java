package com.linlic.ccmtv.yx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.entity.Video_book_Entity;
import com.linlic.ccmtv.yx.holder.BaseViewHolder;
import com.linlic.ccmtv.yx.utils.FirstLetter;

import java.util.List;

/**
 * @Description:gridview的Adapter
 * @author http://blog.csdn.net/finddreams 
 */
public class Video_book_Adapter extends BaseAdapter {
	private Context mContext;
	public List<Video_book_Entity> video_book_entities;


	public Video_book_Adapter(Context mContext, List<Video_book_Entity>  video_book_entities ) {

		this.mContext = mContext;
		this.video_book_entities = video_book_entities;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return video_book_entities.size();
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
					R.layout.video_book_gridadapter_item, parent, false);
		}
		ImageView cover_chart = BaseViewHolder.get(convertView, R.id.cover_chart);
		TextView name = BaseViewHolder.get(convertView, R.id.name);
		TextView estate = BaseViewHolder.get(convertView, R.id.estate);
		com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(FirstLetter.getSpells(video_book_entities.get(position).getCover_chart()), cover_chart);
		cover_chart.setTag(video_book_entities.get(position).getId());
		name.setText(video_book_entities.get(position).getName());
		estate.setText(video_book_entities.get(position).getEstate());
		return convertView;
	}

}
