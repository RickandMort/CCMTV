package com.linlic.ccmtv.yx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.entity.Home_Video;
import com.linlic.ccmtv.yx.holder.BaseViewHolder;
import com.linlic.ccmtv.yx.utils.FirstLetter;

import java.util.List;

/**
 * @Description:gridview的Adapter
 * @author http://blog.csdn.net/finddreams 
 */
public class Videos_expertAdapter extends BaseAdapter {
	private Context mContext;
	public List<Home_Video> videos ;
	public Videos_expertAdapter(Context mContext, List<Home_Video> videos ) {
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
		TextView video_aid = null;//视频ID
		ImageView video_cover_chart = null;//视频封面图
		ImageView video_cover_chart_ispay = null;//角标
		TextView video_title = null;//视频名称

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.home_grid_item2, parent, false);
		}
        video_aid =BaseViewHolder.get(convertView, R.id.video_aid);
        video_title = BaseViewHolder.get(convertView, R.id.video_title);
        video_cover_chart =  BaseViewHolder.get(convertView, R.id.video_cover_chart);
        video_cover_chart_ispay = BaseViewHolder.get(convertView, R.id.video_cover_chart_ispay);
		//第一步设置视频ID
		video_aid.setText(videos.get(position).getAid());
		//第二步设置视频标题
		video_title.setText(videos.get(position).getTitle());
		//第三部设置视频封面图
//		com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(FirstLetter.getSpells(videos.get(position).getPicurl()), video_cover_chart);
		RequestOptions options = new RequestOptions().placeholder(R.mipmap.img_default)
				//.error(R.mipmap.icon_error)
				.error(R.mipmap.img_default);
		Glide.with(mContext)
				.load(FirstLetter.getSpells(videos.get(position).getPicurl()))
				.apply(options)
				.into(video_cover_chart);

		//第四步设置角标
  /*
            * 角标显示
            * 1.优先判断是否是收费视频，如果是收费视频即 显示收费角标
            * 2.非收费视频，在进行判断是否为VIP 视频
            * 3. 积分/免费视频 不显示角标
            * */
		// 先判断是否是收费视频 Videopaymoney  等于0 代表非收费视频  非0 即为收费视频
		if (!videos.get(position).getVideopaymoney().equals("0")) {
			//收费
			 video_cover_chart_ispay.setVisibility(View.VISIBLE);
			 video_cover_chart_ispay.setImageResource(R.mipmap.charge);
		} else{
			 video_cover_chart_ispay.setVisibility(View.GONE);
			//判断 Flag 是否等于3  3为VIP视频  非3为非会员视频
			if (videos.get(position).getFlag().equals("3")) {
				//会员
				 video_cover_chart_ispay.setVisibility(View.VISIBLE);
				 video_cover_chart_ispay.setImageResource(R.mipmap.vip_img);
			}else {
				 video_cover_chart_ispay.setVisibility(View.GONE);
			}
		}

		return convertView;
	}




}
