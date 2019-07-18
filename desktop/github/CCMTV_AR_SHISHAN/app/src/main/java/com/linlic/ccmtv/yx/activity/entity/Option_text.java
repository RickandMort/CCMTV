package com.linlic.ccmtv.yx.activity.entity;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/8.
 * 选项
 */
public class Option_text {
    private String option_id="";
    private String option_text="";
    private String option_type="";

    public void  setOption(JSONObject json){
        try {
            this.option_id  = json.has("option_id")?json.getString("option_id"):"";
            this.option_text  = json.has("option_name")?json.getString("option_name"):"" ;
            this.option_type =  json.has("option_type") ? json.getString("option_type"):"0";
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public Option_text(String option_id, String option_type, String option_text){
        this.option_id = option_id;
        this.option_type = option_type;
        this.option_text = option_text;
    }

    public Option_text(){
        super();
    }

    public String getOption_id() {
        return option_id;
    }

    public void setOption_id(String option_id) {
        this.option_id = option_id;
    }

    public String getOption_text() {
        return option_text;
    }

    public void setOption_text(String option_text) {
        this.option_text = option_text;
    }

    public String getOption_type() {
        return option_type;
    }

    public void setOption_type(String option_type) {
        this.option_type = option_type;
    }


    @Override
    public String toString() {
        return "Option{" +
                "option_id='" + option_id + '\'' +
                ", option_text='" + option_text + '\'' +
                ", option_type='" + option_type + '\'' +
                '}';
    }
}
