package com.linlic.ccmtv.yx.activity.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/9.
 */

public class Icon  implements Serializable {
    private String icon;
    private String title;
    private String type;
    private String webUrl;

    public Icon() {
    }

    @Override
    public String toString() {
        return "Icon{" +
                "icon='" + icon + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", webUrl='" + webUrl + '\'' +
                '}';
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public Icon(String icon, String title, String type, String webUrl) {
        this.icon = icon;
        this.title = title;
        this.type = type;
        this.webUrl = webUrl;
    }
}
