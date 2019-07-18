
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

public class Message_managementAdapter extends BaseAdapter {
    /**
     * 上下文对象
     */
    private Context mContext = null;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

    private  int mRightWidth = 0;

    /**
     * @param
     */
    public Message_managementAdapter(Context ctx, List<Map<String, Object>> data, int rightWidth) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_message_management, parent, false);
            holder = new ViewHolder();
            holder.item_left = (RelativeLayout)convertView.findViewById(R.id.item_left);
            holder.item_right = (RelativeLayout)convertView.findViewById(R.id.item_right);
            holder._item_title = (TextView) convertView.findViewById(R.id._item_title);
            holder._item_content = (TextView)convertView.findViewById(R.id._item_content);
            holder._item_time = (TextView)convertView.findViewById(R.id._item_time);
            holder._item_num = (TextView)convertView.findViewById(R.id._item_num);
            holder._item_icon = (ImageView) convertView.findViewById(R.id._item_icon);
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

        holder._item_title.setText(map.get("name").toString());
        holder._item_content.setText(map.get("content").toString() );
        holder._item_time.setText(map.get("create_time").toString() );
        holder._item_num.setText(map.get("count").toString() );
        if(map.get("count").toString().equals("0")){
            holder._item_num.setVisibility(View.INVISIBLE);
        }else{
            holder._item_num.setVisibility(View.VISIBLE);
        }
        loadImg( holder._item_icon,map.get("icon").toString());

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
        TextView _item_title;
        TextView _item_content;
        TextView _item_time;
        TextView _item_num;
        ImageView _item_icon;
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
