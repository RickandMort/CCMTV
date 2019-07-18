package com.linlic.ccmtv.yx.kzbf.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Niklaus on 2017/12/26.
 * Describe:文章详细
 */

public class DbMedicineDetial implements MultiItemEntity {
    public static final int TYPE_TUIJIAN= 1;//推荐

    private int itemType;//item类型

    public DbMedicineDetial(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    //相关推荐
    public String recommend_id;
    public String recommend_cid;// 1药讯 2指南 3文献
    public String recommend_title;

    public String getRecommend_id() {
        return recommend_id;
    }

    public void setRecommend_id(String recommend_id) {
        this.recommend_id = recommend_id;
    }

    public String getRecommend_title() {
        return recommend_title;
    }

    public void setRecommend_title(String recommend_title) {
        this.recommend_title = recommend_title;
    }

    public String getRecommend_cid() {
        return recommend_cid;
    }

    public void setRecommend_cid(String recommend_cid) {
        this.recommend_cid = recommend_cid;
    }
}
