package com.linlic.ccmtv.yx.activity.entity;

/**
 * name：医学直播
 * author：Larry
 * data：2017/2/14.
 */
public class Live {


    /**
     * id : 55
     * livename : 123456
     * liveurl : http://ccmtvbj.gensee.com/training/site/s/85064221?nickname=ccmtv1481275643
     * livestrattime : 开始时间：2016-12-07 09:59:41
     * livecontact : 主讲人：ccmtvcs
     * imgurl : http://www.ccmtv.cn//upload_files/new_upload_files/liveimg/55.jpg
     */

    private String id;
    private String livename;
    private String liveurl;
    private String livestrattime;
    private String livecontact;
    private String imgurl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLivename() {
        return livename;
    }

    public void setLivename(String livename) {
        this.livename = livename;
    }

    public String getLiveurl() {
        return liveurl;
    }

    public void setLiveurl(String liveurl) {
        this.liveurl = liveurl;
    }

    public String getLivestrattime() {
        return livestrattime;
    }

    public void setLivestrattime(String livestrattime) {
        this.livestrattime = livestrattime;
    }

    public String getLivecontact() {
        return livecontact;
    }

    public void setLivecontact(String livecontact) {
        this.livecontact = livecontact;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    @Override
    public String toString() {
        return "Live{" +
                "id='" + id + '\'' +
                ", livename='" + livename + '\'' +
                ", liveurl='" + liveurl + '\'' +
                ", livestrattime='" + livestrattime + '\'' +
                ", livecontact='" + livecontact + '\'' +
                ", imgurl='" + imgurl + '\'' +
                '}';
    }
}
