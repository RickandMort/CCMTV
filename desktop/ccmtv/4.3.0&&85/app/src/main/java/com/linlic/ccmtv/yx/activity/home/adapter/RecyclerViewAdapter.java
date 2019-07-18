package com.linlic.ccmtv.yx.activity.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.home.entry.FloatArrInfo;

import java.util.List;

/**
 * name：首页横向滚动适配器
 * author：Larry
 * data：2017/7/13.
 */
public class RecyclerViewAdapter extends
        RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements View.OnClickListener {
    private LayoutInflater mInflater;
    private List<FloatArrInfo> floatArrInfos;
    private int selectIndex = -1;
    OnItemClickListener mOnItemClickListener = null;

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public RecyclerViewAdapter(Context context, List<FloatArrInfo> floatArrInfos) {
        mInflater = LayoutInflater.from(context);
        this.floatArrInfos = floatArrInfos;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        TextView tv_keshi;
        View tv_keshi_lan;
    }

    @Override
    public int getItemCount() {
        return floatArrInfos.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.home_item,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tv_keshi = (TextView) view
                .findViewById(R.id.tv_keshi);
        viewHolder.tv_keshi_lan = view
                .findViewById(R.id.tv_keshi_lan);
        viewHolder.tv_keshi.setOnClickListener(this);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.tv_keshi.setText(floatArrInfos.get(position).getName());
        if (position == selectIndex) {
            viewHolder.tv_keshi_lan.setSelected(true);
        } else {
            viewHolder.tv_keshi_lan.setSelected(false);
        }
        viewHolder.tv_keshi.setTag(position);
    }

    public void setSelectIndex(int i) {
        selectIndex = i;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
