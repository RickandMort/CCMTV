package com.linlic.ccmtv.yx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;

import java.util.List;
import java.util.Map;

public class MyAdapter extends BaseAdapter {
	
	private Context mContext ;
	private List<Map<String,Object>> mList;
	
	public MyAdapter(Context context ,List<Map<String,Object>> list){
		this.mContext = context;
		this.mList = list;
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);   
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holderView = null;
		if(convertView == null ){
			holderView = new HolderView();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.transverselistview_item, null);
			
			holderView.imageView =(ImageView) convertView.findViewById(R.id.imageView);
			holderView.textView = (TextView) convertView.findViewById(R.id.textView);
			
			convertView.setTag(holderView);
		}else{
			holderView = (HolderView) convertView.getTag();
		}
	

		holderView.imageView.setImageResource((Integer) mList.get(position).get("img"));
		holderView.textView.setText((String) mList.get(position).get("index"));
		
		//return convertView.findViewById(R.id.item);
		return convertView;
	}
	
	class HolderView{
		ImageView imageView;
		TextView textView;
	}
}
