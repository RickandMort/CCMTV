package com.linlic.ccmtv.yx.activity.bigcase.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.bigcase.entity.BigCaseYearMonthEntrty;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by bentley on 2018/12/18.
 */

public class BigCaseYearNonthAdapter extends RecyclerView.Adapter<BigCaseYearNonthAdapter.ViewHolder> implements View.OnClickListener {
    private List<BigCaseYearMonthEntrty> data = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener mItemClickListener;

    public BigCaseYearNonthAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_big_case_yearmonth, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final BigCaseYearMonthEntrty entrty = data.get(position);
        holder.itemView.setTag(position);
        holder.mTittle.setText(entrty.getTitle());
        holder.tvMonth.setText(entrty.getMonth()+"月");
        holder.itemNum.setText(entrty.getCount());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void refresh(List<BigCaseYearMonthEntrty> newDatas) {
        data.clear();
        data.addAll(newDatas);
        notifyDataSetChanged();
    }

    public void clearData() {
        data.clear();
        notifyDataSetChanged();
    }

    public void addData(List<BigCaseYearMonthEntrty> newDatas) {
        if (newDatas != null) {
            int size = newDatas.size();
            data.addAll(newDatas);
            notifyItemRangeInserted(getItemCount(), size);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, BigCaseYearMonthEntrty entrty);
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
        @Bind(R.id._item_title)
        TextView mTittle;
        @Bind(R.id.layout_bigcase)
        LinearLayout mLayoutBigcase;
        @Bind(R.id.tv_month)
        TextView tvMonth;
        @Bind(R.id.item_num)
        TextView itemNum;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
