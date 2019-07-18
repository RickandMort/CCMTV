package com.linlic.ccmtv.yx.activity.home.entry;

/**
 * name：
 * author：Larry
 * data：2017/7/7.
 */
public class IndexInfo {

    /*  "aid":"2540",
                "title":"2540",
                "picurl":"http://192.168.30.201/do/ccmtvappandroid/dayicon/csco_app_banner.jpg",
                "urlflg":0*/
    private String aid;
    private String title;
    private String picurl;
    private String urlflg;

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

    public String getUrlflg() {
        return urlflg;
    }

    public void setUrlflg(String urlflg) {
        this.urlflg = urlflg;
    }

    @Override
    public String toString() {
        return "IndexInfo{" +
                "aid='" + aid + '\'' +
                ", title='" + title + '\'' +
                ", picurl='" + picurl + '\'' +
                ", urlflg='" + urlflg + '\'' +
                '}';
    }
}
