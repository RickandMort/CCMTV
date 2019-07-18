package com.linlic.ccmtv.yx.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.entity.Home_Video;
import com.linlic.ccmtv.yx.activity.vip.adapter.OnRecyclerviewItemClickListener;
import com.linlic.ccmtv.yx.utils.FirstLetter;

import java.util.List;

/**
 * @Description:gridview的Adapter
 * @author http://blog.csdn.net/finddreams 
 */
public class Videos_expertAdapter2 extends  RecyclerView.Adapter<Videos_expertAdapter2.ViewHolder> {
	private LayoutInflater mInflater;
	private int selectIndex = -1;
	//声明自定义的监听接口
	private OnRecyclerviewItemClickListener mOnRecyclerviewItemClickListener = null;
	private Context mContext;
	public List<Home_Video> videos ;
	private boolean isShow = false;
	public Videos_expertAdapter2(Context mContext, List<Home_Video> videos , OnRecyclerviewItemClickListener mOnRecyclerviewItemClickListener) {
		mInflater = LayoutInflater.from(mContext);
		this.mContext = mContext;
		this.videos = videos;
		this.mOnRecyclerviewItemClickListener = mOnRecyclerviewItemClickListener;
	}



	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}



	public class ViewHolder extends RecyclerView.ViewHolder {
		public ViewHolder(View arg0) {
			super(arg0);
		}

		TextView video_aid = null;//视频ID
		ImageView video_cover_chart = null;//视频封面图
		ImageView video_cover_chart_ispay = null;//角标
		TextView video_title = null;//视频名称
	}

	@Override
	public int getItemCount() {
		return videos.size();
	}

	/**
	 * 创建ViewHolder
	 */
	@Override
	public  ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {

		View view = mInflater.inflate(R.layout.home_grid_item2, viewGroup, false);
	 ViewHolder viewHolder = new ViewHolder(view);
		viewHolder.video_aid = (TextView) view
				.findViewById(R.id.video_aid);
		viewHolder.video_title = (TextView) view
				.findViewById(R.id.video_title);
		viewHolder.video_cover_chart = (ImageView) view
				.findViewById(R.id.video_cover_chart);
		viewHolder.video_cover_chart_ispay = (ImageView) view
				.findViewById(R.id.video_cover_chart_ispay);


		//这里 我们可以拿到点击的item的view 对象，所以在这里给view设置点击监听，
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mOnRecyclerviewItemClickListener.onItemClickListener(view,i);
			}
		});
		return viewHolder;
	}

	/**
	 * 设置值
	 */
	@Override
	public void onBindViewHolder(final  ViewHolder viewHolder, final int position) {
		//第一步设置视频ID
		viewHolder.video_aid.setText(videos.get(position).getAid());
		//第二步设置视频标题
		viewHolder.video_title.setText(videos.get(position).getTitle());
		//第三部设置视频封面图
//		com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(FirstLetter.getSpells(videos.get(position).getPicurl()), video_cover_chart);
		RequestOptions options = new RequestOptions()
				.placeholder(R.mipmap.img_default)
				.error(R.mipmap.img_default);
		Glide.with(mContext)
				.load(FirstLetter.getSpells(videos.get(position).getPicurl()))
				.apply(options)
				.into(viewHolder.video_cover_chart);

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
			viewHolder.video_cover_chart_ispay.setVisibility(View.VISIBLE);
			viewHolder.video_cover_chart_ispay.setImageResource(R.mipmap.charge);
		} else{
			viewHolder.video_cover_chart_ispay.setVisibility(View.GONE);
			//判断 Flag 是否等于3  3为VIP视频  非3为非会员视频
			if (videos.get(position).getFlag().equals("3")) {
				//会员
				viewHolder.video_cover_chart_ispay.setVisibility(View.VISIBLE);
				viewHolder.video_cover_chart_ispay.setImageResource(R.mipmap.vip_img);
			}else {
				viewHolder.video_cover_chart_ispay.setVisibility(View.GONE);
			}
		}
	}

//	@Override
//	public void onClick(View v) {
//		//将监听传递给自定义接口
//		mOnRecyclerviewItemClickListener.onItemClickListener(v, ((int) v.getTag()));
//	}


	public void setSelectIndex(int i) {
		selectIndex = i;
	}
	public void setIsShowRedDot(boolean isShow) {
		this.isShow = isShow;
	}





}
