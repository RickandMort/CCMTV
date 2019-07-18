package com.linlic.ccmtv.yx.kzbf.bean;

import java.io.Serializable;

/**
 * Created Niklaus yu on 2018/1/11.
 */

public class DbArticle implements Serializable {
    private static final long serialVersionUID = 2072893447591548403L;

    public String name;
    public String url;
    public String size;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
