package com.linlic.ccmtv.yx.activity.rules_to_compensate.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.javabean.DailyStudent;

import java.util.List;

/**
 * Created by bentley on 2019/4/19.
 */

public class GpGraduateExamAdapter extends BaseAdapter {

    private Context context;
    private List<DailyStudent> list;
    private String flag;

    public GpGraduateExamAdapter(Context context, List<DailyStudent> list,String flag) {
        this.context = context;
        this.list = list;
        this.flag=flag;
    }

    public void notifyData(List<DailyStudent> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            if(flag.equals("1")){
                convertView = LayoutInflater.from(context).inflate(R.layout.adapter_daily_inspection, parent, false);
            }else if(flag.equals("2")){
                convertView = LayoutInflater.from(context).inflate(R.layout.item_gp_exam_student_list, parent, false);
            }
            holder.tvStudentName = (TextView) convertView.findViewById(R.id.id_tv_gp_exam_student_list_item_name);
            holder.tvStudentDepartment = (TextView) convertView.findViewById(R.id.id_tv_gp_exam_student_list_item_department);
            holder.tvStudentScore = (TextView) convertView.findViewById(R.id.id_tv_gp_exam_student_list_item_score);
            holder.tvStudentScore_view = (TextView) convertView.findViewById(R.id.id_tv_gp_exam_student_list_item_score2);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        final DailyStudent examStudent = list.get(position);
        holder.tvStudentName.setText(examStudent.getRealname());
        holder.tvStudentDepartment.setText(examStudent.getStandard_name());
        holder.tvStudentScore.setText(examStudent.getScore());
        switch (examStudent.getScore()){

            case "待录入":
                holder.tvStudentScore.setTextColor(Color.parseColor("#666666"));
                holder.tvStudentScore_view.setVisibility(View.GONE);
                break;
            case "不考核":
                holder.tvStudentScore.setTextColor(Color.parseColor("#666666"));
                holder.tvStudentScore_view.setVisibility(View.GONE);
                break;
            case "缺考":
                holder.tvStudentScore.setTextColor(Color.parseColor("#F79801"));
                holder.tvStudentScore_view.setVisibility(View.GONE);
                break;
            default:
                if(isNumeric(examStudent.getScore())){
                    //数字
                    holder.tvStudentScore_view.setVisibility(View.VISIBLE);
                } else {
                    //非数字
                    holder.tvStudentScore_view.setVisibility(View.GONE);
                }
                holder.tvStudentScore.setTextColor(Color.parseColor("#4391d4"));
                break;
        }

        return convertView;
    }

    class ViewHolder {
        TextView tvStudentName;
        TextView tvStudentDepartment;
        TextView tvStudentScore;
        TextView tvStudentScore_view;

    }

    public static boolean isNumeric(String str){

        for (int i = str.length();--i>=0;){

            if (!Character.isDigit(str.charAt(i))){

                return false;

            }

        }

        return true;

    }
}
