package com.linlic.ccmtv.yx.activity.comment360.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.comment360.entity.AppraiseIndexEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by bentley on 2019/1/15.
 */

public class AppraiseIndexAdapter extends RecyclerView.Adapter<AppraiseIndexAdapter.ViewHolder> implements View.OnClickListener{
    private List<AppraiseIndexEntity> data = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener mItemClickListener;

    public AppraiseIndexAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_assgin_ks, parent, false);
        view.setOnClickListener(this);
        return new AppraiseIndexAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final AppraiseIndexEntity entrty = data.get(position);
        holder.itemView.setTag(position);
        holder.mTvMonth.setText(entrty.getMonth());
        holder.mTvTittle.setText(entrty.getTitle());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void refresh(List<AppraiseIndexEntity> newDatas) {
        data.clear();
        data.addAll(newDatas);
        notifyDataSetChanged();
    }

    public void clearData(){
        data.clear();
        notifyDataSetChanged();
    }

    public void addData(List<AppraiseIndexEntity> newDatas) {
        if (newDatas != null) {
            int size = newDatas.size();
            data.addAll(newDatas);
            notifyItemRangeInserted(getItemCount(), size);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position, AppraiseIndexEntity entrty);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener!=null){
            mItemClickListener.onItemClick((Integer) v.getTag(), data.get((Integer) v.getTag()));
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.id_tv_item_gp_exam_manage_list_month)
        TextView mTvMonth;
        @Bind(R.id.id_tv_item_gp_exam_manage_list_title)
        TextView mTvTittle;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
