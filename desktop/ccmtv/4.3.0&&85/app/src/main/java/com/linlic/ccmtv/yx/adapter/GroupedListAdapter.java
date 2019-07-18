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

/**
 * Created by Administrator on 2019/6/24.
 */

public class GroupedListAdapter extends GroupedRecyclerViewAdapter {
    private ArrayList<GroupEntity> mGroups;
    private Examination_paper examination_paper;
    public GroupedListAdapter(Context context, ArrayList<GroupEntity> groups, Examination_paper examination_paper) {
        super(context);
       this.mGroups = groups;
       this.examination_paper = examination_paper;
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

        switch (examination_paper.getStatus().get(Integer.parseInt(entity.getChild())-1)){
            case 1:
                holder.setTextColor(R.id.tv_child, Color.parseColor("#ffffff"));
                holder.setBackgroundRes(R.id.tv_child, R.drawable.anniu17);
                break;
            case 0:
                holder.setTextColor(R.id.tv_child, Color.parseColor("#3798F9"));
                holder.setBackgroundRes(R.id.tv_child, R.drawable.anniu18);
                break;

            default:
                break;
        }
        holder.setText(R.id.tv_child, entity.getChild());
    }
}