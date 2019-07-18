package com.linlic.ccmtv.yx.activity.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
public class HomeNewRecyclerViewAdapter extends
        RecyclerView.Adapter<HomeNewRecyclerViewAdapter.ViewHolder> implements View.OnClickListener{
    private List<Icon> list;
    private LayoutInflater mInflater;
    private int selectIndex = -1;
    //声明自定义的监听接口
    private OnRecyclerviewItemClickListener mOnRecyclerviewItemClickListener = null;
    private boolean isShow = false;

    public HomeNewRecyclerViewAdapter(Context context, List<Icon> list, OnRecyclerviewItemClickListener mOnRecyclerviewItemClickListener) {
        mInflater = LayoutInflater.from(context);
        this.list = list;
        this.mOnRecyclerviewItemClickListener = mOnRecyclerviewItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        TextView tv_name;
        ImageView tv_img;
        ImageView iv_red_dot;
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
        View view = mInflater.inflate(R.layout.home_horizontallistview_item,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tv_name = (TextView) view
                .findViewById(R.id.home_horizontallistview_item_title);
        viewHolder.tv_img = (ImageView) view
                .findViewById(R.id.home_horizontallistview_item_img);
        viewHolder.iv_red_dot = view.findViewById(R.id.id_iv_red_dot);
        //这里 我们可以拿到点击的item的view 对象，所以在这里给view设置点击监听，
        view.setOnClickListener(this);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.tv_name.setText(list.get(position).getTitle());
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(FirstLetter.getSpells(list.get(position).getIcon()), viewHolder.tv_img);

        viewHolder.itemView.setTag(position);//给view设置tag以作为参数传递到监听回调方法中
        if (isShow && list.get(position).getTitle().equals("空中拜访")) {
            viewHolder.iv_red_dot.setVisibility(View.VISIBLE);
        } else {
            viewHolder.iv_red_dot.setVisibility(View.GONE);
        }
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
