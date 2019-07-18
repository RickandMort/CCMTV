package com.linlic.ccmtv.yx.activity.entity;

import java.io.Serializable;

/**
 * Created by yu on 2018/4/16.
 */

public class UploadModel implements Serializable {

    public String type;// 上传类型  --  视频或病例

    public String name;
    public String url;
    public String iconUrl;
    public long last_look_time;
    public String vtime = "00:00:00";
    public String checkBoxStatus = "0";
    public boolean isChecked = false;
    public String upload_state = "1";
    public int checkNum = 0;
    public String aid;
    public String isVisibility = "1";
    public int picCount;
    public String payType;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public long getLast_look_time() {
        return last_look_time;
    }

    public void setLast_look_time(long last_look_time) {
        this.last_look_time = last_look_time;
    }

    public String getVtime() {
        return vtime;
    }

    public void setVtime(String vtime) {
        this.vtime = vtime;
    }

    public String getCheckBoxStatus() {
        return checkBoxStatus;
    }

    public void setCheckBoxStatus(String checkBoxStatus) {
        this.checkBoxStatus = checkBoxStatus;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getUpload_state() {
        return upload_state;
    }

    public void setUpload_state(String upload_state) {
        this.upload_state = upload_state;
    }

    public int getCheckNum() {
        return checkNum;
    }

    public void setCheckNum(int checkNum) {
        this.checkNum = checkNum;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getIsVisibility() {
        return isVisibility;
    }

    public void setIsVisibility(String isVisibility) {
        this.isVisibility = isVisibility;
    }

    public int getPicCount() {
        return picCount;
    }

    public void setPicCount(int picCount) {
        this.picCount = picCount;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }
}
