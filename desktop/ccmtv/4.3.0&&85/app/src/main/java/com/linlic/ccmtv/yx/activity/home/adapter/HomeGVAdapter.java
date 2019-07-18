package com.linlic.ccmtv.yx.activity.home.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.home.entry.IconArr;
import com.linlic.ccmtv.yx.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * name：首页轮播图下面选择卡
 * author：Larry
 * data：2017/7/7.
 */
public class HomeGVAdapter extends BaseAdapter {
    private Context context;
    private List<IconArr> iconArrs = new ArrayList<>();

    public HomeGVAdapter(Context context, List<IconArr> iconArrs) {
        this.context = context;
        this.iconArrs = iconArrs;
    }

    public int getCount() {
        return iconArrs.size();
    }

    public Object getItem(int pos) {
        return iconArrs.get(pos);
    }

    public long getItemId(int pos) {
        return pos;
    }

    public View getView(final int pos, View view, ViewGroup group) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.home_gv_item
                    , null);
            // viewHolder.iv_gvimg = (ImageView) view.findViewById(R.id.iv_gvimg);
            viewHolder.tv_gvtext = (TextView) view.findViewById(R.id.tv_gvtext);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                int MinimumWidth = context.getResources().getDrawable(R.mipmap.icon_11).getMinimumWidth();
                int MinimumHeight = context.getResources().getDrawable(R.mipmap.icon_11).getMinimumHeight();
                final BitmapDrawable nav_up = new BitmapDrawable(Utils.returnBitmap(iconArrs.get(pos).getImgurl()));
                //nav_up.setBounds(0, 0, MinimumWidth, MinimumHeight);
                nav_up.setBounds(0, 0, MinimumWidth, MinimumHeight);
                viewHolder.tv_gvtext.post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        viewHolder.tv_gvtext.setCompoundDrawables(null, nav_up, null, null);
                        //  ImageLoader.load(context, iconArrs.get(pos).getImgurl(), viewHolder.iv_gvimg);
                        viewHolder.tv_gvtext.setText(iconArrs.get(pos).getTitle());
                    }
                });

            }
        }).start();
        // Calculate the item width by the column number to let total width fill the screen width
        // 根据列数计算项目宽度，以使总宽度尽量填充屏幕
      /*  int itemWidth = (int) (context.getResources().getDisplayMetrics().widthPixels - 4 * 10) / 4;
        // Calculate the height by your scale rate, I just use itemWidth here
        // 下面根据比例计算您的item的高度，此处只是使用itemWidth
        int itemHeight = itemWidth;2

        AbsListView.LayoutParams param = new AbsListView.LayoutParams(
                itemWidth,
                itemHeight);
        view.setLayoutParams(param);*/

        return view;
    }

    final static class ViewHolder {
        //   ImageView iv_gvimg;
        TextView tv_gvtext;
    }
}
