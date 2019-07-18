package com.linlic.ccmtv.yx.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.utils.FirstLetter;

import java.util.List;
import java.util.Map;

/**
 * Created by yu on 2018/5/8.
 */

public class SearchVideoListAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String,Object>> conferenceDatas;

    public SearchVideoListAdapter(Context context, List<Map<String,Object>> conferenceDatas) {
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
            convertView= LayoutInflater.from(context).inflate(R.layout.custom_item_video,null);
            holder.tvVideoTitle = (TextView) convertView.findViewById(R.id.departemnt_item_title);
            holder.tvVideoTime = (TextView) convertView.findViewById(R.id.department_times);
            holder.tvVideoPlayTimes = (TextView) convertView.findViewById(R.id.department_on_demand);
            holder.ivVideoPic = (ImageView) convertView.findViewById(R.id.departemnt_item_img);
            holder.ivVideoTopImg = (ImageView) convertView.findViewById(R.id.departemnt_item_top_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.tvVideoTitle.setText(conferenceDatas.get(position).get("title").toString());
        holder.tvVideoTime.setText(conferenceDatas.get(position).get("posttime").toString());
        RequestOptions options = new RequestOptions().placeholder(R.mipmap.img_default)
                .override(100,80)
                .error(R.mipmap.img_default);
        Glide.with(context)
                .load(FirstLetter.getSpells(conferenceDatas.get(position).get("picurl").toString()))
                .apply(options)
                .into(holder.ivVideoPic);
        //Glide.with(context).load(conferenceDatas.get(position).get("picurl")).into(holder.ivVideoPic);
        return convertView;
    }

    class ViewHolder{
        TextView tvVideoTitle;
        TextView tvVideoTime;
        TextView tvVideoPlayTimes;
        ImageView ivVideoPic;
        ImageView ivVideoTopImg;

    }
}