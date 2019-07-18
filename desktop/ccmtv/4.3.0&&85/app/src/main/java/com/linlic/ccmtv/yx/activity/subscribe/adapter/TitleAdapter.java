package com.linlic.ccmtv.yx.activity.subscribe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.subscribe.entiy.SubKeshiData;

import java.util.ArrayList;
import java.util.List;

/**
 * name：订阅顶部标题导航
 * author：Larry
 * data：2017/7/27.
 */
public class TitleAdapter extends BaseAdapter {
    private Context context;
    private List<SubKeshiData> keshiDatas = new ArrayList<>();

    public TitleAdapter(Context context, List<SubKeshiData> keshiDatas) {
        this.context = context;
        this.keshiDatas = keshiDatas;
    }

    public int getCount() {
        return keshiDatas.size();
    }

    public Object getItem(int pos) {
        return keshiDatas.get(pos);
    }

    public long getItemId(int pos) {
        return pos;
    }

    public View getView(final int pos, View view, ViewGroup group) {
        final ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_subscribe, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_mytitle = (TextView) view.findViewById(R.id.tv_mytitle);
            viewHolder.iv_img = (ImageView)view.findViewById(R.id.iv_img);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tv_mytitle.setText(keshiDatas.get(pos).getTitle());
        Glide.with(context)
                .load(keshiDatas.get(pos).getPicurl())
                .into(viewHolder.iv_img);

        return view;
    }

    static class ViewHolder {
        TextView tv_mytitle;
        ImageView iv_img;

    }

}
