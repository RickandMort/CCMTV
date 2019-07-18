package com.linlic.ccmtv.yx.activity.upload.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.linlic.ccmtv.yx.R;

import java.util.List;

/**
 * Created by yu on 2018/5/14.
 */

public class MyCaseGridViewAdapter3 extends BaseAdapter {

    private Context context;
    private List<String> uploadFileList;

    public MyCaseGridViewAdapter3(Context context, List<String> uploadFileList) {
        this.context = context;
        this.uploadFileList = uploadFileList;
//        Log.e("MyCaseGridViewAdapter", "MyCaseGridViewAdapter: listSize:"+uploadFileList.size());
    }

    @Override
    public int getCount() {
        return uploadFileList.size();
    }

    @Override
    public Object getItem(int position) {
        return uploadFileList.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_look_my_case_image, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.id_iv_item_mycase);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //holder.imageView.setImageResource(R.mipmap.img_default);
        RequestOptions options = new RequestOptions().error(R.mipmap.img_default);
        Glide.with(context)
                .load(uploadFileList.get(position))
                .apply(options)
                .into(holder.imageView);

        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
    }
}
