package com.linlic.ccmtv.yx.activity.rules_to_compensate.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;

import java.util.List;
import java.util.Map;

/**
 * Created by bentley on 2018/9/21.
 */

public class StuPeriodicalExaminerAdapter extends RecyclerView.Adapter<StuPeriodicalExaminerAdapter.ViewHolder> {
    //动态数组
    private List<Map<String,String>> mList;
    private Context context;
    private StuPeriodicalExaminerAdapter.OnItemClickListener mOnItemClickListener;
    private int selectedPosition = 0;

    public StuPeriodicalExaminerAdapter(Context context, List<Map<String, String>> mList) {
        this.mList = mList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //绑定行布局
        View view = View.inflate(parent.getContext(), R.layout.item_periodical_examiner_singleline_text,null);
        //实例化ViewHolder
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
//设置
        holder.text.setText(mList.get(position).get("name"));
        if (position == selectedPosition){
            holder.text.setTextColor(Color.parseColor("#000000"));
            holder.text.setTextSize(16);
            holder.line.setVisibility(View.VISIBLE);
        } else {
            holder.text.setTextColor(Color.parseColor("#666666"));
            holder.text.setTextSize(14);
            holder.line.setVisibility(View.INVISIBLE);
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
        View line;
        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.id_tv_item_periodical_examiner_singleline);
            line = (View) itemView.findViewById(R.id.id_line);
        }
    }

    public interface OnItemClickListener{
        void onClick(int position);
    }
    public void setOnItemClickListener(StuPeriodicalExaminerAdapter.OnItemClickListener onItemClickListener){
        this.mOnItemClickListener=onItemClickListener;
    }
}
