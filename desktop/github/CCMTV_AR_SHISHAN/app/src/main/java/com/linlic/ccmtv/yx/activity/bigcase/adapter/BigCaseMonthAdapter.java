package com.linlic.ccmtv.yx.activity.bigcase.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.bigcase.BigCaseUpdateActivity;
import com.linlic.ccmtv.yx.activity.bigcase.entity.BigCaseMonthEntrry;
import com.linlic.ccmtv.yx.adapter.MyImgAdapter;
import com.linlic.ccmtv.yx.utils.permission.SpacesItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by bentley on 2018/12/18.
 */

public class BigCaseMonthAdapter extends RecyclerView.Adapter<BigCaseMonthAdapter.ViewHolder> implements View.OnClickListener {
    private List<BigCaseMonthEntrry> data = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener mItemClickListener;
    private MyImgAdapter adapter;
    //private List<String> list = new ArrayList<>();
    private String is_type;

    public BigCaseMonthAdapter(Context context,String is_type) {
        mContext = context;
        is_type = is_type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_big_case_month, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final BigCaseMonthEntrry entrty = data.get(position);
        holder.itemView.setTag(position);

        holder.tvTitle.setText(entrty.getTitle());
        holder.tvTime.setText(entrty.getTime());
        holder.tvCount.setText(entrty.getCount());

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        holder.myRecyclerview.setLayoutManager(manager);
        holder.myRecyclerview.addItemDecoration(new SpacesItemDecoration(10));
        //设置为横向滑动
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);

        try {
            List<String> list = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(entrty.getImg());
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(entrty.getHttp_url() + jsonArray.get(i).toString());
            }
            adapter = new MyImgAdapter(mContext, list);
            holder.myRecyclerview.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.llOnclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, BigCaseUpdateActivity.class);
                intent.putExtra("id", entrty.getId());
                intent.putExtra("is_type",is_type);
                intent.putExtra("is_edit", entrty.getIs_edit());
                mContext.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void refresh(List<BigCaseMonthEntrry> newDatas) {
        data.clear();
        data.addAll(newDatas);
        notifyDataSetChanged();
    }

    public void clearData() {
        data.clear();
        notifyDataSetChanged();
    }

    public void addData(List<BigCaseMonthEntrry> newDatas) {
        if (newDatas != null) {
            int size = newDatas.size();
            data.addAll(newDatas);
            notifyItemRangeInserted(getItemCount(), size);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, BigCaseMonthEntrry entrty);
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

    class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.ll_right)
        LinearLayout llRight;
        @Bind(R.id.my_recyclerview)
        RecyclerView myRecyclerview;
        @Bind(R.id.tv_count)
        TextView tvCount;
        @Bind(R.id.ll_onclick)
        LinearLayout llOnclick;
        @Bind(R.id.layout_bigcase)
        LinearLayout layoutBigcase;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
