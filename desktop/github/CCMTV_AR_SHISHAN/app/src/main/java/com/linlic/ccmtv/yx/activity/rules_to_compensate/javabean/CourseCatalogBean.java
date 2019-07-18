package com.linlic.ccmtv.yx.activity.rules_to_compensate.javabean;

import com.chad.library.adapter.base.entity.SectionEntity;

import java.util.List;

/**
 * Created by bentley on 2019/6/6.
 */

public class CourseCatalogBean extends SectionEntity<ItemInfo> {

    public CourseCatalogBean(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public CourseCatalogBean(ItemInfo itemInfo) {
        super(itemInfo);
    }
    private String name;
    private List<ItemInfo> itemInfos;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ItemInfo> getItemInfos() {
        return itemInfos;
    }

    public void setItemInfos(List<ItemInfo> itemInfos) {
        this.itemInfos = itemInfos;
    }
}
