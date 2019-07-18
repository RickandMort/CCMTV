package com.linlic.ccmtv.yx.activity.rules_to_compensate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.javabean.GpExamSubject;

import java.util.List;


/**
 * Created by yu on 2018/6/20.
 */

public class GpExamSubjectAdapter extends BaseAdapter{

    private Context context;
    private List<GpExamSubject> list;

    public GpExamSubjectAdapter(Context context, List<GpExamSubject> list) {
        this.context = context;
        this.list = list;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gp_exam_subject_list, parent, false);
            holder.tvSubjectName = (TextView) convertView.findViewById(R.id.id_tv_gp_exam_subject_name);
            holder.etSubjectScore = (TextView) convertView.findViewById(R.id.id_et_gp_exam_subject_score);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        final GpExamSubject gpExamSubject = list.get(position);
        holder.tvSubjectName.setText(gpExamSubject.getName());
        holder.etSubjectScore.setText(gpExamSubject.getScore());


        /*if (gpExamSubject.getItem_status().equals("1")) {
            holder.etSubjectScore.setText(gpExamSubject.getScore());
        } else if (gpExamSubject.getItem_status().equals("2")) {
            holder.etSubjectScore.setText("缺考");
        }*/

        return convertView;
    }

    class ViewHolder {
        TextView tvSubjectName;
        TextView etSubjectScore;

    }
}
