package com.linlic.ccmtv.yx.activity.my.our_video;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.linlic.ccmtv.yx.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yu on 2018/5/30.
 */

public class MyOurResourcesAdapter extends BaseAdapter{

    private Context context;
    private List<Map<String,Object>> resourcesData;
    private LayoutInflater inflater;
    //为两种布局定义一个标识
    private final int TYPE1 = 0;
    private final int TYPE2 = 1;

    public MyOurResourcesAdapter(Context context, List<Map<String, Object>> resourcesData) {
        this.context=context;
        this.resourcesData=resourcesData;
        //别忘了初始化inflater
        inflater = LayoutInflater.from(this.context);
    }

    //这个方法必须重写，它返回了有几种不同的布局
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    // 每个convertView都会调用此方法，获得当前应该加载的布局样式
    @Override
    public int getItemViewType(int position) {
        //获取当前布局的数据
        Map<String,Object> map = resourcesData.get(position);

        if ((int)map.get("mType") == 0) {
            return TYPE1;
        } else {
            return TYPE2;
        }
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
        //初始化每个holder
        ViewHolder1 holder1 = null;
        ViewHolder2 holder2 = null;

        int type = getItemViewType(position);

        if (convertView == null) {
            switch (type) {
                case TYPE1:
                    convertView = inflater.inflate(R.layout.item_our_resources_folder, null, false);
                    holder1 = new ViewHolder1();
                    holder1.item1_iv_icon = (ImageView) convertView.findViewById(R.id.id_iv_our_resources_folder);
                    holder1.item1_tv_title = (TextView) convertView.findViewById(R.id.id_tv_our_resources_folder);
                    convertView.setTag(holder1);
                    break;
                case TYPE2:
                    convertView = inflater.inflate(R.layout.item_our_resources_file, null, false);
                    holder2 = new ViewHolder2();
                    holder2.item2_iv_icon = (ImageView) convertView.findViewById(R.id.id_iv_our_resources_file_icon);
                    holder2.item2_tv_title = (TextView) convertView.findViewById(R.id.id_tv_our_resources_file_title);
                    holder2.item2_tv_time = (TextView) convertView.findViewById(R.id.id_tv_our_resources_file_time);
                    holder2.item2_tv_size = (TextView) convertView.findViewById(R.id.id_tv_our_resources_file_size);
                    convertView.setTag(holder2);
                    break;
                default:
                    break;
            }
        } else {
            switch (type) {
                case TYPE1:
                    holder1 = (ViewHolder1) convertView.getTag();
                    break;
                case TYPE2:
                    holder2 = (ViewHolder2) convertView.getTag();
                    break;
            }
        }
        //为布局设置数据
        switch (type) {
            case TYPE1:
                //Glide.with(context).load(resourcesData.get(position).get("icon").toString()).into(holder1.item1_iv_icon);
                holder1.item1_tv_title.setText(resourcesData.get(position).get("name").toString());
                break;
            case TYPE2:
                //Glide.with(context).load(resourcesData.get(position).get("icon").toString()).into(holder2.item2_iv_icon);
                holder2.item2_tv_title.setText(resourcesData.get(position).get("name").toString());
                holder2.item2_tv_time.setText(resourcesData.get(position).get("posttime").toString());
                holder2.item2_tv_size.setText(resourcesData.get(position).get("size").toString());

                if (resourcesData.get(position).get("type").equals("mp4")){
                    holder2.item2_iv_icon.setImageResource(R.mipmap.our_resources_video);
                }else if (resourcesData.get(position).get("type").equals("ppt")){
                    holder2.item2_iv_icon.setImageResource(R.mipmap.our_resources_ppt);
                }else if (resourcesData.get(position).get("type").equals("pdf")){
                    holder2.item2_iv_icon.setImageResource(R.mipmap.our_resources_pdf);
                }else if (resourcesData.get(position).get("type").equals("word")){
                    holder2.item2_iv_icon.setImageResource(R.mipmap.our_resources_word);
                }
                break;
        }

        return convertView;
    }

    //为每种布局定义自己的ViewHolder
    public class ViewHolder1 {
        ImageView item1_iv_icon;
        TextView item1_tv_title;
    }

    public class ViewHolder2 {
        ImageView item2_iv_icon;
        TextView item2_tv_title;
        TextView item2_tv_time;
        TextView item2_tv_size;
    }

    public void setDataList(ArrayList<Map<String, Object>> dataList) {
        if (dataList != null) {
            resourcesData = (List<Map<String, Object>>) dataList.clone();
            notifyDataSetChanged();
        }
    }

    public void clearDataList() {
        if (resourcesData != null) {
            resourcesData.clear();
        }
    }
}
