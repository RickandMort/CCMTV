package com.linlic.ccmtv.yx.activity.rules_to_compensate.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.periodical_exam.StuPeriodicalExamActivity;

import java.util.List;

import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by yu on 2018/6/20.
 */

public class StuPeriodicalExamPicGridViewAdapter extends BaseAdapter {
    private StuPeriodicalExamActivity context;
    private List<PhotoInfo> list;
    private String disabled = "1";

    public StuPeriodicalExamPicGridViewAdapter(StuPeriodicalExamActivity context, List<PhotoInfo> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_graduate_exam_image, parent, false);
            holder.imageView = (ImageView) convertView.findViewById(R.id.id_iv_item_pic);
            holder.imageView_delete = (ImageView) convertView.findViewById(R.id.id_iv_item_delete);
            holder.imageView_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*list.remove(position);
                    notifyDataSetChanged();*/
                    context.deletePic(position);
                }
            });
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (disabled.equals("0")) {       //是否可以编辑  1、不可编辑   0、可以编辑
            holder.imageView_delete.setVisibility(View.VISIBLE);
        } else {
            holder.imageView_delete.setVisibility(View.GONE);
        }
        //ImageView img = BaseViewHolder.get(convertView, R.id.upload_image);
        if (list.get(position).getPhotoPath().equals("icon_add")) {
            Glide.with(context).load(R.mipmap.icon_add).into(holder.imageView);
        } else {
            RequestOptions options = new RequestOptions().error(R.mipmap.img_default);
            Glide.with(context)
                    .load(list.get(position).getPhotoPath())
                    .apply(options)
                    .into(holder.imageView);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        ImageView imageView_delete;
    }

    public void setDisabled(String disabled){
        this.disabled = disabled;
    }
}
