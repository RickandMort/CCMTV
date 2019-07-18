package com.linlic.ccmtv.yx.activity.integral_mall;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.entity.Exchange_record_entity;

import java.util.List;
/**
 * Created by Administrator on 2017/12/4.
 */

public class Exchange_recordAdapter extends RecyclerView.Adapter {


    private static final String TAG = Exchange_recordAdapter.class.getSimpleName();
    private List<Exchange_record_entity> list;
    private MyItemClickListener mItemClickListener;

    public Exchange_recordAdapter(List<Exchange_record_entity> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.exchange_record_item, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new PersonViewHolder(view,mItemClickListener);
    }

    /**
     * 设置Item点击监听
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        PersonViewHolder holder = (PersonViewHolder) viewHolder;
        holder.position = i;
        Exchange_record_entity exchange_record_entity = list.get(i);
        holder.time_text.setText(exchange_record_entity.getTime_text());
        holder.time.setText(exchange_record_entity.getTime());
        holder.integral.setText(exchange_record_entity.getIntegral());
        holder.remark.setText(exchange_record_entity.getRemark());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener
    {

        public TextView time_text;
        public TextView time;
        public TextView integral;
        public TextView remark;
        public LinearLayout exchange_record_layout;
        public int position;
        private MyItemClickListener mListener;

        public PersonViewHolder(View itemView,MyItemClickListener listener) {
            super(itemView);
            time_text = (TextView) itemView.findViewById(R.id.time_text);
            time = (TextView) itemView.findViewById(R.id.time);
            integral = (TextView) itemView.findViewById(R.id.integral);
            remark = (TextView) itemView.findViewById(R.id.remark);
            this.mListener = listener;
            exchange_record_layout = (LinearLayout) itemView.findViewById(R.id.exchange_record_layout);
//            rootView = itemView.findViewById(R.id.recycler_view_test_item_person_view);
            exchange_record_layout.setOnClickListener(this);
//            rootView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener != null){
                mListener.onItemClick(v,getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
          /*  if(null != onRecyclerViewListener){
                return onRecyclerViewListener.onItemLongClick(position);
            }*/
            return false;
        }
    }

}