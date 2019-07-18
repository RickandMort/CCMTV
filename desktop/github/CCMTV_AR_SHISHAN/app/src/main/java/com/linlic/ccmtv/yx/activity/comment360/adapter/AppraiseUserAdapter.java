package com.linlic.ccmtv.yx.activity.comment360.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.comment360.entity.AppraiseUserEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by bentley on 2019/1/15.
 */

public class AppraiseUserAdapter extends RecyclerView.Adapter<AppraiseUserAdapter.ViewHolder> implements View.OnClickListener {
    private List<AppraiseUserEntity> data = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener mItemClickListener;

    public AppraiseUserAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appraise_user, parent, false);
        view.setOnClickListener(this);
        return new AppraiseUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final AppraiseUserEntity entrty = data.get(position);
        holder.itemView.setTag(position);
        holder.tvName.setText(entrty.getUname());
        holder.tvGrade.setText(entrty.getScore());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void refresh(List<AppraiseUserEntity> newDatas) {
        data.clear();
        data.addAll(newDatas);
        notifyDataSetChanged();
    }

    public void clearData() {
        data.clear();
        notifyDataSetChanged();
    }

    public void addData(List<AppraiseUserEntity> newDatas) {
        if (newDatas != null) {
            int size = newDatas.size();
            data.addAll(newDatas);
            notifyItemRangeInserted(getItemCount(), size);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, AppraiseUserEntity entrty);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick((Integer) v.getTag(), data.get((Integer) v.getTag()));
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_grade)
        TextView tvGrade;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
