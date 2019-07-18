package com.linlic.ccmtv.yx.activity.entity;

import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.TimeUtil;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/12/4.
 */

public class Exchange_record_entity {

    private String time_text;
    private String time;
    private String integral;
    private String remark;
    private String id;

    public Exchange_record_entity(JSONObject json){
        try {
            this.id = json.has("id")?json.getString("id"):"";
            this.remark = json.has("about")?json.getString("about"):"";
            this.integral = json.has("money")?json.getString("money"):"";
            this.time = json.has("posttime")?json.getString("posttime"):"";
            if(this.time.length()>0){
                String[] str = TimeUtil.getChatTimeStr(Long.parseLong(this.time)).split(" ");
                this.time_text = str[0];
                this.time = str[1];
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public String getTime_text() {
        return time_text;
    }

    public void setTime_text(String time_text) {
        this.time_text = time_text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Exchange_record_entity{" +
                "time_text='" + time_text + '\'' +
                ", time='" + time + '\'' +
                ", integral='" + integral + '\'' +
                ", remark='" + remark + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
