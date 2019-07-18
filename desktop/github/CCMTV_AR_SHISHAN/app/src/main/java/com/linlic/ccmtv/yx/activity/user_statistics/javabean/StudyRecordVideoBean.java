package com.linlic.ccmtv.yx.activity.user_statistics.javabean;

/**
 * Created by yu on 2018/5/18.
 */

public class StudyRecordVideoBean {

    public String aid;
    public String picUrl;
    public String videoTitle;
    public String last_look_time;
    public String money;//观看所需积分
    public String videopaymoney;


    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getLast_look_time() {
        return last_look_time;
    }

    public void setLast_look_time(String last_look_time) {
        this.last_look_time = last_look_time;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getVideopaymoney() {
        return videopaymoney;
    }

    public void setVideopaymoney(String videopaymoney) {
        this.videopaymoney = videopaymoney;
    }
}
