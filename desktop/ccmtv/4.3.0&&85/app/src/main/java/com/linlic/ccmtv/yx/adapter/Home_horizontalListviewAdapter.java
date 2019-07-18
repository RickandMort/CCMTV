package com.linlic.ccmtv.yx.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.entity.Icon;
import com.linlic.ccmtv.yx.activity.entity.StepData;
import com.linlic.ccmtv.yx.holder.BaseViewHolder;
import com.linlic.ccmtv.yx.utils.FirstLetter;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2017/12/4.
 */

public class Home_horizontalListviewAdapter extends BaseAdapter {
    private Context mContext;

    private static final String TAG = Home_horizontalListviewAdapter.class.getSimpleName();
    private List<Icon> list;

    public Home_horizontalListviewAdapter(Context mContext,List<Icon> list) {
        this.list = list;
        this.mContext = mContext;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.home_horizontallistview_item, parent, false);
        }
        ImageView tv = BaseViewHolder.get(convertView, R.id.home_horizontallistview_item_img);
        TextView tText = BaseViewHolder.get(convertView, R.id.home_horizontallistview_item_title);
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(FirstLetter.getSpells(list.get(position).getIcon()), tv);
        tText.setText(list.get(position).getTitle());
        return convertView;
    }



}