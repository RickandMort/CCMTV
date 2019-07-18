package com.linlic.ccmtv.yx.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.entity.Exchange_record_entity;
import com.linlic.ccmtv.yx.activity.entity.Get_integral_list_item;
import com.linlic.ccmtv.yx.activity.integral_mall.MyItemClickListener;

import java.util.List;

/**
 * Created by Administrator on 2017/12/4.
 */

public class Get_IntegralAdapter extends RecyclerView.Adapter {


    private static final String TAG = Get_IntegralAdapter.class.getSimpleName();
    private List<Get_integral_list_item> list;

    public Get_IntegralAdapter(List<Get_integral_list_item> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.get_integral_list_item, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new PersonViewHolder(view);
    }




    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        PersonViewHolder holder = (PersonViewHolder) viewHolder;
        holder.position = i;
        Get_integral_list_item get_integral_list_item = list.get(i);
        holder.text1.setText(get_integral_list_item.getText1());
        holder.text2.setText(get_integral_list_item.getText2());
        holder.text3.setText(get_integral_list_item.getText3());
        holder.text4.setText(get_integral_list_item.getText4());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    class PersonViewHolder extends RecyclerView.ViewHolder
    {

        public TextView text1;
        public TextView text2;
        public TextView text3;
        public TextView text4;
        public int position;
        public PersonViewHolder(View itemView) {
            super(itemView);
            text1 = (TextView) itemView.findViewById(R.id.text1);
            text2 = (TextView) itemView.findViewById(R.id.text2);
            text3 = (TextView) itemView.findViewById(R.id.text3);
            text4 = (TextView) itemView.findViewById(R.id.text4);
        }
    }

}