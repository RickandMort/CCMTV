package com.linlic.ccmtv.yx.activity.bigcase.entity;

/**
 * Created by bentley on 2018/12/18.
 */

public class BigCaseMonthEntrry {
    /**
     * id : 13
     * title : 阿狸
     */

    private String id;
    private String title;
    private int is_edit;
    private String time;
    private String count;
    private String img;
    private String http_url;

    public String getHttp_url() {
        return http_url;
    }

    public void setHttp_url(String http_url) {
        this.http_url = http_url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getIs_edit() {
        return is_edit;
    }

    public void setIs_edit(int is_edit) {
        this.is_edit = is_edit;
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
}
