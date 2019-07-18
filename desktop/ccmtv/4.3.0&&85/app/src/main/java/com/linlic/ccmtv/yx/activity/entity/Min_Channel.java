package com.linlic.ccmtv.yx.activity.entity;

import java.io.Serializable;

/**频道 小频道类
 * Created by Administrator on 2018/5/8.
 */

public class Min_Channel implements Serializable {
    private String id;
    private String title;
    private String icon;
    private String url;
    private String isurl;

    public Min_Channel() {
    }

    @Override
    public String toString() {
        return "Min_Channel{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", icon='" + icon + '\'' +
                ", url='" + url + '\'' +
                ", isurl='" + isurl + '\'' +
                '}';
    }

    public Min_Channel(String id, String title, String icon, String url, String isurl) {
        this.id = id;
        this.title = title;
        this.icon = icon;
        this.url = url;
        this.isurl = isurl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIsurl() {
        return isurl;
    }

    public void setIsurl(String isurl) {
        this.isurl = isurl;
    }

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
