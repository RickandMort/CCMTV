package com.linlic.ccmtv.yx.activity.subscribe.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.subscribe.entiy.Illness;

import java.util.List;

import cn.cc.android.sdk.util.Logger;

/**
 * Created by Niklaus on 2017/11/29.
 */

public class RecyclerviewAdapter extends RecyclerView.Adapter {
    private Context context;

    public static interface OnRecyclerViewListener {
        void onItemClick(int position);

        boolean onItemLongClick(int position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private static final String TAG = RecyclerviewAdapter.class.getSimpleName();
    private List<Illness> list;

    public RecyclerviewAdapter(Context context,List<Illness> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        Logger.d(TAG, "onCreateViewHolder, i: " + i);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_illness, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
//        Logger.d(TAG, "onBindViewHolder, i: " + i + ", viewHolder: " + viewHolder);
        PersonViewHolder holder = (PersonViewHolder) viewHolder;
        holder.position = i;
        Illness person = list.get(i);
        holder.illness.setText(person.getIllness());
        if (list.get(i).isChecked()){
            holder.illness.setTextColor(Color.parseColor("#333333"));
            holder.illness.setBackgroundResource(R.drawable.conference_condition_selected);
        }else if (!list.get(i).isChecked()){
            holder.illness.setTextColor(Color.parseColor("#999999"));
            holder.illness.setBackgroundResource(R.drawable.store_title_image_mr);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView illness;
        public ImageView imageView;
        public int position;

        public PersonViewHolder(View itemView) {
            super(itemView);
            illness = (TextView) itemView.findViewById(R.id.item_illness);
            imageView = (ImageView) itemView.findViewById(R.id.item_img);
            illness.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (null != onRecyclerViewListener) {
                onRecyclerViewListener.onItemClick(position);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (null != onRecyclerViewListener) {
                return onRecyclerViewListener.onItemLongClick(position);
            }
            return false;
        }
    }
}
