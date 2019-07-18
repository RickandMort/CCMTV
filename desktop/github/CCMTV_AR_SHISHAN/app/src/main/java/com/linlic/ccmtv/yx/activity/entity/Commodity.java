package com.linlic.ccmtv.yx.activity.entity;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/11/20.
 */

public class Commodity {

    private String id;
    private String name ;
    private String money;
    private String uri;



    public  Commodity(JSONObject json){
        try {
            this.id = json.has("id")?json.getString("id"):"";
            this.name = json.has("name")?json.getString("name"):"";
            this.money = json.has("money")?json.getString("money"):"";
            this.uri = json.has("imgs")?json.getString("imgs"):"";
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

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "Commodity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", money='" + money + '\'' +
                ", uri='" + uri + '\'' +
                '}';
    }
}
