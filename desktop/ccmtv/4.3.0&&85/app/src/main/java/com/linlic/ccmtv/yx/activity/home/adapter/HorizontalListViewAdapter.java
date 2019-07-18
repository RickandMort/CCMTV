package com.linlic.ccmtv.yx.activity.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.home.entry.FloatArrInfo;

import java.util.List;

/**
 * name：
 * author：Larry
 * data：2017/7/12.
 */
public class HorizontalListViewAdapter extends BaseAdapter {
    private List<FloatArrInfo> floatArrInfos;
    private Context mContext;
    private int selectIndex = -1;

    public HorizontalListViewAdapter(Context context, List<FloatArrInfo> floatArrInfos) {
        this.mContext = context;
        this.floatArrInfos = floatArrInfos;
    }

    @Override
    public int getCount() {
        return floatArrInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.home_item
                    , null);
            holder.tv_keshi = (TextView) convertView.findViewById(R.id.tv_keshi);
            holder.tv_keshi_lan = (View) convertView.findViewById(R.id.tv_keshi_lan);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == selectIndex) {
            holder.tv_keshi_lan.setSelected(true);
        } else {
            holder.tv_keshi_lan.setSelected(false);
        }
        holder.tv_keshi.setText(floatArrInfos.get(position).getName());
        return convertView;
    }

    private static class ViewHolder {
        private TextView tv_keshi;
        private View tv_keshi_lan;
    }

    public void setSelectIndex(int i) {
        selectIndex = i;
    }
}
