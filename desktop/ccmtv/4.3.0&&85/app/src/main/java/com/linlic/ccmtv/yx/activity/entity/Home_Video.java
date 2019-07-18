package com.linlic.ccmtv.yx.activity.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/10.
 */

public class Home_Video implements Serializable {
    private String aid;
    private String title;
    private String picurl;
    private String flag;
    private String videopaymoney;
    private String money ;

    @Override
    public String toString() {
        return "Home_Video{" +
                "aid='" + aid + '\'' +
                ", title='" + title + '\'' +
                ", picurl='" + picurl + '\'' +
                ", flag='" + flag + '\'' +
                ", videopaymoney='" + videopaymoney + '\'' +
                ", money='" + money + '\'' +
                '}';
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getVideopaymoney() {
        return videopaymoney;
    }

    public void setVideopaymoney(String videopaymoney) {
        this.videopaymoney = videopaymoney;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public Home_Video() {
    }

    public Home_Video(String aid, String title, String picurl, String flag, String videopaymoney, String money) {
        this.aid = aid;
        this.title = title;
        this.picurl = picurl;
        this.flag = flag;
        this.videopaymoney = videopaymoney;
        this.money = money;
    }
}
