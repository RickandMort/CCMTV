package com.linlic.ccmtv.yx.activity.upload.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yu on 2018/4/18.
 */

public class UploadFeesAdapter extends BaseAdapter{

    private Context context;
//    private String[] feesDatas;
    private List<String> feesDatas2 = new ArrayList<>();
    private int selectedPosition = -1;

    /*public UploadFeesAdapter(Context context, String[] feesDatas) {
        this.context = context;
        this.feesDatas = feesDatas;
    }*/

    public UploadFeesAdapter(Context context, List<String> feesDatas2) {
        this.context = context;
        this.feesDatas2 = feesDatas2;
    }

    @Override
    public int getCount() {
        return feesDatas2.size();
    }

    @Override
    public Object getItem(int position) {
        return feesDatas2.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.item_layout_upload_fees,null);
        TextView tvFees = (TextView) itemView.findViewById(R.id.id_tv_item_upload_fees);

//        tvFees.setText(feesDatas[position]);
        tvFees.setText(feesDatas2.get(position));
        if (position==selectedPosition){
            tvFees.setTextColor(Color.parseColor("#3897F9"));
            tvFees.setBackgroundResource(R.mipmap.bg_upload_fees_unselect);
        }else {
            tvFees.setTextColor(Color.parseColor("#999999"));
            tvFees.setBackgroundResource(R.drawable.anniu2);
        }
        return itemView;
    }

    public void setSelected(int position) {
        this.selectedPosition=position;
        notifyDataSetChanged();
    }

    public int getSelected() {
        return this.selectedPosition;
    }
}
