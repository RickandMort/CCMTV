
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

public class SwipeAdapter extends BaseAdapter {
    /**
     * 上下文对象
     */
    private Context mContext = null;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

    private  int mRightWidth = 0;

    /**
     * @param
     */
    public SwipeAdapter(Context ctx,List<Map<String, Object>> data, int rightWidth) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.my_collection_item, parent, false);
            holder = new ViewHolder();
            holder.item_left = (RelativeLayout)convertView.findViewById(R.id.item_left);
            holder.item_right = (RelativeLayout)convertView.findViewById(R.id.item_right);
            holder.collect_top_item_img = (ImageView)convertView.findViewById(R.id.collect_top_item_img);
            holder.collect_item_img = (ImageView) convertView.findViewById(R.id.collect_item_img);
            holder.collect_item_title = (TextView)convertView.findViewById(R.id.collect_item_title);
            holder.collect_times = (TextView)convertView.findViewById(R.id.collect_times);
            holder.collect_on_demand = (TextView)convertView.findViewById(R.id.collect_on_demand);
            holder.item_right_txt = (TextView)convertView.findViewById(R.id.item_right_txt);
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

        holder.collect_item_title.setText(map.get("collect_item_title").toString());
        holder.collect_times.setText(map.get("collect_times").toString());
        holder.collect_on_demand.setText("播放数:" + map.get("collect_on_demand").toString());
        loadImg(holder.collect_item_img,map.get("collect_item_img").toString());

        //videopaymoney  不为0则为收费 为0 测试代表着是非收费视频
        if( !map.get("videopaymoney").toString() .equals("0") ){
            //收费
            holder.collect_top_item_img.setImageResource(R.mipmap.charge);
            holder.collect_top_item_img.setVisibility(View.VISIBLE);
        }else {
            holder.collect_top_item_img.setVisibility(View.GONE);
            if(map.get("money").toString().equals("3")){
                //会员
                holder.collect_top_item_img.setImageResource(R.mipmap.vip_img);
                holder.collect_top_item_img.setVisibility(  View.VISIBLE);
            }
        }

        //holder.collect_item_img.setImageResource(msg.get(key));

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
        TextView collect_on_demand;
        ImageView collect_item_img;

        TextView item_right_txt;
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
