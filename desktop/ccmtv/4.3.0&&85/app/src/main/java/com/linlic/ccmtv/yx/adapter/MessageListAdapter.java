package com.linlic.ccmtv.yx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.XUtilsImageLoader;

import java.util.List;
import java.util.Map;

/**
 * Created by yu on 2016/3/17.
 */
public class MessageListAdapter extends BaseAdapter {
    Context context;
    List<Map<String, Object>> data;
    String FLG;

    public MessageListAdapter(Context context, List<Map<String, Object>> data, String FLG) {
        this.context = context;
        this.data = data;
        this.FLG = FLG;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int arg0, View arg1, ViewGroup arg2) {
        final ViewHolder vh;
        if (arg1 == null) {
            vh = new ViewHolder();
            arg1 = LayoutInflater.from(context).inflate(R.layout.message_item,
                    null);
            vh.iv_message_item = (ImageView) arg1.findViewById(R.id.iv_message_item);
            vh.message_item_pop = (TextView) arg1.findViewById(R.id.message_item_pop);
            vh.message_item_date = (TextView) arg1.findViewById(R.id.message_item_date);
            vh.message_item_title = (TextView) arg1.findViewById(R.id.message_item_title);
            vh.tv_lookdetail = (TextView) arg1.findViewById(R.id.tv_lookdetail);
            vh.iv_red_dian = (ImageView) arg1.findViewById(R.id.iv_red_dian);
            arg1.setTag(vh);
        } else {
            vh = (ViewHolder) arg1.getTag();
        }
        loadImg(vh.iv_message_item, data.get(arg0).get("icon").toString());
        if ("addresser".equals(FLG)) {
            vh.message_item_pop.setText(data.get(arg0).get("musername").toString());
        } else {
            vh.message_item_pop.setText(data.get(arg0).get("pusername").toString());
        }

        vh.message_item_date.setText(data.get(arg0).get("mdate").toString());
        vh.message_item_title.setText(data.get(arg0).get("title").toString());
        if (data.get(arg0).get("ifnew").toString().equals("1") && "receiver".equals(FLG)) {
            vh.iv_red_dian.setVisibility(View.VISIBLE);
        }
        return arg1;
    }

    class ViewHolder {
        ImageView iv_message_item;
        TextView message_item_pop;
        TextView message_item_date;
        TextView message_item_title;
        TextView tv_lookdetail;
        ImageView iv_red_dian;
    }

    /**
     * name:使用xutils 加载图片 author:Tom 2016-1-7下午1:28:03
     *
     * @param img  图片控件
     * @param path 图片网络地址
     */
    public void loadImg(ImageView img, String path) {

        XUtilsImageLoader xUtilsImageLoader = new XUtilsImageLoader(context);
        xUtilsImageLoader.display(img, FirstLetter.getSpells(path));
    }

}
