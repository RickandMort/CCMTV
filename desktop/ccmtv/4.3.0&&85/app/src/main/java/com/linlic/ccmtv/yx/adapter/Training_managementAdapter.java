
package com.linlic.ccmtv.yx.adapter;

import android.content.Context;
import android.graphics.Color;
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

public class Training_managementAdapter extends BaseAdapter {
    /**
     * 上下文对象
     */
    private Context mContext = null;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

    private  int mRightWidth = 0;
    private String is_show_btn = "";

    /**
     * @param
     */
    public Training_managementAdapter(Context ctx, List<Map<String, Object>> data, int rightWidth,String is_show_btn) {
        mContext = ctx;
        this.data = data;
        mRightWidth = rightWidth;
        this.is_show_btn = is_show_btn;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_training_management, parent, false);
            holder = new ViewHolder();
            holder.item_left = (RelativeLayout)convertView.findViewById(R.id.item_left);
            holder.item_right = (RelativeLayout)convertView.findViewById(R.id.item_right);
            holder.item_content = (TextView) convertView.findViewById(R.id.item_content);
            holder.item_time = (TextView)convertView.findViewById(R.id.item_time);
            holder._item_button = (TextView)convertView.findViewById(R.id._item_button);
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

        holder.item_content.setText(map.get("name").toString());
        holder.item_time.setText(map.get("add_time").toString()+" - "+map.get("end_time").toString());

//                1 未开始 2 进行中 3 已结束 4 审核中 5 审核通过 6 审核失败
        switch (map.get("status").toString()){
            case "1":

                    holder._item_button.setText("编辑");

                holder._item_button.setBackgroundResource(R.mipmap.training_05);
                holder._item_button.setTextColor(Color.parseColor("#ffffff"));
                break;
            case "2":
                holder._item_button.setText("上传");
                holder._item_button.setBackgroundResource(R.mipmap.training_05);
                holder._item_button.setTextColor(Color.parseColor("#ffffff"));
                break;
            case "3":
                holder._item_button.setText("上传");
                holder._item_button.setBackgroundResource(R.mipmap.training_05);
                holder._item_button.setTextColor(Color.parseColor("#ffffff"));
                break;
            case "4":
                holder._item_button.setText("审核中");
                holder._item_button.setBackgroundResource(R.mipmap.training_06);
                holder._item_button.setTextColor(Color.parseColor("#666666"));
                break;
            case "5":
                holder._item_button.setText("已通过");
                holder._item_button.setBackgroundResource(R.mipmap.training_06);
                holder._item_button.setTextColor(Color.parseColor("#666666"));
                break;
            case "6":
                holder._item_button.setText("未通过");
                holder._item_button.setBackgroundResource(R.mipmap.training_06);
                holder._item_button.setTextColor(Color.parseColor("#ff3333"));
                break;
            default:
                holder._item_button.setText("未开始");
                holder._item_button.setBackgroundResource(R.mipmap.training_06);
                holder._item_button.setTextColor(Color.parseColor("#666666"));
                break;
        }

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
        TextView item_content;
        TextView item_time;
        TextView _item_button;
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
