package com.linlic.ccmtv.yx.activity.entity;

/**
 * Created by Administrator on 2019/5/23.
 */

public class Practicing_itme {
    private String fid;
    private String title;
    private String img;

    @Override
    public String toString() {
        return "Practicing_itme{" +
                "fid='" + fid + '\'' +
                ", title='" + title + '\'' +
                ", img='" + img + '\'' +
                '}';
    }

    public Practicing_itme() {
    }

    public Practicing_itme(String fid, String title, String img) {
        this.fid = fid;
        this.title = title;
        this.img = img;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
