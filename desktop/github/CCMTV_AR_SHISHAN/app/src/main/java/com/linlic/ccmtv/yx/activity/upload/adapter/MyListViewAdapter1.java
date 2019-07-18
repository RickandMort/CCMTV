package com.linlic.ccmtv.yx.activity.upload.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;

import java.util.List;
import java.util.Map;

/**
 * name：正在审核、已经上传使用（病例）
 * author：MrSong
 * data：2016/3/31.
 */
public class MyListViewAdapter1 extends BaseAdapter {
    private Context context;
    private List<Map<String, String>> list;
    private LayoutInflater inflater;
    private String upload;

    public MyListViewAdapter1(Context context, List<Map<String, String>> list,String upload) {
        this.context = context;
        this.list = list;
        this.upload = upload;
        this.inflater = LayoutInflater.from(context);
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

            convertView = inflater.inflate(R.layout.upload_item2, null);
            holder.title = (TextView) convertView.findViewById(R.id.dupload_names);
            holder.id = (TextView) convertView.findViewById(R.id.department_id);
            if (upload.equals("upload")){
                convertView.findViewById(R.id.upload_tiems).setVisibility(View.GONE);
                convertView.findViewById(R.id.upload_tiems1).setVisibility(View.VISIBLE);
                holder.shenhe = (TextView) convertView.findViewById(R.id.upload_tiems1);
            }else {
                holder.shenhe = (TextView) convertView.findViewById(R.id.upload_tiems);
            }

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(list.get(position).get("mvtitle"));
        holder.shenhe.setText(list.get(position).get("shenhe"));
        holder.id.setText(list.get(position).get("id"));

        return convertView;
    }

    public final class ViewHolder {
        public TextView title;
        public TextView id;
        public TextView shenhe;
    }
}
