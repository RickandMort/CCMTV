package com.linlic.ccmtv.yx.activity.home.entry;

/**
 * name：
 * author：Larry
 * data：2017/7/7.
 */
public class ListData {
    private String aid;
    private String fid;
    private String title;
    private String hits;
    private String posttime;
    private String picurl;
    private String money;
    private String flag;
    private String videopaymoney;

    public ListData() {
    }

    public ListData(String aid, String fid, String title, String hits, String posttime, String picurl, String money, String flag, String videopaymoney) {
        this.aid = aid;
        this.fid = fid;
        this.title = title;
        this.hits = hits;
        this.posttime = posttime;
        this.picurl = picurl;
        this.money = money;
        this.flag = flag;
        this.videopaymoney = videopaymoney;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHits() {
        return hits;
    }

    public void setHits(String hits) {
        this.hits = hits;
    }

    public String getPosttime() {
        return posttime;
    }

    public void setPosttime(String posttime) {
        this.posttime = posttime;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
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

    public String getVideopaymoney() {
        return videopaymoney;
    }

    public void setVideopaymoney(String videopaymoney) {
        this.videopaymoney = videopaymoney;
    }

    @Override
    public String toString() {
        return "ListData{" +
                "aid='" + aid + '\'' +
                ", fid='" + fid + '\'' +
                ", title='" + title + '\'' +
                ", hits='" + hits + '\'' +
                ", posttime='" + posttime + '\'' +
                ", picurl='" + picurl + '\'' +
                ", money='" + money + '\'' +
                ", flag='" + flag + '\'' +
                ", videopaymoney='" + videopaymoney + '\'' +
                '}';
    }
}
