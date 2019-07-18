package com.linlic.ccmtv.yx.kzbf.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Niklaus on 2017/12/20.
 * Describe:热搜
 */

public class DbHotSearch implements MultiItemEntity {
    public static final int TYPE_HOT = 1;
    public static final int TYPE_HISTORY = 2;

    private int itemType;//item类型

    public DbHotSearch(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
