package com.linlic.ccmtv.yx.activity.home.entry;

/**
 * name：
 * author：Larry
 * data：2017/7/7.
 */
public class Gg {

    private String img;
    private String url;
    private String title;

    public Gg() {
    }

    public Gg(String img, String url, String title) {
        this.img = img;
        this.url = url;
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Gg{" +
                "img='" + img + '\'' +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

}
