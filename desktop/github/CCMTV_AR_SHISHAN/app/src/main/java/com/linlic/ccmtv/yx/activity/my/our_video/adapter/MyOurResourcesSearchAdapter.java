package com.linlic.ccmtv.yx.activity.my.our_video.adapter;

import android.content.Context;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.utils.TextHighLight;

import java.util.List;
import java.util.Map;

/**
 * Created by yu on 2018/5/30.
 */

public class MyOurResourcesSearchAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String, Object>> resourcesData;
    private LayoutInflater inflater;

    public MyOurResourcesSearchAdapter(Context context, List<Map<String, Object>> resourcesData) {
        this.context = context;
        this.resourcesData = resourcesData;
        //别忘了初始化inflater
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return resourcesData.size();
    }

    @Override
    public Object getItem(int position) {
        return resourcesData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_our_resources_file, null, false);
            holder = new ViewHolder();
            holder.item_iv_icon = (ImageView) convertView.findViewById(R.id.id_iv_our_resources_file_icon);
            holder.item_tv_title = (TextView) convertView.findViewById(R.id.id_tv_our_resources_file_title);
            holder.item_tv_time = (TextView) convertView.findViewById(R.id.id_tv_our_resources_file_time);
            holder.item_tv_size = (TextView) convertView.findViewById(R.id.id_tv_our_resources_file_size);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //为布局设置数据
        //SpannableStringBuilder spannableStringBuilder= TextHighLight.matcherSearchContent(resourcesData.get(position).get("name").toString(),new String[]{resourcesData.get(position).get("keyword").toString()});

        //Glide.with(context).load(resourcesData.get(position).get("icon").toString()).into(holder.item_iv_icon);
        holder.item_tv_title.setText(Html.fromHtml(resourcesData.get(position).get("name").toString()));
        holder.item_tv_time.setText(resourcesData.get(position).get("posttime").toString());
        holder.item_tv_size.setText(resourcesData.get(position).get("size").toString());

        if (resourcesData.get(position).get("type").equals("mp4")){
            holder.item_iv_icon.setImageResource(R.mipmap.our_resources_video);
        }else if (resourcesData.get(position).get("type").equals("ppt")){
            holder.item_iv_icon.setImageResource(R.mipmap.our_resources_ppt);
        }else if (resourcesData.get(position).get("type").equals("pdf")){
            holder.item_iv_icon.setImageResource(R.mipmap.our_resources_pdf);
        }else if (resourcesData.get(position).get("type").equals("word")){
            holder.item_iv_icon.setImageResource(R.mipmap.our_resources_word);
        }

        return convertView;
    }

    public class ViewHolder {
        ImageView item_iv_icon;
        TextView item_tv_title;
        TextView item_tv_time;
        TextView item_tv_size;
    }
}
