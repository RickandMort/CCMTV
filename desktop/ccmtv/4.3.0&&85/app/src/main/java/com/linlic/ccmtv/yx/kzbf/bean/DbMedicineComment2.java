package com.linlic.ccmtv.yx.kzbf.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Niklaus on 2017/12/28.
 */

public class DbMedicineComment2 implements MultiItemEntity {
    public static final int TYPE_DETAIL_COMMENT2 = 1;//2级评论

    private int itemType;//item类型

    public DbMedicineComment2(int itemType) {
        this.itemType = itemType;
    }

    public int getItemType() {
        return itemType;
    }

    public String comment2_id;
    public String comment2_aid;
    public String comment2_uid;
    public String comment2_pid;
    public String comment2_content;
    public String comment2_addtime;
    public String comment2_is_del;
    public String comment2_username;

    public String getComment2_id() {
        return comment2_id;
    }

    public void setComment2_id(String comment2_id) {
        this.comment2_id = comment2_id;
    }

    public String getComment2_aid() {
        return comment2_aid;
    }

    public void setComment2_aid(String comment2_aid) {
        this.comment2_aid = comment2_aid;
    }

    public String getComment2_uid() {
        return comment2_uid;
    }

    public void setComment2_uid(String comment2_uid) {
        this.comment2_uid = comment2_uid;
    }

    public String getComment2_pid() {
        return comment2_pid;
    }

    public void setComment2_pid(String comment2_pid) {
        this.comment2_pid = comment2_pid;
    }

    public String getComment2_content() {
        return comment2_content;
    }

    public void setComment2_content(String comment2_content) {
        this.comment2_content = comment2_content;
    }

    public String getComment2_addtime() {
        return comment2_addtime;
    }

    public void setComment2_addtime(String comment2_addtime) {
        this.comment2_addtime = comment2_addtime;
    }

    public String getComment2_is_del() {
        return comment2_is_del;
    }

    public void setComment2_is_del(String comment2_is_del) {
        this.comment2_is_del = comment2_is_del;
    }

    public String getComment2_username() {
        return comment2_username;
    }

    public void setComment2_username(String comment2_username) {
        this.comment2_username = comment2_username;
    }
}
