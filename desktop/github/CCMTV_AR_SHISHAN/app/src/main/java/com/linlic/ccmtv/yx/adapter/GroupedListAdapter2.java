package com.linlic.ccmtv.yx.adapter;

import android.content.Context;
import android.graphics.Color;

import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.entity.ChildEntity;
import com.linlic.ccmtv.yx.activity.entity.Examination_paper;
import com.linlic.ccmtv.yx.activity.entity.GroupEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/6/24.
 */

public class GroupedListAdapter2 extends GroupedRecyclerViewAdapter {
    private ArrayList<GroupEntity> mGroups;
    private Examination_paper examination_paper;
    public Map<String,Integer> map;
    public GroupedListAdapter2(Context context, ArrayList<GroupEntity> groups, Examination_paper examination_paper, Map<String,Integer> map) {
        super(context);
       this.mGroups = groups;
       this.examination_paper = examination_paper;
        this.map = map;
    }

    @Override
    public int getGroupCount() {
        return mGroups == null ? 0 : mGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<ChildEntity> children = mGroups.get(groupPosition).getChildren();
        return children == null ? 0 : children.size();
    }

    @Override
    public boolean hasHeader(int groupPosition) {
        return true;
    }

    @Override
    public boolean hasFooter(int groupPosition) {
        return true;
    }

    @Override
    public int getHeaderLayout(int viewType) {
        return R.layout.adapter_header2;
    }

    @Override
    public int getFooterLayout(int viewType) {
        return R.layout.adapter_footer;
    }

    @Override
    public int getChildLayout(int viewType) {
        return R.layout.adapter_child;
    }

    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {
        GroupEntity entity = mGroups.get(groupPosition);
        holder.setText(R.id.tv_header, entity.getHeader());
    }

    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {
        /*GroupEntity entity = mGroups.get(groupPosition);
        holder.setText(R.id.tv_footer, entity.getFooter());*/
    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
        ChildEntity entity = mGroups.get(groupPosition).getChildren().get(childPosition);

        switch (map.get(entity.getChild())){
            case 1:
                holder.setBackgroundRes(R.id.tv_child, R.drawable.anniu14);
                break;
            case 2:
                holder.setBackgroundRes(R.id.tv_child, R.drawable.anniu15);
                break;
            case 3:
                holder.setBackgroundRes(R.id.tv_child, R.drawable.anniu16);
                break;

            default:
                break;
        }
        holder.setTextColor(R.id.tv_child, Color.parseColor("#ffffff"));
        holder.setText(R.id.tv_child, entity.getChild());
    }
}