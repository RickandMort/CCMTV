package com.linlic.ccmtv.yx.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.holder.BaseViewHolder;

import java.util.List;

/**
 * @Description:gridview的Adapter
 * @author http://blog.csdn.net/finddreams 
 */
public class MyGridAdapter2 extends BaseAdapter {
	private Context mContext;
	public List<String> img_text;
	public  List<Integer> imgs;

	public MyGridAdapter2(Context mContext, List<String> texts, List<Integer> imgs) {

		this.mContext = mContext;
		this.img_text = texts;
		this.imgs = imgs;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return img_text.size();
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
					R.layout.department_columnwidth2, parent, false);
		}
		TextView tv = BaseViewHolder.get(convertView, R.id.tv_item);
		switch (imgs.get(position)){
			case 1:
				tv.setTextColor(Color.parseColor("#ffffff"));
				tv.setBackground(mContext.getResources().getDrawable(R.drawable.anniu17));
				break;
			case 0:
				tv.setTextColor(Color.parseColor("#3798F9"));
				tv.setBackground(mContext.getResources().getDrawable(R.drawable.anniu18));
				break;

			default:
				break;
		}

		tv.setText(img_text.get(position));
		return convertView;
	}

}
