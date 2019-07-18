package com.linlic.ccmtv.yx.activity.vip.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.channel.Channel_Main;
import com.linlic.ccmtv.yx.utils.FirstLetter;

import java.util.List;
import java.util.Map;

/**
 * name：首页横向滚动适配器
 * author：Larry
 * data：2017/7/13.
 */
public class VipRecyclerViewAdapter4 extends
        RecyclerView.Adapter<VipRecyclerViewAdapter4.ViewHolder> implements View.OnClickListener{
    private LayoutInflater mInflater;
    private List<Map<String ,Object>> arrays;
    private int selectIndex = -1;
    private Context context;
    //声明自定义的监听接口
    private OnRecyclerviewItemClickListener mOnRecyclerviewItemClickListener = null;
    public VipRecyclerViewAdapter4(Context context, List<Map<String ,Object>>  arrays,OnRecyclerviewItemClickListener mOnRecyclerviewItemClickListener) {
        mInflater = LayoutInflater.from(context);
        this.arrays = arrays;
        this.context = context;
        this.mOnRecyclerviewItemClickListener = mOnRecyclerviewItemClickListener;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        TextView tvCity;
        View tvCode;
    }

    @Override
    public int getItemCount() {
        return arrays.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.list_item_04,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tvCity = (TextView) view
                .findViewById(R.id.tvCity);
        viewHolder.tvCode =   view
                .findViewById(R.id.tvCode);
        //这里 我们可以拿到点击的item的view 对象，所以在这里给view设置点击监听，
        view.setOnClickListener(this);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {


        Map<String,Object> city = arrays.get(position);
        viewHolder.tvCity.setText(city.get("name").toString());
        if(position == selectIndex){
            viewHolder.tvCode.setVisibility(View.VISIBLE);
            viewHolder.tvCity.setTextColor(Color.parseColor("#B78A2E"));
        }else{
            viewHolder.tvCode.setVisibility(View.INVISIBLE);
            viewHolder.tvCity.setTextColor(Color.parseColor("#666666"));
        }
        viewHolder.itemView.setTag(position);//给view设置tag以作为参数传递到监听回调方法中


    }

    @Override
    public void onClick(View v) {
        //将监听传递给自定义接口
        mOnRecyclerviewItemClickListener.onItemClickListener(v, ((int) v.getTag()));
    }


    public void setSelectIndex(int i) {
        selectIndex = i;
    }


}
