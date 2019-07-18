package com.linlic.ccmtv.yx.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.db.MyDbUtils;
import com.linlic.ccmtv.yx.activity.entity.Option;
import com.linlic.ccmtv.yx.activity.entity.Problem;
import com.linlic.ccmtv.yx.holder.BaseViewHolder;

/**
 * @Description:gridview的Adapter
 * @author http://blog.csdn.net/finddreams 
 */
public class Common_answer_questionGridAdapter2 extends BaseAdapter {
	private Context mContext;
	public Problem problem;
	public int problem_position;


	public Common_answer_questionGridAdapter2(Context mContext, Problem problem, int problem_position, String eid) {

		this.mContext = mContext;
		this.problem = problem;
		this.problem_position = problem_position;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return problem.getOptions().size();
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
					R.layout.common_answer_question_mygridadapter_item, parent, false);
		}

		TextView radio_id = BaseViewHolder.get(convertView, R.id.radio_id);
		TextView radio_text = BaseViewHolder.get(convertView, R.id.radio_text);
		TextView radio_type = BaseViewHolder.get(convertView, R.id.radio_type);
		FrameLayout item_frame = BaseViewHolder.get(convertView, R.id.item_frame);
		radio_id.setTag(problem.getTitle_serial_number());
		radio_text.setText(problem.getOptions().get(position).getOption_id());
		radio_text.setTag(problem_position);
		radio_type.setText(problem.getQuestion_type());
		radio_type.setTag(position);
		if(Integer.parseInt(problem.getOptions().get(position).getOption_type()) ==0){
			radio_id.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_07));
			radio_text.setTextColor(mContext.getResources().getColor(R.color.no_select_text_color));
		}else if (Integer.parseInt(problem.getOptions().get(position).getOption_type()) ==1){
			radio_id.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_09));
			radio_text.setTextColor(mContext.getResources().getColor(R.color.check_the_answer_sheet_item_text));
		}else if (Integer.parseInt(problem.getOptions().get(position).getOption_type()) ==2){
			radio_id.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_08));
			radio_text.setTextColor(mContext.getResources().getColor(R.color.exams_list_item_text_color8));
		}else if (Integer.parseInt(problem.getOptions().get(position).getOption_type()) ==3){
			radio_id.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_07));
			radio_text.setTextColor(mContext.getResources().getColor(R.color.check_the_answer_sheet_item_text));
		}
		item_frame.setClickable(false);

		return convertView;
	}

}
