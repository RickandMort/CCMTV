package com.linlic.ccmtv.yx.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.entity.Home_Video;
import com.linlic.ccmtv.yx.holder.BaseViewHolder;
import com.linlic.ccmtv.yx.utils.CustomImageView;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.Video_menu_expert;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * @Description:gridview的Adapter
 * @author http://blog.csdn.net/finddreams 
 */
public class Video_Experts_GridAdapter extends BaseAdapter {
	private Context mContext;
	public  List<Video_menu_expert> experts  ;
	public Video_Experts_GridAdapter(Context mContext, List<Video_menu_expert> experts ) {
		this.mContext = mContext;
		this.experts = experts;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return experts.size();
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
		CustomImageView experts_img = null;//专家图片
		TextView experts_title = null;//专家title

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.video_experts_list_item, parent, false);
		}
		experts_img =BaseViewHolder.get(convertView, R.id.experts_img);
		experts_title = BaseViewHolder.get(convertView, R.id.experts_title);

		//第一步设置视频ID
		experts_title.setText(experts.get(position).getVideo_menu_expert_name().length()>0?experts.get(position).getVideo_menu_expert_name():"未知");
		//第二部设置视频封面图
//		Log.e("专家图片",experts.get(position).getVideo_menu_expert_img());
		/*Glide.with(mContext)
				.load(FirstLetter.getSpells(experts.get(position).getVideo_menu_expert_img()))
				.placeholder(R.mipmap.img_default)
				//.error(R.mipmap.icon_error)
				.error(R.mipmap.img_default)
				.into(experts_img);
*/
		ImageLoader.getInstance().displayImage(FirstLetter.getSpells(experts.get(position).getVideo_menu_expert_img()), experts_img);
		return convertView;
	}




}
