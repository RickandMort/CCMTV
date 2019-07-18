package com.linlic.ccmtv.yx.activity.entity;

import java.io.Serializable;
import java.util.List;

/** 频道 大频道类
 * Created by tom on 2018/5/8.
 */

public class Max_Channel implements Serializable {
    private String id;
    private String title;
    private String icon;
    private boolean select;
    private List<Min_Channel> min_channels;

    public Max_Channel() {
    }

    @Override
    public String toString() {
        return "Max_Channel{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", icon='" + icon + '\'' +
                ", select=" + select +
                ", min_channels=" + min_channels +
                '}';
    }

    public Max_Channel(String id, String title, String icon, boolean select, List<Min_Channel> min_channels) {
        this.id = id;
        this.title = title;
        this.icon = icon;
        this.select = select;
        this.min_channels = min_channels;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public List<Min_Channel> getMin_channels() {
        return min_channels;
    }

    public void setMin_channels(List<Min_Channel> min_channels) {
        this.min_channels = min_channels;
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
