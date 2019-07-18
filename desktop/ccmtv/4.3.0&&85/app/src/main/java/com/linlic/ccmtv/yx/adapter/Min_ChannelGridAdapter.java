package com.linlic.ccmtv.yx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.entity.Min_Channel;
import com.linlic.ccmtv.yx.holder.BaseViewHolder;
import com.linlic.ccmtv.yx.utils.FirstLetter;

import java.util.List;

/**
 * @Description:gridview的Adapter
 * @author http://blog.csdn.net/finddreams 
 */
public class Min_ChannelGridAdapter extends BaseAdapter {
	private Context mContext;
	public List<Min_Channel> min_channels;

	public Min_ChannelGridAdapter(Context mContext, List<Min_Channel> min_channels ) {

		this.mContext = mContext;
		this.min_channels = min_channels;

	}

	public List<Min_Channel> getDatas(){
		return min_channels;
	}

	public void setMin_channels( List<Min_Channel> min_channels ){
		this.min_channels = min_channels;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return min_channels.size();
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
					R.layout.min_channel_item, parent, false);
		}
		ImageView min_channel_icon = BaseViewHolder.get(convertView, R.id.min_channel_icon);
		TextView min_channel_title = BaseViewHolder.get(convertView, R.id.min_channel_title);
		TextView min_channel_id = BaseViewHolder.get(convertView, R.id.min_channel_id);
		View linne3 = BaseViewHolder.get(convertView, R.id.linne3);
		min_channel_id.setText(min_channels.get(position).getId());
		min_channel_title.setText(min_channels.get(position).getTitle());
		com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(FirstLetter.getSpells(min_channels.get(position).getIcon()), min_channel_icon);
		if((position+1)%3==0){
			linne3.setVisibility(View.INVISIBLE);
		}else{
			linne3.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

}
