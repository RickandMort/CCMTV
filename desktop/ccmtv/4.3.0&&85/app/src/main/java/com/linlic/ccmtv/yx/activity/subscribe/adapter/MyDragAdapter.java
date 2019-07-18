package com.linlic.ccmtv.yx.activity.subscribe.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.subscribe.entiy.Followks;

import java.util.List;

/**
 * Author:Niklaus
 * Date:2017年10月12日
 * Descprition:可以拖动的GridView的适配器
 */
public class MyDragAdapter extends BaseAdapter {
    /** TAG*/
    private final static String TAG = "MyDragAdapter";
    /** 是否显示底部的ITEM */
    private boolean isItemShow = false;
    private Context context;
    /** 控制的postion */
    private int holdPosition;
    /** 是否改变 */
    private boolean isChanged = false;
    /** 列表数据是否改变 */
    private boolean isListChanged = false;
    /** 是否可见 */
    boolean isVisible = true;
    /** 可以拖动的列表（即用户选择的频道列表） */
    public List<Followks> followkses;
    /** TextView 频道内容 */
    private TextView item_text;
    /** 要删除的position */
    public int remove_position = -1;
    /** 是否是用户频道 */
    private boolean isUser = false;

    public MyDragAdapter(Context context, List<Followks> followkses, boolean isUser) {
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
        if (isChanged && (position == holdPosition) && !isItemShow) {
            item_text.setText("");
            item_text.setSelected(true);
            item_text.setEnabled(true);
            isChanged = false;
        }
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
        isListChanged = true;
        notifyDataSetChanged();
    }

    /** 拖动变更频道排序 */
    public void exchange(int dragPostion, int dropPostion) {
        holdPosition = dropPostion;
        Followks dragItem = getItem(dragPostion);
//        Log.d(TAG, "startPostion=" + dragPostion + ";endPosition=" + dropPostion);
        if (dragPostion < dropPostion) {
            followkses.add(dropPostion + 1, dragItem);
            followkses.remove(dragPostion);
        } else {
            followkses.add(dropPostion, dragItem);
            followkses.remove(dragPostion + 1);
        }
        isChanged = true;
        isListChanged = true;
        notifyDataSetChanged();
//        Log.e("followkses",followkses.toString());
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
            isListChanged = true;
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

    /** 排序是否发生改变 */
    public boolean isListChanged() {
        return isListChanged;
    }

    /** 设置是否可见 */
    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    /** 显示放下的ITEM */
    public void setShowDropItem(boolean show) {
        isItemShow = show;
    }

}
