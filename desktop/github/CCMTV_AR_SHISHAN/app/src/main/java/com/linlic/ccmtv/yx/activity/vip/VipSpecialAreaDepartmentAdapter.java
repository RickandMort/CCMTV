package com.linlic.ccmtv.yx.activity.vip;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;

/**
 * Created by yu on 2018/5/28.
 */

class VipSpecialAreaDepartmentAdapter extends RecyclerView.Adapter<VipSpecialAreaDepartmentAdapter.ViewHolder>{

    private String[] mList;
    private Context context;
    private int selectedPosition;
    private OnItemClickListener mOnItemClickListener;

    public VipSpecialAreaDepartmentAdapter(Context context, String[] departmentStrings) {
        this.mList = departmentStrings;
        this.context=context;
    }

    @Override
    public VipSpecialAreaDepartmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //绑定行布局
        View view = View.inflate(parent.getContext(), R.layout.item_vip_area_singleline_text,null);
        //实例化ViewHolder
        VipSpecialAreaDepartmentAdapter.ViewHolder holder = new VipSpecialAreaDepartmentAdapter.ViewHolder(view);
        return holder;
    }

    //设置数据
    @Override
    public void onBindViewHolder(VipSpecialAreaDepartmentAdapter.ViewHolder holder, final int position) {
        //设置

        holder.text.setText(mList[position]);
        if (position == selectedPosition){
            holder.text.setTextColor(Color.parseColor("#000000"));
            holder.text.setTextSize(18);
//            holder.text.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.text.setBackgroundResource(R.drawable.vip_department_bg);
        } else {
            holder.text.setTextColor(Color.parseColor("#666666"));
            holder.text.setTextSize(17);
            holder.text.setBackgroundColor(Color.TRANSPARENT);
        }
        /*holder.text.setText(mList.get(position).getKname());
        if (position == selectedPosition){
            holder.text.setTextColor(Color.parseColor("#000000"));
            holder.text.setTextSize(18);
//            holder.text.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.text.setBackgroundResource(R.drawable.conference_condition_selected);
        } else {
            holder.text.setTextColor(Color.parseColor("#666666"));
            holder.text.setTextSize(17);
            holder.text.setBackgroundColor(Color.TRANSPARENT);
        }*/

        if(mOnItemClickListener!= null){
            holder.itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(position);
                    selectedPosition=position;
                    notifyDataSetChanged();
                }
            });
        }
    }

    //数量
    @Override
    public int getItemCount() {
        return mList.length;
    }

    public void setSelected(int i) {
        selectedPosition=i;
        notifyDataSetChanged();
    }

    //内部类
    class ViewHolder extends RecyclerView.ViewHolder{
        //行布局中的控件
        TextView text;
        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.id_tv_item_vip_area_department_singleline);
        }
    }

    public interface OnItemClickListener{
        void onClick(int position);
    }
    public void setOnItemClickListener(VipSpecialAreaDepartmentAdapter.OnItemClickListener onItemClickListener){
        this.mOnItemClickListener=onItemClickListener;
    }
}
