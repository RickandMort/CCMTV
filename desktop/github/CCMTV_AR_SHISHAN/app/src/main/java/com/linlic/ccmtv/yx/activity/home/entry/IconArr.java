package com.linlic.ccmtv.yx.activity.home.entry;

/**
 * name：
 * author：Larry
 * data：2017/7/7.
 */
public class IconArr {
    /*   "title":"手术演示",
                "imgurl":"http://www.ccmtv.cn/do/ccmtvappandroid/dayicon/gq1.png",
                "isnet":0,
                "type":"1",
                "tzurl":""*/
    private String title;
    private String imgurl;
    private String isnet;
    private String type;
    private String tzurl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getIsnet() {
        return isnet;
    }

    public void setIsnet(String isnet) {
        this.isnet = isnet;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTzurl() {
        return tzurl;
    }

    public void setTzurl(String tzurl) {
        this.tzurl = tzurl;
    }

    @Override
    public String toString() {
        return "IconArr{" +
                "title='" + title + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", isnet='" + isnet + '\'' +
                ", type='" + type + '\'' +
                ", tzurl='" + tzurl + '\'' +
                '}';
    }
}
