package com.linlic.ccmtv.yx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * name：开通会员  套餐详情适配器
 * author：Larry
 * data：2016/5/24.
 */
public class VipTypeAdapter extends BaseAdapter {
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    Context context;
    boolean isVip;
    private int Select_position = -1;

    public int setSelect(int position){
        this.Select_position = position;
        return Select_position;
    }

    public VipTypeAdapter(Context context, List<Map<String, Object>> data, boolean isVip) {
        this.context = context;
        this.data = data;
        this.isVip = isVip;
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
            //convertView = LayoutInflater.from(context).inflate(R.layout.item_viptype, parent, false);
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_vip_pay, parent, false);
            holder = new ViewHolder();
            holder.tv_vip_type = (TextView) convertView.findViewById(R.id.tv_vip_type);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.tv_price_day = (TextView) convertView.findViewById(R.id.tv_price_day);
            holder.ll_month = (LinearLayout) convertView.findViewById(R.id.ll_month);

//            holder.tv_vipprice2 = (TextView) convertView.findViewById(R.id.tv_vipprice2);
//            holder.open_vip = (ImageView) convertView.findViewById(R.id.open_vip);
//            holder.openmenber_img = (ImageView) convertView.findViewById(R.id.openmenber_img);

            convertView.setTag(holder);
        } else {// 有直接获得ViewHolder
            holder = (ViewHolder) convertView.getTag();
        }
        Map<String, Object> map = data.get(position);

        holder.tv_vip_type.setText("VIP" + map.get("integrationvip").toString() + "个月");
        holder.price.setText(map.get("money").toString()  );
        holder.tv_price_day.setText(map.get("moneyx").toString()  );

        if(Select_position == position){
            holder.ll_month.setBackground(context.getResources().getDrawable(R.mipmap.open_vip_checked));
        }else {
            holder.ll_month.setBackground(context.getResources().getDrawable(R.mipmap.open_vip_nocheck));
        }

//        if(map.get("moneyx").toString().length()<1){
//            holder.openmenber_img.setVisibility(View.GONE);
//            holder.tv_vipprice2.setVisibility(View.GONE);
//        }else{
//            holder.openmenber_img.setVisibility(View.VISIBLE);
//            holder.tv_vipprice2.setVisibility(View.VISIBLE);
//        }
//
//        if (isVip) {
//            holder.open_vip.setImageResource(R.mipmap.icon_open_open);
//        }
//        holder.open_vip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, OpenMenberActivity.class);
//                double money = 0.01;
//                money = Double.valueOf(data.get(position).get("money").toString());
//                intent.putExtra("viptitle", "VIP" + data.get(position).get("integrationvip").toString() + "个月");
//                intent.putExtra("vip_time", data.get(position).get("integrationvip").toString() + "个月");
//                intent.putExtra("payfor", "vip");//积分支付
//                intent.putExtra("vipflg_Str", data.get(position).get("vipflg_str").toString());//积分支付
//                intent.putExtra("money", money);
//                context.startActivity(intent);
//            }
//        });
        return convertView;
    }

    static class ViewHolder {
        TextView tv_vip_type;
        TextView price;
        TextView tv_price_day;
        LinearLayout ll_month;
//        TextView tv_vipprice2;
//        ImageView open_vip;
//        ImageView openmenber_img;
    }
}

