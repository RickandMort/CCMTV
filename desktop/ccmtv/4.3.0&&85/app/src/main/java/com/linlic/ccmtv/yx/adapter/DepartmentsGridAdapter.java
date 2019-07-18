package com.linlic.ccmtv.yx.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.holder.BaseViewHolder;

import java.util.List;
import java.util.Map;

/**
 * @Description:gridview的Adapter
 * @author http://blog.csdn.net/finddreams 
 */
public class DepartmentsGridAdapter extends BaseAdapter {
	private Context mContext;
	public List<Map<String,Object>> departments;
	//当前Item被点击的位置
	private int currentItem;
	public void setCurrentItem(int currentItem) {
		this.currentItem = currentItem;
	}

	public int getCurrentItem() {
		return currentItem;
	}

	public DepartmentsGridAdapter(Context mContext, List<Map<String,Object>> departments ) {

		this.mContext = mContext;
		this.departments = departments;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return departments.size();
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
					R.layout.department_list_item2, parent, false);
		}
		TextView department_name = BaseViewHolder.get(convertView, R.id.department_name);
		LinearLayout department_layout = BaseViewHolder.get(convertView, R.id.department_layout);
		/*if(departments.get(position).get("department_name").toString().length()>4){
			department_name.setPadding(0,0,0,0);
		}else{
			department_name.setPadding(0,25,0,25);
		}*/
		department_name.setText(departments.get(position).get("department_name").toString()  );

		TextView department_id = BaseViewHolder.get(convertView, R.id.department_id);
		department_id.setText(departments.get(position).get("department_id").toString());
/*

		if (currentItem == position) {
			//如果被点击，设置当前TextView被选中
			department_name.setSelected(true);
			department_layout.setBackgroundColor(Color.parseColor("#ebf4fe"));
		} else {
			//如果没有被点击，设置当前TextView未被选中
			department_name.setSelected(false);
			department_layout.setBackgroundColor(Color.parseColor("#ffffff"));
		}
*/

		if (departments.get(position).get("department_type").toString().equals("1")) {
			//如果被点击，设置当前TextView被选中
			department_name.setSelected(true);
			department_layout.setBackground(mContext.getResources().getDrawable(R.mipmap.practice_main_icon09));
		} else {
			//如果没有被点击，设置当前TextView未被选中
			department_name.setSelected(false);
			department_layout.setBackground(mContext.getResources().getDrawable(R.mipmap.practice_main_icon08));
		}

		return convertView;
	}

}
