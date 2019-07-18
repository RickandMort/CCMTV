package com.linlic.ccmtv.yx.activity.upload.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.entity.UploadedBean;

import java.util.List;

/**
 * Created by bentley on 2018/7/18.
 */

public class UploadAdapter3 extends RecyclerView.Adapter<UploadAdapter3.ViewHolder> {

    private Context context;
    private List<UploadedBean> uploadedList;
    private OnItemClickListener onItemClickListener;

    public UploadAdapter3(Context context, List<UploadedBean> uploadedList) {
        this.context = context;
        this.uploadedList = uploadedList;
//        Log.e("UploadAdapter3", "UploadAdapter3er3: listSize:"+uploadedList.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //绑定行布局
        View view = View.inflate(parent.getContext(), R.layout.item_upload_manager4,null);
        //实例化ViewHolder
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        UploadedBean uploadedBean=uploadedList.get(position);
     /*   if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onClick(position);
                }
            });
        }

        if (uploadedBean!=null){
            holder.name.setText(uploadedBean.getMvtitle());
            if (uploadedBean.getMvstatus().equals("1")){
                holder.verifyStatus.setTextColor(R.color.black);
                holder.verifyStatus.setText("审核中");
            }else if (uploadedBean.getMvstatus().equals("2")){
                holder.verifyStatus.setTextColor(R.color.layout_bg2);
                holder.verifyStatus.setText("已审核");
            }else {
                holder.verifyStatus.setTextColor(R.color.exams_list_item_text_color8);
                holder.verifyStatus.setText("审核未通过");
            }
            holder.date.setText(uploadedBean.getRow_add_time().substring(0,10));
            if (uploadedBean.getStyletype().equals("video")){
                Glide.with(context).load(uploadedBean.getImgurl()).error(R.mipmap.img_default).into(holder.icon);
                holder.tvItemTime.setText(uploadedBean.getVtime());
            }else {
                Glide.with(context).load(R.mipmap.ic_upload_case_pic).error(R.mipmap.img_default).into(holder.icon);
                holder.tvItemTime.setText(uploadedBean.getVtime()+"");
            }
        }*/
    }

    @Override
    public int getItemCount() {
        return uploadedList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView icon;
        TextView tvItemTime;
        TextView name;
        TextView date;
        TextView payType;
        TextView verifyStatus;

        public ViewHolder(View itemView) {
            super(itemView);
          /*  icon = itemView.findViewById(R.id.icon);
            tvItemTime = itemView.findViewById(R.id.item_time);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            payType = itemView.findViewById(R.id.pay_type);
            verifyStatus = itemView.findViewById(R.id.id_tv_item_upload_verify_status3);*/
        }
    }

    public interface OnItemClickListener{
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}