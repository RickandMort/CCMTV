package com.linlic.ccmtv.yx.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.entity.Option;
import com.linlic.ccmtv.yx.activity.entity.Problem;
import com.linlic.ccmtv.yx.utils.LogUtil;

import java.util.List;

/**
 * @Description:gridview的Adapter
 * @author http://blog.csdn.net/finddreams 
 */
public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.Holder> {
	private Context mContext;
	public List<Option> options ;

	public OptionAdapter(Context mContext, List<Option> options ) {

		this.mContext = mContext;
		this.options = options;
	}

	@Override
	public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
		// 实例化展示的view
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.option_item, parent, false);
		// 实例化viewholder
		Holder viewHolder = new Holder(v);
		return viewHolder;


	}

	@Override
	public void onBindViewHolder(Holder holder, int position)
	{
		//设置题目序号 title_serial_number
		holder.option_text.setText(Html.fromHtml(options.get(position).getOption_id()+"、"+ options.get(position).getOption_text()));//题目序号

	}

	@Override
	public int getItemCount()
	{
		return options.size();
	}



	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}


	class Holder extends RecyclerView.ViewHolder {

		TextView option_text;
 		public Holder(View view)
		{
			super(view);
			option_text = (TextView) view.findViewById(R.id.option_text);
		}
	}

}
