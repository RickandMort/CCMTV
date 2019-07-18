package com.linlic.ccmtv.yx.activity.cashier;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by bentley on 2018/4/10.
 */

public class CashierAdapter extends BaseAdapter {
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private Context context;

    public CashierAdapter(List<Map<String, Object>> data, Context context) {
        this.data = data;
        this.context = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.item_wallet_consume_detail,null);
            viewHolder.ivConsumeIcon = (ImageView) convertView.findViewById(R.id.id_iv_item_consume_detail_icon);
            viewHolder.tvConsumeContent = (TextView) convertView.findViewById(R.id.id_tv_item_consume_detail_content);
            viewHolder.tvConsumeNumber = (TextView) convertView.findViewById(R.id.id_tv_item_consume_detail_number);
            viewHolder.tvConsumeTime = (TextView) convertView.findViewById(R.id.id_tv_item_consume_detail_time);
            viewHolder.tv_success = (TextView)convertView.findViewById(R.id.tv_success);
            convertView.setTag(viewHolder);
        } else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        try {
            if (data!=null||data.size()>0){
                Map<String, Object> map=data.get(position);

                /*  get_integral_item_state   1 代表 充值 费用
                2 代表 开通vip 费用
                3 代表 观看视频 和 购买积分 费用
                4 代表 用户观看我的收费视频 费用（收入记录）
                5 代表 购买电子书 视频 费用*/
                if (map.get("get_integral_item_state").equals("1")){
                    viewHolder.ivConsumeIcon.setImageResource(R.mipmap.ic_consume_detail_charge);
                    viewHolder.tvConsumeNumber.setText("+¥"+map.get("get_integral_item_increase"));
                    viewHolder.tv_success.setText("充值成功");
                }else if (map.get("get_integral_item_state").equals("2")){
                    viewHolder.ivConsumeIcon.setImageResource(R.mipmap.ic_consume_detail_vip);
                    viewHolder.tvConsumeNumber.setText("-¥"+map.get("get_integral_item_increase"));
                    viewHolder.tv_success.setText("支付成功");
                }else if (map.get("get_integral_item_state").equals("3")){
//                    viewHolder.ivConsumeIcon.setImageResource(R.mipmap.ic_consume_detail_withdrawals);
                    viewHolder.ivConsumeIcon.setImageResource(R.mipmap.ic_consume_detail_jifen);
                    viewHolder.tvConsumeNumber.setText("-¥"+map.get("get_integral_item_increase"));
                    viewHolder.tv_success.setText("购买成功");
                }else if (map.get("get_integral_item_state").equals("4")){
                    viewHolder.ivConsumeIcon.setImageResource(R.mipmap.ic_consume_detail_video);
                    viewHolder.tvConsumeNumber.setText("+¥"+map.get("get_integral_item_increase"));
                    viewHolder.tv_success.setText("收费成功");
                }else if (map.get("get_integral_item_state").equals("5")){
                    viewHolder.ivConsumeIcon.setImageResource(R.mipmap.ic_consume_detail_video);
                    viewHolder.tvConsumeNumber.setText("-¥"+map.get("get_integral_item_increase"));
                    viewHolder.tv_success.setText("购买成功");
                }/*else {
                    viewHolder.ivConsumeIcon.setImageResource(R.mipmap.img_default);
                    viewHolder.tvConsumeNumber.setText(map.get("get_integral_item_increase")+"");
                }*/
                viewHolder.tvConsumeContent.setText(map.get("get_integral_item_reason")+"");
                viewHolder.tvConsumeTime.setText(map.get("get_integral_item_time")+"");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    class ViewHolder {
        private ImageView ivConsumeIcon;
        private TextView tvConsumeContent;
        private TextView tvConsumeNumber;
        private TextView tvConsumeTime;
        private TextView tv_success;
    }
}
