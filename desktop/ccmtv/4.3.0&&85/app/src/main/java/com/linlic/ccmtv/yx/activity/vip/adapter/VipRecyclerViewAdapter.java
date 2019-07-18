package com.linlic.ccmtv.yx.activity.vip.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.home.entry.FloatArrInfo;
import com.linlic.ccmtv.yx.utils.FirstLetter;

import java.util.List;
import java.util.Map;

/**
 * name：首页横向滚动适配器
 * author：Larry
 * data：2017/7/13.
 */
public class VipRecyclerViewAdapter extends
        RecyclerView.Adapter<VipRecyclerViewAdapter.ViewHolder> implements View.OnClickListener{
    private LayoutInflater mInflater;
    private List<Map<String ,Object>> arrays;
    private int selectIndex = -1;
    //声明自定义的监听接口
    private OnRecyclerviewItemClickListener mOnRecyclerviewItemClickListener = null;
    public VipRecyclerViewAdapter(Context context, List<Map<String ,Object>>  arrays,OnRecyclerviewItemClickListener mOnRecyclerviewItemClickListener) {
        mInflater = LayoutInflater.from(context);
        this.arrays = arrays;
        this.mOnRecyclerviewItemClickListener = mOnRecyclerviewItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        TextView tv_name;
        ImageView tv_img;
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
        View view = mInflater.inflate(R.layout.vip_rcview_item,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tv_name = (TextView) view
                .findViewById(R.id.tv_name);
        viewHolder.tv_img = (ImageView) view
                .findViewById(R.id.tv_img);
        //这里 我们可以拿到点击的item的view 对象，所以在这里给view设置点击监听，
        view.setOnClickListener(this);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.tv_name.setText(arrays.get(position).get("tv_name").toString());
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(FirstLetter.getSpells(arrays.get(position).get("tv_img").toString()), viewHolder.tv_img);
        if (position == selectIndex) {
            viewHolder.tv_name.setSelected(true);
        } else {
            viewHolder.tv_name.setSelected(false);
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
