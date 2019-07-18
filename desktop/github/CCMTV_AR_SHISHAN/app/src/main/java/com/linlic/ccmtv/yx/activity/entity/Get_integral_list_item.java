package com.linlic.ccmtv.yx.activity.entity;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/12/6.
 */

public class Get_integral_list_item {
    private String text1;
    private String text2;
    private String text3;
    private String text4;

    public Get_integral_list_item(JSONObject json){
        try {
            this.text1 = json.has("type")?json.getString("type"):"";
            this.text2 = json.has("money")?json.getString("money"):"";
            this.text3 = json.has("about")?json.getString("about"):"";
            this.text4 = json.has("posttime")?json.getString("posttime").substring(0,10):"";
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public String getText3() {
        return text3;
    }

    public void setText3(String text3) {
        this.text3 = text3;
    }

    public String getText4() {
        return text4;
    }

    public void setText4(String text4) {
        this.text4 = text4;
    }

    @Override
    public String toString() {
        return "Get_integral_list_item{" +
                "text1='" + text1 + '\'' +
                ", text2='" + text2 + '\'' +
                ", text3='" + text3 + '\'' +
                ", text4='" + text4 + '\'' +
                '}';
    }
}
