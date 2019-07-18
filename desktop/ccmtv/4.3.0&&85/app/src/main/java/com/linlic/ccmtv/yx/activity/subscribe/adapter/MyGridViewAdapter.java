package com.linlic.ccmtv.yx.activity.subscribe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.subscribe.entiy.Illness;

import java.util.List;

/**
 * Created by yu on 2017/11/30.
 */

public class MyGridViewAdapter extends BaseAdapter {
    private Context mContext;
    private int lastPosition;//定义一个标记为最后选择的位置
    private List<Illness> list;

    public void setData(List<Illness> list) {
        this.list = list;
    }

    public void setSeclection(int position) {
        lastPosition = position;
    }

    public MyGridViewAdapter(Context mContext) {
        super();
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return list.size();
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
    public View getView(final int position, View mView, ViewGroup arg2) {
        ViewHolder holder = null;
        if (mView == null) {
            holder = new ViewHolder();
            mView = LayoutInflater.from(mContext).inflate(R.layout.layout_item_gridview, null);
            holder.text = (TextView) mView.findViewById(R.id.idGridviewTextview);
            holder.check = (CheckBox) mView.findViewById(R.id.idGridviewCheck);
            holder.imageView = (ImageView) mView.findViewById(R.id.image);
            mView.setTag(holder);
        } else {
            holder = (ViewHolder) mView.getTag();
        }
        holder.text.setText(list.get(position).getIllness());
        if (list.get(position).isChecked()) {
            holder.imageView.setImageResource(R.mipmap.dialog_subscribe1);
        } else if (!list.get(position).isChecked()) {
            holder.imageView.setImageResource(R.mipmap.dialog_subscribe2);
        }
        return mView;

    }

    public OnItemChangeListener OnItemChangeListener;

    public void addOnItemChangeListener(OnItemChangeListener lis) {
        OnItemChangeListener = lis;
    }

    public interface OnItemChangeListener {
        void checkTrue();

        void checkfalse();
    }

    class ViewHolder {
        private TextView text;
        private CheckBox check;
        private ImageView imageView;
    }
}
