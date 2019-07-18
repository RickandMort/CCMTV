package com.linlic.ccmtv.yx.activity.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.linlic.ccmtv.yx.R;

import java.util.ArrayList;
import java.util.List;

/**
 * name：
 * author：Larry
 * data：2017/3/28.
 */
public class ChooseTypeAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> showTypes = new ArrayList<>();

    public ChooseTypeAdapter(Context mContext, List<String> showTypes) {
        this.mContext = mContext;
        this.showTypes = showTypes;
    }

    @Override
    public int getCount() {
        return showTypes.size();
    }

    @Override
    public Object getItem(int i) {
        return showTypes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        if (view == null) {
            vh = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_type, null);

            vh.title = (Button) view.findViewById(R.id.but_item_hotkey);

            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        final String name = showTypes.get(i);
        vh.title.setText(name);
        return view;
    }

    public class ViewHolder {
        Button title;
    }
}

