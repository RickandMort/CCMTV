package com.linlic.ccmtv.yx.kzbf.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Niklaus on 2017/12/20.
 * Describe:收件箱，发件箱
 */

public class DbMessage implements MultiItemEntity {
    public static final int TYPE_NORMAL = 1;

    private int itemType;//item类型

    public DbMessage(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public String id;
    public String title;
    public String addressor_uid;
    public String addtime;
    public String u_is_look;
    public String helper;
    public String company;
    public String drug;
    public String user_img;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddressor_uid() {
        return addressor_uid;
    }

    public void setAddressor_uid(String addressor_uid) {
        this.addressor_uid = addressor_uid;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getU_is_look() {
        return u_is_look;
    }

    public void setU_is_look(String u_is_look) {
        this.u_is_look = u_is_look;
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
}
