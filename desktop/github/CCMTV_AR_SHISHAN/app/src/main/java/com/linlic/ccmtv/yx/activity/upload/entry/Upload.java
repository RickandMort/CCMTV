package com.linlic.ccmtv.yx.activity.upload.entry;

/**
 * name：上传历史
 * author：Larry
 * data：2017/7/13.
 */
public class Upload {

    /**
     * mvtitle : 测试
     * styletype : medicalrecord
     * mvstatus : 3
     * mvstatusword : 审核失败
     * styletypeword : 病例
     * row_add_time : 2017-03-16
     */

    private String mvtitle;
    private String styletype;
    private String mvstatus;
    private String mvstatusword;
    private String styletypeword;
    private String row_add_time;

    public String getMvtitle() {
        return mvtitle;
    }

    public void setMvtitle(String mvtitle) {
        this.mvtitle = mvtitle;
    }

    public String getStyletype() {
        return styletype;
    }

    public void setStyletype(String styletype) {
        this.styletype = styletype;
    }

    public String getMvstatus() {
        return mvstatus;
    }

    public void setMvstatus(String mvstatus) {
        this.mvstatus = mvstatus;
    }

    public String getMvstatusword() {
        return mvstatusword;
    }

    public void setMvstatusword(String mvstatusword) {
        this.mvstatusword = mvstatusword;
    }

    public String getStyletypeword() {
        return styletypeword;
    }

    public void setStyletypeword(String styletypeword) {
        this.styletypeword = styletypeword;
    }

    public String getRow_add_time() {
        return row_add_time;
    }

    public void setRow_add_time(String row_add_time) {
        this.row_add_time = row_add_time;
    }

    @Override
    public String toString() {
        return "Upload{" +
                "mvtitle='" + mvtitle + '\'' +
                ", styletype='" + styletype + '\'' +
                ", mvstatus='" + mvstatus + '\'' +
                ", mvstatusword='" + mvstatusword + '\'' +
                ", styletypeword='" + styletypeword + '\'' +
                ", row_add_time='" + row_add_time + '\'' +
                '}';
    }
}
