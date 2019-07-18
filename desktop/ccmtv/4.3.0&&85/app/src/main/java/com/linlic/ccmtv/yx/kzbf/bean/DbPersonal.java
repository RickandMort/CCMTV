package com.linlic.ccmtv.yx.kzbf.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Niklaus on 2018/1/8.
 * 个人主页
 */

public class DbPersonal implements MultiItemEntity {
    public static final int TYPE_PERSONAL = 1;

    private int itemType;//item类型

    public DbPersonal(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public String id;
    public String uid;
    public String title;
    public String posttime;
    public String img_url;
    public String laud_num;
    public String is_laud;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosttime() {
        return posttime;
    }

    public void setPosttime(String posttime) {
        this.posttime = posttime;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getLaud_num() {
        return laud_num;
    }

    public void setLaud_num(String laud_num) {
        this.laud_num = laud_num;
    }

    public String getIs_laud() {
        return is_laud;
    }

    public void setIs_laud(String is_laud) {
        this.is_laud = is_laud;
    }
}
