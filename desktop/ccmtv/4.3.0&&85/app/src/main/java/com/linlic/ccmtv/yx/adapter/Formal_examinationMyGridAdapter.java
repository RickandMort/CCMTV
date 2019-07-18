package com.linlic.ccmtv.yx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.holder.BaseViewHolder;
import com.linlic.ccmtv.yx.util.ImageLoader;
import com.linlic.ccmtv.yx.utils.FirstLetter;

import java.util.List;

/**
 * @Description:gridview的Adapter
 * @author http://blog.csdn.net/finddreams 
 */
public class Formal_examinationMyGridAdapter extends BaseAdapter {
	private Context mContext;
	public List<String> thumbnail;
	public  List<String> imgs;

	public Formal_examinationMyGridAdapter(Context mContext, List<String> thumbnail, List<String> imgs) {

		this.mContext = mContext;
		this.thumbnail = thumbnail;
		this.imgs = imgs;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return thumbnail.size();
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
					R.layout.formal_examination_mygridadapter_item, parent, false);
		}
		ImageView tv = BaseViewHolder.get(convertView, R.id.tv_item);
		TextView tText = BaseViewHolder.get(convertView, R.id.tv_item_text);
//		ImageLoader.load(mContext, thumbnail.get(position), tv);
		com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(FirstLetter.getSpells(thumbnail.get(position)), tv);
		tv.setTag(imgs.get(position));
//		tText.setText("图片"+(position+1));
		return convertView;
	}

}
