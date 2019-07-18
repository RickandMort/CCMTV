package com.linlic.ccmtv.yx.activity.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/3/12.
 */

public class Direct_broadcast   implements Serializable {
    private String direct_broadcast_item_type;
    private String direct_broadcast_item_title;
    private String direct_broadcast_item_icon;
    private String direct_broadcast_item_add;
    private String direct_broadcast_item_banner;
    private List<Live_broadcast> live_broadcasts;
    private int position;
    private String starttime;
    private String endtime;
    private String about;
    private String address;
    public Direct_broadcast(){
        super();
    }

    @Override
    public String toString() {
        return "Direct_broadcast{" +
                "direct_broadcast_item_type='" + direct_broadcast_item_type + '\'' +
                ", direct_broadcast_item_title='" + direct_broadcast_item_title + '\'' +
                ", direct_broadcast_item_icon='" + direct_broadcast_item_icon + '\'' +
                ", direct_broadcast_item_add='" + direct_broadcast_item_add + '\'' +
                ", direct_broadcast_item_banner='" + direct_broadcast_item_banner + '\'' +
                ", live_broadcasts=" + live_broadcasts +
                ", position=" + position +
                '}';
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getDirect_broadcast_item_type() {
        return direct_broadcast_item_type;
    }

    public void setDirect_broadcast_item_type(String direct_broadcast_item_type) {
        this.direct_broadcast_item_type = direct_broadcast_item_type;
    }

    public String getDirect_broadcast_item_title() {
        return direct_broadcast_item_title;
    }

    public void setDirect_broadcast_item_title(String direct_broadcast_item_title) {
        this.direct_broadcast_item_title = direct_broadcast_item_title;
    }

    public String getDirect_broadcast_item_icon() {
        return direct_broadcast_item_icon;
    }

    public void setDirect_broadcast_item_icon(String direct_broadcast_item_icon) {
        this.direct_broadcast_item_icon = direct_broadcast_item_icon;
    }

    public String getDirect_broadcast_item_add() {
        return direct_broadcast_item_add;
    }

    public void setDirect_broadcast_item_add(String direct_broadcast_item_add) {
        this.direct_broadcast_item_add = direct_broadcast_item_add;
    }

    public String getDirect_broadcast_item_banner() {
        return direct_broadcast_item_banner;
    }

    public void setDirect_broadcast_item_banner(String direct_broadcast_item_banner) {
        this.direct_broadcast_item_banner = direct_broadcast_item_banner;
    }

    public List<Live_broadcast> getLive_broadcasts() {
        return live_broadcasts;
    }

    public void setLive_broadcasts(List<Live_broadcast> live_broadcasts) {
        this.live_broadcasts = live_broadcasts;
    }
}
