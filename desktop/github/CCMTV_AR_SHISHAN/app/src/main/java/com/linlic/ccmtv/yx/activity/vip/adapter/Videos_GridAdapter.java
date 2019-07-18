package com.linlic.ccmtv.yx.activity.vip.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.entity.Home_Video;
import com.linlic.ccmtv.yx.holder.BaseViewHolder;
import com.linlic.ccmtv.yx.utils.FirstLetter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * @Description:gridview的Adapter
 * @author http://blog.csdn.net/finddreams 
 */
public class Videos_GridAdapter extends BaseAdapter {
	private Context mContext;
	public JSONArray jsonArray;
	public Videos_GridAdapter(Context mContext, JSONArray jsonArray) {
		this.mContext = mContext;
		this.jsonArray = jsonArray;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return jsonArray.length();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        TextView _item_text = null;//
        ImageView _item_img = null;//
        LinearLayout _item_layout = null;//
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
					R.layout.vip_grid_item, parent, false);
        }

        _item_text =BaseViewHolder.get(convertView, R.id._item_text);
        _item_img = BaseViewHolder.get(convertView, R.id._item_img);
        _item_layout =  BaseViewHolder.get(convertView, R.id._item_layout);

        try {
            _item_text.setText(jsonArray.getJSONObject(position).getString("kname"));
            com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(FirstLetter.getSpells(jsonArray.getJSONObject(position).getString("icon")), _item_img);
            if(jsonArray.getJSONObject(position).has("type") && jsonArray.getJSONObject(position).getString("type").equals("1")){
                _item_layout.setBackground(mContext.getResources().getDrawable(R.mipmap.privilege_page_icon10));
            }else{
                _item_layout.setBackground(null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
	}




}
