package com.linlic.ccmtv.yx.activity.entity;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/22.
 */
public class Special {

    private String title_max;
    private String title_max_id;
    private List<Map<String ,Object>> specials;

    @Override
    public String toString() {
        return "Special{" +
                "title_max='" + title_max + '\'' +
                ", title_max_id='" + title_max_id + '\'' +
                ", specials=" + specials +
                '}';
    }

    public String getTitle_max() {
        return title_max;
    }

    public void setTitle_max(String title_max) {
        this.title_max = title_max;
    }

    public String getTitle_max_id() {
        return title_max_id;
    }

    public void setTitle_max_id(String title_max_id) {
        this.title_max_id = title_max_id;
    }

    public List<Map<String, Object>> getSpecials() {
        return specials;
    }

    public void setSpecials(List<Map<String, Object>> specials) {
        this.specials = specials;
    }
}
