package com.linlic.ccmtv.yx.activity.entity;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/11/20.
 */

public class Commodity_Type {

    private String id;
    private String name ;



    public Commodity_Type(JSONObject json){
        try {
            this.id = json.has("id")?json.getString("id"):"";
            this.name = json.has("name")?json.getString("name"):"";
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "Commodity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
