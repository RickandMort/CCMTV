package com.linlic.ccmtv.yx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.entity.Section;
import com.linlic.ccmtv.yx.activity.entity.VideoModel;
import com.linlic.ccmtv.yx.holder.BaseViewHolder;
import com.linlic.ccmtv.yx.util.ImageLoader;

import java.util.List;

/**
 * @Description:gridview的Adapter
 * @author http://blog.csdn.net/finddreams 
 */
public class MyGridAdapter4 extends BaseAdapter {
	private Context mContext;
	public List<Section> videos;


	public MyGridAdapter4(Context mContext, List<Section> videos) {

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
					R.layout.hospital_training_main_gridview_item, parent, false);
		}
		TextView section_name = BaseViewHolder.get(convertView, R.id.section_name);
		TextView section_type = BaseViewHolder.get(convertView, R.id.section_type);
		section_name.setText(videos.get(position).getText()+" ");
		section_type.setText(videos.get(position).getType());
		LinearLayout section_laout = BaseViewHolder.get(convertView, R.id.section_laout);
		switch (position){
			case 0:
				section_laout.setBackground(mContext.getResources().getDrawable(R.mipmap.section_img01));
				break;
			case 1:
				section_laout.setBackground(mContext.getResources().getDrawable(R.mipmap.section_img02));
				break;
			case 2:
				section_laout.setBackground(mContext.getResources().getDrawable(R.mipmap.section_img03));
				break;
			case 3:
				section_laout.setBackground(mContext.getResources().getDrawable(R.mipmap.section_img04));
				break;
			default:
				section_laout.setBackground(mContext.getResources().getDrawable(R.mipmap.section_img01));
				break;
		}

		return convertView;
	}

}
