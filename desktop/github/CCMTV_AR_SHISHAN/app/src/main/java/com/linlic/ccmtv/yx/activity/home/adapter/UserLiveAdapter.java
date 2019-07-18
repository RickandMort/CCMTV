package com.linlic.ccmtv.yx.activity.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.entity.Live;

import java.util.List;

/**
 * name：用户直播
 * author：Larry
 * data：2017/2/14.
 */
public class UserLiveAdapter extends BaseAdapter {
    Context context;
    List<Live> list;

    public UserLiveAdapter(Context context, List<Live> list) {
        this.context = context;
        this.list = list;
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int pos) {
        return this.list.get(pos);
    }

    public long getItemId(int pos) {
        return pos;
    }

    public View getView(final int pos, View view, ViewGroup group) {
        final ViewHolder viewHolder;
        final Live mContent = list.get(pos);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_userlive, null);
            viewHolder.tv_live_title = (TextView) view.findViewById(R.id.tv_live_title);
            viewHolder.tv_live_times = (TextView) view.findViewById(R.id.tv_live_times);
            viewHolder.tv_live_name = (TextView) view.findViewById(R.id.tv_live_name);
            viewHolder.iv_live_img = (ImageView) view.findViewById(R.id.iv_live_img);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv_live_title.setText(mContent.getLivename());
        viewHolder.tv_live_times.setText(mContent.getLivestrattime());//.substring(0, 10)
        viewHolder.tv_live_name.setText(mContent.getLivecontact());
        RequestOptions options = new RequestOptions().placeholder(R.mipmap.img_default);
        Glide.with(context)
                .load(mContent.getImgurl())
                .apply(options)
                .into(viewHolder.iv_live_img);
        return view;
    }

    final static class ViewHolder {
        TextView tv_live_title;
        TextView tv_live_times;
        TextView tv_live_name;
        ImageView iv_live_img;
    }
}
