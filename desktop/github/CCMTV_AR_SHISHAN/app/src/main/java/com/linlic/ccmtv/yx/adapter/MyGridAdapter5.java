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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @Description:gridview的Adapter
 * @author http://blog.csdn.net/finddreams 
 */
public class MyGridAdapter5 extends BaseAdapter {
	private Context mContext;
	private JSONArray array;

	public MyGridAdapter5(Context mContext, JSONArray array) {

		this.mContext = mContext;
		this.array = array;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return array.length();
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

		try {
			JSONObject jsonObject = array.getJSONObject(position);
			TextView tv = BaseViewHolder.get(convertView, R.id.tv_item);
			tv.setText(jsonObject.getString("page"));
			if(jsonObject.getString("type").equals("1")){
				//主观题
				if(jsonObject.getString("judge").equals("3")){
					//作答
					tv.setTextColor(Color.parseColor("#ffffff"));
					tv.setBackground(mContext.getResources().getDrawable(R.drawable.anniu32));
				}else{
					//未作答
					tv.setTextColor(Color.parseColor("#3798F9"));
					tv.setBackground(mContext.getResources().getDrawable(R.drawable.anniu18));
				}
			}else{
				//非主观题
				switch (jsonObject.getString("judge")){
					case "0"://错误
						tv.setTextColor(Color.parseColor("#ffffff"));
						tv.setBackground(mContext.getResources().getDrawable(R.drawable.anniu15));
						break;
					case "1"://正确
						tv.setTextColor(Color.parseColor("#ffffff"));
						tv.setBackground(mContext.getResources().getDrawable(R.drawable.anniu14));
						break;
					case "2"://未作答
						tv.setTextColor(Color.parseColor("#3798F9"));
						tv.setBackground(mContext.getResources().getDrawable(R.drawable.anniu18));
						break;
					case "4"://部分正确
						tv.setTextColor(Color.parseColor("#ffffff"));
						tv.setBackground(mContext.getResources().getDrawable(R.drawable.anniu16));
						break;

					default:
						break;
				}
			}


		} catch (Exception e) {
			e.printStackTrace();
		}

		return convertView;
	}

}
