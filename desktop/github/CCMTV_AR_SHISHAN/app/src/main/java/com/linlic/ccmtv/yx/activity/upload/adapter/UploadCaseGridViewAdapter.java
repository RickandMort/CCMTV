package com.linlic.ccmtv.yx.activity.upload.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.holder.BaseViewHolder;

import java.util.List;

import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by yu on 2018/5/14.
 */

public class UploadCaseGridViewAdapter extends BaseAdapter {

    private Context context;
    private List<PhotoInfo> list;

    public UploadCaseGridViewAdapter(Context context, List<PhotoInfo> list) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_my_case_image, parent, false);
            holder.imageView = (ImageView) convertView.findViewById(R.id.id_iv_item_mycase);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        //ImageView img = BaseViewHolder.get(convertView, R.id.upload_image);
        if (list.get(position).getPhotoPath().equals("icon_add")) {
            Glide.with(context).load(R.mipmap.icon_add).into(holder.imageView);
        } else {
            Glide.with(context).load(list.get(position).getPhotoPath()).into(holder.imageView);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
    }
}
