package com.linlic.ccmtv.yx.kzbf.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by Niklaus on 2017/12/28.
 */

public class DbMedicineComment implements MultiItemEntity {
    public static final int TYPE_DETAIL_COMMENT = 1;//详细页评论
    public static final int TYPE_MORE_COMMENT = 2;//更多评论
    public static final int TYPE_TWO_COMMENT = 3;//twocomment
    public static final int TYPE_EXPAND_ALL = 4;//twocomment
    public static final int TYPE_UNDER_LINE = 5;//下划线
    public static final int TYPE_UNEXPAND_ALL = 6;//隐藏更多评论

    private int itemType;//item类型

    private MoreCommentBean.DataBean commentItemBean;
    private MoreCommentBean.DataBean.TwoCommentBean commentItemChileBean;

    public MoreCommentBean.DataBean.TwoCommentBean getCommentItemChileBean() {
        return commentItemChileBean;
    }

    public void setCommentItemChileBean(MoreCommentBean.DataBean.TwoCommentBean commentItemChileBean) {
        this.commentItemChileBean = commentItemChileBean;
    }

    public MoreCommentBean.DataBean getCommentItemBean() {
        return commentItemBean;
    }

    public void setCommentItemBean(MoreCommentBean.DataBean commentItemBean) {
        this.commentItemBean = commentItemBean;
    }

    public DbMedicineComment(int itemType) {
        this.itemType = itemType;
    }

    public int getItemType() {
        return itemType;
    }

    public String comment_id;
    public String comment_aid;
    public String comment_uid;
    public String comment_pid;
    public String comment_content;
    public String comment_addtime;
    public String comment_is_del;
    public String comment_laud_num;
    public String comment_username;
    public String comment_user_img;
    public String comment_elite;
    public String comment_stick;
    public String comment_is_look;
    public String comment_is_laud;

    public List comment2_list;

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment_aid() {
        return comment_aid;
    }

    public void setComment_aid(String comment_aid) {
        this.comment_aid = comment_aid;
    }

    public String getComment_uid() {
        return comment_uid;
    }

    public void setComment_uid(String comment_uid) {
        this.comment_uid = comment_uid;
    }

    public String getComment_pid() {
        return comment_pid;
    }

    public void setComment_pid(String comment_pid) {
        this.comment_pid = comment_pid;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public String getComment_addtime() {
        return comment_addtime;
    }

    public void setComment_addtime(String comment_addtime) {
        this.comment_addtime = comment_addtime;
    }

    public String getComment_is_del() {
        return comment_is_del;
    }

    public void setComment_is_del(String comment_is_del) {
        this.comment_is_del = comment_is_del;
    }

    public String getComment_laud_num() {
        return comment_laud_num;
    }

    public void setComment_laud_num(String comment_laud_num) {
        this.comment_laud_num = comment_laud_num;
    }

    public String getComment_username() {
        return comment_username;
    }

    public void setComment_username(String comment_username) {
        this.comment_username = comment_username;
    }

    public String getComment_user_img() {
        return comment_user_img;
    }

    public void setComment_user_img(String comment_user_img) {
        this.comment_user_img = comment_user_img;
    }

    public String getComment_elite() {
        return comment_elite;
    }

    public void setComment_elite(String comment_elite) {
        this.comment_elite = comment_elite;
    }

    public String getComment_stick() {
        return comment_stick;
    }

    public void setComment_stick(String comment_stick) {
        this.comment_stick = comment_stick;
    }

    public String getComment_is_look() {
        return comment_is_look;
    }

    public void setComment_is_look(String comment_is_look) {
        this.comment_is_look = comment_is_look;
    }

    public String getComment_is_laud() {
        return comment_is_laud;
    }

    public void setComment_is_laud(String comment_is_laud) {
        this.comment_is_laud = comment_is_laud;
    }

    public List getComment2_list() {
        return comment2_list;
    }

    public void setComment2_list(List comment2_list) {
        this.comment2_list = comment2_list;
    }
}
