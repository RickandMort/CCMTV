package com.linlic.ccmtv.yx.activity.conference.databean;

/**
 * Created by yu on 2018/5/9.
 */

public class ConferenceBean {

    private String id;
    private String title;
    private String time;
    private String iconUrl;
    private String collectStatus;        //是否收藏  0为未收藏     1为已收藏

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getCollectStatus() {
        return collectStatus;
    }

    public void setCollectStatus(String collectStatus) {
        this.collectStatus = collectStatus;
    }
}
