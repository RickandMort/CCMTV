package com.linlic.ccmtv.yx.activity.assginks.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.assginks.entity.AssignKsUserEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by bentley on 2019/1/15.
 */

public class AssginKsUserAdapter extends RecyclerView.Adapter<AssginKsUserAdapter.ViewHolder> implements View.OnClickListener {
    private List<AssignKsUserEntity> data = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener mItemClickListener;

    public AssginKsUserAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_assign_ks_user, parent, false);
        view.setOnClickListener(this);
        return new AssginKsUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final AssignKsUserEntity entrty = data.get(position);
        holder.itemView.setTag(position);
        holder.tvName.setText(entrty.getRealname());
        holder.tvCycle.setText(entrty.getCycle_ks());
        holder.tvAssign.setText(entrty.getAssign_ks());
        if ("1".equals(entrty.getStatus())) {
            //0未入  1 已入   如果已入则前面蓝点变灰色
            holder.viewDot.setBackgroundColor(mContext.getResources().getColor(R.color.bg_edittext_focused));
        } else {
            holder.viewDot.setBackgroundColor(mContext.getResources().getColor(R.color.videofive_comment_reply));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void refresh(List<AssignKsUserEntity> newDatas) {
        data.clear();
        data.addAll(newDatas);
        notifyDataSetChanged();
    }

    public void clearData() {
        data.clear();
        notifyDataSetChanged();
    }

    public void addData(List<AssignKsUserEntity> newDatas) {
        if (newDatas != null) {
            int size = newDatas.size();
            data.addAll(newDatas);
            notifyItemRangeInserted(getItemCount(), size);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, AssignKsUserEntity entrty);
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
        @Bind(R.id.tv_cycle)
        TextView tvCycle;
        @Bind(R.id.tv_assign)
        TextView tvAssign;
        @Bind(R.id.view_dot)
        View viewDot;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
