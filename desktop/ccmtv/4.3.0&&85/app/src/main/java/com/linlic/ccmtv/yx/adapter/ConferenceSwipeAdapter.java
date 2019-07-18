
package com.linlic.ccmtv.yx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.utils.FirstLetter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConferenceSwipeAdapter extends BaseAdapter {
    /**
     * 上下文对象
     */
    private Context mContext = null;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

    private  int mRightWidth = 0;

    /**
     * @param
     */
    public ConferenceSwipeAdapter(Context ctx, List<Map<String, Object>> data, int rightWidth) {
        mContext = ctx;
        this.data = data;
        mRightWidth = rightWidth;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_conference_collection_list, parent, false);
            holder = new ViewHolder();
            holder.item_left = (RelativeLayout)convertView.findViewById(R.id.item_left);
            holder.item_right = (RelativeLayout)convertView.findViewById(R.id.item_right);
            holder.collect_top_item_img = (ImageView)convertView.findViewById(R.id.id_iv_item_conference_main_list_pic);
//            holder.collect_item_collection_img = (ImageView) convertView.findViewById(R.id.id_iv_item_conference_main_list_collect);
            holder.collect_item_title = (TextView)convertView.findViewById(R.id.id_tv_item_conference_main_list_title);
            holder.collect_times = (TextView)convertView.findViewById(R.id.id_tv_item_conference_main_list_time);
            convertView.setTag(holder);
        } else {// 有直接获得ViewHolder
            holder = (ViewHolder)convertView.getTag();
        }

        LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        holder.item_left.setLayoutParams(lp1);
        LayoutParams lp2 = new LayoutParams(mRightWidth, LayoutParams.MATCH_PARENT);
        holder.item_right.setLayoutParams(lp2);

        Map<String, Object> map = data.get(position);

        holder.collect_item_title.setText(map.get("title").toString());
        loadImg(holder.collect_top_item_img,map.get("picurl").toString());

        holder.item_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRightItemClick(v, position);

                }
            }
        });
        return convertView;
    }

    /**
     *
     * name:使用xutils 夹在图片 author:Tom 2016-1-7下午1:28:03
     *
     * @param img
     *            图片控件
     * @param path
     *            图片网络地址
     */
    public void loadImg(ImageView img, String path) {

       /* XUtilsImageLoader xUtilsImageLoader = new XUtilsImageLoader(mContext);
        xUtilsImageLoader.display(img, FirstLetter.getSpells(path));*/
        RequestOptions options = new RequestOptions().placeholder(R.mipmap.img_default);
        Glide.with(mContext)
                .load(FirstLetter.getSpells(path))
                .apply(options)
                .into(img);
    }

    static class ViewHolder {
        RelativeLayout item_left;
        RelativeLayout item_right;
        ImageView collect_top_item_img;
        TextView collect_item_title;
        TextView collect_times;
//        ImageView collect_item_collection_img;

    }

    /**
     * 单击事件监听器
     */
    private onRightItemClickListener mListener = null;

    public void setOnRightItemClickListener(onRightItemClickListener listener){
        mListener = listener;
    }

    public interface onRightItemClickListener {
        void onRightItemClick(View v, int position);
    }
}
