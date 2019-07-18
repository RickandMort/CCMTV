package com.linlic.ccmtv.yx.activity.subscribe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.conference.databean.ConferenceDepartmentBean;

import java.util.List;

/**
 * Created by yu on 2018/5/8.
 */

public class MaxDepartmentAdapter extends RecyclerView.Adapter<MaxDepartmentAdapter.ViewHolder>{
    //动态数组
    private List<ConferenceDepartmentBean> mList;
    private Context context;
    private OnItemClickListener mOnItemClickListener;
    private int selectedPosition = -1;

    //构造
    public MaxDepartmentAdapter(Context context, List<ConferenceDepartmentBean> mList) {
        this.mList = mList;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //绑定行布局
        View view = View.inflate(parent.getContext(), R.layout.item_conference_singleline_text,null);
        //实例化ViewHolder
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    //设置数据
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //设置
        holder.text.setText(mList.get(position).getKname());
        if (position == selectedPosition){
            holder.text.setTextColor(context.getResources().getColor(R.color.no_select_text_color));
            holder.text.setTextSize(15);
        } else {
            holder.text.setTextColor(context.getResources().getColor(R.color.no_select_text_color2));
            holder.text.setTextSize(14);
        }

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
        return mList.size();
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
            text = (TextView) itemView.findViewById(R.id.id_tv_item_conference_singleline);
        }
    }

    public interface OnItemClickListener{
        void onClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener=onItemClickListener;
    }
}
