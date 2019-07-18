package com.linlic.ccmtv.yx.activity.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.widget.NumberProgressBar;

import java.util.List;
import java.util.Map;

/**
 * name：柳叶杯排行榜adapter
 * author：Larry
 * data：2017/4/10.
 */
public class WillowCupTopAdapter extends BaseAdapter {
    Context context;
    List<Map<String, Object>> data;

    public WillowCupTopAdapter(Context context, List<Map<String, Object>> data) {
        this.context = context;
        this.data = data;
    }

    public int getCount() {
        return this.data.size();
    }

    public Object getItem(int pos) {
        return this.data.get(pos);
    }

    public long getItemId(int pos) {
        return pos;
    }

    public View getView(final int pos, View view, ViewGroup group) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_willowcuptop, null);
            viewHolder.tv_position = (TextView) view.findViewById(R.id.tv_position);
            viewHolder.tv_toptitle = (TextView) view.findViewById(R.id.tv_toptitle);
            viewHolder.tv_topnum = (TextView) view.findViewById(R.id.tv_topnum);
            viewHolder.topprogressbar = (NumberProgressBar) view.findViewById(R.id.topprogressbar);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv_position.setText(pos + 1 + ".");
        String percent = data.get(pos).get("percent").toString();
        double d = Double.valueOf(percent) * 100;
        viewHolder.topprogressbar.setProgress((int) d);
        viewHolder.tv_topnum.setText(data.get(pos).get("num") + "");
        viewHolder.tv_toptitle.setText(data.get(pos).get("title").toString());
        return view;
    }

    final static class ViewHolder {
        TextView tv_position;
        TextView tv_toptitle;
        TextView tv_topnum;
        NumberProgressBar topprogressbar;
    }
}
