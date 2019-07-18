package com.linlic.ccmtv.yx.activity.entity;

import java.io.Serializable;

/**
 * Created by tom on 2019/2/21.
 */

public class Files_info  implements Serializable {
    private String url;
    private String url_name;

    @Override
    public String toString() {
        return "Files_info{" +
                "url='" + url + '\'' +
                ", url_name='" + url_name + '\'' +
                '}';
    }

    public Files_info() {
    }

    public Files_info(String url, String url_name) {
        this.url = url;
        this.url_name = url_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl_name() {
        return url_name;
    }

    public void setUrl_name(String url_name) {
        this.url_name = url_name;
    }
}
