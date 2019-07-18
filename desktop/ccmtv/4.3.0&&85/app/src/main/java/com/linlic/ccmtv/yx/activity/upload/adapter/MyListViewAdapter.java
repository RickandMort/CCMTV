package com.linlic.ccmtv.yx.activity.upload.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.XUtilsImageLoader;

import java.util.List;
import java.util.Map;

/**
 * name：正在审核、已经上传使用（视频）
 * author：MrSong
 * data：2016/3/31.
 */
public class MyListViewAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, String>> list;
    private LayoutInflater inflater;

    public MyListViewAdapter(Context context, List<Map<String, String>> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
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

            convertView = inflater.inflate(R.layout.upload_item1, null);
            holder.image = (ImageView) convertView.findViewById(R.id.upload_item_img);
            holder.upload_top_item_img = (ImageView) convertView.findViewById(R.id.upload_top_item_img);
            holder.time = (TextView) convertView.findViewById(R.id.upload_times);
            holder.title = (TextView) convertView.findViewById(R.id.upload_item_title);
            holder.id = (TextView) convertView.findViewById(R.id.department_id);
            holder.shenhe = (TextView) convertView.findViewById(R.id.upload_on_demand);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(list.get(position).get("mvtitle"));
        holder.time.setText(list.get(position).get("row_add_time"));
        holder.id.setText(list.get(position).get("id"));
        holder.shenhe.setText(list.get(position).get("shenhe"));

        if( SharedPreferencesTools.getVipFlag(context) == 1){
            //VIP
            if(Integer.parseInt(list.get(position).get("money"))>0){
                //会员
                holder.upload_top_item_img.setImageResource(R.mipmap.vip_img);
                holder.upload_top_item_img.setVisibility(  View.VISIBLE);
            }else{
                //免费
                holder.upload_top_item_img.setVisibility(  View.GONE);
//                holder.upload_top_item_img.setImageResource(R.mipmap.free_img_blue);
//                holder.upload_top_item_img.setVisibility(  View.VISIBLE);
            }
        }else{
            //非vip
            if(Integer.parseInt(list.get(position).get("money"))>0){
                //积分
                holder.upload_top_item_img.setImageResource(R.mipmap.integral_img);
                holder.upload_top_item_img.setVisibility(  View.VISIBLE);
            }else{
                //免费
                holder.upload_top_item_img.setVisibility(  View.GONE);
//                holder.upload_top_item_img.setImageResource(R.mipmap.free_img_blue);
//                holder.upload_top_item_img.setVisibility(  View.VISIBLE);
            }
        }
        XUtilsImageLoader xUtilsImageLoader = new XUtilsImageLoader(context);
        xUtilsImageLoader.display(holder.image, FirstLetter.getSpells(list.get(position).get("imgurl")));

        if (holder.shenhe.getText().equals("100.00%")){
            holder.shenhe.setText("上传成功");
        }

        return convertView;
    }

    public final class ViewHolder {
        public ImageView image;
        public ImageView upload_top_item_img;
        public TextView time;
        public TextView title;
        public TextView id;
        public TextView shenhe;
    }
}
