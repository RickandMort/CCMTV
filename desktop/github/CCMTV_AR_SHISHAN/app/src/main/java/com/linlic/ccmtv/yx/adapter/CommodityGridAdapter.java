package com.linlic.ccmtv.yx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.entity.Commodity;
import com.linlic.ccmtv.yx.holder.BaseViewHolder;
import com.linlic.ccmtv.yx.utils.FirstLetter;

import java.util.List;

/**
 * @Description:gridview的Adapter
 * @author http://blog.csdn.net/finddreams 
 */
public class CommodityGridAdapter extends BaseAdapter {
	private Context mContext;
	public List<Commodity> commodities;


	public CommodityGridAdapter(Context mContext, List<Commodity> commodities) {

		this.mContext = mContext;
		this.commodities = commodities;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return commodities.size();
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
					R.layout.gift_gridview_item, parent, false);
		}
		ImageView item_img = BaseViewHolder.get(convertView, R.id.item_img);
		TextView item_name = BaseViewHolder.get(convertView, R.id.item_name);
		TextView integral_text1 = BaseViewHolder.get(convertView, R.id.integral_text1);
		TextView commodity_id = BaseViewHolder.get(convertView, R.id.commodity_id);
		com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(FirstLetter.getSpells(commodities.get(position).getUri()), item_img);
		integral_text1.setText(commodities.get(position).getMoney());
		item_name.setText( commodities.get(position).getName());
		commodity_id.setText(commodities.get(position).getId());
		return convertView;
	}

}
