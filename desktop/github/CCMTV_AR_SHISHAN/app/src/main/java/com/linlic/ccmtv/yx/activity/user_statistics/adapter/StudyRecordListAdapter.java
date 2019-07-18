package com.linlic.ccmtv.yx.activity.user_statistics.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.user_statistics.javabean.StudyRecordVideoBean;

import java.util.List;

/**
 * Created by yu on 2018/5/9.
 */

public class StudyRecordListAdapter extends BaseAdapter{

    private Context context;
    private List<StudyRecordVideoBean> studyRecordDatas;

    public StudyRecordListAdapter(Context context, List<StudyRecordVideoBean> conferenceDatas) {
        this.context = context;
        this.studyRecordDatas = conferenceDatas;
    }

    @Override
    public int getCount() {
        return studyRecordDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return studyRecordDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            holder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.item_study_record_list,null);
            holder.tvListTitle = (TextView) convertView.findViewById(R.id.id_tv_item_study_record_title);
            holder.tvListLastlooktime = (TextView) convertView.findViewById(R.id.id_tv_item_study_record_last_look_time);
            holder.ivListPic = (ImageView) convertView.findViewById(R.id.id_iv_item_study_record_pic);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvListTitle.setText(studyRecordDatas.get(position).getVideoTitle());
        holder.tvListLastlooktime.setText(studyRecordDatas.get(position).getLast_look_time());
        Glide.with(context).load(studyRecordDatas.get(position).getPicUrl()).into(holder.ivListPic);
        return convertView;
    }

    class ViewHolder{
        TextView tvListTitle;
        TextView tvListLastlooktime;
        ImageView ivListPic;
    }
}
