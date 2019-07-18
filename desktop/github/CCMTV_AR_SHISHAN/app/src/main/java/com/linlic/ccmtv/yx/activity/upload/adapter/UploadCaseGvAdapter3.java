package com.linlic.ccmtv.yx.activity.upload.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.upload.new_upload.UploadCaseInputActivity;

import java.util.List;

import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by bentley on 2018/7/26.
 */

public class UploadCaseGvAdapter3 extends BaseAdapter {


    private UploadCaseInputActivity context;
    private List<PhotoInfo> list;

    public UploadCaseGvAdapter3(UploadCaseInputActivity context, List<PhotoInfo> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_my_case_image3, parent, false);
            holder.imageView = (ImageView) convertView.findViewById(R.id.id_iv_item_mycase);
            holder.imageView_delete = (ImageView) convertView.findViewById(R.id.id_iv_item_delete);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        //ImageView img = BaseViewHolder.get(convertView, R.id.upload_image);
        if (list.get(position).getPhotoPath().equals("icon_add")) {
            holder.imageView_delete.setVisibility(View.GONE);
            Glide.with(context).load(R.mipmap.icon_add).into(holder.imageView);
        } else {
            holder.imageView_delete.setVisibility(View.VISIBLE);
            Glide.with(context).load(list.get(position).getPhotoPath()).into(holder.imageView);
        }
        holder.imageView_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    String deletePicName = list.get(position).getPhotoName();
//                    list.remove(position);
//                    context.deletePic(deletePicName, list);
                context.deletePic(position);
//                    notifyDataSetChanged();
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        ImageView imageView_delete;
    }
}
