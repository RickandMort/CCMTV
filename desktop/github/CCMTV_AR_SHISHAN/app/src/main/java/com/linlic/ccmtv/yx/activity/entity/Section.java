package com.linlic.ccmtv.yx.activity.entity;

import org.json.JSONObject;

/**
 * Created by tom on 2017/12/25.
 * 科室
 */

public class Section {
    private String icon;
    private String text;
    private String type;

    public Section(JSONObject json){
        try {
            this.icon = json.has("icon")?json.getString("icon"):"";
            this.text = json.has("name")?json.getString("name"):"";
            this.type = json.has("id")?json.getString("id"):"";
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public Section(){
        super();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Section{" +
                "icon='" + icon + '\'' +
                ", text='" + text + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
