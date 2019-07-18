package com.linlic.ccmtv.yx.activity.entity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/20.
 */

public class Commodity_module {
    private String title;
    private List<Commodity> commodities = new ArrayList<>();

    public Commodity_module(JSONObject json){
        try {
            this.title = json.has("title")?json.getString("title"):"";
            for (int j = 0;j < json.getJSONArray("good").length();j++){
                Commodity commodity = new Commodity(json.getJSONArray("good").getJSONObject(j));
                commodities.add(commodity);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Commodity> getCommodities() {
        return commodities;
    }

    public void setCommodities(List<Commodity> commodities) {
        this.commodities = commodities;
    }

    @Override
    public String toString() {
        return "Commodity_module{" +
                "title='" + title + '\'' +
                ", commodities=" + commodities +
                '}';
    }
}
