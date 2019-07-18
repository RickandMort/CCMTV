package com.linlic.ccmtv.yx.activity.conference.databean;

/**
 * Created by yu on 2018/5/16.
 * 会议二级专题更多视频  ：
 * http://192.168.30.201/upload_files/new_upload_files/api/#!/project/api/detail?projectID=16&groupID=36&apiID=99
 */

public class ConferenceVideoBean {

    private String videoPayMoney;
    private String picUrl;
    private String title;
    private String fid;
    private String aid;
    private String money;
    private String flag;

    public String getVideoPayMoney() {
        return videoPayMoney;
    }

    public void setVideoPayMoney(String videoPayMoney) {
        this.videoPayMoney = videoPayMoney;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
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

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
