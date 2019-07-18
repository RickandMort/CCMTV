package com.linlic.ccmtv.yx.adapter;

import android.content.Context;
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
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseViewHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONObject;

/**
 * @Description:gridview的Adapter
 * @author http://blog.csdn.net/finddreams 
 */
public class Common_answer_questionGridAdapter3 extends BaseAdapter {
	private Context mContext;
	public Problem problem;
	private String eid;


	public Common_answer_questionGridAdapter3(Context mContext, Problem problem, String eid) {

		this.mContext = mContext;
		this.problem = problem;
		this.eid = eid;
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
		radio_type.setText(problem.getQuestion_type());
		radio_type.setTag(position);
		final int pos = position;


		//设置颜色
		if(Integer.parseInt(problem.getOptions().get(position).getOption_type()) ==0){
			radio_id.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_07));
			radio_text.setTextColor(mContext.getResources().getColor(R.color.no_select_text_color));
		}else if (Integer.parseInt(problem.getOptions().get(position).getOption_type()) ==1){
			radio_id.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_10));
			radio_text.setTextColor(mContext.getResources().getColor(R.color.exams_list_item_text_color4));
		}


		item_frame.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//第一步改变选项状态
				for (Option option:problem.getOptions() ) {
					option.setOption_type("0");
				}
				problem.getOptions().get( pos).setOption_type("1");
				//第二步改变界面选项选中状态
				TextView radio_type1 = (TextView) v.findViewById( R.id.radio_type);
				//第三部步 存储
				MyDbUtils.saveExamination_script(v.getContext(), eid, problem.getTitle_serial_number(), problem.getOptions().get(Integer.parseInt(radio_type1.getTag().toString().trim())).getOption_text());
				//第四步 互斥
				notifyDataSetChanged();
			}
		});

		return convertView;
	}


}
