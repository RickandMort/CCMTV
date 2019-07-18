package com.linlic.ccmtv.yx.activity.home.entry;

/**
 * name：
 * author：Larry
 * data：2017/7/7.
 */
public class CCMTVActivity {

   /* "imgurl":"http://www.ccmtv.cn/upload_files/new_upload_files/huxiseason/Picture/app_banner3.jpg",
            "activityurl":"http://www.ccmtv.cn/upload_files/new_upload_files/huxiseason/App_mobile/index.php?type=android",
            "activitytitle":"呼吸时令",
            "urlflg":0*/
    private String imgurl;
    private String activityurl;
    private String activitytitle;
    private String urlflg;

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getActivityurl() {
        return activityurl;
    }

    public void setActivityurl(String activityurl) {
        this.activityurl = activityurl;
    }

    public String getActivitytitle() {
        return activitytitle;
    }

    public void setActivitytitle(String activitytitle) {
        this.activitytitle = activitytitle;
    }

    public String getUrlflg() {
        return urlflg;
    }

    public void setUrlflg(String urlflg) {
        this.urlflg = urlflg;
    }

    @Override
    public String toString() {
        return "CCMTVActivity{" +
                "imgurl='" + imgurl + '\'' +
                ", activityurl='" + activityurl + '\'' +
                ", activitytitle='" + activitytitle + '\'' +
                ", urlflg='" + urlflg + '\'' +
                '}';
    }
}
