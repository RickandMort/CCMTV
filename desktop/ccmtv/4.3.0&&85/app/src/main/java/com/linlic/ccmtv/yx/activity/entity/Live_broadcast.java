package com.linlic.ccmtv.yx.activity.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/12.
 */

public class Live_broadcast implements Serializable {
    private String title;//分会场标题
    private String time;
    private String hid;//详细会场id
    private String turl; //老师直播链接
    private String surl;//学生直播链接
    private String reviewurl;//回顾链接
    private String botten_text;//按钮名字
    private int tid;
    private String dename;

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getDename() {
        return dename;
    }

    public void setDename(String dename) {
        this.dename = dename;
    }

    public String getBotten_text() {
        return botten_text;
    }

    public void setBotten_text(String botten_text) {
        this.botten_text = botten_text;
    }

    public Live_broadcast() {
        super();
    }

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public String getTurl() {
        return turl;
    }

    public void setTurl(String turl) {
        this.turl = turl;
    }

    public String getSurl() {
        return surl;
    }

    public void setSurl(String surl) {
        this.surl = surl;
    }

    public String getReviewurl() {
        return reviewurl;
    }

    public void setReviewurl(String reviewurl) {
        this.reviewurl = reviewurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
