package com.linlic.ccmtv.yx.activity.bigcase.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.bigcase.entity.BigcaseUserEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by bentley on 2018/12/18.
 */

public class BigCaseUserAdapter extends RecyclerView.Adapter<BigCaseUserAdapter.ViewHolder> implements View.OnClickListener {

    private List<BigcaseUserEntity> data = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener mItemClickListener;

    public BigCaseUserAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_big_case_user, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final BigcaseUserEntity entrty = data.get(position);
        holder.itemView.setTag(position);
        holder.tvName.setText(entrty.getRealname());
        holder.tvNum.setText(entrty.getCount());
        String sex=entrty.getSex();
        if(sex.equals("0")){
            holder.ivSex.setVisibility(View.GONE);
        }else if(sex.equals("1")){
            holder.ivSex.setVisibility(View.VISIBLE);
            holder.ivSex.setBackground(mContext.getDrawable(R.mipmap.sex_boy));
        }else if(sex.equals("2")){
            holder.ivSex.setVisibility(View.VISIBLE);
            holder.ivSex.setBackground(mContext.getDrawable(R.mipmap.sex_girl));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void refresh(List<BigcaseUserEntity> newDatas) {
        data.clear();
        data.addAll(newDatas);
        notifyDataSetChanged();
    }

    public void clearData() {
        data.clear();
        notifyDataSetChanged();
    }

    public void addData(List<BigcaseUserEntity> newDatas) {
        if (newDatas != null) {
            int size = newDatas.size();
            data.addAll(newDatas);
            notifyItemRangeInserted(getItemCount(), size);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, BigcaseUserEntity entrty);
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
        @Bind(R.id.iv_sex)
        ImageView ivSex;
        @Bind(R.id.tv_num)
        TextView tvNum;
        @Bind(R.id.layout_bigcase)
        LinearLayout layoutBigcase;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
