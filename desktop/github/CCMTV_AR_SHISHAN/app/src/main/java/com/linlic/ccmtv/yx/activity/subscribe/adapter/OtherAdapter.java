package com.linlic.ccmtv.yx.activity.subscribe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.subscribe.entiy.Followks;

import java.util.List;

/**
 * Created by fuweiwei on 2016/1/8.
 */
public class OtherAdapter extends BaseAdapter {

    private Context context;
    List<Followks> followkses;
    private TextView item_text;
    /** 是否可见 在移动动画完毕之前不可见，动画完毕后可见*/
    boolean isVisible = true;
    /** 要删除的position */
    public int remove_position = -1;
    /** 是否是用户频道 */
    private boolean isUser = false;

    public OtherAdapter(Context context, List<Followks> followkses, boolean isUser) {
        this.context = context;
        this.followkses = followkses;
        this.isUser = isUser;
    }

    @Override
    public int getCount() {
        return followkses == null ? 0 : followkses.size();
    }

    @Override
    public Followks getItem(int position) {
        if (followkses != null && followkses.size() != 0) {
            return followkses.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_mygridview_item, null);
        item_text = (TextView) view.findViewById(R.id.text_item);
        Followks channel = getItem(position);
        item_text.setText(channel.getName());
        /*if(isUser){
            if ((position == 0) || (position == 1)){
                item_text.setEnabled(false);
            }
        }*/
        if (!isVisible && (position == -1 + followkses.size())){
            item_text.setText("");
            item_text.setSelected(true);
            item_text.setEnabled(true);
        }
        if(remove_position == position){
            item_text.setText("");
        }
        return view;
    }

    /** 获取频道列表 */
    public List<Followks> getChannnelLst() {
        return followkses;
    }

    /** 添加频道列表 */
    public void addItem(Followks followks) {
        followkses.add(followks);
        notifyDataSetChanged();
    }

    /** 设置删除的position */
    public void setRemove(int position) {
        remove_position = position;
        notifyDataSetChanged();
    }

    /** 删除频道列表 */
    public void remove() {
        try {
            followkses.remove(remove_position);
            remove_position = -1;
            notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    /** 设置频道列表 */
    public void setListDate(List<Followks> list) {
        followkses = list;
    }

    /** 获取是否可见 */
    public boolean isVisible() {
        return isVisible;
    }

    /** 设置是否可见 */
    public void setVisible(boolean visible) {
        isVisible = visible;
    }

}
