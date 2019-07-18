package com.linlic.ccmtv.yx.kzbf.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Niklaus on 2017/12/25.
 * Describe:药讯助手
 */

public class DbMedicineAssistant implements MultiItemEntity {
    public static final int TYPE_FOUSE = 1;
    public static final int TYPE_CONSULT = 2;

    private int itemType;//item类型

    public DbMedicineAssistant(int itemType) {
        this.itemType = itemType;
    }

    public int getItemType() {
        return itemType;
    }

    public String uid;
    public String helper;
    public String company;
    public String drug;
    public String article_title;
    public String user_img;
    public String look_num;
    public String isShowRedDot = "0";

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getHelper() {
        return helper;
    }

    public void setHelper(String helper) {
        this.helper = helper;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDrug() {
        return drug;
    }

    public void setDrug(String drug) {
        this.drug = drug;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public String getLook_num() {
        return look_num;
    }

    public void setLook_num(String look_num) {
        this.look_num = look_num;
    }

    public String getArticle_title() {
        return article_title;
    }

    public void setArticle_title(String article_title) {
        this.article_title = article_title;
    }

    public String getIsShowRedDot() {
        return isShowRedDot;
    }

    public void setIsShowRedDot(String isShowRedDot) {
        this.isShowRedDot = isShowRedDot;
    }
}
