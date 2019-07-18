package com.linlic.ccmtv.yx.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.entity.Problem;
import com.linlic.ccmtv.yx.activity.my.medical_examination.ClassOfTextWatcher3;

import java.util.List;

/**
 * @Description:gridview的Adapter
 * @author http://blog.csdn.net/finddreams 
 */
public class Public_case_analysis_problem2 extends RecyclerView.Adapter<Public_case_analysis_problem2.Holder> {
	private Context mContext;
	public List<Problem> problems ;
	private int list_position ;
	private String  eid;

	public Public_case_analysis_problem2(Context mContext, List<Problem> problems, int position, String eid) {

		this.mContext = mContext;
		this.problems = problems;
		this.list_position = position;
		this.eid = eid;

	}

	@Override
	public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
		// 实例化展示的view
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_case_analysis_problem2, parent, false);
		// 实例化viewholder
		Holder viewHolder = new Holder(v);
		return viewHolder;


	}

	@Override
	public void onBindViewHolder(Holder holder, int position)
	{
		//设置题目序号 title_serial_number
		holder.title_serial_number.setText(problems.get(position).getTitle_serial_number());
		holder.public_case_analysis_problem_eid.setText(eid);
		holder.public_case_analysis_problem.setText(Html.fromHtml(problems.get(position).getQuestion_type_text() + problems.get(position).getProblem()));
 		holder.public_case_analysis_my_answer.setText(Html.fromHtml("您的答案："+problems.get(position).getUser_answer()));
 		holder.public_case_analysis_correct.setText(Html.fromHtml("正确答案："+problems.get(position).getTrue_answer()));
	}

	@Override
	public int getItemCount()
	{
		return problems.size();
	}



	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}



	class Holder extends RecyclerView.ViewHolder {
		LinearLayout public_case_analysis_problem_s;
		TextView public_case_analysis_problem_type;//类型
		TextView public_case_analysis_problem_eid;//eid
		TextView title_serial_number;//序号
		TextView public_case_analysis_problem;//题目
		TextView public_case_analysis_my_answer;//
		TextView public_case_analysis_correct;
		public Holder(View view)
		{
			super(view);
			public_case_analysis_problem_s =(LinearLayout) view.findViewById(R.id.public_case_analysis_problem_s);
			public_case_analysis_problem_type = (TextView) view.findViewById(R.id.public_case_analysis_problem_type);
			title_serial_number = (TextView) view.findViewById(R.id.title_serial_number);
			public_case_analysis_problem = (TextView) view.findViewById(R.id.public_case_analysis_problem);
			public_case_analysis_problem_eid = (TextView) view.findViewById(R.id.public_case_analysis_problem_eid);
			public_case_analysis_my_answer = (TextView) view.findViewById(R.id.public_case_analysis_my_answer);
			public_case_analysis_correct = (TextView) view.findViewById(R.id.public_case_analysis_correct);


		}
	}

}
