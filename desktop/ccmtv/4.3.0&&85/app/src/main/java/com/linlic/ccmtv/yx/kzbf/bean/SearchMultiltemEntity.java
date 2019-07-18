package com.linlic.ccmtv.yx.kzbf.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Project: SqDemo01
 * Author:  Niklaus
 * Version:  1.0
 * Date:    2018/1/17
 * Copyright notice:
 */
public class SearchMultiltemEntity implements MultiItemEntity {
    public static final int TYPE_ITEM_ALONE = 1;
    public static final int TYPE_ITEM_BASE = 2;
    public static final int TYPE_ITEM_YAODAI = 3;
    public static final int TYPE_ITEM_HOT = 4;
    public static final int TYPE_ITEM_HISTORY = 5;
    private int itemType;
    private SearchDataBean.DataBean bean;
    private String hotTitle;
    private String hotId;
    private String historyTitle;

    public SearchMultiltemEntity(int itemType) {
        this.itemType = itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public SearchDataBean.DataBean getBean() {
        return bean;
    }

    public void setBean(SearchDataBean.DataBean bean) {
        this.bean = bean;
    }

    public String getHotTitle() {
        return hotTitle;
    }

    public void setHotTitle(String hotTitle) {
        this.hotTitle = hotTitle;
    }

    public String getHistoryTitle() {
        return historyTitle;
    }

    public void setHistoryTitle(String historyTitle) {
        this.historyTitle = historyTitle;
    }

    public String getHotId() {
        return hotId;
    }

    public void setHotId(String hotId) {
        this.hotId = hotId;
    }
}
