package com.linlic.ccmtv.yx.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.entity.Get_integral_list_item;
import com.linlic.ccmtv.yx.activity.entity.StepData;
import com.linlic.ccmtv.yx.utils.DateUtil;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2017/12/4.
 */

public class Historical_step_numberAdapter extends RecyclerView.Adapter {


    private static final String TAG = Historical_step_numberAdapter.class.getSimpleName();
    private List<StepData> list;

    public Historical_step_numberAdapter(List<StepData> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.historical_step_number_list_item, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new PersonViewHolder(view);
    }




    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        PersonViewHolder holder = (PersonViewHolder) viewHolder;
        holder.position = i;
        StepData stepData = list.get(i);
        String[] date = stepData.getToday().split("-");
        holder.time.setText(date[0]+"年"+date[1]+"月"+date[2]+"日");
        holder.main_text_step.setText(stepData.getStep());
        BigDecimal bg = new BigDecimal(0.043512*Integer.parseInt(stepData.getStep()));
        double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        holder.calorie.setText("消耗了"+ f1+"卡路里");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    class PersonViewHolder extends RecyclerView.ViewHolder
    {

        public TextView time;
        public TextView main_text_step;
        public TextView calorie;
        public int position;
        public PersonViewHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.time);
            main_text_step = (TextView) itemView.findViewById(R.id.main_text_step);
            calorie = (TextView) itemView.findViewById(R.id.calorie);
        }
    }

}