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
import com.linlic.ccmtv.yx.activity.entity.Problem;
import com.linlic.ccmtv.yx.utils.LogUtil;

import java.util.List;

/**
 * @Description:gridview的Adapter
 * @author http://blog.csdn.net/finddreams 
 */
public class Problem_GridAdapter2 extends RecyclerView.Adapter<Problem_GridAdapter2.Holder> {
	private Context mContext;
	public List<Problem> problems ;
	private int list_position ;

	public Problem_GridAdapter2(Context mContext, List<Problem> problems, int position) {

		this.mContext = mContext;
		this.problems = problems;
		this.list_position = position;
	}

	@Override
	public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
		// 实例化展示的view
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_topic_type_problem2, parent, false);
		// 实例化viewholder
		Holder viewHolder = new Holder(v);
		return viewHolder;


	}

	@Override
	public void onBindViewHolder(Holder holder, int position)
	{
		//设置题目序号 title_serial_number
		holder.title_serial_number.setText(problems.get(position).getTitle_serial_number() );//题目序号
		//设置 题目
		holder.public_topic_radios_problem.setText(Html.fromHtml(problems.get(position).getProblem()));//题目
		LogUtil.e("题目的选项",problems.get(position).getOptions().toString());
		//循环增加选项
		try {
			for(int i = 0;i<problems.get(position).getOptions().size();i++){
				switch (i){
					case 0:
						holder.public_topic_radio_text01.setText(Html.fromHtml(problems.get(position).getOptions().get(0).getOption_id() + "、" + problems.get(position).getOptions().get(0).getOption_text()));
						holder.public_topic_radios_layout_01.setVisibility(View.VISIBLE);
						holder.public_topic_radios_layout_01.setTag(problems.get(position).getTitle_serial_number());
						holder.public_topic_radio_text01.setTag("0");
						holder.public_topic_radio_id01.setTag(problems.get(position).getPosition());
						holder.public_topic_radio_type01.setTag(list_position);
						holder.public_topic_radio_type01.setText(  problems.get(position).getQuestion_type());
						//设置颜色

						if(Integer.parseInt( problems.get(position).getOptions().get(0).getOption_type()) ==0){
							holder.public_topic_radio_id01.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_07));
							holder.public_topic_radio_text01.setTextColor(mContext.getResources().getColor(R.color.no_select_text_color2));
						}else if (Integer.parseInt( problems.get(position).getOptions().get(0).getOption_type()) ==1){
							holder.public_topic_radio_id01.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_09));
							holder.public_topic_radio_text01.setTextColor(mContext.getResources().getColor(R.color.check_the_answer_sheet_item_text));
						}else if (Integer.parseInt( problems.get(position).getOptions().get(0).getOption_type())==2){
							holder.public_topic_radio_id01.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_08));
							holder.public_topic_radio_text01.setTextColor(mContext.getResources().getColor(R.color.exams_list_item_text_color8));
						}else if (Integer.parseInt( problems.get(position).getOptions().get(0).getOption_type()) ==3){
							holder.public_topic_radio_id01.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_07));
							holder.public_topic_radio_text01.setTextColor(mContext.getResources().getColor(R.color.check_the_answer_sheet_item_text));
						}


						holder.public_topic_radios_layout_02.setVisibility(View.GONE);
						holder.public_topic_radios_layout_03.setVisibility(View.GONE);
						holder.public_topic_radios_layout_04.setVisibility(View.GONE);
						holder.public_topic_radios_layout_05.setVisibility(View.GONE);
						holder.public_topic_radios_layout_06.setVisibility(View.GONE);
						holder.public_topic_radios_layout_07.setVisibility(View.GONE);
						holder.public_topic_radios_layout_08.setVisibility(View.GONE);

						break;
					case 1:
						holder.public_topic_radio_text02.setText(Html.fromHtml(problems.get(position).getOptions().get(1).getOption_id() + "、" + problems.get(position).getOptions().get(1).getOption_text()));
						holder.public_topic_radios_layout_02.setVisibility(View.VISIBLE);
						holder.public_topic_radios_layout_02.setTag(problems.get(position).getTitle_serial_number());
						holder.public_topic_radio_text02.setTag("1");
						holder.public_topic_radio_id02.setTag(problems.get(position).getPosition());
						holder.public_topic_radio_type02.setText(problems.get(position).getQuestion_type());
						holder.public_topic_radio_type02.setTag(list_position);
						//设置颜色
						if(Integer.parseInt( problems.get(position).getOptions().get(1).getOption_type()) ==0){
							holder.public_topic_radio_id02.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_07));
							holder.public_topic_radio_text02.setTextColor(mContext.getResources().getColor(R.color.no_select_text_color2));
						}else if (Integer.parseInt( problems.get(position).getOptions().get(1).getOption_type()) ==1){
							holder.public_topic_radio_id02.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_09));
							holder.public_topic_radio_text02.setTextColor(mContext.getResources().getColor(R.color.check_the_answer_sheet_item_text));
						}else if (Integer.parseInt( problems.get(position).getOptions().get(1).getOption_type())==2){
							holder.public_topic_radio_id02.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_08));
							holder.public_topic_radio_text02.setTextColor(mContext.getResources().getColor(R.color.exams_list_item_text_color8));
						}else if (Integer.parseInt( problems.get(position).getOptions().get(1).getOption_type()) ==3){
							holder.public_topic_radio_id02.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_07));
							holder.public_topic_radio_text02.setTextColor(mContext.getResources().getColor(R.color.check_the_answer_sheet_item_text));
						}

						holder.public_topic_radios_layout_03.setVisibility(View.GONE);
						holder.public_topic_radios_layout_04.setVisibility(View.GONE);
						holder.public_topic_radios_layout_05.setVisibility(View.GONE);
						holder.public_topic_radios_layout_06.setVisibility(View.GONE);
						holder.public_topic_radios_layout_07.setVisibility(View.GONE);
						holder.public_topic_radios_layout_08.setVisibility(View.GONE);
						break;
					case 2:
						holder.public_topic_radio_text03.setText(Html.fromHtml(problems.get(position).getOptions().get(2).getOption_id() + "、" + problems.get(position).getOptions().get(2).getOption_text()));
						holder.public_topic_radios_layout_03.setVisibility(View.VISIBLE);
						holder.public_topic_radios_layout_03.setTag(problems.get(position).getTitle_serial_number());
						holder.public_topic_radio_text03.setTag("2");
						holder.public_topic_radio_id03.setTag(problems.get(position).getPosition());
						holder.public_topic_radio_type03.setText(problems.get(position).getQuestion_type());
						holder.public_topic_radio_type03.setTag(list_position);
						//设置颜色
						if(Integer.parseInt( problems.get(position).getOptions().get(2).getOption_type()) ==0){
							holder.public_topic_radio_id03.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_07));
							holder.public_topic_radio_text03.setTextColor(mContext.getResources().getColor(R.color.no_select_text_color2));
						}else if (Integer.parseInt( problems.get(position).getOptions().get(2).getOption_type()) ==1){
							holder.public_topic_radio_id03.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_09));
							holder.public_topic_radio_text03.setTextColor(mContext.getResources().getColor(R.color.check_the_answer_sheet_item_text));
						}else if (Integer.parseInt( problems.get(position).getOptions().get(2).getOption_type())==2){
							holder.public_topic_radio_id03.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_08));
							holder.public_topic_radio_text03.setTextColor(mContext.getResources().getColor(R.color.exams_list_item_text_color8));
						}else if (Integer.parseInt( problems.get(position).getOptions().get(2).getOption_type()) ==3){
							holder.public_topic_radio_id03.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_07));
							holder.public_topic_radio_text03.setTextColor(mContext.getResources().getColor(R.color.check_the_answer_sheet_item_text));
						}

						holder.public_topic_radios_layout_04.setVisibility(View.GONE);
						holder.public_topic_radios_layout_05.setVisibility(View.GONE);
						holder.public_topic_radios_layout_06.setVisibility(View.GONE);
						holder.public_topic_radios_layout_07.setVisibility(View.GONE);
						holder.public_topic_radios_layout_08.setVisibility(View.GONE);
						break;
					case 3:
						holder.public_topic_radio_text04.setText(Html.fromHtml(problems.get(position).getOptions().get(3).getOption_id() + "、" + problems.get(position).getOptions().get(3).getOption_text()));
						holder.public_topic_radios_layout_04.setVisibility(View.VISIBLE);
						holder.public_topic_radios_layout_04.setTag(problems.get(position).getTitle_serial_number());
						holder.public_topic_radio_text04.setTag("3");
						holder.public_topic_radio_id04.setTag(problems.get(position).getPosition());
						holder.public_topic_radio_type04.setText(problems.get(position).getQuestion_type());
						holder.public_topic_radio_type04.setTag(list_position);
						//设置颜色
						if(Integer.parseInt( problems.get(position).getOptions().get(3).getOption_type()) ==0){
							holder.public_topic_radio_id04.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_07));
							holder.public_topic_radio_text04.setTextColor(mContext.getResources().getColor(R.color.no_select_text_color2));
						}else if (Integer.parseInt( problems.get(position).getOptions().get(3).getOption_type()) ==1){
							holder.public_topic_radio_id04.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_09));
							holder.public_topic_radio_text04.setTextColor(mContext.getResources().getColor(R.color.check_the_answer_sheet_item_text));
						}else if (Integer.parseInt( problems.get(position).getOptions().get(3).getOption_type())==2){
							holder.public_topic_radio_id04.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_08));
							holder.public_topic_radio_text04.setTextColor(mContext.getResources().getColor(R.color.exams_list_item_text_color8));
						}else if (Integer.parseInt( problems.get(position).getOptions().get(3).getOption_type()) ==3){
							holder.public_topic_radio_id04.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_07));
							holder.public_topic_radio_text04.setTextColor(mContext.getResources().getColor(R.color.check_the_answer_sheet_item_text));
						}

						holder.public_topic_radios_layout_05.setVisibility(View.GONE);
						holder.public_topic_radios_layout_06.setVisibility(View.GONE);
						holder.public_topic_radios_layout_07.setVisibility(View.GONE);
						holder.public_topic_radios_layout_08.setVisibility(View.GONE);
						break;
					case 4:
						holder.public_topic_radio_text05.setText(Html.fromHtml(problems.get(position).getOptions().get(4).getOption_id() + "、" + problems.get(position).getOptions().get(4).getOption_text()));
						holder.public_topic_radios_layout_05.setVisibility(View.VISIBLE);
						holder.public_topic_radios_layout_05.setTag(problems.get(position).getTitle_serial_number());
						holder.public_topic_radio_text05.setTag("4");
						holder.public_topic_radio_id05.setTag(problems.get(position).getPosition());
						holder.public_topic_radio_type05.setText(problems.get(position).getQuestion_type());
						holder.public_topic_radio_type05.setTag(list_position);
						//设置颜色
						if(Integer.parseInt( problems.get(position).getOptions().get(4).getOption_type()) ==0){
							holder.public_topic_radio_id05.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_07));
							holder.public_topic_radio_text05.setTextColor(mContext.getResources().getColor(R.color.no_select_text_color2));
						}else if (Integer.parseInt( problems.get(position).getOptions().get(4).getOption_type()) ==1){
							holder.public_topic_radio_id05.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_09));
							holder.public_topic_radio_text05.setTextColor(mContext.getResources().getColor(R.color.check_the_answer_sheet_item_text));
						}else if (Integer.parseInt( problems.get(position).getOptions().get(4).getOption_type())==2){
							holder.public_topic_radio_id05.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_08));
							holder.public_topic_radio_text05.setTextColor(mContext.getResources().getColor(R.color.exams_list_item_text_color8));
						}else if (Integer.parseInt( problems.get(position).getOptions().get(4).getOption_type()) ==3){
							holder.public_topic_radio_id05.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_07));
							holder.public_topic_radio_text05.setTextColor(mContext.getResources().getColor(R.color.check_the_answer_sheet_item_text));
						}

						holder.public_topic_radios_layout_06.setVisibility(View.GONE);
						holder.public_topic_radios_layout_07.setVisibility(View.GONE);
						holder.public_topic_radios_layout_08.setVisibility(View.GONE);
						break;
					case 5:
						holder.public_topic_radio_text06.setText(Html.fromHtml(problems.get(position).getOptions().get(5).getOption_id() + "、" + problems.get(position).getOptions().get(5).getOption_text()));
						holder.public_topic_radios_layout_06.setVisibility(View.VISIBLE);
						holder.public_topic_radios_layout_06.setTag(problems.get(position).getTitle_serial_number());
						holder.public_topic_radio_text06.setTag("5");
						holder.public_topic_radio_id06.setTag(problems.get(position).getPosition());
						holder.public_topic_radio_type06.setText(problems.get(position).getQuestion_type());
						holder.public_topic_radio_type06.setTag(list_position);
						//设置颜色
						if(Integer.parseInt( problems.get(position).getOptions().get(5).getOption_type()) ==0){
							holder.public_topic_radio_id06.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_07));
							holder.public_topic_radio_text06.setTextColor(mContext.getResources().getColor(R.color.no_select_text_color2));
						}else if (Integer.parseInt( problems.get(position).getOptions().get(5).getOption_type()) ==1){
							holder.public_topic_radio_id06.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_09));
							holder.public_topic_radio_text06.setTextColor(mContext.getResources().getColor(R.color.check_the_answer_sheet_item_text));
						}else if (Integer.parseInt( problems.get(position).getOptions().get(5).getOption_type())==2){
							holder.public_topic_radio_id06.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_08));
							holder.public_topic_radio_text06.setTextColor(mContext.getResources().getColor(R.color.exams_list_item_text_color8));
						}else if (Integer.parseInt( problems.get(position).getOptions().get(5).getOption_type()) ==3){
							holder.public_topic_radio_id06.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_07));
							holder.public_topic_radio_text06.setTextColor(mContext.getResources().getColor(R.color.check_the_answer_sheet_item_text));
						}

						holder.public_topic_radios_layout_07.setVisibility(View.GONE);
						holder.public_topic_radios_layout_08.setVisibility(View.GONE);
						break;
					case 6:
						holder.public_topic_radio_text07.setText(Html.fromHtml(problems.get(position).getOptions().get(6).getOption_id() + "、" + problems.get(position).getOptions().get(6).getOption_text()));
						holder.public_topic_radios_layout_07.setVisibility(View.VISIBLE);
						holder.public_topic_radios_layout_07.setTag(problems.get(position).getTitle_serial_number());
						holder.public_topic_radio_text07.setTag("6");
						holder.public_topic_radio_id07.setTag(problems.get(position).getPosition());
						holder.public_topic_radio_type07.setText(problems.get(position).getQuestion_type());
						holder.public_topic_radio_type07.setTag(list_position);
						//设置颜色
						if(Integer.parseInt( problems.get(position).getOptions().get(6).getOption_type()) ==0){
							holder.public_topic_radio_id07.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_07));
							holder.public_topic_radio_text07.setTextColor(mContext.getResources().getColor(R.color.no_select_text_color2));
						}else if (Integer.parseInt( problems.get(position).getOptions().get(6).getOption_type()) ==1){
							holder.public_topic_radio_id07.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_09));
							holder.public_topic_radio_text07.setTextColor(mContext.getResources().getColor(R.color.check_the_answer_sheet_item_text));
						}else if (Integer.parseInt( problems.get(position).getOptions().get(6).getOption_type())==2){
							holder.public_topic_radio_id07.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_08));
							holder.public_topic_radio_text07.setTextColor(mContext.getResources().getColor(R.color.exams_list_item_text_color8));
						}else if (Integer.parseInt( problems.get(position).getOptions().get(6).getOption_type()) ==3){
							holder.public_topic_radio_id07.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_07));
							holder.public_topic_radio_text07.setTextColor(mContext.getResources().getColor(R.color.check_the_answer_sheet_item_text));
						}
						holder.public_topic_radios_layout_08.setVisibility(View.GONE);
						break;
					case 7:
						holder.public_topic_radio_text08.setText(Html.fromHtml(problems.get(position).getOptions().get(7).getOption_id() + "、" + problems.get(position).getOptions().get(7).getOption_text()));
						holder.public_topic_radios_layout_08.setVisibility(View.VISIBLE);
						holder.public_topic_radios_layout_08.setTag(problems.get(position).getTitle_serial_number());
						holder.public_topic_radio_text08.setTag("7");
						holder.public_topic_radio_id08.setTag(problems.get(position).getPosition());
						holder.public_topic_radio_type08.setText(problems.get(position).getQuestion_type());
						holder.public_topic_radio_type08.setTag(list_position);
						//设置颜色
						if(Integer.parseInt( problems.get(position).getOptions().get(7).getOption_type()) ==0){
							holder.public_topic_radio_id08.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_07));
							holder.public_topic_radio_text08.setTextColor(mContext.getResources().getColor(R.color.no_select_text_color2));
						}else if (Integer.parseInt( problems.get(position).getOptions().get(7).getOption_type()) ==1){
							holder.public_topic_radio_id08.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_09));
							holder.public_topic_radio_text08.setTextColor(mContext.getResources().getColor(R.color.check_the_answer_sheet_item_text));
						}else if (Integer.parseInt( problems.get(position).getOptions().get(7).getOption_type())==2){
							holder.public_topic_radio_id08.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_08));
							holder.public_topic_radio_text08.setTextColor(mContext.getResources().getColor(R.color.exams_list_item_text_color8));
						}else if (Integer.parseInt( problems.get(position).getOptions().get(7).getOption_type()) ==3){
							holder.public_topic_radio_id08.setBackground(mContext.getResources().getDrawable(R.mipmap.exams2_07));
							holder.public_topic_radio_text08.setTextColor(mContext.getResources().getColor(R.color.check_the_answer_sheet_item_text));
						}

						break;
					default:
						break;
				}

			}



		}catch (Exception e){
			e.printStackTrace();
		}
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


	/*class holder{
		LinearLayout public_topic_radios;
		TextView public_topic_radios_type;//类型
		TextView title_serial_number;//序号
		TextView public_topic_radios_problem;//题目
		LinearLayout public_topic_radios_layout_01,public_topic_radios_layout_02,public_topic_radios_layout_03,public_topic_radios_layout_04,public_topic_radios_layout_05,public_topic_radios_layout_06,public_topic_radios_layout_07,public_topic_radios_layout_08;
		TextView public_topic_radio_id01,public_topic_radio_id02,public_topic_radio_id03,public_topic_radio_id04,public_topic_radio_id05,public_topic_radio_id06,public_topic_radio_id07,public_topic_radio_id08;
		TextView public_topic_radio_text01,public_topic_radio_text02,public_topic_radio_text03,public_topic_radio_text04,public_topic_radio_text05,public_topic_radio_text06,public_topic_radio_text07,public_topic_radio_text08;
		TextView public_topic_radio_type01,public_topic_radio_type02,public_topic_radio_type03,public_topic_radio_type04,public_topic_radio_type05,public_topic_radio_type06,public_topic_radio_type07,public_topic_radio_type08;
	}*/
	class Holder extends RecyclerView.ViewHolder {
		LinearLayout public_topic_radios;
		TextView public_topic_radios_type;//类型
		TextView title_serial_number;//序号
		TextView public_topic_radios_problem;//题目
		LinearLayout public_topic_radios_layout_01,public_topic_radios_layout_02,public_topic_radios_layout_03,public_topic_radios_layout_04,public_topic_radios_layout_05,public_topic_radios_layout_06,public_topic_radios_layout_07,public_topic_radios_layout_08;
		TextView public_topic_radio_id01,public_topic_radio_id02,public_topic_radio_id03,public_topic_radio_id04,public_topic_radio_id05,public_topic_radio_id06,public_topic_radio_id07,public_topic_radio_id08;
		TextView public_topic_radio_text01,public_topic_radio_text02,public_topic_radio_text03,public_topic_radio_text04,public_topic_radio_text05,public_topic_radio_text06,public_topic_radio_text07,public_topic_radio_text08;
		TextView public_topic_radio_type01,public_topic_radio_type02,public_topic_radio_type03,public_topic_radio_type04,public_topic_radio_type05,public_topic_radio_type06,public_topic_radio_type07,public_topic_radio_type08;

		public Holder(View view)
		{
			super(view);
			public_topic_radios =(LinearLayout) view.findViewById(R.id.public_topic_radios);
			public_topic_radios_type = (TextView) view.findViewById(R.id.public_topic_radios_type);
			title_serial_number = (TextView) view.findViewById(R.id.title_serial_number);
			public_topic_radios_problem = (TextView) view.findViewById(R.id.public_topic_radios_problem);
			public_topic_radios_layout_01 = (LinearLayout) view.findViewById(R.id.public_topic_radios_layout_01);
			public_topic_radios_layout_02 = (LinearLayout) view.findViewById(R.id.public_topic_radios_layout_02);
			public_topic_radios_layout_03 = (LinearLayout) view.findViewById(R.id.public_topic_radios_layout_03);
			public_topic_radios_layout_04 = (LinearLayout) view.findViewById(R.id.public_topic_radios_layout_04);
			public_topic_radios_layout_05 = (LinearLayout) view.findViewById(R.id.public_topic_radios_layout_05);
			public_topic_radios_layout_06 = (LinearLayout) view.findViewById(R.id.public_topic_radios_layout_06);
			public_topic_radios_layout_07 = (LinearLayout) view.findViewById(R.id.public_topic_radios_layout_07);
			public_topic_radios_layout_08 = (LinearLayout) view.findViewById(R.id.public_topic_radios_layout_08);
			public_topic_radio_id01 = (TextView) view.findViewById(R.id.public_topic_radio_id01);
			public_topic_radio_id02 = (TextView) view.findViewById(R.id.public_topic_radio_id02);
			public_topic_radio_id03 = (TextView) view.findViewById(R.id.public_topic_radio_id03);
			public_topic_radio_id04 = (TextView) view.findViewById(R.id.public_topic_radio_id04);
			public_topic_radio_id05 = (TextView) view.findViewById(R.id.public_topic_radio_id05);
			public_topic_radio_id06 = (TextView) view.findViewById(R.id.public_topic_radio_id06);
			public_topic_radio_id07 = (TextView) view.findViewById(R.id.public_topic_radio_id07);
			public_topic_radio_id08 = (TextView) view.findViewById(R.id.public_topic_radio_id08);
			public_topic_radio_text01 = (TextView) view.findViewById(R.id.public_topic_radio_text01);
			public_topic_radio_text02 = (TextView) view.findViewById(R.id.public_topic_radio_text02);
			public_topic_radio_text03 = (TextView) view.findViewById(R.id.public_topic_radio_text03);
			public_topic_radio_text04 = (TextView) view.findViewById(R.id.public_topic_radio_text04);
			public_topic_radio_text05 = (TextView) view.findViewById(R.id.public_topic_radio_text05);
			public_topic_radio_text06 = (TextView) view.findViewById(R.id.public_topic_radio_text06);
			public_topic_radio_text07 = (TextView) view.findViewById(R.id.public_topic_radio_text07);
			public_topic_radio_text08 = (TextView) view.findViewById(R.id.public_topic_radio_text08);
			public_topic_radio_type01 = (TextView) view.findViewById(R.id.public_topic_radio_type01);
			public_topic_radio_type02 = (TextView) view.findViewById(R.id.public_topic_radio_type02);
			public_topic_radio_type03 = (TextView) view.findViewById(R.id.public_topic_radio_type03);
			public_topic_radio_type04 = (TextView) view.findViewById(R.id.public_topic_radio_type04);
			public_topic_radio_type05 = (TextView) view.findViewById(R.id.public_topic_radio_type05);
			public_topic_radio_type06 = (TextView) view.findViewById(R.id.public_topic_radio_type06);
			public_topic_radio_type07 = (TextView) view.findViewById(R.id.public_topic_radio_type07);
			public_topic_radio_type08 = (TextView) view.findViewById(R.id.public_topic_radio_type08);
		}
	}

}
