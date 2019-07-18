package com.linlic.ccmtv.yx.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.linlic.ccmtv.yx.widget.CircleImageView;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.entity.Icon;
import com.linlic.ccmtv.yx.activity.vip.adapter.OnRecyclerviewItemClickListener;
import com.linlic.ccmtv.yx.utils.FirstLetter;

import java.util.List;
import java.util.Map;

/**
 * name：首页横向滚动适配器
 * author：Larry
 * data：2017/7/13.
 */
public class FollowActivityViewAdapter extends
        RecyclerView.Adapter<FollowActivityViewAdapter.ViewHolder> implements View.OnClickListener{
    private LayoutInflater mInflater;
    private List<Map<String,Object>> list;
    private int selectIndex = -1;
    //声明自定义的监听接口
    private OnRecyclerviewItemClickListener mOnRecyclerviewItemClickListener = null;
    private boolean isShow = false;

    public FollowActivityViewAdapter(Context context,List<Map<String,Object>> list, OnRecyclerviewItemClickListener mOnRecyclerviewItemClickListener) {
        mInflater = LayoutInflater.from(context);
        this.list = list;
        this.mOnRecyclerviewItemClickListener = mOnRecyclerviewItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        TextView iv_text;
        TextView iv_id;
        CircleImageView iv_headImg;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_follow,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.iv_text = (TextView) view
                .findViewById(R.id.iv_text);
        viewHolder.iv_id = (TextView) view
                .findViewById(R.id.iv_id);
        viewHolder.iv_headImg = view.findViewById(R.id.iv_headImg);
        //这里 我们可以拿到点击的item的view 对象，所以在这里给view设置点击监听，
        view.setOnClickListener(this);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.iv_text.setText(list.get(position).get("username").toString());
        viewHolder.iv_id.setText(list.get(position).get("uid").toString());
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(FirstLetter.getSpells(list.get(position).get("icon").toString()), viewHolder.iv_headImg);

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
    public void setIsShowRedDot(boolean isShow) {
        this.isShow = isShow;
    }


}
