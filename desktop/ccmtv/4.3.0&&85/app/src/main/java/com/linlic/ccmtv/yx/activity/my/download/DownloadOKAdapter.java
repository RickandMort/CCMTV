package com.linlic.ccmtv.yx.activity.my.download;/*
package com.linlic.ccmtv.yx.activity.my.download;

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

*/
/**
 * name：
 * author：MrSong
 * data：2016/7/5.
 *//*

public class DownloadOKAdapter extends BaseAdapter {
    private List<Map<String, String>> list = new ArrayList<>();//数据list
    private Context context;

    public DownloadOKAdapter(Context context, List<Map<String, String>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();

            convertView = LayoutInflater.from(context).inflate(R.layout.upload_download_list_item, null);
            holder.img = (ImageView) convertView.findViewById(R.id.upload_down_item_img);
            holder.videoname = (TextView) convertView.findViewById(R.id.upload_down_item_videoname);
            holder.size = (TextView) convertView.findViewById(R.id.upload_down_item_size);
            holder.status = (TextView) convertView.findViewById(R.id.upload_down_item_state);
            holder.videoid = (TextView) convertView.findViewById(R.id.upload_down_item_videoid);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (list.size() != 0) {
            holder.videoname.setText(list.get(position).get("videoName"));
            holder.size.setText(list.get(position).get("total"));
            holder.status.setText(list.get(position).get("filePath"));
            holder.videoid.setText(list.get(position).get("videoId"));
            Glide.with(context).load(list.get(position).get("picUrl")).into(holder.img);
        }


        return convertView;
    }

    private final class ViewHolder {
        ImageView img;
        TextView videoname;
        TextView size;
        TextView status;
        TextView videoid;
    }
}
*/
