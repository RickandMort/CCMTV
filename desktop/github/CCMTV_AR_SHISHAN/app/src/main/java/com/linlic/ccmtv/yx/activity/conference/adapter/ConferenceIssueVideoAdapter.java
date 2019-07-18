package com.linlic.ccmtv.yx.activity.conference.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.conference.databean.ConferenceVideoBean;

import java.util.List;

/**
 * Created by yu on 2018/5/9.
 */

public class ConferenceIssueVideoAdapter extends BaseAdapter{

    private Context context;
    private List<ConferenceVideoBean> conferenceDatas;

    public ConferenceIssueVideoAdapter(Context context, List<ConferenceVideoBean> conferenceDatas) {
        this.context = context;
        this.conferenceDatas = conferenceDatas;
    }

    @Override
    public int getCount() {
        return conferenceDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return conferenceDatas.get(position);
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
            convertView= LayoutInflater.from(context).inflate(R.layout.item_conference_issue_video_list,null);
            holder.tvConferenceTitle = (TextView) convertView.findViewById(R.id.id_tv_item_conference_issue_video);
            holder.ivConferencePic = (ImageView) convertView.findViewById(R.id.id_iv_item_conference_issue_video);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvConferenceTitle.setText(conferenceDatas.get(position).getTitle());
        Glide.with(context).load(conferenceDatas.get(position).getPicUrl()).into(holder.ivConferencePic);
        return convertView;
    }

    class ViewHolder{
        TextView tvConferenceTitle;
        ImageView ivConferencePic;
    }
}
