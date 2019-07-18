package com.linlic.ccmtv.yx.activity.upload.entry;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bentley on 2018/7/31.
 */

public class UploadCaseDetail3 implements Serializable {

    private String info;
    private String icon;
    private List<String> urlList;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }
}
