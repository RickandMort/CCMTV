package com.linlic.ccmtv.yx.activity.conference.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.conference.databean.ConferenceIssueBean;

import java.util.List;

/**
 * Created by yu on 2018/5/9.
 */

public class ConferenceIssueSelectAdapter extends BaseAdapter{

    private Context context;
    private List<ConferenceIssueBean> conferenceIssueDatas;
    private int selection=-1;

    public ConferenceIssueSelectAdapter(Context context, List<ConferenceIssueBean> conferenceDatas) {
        this.context = context;
        this.conferenceIssueDatas = conferenceDatas;
    }

    @Override
    public int getCount() {
        return conferenceIssueDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return conferenceIssueDatas.get(position);
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
            convertView= LayoutInflater.from(context).inflate(R.layout.item_conference_issue_select_list,null);
            holder.tvConferenceSelectTitle = (TextView) convertView.findViewById(R.id.id_tv_item_conference_issue_select);
            holder.ivConferenceselected = (ImageView) convertView.findViewById(R.id.id_iv_item_conference_issue_selected);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvConferenceSelectTitle.setText(conferenceIssueDatas.get(position).getFtitle());
        if (position==selection){
            holder.tvConferenceSelectTitle.setTextColor(Color.parseColor("#3698F9"));
            holder.ivConferenceselected.setVisibility(View.VISIBLE);
        }else {
            holder.tvConferenceSelectTitle.setTextColor(Color.parseColor("#000000"));
            holder.ivConferenceselected.setVisibility(View.GONE);
        }
        return convertView;
    }

    public void setSelect(int position) {
        selection=position;
        notifyDataSetChanged();
    }

    class ViewHolder{
        TextView tvConferenceSelectTitle;
        ImageView ivConferenceselected;
    }
}
